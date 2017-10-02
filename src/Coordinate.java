public class Coordinate {
	
	/* @param: playerNumber
	 * 0 denotes empty slot
	 * 1 to 8 denotes player number 1 to 8
	 */
	int xCoordinate;
	int yCoordinate;
	int playerStatus;
	
	public Coordinate(int x, int y)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.playerStatus = 0;
	}
	
	public Coordinate(int x, int y, int playerNumber)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.playerStatus = playerNumber;
	}
}
