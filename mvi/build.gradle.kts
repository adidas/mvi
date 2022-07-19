import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = "com.adidas"
version = project.findProperty("version")?.toString()?.replace("v", "") ?: "DEBUG"

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

lateinit var sourcesArtifact: PublishArtifact

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allJava.srcDirs)
    }

    artifacts {
        sourcesArtifact = archives(sourcesJar)
    }
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
            url = uri("https://maven.pkg.github.com/adidas/mvi")
            credentials {
                username = project.findProperty("username").toString()
                password = project.findProperty("password").toString()
            }
        }
    }
    publications {
        register<MavenPublication>("release") {
            from(components["kotlin"])
            artifact(sourcesArtifact)
        }
    }
}
