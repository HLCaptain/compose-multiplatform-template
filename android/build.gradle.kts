plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.google.ksp)
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

android {
    namespace = "com.example.demo"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.demo"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    applicationVariants.all {
        val variantName = name
        sourceSets {
            getByName("main") {
                java.srcDir(File("build/generated/ksp/$variantName/kotlin"))
            }
        }
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    ksp(libs.koin.ksp)
}

tasks.register("BuildAndRun") {
    doFirst {
        exec {
            workingDir(projectDir.parentFile)
            commandLine("./gradlew", "android:build")
            commandLine("./gradlew", "android:installDebug")
        }
    }
}
