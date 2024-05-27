// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:7.4.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        extra.apply {
            set("room_version", "2.5.0")
            set("activity_version", "1.1.0")
            set("appcompat_version", "1.6.0")
            set("constraintlayout_version", "2.1.4")
            set("core_ktx_version", "1.9.0")
            set("coroutines_version", "1.4.2")
            set("kotlin_version", "1.8.0")
            set("lifecycle_version", "2.5.1")
            set("material_version", "1.8.0")
            set("nav_version", "2.5.3")
            set("room_version", "2.5.0")
        }
    }
    repositories {
        google()
        mavenCentral()
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}