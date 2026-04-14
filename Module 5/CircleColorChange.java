import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CircleColorChange extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a circle with initial white color
        Circle circle = new Circle(100);  // Radius of 100
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);    // Black border to see the circle
        circle.setStrokeWidth(2);

        // Set up mouse pressed event (button down) - turn black
        circle.setOnMousePressed(event -> {
            circle.setFill(Color.BLACK);
        });

        // Set up mouse released event (button up) - turn white
        circle.setOnMouseReleased(event -> {
            circle.setFill(Color.WHITE);
        });

        // Create layout and add circle
        StackPane root = new StackPane();
        root.getChildren().add(circle);

        // Create scene
        Scene scene = new Scene(root, 400, 400);
        
        primaryStage.setTitle("Circle Color Change");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

        launch(args);
    }
}