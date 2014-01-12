import java.awt.Graphics;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 */

/*
 * This interface describes the functions all drawable objects in the game must have
 */


public interface DrawableEntity {
	
	//Draws the object
	public abstract void draw(Graphics g);
		
	//Updates the object
	public abstract void update(Long millisPassed, UserInput currentUserInput);

}
