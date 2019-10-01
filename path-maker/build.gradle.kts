import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect", "1.3.41"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.0-M2")
    implementation("no.tornado", "tornadofx", "1.7.19")
    compile(project(":trajectories"))
}

application {
    mainClassName = "org.sert2521.sertain.pathmaker.MainKt"
}

javafx {
    version = "13"
    modules = listOf("javafx.controls", "javafx.fxml")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
