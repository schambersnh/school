import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Lets the user select a book management panel, modify account panel, customize account panel, sell book panel, and transaction history panel.
 * View the corresponding panels in order to learn more information about the capabilities of the MyAccountPanel. 
 * @author Team11
 *
 */
public class MyAccountPanel extends JPanel{
	/**
	 * The current user of the program.
	 */
	private User curUser;
	
	/**
	 * The main frame all panel's are displayed on.
	 */
	private MainFrame mainFrame;
	
	/**
	 * A panel that allows the user to accomplish the panel's task.
	 */
	private JPanel manageBooksPanel, customizePanel, sellBookPanel, modificationPanel;
	private JTabbedPane transactionPanel;
	
	/**
	 *
	 * Creates the panel displaying 5 buttons that allow the user to manage books, customize their table,
	 * sell a book, view their transaction history, and modify their account information.
	 *
	 * @param u
	 * The current User.
	 */
	public MyAccountPanel(User u){
		curUser = u;
		mainFrame = MainFrame.getInstance();
		
		manageBooksPanel = new ManageBooksPanel(curUser);
		customizePanel = new CustomizePanel(curUser);
		sellBookPanel = new SellBookPanel(curUser);
		transactionPanel = new TransactionPanel(curUser);
		modificationPanel = new ModificationPanel(curUser);
		
		setLayout(new BorderLayout());
		
		JPanel welPanel = new JPanel();
		welPanel.setLayout(new BoxLayout(welPanel, BoxLayout.Y_AXIS));
		welPanel.add(GUIFactory.createTitleBox("Welcome, " + curUser.getFirstName() + "!"));
		welPanel.add(GUIFactory.resizedVerticalStrut(30));
		add(welPanel, BorderLayout.NORTH);
		
		JPanel btnPanel = new JPanel();
		
		JButton myBooks = GUIFactory.createIconButton("Manage Books", MyAccountPanel.class.getResource("/resources/books.gif"));
		myBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert manage books button info here
				((ManageBooksPanel)manageBooksPanel).updatePanel();
				mainFrame.setScrollPanel(manageBooksPanel);
			}
		});
		btnPanel.add(myBooks);
		btnPanel.add(GUIFactory.resizedHorizontalStrut(50));
		
		JButton sellBook = GUIFactory.createIconButton("Sell a Book", MyAccountPanel.class.getResource("/resources/bookmoney.gif"));
		sellBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert sell books button info here
				((SellBookPanel)sellBookPanel).resetPanel();
				mainFrame.setScrollPanel(sellBookPanel);
			}
		});
		btnPanel.add(sellBook);
		btnPanel.add(GUIFactory.resizedHorizontalStrut(50));
		
		JButton transBtn = GUIFactory.createIconButton("Book Transaction History", MyAccountPanel.class.getResource("/resources/transactionhistory.gif"));
		transBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert transaction books button info here
				transactionPanel = new TransactionPanel(curUser);
				((TransactionPanel)transactionPanel).updatePanel();
				mainFrame.setScrollPanel(transactionPanel);
			}
		});
		btnPanel.add(transBtn);
		
		JPanel btnPanel_2 = new JPanel();		
		JButton myAccount = GUIFactory.createIconButton("Modify Information", MyAccountPanel.class.getResource("/resources/lock.gif"));
		myAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert customize account info here
				
				((ModificationPanel) modificationPanel).updatePanel();
				mainFrame.setScrollPanel(modificationPanel);
				//mainFrame.setScrollPanel(new RegisterPanel(curUser));		
			}
		});
		btnPanel_2.add(myAccount);
		btnPanel_2.add(GUIFactory.resizedHorizontalStrut(50));
		
		JButton myCustom = GUIFactory.createIconButton("Customize", MyAccountPanel.class.getResource("/resources/colorwheel.gif"));
		myCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert customize account button info here
				((CustomizePanel)customizePanel).loadFontAndColor();
				mainFrame.setScrollPanel(customizePanel);
			}
		});
		btnPanel_2.add(myCustom);
		
		JPanel allBtnPanel = new JPanel(new BorderLayout());
		allBtnPanel.add(btnPanel, BorderLayout.NORTH);
		allBtnPanel.add(btnPanel_2, BorderLayout.CENTER);
		
		add(allBtnPanel, BorderLayout.CENTER);
	}
	
}//End of class MyAccountPanel.java
