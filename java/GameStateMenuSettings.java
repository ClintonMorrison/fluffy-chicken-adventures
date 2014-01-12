import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: June 7, 2013
 *
 * This class describes the settings menu, where the user can toggle the sound
 * and difficulty of the game.
 */

public class GameStateMenuSettings extends GameStateMenu
{
	//Constants defining buttons
	public static final int BUTTON_DIFFICULTY = 0;
	public static final int BUTTON_SOUND = 1;
	public static final int BUTTON_MUSIC = 2;
	public static final int BUTTON_BACK = 3;
	
	//Constructor
	public GameStateMenuSettings(LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) {
		super(labels, buttons, gameStateManager, g, window);
		// TODO Auto-generated constructor stub
	}


	
	//Creates from a file
	public static GameStateMenuSettings createFromFile(String filename, Graphics g, Dimension window, GameStateManager manager)
	{
		GameFileContents contents = GameFileReader.readGameFile(filename);
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(contents, g, window, manager);
		return new GameStateMenuSettings(menu.getLabels(), menu.getButtons(), menu.getGameStateManager(), g, window);
	}
	
	//Overrides menu setMuted function (leaves sound button unmuted)
	public void setMuted(boolean muted)
	{
		super.setMuted(muted);
		
		//Leave button clicked sound on so button isn't silent when sound toggled back on
		getButtons().get(BUTTON_SOUND).getClickedSoundEffect().setMuted(false);
	}
	
	//Updates the menu
	public void update(Long millisTaken, UserInput currentUserInput)
	{
		super.update(millisTaken, currentUserInput);
		
		//If the BACK button was clicked
		if(getButtons().get(BUTTON_BACK).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_MAIN);
		}
		//If the SOUND button was clicked
		else if(getButtons().get(BUTTON_SOUND).wasClicked())
		{
			getGameStateManager().toggleSound();
			
			if(getGameStateManager().soundIsOn())
				this.getButtons().get(BUTTON_SOUND).setText("Sound: On");
			else
				this.getButtons().get(BUTTON_SOUND).setText("Sound: Off");
		}
		//If the MUSIC button was clicked
		else if(getButtons().get(BUTTON_MUSIC).wasClicked())
		{
			getGameStateManager().toggleMusic();
			
			if(getGameStateManager().musicIsOn())
				this.getButtons().get(BUTTON_MUSIC).setText("Music: On");
			else
				this.getButtons().get(BUTTON_MUSIC).setText("Music: Off");
		}
		//If the DIFFICULTY button was clicked
		else if(getButtons().get(BUTTON_DIFFICULTY).wasClicked())
		{
			getGameStateManager().toggleDifficulty();
			GameStateManager.Difficulty d = getGameStateManager().getDifficulty();
			if(d == GameStateManager.Difficulty.EASY)
				this.getButtons().get(BUTTON_DIFFICULTY).setText("Difficulty: Easy");
			else if(d == GameStateManager.Difficulty.MEDIUM)
				this.getButtons().get(BUTTON_DIFFICULTY).setText("Difficulty: Med");
			else if(d == GameStateManager.Difficulty.HARD)
				this.getButtons().get(BUTTON_DIFFICULTY).setText("Difficulty: Hard");
		}
		
	}

}
