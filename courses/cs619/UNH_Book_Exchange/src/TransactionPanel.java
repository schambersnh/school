import java.awt.BorderLayout;
import java.sql.ResultSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * Details the books the user has sold, the sales pending, the bought books, and the purchases pending.
 * @author Team11
 *
 */
public class TransactionPanel extends JTabbedPane {

	private JPanel salesPendingPanel, purchasesPendingPanel;
	
	/**
	 * The instance of the server used to process database requests.
	 */
	private ServerAccessPoint sap;
	
	/**
	 * The current user of the program
	 */
	private User curUser;
	
	/**
	 * initializes the panels view buttons.
	 * @param cU
	 */
	public TransactionPanel(User cU) {
		curUser = cU;
		sap = ServerAccessPoint.getInstance();
		
		setFont(InputPanel.defaultFont);
		
		updatePanel();
		
		addTab("Sales Pending", salesPendingPanel);
		addTab("Purchases Pending", purchasesPendingPanel);
		
		// TODO Auto-generated constructor stub
	}

	/**
	 * Generates a panel with the requested transaction information.
	 * @param buyer
	 * The buyer in the transaction.
	 * @param seller
	 * The seller in the transaction.
	 * @param sold
	 * Whether or not the sale has been completed.
	 * @return
	 * The panel with all requested transaction information in a JTable
	 */
	private JPanel createHistoryPanel(String buyer, String seller, String role, boolean sold){
		String tableType = "Transaction";
		
		ColorTable transactionTable;
		if(role.equals("Buyer")){
			transactionTable = GUIFactory.createTable(tableType, GUIFactory.transactionTableHeader, "",
				role, buyer, true);
		}else{
			transactionTable = GUIFactory.createTable(tableType, GUIFactory.transactionTableHeader, "",
				role, seller, true);
		}
		
		JPanel jp = new JPanel(new BorderLayout());
		jp.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
		
		return jp;
	}
	
	/**
	 * Updates the panel for the most recent transaction history
	 */
	public void updatePanel(){
		salesPendingPanel = createHistoryPanel("", curUser.getEmail(), "Seller", false);
		purchasesPendingPanel = createHistoryPanel(curUser.getEmail(), "", "Buyer", false);
		
		salesPendingPanel.validate();
		purchasesPendingPanel.validate();
		repaint();
	}
}
