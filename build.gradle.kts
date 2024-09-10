import com.vanniktech.maven.publish.SonatypeHost

buildscript {

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.mavenPublish)
}

configure(subprojects) {

    pluginManager.withPlugin("com.vanniktech.maven.publish") {
        mavenPublishing {
            publishToMavenCentral(SonatypeHost.S01)
            signAllPublications()
        }
    }
}