/*
 * 	ColorTable.java
 *		An easily colorable, scrollable, font changeable JTable
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * A JTable that can have a specified header font, table font, highlighting color, odd
 * row color, and even row color.
 * @author Team11
 *
 */
public class ColorTable extends JTable{

	/**
	 * The default font values for the tables.
	 */
	protected static final Font FONTHEADER = new Font("Tahoma", Font.BOLD, 18), 
			FONTTABLE = new Font("Tahoma", Font.PLAIN, 16);
	
	/**
	 * The default color value for the table.
	 */
	protected static final Color COLORODD = new Color(153, 153, 255), COLOREVEN = new Color(204, 204, 255),
			COLORHIGHLIGHT = new Color(0, 0, 102);
	
	/**
	 * The user's stored color scheme.
	 */
	public static Color oddRowColor = COLORODD, evenRowColor = COLOREVEN, highlightColor = COLORHIGHLIGHT;
	
	/**
	 * The user's stored font scheme.
	 */
	public static Font headerFont = FONTHEADER, tableFont = FONTTABLE;
	
	//VALUES ACTUALLY IMPLEMENTED
	private Color tempOddRowColor, tempEvenRowColor, tempHighlightColor;
	private Font tempHeaderFont, tempTableFont;
	/**
	 * Initiates the JTable with the given custom information. 
	 * @param data
	 * The table's contents.
	 * @param cNames
	 * The table's column names.
	 * @param headF
	 * The header font.
	 * @param tableF
	 * The table font.
	 * @param high
	 * The highlighting color.
	 * @param odd
	 * The odd row color.
	 * @param even
	 * The even row color.
	 */
	ColorTable(String[][] data, String[] cNames, Font headF, Font tableF, Color high, Color odd, Color even){
		super(data, cNames);
		
		tempHighlightColor = high;
		tempHeaderFont = headF;
		tempTableFont = tableF;
		tempOddRowColor = odd;
		tempEvenRowColor = even;
				
		FontMetrics metrics = getFontMetrics(tempTableFont); 
		int fontHeight = metrics.getHeight();
		setRowHeight( fontHeight + (int)(fontHeight*0.2) );
		
		getTableHeader().setDefaultRenderer(new HeaderRenderer(this));
	}
	
	/**
	 * 
	 * @author Team11
	 *	A custom table header renderer used to change the table header's font.
	 */
	private class HeaderRenderer implements TableCellRenderer {

	    DefaultTableCellRenderer renderer;

	    public HeaderRenderer(JTable table) {
	    	setOpaque(true);
	        renderer = (DefaultTableCellRenderer)
	            table.getTableHeader().getDefaultRenderer();
	        renderer.setHorizontalAlignment(JLabel.CENTER);
	    }

	    @Override
	    public Component getTableCellRendererComponent(
	        JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int col) {
	    	Component cell = renderer.getTableCellRendererComponent(
		            table, value, isSelected, hasFocus, row, col);
	    	//cell.setBackground(headerColor);
	    	//setBackground(headerColor);
	    	cell.setFont(tempHeaderFont);
	        return cell;
	    }
	}
	
	/**
	 * Overwrites the cell editable method to ensure the table cannot be edited.
	 * @return
	 * Always returns false.
	 */
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	/**
	 * Prepares the table renderer. Used for customizing table colors and table fonts.
	 * @param renderer
	 * @param Index_row
	 * The table row number.
	 * @param Index_col
	 * The table column model.
	 * @return
	 * The customized table cell.
	 */
	public Component prepareRenderer (TableCellRenderer renderer,int Index_row, int Index_col) {
		Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
		//set the font
		 comp.setFont(tempTableFont);
		
		//even index, selected or not selected
		if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
			comp.setBackground(tempEvenRowColor);
		} 
		else if(!isCellSelected(Index_row,Index_col)){
			comp.setBackground(tempOddRowColor);
		}
		else{
			comp.setBackground(tempHighlightColor);
		}
		
		return comp;
	}
}//End of class ColorTable
