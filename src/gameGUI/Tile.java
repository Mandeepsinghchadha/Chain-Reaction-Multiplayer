package gameGUI;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;

public class Tile extends StackPane{

	int count = 1;
	Sphere orb;
	Group group = new Group();
	
	public void drawSphere()
	{	
		orb = new Sphere();
		if(count%4==1)
		{
			group.getChildren().add(orb);
		}
		else if(count%4==2)
		{
			orb.setTranslateX(12);
			orb.setTranslateY(12);
			group.getChildren().add(orb);
		}
		else if(count%4==3)
		{
			orb.setTranslateX(-12);
			orb.setTranslateY(12);
			group.getChildren().add(orb);
		}
		else if(count%4==0)
		{
			group.getChildren().clear();
		}
		PhongMaterial material = new PhongMaterial();
	    material.setDiffuseColor(Color.BLUE);
	    material.setSpecularColor(Color.BLACK);
		orb.setRadius(12);
		orb.setMaterial(material);
		count+=1;
	}
	
	public Tile(int numberOfRows, int numberOfColumns)
	{
		Rectangle border = new Rectangle(100,100);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		
		setAlignment(Pos.CENTER);
		getChildren().addAll(border);
		
		setOnMouseClicked(event -> {
			drawSphere();
		});
		getChildren().add(group);
	}
}
