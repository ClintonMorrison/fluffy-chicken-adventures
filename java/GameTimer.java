/*
 * Author: Clinton Morrison
 * Date: May 20, 2013
 */


/*
 * This class acts as a timer. It must be intialized and started, and then updated regularily.
 * Another class using the timer can check when the timer has finished, and perform an action.
 */

public class GameTimer 
{
	//Attributes
	private Long waitTime; //Time to wait
	private Long timeElapsed; //Time elapsed so far
	private boolean isFinished;
	
	//Constructor
	public GameTimer(Long waitTime)
	{
		this.waitTime = waitTime;
		isFinished = false;
		timeElapsed = (long)0;
	}
	
	//Updates the timer
	public void update(Long millisTaken)
	{
		timeElapsed += millisTaken;
		if(timeElapsed > waitTime)
			isFinished = true;
		else 
			isFinished = false;
	}
	
	//Gets if timer is finished
	public boolean getIsFinished()
	{
		return isFinished;
	}
	
	//Resets timer to 0
	public void reset()
	{
		timeElapsed = (long) 0;
		isFinished = false;
	}
	
	//Get the  wait time
	public Long getWaitTime()
	{
		return waitTime;
	}

}
