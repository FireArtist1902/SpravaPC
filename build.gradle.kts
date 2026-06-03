plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "org.example"

version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.50.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.50.1")
    implementation("org.swinglabs:swingx:1.6.1")
    implementation("com.github.lgooddatepicker:LGoodDatePicker:11.2.1")

    implementation("org.xerial:sqlite-jdbc:3.46.0.0")

    implementation("com.formdev:flatlaf:3.5")
}

kotlin {

    jvmToolchain(17)
}

application {

    mainClass.set("MainKt")
}