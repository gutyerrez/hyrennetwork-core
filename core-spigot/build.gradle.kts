dependencies {
    // paperspigot
    compileOnly("org.github.paperspigot:paperspigot:1.8.8-R0.1-SNAPSHOT")

    // waterfall chat
    compileOnly("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")

    // eventbus
    compileOnly("org.greenrobot:eventbus:3.2.0")

    // core shared
    implementation(project(":core-shared"))
}