import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(17)
    jvm {
        compilations.all {
            kotlinOptions.freeCompilerArgs += "-Xjvm-default=all" // allow MagicMap to handle default methods
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasm {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("reflect"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api("org.slf4j:slf4j-api:2.0.6")
                api("com.google.guava:guava:32.1.1-jre")
                api("com.github.ben-manes.caffeine:caffeine:3.1.5")
            }
        }
        val jvmTest by getting
//        val wasmMain by getting {
//            dependencies {
//                implementation(npm("@js-joda/timezone", "2.3.0"))
//            }
//        }
    }
}

// Use a proper version of webpack, TODO remove after updating to Kotlin 1.9.
rootProject.the<NodeJsRootExtension>().versions.webpack.version = "5.76.2"
