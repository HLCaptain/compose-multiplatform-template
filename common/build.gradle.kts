import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    alias(libs.plugins.google.ksp)
}

group = "com.example"
version = "1.0-SNAPSHOT"

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    jvmToolchain(17)
    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
    js(IR) {
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.materialIconsExtended)
                api(compose.material3)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottomSheetNavigator)
                implementation(libs.voyager.tabNavigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)
                implementation(libs.ktor.core)
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.napier)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.androidx.appcompat)
                api(libs.androidx.core)
                implementation(libs.ktor.jvm)
                implementation(libs.voyager.androidx)
                implementation(libs.koin.ktor)
                implementation(libs.koin.logger.slf4j)
            }
        }

        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                implementation(libs.ktor.jvm)
                implementation(libs.koin.ktor)
                implementation(libs.koin.logger.slf4j)
            }
        }

        val desktopTest by getting

        val jsMain by getting {
            dependencies {
                api(compose.html.core)
                implementation(libs.ktor.js)
                implementation(libs.ktor.jsonjs)
            }
        }

        val jsTest by getting
    }
}

android {
    namespace = "com.example.demo"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
//    add("kspCommonMainMetadata", libs.koin.ksp)
    // DO NOT add bellow dependencies
//    add("kspAndroid", Deps.Koin.kspCompiler)
//    add("kspIosX64", Deps.Koin.kspCompiler)
//    add("kspIosArm64", Deps.Koin.kspCompiler)
//    add("kspIosSimulatorArm64", Deps.Koin.kspCompiler)
    // FIXME: I DID ADD THEM
    add("kspAndroid", libs.koin.ksp)
//    add("kspIosX64", libs.koin.ksp)
//    add("kspIosArm64", libs.koin.ksp)
//    add("kspIosSimulatorArm64", libs.koin.ksp)
    add("kspDesktop", libs.koin.ksp)
}

// WORKAROUND: ADD this dependsOn("kspCommonMainKotlinMetadata") instead of above dependencies
tasks.withType<KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
afterEvaluate {
    tasks.filter {
        it.name.contains("SourcesJar", true)
    }.forEach {
        println("SourceJarTask====>${it.name}")
        it.dependsOn("kspCommonMainKotlinMetadata")
    }
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}
