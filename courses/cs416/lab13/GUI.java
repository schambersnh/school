/** 
 * GUI.java:
 * A JPanel to encapsulate the application's interface to the user
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
   private ControlPanel _controls;
   
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * as is the display panel that will go into the center
    */
   public GUI( Container parent ) 
   {
      super ( new BorderLayout() );
      _parent  = parent;
      
      //create ControlPanel in the South
      _controls = new ControlPanel( this );
      this.add( _controls, BorderLayout.SOUTH );

      //create Buttons in the North
      Component buttonMenu = makeButtonMenu();
      this.add( buttonMenu, BorderLayout.NORTH );
   }
   //------------------- makeButtonMenu --------------------------------
   private Component makeButtonMenu()
   {
      // JPanel defaults to FlowLayout
      String[] labels = { "Data", "Print",  "Max Find", 
                          "InOrder", "PreOrder", "Find" };
      
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
                TreeTasks.makeData();
                break;
             case 1:
                TreeTasks.showTree();
                break;
             case 2:
                TreeTasks.maxFind();
                break;
             case 3:
                TreeTasks.inOrderPrint();
                break;
             case 4:
                TreeTasks.preOrderPrint();
                break;
             case 5:
                TreeTasks.find();
                break;
           }               
      }
   }         
}
