plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mobile_parking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mobile_parking"
        minSdk = 24
        targetSdk = 33 // Downgrade to 33 for Robolectric compatibility
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

    testOptions {
        unitTests.isIncludeAndroidResources = true // Ensures Robolectric can load resources
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("org.robolectric:robolectric:4.10.3") // Use a compatible version
    testImplementation("org.robolectric:android-all:13-robolectric-9030017") // Match your `targetSdkVersion`

    // Android Instrumented Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Mocking for tests
    testImplementation("org.mockito:mockito-core:5.5.0")
    androidTestImplementation("org.mockito:mockito-android:5.5.0")
}
