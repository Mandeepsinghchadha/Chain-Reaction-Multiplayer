package application;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import withoutGUI.TileBoard;
import withoutGUI.TileCell;

public class BoardGUI {

	TileBoard tb;
	int numberOfRows;
	int numberOfColumns;
	public static long startTime;
	CoordinateTile[][] board;
	int numberOfPlayers;
	public boolean shownPrompt;
	ArrayList<PlayerController> allPlayers;
	static Color[] allColours = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.CYAN,Color.ORANGE,Color.GRAY};
	
	BoardGUI(int m, int n, int numberOfPlayers)
	{
		this.shownPrompt=true;
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.numberOfPlayers = numberOfPlayers;
		board = new CoordinateTile[this.numberOfRows][this.numberOfColumns];
		this.tb = new TileBoard(this.numberOfRows,this.numberOfColumns,this.numberOfPlayers);
		
		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				if(this.numberOfRows==9)
				{
					this.board[i][j] = new CoordinateTile(i,j,this.numberOfRows,this.numberOfColumns,this,50);
				}
				else
				{
					this.board[i][j] = new CoordinateTile(i,j,this.numberOfRows,this.numberOfColumns,this,40);
				}
			}
		}
		
		this.allPlayers = new ArrayList<PlayerController>();
		for(int i=0;i<numberOfPlayers;i+=1)
		{
			PlayerController p = new PlayerController(i+1,allColours[i]);
			allPlayers.add(p);
		}
		
		for(int i=0;i<tb.numberOfRows;i+=1)
		{
			for(int j=0;j<tb.numberOfColumns;j+=1)
			{
				this.tb.board[i][j].value = this.board[i][j].t.value;
			}
		}
	}
	
	public void loadGUIfromState(TileBoard tb, boolean resumeSavedGame)
	{
		if(!resumeSavedGame)
		{
				if(this.tb.undoOnce)
				{
				/*
				 * Resets The Board
				 */
				for(int i=0;i<tb.numberOfRows;i+=1)
				{
					for(int j=0;j<tb.numberOfColumns;j+=1)
					{
						this.board[i][j].rotateGroup.stop();
						this.board[i][j].parallelSplit.stop();
						this.board[i][j].allOrbs.getChildren().clear();
						this.board[i][j].border.setStroke(allColours[0]);
						this.board[i][j].value = 0;
						this.board[i][j].playerStatus = 0;
						this.board[i][j].colour = allColours[0];
					}
				}
				
				/*
				 * Designs Board According to State
				 */
				for(int i=0;i<tb.numberOfRows;i+=1)
				{
					for(int j=0;j<tb.numberOfColumns;j+=1)
					{
						
						this.board[i][j].colour = Color.valueOf(tb.board[i][j].colour);
						this.board[i][j].playerStatus = tb.board[i][j].playerStatus;
						this.board[i][j].border.setStroke(Color.valueOf(tb.board[i][j].borderColour));
						for(int k=0;k<tb.board[i][j].value;k+=1)
						{
							this.board[i][j].drawSphere();
						}
						this.board[i][j].rotateGroup.play();
						
						this.tb.board[i][j].colour = this.board[i][j].colour.toString();
						this.tb.board[i][j].playerStatus = this.board[i][j].playerStatus;
						this.tb.board[i][j].value = this.board[i][j].value;
						this.tb.board[i][j].borderColour = tb.board[i][j].borderColour;
					}
				}
				
				if(!CoordinateTile.init && CoordinateTile.counterForInitialGamePlay>this.numberOfPlayers)
				{
					CoordinateTile.currentPlayer = (((CoordinateTile.currentPlayer - 1) % this.numberOfPlayers) + this.numberOfPlayers) % this.numberOfPlayers;
				
					for(int i=0;i<this.numberOfPlayers;i+=1)
					{
						if(this.playerCount(i+1)>0)
						{
							this.allPlayers.get(i).active = true;
						}
						else
						{
							this.allPlayers.get(i).active = false;
						}
					}
				}
				else
				{
					CoordinateTile.counterForInitialGamePlay = (CoordinateTile.counterForInitialGamePlay - 1);
					TileCell.counterForInitialGamePlay-=1;
					CoordinateTile.counterForInitialBorder = (CoordinateTile.counterForInitialBorder - 1);
					TileCell.counterForInitialBorder-=1;
					
					if(this.playerCount(CoordinateTile.counterForInitialGamePlay+1)<=0)
					{
						this.allPlayers.get(CoordinateTile.counterForInitialGamePlay).orbCount = -2147483648;
						this.allPlayers.get(CoordinateTile.counterForInitialGamePlay).p.orbCount = -2147483648;
					}
					else
					{
						CoordinateTile.counterForInitialGamePlay+=1;
						TileCell.counterForInitialGamePlay+=1;
						CoordinateTile.counterForInitialBorder+=1;
						TileCell.counterForInitialBorder+=1;
					}
				}
				
				this.tb.undoOnce = false;
				mainApp.undoButton.setDisable(true);
			}
		}
		else if(resumeSavedGame)
		{
			for(int i=0;i<tb.numberOfRows;i+=1)
			{
				for(int j=0;j<tb.numberOfColumns;j+=1)
				{
					this.board[i][j].rotateGroup.stop();
					this.board[i][j].parallelSplit.stop();
					this.board[i][j].allOrbs.getChildren().clear();
					this.board[i][j].border.setStroke(allColours[0]);
					this.board[i][j].value = 0;
					this.board[i][j].playerStatus = 0;
					this.board[i][j].colour = allColours[0];
				}
			}
			
			/*
			 * Designs Board According to State
			 */
			for(int i=0;i<tb.numberOfRows;i+=1)
			{
				for(int j=0;j<tb.numberOfColumns;j+=1)
				{
					
					this.board[i][j].colour = Color.valueOf(tb.board[i][j].colour);
					this.board[i][j].playerStatus = tb.board[i][j].playerStatus;
					this.board[i][j].border.setStroke(Color.valueOf(tb.board[i][j].borderColour));
					for(int k=0;k<tb.board[i][j].value;k+=1)
					{
						this.board[i][j].drawSphere();
					}
					this.board[i][j].rotateGroup.play();
					
					this.tb.board[i][j].colour = this.board[i][j].colour.toString();
					this.tb.board[i][j].playerStatus = this.board[i][j].playerStatus;
					this.tb.board[i][j].value = this.board[i][j].value;
					this.tb.board[i][j].borderColour = tb.board[i][j].borderColour;
				}
			}
			
			for(int i=0;i<this.numberOfPlayers;i+=1)
			{
				this.allPlayers.get(i).active = tb.allPlayers.get(i).active;
			}
		}
	}
	
	public boolean checkValidCoordinate(int i, int j)
	{
		if(i>=0 && i<this.numberOfRows && j>=0 && j<this.numberOfColumns)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<CoordinateTile> getListOfNeighbours(int i, int j)
	{
		ArrayList<CoordinateTile> allNeighbours = new ArrayList<CoordinateTile>();
		if(this.checkValidCoordinate(i, j+1))
		{
			allNeighbours.add(this.board[i][j+1]);
		}
		if(this.checkValidCoordinate(i, j-1))
		{
			allNeighbours.add(this.board[i][j-1]);
		}
		if(this.checkValidCoordinate(i+1, j))
		{
			allNeighbours.add(this.board[i+1][j]);
		}
		if(this.checkValidCoordinate(i-1, j))
		{
			allNeighbours.add(this.board[i-1][j]);
		}
		return allNeighbours;
	}
	
	public int playerCount(int playerStatus)
	{
		int count = 0;
		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				if(this.board[i][j].playerStatus==playerStatus)
				{
					count+=this.board[i][j].value;
				}
			}
		}
		return count;
	}
	
	public int countEmptyCells()
	{
		int count = 0;
		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				if(this.board[i][j].value==0)
				{
					count+=1;
				}
			}
		}
		return count;
	}
	
	public int countAllActivePlayers(ArrayList<PlayerController> allPlayers)
	{
		int count = 0;
		for(int i=0;i<allPlayers.size();i+=1)
		{
			if(allPlayers.get(i).active)
			{
				count+=1;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
