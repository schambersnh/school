import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Creates the panel that awaits the user to verify their e-mail. A user can be brought to this panel after the registration process or after the user has tried to change their email address
 * through the ModificationPanel. If the user does not verify their email before leaving this page, the verification key is no longer valid and the user must re-submit the associated forms in order
 * to gain a new verification key.
 * @author Team11
 *
 */
public class ConfirmationPanel extends InputPanel {

	/**
	 * Field where the user enters their confirmation key.
	 */
	private JTextField confirmationKey;
	
	/**
	 * A reference to the main frame all panels display on.
	 */
	private MainFrame mainFrame;
	
	/**
	 * The new user looking to enter the system.
	 */
	private User newUser;
	
	/**
	 * A reference to the ServerAccessPoint needed to formally add the incoming user to the database.
	 */
	private ServerAccessPoint sap;
	
	/**
	 * Determines if the user being confirmed is an old user (who is looking to update their e-mail) or a newly registered user.
	 */
	private String oldEmail;
	
	/**
	 * The generated key used to ensure the user's E-Mail exists.
	 */
	private String verifyKey;
	
	/**
	 * Initializes the Confirmation Panel.
	 * @param nU
	 * The user being confirmed into the system.
	 * @param oE
	 * The email of the user being confirmed, null if first time user.
	 */
	public ConfirmationPanel(User nU, String oE) {
		setLayout(new BorderLayout());
		
		oldEmail = oE;
		mainFrame = MainFrame.getInstance();
		newUser = nU;
		sap = ServerAccessPoint.getInstance();
		
		//title and form panel portions
		JPanel nonImage = new JPanel();
		nonImage.setLayout(new BoxLayout(nonImage, BoxLayout.Y_AXIS));
		String s;
		if(oldEmail != null){
			s = "A re-confirmation message has been sent to your new e-mail. Please type the attached key below to finish the modification process:";
		}
		else{
			s = "A confirmation message has been sent to your e-mail. Please type the attached key below to finish the registration process:";
		}
		nonImage.add(GUIFactory.createTitleBox(s));
		confirmationKey = new JTextField();
		confirmationKey.setColumns(10);
		confirmationKey.setFont(InputPanel.defaultFont);
		
		JPanel jp = new JPanel();
		JLabel jLbl = new JLabel("Confirmation Key:  ");
		jLbl.setFont(InputPanel.defaultFont);
		jp.add(jLbl);
		jp.add(confirmationKey);
		nonImage.add(jp);
		
		nonImage.add(createConfirmationPanel());
		
		add(nonImage, BorderLayout.NORTH);

		//creates the image portion
		add(GUIFactory.createImageBox(ConfirmationPanel.class.getResource("/resources/mailman.gif")), BorderLayout.CENTER);
		verifyKey = generateRandomKey();
		EmailClient.MailThread mail = EmailClient.getInstance().new MailThread(newUser.getEmail(), "UNH BookExchange Verification Key", "Welcome " + newUser.getFirstName() +
				"!\n\nThis key is to verify that the email address you provided is really you. Please copy and paste the registration code below " +
				"into the verification box to get started!\n\nRegistration Key:\n" + verifyKey + "\n\n\nSincerely,\nThe UNH BookExchange Team");
		mail.start();
	}
	
	
	/**
	 * Creates the verify button panel.
	 * @return
	 * JPanel containing the verify button.
	 */
	private JPanel createConfirmationPanel(){
		JPanel lbPanel = new JPanel();
		
		JButton regButton = GUIFactory.createConfirmationButton("Verify");
		lbPanel.add(regButton);
		
		regButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert verify button code here
				if(confirmationKey.getText().equals(verifyKey)){
					clearFields();
					if(oldEmail == null){
						sap.addToUserInformation(newUser.getFirstName(), newUser.getLastName(), newUser.getEmail(), newUser.getPassword());
					}
					else{
						//modify information instead of add new user
						sap.modifyInformation("User Information", "E-Mail", newUser.getEmail(), oldEmail);
					}
					mainFrame.initiateSession(newUser, true);
				}
				else{
					mainFrame.getGlassPane().setVisible(true);
					JOptionPane.showMessageDialog(null, "Your entered an incorrect verification key. Please check your email and try again.\n" +
					"\nNote: Based on your email settings, the email may have been sent to your junk email. Be sure to check there.");
					mainFrame.getGlassPane().setVisible(false);
				}
			}
		});
		
		//set all text fields in this InputPanel to correspond to the register button
		setEnterButton(regButton);
		
		return lbPanel;
	}
	/**
	 * Generates a random key. Used to verify a user's E-Mail.
	 * @return
	 * Random ten character string containing digits, upper case letters, and lower case letters.
	 */
	private String generateRandomKey(){
		Random r = new Random();
		String key = "";
		String randomLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		for(int i = 0; i < 10; i++){
			if(r.nextInt(2)== 1)
				key += 1 + r.nextInt(9);
			else
				key += randomLetters.charAt(r.nextInt(51));
		}
		return key;
	}
	
}//End of class ConfirmationPanel
