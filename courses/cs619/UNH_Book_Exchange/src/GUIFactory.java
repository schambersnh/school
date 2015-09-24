import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 * This class provides convenience methods for creating GUI screens in addition to storing general GUI information. This includes
 * creating forms, resizing images, and many other methods for commonly used tasks in the panel creating process.
 * @author Team11
 *
 */
public abstract class GUIFactory {
	/**
	 * The table header used for tables displaying user information.
	 */
	public static final String[] userTableHeader = {"Last Name", "First Name", "E-Mail", "Password", "Status"};
	
	/**
	 * The table header used for tables displaying transaction information
	 */
	public static final String[] transactionTableHeader = {"Buyer", "Seller", "ID", "Request Date"};
	
	/**
	 * The table header used tables displaying book information.
	 */
	public static final String[] bookTableHeader = {"ID", "E-Mail", "ISBN", "ISBN13", "Title", "Author", "Condition", "Price"};
	
	/**
	 * The dimensions of the user's screen.  Used to resize images based on resolution.
	 */
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	
	/**
	 * Convenience method for creating a FormPanel.
	 * @param leftSide
	 * All JComponents on the left side of the form.
	 * @param rightSide
	 * All JComponents on the right side of the form.
	 * @param singleComponent
	 * Optional single component to be placed at bottom of the form.<br>
	 * i.e. Confirmation button
	 * @return
	 * JPanel containing the created FormPanel.
	 */
	public static JPanel[] createFormPanel(ArrayList<? extends JComponent> leftSide, ArrayList<? extends JComponent> rightSide, JComponent singleComponent){
		JPanel mainForm = new JPanel(new BorderLayout());
		
		JPanel panelPtr = mainForm;
		
		for(int i = 0; i < leftSide.size(); i++){
			JPanel subForm = new JPanel(new BorderLayout());
			
			JPanel sectionPanel = new JPanel();
			sectionPanel.add(leftSide.get(i));
			sectionPanel.add(rightSide.get(i));
			panelPtr.add(sectionPanel, BorderLayout.NORTH);
			
			panelPtr.add(subForm, BorderLayout.CENTER);
			
			panelPtr = subForm;
		}
		
		if(singleComponent != null){
			JPanel singlePanel = new JPanel();
			singlePanel.add(singleComponent);
			panelPtr.add(singlePanel, BorderLayout.NORTH);
		}
		
		//return the outermost panel and inner most panel)
		JPanel[] panels = {mainForm, panelPtr};
		return panels;
	}
	
	/**
	 * Creates a centered image panel with a north strut.
	 * @param url
	 * URL of the image.
	 * @return
	 * Box containing the image.
	 */
	public static Box createImageBox(URL url){
		ImageIcon resizedII = resizeImage(url);
		
		Box imageBox = Box.createVerticalBox();
		imageBox.add(GUIFactory.resizedVerticalStrut(50));
		JLabel imgLbl = new JLabel(resizedII);
		imgLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		imageBox.add(imgLbl);
		
		return imageBox;
	}
	/**
	 * Resize an image based on the screen size.
	 * @param url
	 * The image being resized.
	 * @return
	 * The resized image.
	 */
	public static ImageIcon resizeImage(URL url){		
		BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(url);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int width = bimg.getWidth();
		int height = bimg.getHeight();
		
		Image resizedImage = bimg.getScaledInstance((int)(width*(screenSize.getWidth()/1920.0)), (int)(height*(screenSize.getHeight()/1080.0)), Image.SCALE_SMOOTH);//height*((int)(dim.getHeight()/1080.0)), width*((int)(dim.getWidth()/1920.0)), Image.SCALE_DEFAULT);
		
		
		ImageIcon ii = new ImageIcon(resizedImage);
		return ii;
	}
	
	/**
	 * Creates a title box.
	 * @param title
	 * Text of the title box.
	 * @return
	 * Box containing the title box.
	 */
	public static Box createTitleBox(String title){
		Box tBox = Box.createVerticalBox();
		
		Component verticalStrut = GUIFactory.resizedVerticalStrut(20);
		tBox.add(verticalStrut);
		
		JLabel loginTitle = new JLabel(title);
		tBox.add(loginTitle);
		loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		Component verticalStrut_1 = GUIFactory.resizedVerticalStrut(10);
		tBox.add(verticalStrut_1);
		
		return tBox;
	}
	
	/**
	 * Creates a section. The first string is the title, the second is the information
	 * associated with that title.
	 * @param title
	 * Title of section.
	 * @param info
	 * Body of section.
	 * @return
	 * JPanel containing the section.
	 */
	public static JPanel createTitleAndTextPanel(String title, String info){
		JPanel sectionPanel = new JPanel(new BorderLayout());
		Box sBox = Box.createVerticalBox();
		
		//create space
		Component verticalStrut = GUIFactory.resizedVerticalStrut(20);
		sBox.add(verticalStrut);
		
		//create the section title
		JLabel loginTitle = new JLabel(title);
		sBox.add(loginTitle);
		loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		//create the info text area
		JPanel infoPanel = new JPanel();
		JTextArea infoTA = new JTextArea(info, 5, 50);
		infoTA.setRows(infoTA.getLineCount());
		infoTA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		infoTA.setBackground(MainFrame.offWhite);
		infoTA.setLineWrap(true);
		infoTA.setEditable(false);
		infoTA.setWrapStyleWord(true);
		infoPanel.add(infoTA);
		sBox.add(infoPanel);
		
		sectionPanel.add(sBox, BorderLayout.NORTH);
		
		return sectionPanel;
	}
	
	/**
	 * Creates a confirmation button.
	 * @param title
	 * Text of the button.
	 * @return
	 * The confirmation button.
	 */
	public static JButton createConfirmationButton(String title){
		JButton cButton = new JButton(title);
		cButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		return cButton;
	}
	
	/**
	 * Creates a button with an image inside of it.
	 * @param str
	 * Text of the button.
	 * @param url
	 * Image of the button.
	 * @return
	 * The icon button.
	 */
	public static JButton createIconButton(String str, URL url){		
		JButton imgBtn = new JButton(str,resizeImage(url));
		imgBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
		imgBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		imgBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		imgBtn.setVerticalAlignment(SwingConstants.TOP);
		
		return imgBtn;
	}
	
	/**
	 * Create a vertical strut that based on screen size.
	 * @param size
	 * The size of the previous vertical strut.
	 * @return
	 * A rescaled vertical strut.
	 */
	public static Component resizedVerticalStrut(int size){
		size = (int)(size*screenSize.getHeight()/1080.0);
		return Box.createVerticalStrut(size);
	}
	
	/**
	 * Create a horizontal strut that based on screen size.
	 * @param size
	 * The size of the previous horizontal strut.
	 * @return
	 * A rescaled horizontal strut.
	 */
	public static Component resizedHorizontalStrut(int size){
		size = (int)(size*screenSize.getWidth()/1920.0);
		return Box.createHorizontalStrut(size);
	}
	
	/**
	 * Rescales a height.
	 * @param size
	 * The size of the previous height.
	 * @return
	 * A rescaled height.
	 */
	public static int resizedNumberHeight(int size){
		size = (int)(size*screenSize.getHeight()/1080.0);
		return size;
	}
	
	/**
	 * Creates a JTable. Used for retrieving book information, user information, etc.
	 * @param tableName
	 * Name of the SQL table that the JTable is using for data.
	 * @param cNames
	 * An containing the names of each JTable column.
	 * @param category
	 * The field in the SQL database that is used to retrieve JTable data.
	 * @param search
	 * The specific value of the SQL field that is used to retrieve JTable data.
	 * @param b
	 * Determines if the search is an exact match.<br>
	 * <pre>
	 * 		true: exact match
	 * 		false:  relative match
	 * </pre>
	 * @return
	 * JTable containing all necessary information from the SQL server.
	 */
	public static ColorTable createTable(String tableName, String[] cNames, String card, String category, String search, 
			boolean b){
		ResultSet rs = ServerAccessPoint.getInstance().retrieveInfo(tableName, card, category, search, b);

		String[][] data = null;
		
		try {
			if(rs.next()){
				rs.previous();
			}
			else{
				return null;
			}
			
			rs.last();
			int size = rs.getRow();
			rs.beforeFirst();
			
			data = new String[size][cNames.length];
			
			int i = 0;
			while(rs.next()){
				for(int j = 0; j < cNames.length; j++){
					data[i][j] = rs.getString(j + 1);
				}
				i++;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		final ColorTable userTable = new ColorTable(data, cNames, ColorTable.headerFont, ColorTable.tableFont, ColorTable.highlightColor,
				ColorTable.evenRowColor, ColorTable.oddRowColor){
			  public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
			  }
		};
		userTable.setModel(new DefaultTableModel(data,cNames));
		
		userTable.setAutoCreateRowSorter(true);
		
		return userTable;
	}
}//End of class GuiFactory