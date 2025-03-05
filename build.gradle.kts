plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("com.gradleup.shadow") version "8.3.1"
}

application.mainClass = "com.mhu.bot.LabBotKt"
group = "org.example"
version = "1.2-SNAPSHOT"

val jdaVersion = "5.2.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    implementation("com.fazecast:jSerialComm:2.11.0")
}



tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    sourceCompatibility = "17"
    options.release = 17
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

