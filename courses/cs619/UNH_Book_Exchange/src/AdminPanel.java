import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 * Creates the panel for the Administrative User. The Administrative user is capable of modifying user information, deleting users,
 * and deleting books from the database. <br> An administrative user is not allowed to modify another administrators information, but can
 * promote normal users to administrators.
 * 
 * @author Team11
 */
public class AdminPanel extends JPanel {
	/**
	 * Reference to the main frame all panels are stored on.
	 */
	private static MainFrame mainFrame;
	
	/**
	 * Reference to the ServerAccessPoint used for all database transactions.
	 */
	private static ServerAccessPoint sap;
	
	/**
	 * The type of table associated.
	 */
	private String tableType;
	
	/**
	 * Initializes the AdminPanel.<br><br>
	 * 
	 * Creates two buttons for convenient access to all users and books stored in the database. <br>The "Edit All Users"
	 * button displays all of the users while the "Edit All Books" displays all books.
	 */
	public AdminPanel(){
		mainFrame = MainFrame.getInstance();
		sap = ServerAccessPoint.getInstance();
		
		setLayout(new BorderLayout());
		
		add(GUIFactory.resizedVerticalStrut(50), BorderLayout.NORTH);
		
		JPanel btnPanel = new JPanel();
		
		JButton allBooks = GUIFactory.createIconButton("Edit All Books", MyAccountPanel.class.getResource("/resources/booksicon.gif"));
		allBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert admin books button info here
				mainFrame.setScrollPanel(createAllInfoPanel("Book Information", GUIFactory.bookTableHeader));
			}
		});
		btnPanel.add(allBooks);
		btnPanel.add(GUIFactory.resizedHorizontalStrut(50));
		
		JButton allUsers = GUIFactory.createIconButton("Edit All Users", MyAccountPanel.class.getResource("/resources/boygirlicon.gif"));
		allUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//insert admin account info here
				mainFrame.setScrollPanel(createAllInfoPanel("User Information", GUIFactory.userTableHeader));	
			}
		});
		btnPanel.add(allUsers);
		
		add(btnPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Creates a table based on which button was clicked.
	 * @param type 
	 * Type of table that is being created.<br>
	 * Possible options: User Information, Book Information
	 * @param cNames 
	 * Column names of the table. Based on whatever the table type is.
	 * @return
	 * JPanel containing the JTable
	 */
	private JPanel createAllInfoPanel(String type, String[] cNames){
		tableType = type;
		JPanel allInfoPanel = new JPanel();
		allInfoPanel.setLayout(new BorderLayout());
		
		allInfoPanel.add(GUIFactory.createTitleBox("Modify the " + tableType + " table below by double clicking:"), BorderLayout.NORTH);
		
		JTable finalTable = GUIFactory.createTable(tableType, cNames, "", "", "", true);
		establishMouseListener(finalTable);
		
		JScrollPane jsp = new JScrollPane(finalTable);
				
		
		allInfoPanel.add(jsp, BorderLayout.CENTER);
		return allInfoPanel;
	}
	
	/**
	 * Establishes a MouseListener for the created JTable to allow the deletion and modification of database items.
	 * @param adminTable
	 * Table to which MouseListener is added.
	 */
	private void establishMouseListener(final JTable adminTable){
		adminTable.addMouseListener(new MouseAdapter() {
			   @Override
			   public void mouseClicked(MouseEvent e) {
				   if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {	
					   if(tableType.equals("User Information"))
						   editTable(adminTable, "User Information", true);
					   else
						   editTable(adminTable, "Book Information", true);
				   }
			    }
			    
			}
		);
	}
	/**
	 * Adds double-clicking functionality to the JTable.<br><br>
	 * 
	 * Delete: Deletes an item from the database<br>
	 * Modify: Modifies a field of an item in the database<br>
	 * 
	 * @param table
	 * The JTable being edited.
	 * @param tableType
	 * The type of JTable.
	 * @param isAdmin
	 * true: All fields available for modification.<br>
	 * false: Only user-controlled fields are available for modification.
	 */
	public static void editTable(JTable table, String tableType, boolean isAdmin){
		  if(sap == null){
			   sap = ServerAccessPoint.getInstance();
		   }
		  
		  if(mainFrame == null){
			  mainFrame = MainFrame.getInstance();
		  }
		
		   int[] selection = table.getSelectedRows();
		   for (int i = 0; i < selection.length; i++) {
		     selection[i] = table.convertRowIndexToView(selection[i]);
		   }
		  
		   //determine which table is being clicked
		   String tableInformation = "";
		    
		   String[] myHeader;
		   String pKey = "";
		   int pKeyIndex;
		   DefaultTableModel updateModel;
		
		   if(tableType.equals("User Information")){
			   myHeader = GUIFactory.userTableHeader;
			   pKey = sap.determinePrimaryKey(tableType);
			   pKeyIndex = 2;
		   }
		   else{
			   myHeader = GUIFactory.bookTableHeader;
			 
			   pKey = sap.determinePrimaryKey(tableType);
			   pKeyIndex = 0;
		   }
		   
			//get the selected row   
		    int selectedRowIndex = table.getSelectedRow();
		    System.out.println("selected row: " + selectedRowIndex);
		    TableModel t = table.getModel();
		    updateModel = (DefaultTableModel)t;
		    System.out.println(table.getValueAt(0,0));
		   
		    //Set visibility of information
		    if(isAdmin){
			    for(int i = 0; i < myHeader.length; i++){
			    	tableInformation += myHeader[i] + ": " + table.getValueAt(selectedRowIndex, i) + "\n";
			    }
		    }
		    else{
				    	tableInformation += myHeader[6] + ": " + table.getValueAt(selectedRowIndex,6 ) + "\n" +
				    			myHeader[7] + ": " + table.getValueAt(selectedRowIndex, 7) + "\n";
		    }
		    
		    //Prompt the user for deleting or modification
		    String cmd;
		    String[] possibleValues = {"Delete", "Modify"};
		    
		    if(!isAdmin){
			    
			    mainFrame.getGlassPane().setVisible(true);
			    cmd = (String) JOptionPane.showInputDialog(null, tableInformation + "\n\n" + 
			    "Please select an option", "Input", JOptionPane.INFORMATION_MESSAGE, null,
			    possibleValues, possibleValues[0]);
			    mainFrame.getGlassPane().setVisible(false);
		    }
		    else{
			   if(tableType.equals("User Information")){
				   
				   if(!table.getValueAt(selectedRowIndex, 4).equals("1")){
					   cmd = (String) JOptionPane.showInputDialog(null, tableInformation + "\n\n" + 
							    "Please select an option", "Input", JOptionPane.INFORMATION_MESSAGE, null,
							    possibleValues, possibleValues[0]);
							    mainFrame.getGlassPane().setVisible(false);
				   }
				   else{
				   cmd = "NotReal";
				   }
			   }
			   else{
				   cmd = "Delete";
			   }
		    }
		   // System.out.println(cmd);
		    if(cmd != null)
		    {
		    //Cannot modify or delete an administrator
		    if(table.getValueAt(selectedRowIndex, 4).equals("1")){
		    	 mainFrame.getGlassPane().setVisible(true);
			     JOptionPane.showMessageDialog(null, "This user is an administrator and cannot be deleted or modified.");
			     mainFrame.getGlassPane().setVisible(false);
		    }
		    else{
		    	//Delete an item
		    	if(cmd.equals("Delete")){
				    mainFrame.getGlassPane().setVisible(true);
				    String cmd2 = JOptionPane.showInputDialog(null, "Are you SURE you want to delete this item from the database?" + 
			         " Type DELETE to confirm.", "Confirm", JOptionPane.WARNING_MESSAGE);
				    if(cmd2 != null && cmd2.equals("DELETE")){
				    	int prevRow = table.convertRowIndexToModel(selectedRowIndex);
				    	sap.deleteEntry(tableType, pKey, (String)table.getValueAt(selectedRowIndex, pKeyIndex)); 
				    	updateModel.removeRow(prevRow);
				    	//table.convertRowIndexToView(modelRowIndex)
				    }
				    mainFrame.getGlassPane().setVisible(false);
		    	}
		    	//Modify an item
			    else if(cmd.equals("Modify")){
			     
			    	mainFrame.getGlassPane().setVisible(true);
			    	String[] modify;
			    	String modifyField;
			    	//Initialize fields based on visibility
			    	if(isAdmin)
			    	{
			    		modify = myHeader;
				    	modifyField = (String) JOptionPane.showInputDialog(null, 
				    	"Please select an option", "Input", JOptionPane.INFORMATION_MESSAGE, null,
				    	modify, modify[0]);
			    	}
			    	else
			    	{
			    		modify = new String[2];
			    		modify[0] = "Condition";
			    		modify[1] = "Price";
			    	
			    		modifyField = (String) JOptionPane.showInputDialog(null, 
						"Please select an option", "Input", JOptionPane.INFORMATION_MESSAGE, null,
						modify, modify[0]);
			    	}
			    	mainFrame.getGlassPane().setVisible(false);
			    
			    	//Find the index that modify field is in
			    	int selectedFieldIndex = 0;
			    	for(int i = 0; i < myHeader.length; i++){
			    		if(table.getColumnName(i).equals(modifyField)){
			    			selectedFieldIndex = i;
			    			break;
			    		}
			    	}
			      
			    
			    	if(modifyField != null){
			    		//Create the conditions drop down box
			    		mainFrame.getGlassPane().setVisible(true);
			    		String[] conditions = {"New", "Excellent", "Good", "Fair", "Poor"};
			    		String modifyTo;
			    		
			    		if(modifyField.equals("Condition"))
			    		{
			    			//The user will select a condition from a drop down menu
				    		modifyTo = (String)JOptionPane.showInputDialog(null, "What would you like to modify the value of " + modifyField + 
				    			  " to?\n\n" + "Current value: " + table.getValueAt(selectedRowIndex, selectedFieldIndex) + "\n", "Modify",
				    			  JOptionPane.INFORMATION_MESSAGE, null, conditions, conditions[0]);
				    		mainFrame.getGlassPane().setVisible(false);
			    		}
			    		else
			    		{
			    			//The user will enter what they would like to modify
			    			modifyTo = JOptionPane.showInputDialog(null, "What would you like to modify the value of " + modifyField + 
					    			  " to?\n\n" + "Current value: " + table.getValueAt(selectedRowIndex, selectedFieldIndex) + "\n");
					    		mainFrame.getGlassPane().setVisible(false);
					    		
					    		//Check the price with a regex
					    		if(modifyField.equals("Price")){
					    			if(modifyTo != null)
					    			{
						    			if(!mainFrame.isValidPrice(modifyTo)){
						    				mainFrame.getGlassPane().setVisible(true);
						    				JOptionPane.showMessageDialog(null, "The price that you entered is not in the correct format. \n\nPlease enter " +
						    						"the price in U.S. Currency Format\nEx:  $100.00");
						    				mainFrame.getGlassPane().setVisible(false);
						    				modifyTo = null;
						    			}
					    			}
					    		}
			    		}
			    	  
			    		if(modifyTo != null){
			    			//Add to server
			    			int prevRow = table.convertRowIndexToModel(selectedRowIndex);
			    			sap.modifyInformation(tableType, modifyField, modifyTo, (String)table.getValueAt(selectedRowIndex, pKeyIndex));	
			    			updateModel.setValueAt(modifyTo, prevRow, selectedFieldIndex);
			    		}
			    	}
			    }    
		    }
		}
	}
	
}//End of class AdminPanel.java