Publishing
============

Before starting the release process, ensure that the milestone is update-to-date as per CONTRIBUTING.md

### To Publish Using CI (Recommended Approach)

#### Sequence of steps for release:

1.  Make sure the version is changed to a non-SNAPSHOT version in version field at `sdk/build.gradle.kts`.

2.  Update the `README.md` with the new version.
   
3.  Create release branch, _**release/<version_number>**_ (etc. release/1.5.0) and push.

4.  Create and push tag _**tag/v1.5.0**_. Bitrise CI will create and publish a **release** build and notify ui components release channel.

5.  If the build fails, fix the issue and try again.

6.  GitHub package release should now be created and ready to consume


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
