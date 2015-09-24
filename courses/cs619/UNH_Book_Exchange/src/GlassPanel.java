/*
 *	GlassPanel.java
 *		Provides a black see through panel used by JOptionPanes for effect
 * 
 */

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
/**
 * Creates a see-through panel that covers the entire contents of the frame.<br> Used when JOptionPane's appear to give a
 * more professional feel.
 * @author Team11
 *
 */
public class GlassPanel extends JComponent{
	/**
	 * The color transparent color used to give a faded out feel to the background panel.
	 */
	private final Color bgColor = new Color(0, 0, 0, 128);
	
	/**
	 *	Sets the panel to opaque
	 */
	public GlassPanel() {
		setOpaque(false);
	}

	/**
	 * Makes sure the background is also still be painted.
	 * @param g
	 * The graphic component associated with this panel.
	 */
	public void paintComponent(Graphics g)  {  
		g.setColor( bgColor );
		g.fillRect(0, 0, getSize().width, getSize().height);
	}  
}