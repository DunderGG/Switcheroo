package main;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;


public interface SPI extends StdCallLibrary
{
	//From MSDN article: http://channel9.msdn.com/coding4fun/articles/Setting-Wallpaper
	int SPI_SETDESKWALLPAPER = 0x14;
	int SPIF_UPDATEINIFILE = 0x01;
	int SPIF_SENDWININICHANGE = 0x02;
	
	SPI INSTANCE = (SPI) Native.loadLibrary("user32", SPI.class);
	
	boolean SystemParametersInfoA
	(
			int uiAction,
			int uiParam,
			String pvParam,
			int fWinIni
	);
}//END OF INTERFACE SPI
