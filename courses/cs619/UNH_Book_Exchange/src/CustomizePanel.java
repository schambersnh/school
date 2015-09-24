/*
 *	CustomizePanel.java
 *		A panel that allows the user to customize the tables
 *		that display books
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * A panel used to customize the user's graphical interface. The CustomizePanel allows the user to choose custom colors and fonts for the tables
 * that list information throughout the GUI. For the Administrator, this includes the database management panels. The user can also quickly revert 
 * their colors back to the default scheme in this panel.
 * 
 * @author Team11
 *
 */
public class CustomizePanel extends JPanel{
	
	/**
	 * The ArrayList describing which fields are being customized.
	 */
	private ArrayList<JLabel> labels;
	/**
	 * Values of both the header and table font that are stored and only used when the user accepts the customization changes to their account.
	 * These values are necessary because of the way the fonts are displayed in the JTextFields used to chose fonts.
	 */
	private Font tempHeaderFont, tempTableFont;
	/**
	 * The fields that allow the user to customize their account. This includes both color and font customization. 
	 */
	private ArrayList<JTextField> fields;
	/**
	 * Stores the users new customization information in the database
	 */
	private JButton acceptButton;
	/**
	 * Reverts the displayed customization information to the interface defaults.
	 */
    private JButton revertButton;
    /**
     * A field associated with the corresponding customization field.
     */
	private JTextField highlightField, evenField, oddField, 
		headerFontField, tableFontField;
	/**
	 * An instance of the main frame all panels are stored on.
	 */
	private MainFrame mainFrame;
	/**
	 * The panel used to store the sample table.
	 */
	private JPanel form_3;
	/**
	 * The current user of the program.
	 */
	private User curUser;
	
/**
 * Initializes the customization form and sample table.
 * @param cU
 * The current user.
 */
	public CustomizePanel(User cU) {
		curUser = cU;
		
		mainFrame = MainFrame.getInstance();
		
		labels = new ArrayList<JLabel>();
		fields = new ArrayList<JTextField>();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create the title section of form
		add(GUIFactory.createTitleBox("Choose your color scheme by double clicking on the fields below!"));
		
		//highlight color section of form
		JLabel highlightLbl = new JLabel("Highlighting Color:");
		labels.add(highlightLbl);
		highlightField = new JTextField();
		fields.add(highlightField);
				
		//even color section of form
		JLabel evenLbl = new JLabel("Even Row Color:   ");
		labels.add(evenLbl);
		evenField = new JTextField();
		
		fields.add(evenField);

		//odd color section of form
		JLabel oddLbl = new JLabel("Odd Row Color:    ");
		labels.add(oddLbl);
		oddField = new JTextField();
		
		fields.add(oddField);
		
		//header font section of form
		JLabel headerFontLbl = new JLabel("Header Font:        ");
		labels.add(headerFontLbl);
		headerFontField = new JTextField();
		
		fields.add(headerFontField);
		
		//table font section of form
		JLabel tableFontLbl = new JLabel("Table Font:          ");
		labels.add(tableFontLbl);
		tableFontField = new JTextField();
		
		fields.add(tableFontField);
				
		//create the form and add it to the panel
		JPanel formArr[] = GUIFactory.createFormPanel(labels, fields, null);
		
		//create the button
		acceptButton = new JButton("Accept Changes");
		setAcceptButtonListener();
		revertButton = new JButton("Revert to Defaults");
		setRevertButtonListener();
		JPanel form_2 = new JPanel(new BorderLayout());
		JPanel btnPanel = new JPanel();
		btnPanel.add(acceptButton);
		btnPanel.add(revertButton);
		form_2.add(btnPanel, BorderLayout.NORTH);
		formArr[1].add(form_2, BorderLayout.CENTER);
		
		form_3 = new JPanel(new BorderLayout());
		form_3.add(GUIFactory.resizedVerticalStrut(75), BorderLayout.NORTH);
		form_2.add(form_3, BorderLayout.CENTER);
		
		updateFormat();
		
		add(formArr[0]);
		
		setFieldListeners();
	}
	
	/**
	 * Loads the user's custom font and color scheme.
	 */
	public void loadFontAndColor(){
		highlightField.setBackground(ColorTable.highlightColor);
		evenField.setBackground(ColorTable.evenRowColor);
		oddField.setBackground(ColorTable.oddRowColor);
		
		tempHeaderFont = ColorTable.headerFont;
		headerFontField.setText(tempHeaderFont.getName());
		
		tempTableFont = ColorTable.tableFont;
		tableFontField.setText(tempTableFont.getName());
	
		updateTable();
	}
	
	/**
	 * Saves the user's new custom font and color scheme.
	 */
	private void updateFontAndColor(){
		ColorTable.headerFont = tempHeaderFont;
		ColorTable.tableFont = tempTableFont;
		
		ColorTable.highlightColor = highlightField.getBackground();
		ColorTable.evenRowColor = evenField.getBackground();
		ColorTable.oddRowColor = oddField.getBackground();
	}
	
	/**
	 * Generates a new table displaying the user's customization.
	 */
	private void updateTable(){		
		String[] cNames = {"SAMPLE", "SAMPLE", "SAMPLE", "SAMPLE", "SAMPLE"};
		
		String[][]data = new String[50][5];
		
		for(int i = 0; i < 50; i++){
			for(int j = 0; j < 5; j++){
				data[i][j] = "sample text";
			}
		}
		
		ColorTable finalTable = new ColorTable(data, cNames, tempHeaderFont, tempTableFont,
				highlightField.getBackground(), evenField.getBackground(), oddField.getBackground()){
			  public boolean isCellEditable(int rowIndex, int colIndex) {
				  return false; //Disallow the editing of any cell
			  }
		};
		
		BorderLayout layout = (BorderLayout) form_3.getLayout();
		if(layout.getLayoutComponent(BorderLayout.CENTER) != null){
			form_3.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		}
		
		form_3.add(new JScrollPane(finalTable), BorderLayout.CENTER);
		
		validate();
		repaint();
	}
	
	/**
	 * Sets both the ColorChooser and FontChooser listeners for the associated
	 * JTextFields in the customization form.
	 */
	private void setFieldListeners(){
		int i = 0;
		
		for(final JTextField jtf : fields){
			if(i < 3){
				jtf.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {	
					if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
						mainFrame.getGlassPane().setVisible(true);
						Color c = JColorChooser.showDialog(MainFrame.getInstance(), 
								"Choose a Primary Color for your Color Scheme!", jtf.getBackground());
						if(c != null){
							jtf.setBackground(c);
							updateTable();
						}
						mainFrame.getGlassPane().setVisible(false);
					}
					////show your dialog with the selected row's contents
					}
				});
			}
			else{
				jtf.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {	
					if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
						Font chooseFont;
						if(jtf == headerFontField){
							chooseFont = tempHeaderFont;
						}
						else{
							chooseFont = tempTableFont;
						}
						JFontChooser jfc = new JFontChooser(chooseFont);
						mainFrame.getGlassPane().setVisible(true);
						if (jfc.showDialog(null, "Select a Font") == JFontChooser.ACCEPT_OPTION) {
							System.err.println("HELLO");
							Font tempFont = jfc.getSelectedFont();
							if(jtf == headerFontField){
								tempHeaderFont = tempFont;
							}
							else{
								tempTableFont = tempFont;
							}

							jtf.setText(tempFont.getName());	
							
							updateTable();
						}
						mainFrame.getGlassPane().setVisible(false);
					}
					}
				});
			}
			
			i++;
		}
	}
	
	/**
	 * Sets the accept changes button. Makes calls to formally store the user's new customization in the server.
	 */
	private void setAcceptButtonListener(){
		acceptButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				updateFontAndColor();
				
				ServerAccessPoint sap = ServerAccessPoint.getInstance();
				
				sap.modifyInformation("Custom Information", "Highlighting Color", Integer.toString(ColorTable.highlightColor.getRGB()), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Even Row Color", Integer.toString(ColorTable.evenRowColor.getRGB()), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Odd Row Color", Integer.toString(ColorTable.oddRowColor.getRGB()), curUser.getEmail());
				
				sap.modifyInformation("Custom Information", "Header Font Type", ColorTable.headerFont.getName(), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Header Font Style", Integer.toString(ColorTable.headerFont.getStyle()), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Header Font Size", Integer.toString(ColorTable.headerFont.getSize()), curUser.getEmail());
				
				sap.modifyInformation("Custom Information", "Table Font Type", ColorTable.tableFont.getName(), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Table Font Style", Integer.toString(ColorTable.tableFont.getStyle()), curUser.getEmail());
				sap.modifyInformation("Custom Information", "Table Font Size", Integer.toString(ColorTable.tableFont.getSize()), curUser.getEmail());

				mainFrame.setScrollPanel(new PicturePanel("Your table color scheme has been successfully updated!", 
						MainFrame.class.getResource("/resources/checkmark.gif")));
			}
		});
	}
	
	/**
	 * Sets the sample table and form to the default color scheme.
	 */
	private void setRevertButtonListener(){
		revertButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				revertColors();
				updateTable();
			}
		});
	}
	
	/**
	 * Reverts the fonts and colors of the customization form to the defaults.
	 */
	private void revertColors(){
		headerFontField.setText(ColorTable.FONTHEADER.getName());
		tableFontField.setText(ColorTable.FONTTABLE.getName());
		
		highlightField.setBackground(ColorTable.COLORHIGHLIGHT);
		evenField.setBackground(ColorTable.COLOREVEN);
		oddField.setBackground(ColorTable.COLORODD);
	}
	
	/**
	 * Updates the text font format used by the labels and buttons.
	 */
	public void updateFormat(){
		for(JLabel lbl : labels){
			lbl.setFont(InputPanel.defaultFont);
		}
		
		for(JTextField jtf: fields){
			jtf.setFont(InputPanel.defaultFont);
			jtf.setEditable(false);
			jtf.setColumns(10);
		}
		
		acceptButton.setFont(InputPanel.defaultFont);
		revertButton.setFont(InputPanel.defaultFont);
	}

}//End of class CustomizePanel.java