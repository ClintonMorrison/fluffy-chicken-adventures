import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 */


/*
 * This class describes a button. These will be used to construct menus.
 * The button is highlighted when the users mouse is over the button
 * 
 */

public class GameButton extends GameLabel implements DrawableEntity {

	//Attributes
	private Rectangle bounds; //Outer bounds of button
	private int borderWidth; //How think the border of the button is
	private Color borderColor;
	private Color highlightedBorderColor;
	private Color interiorColor;
	private boolean highlighted; //Is the mouse over the button
	private boolean buttonClicked;
	private SoundEffect soundSelected;
	private SoundEffect soundClicked;

	//Constructor
	public GameButton(String name, Graphics g, Rectangle boundingRect,
			String text, Font font, Color textColor, int borderWidth, Color borderColor, 
			Color highlightedBorderColor, Color interiorColor, SoundEffect soundSelected, SoundEffect soundClicked) 
	{
		super(name, g, boundingRect, text, font, textColor);
		this.bounds = boundingRect;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		this.highlightedBorderColor = highlightedBorderColor;
		this.interiorColor = interiorColor;
		buttonClicked = false;
		this.soundSelected = soundSelected;
		this.soundClicked = soundClicked;
	}
	
	//Create button from ButtonType
	public GameButton(String name, Graphics g, Vector center,
			String text, ButtonType buttonType) 
	{
		super(name, g, new Rectangle((int)center.x - buttonType.getSize().width/2, (int)center.y - buttonType.getSize().height/2, buttonType.getSize().width, buttonType.getSize().height), text, buttonType.getFont(), buttonType.getFontColor());
		this.bounds = new Rectangle((int)center.x - buttonType.getSize().width/2, (int)center.y - buttonType.getSize().height/2, buttonType.getSize().width, buttonType.getSize().height);
		this.borderWidth = buttonType.getBorderWidth();
		this.borderColor = buttonType.getBorderColor();
		this.highlightedBorderColor = buttonType.getHighlightedBorderColor();
		this.interiorColor = buttonType.getInteriorColor();
		this.soundSelected = buttonType.getSoundSelected();
		this.soundClicked = buttonType.getSoundClicked();
	}  
	
	
	//Draws button
	public void draw(Graphics g) {
		
		//Draw border
		if(highlighted)
			g.setColor(highlightedBorderColor);
		else
			g.setColor(borderColor);
		
		g.fill3DRect(bounds.x, bounds.y, bounds.width, bounds.height, true);
		
		//Draw inside of button
		g.setColor(interiorColor);
		g.fillRect(bounds.x + borderWidth, bounds.y + borderWidth, bounds.width - 2*borderWidth, bounds.height - 2*borderWidth);
		
		//Draw text
		super.draw(g);
	}
	
	//Gets the list of sound effects
	public SoundEffect getClickedSoundEffect()
	{
		return this.soundClicked;
	}
	
	
	//Updates button
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		soundClicked.update();
		soundSelected.update();
		
		//Check if mouse if over button
		if(!highlighted && bounds.contains(new Point((int) currentUserInput.getMousePosition().x, (int)currentUserInput.getMousePosition().y)))
		{
			soundSelected.play();
			highlighted = true;
		}
		else if(!bounds.contains(new Point((int) currentUserInput.getMousePosition().x, (int)currentUserInput.getMousePosition().y)))
		{
			highlighted = false;
		}
		
		//Check if button was clicked
		if(highlighted == true && currentUserInput.mouseClickedInArea(bounds))
		{
			soundClicked.play();
			buttonClicked = true;
		}

	}
	
	//Gets if button was clicked (and resets button clicked variable to listen for another click)
	public boolean wasClicked()
	{
		if(buttonClicked)
		{
			buttonClicked = false;
			return true;
		}
		else
			return false;
	}
	
	//Set the sounds to muted or on
	public void setMuted(boolean muted)
	{
		soundSelected.setMuted(muted);
		soundClicked.setMuted(muted);
	}

}
