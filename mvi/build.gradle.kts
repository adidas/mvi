import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.kotest)
    alias(libs.plugins.atomicfu)
}

kotlin {
    explicitApi()

    jvm {
        withJava()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()
    watchosX64()
    tvosArm64()
    tvosSimulatorArm64()
    tvosX64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutinesCore)
        }

        commonTest.dependencies {
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.assertions)
            implementation(libs.coroutinesTest)
        }

        jvmTest.dependencies {
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
