import java.util.ArrayList;
import java.util.Scanner;

public class Board {

	int m;
	int n;
	Coordinate[][] board;
	
	public Board(int numberOfRows, int numberOfColumns)
	{
		this.m = numberOfRows;
		this.n = numberOfColumns;
		this.board = new Coordinate[this.m][this.n];
		
		for(int i =0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				this.board[i][j] = new Coordinate(i,j);
			}
		}
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
				System.out.print(""+board[i][j].value+board[i][j].color.charAt(0)+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void setCoordinateOfBoard(int i,int j,int value)
	{
		this.board[i][j].xCoordinate = i;
		this.board[i][j].yCoordinate = j;
		this.board[i][j].value = value;
	}
	
	public int getValueAtCoordinate(int i, int j)
	{
		return this.board[i][j].value;
	}
	
	public int getCriticalMass(int i, int j)
	{
		if ((i==0 && j==0) || (i==0 && j==this.n-1) || (i==this.m-1 && j==0) || (i==this.m-1 && j==this.n-1))
		{
			return 2;
		}
		else if((i==0) || (i==this.m-1) || (j==0) || (j==this.n-1))
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}
	
	public int playerCount(int playerStatus)
	{
		int count = 0;
		for(int i=0;i<this.m;i+=1)
		{
			for(int j=0;j<this.n;j+=1)
			{
				if(this.board[i][j].playerStatus==playerStatus)
				{
					count+=1;
				}
			}
		}
		return count;
	}
	
	public ArrayList<Coordinate> getListOfNeighbours(int i, int j)
	{
		ArrayList<Coordinate> allNeighbours = new ArrayList<Coordinate>();
		if(this.checkValidCoordinate(i, j+1))
		{
			allNeighbours.add(this.board[i][j+1]);
		}
		if(this.checkValidCoordinate(i, j-1))
		{
			allNeighbours.add(this.board[i][j-1]);
		}
		if(this.checkValidCoordinate(i+1, j))
		{
			allNeighbours.add(this.board[i+1][j]);
		}
		if(this.checkValidCoordinate(i-1, j))
		{
			allNeighbours.add(this.board[i-1][j]);
		}
		return allNeighbours;
	}
	
	public static void main(String[] args)
	{
		Board b = new Board(8,6);
		Player p1 = new Player(1,"RED");
		Player p2 = new Player(2,"GREEN");
		Scanner s = new Scanner(System.in);
		System.out.println("Player 1: Enter x and y");
		int x = s.nextInt();
		int y = s.nextInt();
		p1.move(b, x, y);
		System.out.println("Player 2: Enter x and y");
		x = s.nextInt();
		y = s.nextInt();
		p2.move(b, x, y);
		while(b.playerCount(p1.playerNumber)>0 && b.playerCount(p2.playerNumber)>0)
		{
			System.out.println("Player 1: Enter x and y");
			x = s.nextInt();
			y = s.nextInt();
			p1.move(b, x, y);
			System.out.println("Player 2: Enter x and y");
			x = s.nextInt();
			y = s.nextInt();
			p2.move(b, x, y);
		}
		
		if(b.playerCount(p1.playerNumber)==0)
		{
			System.out.println("Player 2 wins");
		}
		else if(b.playerCount(p2.playerNumber)==0)
		{
			System.out.println("Player 1 wins");
		}
		b.displayBoard();
	}
}
