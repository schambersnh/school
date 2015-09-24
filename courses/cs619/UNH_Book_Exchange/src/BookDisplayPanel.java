import java.awt.Component;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * This class is a panel containing the book display. There is an image of the book on the left and its attributes
 * are on the right. The BookDisplayPanel is typically created based off of information from the BookRequestInfo class. 
 * @author Team11
 *
 */
public class BookDisplayPanel extends JPanel {
	private JButton bookButton;
	private JLabel bookPicture, lblTitle, lblAuthor, lblISBN10, lblISBN13, lblBinding, lblPrice, lblCondition;
	private static Hashtable<String, BookRequestInfo> bookHash;
	private Box picBox;
	
	/**
	 * Initialize the BookDisplayPanel GUI.
	 */
	public BookDisplayPanel(){
		bookPicture = new JLabel();
		picBox = Box.createVerticalBox();
		picBox.add(bookPicture);
	//	picBox.add(Box.createHorizontalStrut(650));
		add(picBox);
		add(GUIFactory.resizedVerticalStrut(500));
		add(createDescriptionBox());
	}
	
	/**
	 * Creates the box containing the book's author, title, ISBN10, ISBN13, binding, price and condition.
	 * @return 
	 * Box containing the description of the book being sold.
	 */
	private Box createDescriptionBox(){
		Box desBox = Box.createVerticalBox();
		
		lblTitle = new JLabel();
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		desBox.add(lblTitle);
		
		desBox.add(GUIFactory.resizedVerticalStrut(5));
		
		lblAuthor = new JLabel();
		lblAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAuthor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desBox.add(lblAuthor);
		
		desBox.add(GUIFactory.resizedVerticalStrut(5));
		
		lblPrice = new JLabel();
		lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		desBox.add(lblPrice);
		
		lblCondition = new JLabel();
		lblCondition.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCondition.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desBox.add(lblCondition);
		
		lblBinding = new JLabel();
		lblBinding.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBinding.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desBox.add(lblBinding);
		
		desBox.add(Box.createVerticalStrut(10));
		
		lblISBN10 = new JLabel();
		lblISBN10.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblISBN10.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desBox.add(lblISBN10);
		
		lblISBN13= new JLabel();
		lblISBN13.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblISBN13.setFont(new Font("Tahoma", Font.PLAIN, 14));
		desBox.add(lblISBN13);
		
		desBox.add(GUIFactory.resizedVerticalStrut(15));
		
		bookButton = GUIFactory.createConfirmationButton("");
		bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		desBox.add(bookButton);
		bookButton.setVisible(false);
		
		desBox.add(GUIFactory.resizedHorizontalStrut(525));
		
		return desBox;
	}
	
	/**
	 * Returns the button associated with the book.
	 * @return
	 * Button that, when clicked, displays the book and its corresponding information.
	 */
	public JButton getBookButton(){
		return bookButton;
	}
	
	/**
	 * Updates the book display.
	 * @param bri 
	 * BookRequestInfo containing all book information about the book.
	 * @param condition 
	 * Updated condition of the book.
	 * @param price 
	 * Updated price of the book.
	 */
	public void updateBook(BookRequestInfo bri, String condition, String price){
		bookPicture.setIcon(bri.getLargeImage());
		lblTitle.setText(bri.getTitle());
		lblAuthor.setText("Author:  " + bri.getAuthor());
		lblBinding.setText(bri.getBinding());
		lblISBN10.setText("ISBN-10:   " + bri.getISBN10() + "      ");
		lblISBN13.setText("ISBN-13:   " + bri.getISBN13());
		lblCondition.setText(condition);
		lblPrice.setText(price);
		
		picBox.validate();
		picBox.repaint();
		validateLabelLength();
	}
	
	/**
	 *	Trims all lengthy labels so they fit nicely in the frame.
	 */
	private void validateLabelLength(){
		if(lblTitle.getText().length() > 50){
			lblTitle.setText(lblTitle.getText().substring(0, 50) + "...");
		}
		
		if(lblAuthor.getText().length() > 50){
			lblAuthor.setText(lblAuthor.getText().substring(0, 50) + "...");
		}
	}

	/**
	 * Clears all information in the BookDisplayPanel and sets the associated button to false.
	 */
	public void resetPanel() {
		bookPicture.setIcon(null);
		lblTitle.setText("");
		lblAuthor.setText("");
		lblBinding.setText("");
		lblISBN10.setText("");
		lblISBN13.setText("");
		lblCondition.setText("");
		lblPrice.setText("");
		bookButton.setVisible(false);
		
	}
	
/**
 * Updates a BookDisplayPanel when given an ISBN number, condition of the book, and price to which the book will be sold.<br>
 * @param bdp
 * The BookDisplayPanel being updated.
 * @param j
 * The JTable containing information needed to display a book.
 * @param selectedRowIndex
 * the row selected by the user in the table to view.
 * 
 * @return
 * true if the ISBN was valid, false otherwise.
 */
	public static boolean setBookDisplay(BookDisplayPanel bdp, JTable j, int selectedRowIndex){
		
		
		int[] selection = j.getSelectedRows();
		for (int i = 0; i < selection.length; i++) {
		  selection[i] = j.convertRowIndexToView(selection[i]);
	 }
		
		
		String isbn = (String)j.getValueAt(selectedRowIndex, 2);
		String condition = (String)j.getValueAt(selectedRowIndex, 5);
		String price = (String)j.getValueAt(selectedRowIndex, 6);
		
		if(bookHash == null){
			bookHash = new Hashtable<String, BookRequestInfo>();
		}
		
		BookRequestInfo bri = bookHash.get(isbn);
		if(bri == null){
			bri = new BookRequestInfo(isbn);
			if(!bri.isValid()){
				return false;
			}
			bookHash.put(isbn, bri);
		}
		
		bdp.updateBook(bri, condition, price);
		
		bdp.validate();
		bdp.repaint();
		
		return true;
	}
	
}//End of class BookDisplayPanel.java
