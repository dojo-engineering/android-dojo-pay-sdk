plugins {
    `kotlin-dsl`
}
repositories {
    google()
    gradlePluginPortal()
}

object Plugins {
    const val AGP = "8.6.1"
    const val KOTLIN = "1.9.0"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Plugins.KOTLIN}")
    implementation("com.android.tools.build:gradle:${Plugins.AGP}")
}
