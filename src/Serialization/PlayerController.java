package Serialization;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class PlayerController implements Serializable {
	
	int orbCount;
	int playerNumber;
	transient Color colour;
	boolean active;
	
	PlayerController(int playerNumber, Color c)
	{
		this.orbCount = Integer.MIN_VALUE;
		this.playerNumber = playerNumber;
		this.colour = c;
		this.active = true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
