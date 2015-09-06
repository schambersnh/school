/**
 * BinTreeLab -- Trees
 * 
 * This program has a slightly different structure than most of the
 * previous assignments. The goal is to distribute of the major 
 * implementation responsibilities to 3 classes which each have a
 * set of related responsibilities:
 * 
 *     - the interaction component, GUI.java
 *     - the non-graphical, non-GUI application semantics, TreeApp.java
 *     - the display component, DisplayListPanel.java
 * 
 * There are application-dependent components in all three, but the
 * partitioning helps to distribute code in a logical way. It also
 * let's you change the form of the GUI or the display output implementation
 * with minimal changes to the other components.
 */
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class BinTreeLab extends JFrame
{
   //---------------------- instance variables ----------------------   
   //--------------------------- constructor -----------------------
   public BinTreeLab( String title, String[] args ) 
   {      
      super( title );
      GUI          gui     = new GUI( this );
      
      add( gui );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      this.pack();
      this.setSize( new Dimension( gui.getWidth() + 600, gui.getHeight() + 100 ));
      this.setVisible( true );
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      BinTreeLab app = new BinTreeLab( "TreeApp", args );
   }
}