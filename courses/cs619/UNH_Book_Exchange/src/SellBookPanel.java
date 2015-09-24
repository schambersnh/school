
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A panel that allows the user to sell books. The user can sell a book by providing the book ISBN, a book price in valid U.S. Currency format, and
 * by specifying the book condition by selecting one of the options in the drop down menu. Upon pressing the preview button, if the ISBN 
 * number is not valid, the user is notified. If the ISBN number is valid, the user is given a preview of what the sale will look like.
 * If the user wishes to precede with the sale, they can do so by pressing the "Sell this Book" button beneath the book information.
 * @author Team11
 *
 */
public class SellBookPanel extends InputPanel {

	/**
	 * A way of storing all of the JComponents required for the SellBookPanel.
	 */
	private ArrayList<JComponent> components;
	
	/**
	 * Text fields allowing the user to enter an ISBN number and a price.
	 */
	private JTextField isbnField, priceField;
	
	/**
	 * ComboBox allowing the user to select a condition between New, Excellent, Good, Fair, and Poor.
	 */
	private JComboBox<String> conditionCombo;
	
	/**
	 * The BookRequestInfo item allowing the user to give an ISBN and receive encapsulated information back.
	 */
	private BookRequestInfo bri;
	
	/**
	 * The current user.
	 */
	private User curUser;
	
	/**
	 * Allows the class to be able to manipulate the SQL server.
	 */
	private ServerAccessPoint sap;
	
	/**
	 * Allows the panel to display book information.
	 */
	private BookDisplayPanel bdp;
	
	/**
	 * Instance of the main frame all panels are displayed on.
	 */
	private MainFrame mainFrame;
	
	/**
	 * Stores the condition and price of the book being sold chosen by the user.
	 */
	private String strCondition, strPrice;
	
	/**
	 * Button that, when clicked, sells the book.
	 */
	private JButton sellBookBtn;
	
	/**
	 * Creates the panel.
	 * @param u
	 * The current user.
	 */
	public SellBookPanel(User u) {
		components = new ArrayList<JComponent>();
		
		curUser = u;
		
		sap = ServerAccessPoint.getInstance();
		mainFrame = MainFrame.getInstance();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create the title section of form
		add(GUIFactory.createTitleBox("Enter the following information to sell a book:"));
		
		//isbn section of form
		JLabel isbnLbl = new JLabel("ISBN ");
		labels.add(isbnLbl);
		isbnField = new JTextField();
		textFields.add(isbnField);
		components.add(isbnField);
				
		//price section of form
		JLabel priceLbl = new JLabel("Price ");
		labels.add(priceLbl);
		priceField = new JTextField();
		textFields.add(priceField);
		components.add(priceField);

		//price section of form
		JLabel conditionLbl = new JLabel("Condition ");
		labels.add(conditionLbl);
		conditionCombo = new JComboBox<String>();
		conditionCombo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		conditionCombo.setModel(new DefaultComboBoxModel<String>(new String[] {"New", "Excellent", "Good", "Fair", "Poor"}));
		components.add(conditionCombo);
		
		//create the button
		acceptButton = new JButton("Preview Sale");
		setPreviewButtonListener(acceptButton);
				
		//create the form and add it to the panel
		JPanel[] formArr = GUIFactory.createFormPanel(labels, components, acceptButton);
		JPanel form = formArr[0];
		add(form);
		
		
		bdp = new BookDisplayPanel();
		sellBookBtn = bdp.getBookButton();
		sellBookBtn.setText("Sell this Book!");
		setSellButtonListener(sellBookBtn);
		formArr[1].add(bdp, BorderLayout.CENTER);
		
		updateFormat();
	}
	
	/**
	 * Adds a button listener to a button which displays a panel showing the user what they're listing will look like when sold.
	 * @param btn
	 * The button to add the listener to.
	 */
	private void setPreviewButtonListener(final JButton btn){
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert preview sale button code here
				
				//Make sure the fields are trimmed
				trimFields();
				bri = new BookRequestInfo(isbnField.getText());
				
				if(!mainFrame.isValidISBN(isbnField.getText())){
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "The ISBN you entered is not valid. Please try again.");
					mainFrame.getGlassPane().setVisible(false);
				}				
				else if(!bri.isValid()){
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "The ISBN you entered is not valid. Please try again.");
					mainFrame.getGlassPane().setVisible(false);
				}
				else if(!mainFrame.isValidPrice(priceField.getText())){
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "The price that you entered is not in the correct format. \n\nPlease enter " +
					"the price in U.S. Currency Format\nEx:  $100.00");
					mainFrame.getGlassPane().setVisible(false);
				}
				else{
					strCondition = (String)conditionCombo.getSelectedItem();
					strPrice = priceField.getText();
					bdp.updateBook(bri, strCondition, strPrice);
					sellBookBtn.setVisible(true);
				}
			}
		});
		
		setEnterButton(btn);
	}
	
	/**
	 * Adds a button listener to a button which actually sells the book into the system.
	 * @param btn
	 * The button that the listener is being added to.
	 */
	private void setSellButtonListener(final JButton btn){
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert preview sale button code here
				sap.addToBookInformation(curUser.getEmail(), isbnField.getText(), bri.getISBN13(), bri.getTitle(), 
						bri.getAuthor(), strCondition, strPrice);
				mainFrame.setScrollPanel((new PicturePanel("Your book has been successfully listed!", 
						MainFrame.class.getResource("/resources/checkmark.gif"))));
				
			}
		});
	}
	
	/**
	 * Resets the panel.
	 */
	public void resetPanel(){
		clearFields();
		priceField.setText("$");
		conditionCombo.setSelectedIndex(0);
		bdp.resetPanel();
	}
	
	/**
	 * Updates the format of the panel.<br>
	 * i.e. Change all textField sizes
	 */
	protected void updateFormat(){
		super.updateFormat();
		
		for(JComponent jc : components){
			jc.setFont(defaultFont);
		}
		
		acceptButton.setFont(defaultFont);
	}
	
}//End of class SellBookPanel.java
