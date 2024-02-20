
plugins {
    id("com.android.application")
    
}

android {
    namespace = "com.smedia.app"
    compileSdk = 33
    
    useLibrary("org.apache.http.legacy")
    
    
    defaultConfig {
        applicationId = "com.smedia.app"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        
    }
    
}

dependencies {


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:19.0.0")
    implementation("com.google.firebase:firebase-database:19.0.0")
    implementation("com.google.firebase:firebase-storage:19.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.code.gson:gson:2.8.7")
    
}
