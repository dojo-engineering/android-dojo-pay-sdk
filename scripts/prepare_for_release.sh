#!/bin/bash

FILE="./buildSrc/src/main/kotlin/publish.gradle.kts"

# adding imports
awk 'NR=1 {gsub(/import com.android.build.gradle.LibraryExtension/, "import com.android.build.gradle.LibraryExtension\nimport java.io.FileInputStream\nimport java.util.Properties")} {print}' "$FILE" > temp && mv temp "$FILE"
# uncommenting lines for the publish process
awk 'NR>=55 && NR<=56 {sub(/\/\//, "")} {print}' "$FILE" > temp && mv temp "$FILE"
awk 'NR>=63 && NR<=71 {sub(/\/\//, "")} {print}' "$FILE" > temp && mv temp "$FILE"

if [[ $? -ne 0 ]]; then
    echo "Looks like we had some issues updating the file."
    exit 1
fi

echo "publish.gradle now is ready for the publish process completed."