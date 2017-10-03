import java.util.ArrayList;

public class Player {

	int playerNumber;
	String colour;
	
	public Player(int playerNumber, String colour)
	{
		this.playerNumber = playerNumber;
		this.colour = colour;
	}

	public Board move(Board b, int x, int y)
	{
		if((b.board[x][y].playerStatus==this.playerNumber) || (b.board[x][y].playerStatus==0))
		{
			b.setCoordinateOfBoard(x,y, b.getValueAtCoordinate(x,y)+1);
			if(b.getValueAtCoordinate(x,y)>=b.getCriticalMass(x,y))
			{
				//Explosion
				ArrayList<Coordinate> allNeighbours = b.getListOfNeighbours(x,y);
				for(int i=0;i<allNeighbours.size();i+=1)
				{
					allNeighbours.get(i).playerStatus = this.playerNumber;
					allNeighbours.get(i).color = this.colour;
					b.setCoordinateOfBoard(allNeighbours.get(i).xCoordinate,allNeighbours.get(i).yCoordinate, b.getValueAtCoordinate(allNeighbours.get(i).xCoordinate,allNeighbours.get(i).yCoordinate)+1);
					b.setCoordinateOfBoard(b.board[x][y].xCoordinate,b.board[x][y].yCoordinate, 0);
					b.board[x][y].playerStatus = 0;
				}
				ArrayList<Coordinate> allUnstableCells = this.getAllUnstableCells(b);
				b.board[x][y].color = "default";
				b = this.chainReaction(b, allUnstableCells); 
				return b;
			}
			else
			{
				b.board[x][y].playerStatus = this.playerNumber;
				b.board[x][y].color = this.colour;
				return b;
			}
		}
		else
		{
			System.out.println("Illegal Move!");
			return b;
		}
	}
	
	public ArrayList<Coordinate> getAllUnstableCells(Board b)
	{
		ArrayList<Coordinate> allUnstableCells = new ArrayList<Coordinate>();
		for(int i=0;i<b.m;i+=1)
		{
			for(int j=0;j<b.n;j+=1)
			{
				if(b.getValueAtCoordinate(i,j)>=b.getCriticalMass(i,j))
				{
					allUnstableCells.add(b.board[i][j]);
				}
			}
		}
		return allUnstableCells;
	}
	
	public ArrayList<Coordinate> getAllUnstableNeighbourCells(ArrayList<Coordinate> allNeighbours, Board b)
	{
		ArrayList<Coordinate> UnstableNeighbours = new ArrayList<Coordinate>();
		for(int i=0;i<allNeighbours.size();i+=1)
		{
			if(allNeighbours.get(i).value>=b.getCriticalMass(allNeighbours.get(i).xCoordinate, allNeighbours.get(i).yCoordinate))
			{
				UnstableNeighbours.add(allNeighbours.get(i));
			}
		}
		return UnstableNeighbours;
	}
	
	public Board chainReaction(Board b, ArrayList<Coordinate> allUnstableCells)
	{
		for(int i=0;i<allUnstableCells.size();i+=1)
		{
			Coordinate eachUnstableCell = allUnstableCells.get(i);
			ArrayList<Coordinate> neighbours = b.getListOfNeighbours(eachUnstableCell.xCoordinate,eachUnstableCell.yCoordinate);
			for(int j=0;j<neighbours.size();j+=1)
			{
				neighbours.get(j).playerStatus = eachUnstableCell.playerStatus;
				neighbours.get(j).color = eachUnstableCell.color;
				b.setCoordinateOfBoard(neighbours.get(j).xCoordinate,neighbours.get(j).yCoordinate, b.getValueAtCoordinate(neighbours.get(j).xCoordinate,neighbours.get(j).yCoordinate)+1);
				b.setCoordinateOfBoard(eachUnstableCell.xCoordinate,eachUnstableCell.yCoordinate, 0);		
			}
			ArrayList<Coordinate> NeighbourCellsOfUnstableCell = b.getListOfNeighbours(eachUnstableCell.xCoordinate,eachUnstableCell.yCoordinate);
			ArrayList<Coordinate> NeighbourCellsWhichAreThemselvesUnstable = getAllUnstableNeighbourCells(NeighbourCellsOfUnstableCell,b);
			eachUnstableCell.color = "default";
			b = chainReaction(b,NeighbourCellsWhichAreThemselvesUnstable);
		}
		return b;
	}
}
