/*
 * Author: Clinton Morrison
 * Date: May 9, 2013
 * 
 * This class describes an animation sequence.
 * This will be the building block of all animated objects that exist in the game
 */

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Animation {
	
	//Attributes
	private ArrayList<Image> images;  //All images (in order) needed for animation
	private ArrayList<Long> frameTimes;  //How long each image (in order) should be shown for animation
	private int frameCount;  //How many frames are there?
	private long timeElapsedOnCurrentFrame;  //How long has the current frame been shown?
	private int currentFrameIndex;  //The index of the current frame
	private boolean looping;  //Does this animation loop? (Otherwise it terminates and freezes on last frame)
	private boolean finished; //Becomes true when terminating animation finishs
	
	//Constructor
	public Animation(ArrayList<Image> images, ArrayList<Long> frameTimes, boolean looping)
	{
		this.images = images;
		this.images.trimToSize();
		this.frameTimes = frameTimes;
		this.frameTimes.trimToSize();
		
		if(images.size() != frameTimes.size())
			System.out.println("WARNING: There are not the same number of images and frame times");
		
		frameCount = Math.min(images.size(), frameTimes.size());
		
		timeElapsedOnCurrentFrame = 0;
		this.looping = looping;
		finished = false;
	}
	
	//Updates the animation
	public void update(Long millisTaken)
	{
		timeElapsedOnCurrentFrame += millisTaken;
		
		while(timeElapsedOnCurrentFrame > frameTimes.get(currentFrameIndex))
		{
			timeElapsedOnCurrentFrame -= frameTimes.get(currentFrameIndex);
			currentFrameIndex ++;
			
			if(currentFrameIndex > frameCount - 1)
			{
				if(looping)
					currentFrameIndex = 0;
				else
				{
					currentFrameIndex = frameCount - 1;
					finished = true;
				}
			}
		}
	}

	//Resets the animation from the first frame
	public void reset()
	{
		currentFrameIndex = 0;
		timeElapsedOnCurrentFrame = 0;
	}
	
	//Gets the current frame
	public Image getCurrentFrame()
	{
		return images.get(currentFrameIndex);
	}
	
	//Get if animation is finished
	public boolean getFinished()
	{
		return finished;
	}
	
	//Creates the animation from the contents of a game object file
	public static Animation createFromGamefileContents(GameFileContents contents)
	{
		ArrayList<Image> images = new ArrayList<Image>();
		ArrayList<Long> frameTimes = new ArrayList<Long>();
		boolean looping = Boolean.parseBoolean(contents.getValue("Loops"));
		
		int frameCount = 1;
		boolean moreFramesToAdd = true; //Are there still frames defined in text file to add?
		
		while(moreFramesToAdd)
		{
			if(contents.containsAttribute("Frame"+frameCount+"_Image") && contents.containsAttribute("Frame"+frameCount+"_Time"))
			{
				try
				{
					Image img = ImageReader.loadImage(contents.getValue("Frame"+frameCount+"_Image"));
					images.add(img); //Add image
				}
				catch (Exception e)
				{
					System.out.println("Error! Problem loading image:");
					e.printStackTrace();
				}
				
				
				//Load and add frame time
				frameTimes.add(Long.parseLong(contents.getValue("Frame"+frameCount+"_Time")));
				
				//Look for next frame
				frameCount++;
			}
			else //Otherwise if the next frame/image isn't defined
				moreFramesToAdd = false;
		}
		return new Animation(images, frameTimes, looping);
	}
	
	//Get the dimensions of the object
	public Dimension getDimensions()
	{
		ImageIcon frame = new ImageIcon(getCurrentFrame()); //Create image icon to find width and height of current frame	
		return new Dimension(frame.getIconWidth(), frame.getIconHeight());  //Return dimensions
	}
	
	//Copies this object
	public Animation copy()
	{
		return new Animation(images, frameTimes, looping);
	}
	

}
