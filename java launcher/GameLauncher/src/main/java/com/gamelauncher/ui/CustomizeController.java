package com.gamelauncher.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller for the UI Customization Dialog
 * Allows users to customize all UI colors in real-time
 */
public class CustomizeController {
    
    @FXML private ColorPicker textColorPicker;
    @FXML private ColorPicker bgColorPicker;
    @FXML private ColorPicker cardBgColorPicker;
    @FXML private ColorPicker cardBorderColorPicker;
    @FXML private ColorPicker accentColorPicker;
    @FXML private ColorPicker headerBgColorPicker;
    @FXML private ColorPicker headerTextColorPicker;
    @FXML private ColorPicker sidebarBgColorPicker;
    @FXML private ColorPicker buttonBgColorPicker;
    @FXML private ColorPicker deleteButtonColorPicker;
    
    @FXML private Label textColorPreview;
    @FXML private Label bgColorPreview;
    @FXML private Label cardBgColorPreview;
    @FXML private Label cardBorderColorPreview;
    @FXML private Label accentColorPreview;
    @FXML private Label headerBgColorPreview;
    @FXML private Label headerTextColorPreview;
    @FXML private Label sidebarBgColorPreview;
    @FXML private Label buttonBgColorPreview;
    @FXML private Label deleteButtonColorPreview;
    
    private MainController mainController;
    private Stage dialogStage;
    
    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Initialize all ColorPickers with default values
        textColorPicker.setValue(Color.web("#FFFFFF"));
        bgColorPicker.setValue(Color.web("#1a1a2e"));
        cardBgColorPicker.setValue(Color.web("#16213e"));
        cardBorderColorPicker.setValue(Color.web("#0f3460"));
        accentColorPicker.setValue(Color.web("#e94560"));
        headerBgColorPicker.setValue(Color.web("#16213e"));
        headerTextColorPicker.setValue(Color.web("#e94560"));
        sidebarBgColorPicker.setValue(Color.web("#16213e"));
        buttonBgColorPicker.setValue(Color.web("#4CAF50"));
        deleteButtonColorPicker.setValue(Color.web("#f44336"));
        
        // Add listeners to update preview colors in real-time
        textColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        bgColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        cardBgColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        cardBorderColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        accentColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        headerBgColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        headerTextColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        sidebarBgColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        buttonBgColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        deleteButtonColorPicker.valueProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        
        // Initial preview update
        updatePreview();
    }
    
    /**
     * Update preview labels with current color picker values
     */
    private void updatePreview() {
        textColorPreview.setStyle("-fx-background-color: " + toHexString(textColorPicker.getValue()) + "; -fx-text-fill: black; -fx-padding: 5;");
        bgColorPreview.setStyle("-fx-background-color: " + toHexString(bgColorPicker.getValue()) + "; -fx-border-color: #888; -fx-padding: 5;");
        cardBgColorPreview.setStyle("-fx-background-color: " + toHexString(cardBgColorPicker.getValue()) + "; -fx-border-color: #888; -fx-padding: 5;");
        cardBorderColorPreview.setStyle("-fx-background-color: " + toHexString(cardBorderColorPicker.getValue()) + "; -fx-border-color: #888; -fx-padding: 5;");
        accentColorPreview.setStyle("-fx-background-color: " + toHexString(accentColorPicker.getValue()) + "; -fx-text-fill: white; -fx-padding: 5;");
        headerBgColorPreview.setStyle("-fx-background-color: " + toHexString(headerBgColorPicker.getValue()) + "; -fx-border-color: #888; -fx-padding: 5;");
        headerTextColorPreview.setStyle("-fx-background-color: " + toHexString(headerTextColorPicker.getValue()) + "; -fx-text-fill: white; -fx-padding: 5;");
        sidebarBgColorPreview.setStyle("-fx-background-color: " + toHexString(sidebarBgColorPicker.getValue()) + "; -fx-border-color: #888; -fx-padding: 5;");
        buttonBgColorPreview.setStyle("-fx-background-color: " + toHexString(buttonBgColorPicker.getValue()) + "; -fx-text-fill: white; -fx-padding: 5;");
        deleteButtonColorPreview.setStyle("-fx-background-color: " + toHexString(deleteButtonColorPicker.getValue()) + "; -fx-text-fill: white; -fx-padding: 5;");
    }
    
    /**
     * Convert Color to hex string format
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    /**
     * Convert Color to RGB string format
     */
    private String toRgbString(Color color) {
        return String.format("rgb(%d,%d,%d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    /**
     * Set the main controller reference
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    /**
     * Set the dialog stage reference
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }
    
    /**
     * Handle reset to defaults button
     */
    @FXML
    private void handleResetDefaults() {
        textColorPicker.setValue(Color.web("#FFFFFF"));
        bgColorPicker.setValue(Color.web("#1a1a2e"));
        cardBgColorPicker.setValue(Color.web("#16213e"));
        cardBorderColorPicker.setValue(Color.web("#0f3460"));
        accentColorPicker.setValue(Color.web("#e94560"));
        headerBgColorPicker.setValue(Color.web("#16213e"));
        headerTextColorPicker.setValue(Color.web("#e94560"));
        sidebarBgColorPicker.setValue(Color.web("#16213e"));
        buttonBgColorPicker.setValue(Color.web("#4CAF50"));
        deleteButtonColorPicker.setValue(Color.web("#f44336"));
        updatePreview();
    }
    
    /**
     * Handle cancel button
     */
    @FXML
    private void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
    
    /**
     * Handle apply button - apply all colors to main UI
     */
    @FXML
    private void handleApply() {
        if (mainController != null) {
            mainController.applyCustomTheme(
                toHexString(textColorPicker.getValue()),
                toHexString(bgColorPicker.getValue()),
                toHexString(cardBgColorPicker.getValue()),
                toHexString(cardBorderColorPicker.getValue()),
                toHexString(accentColorPicker.getValue()),
                toHexString(headerBgColorPicker.getValue()),
                toHexString(headerTextColorPicker.getValue()),
                toHexString(sidebarBgColorPicker.getValue()),
                toHexString(buttonBgColorPicker.getValue()),
                toHexString(deleteButtonColorPicker.getValue())
            );
        }
        
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
