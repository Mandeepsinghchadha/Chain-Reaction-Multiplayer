package remakeLogicAndGUI;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class PlayerController {
	
	int orbCount;
	int playerNumber;
	Color colour;
	boolean active;
	
	PlayerController(int playerNumber, Color c)
	{
		this.orbCount = Integer.MIN_VALUE;
		this.playerNumber = playerNumber;
		this.colour = c;
		this.active = true;
	}
	
	public BoardGUI move(BoardGUI b, int x, int y) throws IllegalMoveException
	{
		if((b.board[x][y].playerStatus==this.playerNumber)||(b.board[x][y].playerStatus==0))
		{
			b.board[x][y].p = this;
			b.board[x][y].playerStatus = this.playerNumber;
			b.board[x][y].colour = this.colour;
			b.board[x][y].drawSphere();
			b.board[x][y].rotateGroup.play();
			if(b.board[x][y].value>=b.board[x][y].criticalMass)
			{
				b.board[x][y].value = 0;
				b.board[x][y].playerStatus = 0;
				b.board[x][y].rotateGroup.stop();
				
				b.board[x][y].allOrbs.getChildren().clear();

				if(b.board[x][y].rightOrb!=null)
				{
					b.board[x][y].getChildren().add(b.board[x][y].rightOrb);
					b.board[x][y].transRight.setNode(b.board[x][y].rightOrb);
				}
				if(b.board[x][y].leftOrb!=null)
				{
					b.board[x][y].getChildren().add(b.board[x][y].leftOrb);
					b.board[x][y].transLeft.setNode(b.board[x][y].leftOrb);
				}
				if(b.board[x][y].aboveOrb!=null)
				{
					b.board[x][y].getChildren().add(b.board[x][y].aboveOrb);
					b.board[x][y].transAbove.setNode(b.board[x][y].aboveOrb);
				}
				if(b.board[x][y].belowOrb!=null)
				{
					b.board[x][y].getChildren().add(b.board[x][y].belowOrb);
					b.board[x][y].transBelow.setNode(b.board[x][y].belowOrb);
				}
				
				if(b.board[x][y].transAbove.getNode()!=null)
				{
					b.board[x][y].pt.getChildren().add(b.board[x][y].transAbove);
				}
				if(b.board[x][y].transBelow.getNode()!=null)
				{
					b.board[x][y].pt.getChildren().add(b.board[x][y].transBelow);
				}
				if(b.board[x][y].transLeft.getNode()!=null)
				{
					b.board[x][y].pt.getChildren().add(b.board[x][y].transLeft);
				}
				if(b.board[x][y].transRight.getNode()!=null)
				{
					b.board[x][y].pt.getChildren().add(b.board[x][y].transRight);
				}
				b.board[x][y].pt.play();
				
//				ArrayList<CoordinateTile> allNeighbours = b.getListOfNeighbours(x, y);
//				for(int i=0;i<allNeighbours.size();i+=1)
//				{
//					allNeighbours.get(i).playerStatus = this.playerNumber;
//					allNeighbours.get(i).colour = this.colour;
//					for(Sphere n : allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]))
//					{
//						PhongMaterial material = new PhongMaterial();
//					    material.setDiffuseColor(this.colour);
//					    material.setSpecularColor(Color.BLACK);
//					    n.setMaterial(material);
//					}
//					allNeighbours.get(i).drawSphere();
//					allNeighbours.get(i).rotateGroup.play();
//				}
				
				ArrayList<CoordinateTile> NeighbourCellsOfJustMovedCell = b.getListOfNeighbours(x,y);
				ArrayList<CoordinateTile> NeighbourCellsWhichAreThemselvesUnstable = getAllUnstableNeighbourCells(NeighbourCellsOfJustMovedCell,b);
				
//				for(int j=0;j<b.numberOfPlayers;j+=1)
//				{
//					if(b.allPlayers.get(j).active)
//					{
//						b.allPlayers.get(j).orbCount = b.playerCount(j+1);
//						if(b.allPlayers.get(j).orbCount==0)
//						{
//							b.allPlayers.get(j).active=false;
//						}
//					}
//				}
				
				b.board[x][y].pt.getChildren().clear();
				
				if(b.countAllActivePlayers(b.allPlayers)!=1)
				{
					for(int p=0;p<NeighbourCellsWhichAreThemselvesUnstable.size();p+=1)
					{
						b = move(b,NeighbourCellsWhichAreThemselvesUnstable.get(p).xCoordinate,NeighbourCellsWhichAreThemselvesUnstable.get(p).yCoordinate);
					}
				}
				b.board[x][y].colour = Color.WHITESMOKE; 
				return b;
			}
			else
			{
				return b;
			}
		}
		else
		{
			throw new IllegalMoveException("Illegal Move!");
		}
	}
	
	public ArrayList<CoordinateTile> getAllUnstableNeighbourCells(ArrayList<CoordinateTile> allNeighbours, BoardGUI b)
	{
		ArrayList<CoordinateTile> UnstableNeighbours = new ArrayList<CoordinateTile>();
		for(int i=0;i<allNeighbours.size();i+=1)
		{
			if(allNeighbours.get(i).value>=b.board[allNeighbours.get(i).xCoordinate][allNeighbours.get(i).yCoordinate].criticalMass)
			{
				UnstableNeighbours.add(allNeighbours.get(i));
			}
		}
		return UnstableNeighbours;
	}
}
