import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 20, 2013
 */


/*
 * This class describes a character in the game. This include the player's character as well
 * as any NPCs the character must fight.
 */
public class Character extends RotatedSprite 
{
	//Constants defining animation sequences
	public static final int ANIMATION_FACING_RIGHT = 0;
	public static final int ANIMATION_FACING_LEFT = 1;
	public static final int ANIMATION_EXPLODING = 2;
	public static final int ANIMATION_FIRING_LASER_RIGHT = 3;
	public static final int ANIMATION_FIRING_LASER_LEFT = 4;
	
	private static final int NUMBER_OF_ANIMATIONS = 5;
	
	//Constants related to sound effects
	public static final int SOUNDEFFECT_IDLE = 0;
	public static final int SOUNDEFFECT_FALL = 1;
	public static final int SOUNDEFFECT_DIE = 2;
	public static final int SOUNDEFFECT_HURT = 3;
	public static final int SOUNDEFFECT_JUMP = 4;


	
	//Other constants
	public static final double friction_coefficient = 1.9;
	public static final long walkSoundTime = 500;
	public static final long idleSoundTime = 5000;
	
	//Attributes
	private boolean inAir; //Is the character currently jumping, falling, or in the air? (He cannot jump when in air and walking sounds are not played)
	private boolean isAlive; //Is the character still alive?
	private double mass; //Mass of the character
	Forces forces; //Manages forces player is under (gravity, friction, force exerted to move, etc.)
	private double maxForce; //The maximum x force the character can exert to move left or right
	private Facing facing; //Which way the character is facing
	private ArrayList<SoundEffect> soundEffects; //Contains all of the sound effects associated with this unit
	private GameTimer walkSoundTimer; //Timer that keeps track of when to play walk sounds
	private GameTimer idleSoundTimer; //Timer that keeps track of when to play idle sounds
	private Stats characterStats; 
	private ProjectileType weapon;
	private LinkedList<Projectile> projectilesFired;
	private GameStateManager.Difficulty difficulty;
	
	//Constructor
	public Character(String name, Vector position, ArrayList<Animation> animations, ArrayList<SoundEffect> soundEffects, ProjectileType weapon, double maxHealth, double walkSpeed, double maxForce, double mass, GameStateManager.Difficulty difficulty) 
	{
		super(name, position, animations, 0, new Vector(0,0), //Set name, position, animations, starting animation to 0, and initial speed to 0
				0.0, new Vector(0,0), 0.0); //Start the character with 0 degrees rotation, rotating about center, with angular velocity 0
		
		//Assign other local variables
		characterStats = new Stats(maxHealth, walkSpeed, 2*walkSpeed, 0); //TODO: replace '0' with attack value; TODO: Make jumpSpeed a seperate variable
		//this.health = maxHealth; TODO: remove
		//this.maxHealth = maxHealth;  TODO: remove
		//this.maxSpeed = maxSpeed;	 TODO: remove
		this.maxForce = maxForce;
		
		this.mass = mass;
		this.soundEffects = soundEffects;
		this.soundEffects.trimToSize();
		
		this.weapon = weapon;
		forces = new Forces(mass);
		//force = new Vector(0,0);
		inAir = true;
		isAlive = true;
		facing = Facing.RIGHT;
		walkSoundTimer = new GameTimer((long) walkSoundTime); //Play walk sound effect
		idleSoundTimer = new GameTimer((long) idleSoundTime); //Play idle sound effect 
		projectilesFired = new LinkedList<Projectile>();
		setMuted(false);
		this.difficulty = difficulty;
	}
	
	//Control is sounds are muted
	public void setMuted(boolean muted)
	{
		if(weapon != null)
			weapon.setMuted(muted);
		
		for (int i = 0; i < soundEffects.size(); i++)
		{
			if(soundEffects.get(i) != null)
				soundEffects.get(i).setMuted(muted);
		}
		
	}
	
	//Sets difficulty level
	public void setDifficulty(GameStateManager.Difficulty difficulty)
	{
		this.difficulty = difficulty;
	}
	
	//Gets difficulty level
	public GameStateManager.Difficulty getDifficulty()
	{
		return difficulty;
	}
	
	//Gets the character's health
	public double getHealth()
	{
		return characterStats.getHealth();
	}
	
	//Gets the character's max velocity
	public double getWalkSpeed()
	{
		return characterStats.getWalkSpeed();
	}
	
	//Damages the character
	public void damageHealth(double damage)
	{
		if(isAlive)
		{
			soundEffects.get(SOUNDEFFECT_HURT).play();
			characterStats.damageHealth(damage);
		}
	}
	
	//Kills the character instantly
	public void kill()
	{
		characterStats.damageHealth(characterStats.getHealth());
		checkIfDeadAndRespond();
	}
	
	//Gets if character is still alive
	public boolean isAlive()
	{
		return isAlive; 
	}
	
	//Gets if character is jumping
	public boolean isJumping()
	{
		return inAir;
	}
	
	//Gets force acting on character
	public Vector getForce()
	{
		return forces.getEffectiveForce();
	}
	
	//Gets sound effects
	public ArrayList<SoundEffect> getSoundEffects()
	{
		return soundEffects;
	}
	
	//Get max health
	public double getMaxHealth()
	{
		return characterStats.getMaxHealth();
	}
	
	//Get max force
	public double getMaxForce()
	{
		return maxForce;
	}
	
	//Get mass
	public double getMass()
	{
		return mass;
	}
	
	//Gets stats
	public Stats getStats()
	{
		return characterStats;
	}
	
	//Sets if character is in air or on ground
	public void setInAir(boolean inAir)
	{
		if(this.inAir == true && inAir == false)
			soundEffects.get(SOUNDEFFECT_FALL).play();
		
		this.inAir = inAir;
	}
	
	//Makes the character jump if able
	public void jump()
	{
		if(!inAir && isAlive)
		{
			getVelocity().y = -characterStats.getJumpSpeed();
			soundEffects.get(SOUNDEFFECT_JUMP).play();
			slide(new Vector(0, -15)); //Slide character up so no longer in contact with ground (this prevents inAir flag from being set to false as the character jumps)
			setInAir(true); //Set in air so you can't jump while in air
		}
	}
	
	//Moves the character left
	public void moveLeft()
	{
		if(isAlive)
		{
			forces.setMovementForce(new Vector(-maxForce, 0));
			
			if(facing == Facing.RIGHT)
				getVelocity().x = 0;
			
			facing = Facing.LEFT;
			this.setCurrentAnimationIndex(ANIMATION_FACING_LEFT);
		}
	}
	
	//Moves the character left
	public void moveRight()
	{
		if(isAlive)
		{
			forces.setMovementForce(new Vector(maxForce, 0));
			
			if(facing == Facing.LEFT)
				getVelocity().x = 0;
			
			facing = Facing.RIGHT;
			this.setCurrentAnimationIndex(ANIMATION_FACING_RIGHT);
		}
	}
	
	//Gets list of fired projectiles fired by  this character (it also resets the list)
	public LinkedList<Projectile> getProjectilesFired()
	{
		LinkedList<Projectile> newProjectiles = new LinkedList<Projectile>();
		for(int i = 0; i < projectilesFired.size(); i++)
		{
			newProjectiles.add(projectilesFired.get(i));
		}
		
		projectilesFired = new LinkedList<Projectile>();
		
		return newProjectiles;
	}
	
	//The character isn't trying to move (ie not exerting force to move, this doesn't set velocity to 0, it sets movement force to 0)
	public void notMoving()
	{
		forces.setMovementForce(new Vector(0,0));
	}
	
	public ProjectileType getProjectileType()
	{
		return weapon;
	}
	
	//Sets x component of velocity to 0
	public void stopHorizontalMovement()
	{
		getVelocity().x = 0;
	}
	
	//Verifies that character is still alive, and acts if character is dead
	private void checkIfDeadAndRespond()
	{
		if(isAlive && characterStats.getHealth() <= 0)
		{
			this.setCurrentAnimationIndex(ANIMATION_EXPLODING);
			this.soundEffects.get(SOUNDEFFECT_DIE).play();
			forces = new Forces(mass);
			setVelocity(new Vector(0,0));
			isAlive = false;
		}
	}
	
	//Cause player to collect given item
	public void collectItem(Item item)
	{
		ItemEffect effect = item.collect();
		characterStats.applyItemEffect(effect);
	}
	
	//Gets the direction the character is facing
	public Facing getFacing()
	{
		return facing;
	}
	
	//Updates the character
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		
		//Update sound effects
		for(int i = 0; i < soundEffects.size(); i++)
		{
			if(soundEffects.get(i) != null)
				soundEffects.get(i).update();
		}
		
		
		//Change velocity based on forces being applied
		getVelocity().x += millisTaken * forces.getEffectiveForce().x / mass;
		getVelocity().y += millisTaken * forces.getEffectiveForce().y / mass;
		
		//Cap velocities at maximum
		if(this.getVelocity().x > Math.abs(characterStats.getWalkSpeed()))
			this.getVelocity().x = characterStats.getWalkSpeed();
		if(this.getVelocity().x < -Math.abs(characterStats.getWalkSpeed()))
			this.getVelocity().x = -getWalkSpeed();
		if(this.getVelocity().y < -characterStats.getJumpSpeed())
			this.getVelocity().y = -characterStats.getJumpSpeed();
		if(this.getVelocity().y > characterStats.getJumpSpeed())
			this.getVelocity().y = characterStats.getJumpSpeed();
		
		if(weapon != null)
		{
			weapon.update(millisTaken);
		}
		
		characterStats.update(millisTaken);
		
		//Apply friction force 
		forces.updateFriction(getVelocity(), friction_coefficient); //TODO: Make friction coefficient depend on tile you are on (send this info when inAir is updated)
		
		//Update sprite
		super.update(millisTaken, currentUserInput); 
		
		//Update timers
		idleSoundTimer.update(millisTaken);
		walkSoundTimer.update(millisTaken);
		
		//Act on timers which have finished
		if (idleSoundTimer.getIsFinished() && isAlive)
		{			
			idleSoundTimer.reset(); //Reset timer
		}
		
		if (walkSoundTimer.getIsFinished() && isAlive) //TODO: Remove this
		{	
			walkSoundTimer.reset(); //Reset timer
		}
		
		//Check if character has died and modify animation and sound accordingly
		checkIfDeadAndRespond();
	}
	
	//Fires weapon and returns projectile
	public void fireWeapon(boolean isPlayer)
	{
		if(weapon != null)
		{
			Projectile p = weapon.fireProjectile(this, isPlayer);
			if(p != null && isAlive())
			{
				projectilesFired.add(p);
			}
		}
	}
	
	//Creates Character from file
	public static Character createFromGamefileContents(GameFileContents contents, Vector position, GameStateManager.Difficulty difficulty)
	{
		//Attributes to create
		String newName = "";
		double newMaxHealth = 0, newMaxSpeed = 0, newMaxForce= 0, newMass = 0;
		ArrayList<Animation> newAnims = new ArrayList<Animation>(NUMBER_OF_ANIMATIONS);
		ArrayList<SoundEffect> newSoundEffects = new ArrayList<SoundEffect>();
		ProjectileType weapon = null;
		
		//Make sure the file is a Character file
		if(contents.containsAttribute("Object"))
		
		//Get the name
		if(contents.containsAttribute("Name"))
			newName = contents.getValue("Name");
		
		//Create projectileType
		if(contents.containsAttribute("ProjectileType"))
		{
			if(!contents.getValue("ProjectileType").equals("None"))
			{
				weapon = ProjectileType.createFromGameFileContents(GameFileReader.readGameFile(contents.getValue("ProjectileType")));
			}
		}
		
		//Get stats
		if(contents.containsAttribute("MaxHealth"))
			newMaxHealth = Double.parseDouble(contents.getValue("MaxHealth"));
		if(contents.containsAttribute("MaxSpeed"))
			newMaxSpeed = Double.parseDouble(contents.getValue("MaxSpeed"));
		if(contents.containsAttribute("MaxForce"))
			newMaxForce = Double.parseDouble(contents.getValue("MaxForce"));
		if(contents.containsAttribute("Mass"))
			newMass = Double.parseDouble(contents.getValue("Mass"));
		
		//Get animations
		for(int i = 1; i < 5; i++)
		{
			if(contents.containsAttribute("Animation" + i))
			{
				newAnims.add(Animation.createFromGamefileContents(GameFileReader.readGameFile(contents.getValue("Animation" + i)))); //Add animation to list
			}
			else
			{
				newAnims.add(null); //Add null if animation not defined
		
			}
		}
		
		//Get sound effects
		for(int i = 1; i < 7; i++)
		{
			if(contents.containsAttribute("Sound" + i))
				newSoundEffects.add(new SoundEffect(contents.getValue("Sound" + i))); //Add sound to list
			else
				newSoundEffects.add(null); //Add null if sound not found/loaded
		}
		
		return new Character(newName, position, newAnims, newSoundEffects, weapon, newMaxHealth, newMaxSpeed, newMaxForce, newMass, difficulty);
		
	}
	
	//Applies gravity to character
	public void applyGravity(Vector grav)
	{
		forces.setGravity(grav);
	}
	
	//Draws health bar for character
	public void drawHealthBar(Graphics g, Vector position, int healthBarWidth)
	{
		if(isAlive())
		{
			//Draw health bar
			int healthWidth = (int) ((getStats().getHealth() / getStats().getMaxHealth()) * healthBarWidth);
			g.setColor(Color.RED);
			g.fill3DRect((int)position.x, (int)position.y, healthBarWidth, 20, true);
			g.setColor(Color.GREEN);
			g.fill3DRect((int)position.x, (int)position.y, healthWidth, 20, true);
		}
	}
	
	//Determines if there is a collision between this character and another
	public boolean collidingWith(Character c)
	{
		return (c.getBoundingRectangle().intersects(this.getBoundingRectangle()));
	}
	
	//Determines if there is a collision between this character and a region specified by a rectangle
	public boolean collidingWith(Rectangle r)
	{
		return (r.intersects(this.getBoundingRectangle()));
	}
	
	//Correct collision between characters c1 and c2 by moving c1 away from c2
	public static void fixCollision(Character c1, Character c2)
	{
		fixCollision(c1, c2.getBoundingRectangle());
	}
	
	//Correct collision between character and region defined by rectangle
	public static void fixCollision(Character c, Rectangle r)
	{
		while(c.collidingWith(r)) //Act until character is no longer colliding with rectangle
		{
			
			//Get difference between center of character and colliding object
			Vector centerDifference = new Vector(c.getCenter().x - r.getCenterX(), c.getCenter().y - r.getCenterY());
			Rectangle intersect = r.intersection(c.getBoundingRectangle());
			
			//Move character is direction that requires least movement
			if(intersect.width < intersect.height) //If the first character is mostly horizontally seperated from the second
			{	
				if(centerDifference.x > 0) //If the first character is mostly to the right of the second
				{
					c.slide(new Vector(intersect.width, 0)); //Move him right
					c.getVelocity().x *= -0.25;
				}
				else
				{
					c.slide(new Vector(-intersect.width, 0)); //Otherwise move him left
					c.getVelocity().x *= -0.25;
				}
			}
			else //Otherwise if they are mostly vertically separated
			{
				 if(centerDifference.y > 0) //If the first character is mostly to the above the second
				{
					c.slide(new Vector(0, intersect.height)); //Move him down
					c.getVelocity().y *= -0.1;
				}
				else
				{
					c.slide(new Vector(0, -intersect.height)); //Otherwise move him up	
					c.getVelocity().y *= -0.0;
				}
			}
			
		}
		
	}
	
}
