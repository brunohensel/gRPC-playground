import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    application
    kotlin("jvm")
    id("com.squareup.wire")
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin.version.get()
}
group = "dev.brunohensel"
version = "0.0.1"

application {
    mainClass.set("dev.brunohensel.misk.MiskApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjvm-default=all", "-progressive")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

wire {
    sourcePath {
        srcProject(":proto")
    }

    kotlin {
        out = "$rootDir/server/misk/src/main/kotlin/protos"
        rpcCallStyle = "blocking"
        rpcRole = "server"
        singleMethodServices = true
    }
}

dependencies {
    implementation(project(":proto"))
    implementation(libs.wire.runtime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.misk)
    implementation(libs.logback.classic)
}
