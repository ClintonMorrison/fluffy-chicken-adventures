import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 17, 2013
 * 
 * This class describes the functions the game performs while the player is playing the game. 
 */
public class GameStateRunning extends GameStateMenu {

	//Button IDs
	public static final int BUTTON_MENU = 0;
	public static final int BUTTON_PAUSE = 1;

	//Attributes
	private StageLoader stageLoader;
	private int stageNumber;
	private Stage currentStage;
	private Dimension window;
	
	//Constructor
	public GameStateRunning(String stageLoaderFilename, LinkedList<GameLabel> labels,
			LinkedList<GameButton> buttons, GameStateManager gameStateManager,
			Graphics g, Dimension window) 
	{
		super(labels, buttons, gameStateManager, g, window);
		stageLoader = new StageLoader(stageLoaderFilename);
		stageNumber = 0;
		this.window = window;
		currentStage = stageLoader.loadStage(stageNumber, window, !getGameStateManager().soundIsOn(), getGameStateManager().getDifficulty());
	}
	
	//Draws the game
	public void draw(Graphics g) 
	{
		currentStage.draw(g);
		super.draw(g);
	}

	//Updates the game
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		super.update(millisPassed, currentUserInput);
		currentStage.update(millisPassed, currentUserInput);
		
		if(currentStage.wasLost()) //Reload stage if player lost
		{
			gameSleep(1000);
			currentStage = stageLoader.loadStage(stageNumber, window, !getGameStateManager().soundIsOn(), getGameStateManager().getDifficulty());
			currentStage.setDifficulty(getGameStateManager().getDifficulty());
			transitionToStage();
		}
		
		else if(currentStage.wasCompleted()) //Load next stage if player won 
		{
			gameSleep(1000);
			stageNumber += 1;
			
			//Advance to the next stage
			if(!(stageNumber>stageLoader.getStageCount()-1))
			{
				currentStage = stageLoader.loadStage(stageNumber, window, !getGameStateManager().soundIsOn(), getGameStateManager().getDifficulty());
				currentStage.setDifficulty(getGameStateManager().getDifficulty());
				transitionToStage();
			}
			else
			{
				stageNumber = 0;
				currentStage = stageLoader.loadStage(stageNumber, window, !getGameStateManager().soundIsOn(), getGameStateManager().getDifficulty());
				currentStage.setDifficulty(getGameStateManager().getDifficulty());
				this.getGameStateManager().setGameStarted(false);
				getGameStateManager().transition("You Win!", 4000, GameStateManager.MENU_MAIN);
			}
		}
		
		//Allow user to press ESC to get back to main menu
		if(currentUserInput.keyIsDown(KeyEvent.VK_ESCAPE))
		{
			getGameStateManager().setState(GameStateManager.MENU_MAIN);
		}
		
		//Handle button presses
		if(getButtons().get(BUTTON_MENU).wasClicked())
		{
			getGameStateManager().setState(GameStateManager.MENU_MAIN);
		}
		else if(getButtons().get(BUTTON_PAUSE).wasClicked() || currentUserInput.keyWasTyped(KeyEvent.VK_P))
		{
			GameStatePaused paused = (GameStatePaused)getGameStateManager().getGameState(GameStateManager.PAUSED);
			Image screenshot = currentStage.getScreenShot();
			paused.setScreenshot(screenshot);
			getGameStateManager().setState(GameStateManager.PAUSED);
		}
		
		
	}


	//Sets if the game is muted
	public void setMuted(boolean muted) 
	{
		currentStage.setMuted(muted);
	}
	
	//Sets difficulty level
	public void setDifficulty(GameStateManager.Difficulty difficulty)
	{
		currentStage.setDifficulty(difficulty);
	}
	
	//Transitions to the current stage
	public void transitionToStage()
	{
		getGameStateManager().transition("Stage " + (stageNumber+1) +" - " + currentStage.getName(), 2000, GameStateManager.RUNNING);
	}
	
	//Creates an instance of this game state from a file
	public static GameStateRunning createFromFile(String menuFile, String stageLoaderFilename, 
									GameStateManager gameStateManager, Graphics g, Dimension window)
	{
		GameStateMenu menu = GameStateMenu.createFromGameFileContents(GameFileReader.readGameFile(menuFile), g, window, gameStateManager);
		
		return new GameStateRunning(stageLoaderFilename, menu.getLabels(), menu.getButtons(), gameStateManager, g, window);
		
	}
}
