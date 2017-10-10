package remakeLogicAndGUI;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class PlayerController {
	
	int orbCount;
	int playerNumber;
	Color colour;
	boolean active;
	
	PlayerController(int playerNumber, Color c)
	{
		this.orbCount = Integer.MIN_VALUE;
		this.playerNumber = playerNumber;
		this.colour = c;
		this.active = true;
	}
	
	public BoardGUI move(BoardGUI b, int x, int y) throws IllegalMoveException
	{
		if((b.board[x][y].playerStatus==this.playerNumber)||(b.board[x][y].playerStatus==0))
		{
			b.board[x][y].playerStatus = this.playerNumber;
			b.board[x][y].colour = this.colour;
			b.board[x][y].drawSphere();
			if(b.board[x][y].value>=b.board[x][y].criticalMass)
			{
				ArrayList<CoordinateTile> allNeighbours = b.getListOfNeighbours(x, y);
				for(int i=0;i<allNeighbours.size();i+=1)
				{
					allNeighbours.get(i).playerStatus = this.playerNumber;
					allNeighbours.get(i).colour = this.colour;
					for(Sphere n : allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]))
					{
						PhongMaterial material = new PhongMaterial();
					    material.setDiffuseColor(this.colour);
					    material.setSpecularColor(Color.BLACK);
					    n.setMaterial(material);
					}
					allNeighbours.get(i).drawSphere();
					b.board[x][y].value = 0;
					b.board[x][y].allOrbs.getChildren().clear();
					b.board[x][y].playerStatus = 0;
				}
				ArrayList<CoordinateTile> NeighbourCellsOfJustMovedCell = b.getListOfNeighbours(x,y);
				ArrayList<CoordinateTile> NeighbourCellsWhichAreThemselvesUnstable = getAllUnstableNeighbourCells(NeighbourCellsOfJustMovedCell,b);

				for(int p=0;p<NeighbourCellsWhichAreThemselvesUnstable.size();p+=1)
					{
						b = move(b,NeighbourCellsWhichAreThemselvesUnstable.get(p).xCoordinate,NeighbourCellsWhichAreThemselvesUnstable.get(p).yCoordinate);
					}
				b.board[x][y].colour = Color.WHITESMOKE; 
				return b;
			}
			else
			{
				return b;
			}
		}
		else
		{
			throw new IllegalMoveException("Illegal Move!");
		}
	}
	
	public ArrayList<CoordinateTile> getAllUnstableNeighbourCells(ArrayList<CoordinateTile> allNeighbours, BoardGUI b)
	{
		ArrayList<CoordinateTile> UnstableNeighbours = new ArrayList<CoordinateTile>();
		for(int i=0;i<allNeighbours.size();i+=1)
		{
			if(allNeighbours.get(i).value>=b.board[allNeighbours.get(i).xCoordinate][allNeighbours.get(i).yCoordinate].criticalMass)
			{
				UnstableNeighbours.add(allNeighbours.get(i));
			}
		}
		return UnstableNeighbours;
	}
}
