import java.io.*;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

/**
 * This class is used to send E-mails to the users of the UNH Book Exchange. It uses an Authenticated SMTP client to connect to 
 * the Google SMTP server (smtp.gmail.com). Once connected, the port is encrypted through TLS and the corresponding SMTP protocol 
 * commands are used to send an email with a specified subject and message.
 * @author Team11
 *
 */
public class EmailClient {
	
	/**
	 * The single instance of the EmailClient.
	 */
	private static EmailClient emailClient;
	
	/**
	 * The client used to send the E-mails.
	 */
	private AuthenticatingSMTPClient client;
	
	/**
	 * Private constructor to implement the Singleton design pattern.
	 */
	private EmailClient()
	{
		try {
			client = new AuthenticatingSMTPClient();
		} catch (NoSuchAlgorithmException e1) {
			System.err.println("Error in init");
			client = null;
		}
	}
	
	/**
	 * A thread that sends an email and then dies. Created in order to reduce lag-time in the interface due to Internet issues.
	 * @author Team11
	 *
	 */
	public class MailThread extends Thread{
		String t, s, m;
		/**
		 * Initialize the thread to contain email information.
		 * @param to
		 * The recipient of the email.
		 * @param sub
		 * The email subject.
		 * @param mes
		 * The email message.
		 */
		public MailThread(String to, String sub, String mes){
			t = to;
			s = sub;
			m = mes;
		}
		/**
		 * Sends an E-mail using the EmailClient sendMail method.
		 */
		public void run(){
			sendEmail(t, s, m);
		}
	}
	
	/**
	 * Allows other classes to access the EmailClient object. Only instantiated once throughout the life of the program.
	 * @return
	 * Singleton EmailClient object.
	 */
	public static EmailClient getInstance(){
		if(emailClient == null){
			emailClient = new EmailClient();
		}
		return emailClient;
	}
	
	/**
	 * Sends an email to the toAddress with a specified subject and message.
	 * @param toAddress
	 * Recipient of the E-mail.
	 * @param subject
	 * Subject of the E-mail.
	 * @param message
	 * Message of the E-mail.
	 * @return
	 * true if E-mail was sent successfully, false if otherwise.
	 */
	public boolean sendEmail(String toAddress, String subject, String message)
	{	
		try {
			client = new AuthenticatingSMTPClient();
		} catch (NoSuchAlgorithmException e1) {
			System.err.println("Error in init");
			client = null;
			//NOTE: this should only happen if internet connection is lost
			//should not let the user send an email if this is the case
		}
		try{	
			int reply;
			//Keep for debugging purposes for now
			
            //client.addProtocolCommandListener(new PrintCommandListener(
              //      new PrintWriter(System.out), true));
            
			//create account and use credentials here
			verifyEmail("unhbookexchange@gmail.com", "cs619jin");
			
			/*NOTE: Only want to connect within email, or else server will be waiting for information 
			/and timeout*/
			
			//Send another EHLO to indicate a new command is coming
			//client.elogin();
			
			//Make sure server is logged in correctly before sending email
			reply = client.getReplyCode();
			
			if(!SMTPReply.isPositiveCompletion(reply)){
				client.disconnect();
				System.err.println("SMTP server refused connection");
			}
			
			//Set the sender and recipient
			client.setSender("unhbookexchange@gmail.com");
			client.addRecipient(toAddress);
			
			//Prepare the server channel for writing data 
			Writer r = client.sendMessageData();
			
			//Make sure the channel was opened successfully
			if(r == null){
				return false;
			}
			
			//Create a header from the passed in fields
			SimpleSMTPHeader header = new SimpleSMTPHeader("unhbookexchange@gmail.com", toAddress, subject);

			//Write the header and message into the channel and close the writer
			r.write(header.toString());
			r.write(message);
			r.close();
			
			//Tell the server you are all done and verify its completion
			
			if(!client.completePendingCommand()){
				return false;
			}
			
			return true;
		}
		catch(IOException e) {
		      if(client.isConnected()) {
		        try {
		          client.disconnect();
		        } catch(IOException f) {
		          // do nothing
		        }
		      }
				return false;
	}
		
	}
	
	/**
	 * Verifies that the UNH Book Exchange email account is valid.
	 * @param email
	 * unhbookexchange@gmail.com
	 * @param password
	 * Password for unhbookexchange@gmail.com
	 * @return
	 * True if validated by the SMTP server, false if otherwise.
	 */
		public boolean verifyEmail(String email, String password){
			//Gmail SMTP server
			try{
			client.connect("smtp.gmail.com", 587);
			//Send an authenticated HELO (EHLO) to the server
			client.elogin();
			
			//Encrypt the channel between the client and server, outlook requires this
			if(!client.execTLS()){
				return false;
			}
		
			
			//Send another EHLO to indicate a new command is coming
			client.elogin();
			
			//User has to be a member of GMail and must be authenticated
				return client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, email, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.println("invalid key");
				return false;
			}
		}
}//End of Class EmailClient.java