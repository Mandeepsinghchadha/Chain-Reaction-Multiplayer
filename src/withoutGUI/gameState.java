package withoutGUI;

import java.io.Serializable;
import java.util.Stack;

public class gameState implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4L;
	public Stack<TileBoard> allStates;
	public TileBoard currentBoard;
	public int currentPlayer;
	public int counterForInitialGamePlay;
	public int counterForInitialBorder;
	public boolean init;
	public String allColours[];

	public gameState(TileBoard tb)
	{
		this.allStates = new Stack<TileBoard>();
		this.currentBoard = new TileBoard(tb);
	}

	public void saveState(TileBoard tb)
	{
		if(!this.allStates.isEmpty())
		{
			this.allStates.pop();
		}
		this.allStates.push(tb);
	}

	public TileBoard loadState()
	{
		return this.allStates.pop();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
