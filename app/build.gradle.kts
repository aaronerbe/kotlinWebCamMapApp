plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.0"
    // per Google Maps Tutorial
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.kotlinwebcammapapp"
    //TODO  Possibly need to make 34 per google tutorial.  leaving as is for now
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kotlinwebcammapapp"
        minSdk = 24
        //TODO  Possibly need to make 34 per google tutorial.  leaving as is for now
        targetSdk = 35
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
//for location
    implementation(libs.play.services.location)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //core ktor dependency
    implementation(libs.ktor.client.core)
    //engine dependency (CIO) which handles processing network requests
    implementation(libs.ktor.client.cio)
    //logging plugin
    implementation(libs.ktor.client.logging)
    //content negotiation and serialization.  to handle json response
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
//    //for logging.  removes console warnings about logging to enable this.
//    implementation("ch.qos.logback:logback-classic:1.4.12")

    // Per Google Maps Tutorial
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:19.0.0")
}

// Per Google Maps Tutorial
secrets {
    // To add your Maps API key to this project:
    // 1. If the secrets.properties file does not exist, create it in the same folder as the local.properties file.
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}