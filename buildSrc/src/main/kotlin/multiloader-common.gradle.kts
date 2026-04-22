import groovy.util.Node
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    `maven-publish`
}

val mod_name: String by project
val mod_author: String by project
val minecraft_version: String by project
val java_version: String by project
val minecraft_version_range: String by project
val fabric_version: String by project
val fabric_loader_version: String by project
val mod_id: String by project
val license: String by project
val neoforge_version: String by project
val neoforge_loader_version_range: String by project
val credits: String by project

base {
    archivesName.set("$mod_name-${project.name}-$minecraft_version")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(java_version.toInt())
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    // https://docs.gradle.org/current/userguide/declaring_repositories.html#declaring_content_exclusively_found_in_one_repository
    exclusiveContent {
        forRepository {
            maven {
                name = "Sponge"
                url = uri("https://repo.spongepowered.org/repository/maven-public")
            }
        }
        filter {
            includeGroupAndSubgroups("org.spongepowered")
        }
    }
    maven {
        name = "BlameJared"
        url = uri("https://maven.blamejared.com")
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "GeckoLib"
                url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
            }
        }
        filter { includeGroupAndSubgroups("com.geckolib") }
    }
    maven { url = uri("https://maven.shedaniel.me/") }
}

tasks.named<Jar>("sourcesJar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_$mod_name" }
    }
}

tasks.named<Jar>("jar") {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_$mod_name" }
    }

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_name,
                "Specification-Vendor" to mod_author,
                "Specification-Version" to archiveVersion.get(),
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion.get(),
                "Implementation-Vendor" to mod_author,
                "Built-On-Minecraft" to minecraft_version,
            ),
        )
    }
}

tasks.named<ProcessResources>("processResources") {
    val expandProps = mapOf(
        "version" to version,
        "group" to project.group,
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "fabric_version" to fabric_version,
        "fabric_loader_version" to fabric_loader_version,
        "mod_name" to mod_name,
        "mod_author" to mod_author,
        "mod_id" to mod_id,
        "license" to license,
        "description" to (project.description ?: ""),
        "neoforge_version" to neoforge_version,
        "neoforge_loader_version_range" to neoforge_loader_version_range,
        "credits" to credits,
        "java_version" to java_version,
    )

    val jsonExpandProps = expandProps.mapValues { (_, value) ->
        if (value is String) value.replace("\n", "\\\\n") else value
    }

    filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
        expand(expandProps)
    }

    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "*.mixins.json")) {
        expand(jsonExpandProps)
    }

    inputs.properties(expandProps)
}

extra["mavenDependencyWhitelist"] = emptySet<String>()

fun Node.childText(name: String): String {
    val child = children()
        .filterIsInstance<Node>()
        .firstOrNull { it.name().toString() == name }
        ?: return ""
    return child.text().trim()
}

fun resolveMavenDependencyWhitelist(): Set<String> {
    val rawWhitelist = extra.properties["mavenDependencyWhitelist"]
    val rawValues = when (rawWhitelist) {
        null -> emptyList()
        is CharSequence -> listOf(rawWhitelist.toString())
        is Iterable<*> -> rawWhitelist.filterNotNull().map { it.toString() }
        else -> listOf(rawWhitelist.toString())
    }

    return rawValues
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

data class PomDependencyInfo(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val scope: String,
)

extensions.configure<PublishingExtension> {
    repositories {
        val localMavenUrl = providers.environmentVariable("local_maven_url").orNull
        if (!localMavenUrl.isNullOrBlank()) {
            maven {
                url = uri(localMavenUrl)
            }
        }

        val modVersion = project.version.toString().ifBlank { "unknown" }
        val isSnapshot = modVersion.contains("snapshot", ignoreCase = true)
        val publishUrl = if (isSnapshot) {
            "https://maven.sighs.cc/repository/maven-snapshots/"
        } else {
            "https://maven.sighs.cc/repository/maven-releases/"
        }

        maven {
            name = "remoteRepo"
            url = uri(publishUrl)
            credentials {
                username = providers.environmentVariable("SIGHS_PUBLISH_USER").orNull
                password = providers.environmentVariable("SIGHS_PUBLISH_PASSWORD").orNull
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = base.archivesName.get()
            version = project.version.toString()

            pom.withXml {
                val whitelist = resolveMavenDependencyWhitelist()
                fun isAllowed(groupId: String, artifactId: String): Boolean {
                    val coordinate = "$groupId:$artifactId"
                    return whitelist.contains(groupId) ||
                        whitelist.contains(artifactId) ||
                        whitelist.contains(coordinate)
                }

                val root = asNode()
                var dependenciesNode = root.children()
                    .filterIsInstance<Node>()
                    .firstOrNull { it.name().toString() == "dependencies" }

                if (dependenciesNode != null) {
                    val toRemove = dependenciesNode.children()
                        .filterIsInstance<Node>()
                        .filter { dep ->
                            val groupId = dep.childText("groupId")
                            val artifactId = dep.childText("artifactId")
                            !isAllowed(groupId, artifactId)
                        }

                    toRemove.forEach { dependenciesNode.remove(it) }
                }

                val declaredDependencies = linkedMapOf<String, PomDependencyInfo>()
                listOf(
                    "api" to "compile",
                    "implementation" to "runtime",
                    "runtimeOnly" to "runtime",
                    "compileOnly" to "compile",
                ).forEach { (configurationName, scope) ->
                    val configuration = configurations.findByName(configurationName) ?: return@forEach
                    configuration.dependencies
                        .withType(ExternalModuleDependency::class.java)
                        .forEach { dep ->
                            val groupId = dep.group?.trim().orEmpty()
                            val artifactId = dep.name.trim()
                            val dependencyVersion = dep.version?.trim().orEmpty()

                            if (groupId.isEmpty() || artifactId.isEmpty() || dependencyVersion.isEmpty()) {
                                return@forEach
                            }
                            if (!isAllowed(groupId, artifactId)) {
                                return@forEach
                            }

                            val coordinate = "$groupId:$artifactId"
                            val existing = declaredDependencies[coordinate]
                            if (existing == null || scope == "compile") {
                                declaredDependencies[coordinate] = PomDependencyInfo(
                                    groupId = groupId,
                                    artifactId = artifactId,
                                    version = dependencyVersion,
                                    scope = scope,
                                )
                            }
                        }
                }

                if (dependenciesNode == null && declaredDependencies.isNotEmpty()) {
                    dependenciesNode = root.appendNode("dependencies")
                }

                if (dependenciesNode != null) {
                    val existingCoordinates = dependenciesNode.children()
                        .filterIsInstance<Node>()
                        .map { dep ->
                            val groupId = dep.childText("groupId")
                            val artifactId = dep.childText("artifactId")
                            "$groupId:$artifactId"
                        }
                        .toSet()

                    declaredDependencies.forEach { (coordinate, depInfo) ->
                        if (!existingCoordinates.contains(coordinate)) {
                            val depNode = dependenciesNode.appendNode("dependency")
                            depNode.appendNode("groupId", depInfo.groupId)
                            depNode.appendNode("artifactId", depInfo.artifactId)
                            depNode.appendNode("version", depInfo.version)
                            depNode.appendNode("scope", depInfo.scope)
                        }
                    }
                }

                if (dependenciesNode != null && dependenciesNode.children().isEmpty()) {
                    root.remove(dependenciesNode)
                }
            }
        }
    }
}

