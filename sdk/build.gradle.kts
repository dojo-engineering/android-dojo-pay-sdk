version = "1.4.5"

plugins {
    id("com.android.library")
    kotlin("android")
    publish
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.Activity.ACTIVITY)
    implementation(AndroidX.Fragment.FRAGMENT)
    implementation(AndroidX.APPCOMPAT)
    implementation(Networking.RETROFIT_CORE)
    implementation(Networking.CONVERTER_GSON)
    implementation(Networking.CONVERTER_SCALARS)
    implementation(AndroidX.Lifecycle.VIEWMODEL)
    implementation(Material.MATERIAL)
    implementation(Wallet.GPAY)
//    implementation(Threeds.Cardinal)
    implementation(project(":threeds"))
    implementation(Coroutines.COROUTINES_CORE)
    testImplementation(TestingLib.JUNIT)
    testImplementation(MOCKITO.MOCKITO_KOTLIN)
    testImplementation(MOCKITO.MOCKITO_INLINE)
    testImplementation(Coroutines.COROUTINES_Test)
    testImplementation(AndroidX.CORE_TESTING_ARCH)
    testImplementation(TestingLib.MOCKK)
    androidTestImplementation(AndroidX.AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    testImplementation(TestingLib.JSON_TEST)
}
