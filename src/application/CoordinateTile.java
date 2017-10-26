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
	
	gameState gs;
	TileCell t;
	BoardGUI boardContainer;
	PlayerController playerContainer;
	Rectangle border;
	
	static boolean init = true;
	static int currentPlayer = 0;
	static int counterForInitialGamePlay = 0;
	static int counterForInitialBorder = 0;
	
	int xCoordinate;
	int yCoordinate;
	int playerStatus;
	int value;
	int criticalMass;
	
	Color colour;
	int numberOfRows;
	int numberOfColumns;
	
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

	CoordinateTile(int x, int y, int m, int n, BoardGUI b, int squareSize)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.playerStatus = 0;
		this.value = 0;
		this.criticalMass = this.getCriticalMass(this.xCoordinate, this.yCoordinate);
		this.colour = Color.WHITESMOKE;
		this.boardContainer = b;
		this.t = new TileCell(this.numberOfRows,this.numberOfColumns,this.xCoordinate,this.yCoordinate);
		this.gs = new gameState();

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

				allNeighbours.get(i).drawSphere();
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
			
//			for(int p=0;p<b.numberOfRows;p+=1)
//			{
//				for(int q=0;q<b.numberOfColumns;q+=1)
//				{
//					System.out.print("v"+b.board[p][q].value+"p"+b.board[p][q].playerStatus+" ");
//				}
//				System.out.println();
//			}
//
//			System.out.println("Player "+this.boardContainer.allPlayers.get(currentPlayer).playerNumber+" moves");
//			System.out.println("Player "+this.boardContainer.allPlayers.get(currentPlayer).playerNumber+" count is "+this.boardContainer.playerCount(currentPlayer+1));
//			System.out.println("Player "+this.boardContainer.allPlayers.get(currentPlayer).playerNumber+" old count was "+this.boardContainer.allPlayers.get(currentPlayer).orbCount);
//			System.out.println("Player status of current tile after move is "+b.board[this.xCoordinate][this.yCoordinate].playerStatus);
//			System.out.println("Empty cells are "+this.boardContainer.countEmptyCells());							
//			System.out.println("Active players are "+this.boardContainer.countAllActivePlayers(this.boardContainer.allPlayers));
			
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
			
			b.board[x][y].colour = Color.WHITESMOKE;
			b.board[x][y].t.colour = b.board[x][y].colour.toString();
			b.tb.board[x][y].colour = b.board[x][y].colour.toString();
			
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

		setOnMouseClicked(event -> {
			if(!init)
			{
				if(this.boardContainer.countAllActivePlayers(this.boardContainer.allPlayers)!=1)
				{
					if(!b.allPlayers.get(currentPlayer).active)
					{
						while(!b.allPlayers.get(currentPlayer).active)
						{
							currentPlayer = (currentPlayer + 1) % this.boardContainer.numberOfPlayers;
							TileCell.currentPlayer = (TileCell.currentPlayer + 1) % this.boardContainer.numberOfPlayers;
						}
					}

					try
					{
						this.gs.saveState(this.boardContainer.tb);
						
						TileBoard previousState = this.gs.allStates.peek();
						System.out.println("Details of Previous State");
						for(int a=0;a<previousState.numberOfRows;a+=1)
						{
							for(int c=0;c<previousState.numberOfColumns;c+=1)
							{
//								System.out.print("v"+this.boardContainer.tb.board[a][c].value+"p"+loadFromSave.board[a][c].playerStatus+" ");
								System.out.print("v"+previousState.board[a][c].value+"p"+previousState.board[a][c].playerStatus+" ");
							}
							System.out.println();
						}
						System.out.println("Border colour of previous state is "+previousState.board[0][0].borderColour);
						System.out.println();
						
						
						this.boardContainer.allPlayers.get(currentPlayer).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
						
//						this.gs.serialize(this.boardContainer.tb);
//						TileBoard loadFromSave = this.gs.deserialize();
//						System.out.println("Details of Current State");
//						for(int a=0;a<loadFromSave.numberOfRows;a+=1)
//						{
//							for(int c=0;c<loadFromSave.numberOfColumns;c+=1)
//							{
//								System.out.print("v"+loadFromSave.board[a][c].value+"p"+loadFromSave.board[a][c].playerStatus+" ");
//							}
//							System.out.println();
//						}
//						System.out.println();
					}
					catch (IllegalMoveException e){
						currentPlayer = (currentPlayer - 1) % this.boardContainer.numberOfPlayers;
						TileCell.currentPlayer = (TileCell.currentPlayer - 1) % this.boardContainer.numberOfPlayers;
						System.out.println(e.getMessage());
					}
					catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
//					catch (ClassNotFoundException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
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
					}
				}
			}
			else
			{
				if(counterForInitialGamePlay<this.boardContainer.numberOfPlayers)
				{
					System.out.println("Player "+this.boardContainer.allPlayers.get(counterForInitialGamePlay).playerNumber+" moves");
					try
					{
						this.gs.saveState(this.boardContainer.tb);
						
						TileBoard previousState = this.gs.allStates.peek();
						System.out.println("Details of Previous State");
						for(int a=0;a<previousState.numberOfRows;a+=1)
						{
							for(int c=0;c<previousState.numberOfColumns;c+=1)
							{
//								System.out.print("v"+this.boardContainer.tb.board[a][c].value+"p"+loadFromSave.board[a][c].playerStatus+" ");
								System.out.print("v"+previousState.board[a][c].value+"p"+previousState.board[a][c].playerStatus+" ");
							}
							System.out.println();
						}
						System.out.println("Border colour of previous state is "+previousState.board[0][0].borderColour);
						System.out.println();
						
						
						this.boardContainer.allPlayers.get(counterForInitialGamePlay).move(this.boardContainer, this.xCoordinate, this.yCoordinate);
						
//						this.gs.serialize(this.boardContainer.tb);
//						TileBoard loadFromSave = this.gs.deserialize();
//						for(int a=0;a<loadFromSave.numberOfRows;a+=1)
//						{
//							for(int c=0;c<loadFromSave.numberOfColumns;c+=1)
//							{
//								System.out.print("v"+loadFromSave.board[a][c].value+"p"+loadFromSave.board[a][c].playerStatus+" ");
//							}
//							System.out.println();
//						}
//						System.out.println();
					}
					catch (IllegalMoveException e)
					{
						System.out.println(e.getMessage());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
//					catch (ClassNotFoundException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
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
			}
		});
		getChildren().add(allOrbs);
	}

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

	public void drawSphere()
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
	}
}

