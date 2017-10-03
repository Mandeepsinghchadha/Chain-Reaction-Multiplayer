public class Coordinate {
	
	/* @param: playerNumber
	 * 0 denotes empty slot
	 * 1 to 8 denotes player number 1 to 8
	 */
	int xCoordinate;
	int yCoordinate;
	int playerStatus;
	int value;
	String color;
	
	public Coordinate(int x, int y)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.playerStatus = 0;
		this.value = 0;
		this.color = "default";
	}
}
