

object Sdk {
    const val MIN_SDK_VERSION = 26
    const val TARGET_SDK_VERSION = 36
    const val COMPILE_SDK_VERSION = 36
}

object Versions {
    const val ACTIVITY = "1.11.0"
    const val FRAGMENT = "1.8.9"
    const val ACTIVITY_COMPOSE = "1.11.0"
    const val VIEWMODEL_COMPOSE = "2.9.3"
    const val VIEWMODEL = "2.9.3"
    const val ANDROIDX_TEST_EXT = "1.3.0"
    const val ANDROIDX_TEST = "1.7.0"
    const val APPCOMPAT = "1.7.1"
    const val CONSTRAINT_LAYOUT = "2.2.1"
    const val CORE_KTX = "1.17.0"
    const val ESPRESSO_CORE = "3.7.0"
    const val JUNIT = "4.13.2"
    const val COMPOSE_VERSION = "1.9.1"
    const val COMPOSE_ANIMATION_VERSION = "1.9.1"
    const val KOTLIN = "2.1.21"
    const val COROUTINES = "1.10.2"
    const val MATERIAL = "1.13.0"
    const val MOCKITO = "4.1.0"
    const val MOCKITO_IN_LINE = "4.11.0"
    const val NAVIGATION = "2.9.3"
    const val COMPOSE_NAVIGATION = "2.9.4"
    const val TIMBER = "5.0.1"
    const val GSON = "2.13.1"
    const val csvReader = "1.10.0"
    const val ANDROIDX_WINDOW = "1.4.0"
    const val RETROFIT = "2.12.0"
    const val OKHTTP = "4.12.0"
    const val GPAY = "19.4.0"
    const val JSON_TEST = "20231013"
    const val CARDINAL = "2.2.7-5"
    const val MOCKK = "1.14.5"
}


object MOCKITO {
    const val MOCKITO_KOTLIN = "org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO}"
    const val MOCKITO_INLINE = "org.mockito:mockito-inline:${Versions.MOCKITO_IN_LINE}"
}

object Coroutines {
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_Test =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
}


object Kotlin {
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:${Versions.KOTLIN}"
}

object Material {

    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
}

object Navigation {
    const val NavigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NavigationUI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val NavigationCompose =
        "androidx.navigation:navigation-compose:${Versions.COMPOSE_NAVIGATION}"

}
object CsvReader {
    const val csvReader = "com.github.doyaaaaaken:kotlin-csv-jvm:${Versions.csvReader}"
}
object AndroidX {

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val CORE_TESTING_ARCH = "androidx.arch.core:core-testing:2.1.0"
    const val WINDOW = "androidx.window:window:${Versions.ANDROIDX_WINDOW}"

    object Activity {

        // Compose Integration with activities
        const val ACTIVITY_COMPOSE =
            "androidx.activity:activity-compose:${Versions.ACTIVITY_COMPOSE}"

        // Activity ktx
        const val ACTIVITY = "androidx.activity:activity-ktx:${Versions.ACTIVITY}"
    }

    object Fragment {

        // Compose Integration with activities
        const val FRAGMENT = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
    }

    object Lifecycle {

        // Compose Integration with ViewModels
        const val VIEWMODEL_COMPOSE =
            "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.VIEWMODEL_COMPOSE}"

        const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.VIEWMODEL}"
    }

    object Compose {

        // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
        const val foundation = "androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}"
        const val layout =
            "androidx.compose.foundation:foundation-layout:${Versions.COMPOSE_VERSION}"
        const val ui = "androidx.compose.ui:ui:${Versions.COMPOSE_VERSION}"
        const val uiUtil = "androidx.compose.ui:ui-util:${Versions.COMPOSE_VERSION}"
        const val runtime = "androidx.compose.runtime:runtime:${Versions.COMPOSE_VERSION}"

        // Material Design
        const val material = "androidx.compose.material:material:${Versions.COMPOSE_VERSION}"
        const val iconsExtended = "androidx.compose.material:material-icons-extended:1.7.8"

        const val animation = "androidx.compose.animation:animation:${Versions.COMPOSE_ANIMATION_VERSION}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}"

        // Integration with observables
        const val livedata = "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}"
    }

    object ConstraintLayout {

        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    }

    object Network {
        const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    }

    object AndroidTestingLib {

        const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    }

    object Logging {
        const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    }
}
object Networking {
    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val RETROFIT_CORE = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val CONVERTER_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val CONVERTER_SCALARS = "com.squareup.retrofit2:converter-scalars:${Versions.RETROFIT}"
}

object Wallet {
    const val GPAY = "com.google.android.gms:play-services-wallet:${Versions.GPAY}"
}

object Threeds {
    const val Cardinal = "org.jfrog.cardinalcommerce.gradle:cardinalmobilesdk:${Versions.CARDINAL}"
}
object TestingLib {

    const val JUNIT = "junit:junit:${Versions.JUNIT}"

    const val JSON_TEST = "org.json:json:${Versions.JSON_TEST}"

    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"
}
