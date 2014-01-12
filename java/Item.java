import java.awt.Image;
import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class describes an item.
 * An array of items in the ResourceManager class will relate item IDs to item descriptions
 */

public class Item extends AnimatedObject
{

	//Attributes
	private ItemEffect.Type itemType; //Type of item
	private double magnitude; //The magnitude of the item's effect (if applicable, ie amount to increase health, speed, jump, damage of lasers)
	private long duration; //How long the item's effect will be active (-1 implies infinite)
	private boolean collected;
	private ArrayList<SoundEffect> soundEffects; 
	private GameTimer idleSoundTimer; 
	
	//Enumerations
	public enum Type { RESTORE_HEALTH, INCREASE_WALK_SPEED, INCREASE_JUMP_SPEED, GOAL, HAZARD, COIN }; //Enumeration containing all of the different types of items
	
	//Animation indexes
	public static final int ANIMATION_IDLE = 0;
	//public static final int ANIMATION_COLLECTED = 1; TODO: Add this?
	
	//Sound indexes
	//public static final int SOUND_IDLE = 0;
	public static final int SOUND_COLLECTED = 0;
			
	//Constructor
	public Item(String name, Vector position, ArrayList<Animation> animations, ArrayList<SoundEffect> soundEffects, ItemEffect.Type type, double magnitude, long duration) 
	{
		super(name, position, animations, 0);
		itemType = type;
		this.magnitude = magnitude;
		this.soundEffects = soundEffects;
		this.soundEffects.trimToSize();
		this.duration = duration;
		collected = false;
		idleSoundTimer = new GameTimer((long) 1000);
	}
	
	//Updates the item
	public void update(Long millisTaken, UserInput currentUserInput)
	{	
		//Update sound effects
		for(int i = 0; i < soundEffects.size(); i++)
		{
			soundEffects.get(i).update();
		}
		
		super.update(millisTaken, currentUserInput);
	}
	
	//Get type
	public ItemEffect.Type getItemType()
	{
		return itemType;
	}
	
	//Gets if item was collected
	public boolean isCollected()
	{
		return collected;
	}
	
	//Gets magnitude of item's effect
	public double getMagnitude()
	{
		return magnitude;
	}
	
	//Called when the item is collected, returns 
	public ItemEffect collect()
	{
		if(collected == false) //If the item hasn't been collected yet
		{
			collected = true; 
			
			soundEffects.get(SOUND_COLLECTED).play();
			
			return new ItemEffect(itemType, duration, magnitude, getCurrentFrame().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
			//setCurrentAnimationIndex(ANIMATION_COLLECTED); TODO: Add collected animation? (IE explosion for bomb)
		}
		else
			return null;
	}
	
	//Creates item from the contents of a GameFile
	public static Item createFromGameFileContents(GameFileContents contents, Vector position)
	{
		String name = "";
		ArrayList<Animation> animations = new ArrayList<Animation>();
		ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
		ItemEffect.Type type = null; 
		double magnitude = 0;
		long duration = 0;
		
		//Get name
		if(contents.containsAttribute("Name"))
			name = contents.getValue("Name");

		//Get magnitude
		if(contents.containsAttribute("Duration"))
			duration = Long.parseLong(contents.getValue("Duration"));
		
		//Get magnitude
		if(contents.containsAttribute("Magnitude"))
			magnitude = Double.parseDouble(contents.getValue("Magnitude"));
		
		//Get type
		String str = "";
		if(contents.containsAttribute("Type"))
			str = contents.getValue("Type");
		 
		if(str.equals("COIN"))
			type = ItemEffect.Type.COIN;
		
		else if(str.equals("RESTORE_HEALTH"))
			type = ItemEffect.Type.RESTORE_HEALTH;
		
		else if(str.equals("INCREASE_WALK_SPEED"))
			type = ItemEffect.Type.INCREASE_WALK_SPEED;
		
		else if(str.equals("INCREASE_JUMP_SPEED"))
			type = ItemEffect.Type.INCREASE_JUMP_SPEED;
		
		else if(str.equals("REDUCE_HEALTH"))
			type = ItemEffect.Type.REDUCE_HEALTH;
		
		else if(str.equals("REDUCE_HEALTH"))
			type = ItemEffect.Type.REDUCE_HEALTH;
		
		else if(str.equals("GOAL"))
			type = ItemEffect.Type.GOAL;
		
		//Get animations
		for(int i = 1; i < 5; i++)
		{
			if(contents.containsAttribute("Animation" + i))
			{
				animations.add(Animation.createFromGamefileContents(GameFileReader.readGameFile(contents.getValue("Animation" + i)))); //Add animation to list
			}
			else
			{
				animations.add(null); //Add null if animation not defined
		
			}
		}
				
		//Get sound effects
		for(int i = 1; i < 2; i++)
		{
			if(contents.containsAttribute("Sound" + i))
				soundEffects.add(new SoundEffect(contents.getValue("Sound" + i))); //Add sound to list
			else
				soundEffects.add(null); //Add null if sound not found/loaded
		}
		
		//Return new item
		return new Item(name, position, animations, soundEffects, type, magnitude, duration);	
	}
	
	//Gets the sound effects associated with this item
	public ArrayList<SoundEffect> getSoundEffects()
	{
		return soundEffects;
	}
	
	//Creates a copy of this item
	public Item copy()
	{
		//Copy animations
		ArrayList<Animation> newAnimations = new ArrayList<Animation>(getAnimations().size());
		for(int i = 0; i < getAnimations().size(); i++)
		{
			
			Animation a;
			if(getAnimations().get(i) != null)
				a = getAnimations().get(i).copy();
			else
				a = null;
			
			newAnimations.add(a);
		}
		
		return new Item(getName(), getPosition().copy(), newAnimations, soundEffects, itemType, magnitude, duration);
	}

	//Sets items to be muted
	public void setMuted(boolean muted) 
	{
		for(int i = 0; i < soundEffects.size(); i++)
		{
			soundEffects.get(i).setMuted(muted);
		}
		
	}
	
		

}
