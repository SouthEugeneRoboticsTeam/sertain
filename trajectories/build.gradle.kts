import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect", "1.3.41"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.0-M2")
    implementation("edu.wpi.first.wpilibj", "wpilibj-java", "2019.4.1")
    implementation("edu.wpi.first.hal", "hal-java", "2019.4.1")
    implementation("edu.wpi.first.ntcore", "ntcore-java", "2019.4.1")
    implementation("com.ctre.phoenix", "api-java", "5.14.1")
    compile(project(":core"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}