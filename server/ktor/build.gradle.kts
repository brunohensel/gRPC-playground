plugins {
    application
    kotlin("jvm")
    id("io.ktor.plugin") version libs.versions.ktor.version.get()
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin.version.get()
}

group = "dev.brunohensel"
version = "0.0.1"

application {
    mainClass.set("dev.brunohensel.ktor.KtorApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.logback.classic)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
}

tasks.test {
    useJUnitPlatform()
}
