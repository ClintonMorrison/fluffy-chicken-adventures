import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This is the main class which manages which game state is currently running
 */

public class GameStateManager implements DrawableEntity
{
	//Attributes
	private GameState[] states;
	private int currentState;
	private Difficulty difficulty;
	private boolean soundOn;
	private boolean musicOn;
	private boolean gameStarted; //Should the game resume when player presses play button?
	private String transitionFont;
	private Graphics g;
	private Dimension window;
	private String stageLoaderFilename;
	private AudioClip backgroundMusic;
	private boolean finishedLoading;
	
	//Index related to game state
	static final int LOADING = 0;
	static final int MENU_MAIN = 1;
	static final int MENU_INSTRUCT = 2;
	static final int MENU_SETTINGS = 3;
	static final int MENU_CREDITS = 4;
	static final int RUNNING = 5;
	static final int PAUSED = 6; 
	static final int TRANSITION = 7;
	//Enumeration which stores difficulty
	public enum Difficulty { EASY, MEDIUM, HARD };
	
	
	//Constructor
	public GameStateManager(String stageLoaderFilename, Graphics g, Dimension window)
	{
		finishedLoading = false;
		this.window = window;
		this.g = g;
		soundOn = true; 
		musicOn = true; 
		difficulty = Difficulty.MEDIUM;
		this.stageLoaderFilename = stageLoaderFilename;
		transitionFont = "/resources/objects/font_title.txt";
		gameStarted = false;
		states = new GameState[8];
		states[LOADING] =  GameStateMenuLoading.createFromFile("/resources/objects/menu_loading.txt", g, window, this);
		
		currentState = LOADING; 
		URL audioURL = this.getClass().getResource("/resources/sounds/music1.wav");
		backgroundMusic = Applet.newAudioClip(audioURL);
		backgroundMusic.loop();
	}
	
	//Initialize states (other than loading state)
	public void initilizeStates()
	{
		states[RUNNING] =  GameStateRunning.createFromFile("/resources/objects/menu_running.txt", stageLoaderFilename, this, g, window);//GameStateRunning(this, stageLoaderFilename, window);
		states[MENU_MAIN] = GameStateMainMenu.createFromFile("/resources/objects/menu_main.txt", g, window, this);//new GameStateMainMenu("Fluffy Chicken Adventures", "/resources/objects/font_title.txt", "/resources/objects/button1.txt", this, g, window);
		states[MENU_SETTINGS] = GameStateMenuSettings.createFromFile("/resources/objects/menu_settings.txt", g, window, this);
		states[MENU_CREDITS] = GameStateMenuInfo.createFromFile("/resources/objects/menu_credits.txt", g, window, this);
		states[MENU_INSTRUCT] = GameStateMenuInfo.createFromFile("/resources/objects/menu_instructions.txt", g, window, this);
		states[PAUSED] = GameStatePaused.createFromFile(null, "/resources/objects/menu_paused.txt", this, g, window);
		toggleDifficulty();
		toggleDifficulty(); //Set to "EASY"
		finishedLoading = true;
	}
	
	//Switches to a transition state
	public void transition(String text, long delay, int targetState)
	{
		states[TRANSITION] = new GameStateTransition(text, transitionFont, this, g, window, delay, targetState);
		currentState = TRANSITION;
	}
	
	//Gets if finished loading
	public boolean finishedLoading()
	{
		return finishedLoading;
	}
	
	//Draws the game
	public void draw(Graphics g) 
	{
		states[currentState].draw(g);
		
	}
	
	//Updates the game
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		states[currentState].update(millisPassed, currentUserInput);
	}
	
	//Sets the game state
	public void setState(int i)
	{
		if(states[i] != null)
			currentState = i;
		else
			System.out.println("Error, could not switch state. State " + i + " not initialized.");
	}
	
	//Gets if sound is on
	public boolean soundIsOn()
	{
		return soundOn;
	}
	
	//Sets sound on or off toggle
	public void toggleSound()
	{
		soundOn = ! soundOn;
		
		//Update muted status of all game states
		for(int i = 0; i < states.length; i++)
		{
			if(states[i] != null)
				states[i].setMuted(!soundOn);
		}
		
	}
	
	//Determines if a game has been started by the user yet
	public boolean isGameStarted()
	{
		return this.gameStarted;
	}
	
	//Sets if game has been started by the user
	public void setGameStarted(boolean gameStarted)
	{
		this.gameStarted = gameStarted;
	}
	
	//Gets if music is on
	public boolean musicIsOn()
	{
		return musicOn;
	}
	
	//Sets sound on or off toggle
	public void toggleMusic()
	{
		musicOn = ! musicOn;
		
		if(musicOn)
			backgroundMusic.loop();
		else
			backgroundMusic.stop();
		
		if(musicOn)
			backgroundMusic.loop();
		else
			backgroundMusic.stop();
	}
	
	//Toggles the difficulty level of the game
	public void toggleDifficulty()
	{
		if(difficulty == Difficulty.EASY)
			difficulty = Difficulty.MEDIUM;
		else if(difficulty == Difficulty.MEDIUM)
			difficulty = Difficulty.HARD;
		else if(difficulty == Difficulty.HARD)
			difficulty = Difficulty.EASY;
		
		GameStateRunning r = (GameStateRunning)states[RUNNING];
		r.setDifficulty(difficulty);
	}
	
	//Gets difficulty level of the game
	public Difficulty getDifficulty()
	{
		return difficulty;
	}
	
	//Gets a state of the game
	public GameState getGameState(int i)
	{
		return states[i];
	}

}
