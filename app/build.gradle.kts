import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin.version.get()
    id("com.squareup.wire")
}

val localProperties: File = rootProject.file("local.properties")
val properties = Properties()
properties.load(FileInputStream(localProperties))

android {
    namespace = "dev.brunohensel.app"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "LOCAL_HOST",
            value = properties.getProperty("LOCAL_HOST")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeCompiler {
        enableStrongSkippingMode = true
    }
}

wire {
    sourcePath {
        srcProject(":proto")
    }

    kotlin {
        out = "$rootDir/app/src/main/java/proto"
        rpcCallStyle = "suspending"
        rpcRole = "client"
        singleMethodServices = false
    }
}

dependencies {
    implementation(androidx.activity.activityCompose)
    implementation(androidx.core.coreKtx)
    implementation(androidx.lifecycle.lifecycleRuntime)
    implementation(androidx.compose.composeBom)
    implementation(androidx.composeUi.ui)
    implementation(androidx.composeUi.uiToolingPreview)
    implementation(androidx.composeMaterial3.material3)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.coil.svg)

    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(project(":proto"))
    implementation(libs.wire.grpc.client)
}
