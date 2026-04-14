@echo off
REM Build script for Windows

echo ========================================
echo   Tony Game Launcher - Java Edition - Build Script
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6+ and try again
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17+ and try again
    exit /b 1
)

echo Java version:
java -version
echo.

echo Maven version:
mvn -version
echo.

REM Clean and compile
echo Cleaning previous build...
mvn clean
echo.

REM Compile
echo Compiling...
mvn compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed
    exit /b 1
)
echo.

REM Run tests
echo Running tests...
mvn exec:java -Dexec.mainClass="com.gamelauncher.GameLauncherTest" -Dexec.classpathScope=compile
if %errorlevel% neq 0 (
    echo WARNING: Some tests failed
) else (
    echo All tests passed!
)
echo.

REM Package
echo Packaging application...
mvn package -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Packaging failed
    exit /b 1
)
echo.

echo ========================================
echo   Build Complete!
echo ========================================
echo.
echo To run the application:
echo   mvn javafx:run
echo.
echo Or run the JAR directly:
echo   java -jar target/game-launcher-1.0-SNAPSHOT-launcher.jar
echo.

pause
