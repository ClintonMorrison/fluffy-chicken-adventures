import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 25, 2013
 *
 * This class describes a non-player-character. 
 * This is the type of character which the player encounters in the game.
 * This class extends the character class, but adds several methods to
 * control the character's movement (AI)
 */

public class NPC extends Character
{
	//Specifies AI behavior type
	public enum AI_Type { PATROL, AGGRESSIVE }
	
	//Attributes
	int healthBarWidth; //How wide is health bar of NPC
	GameTimer AIMoveTimer; //When timer elapses AI decides what move to make (faster timers mean harder computers)
	boolean walkingLeft; //Is the AI walking left?
	AI_Type behavior; //What type of behavior does the NPC have?
	long AI_move_time; //Delay for AI move timer
	double damageOnContact; //Does the NPC damage the player on contact?
	GameTimer contactDamageCooldown;  
	
	
	//Constructor
	public NPC(String name, Vector position, ArrayList<Animation> animations,
			ArrayList<SoundEffect> soundEffects, ProjectileType weapon, double maxHealth,
			double maxSpeed, double maxForce, double mass, AI_Type behavior, Long AI_move_time, double damageOnContact, Long contactDamageCooldownTime, GameStateManager.Difficulty difficulty) 
	{
		super(name, position, animations, soundEffects, weapon, maxHealth, maxSpeed, maxForce,
				mass, GameStateManager.Difficulty.MEDIUM);
		
		this.behavior = behavior;
		walkingLeft = false;
		this.AI_move_time = AI_move_time;
		AIMoveTimer = new GameTimer(AI_move_time);
		healthBarWidth = this.getDimensions().width;
		
		AIMoveTimer.update((long) (Math.random() * AIMoveTimer.getWaitTime()));
		this.damageOnContact = damageOnContact;
		contactDamageCooldown = new GameTimer(contactDamageCooldownTime);
	}
	
	//Sets difficult level this NPC is set at
	public void setDifficulty(GameStateManager.Difficulty difficulty)
	{
		
		if(getDifficulty() == GameStateManager.Difficulty.EASY)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 2.0);
			this.getStats().setHealth(this.getHealth() * 2.0);
		}
		else if(getDifficulty() == GameStateManager.Difficulty.HARD)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() / 1.5);
			this.getStats().setHealth(this.getHealth() / 1.5);
		}
		
		if(difficulty == GameStateManager.Difficulty.EASY)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 0.5);
			this.getStats().setHealth(this.getHealth() * 0.5);
		}
		else if(difficulty == GameStateManager.Difficulty.HARD)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 1.5);
			this.getStats().setHealth(this.getHealth() * 1.5);
		}
		
		super.setDifficulty(difficulty);
	}
	
	//Updates character
	public void update(Long millisTaken, UserInput currentUserInput, Player player, Dimension window)
	{
		super.update(millisTaken, currentUserInput);
		AIMoveTimer.update(millisTaken);
		contactDamageCooldown.update(millisTaken);
		
		if(behavior == AI_Type.PATROL)
		{
			if(this.nearPlayer(player, window))
			{
				if(player.getCenter().x > this.getCenter().x && getFacing() == Facing.RIGHT   ||   player.getCenter().x < this.getCenter().x && getFacing() == Facing.LEFT)
					this.fireWeapon(false);
			}
		}
			
		if(AIMoveTimer.getIsFinished())
		{
			if(behavior == AI_Type.AGGRESSIVE)
			{
				if(this.nearPlayer(player, window))
				{
					if(player.getPosition().x > this.getPosition().x)
						moveRight();
					else
						moveLeft();
					
					if(player.getCenter().y < this.getPosition().y)
						this.jump();
					
					if( Math.abs(this.getCenter().y - player.getPosition().y) < Math.max(player.getDimensions().height/2, this.getDimensions().height/2));
						this.fireWeapon(false);
				}
			}
			
			if(behavior == AI_Type.PATROL)
			{
				walkingLeft = !walkingLeft;
				
				if(walkingLeft)
					moveLeft();
				else
					moveRight();
			}
			
				
			AIMoveTimer.reset();
		}
		
	}
	
	//Draws the NPC
	public void draw(Graphics g)
	{
		super.draw(g);
		g.setColor(Color.magenta);
		
		
		if(isAlive())
		{
			Vector healthBarPosition = new Vector((int)getPosition().x + (int)getOffset().x, (int)getPosition().y - 30);
			
			//Draw health bar
			this.drawHealthBar(g, healthBarPosition, healthBarWidth);
		}
	}
	
	//Create from file
	public static NPC createFromGamefileContents(GameFileContents contents, Vector position, GameStateManager.Difficulty difficulty)
	{
		Character c = Character.createFromGamefileContents(contents, position, difficulty);
		long AI_move_time = 1000;
		long contactDamageCooldownTime = 0;
		double damageOnContact = 0;
		
		//Get damage on contact
		if(contents.containsAttribute("DamageOnContact"))
		{
			damageOnContact = Double.parseDouble(contents.getValue("DamageOnContact"));
		}
		
		//Get cooldown time for contact damage
		if(contents.containsAttribute("ContactDamageCooldownTime"))
		{
			contactDamageCooldownTime = Long.parseLong(contents.getValue("ContactDamageCooldownTime"));
		}
		
		
		
		//Get AI type from file
		AI_Type behavior = AI_Type.PATROL;
		
		//Get AI behavior type
		if(contents.containsAttribute("AI_Type"))
		{
			String str = contents.getValue("AI_Type");
			if(str.equals("PATROL"))
				behavior = AI_Type.PATROL;
			if(str.equals("AGGRESSIVE"))
				behavior = AI_Type.AGGRESSIVE;
		}
		
		//Get AI_move_time, the time between AI moves
		if(contents.containsAttribute("AI_Move_Time"))
		{
			String str = contents.getValue("AI_Move_Time");
			AI_move_time = Long.parseLong(str);
		}
		
		return new NPC(c.getName(), c.getPosition(), c.getAnimations(), c.getSoundEffects(), 
				c.getProjectileType(), c.getMaxHealth(), c.getWalkSpeed(), c.getMaxForce(), 
				c.getMass(), behavior, AI_move_time, damageOnContact, contactDamageCooldownTime, difficulty);
	}
	
	//Creates a copy of this NPC
	public NPC copy()
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
		
		ProjectileType newProjectileType = null;
		
		if(getProjectileType() != null)
			newProjectileType = getProjectileType().copy();
		
		
		return new NPC(getName(), getPosition().copy(), newAnimations,
				getSoundEffects(), newProjectileType, getMaxHealth(),
				getWalkSpeed(), getMaxForce(), getMass(), behavior, AI_move_time, damageOnContact, contactDamageCooldown.getWaitTime(), getDifficulty());
	}
	
	//Determines if NPC is near player
	public boolean nearPlayer(Player player, Dimension window)
	{
		return this.getPosition().getDifference(player.getPosition()).getMagnitude() < window.width/2;
	}
	
	//Damages player on contact
	public void damagePlayerOnContact(Player player)
	{
		if(contactDamageCooldown.getIsFinished() && damageOnContact > 0)
		{
			player.damageHealth(damageOnContact);
			contactDamageCooldown.reset();
		}
	}

}
