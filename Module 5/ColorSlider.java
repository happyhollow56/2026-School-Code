import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ColorSlider extends Application {

    // Text to display
    private Text text;
    
    // Sliders for RGB and opacity
    private Slider redSlider;
    private Slider greenSlider;
    private Slider blueSlider;
    private Slider opacitySlider;

    @Override
    public void start(Stage primaryStage) {
        // Create the text
        text = new Text("Show Colors");
        text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        
        // Create sliders (0-255 for RGB, 0-1 for opacity)
        redSlider = createSlider("Red", 255);
        greenSlider = createSlider("Green", 255);
        blueSlider = createSlider("Blue", 255);
        opacitySlider = createSlider("Opacity", 100); // 0-100%
        
        // Add listeners to update color when sliders change
        redSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        greenSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        blueSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        
        // Layout for sliders
        VBox sliderBox = new VBox(10);
        sliderBox.setPadding(new Insets(20));
        sliderBox.setAlignment(Pos.CENTER);
        
        // Add each slider with its label
        sliderBox.getChildren().addAll(
            createSliderRow("Red", redSlider),
            createSliderRow("Green", greenSlider),
            createSliderRow("Blue", blueSlider),
            createSliderRow("Opacity", opacitySlider)
        );
        
        // Main layout
        BorderPane root = new BorderPane();
        root.setCenter(text);
        root.setBottom(sliderBox);
        
        // Create scene
        Scene scene = new Scene(root, 500, 400);
        
        primaryStage.setTitle("Color Slider");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Initialize color
        updateColor();
    }
    
    // Create a slider with given max value
    private Slider createSlider(String name, double max) {
        Slider slider = new Slider(0, max, max / 2);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(max / 4);
        slider.setBlockIncrement(max / 10);
        slider.setPrefWidth(300);
        return slider;
    }
    
    // Create a row with label and slider
    private HBox createSliderRow(String labelText, Slider slider) {
        Label label = new Label(labelText);
        label.setPrefWidth(60);
        label.setAlignment(Pos.CENTER_RIGHT);
        
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        row.getChildren().addAll(label, slider);
        return row;
    }
    
    // Update text color based on slider values
    private void updateColor() {
        double red = redSlider.getValue() / 255.0;
        double green = greenSlider.getValue() / 255.0;
        double blue = blueSlider.getValue() / 255.0;
        double opacity = opacitySlider.getValue() / 100.0;
        
        Color color = new Color(red, green, blue, opacity);
        text.setFill(color);
    }

    public static void main(String[] args) {
        launch(args);
    }
}