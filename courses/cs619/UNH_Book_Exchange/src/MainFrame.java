/*
 *	MainFrame.java
 *		Creates the main frame, setting up the GUI so that only a 
 *		component needs to be added to the center of mainPanel.
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Box;
import javax.swing.border.MatteBorder;

/**
 * Creates the main frame, setting up the GUI so that only a component needs to be added to the center of MainPanel. Originally,
 * the main frame is used to swap between different panels. The main frame implements the singleton design pattern to ensure that there is only one instance of the frame at a given point in time.
 * @author Team11
 *
 */
public class MainFrame extends JFrame {

	/**
	 * The single instance of the main frame used to implement the singleton design pattern.
	 */
	private static MainFrame mainFrame;
	
	/**
	 * The instance of the server used to process database requests.
	 */
	private ServerAccessPoint sap;
	
	/**
	 * The main color scheme used throughout the interface.
	 */
	public static Color darkSlateBlue = new Color(72,61,139), offWhite = new Color(240, 240, 240), lightGray = new Color (169, 169, 169);
	
	/**
	 * Panel used to achieve a certain task of the GUI.
	 */
	private JPanel homePanel, loginPanel, logOrRegPanel, registerPanel, helpPanel, myAccountPanel, managePanel, browsePanel;
	
	/**
	 * Button used to navigate to the associated panel.
	 */
	private JButton btnHome, btnLogin, btnLogout, btnBrowse, btnManage, btnHelp, btnMyAccount;
	
	/**
	 * Encompasses the main inner panel so that if contents do not fit from a frame they can be scrolled through.
	 */
	private JScrollPane scrollPanel;
	
	/**
	 * The current user of the program.
	 */
	private User curUser;
	
	/**
	 * Private constructor for singleton design pattern.
	 */
	private MainFrame() {		
		//establish connection to server
		sap = ServerAccessPoint.getInstance();
		sap.connect();
		
		//set up the main frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 720);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setLookAndFeel("Nimbus");
		setGlassPane(new GlassPanel());
	}
	
	/**
	 * Initialize the session.
	 */
	public void initializeSession(){
		//set up the main panel
		JPanel mainPanel = createMainFramePanels();
		initializePanels();
		setButtonVisibility("logged out");
		setContentPane(mainPanel);
		setScrollPanel(homePanel);
	}
	
	/**
	 * Allows other classes to access the MainFrame object. Only instantiated once throughout the life of the program.
	 * @return
	 * Singleton MainFrame object.
	 */
	public static MainFrame getInstance(){
		if(mainFrame == null){
			mainFrame = new MainFrame();
		}
		
		return mainFrame;
	}
	
	/**
	 * Creates the panels for the main frame.
	 * @return
	 * A JPanel containing all panels needed for the main frame.
	 */
	private JPanel createMainFramePanels(){
		//set up the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(darkSlateBlue);
		mainPanel.setLayout(new BorderLayout());
				
		//set up the site name box
		Box nameBox = createNameBox();
		mainPanel.add(nameBox, BorderLayout.NORTH);
				
		//set up the site copyright box
		Box copyrightBox = createCopyrightBox();
		mainPanel.add(copyrightBox, BorderLayout.SOUTH);
				
		//create the center panel that stores the buttons and scroll-able panel
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(new MatteBorder(5, 0, 5, 0, Color.BLACK));
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BorderLayout());
				
		//create panel that will store all buttons
		JPanel buttonPanel = createButtonPanel();
		centerPanel.add(buttonPanel, BorderLayout.NORTH);
				
		//create the scroll-able area that panels will be added to
		scrollPanel = new JScrollPane();
		scrollPanel.setBorder(null);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.getVerticalScrollBar().setUnitIncrement(5);
		centerPanel.add(scrollPanel, BorderLayout.CENTER);
	
		return mainPanel;
	}
	
	/**
	 * Creates the site name vertical box.
	 * @return
	 * A Box containing the site name.
	 */
	private Box createNameBox(){
		Box nBox = Box.createVerticalBox();
		Component verticalStrut_1 = GUIFactory.resizedVerticalStrut(10);
		nBox.add(verticalStrut_1);
		
		JLabel siteLabel = new JLabel("University of New Hampshire Textbook Exchange");
		siteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		siteLabel.setForeground(offWhite);
		siteLabel.setFont(new Font("Baskerville Old Face", Font.BOLD, 35));
		nBox.add(siteLabel);
		
		Component verticalStrut_2 = GUIFactory.resizedVerticalStrut(5);
		nBox.add(verticalStrut_2);
		
		return nBox;
	}
	
	/**
	 * Creates the copyright vertical box.
	 * @return
	 * A Box containing the copyright information.
	 */
	private Box createCopyrightBox(){
		Box cBox = Box.createVerticalBox();
		
		Component verticalStrut_1 = GUIFactory.resizedVerticalStrut(5);
		cBox.add(verticalStrut_1);
		
		JLabel copyrightLabel = new JLabel("\u00A92012 UNH | Website by Stephen Chambers, Scott Cypher, Jason Gelinas, Nicholas Sjostrom");
		copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		copyrightLabel.setForeground(offWhite);
		copyrightLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		cBox.add(copyrightLabel);
		
		Component verticalStrut_2 = GUIFactory.resizedVerticalStrut(5);
		cBox.add(verticalStrut_2);
		
		return cBox;
	}
	
	/**
	 * Creats the button panel.
	 * @return
	 * JPanel containing the button panel.
	 */
	private JPanel createButtonPanel(){
		JPanel btnPanel = new JPanel();
		btnPanel.setBorder(new MatteBorder(0, 0, 2, 0, lightGray));
		
		btnPanel.add(GUIFactory.resizedVerticalStrut(40));
		
		//creates the home button and the associated action listener
		btnHome = new JButton("Home\r\n");
		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnHome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert home button code here
				setScrollPanel(homePanel);
			}
		});
		btnPanel.add(btnHome);
		
		//creates the login button and the associated action listener
		btnLogin = new JButton("Login or Register\r\n");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert login button code here
				((InputPanel) loginPanel).clearFields();
				((RegisterPanel) registerPanel).resetFields();
				setScrollPanel(logOrRegPanel);
			}
		});
		btnPanel.add(btnLogin);
		
		//creates the logout button and the associated action listener		
		btnMyAccount = new JButton("My Account\r\n");
		btnMyAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnMyAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert my account button code here
				setScrollPanel(myAccountPanel);
			}
		});
		btnPanel.add(btnMyAccount);
		
		//creates the browse button and the associated action listener		
		btnBrowse = new JButton("Search All Books\r\n");
		btnBrowse.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert browse button code here
				setScrollPanel(browsePanel);
			}
		});
		btnPanel.add(btnBrowse);
		
		//creates the manage button and the associated action listener
		btnManage = new JButton("Manage\r\n");
		btnManage.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnManage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert manage button code here
				setScrollPanel(managePanel);
			}
		});
		btnPanel.add(btnManage);
		
		//creates the login button and the associated action listener
		btnHelp = new JButton("Help\r\n");
		btnHelp.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert help button code here
				setScrollPanel(helpPanel);
			}
		});
		btnPanel.add(btnHelp);
		
		//creates the logout button and the associated action listener		
		btnLogout = new JButton("Logout\r\n");
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert logout button code here
				curUser = null;
				setButtonVisibility("logged out");
				setScrollPanel(homePanel);
				
				//seems like the right thing to do...
				myAccountPanel = null;
				managePanel = null;
			}
		});
		btnPanel.add(btnLogout);
		
		return btnPanel;
	}
	
	/**
	 * Sets the button visibility level:
	 * <pre>
	 *		"logged out"	-	shows Home, Login, Help
	 *		"logged in"		-	shows all except Login and Manage
	 *		"administrator"	-	shows all except Login
	 *</pre>
	 * @param level
	 * String containing the user status
	 */
	public void setButtonVisibility(String level){
		if(level.equalsIgnoreCase("logged out")){
			btnLogout.setVisible(false);
			btnLogin.setVisible(true);
			btnMyAccount.setVisible(false);
			btnBrowse.setVisible(false);
			btnManage.setVisible(false);
		}
		else {
			btnBrowse.setVisible(true);
			btnMyAccount.setVisible(true);
			btnLogin.setVisible(false);
			btnLogout.setVisible(true);
			
			if(level.equalsIgnoreCase("logged in")){
				btnManage.setVisible(false);
			}
			else if(level.equalsIgnoreCase("administrator")){
				btnManage.setVisible(true);
			}
		}
	}	  
	
	/**
	 *	Initializes all panels that will be swapped through.
	 */
	private void initializePanels(){
		homePanel = new PicturePanel("This site is designed to connect students across UNH for the purpose of buying and selling textbooks", 
				MainFrame.class.getResource("/resources/wildcat.gif"));
		
		logOrRegPanel = new JPanel();
		logOrRegPanel.setLayout(new BoxLayout(logOrRegPanel, BoxLayout.X_AXIS));
		loginPanel = new LoginPanel();
		logOrRegPanel.add(loginPanel);
		registerPanel = new RegisterPanel();
		registerPanel.setBorder(new MatteBorder(0, 2, 0, 0, lightGray));
		logOrRegPanel.add(registerPanel);
		
		helpPanel = new HelpPanel();
	}
	
	/**
	 * Accessor for the my account panel.
	 * @return
	 * The MyAccountPanel object.
	 */
	public MyAccountPanel getMyAccountPanel(){
		return (MyAccountPanel) myAccountPanel;
	}
	
	/**
	 * Clears and sets the cscrollPanelenter panel.
	 * @param newPanel
	 * The JPanel that the main screen is set to.
	 */
	public void setScrollPanel(JComponent newPanel){
		scrollPanel.setViewportView(newPanel);
	}
	
	/**
	 * Gets the current date.
	 * @return
	 * The current date in MM/dd/yyyy format.
	 */
	public String getCurrentDate(){
		   DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		   
		   Calendar cal = Calendar.getInstance();
		   return dateFormat.format(cal.getTime());
	}
	
	/**
	 * Changes the default lookAndFeel.
	 * @param lookAndFeel
	 * The lookAndFeel to be changed to.
	 */
	public static void setLookAndFeel(String lookAndFeel){
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if (lookAndFeel.equals(info.getName())) {
	            try {
					UIManager.setLookAndFeel(info.getClassName());
				}
	            catch(Exception e){
					e.printStackTrace();
				}
	            break;
	        }
	    }
	}
	
	/**
	 * Launches the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = MainFrame.getInstance();
					frame.initializeSession();
					frame.setVisible(true);
					//frame.sellAllBooks();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initiate the user session.
	 * @param newUser
	 * User of the system.
	 * @param isNew
	 * <pre>
	 * 		true if user is new, false otherwise
	 * </pre>
	 */
	public void initiateSession(User newUser, boolean isNew) {
		curUser = newUser;
		
		//Insert user details check (admin vs normal)
		if(!newUser.isAdmin()){
			setButtonVisibility("logged in");
		}
		else{
			setButtonVisibility("administrator");
			managePanel = new AdminPanel();
		}
		
		ResultSet rs = sap.retrieveInfo("Custom Information", "", "E-Mail", newUser.getEmail(), true);
		try {
			if(rs.next()){
				ColorTable.tableFont = new Font(rs.getString("Table Font Type"), Integer.parseInt(rs.getString("Table Font Style")), Integer.parseInt(rs.getString("Table Font Size")));
				ColorTable.headerFont = new Font(rs.getString("Header Font Type"), Integer.parseInt(rs.getString("Header Font Style")), Integer.parseInt(rs.getString("Header Font Size")));
				ColorTable.highlightColor = new Color(Integer.parseInt(rs.getString("Highlighting Color")));
				ColorTable.evenRowColor = new Color(Integer.parseInt(rs.getString("Even Row Color")));
				ColorTable.oddRowColor = new Color(Integer.parseInt(rs.getString("Odd Row Color")));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		myAccountPanel = new MyAccountPanel(curUser);
		browsePanel = new SearchBooksPanel(curUser);
		
		if(isNew){
			setScrollPanel(new PicturePanel("Your account has been successfully verified!", 
					MainFrame.class.getResource("/resources/checkmark.gif")));
		}
		else{
			setScrollPanel(myAccountPanel);
		}
	}
	/**
	 * Determines if the price entered is in the correct format.
	 * @param price
	 * Price to check against.
	 * @return
	 * <pre>
	 * 		true if valid price, false otherwise.
	 * </pre>
	 */
	public boolean isValidPrice(String price){
		 Pattern p = Pattern.compile("^\\$([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+).[0-9][0-9]$");
		 Matcher m = p.matcher(price);
		 return m.matches();
	}
	
	/**
	 * Determines if the ISBN is valid.
	 * @param isbn
	 * The ISBN number that is being validated.
	 * @return
	 * <pre>
	 * 		true if ISBN is a valid ISBN, false otherwise.
	 * </pre>
	 */
	public boolean isValidISBN(String isbn){
		Pattern p = Pattern.compile("^(97(8|9))?[0-9]{9}([0-9]|X)$");
		p.matcher(isbn);
		return true;
	}
	
}//End of class MainFrame.java