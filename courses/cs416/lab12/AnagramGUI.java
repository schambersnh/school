/**
 * AnagramGUI
 * 
 * A simple GUI front end to the Anagram program
 * 
 * It allows the user to change the data structure while executing
 * (rather than having to restart the program).
 *
 * It puts up a radio button menu with the data structure choices.
 * 
 * It also displays a Report button; when clicked, it attempts to
 * report the hash table array statistics -- but only if the current
 * dictionary is a HashTable object.
 *
 * March 2009
 * rdb
 * 
 * Edited: Oct 2010 rdb
 *         Added text entry for specifying table size as a modal parameter
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.text.*;

public class AnagramGUI extends JFrame
{
   //---------------------- instance variables ----------------------
   private int _tableSize = 53;
   private JFormattedTextField _sizeField = null;
   private ButtonGroup         _dsButtonGroup = null; // instance variable so
                                               // can deselect in size change
   //--------------------------- constructor -----------------------
   public AnagramGUI( String title )     
   {
      super( title );
 
      this.setBackground( Color.LIGHT_GRAY );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      JPanel controlPanel = new JPanel();
      String[] dsLabels = { "v: Vector", "l: LinkedList", 
                            "b: BinarySearchVector", "h: HashSet", 
                            "x: bad hash", "j: Java hashCode", "m: my hash" };
      JPanel dsMenu = makeRadioButtonMenu( dsLabels, 
                                             new DataStructureListener() );
      controlPanel.add( dsMenu );
      
      JButton reportButton = new JButton( "Report Table Stats" );
      reportButton.addActionListener( 
         // This code shows an example of an "anonymous" inner class 
         // that extends ActionListener. After the invocation of the 
         // ActionListener constructor, a set of braces indicates that the
         // object to be created should be an instance of a new "unnamed"
         // class that extends ActionListener and overrides the actionPerformed
         // method as specified in the { ... } block that follows the new clause.
         new ActionListener()
         {
            public void actionPerformed( ActionEvent ae ) { Anagram.report(); } 
         }
      );
      controlPanel.add( reportButton );
      controlPanel.add( makeSizeTextField() );
      
      this.add( controlPanel );
      this.setSize( 350, 250 );
      this.setLocation( 300, 300 );
      this.setVisible( true );
   }
   //------------------- makeSizeTextField --------------------------------
   /**
    * create a panel with a set of Radio Buttons whose labels are passed
    * as the first argument. The second argument is an ActionListener object
    * that will handle all the buttons.
    */
   private JPanel makeSizeTextField()
   {
      JPanel sizePanel = new JPanel();
      sizePanel.add( new JLabel( "Enter size: " ));
      NumberFormat sizeFormat = NumberFormat.getIntegerInstance();
      _sizeField = new JFormattedTextField( sizeFormat );
      _sizeField.setValue(  new Integer( _tableSize ));
      _sizeField.setColumns( 4 );
      _sizeField.addPropertyChangeListener( "value", 
          new PropertyChangeListener()
          {
              public void propertyChange(PropertyChangeEvent e) 
              {
                 _tableSize = ((Number)_sizeField.getValue()).intValue();
                 Anagram.setTableSize( _tableSize );
                 System.out.println( "New table size: " + _tableSize );
                 _dsButtonGroup.clearSelection();
              }
          }
      );
      sizePanel.add( _sizeField );
      return sizePanel;
   }
   //------------------- makeRadioButtonMenu --------------------------------
   /**
    * create a panel with a set of Radio Buttons whose labels are passed
    * as the first argument. The second argument is an ActionListener object
    * that will handle all the buttons.
    */
   private JPanel makeRadioButtonMenu( String[] labels, ActionListener listen )
   {
      JPanel bMenu = new JPanel( new GridLayout( 5, 1 )); 
      _dsButtonGroup = new ButtonGroup();
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
         _dsButtonGroup.add( button );
         button.addActionListener( listen );
      }      
      return bMenu;
   }
   //+++++++++++++++++ DataStructureListener inner class ++++++++++++++++++++++++
   /**
    * RadioListener handles all button events and passes them along
    * to methods of the AnagramGUI class.
    */
   public class DataStructureListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         JRadioButton which = (JRadioButton) ev.getSource();
         Anagram.setDataStructure( which.getText() );
         new Anagram( "from AnagramGUI" );
      }
   }       

   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      new AnagramGUI( "AnagramGUI" );
   }
}
