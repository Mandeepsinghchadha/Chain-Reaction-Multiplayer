import java.util.ArrayList;

public class Player {

	int playerNumber;
	String colour;
	
	public Player(int playerNumber, String colour)
	{
		this.playerNumber = playerNumber;
		this.colour = colour;
	}

	public Board move(Board b, Coordinate c)
	{
		if((c.playerStatus==this.playerNumber) || (c.playerStatus==0))
		{
			b.setCoordinateOfBoard(c, b.getValueAtCoordinate(c)+1);
			if(b.getValueAtCoordinate(c)>=b.getCriticalMass(c))
			{
				//Explosion
				ArrayList<Coordinate> allNeighbours = b.getListOfNeighbours(c);
				for(int i=0;i<allNeighbours.size();i+=1)
				{
					allNeighbours.get(i).playerStatus = this.playerNumber;
					b.setCoordinateOfBoard(allNeighbours.get(i), b.getValueAtCoordinate(allNeighbours.get(i))+1);
					b.setCoordinateOfBoard(c, 0);
					c.playerStatus = 0;
					ArrayList<Coordinate> allUnstableCells = this.getAllUnstableCells(b);
					b = this.chainReaction(b, allUnstableCells); 
//					b = move(b,allNeighbours.get(i));
				}
				return b;
			}
			else
			{
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
				if(b.getValueAtCoordinate(new Coordinate(i,j))>=b.getCriticalMass(new Coordinate(i,j)))
				{
					allUnstableCells.add(new Coordinate(i,j));
				}
			}
		}
		return allUnstableCells;
	}
	
	public Board chainReaction(Board b, ArrayList<Coordinate> allUnstableCells)
	{
		for(int i=0;i<allUnstableCells.size();i+=1)
		{
			Coordinate eachUnstableCell = allUnstableCells.get(i);
			ArrayList<Coordinate> neighbours = b.getListOfNeighbours(eachUnstableCell);
			for(int j=0;j<neighbours.size();j+=1)
			{
				neighbours.get(j).playerStatus = eachUnstableCell.playerStatus;
				b.setCoordinateOfBoard(neighbours.get(j), b.getValueAtCoordinate(neighbours.get(j))+1);
				eachUnstableCell.playerStatus = 0;
				b.setCoordinateOfBoard(eachUnstableCell, 0);
				ArrayList<Coordinate> unstableCellsForNeighbour = this.getAllUnstableCells(b);
				b = chainReaction(b,unstableCellsForNeighbour);
			}
		}
		return b;
	}
}
