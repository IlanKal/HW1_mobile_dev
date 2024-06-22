plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.IlanKal.hw1_mobile_dev"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.IlanKal.hw1_mobile_dev"
        minSdk = 26
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Use 11 or the version you're targeting
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //WorkManager
    implementation(libs.work.runtime)

    //Glide
    implementation(libs.glide)

}