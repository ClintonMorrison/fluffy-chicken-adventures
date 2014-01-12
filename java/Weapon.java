import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 16, 2013
 * 
 * This class describes the weapon that a character has.
 * The weapon is held in the arm of the character.
 * There are ranged, magic, and meele weapons. 
 * The class stores information about the name, damage, and type of weapon, as well as any special effects. (TODO: add special effects for weapons)
 * The class also extends AnimatedObject and is drawable/updatable
 */
public class Weapon extends AnimatedObject implements DrawableEntity{
	
	public enum Type { MEELE, RANGED, MAGIC };
	public final int ANIMATION_FACING_RIGHT = 0;
	public final int ANIMATION_FACING_LEFT = 1;
	public final int ANIMATION_FIRE = 2;
	
	//Attributes
	private double damage; //How much damage the weapon does
	private Type type; //The type of the weapon
	private Projectile projectile; //The type of projectile the weapon fires (NULL if sword/warrior weapon)
	
	
	//Constructor
	public Weapon(String name, Vector position,
			ArrayList<Animation> animations, int currentAnimationIndex, double damage, Type type, Projectile projectile) 
	{
		super(name, position, animations, currentAnimationIndex);
		this.damage = damage;
		this.type = type;
		this.projectile = projectile; 
	}
	
	//Creates a projectile 
	public Projectile getLaunchedProjectile()
	{
		return null;
	}
	
	//Gets amount of damage weapon does
	public double getDamage()
	{
		return damage;
	}
	
	//Gets type of weapon
	public Type getType()
	{
		return type;
	}
	
	
}
