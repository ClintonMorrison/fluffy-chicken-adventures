import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 20, 2013
 */


/*
 * This class describes the character which the player controls
 */
public class Player extends Character
{
	//Attributes
	private Dimension window;
	private Vector initialPosition;
	
	public Player(Dimension window, String name, Vector position,
			ArrayList<Animation> animations,
			ArrayList<SoundEffect> soundEffects, ProjectileType weapon, double maxHealth,
			double maxSpeed, double maxForce, double mass, GameStateManager.Difficulty difficulty) 
	{
		super(name, position, animations, soundEffects, weapon, maxHealth, maxSpeed, maxForce,
				mass, GameStateManager.Difficulty.MEDIUM);
		this.window = window;
		this.setCenterPosition(new Vector(window.width/2, window.height/2));
		initialPosition = new Vector(this.getPosition().x, this.getPosition().y);
		setMuted(false);
		// TODO Auto-generated constructor stub
	}
	
	//Get offset from original position
	public Vector getOffset()
	{
		return new Vector(initialPosition.x - getPosition().x, initialPosition.y - getPosition().y);
	}

	//Respond to user input and update player
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		
		
		
		if(currentUserInput.keyIsDown(KeyEvent.VK_RIGHT) || currentUserInput.keyIsDown(KeyEvent.VK_D))
		{
			moveRight();
		}
		else if(currentUserInput.keyIsDown(KeyEvent.VK_LEFT)|| currentUserInput.keyIsDown(KeyEvent.VK_A))
		{
			moveLeft();
		}
		else
		{
		}
		
		if(currentUserInput.keyWasTyped(KeyEvent.VK_LEFT) || 
				currentUserInput.keyWasTyped(KeyEvent.VK_RIGHT) || 
				currentUserInput.keyWasTyped(KeyEvent.VK_A) || 
				currentUserInput.keyWasTyped(KeyEvent.VK_D))
		{
			notMoving();
		}
		
		
		if(currentUserInput.keyWasTyped(KeyEvent.VK_SPACE) || currentUserInput.keyIsDown(KeyEvent.VK_SPACE))
		{
			this.fireWeapon(true);
		}
		
		if(currentUserInput.keyIsDown(KeyEvent.VK_Q)) //Debug only, set V to 0. TODO: Remove this code.
		{
			this.setVelocity(new Vector(0,0));
			this.getForce().x = 0;
			this.getForce().y = 0;
		}
		
		if(currentUserInput.keyWasTyped(KeyEvent.VK_X)) //Debug only, deal damage to self TODO: Remove this code.
		{
			this.damageHealth(10);
		}
		
		
		if(currentUserInput.keyIsDown(KeyEvent.VK_UP) || currentUserInput.keyIsDown(KeyEvent.VK_W))
		{
			this.jump();
		}
		
	}
	
	//Overrides the draw method, drawing player at center screen (his position is used to offset tilemap)
	public void draw(Graphics g)
	{
		//Draw current animation frame at screen center
		g.drawImage(getCurrentFrame(), (int)initialPosition.x, (int)getPosition().y, null);
	}
	
	//Create from file
	public static Player createFromGamefileContents(GameFileContents contents, Vector position, Dimension window, GameStateManager.Difficulty difficulty)
	{
		Character c = Character.createFromGamefileContents(contents, position, difficulty);
		return new Player(window, c.getName(), c.getPosition(), c.getAnimations(), c.getSoundEffects(), 
							c.getProjectileType(), c.getMaxHealth(), c.getWalkSpeed(), c.getMaxForce(), 
							c.getMass(), difficulty);
	}
	
	//Gets if player has collected the goal
	public boolean reachedGoal()
	{
		return getStats().reachedGoal();
	}
	
	//Sets difficult level this Player is set at
	public void setDifficulty(GameStateManager.Difficulty difficulty)
	{
		
		if(getDifficulty() == GameStateManager.Difficulty.EASY)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() / 2.0);
			this.getStats().setHealth(this.getHealth() / 2.0);
		}
		else if(getDifficulty() == GameStateManager.Difficulty.HARD)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 2);
			this.getStats().setHealth(this.getHealth() * 2);
		}
	
		if(difficulty == GameStateManager.Difficulty.EASY)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 2);
			this.getStats().setHealth(this.getHealth() * 2);
		}
		else if(difficulty == GameStateManager.Difficulty.HARD)
		{
			this.getStats().setMaxHealth(this.getMaxHealth() * 0.5);
			this.getStats().setHealth(this.getHealth() * 0.5);
		}
			
			super.setDifficulty(difficulty);
		}

	
	
}
