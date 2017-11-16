package withoutGUI;

import java.io.Serializable;
import java.util.Stack;

public class gameState implements Serializable{

	private static final long serialVersionUID = 4L;
	public Stack<TileBoard> allStates;
	public TileBoard currentBoard;
	public int currentPlayer;
	public int counterForInitialGamePlay;
	public int counterForInitialBorder;
	public boolean init;
	public String allColours[];
	
	/**
	 * Initializes the Stacks of States used for Undo Operations.
	 * Also, stores the currentBoard used for Resume Operations.
	 * @param tb
	 * @author Madhur Tandon
	 */
	public gameState(TileBoard tb)
	{
		this.allStates = new Stack<TileBoard>();
		this.currentBoard = new TileBoard(tb);
	}
	
	/**
	 * Ensures that only one state is stored in the stack of states.
	 * @param tb
	 * @author Madhur Tandon
	 */
	public void saveState(TileBoard tb)
	{
		if(!this.allStates.isEmpty())
		{
			this.allStates.pop();
		}
		this.allStates.push(tb);
	}
	
	/**
	 * Pops the last state from the stack of states.
	 * @return the tileBoard configuration from the last saved state in the stack.
	 * @author Madhur Tandon
	 */
	public TileBoard loadState()
	{
		return this.allStates.pop();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
