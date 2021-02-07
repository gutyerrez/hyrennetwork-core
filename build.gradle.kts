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
                val fileName = "${project.name}.jar"
                val file = file("build/libs/$fileName")

                val toDelete = file("/home/cloud/output/$fileName")
                
                if (toDelete.exists()) toDelete.delete()

                file.copyTo(file("/home/cloud/output/$fileName"))
                file.delete()
            }
        }

        shadowJar {
            archiveFileName.set("${project.name}.jar")
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