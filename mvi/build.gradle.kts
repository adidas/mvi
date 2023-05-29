import org.jlleitschuh.gradle.ktlint.KtlintExtension
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

plugins {
    kotlin("jvm") version "1.6.21"
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
