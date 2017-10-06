package remakeLogicAndGUI;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;

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
		
		Rectangle border = new Rectangle(100,100);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(border);
		
		setOnMouseClicked(event -> {
			if(!init)
			{
				if(b.countAllActivePlayers(b.allPlayers)!=1)
				{
					if(b.allPlayers.get(currentPlayer).active)
					{
						b.allPlayers.get(currentPlayer).move(b, this.xCoordinate, this.yCoordinate);
						if(b.playerCount(currentPlayer+1)<=b.allPlayers.get(currentPlayer).orbCount)
						{
							currentPlayer = (currentPlayer - 1) % b.numberOfPlayers;
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
					}
					currentPlayer = (currentPlayer + 1) % b.numberOfPlayers;
				}
			}
			else
			{
				if(i<b.numberOfPlayers)
				{
					b.allPlayers.get(i).move(b, x, y);
					if(b.playerCount(i+1)>0)
					{
						b.allPlayers.get(i).orbCount = 1;
					}
					else
					{
						i-=1;
					}
					i+=1;
				}
				if(i>=b.numberOfPlayers)
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
				orb.setTranslateX(-12);
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
