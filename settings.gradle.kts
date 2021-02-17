pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "core"

include(
    "core-shared",
    "core-bungee",
    "core-spigot"
)