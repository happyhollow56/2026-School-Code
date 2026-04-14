package com.gamelauncher.ui;

import com.gamelauncher.model.GameLibrary;
import com.gamelauncher.model.PCGame;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.UUID;

/**
 * Controller for the Add Game dialog.
 * Handles user input for adding a new game to the library.
 */
public class AddGameController {
    
    @FXML private TextField titleField;
    @FXML private TextField executablePathField;
    @FXML private TextField installDirField;
    @FXML private TextField platformField;
    @FXML private TextField imagePathField;
    @FXML private TextArea descriptionArea;
    
    private Stage dialogStage;
    private GameLibrary gameLibrary;
    private PCGame existingGame;
    private boolean saved = false;
    
    /**
     * Set the dialog stage reference
     * @param dialogStage The stage for this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    /**
     * Set the game library reference
     * @param gameLibrary The game library to add games to
     */
    public void setGameLibrary(GameLibrary gameLibrary) {
        this.gameLibrary = gameLibrary;
    }

    public void setExistingGame(PCGame game) {
        this.existingGame = game;
        if (game != null) {
            titleField.setText(game.getTitle());
            executablePathField.setText(game.getExecutablePath());
            installDirField.setText(game.getInstallDirectory() != null ? game.getInstallDirectory() : "");
            platformField.setText(game.getPlatform() != null ? game.getPlatform() : "Standalone");
            imagePathField.setText(game.getCustomImagePath() != null ? game.getCustomImagePath() : "");
            descriptionArea.setText(game.getDescription() != null ? game.getDescription() : "");
        }
    }
    
    /**
     * Check if the game was saved
     * @return true if saved, false otherwise
    */
    public boolean isSaved() {
        return saved;
    }
    
    /**
     * Initialize the controller
     */
    @FXML
    private void initialize() {
        // Set default platform
        platformField.setText("Standalone");
    }
    
    /**
     * Handle browse button for executable
     */
    @FXML
    private void handleBrowseExecutable() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Game Executable");
        
        // Add filters for executables
        FileChooser.ExtensionFilter exeFilter = 
            new FileChooser.ExtensionFilter("Executable Files", "*.exe");
        FileChooser.ExtensionFilter allFilter = 
            new FileChooser.ExtensionFilter("All Files", "*.*");
        
        fileChooser.getExtensionFilters().addAll(exeFilter, allFilter);
        
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            executablePathField.setText(selectedFile.getAbsolutePath());
            
            // Auto-fill install directory if empty
            if (installDirField.getText().isEmpty()) {
                installDirField.setText(selectedFile.getParent());
            }
            
            // Auto-fill title if empty
            if (titleField.getText().isEmpty()) {
                String fileName = selectedFile.getName();
                // Remove .exe extension
                if (fileName.toLowerCase().endsWith(".exe")) {
                    fileName = fileName.substring(0, fileName.length() - 4);
                }
                // Clean up the name
                fileName = fileName.replaceAll("[_-]", " ");
                fileName = toTitleCase(fileName);
                titleField.setText(fileName);
            }
        }
    }
    
    /**
     * Handle browse button for install directory
     */
    @FXML
    private void handleBrowseDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Installation Directory");
        
        // Use a directory chooser instead
        javafx.stage.DirectoryChooser dirChooser = new javafx.stage.DirectoryChooser();
        dirChooser.setTitle("Select Installation Directory");
        
        File selectedDir = dirChooser.showDialog(dialogStage);
        if (selectedDir != null) {
            installDirField.setText(selectedDir.getAbsolutePath());
        }
    }
    
    /**
     * Handle browse button for custom image
     */
    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Custom Game Image");
        
        // Add filters for image files
        FileChooser.ExtensionFilter jpgFilter = 
            new FileChooser.ExtensionFilter("JPG Images", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter imageFilter = 
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        FileChooser.ExtensionFilter allFilter = 
            new FileChooser.ExtensionFilter("All Files", "*.*");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, imageFilter, allFilter);
        
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }
    
    /**
     * Handle save button
     */
    @FXML
    private void handleSave() {
        // Validate inputs
        String title = titleField.getText().trim();
        String executablePath = executablePathField.getText().trim();
        String installDir = installDirField.getText().trim();
        String platform = platformField.getText().trim();
        String imagePath = imagePathField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        // Validation
        if (title.isEmpty()) {
            showError("Title is required.");
            return;
        }
        
        if (executablePath.isEmpty()) {
            showError("Executable path is required.");
            return;
        }
        
        // Check if executable exists
        File exeFile = new File(executablePath);
        if (!exeFile.exists()) {
            showWarning("The specified executable file does not exist. The game will be added but may not launch correctly.");
        }
        
        if (existingGame != null) {
            // Edit existing game
            existingGame.setTitle(title);
            existingGame.setExecutablePath(executablePath);
            existingGame.setDescription(description.isEmpty() ? null : description);
            existingGame.setInstallDirectory(installDir.isEmpty() ? null : installDir);
            existingGame.setPlatform(platform.isEmpty() ? "Standalone" : platform);
            existingGame.setCustomImagePath(imagePath.isEmpty() ? null : imagePath);

            boolean updated = gameLibrary.updateGame(existingGame);
            if (updated) {
                saved = true;
                dialogStage.close();
            } else {
                showError("Failed to update the game.");
            }

        } else {
            // Create and add new game
            PCGame game = new PCGame();
            game.setGameId(UUID.randomUUID().toString().substring(0, 8));
            game.setTitle(title);
            game.setExecutablePath(executablePath);
            game.setDescription(description.isEmpty() ? null : description);
            game.setInstallDirectory(installDir.isEmpty() ? null : installDir);
            game.setPlatform(platform.isEmpty() ? "Standalone" : platform);
            game.setCustomImagePath(imagePath.isEmpty() ? null : imagePath);

            boolean added = gameLibrary.addGame(game);
            if (added) {
                saved = true;
                dialogStage.close();
            } else {
                showError("A game with this ID already exists.");
            }
        }
    }
    
    /**
     * Handle cancel button
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    /**
     * Convert string to title case
     */
    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
                titleCase.append(c);
            } else if (nextTitleCase) {
                titleCase.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                titleCase.append(Character.toLowerCase(c));
            }
        }
        
        return titleCase.toString();
    }
    
    /**
     * Show error alert
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show warning alert
     */
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
