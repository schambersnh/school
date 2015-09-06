/**
* DictionaryApp - basis for experimenting with different kinds of dictionaries.
 * 
 * This class creates a DictionaryPanel .
 * 
 * rdb
 * 03/03/08
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class DictionaryApp extends JFrame
{
   //---------------------- instance variables ----------------------
   private DictionaryPanel _appPanel;
   
   //--------------------------- constructor -----------------------
   public DictionaryApp( String title, String[] args )     
   {
      super( title );
 
      this.setBackground( Color.WHITE );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      _appPanel = new DictionaryPanel( this );
      this.add( _appPanel );
      
      this.setSize( new Dimension( 800, 700 ));
      this.setVisible( true );
   }
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      new DictionaryApp( "DictionaryApp", args );
   }
}