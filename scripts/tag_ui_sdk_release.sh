#!/bin/bash

# Define the path to your build.gradle.kts file
BUILD_FILE="./uisdk/build.gradle.kts"

# Extract the version from build.gradle.kts from line 1
currentVersion=$(head -n 1 "$BUILD_FILE")
currentVersion=$(echo "$currentVersion" | sed 's/"//g')
currentVersion=$(echo "$currentVersion" | sed 's/version = //g')

modifiedFiles=$(git ls-files -m)
modifiedFiles=''
if [ -n "$modifiedFiles" ]; then
  echo "You have local changes to this files:"
  echo "$modifiedFiles"
else

  echo "Insert the new version name (x.y.z), current version is '$currentVersion'"
  echo "\n"
  read -r inputVersionName
  # Check if the input is empty
  if [[ -z "$inputVersionName" ]]; then
    echo "Error: Version name cannot be empty."
    exit 1
  fi

  # Check if the input contains only valid characters
  if [[ ! "$inputVersionName" =~ ^[A-Za-z0-9._-]+$ ]]; then
    echo "Error: look like version name can only contain special characters."
    exit 1
  fi

  echo ""
  echo "Fetching remote branch..."
  git pull origin

  currentBranch=$(git rev-parse --abbrev-ref HEAD)

  echo "Current branch $currentBranch"
  echo ""

  sed -i.bak "s/version = \".*\"/version = \"$inputVersionName\"/" "$BUILD_FILE"
  rm "$BUILD_FILE.bak"

  echo ""
  echo "Version will be updated from '$currentVersion' to '$inputVersionName', release tag will be created and pushed!"
  echo "Continue? y/n"
  read -r response
  case "$response" in
  [yY][eE][sS] | [yY])
    echo "Commit in progress... $inputVersionName"
    git commit $BUILD_FILE -m "bumped version $inputVersionName"
    git push
    git tag -a "tag/ui_v$inputVersionName" -m ''
    git push origin "RC_$inputVersionName"
    ;;
  *)
  git restore $BUILD_FILE
    echo "Bye Bye"
    ;;
  esac
fi