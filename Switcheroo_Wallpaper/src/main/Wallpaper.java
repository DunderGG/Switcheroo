package main;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class Wallpaper extends File
{
	private final static Logger LOGGER = Logger.getLogger(Wallpaper.class.getName());
	
	String path;
	
	//UUID wall_id = UUID.randomUUID();
	
	
	public Wallpaper(String filePath)
	{
		super(filePath);
		
		this.path = filePath;		
		//System.out.println("Creating new Wallpaper: " + path);
	}
	
	public String toString()
	{
		return path;
	}
	
}//END OF CLASS Wallpaper

