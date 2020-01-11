rootProject.name = "sertain"

include(":core")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.3.61"
    }
    repositories {
        mavenCentral()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> {
                    println("Lol you are trash")
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
                }
            }
        }
    }
}

