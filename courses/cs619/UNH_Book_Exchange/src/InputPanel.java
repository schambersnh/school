
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * An abstract class the provides methods for checking text fields. This includes trimming fields, checking to see if they're valid, checking field 
 * length, clearing fields, and setting all input fields to a single button. 
 * @author Team11
 *
 */
public abstract class InputPanel extends JPanel {
	/**
	 * The labels displayed next to the input fields
	 */
	protected ArrayList<JLabel> labels;
	
	/**
	 * The input fields for the panel.
	 */
	protected ArrayList<JTextField> textFields;
	
	/**
	 * The accept button associated with the panel.
	 */
	protected JButton acceptButton;
	
	/**
	 * The default font for all InputPanels.
	 */
	public static final Font defaultFont = new Font("Tahoma", Font.PLAIN, 16);
	
	/**
	 * Initialize array list of input fields.
	 */
	public InputPanel() {
		labels = new ArrayList<JLabel>();
		textFields = new ArrayList<JTextField>();
	}

	/**
	 * Trims all text fields EXCEPT password fields.
	 */
	protected void trimFields(){
		for(int i = 0; i < textFields.size(); i++){
			JTextField jtf = textFields.get(i);
			if(!jtf.getClass().equals(JPasswordField.class)){
				jtf.setText(jtf.getText().trim());
			}
		}
	}
	
	/**
	 * Returns if any fields are empty (no characters except white space).
	 * @return
	 * true if any fields are empty, false otherwise.
	 */
	protected boolean isFieldEmpty(){
		for(int i = 0; i < textFields.size(); i++){
			if(textFields.get(i).getText().length() == 0){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if any field length exceeds 40.
	 * @return
	 * true if any field lengths exceed 40, false otherwise.
	 */
	protected boolean checkFieldLength(){
		for(int i = 0; i < textFields.size(); i++){
			if(textFields.get(i).getText().length() > 40){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Clears all password fields and text fields.
	 */
	protected void clearFields(){
		for(int i = 0; i < textFields.size(); i++){
			textFields.get(i).setText("");
		}
	}

	/**
	 * Sets text fields enter to click corresponding button.
	 */
	protected void setEnterButton(final JButton btn){
		for(int i = 0; i < textFields.size(); i++){
			textFields.get(i).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						btn.doClick();
					}
				}
			});
		}
	}
	/**
	 * Updates the format of the InputPanel.<br>
	 * i.e. Change all textField sizes
	 */
	protected void updateFormat(){
		for(JLabel lbl : labels){
			lbl.setFont(defaultFont);
		}
		
		for(JTextField jtf: textFields){
			jtf.setFont(defaultFont);
			jtf.setColumns(15);
		}
		
		acceptButton.setFont(defaultFont);
	}
	
}//End of class InputPanel
