package withoutGUI;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class TileCell implements Serializable {
	
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

	/**
	 * Initializes a particular tile cell with the following initial properties.
	 * @param x	The X Coordinate
	 * @param y	The Y Coordinate
	 * @param m	The total number of Rows
	 * @param n	The total number of Columns
	 * @author Madhur Tandon
	 */
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
	
	/**
	 * A copy constructor which creates a new object with
	 * properties of the passed in TileCell object.
	 * @param tc
	 * @author Madhur Tandon
	 */
	public TileCell(TileCell tc) {
		// TODO Auto-generated constructor stub
		this.numberOfRows = tc.numberOfRows;
		this.numberOfColumns = tc.numberOfColumns;
		this.xCoordinate = tc.xCoordinate;
		this.yCoordinate = tc.yCoordinate;
		this.playerStatus = tc.playerStatus;
		this.value = tc.value;
		this.criticalMass = tc.criticalMass;
		this.colour = tc.colour;
		this.borderColour = tc.borderColour;
	}

	/**
	 * Calculates the appropriate critical mass for the cell (2 for corner cell, 3 for sides and 4 otherwise).
	 * @param i The given X coordinate
	 * @param j The given Y coordinate
	 * @return An integer, the calculated critical mass.
	 * @author Madhur Tandon
	 */
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
