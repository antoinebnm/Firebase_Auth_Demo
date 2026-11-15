plugins {
    alias(libs.plugins.android.application) // kotlin is built into agp 9
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services) // reads app/google-services.json
}

android {
    namespace = "fr.isep.campusauth"
    compileSdk = 37

    defaultConfig {
        applicationId = "fr.isep.campusauth"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    implementation(platform(libs.firebase.bom)) // bom picks the firebase versions
    implementation(libs.firebase.auth)
    implementation(libs.kotlinx.coroutines.play.services) // Task.await()
}
