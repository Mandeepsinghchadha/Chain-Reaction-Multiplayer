package application;

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

public class mainApp extends Application{
	
	BoardGUI b;
	int numRows,numCols,numPlayers;
	Scene menu, game, settingsPage;
	Stage window;
	
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
			layout.add(row,6,16+5*i);
		}
		Button doneButton = new Button("Done");
		GridPane.setHalignment(doneButton, HPos.CENTER);
		doneButton.setAlignment(Pos.CENTER);
		
		doneButton.setOnAction(event -> {
			window.setScene(menu);
		});
		layout.add(doneButton,6,66);
		
		Button resetAllColorsButton = new Button("Reset All Colors");
		GridPane.setHalignment(resetAllColorsButton, HPos.CENTER);
		resetAllColorsButton.setAlignment(Pos.CENTER);
		
		resetAllColorsButton.setOnAction(event -> {
			BoardGUI.allColours = new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.CYAN,Color.ORANGE,Color.GRAY};
			TileBoard.allColours = new String[] {Color.RED.toString(),Color.GREEN.toString(),Color.BLUE.toString(),Color.YELLOW.toString(),Color.MAGENTA.toString(),Color.CYAN.toString(),Color.ORANGE.toString(),Color.GRAY.toString()};
			window.setScene(menu);
		});
		layout.add(resetAllColorsButton,6,62);
		
		settingsPage=new Scene(layout,640,520);
		settingsPage.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	}
	
	public void createMenu(){
		
		Button playButton = new Button("New Game");
		Button resumeButton = new Button("Resume");
		Button settingsButton = new Button("Settings");
		
		playButton.setAlignment(Pos.CENTER);
		
		playButton.setOnAction(event -> {
			this.b = new BoardGUI(numRows,numCols,numPlayers);
			CoordinateTile.init = true;
			TileCell.init = true;
			
			CoordinateTile.currentPlayer = 0;
			TileCell.currentPlayer = 0;
			
			CoordinateTile.counterForInitialGamePlay = 0;
			TileCell.counterForInitialGamePlay = 0;
			
			CoordinateTile.counterForInitialBorder = 0;
			TileCell.counterForInitialBorder = 0;
			
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
		
		menu.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
	}
	
	public Parent createContent()
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
			this.createMenu();
			window.setScene(menu);
		});
		Button newGameButton = new Button("New Game");
		newGameButton.setOnAction(event -> {
			this.b = new BoardGUI(numRows,numCols,numPlayers);
			CoordinateTile.init = true;
			TileCell.init = true;
			
			CoordinateTile.currentPlayer = 0;
			TileCell.currentPlayer = 0;
			
			CoordinateTile.counterForInitialGamePlay = 0;
			TileCell.counterForInitialGamePlay = 0;
			
			CoordinateTile.counterForInitialBorder = 0;
			TileCell.counterForInitialBorder = 0;
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

