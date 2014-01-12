import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class is used to read and process .txt files which describe game objects
 * 
 * # marks a comment line
 * Format -> Attribute: value { on each line }
 */

public class GameFileReader {
	
	//Attributes
	private String filePath; 
	private InputStream in;
	private Scanner input;
	
	//Constructor
	public GameFileReader(String filePath)
	{
		this.filePath = filePath;
		try {
			this.in = getClass().getResourceAsStream(filePath);
			this.input = new Scanner(in);
		} catch (Exception e) {
			System.out.println("Error. There was an error reading file " + filePath);
		}
		
		if(this.input == null) {
			System.out.println("Warning - GameFileReader could not open file " + filePath);
		}
		
	}
	
	public GameFileContents getContents()
	{
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		
		while(input.hasNextLine())
		{
			String line = input.nextLine(); //to lower case
			
			if(line.length() == 0) //If the line is empty
			{
				//Do nothing
			}
			else if(line.charAt(0) == '#') //If the line is a comment
			{
				line = ""; //Ignore line, it is a comment
			}
			else
			{
				//Read attribute
				String attribute = "";
				int i;
				for(i = 0; line.charAt(i) != ':'; i++ )
				{
					if(line.charAt(i) != ' ') //Add non whitespace characters to attribute name
						attribute += line.charAt(i);
				}
				i++; //Skip the ':' character
				
				//Read value
				String value = "";
				boolean firstCharFound = false;
				for(i = i; i < line.length(); i++ )
				{
					if(line.charAt(i) == ' ' && !firstCharFound) //Add non whitespace characters to attribute name (until first char reached)
					{
						//Do nothing
					}
					else
					{
						value += line.charAt(i);
						firstCharFound = true;
					}
					
				}
				
				//Add attribute and value
				attributes.add(attribute);
				values.add(value);
			}
		}
		
		//Return contents of file
		return new GameFileContents(attributes, values);
	}
	
	
	//Closes the file
	public void close()
	{
		input.close();
		try 
		{
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Open, read, and close file (may have delay)
	public static GameFileContents readGameFile(String filePath)
	{	
		try
		{
			GameFileReader gameFile = new GameFileReader(filePath);
			System.out.println("Reading " + filePath);
			GameFileContents contents = gameFile.getContents();
			System.out.println("Contents: " + contents);
			gameFile.close();
			return contents;
		}
		catch (NullPointerException e)
		{ e.printStackTrace(); }
		
		return null;
		
	}

}
