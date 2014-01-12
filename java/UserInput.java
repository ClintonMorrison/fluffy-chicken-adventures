import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 17, 2013
 */


/*
 * This class is used as a container for all of the mouse and keyboard input that the user enters.
 * It is passed into various classes to be processed. It provides several functions for other classes
 * to determine what the user has input into the game.
 * 
 * Note that this class is to be refreshed between updates of the game so input isn't double counted.
 */


//TODO: Fix null mouse position bug


public class UserInput 
{
	//Attributes
	private Point mousePosition; //The position of the mouse
	private LinkedList<Point> mouseClicks; //A list of locations where the user has clicked
	private LinkedList<Integer> keysDown; //A list of keys the user currently has down (stored as key codes)
	private LinkedList<Integer> keysUp; //A list of keys the user has just released (stored as key codes)
	
	//Constructor
	public UserInput()
	{
		this.mousePosition = new Point();
		this.mouseClicks = new LinkedList<Point>();
		this.keysDown = new LinkedList<Integer>();
		this.keysUp = new LinkedList<Integer>();
	}
	
	//Gets mouse position
	public Vector getMousePosition()
	{
		if(mousePosition != null)
			return new Vector (mousePosition.x, mousePosition.y);
		else return new Vector(0,0);
	}
	
	//Checks if any mouse clicks are in a given area
	public boolean mouseClickedInArea(Rectangle rect)
	{		
		for (int i = 0; i < mouseClicks.size(); i++)
		{
			if(rect.contains(mouseClicks.get(i)))
			{
				return true; //The rectangle contains one of the mouse clicks
			}
		}
		
		return false; //No mouse click points were in area 
		
	}
	
	//Checks if a particular key is down
	public boolean keyIsDown(Integer key)
	{
		return keysDown.contains(key);
	}
	
	//Checks if a particular key is typed (down and then released)
	public boolean keyWasTyped(Integer key)
	{
		return keysUp.contains(key);
	}
	
	
	//Removes a key from the list of keys down (triggered when key up event happens)
	public void keyUp(Integer key)
	{
		while(keysDown.contains(key))
			keysDown.remove(key);
		
		if(!keysUp.contains(key))
			keysUp.add(key);
	}

	//Adds a key to the list of keys down (triggered when key down event happens)
	public void keyDown(Integer key)
	{
		keysDown.add(key);
	}

	//Adds mouse click to records at point p
	public void mouseClick(Point p)
	{
		mouseClicks.add(p);
	}
	
	//Adds mouse click to records at point p
	public void setMousePosition(Point p)
	{
		mousePosition = p;
	}
	
	//Resets all of the values held in class
	public void reset()
	{
		this.mousePosition = new Point();
		this.mouseClicks = new LinkedList<Point>();
		this.keysUp = new LinkedList<Integer>();
	}
	
}
