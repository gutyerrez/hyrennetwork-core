dependencies {
    // waterfall
    compileOnly("io.github.waterfallmc:waterfall-api:1.16-R0.5-SNAPSHOT")
    compileOnly("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")
    compileOnly("io.github.waterfallmc:waterfall-proxy:1.16-R0.5-SNAPSHOT")
    compileOnly("io.github.waterfallmc:waterfall-event:1.16-R0.5-SNAPSHOT")

    // exposed
    compileOnly("org.jetbrains.exposed:exposed-core:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-dao:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jodatime:0.31.1")

    // eventbus
    compileOnly("org.greenrobot:eventbus:3.2.0")

    // core-shared
    implementation(project(":core-shared"))
}