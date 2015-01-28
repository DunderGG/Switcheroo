package main;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.JList;


public class JListMouseListener extends MouseAdapter
{
	private final static Logger LOGGER = Logger.getLogger(JListMouseListener.class.getName());
	
	private int index = 0;

	GUI gui;
	JList<Wallpaper> jListOfWalls = FileList.fileList;
	Wallpaper clickedFile;
	
	public JListMouseListener(GUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{ 		
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{	
		Rectangle bounds = jListOfWalls.getCellBounds(0, jListOfWalls.getModel().getSize() - 1);
		if(bounds != null)
		{
			if(bounds.contains(e.getPoint()))
			{
				if(jListOfWalls.getSelectedIndex() >= 0)
					clickedFile = (Wallpaper) jListOfWalls.getModel().getElementAt(jListOfWalls.getSelectedIndex());
				else
					return;
				
				imageDataHandler.updateDataContainer(clickedFile);
			}
			else
			{
				jListOfWalls.clearSelection();
				jListOfWalls.getSelectionModel().setAnchorSelectionIndex(index);
				jListOfWalls.getSelectionModel().setLeadSelectionIndex(index);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{		
		
		Rectangle bounds = jListOfWalls.getCellBounds(0, jListOfWalls.getModel().getSize() - 1);
		
		if(bounds != null)
		{
			if(bounds.contains(e.getPoint()))
			{
				if(jListOfWalls.getSelectedIndex() >= 0)
				{
					clickedFile = (Wallpaper) jListOfWalls.getModel().getElementAt(jListOfWalls.getSelectedIndex());;
				}
				else
					return;
				
				//Change the preview to the selected File
				gui.changePreview(clickedFile);
				
				//When double clicking listitem, set the currently selected File
				if(e.getClickCount() == 2)
				{
					new Switcheroo(clickedFile);
				}
			}
			else
			{
				jListOfWalls.clearSelection();
				jListOfWalls.getSelectionModel().setAnchorSelectionIndex(index);
				jListOfWalls.getSelectionModel().setLeadSelectionIndex(index);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		/*
		 * When dragging mouse in whitespace beneath last item in list, the last item gets selected until mouse button is released. 
		 * DONT KNOW HOW TO FIX
		 */
		super.mouseDragged(e);		
		LOGGER.info("mouseDragged");
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

}
