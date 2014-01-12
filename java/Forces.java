/*
 * Author: Clinton Morrison
 * Date: May 21, 2013
 */


/*
 * This class contains a set of force vectors. 
 * Characters are subject to various forces. 
 * This class holds these forces and calculates
 * the effective force on the Character/object.
 */

public class Forces 
{
	//Attributes
	private Vector gravity;  //Acceleration due to gravity (F = mg )
	private Vector friction;  //Force from friction
	private Vector movement;  //Force character exerts to move himself
	private double mass;
	
	//Constructor
	public Forces(double mass)
	{
		this.gravity = new Vector(0,0);
		this.friction = new Vector(0,0);
		this.movement =  new Vector(0,0);
		this.mass = mass;
	}
	
	//Gets effective force
	public Vector getEffectiveForce()
	{
		return new Vector(gravity.x + friction.x + movement.x, gravity.y*mass + friction.y + movement.y);
	}
	
	//Sets the force of friction
	public void updateFriction(Vector velocity, double friction_coef)
	{
		friction = calculateFricition(velocity, friction_coef);
	}
	
	//Sets the movement vector controlling character
	public void setMovementForce(Vector force)
	{
		this.movement = force;
	}
	
	//Sets gravity vector
	public void setGravity(Vector acceleration)
	{
		this.gravity = acceleration;
	}
	
	//Calculates friction force
	private Vector calculateFricition(Vector velocity, double friction_coef)
	{
		Vector f = new Vector(0,0);
		
		if(velocity.x > 0 && movement.x == 0)
		{
			f.x = -mass*gravity.y*friction_coef;
		}
		if(velocity.x < 0 && movement.x == 0)
		{
			f.x = mass*gravity.y*friction_coef;
		} 
		
		//Set velocity to 0 if velocity is very small (to prevent character from vibrating)
		if(Math.abs(velocity.x) < 0.09 && velocity.x != 0)
		{
			f.x = 0;
			velocity.x = 0;
		}
		
		return f;
	}

	
}
