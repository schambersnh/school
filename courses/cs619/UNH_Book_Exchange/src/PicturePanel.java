import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.Box;

/**
 * Creates a panel with a title and picture.
 * @author Team11
 *
 */
public class PicturePanel extends JPanel{

	/**
	 * Creates the PicturePanel.
	 * @param title
	 * The title being displayed at the top of the panel.
	 * @param url
	 * The URL of the picture.
	 */
	public PicturePanel(String title, URL url) {
		setLayout(new BorderLayout());
		
		//creates the home title box
		Box siteSummaryBox = GUIFactory.createTitleBox(title);
		add(siteSummaryBox, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		
		//creates the image
		add(GUIFactory.createImageBox(url), BorderLayout.CENTER);
	}

}//End of class PicturePanel.java