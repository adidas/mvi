plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_21.toString()
    }

    buildFeatures {
        compose = true
    }

    android.testOptions {
        unitTests.all {
            it.useJUnitPlatform()
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
    implementation(project(":mvi-compose"))

    implementation(libs.koinCore)
    implementation(libs.koinAndroid)
    implementation(libs.koinAnnotations)
    implementation(libs.koinCompose)
    ksp(libs.koinKspCompiler)

    testImplementation(libs.kotestRunner)
    testImplementation(libs.coroutinesTest)
    testImplementation(project(":mvi-kotest"))
}
