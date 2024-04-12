plugins {
    id("com.android.application")
}

android {
    namespace = "com.bradburzon.a2dayslist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bradburzon.a2dayslist"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.10"

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

dependencies {
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.work:work-runtime:2.9.0")

    // Unit Testing
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("androidx.test.ext:junit:1.1.5")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    // Use mockito-inline if you need to mock final classes/methods
    testImplementation ("org.mockito:mockito-inline:3.7.7")

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test:rules:1.5.0")

    // Assertions
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    // Android Instrumented Tests
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    // UI
    implementation ("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    implementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation ("org.hamcrest:hamcrest:2.2")

}
