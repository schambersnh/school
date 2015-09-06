/** 
 * GUI.java:
 * A JPanel to control the application's interface to the application
 *
 * @author rdb
 * CS416 Spring 2008
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class GUI extends JPanel 
{
   //---------------- class variables ---------------------------
   // These have package access and are referenced by RegexManager
   private static JLabel  regexLabel;
   static DisplayPanel    display;
   
   //---------------- instance variables ---------------------------
   private Container    _parent;
   private RegexManager     _mgr;
   
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * along with the application object.
    */
   public GUI( Container parent ) 
   {
      super ( new BorderLayout() );
      
      _parent  = parent;  
      
      display = new DisplayPanel();
      
      JScrollPane sPane = new JScrollPane( display,
               ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
               ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
      );
      
      this.add( sPane, BorderLayout.CENTER );
      _mgr     = new RegexManager();
      
      //Display the regular expression in the South
      this.add( makeRegexLabel(), BorderLayout.SOUTH );

      //create Buttons in the North
      Component buttonMenu = makeButtonMenu();
      this.add( buttonMenu, BorderLayout.NORTH );
      
      int width = display.getPreferredSize().width;
      
      sPane.setPreferredSize( new Dimension( width, 400 ));           
   }
   //-------------- makeRegexLabel() --------------------------
   private Component makeRegexLabel()
   {
      JPanel labelPanel = new JPanel();
      
      labelPanel.add( new JLabel( "Regex: " ));
      regexLabel = new JLabel( "      " );
      regexLabel.setFont( getFont().deriveFont( 20.0f )); // make the font big

      regexLabel.setBorder( new LineBorder( Color.BLACK ));
      labelPanel.add( regexLabel );
            
      return labelPanel;
   }
   //--------------------- setRegexLabel( String ) ----------------------
   /**
    * update the label field showing the current regular expression
    */
   public static void setRegexLabel( String newText )
   {
      regexLabel.setText( "  " + newText + "  " );
   }
   //------------------- makeButtonMenu --------------------------------
   private Component makeButtonMenu()
   {
      // JPanel defaults to FlowLayout
      String[] labels = { "Type input", "Read file", "Execute",
         "Sample", "NameReverse", "NameReplace", "DNAHeader", "Ortholog" };
      
      JPanel bMenu = new JPanel( new GridLayout( 1, 0 )); 
      JButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         button = new JButton( labels[ i ] );
         //button.setFont( getFont().deriveFont( 11.0f ));
         bMenu.add( button );
         button.addActionListener( new ButtonListener( i ));
      }      
      return bMenu;
   }
   //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener handles all button events and passes them along
    * to methods of the ListPanel class.
    */
   public class ButtonListener implements ActionListener
   {
      int _buttonId;
      public ButtonListener( int buttonId )
      {
         _buttonId = buttonId;
      }
      public void actionPerformed( ActionEvent ev )
      {
          switch ( _buttonId )
          {
             case 0:
                _mgr.newInputString();
                break;
             case 1:
                _mgr.newInputFile();
                break;
             case 2:
                _mgr.execute();
                break;
             default:
                _mgr.definePattern( _buttonId - 3 );
                break;
          }               
      }
   }         
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      RegexApp r = new RegexApp( "Regex in Java", args );
   }
}
