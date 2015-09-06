/**
 * ALine.java -- version 1
 *              a convenience class for simplifying access to awt ALine2D.
 *              the model for this class is the wheels ALine class.
 *          
 * This class is patterned after the SmartEllipse and SmartRectangle classes
 * from Ch 7 of Sanders and van Dam, Object-Oriented Programming with Java
 * as revised into our AEllipse and ARectangle classes.
 * 
 * @author rdb
 * Last modified: January 2010
 */

import java.awt.geom.*;
import java.awt.*;

public class ALine extends java.awt.geom.Line2D.Double 
                   implements AShape
{
   //---------------- instance variables -------------------------------
   private Color _lineColor; 
   private int   _lineWidth = 1;

   private Color defaultColor = Color.BLACK;
   
   //----------------- constructors ------------------------------------
   /**
    * No argument constructor
    */
   public ALine()
   {
      setColor( defaultColor );
   }
   
   /**
    *  Constructor based on constructors for SmartEllipse and SmartRectangle
    */
   public ALine( Color aColor )
   { 
      _lineColor = aColor;
   }
   /**
    * Another wheels-like constructor
    */
   public ALine( int x1, int y1, int x2, int y2 )
   {
      this( Color.RED );
      this.setPoints( x1, y1, x2, y2 );
   }   
   //--------------------- wheels-like convenience methods -----------------
   //------------------- setColor( Color ) ------------------------------
   /**
    * setColor -- a wheels method
    */
   public void setColor( Color aColor ) 
   {
      _lineColor = aColor;
   }
   //------------------- setThickness( int ) ------------------------------
   /**
    * setThickness -- a wheels method
    */
   public void setThickness( int w ) 
   {
      _lineWidth = w;
   }
   //------------------- setLineWidth( int ) ------------------------------
   /**
    * setLineWidth -- same as setThickness; awt uses the term "LineWidth"
    */
   public void setLineWidth( int w ) 
   {
      _lineWidth = w;
   }

   //------------------- getXLocation() ------------------------------
   /**
    * getXLocation() - a wheels-like method
    *                  return int value of x location
    */
   public int getXLocation()
   {
      return (int) this.getX1();
   }
   //------------------- getYLocation() ------------------------------
   /**
    * getYLocation() - a wheels-like method
    *                  return int value of y location
    */
   public int getYLocation()
   {
      return (int) this.getY1();
   }
   //-------------- setPoints( int, int, int, int ) -------------
   /**
    * setPoints -- wheels-like method
    */
   public void setPoints( int x1, int y1, int x2, int y2 ) 
   {
      this.setLine ( x1, y1, x2, y2 );
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
      this.moveBy( (int)( x - this.getX1()), (int)( y - this.getY1()) );
   }
   //------------------------ setSize( int, int ) ------------------------
   /**
    * setSize( int, int ) -- wheels-like
    *           this changes the 2nd end point, the parameters
    *           are assumed to represent the relative position of the 2nd
    *           end point to the first
    */
   public void setSize (int aWidth, int aHeight) 
   {
      this.setLine( this.getX1(), this.getY1(), 
                     this.getX1() + aWidth, this.getY1() + aHeight );
   }
   //--------------------------- moveBy( int, int ) ------------------------
   /**
    * moveBy( dx, dy ) -- move the location by delta
    */
   public void moveBy( int dX, int dY ) 
   {
      this.setLine( this.getX1() + dX, this.getY1() + dY,
                    this.getX2() + dX, this.getY2() + dY );
   }
   
   //--------------------------- display( Graphics2D ) ------------------------
   /**
    * display - calls draw awt method for the line; there is no fill for
    *           a line.
    */
   public void display( java.awt.Graphics2D brush2D )
   {
      draw( brush2D );
   }
   //--------------------------- draw( Graphics2D ) ------------------------
   /**
    * draw - overrides parent method
    * 
    * See implementation comments in the AEllipse class.
    */
   public void draw( java.awt.Graphics2D brush2D ) 
   {
      Color savedColor = brush2D.getColor();
      brush2D.setColor( _lineColor );
      java.awt.Stroke savedStroke = brush2D.getStroke();
      brush2D.setStroke( new java.awt.BasicStroke( _lineWidth ));
      
      brush2D.draw( this );     // "this" class, ALine, is subclass
                                // of java.awt.geom.Line2D.Double
                                // A Graphics2D object knows how to draw the
                                // this kind of object.
      brush2D.setStroke( savedStroke );
      brush2D.setColor( savedColor );
   }
}
