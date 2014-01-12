/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 */

/*
 * This class describes a basic 2D vector object, which will be
 * used to keep track of the position and speed of objects in
 * the game
 */
public class Vector {
	
	//Attributes
	public double x;
	public double y;
	
	//Constructor
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	//No args consturctor
	public Vector()
	{
		this.x = 0.0;
		this.y = 0.0;
	}
	
	//Gets magnitude of vector
	public double getMagnitude()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	//Gets angle of vector
	public double getAngle()
	{
		//TODO make it consider the quadrants to get angle from 0 to 2PI, also make it convert to degrees
		if(x > 0) //Quadrant I or IV
			return Math.toDegrees(Math.atan(y/x));
		
		else if(x < 0) //Quadrant I or IV
			return 180 + Math.toDegrees(Math.atan(y/x));
		
		else
			return 0;
		
	}
	
	//Gets angle between this vector and another vector B
	public double getAngleFromVector(Vector B)
	{
		Vector A = new Vector(x,y);
		Vector AB = new Vector(B.x - A.x, B.y - A.y);
		
		return AB.getAngle();
		
		
	}
	
	//Converts to string
	public String toString()
	{
		return "[" + x + ", " + y + "]";
	}
	
	//Returns normalized version of vector B (|B| = 1)
	public static Vector normalizedVectorOf(Vector B)
	{
		return new Vector(B.x / B.getMagnitude(), B.y / B.getMagnitude());
	}
	
	//Returns normalized version of current vector (non-static)
	public Vector getNormalized()
	{
		return normalizedVectorOf(this);
	}
	
	//gets difference between vectors
	public Vector getDifference(Vector b)
	{
		return new Vector(this.x - b.x, this.y - b.y);
	}
	
	//Returns a copy of this vector
	public Vector copy()
	{
		return new Vector(x, y);
	}
}
