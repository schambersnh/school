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

public class GUI extends JPanel 
{
   //---------------- instance variables ---------------------------
   private Container    _parent;
   private DisplayPanel _display;
   private TreeApp      _app;
   private ControlPanel _controls;
   
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * along with the application object.
    */
   public GUI( Container parent ) 
   {
      super ( new BorderLayout() );
      
      _parent  = parent;  
      
      _display = new DisplayPanel();
      _app     = new TreeApp( _display );
           
      JScrollPane sPane = new JScrollPane( _display,
               ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
               ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
      );
      //sPane.getVerticalScrollBar().setUnitIncrement(10);
      //sPane.getHorizontalScrollBar().setUnitIncrement(10);
      
      sPane.setPreferredSize( new Dimension( 500, 400 ));     
      
      this.add( sPane, BorderLayout.CENTER );
      
      //create ControlPanel in the South
      _controls = new ControlPanel( this, _app );
      this.add( _controls, BorderLayout.SOUTH );

      //create Buttons in the North
      Component buttonMenu = makeButtonMenu();
      this.add( buttonMenu, BorderLayout.NORTH );
   }
   //------------------- makeButtonMenu --------------------------------
   private Component makeButtonMenu()
   {
      // JPanel defaults to FlowLayout
      String[] labels = { "Data", "Balance", "Delete by name", 
                          "Name Count", "SwapData", "Null Descendants",
                          "Find Path"
                         };
      
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
                _app.makeData( false ); // don't allow duplicates
                break;
             case 1:
                _app.balanceTree();
                break;
             case 2:
                _app.delByName();
                break;
             case 3:
                _app.nameCount();
                break;
             case 4:
                _app.swapData();
                break;
             case 5:
                _app.nullDescendents();
                break;
             case 6:
                _app.findPath();
                break;
           }               
      }
   }         
}
