
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Creates a panel for the registration page. The user must fill out fields containing their first name, last name, email and pasword.
 * THe email used must be verified in the next panel when the user presses the registration button. Additionally, the user must ensure
 * that they accept the terms of agreement by checking the box under the terms of agreement.
 * @author Team11
 *
 */
public class RegisterPanel extends InputPanel {

	/**
	 * Fields allowing the user to enter their respective information.
	 */
	private JTextField firstNameField, lastNameField, passwordField, emailField;
	/**
	 * Check box ensuring that the user has read the terms of agreement.
	 */
	private JCheckBox checkBox;
	/**
	 * Ensures that the screen is visible on any frame size by adding a scroll bar.
	 */
	private JScrollPane uaScrollPane;
	
	/**
	 * The index of the row in the table selected by the user
	 */
	private MainFrame mainFrame;
	
	/**
	 * Create the panel.
	 */
	public RegisterPanel() {
		
		mainFrame = MainFrame.getInstance();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create the title section of form
		add(GUIFactory.createTitleBox("Not a user? Please enter the following information to register:"));
		
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
		
		//create the form and add it to the panel
		JPanel[] formArr = GUIFactory.createFormPanel(labels, textFields, null);
		add(formArr[0], BorderLayout.CENTER);
		
		JPanel form_2 = createUserAgreementPanel();
		formArr[1].add(form_2, BorderLayout.CENTER);
		
		//create the button
		acceptButton = new JButton("Accept Changes");
		setButtonListener(acceptButton);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(acceptButton);
		form_2.add(buttonPanel, BorderLayout.CENTER);
		
		updateFormat();
	}
	
	/**
	 * Creates the user agreement panel.
	 */
	private JPanel createUserAgreementPanel(){
		//make the user agreement only take up the specified section
		JPanel uaPanel = new JPanel();
		uaPanel.setLayout(new BorderLayout());
		
		JPanel uaSubPanel = new JPanel();
		uaSubPanel.setLayout(new BoxLayout(uaSubPanel, BoxLayout.Y_AXIS));
		
		Component verticalStrut = GUIFactory.resizedVerticalStrut(15);
		uaSubPanel.add(verticalStrut);
		
		//create the user agreement text area
		String userAgreement = "PLEASE READ THE TERMS AND CONDITIONS OF USE CAREFULLY BEFORE USING THIS APPLICATION. This application is free to use by UNH members. And by using this application, you the user are agreeing to comply with and be bound by the following terms of use. After reviewing the following terms and conditions thoroughly, if you do not agree to the terms and conditions, please do not use this application."
			+ "\n\nAcceptance of Agreement. You agree to the terms and conditions outlined in this Terms and Conditions of use Agreement with respect to our application (The UNH Textbook Exchange). This Agreement constitutes the entire and only agreement between us and you, and supersedes all prior or contemporaneous agreements, representations, warranties and understandings with respect to the application, the content,  services provided by or listed on the Application, and the subject matter of this Agreement. This Agreement may be amended by us at any time and at any frequency without specific notice to you. The latest Agreement will be posted on the Site, and you should review this Agreement prior to using the Site."
			+ "\n\nCopyright. The content, organization, graphics, design, and other matters related to the Site are protected under applicable copyrights and other proprietary laws, including but not limited to intellectual property laws. The copying, reproduction, use, modification or publication by you of any such matters or any part of the application is strictly prohibited, without prior written permission."
			+ "\n\nDeleting and Modification. We reserve the right in our sole discretion, without any obligation and without any notice requirement to you, to edit or delete any documents, information or other content appearing on the application, including this Agreement."
			+ "\n\nIndemnification. You agree to indemnify, defend and hold us, our officers, our share holders, our partners, attorneys and employees harmless from any and all liability, loss, damages, claim and expense, including reasonable attorney's fees, related to your violation of this Agreement or use of the application."
			+ "\n\nDisclaimer. THE CONTENT, SERVICES, FREE PRODUCT SAMPLES AND FREEBIE OFFERS FROM OR LISTED THROUGH THE SITE ARE PROVIDED \"AS-IS,\" \"AS AVAILABLE,\" AND ALL WARRANTIES, EXPRESS OR IMPLIED, ARE DISCLAIMED, INCLUDING BUT NOT LIMITED TO THE DISCLAIMER OF ANY IMPLIED WARRANTIES OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY, QUALITY AND FITNESS FOR A PARTICULAR PURPOSE, WITH RESPECT TO THIS SITE AND ANY WEBSITE WITH WHICH IT IS LINKED. THE INFORMATION AND SERVICES MAY CONTAIN BUGS, ERRORS, PROBLEMS OR OTHER LIMITATIONS. WE HAVE NO LIABILITY WHATSOEVER FOR YOUR USE OF ANY INFORMATION OR SERVICE. IN PARTICULAR, BUT NOT AS A LIMITATION, WE ARE NOT LIABLE FOR ANY INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING DAMAGES FOR LOSS OF BUSINESS, LOSS OF PROFITS, LOSS OF MONEY, LITIGATION, OR THE LIKE), WHETHER BASED ON BREACH OF CONTRACT, BREACH OF WARRANTY, NEGLIGENCE, PRODUCT LIABILITY OR OTHERWISE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGES. THE NEGATION OF DAMAGES SET FORTH ABOVE ARE FUNDAMENTAL ELEMENTS OF THE BASIS OF THE BARGAIN BETWEEN US AND YOU THE USER. THIS SITE AND THE INFORMATION WOULD NOT BE PROVIDED WITHOUT SUCH LIMITATIONS. NO ADVICE OR INFORMATION, WHETHER ORAL OR WRITTEN, OBTAINED BY YOU FROM US THROUGH THE SITE SHALL CREATE ANY WARRANTY, REPRESENTATION OR GUARANTEE NOT EXPRESSLY STATED IN THIS AGREEMENT. THE INFORMATION AND ALL OTHER MATERIALS ON THE SITE ARE PROVIDED FOR GENERAL INFORMATION PURPOSES ONLY AND DO NOT CONSTITUTE PROFESSIONAL ADVICE. IT IS YOUR RESPONSIBILITY TO EVALUATE THE ACCURACY AND COMPLETENESS OF ALL INFORMATION AVAILABLE ON THIS SITE OR ANY WEBSITE WITH WHICH IT IS LINKED."
			+ "\n\nLimits. All responsibility or liability for any damages caused by viruses contained within the electronic file containing the form or document is disclaimed. We will not be liable to you for any incidental, special or consequential damages of any kind that may result from use of or inability to use the site."
			+ "\n\nThird-Party Website. All rules, terms and conditions, other policies (including privacy policies) and operating procedures of third-party linked websites will apply to you while on such websites. We are not responsible for the content, accuracy or opinions express in such Websites, and such Websites are not investigated, monitored or checked for accuracy or completeness by us. Inclusion of any linked Website on our Site does not imply approval or endorsement of the linked Website by us. This Site and the third-party linked websites are independent entities and neither party has authority to make any representations or commitments on behalf of the other. If you decide to leave our Site and access these third-party linked sites, you do so at your own risk."
			+ "\n\nThird-Party Products and Services. We advertise third-party linked websites from which you may purchase or otherwise obtain certain sample goods, freebie offerings or free trial services. You understand that we do not operate or control the products, free offerings or services offered by third-party linked websites. Third-party linked websites are responsible for all aspects of order processing, fulfillment, billing and customer service. We are not a party to the transactions entered into between you and third-party linked websites. You agree that use of such third-party linked websites is at your sole risk and is without warranties of any kind by us, expressed, implied or otherwise. Under no circumstances are we liable for any damages arising from the transactions between you and third-party linked websites or for any information appearing on third-party linked websites or any other site linked to or from our site."
			+ "\n\nSubmissions. All suggestions, ideas, notes, concepts and other information you may send to us (collectively, \"Submissions\") shall be deemed and shall remain our sole property and shall not be subject to any obligation of confidence on our part. Without limiting the foregoing, we shall be deemed to own all known and hereafter existing rights of every kind and nature regarding the Submissions and shall be entitled to unrestricted use of the Submissions for any purpose, without compensation to the provider of the Submissions."
			+ "\n\nGeneral. You agree that all actions or proceedings arising directly or indirectly out of this agreement, or your use of the site or any sample products, freebie offers or services obtained by you through such use, shall be litigated in the circuit court of Los Angeles County, California or the United States District Court for the Central District of California. you are expressly submitting and consenting in advance to such jurisdiction in any action or proceeding in any of such courts, and are waiving any claim that Los Angeles, California or the central district of California is an inconvenient forum or an improper forum based on lack of venue. This site is controlled by Perfect Insight, Inc. in the State of California, USA. As such, the laws of California will govern the terms and conditions contained in this Agreement and elsewhere throughout the Site, without giving effect to any principles of conflicts of laws.";
		
		JTextArea tAUA = new JTextArea(userAgreement, 15 , 50);
		tAUA.setLineWrap(true);
		tAUA.setEditable(false);
		tAUA.setWrapStyleWord(true);	

		//make the user agreement scroll-able
		JPanel uataPanel = new JPanel();
		uaScrollPane = new JScrollPane(tAUA);
		uaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		uaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		uataPanel.add(uaScrollPane);
		
		uaSubPanel.add(uataPanel);
		
		//creates check box section to verify user read the user agreement
		JPanel acceptPanel = new JPanel();
		JLabel lblAcceptUA = new JLabel("Before continuing, please accept the user agreement");
		acceptPanel.add(lblAcceptUA);
				
		checkBox = new JCheckBox();
		acceptPanel.add(checkBox);
		
		uaSubPanel.add(acceptPanel);
		
		uaPanel.add(uaSubPanel, BorderLayout.NORTH);
		
		return uaPanel;
	}
	
	/**
	 *	Clears text fields, turns the check box off, and scrolls the ToA to the top.
	 */
	public void resetFields(){
		JScrollBar verticalScrollBar = uaScrollPane.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = uaScrollPane.getHorizontalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMinimum());
		horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
		checkBox.setSelected(false);
		clearFields();
		
	}
	
	/**
	 *	Checks to see if all sections of the registration form are valid.
	 */
	private boolean allFieldsValid(){
		String s = "";
		
		//check for empty fields
		if(isFieldEmpty()){
			s += "\nPlease fill out all fields to register.";
		}
		
		//check for fields exceeding 40 characters
		if(checkFieldLength()){
			s += "\nNo fields can exceed a length of 40 characters.";
		}
		
		//check email
		if(!Pattern.matches("..*@(..*\\.)*unh\\.edu", emailField.getText())){
			s += "\nA valid UNH e-mail address must be used:\n     Ex: sample@wildcats.unh.edu";
		}
		else if(doesEmailExist(emailField.getText())){
			s += "\nThe e-mail you are trying to use has already been taken. Please select a different valid UNH e-mail.";
			
		}
		
		//check accepted ToA
		if(!checkBox.isSelected()){
			s += "\nPlease Accept the Terms of Agreement by checking the box under the Terms of Agreement.";
		}
		
		
		if(!s.equals("")){
			mainFrame.getGlassPane().setVisible(true);
			JOptionPane.showMessageDialog(null, s);
			mainFrame.getGlassPane().setVisible(false);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks to see if the email already exists in the database.
	 * @param email
	 * The email being verified.
	 * @return
	 * true if email is verified, false if otherwise.
	 */
	public static boolean doesEmailExist(String email){
		//check to see if user already exists in the database (check e-mail)
		ServerAccessPoint sap = ServerAccessPoint.getInstance();
		
		ResultSet rs = sap.retrieveInfo("User Information", "", "E-Mail", email, true);
		try {
			if(rs.next()){
				return true;
			}
		}
		catch (HeadlessException he) {
			return true;
		}
		catch (SQLException se) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets the button listener so that the user is modified when clicked.
	 * @param button
	 * The button that the listener is being added to.
	 */
	private void setButtonListener(final JButton button){
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert confirm registration button code here
				trimFields();
				if(allFieldsValid()){
					//NOTE THE USER IS NOT ADDED UNTIL THE CONFIRMATION PANEL
					User modifyUser = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());
					modifyUser.setAdmin(false);
					resetFields();
					
					mainFrame.setScrollPanel(new ConfirmationPanel(modifyUser, null));
					
				}
			}
		});
		
		//set all text fields in this InputPanel to correspond
		setEnterButton(button);
	}
	
}//End of class RegisterPanel.java