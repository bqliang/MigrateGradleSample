val appVersionCode: Int by rootProject.extra
val appVersionName: String by rootProject.extra

@Suppress("DSL_SCOPE_VIOLATION") // https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.android.application)
    kotlin("android") // alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    kotlin("plugin.serialization") // alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.secrets) // Secrets Gradle Plugin for Android: https://github.com/google/secrets-gradle-plugin
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

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
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
        getByName("debug") {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
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
    implementation(libs.kotlinX.serialization.json)
    kapt(libs.androidX.room.compiler)
    implementation(libs.bundles.androidX.room)
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)
    implementation(libs.google.material)
    implementation(libs.androidX.constraintLayout)
}