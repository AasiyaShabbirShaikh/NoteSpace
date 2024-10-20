buildscript {
    repositories {
        // Make sure that you have the following two repositories
        google()  // Google's Maven repository

        mavenCentral()  // Maven Central repository

    }
    dependencies {
        // Add the dependency for the Google services Gradle plugin
//        classpath 'com.google.gms:google-services:4.3.15'

//        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.5'
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.5")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "7.4.2" apply false
    id ("com.android.library") version "7.4.2" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id ("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
//    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}