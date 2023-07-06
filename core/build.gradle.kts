import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    wasm {
        moduleName = "compose-library-wasm"
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    open = mapOf(
                        "app" to mapOf(
                            "name" to "google-chrome",
                            "arguments" to listOf("--js-flags=--experimental-wasm-gc ")
                        )
                    ),
                    static = (devServer?.static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/common/")
                        add(project.rootDir.path + "/nonAndroidMain/")
                        add(project.rootDir.path + "/web/")
                    },
                )
            }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("reflect"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-api:1.7.36")
                implementation("com.google.guava:guava:32.1.1-jre")
            }
        }
        val jvmTest by getting
    }
}

configurations.all {
    val conf = this
    resolutionStrategy.eachDependency {
        val isWasm = conf.name.contains("wasm", true)
        val isJs = conf.name.contains("js", true)
        val isComposeGroup = requested.module.group.startsWith("org.jetbrains.compose")
        val isComposeCompiler = requested.module.group.startsWith("org.jetbrains.compose.compiler")
        if (isComposeGroup && !isComposeCompiler && !isWasm && !isJs) {
            useVersion("1.4.0")
        }
    }
}

// Use a proper version of webpack, TODO remove after updating to Kotlin 1.9.
rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.76.2"
