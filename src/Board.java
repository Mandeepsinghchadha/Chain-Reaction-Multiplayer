import java.util.ArrayList;

public class Board {

	int m;
	int n;
	int[][] board;
	
	public Board(int numberOfRows, int numberOfColumns)
	{
		this.m = numberOfRows;
		this.n = numberOfColumns;
		board = new int[this.m][this.n];
	}
	
	public boolean checkValidCoordinate(int i, int j)
	{
		if(i>=0 && i<this.m && j>=0 && j<this.n)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void displayBoard()
	{
		for(int i=0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void setCoordinateOfBoard(Coordinate c, int value)
	{
		this.board[c.xCoordinate][c.yCoordinate] = value;
	}
	
	public int getValueAtCoordinate(Coordinate c)
	{
		return this.board[c.xCoordinate][c.yCoordinate];
	}
	
	public int getCriticalMass(Coordinate c)
	{
		if ((c.xCoordinate==0 && c.yCoordinate==0) || (c.xCoordinate==0 && c.yCoordinate==this.n-1) || (c.xCoordinate==this.m-1 && c.yCoordinate==0) || (c.xCoordinate==this.m-1 && c.yCoordinate==this.n-1))
		{
			return 2;
		}
		else if((c.xCoordinate==0) || (c.xCoordinate==this.m-1) || (c.yCoordinate==0) || (c.yCoordinate==this.n-1))
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
	
	public ArrayList<Coordinate> getListOfNeighbours(Coordinate c)
	{
		ArrayList<Coordinate> allNeighbours = new ArrayList<Coordinate>();
		if(this.checkValidCoordinate(c.xCoordinate, c.yCoordinate+1))
		{
			allNeighbours.add(new Coordinate(c.xCoordinate, c.yCoordinate+1));
		}
		if(this.checkValidCoordinate(c.xCoordinate, c.yCoordinate-1))
		{
			allNeighbours.add(new Coordinate(c.xCoordinate, c.yCoordinate-1));
		}
		if(this.checkValidCoordinate(c.xCoordinate+1, c.yCoordinate))
		{
			allNeighbours.add(new Coordinate(c.xCoordinate+1, c.yCoordinate));
		}
		if(this.checkValidCoordinate(c.xCoordinate-1, c.yCoordinate))
		{
			allNeighbours.add(new Coordinate(c.xCoordinate-1, c.yCoordinate));
		}
		return allNeighbours;
	}
	
	public static void main(String[] args)
	{
		Board b = new Board(8,6);
		Player p1 = new Player(1,"RED");
		Player p2 = new Player(2,"BLUE");
		
		p1.move(b, new Coordinate(0,0));
		b.displayBoard();
		p1.move(b, new Coordinate(0,0));
		b.displayBoard();
		p1.move(b, new Coordinate(0,1));
		b.displayBoard();
		p1.move(b, new Coordinate(0,1));
		b.displayBoard();
		p1.move(b, new Coordinate(1,1));
		b.displayBoard();
		p1.move(b, new Coordinate(1,1));
		b.displayBoard();
		p1.move(b, new Coordinate(1,1));
		b.displayBoard();
	}
}
