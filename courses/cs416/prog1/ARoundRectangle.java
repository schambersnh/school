/**
 * ARoundRectangle.java: version 1 
 * 
 * Extends Java's ARoundRectangle2D.Double class, adding the capabilities to
 * set color, location, and size, to move to a specified
 * location, and to display itself on a panel.
 * 
 * This class is a modification of the SmartRectangle class from
 * Sanders and van Dam, Object-Oriented Programming in Java, Chapter 7.
 * 
 * The modifications include:
 * 1. reformatting to match CS415/416 style
 * 2. more wheels-like functions--simplifies conversion of wheels programs: 
 *       getXLocation(), getYLocation(), setColor, setLineWidth, setThickness
 * 3. added display method that then calls the awt fill and draw methods.
 * 4. this version's interface is entirely "int"-based, whereas the
 *    Smart classes from the book have some int and partial rotation;
 *    the awt classes have fully "int" interfaces.
 * 
 * @author (of the modifications) rdb
 * 
 * 
 * This class is modified from the ARectangle class
 * Stephen Chambers 1/27/2011 for cs416 Lab1
 * 
 * Further modified on 2/2/2011 by Stephen Chambers for Program 1
 * 
 */

import java.awt.geom.*;
import java.awt.*;

public class ARoundRectangle extends java.awt.geom.RoundRectangle2D.Double 
                        implements AShape
{
   //---------------- instance variables ------------------------
   private Color _borderColor;
   private Color _fillColor;
   private int   _lineWidth = 2;
   
   final private int   defaultW = 30;
   final private int   defaultH = 30;
   final private Color defaultColor = Color.RED;
   
   //--------------------  constructor ---------------------------
   /**
    * Constructor from SmartRectangle
    */
   public ARoundRectangle( Color aColor )
   { 
      this( 0, 0 );
      setColor( aColor );   
   }
   /**
    * Another wheels-like constructor
    */
   public ARoundRectangle( int x, int y )
   {
      setRoundRect( x, y, defaultW, defaultH, 20, 20 );
      this.setColor( defaultColor );
   }   
   /**
    * A default constructor
    */
   public ARoundRectangle()
   { 
      this( 0, 0 );
      setColor( defaultColor );   
   }
   public ARoundRectangle( int x, int y, int w, int h, int arcW, int arcH )
   {
     setRoundRect(x, y, w, h, arcW, arcH);
     this.setColor(defaultColor);
     
   }
 
   
   //++++++++++++++++++ wheels-like convenience methods +++++++++++++++++++
   //----------------------- setFrameColor( Color ) --------------------
   /**
    * setFrameColor -- a wheels method
    */
   public void setFrameColor( Color aColor ) 
   {
      _borderColor = aColor;
   }
   public void setArcSize(double arcWidth, double arcHeight)
   {
      setRoundRect(getX(), getY(), getWidth(), getHeight(), arcWidth, arcHeight);
   }
   //----------------------- setFillColor( Color ) --------------------
   /**
    * setFillColor -- a wheels method
    */
   public void setFillColor( Color aColor ) 
   {
      _fillColor = aColor;
   }
   //----------------------- setColor( Color ) -----------------------
   /**
    * setColor -- a wheels method
    */
   public void setColor( Color aColor ) 
   {
      _fillColor   = aColor;
      _borderColor = aColor;
   }
   //----------------------- setThickness( int ) -------------------------
   /**
    * setThickness -- a wheels method
    */
   public void setThickness( int w ) 
   {
      _lineWidth = w;
   }
   //----------------------- setLineWidth( int ) --------------------------
   /**
    * setLineWidth -- same as setThickness; awt uses the term "LineWidth"
    */
   public void setLineWidth( int w ) 
   {
      _lineWidth = w;
   }

   //----------------------- getXLocation() -------------------------------
   /**
    * getXLocation() - a wheels method
    *                  return int value of x location
    */
   public int getXLocation()
   {
      return (int) this.getX();
   }
   //----------------------- getYLocation() -------------------------------
   /**
    * getYLocation() - a wheels method
    *                  return int value of y location
    */
   public int getYLocation()
   {
      return (int) this.getY();
   }
   //------------------- setLocation( Point ) -----------------------
   /**
    * setLocation( Point ) -- a wheels method
    */
   public void setLocation( Point p ) 
   {
      setLocation( p.x, p.y );
   }
   //------------------- setLocation( int, int ) -----------------------
   /**
    * setLocation( int, int ) -- wheels-like
    */
   public void setLocation( int x, int y ) 
   {
      super.setFrame( x, y, this.getWidth(), this.getHeight() );
   }
   //----------------------- setSize( int, int ) ---------------------------
   /**
    * setSize( int, int )  - a wheels method
    */
   public void setSize ( int aWidth, int aHeight ) 
   {
      super.setFrame( this.getX(), this.getY(), aWidth, aHeight );
   }
   //----------------------- moveBy( int, int ) -------------------------------
   /**
    * moveBy( dx, dy ) -- move the location by delta
    */
   public void moveBy( int dX, int dY ) 
   {
      super.setFrame( this.getX() + dX,
                      this.getY() + dY,
                      this.getWidth(),
                      this.getHeight());
   }
   //----------------------- display( Graphics2D ) -------------------------
   /**
    * display - calls draw and fill awt methods (this is an rdb method).
    */
   public void display( java.awt.Graphics2D brush2D )
   {
      fill( brush2D );
      draw( brush2D );
   }
   //-------------------------- fill( Graphics2D ) ------------------------
   /**
    * fill - overrides parent method
    * 
    * See implementation comments in the AEllipse class.
    */
   public void fill( java.awt.Graphics2D brush2D )
   {
      Color savedColor = brush2D.getColor();
      brush2D.setColor( _fillColor );
      brush2D.fill( this );     // "this" class, ARoundRectangle, is subclass
                                // of java.awt.geom.Rectangle2D.Double
                                // A Graphics2D object knows how to fill it.
      brush2D.setColor( savedColor );
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
      brush2D.setColor( _borderColor );
      java.awt.Stroke savedStroke = brush2D.getStroke();
      brush2D.setStroke( new java.awt.BasicStroke( _lineWidth ));
      brush2D.draw( this );     // "this" class, ARoundRectangle, is subclass
                                // of java.awt.geom.Rectangle2D.Double
                                // A Graphics2D object knows how to draw the
                                // the border of this kind of object.
      brush2D.setStroke( savedStroke );
      brush2D.setColor( savedColor );
   }
}
