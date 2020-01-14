
plugins {
    kotlin("jvm") version "1.3.61"
    application
    java
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/")
    maven("https://dl.bintray.com/kotlin/kotlin-dev")
}

subprojects {
    group = "org.sert2521.sertain"
    version = "0.0.8"
}
