import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: June 7, 2013
 *
 * This class describes the a menu which contains only text info.
 * This includes the credits, as well as the instructions.
 * 
 */

public class GameStateMenuInfo extends GameStateMenu
{
	//Constants defining buttons
	public static final int BUTTON_BACK = 0;
	
	//Constructor
	public GameStateMenuInfo(LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) 
	{
		super(labels, buttons, gameStateManager, g, window);
		
	}

	//Creates from a file
	public static GameStateMenuInfo createFromFile(String filename, Graphics g, Dimension window, GameStateManager manager)
	{
		GameFileContents contents = GameFileReader.readGameFile(filename);
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(contents, g, window, manager);
		return new GameStateMenuInfo(menu.getLabels(), menu.getButtons(), menu.getGameStateManager(), g, window);
	}
	
	//Updates the menu
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		if(getButtons().get(BUTTON_BACK).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_MAIN);
		}
	}

}
