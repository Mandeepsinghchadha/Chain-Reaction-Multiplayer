package withoutGUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class gameState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
	public void serialize(TileBoard tb) throws IOException
	{
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(new FileOutputStream("gameState.db"));
			out.writeObject(tb);
		}
		finally
		{
			out.close();
		}
	}
	
	public TileBoard deserialize() throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream("gameState.db"));
			TileBoard GS = (TileBoard) in.readObject();
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