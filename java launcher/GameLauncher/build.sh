#!/bin/bash
# Build script for Linux/Mac

echo "========================================"
echo "  Tony Game Launcher - Java Edition - Build Script"
echo "========================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6+ and try again"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java 17+ and try again"
    exit 1
fi

echo "Java version:"
java -version
echo ""

echo "Maven version:"
mvn -version
echo ""

# Clean and compile
echo "Cleaning previous build..."
mvn clean
echo ""

# Compile
echo "Compiling..."
mvn compile
if [ $? -ne 0 ]; then
    echo "ERROR: Compilation failed"
    exit 1
fi
echo ""

# Run tests
echo "Running tests..."
mvn exec:java -Dexec.mainClass="com.gamelauncher.GameLauncherTest" -Dexec.classpathScope=compile
if [ $? -ne 0 ]; then
    echo "WARNING: Some tests failed"
else
    echo "All tests passed!"
fi
echo ""

# Package
echo "Packaging application..."
mvn package -DskipTests
if [ $? -ne 0 ]; then
    echo "ERROR: Packaging failed"
    exit 1
fi
echo ""

echo "========================================"
echo "  Build Complete!"
echo "========================================"
echo ""
echo "To run the application:"
echo "  mvn javafx:run"
echo ""
echo "Or run the JAR directly:"
echo "  java -jar target/game-launcher-1.0-SNAPSHOT-launcher.jar"
echo ""
