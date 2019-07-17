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
    implementation(kotlin("reflect", "1.3.41"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.0-M2")
    implementation("edu.wpi.first.wpilibj", "wpilibj-java", "2019.4.1")
    implementation("edu.wpi.first.hal", "hal-java", "2019.4.1")
    implementation("edu.wpi.first.ntcore", "ntcore-java", "2019.4.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
