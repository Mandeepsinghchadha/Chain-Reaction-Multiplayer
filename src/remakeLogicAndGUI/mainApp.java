package remakeLogicAndGUI;

import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class mainApp extends Application{
	
	BoardGUI b; 
	int numRows,numCols,numPlayers;
	Scene menu, game;
	Stage window;
	
	public void createMenu() {
		Button playButton = new Button("Play");
		Button settingsButton = new Button("Settings");
		
		playButton.setAlignment(Pos.CENTER);
		
		playButton.setOnAction(event -> {
			this.b = new BoardGUI(numRows,numCols,numPlayers);
			game=new Scene(this.createContent());
			window.setScene(game);
		});
		GridPane layout = new GridPane();
		GridPane.setHalignment(settingsButton, HPos.CENTER);
		GridPane.setHalignment(playButton, HPos.CENTER);
		layout.setVgap(4);
	    layout.setHgap(10);
	    layout.setPadding(new Insets(40, 40, 40, 40));
		layout.add(settingsButton,5,62);
		layout.add(playButton,5,70);
		
		TextField rows=new TextField(""+numRows+"");
		rows.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if(newValue.length()>0)
					numRows=Integer.parseInt(newValue);
			} catch(java.lang.NumberFormatException e) {
				System.out.println("Invalid value");
			}
		});
		layout.add(new Label("Number of Rows"), 1, 34);
		layout.add(rows,1,36);
		
		TextField cols=new TextField(""+numCols+"");
		cols.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if(newValue.length()>0)
					numCols=Integer.parseInt(newValue);
			} catch(java.lang.NumberFormatException e) {
				System.out.println("Invalid value");
			}
		});
		layout.add(new Label("Number of Columns"), 6, 34);
		layout.add(cols,6,36);
		
		TextField players=new TextField(""+numPlayers+"");	
		players.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if(newValue.length()>0)
					numPlayers=Integer.parseInt(newValue);
			} catch(java.lang.NumberFormatException e) {
				System.out.println("Invalid value");
			}
		});		
		layout.add(new Label("Number of Players"), 5, 42);
		layout.add(players,5,44);

		
		UnaryOperator<Change> filter = value -> {
            if (value.isAdded()) {
                String s = value.getText();
                if(s.length()==0) {
                		value.setText("-1");
                		return value;
                }
                if (s.matches("[0-9]*")) {
                    return value ;
                }
                String s1="";
                for(int i=0;i<s.length();++i)
                		if(s.charAt(i)>='0' && s.charAt(i)<='9')
                			s1+=s.charAt(i);
                value.setText(s1);

            }
            return value ;
        };
        rows.setTextFormatter(new TextFormatter<String>(filter));
        cols.setTextFormatter(new TextFormatter<String>(filter));
        players.setTextFormatter(new TextFormatter<String>(filter));
		
		menu=new Scene(layout,540,480);
		menu.getStylesheets().add("style.css");
	}
	
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
		
		BorderPane ret=new BorderPane();
		HBox menubar=new HBox(10);
		menubar.setPadding(new Insets(10));
		Button backButton = new Button("Back to Menu");
		backButton.setOnAction(event -> {
			this.createMenu();
			window.setScene(menu);
		});
		Pane spacer = new Pane();
		backButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button undoButton = new Button("Undo");
		undoButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		menubar.getChildren().addAll(backButton,spacer,undoButton);
		ret.setTop(menubar);
		ret.setBottom(root);
		//ret.getChildren().addAll(root,backButton,undoButton);
		//root.getChildren().add(backButton);
		//root.getChildren().add(undoButton);
		return ret;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chain Reaction");
		this.window=primaryStage;
		
		numRows=5;
		numCols=5;
		numPlayers=2;
		
		this.createMenu();
		primaryStage.setScene(menu);
		primaryStage.show();
		this.b = new BoardGUI(numRows,numCols,numPlayers);
		game=new Scene(this.createContent());
	}

}
