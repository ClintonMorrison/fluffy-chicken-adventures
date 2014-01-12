import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 */


/*
 * This class describes a tilemap. It is a set of tiles.
 */

public class TileMap 
{
	//Attributes
	private ArrayList<Tile> tileTypes; //The different type of tile
	private int tiles[][]; //The tiles on the map (represented by IDs corresponding to tile_types array
	private int tileLength; //The size of a side length of a tile
	private Dimension window; //How big is the screen the player can see?
	private int colCount;
	private int rowCount;
	private Vector offset; //Translates screen (used to keep player centered)
	private Image background;
	
	//Constructor
	public TileMap(ArrayList<Tile> tileTypes, int[][] tiles, int tileLength, Dimension window, int colCount, int rowCount, Image background)
	{
		this.tileTypes = tileTypes;
		this.tiles = tiles;
		this.tileTypes.trimToSize();
		this.tileLength = tileLength;
		this.window = window;
		this.colCount = colCount;
		this.rowCount = rowCount;
		offset = new Vector(0,0);
		this.background = background;
	}
	
	//Constructor --> Reads map file and creates map
	public TileMap(String filePath, Dimension window)
	{
		offset = new Vector(0,0);
		this.window = window; 
		
		//Get contents from map file
		GameFileContents contents = GameFileReader.readGameFile(filePath);
		String tile_symbols = ""; //Array of characters, each character corresponding to tile type
		
		ArrayList<String> rows = new ArrayList<String>(); //List of all of the rows of strings
		boolean nextRowExists = true;
		tileTypes = new ArrayList<Tile>();
		
		rowCount = 1;
		
		//Find all of the rows defined
		while (nextRowExists)
		{
			if(contents.containsAttribute("Row"+ rowCount)) //If there is another row defined
			{
				rows.add(contents.getValue("Row" + rowCount));
				rowCount ++;
			}
			else
			{
				nextRowExists = false;
			}
		}
		
		rowCount = rows.size(); 
		colCount = rows.get(0).length(); //Number of columns in map
		
		//Find length of smallest row (this will be used for colCount
		for(int i = 0; i < rows.size(); i++)
		{
			colCount = Math.min(colCount, rows.get(i).length());
		}
		
		//Initialize tile array
		tiles = new int[rowCount][colCount];
		
		//Add rows to tile grid
		for(int i = 0; i < rowCount; i++)
		{
			String row = rows.get(i);
			
			for(int j = 0; j < colCount; j++) 
			{
				if (tile_symbols.contains(""+row.charAt(j))) //If the symbol at i,j has already been processed
				{
					tiles[i][j] = tile_symbols.indexOf(row.charAt(j)); //Add id of tile related to tile symbol to coordinate i,j
				} 
				else //If the symbol has not been seen before
				{
					tile_symbols += row.charAt(j); //Add unseen symbol to list of symbols
					String tileFilePath = contents.getValue(""+tile_symbols.charAt(tile_symbols.length()-1)); //Get filepath of gamefile describing Tile object [length - 1]
					try{
						GameFileContents tileContents = GameFileReader.readGameFile(tileFilePath);
						Tile createdTile = Tile.createFromGamefileContent(GameFileReader.readGameFile(tileFilePath));
						tileTypes.add(createdTile);
					}
					catch (Exception e){	
						System.out.println("Critical error, could not read tile file: " + tileFilePath);
					}
					tiles[i][j] = tile_symbols.indexOf(row.charAt(j)); //Add id of tile related to tile symbol to coordinate i,j
				}
			}
			
		}
		
		//Get tile height/width of tile
		ImageIcon tileIcon = new ImageIcon(tileTypes.get(0).getImage()); //Create image icon to find width and height of tile
		tileLength = tileIcon.getIconWidth();
		
		//Load background image
		if(contents.containsAttribute("BackgroundImage"))
		{
			try
			{
				background = ImageReader.loadImage(contents.getValue("BackgroundImage"));
			}
			catch (Exception e)
			{
				System.out.println("Error! Problem loading tilemap background image:");
				e.printStackTrace();
				background = null;
			}
		}
	}
	
	//Draws the tile map
	public void draw(Graphics g)
	{
		g.drawImage(background, 0, 0, null); //Draw background image first
		
		int leftmostVisibleColumn = 0, rightmostVisibleColumn = colCount;
		int highestVisibleRow = 0, lowestVisibleRow = rowCount;
		
		//Determine left most tiles that have to be drawn
		if(offset.x < 0)
			leftmostVisibleColumn = -(int)offset.x / tileLength;
		
		//Determine right most tiles that have to be drawn
		if((-offset.x + (double)window.width)/(double)tileLength + 1 < colCount)
			rightmostVisibleColumn = ((int) ((-offset.x + (double)window.width)/(double)tileLength)) + 1;
		
		//Determine highest tiles that have to be drawn
		highestVisibleRow = 0;
		
		//Determine lowest tiles that have to be drawn
		if(((double)window.height)/(double)tileLength + 1 < rowCount)
			lowestVisibleRow = (int) (((double)window.height)/(double)tileLength) + 1;
		
		//Only draw the tiles that are visible to player
		for(int i = leftmostVisibleColumn; i < rightmostVisibleColumn; i++)
		{
			for(int j = highestVisibleRow; j < lowestVisibleRow; j++)
			{
				g.drawImage(tileTypes.get(tiles[j][i]).getImage(), i*tileLength + (int)offset.x, j*tileLength, null); //Draw each tile
			}
		}
	}
	
	//Gets window size
	public Dimension getWindow()
	{
		return window;
	}
	
	//Determines if tile map is colliding with rectangle
	public boolean collidingWithRectangle(Rectangle r)
	{	
		int leftmostVisibleColumn = 0, rightmostVisibleColumn = colCount;
		int highestVisibleRow = 0, lowestVisibleRow = rowCount;
		
		//Determine left most tiles that have to be checked
		if((int)(r.x / tileLength) > 0)
			leftmostVisibleColumn = (int)(r.x / tileLength);
		
		//Determine right most tiles that have to be checked
		if((int) (r.getMaxX()/tileLength) + 1 < colCount)
			rightmostVisibleColumn = (int) (r.getMaxX()/tileLength) + 1;
		
		//Determine highest tiles that have to be checked
		if((int)(r.getY()/tileLength) > 0)
			highestVisibleRow = (int) (r.getY()/tileLength);
		
		//Determine lowest tiles that have to be checked
		if((int) (r.getMaxY()/tileLength) + 1 < colCount)
			lowestVisibleRow = (int) (r.getMaxY()/tileLength) + 1;
		
		
		//Only return the tiles that need to be checked
		for(int i = leftmostVisibleColumn; i < rightmostVisibleColumn; i++)
		{
			for(int j = highestVisibleRow; j < lowestVisibleRow; j++)
			{
				Rectangle tileRect = new Rectangle(i*tileLength, j*tileLength, tileLength, tileLength);
				
				try
				{
					if(tileTypes.get(tiles[j][i]).getType() == Tile.Type.SOLID && r.intersects(tileRect))
						return true;
				} catch (ArrayIndexOutOfBoundsException excp)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	//Gets tiles character is colliding with
	public LinkedList<Point> getCollidingTiles(Character c)
	{
		LinkedList<Point> collidingTiles = new LinkedList<Point>();
		
		Rectangle r = c.getBoundingRectangle();
		
		int leftmostVisibleColumn = 0, rightmostVisibleColumn = colCount;
		int highestVisibleRow = 0, lowestVisibleRow = rowCount;
		
		//Determine left most tiles that have to be checked
		if((int)(c.getBoundingRectangle().x / tileLength) > 0)
			leftmostVisibleColumn = (int)(c.getBoundingRectangle().x / tileLength);
		
		//Determine right most tiles that have to be checked
		if((int) (c.getBoundingRectangle().getMaxX()/tileLength) + 1 < colCount)
			rightmostVisibleColumn = (int) (c.getBoundingRectangle().getMaxX()/tileLength) + 1;
		
		//Determine highest tiles that have to be checked
		if((int)(c.getBoundingRectangle().getY()/tileLength) > 0)
			highestVisibleRow = (int) (c.getBoundingRectangle().getY()/tileLength);
		
		//Determine lowest tiles that have to be checked
		if((int) (c.getBoundingRectangle().getMaxY()/tileLength) + 1 < colCount)
			lowestVisibleRow = (int) (c.getBoundingRectangle().getMaxY()/tileLength) + 1;
		
		
		//Only return the tiles that need to be checked
		for(int i = leftmostVisibleColumn; i < rightmostVisibleColumn; i++)
		{
			for(int j = highestVisibleRow; j < lowestVisibleRow; j++)
			{
				Rectangle tileRect = new Rectangle(i*tileLength, j*tileLength, tileLength, tileLength);
				
				try
				{
					if(tileTypes.get(tiles[j][i]).getType() == Tile.Type.SOLID && c.collidingWith(tileRect))
						collidingTiles.add(new Point(i, j)); //Add each tile that is near the character
				} catch (ArrayIndexOutOfBoundsException excp)
				{
					c.kill(); //Kill character if not inside map anymore
				}
			}
		}
		
		return collidingTiles;
		
	}
	
	//Fixs collisions with character and tile map
	public void fixCollision(Character c)
	{
		LinkedList<Point> collidingTiles = getCollidingTiles(c);
		
		for(int i = 0; i < collidingTiles.size(); i++)
		{
			Rectangle tileRect = getTileRectangle(collidingTiles.get(i)); //new Rectangle(collidingTiles.get(i).x*tileLength, collidingTiles.get(i).y*tileLength, tileLength, tileLength);
			Character.fixCollision(c, tileRect); //Fix collision with tile and player by moving player
			Vector centerDifference = new Vector(c.getPosition().x - tileRect.getCenterX(), c.getCenter().y - tileRect.getCenterY());
			Rectangle charRect = c.getBoundingRectangle();
			
			//TODO: Imrpove this!!! Present character cannot jump if on edge of block
			if(centerDifference.y < 0 && tileRect.contains(new Point((int)charRect.getCenterX(), (int)charRect.getMaxY() ))) //If player is above tile 
				c.setInAir(false); //Set false since the collision means the player is on the ground //TODO: Fix this so that you have to touch the top of the block
		}
		
	}

	//Sets drawing offset (how much the map should be shifted to account for the player's movement)
	public void setOffset(Vector offset)
	{
		this.offset = offset;
	}
	
	//Creates TileMap from game file contents
	public static TileMap createFromGamefileContents(GameFileContents contents, Dimension window)
	{
		
		
		String tile_symbols = ""; //Array of characters, each character corresponding to tile type
		
		ArrayList<String> rows = new ArrayList<String>(); //List of all of the rows of strings
		boolean nextRowExists = true;
		ArrayList<Tile> tileTypes = new ArrayList<Tile>();
		
		int rowCount = 1;
		
		//Find all of the rows defined
		while (nextRowExists)
		{
			if(contents.containsAttribute("Row"+ rowCount)) //If there is another row defined
			{
				rows.add(contents.getValue("Row" + rowCount));
				rowCount ++;
			}
			else
			{
				nextRowExists = false;
			}
		}
		
		rowCount = rows.size(); 
		int colCount = rows.get(0).length(); //Number of columns in map
		
		//Find length of smallest row (this will be used for colCount
		for(int i = 0; i < rows.size(); i++)
		{
			colCount = Math.min(colCount, rows.get(i).length());
		}
		
		//Initialize tile array
		int[][] tiles = new int[rowCount][colCount];
		
		//Add rows to tile grid
		for(int i = 0; i < rowCount; i++)
		{
			String row = rows.get(i);
			
			for(int j = 0; j < colCount; j++) 
			{
				if (tile_symbols.contains(""+row.charAt(j))) //If the symbol at i,j has already been processed
				{
					tiles[i][j] = tile_symbols.indexOf(row.charAt(j)); //Add id of tile related to tile symbol to coordinate i,j
				} 
				else //If the symbol has not been seen before
				{
					tile_symbols += row.charAt(j); //Add unseen symbol to list of symbols
					String tileFilePath = contents.getValue(""+tile_symbols.charAt(tile_symbols.length()-1)); //Get filepath of gamefile describing Tile object [length - 1]
					tileTypes.add(Tile.createFromGamefileContent(GameFileReader.readGameFile(tileFilePath)));
					tiles[i][j] = tile_symbols.indexOf(row.charAt(j)); //Add id of tile related to tile symbol to coordinate i,j
				}
			}
			
		}
		
		//Get tile height/width of tile
		ImageIcon tileIcon = new ImageIcon(tileTypes.get(0).getImage()); //Create image icon to find width and height of tile
		int tileLength = tileIcon.getIconWidth();
		
		//Load background image
		Image background = null;
		if(contents.containsAttribute("BackgroundImage"))
		{
			try
			{
				background = ImageReader.loadImage(contents.getValue("BackgroundImage"));
			}
			catch (Exception e)
			{
				System.out.println("Error! Problem loading tilemap background image:");
				e.printStackTrace();
				background = null;
			}
		}
		
		return new TileMap(tileTypes, tiles, tileLength, window, colCount, rowCount, background);
	}
	
	//Gets the length of a tile
	public int getTileLength()
	{
		return tileLength;
	}

	//Get the bounding rectangle for a specified tile at point p in the grid
	public Rectangle getTileRectangle(Point p)
	{
		return new Rectangle(p.x * tileLength, p.y*tileLength, tileLength, tileLength);
	}
	
	//Checks if character is out of bounds
	public boolean outOfMapBounds(Character c)
	{
		Rectangle cRect = c.getBoundingRectangle();
		if(cRect.getMaxY() > rowCount*tileLength || cRect.getMaxX() > colCount*tileLength || cRect.getX() < 0)
			return true;
		else 
			return false;
	}
	
	//Checks if position is out of bounds of map
	public boolean outOfMapBounds(Vector position)
	{
		if(position.x / tileLength > (double)colCount || position.x < 0 || position.y/tileLength > rowCount)
			return true;
		else
			return false;
	}
} 
