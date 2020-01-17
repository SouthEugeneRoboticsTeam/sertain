import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    java
    `maven-publish`
    maven
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    implementation(kotlin("reflect", "1.3.61"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("edu.wpi.first.wpilibj:wpilibj-java:2020.1.2")
    implementation("edu.wpi.first.hal:hal-java:2020.1.2")
    implementation("edu.wpi.first.wpiutil:wpiutil-java:2020.1.2")
    implementation("edu.wpi.first.ntcore:ntcore-java:2020.1.2")
    implementation("com.ctre.phoenix:api-java:5.14.1")
    implementation("com.revrobotics.frc:SparkMax-java:1.4.1")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses", "-Xuse-experimental=kotlin.Experimental")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact("$buildDir/libs/${project.name}-${project.version}.jar")
        }
    }
}
