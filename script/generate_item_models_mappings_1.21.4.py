import os
import json
from pathlib import Path

# === 配置区域 Start ===

# 输入（原模型）路径
SOURCE_DIR = r"F:\code\mcmod\project\Ribbits-1.21.10\common\src\main\resources\assets\ribbits\models\item"

# 输出（新 item 模型）路径
OUTPUT_DIR = r"F:\code\mcmod\project\Ribbits-1.21.10\common\src\main\resources\assets\ribbits\items"

# 特殊命名规则
BLOCKING_SUFFIX = "_in_hand"
SPECIAL_ITEM_BASENAME = "maraca"  # 需要用 condition 处理的特殊物品

# 基础 namespace
NAMESPACE = "ribbits"

# 是否跳过 blocking 文件
SKIP_BLOCKING = True

# Condition 配置（仅用于 maraca）
CONDITION_PROPERTY = "ribbits:maraca_using"

# === 配置区域 End ===


def generate_model_json(base_name: str):
    """根据文件名生成对应的 items 模型 JSON"""

    # 1. 跳过所有 _in_hand 结尾的文件
    if SKIP_BLOCKING and base_name.endswith(BLOCKING_SUFFIX):
        return None

    # 2. 特殊处理：maraca → 使用 condition 切换 in_hand 状态
    if base_name == SPECIAL_ITEM_BASENAME:
        return {
            "model": {
                "type": "minecraft:condition",
                "property": CONDITION_PROPERTY,
                "on_true": {
                    "type": "minecraft:model",
                    "model": f"{NAMESPACE}:item/{base_name}{BLOCKING_SUFFIX}"
                },
                "on_false": {
                    "type": "minecraft:model",
                    "model": f"{NAMESPACE}:item/{base_name}"
                }
            }
        }

    # 3. 其他所有普通物品：直接引用原模型（不做 condition）
    return {
        "model": {
            "type": "minecraft:model",
            "model": f"{NAMESPACE}:item/{base_name}"
        }
    }


def main():
    source_path = Path(SOURCE_DIR)
    output_path = Path(OUTPUT_DIR)

    # 确保输出目录存在
    output_path.mkdir(parents=True, exist_ok=True)

    # 调试：检查源路径
    print(f"🔍 扫描目录: {source_path}")
    if not source_path.exists():
        print(f"❌ 错误：源目录不存在！")
        return

    json_files = list(source_path.glob("*.json"))
    print(f"✅ 找到 {len(json_files)} 个 .json 文件")

    if not json_files:
        print(f"⚠️  源目录中没有 .json 文件，请检查。")
        return

    # 处理每个文件
    for file_path in json_files:
        base_name = file_path.stem

        json_data = generate_model_json(base_name)
        if json_data is None:
            print(f"⏩ 跳过: {file_path.name}")
            continue

        output_file = output_path / f"{base_name}.json"
        try:
            with open(output_file, "w", encoding="utf-8") as f:
                json.dump(json_data, f, ensure_ascii=False, indent=2)
            print(f"✅ 生成: {output_file}")
        except Exception as e:
            print(f"❌ 写入失败 {output_file}: {e}")

    print("🎉 所有文件处理完成。")


if __name__ == "__main__":
    main()