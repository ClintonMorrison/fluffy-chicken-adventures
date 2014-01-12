import java.awt.Dimension;
import java.util.ArrayList;

/*
 * Author: Clinton Morrison
 * Date: June 2, 2013
 * 
 * 
 * This class is used to automatically
 * load stages.
 */

public class StageLoader 
{
	//Attributes
	private ArrayList<String> stagePaths;
	
	//Constructor
	public StageLoader(String filename)
	{
		stagePaths = new ArrayList<String>();
		GameFileContents contents = GameFileReader.readGameFile(filename);
		
		int i = 1;
		while(contents.containsAttribute("Stage" + i))
		{
			stagePaths.add(contents.getValue("Stage" + i));
			i++;
		}
		
	}
	
	//Loads the i-th stage
	public Stage loadStage(int i, Dimension window, boolean muted, GameStateManager.Difficulty difficulty)
	{
		if(i < stagePaths.size())
		{
			return Stage.createStageFromFile(stagePaths.get(i), window, muted, difficulty);
		}
		else
			return null;
	}
	
	//Get how many stages there are
	public int getStageCount()
	{
		return stagePaths.size();
	}

}
