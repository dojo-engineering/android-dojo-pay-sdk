#!/bin/bash

# Define the file to modify
FILE="./buildSrc/src/main/kotlin/publish.gradle.kts"

awk 'NR=1 {gsub(/import com.android.build.gradle.LibraryExtension/, "import com.android.build.gradle.LibraryExtension\nimport java.io.FileInputStream\nimport java.util.Properties")} {print}' "$FILE" > temp && mv temp "$FILE"
awk 'NR>=55 && NR<=56 {gsub(/\//, "")} {print}' "$FILE" > temp && mv temp "$FILE"
awk 'NR>=63 && NR<=71 {gsub(/\//, "")} {print}' "$FILE" > temp && mv temp "$FILE"



echo "Modifications completed."