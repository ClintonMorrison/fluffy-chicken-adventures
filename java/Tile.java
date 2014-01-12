import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 */


/*
 * This class describes a tile object
 * Tile maps will contain a set of numbers which reference tile types
 */
//TODO: Add sound effect to class, sound of player stepping on that type of tile


public class Tile
{
	public static enum Type { AIR, SOLID, WATER, HAZARD };
	
	//Attributes
	private String name; //Name of tile type
	private Image img; //Image of tile to be drawn on screen
	private Type type; //The type of tile
	private int damage; //How much damage the tile does (ONLY for Hazard tiles)
	
	//Constructor
	public Tile(String name, Image img, Type type, int damage)
	{
		this.name = name;
		this.img = img;
		this.type = type;
		this.damage = damage;
	}
	
	//Get name
	public String getName()
	{
		return name;
	}
	
	//Get image
	public Image getImage()
	{
		return img;
	}
	
	//Get type
	public Type getType()
	{
		return type;
	}
	
	//Converts string to type
	public static Type stringToType(String str)
	{
		str = str.toLowerCase();
		
		if(str.equals("air"))
			return Type.AIR;
		if(str.equals("solid"))
			return Type.SOLID;
		if(str.equals("water"))
			return Type.WATER;
		if(str.equals("hazard"))
			return Type.HAZARD;
		else
		{
			System.out.println("Warning - Type " + str + " not recognized.");
			return Type.AIR; //Return AIR if type can't be found
		}
	}
	
	
	//Get damage
	public int getDamage()
	{
		if(type == Type.HAZARD) //Only allow for damage if set as Hazard
			return damage;
		else
			return 0;
	}
	
	//Creates a tile from game file contents
	public static Tile createFromGamefileContent(GameFileContents contents)
	{
		//Get name
		String name = contents.getValue("Name");
		
		//Load image
		Image img = ImageReader.loadImage(contents.getValue("Image"));
		
		//Get type
		Type t = stringToType(contents.getValue("Type"));
		
		//Get damage
		int dam = Integer.parseInt(contents.getValue("Damage"));
		
		return new Tile(name, img, t, dam); //Make new tile
	}
	
	
}
