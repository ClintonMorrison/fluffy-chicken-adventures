import java.awt.Graphics;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 * 
 * This class describes an object with a name and position.
 * This will be the building block of all objects that exist in the game
 */

public abstract class GameObject implements DrawableEntity {
	
	//Attributes
	private String name;
	private Vector position;
	
	//Constructor
	public GameObject(String name, Vector position)
	{
		this.name = name;
		this.position = position;
	}
	
	//Draw method (not implemented)
	public abstract void draw(Graphics g);
	
	//Update method (not implemented)
	public abstract void update(Long millisPassed, UserInput currentUserInput);
	
	//Get methods
	public String getName()
	{
		return name;
	}
	
	public Vector getPosition()
	{
		return position;
	}
	
	//Set methods
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPosition(Vector position)
	{
		this.position = position;
	}
	

}


