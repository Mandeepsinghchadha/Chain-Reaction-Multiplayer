package tryAnimation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FxTransitionExample3 extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args);
	}
	
	@Override
	public void start(Stage stage) 
	{
		// Create the Text
		Text text = new Text("A Translate Transition Example");
		text.setFont(Font.font(36));

		// Create the VBox
		VBox root = new VBox(text);
		// Set the Size of the VBox
		root.setPrefSize(500, 100);
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
		// Set the Title
		stage.setTitle("Scrolling Text using a Translate Transition");
		// Display the Stage
		stage.show();

		// Set up a Translate Transition for the Text object
		TranslateTransition trans = new TranslateTransition(Duration.seconds(2), text);
		trans.setFromX(scene.getWidth());
		trans.setToX(-1.0 * text.getLayoutBounds().getWidth());
		// Let the animation run forever
		trans.setCycleCount(1);
		// Reverse direction on alternating cycles
		trans.setAutoReverse(false);
		// Play the Animation
		trans.play();		
	}
}
