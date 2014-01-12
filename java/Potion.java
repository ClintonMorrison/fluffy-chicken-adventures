/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class describes a potion object
 */

public class Potion 
{
	//Attributes
	public enum Type { HEALTH, ENERGY, DEFENCE, SPEED };
	
	private String name; //Name of potion
	private Type potionType; 
	private int magnitude;  //The magnitude of the potion's effect, ie how much health it heals
	
	//Constructor
	public Potion(String name, Type potionType, int magnitude)
	{
		this.name = name;
		this.potionType = potionType;
		this.magnitude = magnitude;
	}

}
