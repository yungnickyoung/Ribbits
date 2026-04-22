import org.gradle.api.attributes.Attribute

plugins {
    id("multiloader-common")
    id("net.neoforged.moddev")
}

val neo_form_version: String by project
val geckolib_version: String by project

neoForge {
    neoFormVersion = neo_form_version

    // Automatically enable AccessTransformers if the file exists
    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")
    compileOnly(files("../libs/YungsApi-26.1.1-Common-5.10.0-beta0.jar"))
    compileOnly("com.geckolib:geckolib-common-26.1:$geckolib_version")
}

// Whitelist entries control which dependencies are kept/added in generated maven POM.
// Use listOf(...) when you need to publish selected dependencies, e.g.:
// extra["mavenDependencyWhitelist"] = listOf(
//     "org.spongepowered",              // by groupId
//     "mixin",                          // by artifactId
//     "io.github.llamalad7:mixinextras-common", // by full coordinate: groupId:artifactId
// )
extra["mavenDependencyWhitelist"] = emptyList<String>()

val commonJava by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "common")
            }
        }
    }
}
