package remakeLogicAndGUI;

import java.io.FileInputStream;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class mainApp extends Application{
	
	BoardGUI b;
	int numRows,numCols,numPlayers;
	Scene menu, game, settingsPage;
	Stage window;
	
	public void createSettingsPage() {
		
	}
	
	public void createMenu(){
		
		Button playButton = new Button("New Game");
		Button resumeButton = new Button("Resume");
		Button settingsButton = new Button("Settings");
		
		playButton.setAlignment(Pos.CENTER);
		
		playButton.setOnAction(event -> {
			this.b = new BoardGUI(numRows,numCols,numPlayers);
			game=new Scene(this.createContent());
			window.setScene(game);
		});
		
		settingsButton.setOnAction(event -> {
			this.createSettingsPage();
			window.setScene(settingsPage);
		});
		
		final ToggleGroup radio = new ToggleGroup();

		RadioButton small = new RadioButton("9 X 6");
		small.setToggleGroup(radio);		
		small.selectedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
		        if(isNowSelected){ 
		            numRows=9;
		            numCols=6;
		        } 
		        else {
		        		numRows=15;
		            numCols=10;
		        }
		    }
		});
		
		RadioButton large = new RadioButton("15 X 10");
		large.setToggleGroup(radio);
		
		if(numRows==15) large.setSelected(true);
		else small.setSelected(true);
		HBox radiobox=new HBox();
		radiobox.setSpacing(50);
		radiobox.getChildren().addAll(small,large);
		
		TextField players=new TextField(""+numPlayers+"");
		players.setAlignment(Pos.CENTER);
		players.textProperty().addListener((observable, oldValue, newValue) -> {
			try {
				if(newValue.length()>0)
					numPlayers=Integer.parseInt(newValue);
			} catch(java.lang.NumberFormatException e) {
				System.out.println("Invalid value");
			}
		});
		
		Button minusButton = new Button("-");
		minusButton.setOnAction(event -> {
			if(Integer.parseInt(players.getText())>2) {
				int x=Integer.parseInt(players.getText())-1;
				numPlayers=x;
				players.clear();
				players.setText(Integer.toString(x));
			}
		});
		Button plusButton = new Button("+");
		plusButton.setOnAction(event -> {
			if(Integer.parseInt(players.getText())<8) {
				int x=Integer.parseInt(players.getText())+1;
				numPlayers=x;
				players.clear();
				players.setText(Integer.toString(x));
			}
		});

		Label gridsize=new Label("Grid Size");
		Label numPlayersLabel=new Label("Number of Players");
		GridPane layout = new GridPane();
		GridPane.setHalignment(settingsButton, HPos.CENTER);
		GridPane.setHalignment(resumeButton, HPos.CENTER);
		GridPane.setHalignment(playButton, HPos.CENTER);
		GridPane.setHalignment(gridsize, HPos.CENTER);
		GridPane.setHalignment(numPlayersLabel, HPos.CENTER);
		GridPane.setHalignment(players, HPos.CENTER);
		GridPane.setHalignment(radiobox, HPos.CENTER);
		radiobox.setAlignment(Pos.CENTER);
		layout.setVgap(3);
	    layout.setHgap(10);
	    layout.setPadding(new Insets(10, 10, 10, 10));
	    
	    resumeButton.setDisable(true);
		
	    layout.add(gridsize, 6, 2);
		layout.add(resumeButton,6,26);
		layout.add(playButton,6,36);
		layout.add(settingsButton,6,42);
		players.setMaxWidth(60);
		layout.add(radiobox, 6, 4);
		layout.add(numPlayersLabel, 6, 12);
		
		HBox numPlayerControl=new HBox();
		numPlayerControl.setSpacing(0);
		numPlayerControl.getChildren().addAll(minusButton,players,plusButton);
		GridPane.setHalignment(numPlayerControl, HPos.CENTER);
		numPlayerControl.setAlignment(Pos.CENTER);
		layout.add(numPlayerControl,6,14);

		
		UnaryOperator<Change> filter = value -> {
            if (value.isAdded()) {
                String s = value.getText();
                if(s.length() == 0)
                		value.setText("-1");
                else if(players.getText().length()>0 || s.charAt(0)<'0' || s.charAt(0)>'9')
            			value.setText("");
                else if (s.charAt(0)<'2')
                		value.setText("2");
                else if (s.charAt(0)>'8')
            			value.setText("8");
            }
            return value ;
        };
        players.setTextFormatter(new TextFormatter<String>(filter));
        
        try {
	    		Image image = new Image(new FileInputStream("./src/logo.png"));
	    		ImageView imageView = new ImageView(image);
	    		imageView.setPreserveRatio(true); 
	    	    imageView.setFitWidth(480); 
	    	    GridPane.setHalignment(imageView, HPos.CENTER);
	    		layout.add(imageView,6,0);
    		}
    		catch(Exception e) {
    			System.out.println("Error retriieving logo");
    		}
    		
		menu=new Scene(layout,640,520);
		
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
		Button newGameButton = new Button("New Game");
		newGameButton.setOnAction(event -> {
			this.b = new BoardGUI(numRows,numCols,numPlayers);
			game=new Scene(this.createContent());
			window.setScene(game);
		});
		Pane spacer = new Pane();
		backButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		newGameButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button undoButton = new Button("Undo");
		undoButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		
		menubar.getChildren().addAll(backButton,spacer,undoButton,newGameButton);

		ret.setTop(menubar);
		ret.setBottom(root);

		return ret;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chain Reaction");
		this.window=primaryStage;
		
		numRows=9;
		numCols=6;
		numPlayers=2;
		
		this.createMenu();
		primaryStage.setScene(menu);
		primaryStage.show();
		this.b = new BoardGUI(numRows,numCols,numPlayers);
		game=new Scene(this.createContent());
	}

}
