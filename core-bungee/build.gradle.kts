dependencies {
    // waterfall proxy
    api("io.github.waterfallmc:waterfall-proxy:1.16-R0.5-SNAPSHOT")

    // exposed
    api("org.jetbrains.exposed:exposed-core:0.29.1")
    api("org.jetbrains.exposed:exposed-jodatime:0.29.1")

    // eventbus
    api("org.greenrobot:eventbus:3.2.0")

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
            from(components["kotlin"])
            artifact(sources.get())
        }
    }
}