#!/bin/bash

# Define the file to modify
FILE="./buildSrc/src/main/kotlin/publish.gradle.kts"

# Insert the import statements after line 1
sed -i '' '1 a\
import java.io.FileInputStream\
import java.util.Properties' "$FILE"

# Uncomment lines 54 and 55
sed -i '' '53,55s|^//||' "$FILE"

# Uncomment lines 61 to 68
sed -i '' '60,67s|^//||' "$FILE"

echo "Modifications completed."