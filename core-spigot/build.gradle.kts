dependencies {
    // spigot
    compileOnly("org.github.paperspigot:paper-spigot-server:1.8.8-R0.1-SNAPSHOT")

    // exposed
    compileOnly("org.jetbrains.exposed:exposed-dao:0.31.1")
    compileOnly("org.jetbrains.exposed:exposed-jodatime:0.31.1")

    // serialization
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")

    // eventbus
    compileOnly("org.greenrobot:eventbus:3.2.0")

    // caffeine
    compileOnly("com.github.ben-manes.caffeine:caffeine:2.8.5")

    // minecraft-server
    compileOnly("net.hyren:minecraft-server:1.8.8-SNAPSHOT")

    // core-shared
    implementation(project(":core-shared"))
}