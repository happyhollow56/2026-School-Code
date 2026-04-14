package com.gamelauncher.model;

import java.io.File;
import java.io.IOException;

/**
 * Concrete implementation of Game representing locally installed PC games.
 * Extends the abstract Game class and adds PC-specific functionality.
 */
public class PCGame extends Game {
    
    public enum LaunchType {
        EXE, LINK
    }
    
    // Additional fields specific to PC games
    private String installDirectory;
    private String platform;
    private double playtimeHours;
    private LaunchType launchType;
    private String primaryLinkId;
    private String customImagePath;

    
    /**
     * Default constructor
     */
    public PCGame() {
        super();
        this.playtimeHours = 0.0;
        this.launchType = LaunchType.EXE;
        this.primaryLinkId = null;
        this.customImagePath = null;
    }
    
    /**
     * Parameterized constructor
     * @param gameId Unique identifier for the game
     * @param title Title of the game
     * @param executablePath Path to the game's executable file
     * @param description Description of the game
     * @param installDirectory Directory where the game is installed
     * @param platform Platform the game runs on (e.g., Steam, Epic, GOG, Standalone)
     */
    public PCGame(String gameId, String title, String executablePath, String description) {
        super(gameId, title, executablePath, description);
        this.installDirectory = "";
        this.platform = "";
        this.playtimeHours = 0.0;
        this.launchType = LaunchType.EXE;
        this.primaryLinkId = null;
        this.customImagePath = null;
    }

    public PCGame(String gameId, String title, String executablePath, String description,
                  String installDirectory, String platform) {
        super(gameId, title, executablePath, description);
        this.installDirectory = installDirectory;
        this.platform = platform;
        this.playtimeHours = 0.0;
        this.launchType = LaunchType.EXE;
        this.primaryLinkId = null;
        this.customImagePath = null;
    }
    
    /**
     * Full parameterized constructor including playtime
     * @param gameId Unique identifier for the game
     * @param title Title of the game
     * @param executablePath Path to the game's executable file
     * @param description Description of the game
     * @param installDirectory Directory where the game is installed
     * @param platform Platform the game runs on
     * @param playtimeHours Total hours played
     */
    public PCGame(String gameId, String title, String executablePath, String description,
                  String installDirectory, String platform, double playtimeHours) {
        super(gameId, title, executablePath, description);
        this.installDirectory = installDirectory;
        this.platform = platform;
        this.playtimeHours = playtimeHours;
        this.launchType = LaunchType.EXE;
        this.primaryLinkId = null;
        this.customImagePath = null;
    }
    
    @Override
    public boolean launch() {
        try {
            if (launchType == LaunchType.EXE) {
                // Try local executable
                if (isValid()) {
                    File executable = new File(getExecutablePath());
                    ProcessBuilder pb = new ProcessBuilder(executable.getAbsolutePath());
                    if (executable.getParentFile() != null) {
                        pb.directory(executable.getParentFile());
                    }
                    pb.start();

                    updateLastPlayed();
                    addPlaytime(0.1);
                    System.out.println("Launched: " + getTitle());
                    return true;
                }
            } else if (launchType == LaunchType.LINK && primaryLinkId != null) {
                // Try primary link
                GameHyperlink link = getLinkById(primaryLinkId);
                if (link != null && link.open()) {
                    updateLastPlayed();
                    addPlaytime(0.1);
                    System.out.println("Launched via link: " + link.getUrl());
                    return true;
                }
            }

            // Fallback: try exe if link failed, or try links if exe failed
            if (launchType == LaunchType.LINK && isValid()) {
                File executable = new File(getExecutablePath());
                ProcessBuilder pb = new ProcessBuilder(executable.getAbsolutePath());
                if (executable.getParentFile() != null) {
                    pb.directory(executable.getParentFile());
                }
                pb.start();

                updateLastPlayed();
                addPlaytime(0.1);
                System.out.println("Launched (fallback): " + getTitle());
                return true;
            } else if (launchType == LaunchType.EXE) {
                // Try any link as fallback
                for (GameHyperlink link : getLinks()) {
                    if (link != null && link.open()) {
                        updateLastPlayed();
                        addPlaytime(0.1);
                        System.out.println("Launched via link (fallback): " + link.getUrl());
                        return true;
                    }
                }
            }

            System.err.println("Cannot launch game: no valid executable or link found");
            return false;

        } catch (IOException e) {
            System.err.println("Failed to launch game: " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            System.err.println("Security exception when launching game: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean isValid() {
        if (getExecutablePath() == null || getExecutablePath().trim().isEmpty()) {
            return false;
        }
        File executable = new File(getExecutablePath());
        return executable.exists() && executable.isFile();
    }
    
    @Override
    public String getGameType() {
        return "PC Game";
    }
    
    /**
     * Opens the game's installation directory in the system file explorer
     * @return true if successful, false otherwise
     */
    public boolean openInstallDirectory() {
        if (installDirectory == null || installDirectory.trim().isEmpty()) {
            return false;
        }
        
        File dir = new File(installDirectory);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            
            if (os.contains("win")) {
                pb = new ProcessBuilder("explorer.exe", dir.getAbsolutePath());
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", dir.getAbsolutePath());
            } else {
                pb = new ProcessBuilder("xdg-open", dir.getAbsolutePath());
            }
            
            pb.start();
            return true;
            
        } catch (IOException e) {
            System.err.println("Failed to open directory: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Adds playtime to the total
     * @param hours Hours to add
     */
    public void addPlaytime(double hours) {
        if (hours > 0) {
            this.playtimeHours += hours;
        }
    }
    
    /**
     * Formats playtime for display
     * @return Formatted string (e.g., "5.5 hours" or "30 minutes")
     */
    public String getFormattedPlaytime() {
        if (playtimeHours < 1.0) {
            int minutes = (int) (playtimeHours * 60);
            return minutes + " minutes";
        } else {
            return String.format("%.1f hours", playtimeHours);
        }
    }
    
    // Getters and Setters
    
    public String getInstallDirectory() {
        return installDirectory;
    }
    
    public void setInstallDirectory(String installDirectory) {
        this.installDirectory = installDirectory;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public double getPlaytimeHours() {
        return playtimeHours;
    }
    
    public void setPlaytimeHours(double playtimeHours) {
        if (playtimeHours >= 0) {
            this.playtimeHours = playtimeHours;
        }
    }
    
    public LaunchType getLaunchType() {
        return launchType;
    }
    
    public void setLaunchType(LaunchType launchType) {
        this.launchType = launchType != null ? launchType : LaunchType.EXE;
    }
    
    public String getPrimaryLinkId() {
        return primaryLinkId;
    }
    
    public void setPrimaryLinkId(String primaryLinkId) {
        this.primaryLinkId = primaryLinkId;
    }
    
    public String getCustomImagePath() {
        return customImagePath;
    }
    
    public void setCustomImagePath(String customImagePath) {
        this.customImagePath = customImagePath;
    }
    
    @Override
    public String toString() {
        return "PCGame [" + super.toString() + 
               ", installDirectory=" + installDirectory + 
               ", platform=" + platform + 
               ", playtimeHours=" + playtimeHours + 
               ", launchType=" + launchType + 
               ", primaryLinkId=" + primaryLinkId + 
               ", customImagePath=" + customImagePath + "]";
    }
}
