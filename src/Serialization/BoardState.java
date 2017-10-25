package Serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BoardState implements Serializable {
	
	public static void serialize(BoardGUI b) throws IOException
	{
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(new FileOutputStream("boardGame.b"));
			out.writeObject(b);
		}
		finally
		{
			out.close();
		}
	}

	public static BoardGUI deserialize() throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream("boardGame.b"));
			BoardGUI loadedB = (BoardGUI) in.readObject();
			return loadedB;
		}
		finally
		{
			in.close();
		}
	}

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BoardGUI b = new BoardGUI(6,6,2);
		serialize(b);
	}

}
