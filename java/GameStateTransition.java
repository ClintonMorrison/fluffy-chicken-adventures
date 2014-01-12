/*
 * Author: Clinton Morrison
 * Date: June 9, 2013
 * 
 * This is a GameState which is to be used between transitions between other gameStates.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class GameStateTransition extends GameState
{

	//Attributes
	private long displayTime; //How long should the transition be shown?
	private long timeElapsed; //How long how the transition been shown?
	private int targetState; //Game state index to switch to after completion of transition
	private GameLabel label; 
	private Color orginalColor;
	//Constructor
	public GameStateTransition(String text, String fontFilePath, GameStateManager gameStateManager,
			Graphics g, Dimension window, long displayTime, int targetState) 
	{
		this.label = new GameLabel("Transition label", g, new Rectangle(0,0, window.width, window.height), text, GameFont.createFromGamefileContents(GameFileReader.readGameFile(fontFilePath)));
		setGameStateManager(gameStateManager);
		this.displayTime = displayTime;
		this.targetState = targetState;
		orginalColor = label.getColor();
		label.setColor(new Color(0,0,0));
	}
	
	@Override
	public void draw(Graphics g) 
	{
		label.draw(g);
	}

	
	public void update(Long millisPassed, UserInput currentUserInput) 
	{	
		timeElapsed += (long) millisPassed;
		
		if(currentUserInput.keyWasTyped(KeyEvent.VK_SPACE)) //If user wants to skip transition
		{
			timeElapsed = displayTime;
		}
		
		if(timeElapsed > displayTime)
		{
			getGameStateManager().setState(targetState); //Set state to target state if all time elapsed
		}
		
		double percentFinished = (double)timeElapsed / (double)displayTime;
		if(percentFinished > 1.0)
			percentFinished = 1;

		Color c = new Color((int) (255 * percentFinished), 
							(int) (255 * percentFinished),
							(int) (255 * percentFinished));
		label.setColor(c);
		
		label.update(millisPassed, currentUserInput);	
	}

	@Override
	public void setMuted(boolean muted) {
		// TODO Auto-generated method stub
		
	}

}
