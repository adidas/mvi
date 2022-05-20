import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = "com.adidas.mvi"
version = project.findProperty("version") ?: "DEBUG"

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
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/adidas/mvi-android")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("GitHubPackages") {
            from(components["kotlin"])
        }
    }
}
