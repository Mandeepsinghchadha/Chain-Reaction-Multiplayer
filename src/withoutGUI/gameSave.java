package withoutGUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class gameSave implements Serializable{

	private static final long serialVersionUID = 5L;
	
	/**
	 * Serializes the gameState given by ``gs`` and writes it to
	 * a file named ``gameState.db``.
	 * @param gs The game state to be serialized
	 * @throws IOException In case errors arise while writing to file
	 * @author Madhur Tandon
	 */
	public void serialize(gameState gs) throws IOException
	{
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(new FileOutputStream("gameState.db"));
			out.writeObject(gs);
		}
		finally
		{
			out.close();
		}
	}
	
	/**
	 * Reads the gameState stored in the file "gameState.db" and returns
	 * the gameState object which can be used for Resume operation.
	 * @return gameState object used to restore the game
	 * @throws IOException IOException In case errors arise while reading from file
	 * @throws ClassNotFoundException Incase of mismatch between saved class and class in the code
	 * @author Madhur Tandon
	 */
	public gameState deserialize() throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream("gameState.db"));
			gameState GS = (gameState) in.readObject();
			return GS;
		}
		finally
		{
			in.close();
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
