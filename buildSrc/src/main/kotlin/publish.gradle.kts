import com.android.build.gradle.LibraryExtension
import java.io.FileInputStream
import java.util.*

/**
 * The following plugin tasks care of setting up:
 * - Publishing to github packages
 * - Dokka for documentation
 * - sourceJar for attaching sources to publications
 *
 * To use it just apply:
 *
 * plugins {
 *     publish
 * }
 *
 * To your build.gradle.kts.
 *
 * If you copy over this file in your project, make sure to copy it inside: buildSrc/src/main/kotlin/publish.gradle.kts.
 * Make sure to copy over also buildSrc/build.gradle.kts otherwise this plugin will fail to compile due to missing dependencies.
 */

plugins {
    id("maven-publish")
//    id("org.jetbrains.dokka")
}

group = "tech.dojo.pay"

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    group = "build"
    description = "Source jar"
    archiveClassifier.set("sources")
    if (plugins.hasPlugin("com.android.library")) {
        from((project.extensions.getByName("android") as LibraryExtension).sourceSets.named("main").get().java.srcDirs)
    } else {
        from((project.extensions.getByName("sourceSets") as SourceSetContainer).named("main").get().allSource)
    }
}
//
// val dokkaJar = tasks.create<Jar>("dokkaJar") {
//    group = JavaBasePlugin.DOCUMENTATION_GROUP
//    description = "Javadoc jar from Analytics"
//    archiveClassifier.set("javadoc")
// //    from(tasks.dokkaJavadoc)
// //    dependsOn(tasks.dokkaJavadoc)
// }

// tasks.dokkaJavadoc.configure {
//    outputDirectory.set(buildDir.resolve("javadoc"))
// }

/**Create credentials.properties in root project folder file with gpr.user=GITHUB_USER_ID  & gpr.key=PERSONAL_ACCESS_TOKEN**/
val credentialProperties = Properties()
credentialProperties.load(FileInputStream(rootProject.file("credentials.properties")))

afterEvaluate {

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Dojo-Engineering/android-dojo-pay-sdk")
                credentials {
                    username = credentialProperties["gpr.user"] as String
                    password = credentialProperties["gpr.key"] as String
                }
            }
        }

        publications {
            create<MavenPublication>("release") {
                if (plugins.hasPlugin("com.android.library")) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }

                artifact(sourcesJar)
//                artifact(dokkaJar)

                pom {
                    if (!"USE_SNAPSHOT".byProperty.isNullOrBlank()) {
                        version = "$version-SNAPSHOT"
                    }
                    description.set("Core project for Android libraries")
                }
            }
        }
    }
}

val String.byProperty: String? get() = findProperty(this) as? String
