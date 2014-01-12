import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 * 
 * This class describes a projectile object in the game.
 * This includes both arrows and magic.
 */

public class Projectile extends Sprite
{
	public final int ANIMATION_FACING_RIGHT = 0;
	public final int ANIMATION_FACING_LEFT = 1;
	public final int ANIMATION_HIT = 0;
	
	public final int SOUNDEFFECT_LAUNCH = 0;
	public final int SOUNDEFFECT_HIT = 1;
	
	//Attributes
	private double damage; //The damage the projectile can do
	private double maxSpeed; //The intial speed of the projectile (magnitude)
	private ArrayList<Animation> animations;
	private ArrayList<SoundEffect> soundEffects;
	private boolean isActive;
	private boolean fromPlayer; //Is the projectile from an NPC or the player?
	private boolean muted; 
	
	//Constructor
	public Projectile(String name, ArrayList<Animation> animations,  ArrayList<SoundEffect> soundEffects, Character c, double maxSpeed, double damage, boolean fromPlayer, boolean muted) 
	{
		super(name, new Vector(0,0), animations, 0, new Vector(0,0));
		
		this.damage = damage;
		this.maxSpeed = maxSpeed;
		this.soundEffects = soundEffects;
		this.soundEffects.trimToSize();
		this.animations = animations;
		this.animations.trimToSize();
		isActive = true;
		
		//Assign proper position and velocity to projectile based on direction character is facing	
		if(c.getFacing() == Facing.RIGHT)
		{
			this.getPosition().x = c.getBoundingRectangle().getMaxX() + c.getDimensions().getWidth()/1.5;
			this.getPosition().y = c.getPosition().y + c.getBoundingRectangle().height/4;
			this.getVelocity().x = maxSpeed;
			this.setCurrentAnimationIndex(ANIMATION_FACING_RIGHT);
		}
		else
		{
			this.getPosition().x = c.getPosition().x - c.getDimensions().getWidth()/1.5 - this.getDimensions().width/2;
			this.getPosition().y = c.getPosition().y + c.getBoundingRectangle().height/4;
			this.getVelocity().x = -maxSpeed;
			this.setCurrentAnimationIndex(ANIMATION_FACING_LEFT);
		}
		
		//Play launch sound effect
		soundEffects.get(SOUNDEFFECT_LAUNCH).play();
	}
	
	//Mutes sound effects
	public void setMuted(boolean muted)
	{
		for(int i = 0; i < soundEffects.size(); i++)
		{
			soundEffects.get(i).setMuted(muted);
		}
	}
	
	//Updates the projectile
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		
		//Update sound effects
		for(int i = 0; i < soundEffects.size(); i++)
		{
			soundEffects.get(i).update();
		}
	}
	
	
	//Gets damage done by projectile
	public double getDamage()
	{
		return damage;
	}
	
	//Gets if projectile was launched by player
	public boolean fromPlayer()
	{
		return fromPlayer;
	}
	
	//Called when the projectile hits an object
	public void hitObject()
	{
		this.setCurrentAnimationIndex(ANIMATION_HIT);
		this.setVelocity(new Vector(0,0));
		isActive = false;
		//soundEffects.get(SOUNDEFFECT_HIT).play();
	}
	
	//Called when projectile hits character, deals damage to character
	public void hitCharacter(Character c)
	{
		if(isActive)
		{
			hitObject();
			c.damageHealth(damage);
		}
	}
	
	
	
}
