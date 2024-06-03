buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plug.kotlin)
        classpath(libs.plug.android)
        classpath(libs.plug.compose.compiler)
        classpath(libs.plug.wire)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
