import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.1.20"
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
//        iosX64(),
//        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    jvm()

//    js(IR) {
//        useCommonJs()
//        browser()
//    }

    sourceSets {

        commonMain {
            dependencies {
                implementation(compose.material)
                implementation(compose.components.resources)
                //koin
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
//              implementation(libs.koin.compose.viewmodel.navigation)

                api(libs.ktor.client.core)
                api(libs.ktor.serialization.json)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.client.logging)
                api(libs.atomicfu)
            }
        }

        androidMain {
            dependencies {
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.android)

                api(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        jvmMain.dependencies {
            api(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.wlj.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    dependencies {
        debugImplementation(libs.loginterceptor)
    }
}
