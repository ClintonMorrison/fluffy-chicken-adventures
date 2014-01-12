import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class describes an automatically centered label. labels are are important 
 * for drawing text to the screen in the game.
 * The labels are centered about a rectangle
 */

public class GameLabel extends GameObject implements DrawableEntity {

	//Attributes
	private Font font;
	private FontMetrics fontMetrics;
	private String text;
	private Color fontColor;
	
	//Constructor
	public GameLabel(String name, Graphics g, Rectangle boundingRect, String text, Font font, Color fontColor) {
		super(name, new Vector(0,0));
		this.text = text;
		this.font = font;
		this.fontColor = fontColor;
		fontMetrics = g.getFontMetrics(font);
		centerAboutRectangle(g, boundingRect);
	}
	
	//Construct from a labelType object
	public GameLabel(String name, Graphics g, Rectangle boundingRect, String text, GameFont labelType) {
		super(name, new Vector(0,0));
		this.text = text;
		this.font = labelType.getFont();
		this.fontColor = labelType.getFontColor();
		fontMetrics = g.getFontMetrics(font);
		centerAboutRectangle(g, boundingRect);
	}
	
	//Centers the label at the center of a rectangle
	public void centerAboutRectangle(Graphics g, Rectangle rect)
	{	
		java.awt.geom.Rectangle2D font_rect = fontMetrics.getStringBounds(text, g);

		int textHeight = (int)(font_rect.getHeight()); 
		int textWidth  = (int)(font_rect.getWidth());
		int panelHeight= rect.height;
		int panelWidth = rect.width;

		// Center text horizontally and vertically
		int x = (panelWidth  - textWidth)  / 2  + rect.x;
		int y = (panelHeight - textHeight) / 2  + fontMetrics.getAscent() + rect.y;
		
		//Set position of label
		super.setPosition(new Vector(x, y));
	}
	
	//Draws the label
	public void draw(Graphics g) 
	{
		g.setFont(font);
		g.setColor(fontColor);
		g.drawString(text, (int)getPosition().x, (int)getPosition().y);
		
	}

	//Updates the label
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		// Do nothing to update label	
	}
	
	//Get the text
	public String getText()
	{
		return text;
	}
	
	//Set the text
	public void setText(String text)
	{
		this.text = text;
	}
	
	//Gets the color
	public Color getColor()
	{
		return fontColor;
	}
	
	//Sets the color
	public void setColor(Color fontColor)
	{
		this.fontColor = fontColor;
	}
}
