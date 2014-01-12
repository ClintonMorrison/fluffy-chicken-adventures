/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This abstract class describes how the game should 
 */
public abstract class GameState implements DrawableEntity  
{
	
	
	//Attributes
	private GameStateManager gameStateManager;

	//Gets game manager 
	public GameStateManager getGameStateManager()
	{
		return gameStateManager;
	}
	
	//Sets game manager
	public void setGameStateManager(GameStateManager gameStateManager)
	{
		this.gameStateManager = gameStateManager;
	}

	//Causes thread to sleep 
	public void gameSleep(long time) 
	{
		try 
		{
		   Thread.sleep(time);   
		}
		catch (InterruptedException e) { e.printStackTrace();}
	}

	//Sets if this state is muted
	public abstract void setMuted(boolean muted);
}
