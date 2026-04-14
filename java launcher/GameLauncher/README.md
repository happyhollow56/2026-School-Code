# Tony Game Launcher - Java Edition

A JavaFX desktop application for organizing and launching PC games. This application provides a centralized hub for managing games from various platforms including Steam, Epic Games Store, GOG, and standalone installations.

## Features

- **Game Library Management**: Add, remove, and organize your PC games
- **One-Click Launch**: Launch games directly from the application
- **Categories**: Organize games with default and custom categories
- **Search**: Quickly find games by title
- **Hyperlinks**: Store and access game-related websites (wikis, forums, etc.)
- **Playtime Tracking**: Track hours played for each game
- **Recently Played**: Quick access to recently launched games
- **Dark Theme**: Modern dark UI design

## Project Structure

```
GameLauncher/
├── src/
│   ├── main/
│   │   ├── java/com/gamelauncher/
│   │   │   ├── model/           # Data models
│   │   │   │   ├── Game.java           # Abstract base class
│   │   │   │   ├── PCGame.java         # Concrete game implementation
│   │   │   │   ├── GameLibrary.java    # Library manager
│   │   │   │   └── GameHyperlink.java  # Hyperlink model
│   │   │   ├── ui/              # User interface
│   │   │   │   ├── GameLauncherApp.java    # Main application
│   │   │   │   ├── MainController.java     # Main UI controller
│   │   │   │   └── AddGameController.java  # Add game dialog
│   │   │   └── GameLauncherTest.java  # Test class
│   │   └── resources/
│   │       ├── main_view.fxml       # Main UI layout
│   │       ├── add_game_dialog.fxml # Add game dialog
│   │       └── styles.css           # Application styles
│   └── pom.xml                  # Maven configuration
└── README.md
```

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- JavaFX 17

## Class Structure

### Abstract Class: `Game`
Base class defining common game properties and behaviors.
**Fields**: gameId, title, executablePath, description, lastPlayed

### Concrete Class: `PCGame` (extends Game)
Represents locally installed PC games with platform-specific features.
**Additional Fields**: installDirectory, platform, playtimeHours

### Manager Class: `GameLibrary`
Manages the collection of games with CRUD operations and persistence.
**Fields**: games, categories, dataFilePath, lastModified

### Supporting Class: `GameHyperlink`
Stores and manages hyperlinks associated with games.
**Fields**: linkId, gameId, name, url, description

**Total Fields: 12** (exceeds the minimum requirement of 10)

## Building the Application

### Using Maven

1. Navigate to the project directory:
```bash
cd GameLauncher
```

2. Compile the project:
```bash
mvn clean compile
```

3. Run the tests:
```bash
mvn test
```

4. Package as executable JAR:
```bash
mvn clean package
```

The executable JAR will be created at:
```
target/game-launcher-1.0-SNAPSHOT-launcher.jar
```

## Running the Application

### One-Command Launch (recommended)
From project root:
```bash
mvn clean package javafx:run -Djavafx.platform=win
```

### Method 1: Using Maven
```bash
mvn javafx:run
```

### Method 2: Using the executable JAR
```bash
java -jar target/game-launcher-1.0-SNAPSHOT-launcher.jar
```

### Method 3: Running tests only
```bash
mvn exec:java -Dexec.mainClass="com.gamelauncher.GameLauncherTest"
```

Or compile and run directly:
```bash
cd src/main/java
javac com/gamelauncher/GameLauncherTest.java
java com.gamelauncher.GameLauncherTest
```

## Using the Application

### Adding a Game
1. Click the "Add Game" button
2. Fill in the game details:
   - Title (required)
   - Executable path (required) - use Browse to select the .exe file
   - Installation directory (optional)
   - Platform (e.g., Steam, Epic, GOG)
   - Description (optional)
3. Click Save

### Launching a Game
- Click the "Launch" button on a game card, or
- Select a game and click the "Launch" button in the toolbar

### Adding Hyperlinks
1. Select a game to view its details
2. Click "Add Link" in the details panel
3. Enter the link name and URL
4. Click the link to open in your default browser

### Managing Categories
- Use the sidebar to filter by category
- Click "Add Category" to create custom categories

## Data Storage

Game data is stored in a text file (`data/gamelibrary.dat`) using a pipe-delimited format:
```
GAME|gameId|title|executablePath|description|lastPlayed|installDirectory|platform|playtimeHours
```

## Error Handling

The application includes comprehensive error handling for:
- Invalid file paths
- Missing executable files
- Duplicate game entries
- Invalid URL formats
- File I/O errors

## License

This project is created for educational purposes as part of a Java programming course.
