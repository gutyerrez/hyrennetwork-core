dependencies {
    // kotlin lib
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // google
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.google.code.gson:gson:2.8.6")

    // commons-lang 3
    implementation("org.apache.commons:commons-lang3:3.11")

    // commons-io
    implementation("commons-io:commons-io:2.8.0")

    // hikari cp
    implementation("com.zaxxer:HikariCP:3.4.5")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.29.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.29.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.29.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.29.1")

    // postgres
    implementation("org.postgresql:postgresql:42.2.16")

    // mongodb
    implementation("org.mongodb:mongo-java-driver:3.9.1")
    implementation("org.mongodb:mongodb-driver-sync:4.1.0")
    implementation("org.mongodb:bson:4.1.0")

    // jedis
    implementation("redis.clients:jedis:3.3.0")

    // influx db
    implementation("org.influxdb:influxdb-java:2.20")

    // jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.11.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.11.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

    // eventbus
    implementation("org.greenrobot:eventbus:3.2.0")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.8.1")
    implementation("com.squareup.okio:okio:2.8.0")

    // caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:2.8.5")

    // waterfall-chat
    api("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")
}

val sources by tasks.registering(Jar::class) {
    archiveBaseName.set(project.name)
    archiveClassifier.set("sources")
    archiveVersion.set(null as String?)

    destinationDirectory.set(file("/home/cloud/output"))

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