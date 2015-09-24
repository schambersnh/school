/**
 * Encapsulates current user information.
 * @author Team11
 *
 */
public class User {
  /**
   * Information associated with the user.
   */
  private String userFirstName, userLastName, userEmail, userPassword;
  
  /**
   * True if the user is an administrator on the system, false otherwise.<br>
   * Administrators are able to delete and modify user and book information.
   */
  private boolean isAdmin;
  
/**
 * Initializes the user.
 * @param firstName
 * The first name of the user.
 * @param lastName
 * The last name of the user.
 * @param email
 * The email of the user.
 * @param password
 * The password of the user.
 */
  public User(String firstName, String lastName, String email, String password){
	  userFirstName = firstName;
	  userLastName = lastName;
	  userEmail = email;
	  userPassword = password;
  }
  
  /**
   * @return
   * User First Name
   */
  public String getFirstName(){
	  return userFirstName;
  }
  
  /**
   * @return
   * User Last Name
   */
  public String getLastName(){
	  return userLastName;
  }

  /**
   * @return
   * User email
   */
  public String getEmail(){
	  return userEmail;
  }  
  
  /**
   * @return
   * User Password
   */
  public String getPassword(){
	  return userPassword;
  }
  /**
   * Sets the first name
   */
  public void setFirstName(String firstName){
	  userFirstName = firstName;
  }
  /**
   * Sets the last name
   */
  public void setLastName(String lastName){
	  userLastName = lastName;
  }
  /**
   * Sets the email
   */
  public void setEmail(String email){
	  userEmail = email;
  }
  /**
   * Sets the password
   */
  public void setPassword(String password){
	  userPassword = password;
  }
  /**
   * Sets the Administrative status
   */
  public void setAdmin(boolean b){
	  isAdmin = b;
  }
  
  /**
   * Returns whether or not the user is an Administrator
   */
  public boolean isAdmin() {
	return isAdmin;
  } 
} // End of class User
