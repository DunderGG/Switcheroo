package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

		LOGGER.info("ENTERED FUNCTION GUI()");
		
		contentPane.setBackground(Color.BLACK);	
		
		/*
		 * START OF optionsPanel 
		 */
		JPanel optionsPanel = new JPanel(new GridLayout(1,0));
		
		/*
		 * BUTTON1
		 */
		button1 = new JButton("Choose folder");
		button1.setFont(getFont("HURTM___.otf", 25f));
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
					    	//Save the previous value of "currentDir"
					    	String previousDir = newDir;

					    	//Assign "currentDir" the new value
					    	/*
					    	 * getSelectedFile() returns the selected folder (despite its name),
					    	 * getCurrentDirectory() returns the folder of the selected folder, the parent folder (so don't use this one for the job)
					    	 */					    	
					    	newDir = chooser.getSelectedFile().toString();
					    	LOGGER.info("\n" + "Previous folder: " + previousDir + "\n" + "New folder: " + newDir + "\n");
					    	
					    	//If the same folder is chosen, do nothing
					    	if(newDir.equals(previousDir))
					    	{
					    		
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
						    			LOGGER.info("Updating list from folder: " + newDir);
						    			//If the chosen folder exists
								    	if(newDir.toString() != null)
								    	{								    										    	
								    		fl.updateListData(f);
								    		fl.updateListInfo();
								    	}
								    	else
								    		System.out.println("Folder did not exist");
						    			
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
					    	LOGGER.info("User cancelled" + "\n");
					    }
					    else
					    {
					    	LOGGER.info("Unknown error with opening folder");
					    }
					}
				});		
		
		/*
		 * BUTTON2
		 */
		button2 = new JButton("Set File");
		button2.setFont(getFont("HURTM___.otf", 25f));
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
		button3.setFont(getFont("HURTM___.otf", 25f));
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
		button4.setFont(getFont("HURTM___.otf", 25f));
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
						
						FileList.fileList.getModel().getElementAt(index).isFavorite = true;
						//FileList.updateListFavorites();
						System.out.println("Wallpaper set to favorite? " + FileList.fileList.getModel().getElementAt(index).isFavorite);
						imgIsFavorite.setText("Favorite: Yes");
					}
				});
		//END OF BUTTONS
				
		contentPane.add(optionsPanel, BorderLayout.PAGE_START);
		//END OF optionsPanel 
		
		
		//Create and add the scrollpane with the list of files
		scroll = fl.getScrollPane(this);
		contentPane.add(scroll, BorderLayout.LINE_START);
		newDir = fl.wallFolder.getPath();
		LOGGER.info("Starting point of newDir = " + newDir);

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
		lblListSize.setFont(getFont("HURTM___.otf", 25f));
		panel1.add(lblListSize);
		c.gridy = 0;
		c.gridx = 0;
		c.weighty=0;
		c.anchor = GridBagConstraints.PAGE_START;
		dataContainer.add(panel1, c);
		
		/*
		 *START OF imageDataContainer 
		 */
		JPanel imageDataContainer = new JPanel(new GridLayout(0,1));		
		//imageDataContainer.setPreferredSize(new Dimension(200, contentPane.getHeight()));
		//imageDataContainer.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		imageDataHandler imgDataHandler = new imageDataHandler(previewFile, this);
		
		//Name of the image
		imgNameVal = new JTextArea("Name: " + imgDataHandler.name);
		imgNameVal.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		imgNameVal.setLineWrap(true);
		imageDataContainer.add(imgNameVal);
		
		//Resolution of the image
		imgResVal = new JTextArea("Resolution: " + imgDataHandler.resolution);
		imgResVal.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		imageDataContainer.add(imgResVal);
		
		//Aspect ratio of the image
		imgRatioVal = new JTextArea("Aspect ratio: " + imgDataHandler.aspectRatio);
		imgRatioVal.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		imageDataContainer.add(imgRatioVal);
		
		//Is the image a favorite?
		imgIsFavorite = new JTextArea("Favorite: No");
		imgIsFavorite.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		imageDataContainer.add(imgIsFavorite);
		
		c.gridy = 1;
		c.gridx = 0;
		c.weighty=0.5;		
		dataContainer.add(imageDataContainer, c);
		//END OF imageDataContainer
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		lblListInfo = new JLabel("List information");
		lblListInfo.setFont(getFont("HURTM___.otf", 25f));
		panel2.add(lblListInfo);
		c.gridy = 2;
		c.gridx = 0;
		c.weighty=0;
		dataContainer.add(panel2, c);
		
		/*
		 * START OF listDataContainer
		 */
		JPanel listDataContainer = new JPanel(new GridLayout(0,1));
		
		//Nr of files in list
		listNrVal = new JTextArea("Nr. of files: " + fl.listNrOfItems);
		listNrVal.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		listDataContainer.add(listNrVal);
		
		//Size of all files in list
		listSizeVal = new JTextArea("Combined size: " + fl.listTotalSize);
		listSizeVal.setFont(getFont("TitilliumWeb-Light.ttf", 16f));
		listDataContainer.add(listSizeVal);
		
		c.gridy = 3;
		c.gridx = 0;
		c.weighty=0.5;		
		dataContainer.add(listDataContainer, c);
		//END OF listDataContainer
		
		/*
		 * HANDLING THE WINDOW
		 */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		System.out.print("Component:\tcontentPane\toptionsPane\tscroll\t\timageLabel\n");
		System.out.print("Width: \t\t" + contentPane.getSize().width + "\t\t" + optionsPanel.getSize().width + "\t\t" + scroll.getSize().width + "\t\t" + imageLabel.getSize().width + "\n");
		System.out.print("Height: \t" + contentPane.getSize().height + "\t\t" + optionsPanel.getSize().height + "\t\t" + scroll.getSize().height + "\t\t" + imageLabel.getSize().height + "\n");
		
		
	}

	public Font getFont(String fontName, float fontSize)
	{
		Font font = null;
		try 
		{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("res/"+fontName));
		} //If it fails, just return the default font
		catch (IOException|FontFormatException e) {return new JLabel().getFont();}
		
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
	

	
}
