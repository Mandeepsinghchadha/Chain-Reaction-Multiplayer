package withoutGUI;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class Player implements Serializable {

	private static final long serialVersionUID = 3L;
	public int orbCount;
	public int playerNumber;
	public String colour;
	public boolean active;
	
	/**
	 * Initializes the Player with its initial properties, i.e.
	 * the player number and the initial assigned color.
	 * @param playerNumber The index of the player in question
	 * @param c The player's orb color
	 * @author Madhur Tandon
	 */
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
