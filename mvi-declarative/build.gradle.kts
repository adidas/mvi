import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = "com.adidas"
val versionFromTag = project.findProperty("version")?.takeIf { it != "unspecified" }?.toString()?.replace("v", "")
version = versionFromTag ?: "DEBUG"

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
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
    if (!versionFromTag.isNullOrEmpty()) {
        implementation("com.github.adidas:mvi:$versionFromTag")
    } else {
        implementation(project(":mvi"))
    }

    testImplementation(libs.kotest.runner)
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
