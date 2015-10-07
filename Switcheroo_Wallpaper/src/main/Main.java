package main;

import javax.swing.UIManager;

public class Main
{	
	public static void main(String[] args)
	{
		//Set the look and feel of the program to be the same as system default
		try
		{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (Exception e)
		{e.printStackTrace();}

		new GUI();
	}

}
