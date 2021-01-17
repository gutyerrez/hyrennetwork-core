plugins {
    kotlin("jvm") version "1.4.10"

    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

repositories {
    mavenCentral()

    jcenter()
}

subprojects {
    plugins.apply("com.github.johnrengelman.shadow")
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("maven-publish")
    plugins.apply("java")

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = "14"
            }
        }
    }

    group = "com.redefantasy"
    version = "0.1-ALPHA"

    repositories {
        mavenCentral()

        jcenter()
    }
}