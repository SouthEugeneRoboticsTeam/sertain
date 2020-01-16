rootProject.name = "sertain"

include(":core")

pluginManagement {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
        eachPlugin {
            when (target.id.id) {
                "org.jetbrains.kotlin.jvm" -> {
                    useModule("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${target.version}")
                }
            }
        }
    }
}
