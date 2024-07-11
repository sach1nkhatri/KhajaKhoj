plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleGmsGoogleServices)
    kotlin("plugin.serialization") version "1.8.22" // Use the latest version

}

android {
    buildFeatures{
        viewBinding = true
    }
    namespace = "com.example.khajakhoj"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.khajakhoj"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures{
        viewBinding=true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.location)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.ui.desktop)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.dotsindicator)


    implementation ("com.googlecode.libphonenumber:libphonenumber:8.13.11")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("com.google.android.gms:play-services-safetynet:18.1.0")
//15
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2") // or the latest version
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2") // or the latest version
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0") // Check for the latest version
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1") // Use the latest version

}




