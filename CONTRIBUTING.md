Contributing
============

Interested in contributing? Awesome! 🤩 

We deeply value code contributions from the Dojo developers community ❤️. We'd like to make it as easy as possible for you to work in this repository. Ensure that you follow the guidelines before contributing.

## Let's get started

There are many ways you can contribute! ❤️

### Bug reports and fixes 🐞
-  If you find a bug, please search for it in the [issues](https://github.com/dojo-engineering/android-dojo-pay-sdk/issues), and if it isn't already tracked,
   [create a new issue](https://github.com/dojo-engineering/android-dojo-pay-sdk/issues/new). Fill out the **Bug Report** section of the issue template. Even if an issue is closed, feel free to comment and add details, we will still review it.
-  Issues that have already been identified as a bug (📝 and able to reproduce) will be labelled `bug`.
-  If you'd like to submit a fix for a bug, send a pull request and mention the issue number.
-  Include tests that isolate the bug and verifies that it was fixed.

### New Features 💡
-  If you'd like to add new functionality to this project, describe the problem you want to solve in a [new issue](https://github.com/dojo-engineering/android-dojo-pay-sdk/issues/new).
-  Issues that have been identified as a feature request will be labelled `enhancement`.
-  If you'd like to implement the new feature, wait for feedback from the project
   maintainers before spending too much time writing the code. In some cases, an `enhancement` may
   not align well with the project objectives at the time.

### Tests 🔎, documentation 📚, miscellaneous ✨

- If you'd like to improve the tests, or want to make the documentation clearer, you have an alternative implementation of something that may have advantages over the way it's currently done, or you have any other change, we would be happy to hear about it!
-  If it's a trivial change, go ahead and pull request with the changes you have in mind.
-  If not, [open an issue](https://github.com/dojo-engineering/android-dojo-pay-sdk/issues/new) to discuss the idea first.


## Requirements

For your contribution to be accepted:

- ✅ The test suite must be complete and pass.
- ✅ The changes must be approved by code review.
- ✅ Commits should be atomic and messages must be descriptive. Related issues should be mentioned by issue number.

If the contribution doesn't meet the above criteria, you may fail our automated checks or a maintainer will discuss it with you. You can continue to improve a pull request by adding commits to the branch from which the pull request was created.


## Creating a pull request

1. 🍴 Fork the repository on GitHub.
2. 🏃‍♀️ Clone/fetch your fork to your local development machine. It's a good idea to run the tests just to make sure everything is in order.
3. 🌿 Create a new branch and check it out.
4. 🔮 Make your changes and commit them locally. Magic happens here!
5. ⤴️ Push your new branch to your fork. (e.g. `git push username fix-issue-16`).
6. 📥 Open a Pull Request on github.com from your new branch on your fork to `main` in this repository.

## Static Analysis 🔍

This project is using [**ktlint**](https://github.com/pinterest/ktlint) with the [ktlint-gradle](https://github.com/jlleitschuh/ktlint-gradle) plugin to format your code. To reformat all the source code as well as the buildscript, run the `ktlintFormat` gradle task.

This project is also using [**detekt**](https://github.com/detekt/detekt) to analyze the source code, with the configuration that is stored in the [detekt.yml](configs/detekt/detekt.yml) file (the file has been generated with the `detektGenerateConfig` task).

```
./gradlew detekt - Runs detekt

./gradlew ktlintCheck - checks all SourceSets and project Kotlin script files

./gradlew ktlintFormat - tries to format according to the code style all SourceSets Kotlin files and project Kotlin script files

```