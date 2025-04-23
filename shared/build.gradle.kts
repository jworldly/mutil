import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.hot.reload)
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
                implementation(compose.material3)
                implementation(compose.components.resources)
                //koin
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                api(libs.koin.compose)
                api(libs.koin.compose.viewmodel)
//                api(libs.koin.compose.viewmodel.navigation)
                // Koin Annotations
                api(libs.koin.annotations)//annotations
//                ksp(libs.koin.ksp.compiler)

                //ktor
                api(libs.ktor.client.core)
                api(libs.ktor.serialization.json)
                api(libs.ktor.client.content.negotiation)
                api(libs.ktor.client.logging)

                api(libs.atomicfu)


                api(libs.settings.coroutines)
//                api(libs.settings.serialization)//FlowSettings暂不支持
            }
        }

        androidMain {
            dependencies {
                api(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.android)
                api(libs.ktor.client.okhttp)
                //kv
                api(libs.androidx.datastore.core)
                api(libs.settings.datastore)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
                //kv
                api(libs.settings.datastore)
                api(libs.androidx.datastore.core)
            }
        }

        jvmMain.dependencies {
            api(libs.ktor.client.okhttp)
            //kv
            api(libs.settings.datastore)
            api(libs.androidx.datastore.core)
        }
        wasmJsMain.dependencies {
            api(libs.settings)
            api(libs.settings.make.observable )
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
