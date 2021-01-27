dependencies {
    // waterfall proxy
    api("io.github.waterfallmc:waterfall-proxy:1.16-R0.4-SNAPSHOT")

    // core shared
    implementation(project(":core-shared"))
}

val sources by tasks.registering(Jar::class) {
    archiveBaseName.set(project.name)
    archiveClassifier.set("sources")
    archiveVersion.set(null as String?)

    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sources.get())
        }
    }
}