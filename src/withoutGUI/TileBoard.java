package withoutGUI;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.paint.Color;

public class TileBoard implements Serializable {

	/**
	 *
	 */
	public boolean undoOnce = true;
	private static final long serialVersionUID = 2L;
	public boolean lastGameCompleted;
	public int numberOfRows;
	public int numberOfColumns;
	public TileCell[][] board;
	public int numberOfPlayers;
	public ArrayList<Player> allPlayers;
	public static String[] allColours = {Color.RED.toString(),Color.GREEN.toString(),Color.BLUE.toString(),Color.YELLOW.toString(),Color.MAGENTA.toString(),Color.CYAN.toString(),Color.ORANGE.toString(),Color.GRAY.toString()};

	public TileBoard(int m, int n, int numberOfPlayers)
	{
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.numberOfPlayers = numberOfPlayers;
		this.lastGameCompleted = false;

		this.board = new TileCell[this.numberOfRows][this.numberOfColumns];
		this.allPlayers = new ArrayList<Player>();

		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				this.board[i][j] = new TileCell(this.numberOfRows,this.numberOfColumns,i,j);
			}
		}

		for(int i=0;i<this.numberOfPlayers;i+=1)
		{
			Player p = new Player(i+1,Color.valueOf(allColours[i]));
			this.allPlayers.add(p);
		}
	}

	public TileBoard(TileBoard tb) {
		// TODO Auto-generated constructor stub
		this.numberOfRows = tb.numberOfRows;
		this.numberOfColumns = tb.numberOfColumns;
		this.numberOfPlayers = tb.numberOfPlayers;
		this.lastGameCompleted = tb.lastGameCompleted;

		this.board = new TileCell[tb.numberOfRows][tb.numberOfColumns];
		this.allPlayers = new ArrayList<>(tb.allPlayers);

		for(int i=0;i<this.numberOfRows;i+=1)
		{
			for(int j=0;j<this.numberOfColumns;j+=1)
			{
				this.board[i][j] = new TileCell(tb.board[i][j]);
			}
		}

		for(int i=0;i<this.numberOfPlayers;i+=1)
		{
			this.allPlayers.add(tb.allPlayers.get(i));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
