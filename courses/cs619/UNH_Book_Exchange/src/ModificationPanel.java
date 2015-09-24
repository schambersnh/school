import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Allows the user to modify their own user information. Only if the user chooses to modify their email field are they required to 
 * go through the verification process again. All fields modified in this panel are updated to the database when a button is clicked.
 * @author Team11
 *
 */
public class ModificationPanel extends InputPanel{
	/**
	 * A possible field for the user to modify.
	 */
	private JTextField firstNameField, lastNameField, emailField, passwordField;
	
	/**
	 * The current user of the program.
	 */
	private User curUser;
	
	/**
	 * Creates the ModificationPanel.
	 * @param cU
	 * The current User.
	 */
	public ModificationPanel(User cU){		
		curUser = cU;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create the title section of form
		add(GUIFactory.createTitleBox("Please edit the following information to modify your account:"));
		
		//first name section of form		
		JLabel firstNameLbl = new JLabel("First Name:  ");
		labels.add(firstNameLbl);
		firstNameField = new JTextField();
		textFields.add(firstNameField);
		
		//last name section of form
		JLabel lastNameLbl = new JLabel("Last Name:   ");
		labels.add(lastNameLbl);
		lastNameField = new JTextField();
		textFields.add(lastNameField);
		
		//email section of form
		JLabel emailLbl = new JLabel("E-Mail:         ");
		labels.add(emailLbl);
		emailField = new JTextField();
		textFields.add(emailField);
		
		//password section of form
		JLabel passwordLbl = new JLabel("Password:    ");
		labels.add(passwordLbl);
		passwordField = new JPasswordField();
		textFields.add(passwordField);

		//create the button
		acceptButton = new JButton("Accept Changes");
		setButtonListener(acceptButton);
		
		//create the form and add it to the panel
		JPanel form = GUIFactory.createFormPanel(labels, textFields, acceptButton)[0];
		add(form);
		
		updateFormat();		
	}
	
	/**
	 * Updates the panel based on what the user modified.
	 */
	public void updatePanel(){
		firstNameField.setText(curUser.getFirstName());
		lastNameField.setText(curUser.getLastName());
		emailField.setText(curUser.getEmail());
		passwordField.setText(curUser.getPassword());
	}
	
	/**
	 * Adds a ButtonListener to a button. Used to update the panel information to display user modifications.
	 * @param button
	 * Button to add the ButtonListener to.
	 */
	private void setButtonListener(final JButton button){
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if(allFieldsValid()){
					MainFrame mainFrame = MainFrame.getInstance();
					
					User modifiedUser = new User(firstNameField.getText(), lastNameField.getText(), 
							emailField.getText(), passwordField.getText());
					
					modifiedUser.setAdmin(false);
					
					clearFields();
					
					updateUserInformation(modifiedUser);
					if(curUser.getEmail().equals(modifiedUser.getEmail())){
						//update account information
						mainFrame.setScrollPanel(new PicturePanel("Your account has been successfully modified!", 
								MainFrame.class.getResource("/resources/checkmark.gif")));
					}
					else{							
						mainFrame.setScrollPanel(new ConfirmationPanel(modifiedUser, curUser.getEmail()));
					}
				}
					
			}
			
		});
		
		//set all text fields in this InputPanel to correspond
		setEnterButton(button);
	}
	
	/**
	 * Updates the user information in both the SQL server and the User class.
	 * @param u
	 */
	public void updateUserInformation(User u){
		ServerAccessPoint sap = ServerAccessPoint.getInstance();
		sap.modifyInformation("User Information", "First Name", u.getFirstName(), curUser.getEmail());
		sap.modifyInformation("User Information", "Last Name", u.getLastName(), curUser.getEmail());
		sap.modifyInformation("User Information", "Password", u.getPassword(), curUser.getEmail());
		
		curUser.setFirstName(u.getFirstName());
		curUser.setLastName(u.getLastName());
		curUser.setPassword(u.getPassword());
	}
	
	/**
	 * Determines if all fields are valid.
	 * @return
	 * true if all fields are valid, false otherwise.
	 */
	private boolean allFieldsValid(){
		trimFields();		
		
		String s = "";
		
		//check for empty fields
		if(isFieldEmpty()){
			s += "\nPlease fill out all fields to update information.";
		}
		
		//check for fields exceeding 40 characters
		if(checkFieldLength()){
			s += "\nNo fields can exceed a length of 40 characters.";
		}
		
		//check email
		if(!Pattern.matches("..*@(..*\\.)*unh\\.edu", emailField.getText())){
			s += "\nA valid UNH e-mail address must be used:\n     Ex: sample@wildcats.unh.edu";
		}
		else if(curUser != null && !curUser.getEmail().equals(emailField.getText())){
			if(RegisterPanel.doesEmailExist(emailField.getText())){
				s += "\nThe e-mail you are trying to use has already been taken. Please select a different valid UNH e-mail.";
			}
		}
		
		if(!s.equals("")){
			MainFrame mainFrame = MainFrame.getInstance();
			mainFrame.getGlassPane().setVisible(true);
			JOptionPane.showMessageDialog(null, s);
			mainFrame.getGlassPane().setVisible(false);
			return false;
		}
		
		return true;
	}
	
}//End of class ModificationPanel
