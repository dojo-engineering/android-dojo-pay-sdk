Publishing
============

Before starting the release process, ensure that the milestone is update-to-date as per CONTRIBUTING.md

### To Publish Using CI (Recommended Approach)

#### Sequence of steps for release:

1. Create a release branch from develop.<br>For CORE name it releases/CORE_1.7.0<br>For UI name it release/UI_1.4.0<br>If you need to release both at the same time, name it releases/CORE_1.7.0_UI_1.4.0
2. Checkout the new branch and run the next command to release:
   <br>core → sh scripts/publish_core_sdk_release.sh
   <br>ui → sh scripts/publish_ui_sdk_release.sh
   <br>The script will update the version asking to input the new version commit the change and create a tag. CI is set to run
   automatically the workflow to publish the new version (ui or core) based on the tag just created.


### Manual publishing

This method is not recommended and must only be used for debugging, or if asked by a CODEOWNER


- Ensure `github.properties` file is populated with the following

```
gpr.user=GITHUB_USERID 
gpr.key=PERSONAL_ACCESS_TOKEN
```

- To publish to github packages

```
./gradlew publish
```
