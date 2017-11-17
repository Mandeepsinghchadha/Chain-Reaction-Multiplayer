package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.util.Duration;
import withoutGUI.TileBoard;
import withoutGUI.TileCell;
import withoutGUI.gameState;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;

import javafx.geometry.Point3D;
import javafx.geometry.Pos;

import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class CoordinateTile extends StackPane {
	static gameState gs;
	TileCell t;
	BoardGUI boardContainer;
	PlayerController playerContainer;
	Rectangle border;
	public boolean pleaseSend=true;
	
	public static boolean init = true;
	public static int currentPlayer = 0;
	public static int counterForInitialGamePlay = 0;
	public static int counterForInitialBorder = 0;
	
	public int xCoordinate;
	public int yCoordinate;
	public int playerStatus;
	public int value;
	public int criticalMass;
	
	public Color colour;
	public int numberOfRows;
	public int numberOfColumns;
	
	Group allOrbs = new Group();
	Point3D allAxes[] = {Rotate.X_AXIS,Rotate.Y_AXIS,Rotate.Z_AXIS};
	
	RotateTransition rotateGroup;

	TranslateTransition transRight;
	TranslateTransition transLeft;
	TranslateTransition transAbove;
	TranslateTransition transBelow;
	
	ParallelTransition parallelSplit;

	Sphere rightOrb;
	Sphere leftOrb;
	Sphere aboveOrb;
	Sphere belowOrb;
	Sphere otherOrb;
	/**
	 * Initializes the tile with required animation(rotation, translations, parallel split). 
	 * Also coordinates the movements of players in the round robin manner.
	 * @param x X coordinate of the tile in question
	 * @param y Y coordinate of the tile in question
	 * @param m Number of rows of the board
	 * @param n Number of columns of the board
	 * @param b The board where the game is being played
	 * @param squareSize The tile size for different grid sizes in order to fit in the visible workspace window.
	 * @author Madhur Tandon
	 */
	CoordinateTile(int x, int y, int m, int n, BoardGUI b, int squareSize)
	{
		this.pleaseSend=true;
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.playerStatus = 0;
		this.value = 0;
		this.criticalMass = this.getCriticalMass(this.xCoordinate, this.yCoordinate);
		this.colour = BoardGUI.allColours[0];
		this.boardContainer = b;
		this.t = new TileCell(this.numberOfRows,this.numberOfColumns,this.xCoordinate,this.yCoordinate);
		gs = new gameState(this.boardContainer.tb);

		rotateGroup = new RotateTransition(Duration.millis(1500+Math.random()*500), allOrbs);
		rotateGroup.setFromAngle(0);
		rotateGroup.setToAngle(360);
		rotateGroup.setInterpolator(Interpolator.LINEAR);
		rotateGroup.setCycleCount(RotateTransition.INDEFINITE);
		rotateGroup.setAxis(allAxes[new Random().nextInt(allAxes.length)]);
		rotateGroup.setAutoReverse(false);

		transRight = new TranslateTransition(Duration.millis(500));
		transRight.setToX(squareSize);
		transRight.setCycleCount(1);
		transRight.setAutoReverse(false);
		transRight.setOnFinished(e->{
			this.getChildren().remove(this.rightOrb);
		});
		
		transLeft = new TranslateTransition(Duration.millis(500));
		transLeft.setToX(-1*squareSize);
		transLeft.setCycleCount(1);
		transLeft.setAutoReverse(false);
		transLeft.setOnFinished(e->{
			this.getChildren().remove(this.leftOrb);
		});
		
		transBelow = new TranslateTransition(Duration.millis(500));
		transBelow.setToY(squareSize);
		transBelow.setCycleCount(1);
		transBelow.setAutoReverse(false);
		transBelow.setOnFinished(e->{
			this.getChildren().remove(this.belowOrb);
		});
		
		transAbove = new TranslateTransition(Duration.millis(500));
		transAbove.setToY(-1*squareSize);
		transAbove.setCycleCount(1);
		transAbove.setAutoReverse(false);
		transAbove.setOnFinished(e->{
			this.getChildren().remove(this.aboveOrb);
		});

		parallelSplit = new ParallelTransition();
		parallelSplit.setOnFinished(e->{
			
			BoardGUI.startTime=System.currentTimeMillis();
			ArrayList<CoordinateTile> allNeighbours = b.getListOfNeighbours(this.xCoordinate, this.yCoordinate);
			for(int i=0;i<allNeighbours.size();i+=1)
			{
				allNeighbours.get(i).playerStatus = this.playerContainer.playerNumber;
				allNeighbours.get(i).colour = this.playerContainer.colour;
				
				allNeighbours.get(i).t.playerStatus = allNeighbours.get(i).playerStatus;
				allNeighbours.get(i).t.colour = allNeighbours.get(i).colour.toString();
				b.tb.board[allNeighbours.get(i).xCoordinate][allNeighbours.get(i).yCoordinate].playerStatus = allNeighbours.get(i).playerStatus;
				b.tb.board[allNeighbours.get(i).xCoordinate][allNeighbours.get(i).yCoordinate].colour = allNeighbours.get(i).colour.toString();
				
				allNeighbours.get(i).getChildren().remove(allNeighbours.get(i).leftOrb);
				allNeighbours.get(i).getChildren().remove(allNeighbours.get(i).rightOrb);
				allNeighbours.get(i).getChildren().remove(allNeighbours.get(i).aboveOrb);
				allNeighbours.get(i).getChildren().remove(allNeighbours.get(i).belowOrb);
				
				Sphere[] allNeighbourSpheres = allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]);
				for(int a=0;a<allNeighbourSpheres.length;a+=1)
				{
					PhongMaterial material = new PhongMaterial();
				    material.setDiffuseColor(this.playerContainer.colour);
				    material.setSpecularColor(Color.BLACK);
				    allNeighbourSpheres[a].setMaterial(material);
				}

				allNeighbours.get(i).drawSphere(false);
				allNeighbours.get(i).rotateGroup.play();
			}
			
			for(int j=0;j<b.numberOfPlayers;j+=1)
			{
				if(b.allPlayers.get(j).active)
				{
					b.allPlayers.get(j).orbCount = b.playerCount(j+1);
					b.allPlayers.get(j).p.orbCount = b.allPlayers.get(j).orbCount;
					b.tb.allPlayers.get(j).orbCount = b.allPlayers.get(j).orbCount;
					if(b.allPlayers.get(j).orbCount==0)
					{
						b.allPlayers.get(j).active=false;
						b.tb.allPlayers.get(j).active = false;
						b.allPlayers.get(j).p.active = false;
					}
				}
			}
			
			b.board[x][y].value = b.board[x][y].value % b.board[x][y].criticalMass;
			b.tb.board[x][y].value=b.board[x][y].value % b.board[x][y].criticalMass;
			b.board[x][y].t.value = b.board[x][y].value % b.board[x][y].criticalMass;
			
			ArrayList<CoordinateTile> NeighbourCellsOfJustMovedCell = b.getListOfNeighbours(x,y);
			ArrayList<CoordinateTile> NeighbourCellsWhichAreThemselvesUnstable = this.playerContainer.getAllUnstableNeighbourCells(NeighbourCellsOfJustMovedCell,b);
			
			b.board[x][y].parallelSplit.getChildren().clear();
			
			if(b.countAllActivePlayers(b.allPlayers)!=1)
			{
				for(int a=0;a<NeighbourCellsWhichAreThemselvesUnstable.size();a+=1)
				{
					 try {
						this.playerContainer.move(b,NeighbourCellsWhichAreThemselvesUnstable.get(a).xCoordinate,NeighbourCellsWhichAreThemselvesUnstable.get(a).yCoordinate);
					} catch (IllegalMoveException e1) {
						// TODO Auto-generated catch block
						System.out.println(e1.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			else
			{
				int winningPlayerNumber = -1;
				int numberOfActivePlayers = 0;
				for(int i=0;i<this.boardContainer.numberOfPlayers;i+=1)
				{
					if(this.boardContainer.allPlayers.get(i).active)
					{
						winningPlayerNumber = i;
						numberOfActivePlayers+=1;
					}
				}
				this.boardContainer.tb.lastGameCompleted = true;
				this.boardContainer.tb.undoOnce = false;
				mainApp.undoButton.setDisable(true);
				if(numberOfActivePlayers==1 && this.boardContainer.shownPrompt)
				{
					this.boardContainer.shownPrompt=false;
					try {
						mainApp.showWinAlertBox(winningPlayerNumber+1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			int p = currentPlayer;
			while(!b.allPlayers.get(p).active)
			{
				p = (p + 1) % this.boardContainer.numberOfPlayers;
			}

			for(int q=0;q<b.numberOfRows;q+=1)
			{
				for(int r=0;r<b.numberOfColumns;r+=1)
				{
					b.board[q][r].border.setStroke(b.allPlayers.get(p).colour);
					b.board[q][r].t.borderColour = b.allPlayers.get(p).colour.toString();
					b.tb.board[q][r].borderColour = b.allPlayers.get(p).colour.toString();
					if(b.board[q][r].value==0)
					{
						b.board[q][r].colour = (Color) b.board[q][r].border.getStroke();
						b.tb.board[q][r].colour = b.board[q][r].border.getStroke().toString();
						b.board[q][r].t.colour = b.board[q][r].border.getStroke().toString();
					}
				}
			}
			
			if(NeighbourCellsWhichAreThemselvesUnstable.isEmpty())
			{
//				System.out.println("End of Transition");
				try {
					gs.currentBoard = new TileBoard(this.boardContainer.tb);
					gs.currentPlayer = currentPlayer;
					gs.counterForInitialBorder = counterForInitialBorder;
					gs.counterForInitialGamePlay = counterForInitialGamePlay;
					gs.init = init;
					gs.allColours = TileBoard.allColours;
					mainApp.resumeGS.serialize(gs);
					
//					System.out.println("Details of Saved Game After Saving are:");
//					System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
//					System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
//					System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
//					System.out.println("init : "+CoordinateTile.gs.init);
//					System.out.println();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		this.border = new Rectangle(squareSize,squareSize);
		border.setFill(null);
		border.setStroke(BoardGUI.allColours[0]);
		this.t.borderColour = BoardGUI.allColours[0].toString();
		b.tb.board[this.xCoordinate][this.yCoordinate].borderColour = BoardGUI.allColours[0].toString();

		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(border);
		/**
		 * Helper function to highlight the cell where the mouse is currently hovering.
		 * @author Madhur Tandon
		 */
		this.setOnMouseEntered(event->{
			if(this.border.getStroke().equals(this.colour))
			{
				this.border.setFill(this.colour);
			}
		});
		/**
		 * Helper function to un-highlight the cell when the mouse is done hovering the cell.
		 * @author Madhur Tandon
		 */
		this.setOnMouseExited(event->{
			this.border.setFill(null);
		});
		/**
		 * Listener function to take appropriate action when mouse is clicked on a particular tile.
		 * If the move is not valid, it throws an invalid move exception, else calls the move method in the playercontroller class.
		 * Also uses a timer to not allow actions in case a transition is going on.
		 * @author Madhur Tandon
		 */
		setOnMouseClicked(event -> {
			mainApp.network.readyToAccept=false;
			
			if(System.currentTimeMillis() - BoardGUI.startTime < 550) 
			{
				return;
			}
			if(System.currentTimeMillis() - BoardGUI.coordinateStartTime < 40) 
			{
				return;
			}
			if(counterForInitialGamePlay<this.boardContainer.numberOfPlayers){
				if(mainApp.isNetwork && counterForInitialGamePlay+1!=boardContainer.networkPlayerNumber)
					return;
			}
			else {
				System.out.println(currentPlayer+1);
				if(mainApp.isNetwork && currentPlayer+1!=boardContainer.networkPlayerNumber)
					return;
				
			}
			BoardGUI.coordinateStartTime=System.currentTimeMillis();
			if(mainApp.isNetwork) mainApp.network.send("move "+this.xCoordinate+" "+this.yCoordinate);
			if(counterForInitialGamePlay>=this.boardContainer.numberOfPlayers)
			{
				counterForInitialGamePlay+=1;
				if(this.boardContainer.countAllActivePlayers(this.boardContainer.allPlayers)>1)
				{
					if(!b.allPlayers.get(currentPlayer).active)
					{
						while(!b.allPlayers.get(currentPlayer).active)
						{
							currentPlayer = (currentPlayer + 1) % this.boardContainer.numberOfPlayers;
							TileCell.currentPlayer = (TileCell.currentPlayer + 1) % this.boardContainer.numberOfPlayers;
						}
					}

					CoordinateTile.gs.saveState(new TileBoard(this.boardContainer.tb));
					try
					{
						this.boardContainer.allPlayers.get(currentPlayer).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
						this.boardContainer.tb.undoOnce = true;
						mainApp.undoButton.setDisable(false);
					}
					catch (IllegalMoveException e){
						currentPlayer = (((currentPlayer - 1) % this.boardContainer.numberOfPlayers) + this.boardContainer.numberOfPlayers) % this.boardContainer.numberOfPlayers;
						TileCell.currentPlayer = (((TileCell.currentPlayer - 1) % this.boardContainer.numberOfPlayers) + this.boardContainer.numberOfPlayers) % this.boardContainer.numberOfPlayers;
						System.out.println(e.getMessage());
					}
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
				else
				{
					int winningPlayerNumber = -1;
					int numberOfActivePlayers = 0;
					for(int i=0;i<this.boardContainer.numberOfPlayers;i+=1)
					{
						if(this.boardContainer.allPlayers.get(i).active)
						{
							winningPlayerNumber = i;
							numberOfActivePlayers+=1;
						}
					}
					this.boardContainer.tb.lastGameCompleted = true;
					this.boardContainer.tb.undoOnce = false;
					mainApp.undoButton.setDisable(true);
					if(numberOfActivePlayers==1 && this.boardContainer.shownPrompt)
					{
						this.boardContainer.shownPrompt=false;
						try {
							mainApp.showWinAlertBox(winningPlayerNumber+1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				currentPlayer = (currentPlayer + 1) % this.boardContainer.numberOfPlayers;
				TileCell.currentPlayer = (TileCell.currentPlayer + 1) % this.boardContainer.numberOfPlayers;
				
				int p = currentPlayer;
				while(!b.allPlayers.get(p).active)
				{
					p = (p + 1) % this.boardContainer.numberOfPlayers;
				}

				for(int q=0;q<b.numberOfRows;q+=1)
				{
					for(int r=0;r<b.numberOfColumns;r+=1)
					{
						b.board[q][r].border.setStroke(b.allPlayers.get(p).colour);
						b.board[q][r].t.borderColour = b.allPlayers.get(p).colour.toString();
						b.tb.board[q][r].borderColour = b.allPlayers.get(p).colour.toString();
						if(b.board[q][r].value==0)
						{
							b.board[q][r].colour = (Color) b.board[q][r].border.getStroke();
							b.tb.board[q][r].colour = b.board[q][r].border.getStroke().toString();
							b.board[q][r].t.colour = b.board[q][r].border.getStroke().toString();
						}
					}
				}
				try {
					gs.currentBoard = new TileBoard(this.boardContainer.tb);
					gs.currentPlayer = currentPlayer;
					gs.counterForInitialBorder = counterForInitialBorder;
					gs.counterForInitialGamePlay = counterForInitialGamePlay;
					gs.init = init;
					gs.allColours = TileBoard.allColours;
					mainApp.resumeGS.serialize(gs);
					
//					System.out.println("Details of Saved Game After Saving are:");
//					System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
//					System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
//					System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
//					System.out.println("init : "+CoordinateTile.gs.init);
//					System.out.println();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
			{
				if(mainApp.isNetwork) {
					System.out.println(counterForInitialGamePlay);
					if(counterForInitialGamePlay+1!=boardContainer.networkPlayerNumber) return;
				}
				if(counterForInitialGamePlay<this.boardContainer.numberOfPlayers)
				{
					CoordinateTile.gs.saveState(new TileBoard(this.boardContainer.tb));
					try
					{
						this.boardContainer.allPlayers.get(counterForInitialGamePlay).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
						this.boardContainer.tb.undoOnce = true;
						mainApp.undoButton.setDisable(false);
					}
					catch (IllegalMoveException e)
					{
						System.out.println(e.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					if(this.boardContainer.playerCount(counterForInitialGamePlay+1)>0)
					{
						this.boardContainer.allPlayers.get(counterForInitialGamePlay).orbCount = 1;
						this.boardContainer.allPlayers.get(counterForInitialGamePlay).p.orbCount = 1;
					}
					else
					{
						counterForInitialGamePlay-=1;
						TileCell.counterForInitialGamePlay-=1;
						counterForInitialBorder-=1;
						TileCell.counterForInitialBorder-=1;
					}
					counterForInitialGamePlay+=1;
					TileCell.counterForInitialGamePlay+=1;

					for(int p=0;p<b.numberOfRows;p+=1)
					{
						for(int q=0;q<b.numberOfColumns;q+=1)
						{
							b.board[p][q].border.setStroke(BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()]);
							b.board[p][q].t.borderColour = BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()].toString();
							b.tb.board[p][q].borderColour = BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()].toString();
							if(b.board[p][q].value==0)
							{
								b.board[p][q].colour = (Color) b.board[p][q].border.getStroke();
								b.tb.board[p][q].colour = b.board[p][q].border.getStroke().toString();
								b.board[p][q].t.colour = b.board[p][q].border.getStroke().toString();
							}
						}
					}
					counterForInitialBorder+=1;
					TileCell.counterForInitialBorder+=1;
				}
				if(counterForInitialGamePlay>=this.boardContainer.numberOfPlayers)
				{
					init = false;
					TileCell.init = false;
				}
				try {
					gs.currentBoard = new TileBoard(this.boardContainer.tb);
					gs.currentPlayer = currentPlayer;
					gs.counterForInitialBorder = counterForInitialBorder;
					gs.counterForInitialGamePlay = counterForInitialGamePlay;
					gs.init = init;
					gs.allColours = TileBoard.allColours;
					mainApp.resumeGS.serialize(gs);
					
//					System.out.println("Details of Saved Game After Saving are:");
//					System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
//					System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
//					System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
//					System.out.println("init : "+CoordinateTile.gs.init);
//					System.out.println();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		getChildren().add(allOrbs);
	}
	public void handle(){
		mainApp.network.readyToAccept=false;
		BoardGUI b=boardContainer;
		if(System.currentTimeMillis() - BoardGUI.coordinateStartTime < 40) 
		{
			return;
		}
		BoardGUI.coordinateStartTime=System.currentTimeMillis();
		
		if(counterForInitialGamePlay>=this.boardContainer.numberOfPlayers)
		{
			counterForInitialGamePlay+=1;
			if(this.boardContainer.countAllActivePlayers(this.boardContainer.allPlayers)>1)
			{
				if(!b.allPlayers.get(currentPlayer).active)
				{
					while(!b.allPlayers.get(currentPlayer).active)
					{
						currentPlayer = (currentPlayer + 1) % this.boardContainer.numberOfPlayers;
						TileCell.currentPlayer = (TileCell.currentPlayer + 1) % this.boardContainer.numberOfPlayers;
					}
				}

				
				CoordinateTile.gs.saveState(new TileBoard(this.boardContainer.tb));
				try
				{
					this.boardContainer.allPlayers.get(currentPlayer).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
					this.boardContainer.tb.undoOnce = true;
					mainApp.undoButton.setDisable(false);
				}
				catch (IllegalMoveException e){
					currentPlayer = (((currentPlayer - 1) % this.boardContainer.numberOfPlayers) + this.boardContainer.numberOfPlayers) % this.boardContainer.numberOfPlayers;
					TileCell.currentPlayer = (((TileCell.currentPlayer - 1) % this.boardContainer.numberOfPlayers) + this.boardContainer.numberOfPlayers) % this.boardContainer.numberOfPlayers;
					System.out.println(e.getMessage());
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
			else
			{
				int winningPlayerNumber = -1;
				int numberOfActivePlayers = 0;
				for(int i=0;i<this.boardContainer.numberOfPlayers;i+=1)
				{
					if(this.boardContainer.allPlayers.get(i).active)
					{
						winningPlayerNumber = i;
						numberOfActivePlayers+=1;
					}
				}
				this.boardContainer.tb.lastGameCompleted = true;
				this.boardContainer.tb.undoOnce = false;
				mainApp.undoButton.setDisable(true);
				if(numberOfActivePlayers==1 && this.boardContainer.shownPrompt)
				{
					this.boardContainer.shownPrompt=false;
					try {
						mainApp.showWinAlertBox(winningPlayerNumber+1);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			currentPlayer = (currentPlayer + 1) % this.boardContainer.numberOfPlayers;
			TileCell.currentPlayer = (TileCell.currentPlayer + 1) % this.boardContainer.numberOfPlayers;
			
			int p = currentPlayer;
			while(!b.allPlayers.get(p).active)
			{
				p = (p + 1) % this.boardContainer.numberOfPlayers;
			}

			for(int q=0;q<b.numberOfRows;q+=1)
			{
				for(int r=0;r<b.numberOfColumns;r+=1)
				{
					b.board[q][r].border.setStroke(b.allPlayers.get(p).colour);
					b.board[q][r].t.borderColour = b.allPlayers.get(p).colour.toString();
					b.tb.board[q][r].borderColour = b.allPlayers.get(p).colour.toString();
					if(b.board[q][r].value==0)
					{
						b.board[q][r].colour = (Color) b.board[q][r].border.getStroke();
						b.tb.board[q][r].colour = b.board[q][r].border.getStroke().toString();
						b.board[q][r].t.colour = b.board[q][r].border.getStroke().toString();
					}
				}
			}
			try {
				gs.currentBoard = new TileBoard(this.boardContainer.tb);
				gs.currentPlayer = currentPlayer;
				gs.counterForInitialBorder = counterForInitialBorder;
				gs.counterForInitialGamePlay = counterForInitialGamePlay;
				gs.init = init;
				gs.allColours = TileBoard.allColours;
				mainApp.resumeGS.serialize(gs);
				
//				System.out.println("Details of Saved Game After Saving are:");
//				System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
//				System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
//				System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
//				System.out.println("init : "+CoordinateTile.gs.init);
//				System.out.println();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else
		{
			if(counterForInitialGamePlay<this.boardContainer.numberOfPlayers)
			{
				CoordinateTile.gs.saveState(new TileBoard(this.boardContainer.tb));
				try
				{
					this.boardContainer.allPlayers.get(counterForInitialGamePlay).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
					this.boardContainer.tb.undoOnce = true;
					mainApp.undoButton.setDisable(false);
				}
				catch (IllegalMoveException e)
				{
					System.out.println(e.getMessage());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				if(this.boardContainer.playerCount(counterForInitialGamePlay+1)>0)
				{
					this.boardContainer.allPlayers.get(counterForInitialGamePlay).orbCount = 1;
					this.boardContainer.allPlayers.get(counterForInitialGamePlay).p.orbCount = 1;
				}
				else
				{
					counterForInitialGamePlay-=1;
					TileCell.counterForInitialGamePlay-=1;
					counterForInitialBorder-=1;
					TileCell.counterForInitialBorder-=1;
				}
				counterForInitialGamePlay+=1;
				TileCell.counterForInitialGamePlay+=1;

				for(int p=0;p<b.numberOfRows;p+=1)
				{
					for(int q=0;q<b.numberOfColumns;q+=1)
					{
						b.board[p][q].border.setStroke(BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()]);
						b.board[p][q].t.borderColour = BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()].toString();
						b.tb.board[p][q].borderColour = BoardGUI.allColours[(counterForInitialBorder+1)%b.allPlayers.size()].toString();
						if(b.board[p][q].value==0)
						{
							b.board[p][q].colour = (Color) b.board[p][q].border.getStroke();
							b.tb.board[p][q].colour = b.board[p][q].border.getStroke().toString();
							b.board[p][q].t.colour = b.board[p][q].border.getStroke().toString();
						}
					}
				}
				counterForInitialBorder+=1;
				TileCell.counterForInitialBorder+=1;
			}
			if(counterForInitialGamePlay>=this.boardContainer.numberOfPlayers)
			{
				init = false;
				TileCell.init = false;
			}
			try {
				gs.currentBoard = new TileBoard(this.boardContainer.tb);
				gs.currentPlayer = currentPlayer;
				gs.counterForInitialBorder = counterForInitialBorder;
				gs.counterForInitialGamePlay = counterForInitialGamePlay;
				gs.init = init;
				gs.allColours = TileBoard.allColours;
				mainApp.resumeGS.serialize(gs);
				
//				System.out.println("Details of Saved Game After Saving are:");
//				System.out.println("CurrentPlayer : "+CoordinateTile.gs.currentPlayer);
//				System.out.println("counterForInitialBorder : "+CoordinateTile.gs.counterForInitialBorder);
//				System.out.println("counterForInitialGamePlay : "+CoordinateTile.gs.counterForInitialGamePlay);
//				System.out.println("init : "+CoordinateTile.gs.init);
//				System.out.println();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Calculates the appropriate critical mass for the cell (2 for corner cell, 3 for sides and 4 otherwise).
	 * @param i The given X coordinate
	 * @param j The given Y coordinate
	 * @return An integer, the calculated critical mass.
	 * @author Madhur Tandon
	 */
	public int getCriticalMass(int i, int j)
	{
		if ((i==0 && j==0) || (i==0 && j==this.numberOfColumns-1) || (i==this.numberOfRows-1 && j==0) || (i==this.numberOfRows-1 && j==this.numberOfColumns-1))
		{
			return 2;
		}
		else if((i==0) || (i==this.numberOfRows-1) || (j==0) || (j==this.numberOfColumns-1))
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
	/**
	 * Renders the sphere on the tile, colors according to the player which has called. Overlaps them in case multiple orbs in same tile.
	 * @author Madhur Tandon
	 */
	public void drawSphere(boolean extraOrbs)
	{
		if(!extraOrbs)
		{
			if(this.xCoordinate==0 && this.yCoordinate==0)
			{
				// Upper left Corner
				if((this.value+1)%this.criticalMass==1)
				{
					belowOrb = new Sphere();
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					rightOrb = new Sphere();
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.xCoordinate==0 && this.yCoordinate==this.numberOfColumns-1)
			{
				// Upper Right Corner
				if((this.value+1)%this.criticalMass==1)
				{
					belowOrb = new Sphere();
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					leftOrb = new Sphere();
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.xCoordinate==this.numberOfRows-1 && this.yCoordinate==0)
			{
				// Lower Left Corner
				if((this.value+1)%this.criticalMass==1)
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					rightOrb = new Sphere();
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.xCoordinate==this.numberOfRows-1 && this.yCoordinate==this.numberOfColumns-1)
			{
				// Lower Right Corner
				if((this.value+1)%this.criticalMass==1)
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					leftOrb = new Sphere();
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.xCoordinate==0)
			{
				// First Row
				if((this.value+1)%this.criticalMass==1)
				{
					leftOrb = new Sphere();
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					rightOrb = new Sphere();
					rightOrb.setTranslateX(12);
					rightOrb.setTranslateY(12);
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					belowOrb = new Sphere();
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.xCoordinate==this.numberOfRows-1)
			{
				// Last Row
				if((this.value+1)%this.criticalMass==1)
				{
					leftOrb = new Sphere();
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					rightOrb = new Sphere();
					rightOrb.setTranslateX(12);
					rightOrb.setTranslateY(12);
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.yCoordinate==0)
			{
				// First Column
				if((this.value+1)%this.criticalMass==1)
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					belowOrb = new Sphere();
					belowOrb.setTranslateX(12);
					belowOrb.setTranslateY(12);
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					rightOrb = new Sphere();
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.yCoordinate==this.numberOfColumns-1)
			{
				// Last Column
				if((this.value+1)%this.criticalMass==1)
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					belowOrb = new Sphere();
					belowOrb.setTranslateX(12);
					belowOrb.setTranslateY(12);
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					leftOrb = new Sphere();
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else
			{
				// Middle of the grid
				if((this.value+1)%this.criticalMass==1)
				{
					aboveOrb = new Sphere();
					allOrbs.getChildren().add(aboveOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					belowOrb = new Sphere();
					belowOrb.setTranslateX(12);
					belowOrb.setTranslateY(12);
					allOrbs.getChildren().add(belowOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==3)
				{
					leftOrb = new Sphere();
					leftOrb.setTranslateX(-6);
					leftOrb.setTranslateY(12);
					allOrbs.getChildren().add(leftOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					rightOrb = new Sphere();
					allOrbs.getChildren().add(rightOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
		}
	else
		{
			if(this.criticalMass==2)
			{
				if((this.value+1)%this.criticalMass==1)
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.criticalMass==3)
			{
				if((this.value+1)%this.criticalMass==1)
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					otherOrb = new Sphere();
					otherOrb.setTranslateX(12);
					otherOrb.setTranslateY(12);
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
			else if(this.criticalMass==4)
			{
				if((this.value+1)%this.criticalMass==1)
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==2)
				{
					otherOrb = new Sphere();
					otherOrb.setTranslateX(12);
					otherOrb.setTranslateY(12);
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else if((this.value+1)%this.criticalMass==3)
				{
					otherOrb = new Sphere();
					otherOrb.setTranslateX(-6);
					otherOrb.setTranslateY(12);
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
				else
				{
					otherOrb = new Sphere();
					allOrbs.getChildren().add(otherOrb);
					this.value+=1;
					this.boardContainer.tb.board[this.xCoordinate][this.yCoordinate].value+=1;
					this.t.value+=1;
				}
			}
		}

	  PhongMaterial material = new PhongMaterial();
	  material.setDiffuseColor(this.colour);
	  material.setSpecularColor(Color.BLACK);
	  if(leftOrb!=null)
	  {
		  leftOrb.setRadius(10);
		  leftOrb.setMaterial(material);
	  }
	  if(rightOrb!=null)
	  {
		  rightOrb.setRadius(10);
		  rightOrb.setMaterial(material);
	  }
	  if(aboveOrb!=null)
	  {
		  aboveOrb.setRadius(10);
		  aboveOrb.setMaterial(material);
	  }
	  if(belowOrb!=null)
	  {
		  belowOrb.setRadius(10);
		  belowOrb.setMaterial(material);
	  }
	  if(otherOrb!=null)
	  {
		  otherOrb.setRadius(10);
		  otherOrb.setMaterial(material);
	  }
	}
}

