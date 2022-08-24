import org.jetbrains.kotlin.com.google.gson.Gson

object Sdk {

    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 31
    const val COMPILE_SDK_VERSION = 31
}

object Versions {
    const val ACTIVITY = "1.3.1"
    const val FRAGMENT = "1.4.1"
    const val ACTIVITY_COMPOSE = "1.3.0-beta01"
    const val VIEWMODEL_COMPOSE = "1.0.0-alpha04"
    const val VIEWMODEL = "  2.4.1"
    const val VIEWBINDING = "4.2.1"
    const val ANDROIDX_TEST_EXT = "1.1.3"
    const val ANDROIDX_TEST = "1.4.0"
    const val APPCOMPAT = "1.4.1"
    const val CONSTRAINT_LAYOUT = "2.1.0"
    const val CONSTRAINT_LAYOUT_COMPOSE = "1.0.0-alpha07"
    const val CORE_KTX = "1.7.0"
    const val ESPRESSO_CORE = "3.4.0"
    const val JUNIT = "4.13.2"
    const val KTLINT = "0.43.2"
    const val COMPOSE_VERSION = "1.1.0"
    const val KOTLIN = "1.7.0"
    const val ACCOMPANIST = "0.23.0"
    const val COROUTINES = "1.6.1"
    const val MATERIAL = "1.5.0"
    const val DOJO_PAY_CORE = "1.2.0"
    const val MOCKITO = "4.0.0"
    const val MOCKITO_IN_LINE = "4.3.1"
    const val NAVIGATION = "2.3.3"
    const val COMPOSE_NAVIGATION = "2.4.2"
    const val COMPOSE_NAVIGATION_ANIMATION = "0.23.1"
    const val TIMBER = "5.0.1"
    const val GSON = "2.8.7"
}

object BuildPluginsVersion {

    const val DETEKT = "1.17.1"
    const val KTLINT = "10.2.0"
    const val VERSIONS_PLUGIN = "0.39.0"
}

object Accompanist {
    const val insets = "com.google.accompanist:accompanist-insets:${Versions.ACCOMPANIST}"
    const val systemuicontroller =
        "com.google.accompanist:accompanist-systemuicontroller:${Versions.ACCOMPANIST}"
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

object DojoPayCore {
    const val DOJO_PAY_CORE = "tech.dojo.pay:sdk:${Versions.DOJO_PAY_CORE}"
}

object Kotlin {

    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
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
    const val NavigationAnimationCompose =
        "com.google.accompanist:accompanist-navigation-animation:${Versions.COMPOSE_NAVIGATION_ANIMATION}"

}

object AndroidX {

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val VIEWBINDING = "androidx.databinding:viewbinding:${Versions.VIEWBINDING}"
    const val CORE_TESTING_ARCH = "androidx.arch.core:core-testing:2.1.0"

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
        const val iconsExtended =
            "androidx.compose.material:material-icons-extended:${Versions.COMPOSE_VERSION}"

        const val animation = "androidx.compose.animation:animation:${Versions.COMPOSE_VERSION}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}"
        const val uiTest = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_VERSION}"

        // Integration with observables
        const val livedata = "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}"

        // Compiler
        const val compiler = "androidx.compose.compiler:compiler:${Versions.COMPOSE_VERSION}"
    }

    object ConstraintLayout {

        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
        const val CONSTRAINT_LAYOUT_COMPOSE =
            "androidx.constraintlayout:constraintlayout-compose:${Versions.CONSTRAINT_LAYOUT_COMPOSE}"
    }

    object Network {
        const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    }

    object AndroidTestingLib {

        const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
        const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
        const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
    }

    object Logging {
        const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    }


}

object TestingLib {

    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}