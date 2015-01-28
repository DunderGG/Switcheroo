package main;

import java.io.File;
import java.util.UUID;
import java.util.logging.Logger;

public class Wallpaper extends File
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private final static Logger LOGGER = Logger.getLogger(Wallpaper.class.getName());
	
	
	String path;
	
	UUID wall_id = UUID.randomUUID();
	
	public boolean isFavorite = false;
	public String helloWorld = "HellO!"; 
	
	public Wallpaper(String filePath)
	{
		super(filePath);
		
		LOGGER.info("ENTERED FUNCTION Wallpaper()");
		
		this.path = filePath;		
		//System.out.println("Creating new Wallpaper: " + path);
	}
	
	public boolean isFavorite()
	{
		return isFavorite;
	}
	
	public String toString()
	{
		
		return path;
	}
	
	

	
}//END OF CLASS Wallpaper

