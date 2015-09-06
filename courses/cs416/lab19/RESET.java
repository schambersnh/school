/**
 * Reset -- RESET: Regular Expression Simple Exploration Tool
 * 
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class RESET extends JFrame
{
   //---------------------- instance variables ----------------------   
   //--------------------------- constructor -----------------------
   public RESET( String title, String[] args ) 
   {      
      super( title );
      GUI   gui = new GUI( this );
      
      add( gui );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      this.setSize( 1000, 700 );
      this.setVisible( true );
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      RESET r = new RESET( "Regular Expression Simple Exploration Tool", args );
   }
}
