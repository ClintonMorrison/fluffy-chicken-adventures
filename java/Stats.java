/*
 * Author: Clinton Morrison
 * Date: May 28, 2013
 *
 * This class contains statics specific to 
 * a character regarding their health
 * speed, and attack. 
 * 
 * It also maintains a list of active effects
 * that are currently acting on a Character.
 * 
 */

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

public class Stats 
{
	//Attributes
	private int coins; 
	private double health;
	private double maxHealth; 
	private double walkSpeed;
	private double jumpSpeed;
	private double attack;
	private boolean collectedGoal; //Has the player collected the goal?
	private LinkedList<ItemEffect> activeItemEffects; 
	
	//Constructor
	public Stats(double maxHealth, double walkSpeed, double jumpSpeed, double attack)
	{
		coins = 0;
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.walkSpeed = walkSpeed;
		this.jumpSpeed = jumpSpeed;
		this.attack = attack;
		activeItemEffects = new LinkedList<ItemEffect>();
		collectedGoal = false;
	}
	
	//Updates the active effects list
	public void update(Long millisTaken)
	{
		for(int i = 0; i < activeItemEffects.size(); i++)
		{
			activeItemEffects.get(i).update(millisTaken);
			
			if(activeItemEffects.get(i).isExpired())
				activeItemEffects.remove(i);
			
		}
	}
	
	//Gets health of character
	public double getHealth()
	{
		return health;
	}
	
	//Damages the character
	public void damageHealth(double damage)
	{
		health -= damage;
		
		if(health < 0)
			health = 0;
		
		if(health > maxHealth)
			health = maxHealth;
	}
	
	//Heals health of character
	public void restoreHealth(double amount)
	{
		health += amount;
		
		if(health < 0)
			health = 0;
		
		if(health > maxHealth) //Make sure health doesn't exceed maximum health
			health = maxHealth;
	}
	
	//Gets the maximum horizontal speed
	public double getWalkSpeed()
	{
			return walkSpeed + getMagnitudeOfActiveEffect(ItemEffect.Type.INCREASE_WALK_SPEED);
	}
	
	//Gets coins collected
	public int getCoins()
	{
		return coins;
	}
	
	//Gets max health of character
	public double getMaxHealth()
	{
		return maxHealth;
	}
	
	//Gets the jump speed
	public double getJumpSpeed()
	{
		return jumpSpeed + getMagnitudeOfActiveEffect(ItemEffect.Type.INCREASE_JUMP_SPEED);
	}
	
	//Gets the attack of the character
	public double getAttack()
	{
		return attack;
	}
	
	//Gets the magnitude of all of the active effects of a given type
	private double getMagnitudeOfActiveEffect(ItemEffect.Type type)
	{
		double magnitude = 0;
		
		for(int i = 0; i < activeItemEffects.size(); i++)
		{
			ItemEffect effect = activeItemEffects.get(i);
			if(effect.getType() == type)
				magnitude += effect.getMagnitude();
		}
		
		return magnitude;
	}

	//Adds an item effect to the list of active effects
	public void applyItemEffect(ItemEffect effect)
	{	
		if(effect.getType() == ItemEffect.Type.COIN)
		{
			coins += effect.getMagnitude();
		}
		else if (effect.getType() == ItemEffect.Type.RESTORE_HEALTH)
		{	
			restoreHealth(effect.getMagnitude());
		}
		else if (effect.getType() == ItemEffect.Type.REDUCE_HEALTH)
		{	
			damageHealth(effect.getMagnitude());
		}
		else if (effect.getType() == ItemEffect.Type.GOAL)
		{	
			collectedGoal = true;
		}
		else
		{
			activeItemEffects.add(effect);
		}
	}

	//Gets if character has collected goal
	public boolean reachedGoal()
	{
		return collectedGoal;
	}
	
	//Sets the character's max health
	public void setMaxHealth(double maxHealth)
	{
		this.maxHealth = maxHealth;
	}
	
	//Gets current amount of health
	public void setHealth(double health)
	{
		this.health = health;
	}
	
	//Get active effects icons
	public ArrayList<Image> getActiveEffectIcons()
	{
		ArrayList<Image> icons = new ArrayList<Image>(activeItemEffects.size());
		
		for(int i = 0; i < activeItemEffects.size(); i++)
		{
			icons.add(activeItemEffects.get(i).getIcon());
		}
		
		return icons;
	}
}
