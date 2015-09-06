/**
 * GNode - represents a d.s. node graphicially as a JLabel
 */

import javax.swing.JLabel;
import javax.swing.border.*;
import java.awt.*;

public class GNode extends JLabel
{
   //-------------------- instance variables -----------------------
   public BinarySearchTree.Node node;    // the data from the real node
   public GNode left;
   public GNode right;
   public GNode parent;
   //-------------------- constructor ------------------------------
   /**
    * set up default values, location gets specified later
    */
   public GNode( BinarySearchTree.Node n, int w, int h )
   {
      super( n.data.toString() );
      node = n;
      setFont( getFont().deriveFont( 11.0f )); // make the font smaller

      setSize( w, h );
      setBorder( new LineBorder( Color.BLACK, 2 ));
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
}