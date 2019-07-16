import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
}

group = "org.sert2521.sertain"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("http://first.wpi.edu/FRC/roborio/maven/release")
    maven("http://devsite.ctr-electronics.com/maven/release/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("edu.wpi.first.wpilibj", "wpilibj-java", "2019.4.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
