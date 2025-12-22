import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.atomicfu)
    alias(libs.plugins.kotestMultiplatform)
    alias(libs.plugins.ksp)
}

kotlin {
    explicitApi()

    jvm()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64(),
        watchosArm32(),
        watchosArm64(),
        watchosDeviceArm64(),
        watchosSimulatorArm64(),
        watchosX64(),
        tvosArm64(),
        tvosSimulatorArm64(),
        tvosX64(),
    ).forEach {
        it.binaries.framework {
            baseName = "mvi"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
        }

        jvmTest.dependencies {
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.assertions)
            implementation(libs.coroutinesTest)
            implementation(libs.kotestRunner)
        }
    }
}

configure<KtlintExtension> {
    version.set(libs.versions.ktlint.lib.get())
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

configure<MavenPublishBaseExtension> {
    configure(KotlinMultiplatform(javadocJar = JavadocJar.Empty()))
}
