package main;

import java.util.logging.Logger;

import javax.swing.UIManager;

public class Main
{
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args)
	{
		LOGGER.info("ENTERED FUNCTION main()");
		
		//Set the look and feel of the program to be the same as system default
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		new GUI();
	}

}
