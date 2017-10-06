package gameGUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class gridLayout extends Application{

	public Parent createContent()
	{
		Pane root = new Pane();
		root.setPrefSize(900, 600);
		
		for(int i=0;i<6;i+=1)
		{
			for(int j=0;j<9;j+=1)
			{
				Tile tile = new Tile(i,j);
				tile.setTranslateX(j*100);
				tile.setTranslateY(i*100);
				
				root.getChildren().add(tile);
			}
		}
		
		return root;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setScene(new Scene(this.createContent()));
		primaryStage.show();
	}

}
