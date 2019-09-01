plugins {
    kotlin("jvm") version "1.3.50"
}

allprojects {
    group = "org.sert2521.sertain"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven("http://first.wpi.edu/FRC/roborio/maven/release")
        maven("http://devsite.ctr-electronics.com/maven/release/")
    }
}


