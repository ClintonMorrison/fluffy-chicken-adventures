import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 17, 2013
 * 
 * This class describes the main menu of the game
 */
public class GameStateMainMenu extends GameStateMenu {

	//Constants defining buttons
	public static final int BUTTON_PLAY = 0;
	public static final int BUTTON_INSTRUCT = 1;
	public static final int BUTTON_SETTINGS = 2;
	public static final int BUTTON_CREDITS = 3;
	
	
	//Constructor
	public GameStateMainMenu(LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) {
		super(labels, buttons, gameStateManager, g, window);
		
	}
	
	//Draws the game
	public void draw(Graphics g) 
	{
		super.draw(g);
	}

	//Updates the game
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		super.update(millisPassed, currentUserInput);
		
		if(this.getGameStateManager().isGameStarted() && !getButtons().get(BUTTON_PLAY).getText().equals(" Resume "))
		{
			getButtons().get(BUTTON_PLAY).setText(" Resume ");
		}
		else if(!this.getGameStateManager().isGameStarted() && !getButtons().get(BUTTON_PLAY).getText().equals("New Game"))
		{
			getButtons().get(BUTTON_PLAY).setText("New Game");
		}
		
		//Respond to button clicks
		if(getButtons().get(BUTTON_PLAY).wasClicked())
		{
			if(!this.getGameStateManager().isGameStarted())
				this.getGameStateManager().setGameStarted(true);
			
			GameStateRunning s = (GameStateRunning)getGameStateManager().getGameState(GameStateManager.RUNNING);
			s.transitionToStage();
		}		
		else if(getButtons().get(BUTTON_SETTINGS).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_SETTINGS);
		}
		else if(getButtons().get(BUTTON_INSTRUCT).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_INSTRUCT);
		}
		else if(getButtons().get(BUTTON_CREDITS).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_CREDITS);
		}
		
	}
	
	
	//Creates from a file
	public static GameStateMainMenu createFromFile(String filename, Graphics g, Dimension window, GameStateManager manager)
	{
		GameFileContents contents = GameFileReader.readGameFile(filename);
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(contents, g, window, manager);
		return new GameStateMainMenu(menu.getLabels(), menu.getButtons(), menu.getGameStateManager(), g, window);
	}
	
}
