import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;

/**
 * Allows the user to modify their books for sale. In the MangeBookPanel, the user is able to view the books they are currently selling, modify the price and 
 * condition of the book they are selling, deleting a book they are selling, and mark a book they are currently selling as sold. 
 * @author Team11
 *
 */
public class ManageBooksPanel extends JPanel{
	/**
	 * The book display panel used to display the user's book and the corresponding book information.
	 */
	private BookDisplayPanel bdp;
	
	/**
	 * The table used to navigate through the user's books.
	 */
	private JTable finalTable;
	
	/**
	 * The current user of the program.
	 */
	private User curUser;
	
	/**
	 * The panel containing the book table.
	 */
	private JPanel listPanel;
	
	/**
	 *  The box containing title at the top of the screen.
	 */
	private Box titleBox;
	
	/**
	 * Creates the Panel.
	 * @param cU
	 * The current user.
	 */
	public ManageBooksPanel(User cU) {
		curUser = cU;
		setLayout(new BorderLayout());
		
		titleBox = GUIFactory.createTitleBox("Click on the table to view a book. Double click to modify!");
		
		add(titleBox, BorderLayout.NORTH);
		
		listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		add(listPanel, BorderLayout.CENTER);
		
		bdp = new BookDisplayPanel();
		bdp.getBookButton().setVisible(false);


		listPanel.add(bdp, BorderLayout.NORTH);
	}
	
	/**
	 * Updates the table, showing all sold books of the current user.
	 */
	public void updateTable(){
		finalTable = GUIFactory.createTable("Book Information", GUIFactory.bookTableHeader, "",
				"E-Mail", curUser.getEmail(), true);
		
		BorderLayout layout = (BorderLayout) listPanel.getLayout();
		if(layout.getLayoutComponent(BorderLayout.CENTER) != null){
			listPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		}
		if(layout.getLayoutComponent(BorderLayout.NORTH) != null){
			listPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
		}
		
		if(finalTable == null){
			listPanel.add(new PicturePanel("You currently have no books for sale", SearchBooksPanel.class.getResource("/resources/nobooks.gif")), BorderLayout.NORTH);
			titleBox.setVisible(false);
		}
		else{
			titleBox.setVisible(true);
			listPanel.add(bdp, BorderLayout.NORTH);
			TableColumnModel tcm = finalTable.getColumnModel();
			//remove id
		    tcm.getColumn(0).setMaxWidth(0);
		    tcm.getColumn(0).setMinWidth(0);
		    tcm.getColumn(0).setPreferredWidth(0);
			//remove e-mail
		    tcm.getColumn(1).setMaxWidth(0);
		    tcm.getColumn(1).setMinWidth(0);
		    tcm.getColumn(1).setPreferredWidth(0);
			
			establishMouseListener(finalTable);
			listPanel.add(new JScrollPane(finalTable), BorderLayout.CENTER);
		}
		validate();
		repaint();
	}
	
	/**
	 * Updates the manage books panel. Clears the book display and updates the user's book table.
	 */
	public void updatePanel(){
		bdp.resetPanel();
		updateTable();
	}
	
	/**
	 * Established a MouseListener for the table.<br>
	 * <pre>
	 * 		Single Click: Displays book information above table.
	 * 		Double Click: Allows user to delete or modify book information.
	 * </pre>
	 * @param j
	 * JTable to add MouseListener to.
	 */
	private void establishMouseListener(final JTable j){
		j.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
					int selectedRowIndex = j.getSelectedRow();
					
					//pass update book the ISBN number
					BookDisplayPanel.setBookDisplay(bdp, j, selectedRowIndex);
				}
				else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
					AdminPanel.editTable(j, "Book Information", false);
				}
			}
		});
	}

}//End of class ManageBooksPanel
