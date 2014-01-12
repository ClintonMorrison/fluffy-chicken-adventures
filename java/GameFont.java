import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class is a template for a GameLabel.
 * It does not specify a text or position on the screen.
 * It can be used to create a game label
 */

public class GameFont {

	//Attributes
	private Font font;
	private Color fontColor;
	
	//Constructor
	public GameFont( Font font, Color fontColor) 
	{
		this.font = font;
		this.fontColor = fontColor;

	}
	
	//Gets font
	public Font getFont()
	{
		return font;
	}
	
	//Gets color
	public Color getFontColor()
	{
		return fontColor;
	}

	public static GameFont createFromGamefileContents(GameFileContents contents) {
		//Create font
		String fontName;
		int fontStyle, fontSize;
		Color fontColor;
		
		fontName = contents.getValue("FontName");
		fontStyle = Integer.parseInt(contents.getValue("FontStyle"));
		fontSize = Integer.parseInt(contents.getValue("FontSize"));
		
		
		try 
		{
		    Field field = Class.forName("java.awt.Color").getField(contents.getValue("FontColor"));
		    fontColor = (Color)field.get(null);
		} 
		catch (Exception e) 
		{
			System.out.println("Error. Color not recognized. Couldn't create font.");
		    fontColor = null; // Not defined
		}
		
		
		Font f = new Font(fontName, fontStyle, fontSize);
		
		return new GameFont(f, fontColor);
		
	}

	
}
