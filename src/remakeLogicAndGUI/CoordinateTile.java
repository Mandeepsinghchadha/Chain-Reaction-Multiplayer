package remakeLogicAndGUI;

import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
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
	static boolean init = true;
	static int currentPlayer = 0;
	static int i = 0;
	int xCoordinate;
	int yCoordinate;
	int playerStatus;
	int value;
	int criticalMass;
	Color colour;
	int numberOfRows;
	int numberOfColumns;
	Sphere orb;
	Group allOrbs = new Group();
	Point3D allAxes[] = {Rotate.X_AXIS,Rotate.Y_AXIS,Rotate.Z_AXIS};
	RotateTransition rotateGroup;
	
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
		
		Rectangle border = new Rectangle(100,100);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(border);
		
		setOnMouseClicked(event -> {
			if(!init)
			{
				if(this.b.countAllActivePlayers(this.b.allPlayers)!=1)
				{
					if(this.b.allPlayers.get(currentPlayer).active)
					{
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
							System.out.println("Empty cells are "+this.b.countEmptyCells());
							System.out.println("Active players are "+this.b.countAllActivePlayers(this.b.allPlayers));
						}
						catch (IllegalMoveException e){
							currentPlayer = (currentPlayer - 1) % this.b.numberOfPlayers;
							System.out.println(e.getMessage());
						}
					}
					currentPlayer = (currentPlayer + 1) % this.b.numberOfPlayers;
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
					}
					i+=1;
				}
				if(i>=this.b.numberOfPlayers)
				{
					init = false;
				}
			}
			rotateGroup.play();
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
		orb = new Sphere();
		if(this.criticalMass==2)
		{
			allOrbs.getChildren().add(orb);
			this.value+=1;
		}
		else if(this.criticalMass==3)
		{
			if((this.value+1)%this.criticalMass==1)
			{
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				orb.setTranslateX(12);
				orb.setTranslateY(12);
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
			else
			{
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
		}
		else if(this.criticalMass==4)
		{
			if((this.value+1)%this.criticalMass==1)
			{
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==2)
			{
				orb.setTranslateX(12);
				orb.setTranslateY(12);
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
			else if((this.value+1)%this.criticalMass==3)
			{
				orb.setTranslateX(-8);
				orb.setTranslateY(12);
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
			else
			{
				allOrbs.getChildren().add(orb);
				this.value+=1;
			}
		}
		PhongMaterial material = new PhongMaterial();
	    material.setDiffuseColor(this.colour);
	    material.setSpecularColor(Color.BLACK);
		orb.setRadius(12);
		orb.setMaterial(material);
	}
}
