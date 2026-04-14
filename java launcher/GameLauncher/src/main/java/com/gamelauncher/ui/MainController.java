package com.gamelauncher.ui;

import com.gamelauncher.model.Game;
import com.gamelauncher.model.GameHyperlink;
import com.gamelauncher.model.GameLibrary;
import com.gamelauncher.model.PCGame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;import javafx.scene.paint.Color;import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main controller for the Tony Game Launcher - Java Edition UI.
 * Handles user interactions and manages the game library display.
 */
public class MainController {
    
    @FXML private TextField searchField;
    @FXML private Label statusLabel;
    @FXML private Label gameCountLabel;
    @FXML private FlowPane gamesContainer;
    @FXML private VBox detailsPanel;
    @FXML private FlowPane customCategoriesBox;
    @FXML private VBox linksContainer;
    @FXML private ComboBox<String> launchTypeCombo;
    @FXML private ChoiceBox<String> primaryLinkChoice;
    
    @FXML private Button removeButton;
    @FXML private Button openFolderButton;
    @FXML private Button launchButton;
    @FXML private Button addToCategoryButton;
    @FXML private Button removeFromCategoryButton;
    @FXML private Button removeCategoryButton;
    
    @FXML private Label detailTitle;
    @FXML private Label detailPlatform;
    @FXML private Label detailDescription;
    @FXML private Label detailPlaytime;
    @FXML private Label detailLastPlayed;
    @FXML private ImageView gameIconLabel;
    @FXML private ImageView headerIconView;
    
    @FXML private ToggleButton allGamesBtn;
    @FXML private ToggleButton favoritesBtn;
    @FXML private ToggleButton recentBtn;

    @FXML private ColorPicker textColorPicker;
    @FXML private ColorPicker bgColorPicker;
    @FXML private BorderPane rootPane;
    
    private GameLibrary gameLibrary;
    private PCGame selectedGame;
    private ToggleGroup categoryGroup;
    
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Initialize library
        gameLibrary = new GameLibrary("data/gamelibrary.dat");
        
        // Load default header icon
        try {
            Image headerIcon = new Image(getClass().getResource("/Icon.jpg").toExternalForm());
            headerIconView.setImage(headerIcon);
        } catch (Exception e) {
            // If icon fails to load, just leave it empty
            System.out.println("Could not load header icon: " + e.getMessage());
        }
        
        // Setup category toggle group
        categoryGroup = new ToggleGroup();
        allGamesBtn.setToggleGroup(categoryGroup);
        favoritesBtn.setToggleGroup(categoryGroup);
        recentBtn.setToggleGroup(categoryGroup);
        
        // Load custom categories
        loadCustomCategories();
        
        // Initialize theme pickers with current stylesheet colors
        textColorPicker.setValue(Color.WHITE);
        bgColorPicker.setValue(Color.web("#1a1a2e"));
        
        // Display all games initially
        refreshGameDisplay();
        
        // Setup search field listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            handleSearch();
        });
        
        // Setup launch type combo
        launchTypeCombo.getItems().addAll("EXE", "LINK");
        launchTypeCombo.setOnAction(e -> handleLaunchTypeChange());
        
        // Setup primary link choice
        primaryLinkChoice.setOnAction(e -> handlePrimaryLinkChange());
        
        updateStatus("Ready - " + gameLibrary.getGameCount() + " games in library");
    }
    
    /**
     * Refresh the game grid display
     */
    private void refreshGameDisplay() {
        gamesContainer.getChildren().clear();
        
        List<Game> games = getCurrentGameList();
        
        for (Game game : games) {
            if (game instanceof PCGame) {
                gamesContainer.getChildren().add(createGameCard((PCGame) game));
            }
        }
        
        gameCountLabel.setText(gameLibrary.getGameCount() + " games");

        String selectedCategory = getSelectedCategory();
        boolean categoryAssignable = selectedGame != null && !selectedCategory.equals("All Games") && !selectedCategory.equals("Recently Played");
        addToCategoryButton.setDisable(!categoryAssignable);
        removeFromCategoryButton.setDisable(!categoryAssignable || !selectedGame.hasCategory(selectedCategory));
        removeCategoryButton.setDisable(selectedCategory == null || selectedCategory.isEmpty());
    }
    
    /**
     * Get the current list of games based on selected category/search
     */
    private List<Game> getCurrentGameList() {
        ToggleButton selected = (ToggleButton) categoryGroup.getSelectedToggle();

        String selectedCategory = getSelectedCategory();

        if (selected == recentBtn) {
            return new ArrayList<>(gameLibrary.getRecentlyPlayed());
        }

        List<Game> categoryGames = gameLibrary.getGamesByCategory(selectedCategory);

        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            return categoryGames;
        } else {
            String lowerSearch = searchText.toLowerCase();
            return categoryGames.stream()
                    .filter(g -> g.getTitle().toLowerCase().contains(lowerSearch))
                    .collect(Collectors.toList());
        }
    }

    private String normalizeCategoryName(String category) {
        if (category == null) {
            return "";
        }
        String trimmed = category.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        // Remove leading non-alphanumeric symbols (emoji/punctuation) and normalize spacing
        trimmed = trimmed.replaceAll("^[^A-Za-z0-9]+", "").trim();

        return trimmed.replaceAll("\s+", " ");
    }

    private String getSelectedCategory() {
        ToggleButton selected = (ToggleButton) categoryGroup.getSelectedToggle();
        if (selected == null) {
            return "All Games";
        }

        String text = selected.getText();
        String normalized = normalizeCategoryName(text);
        if (normalized.isEmpty()) {
            return "All Games";
        }

        return normalized;
    }
    
    /**
     * Create a game card for the grid
     */
    private VBox createGameCard(PCGame game) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setMaxWidth(170);
        card.setPrefWidth(150);
        card.setMinWidth(140);
        card.setMaxHeight(200);
        card.setPrefHeight(180);
        card.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; " +
                      "-fx-border-color: #0f3460; -fx-border-radius: 10; -fx-cursor: hand;");
        
        // Game icon
        ImageView iconView = new ImageView();
        String imagePath = game.getCustomImagePath() != null && !game.getCustomImagePath().trim().isEmpty() 
                          ? game.getCustomImagePath() : "/Icon.jpg";
        try {
            Image iconImage = new Image(getClass().getResource(imagePath).toExternalForm());
            iconView.setImage(iconImage);
            iconView.setFitWidth(40);
            iconView.setFitHeight(40);
            iconView.setPreserveRatio(true);
        } catch (Exception e) {
            // Fallback to emoji if image fails to load
            Label fallbackLabel = new Label("🎮");
            fallbackLabel.setStyle("-fx-font-size: 40px;");
            iconView = null; // Will use fallbackLabel instead
        }
        VBox.setVgrow(iconView != null ? iconView : new Label("🎮"), Priority.NEVER);
        
        // Title
        Label titleLabel = new Label(game.getTitle());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setMinWidth(120);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        VBox.setVgrow(titleLabel, Priority.NEVER);
        
        // Platform
        Label platformLabel = new Label(game.getPlatform() != null ? game.getPlatform() : "PC");
        platformLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 10px;");
        VBox.setVgrow(platformLabel, Priority.NEVER);
        
        // Launch button
        Button launchBtn = new Button("▶ Launch");
        launchBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; " +
                           "-fx-font-weight: bold; -fx-background-radius: 12; -fx-font-size: 11px;");
        launchBtn.setMaxWidth(Double.MAX_VALUE);
        launchBtn.setOnAction(e -> {
            e.consume();
            launchGame(game);
        });
        VBox.setVgrow(launchBtn, Priority.NEVER);
        
        // Add children to card
        if (iconView != null) {
            card.getChildren().addAll(iconView, titleLabel, platformLabel, launchBtn);
        } else {
            Label fallbackLabel = new Label("🎮");
            fallbackLabel.setStyle("-fx-font-size: 40px;");
            card.getChildren().addAll(fallbackLabel, titleLabel, platformLabel, launchBtn);
        }
        
        // Hover effects
        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 10; " +
                          "-fx-border-color: #e94560; -fx-border-radius: 10; -fx-cursor: hand;");
        });
        
        card.setOnMouseExited(e -> {
            if (game != selectedGame) {
                card.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; " +
                              "-fx-border-color: #0f3460; -fx-border-radius: 10; -fx-cursor: hand;");
            }
        });
        
        // Click to select
        card.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                selectGame(game, card);
            }
        });
        
        return card;
    }
    
    /**
     * Select a game and show its details
     */
    @FXML
    private void applyTheme() {
        String textColor = toRgbString(textColorPicker.getValue());
        String bgColor = toRgbString(bgColorPicker.getValue());

        // apply to root and labels/buttons in UI
        rootPane.setStyle("-fx-background-color: " + bgColor + ";");
        detailsPanel.setStyle("-fx-background-color: " + bgColor + ";");

        // global body style
        Scene scene = rootPane.getScene();
        if (scene != null) {
            scene.getRoot().setStyle("-fx-base: " + bgColor + "; -fx-text-fill: " + textColor + ";");
        }
        
        // Apply to key text controls
        detailTitle.setStyle("-fx-text-fill: " + textColor + ";");
        detailPlatform.setStyle("-fx-text-fill: " + textColor + ";");
        detailDescription.setStyle("-fx-text-fill: " + textColor + ";");
        detailPlaytime.setStyle("-fx-text-fill: " + textColor + ";");
        detailLastPlayed.setStyle("-fx-text-fill: " + textColor + ";");
    }

    private String toRgbString(Color color) {
        return String.format("rgb(%d,%d,%d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    /**
     * Open the customization dialog
     */
    @FXML
    private void handleOpenCustomizeDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customize_dialog.fxml"));
            Parent root = loader.load();
            CustomizeController controller = loader.getController();
            controller.setMainController(this);
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("UI Customization");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(rootPane.getScene().getWindow());
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            updateStatus("Error opening customization dialog");
        }
    }
    
    /**
     * Apply custom theme with all color parameters
     */
    public void applyCustomTheme(String textColor, String bgColor, String cardBgColor, String cardBorderColor,
                                  String accentColor, String headerBgColor, String headerTextColor,
                                  String sidebarBgColor, String buttonBgColor, String deleteButtonColor) {
        // Apply root and main panel colors
        rootPane.setStyle("-fx-background-color: " + bgColor + ";");
        detailsPanel.setStyle("-fx-background-color: " + bgColor + ";");
        
        // Apply text colors
        detailTitle.setStyle("-fx-text-fill: " + textColor + ";");
        detailPlatform.setStyle("-fx-text-fill: " + textColor + ";");
        detailDescription.setStyle("-fx-text-fill: " + textColor + ";");
        detailPlaytime.setStyle("-fx-text-fill: " + textColor + ";");
        detailLastPlayed.setStyle("-fx-text-fill: " + textColor + ";");
        
        // Update all game cards with new colors
        for (Node node : gamesContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                PCGame game = null;
                
                // Find the game associated with this card
                for (Game g : gameLibrary.getAllGames()) {
                    if (g instanceof PCGame) {
                        // This is a simplified approach - we'll update the style regardless
                        break;
                    }
                }
                
                // Update card style
                card.setStyle("-fx-background-color: " + cardBgColor + "; -fx-background-radius: 10; " +
                              "-fx-border-color: " + cardBorderColor + "; -fx-border-radius: 10; -fx-cursor: hand;");
                
                // Update launch buttons within the card
                for (Node cardChild : card.getChildren()) {
                    if (cardChild instanceof Button) {
                        Button btn = (Button) cardChild;
                        if (btn.getText().contains("Launch")) {
                            btn.setStyle("-fx-background-color: " + accentColor + "; -fx-text-fill: white; " +
                                       "-fx-font-weight: bold; -fx-background-radius: 12; -fx-font-size: 11px;");
                        }
                    }
                }
            }
        }
        
        // Store the current colors for persistence or later use
        // TODO: Optional - save color preferences to file for next session
        
        updateStatus("Theme applied successfully");
    }

    private void selectGame(PCGame game, VBox card) {
        // Reset previous selection style
        for (Node node : gamesContainer.getChildren()) {
            node.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10; " +
                          "-fx-border-color: #0f3460; -fx-border-radius: 10;");
        }
        
        // Highlight selected
        card.setStyle("-fx-background-color: #0f3460; -fx-background-radius: 10; " +
                      "-fx-border-color: #e94560; -fx-border-radius: 10; -fx-border-width: 2;");
        
        selectedGame = game;
        showGameDetails(game);
        
        // Enable action buttons
        removeButton.setDisable(false);
        openFolderButton.setDisable(false);
        launchButton.setDisable(false);
    }
    
    /**
     * Show game details in the right panel
     */
    private void showGameDetails(PCGame game) {
        detailsPanel.setVisible(true);
        detailsPanel.setManaged(true);
        
        detailTitle.setText(game.getTitle());
        detailPlatform.setText(game.getPlatform() != null ? game.getPlatform() : "PC Game");
        detailDescription.setText(game.getDescription() != null ? game.getDescription() : "No description available.");
        detailPlaytime.setText(game.getFormattedPlaytime());
        detailLastPlayed.setText(game.getLastPlayed() != null ? 
                game.getLastPlayed().format(DATE_FORMATTER) : "Never");
        
        // Set game icon
        String imagePath = game.getCustomImagePath() != null && !game.getCustomImagePath().trim().isEmpty() 
                          ? game.getCustomImagePath() : "/Icon.jpg";
        try {
            Image iconImage = new Image(getClass().getResource(imagePath).toExternalForm());
            gameIconLabel.setImage(iconImage);
        } catch (Exception e) {
            // Fallback to default icon
            try {
                Image defaultImage = new Image(getClass().getResource("/Icon.jpg").toExternalForm());
                gameIconLabel.setImage(defaultImage);
            } catch (Exception e2) {
                // If even default fails, just leave it as is
            }
        }
        
        // Set launch type
        launchTypeCombo.setValue(game.getLaunchType().toString());
        updatePrimaryLinkChoice(game);
        
        // Load links for this game
        loadGameLinks(game.getGameId());
    }
    
    /**
     * Update the primary link choice box based on current game and launch type
     */
    private void updatePrimaryLinkChoice(PCGame game) {
        primaryLinkChoice.getItems().clear();
        if (game == null) return;
        
        for (GameHyperlink link : game.getLinks()) {
            String display = link.getName() != null && !link.getName().isEmpty() ? 
                link.getName() : link.getShortUrl();
            primaryLinkChoice.getItems().add(display);
        }
        
        // Set current primary link
        if (game.getPrimaryLinkId() != null) {
            GameHyperlink primaryLink = game.getLinkById(game.getPrimaryLinkId());
            if (primaryLink != null) {
                String display = primaryLink.getName() != null && !primaryLink.getName().isEmpty() ? 
                    primaryLink.getName() : primaryLink.getShortUrl();
                primaryLinkChoice.setValue(display);
            }
        }
        
        // Enable/disable based on launch type
        primaryLinkChoice.setDisable(!"LINK".equals(launchTypeCombo.getValue()) || primaryLinkChoice.getItems().isEmpty());
    }
    
    /**
     * Handle launch type combo box change
     */
    @FXML
    private void handleLaunchTypeChange() {
        if (selectedGame == null) return;
        
        String selectedType = launchTypeCombo.getValue();
        if ("EXE".equals(selectedType)) {
            selectedGame.setLaunchType(PCGame.LaunchType.EXE);
            selectedGame.setPrimaryLinkId(null);
        } else if ("LINK".equals(selectedType)) {
            selectedGame.setLaunchType(PCGame.LaunchType.LINK);
        }
        
        updatePrimaryLinkChoice(selectedGame);
        gameLibrary.saveToFile();
        updateStatus("Launch method updated to " + selectedType);
    }
    
    /**
     * Handle primary link choice box change
     */
    @FXML
    private void handlePrimaryLinkChange() {
        if (selectedGame == null) return;
        
        String selectedLinkDisplay = primaryLinkChoice.getValue();
        if (selectedLinkDisplay == null || selectedLinkDisplay.isEmpty()) {
            selectedGame.setPrimaryLinkId(null);
        } else {
            // Find the link by display name
            for (GameHyperlink link : selectedGame.getLinks()) {
                String display = link.getName() != null && !link.getName().isEmpty() ? 
                    link.getName() : link.getShortUrl();
                if (display.equals(selectedLinkDisplay)) {
                    selectedGame.setPrimaryLinkId(link.getLinkId());
                    break;
                }
            }
        }
        
        gameLibrary.saveToFile();
        updateStatus("Primary launch link updated");
    }
    
    /**
     * Load hyperlinks for a game
     */
    private void loadGameLinks(String gameId) {
        linksContainer.getChildren().clear();
        
        if (selectedGame == null || !selectedGame.getGameId().equals(gameId)) {
            return;
        }

        for (GameHyperlink link : selectedGame.getLinks()) {
            Hyperlink hyperlink = new Hyperlink(link.getName() == null || link.getName().isEmpty() ? link.getShortUrl() : link.getName());
            hyperlink.setStyle("-fx-text-fill: #2196F3;");
            hyperlink.setOnAction(e -> {
                if (link.open()) {
                    updateStatus("Opened link: " + link.getUrl());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Link Open Failed", "Could not open link: " + link.getUrl());
                    updateStatus("Failed to open link.");
                }
            });

            Button launchBtn = new Button("▶");
            launchBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-font-size: 10px;");
            launchBtn.setOnAction(e -> {
                if (link.open()) {
                    updateStatus("Launched via link: " + link.getUrl());
                    selectedGame.updateLastPlayed();
                    gameLibrary.saveToFile();
                    showGameDetails(selectedGame);
                    refreshGameDisplay();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Launch Link Failed", "Could not launch link: " + link.getUrl());
                    updateStatus("Failed to launch via link.");
                }
            });

            Button editBtn = new Button("✏");
            editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFC107; -fx-font-size: 10px;");
            editBtn.setOnAction(e -> handleEditLink(link));

            Button deleteBtn = new Button("🗑");
            deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F44336; -fx-font-size: 10px;");
            deleteBtn.setOnAction(e -> {
                if (selectedGame != null && selectedGame.removeLink(link.getLinkId())) {
                    gameLibrary.saveToFile();
                    loadGameLinks(gameId);
                    updateStatus("Link removed.");
                }
            });

            Button primaryBtn = new Button("⭐");
            primaryBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700; -fx-font-size: 10px;");
            primaryBtn.setTooltip(new Tooltip("Set as primary launch link"));
            primaryBtn.setOnAction(e -> {
                selectedGame.setPrimaryLinkId(link.getLinkId());
                gameLibrary.saveToFile();
                updatePrimaryLinkChoice(selectedGame);
                updateStatus("Primary launch link set to: " + (link.getName() != null && !link.getName().isEmpty() ? link.getName() : link.getShortUrl()));
            });

            HBox linkRow = new HBox(6, hyperlink, launchBtn, primaryBtn, editBtn, deleteBtn);
            linkRow.setAlignment(Pos.CENTER_LEFT);
            linksContainer.getChildren().add(linkRow);
        }
    }
    
    /**
     * Launch a game
     */
    private void launchGame(PCGame game) {
        updateStatus("Launching " + game.getTitle() + "...");
        
        boolean success = gameLibrary.launchGame(game.getGameId());
        
        if (success) {
            updateStatus(game.getTitle() + " launched successfully!");
            if (selectedGame == game) {
                showGameDetails(game);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Launch Failed", 
                     "Could not launch " + game.getTitle() + ".\nPlease check if the executable path is valid.");
            updateStatus("Failed to launch " + game.getTitle());
        }
    }
    
    // ===== Event Handlers =====
    
    @FXML
    private void handleAddGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_game_dialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Game");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            
            AddGameController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGameLibrary(gameLibrary);
            
            dialogStage.showAndWait();
            
            if (controller.isSaved()) {
                refreshGameDisplay();
                updateStatus("Game added successfully!");
            }
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open add game dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRemoveGame() {
        if (selectedGame == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove " + selectedGame.getTitle() + "?");
        confirm.setContentText("This action cannot be undone.");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameLibrary.removeGame(selectedGame.getGameId());
            selectedGame = null;
            detailsPanel.setVisible(false);
            detailsPanel.setManaged(false);
            removeButton.setDisable(true);
            openFolderButton.setDisable(true);
            launchButton.setDisable(true);
            refreshGameDisplay();
            updateStatus("Game removed successfully!");
        }
    }
    
    @FXML
    private void handleOpenFolder() {
        if (selectedGame == null) return;
        
        boolean success = selectedGame.openInstallDirectory();
        if (!success) {
            showAlert(Alert.AlertType.WARNING, "Could Not Open Folder", 
                     "The installation directory could not be opened.");
        }
    }
    
    @FXML
    private void handleLaunchGame() {
        if (selectedGame != null) {
            launchGame(selectedGame);
        }
    }
    
    @FXML
    private void handleSearch() {
        refreshGameDisplay();
    }

    @FXML
    private void handleEditPlaytime() {
        if (selectedGame == null) {
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedGame.getPlaytimeHours()));
        dialog.setTitle("Edit Playtime");
        dialog.setHeaderText("Edit playtime in hours (decimals allowed)");
        dialog.setContentText("Total hours:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return;
        }

        try {
            double hours = Double.parseDouble(result.get());
            if (hours < 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Value", "Playtime cannot be negative.");
                return;
            }
            selectedGame.setPlaytimeHours(hours);
            gameLibrary.saveToFile();
            showGameDetails(selectedGame);
            refreshGameDisplay();
            updateStatus("Playtime updated to " + selectedGame.getFormattedPlaytime() + ".");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a valid number.");
        }
    }
    
    @FXML
    private void handleCategorySelect(ActionEvent event) {
        refreshGameDisplay();
    }

    @FXML
    private void handleAddToCategory() {
        if (selectedGame == null) return;

        String category = getSelectedCategory();
        if (category == null || category.isEmpty() || category.equals("All Games") || category.equals("Recently Played")) {
            showAlert(Alert.AlertType.WARNING, "Category Selection", "Choose a custom category to assign.");
            return;
        }

        selectedGame.addCategory(category);
        gameLibrary.saveToFile();
        updateStatus("Game '" + selectedGame.getTitle() + "' added to '" + category + "'.");
        refreshGameDisplay();
    }

    @FXML
    private void handleRemoveFromCategory() {
        if (selectedGame == null) return;

        String category = getSelectedCategory();
        if (category == null || category.isEmpty() || category.equals("All Games") || category.equals("Recently Played")) {
            showAlert(Alert.AlertType.WARNING, "Category Selection", "Choose a custom category to remove.");
            return;
        }

        selectedGame.removeCategory(category);
        gameLibrary.saveToFile();
        updateStatus("Game '" + selectedGame.getTitle() + "' removed from '" + category + "'.");
        refreshGameDisplay();
    }
    
    @FXML
    private void handleRemoveCategory() {
        String selectedCategory = getSelectedCategory();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Category Selected", "Please select a category to remove.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Category Removal");
        confirm.setHeaderText("Remove category: " + selectedCategory);
        confirm.setContentText("This will remove the category from all games and remove it from the category list. Continue?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean removed = gameLibrary.removeCategory(selectedCategory);
            if (removed) {
                loadCustomCategories();
                refreshCategoryButtons();
                refreshGameDisplay();
                updateStatus("Category '" + selectedCategory + "' removed.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Category Removal", "Category could not be removed.");
            }
        }
    }

    @FXML
    private void handleAddCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Create New Category");
        dialog.setContentText("Category name:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            String normalized = normalizeCategoryName(name);
            if (normalized.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Invalid Category", "Category name cannot be empty.");
                return;
            }

            if (gameLibrary.addCategory(normalized)) {
                addCustomCategoryButton(normalized);
                refreshCategoryButtons();
                updateStatus("Category '" + normalized + "' added!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Category Exists", 
                         "A category with this name already exists.");
            }
        });
    }
    
    @FXML
    private void handleAddLink() {
        if (selectedGame == null) {
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game first.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Link");
        nameDialog.setHeaderText("Add Hyperlink");
        nameDialog.setContentText("Link name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent()) return;

        TextInputDialog urlDialog = new TextInputDialog("https://");
        urlDialog.setTitle("Add Link");
        urlDialog.setHeaderText("Enter URL");
        urlDialog.setContentText("URL:");

        Optional<String> urlResult = urlDialog.showAndWait();
        if (!urlResult.isPresent()) return;

        GameHyperlink link = new GameHyperlink(
            UUID.randomUUID().toString(),
            selectedGame.getGameId(),
            nameResult.get(),
            urlResult.get()
        );

        selectedGame.addLink(link);
        gameLibrary.saveToFile();
        loadGameLinks(selectedGame.getGameId());
        updateStatus("Link added!");
    }

    private void handleEditLink(GameHyperlink link) {
        if (selectedGame == null || link == null) return;

        TextInputDialog nameDialog = new TextInputDialog(link.getName());
        nameDialog.setTitle("Edit Link");
        nameDialog.setHeaderText("Link name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent()) return;

        TextInputDialog urlDialog = new TextInputDialog(link.getUrl());
        urlDialog.setTitle("Edit Link");
        urlDialog.setHeaderText("URL:");
        Optional<String> urlResult = urlDialog.showAndWait();
        if (!urlResult.isPresent()) return;

        link.setName(nameResult.get());
        link.setUrl(urlResult.get());
        selectedGame.addLink(link); // replace existing by ID
        gameLibrary.saveToFile();
        loadGameLinks(selectedGame.getGameId());
        updateStatus("Link updated!");
    }
    
    @FXML
    private void handleEditGame() {
        if (selectedGame == null) {
            showAlert(Alert.AlertType.WARNING, "No Game Selected", "Please select a game to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_game_dialog.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Game");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));

            AddGameController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGameLibrary(gameLibrary);
            controller.setExistingGame(selectedGame);

            dialogStage.showAndWait();

            if (controller.isSaved()) {
                gameLibrary.saveToFile();
                refreshGameDisplay();
                showGameDetails(selectedGame);
                updateStatus("Game updated successfully.");
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to open edit dialog: " + e.getMessage());
        }
    }
    
    // ===== Helper Methods =====
    
    private void loadCustomCategories() {
        customCategoriesBox.getChildren().clear();

        for (String category : gameLibrary.getCategories()) {
            if (!category.equals("All Games") && !category.equals("Favorites") && !category.equals("Recently Played")) {
                addCustomCategoryButton(category);
            }
        }

        refreshCategoryButtons();
    }

    private void refreshCategoryButtons() {
        updateButtonState(allGamesBtn, "All Games");
        updateButtonState(favoritesBtn, "Favorites");
        updateButtonState(recentBtn, "Recently Played");

        if (categoryGroup.getSelectedToggle() == null || !((ToggleButton) categoryGroup.getSelectedToggle()).isVisible()) {
            if (allGamesBtn.isVisible()) {
                allGamesBtn.setSelected(true);
            } else if (!categoryGroup.getToggles().isEmpty()) {
                categoryGroup.getToggles().get(0).setSelected(true);
            }
        }
    }

    private void updateButtonState(ToggleButton button, String categoryName) {
        if (gameLibrary.getCategories().contains(categoryName)) {
            button.setVisible(true);
            button.setManaged(true);
            if (!categoryGroup.getToggles().contains(button)) {
                button.setToggleGroup(categoryGroup);
            }
        } else {
            button.setVisible(false);
            button.setManaged(false);
            categoryGroup.getToggles().remove(button);
        }
    }
    
    private void addCustomCategoryButton(String category) {
        String normalized = normalizeCategoryName(category);
        if (normalized.isEmpty()) {
            normalized = "Unnamed Category";
        }

        ToggleButton btn = new ToggleButton(normalized);
        btn.setToggleGroup(categoryGroup);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; " +
                     "-fx-alignment: CENTER; " +
                     "-fx-wrap-text: true; -fx-text-overrun: ellipsis; " +
                     "-fx-min-width: 80; -fx-max-width: 140; " +
                     "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; " +
                     "-fx-border-color: transparent;");
        btn.setWrapText(true);
        btn.setTextOverrun(OverrunStyle.ELLIPSIS);
        btn.setTooltip(new Tooltip(normalized));
        btn.setOnAction(this::handleCategorySelect);

        customCategoriesBox.getChildren().add(btn);
    }
    
    private void updateStatus(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
