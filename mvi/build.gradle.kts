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

configure<KtlintExtension> {
    version.set(libs.versions.ktlint.lib.get())
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.coroutinesCore)

    testImplementation(libs.kotestRunner)
    testImplementation(libs.coroutinesTest)
}
