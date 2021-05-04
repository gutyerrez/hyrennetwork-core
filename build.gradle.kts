import java.net.URI

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

	fun RepositoryHandler.githubRepository(): MavenArtifactRepository {
		return maven {
			name = "github"
			url = URI("https://maven.pkg.github.com/hyrendev/nexus/")
		}
	}

	repositories {
		mavenCentral()

		jcenter()

		maven {
			name = "sonatype-nexus-snapshot"
			url = URI("https://hub.spigotmc.org/nexus/content/repositories/sonatype-nexus-snapshots/")
		}

		this.githubRepository()
	}

	val sources by tasks.registering(Jar::class) {
		archiveFileName.set(project.name)
		archiveClassifier.set("sources")
		archiveVersion.set(null as String?)

		from(sourceSets.main.get().allSource)
	}

	publishing {
		publications {
			repositories { this.githubRepository() }

			create<MavenPublication>("maven") {
				from(components["kotlin"])
				artifact(sources.get())
			}
		}
	}
}