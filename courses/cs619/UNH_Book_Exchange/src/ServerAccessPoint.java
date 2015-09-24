import java.sql.*;

/**
 * The communication medium between the application and the SQL server. The ServerAccessPoint is used to update all tables within
 * the database. The ServerAccessPoint implements the Singleton design pattern in order to ensure that there is only one object capable
 * of modifying the database. The ServerAccessPoint is capable of many SQL operations, such as modifying a table entry, deleting a table entry, and
 * adding a table entry.
 * @author Team11
 *
 */
public class ServerAccessPoint {

	/**
	 * The single instance of ServerAccessPoint.
	 */
	private static ServerAccessPoint sap;
	
	/**
	 * The connection associated with this ServerAccessPoint.
	 */
	private Connection conn;
	
	/**
	 * Private constructor for singleton design pattern.
	 */
	private ServerAccessPoint(){}
	
	/**
	 *	Returns the ServerAccessPoint.
	 */
	public static ServerAccessPoint getInstance(){
		if(sap == null){
			sap = new ServerAccessPoint();
		}

		return sap;
	}
	
	/**
	 * Attempts to connect to the mySQL server. Returns false if unsuccessful.
	 */
	public boolean connect(){
		try {
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1", "root", "");
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Attempts to disconnect from the server.
	 * @return
	 * true if successful, false otherwise.
	 */
	public boolean disconnect(){
		try{
			conn.close();
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	/**
	 *
	 *  Retrieves the ResultSet for the specified parameters.<br><br>
	 *
	 *	EXAMPLE:<br>
	 *		ResultSet rs = sap.retrieveInfo("UserInformation", "", "Wildcats_ID", "2", false);<br>
	 *		//the above line will search for any wildcats id in the UserInformation table that has an s in it<br>
	 *		rs.next()<br>
	 *		String s = rs.getString("Desired_Field");<br>
	 *  Make sure to include in a try catch method<br>
	 * @param table
	 * The table name in the SQL server.
	 * @param field
	 * Which field the information will retrieve, "" for wildcard.
	 * @param specificField
	 * Which specific field is being looked through.
	 * @param specificSearch
	 * What is going to be searched for in the specified field.
	 * @param exactMatch
	 * If specified search must EXACTLY match.
	 * @return
	 * ResultSet containing the desired info.
	 */	
	public ResultSet retrieveInfo(String table, String field, String specificField, String specificSearch, boolean exactMatch){
		String sql = "SELECT ";
		
		if(field.equals("")){
			sql += "*";
		}
		else{
			sql += field;
		}
		
		sql += " from `" + table;
		
		if(!specificField.equals("")){
			if(exactMatch)
				sql += "` WHERE `" + specificField + "`='" + specificSearch + "'";
			else
				sql += "` WHERE `" + specificField + "`LIKE'" + specificSearch + "%'";
		}

		return executeQuery(sql);
	}
	
	
	/**
	 * Returns a result set for the given search.<br>
	 * 		(Not to be confused with executeUpdate, which returns a boolean and is used for table updates)
	 * Returns null if a problem is encountered.
	 * @param sqlStr
	 * The SQL statement to perform.
	 * @return
	 * ResultSet containing the desired info, null if problem encountered.
	 */
	private ResultSet executeQuery(String sqlStr){
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(sqlStr);
			ResultSet rs = ps.executeQuery();
			return rs;
		}
		catch (SQLException e) {
				return null;
		}
	}
	
	/**
	 * Inserts an entry into the User Information table. Also inserts their customization choices into the Custom Information table.
	 *
	 * @param firstName
	 * The first name of the user.
	 * @param lastName
	 * The last name of the user.
	 * @param email
	 * The email of the user.
	 * @param password
	 * The password of the user.
	 * @return
	 * true if entry was successfully added. false if otherwise.
	 */
	public boolean addToUserInformation(String firstName, String lastName, String email, String password){
		String[] input = {firstName, lastName, email, password};
		String[] output = clean(input);
		
		String sqlStr = "INSERT INTO `User Information`(`First Name`, `Last Name`, `E-Mail`, `Password`) " +
				"VALUES ('" + output[0] + "','" + output[1] + "','" + output[2] + "','" + output[3] + "')";
		
		String sqlCust = "INSERT INTO `Custom Information`(`E-Mail`, `Highlighting Color`, `Even Row Color`, `Odd Row Color`, `Header Font Type`, `Header Font Style`, `Header Font Size`, `Table Font Type`, `Table Font Style`, `Table Font Size`) " +
				"VALUES ('" + email  + "','" + ColorTable.COLORHIGHLIGHT.getRGB() + "','" + ColorTable.COLOREVEN.getRGB() + "','" + ColorTable.COLORODD.getRGB() + "','" + ColorTable.FONTHEADER.getName() + "','" + ColorTable.FONTHEADER.getStyle() + "','" + ColorTable.FONTHEADER.getSize() + "','" + ColorTable.FONTTABLE.getName() + "','" + ColorTable.FONTTABLE.getStyle() + "','" + ColorTable.FONTTABLE.getSize() + "')";

		
		System.out.println("Adding to User Information: " + sqlStr);
		return (executeUpdate(sqlStr) && executeUpdate(sqlCust));
	}
	/**
	 * Inserts entry into the Book Information table.
	 * @param email
	 * The seller of the book.
	 * @param isbn
	 * The ISBN of the book.
	 * @param isbn13
	 * The ISBN13 of the book.
	 * @param title
	 * The title of the book.
	 * @param author
	 * The author of the book.
	 * @param condition
	 * The condition of the book.
	 * @param price
	 * The price of the book.
	 * @return
	 * true if book is successfully added, false if otherwise.
	 */
	public boolean addToBookInformation(String email, String isbn, String isbn13, String title, String author, String condition, String price){
		String[] input = {email, isbn, isbn13, title, author, condition, price};
		String[] output = clean(input);
		
		String sqlStr = "INSERT INTO `Book Information`(`E-Mail`, `ISBN`, `ISBN13`, `Title`, `Author`, `Condition`, `Price`) " +
				"VALUES ('" + output[0] + "','" + output[1] + "','" + output[2] + "','" + output[3] + "','" + output[4] + "','" + output[5] + "','" + output[6] + "')";
		
		System.out.println("Adding to Book Information: " + sqlStr);
		return executeUpdate(sqlStr);
	}
	
	/**
	 * Inserts entry into Transaction table.
	 * @param buyer
	 * The user requesting to buy the book.
	 * @param seller
	 * The seller of the book.
	 * @param id
	 * The id of the book.
	 * @param requestDate
	 * The date that the user requested to buy the book.
	 * @return
	 * true if successfully added, false if otherwise.
	 */
	public boolean addTransaction(String buyer, String seller, String id, String requestDate){
		String[] input = {buyer, seller, id, requestDate};
		String[] output = clean(input);
		
		String sqlStr = "INSERT INTO `Transaction`(`Buyer`, `Seller`, `ID`, `Request Date`) " +
				"VALUES ('" + output[0] + "','" + output[1] + "','" + output[2] + "','" + output[3] + "')";
		
		System.out.println("Adding to Transaction Information: " + sqlStr);
		return executeUpdate(sqlStr);
	}
	/**
	 * Closes a transaction.
	 * @param date
	 * The date that the book is marked sold.
	 * @return
	 * true if successfully closed, false if otherwise.
	 */
	public boolean closeTransaction(String date)
	{
		String sqlStr = "INSERT INTO `Transaction`(`EndDate`) " +
				"VALUES ('" + date.replace("'", "\'") + "')";
		
		System.out.println("Closing Transaction: " + sqlStr);
		return executeUpdate(sqlStr);
	}
	
	/**
	 * Modifies information in the specified table based on primary key match.
	 * @param table
	 * The table that is being modified.
	 * @param category
	 * The field that is being modified.
	 * @param newValue
	 * The new value of the field.
	 * @param pKeyVal
	 * The value of the tables primary key.
	 * @return
	 * true if the associated update executed successfully, false if otherwise.
	 */
	public boolean modifyInformation(String table, String category, String newValue, String pKeyVal){
		String sqlStr = "UPDATE `" + table + "` SET `" + category + "`='" + newValue.replace("'", "\'") + "' WHERE `" + 
				   determinePrimaryKey(table) + "`='" + pKeyVal + "'";
		
		System.out.println("Modifying Information: " + sqlStr);
		
		return executeUpdate(sqlStr);
	}
	
	/**
	 * Executes the given update statement. Used for making modifications to a table.<br>
	 *		(Not to be confused with executeQuery, which returns a result set and is used for searches).
	 * 
	 * @param sqlStr
	 * The SQL statement to perform.
	 * @return
	 * true if update is executed successfully, false if otherwise.
	 */
	private boolean executeUpdate(String sqlStr){
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sqlStr);
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Deletes an entry from the table<br><br>
	 * 
	 * ex: To delete a book.<br><br>
	 * 
	 * deleteEntry("Book Information", "ID", 5);
	 * @param table
	 * The table that information is being deleted from.
	 * @param category
	 * The field that is being deleted.
	 * @param value
	 * The category value.
	 * @return
	 * true if the associated update executed successfully, false if otherwise.
	 */
	public boolean deleteEntry(String table, String category, String value){
		  String sqlStr = "DELETE FROM `" + table + "` WHERE `" + category + "`='" + value + "'";
		  
		  System.out.println("Deleting information: " + sqlStr);
		  return executeUpdate(sqlStr);

	}
	
	 /**
	  * Returns the primary key based on the table name.
	  * @param table
	  * The table that the primary key should be fetched from.
	  * @return
	  * The primary key of the table parameter.
	  */
	 public String determinePrimaryKey(String table){
		 if(table.equals("User Information") || table.equals("Custom Information"))
			 return "E-Mail";
		 else
			 return "ID";	 
	 }
	 
	 /**
	  * Readies the input for the SQL server.
	  * @param input
	  * The string to be changed.
	  * @return
	  * A new string representing the SQL server ready version of the string passed in.
	  */
	 public String[] clean(String[] input){
		 for(int i = 0; i < input.length; i++){
			   input[i] = input[i].replace("'", "\\'");
		 }
		 return input;
	}
}//End of class ServerAccessPoint
