import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

/*
 * Author: Clinton Morrison
 * Date: May 10, 2013
 * 
 * This class is used to read image files.
 */

public class ImageReader {
	
	//Attributes
	private String filePath; 
	private InputStream in;
	private Image img;
	
	//Constructor
	public ImageReader(String filePath)
	{
		this.filePath = filePath;
		this.in = getClass().getResourceAsStream(filePath);
		
		try {
			this.img = ImageIO.read(this.in);
		} catch (IOException e) {
			System.out.println("Error! ImageReader failed to read " + filePath);
			e.printStackTrace();
		}
	}
	
	//Gets image
	public Image getImage() {
		return this.img;
	}
	
	//Loads image
	public static Image loadImage(String filePath) {
		ImageReader imageReader = new ImageReader(filePath);
		return imageReader.getImage();
	}
	


}
