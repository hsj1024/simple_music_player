plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.2" apply false

}

android {
    namespace = "com.example.mp3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mp3"
        minSdk = 24
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

    implementation(platform(libs.androidx.compose.bom))  // BOM으로 통일
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material3만 사용 (Material 제거)
    // implementation("androidx.compose.material:material:1.3.1")

    // 기타 라이브러리
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.room:room-runtime:2.5.0")  // 최신 Room 버전으로 업데이트
    implementation("androidx.navigation:navigation-compose:2.7.0") // Compose Navigation 추가

    // Material3 라이브러리
    implementation("androidx.compose.material3:material3:1.1.0")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation ("androidx.compose.ui:ui-text-google-fonts:1.3.1")  // 폰트 관련 라이브러리

    implementation ("androidx.compose.foundation:foundation:1.4.0")  // Compose Foundation
    implementation ("androidx.compose.material3:material3:1.1.0")    // Material 3
    implementation ("androidx.compose.ui:ui:1.4.0")                  // Compose UI

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // 테스트 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
