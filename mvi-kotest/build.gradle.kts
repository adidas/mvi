import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.ktlint)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    explicitApi()
}

configure<KtlintExtension> {
    version.set(libs.versions.ktlint.lib.get())
}

dependencies {
    implementation(libs.kotestRunner)
    implementation(libs.mvi)
}
