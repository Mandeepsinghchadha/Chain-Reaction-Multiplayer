package remakeLogicAndGUI;

import java.util.ArrayList;
import java.util.Random;

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
import javafx.util.Duration;

public class CoordinateTile extends StackPane {
	
	BoardGUI b;
	public PlayerController p;
	Rectangle border;
	static boolean init = true;
	static int currentPlayer = 0;
	static int i = 0;
	static int k = 0;
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
	
	ParallelTransition pt;

	Sphere rightOrb;
	Sphere leftOrb;
	Sphere aboveOrb;
	Sphere belowOrb;

	CoordinateTile(int x, int y, int m, int n, BoardGUI b)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.playerStatus = 0;
		this.value = 0;
		this.criticalMass = this.getCriticalMass(this.xCoordinate, this.yCoordinate);
		this.colour = Color.WHITESMOKE;
		this.b = b;

		rotateGroup = new RotateTransition(Duration.millis(1500+Math.random()*500), allOrbs);
		rotateGroup.setFromAngle(0);
		rotateGroup.setToAngle(360);
		rotateGroup.setInterpolator(Interpolator.LINEAR);
		rotateGroup.setCycleCount(RotateTransition.INDEFINITE);
		rotateGroup.setAxis(allAxes[new Random().nextInt(allAxes.length)]);
		rotateGroup.setAutoReverse(false);

		transRight = new TranslateTransition(Duration.millis(700));
		transRight.setToX(100);
		transRight.setCycleCount(1);
		transRight.setAutoReverse(false);
		transRight.setOnFinished(e->{
			this.getChildren().remove(this.rightOrb);
		});
		
		transLeft = new TranslateTransition(Duration.millis(700));
		transLeft.setToX(-100);
		transLeft.setCycleCount(1);
		transLeft.setAutoReverse(false);
		transLeft.setOnFinished(e->{
			this.getChildren().remove(this.leftOrb);
		});
		
		transBelow = new TranslateTransition(Duration.millis(700));
		transBelow.setToY(100);
		transBelow.setCycleCount(1);
		transBelow.setAutoReverse(false);
		transBelow.setOnFinished(e->{
			this.getChildren().remove(this.belowOrb);
		});
		
		transAbove = new TranslateTransition(Duration.millis(700));
		transAbove.setToY(-100);
		transAbove.setCycleCount(1);
		transAbove.setAutoReverse(false);
		transAbove.setOnFinished(e->{
			this.getChildren().remove(this.aboveOrb);
		});

		pt = new ParallelTransition();
		pt.setOnFinished(e->{
			this.getChildren().remove(this.aboveOrb);
			this.getChildren().remove(this.belowOrb);
			this.getChildren().remove(this.leftOrb);
			this.getChildren().remove(this.rightOrb);
			
			ArrayList<CoordinateTile> allNeighbours = b.getListOfNeighbours(x, y);
			for(int i=0;i<allNeighbours.size();i+=1)
			{
				allNeighbours.get(i).playerStatus = p.playerNumber;
				allNeighbours.get(i).colour = p.colour;
				
				Sphere[] allNeighbourSpheres = allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]);
				for(int a=0;a<allNeighbourSpheres.length;a+=1)
				{
					PhongMaterial material = new PhongMaterial();
				    material.setDiffuseColor(p.colour);
				    material.setSpecularColor(Color.BLACK);
				    allNeighbourSpheres[a].setMaterial(material);
				}
//				for(Sphere n : allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]))
//				{
//					PhongMaterial material = new PhongMaterial();
//				    material.setDiffuseColor(p.colour);
//				    material.setSpecularColor(Color.BLACK);
//				    n.setMaterial(material);
//				}
				allNeighbours.get(i).drawSphere();
				allNeighbours.get(i).rotateGroup.play();
			}
			
			for(int j=0;j<b.numberOfPlayers;j+=1)
			{
				if(b.allPlayers.get(j).active)
				{
					b.allPlayers.get(j).orbCount = b.playerCount(j+1);
					if(b.allPlayers.get(j).orbCount==0)
					{
						b.allPlayers.get(j).active=false;
					}
				}
			}
			
			ArrayList<CoordinateTile> NeighbourCellsOfJustMovedCell = b.getListOfNeighbours(x,y);
			ArrayList<CoordinateTile> NeighbourCellsWhichAreThemselvesUnstable = p.getAllUnstableNeighbourCells(NeighbourCellsOfJustMovedCell,b);
			
			b.board[x][y].pt.getChildren().clear();
			
			if(b.countAllActivePlayers(b.allPlayers)!=1)
			{
				for(int a=0;a<NeighbourCellsWhichAreThemselvesUnstable.size();a+=1)
				{
					 try {
						p.move(b,NeighbourCellsWhichAreThemselvesUnstable.get(a).xCoordinate,NeighbourCellsWhichAreThemselvesUnstable.get(a).yCoordinate);
					} catch (IllegalMoveException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			b.board[x][y].colour = Color.WHITESMOKE; 
			
			int p = currentPlayer;
			while(!b.allPlayers.get(p).active)
			{
				p = (p + 1) % this.b.numberOfPlayers;
			}

			for(int q=0;q<b.numberOfRows;q+=1)
			{
				for(int r=0;r<b.numberOfColumns;r+=1)
				{
					b.board[q][r].border.setStroke(b.allPlayers.get(p).colour);
				}
			}
			
		});
		
		this.border = new Rectangle(100,100);
		border.setFill(null);
		border.setStroke(Color.RED);

		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(border);

		setOnMouseClicked(event -> {
			if(!init)
			{
				if(this.b.countAllActivePlayers(this.b.allPlayers)!=1)
				{
					if(!b.allPlayers.get(currentPlayer).active)
					{
						while(!b.allPlayers.get(currentPlayer).active)
						{
							currentPlayer = (currentPlayer + 1) % this.b.numberOfPlayers;
						}
					}

					try
					{
						this.b=this.b.allPlayers.get(currentPlayer).move(this.b, this.xCoordinate, this.yCoordinate);

						for(int p=0;p<b.numberOfRows;p+=1)
						{
							for(int q=0;q<b.numberOfColumns;q+=1)
							{
								System.out.print("v"+b.board[p][q].value+"p"+b.board[p][q].playerStatus+" ");
							}
							System.out.println();
						}

						System.out.println("Player "+this.b.allPlayers.get(currentPlayer).playerNumber+" moves");
						System.out.println("Player "+this.b.allPlayers.get(currentPlayer).playerNumber+" count is "+this.b.playerCount(currentPlayer+1));
						System.out.println("Player "+this.b.allPlayers.get(currentPlayer).playerNumber+" old count was "+this.b.allPlayers.get(currentPlayer).orbCount);
						System.out.println("Player status of current tile after move is "+b.board[this.xCoordinate][this.yCoordinate].playerStatus);
						System.out.println("Empty cells are "+this.b.countEmptyCells());							System.out.println("Active players are "+this.b.countAllActivePlayers(this.b.allPlayers));
					}
					catch (IllegalMoveException e){
						currentPlayer = (currentPlayer - 1) % this.b.numberOfPlayers;
						System.out.println(e.getMessage());
					}
				}
				currentPlayer = (currentPlayer + 1) % this.b.numberOfPlayers;
				
				int p = currentPlayer;
				while(!b.allPlayers.get(p).active)
				{
					p = (p + 1) % this.b.numberOfPlayers;
				}

				for(int q=0;q<b.numberOfRows;q+=1)
				{
					for(int r=0;r<b.numberOfColumns;r+=1)
					{
						b.board[q][r].border.setStroke(b.allPlayers.get(p).colour);
					}
				}

				}
			else
			{
				if(i<this.b.numberOfPlayers)
				{
					System.out.println("Player "+this.b.allPlayers.get(i).playerNumber+" moves");
					try
					{
						this.b.allPlayers.get(i).move(this.b, this.xCoordinate, this.yCoordinate);
					}
					catch (IllegalMoveException e)
					{
						System.out.println(e.getMessage());
					}
					if(this.b.playerCount(i+1)>0)
					{
						this.b.allPlayers.get(i).orbCount = 1;
					}
					else
					{
						i-=1;
						k-=1;
					}
					i+=1;

					for(int p=0;p<b.numberOfRows;p+=1)
					{
						for(int q=0;q<b.numberOfColumns;q+=1)
						{
							b.board[p][q].border.setStroke(b.allColours[(k+1)%b.allPlayers.size()]);
						}
					}
					k+=1;

				}
				if(i>=this.b.numberOfPlayers)
				{
					init = false;
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
			}
			else
			{
				rightOrb = new Sphere();
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
		}
		else if(this.xCoordinate==0 && this.yCoordinate==b.numberOfColumns-1)
		{
			// Upper Right Corner
			if((this.value+1)%this.criticalMass==1)
			{
				belowOrb = new Sphere();
				allOrbs.getChildren().add(belowOrb);
				this.value+=1;
			}
			else
			{
				leftOrb = new Sphere();
				allOrbs.getChildren().add(leftOrb);
				this.value+=1;
			}
		}
		else if(this.xCoordinate==b.numberOfRows-1 && this.yCoordinate==0)
		{
			// Lower Left Corner
			if((this.value+1)%this.criticalMass==1)
			{
				aboveOrb = new Sphere();
				allOrbs.getChildren().add(aboveOrb);
				this.value+=1;
			}
			else
			{
				rightOrb = new Sphere();
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
		}
		else if(this.xCoordinate==b.numberOfRows-1 && this.yCoordinate==b.numberOfColumns-1)
		{
			// Lower Right Corner
			if((this.value+1)%this.criticalMass==1)
			{
				aboveOrb = new Sphere();
				allOrbs.getChildren().add(aboveOrb);
				this.value+=1;
			}
			else
			{
				leftOrb = new Sphere();
				allOrbs.getChildren().add(leftOrb);
				this.value+=1;
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
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				rightOrb = new Sphere();
				rightOrb.setTranslateX(12);
				rightOrb.setTranslateY(12);
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
			else
			{
				belowOrb = new Sphere();
				allOrbs.getChildren().add(belowOrb);
				this.value+=1;
			}
		}
		else if(this.xCoordinate==b.numberOfRows-1)
		{
			// Last Row
			if((this.value+1)%this.criticalMass==1)
			{
				leftOrb = new Sphere();
				allOrbs.getChildren().add(leftOrb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				rightOrb = new Sphere();
				rightOrb.setTranslateX(12);
				rightOrb.setTranslateY(12);
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
			else
			{
				aboveOrb = new Sphere();
				allOrbs.getChildren().add(aboveOrb);
				this.value+=1;
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
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				belowOrb = new Sphere();
				belowOrb.setTranslateX(12);
				belowOrb.setTranslateY(12);
				allOrbs.getChildren().add(belowOrb);
				this.value+=1;
			}
			else
			{
				rightOrb = new Sphere();
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
		}
		else if(this.yCoordinate==b.numberOfColumns-1)
		{
			// Last Column
			if((this.value+1)%this.criticalMass==1)
			{
				aboveOrb = new Sphere();
				allOrbs.getChildren().add(aboveOrb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				belowOrb = new Sphere();
				belowOrb.setTranslateX(12);
				belowOrb.setTranslateY(12);
				allOrbs.getChildren().add(belowOrb);
				this.value+=1;
			}
			else
			{
				leftOrb = new Sphere();
				allOrbs.getChildren().add(leftOrb);
				this.value+=1;
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
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				belowOrb = new Sphere();
				belowOrb.setTranslateX(12);
				belowOrb.setTranslateY(12);
				allOrbs.getChildren().add(belowOrb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==3)
			{
				leftOrb = new Sphere();
				leftOrb.setTranslateX(-8);
				leftOrb.setTranslateY(12);
				allOrbs.getChildren().add(leftOrb);
				this.value+=1;
			}
			else
			{
				rightOrb = new Sphere();
				allOrbs.getChildren().add(rightOrb);
				this.value+=1;
			}
		}

		PhongMaterial material = new PhongMaterial();
	  material.setDiffuseColor(this.colour);
	  material.setSpecularColor(Color.BLACK);
	  if(leftOrb!=null)
	  {
	   leftOrb.setRadius(12);
	   leftOrb.setMaterial(material);
	  }
	  if(rightOrb!=null)
	  {
	   rightOrb.setRadius(12);
	   rightOrb.setMaterial(material);
	  }
	  if(aboveOrb!=null)
	  {
	   aboveOrb.setRadius(12);
	   aboveOrb.setMaterial(material);
	  }
	  if(belowOrb!=null)
	  {
	   belowOrb.setRadius(12);
	   belowOrb.setMaterial(material);
	  }
	}
}
