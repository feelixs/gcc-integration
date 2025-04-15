plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "com.mikefmh.gcc-integration"

// WHEN UPLOADING A NEW VERSION:
// Edit plugin.xml if you need to update compatible versions of pycharm/etc
// THEN edit this file as well to update compatibility
version = "1.2.5"

repositories {
    mavenCentral()
}

intellij {
    type.set("IC")
    version.set("2024.3.5")
    updateSinceUntilBuild.set(true)
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



    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
