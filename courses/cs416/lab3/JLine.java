/**
 * JLine.java -- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class. The interface is patterned
 *    after that of wheels.
 *
 * It has functions for setting the color, location and size and           
 * move by a specified offset, and to display itself on a panel. 
 * There is no rotation and no "double" parameters in the interface
 *
 * The "location" is the position of the first endpoint, 
 * the "size" (width, height) is the x2 - x1, y2 - y1
 * 
 * @author rdb
 * January 2008
 *
 * 01/24/09 rdb: Created from Gwheels versions
 * 01/25/09 rdb: Figured out correct clip spec based on linewidth
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class JLine extends JShape
{
   //---------------- instance variables -------------------------------
   private int    _x1, _y1;
   private int    _x2, _y2;
   private int    _drawX1, _drawY1;
   private int    _drawX2, _drawY2;

   //----------------- constructors ------------------------------------
   /**
    * No parameter constructor
    */
   public JLine()
   {
      this( Color.BLACK );
   }
   
   /**
    *  Constructor based on constructors for SmartEllipse and SmartRectangle
    */
   public JLine( Color aColor )
   { 
      this( 0, 0, 10, 10 );
      setColor( aColor );
   }
   /**
    * Another wheels-like constructor
    */
   public JLine( int x1, int y1, int x2, int y2 )
   {
      super( Color.RED );
      setPoints( x1, y1, x2, y2 );
   }   
   //--------------------- wheels-like convenience methods -----------------
   //------------------- getXLocation() ------------------------------
   /**
    * getXLocation() - a wheels-like method
    *                  return int value of x location
    */
   public int getXLocation()
   {
      return _x1;
   }
   //------------------- getYLocation() ------------------------------
   /**
    * getYLocation() - a wheels-like method
    *                  return int value of y location
    */
   public int getYLocation()
   {
      return _y1;
   }
   //-------------------- setPoints( int, int, int, int ) -----------------
   /**
    * setPoints -- wheels-like method
    *           define the endpoints of the line with int
    */
   //public void setPoints( double x1, double y1, double x2, double y2 ) 
   public void setPoints( int x1, int y1, int x2, int y2 ) 
   {
      _x1 = x1;  _y1 = y1;
      _x2 = x2;  _y2 = y2;
      updateComponent();
   }
   //----------------- setLocation( int, int ) ------------------------
   /**
    * setLocation( int, int ) -- wheels-like
    *         location is the first endpoint
    *         changing the location assumes the other end point should
    *         move accordingly.
    */
   public void setLocation( int x, int y )
   {
      _x1 = x;
      _y1 = y;
      updateComponent();
   }
   //------------------------ setSize( int, int ) ------------------------
   /**
    * setSize( int, int ) -- wheels-like
    *           this changes the 2nd end point, the parameters
    *           are assumed to represent the relative position of the 2nd
    *           end point to the first, so they can be negative
    */
   public void setSize (int w, int h) 
   {
      _x2 = _x1 + w;
      _y2 = _y1 + h;
      updateComponent();
   }
   //--------------------------- moveBy( int, int ) ------------------------
   /**
    * moveBy( dx, dy ) -- move the location by delta
    */
   public void moveBy( int deltaX, int deltaY ) 
   {
      _x1 += deltaX;
      _y1 += deltaY;
      _x2 += deltaX;
      _y2 += deltaY;
      updateComponent();
   }
   //--------------------------- updateComponent() ------------------------
   /**
    * updateComponent - update size and location of the enclosing JComponent
    */
   private void updateComponent( )
   {
      // find the upper left corner of the bounding box of the line. This 
      //   will be the JComponent's location.
      int locX   = Math.min( _x1, _x2 );
      int locY   = Math.min( _y1, _y2 );
      int width  = Math.max( _x1, _x2 ) - locX;
      int height = Math.max( _y1, _y2 ) - locY;
      width      = Math.max( width, 1 );
      height     = Math.max( height, 1 );

      super.setLocation( locX, locY );
      super.setSize( width, height );
 
      // compute the coordinates of the line inside the JComponent
      //   these will be relative to the upper left corner.
      _drawX1 = _x1 - locX;
      _drawY1 = _y1 - locY;
      _drawX2 = _x2 - locX;
      _drawY2 = _y2 - locY;
   }
   
   //--------------------- paintComponent( Graphics ) ------------------------
   /**
    * paintComponent - override JComponent method
    */
   public void paintComponent( java.awt.Graphics brush )
   {
      super.paintComponent( brush );
      Graphics2D brush2 = (Graphics2D) brush;
      int w = getWidth();
      int h = getHeight();
      int lw = getLineWidth();
      
      // Need to make the clip window bigger than the component in order to 
      // accommodate a vertical line of some line width. Need border on 
      // all sides that is lineWidth wide.
      //   
      brush2.setClip( -lw, -lw, w + 2 * lw, h + 2 * lw );
      
      // note that the line is displayed in a JComponent that occupies the
      // region of the rectangle that bounds the line. All drawing is relative
      // to the JComponent locaton. Hence, we had to compute the line's end
      // points relative to this location. These coordinates are saved in
      // the "draw" instance variables.
      
      brush2.setColor( getColor() );
      brush2.setStroke( new BasicStroke( lw ));
      brush2.drawLine( _drawX1, _drawY1, _drawX2, _drawY2 );
   }
}
