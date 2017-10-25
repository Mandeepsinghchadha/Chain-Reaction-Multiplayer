package Serialization;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.paint.Color;

public class BoardGUI implements Serializable {

	int numberOfRows;
	int numberOfColumns;
	CoordinateTile[][] board;
	ArrayList<PlayerController> allPlayers;
	int numberOfPlayers;
	transient static Color[] allColours = {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.CYAN,Color.ORANGE,Color.GRAY};
	
	public BoardGUI(int m, int n, int numberOfPlayers)
	{
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.numberOfPlayers = numberOfPlayers;
		board = new CoordinateTile[this.numberOfRows][this.numberOfColumns];
		
		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				this.board[i][j] = new CoordinateTile(i,j,this.numberOfRows,this.numberOfColumns,this);
			}
		}
		
		this.allPlayers = new ArrayList<PlayerController>();
		for(int i=0;i<numberOfPlayers;i+=1)
		{
			PlayerController p = new PlayerController(i+1,allColours[i]);
			allPlayers.add(p);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
