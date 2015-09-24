
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextAttribute;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Creates a panel for the login page. In order to successfully use this page, the user must already be registered in the database. If the
 * user has registered in the database, but forgotten their password, they can use this panel for password retrieval with their registered email.
 * @author Team11
 *
 */
public class LoginPanel extends InputPanel {

	/**
	 * The email field of the login panel.
	 */
	private JTextField emailField;
	
	/**
	 * The password field of the login panel.
	 */
	private JTextField passwordField;
	
	/**
	 * Instance of the server used to verify users logging.
	 */
	private ServerAccessPoint sap;
	
	/**
	 * Instance of the main frame all panels are displayed on.
	 */
	private MainFrame mainFrame;
	
	/**
	 * A clickable label used to send lost passwords to a user's E-mail.
	 */
	private ClickableLabel forgotPassword;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		sap = ServerAccessPoint.getInstance();
		mainFrame = MainFrame.getInstance();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create the title section of form
		add(GUIFactory.createTitleBox("Login"));
		
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
		acceptButton = new JButton("Login");
		setButtonListener(acceptButton);
				
		//create the form and add it to the panel
		JPanel formArr[] = GUIFactory.createFormPanel(labels, textFields, null);
		JPanel form = formArr[0];
		JPanel form_2 = formArr[1];
		
		JPanel form_3 = new JPanel();
		forgotPassword = new ClickableLabel("Forgot your password?");
		
		form_3.add(Box.createHorizontalStrut(100));
		form_3.add(acceptButton);
		form_3.add(forgotPassword);
		form_2.add(form_3, BorderLayout.NORTH);
		add(form);
				
		updateFormat();
	}
	
	/**
	 * 
	 * @author Team11
	 *	A clickable label used to enable the user to retrieve their password.
	 */
	private class ClickableLabel extends JLabel implements MouseMotionListener, MouseListener{

		/**
		 * Creates a blue underlined label set to the given text.
		 * Initializes the mouse listeners.
		 * @param text
		 * The text the label will contain.
		 */
		private ClickableLabel(String text){
			super(text);
			setForeground(Color.BLUE);
			Map map = getFont().getAttributes();
			map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			setFont(new Font(map));
			addMouseMotionListener(this);
			addMouseListener(this);
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {}

		@Override
		/**
		 * If the mouse is placed over the label the cursor changes to a hand cursor
		 * in order to let the user know this label is clickable.
		 */
		public void mouseMoved(MouseEvent e) {
		    if( this.contains( e.getPoint() ) ){
		      this.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) ) ;
		    }
		    else{
		      this.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) ) ;
		    }
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		/**
		 * Upon pressing the label, the user can fill out a form to retrieve their password.
		 */
		public void mousePressed(MouseEvent e) {
			mainFrame.getGlassPane().setVisible(true);
			String userEmail = JOptionPane.showInputDialog(null, "Please enter your email in the field below to retrieve your password.");
			mainFrame.getGlassPane().setVisible(false);
			ResultSet rs = sap.retrieveInfo("User Information", "", "E-Mail", userEmail, true);
			String userPassword = "notValid";
			String firstName = "";
			try {
				if(rs.next()){
					firstName = rs.getString("First Name");
					userPassword = rs.getString("Password");
				}
			} catch (NumberFormatException nee) {
				nee.printStackTrace();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			if(userPassword.equals("notValid")){
				mainFrame.getGlassPane().setVisible(true);
				JOptionPane.showMessageDialog(null, "The email provided does not exist in the database.\nPlease make sure that you have entered your email address and try again.");
				mainFrame.getGlassPane().setVisible(false);
			}
			else{
				String message = "Hello " + firstName + 
						",\n\nIt seems that you have forgotten your password. We have provided it for you below:\n" + 
						userPassword + "\n\nSincerely,\nThe UNH BookExchange Team";
				
				EmailClient.MailThread mail = EmailClient.getInstance().new MailThread(userEmail, "Forgotten Password", message);
				mail.start();
				mainFrame.setScrollPanel(new PicturePanel("Your password retrieval e-mail has been successfully sent!", 
					LoginPanel.class.getResource("/resources/checkmark.gif")));
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	/**
	 *	Creates the login confirmation button panel.
	 */
	private void setButtonListener(final JButton button){
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//insert confirm login button code here
				if(allFieldsValid()){
					ResultSet rs = sap.retrieveInfo("User Information", "", "E-Mail", emailField.getText(), true);
					String pass = "";
					User newUser = null;
					
					try{
						rs.next();
						pass = rs.getString("Password");
						if(pass.equals(passwordField.getText())){
							newUser = new User(rs.getString("First Name"), rs.getString("Last Name"), 
									rs.getString("E-Mail"), rs.getString("Password"));
							if(rs.getInt("Status") == 1){
								newUser.setAdmin(true);
							}
						}	
					} 
					catch (SQLException sqe){
						newUser = null;
					}
					
					if(newUser != null){
						clearFields();
						mainFrame.initiateSession(newUser, false);
					}
					else{
						mainFrame.getGlassPane().setVisible(true);
						JOptionPane.showMessageDialog(null, "Your account could not be verified. Please check your e-mail and password and try again.");
						mainFrame.getGlassPane().setVisible(false);
					}
				}
			}
		});
		
		//set all text fields in this InputPanel to correspond to the login button
		setEnterButton(button);
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
		
		if(!s.equals("")){
			mainFrame.getGlassPane().setVisible(true);
			JOptionPane.showMessageDialog(null, s);
			mainFrame.getGlassPane().setVisible(false);
			return false;
		}
		
		return true;
	}
	
}//End of class LoginPanel.java