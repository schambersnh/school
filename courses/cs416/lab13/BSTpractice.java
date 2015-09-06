/**
 * BSTpractice -- List and Recursion TreeTasks
 * 
 *
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class BSTpractice extends JFrame
{
   //---------------------- instance variables ----------------------   
   //--------------------------- constructor -----------------------
   public BSTpractice( String title, String[] args ) 
   {      
      super( title );
      GUI          gui     = new GUI( this );
      
      add( gui );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      this.setSize(  new Dimension( 950, 200 ));
      this.setLocation( 100, 200 );
      this.setVisible( true );
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      BSTpractice app = new BSTpractice( "TreeTasks", args );
   }
}
