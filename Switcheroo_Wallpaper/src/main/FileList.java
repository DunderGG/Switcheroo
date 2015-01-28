package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.DecimalFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;

public class FileList
{
	public static JList<Wallpaper> fileList;
	
	public File wallFolder;
	public static Wallpaper[] wallArray;
	
	public String listTotalSize;
	public String listNrOfItems;
	
	public String currentPath;
	
	private GUI gui;
	
	public FileList(GUI gui)
	{
		this.gui = gui;
	}
	
	public JScrollPane getScrollPane(GUI gui)
	{
		JScrollPane scroll;
		
		/*
		 * FROM: http://stackoverflow.com/questions/7222161/jlist-that-contains-the-list-of-files-in-a-directory
		 */
		currentPath = System.getProperty("user.home")+"\\Pictures";
		wallFolder = new File(currentPath);
				
		final String[] nameArray = wallFolder.list(new TextFileFilter2());
		
		//Create the Wallpaper array and set its size to the same as the number of files in the wallFolder
		wallArray = new Wallpaper[nameArray.length];
		
		for(int i = 0; i<nameArray.length; i++)
			wallArray[i] = new Wallpaper(new File(wallFolder, nameArray[i]).getAbsolutePath());
		
		//Put File objects in the list
		fileList = new JList<Wallpaper>(wallArray);
		
		//...then use a renderer
		fileList.setCellRenderer(new FileRenderer());
		
		//Calculate the number of items and the size
		listNrOfItems = Integer.toString(fileList.getModel().getSize());
		listTotalSize = getListSize();
		
		//Attach our custom JListMouseListener to listen for mouse events
		JListMouseListener mListen = new JListMouseListener(gui); 		
		fileList.addMouseListener(mListen);
		
		//Attach our custom JListKeyListener to listen for keyboard events
		JListKeyListener kListen = new JListKeyListener(gui);
		fileList.addKeyListener(kListen);
		
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL);
		fileList.setVisibleRowCount(-1);
		fileList.setFont(gui.getFont("TitilliumWeb-Light.ttf", 10f));
		scroll = new JScrollPane(fileList);
		scroll.setPreferredSize(new Dimension(200, gui.getContentPane().getHeight()));
		
		return scroll;
	}
	
	public void updateListData(Wallpaper wallFile)
	{
		final String[] nameArray = wallFile.list(new TextFileFilter2());
		wallArray = new Wallpaper[nameArray.length];
		for(int i = 0; i<nameArray.length; i++)
			wallArray[i] = new Wallpaper(new File(wallFile, nameArray[i]).getAbsolutePath());

		fileList.setListData(wallArray);
		fileList.revalidate();
		fileList.repaint();
	}
	
	public void updateListInfo(Wallpaper wallFile)
	{
		listNrOfItems = Integer.toString(fileList.getModel().getSize());
		currentPath = wallFile.path;
		
		gui.listNrVal.setText("Nr. of files: " + listNrOfItems);
		gui.listSizeVal.setText("Combined size: " + getListSize());
		gui.listPathVal.setText("Current directory: " + currentPath);
	}
	
	public String getListSize()
	{
		double size = 0;
		DecimalFormat df = new DecimalFormat("#.00");
		for(File f : wallArray)
		{
			size += f.length();
		}
		int i = 0;
		while(size > 1000)
		{			
			size = (size/1024);
			i++;
		}
		
		String prefix = "";
		
		switch(i)
		{
		case 1:
			prefix = "K";
			break;
		case 2:
			prefix = "M";
			break;
		case 3:
			prefix = "G";
			break;
		case 4:
			prefix = "T";
			break;
		default:
			prefix = "";
			break;
		}
		
		return df.format(size) + " " + prefix + "B";
	}
}

class FileRenderer extends DefaultListCellRenderer
{
	private static final long serialVersionUID = 1L;

	public FileRenderer()
	{
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		
		for(Wallpaper wall : FileList.wallArray)
		{
			if(value.equals(wall.getPath()))
			{
				if(wall.isFavorite)
					setBackground(Color.GREEN);
			}
		}
		
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		File f = (File) value;
		
		JLabel l = (JLabel) c;
		l.setText(f.getName());
		l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));
		
		return l;
	}
	
	
}

class TextFileFilter implements FileFilter
{
	public boolean accept(File file)
	{
		//Implement the logic to select files here
		
		String fileName = file.getName();
		
		return fileName.endsWith(".png") || fileName.endsWith(".jpg");
	}
}

class TextFileFilter2 implements FilenameFilter
{
	public boolean accept(File file)
	{
		//Implement the logic to select files here
		
		String fileName = file.getName();
		
		return fileName.endsWith(".png") || fileName.endsWith(".jpg");
	}

	@Override
	public boolean accept(File dir, String name)
	{
		return name.endsWith(".png") || name.endsWith(".jpg");
	}
}
















