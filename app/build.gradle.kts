plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.sergiosabater.rickmortypedia"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.sergiosabater.rickmortypedia"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.bundles.androidx.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.datastore)
    implementation(libs.bundles.kotlinx.serialization)
    testImplementation(libs.bundles.unit.testing)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.testing.android)
    debugImplementation(libs.bundles.debug.compose)
}