import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class FlagGrid extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a GridPane with spacing and padding
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);  // Horizontal gap between images
        gridPane.setVgap(20);  // Vertical gap between images
        gridPane.setPadding(new Insets(20));  // Padding around the grid

        ImageView flag1 = createImageView("flag1.gif");
        ImageView flag2 = createImageView("flag2.gif");
        ImageView flag6 = createImageView("flag6.gif");
        ImageView flag7 = createImageView("flag7.gif");

        // Add images to the grid (column, row)
        gridPane.add(flag1, 0, 0);
        gridPane.add(flag2, 1, 0);
        gridPane.add(flag6, 0, 1);
        gridPane.add(flag7, 1, 1);

        // Create scene and stage
        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setTitle("Four Flags in Grid Pane");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper method to create an ImageView with consistent sizing
    private ImageView createImageView(String path) {
        try {
            // Use file: protocol for local files in same directory
            Image image = new Image("file:" + path);
            
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(250);   // Set width
            imageView.setFitHeight(150);  // Set height
            imageView.setPreserveRatio(true);  // Maintain aspect ratio
            return imageView;
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            e.printStackTrace();
            return new ImageView();  // Return empty view if loading fails
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}