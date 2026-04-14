# Tony Game Launcher - Java Edition - UML Class Diagram

## Class Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              <<abstract>>                                     │
│                                  Game                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│ - gameId: String                                                            │
│ - title: String                                                             │
│ - executablePath: String                                                    │
│ - description: String                                                       │
│ - lastPlayed: LocalDateTime                                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│ + launch(): boolean {abstract}                                              │
│ + isValid(): boolean {abstract}                                             │
│ + getGameType(): String {abstract}                                          │
│ + updateLastPlayed(): void                                                  │
│ + validateGameId(String): boolean {protected}                               │
│ + validateTitle(String): boolean {protected}                                │
│ + validateExecutablePath(String): boolean {protected}                       │
│ + getters/setters                                                           │
└─────────────────────────────────────────────────────────────────────────────┘
                                        △
                                        │ extends
                                        │
┌─────────────────────────────────────────────────────────────────────────────┐
│                                 PCGame                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│ - installDirectory: String                                                  │
│ - platform: String                                                          │
│ - playtimeHours: double                                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│ + launch(): boolean                                                         │
│ + isValid(): boolean                                                        │
│ + getGameType(): String                                                     │
│ + openInstallDirectory(): boolean                                           │
│ + addPlaytime(double): void                                                 │
│ + getFormattedPlaytime(): String                                            │
│ + getters/setters                                                           │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                              GameLibrary                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│ - games: List<Game>                                                         │
│ - categories: List<String>                                                  │
│ - dataFilePath: String                                                      │
│ - lastModified: LocalDateTime                                               │
├─────────────────────────────────────────────────────────────────────────────┤
│ + addGame(Game): boolean                                                    │
│ + removeGame(String): boolean                                               │
│ + findGameById(String): Game                                                │
│ + searchByTitle(String): List<Game>                                         │
│ + getAllGames(): List<Game>                                                 │
│ + getGameCount(): int                                                       │
│ + getRecentlyPlayed(): List<Game>                                           │
│ + launchGame(String): boolean                                               │
│ + addCategory(String): boolean                                              │
│ + removeCategory(String): boolean                                           │
│ + getCategories(): List<String>                                             │
│ + saveToFile(): void                                                        │
│ + loadFromFile(): void                                                      │
│ + clear(): void                                                             │
│ + getters/setters                                                           │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                            GameHyperlink                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│ - linkId: String                                                            │
│ - gameId: String                                                            │
│ - name: String                                                              │
│ - url: String                                                               │
│ - description: String                                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│ + openInBrowser(): boolean                                                  │
│ + isValidUrl(): boolean                                                     │
│ + getShortUrl(): String                                                     │
│ + getters/setters                                                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Relationships

### Inheritance
```
Game <|-- PCGame
```
- `PCGame` extends the abstract class `Game`
- Inherits all fields and concrete methods from `Game`
- Implements all abstract methods from `Game`

### Association
```
GameLibrary --> "0..*" Game : contains
GameLibrary --> "0..*" String : categories
```
- `GameLibrary` has a composition relationship with `Game`
- `GameLibrary` maintains a list of `Game` objects
- `GameLibrary` maintains a list of category strings

### Usage
```
MainController --> GameLibrary : uses
MainController --> PCGame : displays
MainController --> GameHyperlink : manages links
AddGameController --> GameLibrary : adds games
```

## Field Summary (12 Total Fields)

| Class | Field | Type | Description |
|-------|-------|------|-------------|
| Game | gameId | String | Unique identifier |
| Game | title | String | Game title |
| Game | executablePath | String | Path to executable |
| Game | description | String | Game description |
| Game | lastPlayed | LocalDateTime | Last played timestamp |
| PCGame | installDirectory | String | Installation folder |
| PCGame | platform | String | Platform (Steam, Epic, etc.) |
| PCGame | playtimeHours | double | Total playtime |
| GameLibrary | games | List<Game> | Collection of games |
| GameLibrary | categories | List<String> | Category names |
| GameLibrary | dataFilePath | String | Persistence file path |
| GameLibrary | lastModified | LocalDateTime | Last modification time |

## Method Summary

### Abstract Methods (Game)
- `launch(): boolean` - Launches the game
- `isValid(): boolean` - Validates game can be launched
- `getGameType(): String` - Returns game type identifier

### Concrete Methods

#### PCGame
- `launch()` - Executes the game executable
- `isValid()` - Checks if executable file exists
- `getGameType()` - Returns "PC Game"
- `openInstallDirectory()` - Opens folder in file explorer
- `addPlaytime(double)` - Adds to total playtime
- `getFormattedPlaytime()` - Returns formatted playtime string

#### GameLibrary
- `addGame(Game)` - Adds game to library
- `removeGame(String)` - Removes game by ID
- `findGameById(String)` - Finds game by ID
- `searchByTitle(String)` - Searches games by title
- `getAllGames()` - Returns all games
- `getRecentlyPlayed()` - Returns games sorted by last played
- `launchGame(String)` - Launches game by ID
- `saveToFile()` - Persists library to file
- `loadFromFile()` - Loads library from file

#### GameHyperlink
- `openInBrowser()` - Opens URL in default browser
- `isValidUrl()` - Validates URL format
- `getShortUrl()` - Returns shortened URL for display
