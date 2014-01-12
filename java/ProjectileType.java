import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 16, 2013
 */


/*
 * This class describes a weapon. The Projectile class uses a projectile type to create a moving projectile.
 * 
 */
public class ProjectileType 
{
	//Attributes
	private double damage; //The damage the projectile can do
	private double maxSpeed; //The intial speed of the projectile (magnitude)
	private long coolOff; //Milliseconds that must be waited before weapon can be fired again
	private boolean readyToFire;
	private GameTimer coolOffTimer;
	ArrayList<Animation> animations;
	ArrayList<SoundEffect> soundEffects;
	boolean muted;
	
	//Constructor
	public ProjectileType(double damage, double maxSpeed, long coolOff, ArrayList<Animation> animations, ArrayList<SoundEffect> soundEffects)
	{
		this.damage = damage;
		this.maxSpeed = maxSpeed;
		this.coolOff = coolOff;
		coolOffTimer = new GameTimer(coolOff);
		this.animations = animations;
		this.animations.trimToSize();
		this.soundEffects = soundEffects;
		this.soundEffects.trimToSize();
		readyToFire = true;
		setMuted(false);
	}
	
	//Sets sounds to muted
	public void setMuted(boolean muted)
	{
		for(int i = 0; i < soundEffects.size(); i++)
		{
			soundEffects.get(i).setMuted(muted);
		}
	}
	
	//Updates the projectile type
	public void update(long millisTaken)
	{
		if(!readyToFire)
			coolOffTimer.update(millisTaken);
		
		if(coolOffTimer.getIsFinished() == true)
		{
			coolOffTimer.reset();
			readyToFire = true;
		}
	}
	
	//Fires projectile, returns projectile
	public Projectile fireProjectile(Character c, boolean isPlayer)
	{
		if( readyToFire )
		{
			readyToFire = false;
			ArrayList<Animation> newAnims = new ArrayList<Animation>(animations.size());
			
			for(int i = 0; i < animations.size(); i++)
			{
				newAnims.add(animations.get(i).copy());
			}
			
			return new Projectile("projectile from " + c.getName(), newAnims, soundEffects, c, maxSpeed, damage, isPlayer, muted);
		}
		
		return null;
		
	}
	
	//Creates projectile from game file contents
	public static ProjectileType createFromGameFileContents(GameFileContents contents)
	{
		//Attributes to create
		double damage = 0, maxSpeed = 0;
		long coolOff = 0;
		ArrayList<Animation> animations = new ArrayList<Animation>();
		ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
		
		//Get stats
		if(contents.containsAttribute("Damage"))
			damage = Double.parseDouble(contents.getValue("Damage"));
		if(contents.containsAttribute("MaxSpeed"))
			maxSpeed = Double.parseDouble(contents.getValue("MaxSpeed"));
		if(contents.containsAttribute("CoolOff"))
			coolOff = Long.parseLong(contents.getValue("CoolOff"));
	
		//Get animations
		for(int i = 1; i < 3; i++)
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
		for(int i = 1; i < 3; i++)
		{
			if(contents.containsAttribute("Sound" + i))
				soundEffects.add(new SoundEffect(contents.getValue("Sound" + i))); //Add sound to list
			else
				soundEffects.add(null); //Add null if sound not found/loaded
		}
			
		
		return new ProjectileType(damage, maxSpeed, coolOff, animations, soundEffects);
	}
	
	//Copies this projectile type
	public ProjectileType copy()
	{
		return new ProjectileType(damage, maxSpeed, coolOff, animations, soundEffects);
	}
	
	
}
