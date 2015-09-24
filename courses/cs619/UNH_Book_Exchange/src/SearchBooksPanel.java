import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Allows the user to search through all the books in the database. The user is able to specify what category what category they want to 
 * search by, i.e. title. Upon pressing enter, a table is displayed matching all books that match that field. The user is able to then browse through
 * the book. If the user selects one of the books in the table, the book picture and information is displayed. At this point, the user can 
 * choose to buy the book by pressing the "Buy this Book" button underneath the book information. THe user is not allowed to send another email 
 * to a book lister if one is already sent. If the user searches for a null string, all books in that category are displayed. If the user specifies a search where no entry matches the search, an image is displayed notifiying 
 * the user that no book matches their specified search.
 * @author Team11
 *
 */
public class SearchBooksPanel extends JPanel {
	/**
	 * The field allowing the user to specify what they specifically want to search for.
	 */
	private JTextField searchField;
	/**
	 * Narrows down the user search.
	 */
	private JComboBox<String> comboBox;
	
	/**
	 * Panel containing list of books.
	 */
	private JPanel listPanel;
	/**
	 * Button that contacts the seller about book interest.
	 */
	private JButton buyBtn;
	/**
	 * Table of books searched for.
	 */
	private JTable finalTable;
	/**
	 * Instance of the main frame all panels are displayed on.
	 */
	private MainFrame mainFrame;
	/**
	 * The index of the row in the table selected by the user.
	 */
	private int selectedRowIndex = 0;
	/**
	 * Allows the class to have access to the SQL server.
	 */
	private ServerAccessPoint sap;
	/**
	 * The current user of the program.
	 */
	private User curUser;
	/**
	 * The book display panel used to display the users book and corresponding book information.
	 */
	private BookDisplayPanel bdp;
	
	
	/**
	 * Creates the panel.
	 * @param cU
	 * The current user
	 */
	public SearchBooksPanel(User cU){
		curUser = cU;
		mainFrame = MainFrame.getInstance();
		
		sap = ServerAccessPoint.getInstance();
		
		setLayout(new BorderLayout());
		
		JPanel searchPanel = createSearchPanel();
		add(searchPanel, BorderLayout.NORTH);
		
		
		listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		add(listPanel, BorderLayout.CENTER);
		
		
		bdp = new BookDisplayPanel();
		buyBtn = bdp.getBookButton();
		buyBtn.setText("Buy This Book!");
		setButtonListener(buyBtn);
		listPanel.add(bdp, BorderLayout.NORTH);
	}
	
	/**
	 * Creates the search panel.
	 */
	private JPanel createSearchPanel(){
		JPanel searchPanel = new JPanel();
		
		JLabel srchLbl = new JLabel("Search By: ");
		srchLbl.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchPanel.add(srchLbl);
		
		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Title", "ISBN", "Author", "Condition", "Price"}));
		searchPanel.add(comboBox);
		
		searchField = new JTextField();
		
		searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchField.setColumns(10);
		searchPanel.add(searchField);
		
		final JButton goBtn = GUIFactory.createConfirmationButton("Go");
		searchPanel.add(goBtn);
		goBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					updateTable();
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					goBtn.doClick();
				}
			}
		});
		
		return searchPanel;
	}
	
	/**
	 * Adds a button listener to a button to contact the seller about book interest.
	 * @param btn
	 * The JButton that the listener is being added to.
	 */
	private void setButtonListener(final JButton btn){
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//buy the book
				TableModel temp = finalTable.getModel();
				String sellerEmail = temp.getValueAt(selectedRowIndex, 1).toString();
				
				//If the email is sent to yourself
				if(sellerEmail.equals(curUser.getEmail())){
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "You cannot buy your own book!");
					mainFrame.getGlassPane().setVisible(false);
				}
				//if you've already sent an email to this person
				else if(transactionExists(temp.getValueAt(selectedRowIndex, 0).toString())){
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "An email message has already been sent to this buyer. If the buyer is interested, " +
					"they will touch base with you once the email is read.");
					mainFrame.getGlassPane().setVisible(false);
				}
				//send the email and add to transaction history
				else{
					contactSeller(sellerEmail);
				}
			}
		});
	}
	
		/**
		 * Determines if the current user has already sent an email to the book seller.
		 * @param bookID
		 * The ID of the book being sold.
		 * @return
		 * <pre>
		 * 		true if the current user has already contacted the seller, false if otherwise.
		 * </pre>
		 */
		public boolean transactionExists(String bookID){
		ResultSet rs = sap.retrieveInfo("Transaction", "", "ID", bookID, true);
		try {
			while(rs.next()){
				if(rs.getString("Buyer").equals(curUser.getEmail()))
					return true;
			}
		} catch (SQLException e) {
			System.err.println("Error determining if transaction existed");
		}
		return false;
	}
	
	/**
	 * Updates the JTable containing all of the books that the user has searched for.
	 */
	private void updateTable(){
		bdp.resetPanel();
		
		finalTable = GUIFactory.createTable("Book Information", GUIFactory.bookTableHeader, "",
				(String)comboBox.getSelectedItem(), searchField.getText(), false);
		
		BorderLayout layout = (BorderLayout) listPanel.getLayout();
		if(layout.getLayoutComponent(BorderLayout.CENTER) != null){
			listPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		}
		if(layout.getLayoutComponent(BorderLayout.NORTH) != null){
			listPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
		}
		
		if(finalTable == null){
			listPanel.add(new PicturePanel("", SearchBooksPanel.class.getResource("/resources/booknotfound.gif")), BorderLayout.NORTH);
		}
		else{
			listPanel.add(bdp, BorderLayout.NORTH);
			TableColumnModel tcm = finalTable.getColumnModel();
			//remove id
		    tcm.getColumn(0).setMaxWidth(0);
		    tcm.getColumn(0).setMinWidth(0);
		    tcm.getColumn(0).setPreferredWidth(0);
			//remove e-mail
		    tcm.getColumn(1).setMaxWidth(0);
		    tcm.getColumn(1).setMinWidth(0);
		    tcm.getColumn(1).setPreferredWidth(0);
			
			establishMouseListener(finalTable);
			listPanel.add(new JScrollPane(finalTable), BorderLayout.CENTER);
		}
		
		validate();
		repaint();
	}
	
/**
 * Add a mouse listener to the JTable which creates a BookDisplayPanel above the JTable.
 * @param j
 * The JTable that the MouseListener is being added to.
 * 
 */
	private void establishMouseListener(final JTable j){
		j.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
						selectedRowIndex = j.getSelectedRow();
						
						//pass update book the ISBN number
						BookDisplayPanel.setBookDisplay(bdp, j, selectedRowIndex);
	
						buyBtn.setVisible(true);
					}
				}
			});
	}
	
	/**
	 * Send an email to the seller of the book.
	 * @param contactEmail
	 * The recipient of the email.
	 */
	private void contactSeller(String contactEmail){
		TableModel temp = finalTable.getModel();
		String subject = "Offer for " + temp.getValueAt(selectedRowIndex, 4).toString();
		mainFrame.getGlassPane().setVisible(true);
		String personalMessage = JOptionPane.showInputDialog(null, "An email will be sent to the book seller letting them know that you " +
		"are interested in buying the book. \nIf desired, a personal message can be added to the email by typing into the text field below.");
		mainFrame.getGlassPane().setVisible(false);
		
		ResultSet rs = sap.retrieveInfo("User Information", "", "E-Mail", contactEmail, true);
		String contactName = "";
		try {
			if(rs.next()){
				contactName = rs.getString("First Name");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String message = "Hello " + contactName + ",\n\n" + curUser.getFirstName() + " is interested in buying your book!" +
				" The information is posted below:\n\n" + temp.getValueAt(selectedRowIndex, 4).toString() + "\n" + 
				temp.getValueAt(selectedRowIndex, 3).toString() + "\n" + temp.getValueAt(selectedRowIndex,  5) + "\n" +
				temp.getValueAt(selectedRowIndex, 6).toString() + "\n" + temp.getValueAt(selectedRowIndex, 7).toString() + "\n\n";	
		
		if(personalMessage != null && !personalMessage.trim().equals("")){
		message += "Buyer Comments: " + personalMessage + "\n\nIf you would like to contact them further, their email is provided below:\n" + 
				curUser.getEmail() + "\n\nSincerely,\nThe UNH BookExchange Team";
		}
		else{
			message += "If you would like to contact them further, their email is provided below:\n" + 
					curUser.getEmail() + "\n\nSincerely,\nThe UNH BookExchange Team";
		}
		
		EmailClient.MailThread mail = EmailClient.getInstance().new MailThread(contactEmail, subject, message);
		mail.start();
		
		sap.addTransaction(curUser.getEmail(), contactEmail, temp.getValueAt(selectedRowIndex, 0).toString(), 
				mainFrame.getCurrentDate());
	}
	
}//End of class SearchBooksPanel.java