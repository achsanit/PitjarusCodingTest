plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.achsanit.pitjaruscodingtest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.achsanit.pitjaruscodingtest"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        val baseApiUrl: String by project

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String","BASE_API_URL", baseApiUrl)
        }
        debug {
            buildConfigField("String","BASE_API_URL", baseApiUrl)
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.vmadalin:easypermissions-ktx:1.0.0")

    val nav_version = "2.6.0"
    //navigation component
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    val lifecycle_version = "2.6.2"
    // ViewModel LiveData (lifecycle)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    val retrofit_version = "2.9.0"
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    val koin_version = "3.5.0"
    // koin for DI
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-android:$koin_version")

    val chucker_version = "3.5.2"
    // chucker for dev logging
    implementation("com.github.chuckerteam.chucker:library:$chucker_version")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:$chucker_version")

    // rxjava
    implementation("io.reactivex.rxjava3:rxjava:3.1.1")
    implementation("com.jakewharton.rxbinding2:rxbinding:2.0.0")

    val room_version = "2.5.1"
    // room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // circle avatar
    implementation("de.hdodenhof:circleimageview:3.1.0")

    val camerax_version = "1.2.2"
    // camerax
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")

    // timber
    implementation("com.jakewharton.timber:timber:4.7.1")
}