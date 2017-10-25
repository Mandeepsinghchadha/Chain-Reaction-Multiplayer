package withoutGUI;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class TileCell implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean init = true;
	public static int currentPlayer = 0;
	public static int counterForInitialGamePlay = 0;
	public static int counterForInitialBorder = 0;
	
	public int xCoordinate;
	public int yCoordinate;
	public int playerStatus;
	public int value;
	public int criticalMass;
	public String borderColour;
	
	public String colour;
	public int numberOfRows;
	public int numberOfColumns;

	public TileCell(int x, int y, int m, int n)
	{
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.playerStatus = 0;
		this.value = 0;
		this.criticalMass = this.getCriticalMass(this.xCoordinate, this.yCoordinate);
		this.colour = Color.WHITESMOKE.toString();
		this.borderColour = Color.WHITESMOKE.toString();
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
