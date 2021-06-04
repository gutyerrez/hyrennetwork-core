dependencies {
    // kotlin lib
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    // kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    // commons-io
    implementation("commons-io:commons-io:2.8.0")

    // hikari cp
    implementation("com.zaxxer:HikariCP:3.4.5")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.31.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.31.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.31.1")

    // postgresql
    implementation("org.postgresql:postgresql:42.2.20")

    // redis
    implementation("redis.clients:jedis:3.3.0")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

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