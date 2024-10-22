import java.io.FileInputStream
import java.util.Properties
plugins {
    id("com.android.application")
    kotlin("android")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "tech.dojo.pay.sdksample"
    defaultConfig {
        compileSdk = Sdk.COMPILE_SDK_VERSION
        applicationId = "tech.dojo.pay.sdksample"
        minSdk = Sdk.MIN_SDK_VERSION
        targetSdk = Sdk.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {}

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_VERSION
    }
}

dependencies {
    implementation(project(":sdk"))
    implementation(project(":uisdk"))
    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.Activity.ACTIVITY)
    implementation(AndroidX.APPCOMPAT)
    implementation(Material.MATERIAL)
    implementation(Networking.OKHTTP)
    implementation(Networking.RETROFIT_CORE) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
    implementation(Networking.CONVERTER_GSON) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
    implementation(AndroidX.ConstraintLayout.CONSTRAINT_LAYOUT)
    implementation(AndroidX.Compose.ui)
    implementation(AndroidX.Compose.runtime)
    implementation(AndroidX.Compose.material)
    implementation(AndroidX.Activity.ACTIVITY_COMPOSE)
    testImplementation(TestingLib.JUNIT)
    androidTestImplementation(AndroidX.AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidX.AndroidTestingLib.ESPRESSO_CORE)
    androidTestImplementation(AndroidX.Compose.compiler)
}
