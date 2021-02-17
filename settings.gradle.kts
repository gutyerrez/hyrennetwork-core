rootProject.name = "core"

pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

include(
    "core-shared",
    "core-bungee",
    "core-spigot"
)