/** 
 * GUI.java:
 * A JPanel to control the application's interface to the application
 *
 * @author rdb
 * CS416 Spring 2008
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
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
      JPanel    buttonPanel = new JPanel();
      buttonPanel.add( makeButtonMenu() );
      //String[] radioLabels = { "AutoBalance", "*ManualBalance" };
      //buttonPanel.add( makeRadioMenu( radioLabels, new AutoBalanceListener() ));
      this.add( buttonPanel, BorderLayout.NORTH );
   }
   //------------------- makeButtonMenu --------------------------------
   private Component makeButtonMenu()
   {
      // JPanel defaults to FlowLayout
      String[] labels = { "Data", "Print", "Split", "Merge", "PointsInRange" };
      
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
   //------------------- makeRadioMenu --------------------------------
   /**
    * create a panel with a set of Radio Buttons whose labels are passed
    * as the first argument. The second argument is an ActionListener object
    * that will handle all the buttons.
    */
   private JPanel makeRadioMenu( String[] labels, ActionListener listen )
   {
      JPanel bMenu = new JPanel( new GridLayout( 1, 2 )); 
      ButtonGroup group = new ButtonGroup();
      bMenu.setBorder( new LineBorder( Color.BLACK, 2 ));
      JRadioButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         if ( labels[i].charAt( 0 ) == '*' )  // this button should be selected
         {  
            button = new JRadioButton( labels[i].substring( 1 )); // remove *
            button.setSelected( true );
         }
         else
            button = new JRadioButton( labels[ i ] );
         bMenu.add( button );
         group.add( button );
         button.addActionListener( listen );
      }      
      return bMenu;
   }
   //++++++++++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++

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
                _app.makeData();
                break;
             case 1:
                _app.print();
                break;
             case 2:
                _app.split();
                break;
             case 3:
                _app.merge();
                break;
             case 4:
                _app.pointsInRange();
                break;
             case 5:
                _app.balance();
                break;
           }               
      }
   }         
   //+++++++++++++++++ AutoBalanceListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener handles all button events and passes them along
    * to methods of the ListPanel class.
    */
   public class AutoBalanceListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         JRadioButton which = (JRadioButton) ev.getSource();
      }
   }         
}
