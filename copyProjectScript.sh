#!/bin/bash

# Get the directory where this script is located (source project root)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET_DIR="$(pwd)"

echo "Copying project from: $SCRIPT_DIR"
echo "To: $TARGET_DIR"

# Check if target directory is different from source
if [ "$SCRIPT_DIR" = "$TARGET_DIR" ]; then
    echo "Error: Cannot copy to the same directory"
    exit 1
fi

# Create .gitignore patterns for rsync exclusion
EXCLUDE_PATTERNS=(
    --exclude='.git/'
    --exclude='build/'
    --exclude='.gradle/'
    --exclude='*.iml'
    --exclude='.idea/'
    --exclude='out/'
    --exclude='target/'
    --exclude='.DS_Store'
    --exclude='*.log'
    --exclude='*.tmp'
    --exclude='node_modules/'
    --exclude='.vscode/'
    --exclude='*.class'
    --exclude='copy-project.sh'
)

# Copy files using rsync
rsync -av "${EXCLUDE_PATTERNS[@]}" "$SCRIPT_DIR/" "$TARGET_DIR/"

echo "Project copied successfully!"
echo "Don't forget to:"
echo "1. Update project name in settings.gradle.kts or build.gradle"
echo "2. Update package names if needed"
echo "3. Initialize git repository: git init"