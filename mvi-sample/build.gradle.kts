import com.android.build.gradle.AppExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.api.BaseVariant

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"
    }

    namespace = "com.adidas.mvi.sample"

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }

    extensions.findByType<AppExtension>()?.let {
        project.extensions.configure(AppExtension::class.java) {
            applyKSP(this@configure, applicationVariants)
        }
    }

    android.testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

private fun <T : BaseVariant> applyKSP(
    extension: TestedExtension,
    variants: DomainObjectSet<T>,
) {
    variants.all {
        extension.sourceSets {
            getByName(this@all.name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.activityCompose)
    implementation(libs.ui)
    implementation(libs.material)

    implementation(libs.mvi)
    implementation(libs.mviCompose)

    implementation(libs.koinCore)
    implementation(libs.koinAndroid)
    implementation(libs.koinAnnotations)
    implementation(libs.koinCompose)
    ksp(libs.koinKspCompiler)

    testImplementation(libs.kotestRunner)
    testImplementation(libs.mockk)
    testImplementation(libs.mviKotest)
}