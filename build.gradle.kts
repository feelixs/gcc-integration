plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.mikefmh.gcc-integration"
version = "1.2.5"

repositories {
    mavenCentral()
}

intellij {
    pluginName.set("gcc-integration")
    version.set("2024.3.5")
    type.set("IC")
    plugins.set(listOf("java"))
    updateSinceUntilBuild.set(true)
    downloadSources.set(!System.getenv().containsKey("CI"))
    instrumentCode.set(true)
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
        sinceBuild.set("233")
        untilBuild.set("245.*")
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
