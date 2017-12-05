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
	/**
	 * Initializes the player state giving it the appropriate player index and color of orb.
	 * @param playerNumber The index player is to be initialized with.
	 * @param c The color player is to be initialized with.
	 * @author Madhur Tandon
	 */
	public PlayerController(int playerNumber, Color c)
	{
		this.orbCount = Integer.MIN_VALUE;
		this.playerNumber = playerNumber;
		this.colour = c;
		this.active = true;
		this.p = new Player(this.playerNumber,this.colour);
	}
	/**
	 * Adds the orb in case the cell is empty, or doesn't contain enough orbs for explosion. In case of explosion, fills the adjacent neighboring cells calling the parallelsplit animation.
	 * @param b The board where the game is being played
	 * @param x The x coordinate from where the move begins (where mouse click has happened)
	 * @param y The y coordinate from where the move begins (where mouse click has happened)
	 * @throws IllegalMoveException Throws this exception in case the 
	 * @throws IOException Calls in case the parallelsplit animation throws IOException
	 * @author Madhur Tandon
	 */
	public void move(BoardGUI b, int x, int y) throws IllegalMoveException, IOException
	{
		System.out.println("Player Number: "+this.playerNumber);
		if((b.board[x][y].playerStatus==this.playerNumber)||(b.board[x][y].playerStatus==0))
		{
			
			b.board[x][y].playerContainer = this;
			
			b.board[x][y].playerStatus = this.playerNumber;
			b.board[x][y].t.playerStatus = b.board[x][y].playerStatus;
			b.tb.board[x][y].playerStatus =  b.board[x][y].playerStatus;
			
			b.board[x][y].colour = this.colour;
			b.board[x][y].t.colour = b.board[x][y].colour.toString();
			b.tb.board[x][y].colour = b.board[x][y].colour.toString();
			
			if(b.board[x][y].value<b.board[x][y].criticalMass)
			{
				b.board[x][y].getChildren().remove(b.board[x][y].rightOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].leftOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].aboveOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].belowOrb);
				
				b.board[x][y].drawSphere(false);
				b.board[x][y].rotateGroup.play();
			}
			
			if(b.board[x][y].value==b.board[x][y].criticalMass)
			{	
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
			
			if(b.board[x][y].value>b.board[x][y].criticalMass)
			{	
				int numberOfExtraOrbs = b.board[x][y].value % b.board[x][y].criticalMass;
				
				b.board[x][y].value = 0;
				b.tb.board[x][y].value=0;
				b.board[x][y].t.value = 0;
				
				b.board[x][y].rotateGroup.stop();
				
				b.board[x][y].getChildren().remove(b.board[x][y].rightOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].leftOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].aboveOrb);
				b.board[x][y].getChildren().remove(b.board[x][y].belowOrb);
				b.board[x][y].allOrbs.getChildren().clear();
				
				for(int i=0;i<numberOfExtraOrbs;i+=1)
				{
					System.out.println(b.board[x][y].value);
					b.board[x][y].drawSphere(true);
					System.out.println(b.board[x][y].value);
				}
				
				System.out.println("Greater Case");
				
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
				
				if(b.board[x][y].transAbove.getNode()!=null && !b.board[x][y].parallelSplit.getChildren().contains(b.board[x][y].transAbove))
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transAbove);
				}
				if(b.board[x][y].transBelow.getNode()!=null && !b.board[x][y].parallelSplit.getChildren().contains(b.board[x][y].transBelow))
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transBelow);
				}
				if(b.board[x][y].transLeft.getNode()!=null && !b.board[x][y].parallelSplit.getChildren().contains(b.board[x][y].transLeft))
				{
					b.board[x][y].parallelSplit.getChildren().add(b.board[x][y].transLeft);
				}
				if(b.board[x][y].transRight.getNode()!=null && !b.board[x][y].parallelSplit.getChildren().contains(b.board[x][y].transRight))
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
	/**
	 * Finds the adjacent neighboring cells ready for explosion,
	 * @param allNeighbours The list of all adjacent neighbors.
	 * @param b Board where the game is currently being played.
	 * @return Returns the list of neighbors which are going to explode by addition of just 1 orb.
	 * @author Madhur Tandon
	 */
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

