#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import re
import sys
import urllib.error
import urllib.parse
import urllib.request
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable


ROOT = Path(__file__).resolve().parent
MODULES = ("common", "fabric", "neoforge")
TEXT_SUFFIXES = {
    ".java",
    ".kt",
    ".kts",
    ".gradle",
    ".properties",
    ".json",
    ".toml",
    ".cfg",
    ".mcmeta",
}

PRIMITIVE_DESCRIPTORS = {
    "byte": "B",
    "char": "C",
    "double": "D",
    "float": "F",
    "int": "I",
    "long": "J",
    "short": "S",
    "boolean": "Z",
    "void": "V",
}

JAVA_LANG_COMMON = {
    "Object",
    "String",
    "Integer",
    "Long",
    "Short",
    "Byte",
    "Boolean",
    "Double",
    "Float",
    "Character",
    "Number",
    "Class",
    "Enum",
    "Throwable",
    "RuntimeException",
    "Exception",
}


@dataclass
class ProjectState:
    package: str
    mod_id: str
    mod_name: str
    common_main: str
    fabric_main: str
    neoforge_main: str


class VanillaSourceResolver:
    def __init__(self, jar_path: Path) -> None:
        self.jar_path = jar_path
        self._zip = zipfile.ZipFile(jar_path, "r")
        self._source_cache: dict[str, str] = {}
        self._imports_cache: dict[str, tuple[str, dict[str, str], list[str]]] = {}
        self._field_cache: dict[tuple[str, str], tuple[str | None, str | None]] = {}

    def close(self) -> None:
        self._zip.close()

    def infer_field_descriptor(self, owner_dot: str, field_name: str) -> tuple[str | None, str | None]:
        cache_key = (owner_dot, field_name)
        if cache_key in self._field_cache:
            return self._field_cache[cache_key]

        top_owner = owner_dot.split("$", 1)[0]
        source = self._get_source_for_top(top_owner)
        if source is None:
            result = (None, "vanilla-source-missing")
            self._field_cache[cache_key] = result
            return result

        package_name, imports, wildcard_imports = self._get_import_context(top_owner, source)
        top_simple = top_owner.rsplit(".", 1)[-1]
        inner_parts = owner_dot.split("$")[1:]
        chain = [top_simple] + [part for part in inner_parts if part]

        current_scope = self._strip_comments(source)
        type_vars: dict[str, str] = {}
        for simple_name in chain:
            class_decl = self._extract_class_block(current_scope, simple_name)
            if class_decl is None:
                result = (None, "class-not-found")
                self._field_cache[cache_key] = result
                return result
            header, body = class_decl
            type_vars.update(self._parse_type_var_bounds(header))
            current_scope = body

        field_type = self._find_field_type(current_scope, field_name)
        if field_type is None:
            result = (None, "field-not-found")
            self._field_cache[cache_key] = result
            return result

        descriptor = self._type_to_descriptor(
            type_expr=field_type,
            package_name=package_name,
            imports=imports,
            wildcard_imports=wildcard_imports,
            type_vars=type_vars,
        )
        if descriptor is None:
            result = (None, "descriptor-resolve-failed")
            self._field_cache[cache_key] = result
            return result

        result = (descriptor, None)
        self._field_cache[cache_key] = result
        return result

    def _get_source_for_top(self, top_owner: str) -> str | None:
        if top_owner in self._source_cache:
            cached = self._source_cache[top_owner]
            return cached if cached else None

        entry_name = f"{top_owner.replace('.', '/')}.java"
        try:
            with self._zip.open(entry_name, "r") as src:
                text = src.read().decode("utf-8")
                self._source_cache[top_owner] = text
                return text
        except KeyError:
            self._source_cache[top_owner] = ""
            return None

    def _get_import_context(self, top_owner: str, source: str) -> tuple[str, dict[str, str], list[str]]:
        if top_owner in self._imports_cache:
            return self._imports_cache[top_owner]

        package_name = ""
        imports: dict[str, str] = {}
        wildcard_imports: list[str] = []

        for line in source.splitlines():
            package_match = re.match(r"\s*package\s+([A-Za-z0-9_.]+)\s*;", line)
            if package_match:
                package_name = package_match.group(1)
                continue

            import_match = re.match(r"\s*import\s+(static\s+)?([A-Za-z0-9_.*$]+)\s*;", line)
            if not import_match:
                continue
            if import_match.group(1):
                continue

            fqcn = import_match.group(2)
            if fqcn.endswith(".*"):
                wildcard_imports.append(fqcn[:-2])
            else:
                imports[fqcn.rsplit(".", 1)[-1]] = fqcn

        context = (package_name, imports, wildcard_imports)
        self._imports_cache[top_owner] = context
        return context

    @staticmethod
    def _strip_comments(text: str) -> str:
        no_block = re.sub(r"/\*.*?\*/", "", text, flags=re.DOTALL)
        return re.sub(r"//.*", "", no_block)

    def _extract_class_block(self, text: str, class_name: str) -> tuple[str, str] | None:
        pattern = re.compile(rf"\b(?:class|interface|enum|record)\s+{re.escape(class_name)}\b")
        match = pattern.search(text)
        if not match:
            return None

        brace_start = text.find("{", match.end())
        if brace_start < 0:
            return None

        depth = 1
        i = brace_start + 1
        while i < len(text):
            ch = text[i]
            if ch == "{":
                depth += 1
            elif ch == "}":
                depth -= 1
                if depth == 0:
                    header = text[match.start() : brace_start]
                    body = text[brace_start + 1 : i]
                    return header, body
            i += 1

        return None

    def _parse_type_var_bounds(self, header: str) -> dict[str, str]:
        lt = header.find("<")
        if lt < 0:
            return {}

        depth = 0
        end = -1
        for i in range(lt, len(header)):
            ch = header[i]
            if ch == "<":
                depth += 1
            elif ch == ">":
                depth -= 1
                if depth == 0:
                    end = i
                    break
        if end < 0:
            return {}

        content = header[lt + 1 : end]
        bounds: dict[str, str] = {}
        for part in self._split_top_level(content, ","):
            token = part.strip()
            if not token:
                continue
            if " extends " in token:
                var_name, bound_expr = token.split(" extends ", 1)
                first_bound = bound_expr.split("&", 1)[0].strip()
                bounds[var_name.strip()] = first_bound
            else:
                bounds[token.strip()] = "java.lang.Object"
        return bounds

    def _find_field_type(self, class_body: str, field_name: str) -> str | None:
        depth = 0
        statement_chars: list[str] = []

        for ch in class_body:
            if ch == "{":
                if depth == 0:
                    statement_chars.clear()
                depth += 1
                continue
            if ch == "}":
                depth = max(0, depth - 1)
                continue

            if depth == 0:
                statement_chars.append(ch)
                if ch == ";":
                    statement = "".join(statement_chars).strip()
                    statement_chars.clear()
                    field_type = self._parse_field_statement(statement, field_name)
                    if field_type is not None:
                        return field_type

        return None

    def _parse_field_statement(self, statement: str, field_name: str) -> str | None:
        if not statement:
            return None

        text = statement.rstrip(";").strip()
        if not text:
            return None

        declaration_head = text.split("=", 1)[0]
        if "(" in declaration_head:
            return None

        declaration = self._strip_leading_annotations(text)
        declaration = self._strip_leading_modifiers(declaration)
        if not declaration:
            return None

        split_result = self._split_type_and_vars(declaration)
        if split_result is None:
            return None

        type_part, vars_part = split_result
        for var_expr in self._split_top_level(vars_part, ","):
            raw_var = var_expr.split("=", 1)[0].strip()
            if not raw_var:
                continue

            array_dims = 0
            while raw_var.endswith("[]"):
                raw_var = raw_var[:-2].strip()
                array_dims += 1

            name_match = re.search(r"([A-Za-z_][A-Za-z0-9_]*)$", raw_var)
            if not name_match:
                continue
            if name_match.group(1) != field_name:
                continue

            return f"{type_part}{'[]' * array_dims}"

        return None

    @staticmethod
    def _strip_leading_annotations(text: str) -> str:
        value = text.strip()
        pattern = re.compile(r"^@[A-Za-z0-9_.$]+(?:\([^)]*\))?\s*")
        while True:
            match = pattern.match(value)
            if not match:
                return value
            value = value[match.end() :].lstrip()

    @staticmethod
    def _strip_leading_modifiers(text: str) -> str:
        modifiers = {
            "public",
            "protected",
            "private",
            "static",
            "final",
            "transient",
            "volatile",
            "strictfp",
            "native",
            "synchronized",
            "abstract",
        }
        value = text.strip()
        while True:
            match = re.match(r"^([A-Za-z_][A-Za-z0-9_]*)\b", value)
            if not match:
                return value
            token = match.group(1)
            if token not in modifiers:
                return value
            value = value[match.end() :].lstrip()

    def _split_type_and_vars(self, declaration: str) -> tuple[str, str] | None:
        depth = 0
        for i, ch in enumerate(declaration):
            if ch == "<":
                depth += 1
            elif ch == ">":
                depth = max(0, depth - 1)
            elif ch.isspace() and depth == 0:
                type_part = declaration[:i].strip()
                vars_part = declaration[i:].strip()
                if type_part and vars_part:
                    return type_part, vars_part
        return None

    def _type_to_descriptor(
        self,
        type_expr: str,
        package_name: str,
        imports: dict[str, str],
        wildcard_imports: list[str],
        type_vars: dict[str, str],
        _guard: int = 0,
    ) -> str | None:
        if _guard > 8:
            return None

        value = self._remove_inline_annotations(type_expr).strip()
        if not value:
            return None

        array_dims = 0
        while value.endswith("[]"):
            value = value[:-2].strip()
            array_dims += 1

        value = value.replace("...", "[]")
        while value.endswith("[]"):
            value = value[:-2].strip()
            array_dims += 1

        if value.startswith("? extends "):
            value = value[len("? extends ") :].strip()
        elif value.startswith("? super "):
            value = value[len("? super ") :].strip()
        elif value == "?":
            value = "java.lang.Object"

        value = self._strip_generic_arguments(value)
        value = value.split("&", 1)[0].strip()

        if value in type_vars:
            bound = type_vars[value]
            return self._type_to_descriptor(bound, package_name, imports, wildcard_imports, type_vars, _guard + 1)

        if value in PRIMITIVE_DESCRIPTORS:
            return ("[" * array_dims) + PRIMITIVE_DESCRIPTORS[value]

        fqcn = self._resolve_reference_type(value, package_name, imports, wildcard_imports, type_vars)
        if fqcn is None:
            return None

        return ("[" * array_dims) + "L" + fqcn.replace(".", "/") + ";"

    @staticmethod
    def _remove_inline_annotations(type_expr: str) -> str:
        return re.sub(r"@[A-Za-z0-9_.$]+(?:\([^)]*\))?", "", type_expr)

    @staticmethod
    def _strip_generic_arguments(type_expr: str) -> str:
        out: list[str] = []
        depth = 0
        for ch in type_expr:
            if ch == "<":
                depth += 1
                continue
            if ch == ">":
                depth = max(0, depth - 1)
                continue
            if depth == 0:
                out.append(ch)
        return "".join(out).strip()

    def _resolve_reference_type(
        self,
        type_name: str,
        package_name: str,
        imports: dict[str, str],
        wildcard_imports: list[str],
        type_vars: dict[str, str],
    ) -> str | None:
        value = type_name.strip()
        if not value:
            return None

        if value in type_vars:
            return type_vars[value]

        if value.startswith(("net.", "com.", "org.", "io.", "java.")):
            return value

        if "." in value:
            first, rest = value.split(".", 1)
            if first in imports:
                return imports[first] + "$" + rest.replace(".", "$")
            if first and first[0].isupper():
                base = self._resolve_simple_type(first, package_name, imports, wildcard_imports)
                if base is None:
                    return None
                return base + "$" + rest.replace(".", "$")
            return value

        return self._resolve_simple_type(value, package_name, imports, wildcard_imports)

    @staticmethod
    def _resolve_simple_type(
        simple_name: str,
        package_name: str,
        imports: dict[str, str],
        wildcard_imports: list[str],
    ) -> str | None:
        if simple_name in imports:
            return imports[simple_name]

        for wildcard in wildcard_imports:
            if wildcard.startswith(("net.minecraft", "java.", "com.mojang")):
                return f"{wildcard}.{simple_name}"

        if simple_name in JAVA_LANG_COMMON:
            return f"java.lang.{simple_name}"

        if package_name:
            return f"{package_name}.{simple_name}"

        return None

    @staticmethod
    def _split_top_level(text: str, separator: str) -> list[str]:
        parts: list[str] = []
        buf: list[str] = []
        depth_angle = 0
        depth_paren = 0

        for ch in text:
            if ch == "<":
                depth_angle += 1
            elif ch == ">":
                depth_angle = max(0, depth_angle - 1)
            elif ch == "(":
                depth_paren += 1
            elif ch == ")":
                depth_paren = max(0, depth_paren - 1)

            if ch == separator and depth_angle == 0 and depth_paren == 0:
                parts.append("".join(buf))
                buf.clear()
                continue

            buf.append(ch)

        if buf:
            parts.append("".join(buf))
        return parts


def read_properties(path: Path) -> dict[str, str]:
    props: dict[str, str] = {}
    if not path.exists():
        raise FileNotFoundError(f"gradle properties file not found: {path}")

    for raw in path.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        if "=" not in line:
            continue
        key, value = line.split("=", 1)
        props[key.strip()] = value.strip()
    return props


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def write_text(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")


def move_tree_merge(src: Path, dst: Path) -> None:
    if not src.exists():
        return
    if src.is_file():
        dst.parent.mkdir(parents=True, exist_ok=True)
        if dst.exists():
            dst.unlink()
        src.replace(dst)
        return

    dst.mkdir(parents=True, exist_ok=True)
    for child in src.iterdir():
        move_tree_merge(child, dst / child.name)
    if not any(src.iterdir()):
        src.rmdir()


def clean_empty_dirs(path: Path, stop: Path) -> None:
    current = path
    while current != stop and current.exists():
        try:
            next(current.iterdir())
            break
        except StopIteration:
            parent = current.parent
            current.rmdir()
            current = parent


def ensure_meta_inf() -> None:
    resources = ROOT / "common" / "src" / "main" / "resources"
    meat = resources / "MEAT-INF"
    meta = resources / "META-INF"
    if meat.exists() and not meta.exists():
        meat.replace(meta)


def detect_state(default_mod_id: str, default_mod_name: str, default_common_main: str) -> ProjectState:
    package_name = ""
    fabric_main = ""

    fabric_mod_json = ROOT / "fabric" / "src" / "main" / "resources" / "fabric.mod.json"
    if fabric_mod_json.exists():
        content = read_text(fabric_mod_json)
        match = re.search(r'"main"\s*:\s*\[\s*"([A-Za-z0-9_$.]+)"', content)
        if match:
            fqcn = match.group(1)
            if "." in fqcn:
                package_name, fabric_main = fqcn.rsplit(".", 1)

    if not package_name:
        package_name = _detect_any_package() or "com.example.examplemod"

    common_main, mod_id, mod_name = _detect_common_main_and_constants(
        package_name=package_name,
        default_mod_id=default_mod_id,
        default_mod_name=default_mod_name,
        default_common_main=default_common_main,
    )

    neoforge_main = _detect_neoforge_main(package_name) or f"{common_main}NeoForge"
    if not fabric_main:
        fabric_main = f"{common_main}Fabric"

    return ProjectState(
        package=package_name,
        mod_id=mod_id,
        mod_name=mod_name,
        common_main=common_main,
        fabric_main=fabric_main,
        neoforge_main=neoforge_main,
    )


def _detect_any_package() -> str | None:
    for module in MODULES:
        java_root = ROOT / module / "src" / "main" / "java"
        if not java_root.exists():
            continue
        for file in java_root.rglob("*.java"):
            text = read_text(file)
            pkg_match = re.search(r"^\s*package\s+([A-Za-z0-9_.]+)\s*;", text, re.MULTILINE)
            if pkg_match:
                return pkg_match.group(1)
    return None


def _detect_common_main_and_constants(
    package_name: str,
    default_mod_id: str,
    default_mod_name: str,
    default_common_main: str,
) -> tuple[str, str, str]:
    pkg_dir = ROOT / "common" / "src" / "main" / "java" / Path(*package_name.split("."))
    if not pkg_dir.exists():
        return default_common_main, default_mod_id, default_mod_name

    candidates = sorted(pkg_dir.glob("*.java"))
    main_class = default_common_main
    mod_id = default_mod_id
    mod_name = default_mod_name

    for file in candidates:
        text = read_text(file)
        if re.search(r"\bMOD_ID\b", text):
            main_class = file.stem
            mod_match = re.search(r'MOD_ID\s*=\s*"([^"]+)"', text)
            if mod_match:
                mod_id = mod_match.group(1)
            name_match = re.search(r'MOD_NAME\s*=\s*"([^"]+)"', text)
            if name_match:
                mod_name = name_match.group(1)
            return main_class, mod_id, mod_name

    if candidates:
        main_class = candidates[0].stem

    return main_class, mod_id, mod_name


def _detect_neoforge_main(package_name: str) -> str | None:
    pkg_dir = ROOT / "neoforge" / "src" / "main" / "java" / Path(*package_name.split("."))
    if not pkg_dir.exists():
        return None

    for file in sorted(pkg_dir.glob("*.java")):
        text = read_text(file)
        if "@Mod(" in text:
            class_match = re.search(
                r"^\s*(?:public\s+)?(?:final\s+|abstract\s+)?class\s+([A-Za-z0-9_]+)\b",
                text,
                re.MULTILINE,
            )
            if class_match:
                return class_match.group(1)
    return None


def rename_package_dirs(old_package: str, new_package: str) -> None:
    if old_package == new_package:
        return

    old_rel = Path(*old_package.split("."))
    new_rel = Path(*new_package.split("."))

    for module in MODULES:
        base = ROOT / module / "src" / "main" / "java"
        old_dir = base / old_rel
        new_dir = base / new_rel
        if old_dir.exists():
            move_tree_merge(old_dir, new_dir)
            clean_empty_dirs(old_dir.parent, base)


def rename_main_class_file(module: str, package_name: str, old_name: str, new_name: str) -> None:
    if old_name == new_name:
        return

    base = ROOT / module / "src" / "main" / "java" / Path(*package_name.split("."))
    if not base.exists():
        return

    source = base / f"{old_name}.java"
    target = base / f"{new_name}.java"
    if source.exists():
        if target.exists():
            target.unlink()
        source.replace(target)


def rename_modid_files(old_mod_id: str, new_mod_id: str) -> None:
    if old_mod_id == new_mod_id:
        return

    for module in MODULES:
        resources_root = ROOT / module / "src" / "main" / "resources"
        if not resources_root.exists():
            continue

        for file in sorted(resources_root.rglob(f"{old_mod_id}*"), reverse=True):
            if not file.is_file():
                continue
            new_name = new_mod_id + file.name[len(old_mod_id) :]
            target = file.with_name(new_name)
            if target.exists():
                target.unlink()
            file.replace(target)


def rename_service_filename(old_package: str, new_package: str) -> None:
    if old_package == new_package:
        return

    for module in ("fabric", "neoforge"):
        services_dir = ROOT / module / "src" / "main" / "resources" / "META-INF" / "services"
        if not services_dir.exists():
            continue

        old_name = f"{old_package}.platform.services.IPlatformHelper"
        new_name = f"{new_package}.platform.services.IPlatformHelper"
        src = services_dir / old_name
        dst = services_dir / new_name
        if src.exists():
            if dst.exists():
                dst.unlink()
            src.replace(dst)


def iter_text_files() -> Iterable[Path]:
    for module in MODULES:
        module_root = ROOT / module
        if not module_root.exists():
            continue

        for file in module_root.rglob("*"):
            if not file.is_file():
                continue
            if any(part in {"build", ".gradle"} for part in file.parts):
                continue
            if file.suffix in TEXT_SUFFIXES or file.parent.name == "services":
                yield file


def update_main_constants(package_name: str, main_class: str, mod_id: str, mod_name: str) -> None:
    main_path = ROOT / "common" / "src" / "main" / "java" / Path(*package_name.split(".")) / f"{main_class}.java"
    if not main_path.exists():
        return

    content = read_text(main_path)
    content = re.sub(r'MOD_ID\s*=\s*"[^"]*"', f'MOD_ID = "{mod_id}"', content)
    content = re.sub(r'MOD_NAME\s*=\s*"[^"]*"', f'MOD_NAME = "{mod_name}"', content)
    write_text(main_path, content)


def rewrite_contents(
    old_state: ProjectState,
    new_package: str,
    new_mod_id: str,
    new_common_main: str,
    new_fabric_main: str,
    new_neoforge_main: str,
) -> None:
    old_pkg_slash = old_state.package.replace(".", "/")
    new_pkg_slash = new_package.replace(".", "/")

    replacements = {
        old_state.package: new_package,
        old_pkg_slash: new_pkg_slash,
        old_state.mod_id: new_mod_id,
        f"{old_state.package}.{old_state.common_main}": f"{new_package}.{new_common_main}",
        f"{old_state.package}.{old_state.fabric_main}": f"{new_package}.{new_fabric_main}",
        f"{old_state.package}.{old_state.neoforge_main}": f"{new_package}.{new_neoforge_main}",
    }

    simple_name_replacements = [
        (old_state.common_main, new_common_main),
        (old_state.fabric_main, new_fabric_main),
        (old_state.neoforge_main, new_neoforge_main),
    ]

    legacy_constants = ROOT / "common" / "src" / "main" / "java" / Path(*new_package.split(".")) / "Constants.java"
    if not legacy_constants.exists() and new_common_main != "Constants":
        replacements[f"{new_package}.Constants"] = f"{new_package}.{new_common_main}"
        simple_name_replacements.append(("Constants", new_common_main))

    sorted_replacements = sorted(
        [(k, v) for k, v in replacements.items() if k and v and k != v],
        key=lambda item: len(item[0]),
        reverse=True,
    )

    simple_patterns = [
        (re.compile(rf"\b{re.escape(old)}\b"), new)
        for old, new in sorted(simple_name_replacements, key=lambda item: len(item[0]), reverse=True)
        if old and new and old != new
    ]

    for file in iter_text_files():
        original = read_text(file)
        updated = original

        for old, new in sorted_replacements:
            updated = updated.replace(old, new)

        if file.suffix == ".java":
            for pattern, new in simple_patterns:
                updated = pattern.sub(new, updated)

        if updated != original:
            write_text(file, updated)


def fetch_standard_license_text(license_name: str) -> str | None:
    if not license_name:
        return None

    candidates = [license_name, license_name.lower()]
    headers = {
        "Accept": "application/vnd.github+json",
        "User-Agent": "multiloader-template-sync-script",
    }

    for key in candidates:
        url = f"https://api.github.com/licenses/{urllib.parse.quote(key)}"
        req = urllib.request.Request(url, headers=headers)
        try:
            with urllib.request.urlopen(req, timeout=20) as response:
                payload = json.loads(response.read().decode("utf-8"))
                body = payload.get("body", "")
                if body.strip():
                    return body.rstrip() + "\n"
        except urllib.error.HTTPError as exc:
            if exc.code == 404:
                continue
        except (urllib.error.URLError, TimeoutError):
            return None

    return None


def update_license_file(license_name: str) -> None:
    text = fetch_standard_license_text(license_name)
    if text is None:
        print(f"[sync] Skip LICENSE sync: '{license_name}' is not a recognized GitHub standard license key.")
        return

    write_text(ROOT / "LICENSE", text)
    print(f"[sync] LICENSE updated from GitHub API using key '{license_name}'.")


def parse_modifier(modifier: str) -> tuple[str, str | None]:
    if modifier.endswith("+f"):
        return modifier[:-2], "+f"
    if modifier.endswith("-f"):
        return modifier[:-2], "-f"
    return modifier, None


def normalize_at_line(raw: str) -> str:
    line = raw.strip()
    if not line:
        return ""
    if line.startswith("#") or line.startswith("//"):
        return ""

    hash_index = line.find("#")
    if hash_index >= 0:
        line = line[:hash_index].strip()

    slash_index = line.find("//")
    if slash_index >= 0:
        line = line[:slash_index].strip()

    return line


def convert_at_line_to_aw(
    line: str,
    resolver: VanillaSourceResolver | None,
) -> tuple[list[str], str | None]:
    tokens = line.split()
    if len(tokens) < 2:
        return [], "invalid"

    modifier_raw = tokens[0]
    base_modifier, final_flag = parse_modifier(modifier_raw)
    owner_dot = tokens[1]
    owner_slash = owner_dot.replace(".", "/")

    if not owner_dot.startswith("net.minecraft."):
        return [], "non-minecraft"

    directives: list[str] = []

    if len(tokens) == 2:
        if base_modifier == "public":
            if final_flag == "-f":
                directives.append(f"extendable class {owner_slash}")
            else:
                directives.append(f"accessible class {owner_slash}")
            return directives, None
        if base_modifier == "protected":
            directives.append(f"extendable class {owner_slash}")
            return directives, None
        return [], "unsupported-class-modifier"

    member = tokens[2]
    if "(" in member and ")" in member:
        name, desc = member.split("(", 1)
        desc = "(" + desc
        if base_modifier == "public":
            directives.append(f"accessible method {owner_slash} {name} {desc}")
            if final_flag == "-f":
                return directives, "method-final-removal-partial"
            return directives, None
        if base_modifier == "protected":
            directives.append(f"extendable method {owner_slash} {name} {desc}")
            return directives, None
        return [], "unsupported-method-modifier"

    field_name = member
    field_desc = tokens[3] if len(tokens) >= 4 else None
    if field_desc is None and resolver is not None:
        inferred_desc, reason = resolver.infer_field_descriptor(owner_dot, field_name)
        if inferred_desc:
            field_desc = inferred_desc
        else:
            return [], reason or "field-missing-descriptor"
    if field_desc is None:
        return [], "field-missing-descriptor"

    if base_modifier == "public":
        directives.append(f"accessible field {owner_slash} {field_name} {field_desc}")
    if final_flag == "-f":
        directives.append(f"mutable field {owner_slash} {field_name} {field_desc}")

    if not directives:
        return [], "unsupported-field-modifier"
    return directives, None


def find_vanilla_sources_jar() -> Path | None:
    artifact_dir = ROOT / "common" / "build" / "moddev" / "artifacts"
    if not artifact_dir.exists():
        return None

    candidates = sorted(
        artifact_dir.glob("vanilla-*-sources.jar"),
        key=lambda p: p.stat().st_mtime,
        reverse=True,
    )
    return candidates[0] if candidates else None


def generate_classtweaker(mod_id: str) -> None:
    ensure_meta_inf()
    resources_root = ROOT / "common" / "src" / "main" / "resources"
    at_path = resources_root / "META-INF" / "accesstransformer.cfg"
    if not at_path.exists():
        at_path = resources_root / "MEAT-INF" / "accesstransformer.cfg"
    if not at_path.exists():
        raise FileNotFoundError("Cannot find accesstransformer.cfg under common/src/main/resources/META-INF.")

    sources_jar = find_vanilla_sources_jar()
    resolver = VanillaSourceResolver(sources_jar) if sources_jar else None

    output_path = resources_root / f"{mod_id}.classtweaker"
    generated: list[str] = [
        "accessWidener v2 official",
        "# Generated from common/src/main/resources/META-INF/accesstransformer.cfg",
        "# Only Minecraft vanilla (net.minecraft.*) access widening is auto-converted.",
        "# For non-vanilla entries or unsupported directives, handle manually.",
    ]
    if sources_jar:
        generated.append(f"# Descriptor inference source: {sources_jar.relative_to(ROOT)}")
    generated.append("")

    skipped: list[str] = []

    try:
        for raw in read_text(at_path).splitlines():
            line = normalize_at_line(raw)
            if not line:
                continue
            directives, reason = convert_at_line_to_aw(line, resolver)
            if directives:
                generated.extend(directives)
            if reason:
                skipped.append(f"# SKIPPED [{reason}] {line}")
    finally:
        if resolver is not None:
            resolver.close()

    if skipped:
        generated.append("")
        generated.append("# Skipped source lines")
        generated.extend(skipped)

    write_text(output_path, "\n".join(generated).rstrip() + "\n")
    print(f"[classtweaker] generated: {output_path}")


def sync_from_properties() -> None:
    props = read_properties(ROOT / "gradle.properties")

    target_group = props.get("group", "").strip()
    target_mod_id = props.get("mod_id", "").strip()
    target_mod_name = props.get("mod_name", "").strip()

    legacy_main = props.get("mainclassname", "").strip()
    target_common_main = props.get("common_mainclassname", legacy_main).strip()
    target_fabric_main = props.get("fabric_mainclassname", "").strip()
    target_neoforge_main = props.get("neoforge_mainclassname", "").strip()

    if not target_common_main:
        raise ValueError("Missing required 'common_mainclassname' (or legacy 'mainclassname') in gradle.properties")
    if not target_fabric_main:
        target_fabric_main = f"{target_common_main}Fabric"
    if not target_neoforge_main:
        target_neoforge_main = f"{target_common_main}NeoForge"

    if not target_group:
        raise ValueError("Missing required 'group' in gradle.properties")
    if not target_mod_id:
        raise ValueError("Missing required 'mod_id' in gradle.properties")
    if not target_mod_name:
        raise ValueError("Missing required 'mod_name' in gradle.properties")

    ensure_meta_inf()

    old_state = detect_state(
        default_mod_id=target_mod_id,
        default_mod_name=target_mod_name,
        default_common_main=target_common_main,
    )

    print(f"[sync] package: {old_state.package} -> {target_group}")
    print(f"[sync] mod id: {old_state.mod_id} -> {target_mod_id}")
    print(f"[sync] mod name: {old_state.mod_name} -> {target_mod_name}")
    print(f"[sync] common main: {old_state.common_main} -> {target_common_main}")
    print(f"[sync] fabric main: {old_state.fabric_main} -> {target_fabric_main}")
    print(f"[sync] neoforge main: {old_state.neoforge_main} -> {target_neoforge_main}")

    rename_package_dirs(old_state.package, target_group)

    rename_main_class_file("common", target_group, old_state.common_main, target_common_main)
    rename_main_class_file("fabric", target_group, old_state.fabric_main, target_fabric_main)
    rename_main_class_file("neoforge", target_group, old_state.neoforge_main, target_neoforge_main)

    rename_modid_files(old_state.mod_id, target_mod_id)
    rename_service_filename(old_state.package, target_group)

    rewrite_contents(
        old_state=old_state,
        new_package=target_group,
        new_mod_id=target_mod_id,
        new_common_main=target_common_main,
        new_fabric_main=target_fabric_main,
        new_neoforge_main=target_neoforge_main,
    )

    update_main_constants(
        package_name=target_group,
        main_class=target_common_main,
        mod_id=target_mod_id,
        mod_name=target_mod_name,
    )

    target_license = props.get("license", "").strip()
    if target_license:
        update_license_file(target_license)

    generate_classtweaker(target_mod_id)
    print("[sync] done.")


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(description="Sync MultiLoader template from gradle.properties")
    sub = parser.add_subparsers(dest="command", required=True)
    sub.add_parser("sync", help="Sync package/main classes/mod id/license and regenerate classtweaker.")
    sub.add_parser("generate-classtweaker", help="Generate <mod_id>.classtweaker from accesstransformer.cfg.")
    return parser


def main() -> int:
    parser = build_parser()
    args = parser.parse_args()
    props = read_properties(ROOT / "gradle.properties")
    mod_id = props.get("mod_id", "").strip()

    if args.command == "sync":
        sync_from_properties()
        return 0

    if args.command == "generate-classtweaker":
        if not mod_id:
            raise ValueError("Missing required 'mod_id' in gradle.properties")
        generate_classtweaker(mod_id)
        return 0

    parser.print_help()
    return 1


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exc:  # pragma: no cover
        print(f"[error] {exc}", file=sys.stderr)
        raise
