plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {
    plugins.set(listOf(
        "java" // Bundled Java support
    ))
}

group = "com.mikefmh.gcc-integration"

// WHEN UPLOADING A NEW VERSION:
// edit this file to update compatibility -> it will update xml in build
// make sure 'plugins {' is set to the updated versions
version = "1.2.5"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform {
    type.set("IC") // IntelliJ IDEA Community Edition
    version.set("2024.3.5")
    updateSinceUntilBuild.set(true)
    downloadSources.set(!System.getenv().containsKey("CI")) // Download sources in local dev
    instrumentCode.set(true) // Enable instrumentation code for testing
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        version.set(project.version.toString())
        sinceBuild.set("231")
        untilBuild.set("243.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
