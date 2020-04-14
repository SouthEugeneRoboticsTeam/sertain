pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        val frcYear = "2020"
        val frcHome = if (true) {
            var publicFolder = System.getenv("PUBLIC")
            if (publicFolder == null) {
                publicFolder = "C:\\Users\\Public"
            }
            val homeRoot = File(publicFolder, "wpilib")
            File(homeRoot, frcYear)
        } else {
            val userFolder = System.getProperty("user.home")
            val homeRoot = File(userFolder, "wpilib")
            File(homeRoot, frcYear)
        }
        val frcHomeMaven = File(frcHome, "maven")
        maven {
            name = "frcHome"
            url = frcHomeMaven.toURI()
        }
    }
}

rootProject.name = "sertain"

include("core")
include("example")
