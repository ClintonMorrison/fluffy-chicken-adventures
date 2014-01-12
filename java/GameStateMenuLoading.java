import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;


/*
 * Author: Clinton Morrison
 * Date: June 30, 2013
 *
 * This class describes the loading menu
 * 
 */

public class GameStateMenuLoading extends GameStateMenu implements Runnable
{

	
	//Constructor
	public GameStateMenuLoading(LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) 
	{
		super(labels, buttons, gameStateManager, g, window);
		
		//Load game content in new thread
		Thread t = new Thread(this);
		t.start();	
	}

	//Creates from a file
	public static GameStateMenuLoading createFromFile(String filename, Graphics g, Dimension window, GameStateManager manager)
	{
		
		GameFileContents contents = GameFileReader.readGameFile(filename);
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(contents, g, window, manager);
		return new GameStateMenuLoading(menu.getLabels(), menu.getButtons(), menu.getGameStateManager(), g, window);
	}
	
	//Updates the menu
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		
		//Switch to menu when game is fully loaded
		if(this.getGameStateManager().finishedLoading())
			this.getGameStateManager().transition("A Game by Clinton Morrison", 2500, GameStateManager.MENU_MAIN);
	}

	//Loads the content of the game
	public void run() {
		this.getGameStateManager().initilizeStates();
		
	}

}
