version = "1.6.2"

plugins {
    id("com.android.library")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    publish
}

android {
    namespace = "tech.dojo.pay.uisdk"

    defaultConfig {
        compileSdk = Sdk.COMPILE_SDK_VERSION
        minSdk = Sdk.MIN_SDK_VERSION
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_VERSION
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",

        )
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    buildFeatures {
        buildConfig = true
    }
}
dependencies {
    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.Activity.ACTIVITY)
    implementation(AndroidX.Fragment.FRAGMENT)
    implementation(AndroidX.APPCOMPAT)
    implementation(AndroidX.Lifecycle.VIEWMODEL)
    implementation(AndroidX.Lifecycle.VIEWMODEL_COMPOSE)
    implementation(Coroutines.COROUTINES_CORE)
    implementation(Material.MATERIAL)
    implementation(Navigation.NavigationFragment)
    implementation(Navigation.NavigationCompose)
    implementation(Navigation.NavigationUI)
    implementation(AndroidX.Activity.ACTIVITY_COMPOSE)
    implementation(AndroidX.Compose.compiler)
    implementation(AndroidX.Compose.animation)
    implementation(AndroidX.Compose.foundation)
    implementation(AndroidX.Compose.layout)
    implementation(AndroidX.Compose.ui)
    implementation(AndroidX.Compose.uiUtil)
    implementation(AndroidX.Compose.runtime)
    implementation(AndroidX.Compose.material)
    implementation(AndroidX.Compose.iconsExtended)
    implementation(AndroidX.Compose.livedata)
    implementation(AndroidX.Compose.tooling)
    implementation(AndroidX.Logging.TIMBER)
    implementation(AndroidX.Network.GSON)
    implementation(project(":sdk"))
    implementation(AndroidX.WINDOW)
    testImplementation(TestingLib.JUNIT)
    implementation(CsvReader.csvReader)
    testImplementation(MOCKITO.MOCKITO_KOTLIN)
    testImplementation(MOCKITO.MOCKITO_INLINE)
    testImplementation(Coroutines.COROUTINES_Test)
    testImplementation(AndroidX.CORE_TESTING_ARCH)
    testImplementation(TestingLib.MOCKK)
    androidTestImplementation(AndroidX.AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidX.AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidX.AndroidTestingLib.ESPRESSO_CORE)
}
