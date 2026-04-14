package com.gamelauncher.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStream;

/**
 * Main application class for Tony Game Launcher - Java Edition.
 * Entry point for the JavaFX application.
 */
public class 
GameLauncherApp extends Application {
    
    private static final String APP_TITLE = "Tony Game Launcher - Java Edition";
    private static final double MIN_WIDTH = 1000;
    private static final double MIN_HEIGHT = 700;
    private static final double DEFAULT_WIDTH = 1300;
    private static final double DEFAULT_HEIGHT = 850;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_view.fxml"));
        Parent root = loader.load();
        
        // Create the scene
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        
        // Add stylesheet
        String cssPath = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        
        // Configure the stage
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        
        // Try to set application icon
        try {
            InputStream iconStream = getClass().getResourceAsStream("/icon.jpg");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            }
        } catch (Exception e) {
            System.out.println("Could not load application icon: " + e.getMessage());
        }
        
        // Show the stage
        primaryStage.show();
        
        System.out.println("Game Launcher started successfully!");
    }
    
    @Override
    public void stop() {
        // Cleanup when application closes
        System.out.println("Game Launcher shutting down...");
    }
    
    /**
     * Main entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Ensure data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        launch(args);
    }
}
