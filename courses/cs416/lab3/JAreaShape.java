/**
 * JAreaShape.java -- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class.
 *    The class extends JShape by adding ability to have a border color and
 *      a fill color.
 * 
 *    This is a frame class for 
 *       JEllipse 
 *       JRectangle
 *    Child classes must override paintComponent.
 * 
 * @author rdb
 * January 2008 (Gwheels)
 *
 * 01/24/09 rdb: Renamed Gwheels to JWheels
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

abstract public class JAreaShape extends JShape 
{
   //---------------- instance variables ------------------------
   private Color _fillColor;
   
   //--------------------  constructors ---------------------------
   /**
    * Constructor for JAreaShape
    */
   public JAreaShape()
   { 
      this( Color.RED );
   }
   /**
    * Constructor for JAreaShape
    */
   public JAreaShape( Color aColor )
   { 
      this( 0, 0 );
      setColor( aColor );
   }
   /**
    * Another wheels-like constructor
    */
   public JAreaShape( int x, int y )
   {
      super( x, y );
      this.setSize( 50, 50 );  // areas have default size
      setColor( Color.RED );   // use wheels default color
   }

   //++++++++++++++++++ wheels-like convenience methods +++++++++++++++++++
   //----------------------- setFillColor( Color ) --------------------
   /**
    * setFillColor -- a wheels-like method
    */
   public void setFillColor( Color aColor ) 
   {
      _fillColor = aColor;
      repaint(); 
   }
   //----------------------- setColor( Color ) -----------------------
   /**
    * setColor -- a wheels-like method
    */
   public void setColor( Color aColor ) 
   {
      setFrameColor( aColor );
      setFillColor( aColor );
   }
   //++++++++++++++++++++++++ accessor methods +++++++++++++++++++++++++++++
   //----------------------- getFillColor() --------------------
   /**
    * getFillColor -- a wheels-like method
    */
   public Color getFillColor() 
   {
      return _fillColor;
   }
   //----------------------- getColor() -----------------------
   /**
    * getColor -- a wheels-like method
    */
   public Color getColor() 
   {
      return _fillColor;
   }
}
