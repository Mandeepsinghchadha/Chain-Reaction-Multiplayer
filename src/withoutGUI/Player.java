package withoutGUI;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	public int orbCount;
	public int playerNumber;
	public String colour;
	public boolean active;
	
	public Player(int playerNumber, Color c)
	{
		this.orbCount = Integer.MIN_VALUE;
		this.playerNumber = playerNumber;
		this.colour = c.toString();
		this.active = true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
