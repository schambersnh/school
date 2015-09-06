/**
 * DictionaryPanel - Data structure manipulation 
 * 
 * This class allows the user to add and remove object to a data collection
 * organized as a Stack, Queue or alphabetically sorted list.
 * 
 * The state of the collection is shown in a simple graphical representation.
 * 
 * This is not a complete implementation. It needs the following
 * methods completed:
 *     removeItem
 *     removeItemByKey
 * and the links in the graphical displa should be replaced with something 
 * that shows which end of the link as some kind of arrow head.
 * 
 * In addition the QueueList and StackList and SortedList classes are
 * not complete.
 * 
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DictionaryPanel extends JPanel
{
   //---------------------- instance variables ----------------------
   private StringDictionary     _stringDict = null;   
   private DisplayPanel         _displayPanel;
 
   private boolean   _print = true;   // if set, print after each command
   //--------------------------- constructor -----------------------
   public DictionaryPanel( Container parent )     
   {   
      this.setLayout( new BorderLayout() );
      
      buildGUI();
      _stringDict = new StringDictionary();
      _displayPanel.setTree( _stringDict.getTree() );
      _displayPanel.update();
   }
   //---------------------------- buildGUI -------------------------------------
   private void buildGUI()     
   {
      JPanel northPanel = new JPanel();
      String[] buttonLabels = { "Print",  "Add", "Clear", "Find",  
                                "First", "Next" };
      JPanel buttonMenu = makeButtonMenu( buttonLabels );
      
      JPanel radioPanel = new JPanel( new GridLayout( 3, 1 ));
      String[] dataTypeLabels = { "*String", "DataIntDict", "DataStrDict",
                                  "Dict<DataStr>", "Dict<DataInt>" };
      JPanel dataTypeMenu = makeRadioButtonMenu( dataTypeLabels, 
                                             new DataTypeListener() );
      radioPanel.add( dataTypeMenu );
            
      String[] printOptions = { "*Print On", "Print Off" };
      JPanel printMenu = makeRadioButtonMenu( printOptions, 
                                             new PrintOptionListener() );      
      radioPanel.add( printMenu );
      
      northPanel.add( buttonMenu );
      northPanel.add( radioPanel );
      this.add( northPanel, BorderLayout.NORTH );
      
      _displayPanel = new DisplayPanel();
      this.add( _displayPanel, BorderLayout.CENTER );
   }
   //------------------- makeButtonMenu --------------------------------
   private JPanel makeButtonMenu( String[] labels )
   {
      // JPanel defaults to FlowLayout
      
      JPanel bMenu = new JPanel( new GridLayout( 3, 1 )); 
      JButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         button = new JButton( labels[ i ] );
         bMenu.add( button );
         button.setSize( new Dimension( 50, 20 ));
         button.addActionListener( new ButtonListener( i ));
      }      
      return bMenu;
   }
   //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener handles all button events and passes them along
    * to methods of the DictionaryPanel class.
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
             case 0:   // Print
                printDict( "StringDictionary" );
                break;
             case 1:   // Add
                addItems();
                break;
             case 2:   // Clear
                _stringDict.clear();
                if  ( _print )
                   printDict( "After clear()" );
                _displayPanel.setTree( _stringDict.getTree() );              
                break;
             case 3:   // Find
                findItem();
                break;
             case 4:    // first
                showData( "First", _stringDict.first() );
                break;
             case 5:    // next
                showData( "Next", _stringDict.next() );
                break;
          } 
      }
   }         
   //------------------- makeRadioButtonMenu --------------------------------
   private JPanel makeRadioButtonMenu( String[] labels, ActionListener listen )
   {
      JPanel bMenu = new JPanel(); 
      bMenu.setLayout( new GridLayout( 2, 3 ));
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
   //+++++++++++++++++ DataTypeListener inner class ++++++++++++++++++++++++
   /**
    * RadioListener handles all button events and passes them along
    * to methods of the AnagramGUI class.
    */
   public class DataTypeListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         String which = ((JRadioButton) ev.getSource()).getText();
         _stringDict = null;

         if ( which.equals( "String" ))
            _stringDict = new StringDictionary();
         else
            System.err.println( "Unimplemented dictionary: " + which );
         System.out.println( "-------- New Empty Dictionary --------------" );
      }
   }         
   //+++++++++++++++++ PrintOptionListener inner class ++++++++++++++++++++++++
   /**
    * RadioListener handles all button events and passes them along
    * to methods of the AnagramGUI class.
    */
   public class PrintOptionListener implements ActionListener
   {
      public void actionPerformed( ActionEvent ev )
      {
         JRadioButton which = (JRadioButton) ev.getSource();
         if ( which.getText().equals( "Print On" ))
            _print = true;
         else
            _print = false;
      }
   }         

   //---------------------- addItems -----------------------------
   /**
    * execute dialog to add items to Dictionary 
    */
   public void addItems( )
   {  
      String inMessage = "Enter words to add to Dictionary";
      String input = JOptionPane.showInputDialog( null,  inMessage ); 
      String key;
      String rest = "empty";
      
      if ( input != null && input.trim().length() > 0 )
      {
         String[] keys = input.split( " " );
         for ( int i = 0; i < keys.length; i++ )
         {
            if ( _stringDict != null )
               _stringDict.add( new String( keys[ i ] ));
            else
               System.err.println( "addItem: No active dictionary" );
         }
      }
      if  ( _print )
         printDict( "After add()" );
      _displayPanel.setTree( _stringDict.getTree() );
   }
   //---------------------- findItem -----------------------------
   /**
    * execute dialog to find an item and show if successful
    */
   public void findItem( )
   {  
      String inMessage = "Enter word to find; only 1 word will be searched";
      String input = JOptionPane.showInputDialog( null,  inMessage ); 
      String key;
      String msg;
      
      while ( input != null && input.trim().length() > 0 )
      {
         String[] keys = input.split( " " );
         String result = null;
         result = _stringDict.find( keys[ 0 ] );
         
         if ( result == null )
            msg = keys[ 0 ] + "  NOT found.\n Enter another word?";
         else 
            msg = "Found item: " + result + "\n Enter another word?";
         
         input = JOptionPane.showInputDialog( null,  msg );
      }
   }
     
   //+++++++++++++++++++++++++++++ Utility methods +++++++++++++++++++++++++
   
   //---------------- showData( String, String  ) --------------
   /**
    * Show a word in a dialog box
    */
   private void showData( String label, String data )
   {
      String msg;
      
      if ( data == null )
         msg = label + ": Not found";
      else
         msg = label + ": " + data;
      JOptionPane.showMessageDialog( null,  msg );
   }
        
   //----------------------- printDict -----------------------------
   private void printDict( String title )
   {
      
       System.out.println( "----------------" + title + " ------------" );
       if ( _stringDict != null )
          System.out.print( _stringDict );
       else
          System.out.print( "No active Dictionary" );
          
       System.out.println( "\n---------------------------------------------" );
   }
   //-----------------------------------------------------------------
   public static void main( String[] args )
   {
      new DictionaryApp( "Dictionary", args );
   }
}
