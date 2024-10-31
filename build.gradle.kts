// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("com.android.tools.build:gradle:7.4.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.1")
    }
}
plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        android = false
        debug = false
        verbose = true
        version = "0.49.1"
        ignoreFailures = false
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = rootProject.files("configs/detekt/detekt.yml")
        reports {
            xml.enabled = true
            html.enabled = true
            txt.enabled = true
            sarif.enabled = true
        }
    }
}

tasks {
    register("clean", Delete::class.java) {
        delete(rootProject.layout.buildDirectory)
    }
}
