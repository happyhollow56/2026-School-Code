package com.gamelauncher.model;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Represents a hyperlink associated with a game.
 * Stores URL information and provides functionality to open links in browser.
 */
public class GameHyperlink {
    
    private String linkId;
    private String gameId;
    private String name;
    private String url;
    private String description;
    
    /**
     * Default constructor
     */
    public GameHyperlink() {
    }
    
    /**
     * Parameterized constructor
     * @param linkId Unique identifier for this hyperlink
     * @param gameId ID of the associated game
     * @param name Display name for the link
     * @param url The URL
     */
    public GameHyperlink(String linkId, String gameId, String name, String url) {
        this.linkId = linkId;
        this.gameId = gameId;
        this.name = name;
        this.url = url;
    }
    
    /**
     * Full parameterized constructor
     * @param linkId Unique identifier for this hyperlink
     * @param gameId ID of the associated game
     * @param name Display name for the link
     * @param url The URL
     * @param description Description of what this link contains
     */
    public GameHyperlink(String linkId, String gameId, String name, String url, String description) {
        this(linkId, gameId, name, url);
        this.description = description;
    }
    
    /**
     * Opens this link in browser/local process.
     * Supports http(s), steam:// and local executables.
     * @return true if opened or launched successfully, false otherwise
     */
    public boolean open() {
        if (url == null || url.trim().isEmpty()) {
            System.err.println("URL is empty");
            return false;
        }

        String trimmed = url.trim();

        try {
            // Local path handling
            File localFile = new File(trimmed);
            if (localFile.exists()) {
                if (localFile.isDirectory()) {
                    // open directory in explorer
                    String os = System.getProperty("os.name").toLowerCase();
                    ProcessBuilder pb;
                    if (os.contains("win")) {
                        pb = new ProcessBuilder("explorer.exe", localFile.getAbsolutePath());
                    } else if (os.contains("mac")) {
                        pb = new ProcessBuilder("open", localFile.getAbsolutePath());
                    } else {
                        pb = new ProcessBuilder("xdg-open", localFile.getAbsolutePath());
                    }
                    pb.start();
                    System.out.println("Opening folder: " + localFile.getAbsolutePath());
                    return true;
                } else {
                    // Launch executable/file
                    ProcessBuilder pb = new ProcessBuilder(localFile.getAbsolutePath());
                    if (localFile.getParentFile() != null) {
                        pb.directory(localFile.getParentFile());
                    }
                    pb.start();
                    System.out.println("Launching local file: " + localFile.getAbsolutePath());
                    return true;
                }
            }

            // URL or protocol-based link
            String normalizedUrl = normalizeLinkUrl(trimmed);
            URI uri = new URI(normalizedUrl);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(uri);
                System.out.println("Opening URL: " + normalizedUrl);
                return true;
            }
            System.err.println("Desktop browsing not supported on this system");
            return false;

        } catch (Exception e) {
            System.err.println("Failed to open link: " + e.getMessage());
            return false;
        }
    }

    private String normalizeLinkUrl(String link) {
        String trimmed = link.trim();
        String lower = trimmed.toLowerCase();

        if (lower.startsWith("http://") || lower.startsWith("https://") || lower.startsWith("steam://") || lower.contains("://")) {
            return trimmed;
        }

        // fall back to http when no scheme is provided
        return "https://" + trimmed;
    }

    /**
     * Opens the URL in the default web browser (maintains compatibility).
     * @return true if opened successfully, false otherwise
     */
    public boolean openInBrowser() {
        return open();
    }
    
    /**
     * Validates that the URL is properly formatted
     * @return true if valid, false otherwise
     */
    public boolean isValidUrl() {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        String normalized = normalizeLinkUrl(url);
        if (normalized.toLowerCase().startsWith("steam://")) {
            return true;
        }

        try {
            new URL(normalized).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets a shortened display version of the URL
     * @return Shortened URL string
     */
    public String getShortUrl() {
        if (url == null) return "";

        String formatted = normalizeLinkUrl(url);
        if (formatted.length() > 50) {
            return formatted.substring(0, 47) + "...";
        }
        return formatted;
    }
    
    // Getters and Setters
    
    public String getLinkId() {
        return linkId;
    }
    
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
    
    public String getGameId() {
        return gameId;
    }
    
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "GameHyperlink [linkId=" + linkId + ", gameId=" + gameId + 
               ", name=" + name + ", url=" + url + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameHyperlink that = (GameHyperlink) obj;
        return linkId != null && linkId.equals(that.linkId);
    }
    
    @Override
    public int hashCode() {
        return linkId != null ? linkId.hashCode() : 0;
    }
}
