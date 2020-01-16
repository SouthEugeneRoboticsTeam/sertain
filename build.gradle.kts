plugins {
    kotlin("jvm") version "1.3.60"
    maven
    `maven-publish`
    java
}

allprojects {
    group = "org.sert2521.sertain"
    version = "0.0.8"

    repositories {
        jcenter()
        mavenCentral()
        gradlePluginPortal()
        maven("https://frcmaven.wpi.edu/artifactory/release/")
        maven("https://plugins.gradle.org/m2/")
        maven("http://devsite.ctr-electronics.com/maven/release/")
        maven("http://www.revrobotics.com/content/sw/max/sdk/maven/")
    }
}
