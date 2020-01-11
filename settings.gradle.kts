rootProject.name = "sertain"

include(":core")

pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://plugins.gradle.org/m2/")
    }
}
