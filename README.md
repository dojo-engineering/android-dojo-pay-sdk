# Dojo Pay SDK Android 🤖

This project contains the SDK for payments. This will be used in our internal apps but also
will be used for external customers

## How to use

- The project has the ability to publish UI library specific for Paymentense and Dojo projects.
- The libraries will be published to company nexus repository.
- The library uses Semantic Version scheme as mentioned at https://semver.org/

## Features 🎨

- Publish to **Github packages**.
- Dependency versions managed via `buildSrc`.
- Kotlin Static Analysis via `ktlint` and `detekt`.

## Gradle Setup 🐘

This project is using [**Gradle Kotlin DSL**](https://docs.gradle.org/current/userguide/kotlin_dsl.html) as well as the [Plugin DSL](https://docs.gradle.org/current/userguide/plugins.html#sec:plugins_block) to setup the build.

Dependencies are centralized inside the [Dependencies.kt](buildSrc/src/main/java/Dependencies.kt) file in the `buildSrc` folder. This provides convenient auto-completion when writing your gradle files.

## Static Analysis 🔍

This project is using [**ktlint**](https://github.com/pinterest/ktlint) with the [ktlint-gradle](https://github.com/jlleitschuh/ktlint-gradle) plugin to format your code. To reformat all the source code as well as the buildscript you can run the `ktlintFormat` gradle task.

This project is also using [**detekt**](https://github.com/detekt/detekt) to analyze the source code, with the configuration that is stored in the [detekt.yml](configs/detekt/detekt.yml) file (the file has been generated with the `detektGenerateConfig` task).

```
./gradlew detekt - To run detekt

./gradlew ktlintCheck - checks all SourceSets and project Kotlin script files

./gradlew ktlintFormat - tries to format according to the code style all SourceSets Kotlin files and project Kotlin script files

```

## Using UI Components ##

### 1. Authentication

- Authentication to GitHub Packages is required for installing or using the ui components sdk. More details about Github Authentication for packages can be found at https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
- Ensure `github.properties` file is populated with the following
```
gpr.user=GITHUB_USERID 
gpr.key=PERSONAL_ACCESS_TOKEN
```
Replace GITHUB_USERID with personal / organisation Github User ID and PERSONAL_ACCESS_TOKEN with the token generated. Instructions for generating personal access token is provided below.

#### Generate a Personal Access Token for GitHub
-   Inside you GitHub account:
-   Settings -> Developer Settings -> Personal Access Tokens -> Generate new token
-   Make sure you select the following scopes (“ read:packages”) and Generate a token
-   After Generating make sure to copy your new personal access token. You cannot see it again! The only option is to generate a new key.
- Replace GITHUB_USERID with personal / organisation Github User ID and PERSONAL_ACCESS_TOKEN with the token generated

**NOTE: DO NOT COMMIT WITH YOUR GITHUB CREDENTIALS TO REPOSITORY. IT IS ONY MEANT TO BE USED LOCALLY.**

### 2.Add UI components dependencies

### MAVEN

    TODO: Add Maven Repository


### GRADLE

    TODO: Add Gradle Repository


## Releasing ##

    TODO: Add Release Guidelines


## Contributing ##

We love contributions! Please read our [contribution guidelines](/CONTRIBUTING.md) to get started.