import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/*
 * Author: Clinton Morrison
 * Date: May 17, 2013
 * 
 * This class describes a template for a menu in the game
 */
public class GameStateMenu extends GameState {
	
	//Attributes
	private Dimension window;

	LinkedList<GameButton> buttons;
	LinkedList<GameLabel> labels;
	
	//Constructor
	public GameStateMenu(LinkedList<GameLabel> labels, LinkedList<GameButton> buttons, GameStateManager gameStateManager, Graphics g, Dimension window)
	{
		this.window = window;
		setGameStateManager(gameStateManager);
		this.labels = labels;
		this.buttons = buttons;
	}
	
	//Sets if menu is muted
	public void setMuted(boolean muted)
	{
		for(int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).setMuted(muted);
		}
	}
	
	
	//Draws the menu
	public void draw(Graphics g) 
	{	
		for(int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).draw(g);
		}
		
		for(int i = 0; i < labels.size(); i++)
		{
			labels.get(i).draw(g);
		}
	}

	//Updates the menu
	public void update(Long millisPassed, UserInput currentUserInput) 
	{
		for(int i = 0; i < labels.size(); i++)
		{
			labels.get(i).update(millisPassed, currentUserInput);
		}
		
		
		for(int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).update(millisPassed, currentUserInput);
		}
	}
	
	//Creates a menu from a file
	public static GameStateMenu createFromGameFileContents(GameFileContents contents, Graphics g, Dimension window, GameStateManager gameStateManager)
	{
		LinkedList<GameButton> buttons = new LinkedList<GameButton>();
		LinkedList<GameLabel> labels = new LinkedList<GameLabel>();
		
		int i = 1;
		while(contents.containsAttribute("Button" + i + "_Text"))
		{
			String text = contents.getValue("Button" + i + "_Text");
			ButtonType buttonType = ButtonType.createFromGamefileContents(GameFileReader.readGameFile(contents.getValue("Button" + i + "_Type")));
			double percent_x = Double.parseDouble(contents.getValue("Button" + i + "_Position_X"));
			double percent_y = Double.parseDouble(contents.getValue("Button" + i + "_Position_Y"));
			Vector position = new Vector((int) (percent_x * window.width), (int) (percent_y * window.height));
			buttons.add(new GameButton("button", g, position, text, buttonType));
			i++;
		}
		
		i = 1;
		while(contents.containsAttribute("Label" + i + "_Text"))
		{
			String text = contents.getValue("Label" + i + "_Text");
			GameFont font = GameFont.createFromGamefileContents(GameFileReader.readGameFile(contents.getValue("Label" + i + "_Font")));
			double percent_x = Double.parseDouble(contents.getValue("Label" + i + "_Position_X"));
			double percent_y = Double.parseDouble(contents.getValue("Label" + i + "_Position_Y"));
			Vector position = new Vector((int) (percent_x * window.width), (int) (percent_y * window.height));
			labels.add(new GameLabel("label", g, new Rectangle((int)position.x - 1, (int)position.y - 1, 2, 2), text, font));
			i++;
		}
		
		return new GameStateMenu(labels, buttons, gameStateManager, g, window);
	}
	
	//Gets the window the menu is drawn in
	public Dimension getWindow()
	{
		return window;
	}
	
	//Gets the buttons of the menu
	public LinkedList<GameButton> getButtons()
	{
		return buttons;
	}
	
	//Gets the labels of the menu
	public LinkedList<GameLabel> getLabels()
	{
		return labels;
	}
	
}
