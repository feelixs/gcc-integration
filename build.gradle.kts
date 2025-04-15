plugins {
  id("org.jetbrains.intellij.platform") version "2.5.0"
  id("org.jetbrains.intellij.platform.migration") version "2.5.0"
}

group = "com.mikefmh.gcc-integration"

// WHEN UPLOADING A NEW VERSION:
// edit this file to update compatibility -> it will update xml in build
version = "1.2.5"

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  intellijPlatform {
    create(type, version)
  }
}

intellijPlatform {
    pluginConfigurations {
        intellij {
            version.set("2024.3")
        }
    }
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
