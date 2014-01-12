import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 *
 * This class describes an animated stationary object.
 * This is an object that is animated and can be drawn. Stationary objects,
 * as well as moving animated objects extend this class
 */

public class AnimatedObject extends GameObject implements DrawableEntity{

	//Attributes
	private ArrayList<Animation> animations;  //All of the animations the object is capable of
	private int currentAnimationIndex;  //Index of current animation
	private Vector offset; //Offset to use when drawing object
	
	//Constructor
	public AnimatedObject(String name, Vector position, ArrayList<Animation> animations, int currentAnimationIndex) 
	{
		super(name, position);
		this.animations = animations;
		this.animations.trimToSize();
		this.currentAnimationIndex = currentAnimationIndex;
		offset = new Vector(0,0);
		this.animations.trimToSize();
		if(currentAnimationIndex > animations.size())
		{
			System.out.println("Warning! Animation index intialized was out of bounds! Index set to 0");
			currentAnimationIndex = 0;
			
		}
		
	}

	//Sets offset of object
	public void setOffset(Vector offset)
	{
		this.offset = offset;
	}
	
	//Gets offset of object
	public Vector getOffset()
	{
		return offset;
	}
	
	//Draws the AnimatedObject
	public void draw(Graphics g) 
	{
		g.drawImage(getCurrentFrame(), (int)getPosition().x + (int)offset.x, (int)getPosition().y, null);
	}

	//Updates the object
	public void update(Long millisTaken, UserInput currentUserInput) 
	{
		animations.get(currentAnimationIndex).update(millisTaken);
	}
	
	//Gets the current animation index
	public int getCurrentAnimationIndex()
	{
		return currentAnimationIndex;
	}
	
	//Sets the current animation index
	public void setCurrentAnimationIndex(int index)
	{
		animations.get(currentAnimationIndex).reset();
		currentAnimationIndex = index;
	}
	
	//Returns current frame of animation
	public Image getCurrentFrame()
	{
		try
		{
			return animations.get(currentAnimationIndex).getCurrentFrame();
		}
		catch (IndexOutOfBoundsException e)
		{
			System.out.println("Error getting frame for character \'" + getName() + "\'");
			return null;
		}
	}
	
	//Returns if animation is finished
	public boolean animationFinished()
	{
		return animations.get(currentAnimationIndex).getFinished();
	}
	
	//Get the dimensions of the object
	public Dimension getDimensions()
	{
		ImageIcon frame = new ImageIcon(getCurrentFrame()); //Create image icon to find width and height of current frame	
		return new Dimension(frame.getIconWidth(), frame.getIconHeight());  //Return dimensions
	}
	
	//Gets the center point of the object
	public Vector getCenter()
	{
		return new Vector(this.getPosition().x + this.getDimensions().width/2, this.getPosition().y + this.getDimensions().height/2);
	}
	
	//Sets where the center of the object is
	public void setCenterPosition(Vector new_cent)
	{
		setPosition(new Vector(new_cent.x - getDimensions().width/2, new_cent.y - getDimensions().height/2));
	}
	
	//Gets the set of animations the object can do
	public ArrayList<Animation> getAnimations()
	{
		return animations;
	}
	
	//Gets a rectangle representing the bounds of this object (used for collision detection)
	public Rectangle getBoundingRectangle()
	{
		return new Rectangle((int)getPosition().x, (int)getPosition().y, getDimensions().width, getDimensions().height);
	}
	
}
