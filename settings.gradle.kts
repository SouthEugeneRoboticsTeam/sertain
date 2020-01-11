rootProject.name = "sertain"

include(":core")

pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "kotlin" -> {
                    println("Lol you are trash")
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                }
            }
        }
    }
}
