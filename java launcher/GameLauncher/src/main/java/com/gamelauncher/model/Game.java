package com.gamelauncher.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract base class representing a game in the launcher.
 * This class defines the common properties and behaviors that all game types must implement.
 */
public abstract class Game {
    
    // Fields
    private String gameId;
    private String title;
    private String executablePath;
    private String description;
    private LocalDateTime lastPlayed;
    private Set<String> categories;
    private List<GameHyperlink> links;
    
    /**
     * Default constructor
     */
    public Game() {
        this.lastPlayed = null;
        this.categories = new HashSet<>();
        this.links = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor
     * @param gameId Unique identifier for the game
     * @param title Title of the game
     * @param executablePath Path to the game's executable file
     * @param description Description of the game
     */
    public Game(String gameId, String title, String executablePath, String description) {
        this.gameId = gameId;
        this.title = title;
        this.executablePath = executablePath;
        this.description = description;
        this.lastPlayed = null;
        this.categories = new HashSet<>();
        this.links = new ArrayList<>();
    }
    
    // Abstract methods that subclasses must implement
    
    /**
     * Launches the game
     * @return true if launch was successful, false otherwise
     */
    public abstract boolean launch();
    
    /**
     * Validates if the game can be launched (e.g., executable exists)
     * @return true if valid, false otherwise
     */
    public abstract boolean isValid();
    
    /**
     * Gets the type of game
     * @return String representing the game type
     */
    public abstract String getGameType();
    
    // Concrete methods
    
    /**
     * Updates the last played timestamp to current time
     */
    public void updateLastPlayed() {
        this.lastPlayed = LocalDateTime.now();
    }
    
    /**
     * Validates that the game ID is not null or empty
     * @param gameId The game ID to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateGameId(String gameId) {
        return gameId != null && !gameId.trim().isEmpty();
    }
    
    /**
     * Validates that the title is not null or empty
     * @param title The title to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
    
    /**
     * Validates that the executable path is not null or empty
     * @param path The path to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateExecutablePath(String path) {
        return path != null && !path.trim().isEmpty();
    }
    
    // Getters and Setters
    
    public String getGameId() {
        return gameId;
    }
    
    public void setGameId(String gameId) {
        if (validateGameId(gameId)) {
            this.gameId = gameId;
        } else {
            throw new IllegalArgumentException("Game ID cannot be null or empty");
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        if (validateTitle(title)) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }
    
    public String getExecutablePath() {
        return executablePath;
    }
    
    public void setExecutablePath(String executablePath) {
        if (validateExecutablePath(executablePath)) {
            this.executablePath = executablePath;
        } else {
            throw new IllegalArgumentException("Executable path cannot be null or empty");
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getLastPlayed() {
        return lastPlayed;
    }
    
    public void setLastPlayed(LocalDateTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    
    public Set<String> getCategories() {
        return new HashSet<>(categories);
    }
    
    public void addCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            categories.add(category.trim());
        }
    }
    
    public void removeCategory(String category) {
        if (category != null && !category.trim().isEmpty()) {
            categories.remove(category.trim());
        }
    }
    
    public boolean hasCategory(String category) {
        return category != null && categories.contains(category.trim());
    }

    public List<GameHyperlink> getLinks() {
        return new ArrayList<>(links);
    }

    public void addLink(GameHyperlink link) {
        if (link == null || link.getLinkId() == null) {
            throw new IllegalArgumentException("Link and linkId must be provided");
        }
        // ensure link belongs to this game
        link.setGameId(this.gameId);
        links.removeIf(existing -> existing.getLinkId().equals(link.getLinkId()));
        links.add(link);
    }

    public boolean removeLink(String linkId) {
        return links.removeIf(link -> linkId != null && linkId.equals(link.getLinkId()));
    }

    public GameHyperlink getLinkById(String linkId) {
        if (linkId == null) {
            return null;
        }
        return links.stream()
                .filter(link -> linkId.equals(link.getLinkId()))
                .findFirst()
                .orElse(null);
    }

    public void clearLinks() {
        links.clear();
    }
    
    @Override
    public String toString() {
        return "Game [gameId=" + gameId + ", title=" + title + 
               ", executablePath=" + executablePath + ", description=" + description + 
               ", lastPlayed=" + lastPlayed + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Game game = (Game) obj;
        return gameId != null && gameId.equals(game.gameId);
    }
    
    @Override
    public int hashCode() {
        return gameId != null ? gameId.hashCode() : 0;
    }
}
