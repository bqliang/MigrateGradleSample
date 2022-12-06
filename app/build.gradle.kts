val appVersionCode: Int by rootProject.extra
val appVersionName: String by rootProject.extra

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.bqliang.migrate.gradle.sample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.bqliang.migrate.gradle.sample"
        minSdk = 26
        targetSdk = 33
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        signingConfigs.create("release") {
            storeFile = file("../key.jks")
            storePassword = System.getenv("KEY_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName(name)
        }
        /* 第二种写法
        all {
            signingConfig = if (name == "release") {
                signingConfigs.create("release") {
                    storeFile = file("../key.jks")
                    storePassword = System.getenv("KEY_STORE_PASSWORD")
                    keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                    keyPassword = System.getenv("KEY_PASSWORD")
                }
            } else { signingConfigs.getByName("debug") }
        }
        */
    }

    applicationVariants.configureEach {
        outputs.configureEach {
            (this as? com.android.build.gradle.internal.api.ApkVariantOutputImpl)?.outputFileName =
                "${rootProject.name}-${versionName}-${name/* variant name */}.apk"
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
        viewBinding = true
    }
    sourceSets.getByName("main").res {
        val srcDirs = listOf(
            "settings"
        )
            .map { "src/main/java/com/bqliang/migrate/gradle/sample/$it/res" }
            .plus("src/main/res")
        setSrcDirs(srcDirs)
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}