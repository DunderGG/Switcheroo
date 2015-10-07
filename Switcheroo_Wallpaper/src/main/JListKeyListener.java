package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;
/*
 * Handles all the keyboard interaction with the wallpaper list.
 * Enter sets the wallpaper, up and down arrow keys changes the preview
 * 
 */
public class JListKeyListener extends KeyAdapter
{
	JList<Wallpaper> jListOfWalls = FileList.fileList;
	Wallpaper clickedFile;
	
	GUI gui;
	public JListKeyListener(GUI gui)
	{
		this.gui = gui;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			System.out.println("enter pressed");
			
			//Don't crash if we click on a list with no Files in it, just return
			if(jListOfWalls.getSelectedIndex() >= 0)
				clickedFile = (Wallpaper) jListOfWalls.getModel().getElementAt(jListOfWalls.getSelectedIndex());
			else 
				return;
			
			new Switcheroo(clickedFile);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		//Don't crash if we click on a list with no Files in it, just return
		if(jListOfWalls.getSelectedIndex() >= 0)
			clickedFile = (Wallpaper) jListOfWalls.getModel().getElementAt(jListOfWalls.getSelectedIndex());
		else 
			return;
		
		//Change the preview to the selected File
		gui.changePreview(clickedFile);
		gui.updateDataContainer(clickedFile);
	}

}//END OF CLASS JListKeyListener
