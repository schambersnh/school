/**
 * GNode - represents a d.s. node graphicially as a JLabel
 *         it has a reference to the BinarySearchTreeNode that it
 *         is representing.
 */

import javax.swing.JLabel;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class GNode extends JLabel implements MouseListener
{
   //-------------------- instance variables -----------------------
   public BinarySearchTree.Node bstNode; // reference to the bst node
   public GNode left;
   public GNode right;
   //-------------------- constructor ------------------------------
   /**
    * set up default values, location gets specified later
    */
   public GNode( BinarySearchTree.Node node, int w, int h )
   {
      super( node.data.toString() );
      //nodeData = node.data;
      bstNode = node;
      setFont( getFont().deriveFont( 10.0f )); // make the font smaller

      setSize( w, h );
      setBorder( new LineBorder( Color.BLACK, 1 ));
      addMouseListener( this );
   }   
   //----------------- getNextStart() ----------------------------
   /**
    * Return the position that should be the start of a link
    */
   Point getStart()
   {
      return new Point( getX() + getWidth() / 2, 
                        getY() + getHeight() );
   }
   //----------------- getNextEnd() ----------------------------
   /**
    * Return the position that should be the end of a link
    */
   Point getEnd()
   {
      return new Point( getX() + getWidth() / 2, getY());
   }
   //--------------------- mousePressed --------------------------
   /**
    * mousePressed -- unused
    */ 
   public void mousePressed( MouseEvent e ) 
   {
      System.out.println( "MousePressed: " + bstNode.data );
      TreeApp.treeApp().mouseEvent( this.bstNode );
   }
   
   //----------------------------------------------------------
   /**
    * Unimplemented  mouse methods
    */ 
   public void mouseClicked( MouseEvent e ) { }
   public void mouseReleased( MouseEvent e ) {}
   public void mouseEntered( MouseEvent e ) {}
   public void mouseExited( MouseEvent e ) {}
}