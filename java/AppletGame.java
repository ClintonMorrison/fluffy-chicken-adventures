import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 */


/*
 * This abstract class describes a base for an applet that runs a game. 
 * The actual game extends this class.
 * This class handles the double buffering and basic initialization of the game
 * It also controls a basic game loop which regulates frame speed.
 * 
 * It provides a simple interface of several functions which can be used to control the game.
 * Applets which extend the class need only define gameStart(), gameUpdate(), drawGame(), and gameStop()
 */

public abstract class AppletGame extends Applet implements Runnable, MouseListener, KeyListener 
{
	Dimension window;
	Image buffer; //buffer to render game to
	long lastSystemMillis;  //Milliseconds since last update
	//long timeBetweenUpdates = 15; //How much time should be thread sleep between updates (ideally)
	int ticks;  //Total number of updates
	boolean RUNNING;
	UserInput currentUserInput; //A snapshot of user input
	
	//Called when initializing the applet
	public void init()
	{
		RUNNING = true;
		
		//Note - Only set applet size if it is to be run in applet viewer
		//this.setSize(new Dimension(950, 500)); 
		
		window = new Dimension(this.getWidth(), this.getHeight()); //Get screen dimensions
		setBackground( Color.BLACK );
		addMouseListener(this); //Add mouse listener to applet
		addKeyListener(this); //Add key listener to applet
	}
	
	//Called when applet is ready to launch game
	public abstract void gameStart(); 
	
	//Called when the applet starts
	public void start()
	{
		currentUserInput = new UserInput();
		gameStart();
		Thread t = new Thread(this);
		t.start();
		lastSystemMillis = System.currentTimeMillis();
	}
	
	//Causes thread to sleep (returns false if machine running too slow for ideal frame rate)
	 public void gameSleep(long timeTaken) 
	   {
		   try 
		   {
			    Thread.sleep(0);   
		   }
		   catch (InterruptedException e) { e.printStackTrace();}		   
		   lastSystemMillis = System.currentTimeMillis(); 
	   }
	
	
	//Describes how the game should be updated
	public abstract void gameUpdate(long millisTaken, UserInput currentUserInput);
	
	 //Loop which runs while game is running
	public void gameLoop()
	{
	   draw(); //Render and draw game to screen
	   
	   Vector mousePosition = new Vector();
	   
	   if(this.getMousePosition() != null)
	   {
		   try
		   {
			   currentUserInput.setMousePosition(this.getMousePosition());
		   }
		   catch (NullPointerException e)
		   { e.printStackTrace(); }
	   }
		else
			mousePosition = new Vector();
	   
	   //Determine time since last update
	   if(lastSystemMillis == 0)
		   lastSystemMillis = System.currentTimeMillis();
	   
	   long time_taken = System.currentTimeMillis() -  lastSystemMillis;
	   
	   //Update the game
	   if(time_taken < 50)
		   gameUpdate(time_taken, currentUserInput);
	   else
		   gameUpdate(50, currentUserInput);
	   
	   currentUserInput.reset(); //Reset input stored to avoid double counting key presses and mouse clicks

	   
	   
	   //Sleep 
	   gameSleep(time_taken);
	}
	
	//This function is called by the thread created in the start function
	public void run() 
	{		
		while(RUNNING)
		{
			gameLoop();
		}
		
	}
	
	public abstract void gameStop();
	
	//Called when thread stopped
	public void stop()
	{
		RUNNING = false;
		gameStop();
	}
	
	//Method describing how to draw the game
	public abstract void drawGame(Graphics g);
	
	//Renders game to buffer image
	public void render()
	{
		buffer = createImage(window.width, window.height);
		if (buffer != null)
		{
		   Graphics g = buffer.getGraphics();
		   buffer.getGraphics();	
		   g.setColor(Color.BLACK);
		   g.fillRect(0, 0, window.width, window.height);
		   
		   drawGame(g); //Draw the game
		   
		   g.dispose();
		 }	   
	}

	//Draws the rendered game to the applet
   public void draw()
   {      
	   render(); //Render the game
	   Graphics g = this.getGraphics();
	   g.drawImage(buffer, 0, 0, null); //Draw buffered image
   }
   
   
   
   
   //The below methods hand mouse and keyboard input
   //This input is packaged into a UserInput object which is sent to the applet which extends this class
   
   @Override
	public void mouseClicked(MouseEvent e) 
   {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		currentUserInput.mouseClick(e.getPoint());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		currentUserInput.keyDown(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{		
		currentUserInput.keyUp(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
   
   
   
}
