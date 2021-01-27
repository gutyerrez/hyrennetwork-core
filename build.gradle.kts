plugins {
    kotlin("jvm") version "1.4.21"

    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "6.1.0"
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
                jvmTarget = "1.8"
            }
        }
    }

    group = "com.redefantasy"
    version = "0.1-ALPHA"

    repositories {
        mavenCentral()

        mavenLocal()

        jcenter()
    }
}