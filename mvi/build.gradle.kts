import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.ktlint)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    explicitApi()
}

val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    freeCompilerArgs += "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
}

repositories {
    mavenCentral()
}

configure<KtlintExtension> {
    version.set(libs.versions.ktlint.get())
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.coroutines.core)

    testImplementation(libs.kotest.runner)
    testImplementation(libs.coroutines.test)
}
