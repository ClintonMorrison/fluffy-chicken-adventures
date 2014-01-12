import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Scanner;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class describes a button. These will be used to construct menus.
 * The button is highlighted when the users mouse is over the button
 */

public class ButtonType extends GameFont {

	//Attributes
		private Dimension size; //Outer bounds of button
		private int borderWidth; //How think the border of the button is
		private Color borderColor;
		private Color highlightedBorderColor;
		private Color interiorColor;
		private SoundEffect soundSelected;
		private SoundEffect soundClicked;
		
	//Constructor
	public ButtonType(Font font, Color fontColor, Dimension size, int borderWidth, Color borderColor, Color highlightedBorderColor, Color interiorColor, SoundEffect soundSelected, SoundEffect soundClicked) 
	{
		super(font, fontColor);
		this.size = size;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		this.highlightedBorderColor = highlightedBorderColor;
		this.interiorColor = interiorColor;
		this.soundSelected = soundSelected;
		this.soundClicked = soundClicked;
	}
	
	
	//Gets dimensions of button
	public Dimension getSize()
	{
		return size;
	}
	
	//Gets border width of button
	public int getBorderWidth()
	{
		return borderWidth;
	}
	
	//Gets border color
	public Color getBorderColor()
	{
		return borderColor;
	}
	
	//Gets highlightedBorderColor
	public Color getHighlightedBorderColor()
	{
		return highlightedBorderColor;
	}
	
	//Gets interiorColor
	public Color getInteriorColor()
	{
		return interiorColor;
	}
	
	//Get selected sound
	public SoundEffect getSoundSelected()
	{
		return soundSelected;
	}
	
	//Get clicked sound
	public SoundEffect getSoundClicked()
	{
		return soundClicked;
	}
	
	//Creates button type from file
	public static ButtonType createFromGamefileContents(GameFileContents contents)
	{	
		String fontName;
		int fontStyle, fontSize;
		Color fontColor, borderColor, highlightedBorderColor, interiorColor;
		
		fontName = contents.getValue("FontName");
		fontStyle = Integer.parseInt(contents.getValue("FontStyle"));
		fontSize = Integer.parseInt(contents.getValue("FontSize"));
		
		SoundEffect soundSelected = null;
		SoundEffect soundClicked = null;
		//Get colors
		try 
		{
		    Field field = Class.forName("java.awt.Color").getField(contents.getValue("FontColor"));
		    fontColor = (Color)field.get(null);
		    
		    field = Class.forName("java.awt.Color").getField(contents.getValue("BorderColor"));
		    borderColor = (Color)field.get(null);
		    
		    field = Class.forName("java.awt.Color").getField(contents.getValue("HighlightedBorderColor"));
		    highlightedBorderColor = (Color)field.get(null);
		    
		    field = Class.forName("java.awt.Color").getField(contents.getValue("InteriorColor"));
		    interiorColor = (Color)field.get(null);
		} 
		catch (Exception e) 
		{
			System.out.println("Error. Color not recognized. Couldn't create font.");
		    fontColor = null; // Not defined
		    borderColor = null;
		    highlightedBorderColor = null;
		    interiorColor = null;
		}
		
		//Create font
		Font f = new Font(fontName, fontStyle, fontSize);
		
		//Create dimenions
		Dimension size = new Dimension(Integer.parseInt(contents.getValue("Width")), Integer.parseInt(contents.getValue("Height")));

		//Create border size
		int borderWidth = Integer.parseInt(contents.getValue("BorderWidth"));
		
		//Load sound effects
		if(contents.containsAttribute("SoundSelected"))
			soundSelected = new SoundEffect(contents.getValue("SoundSelected"));
		
		if(contents.containsAttribute("SoundClicked"))
			soundClicked = new SoundEffect(contents.getValue("SoundClicked"));
		
		return new ButtonType(f, fontColor, size,  borderWidth, borderColor, highlightedBorderColor, interiorColor, soundSelected, soundClicked );
	}

} 
