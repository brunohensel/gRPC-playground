pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    versionCatalogs {
        create("androidx") {
            from("androidx.gradle:gradle-version-catalog:2024.05.00")
        }
    }
}

rootProject.name = "dev.brunohensel.grpc-playground"
include(":app")
include(":proto")
include(":server")
include(":server:ktor")
include(":server:misk")
