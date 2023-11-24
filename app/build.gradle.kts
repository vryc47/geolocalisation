plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.geolocalisation"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.geolocalisation"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("org.osmdroid:osmdroid-android:6.1.17")
    implementation("com.google.maps.android:android-maps-utils:3.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.larswerkman:HoloColorPicker:1.5")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}