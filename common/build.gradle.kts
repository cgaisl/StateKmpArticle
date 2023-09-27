@file:Suppress("UnstableApiUsage")


plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("co.touchlab.skie") version "0.4.20"
}

// cocoapods requires version
version = "1.0-SNAPSHOT"

kotlin {
    iosArm64()
    iosSimulatorArm64()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries {
            framework {
                baseName = "common"
                isStatic = true
            }
        }
    }

    androidTarget()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                api("com.rickclephas.kmm:kmm-viewmodel-core:1.0.0-ALPHA-14")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
                implementation ("app.cash.turbine:turbine:1.0.0")
            }
        }
    }
}

android {
    namespace = "at.cgaisl.common"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}

skie {
    analytics {
        disableUpload.set(true)
    }
}