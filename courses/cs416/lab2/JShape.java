/**
 * JShape.java -- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class.
 *    The class extends JComponent, but the interface is patterned
 *    after that of wheels. This is a frame class for
 *       JAreaShape
 *         JEllipse 
 *         JRectangle
 *       JLine
 *    Child classes must override paintComponent.
 * 
 * @author rdb
 * January 2008  (Gwheels)
 *
 * 01/24/09 rdb: Converted from Gwheels of previous semesters; 
 *               figured out clipping, so could delete code that
 *               had been in Gwheels version to add an extra border
 *               area to the JComponent.
 * 01/16/10 rdb: Removed parent parameter to the constructors and
 *               instance variables; can always do getParent()
 */

import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

abstract public class JShape extends JComponent 
{
   //---------------- instance variables ------------------------
   private   Color     _lineColor;
   private   int       _lineWidth = 1;
   private Point       _lastMouse;

   //--------------------  constructors ---------------------------
   /**
    * Constructor from JShape
    */
   public JShape()
   { 
      this( Color.RED );
   }
   /**
    * Constructor from JShape
    */
   public JShape( Color aColor )
   { 
      _lineColor = aColor;
   }
   /**
    * Another wheels-like constructor
    */
   public JShape( int x, int y )
   {
      this( Color.RED );
      this.setLocation( x, y );
   }

   //++++++++++++++++++ wheels-like convenience methods +++++++++++++++++++
   //----------------------- setFrameColor( Color ) --------------------
   /**
    * setFrameColor -- a wheels-like method; defaults to setting line color
    */
   public void setFrameColor( Color aColor ) 
   {
      setLineColor( aColor );
   }
   /**
    * setLineColor -- a wheels -like method; same as setFrameColor, but
    *                 "lineColor" is a more common term 
    */
   public void setLineColor( Color aColor ) 
   {
      _lineColor = aColor;
      this.repaint( this.getBounds() );  // try to limit repaint area
   }
   //----------------------- setColor( Color ) -----------------------
   /**
    * setColor -- a wheels-like method; defaults to setting the line color
    */
   public void setColor( Color aColor ) 
   {
      setLineColor( aColor );
   }
   //----------------------- setThickness( int ) -------------------------
   /**
    * setThickness -- a wheels method
    */
   public void setThickness( int w ) 
   {
      setLineWidth( w );
   }
   //----------------------- setLineWidth( int ) --------------------------
   /**
    * setLineWidth -- same as setThickness, but "line width" is the AWT
    *                 common term
    */
   public void setLineWidth( int w ) 
   {
      _lineWidth = w;
      this.repaint( this.getBounds() );  // try to limit repaint area
   }
   //------------------- setLocation( Point ) -----------------------
   /**
    * setLocation( Point ) -- a wheels method, but it just calls parent.
    */
   public void setLocation( Point p ) 
   {
      setLocation( p.x, p.y );
   }
   //++++++++++++++++++++++++ accessor methods +++++++++++++++++++++++++++++
   //----------------------- getColor() -----------------------
   /**
    * getColor -- a wheels method; defaults to line color
    */
   public Color getColor() 
   {
      return _lineColor;
   }
   //----------------------- getFrameColor() -----------------------
   /**
    * getFrameColor -- a wheels-like method; defaults to line color
    */
   public Color getFrameColor() 
   {
      return _lineColor;
   }
   //----------------------- getLineColor() -----------------------
   /**
    * getLineColor -- a wheels-like method
    */
   public Color getLineColor() 
   {
      return _lineColor;
   }
   //----------------------- getBorderColor() -----------------------
   /**
    * getBorderColor -- a wheels-like method; defaults to line color
    */
   public Color getBorderColor() 
   {
      return _lineColor;
   }
   //----------------------- getThickness() -------------------------
   /**
    * getThickness -- a wheels-like method
    */
   public int getThickness() 
   {
      return _lineWidth;
   }
   //----------------------- getLineWidth( ) --------------------------
   /**
    * getLineWidth -- same as getThickness, but I like the name better
    */
   public int getLineWidth() 
   {
      return _lineWidth;
   }
   //----------------------- getXLocation() -------------------------------
   /**
    * getXLocation() - a wheels-like method
    *                  return int value of x location
    */
   public int getXLocation()
   {
      return this.getX();
   }
   //----------------------- getYLocation() -------------------------------
   /**
    * getYLocation() - a wheels-like method
    *                  return int value of y location
    */
   public int getYLocation()
   {
      return this.getY();
   }

   //----------------------- moveBy( int, int ) --------------------------
   /**
    * moveBy( dx, dy ) -- move the location by delta
    */
   public void moveBy( int deltaX, int deltaY ) 
   {
      // specify that the region currently occupied by the shape should
      //  be repainted at next opportunity.
      this.repaint( this.getBounds() ); 

      // move the shape
      setLocation( getX() + deltaX, getY() + deltaY );

      // specify that the new region occupied by the shape needs to
      //  be repainted.
      this.repaint( this.getBounds() );
   }

}
