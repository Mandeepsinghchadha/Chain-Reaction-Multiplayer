package tryAnimation;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FxTransitionExample6 extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) 
	{
		// Create the Rectangle
		Rectangle rect = new Rectangle(50, 50, Color.GREEN);

		// Create the Path
		Circle path = new Circle(150, 150, 150);
		path.setFill(null);
		path.setStroke(Color.RED);

		// Create the VBox
		VBox root = new VBox(rect, path);
		// Set the Style-properties of the VBox
		root.setStyle("-fx-padding: 10;" +
				"-fx-border-style: solid inside;" +
				"-fx-border-width: 2;" +
				"-fx-border-insets: 5;" +
				"-fx-border-radius: 5;" +
				"-fx-border-color: blue;");
		
		// Create the Scene
		Scene scene = new Scene(root);
		// Add the Scene to the Stage
		stage.setScene(scene);
		// Set the Title of the Stage
		stage.setTitle("A Path Transition Example");
		// Display the Stage
		stage.show();
		
		// Set up a Path Transition for the Rectangle
		PathTransition trans = new PathTransition(Duration.seconds(2), path, rect);
		trans.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		// Let the animation run forever
		trans.setCycleCount(FadeTransition.INDEFINITE);
		// Reverse direction on alternating cycles
		trans.setAutoReverse(true);
		// Play the Animation
		trans.play();
	}
}
