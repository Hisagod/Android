plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply { from("${rootDir}/module.gradle") }

android {
    namespace = "com.example.webp.lib"

//    compileSdk = 33
//
//    defaultConfig {
//        minSdk = 16
//        targetSdk = 33
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    compileOnly("io.coil-kt:coil:2.5.0")
//    implementation ("androidx.vectordrawable:vectordrawable-animated:1.1.0")
}
