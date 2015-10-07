package main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.imgscalr.Scalr;

public class GUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(GUI.class.getName());
	
	private imageDataHandler imgDataHandler;
	
	public ImageIcon wallPreview;
	FileList fl = new FileList(this);
	
	private String newDir;
	
	public Wallpaper previewFile;
	
	JScrollPane scroll;
	JLabel imageLabel;	
	
	public JTextArea imgNameVal;
	public JTextArea imgRatioVal;
	public JTextArea imgResVal;
	public JTextArea imgIsFavorite;
	
	public JTextArea listNrVal;
	public JTextArea listSizeVal;
	public JTextArea listPathVal;
	
	JLabel lblListInfo;
	JLabel lblListSize;
	
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	
	Font font;
	Font font2;
	final Container contentPane = getContentPane();	
	public GUI()
	{	
		super("SWITCHEROO");
		

		java.net.URL iconURL = ClassLoader.getSystemResource("res/trayimage2.jpg");
		Image img = new ImageIcon(iconURL).getImage();
		//Image img2 = Toolkit.getDefaultToolkit().createImage(iconURL);
		setIconImage(img);
		
		/*
		 * ADD AN ICON FOR THE SYSTEM TRAY
		 */
		TrayIcon trayIcon = null;
		if(!SystemTray.isSupported())
	        System.out.println("System tray is not supported! ");
		else
		{
			System.out.println("System tray is supported! ");
			
			SystemTray tray = SystemTray.getSystemTray();
			
			//Create the menu itself.
			PopupMenu trayPopup = new PopupMenu();
			
			//1st entry in the popup menu
			MenuItem change = new MenuItem("Change");
			change.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Changing wallpaper, random");
				}
				
			});
			trayPopup.add(change);
			
			//2nd entry in the popup menu
			MenuItem close = new MenuItem("Close");
			close.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Closing");
					
					System.exit(0);
				}
			});
			trayPopup.add(close);
			
			trayIcon = new TrayIcon(img, "Switcheroo", trayPopup);
			trayIcon.setImageAutoSize(true);
			
			trayIcon.addMouseListener(getMouseListener(this));
			
			try
			{
				tray.add(trayIcon);
			} catch (AWTException e1)
			{
				e1.printStackTrace();
			}
		}
		
		contentPane.setBackground(Color.BLACK);	
		
		/*
		 * START OF optionsPanel 
		 */
		JPanel optionsPanel = new JPanel(new GridLayout(1,0));
		
		/*
		 * BUTTON1
		 */
		Font font = getFont("HURTM___.otf", 25f);
		button1 = new JButton("Choose folder");
		button1.setFont(font);
		optionsPanel.add(button1);
		button1.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{						
						JFileChooser chooser = new JFileChooser(newDir);											
						//Disable the "All files" option
						chooser.setAcceptAllFileFilterUsed(false);
						chooser.setDialogTitle("Select a folder");
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						
					    int returnVal = chooser.showOpenDialog(contentPane);					    
					    if(returnVal == JFileChooser.APPROVE_OPTION) 
					    {
					    	//Save the previous value of "currentDir", the folder we were in before changing.
					    	String previousDir = newDir;

					    	//Assign "currentDir" the new value
					    	/*
					    	 * getSelectedFile() returns the selected folder (despite its name),
					    	 * getCurrentDirectory() returns the folder of the selected folder, i.e. the parent folder (so don't use this one for the job)
					    	 */					    	
					    	newDir = chooser.getSelectedFile().toString();
					    	LOGGER.info("\n" + "Previous folder: " + previousDir + "\n" + "New folder: " + newDir + "\n");
					    	
					    	//If the same folder is chosen, do nothing
					    	if(newDir.equals(previousDir))
					    	{
					    		System.out.println("Same folder was chosen");
					    	}					    	
					    	//If another folder was chosen, ask if the list should be updated
					    	else
					    	{
					    		Wallpaper f = new Wallpaper(newDir);
					    		if(f.exists())
					    		{
						    		int n = JOptionPane.showConfirmDialog(
						    			    contentPane,
						    			    "New folder chosen\nWould you like to update the list?",
						    			    "Update list?",
						    			    JOptionPane.YES_NO_OPTION);
						    		
						    		if(n == JOptionPane.YES_OPTION)
						    		{
						    			//User wants to update the list from new folder
						    			LOGGER.info("Updating list from folder: " + newDir + "\n");
						    			//If the chosen folder exists
								    	if(newDir.toString() != null)
								    	{								    										    	
								    		fl.updateListData(f);
								    		fl.updateListInfo(f);
								    	}
								    	else
								    		System.out.println("Folder did not exist");
						    		}
						    		else if(n == JOptionPane.NO_OPTION)
						    		{
						    			//User does not want to update the list
						    			newDir = previousDir;
						    		}
						    	}
					    		else
					    		{
						    		JOptionPane.showMessageDialog(contentPane, "Folder does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
					    		}
					    	}
					    	
					    						    	
					    }
					    else if(returnVal == JFileChooser.CANCEL_OPTION)
					    {
					    	LOGGER.info("User cancelled\n");
					    }
					    else
					    {
					    	LOGGER.info("Unknown error with opening folder\n");
					    }
					}
				});		
		
		/*
		 * BUTTON2
		 */
		button2 = new JButton("Set File");
		button2.setFont(font);
		optionsPanel.add(button2);
		button2.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						LOGGER.info("Selected index: " + Integer.toString(FileList.fileList.getSelectedIndex()) + "\n");
						if(FileList.fileList.getSelectedIndex() >= 0 && FileList.fileList.getSelectedValue() != null)
							new Switcheroo(FileList.fileList.getSelectedValue());
					}
				});
		
		/*
		 * BUTTON3
		 */
		button3 = new JButton("Open in viewer");
		button3.setFont(font);
		optionsPanel.add(button3);
		button3.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						LOGGER.info("Selected index: " + Integer.toString(FileList.fileList.getSelectedIndex()) + "\n");
						
						try
						{
							if(FileList.fileList.getSelectedValue() != null)
								Desktop.getDesktop().open(FileList.fileList.getSelectedValue());
							else
								System.out.println("No item selected");
						} 
						catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
				});
		
		/*
		 * BUTTON4
		 */
		button4 = new JButton("Set favorite");
		button4.setFont(font);
		optionsPanel.add(button4);
		button4.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						LOGGER.info("Selected index: " + Integer.toString(FileList.fileList.getSelectedIndex()) + "\n");
						
						LOGGER.info("Selected value: " + FileList.fileList.getSelectedValue() + "\n");
						int index = FileList.fileList.getSelectedIndex();
						if(index != -1)
						{
							FileList.makeFavorite();
							
							imgIsFavorite.setText("Favorite: Yes");
						}
					}
				});
		//END OF BUTTONS
				
		contentPane.add(optionsPanel, BorderLayout.PAGE_START);
		//END OF optionsPanel 
		
		
		//Create and add the scrollpane with the list of files
		scroll = fl.getScrollPane(this);
		contentPane.add(scroll, BorderLayout.LINE_START);
		newDir = fl.wallFolder.getPath();
		LOGGER.info("Starting point of newDir = " + newDir + "\n");

		/*
		 * START OF imageContainer
		 */
		//String currentWall = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Control Panel\\Desktop", "File");
		
		//Set the default previewFile
		//previewFile = new File(getClass().getResource("default.jpg").getPath());
		//Set the default Image
		wallPreview = new ImageIcon();
		//Apply it to a label
		imageLabel = new JLabel(wallPreview);
		
		//Add the LABEL to the contentpane
		contentPane.add(imageLabel, BorderLayout.CENTER);
		// END OF imageContainer
		
		/*
		 * START OF dataContainer
		 */
		JPanel dataContainer = new JPanel(new GridBagLayout());
		dataContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		contentPane.add(dataContainer, BorderLayout.LINE_END);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx=0;
		//END OF dataContainer
		
		JPanel panel1 = new JPanel();
		panel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		lblListSize = new JLabel("Image information");
		lblListSize.setFont(font);
		panel1.add(lblListSize);
		c.gridy = 0;
		c.gridx = 0;
		c.weighty=0;
		c.anchor = GridBagConstraints.PAGE_START;
		dataContainer.add(panel1, c);
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		lblListInfo = new JLabel("List information");
		lblListInfo.setFont(font);
		panel2.add(lblListInfo);
		c.gridy = 2;
		c.gridx = 0;
		c.weighty=0;
		dataContainer.add(panel2, c);
		
		/*
		 *START OF imageDataContainer 
		 */
		JPanel imageDataContainer = new JPanel(new GridLayout(0,1));		
		//imageDataContainer.setPreferredSize(new Dimension(200, contentPane.getHeight()));
		//imageDataContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		imgDataHandler = new imageDataHandler(previewFile, this);
		
		font = getFont("TitilliumWeb-Light.ttf", 16f);
		
		//Name of the image
		imgNameVal = new JTextArea("Name: " + imgDataHandler.name);
		imgNameVal.setFont(font);
		imgNameVal.setLineWrap(true);
		imageDataContainer.add(imgNameVal);
		
		//Resolution of the image
		imgResVal = new JTextArea("Resolution: " + imgDataHandler.resolution);
		imgResVal.setFont(font);
		imageDataContainer.add(imgResVal);
		
		//Aspect ratio of the image
		imgRatioVal = new JTextArea("Aspect ratio: " + imgDataHandler.aspectRatio);
		imgRatioVal.setFont(font);
		imageDataContainer.add(imgRatioVal);
		
		//Is the image a favorite?
		imgIsFavorite = new JTextArea("Favorite: No");
		imgIsFavorite.setFont(font);
		imageDataContainer.add(imgIsFavorite);
		
		c.gridy = 1;
		c.gridx = 0;
		c.weighty=0.5;		
		dataContainer.add(imageDataContainer, c);
		//END OF imageDataContainer
		
		/*
		 * START OF listDataContainer
		 */
		JPanel listDataContainer = new JPanel(new GridLayout(0,1));
		
		//Nr of files in list
		listNrVal = new JTextArea("Nr. of files: " + fl.listNrOfItems);
		listNrVal.setFont(font);
		listDataContainer.add(listNrVal);
		
		//Size of all files in list
		listSizeVal = new JTextArea("Combined size: " + fl.listTotalSize);
		listSizeVal.setFont(font);
		listDataContainer.add(listSizeVal);
		
		//Current working directory
		listPathVal = new JTextArea("Current directory: " + fl.currentPath);
		listPathVal.setFont(font);
		listDataContainer.add(listPathVal);
		
		c.gridy = 3;
		c.gridx = 0;
		c.weighty=0.5;		
		dataContainer.add(listDataContainer, c);
		//END OF listDataContainer
		
		/*
		 * HANDLING THE WINDOW
		 */
		this.addWindowListener(getWindowListener(this));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setSize(1200,800);
		/*
		try
		{
			BufferedImage previewImage = ImageIO.read(previewFile);
			if(previewImage.getWidth() > previewImage.getHeight())
			{
				//Window size 1225x563 gives imageLabel size 800x500, 16:10 aspect ratio
				setSize(1225, 563);
			}
			else
			{
				//Window size 925x863 gives imageLabel size 500x800, 10:16 aspect ratio
				setSize(925, 863);
			}		
		} 
		catch (IOException e1){e1.printStackTrace();}
		*/
		/*
		 * Printing out sizes
		 */
		//System.out.print("Component:\tcontentPane\toptionsPane\tscroll\t\timageLabel\n");
		//System.out.print("Width: \t\t" + contentPane.getSize().width + "\t\t" + optionsPanel.getSize().width + "\t\t" + scroll.getSize().width + "\t\t" + imageLabel.getSize().width + "\n");
		//System.out.print("Height: \t" + contentPane.getSize().height + "\t\t" + optionsPanel.getSize().height + "\t\t" + scroll.getSize().height + "\t\t" + imageLabel.getSize().height + "\n");
		
		
	}

	private MouseListener getMouseListener(final Frame theFrame)
	{
		MouseListener ML = new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{}
			@Override
			public void mouseEntered(MouseEvent arg0)
			{}
			@Override
			public void mouseExited(MouseEvent arg0)
			{}
			@Override
			public void mouseReleased(MouseEvent arg0)
			{}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.getClickCount() >= 2)
				{
					theFrame.setVisible(true);
				}
			}
		};
		return ML;
	}

	private WindowListener getWindowListener(final Frame theFrame)
	{
		WindowListener WL = new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e)
			{
			}
			@Override
			public void windowIconified(WindowEvent e)
			{
			}			
			@Override
			public void windowDeiconified(WindowEvent e)
			{
			}			
			@Override
			public void windowDeactivated(WindowEvent e)
			{
			}	
			@Override
			public void windowClosed(WindowEvent e)
			{
			}
			@Override
			public void windowActivated(WindowEvent e)
			{
			}
			
			@Override
			public void windowClosing(WindowEvent e)
			{
				theFrame.setVisible(false);
			}
		};
		return WL;
	}

	public Font getFont(String fontName, float fontSize)
	{
		Font font = null;
		java.net.URL fontURL = ClassLoader.getSystemResource("res/" + fontName);
		try 
		{
			InputStream fontStream = new BufferedInputStream(new FileInputStream(fontURL.getPath().toString()));
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} //If it fails, just return the default font
		catch (IOException|FontFormatException e) 
		{
			System.out.println(e.toString());
			LOGGER.info("Failed to get font: " + "res/" + fontName + "\n");
			return new JLabel().getFont();
		}
		
		return font.deriveFont(fontSize);
	}

	public synchronized Dimension changePreview(File clickedFile)
	{
		System.out.println("changing preview: " + clickedFile.getAbsolutePath());
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(clickedFile);
			System.out.println(image.getWidth() + "x" + image.getHeight());
			if(image.getHeight() > image.getWidth())
			{
				image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, imageLabel.getWidth(), imageLabel.getHeight(), Scalr.OP_ANTIALIAS);
				
				//Window size 925x863 gives imageLabel size 500x800, 10:16 aspect ratio
				//setSize(925, 863);
			}
			else
			{
				image = Scalr.resize(image, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, imageLabel.getWidth(), imageLabel.getHeight(), Scalr.OP_ANTIALIAS);
				
				//Window size 1225x563 gives imageLabel size 800x500, 16:10 aspect ratio
				//setSize(1225, 563);
			}
			
			this.wallPreview.setImage(image);
			this.revalidate();
			this.repaint();
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		return new Dimension(image.getWidth(), image.getHeight());
	}
	
	/*
	 * Update the JTextFields in the imageDataContainer in the GUI
	 */
	public void updateDataContainer(Wallpaper f)
	{
		this.imgNameVal.setText("Name: " + f.getName());

		this.imgResVal.setText("Resolution: " + this.imgDataHandler.getResolution(f));

		this.imgRatioVal.setText("Aspect ratio: " + this.imgDataHandler.getAspect(f));
		
		this.imgIsFavorite.setText("Favorite: " + (FileList.isFavorite(f)? "Yes" : "No"));

	}

}
