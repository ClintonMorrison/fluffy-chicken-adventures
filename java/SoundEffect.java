import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
 * Author: Clinton Morrison
 * Date: May 19, 2013
 */


/*
 * This class describes a sound effect. It is capable of loading the sound 
 * effect and controlling how it is played back.
 * 
 * Specifically this class loads sound clips and plays them. It is intended only to be used
 * for short sound effects that need to be played once only. Not for background music.
 * A different class will play background music.
 */

public class SoundEffect
{
	//Attributes
	//private LinkedList<AudioClip> sounds; 
	private boolean muted;
	private String filename;
	private URL audioURL;
	private int soundToPlayIndex;
	
	//Constructor
	public SoundEffect(String filename)
	{
		this.filename = filename;
		//sounds = new LinkedList<AudioClip>();
		audioURL = this.getClass().getResource(filename);
		//sounds.add(Applet.newAudioClip(audioURL));
		//sounds.add(Applet.newAudioClip(audioURL));
		//sounds.add(Applet.newAudioClip(audioURL));

		soundToPlayIndex = 0;
	}
	
	//Sets if sound is muted
	public void setMuted(boolean muted)
	{
		this.muted = muted;
	}
	
	//Play the sound once (in a new thread)
	public void play()
	{
		update();
		
		if(!muted)
		{
			new Thread(new Runnable() {
				public void run() 
				{
					AudioInputStream input = null;
					Clip sound = null;
					
					try 
					{
						input = AudioSystem.getAudioInputStream(audioURL);
						sound = AudioSystem.getClip();
						sound.open(input);
						sound.start();
						
						try {
							Thread.sleep(3500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						while(sound.isActive() || sound.isRunning())
						{
							try {
								Thread.sleep(5);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						input.close();
						sound.close();
					} 
					catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						try {
							if(input != null)
								input.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						if(sound != null)
							sound.close();
					}
					
				} }).start();
		}
	}
	
	//Updates sound effect (used to determine if any lines need to be closed)
	public void update()
	{
		
	}
	
}


