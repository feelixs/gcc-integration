plugins {
  id("org.jetbrains.intellij.platform") version "2.5.0"
  id("org.jetbrains.intellij.platform.migration") version "2.5.0"
  id("java")
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
    create("IC", "2024.3")
  }
}

intellijPlatform {
    pluginConfiguration {
        name.set("GCC Integration")
        version.set(project.version.toString())
        ideaVersion {
            sinceBuild = "231"
            untilBuild = "243.*"
        }
    }
    
    signing {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }
    
    publishing {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}
