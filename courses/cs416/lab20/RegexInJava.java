/**
 * RegexInJava
 * 
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class RegexInJava extends JFrame
{
   //---------------------- instance variables ----------------------   
   //--------------------------- constructor -----------------------
   public RegexInJava( String title, String[] args ) 
   {      
      super( title );
      GUI   gui = new GUI( this );
      
      add( gui );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      this.pack();
      this.setSize(  new Dimension( gui.getWidth(), gui.getHeight() + 100 ));
      this.setVisible( true );
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      RegexInJava r = new RegexInJava( "Regex in Java", args );
   }
}