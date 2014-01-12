import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 */


/*
 * This class describes a moving animated object which can be rotated at an angle.
 * Classes such Characters' body parts are made from this.
 */
public class RotatedSprite extends Sprite implements DrawableEntity
{
	//Attributes
	private double angle; //The angle the sprite is rotated to
	private double angularVelocity; //How many degrees / msec the sprite rotates
	Vector pivotPoint; //Point object rotates about, measured relative to the centriod of the object [ (0,0) would imply rotation about center of object ] 
	
	//Constructor
	public RotatedSprite(String name, Vector position,
			ArrayList<Animation> animations, int currentAnimationIndex,
			Vector velocity, double startingAngle, Vector pivotPoint, double angularVelocity) 
	{
		super(name, position, animations, currentAnimationIndex, velocity);
		this.angle = startingAngle;
		this.pivotPoint = pivotPoint;
		this.angularVelocity = angularVelocity; 
	}
	
	//Draws the rotated sprite
	public void draw(Graphics g) 
	{
		//Create 2D graphics from graphics g
		Graphics2D g2d = (Graphics2D) g.create();
		
		//Rotate the screen by angle, about pivot point (relative to center of object)
		g2d.rotate(Math.toRadians(angle), getCenter().x + pivotPoint.x ,  getCenter().y + pivotPoint.y);
		super.draw(g2d);
		
		//Free 2D graphics from memory
		g2d.dispose();
	}
	
	//Updates the rotated sprite
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		angle += angularVelocity * millisTaken;
	}
	
	//Sets point which arm pivots about
	public void setPivotPoint(Vector p)
	{
		pivotPoint = p;
	}
	
	//Gets the angle that the sprite is rotated to
	public double getAngle()
	{
		return angle;
	}

	//Gets the angle that the sprite is rotated to
	public void setAngle(double angle)
	{
		this.angle = angle;
	}

}
