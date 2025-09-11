plugins {
    `kotlin-dsl`
}
repositories {
    google()
    gradlePluginPortal()
}

object Plugins {
    const val AGP = "8.13.0"
    const val KOTLIN = "2.1.21"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Plugins.KOTLIN}")
    implementation("com.android.tools.build:gradle:${Plugins.AGP}")
}
