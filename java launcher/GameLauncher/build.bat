@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   Tony Game Launcher - Java Edition - Build Script
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if !errorlevel! neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6+ and try again
    exit /b 1
)

call mvn -version
echo.

echo Cleaning previous build...
call mvn clean
if !errorlevel! neq 0 (
    echo ERROR: Clean failed
    exit /b 1
)
echo.

echo Compiling...
call mvn compile
if !errorlevel! neq 0 (
    echo ERROR: Compilation failed
    exit /b 1
)
echo.

echo Running tests...
call mvn test
echo.

echo Packaging application...
call mvn package -DskipTests
if !errorlevel! neq 0 (
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
