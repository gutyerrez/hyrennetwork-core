import java.io.FileNotFoundException

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"

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

    tasks {
        compileKotlin {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        shadowJar {
            val fileName = "${project.name}.jar"

            archiveFileName.set("${project.name}.jar")

            doLast {
                try {
                    val file = file("build/libs/$fileName")

                    val toDelete = file("/home/cloud/output/$fileName")

                    if (toDelete.exists()) toDelete.delete()

                    file.copyTo(file("/home/cloud/output/$fileName"))
                    file.delete()
                } catch (ex: FileNotFoundException) {
                    ex.printStackTrace()
                }
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

    val sources by tasks.registering(Jar::class) {
        archiveFileName.set(project.name)
        archiveClassifier.set("sources")
        archiveVersion.set(null as String?)

        from(sourceSets.main.get().allSource)
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["kotlin"])
                artifact(sources.get())
            }
        }
    }
}