
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.font.*;
import java.awt.geom.*;

//NOTE: This class was modified from an online source.
//http://opencascade.sourcearchive.com/documentation/6.3.0.dfsg.1-1/JFontChooser_8java-source.html

/**
 * A simple FontChooser dialog that implements similar functionality to
 * the JFileChooser, JOptionPane and JColorChooser components provided
 * with Swing.<p>
 * Upon initialization, the JFontChooser polls the system for all available
 * fonts, initializes the various JList components to the values of the
 * default font and provides a preview of the font. As options are changed/selected
 * the preview is updated to display the current font.<p>
 * JFileChooser can either be created and added to a GUI as a typical
 * JComponent or it can display a JDialog using the {@link #showDialog(Component, String) showDialog}
 * method (just like the <b>JFileChooser</b> component). Objects
 * that extend JFontChooser should use the {@link #acceptSelection() acceptSelection} and
 * {@link #cancelSelection() cancelSelection} methods to determine either
 * an accept or cancel selection.<p>
 * <i>Example:</i>
 * <blockquote>
 * <samp>
 * JFontChooser chooser = new JFontChooser(new Font("Arial", Font.BOLD, 12));
 * if (chooser.showDialog(this, "Choose a font...") == JFontChooser.ACCEPT_OPTION) {
 *    Font f = chooser.getSelectedFont();
 *    // Process font here...
 * }
 * </samp></blockquote>
 * <p>
 */
 public class JFontChooser extends JComponent implements ActionListener, ListSelectionListener {
      
      private Font font;
      private JList<?> fontNames, fontSizes, fontStyles;
      private JTextField currentSize;
      private JButton okay, cancel;
      private Font[] availableFonts;
      private JFontPreviewPanel preview;
      private JDialog dialog;
      private int returnValue;

      /**
       * Value returned by {@link #showDialog(Component, String) showDialog} upon an error.
       */
       public static final int ERROR_OPTION=0;

      /**
       * Value returned by {@link #showDialog(Component, String) showDialog} upon a selected font.
       */
       public static final int ACCEPT_OPTION=2;

      /**
       * Value returned by {@link #showDialog(Component, String) showDialog} upon a cancel.
       */
       public static final int CANCEL_OPTION=4;
      
      /**
       * Constructs a new JFontChooser component initialized to the supplied font object.
       * @see     com.lamasoft.JFontChooser#showDialog(Component, String)
       */
       public JFontChooser(Font font) {
            super();
            this.font = font;
            setup();
      }
      
       /**
        * Sets up the font chooser.
        */
      private void setup() {
            setLayout(new BorderLayout());
            
            Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            Vector<Font> fonts = new Vector<Font>(1, 1);
            Vector<String> names = new Vector<String>(1, 1);
            for (int i = 0; i < fontList.length; i++) {
                  String fontName = fontList[i].getFamily();
                  if (! names.contains(fontName)) {
                        names.addElement(fontName);
                        fonts.addElement(fontList[i]);
                  }
            }
            availableFonts = new Font[fonts.size()];
            for (int i = 0; i < fonts.size(); i++) 
                  availableFonts[i] = (Font) fonts.elementAt(i);
            fontNames = new JList<Object>(names);
            JScrollPane fontNamesScroll = new JScrollPane(fontNames);
            fontNames.addListSelectionListener(this);
            
            Object[] styles = {"Regular", "Bold", "Italic", "BoldItalic"};
            fontStyles = new JList<Object>(styles);
            JScrollPane fontStylesScroll = new JScrollPane(fontStyles);
            fontStyles.setSelectedIndex(0);
            fontStyles.addListSelectionListener(this);
            
            String[] sizes = new String[69];
            for (int i = 3; i < 72; i++)
                  sizes[i - 3] = (new Integer(i + 1)).toString();
            fontSizes = new JList<Object>(sizes);
            JScrollPane fontSizesScroll = new JScrollPane(fontSizes);
            fontSizes.addListSelectionListener(this);
            
            currentSize = new JTextField(5);
            currentSize.setText((new Integer(font.getSize())).toString());
            currentSize.addActionListener(this);
            
            GridBagLayout g2 = new GridBagLayout();
            GridBagConstraints c2 = new GridBagConstraints();
            JPanel sizePane = new JPanel(g2);
            c2.gridx = 0;
            c2.gridy = 0;
            c2.insets = new Insets(2, 5, 2, 5);
            c2.anchor = GridBagConstraints.WEST;
            sizePane.add(currentSize);
            g2.setConstraints(currentSize, c2);
            
            sizePane.add(fontSizesScroll);
            c2.gridy++;
            c2.fill = GridBagConstraints.HORIZONTAL;
            g2.setConstraints(fontSizesScroll, c2);
            
            preview = new JFontPreviewPanel(this.font);
            
            okay = new JButton("Ok");
            okay.addActionListener(this);
            cancel = new JButton("Cancel");
            cancel.addActionListener(this);
            
            GridBagLayout g = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            JPanel top = new JPanel(g);
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.VERTICAL;
            c.insets = new Insets(2, 5, 2, 5);
            c.gridx = 0;
            c.gridy = 0;
            top.add(fontNamesScroll);
            g.setConstraints(fontNamesScroll, c);
            c.gridx++;
            top.add(fontStylesScroll);
            g.setConstraints(fontStylesScroll, c);
            c.gridx++;
            top.add(sizePane);
            g.setConstraints(sizePane, c);
            
            add("North", top);
            add("Center", preview);
            
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            
            buttons.add(okay);
            buttons.add(cancel);
            
            add("South", buttons);
            
            fontSizes.setSelectedValue((new Integer(font.getSize())).toString(), true);
            fontNames.setSelectedValue(font.getFamily(), true);
            if (font.getStyle() == Font.PLAIN)
                  fontStyles.setSelectedValue("Regular", false);
            else if (font.getStyle() == Font.ITALIC)
                  fontStyles.setSelectedValue("Italic", false);
            else if (font.getStyle() == Font.BOLD)
                  fontStyles.setSelectedValue("Bold", false);
            else if (font.getStyle() == (Font.BOLD | Font.ITALIC))
                  fontStyles.setSelectedValue("BoldItalic", false);
      }
      
      /**
       * Updates the font.
       * @param f
       * Font to be updated to.
       */
      private void updateFont(Font f) {
            this.font = f;
            preview.setFont(this.font);
      }
      /**
       * Updates the font size.
       * @param size
       * Font size to be updated to.
       */
      private void updateFontSize(int size) {
            updateFont(font.deriveFont((new Integer(size)).floatValue()));
      }
      /**
       * Updates the font style.
       * @param style
       * Font style to be updated to.
       */
      private void updateFontStyle(int style) {
            updateFont(font.deriveFont(style));
      }
      
      /**
       * Returns the currently selected font. Typically called after receipt
       * of an ACCEPT_OPTION (using the {@link #showDialog(Component, String) showDialog} option) or from within the
       * approveSelection method (using the component option).
       * @return java.awt.Font 
       * A font class that represents the currently selected font.
       */
       public Font getSelectedFont() {
            return font;
      }
      
      /**
       * Processes action events from the okay and cancel buttons
       * as well as the current size TextField.
       */
       public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okay) {
                  returnValue = ACCEPT_OPTION;
                  if (dialog != null)
                        dialog.setVisible(false);
                  acceptSelection();
                  
            }
            if (e.getSource() == cancel) {
                  returnValue = CANCEL_OPTION;
                  if (dialog != null)
                        dialog.setVisible(false);
                  cancelSelection();
            }
            if (e.getSource() == currentSize) {
                  fontSizes.setSelectedValue(currentSize.getText(), true);
            }
      }
      
      /**
       * Processes events received from the various JList objects.
       */
       public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == fontNames) {
                  Font f = availableFonts[fontNames.getSelectedIndex()];
                  f = new Font(f.getFontName(), font.getStyle(), font.getSize());
                  updateFont(f);
            }
            if (e.getSource() == fontSizes) {
                  currentSize.setText((String) fontSizes.getSelectedValue());
                  updateFontSize((new Integer(currentSize.getText())).intValue());
            }
            if (e.getSource() == fontStyles) {
                  int style = Font.PLAIN;
                  String selection = (String) fontStyles.getSelectedValue();
                  if (selection.equals("Regular"))
                        style = Font.PLAIN;
                  if (selection.equals("Bold"))
                        style = Font.BOLD;
                  if (selection.equals("Italic"))
                        style = Font.ITALIC;
                  if (selection.equals("BoldItalic"))
                        style = (Font.BOLD | Font.ITALIC);
                  updateFontStyle(style);
            }
      }
      
      /**
       * Pops up a Font chooser dialog.
       * @param title
       * The title of the dialog..
       * @param parent
       * The parent component of the dialog.
       * @return
       * An integer that equates to the static variables <i>ERROR_OPTION</i>, <i>ACCEPT_OPTION</i> or <i>CANCEL_OPTION</i>.
       */
       public int showDialog(Component parent, String title) {
            returnValue = ERROR_OPTION;
            Frame frame = parent instanceof Frame ? (Frame) parent : (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);
            dialog = new JDialog(frame, title, true);
            dialog.getContentPane().add("Center", this);
            dialog.pack();
            dialog.setLocationRelativeTo(parent);
            dialog.setVisible(true);
            return returnValue;
      }
      
      /**
       * This method is called when the user presses the okay button, selecting
       * the currently displayed font. Children of JFontChooser should override this
       * method to process this event.
       */
       public void acceptSelection() {
      }
       
      /**
       * This method is called when the user presses the cancel button. Children of
       * JFontChooser should override this method to process this event.
       */
       public void cancelSelection() {
      }
}


/**
 * Creates an inner class showing the preview panel.   
 */
class JFontPreviewPanel extends JPanel {
      
      private Font font;
      
      /**
       * Sets the border, default font and constructs the JPanel.
       * @param f
       */
      public JFontPreviewPanel(Font f) {
            super();
            setFont(f);
            setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Preview"));
      }
      
      /**
       * Sets the default font.
       */
      public void setFont(Font f) {
            this.font = f;
            repaint();
      }
      
      /**
       * Paints the panel previewing the font.
       * @param g
       * The associated graphics component.
       */
      public void update(Graphics g) {
            paintComponent(g);
            paintBorder(g);
      }
      
      /**
       * Paints the panel previewing the font.
       * @param g
       * The associated graphics component.
       */
      public void paintComponent(Graphics g) {
            Image osi = createImage(getSize().width, getSize().height);
            Graphics osg = osi.getGraphics();
            osg.setFont(this.font);
            Rectangle2D bounds = font.getStringBounds(font.getFontName(), 0, font.getFontName().length(), new FontRenderContext(null, true, false));
           // int width = (new Double(bounds.getWidth())).intValue();
            int height = (new Double(bounds.getHeight())).intValue();
            osg.drawString(font.getFontName(), 5, (((getSize().height - height) / 2) + height));
            
            g.drawImage(osi, 0, 0, this);
      }
      
      /**
       * Gets a customized preferred dimension for the preview panel.
       */
      public Dimension getPreferredSize() {
            return new Dimension(getSize().width, 75);
      }
      
      /**
       * Gets a customized preferred minimum dimension for the preview panel.
       */
      public Dimension getMinimumSize() {
            return getPreferredSize();
      }
}//End of class JFontChooser.java
