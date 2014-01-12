/*
 * Author: Clinton Morrison
 * Date: June 11, 2013
 * 
 * This game state takes over when the user pauses the game.
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.LinkedList;


public class GameStatePaused extends GameStateMenu {

	//Buttons
	public static final int BUTTON_RESUME = 0;
	
	//Attributes
	Image screenshot; //Screenshot of the game
	
	//Constructor
	public GameStatePaused(Image screenshot, LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) 
	{
		super(labels, buttons, gameStateManager, g, window);
		this.screenshot = screenshot;
		
	}
	
	//Draws the state
	public void draw(Graphics g)
	{
		//if(screenshot != null)
			g.drawImage(screenshot, 0, 0, null);
		
		super.draw(g);
	}
	
	//Updates the state
	public void update(Long millisPassed, UserInput currentUserInput)
	{
		super.update(millisPassed, currentUserInput);
		
		if(getButtons().get(BUTTON_RESUME).wasClicked() || currentUserInput.keyWasTyped(KeyEvent.VK_P))
		{
			getGameStateManager().setState(GameStateManager.RUNNING);
		}
		
	}
	
	//Sets screenshot to be used
	public void setScreenshot(Image img)
	{
		this.screenshot = img;
	}
	
	//Creates this game state from a menuFile
	public static GameStatePaused createFromFile(Image screenshot, String menuFilePath, 
			GameStateManager gameStateManager, Graphics g, Dimension window)
	{
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(GameFileReader.readGameFile(menuFilePath), g, window, gameStateManager);
		
		return new GameStatePaused(screenshot, menu.getLabels(), menu.getButtons(), gameStateManager, g, window);
	}
	
}
