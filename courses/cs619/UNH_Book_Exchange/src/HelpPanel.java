import java.awt.BorderLayout;
import javax.swing.JPanel;
/**
 * This class provides a guide if the user does not how to complete a task within the application.
 * @author Team11
 *
 */
public class HelpPanel extends JPanel {

	/**
	 * Initialize the HelpPanel. Uses the GUIFactory to create help sections based on key components of the application.
	 */
	public HelpPanel(){
		setLayout(new BorderLayout());
		
		//create the login help section
		String loginGuide = "1) Press the \"Login or Register\" button at the top of the screen." +
				"\n\n2) Fill out all required sections on the left hand form." +
				"\n\n3) Press \"Confirm\" to compelte the login process.";
		JPanel startSection = GUIFactory.createTitleAndTextPanel("How To Login", loginGuide);
		add(startSection, BorderLayout.NORTH);
		
		//create the register help section
		String registerGuide = "1) Press the \"Login or Register\" button at the top of the screen." +
				"\n\n2) Fill out all required sections on the right hand form.  This includes accepting the Terms of Agreement by selecting the check box underneath the large text area." +
				"\n\n3) Press \"Complete Registration\"." +
				"\n\n4) A confirmation e-mail will be sent to the specified e-mail address.  To finish the registration process you must open this e-mail, type the verification code into \"Confirmation Key\" box, and press \"Verify\"";
		JPanel helpFormPanel = GUIFactory.createTitleAndTextPanel("How To Register", registerGuide);
		startSection.add(helpFormPanel, BorderLayout.CENTER);
		
		//create the sell a book help section
		String sellGuide = "1) Press the \"My Account\" button at the top of the screen after completing the login or register process." +
						"\n\n2) Press the \"Sell a Book\" button in the newly brought up screen." +
						"\n\n3) Fill out all required sections on the form." +
						"\n\n4) Press the \"Preview Sale\" button to generate a preview of the sale below." +
						"\n\n5) If you are satisfied with what your sale looks like, press the \"Sell this Book!\" button.  If you would like to change any information, edit the above fields and repeat step 4.";
		JPanel helpFormPanel_2 = GUIFactory.createTitleAndTextPanel("How To Sell a Book", sellGuide);
		helpFormPanel.add(helpFormPanel_2, BorderLayout.CENTER);
		
		//create the search books help section
		String searchGuide = "1) Press the \"Search all Books\" button at the top of the screen after completing the login or register process." +
						"\n\n2) Using the drop down menu, select a search category." +
						"\n\n3) Type what you want to search for in the adjacent text area." +
						"\n\n4) Press the \"Go\" button to bring up all books that match the specified search criteria in a table at the bottom of the screen." +
						"\n\n5) Double click a book in the table to find out more information about that book.";
		JPanel helpFormPanel_3 = GUIFactory.createTitleAndTextPanel("How To Search and Browse Books", searchGuide);
		helpFormPanel_2.add(helpFormPanel_3, BorderLayout.CENTER);
		
		//create the buy a book help section
		String buyGuide = "1) After selecting the book you wish to buy from the search screen, press the \"Buy This Book\" button to the right of the book's image." +
						"\n\n2) A new screen will appear with the book you wish to purchase and relative information.  Below this, there will be a subject message and message body field.  Fill out these fields and select \"Ok\" in order to send an e-mail to the seller of the book.  Your name and e-mail address will automatically be appended to the bottom of the e-mail, ensuring the seller is able to get back to you if he or she is interested.";
		JPanel helpFormPanel_4 = GUIFactory.createTitleAndTextPanel("How To Buy a Book", buyGuide);
		helpFormPanel_3.add(helpFormPanel_4, BorderLayout.CENTER);
		
		//create the how to view, edit, or remove current books help section
		String cbGuide = "1) Press the \"My Account\" button at the top of the screen after completing the login or register process." +
						"\n\n2) At the bottom of the new screen, a table will appear displaying all books you are currently selling.  Select the book to view more information about it." +
						"\n\n3) If you wish to edit information or delete a book, double click the book in the table." +
						"\n\n4) A new screen will appear follow the form according to how you want to change this book.";
		JPanel helpFormPanel_5 = GUIFactory.createTitleAndTextPanel("How To View, Remove, or Edit Books You Are Currently Selling", cbGuide);
		helpFormPanel_4.add(helpFormPanel_5 , BorderLayout.CENTER);
		
		//create the edit account information help section
		String eGuide = "1) Press the \"My Account\" button at the top of the screen after completing the login or register process." +
						"\n\n2) Press the \"Modify Account\" button in the newly brought up screen." +
						"\n\n3) Select the fields you would like to change and edit the corresponding text areas." +
						"\n\n4) Press the \"Update Account\" button to confirm your account modifications. Note that if you change your e-mail you will be required to re-verify the e-mail by typing in a confirmation key that is sent to the new e-mail.";
		JPanel helpFormPanel_6 = GUIFactory.createTitleAndTextPanel("How To Edit Account Information", eGuide);
		helpFormPanel_5.add(helpFormPanel_6, BorderLayout.CENTER);
		
		//create the retrieve password help section
		String retGuide = "1) Press the \"Login or Register\" button at the top of the screen." +
				"\n\n2) Select the \"Forgot password\" button underneath the password field." +
				"\n\n3) In the new screen, enter your e-mail address associated with the account.  Press the \"E-Mail Password\" button.  An e-mail will be sent to the specified address with the corresponding password.";
		JPanel helpFormPanel_7 = GUIFactory.createTitleAndTextPanel("How To Retrieve a Lost Password", retGuide);
		helpFormPanel_6.add(helpFormPanel_7, BorderLayout.CENTER);
		
		//create the customization help section
		String cGuide = "1) Press the \"My Account\" button at the top of the screen after completing the login or register process." +
				"\n\n2) Press the \"Customize Tables\" button in the newly brought up screen." +
				"\n\n3) Select the fields you would like to change by pressing the corresponding boxes. A preview of your changes will be generated in the table below" +
				"\n\n4) To restore the table to its default settings, press the \"Revert to Defaults\" button below all modifiable fields. Then continue to step 5 to confirm these settings or return to step 4 to continue modifying the table." +
				"\n\n5) To confirm changes made, select the \"Accept Changes\" button below all modifiable fields. All tables will now be updated based on the selected settings.";
		JPanel helpFormPanel_8 = GUIFactory.createTitleAndTextPanel("How To Customize Book Tables", cGuide);
		helpFormPanel_7.add(helpFormPanel_8, BorderLayout.CENTER);
		
		//create the customization help section
		String loGuide = "1) Press the \"Logout\" button at the top of the screen after completing the login or register process.";
		JPanel helpFormPanel_9 = GUIFactory.createTitleAndTextPanel("How To Logout", loGuide);
		helpFormPanel_8.add(helpFormPanel_9, BorderLayout.CENTER);
		
		//add bottom space
		helpFormPanel_8.add(GUIFactory.resizedVerticalStrut(20), BorderLayout.SOUTH);
	}
	
}//End of class HelpPanel.java