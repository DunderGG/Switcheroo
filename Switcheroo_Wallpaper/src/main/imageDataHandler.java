package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

/*
 * Contains helper-functions for wallpaper data, 
 * such as getting size or aspect ratio,
 * and updating the data fields in the GUI.
 * 
 */
public class imageDataHandler
{	
	static File currentFile = null;
	
	static String aspectRatio = null;
	static String resolution = null;
	static String name = null;
	
	static boolean isFavorite = false;
	
	public static GUI gui;
	
	public imageDataHandler(Wallpaper f, GUI gui)
	{
		imageDataHandler.currentFile = f;
		
		imageDataHandler.aspectRatio = getAspect(f);
		imageDataHandler.resolution = getResolution(f);
		imageDataHandler.name = getName(f);
		
		this.gui = gui;
	}

	public static String getName(Wallpaper f)
	{
		if(f == null)
			return "No file selected";
		
		return f.getName();
	}
	
	/*
	 * Returns a formatted String with the File's resolution
	 */
	public static String getResolution(Wallpaper f)
	{
		if(f == null)
			return "No file selected";
		
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(new File(f.getAbsolutePath()));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return img.getWidth() + "x" + img.getHeight();
	}

	/*
	 * Returns a formatted String with the File's aspect ratio
	 */
	public static String getAspect(Wallpaper f)
	{
		if(f == null)
			return "No file selected";
		
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(f);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int r = getGCD(img.getWidth(), img.getHeight());
		
		String ar = img.getWidth()/r + ":" + img.getHeight()/r; 
		
		return ar.equals("8:5") ? "16:10" : ar;
	}

	/*
	 * This method will take the image's width and height
	 * and return the Greatest Common Divisor
	 * 
	 * BigInteger has the gcd() function which does this for us
	 * We convert back to int before returning
	 */
	private static int getGCD(int a, int b)
	{
		return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
	}

}
