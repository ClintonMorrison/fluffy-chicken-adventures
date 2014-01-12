/*
 * Author: Clinton Morrison
 * Date: May 21, 2013
 */


/*
 * This class describes on stage/level in the game. A stage has a tilemap, a player, and a list of NPCs.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Stage implements DrawableEntity
{	
	//Attributes
	private TileMap tileMap;
	private LinkedList<NPC> npcs;
	private LinkedList<Item> items;
	private LinkedList<Projectile> projectiles;
	private Player player;
	private boolean stageRunning; //Flagged false when player wins/losses/pauses game?
	private boolean stagePaused;
	private boolean stageComplete; //Has the player completed the stage?
	private boolean drewCompletedStage;
	private boolean stageLost; //Has played lost the stage?
	private String name; //The name of the level
	private GameTimer stageCompleteTimer; 
	
	//Physics constants
	private Vector gravity;
	
	//Constructor
	public Stage(String name, TileMap tileMap, LinkedList<NPC> npcs, LinkedList<Item> items, Player player, Vector gravity, boolean muted)
	{
		this.name = name;
		this.tileMap = tileMap;
		this.npcs = npcs;
		this.items = items;
		this.player = player; 
		this.gravity = gravity;
		projectiles = new LinkedList<Projectile>();
		stageRunning = true;
		stagePaused = false;
		stageComplete = false;
		drewCompletedStage = false;
		stageLost = false;
		stageCompleteTimer = new GameTimer((long) 100);
		setMuted(muted);
	
	}

	//Sets game elements to be muted
	public void setMuted(boolean muted) 
	{
		player.setMuted(muted);
		
		for(int i = 0; i < npcs.size(); i++)
		{
			npcs.get(i).setMuted(muted);
		}
		
		for(int i = 0; i < items.size(); i++)
		{
			items.get(i).setMuted(muted);
		}
		
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).setMuted(muted);
		}
		
	}


	//Draws the stage
	public void draw(Graphics g) 
	{
		//Draw tilemap
		tileMap.draw(g);
		
		//Draw each NPC
		for(int i = 0; i < npcs.size(); i++)
			npcs.get(i).draw(g);
		
		//Draw each item
		for(int i = 0; i < items.size(); i++)
			items.get(i).draw(g);
		
		//Draw player
		player.draw(g);	
		
		//Draw projectiles
		for(int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).setOffset(player.getOffset());
			projectiles.get(i).draw(g);
		}
		
		//TODO: Move this code elswhere, perhaps to container game state class
		drawPlayerInfo(g);
		
	}
	
	//Draws misc debug info
	private void drawPlayerInfo(Graphics g)
	{
		g.setColor(Color.black);
		final int infoWindowHeight = 50;
		Rectangle windowRect = new Rectangle(0, (int)tileMap.getWindow().height-infoWindowHeight, tileMap.getWindow().width, infoWindowHeight);
		
		//Draw player info window at bottom of screen
		g.fill3DRect(windowRect.x, windowRect.y, windowRect.width, windowRect.height, true);
		
		g.setFont(new Font("Monospaced", 0, 14));
		g.setColor(Color.WHITE);
		
		//Display # of coins collected
		g.drawString("COINS " + (int)player.getStats().getCoins(), (int)(windowRect.x + windowRect.width*0.01), (int)(windowRect.y + windowRect.height*0.92));
		
		//Display Health bar
		g.drawString("HEALTH", (int)(windowRect.x + windowRect.width *0.01 ), (int)(windowRect.y + windowRect.height*0.37));
		player.drawHealthBar(g, new Vector(70, tileMap.getWindow().height-infoWindowHeight*0.9), 400);
		
		//Display active effects
		g.setColor(Color.WHITE);
		g.drawString("ACTIVE EFFECTS", (int)(windowRect.x + windowRect.width *0.57), (int)(windowRect.y + windowRect.height*0.2));
		
		ArrayList<Image> effectIcons = player.getStats().getActiveEffectIcons();
		int x = (int)(windowRect.x + windowRect.width *0.57);
		int y = (int)(windowRect.y + windowRect.height*0.4);
		
		for(int i = 0; i < effectIcons.size(); i++)
		{
			g.drawImage(effectIcons.get(i), x, y, null);
			x += 25;
		}
	}


	//Updates the stage
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		if((stageRunning && !stagePaused && !stageComplete) || !drewCompletedStage)
		{
			if(tileMap.outOfMapBounds(player))
				player.kill();
			
			//Update each NPC
			for(int i = 0; i < npcs.size(); i++)
			{
				addProjectiles(npcs.get(i).getProjectilesFired());
				
				npcs.get(i).update(millisPassed, currentUserInput, player, tileMap.getWindow());
				npcs.get(i).applyGravity(gravity);
				tileMap.fixCollision(npcs.get(i));
				npcs.get(i).setOffset(player.getOffset());
				
				if(tileMap.outOfMapBounds(npcs.get(i)))
					npcs.get(i).kill();
				
				if(player.collidingWith(npcs.get(i)) && npcs.get(i).isAlive())
					npcs.get(i).damagePlayerOnContact(player);	//Character.fixCollision(player, npcs.get(i));
				
				if(!npcs.get(i).isAlive() && npcs.get(i).animationFinished())
				{
					npcs.get(i).update((long)0, currentUserInput);
					npcs.remove(i);
				}
				
			}
			
			//Update each item
			for(int i = 0; i < items.size(); i++)
			{
				items.get(i).update(millisPassed, currentUserInput);
				items.get(i).setOffset(player.getOffset());
			}
			
			//Update each projectile
			for(int i = 0; i < projectiles.size(); i++)
			{
				projectiles.get(i).update(millisPassed, currentUserInput);
				projectiles.get(i).setOffset(player.getOffset());
				
				for(int j = 0; j < npcs.size(); j++)
				{
					if(i < projectiles.size() && npcs.get(j).collidingWith(projectiles.get(i).getBoundingRectangle()))
					{
						projectiles.get(i).hitCharacter(npcs.get(j));
						projectiles.get(i).update((long)0, currentUserInput);
						projectiles.remove(i);
					}
					if(i < projectiles.size() && player.collidingWith(projectiles.get(i).getBoundingRectangle()) && !projectiles.get(i).fromPlayer())
					{
						projectiles.get(i).hitCharacter(player);
						projectiles.get(i).update((long)0, currentUserInput);
						projectiles.remove(i);
					}
					
				}
				
				if(i < projectiles.size() && tileMap.collidingWithRectangle(projectiles.get(i).getBoundingRectangle()))
				{
					projectiles.get(i).hitObject();
					projectiles.get(i).update((long)0, currentUserInput);
					projectiles.remove(i);
				}
			
			
			}
				
			//Update player
			player.update(millisPassed, currentUserInput);
			player.applyGravity(gravity);
			tileMap.fixCollision(player);
			addProjectiles(player.getProjectilesFired());
			
			tileMap.setOffset(player.getOffset()); //Offset by player's position
			
			for(int i = 0; i < items.size(); i++)
			{
				Item item = items.get(i);
				
				if(player.collidingWith(item.getBoundingRectangle()))
				{
					player.collectItem(item);
					items.get(i).update((long)0, currentUserInput);
					items.remove(i); //Remove item from game
				}
			}
			
			if(!player.isAlive())
				stageLost = true;
			else if(player.reachedGoal())
			{
				stageCompleteTimer.update(millisPassed);
				if(stageCompleteTimer.getIsFinished())
					stageComplete = true;
			}
			
		}
	}
	
	//Gets if stage was completed
	public boolean wasCompleted()
	{
		return stageComplete;
	}
	
	//Gets if the player has lost the stage
	public boolean wasLost()
	{
		return stageLost;
	}
	
	//Adds new projectiles to list of projectiles
	public void addProjectiles(LinkedList<Projectile> p)
	{
		for(int i = 0; i < p.size(); i++)
			projectiles.add(p.get(i));
	}
	

	public static Stage createStageFromFile(String filePath, Dimension window, boolean muted, GameStateManager.Difficulty difficulty)
	{	
		//Get contents from tile
		GameFileContents contents = GameFileReader.readGameFile(filePath);
		
		
		TileMap map = null;
		Vector gravity = new Vector(0,0);
		String name = "(No Name Given)";
		
		//Get name
		if(contents.containsAttribute("Name"))
		{
			name = contents.getValue("Name");
		}
		
		//Create tile map
		if(contents.containsAttribute("TileMap"))
			map = new TileMap(contents.getValue("TileMap"), window);
		else
			System.out.println("Error. No map file specified in Stage file.");
		
		//Get gravity
		if(contents.containsAttribute("Gravity"))
			gravity = new Vector(0, Double.parseDouble(contents.getValue("Gravity")));
		else
			System.out.println("Error. Attribute \'gravity\' not defined in Stage file. Gravity will be set to 0.");
		
		//Read tile grid of items, players, and NPCs
		
		String NPC_symbols = ""; //Array of characters, each character corresponding to an NPC type
		String Item_symbols = ""; //Array of characters, each character corresponding to an Item type
		String Player_symbol = ""; //Character(s) corresponding to the Player
		String Empty_symbols = ""; //Array of characters, each corresponding to empty space in grid
		
		ArrayList<String> rows = new ArrayList<String>(); //List of all of the rows of strings
		boolean nextRowExists = true;
		ArrayList<NPC> NPC_types = new ArrayList<NPC>();
		ArrayList<Item> Item_types = new ArrayList<Item>();
		
		LinkedList<NPC> npcs = new LinkedList<NPC>();
		LinkedList<Item> items = new LinkedList<Item>();
		Player player = null;
		
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
		
		//Add rows to tile grid
		for(int j = 0; j < rowCount; j++)
		{
			String row = rows.get(j);
			
			for(int i = 0; i < colCount; i++) 
			{
				String symbol = ""+row.charAt(i); //The current symbol being processed
				Rectangle tileRect = map.getTileRectangle(new Point(i,j)); //Bounding rectangle for the current tile
				
				//Check if symbol is undefined, and try to define it if it is
				if(!NPC_symbols.contains(symbol) && !Item_symbols.contains(symbol) && !Player_symbol.contains(symbol)) 
				{
					if(contents.containsAttribute(symbol))
					{
						if(!contents.getValue(symbol).equals("empty")) //If the symbol isn't defined as empty 
						{
							GameFileContents objectContents = GameFileReader.readGameFile(contents.getValue(symbol));
							
							//Determine what type of object the symbol describes
							if(objectContents.containsAttribute("Object"))
							{
								String objectType = objectContents.getValue("Object");
								
								//If this is an NPC file define a new NPC type
								if(objectType.equals("NPC"))
								{
									NPC newNPCType = NPC.createFromGamefileContents(objectContents, new Vector(0,0), difficulty);
									NPC_types.add(newNPCType);
									NPC_symbols += symbol;
								} //Otherwise if it is an Item file define a new item type
								else if(objectType.equals("Item"))
								{
									Item newItemType = Item.createFromGameFileContents(objectContents, new Vector(0,0));
									Item_types.add(newItemType);
									Item_symbols += symbol;
								} //Otherwise if it is a Player file that defines the player object
								else if(objectType.equals("Player"))
								{
									player = Player.createFromGamefileContents(objectContents, new Vector(0,0), window, difficulty);
									Player_symbol += symbol;
									
								}
								
							}
							else
								System.out.println("Error, object type not defined. Stage file is missing \'Object\' attribute.");
						} else
						{
							Empty_symbols += symbol; //The symbol signifies an empty spot in the grid
						}
					}
					else
						System.out.println("Error, symbol " + symbol + " is not defined in the Stage file.");
				}
				
				if(Empty_symbols.contains(symbol))
				{
					//Do nothing, this tile is empty
				}
				else if (NPC_symbols.contains(symbol)) //If the symbol at i,j has already been processed as an NPC symbol
				{
					
					NPC new_npc = NPC_types.get(NPC_symbols.indexOf(symbol)).copy();
					new_npc.setCenterPosition(new Vector(tileRect.getCenterX(), tileRect.getCenterY())); //Center object at tile in grid specified
					npcs.add(new_npc.copy()); //Add a copy of the npc
					
				} 
				else if (Item_symbols.contains(symbol)) //If the symbol at i,j has already been processed as an Item symbol
				{
					Item newItem = Item_types.get(Item_symbols.indexOf(symbol)).copy();
					newItem.setCenterPosition(new Vector(tileRect.getCenterX(), tileRect.getCenterY()));
					items.add(newItem);
				}
				else if(Player_symbol.contains(symbol))
				{
					player.setCenterPosition(new Vector(tileRect.getCenterX(), tileRect.getCenterY()));
				}
				else //If the symbol has not been seen before
				{
					System.out.println("Error, the symbol " + symbol + " is not defined.");
				}	
			}
		}
		
		
		return new Stage(name, map, npcs, items, player, gravity, muted);
		
	} 
	
	//Gets name of level
	public String getName()
	{
		return name;
	}
	
	//Sets difficulty level of stage
	public void setDifficulty(GameStateManager.Difficulty difficulty)
	{
		player.setDifficulty(difficulty);
		
		for(int i = 0; i < npcs.size(); i++)
		{
			npcs.get(i).setDifficulty(difficulty);
		}
	}
	
	//Gets a screenshot of the current game
	public Image getScreenShot()
	{
		URL imgURL = this.getClass().getResource("/resources/images/black.png");
		Image screenshot = null;
		
		try {
			screenshot = ImageIO.read(imgURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Graphics g = screenshot.getGraphics();
		this.draw(g);
		
		return screenshot;
	}
	
} //End class
