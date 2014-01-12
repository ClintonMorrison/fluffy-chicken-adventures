import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.net.URL;

import javax.swing.ImageIcon;
/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This is the code which describes the main applet which runs the game.
 */

public class FluffyChickenGame extends AppletGame
{
	//Variables
	Stage level1;
	GameStateManager gameStateManager;
	
	//Called when the game starts (loading code here)
	public void gameStart() 
	{
		gameStateManager = new GameStateManager("/resources/objects/stages/stages.txt", this.getGraphics(), window);	
	}

	//Updates the state of the game
	public void gameUpdate(long millisTaken, UserInput currentUserInput) 
	{
		gameStateManager.update(millisTaken, currentUserInput);
	}		

	//Destroys the game when applet is closed
	public void gameStop() {
		if(gameStateManager.musicIsOn())
			gameStateManager.toggleMusic();
		
		System.out.println("Game stopping - gameStop() was called");
		System.exit(0);
	}

	//Renders game to be drawn to applet
	public void drawGame(Graphics g) 
	{	
		gameStateManager.draw(g);
		g.dispose();
	}
	
}
