plugins {
    kotlin("jvm")version "1.4.30-RC"

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

            doLast {
                copy {
                    from("build/libs/${project.name}")
                    to("/home/cloud/output")
                }
            }
        }

        shadowJar {
            archiveFileName.set(project.name)
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