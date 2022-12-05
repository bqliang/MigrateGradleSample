// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
    extra.apply {
        val gitCommitId = ProcessBuilder("git", "rev-parse", "--short", "HEAD").start().inputStream.bufferedReader().readText().trim()
        val gitCommitCount = ProcessBuilder("git", "rev-list", "--count", "HEAD").start().inputStream.bufferedReader().readText().trim().toInt()
        val gitTag = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start().inputStream.bufferedReader().readText().trim().ifEmpty { "v0.0.0" }
        set("appVersionCode", gitCommitCount)
        set("appVersionName", "${gitTag}.r${gitCommitCount}.${gitCommitId}")
    }
}