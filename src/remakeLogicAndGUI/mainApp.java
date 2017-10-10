package remakeLogicAndGUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class mainApp extends Application{
	
	static BoardGUI b = new BoardGUI(6,8,3);
	
	public Parent createContent()
	{
		Pane root = new Pane();
		root.setPrefSize(b.numberOfColumns*100, b.numberOfRows*100);
		for(int i=0;i<b.numberOfRows;i+=1)
		{
			for(int j=0;j<b.numberOfColumns;j+=1)
			{	
				b.board[i][j].setTranslateX(j*100);
				b.board[i][j].setTranslateY(i*100);
				
				root.getChildren().add(b.board[i][j]);
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
