package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/*
 * The class that handles the changing of the wallpaper.
 * We use the SPI interface to make use of the Windows API.
 * The SPI (SystemParametersInfo) function lies inside user32.dll and allows you to set or retrieve hardware and configuration information from your system. 
 */
public class Switcheroo
{	
	private final static Logger LOGGER = Logger.getLogger(Switcheroo.class.getName());
	
	public Switcheroo(File f)
	{
		LOGGER.info("ENTERED FUNCTION Switcheroo()");
		LOGGER.info("Switching to wallpaper: " + f.getAbsolutePath());
		
		//Apparently only jpg works, so if it's not a jpg, we must convert it.
		//At the end we delete the new file.
		boolean rightFormat;
		
		if(!f.getName().endsWith(".jpg"))
		{		
			rightFormat = false;
			try
			{
				BufferedImage img = ImageIO.read(f);
				
				//http://stackoverflow.com/questions/2290336/converting-png-into-jpeg
				BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				newImage.createGraphics().drawImage(img, 0, 0, Color.BLACK, null);
				
				ImageIO.write(newImage, "jpg", f = new File("transcoded.jpg"));
				System.out.println("Wallpaper converted to: " + f.getAbsolutePath());
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
			rightFormat = true;
		
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "Wallpaper", f.getAbsolutePath());
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "WallpaperStyle", "6");
		Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "TileWallpaper", "0");
		
		//Make the call to change the wallpaper
		boolean result = SPI.INSTANCE.SystemParametersInfoA
		(
				SPI.SPI_SETDESKWALLPAPER, 
				0, 
				f.getAbsolutePath(),
				SPI.SPIF_UPDATEINIFILE | SPI.SPIF_SENDWININICHANGE
		);	
		
		if(!rightFormat)
		{
			// Attempt to delete it
		    if (! f.delete())
		      throw new IllegalArgumentException("Delete: deletion failed");
		    else
		    	System.out.println("File deleted");
		}
		System.out.println("Refresh desktop result: " + result); 

	}//END OF FUNCTION Switcheroo()


}//END OF CLASS Switcheroo
