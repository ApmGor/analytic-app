rootProject.name = "analytic-app"

pluginManagement {
    repositories.gradlePluginPortal()
    includeBuild("gradle/plugins")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("product-service")
include("common")
include("analytic-service")

