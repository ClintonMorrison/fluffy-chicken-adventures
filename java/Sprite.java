import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 */


/*
 * This class describes a moving animated object.
 * Classes such Characters' body parts are made from this.
 */

public class Sprite extends AnimatedObject 
{
	//Attributes
	private Vector velocity; 
	
	//Constructor
	public Sprite(String name, Vector position, 
			ArrayList<Animation> animations, int currentAnimationIndex, Vector velocity) 
	{
		super(name, position, animations, currentAnimationIndex);
		this.velocity = velocity;
	}
	
	//Updates the object (overides the update function of AnimatedObject)
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		
		getPosition().x += velocity.x*millisTaken;
		getPosition().y += velocity.y*millisTaken;
	}
	
	//Set velocity
	public void setVelocity(Vector velocity)
	{
		this.velocity = velocity;
	}
	
	//Get velocity
	public Vector getVelocity()
	{
		return velocity;
	}
	
	//Increase the velocity by v
	public void increaseVelocity(Vector v)
	{
		this.velocity.x += v.x;
		this.velocity.y += v.y;
	}
	
	//Move the sprite's position of a translation vector 
	public void slide(Vector slide)
	{
		getPosition().x += slide.x;
		getPosition().y += slide.y;
	}

}
