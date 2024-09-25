#!/bin/bash

# Define the path to your build.gradle.kts file
BUILD_FILE="./uisdk/build.gradle.kts"

# Extract the version from build.gradle.kts from line 1
VERSION=$(head -n 1 "$BUILD_FILE")
VERSION=$(echo "$VERSION" | sed 's/"//g')
VERSION=$(echo "$VERSION" | sed 's/version = //g')

# Check if the version was found
if [ -z "$VERSION" ]; then
    echo "Version not found in $BUILD_FILE"
    exit 1
fi

TAG_NAME="ui-v$VERSION"

# Create a git tag with the version
git tag "$TAG_NAME"

# Push the tag to the remote repository (optional, uncomment to enable)
git push origin "$TAG_NAME"

echo "Git tag '$TAG_NAME'created successfully."