dependencies {
    // paperspigot
    compileOnly("org.github.paperspigot:paperspigot:1.8.8-R0.1-SNAPSHOT")

    // waterfall chat
    compileOnly("io.github.waterfallmc:waterfall-chat:1.16-R0.5-SNAPSHOT")

    // exposed
    compileOnly("org.jetbrains.exposed:exposed-core:0.29.1")
    compileOnly("org.jetbrains.exposed:exposed-dao:0.29.1")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:0.29.1")
    compileOnly("org.jetbrains.exposed:exposed-jodatime:0.29.1")

    // jackson
    compileOnly("com.fasterxml.jackson.core:jackson-core:2.12.2")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.12.2")
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.12.2")
    compileOnly("com.fasterxml.jackson.datatype:jackson-datatype-guava:2.11.2")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.2")

    // eventbus
    compileOnly("org.greenrobot:eventbus:3.2.0")

    // core shared
    implementation(project(":core-shared"))
}