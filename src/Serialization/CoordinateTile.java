package Serialization;

import java.io.Serializable;
import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class CoordinateTile extends StackPane implements Serializable {

	BoardGUI boardContainer;
	PlayerController playerContainer;
	Rectangle border;
	
	int xCoordinate;
	int yCoordinate;
	int playerStatus;
	int value;
	int criticalMass;
	
	transient Color colour;
	int numberOfRows;
	int numberOfColumns;
	
	transient Group allOrbs = new Group();
	transient Point3D allAxes[] = {Rotate.X_AXIS,Rotate.Y_AXIS,Rotate.Z_AXIS};
	
	transient RotateTransition rotateGroup;

	transient TranslateTransition transRight;
	transient TranslateTransition transLeft;
	transient TranslateTransition transAbove;
	transient TranslateTransition transBelow;
	
	transient ParallelTransition parallelSplit;

	Sphere rightOrb;
	Sphere leftOrb;
	Sphere aboveOrb;
	Sphere belowOrb;
	
	CoordinateTile(int x, int y, int m, int n, BoardGUI b)
	{
		this.xCoordinate = x;
		this.yCoordinate = y;
		this.numberOfRows = m;
		this.numberOfColumns = n;
		this.playerStatus = 0;
		this.value = 0;
		
		this.criticalMass = this.getCriticalMass(this.xCoordinate, this.yCoordinate);
		this.colour = Color.WHITESMOKE;
		this.boardContainer = b;
		
		rotateGroup = new RotateTransition(Duration.millis(1500+Math.random()*500), allOrbs);
		rotateGroup.setFromAngle(0);
		rotateGroup.setToAngle(360);
		rotateGroup.setInterpolator(Interpolator.LINEAR);
		rotateGroup.setCycleCount(RotateTransition.INDEFINITE);
		rotateGroup.setAxis(allAxes[new Random().nextInt(allAxes.length)]);
		rotateGroup.setAutoReverse(false);

		transRight = new TranslateTransition(Duration.millis(500));
		transRight.setToX(40);
		transRight.setCycleCount(1);
		transRight.setAutoReverse(false);
		transRight.setOnFinished(e->{
			this.getChildren().remove(this.rightOrb);
		});
		
		transLeft = new TranslateTransition(Duration.millis(500));
		transLeft.setToX(-1*40);
		transLeft.setCycleCount(1);
		transLeft.setAutoReverse(false);
		transLeft.setOnFinished(e->{
			this.getChildren().remove(this.leftOrb);
		});
		
		transBelow = new TranslateTransition(Duration.millis(500));
		transBelow.setToY(40);
		transBelow.setCycleCount(1);
		transBelow.setAutoReverse(false);
		transBelow.setOnFinished(e->{
			this.getChildren().remove(this.belowOrb);
		});
		
		transAbove = new TranslateTransition(Duration.millis(500));
		transAbove.setToY(-1*40);
		transAbove.setCycleCount(1);
		transAbove.setAutoReverse(false);
		transAbove.setOnFinished(e->{
			this.getChildren().remove(this.aboveOrb);
		});

		parallelSplit = new ParallelTransition();

		
	}
	
	public int getCriticalMass(int i, int j)
	{
		if ((i==0 && j==0) || (i==0 && j==this.numberOfColumns-1) || (i==this.numberOfRows-1 && j==0) || (i==this.numberOfRows-1 && j==this.numberOfColumns-1))
		{
			return 2;
		}
		else if((i==0) || (i==this.numberOfRows-1) || (j==0) || (j==this.numberOfColumns-1))
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
