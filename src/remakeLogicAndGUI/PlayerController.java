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
	
	public BoardGUI move(BoardGUI b, int x, int y)
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
					allNeighbours.get(i).drawSphere();
					for(Sphere n : allNeighbours.get(i).allOrbs.getChildren().toArray(new Sphere[allNeighbours.get(i).allOrbs.getChildren().size()]))
					{
						PhongMaterial material = new PhongMaterial();
					    material.setDiffuseColor(this.colour);
					    material.setSpecularColor(Color.BLACK);
					    n.setMaterial(material);
					}
					b.board[x][y].value = 0;
					b.board[x][y].allOrbs.getChildren().clear();
					b.board[x][y].playerStatus = 0;
				}
				ArrayList<CoordinateTile> allUnstableCells = this.getAllUnstableCells(b);
				b.board[x][y].colour = Color.WHITESMOKE;
				b = this.chainReaction(b, allUnstableCells); 
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

	public BoardGUI chainReaction(BoardGUI b, ArrayList<CoordinateTile> allUnstableCells) {
		// TODO Auto-generated method stub
		for(int i=0;i<allUnstableCells.size();i+=1)
		{
			CoordinateTile eachUnstableCell = allUnstableCells.get(i);
			ArrayList<CoordinateTile> neighbours = b.getListOfNeighbours(eachUnstableCell.xCoordinate,eachUnstableCell.yCoordinate);
			for(int j=0;j<neighbours.size();j+=1)
			{
				neighbours.get(j).playerStatus = eachUnstableCell.playerStatus;
				neighbours.get(j).colour = eachUnstableCell.colour;
				for(Sphere n : neighbours.get(j).allOrbs.getChildren().toArray(new Sphere[neighbours.get(j).allOrbs.getChildren().size()]))
				{
					PhongMaterial material = new PhongMaterial();
				    material.setDiffuseColor(eachUnstableCell.colour);
				    material.setSpecularColor(Color.BLACK);
				    n.setMaterial(material);
				}
				b.board[neighbours.get(j).xCoordinate][neighbours.get(j).yCoordinate].drawSphere();
				b.board[eachUnstableCell.xCoordinate][eachUnstableCell.yCoordinate].value=0;
				b.board[eachUnstableCell.xCoordinate][eachUnstableCell.yCoordinate].allOrbs.getChildren().clear();
			}
			ArrayList<CoordinateTile> NeighbourCellsOfUnstableCell = b.getListOfNeighbours(eachUnstableCell.xCoordinate,eachUnstableCell.yCoordinate);
			ArrayList<CoordinateTile> NeighbourCellsWhichAreThemselvesUnstable = getAllUnstableNeighbourCells(NeighbourCellsOfUnstableCell,b);
			eachUnstableCell.colour = Color.WHITESMOKE;
			b = chainReaction(b,NeighbourCellsWhichAreThemselvesUnstable);
		}
		return b;
	}

	public ArrayList<CoordinateTile> getAllUnstableCells(BoardGUI b) {
		// TODO Auto-generated method stub
		ArrayList<CoordinateTile> allUnstableCells = new ArrayList<CoordinateTile>();
		for(int i=0;i<b.numberOfRows;i+=1)
		{
			for(int j=0;j<b.numberOfColumns;j+=1)
			{
				if(b.board[i][j].value>=b.board[i][j].criticalMass)
				{
					allUnstableCells.add(b.board[i][j]);
				}
			}
		}
		return allUnstableCells;
	}
}
