plugins {
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.serialization") version "1.4.31"

	id("com.github.johnrengelman.shadow") version "6.1.0"

	`maven-publish`
	java
}

allprojects {
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
			archiveFileName.set("${project.name}.jar")
		}
	}

	group = "com.redefantasy"
	version = "0.1-ALPHA"


	repositories {
		mavenCentral()

		jcenter()

		maven("https://hub.spigotmc.org/nexus/content/repositories/sonatype-nexus-snapshots/")
		maven("https://maven.pkg.github.com/hyrendev/nexus/") {
			credentials {
				username = System.getenv("MAVEN_USERNAME")
				password = System.getenv("MAVEN_PASSWORD")
			}
		}
	}

	configurations.all {
		resolutionStrategy.cacheChangingModulesFor(120, "seconds")
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
				repositories {
					maven("https://maven.pkg.github.com/hyrendev/nexus/") {
						credentials {
							username = System.getenv("MAVEN_USERNAME")
							password = System.getenv("MAVEN_PASSWORD")
						}
					}
				}

				from(components["kotlin"])
				artifact(sources.get())

				pom {
					name.set(project.name)
					description.set("Core for all projects in Hyren's server")

					developers {
						developer {
							name.set("Gutyerrez")
							url.set("https://twitter.com/SrGutyerrez")
							roles.set(
								listOf(
									"owner"
								)
							)
							organization.set("HyrenDev")
						}
					}
				}
			}
		}
	}
}