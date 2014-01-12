import java.awt.Image;

/*
 * Author: Clinton Morrison
 * Date: May 28, 2013
 *
 * This class describes the effect of an item.
 * It keeps track of the type, magnitude, duration,
 * and time elapsed so far
 */

public class ItemEffect 
{
	//Enumeration
	public enum Type { COIN, RESTORE_HEALTH, REDUCE_HEALTH, INCREASE_WALK_SPEED, INCREASE_JUMP_SPEED, GOAL };
	
	//Attributes
	private GameTimer effectExpiredTimer;
	private double magnitude;
	private Type type;
	private Image icon;
	
	//Constructor
	public ItemEffect(Type type, long duration, double magnitude, Image icon)
	{
		this.type = type;
		this.magnitude = magnitude;
		effectExpiredTimer = new GameTimer(duration);
		this.icon = icon;
	}
	
	//Updates this item effect
	public void update(long millisTaken)
	{
		effectExpiredTimer.update(millisTaken);
	}
	
	//Gets magnitude of effect
	public double getMagnitude()
	{
		return magnitude;
	}
	
	//Gets the type of the effect
	public Type getType()
	{
		return type;
	}
	
	//Gets if effect is expired
	public boolean isExpired()
	{
		return effectExpiredTimer.getIsFinished();
	}
	
	//Gets icon
	public Image getIcon()
	{
		return icon;
	}
}
