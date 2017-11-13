package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import withoutGUI.TileBoard;
import withoutGUI.TileCell;
import withoutGUI.gameSave;
import withoutGUI.gameState;

public class mainApp extends Application{
	static gameSave resumeGS = new gameSave();
	static BoardGUI b;
	int numRows,numCols,numPlayers;
	static Scene menu, game, settingsPage;
	static Stage window;
	public static Button undoButton;
	public static Button resumeButton;
	
	/** 
	 * Shows the win alert box once some player has won.
	 * Pressing the OK button thereafter takes the user back to the menu and resets the state of the game.
	 * @author aayush9
	 * @param x Player who won
	 * @throws IOException In case serialization fails
	 */
	public static void showWinAlertBox(int x) throws IOException{
		b.tb.lastGameCompleted = true;
		CoordinateTile.gs.currentBoard = new TileBoard(b.tb);
		mainApp.resumeGS.serialize(CoordinateTile.gs);
		mainApp.resumeButton.setDisable(true);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText(null);
		alert.setContentText("Player "+x+" wins.\n\nClick OK to go back to menu.");
		alert.setOnHidden(evt -> {
			window.setScene(menu);
		});
		alert.show();
	}
	/** 
	 * Creates the settings menu where user can change colour of players.
	 * The method uses the java-fx's built in color-picker tool to allow for choosing between
	 * predefined colors or making a custom color.
	 * @author aayush9
	 */
	public void createSettingsPage() {
		GridPane layout = new GridPane();
		layout.setVgap(3);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.setAlignment(Pos.CENTER);
		Label heading=new Label("Set Player Colour");
		heading.setStyle("-fx-font: 34 arial;");
		GridPane.setHalignment(heading, HPos.CENTER);
		heading.setAlignment(Pos.CENTER);
		layout.add(heading,6,6);
		for(int i=1;i<=this.numPlayers;++i) {
			HBox row = new HBox();
			GridPane.setHalignment(row, HPos.CENTER);
			row.setSpacing(80);
			Button colorButton = new Button("  ");
			final int idx=i-1;
			colorButton.setOnAction(event -> {
				Dialog<String> alert = new Dialog<>();
				alert.setTitle("Pick a colour");
				alert.setHeaderText(null);
				GridPane grid = new GridPane();
				ColorPicker colourPicker = new ColorPicker();
				ButtonType OKButton = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
		        colourPicker.setValue((new BoardGUI(1,1,8)).allPlayers.get(idx).colour);
				GridPane.setHalignment(colourPicker, HPos.CENTER);
		        colourPicker.setOnAction(ev -> {
		        			colorButton.setStyle("-fx-background-radius : 20px ;"+"-fx-background-color: #"+ String.format( "%02X%02X%02X",
		    	            (int)( colourPicker.getValue().getRed() * 255 ),
		    	            (int)( colourPicker.getValue().getGreen() * 255 ),
		    	            (int)( colourPicker.getValue().getBlue() * 255 ) ));
		        			
		        			BoardGUI.allColours[idx] = Color.color(( colourPicker.getValue().getRed()),( colourPicker.getValue().getGreen()), (colourPicker.getValue().getBlue()));
		        			TileBoard.allColours[idx] = BoardGUI.allColours[idx].toString();
		        });
				grid.setVgap(3);
				grid.setHgap(10);
			    grid.setPadding(new Insets(22, 22, 22, 22));
		        GridPane.setHalignment(colourPicker, HPos.CENTER);
		        grid.add(colourPicker,2,2);
		        alert.getDialogPane().setPrefSize(220,150);
		        alert.getDialogPane().setContent(grid);
		        alert.getDialogPane().getButtonTypes().add(OKButton);
				alert.showAndWait();
			});
			colorButton.setStyle("-fx-background-radius : 20px ;"+"-fx-background-color: #"+ String.format( "%02X%02X%02X",
	            (int)( (new BoardGUI(1,1,8)).allPlayers.get(i-1).colour.getRed() * 255 ),
	            (int)( (new BoardGUI(1,1,8)).allPlayers.get(i-1).colour.getGreen() * 255 ),
	            (int)( (new BoardGUI(1,1,8)).allPlayers.get(i-1).colour.getBlue() * 255 ) ));
			row.getChildren().addAll(new Label("Player "+i),new Label(":"),colorButton);
			row.setAlignment(Pos.CENTER);
			layout.add(row,6,12+4*i);
		}
		Button doneButton = new Button("Done");
		GridPane.setHalignment(doneButton, HPos.CENTER);
		doneButton.setAlignment(Pos.CENTER);
		
		doneButton.setOnAction(event -> {
			window.setScene(menu);
		});
		layout.add(doneButton,6,56);
		
		Button resetAllColorsButton = new Button("Reset All Colors");
		GridPane.setHalignment(resetAllColorsButton, HPos.CENTER);
		resetAllColorsButton.setAlignment(Pos.CENTER);
		
		resetAllColorsButton.setOnAction(event -> {
			BoardGUI.allColours = new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.CYAN,Color.ORANGE,Color.GRAY};
			TileBoard.allColours = new String[] {Color.RED.toString(),Color.GREEN.toString(),Color.BLUE.toString(),Color.YELLOW.toString(),Color.MAGENTA.toString(),Color.CYAN.toString(),Color.ORANGE.toString(),Color.GRAY.toString()};
			window.setScene(menu);
		});
		layout.add(resetAllColorsButton,6,52);
		
		settingsPage=new Scene(layout,640,520);
		settingsPage.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	}
	
	/** 
	 * Creates the main menu page which the user interacts with most of the time.
	 * Presents between 2 choices of grid size and a player counter which can only contain values 2-8.
	 * Has buttons resume game, new game, and settings.
	 * @author aayush9
	 */
	public void createMenu(){
		
		Button playButton = new Button("New Game");
		resumeButton = new Button("Resume");
		Button settingsButton = new Button("Settings");
		
		playButton.setAlignment(Pos.CENTER);
		
		playButton.setOnAction(event -> {
			mainApp.b = new BoardGUI(numRows,numCols,numPlayers);
			CoordinateTile.init = true;
			TileCell.init = true;
			CoordinateTile.gs = new gameState(mainApp.b.tb);
			undoButton.setDisable(true);
			
			CoordinateTile.currentPlayer = 0;
			TileCell.currentPlayer = 0;
			
			CoordinateTile.counterForInitialGamePlay = 0;
			TileCell.counterForInitialGamePlay = 0;
			
			CoordinateTile.counterForInitialBorder = 0;
			TileCell.counterForInitialBorder = 0;
			
			game=new Scene(this.createContent(true));
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
		         
					try {
						gameState loadPreviousGame = resumeGS.deserialize();
						if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
			            {
			            	resumeButton.setDisable(true);
			            }
			            else
			            {
			            	resumeButton.setDisable(false);
			            }
					} catch (ClassNotFoundException | IOException | NullPointerException e) {
						// TODO Auto-generated catch block
						resumeButton.setDisable(true);
					}
		        } 
		        else {
		        	numRows=15;
		            numCols=10;
		            
		            try {
						gameState loadPreviousGame = resumeGS.deserialize();
						if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
			            {
			            	resumeButton.setDisable(true);
			            }
			            else
			            {
			            	resumeButton.setDisable(false);
			            }
					} catch (ClassNotFoundException | IOException | NullPointerException e) {
						// TODO Auto-generated catch block
						resumeButton.setDisable(true);
					}
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
				
				try {
					gameState loadPreviousGame = resumeGS.deserialize();
					if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
		            {
		            	resumeButton.setDisable(true);
		            }
		            else
		            {
		            	resumeButton.setDisable(false);
		            }
				} catch (ClassNotFoundException | IOException | NullPointerException e) {
					// TODO Auto-generated catch block
					resumeButton.setDisable(true);
				}
				
			} catch(java.lang.NumberFormatException e) {
				System.out.println("Invalid value");
			}
		});
		
		Button minusButton = new Button("-");
		minusButton.setOnAction(event -> {
			if(Integer.parseInt(players.getText())>2) {
				int x=Integer.parseInt(players.getText())-1;
				numPlayers=x;
				
				try {
					gameState loadPreviousGame = resumeGS.deserialize();
					if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
		            {
		            	resumeButton.setDisable(true);
		            }
		            else
		            {
		            	resumeButton.setDisable(false);
		            }
				} catch (ClassNotFoundException | IOException | NullPointerException e) {
					// TODO Auto-generated catch block
					resumeButton.setDisable(true);
				}
				
				players.clear();
				players.setText(Integer.toString(x));
			}
		});
		Button plusButton = new Button("+");
		plusButton.setOnAction(event -> {
			if(Integer.parseInt(players.getText())<8) {
				int x=Integer.parseInt(players.getText())+1;
				numPlayers=x;
				
				try {
					gameState loadPreviousGame = resumeGS.deserialize();
					if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
		            {
		            	resumeButton.setDisable(true);
		            }
		            else
		            {
		            	resumeButton.setDisable(false);
		            }
				} catch (ClassNotFoundException | IOException | NullPointerException e) {
					// TODO Auto-generated catch block
					resumeButton.setDisable(true);
				}
				
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
	    
	    try {
			gameState loadPreviousGame = resumeGS.deserialize();
			if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
            {
            	resumeButton.setDisable(true);
            }
            else
            {
            	resumeButton.setDisable(false);
            }
		} catch (ClassNotFoundException | IOException | NullPointerException e) {
			// TODO Auto-generated catch block
			resumeButton.setDisable(true);
		}
	    
	    resumeButton.setOnAction(event->{
	    	try {
	    		CoordinateTile.gs = mainApp.resumeGS.deserialize();
				TileBoard loadSavedGame = CoordinateTile.gs.currentBoard;
				CoordinateTile.currentPlayer = CoordinateTile.gs.currentPlayer;
				CoordinateTile.counterForInitialBorder = CoordinateTile.gs.counterForInitialBorder;
				CoordinateTile.counterForInitialGamePlay = CoordinateTile.gs.counterForInitialGamePlay;
				CoordinateTile.init = CoordinateTile.gs.init;
				TileBoard.allColours = CoordinateTile.gs.allColours;
				
				for(int i=0;i<CoordinateTile.gs.allColours.length;i+=1)
				{
					BoardGUI.allColours[i] = Color.valueOf(CoordinateTile.gs.allColours[i]);
				}
				
				System.out.println("Details of Saved Game After Loading are:");
				System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
				System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
				System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
				System.out.println("init : "+CoordinateTile.gs.init);
				System.out.println("Game Complete Status : "+loadSavedGame.lastGameCompleted);
				System.out.println();
				
				if(!loadSavedGame.lastGameCompleted)
				{
					mainApp.b = new BoardGUI(numRows,numCols,numPlayers);
					mainApp.b.loadGUIfromState(new TileBoard(loadSavedGame),true);
					game=new Scene(this.createContent(loadSavedGame.undoOnce));
					window.setScene(game);
				}
				else
				{
					resumeButton.setDisable(true);
					System.out.println("Last game was completed");
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	catch (NullPointerException e)
	    	{
	    		System.out.println("No State Found");
	    	}
	    	
	    });
		
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
		
		menu.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	}
	/**
	 * Creates a scene with the actual gameplay screen where the user interacts with the grid of tiles.
	 * The scene also provides the functionalities of undo, new game and back to menu.
	 * @author aayush9
	 * @param setUndoButtonVisibility Determines if undo button should be visible or not initially (useful when you are resuming a game from serializable interface).
	 * @return Creates the above mentioned scene and returns to the calling function to set the stage.
	 */
	public Parent createContent(boolean setUndoButtonVisibility)
	{
		Pane root = new Pane();
		
		if(b.numberOfRows==9)
		{
			root.setPrefSize(6*50, 9*50);
			for(int i=0;i<b.numberOfRows;i+=1)
			{
				for(int j=0;j<b.numberOfColumns;j+=1)
				{	
						b.board[i][j].setTranslateX(j*50);
						b.board[i][j].setTranslateY(i*50);
						root.getChildren().add(b.board[i][j]);
				}
			}
		}
		else
		{
			root.setPrefSize(10*40, 15*40);
			for(int i=0;i<b.numberOfRows;i+=1)
			{
				for(int j=0;j<b.numberOfColumns;j+=1)
				{	
						b.board[i][j].setTranslateX(j*40);
						b.board[i][j].setTranslateY(i*40);
						root.getChildren().add(b.board[i][j]);
				}
			}
		}
		
		BorderPane ret=new BorderPane();
		HBox menubar=new HBox(10);
		menubar.setPadding(new Insets(10));
		Button backButton = new Button("Back to Menu");
		backButton.setOnAction(event -> {
			
			try {
				
				CoordinateTile.gs.currentBoard = new TileBoard(mainApp.b.tb);
				CoordinateTile.gs.currentPlayer = CoordinateTile.currentPlayer;
				CoordinateTile.gs.counterForInitialBorder = CoordinateTile.counterForInitialBorder;
				CoordinateTile.gs.counterForInitialGamePlay = CoordinateTile.counterForInitialGamePlay;
				CoordinateTile.gs.init = CoordinateTile.init; 
				CoordinateTile.gs.allColours = TileBoard.allColours;
				
				resumeGS.serialize(CoordinateTile.gs);
				
				System.out.println("Details of Saved Game After Saving are:");
				System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
				System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
				System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
				System.out.println("init : "+CoordinateTile.gs.init);
				System.out.println();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				gameState loadPreviousGame = resumeGS.deserialize();
				if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
	            {
	            	resumeButton.setDisable(true);
	            }
	            else
	            {
	            	resumeButton.setDisable(false);
	            }
			} catch (ClassNotFoundException | IOException | NullPointerException e) {
				// TODO Auto-generated catch block
				resumeButton.setDisable(true);
			}
			
			this.createMenu();
			window.setScene(menu);
		});
		Button newGameButton = new Button("New Game");
		newGameButton.setOnAction(event -> {
			mainApp.b = new BoardGUI(numRows,numCols,numPlayers);
			CoordinateTile.init = true;
			TileCell.init = true;
			CoordinateTile.gs = new gameState(mainApp.b.tb);
			undoButton.setDisable(true);
			
			CoordinateTile.currentPlayer = 0;
			TileCell.currentPlayer = 0;
			
			CoordinateTile.counterForInitialGamePlay = 0;
			TileCell.counterForInitialGamePlay = 0;
			
			CoordinateTile.counterForInitialBorder = 0;
			TileCell.counterForInitialBorder = 0;
			
			try {
				gameState loadPreviousGame = resumeGS.deserialize();
				if(!(loadPreviousGame.currentBoard.numberOfRows==numRows && loadPreviousGame.currentBoard.numberOfColumns==numCols && loadPreviousGame.currentBoard.numberOfPlayers==numPlayers && !loadPreviousGame.currentBoard.lastGameCompleted))
	            {
	            	resumeButton.setDisable(true);
	            }
	            else
	            {
	            	resumeButton.setDisable(false);
	            }
			} catch (ClassNotFoundException | IOException | NullPointerException e) {
				resumeButton.setDisable(true);
			}
			
			game=new Scene(this.createContent(true));
			window.setScene(game);
		});
		Pane spacer = new Pane();
		backButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		newGameButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		
		undoButton = new Button("Undo");
		undoButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
		undoButton.setDisable(setUndoButtonVisibility);
		
		undoButton.setOnAction(event->{
			
			if(System.currentTimeMillis() - BoardGUI.startTime < 1550) 
			{
				return;
			}
			
			if(!CoordinateTile.gs.allStates.isEmpty())
			{
				TileBoard previousState = CoordinateTile.gs.loadState();
				CoordinateTile.gs.currentBoard = new TileBoard(previousState);
				
				b.loadGUIfromState(previousState,false);
				
				CoordinateTile.gs.currentPlayer = CoordinateTile.currentPlayer;
				CoordinateTile.gs.counterForInitialBorder = CoordinateTile.counterForInitialBorder;
				CoordinateTile.gs.counterForInitialGamePlay = CoordinateTile.counterForInitialGamePlay;
				CoordinateTile.gs.init = CoordinateTile.init; 
				CoordinateTile.gs.allColours = TileBoard.allColours;
				
				try {
					resumeGS.serialize(CoordinateTile.gs);
					
					System.out.println("Details of Saved Game After Saving are:");
					System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
					System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
					System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
					System.out.println("init : "+CoordinateTile.gs.init);
					System.out.println();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		menubar.getChildren().addAll(backButton,spacer,undoButton,newGameButton);

		ret.setTop(menubar);
		root.setStyle("-fx-background-color : #fff ;");
		ret.setBottom(root);

		return ret;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The function that is first invoked when the application runs, and calls the menu function to set the primary stage.
	 * It also reads the previous game state, if any, and changes the default settings such that the resume button is available by default.
	 * @author aayush9
	 * @param primaryStage Automatically passed to by javaFX
	 * @throws Exception A generalized exception in case gameState file not found, IOException or NullPointerException 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chain Reaction");
		mainApp.window=primaryStage;
		
		numRows=9;
		numCols=6;
		numPlayers=2;
		
		try {
			gameState lastState = resumeGS.deserialize();
			numRows = lastState.currentBoard.numberOfRows;
			numCols = lastState.currentBoard.numberOfColumns;
			numPlayers = lastState.currentBoard.numberOfPlayers;
		}
		catch (ClassNotFoundException | IOException | NullPointerException e)
		{
			System.out.println("No State Loaded");
		}
		
		
		this.createMenu();
		primaryStage.setScene(menu);
		primaryStage.show();
		mainApp.b = new BoardGUI(numRows,numCols,numPlayers);
		game=new Scene(this.createContent(true));
	}

}

