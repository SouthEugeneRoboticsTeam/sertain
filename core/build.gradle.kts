import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktlint by configurations.creating

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
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.1")
    implementation("edu.wpi.first.wpilibj", "wpilibj-java", "2019.4.1")
    implementation("edu.wpi.first.hal", "hal-java", "2019.4.1")
    implementation("edu.wpi.first.ntcore", "ntcore-java", "2019.4.1")
    implementation("com.ctre.phoenix", "api-java", "5.14.1")

    ktlint("com.pinterest:ktlint:0.34.2")
}

tasks {
    val ktlint by creating(JavaExec::class) {
        group = "verification"
        description = "Check Kotlin code style."
        classpath = configurations["ktlint"]
        main = "com.pinterest.ktlint.Main"
        args = listOf("src/**/*.kt")
    }

    "check" {
        dependsOn(ktlint)
    }

    create("ktlintFormat", JavaExec::class) {
        group = "formatting"
        description = "Fix Kotlin code style deviations."
        classpath = configurations["ktlint"]
        main = "com.pinterest.ktlint.Main"
        args = listOf("-F", "src/**/*.kt")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += setOf("-Xuse-experimental=kotlin.Experimental")
}
