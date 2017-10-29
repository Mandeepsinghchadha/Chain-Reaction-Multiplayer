package application;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import withoutGUI.Player;

public class PlayerController {
	
	Player p;
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
		this.p = new Player(this.playerNumber,this.colour);
	}
	
	public void move(BoardGUI b, int x, int y) throws IllegalMoveException, IOException
	{
		if((b.board[x][y].playerStatus==this.playerNumber)||(b.board[x][y].playerStatus==0))
		{
			b.board[x][y].playerContainer = this;
			
			b.board[x][y].playerStatus = this.playerNumber;
			b.board[x][y].t.playerStatus = b.board[x][y].playerStatus;
			b.tb.board[x][y].playerStatus =  b.board[x][y].playerStatus;
			
			b.board[x][y].colour = this.colour;
			b.board[x][y].t.colour = b.board[x][y].colour.toString();
			b.tb.board[x][y].colour = b.board[x][y].colour.toString();
			
			b.board[x][y].getChildren().remove(b.board[x][y].rightOrb);
			b.board[x][y].getChildren().remove(b.board[x][y].leftOrb);
			b.board[x][y].getChildren().remove(b.board[x][y].aboveOrb);
			b.board[x][y].getChildren().remove(b.board[x][y].belowOrb);
			
			b.board[x][y].drawSphere();
			b.board[x][y].rotateGroup.play();
			
			if(b.board[x][y].value>=b.board[x][y].criticalMass)
			{
				b.board[x][y].value = 0;
				b.tb.board[x][y].value=0;
				b.board[x][y].t.value = 0;
				
				b.board[x][y].playerStatus = 0;
				b.tb.board[x][y].playerStatus=0;
				b.board[x][y].t.playerStatus = 0;
				
				b.board[x][y].rotateGroup.stop();
				
				b.board[x][y].getChildren().remove(b.board[x][y].rightOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].leftOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].aboveOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].belowOrb);
				b.board[x][y].allOrbs.getChildren().clear();

				if(b.board[x][y].rightOrb!=null)
				{
					b.board[x][y].rightOrb.setTranslateX(0);
					b.board[x][y].rightOrb.setTranslateY(0);
					b.board[x][y].getChildren().add(b.board[x][y].rightOrb);
					b.board[x][y].transRight.setNode(b.board[x][y].rightOrb);
				}
				if(b.board[x][y].leftOrb!=null)
				{
					b.board[x][y].leftOrb.setTranslateX(0);
					b.board[x][y].leftOrb.setTranslateY(0);
					b.board[x][y].getChildren().add(b.board[x][y].leftOrb);
					b.board[x][y].transLeft.setNode(b.board[x][y].leftOrb);
				}
				if(b.board[x][y].aboveOrb!=null)
				{
					b.board[x][y].aboveOrb.setTranslateX(0);
					b.board[x][y].aboveOrb.setTranslateY(0);
					b.board[x][y].getChildren().add(b.board[x][y].aboveOrb);
					b.board[x][y].transAbove.setNode(b.board[x][y].aboveOrb);
				}
				if(b.board[x][y].belowOrb!=null)
				{
					b.board[x][y].belowOrb.setTranslateX(0);
					b.board[x][y].belowOrb.setTranslateY(0);
					b.board[x][y].getChildren().add(b.board[x][y].belowOrb);
					b.board[x][y].transBelow.setNode(b.board[x][y].belowOrb);
				}
				
				if(b.board[x][y].transAbove.getNode()!=null)
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transAbove);
				}
				if(b.board[x][y].transBelow.getNode()!=null)
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transBelow);
				}
				if(b.board[x][y].transLeft.getNode()!=null)
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transLeft);
				}
				if(b.board[x][y].transRight.getNode()!=null)
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transRight);
				}
				b.board[x][y].parallelSplit.play();
			}
			else
			{
				return;
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

