package com.gamelauncher.model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager class for the game collection.
 * Handles CRUD operations, persistence, and search functionality.
 */
public class GameLibrary {
    
    // Fields
    private List<Game> games;
    private List<String> categories;
    private String dataFilePath;
    private LocalDateTime lastModified;
    
    private static final String DEFAULT_DATA_FILE = "gamelibrary.dat";
    private static final String DELIMITER = "|";
    
    /**
     * Default constructor - uses default data file path
     */
    public GameLibrary() {
        this(DEFAULT_DATA_FILE);
    }
    
    /**
     * Constructor with custom data file path
     * @param dataFilePath Path to the data file for persistence
     */
    public GameLibrary(String dataFilePath) {
        this.games = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.dataFilePath = dataFilePath;
        this.lastModified = LocalDateTime.now();

        // Load existing data if available; if not, initialize with default categories
        loadFromFile();

        if (categories.isEmpty()) {
            categories.add("All Games");
            categories.add("Favorites");
            categories.add("Recently Played");
        }
    }
    
    /**
     * Adds a game to the library
     * @param game The game to add
     * @return true if added successfully, false if game already exists
     */
    public boolean addGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        
        // Check for duplicate game ID
        if (findGameById(game.getGameId()) != null) {
            System.err.println("Game with ID " + game.getGameId() + " already exists");
            return false;
        }
        
        games.add(game);
        updateLastModified();
        saveToFile();
        return true;
    }

    /**
     * Updates an existing game in the library
     * @param updatedGame The game object with updated values
     * @return true if updated, false if game not found
     */
    public boolean updateGame(PCGame updatedGame) {
        if (updatedGame == null) {
            return false;
        }

        Game existing = findGameById(updatedGame.getGameId());
        if (existing == null) {
            return false;
        }

        games.remove(existing);
        games.add(updatedGame);
        updateLastModified();
        saveToFile();
        return true;
    }
    
    /**
     * Removes a game from the library
     * @param gameId The ID of the game to remove
     * @return true if removed, false if not found
     */
    public boolean removeGame(String gameId) {
        Game game = findGameById(gameId);
        if (game != null) {
            games.remove(game);
            updateLastModified();
            saveToFile();
            return true;
        }
        return false;
    }
    
    /**
     * Finds a game by its ID
     * @param gameId The game ID to search for
     * @return The Game object, or null if not found
     */
    public Game findGameById(String gameId) {
        return games.stream()
                .filter(g -> g.getGameId().equals(gameId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Searches for games by title (partial match)
     * @param searchTerm The search term
     * @return List of matching games
     */
    public List<Game> searchByTitle(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>(games);
        }
        
        String lowerSearch = searchTerm.toLowerCase();
        return games.stream()
                .filter(g -> g.getTitle().toLowerCase().contains(lowerSearch))
                .collect(Collectors.toList());
    }
    
    /**
     * Gets all games in the library
     * @return List of all games
     */
    public List<Game> getAllGames() {
        return new ArrayList<>(games);
    }
    
    /**
     * Gets the number of games in the library
     * @return Game count
     */
    public int getGameCount() {
        return games.size();
    }
    
    /**
     * Gets games sorted by last played (most recent first)
     * @return List of games sorted by last played
     */
    public List<Game> getRecentlyPlayed() {
        return games.stream()
                .filter(g -> g.getLastPlayed() != null)
                .sorted((g1, g2) -> g2.getLastPlayed().compareTo(g1.getLastPlayed()))
                .collect(Collectors.toList());
    }
    
    public List<Game> getGamesByCategory(String category) {
        if (category == null || category.trim().isEmpty() || category.equals("All Games")) {
            return getAllGames();
        }

        if (category.equals("Recently Played")) {
            return getRecentlyPlayed();
        }

        if (category.equals("Favorites")) {
            return games.stream()
                    .filter(g -> g.hasCategory("Favorites"))
                    .collect(Collectors.toList());
        }

        String trimmed = category.trim();
        return games.stream()
                .filter(g -> g.hasCategory(trimmed))
                .collect(Collectors.toList());
    }
    
    /**
     * Launches a game by ID
     * @param gameId The ID of the game to launch
     * @return true if launched successfully, false otherwise
     */
    public boolean launchGame(String gameId) {
        Game game = findGameById(gameId);
        if (game == null) {
            System.err.println("Game not found: " + gameId);
            return false;
        }
        
        boolean success = game.launch();
        if (success) {
            updateLastModified();
            saveToFile();
        }
        return success;
    }
    
    /**
     * Adds a custom category
     * @param category The category name to add
     * @return true if added, false if already exists
     */
    public boolean addCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = category.trim();
        if (!categories.contains(trimmed)) {
            categories.add(trimmed);
            updateLastModified();
            saveToFile();
            return true;
        }
        return false;
    }
    
    /**
     * Removes a category
     * @param category The category to remove
     * @return true if removed, false if not found
     */
    public boolean removeCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        String trimmed = category.trim();
        if (!categories.contains(trimmed)) {
            return false;
        }

        categories.remove(trimmed);

        // Remove category from all games
        for (Game game : games) {
            game.removeCategory(trimmed);
        }

        updateLastModified();
        saveToFile();
        return true;
    }
    
    /**
     * Gets all categories
     * @return List of category names
     */
    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }
    
    /**
     * Saves the library to a text file
     */
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath))) {
            // Write header with categories
            writer.println("# Categories");
            for (String category : categories) {
                writer.println("CATEGORY" + DELIMITER + category);
            }
            
            writer.println("# Games");
            for (Game game : games) {
                if (game instanceof PCGame) {
                    PCGame pcGame = (PCGame) game;
                    String categoryList = String.join(",", pcGame.getCategories());
                    writer.println("GAME" + DELIMITER +
                            pcGame.getGameId() + DELIMITER +
                            pcGame.getTitle() + DELIMITER +
                            pcGame.getExecutablePath() + DELIMITER +
                            (pcGame.getDescription() != null ? pcGame.getDescription() : "") + DELIMITER +
                            (pcGame.getLastPlayed() != null ? pcGame.getLastPlayed().toString() : "null") + DELIMITER +
                            (pcGame.getInstallDirectory() != null ? pcGame.getInstallDirectory() : "") + DELIMITER +
                            (pcGame.getPlatform() != null ? pcGame.getPlatform() : "") + DELIMITER +
                            pcGame.getPlaytimeHours() + DELIMITER +
                            categoryList + DELIMITER +
                            pcGame.getLaunchType() + DELIMITER +
                            (pcGame.getPrimaryLinkId() != null ? pcGame.getPrimaryLinkId() : "") + DELIMITER +
                            (pcGame.getCustomImagePath() != null ? pcGame.getCustomImagePath() : ""));

                    // Save links for this game
                    for (GameHyperlink link : pcGame.getLinks()) {
                        writer.println("LINK" + DELIMITER +
                                pcGame.getGameId() + DELIMITER +
                                link.getLinkId() + DELIMITER +
                                (link.getName() != null ? link.getName() : "") + DELIMITER +
                                (link.getUrl() != null ? link.getUrl() : "") + DELIMITER +
                                (link.getDescription() != null ? link.getDescription() : ""));
                    }
                }
            }
            
            System.out.println("Library saved to " + dataFilePath);
            
        } catch (IOException e) {
            System.err.println("Error saving library: " + e.getMessage());
        }
    }
    
    /**
     * Loads the library from a text file
     */
    public void loadFromFile() {
        File file = new File(dataFilePath);
        if (!file.exists()) {
            System.out.println("No existing library file found. Starting with empty library.");
            return;
        }
        
        games.clear();
        categories.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split("\\" + DELIMITER, -1);
                
                if (parts[0].equals("CATEGORY") && parts.length >= 2) {
                    // Custom category
                    if (!categories.contains(parts[1])) {
                        categories.add(parts[1]);
                    }
                } else if (parts[0].equals("GAME") && parts.length >= 9) {
                    // Game entry
                    try {
                        PCGame game = new PCGame();
                        game.setGameId(parts[1]);
                        game.setTitle(parts[2]);
                        game.setExecutablePath(parts[3]);
                        game.setDescription(parts[4].isEmpty() ? null : parts[4]);
                        
                        if (!parts[5].equals("null")) {
                            game.setLastPlayed(LocalDateTime.parse(parts[5]));
                        }
                        
                        game.setInstallDirectory(parts[6].isEmpty() ? null : parts[6]);
                        game.setPlatform(parts[7].isEmpty() ? null : parts[7]);
                        game.setPlaytimeHours(Double.parseDouble(parts[8]));
                        
                        if (parts.length > 9 && !parts[9].isEmpty()) {
                            String[] categoryArray = parts[9].split(",");
                            for (String category : categoryArray) {
                                String trimmedCategory = category.trim();
                                if (!trimmedCategory.isEmpty()) {
                                    game.addCategory(trimmedCategory);
                                    if (!categories.contains(trimmedCategory)) {
                                        categories.add(trimmedCategory);
                                    }
                                }
                            }
                        }
                        
                        // New fields for launch type and primary link
                        if (parts.length > 10 && !parts[10].isEmpty()) {
                            try {
                                game.setLaunchType(PCGame.LaunchType.valueOf(parts[10]));
                            } catch (IllegalArgumentException e) {
                                game.setLaunchType(PCGame.LaunchType.EXE); // default
                            }
                        }
                        
                        if (parts.length > 11 && !parts[11].isEmpty()) {
                            game.setPrimaryLinkId(parts[11]);
                        }
                        
                        if (parts.length > 12 && !parts[12].isEmpty()) {
                            game.setCustomImagePath(parts[12]);
                        }
                        
                        games.add(game);
                    } catch (Exception e) {
                        System.err.println("Error parsing game entry: " + e.getMessage());
                    }
                } else if (parts[0].equals("LINK") && parts.length >= 6) {
                    String gameId = parts[1];
                    Game game = findGameById(gameId);
                    if (game != null && game instanceof PCGame) {
                        GameHyperlink link = new GameHyperlink(parts[2], gameId,
                                parts[3].isEmpty() ? "" : parts[3],
                                parts[4].isEmpty() ? "" : parts[4],
                                parts[5].isEmpty() ? "" : parts[5]);
                        game.addLink(link);
                    }
                }
            }
            
            System.out.println("Library loaded from " + dataFilePath + ". Found " + games.size() + " games.");
            
        } catch (IOException e) {
            System.err.println("Error loading library: " + e.getMessage());
        }
    }
    
    /**
     * Clears all games from the library
     */
    public void clear() {
        games.clear();
        updateLastModified();
        saveToFile();
    }
    
    private void updateLastModified() {
        this.lastModified = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public String getDataFilePath() {
        return dataFilePath;
    }
    
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }
    
    public LocalDateTime getLastModified() {
        return lastModified;
    }
}
