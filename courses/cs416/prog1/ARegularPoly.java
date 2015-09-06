/**
 * ARegularPoly.java-- a wheels-like convenience class for generating
 * regular polygons; and for using the AWT polygon object
 *
 * 
 *    
 * @author  rdb
 * January 2011
 * 
 * Skeleton Completed by Stephen Chambers for Program 1 on 2/2/2011
 */

import java.awt.geom.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.*;

public class ARegularPoly implements AShape
{
  //---------------- class variables ---------------------------
  private Color            defaultColor = Color.RED;
  
  //---------------- instance variables ------------------------
  // attributes
  private Color            _borderColor;
  private Color            _fillColor;
  private int              _rotation  = 0;
  private int              _lineWidth = 2;
  
  // polygon variables
  private java.awt.Polygon   _polygon = null;
  private int                _nSides;
  private int                _radius;  
  private java.awt.Rectangle _bnds;  
  private int                _ulX;  // upperLeft corner of bounding box
  private int                _ulY;  //   of the base vertices
  private int[]              _inty;
  private int[]              _intx;
  private double[]           _x;    // coords of polygon in double at origin 
  private double[]           _y;
  
  //--------------------  constructors ---------------------------
  /**
   * Constructor from ARegularPoly
   */
  public ARegularPoly( int x, int y, int nSides, int radius )
  { 
    _nSides = nSides;
    _radius = radius;
    setColor( defaultColor );
    makeVertices();   // make the vertices relative to the origin at center
    makePolygon( x, y );    // make java.awt.Polygon 
  }
  
  //-------------------- makeVertices() ------------------------------
  /**
   * Generate double vertex arrays for the x,y "base" vertices for this 
   * RegularPolygon specification. The base vertices represent the shape
   * the regular polygon with its center at (0,0).
   * 
   * Do all calculation in float based on the regular polygon with center at 
   * 0,0. First vertex is always at alpha = 0, horizontal line from center 
   * parallel to x-axis of length radius; i.e., at ( radius, 0 ); rest are
   * at equal angles around the origin.
   * 
   * These vertices are used to create the java.awt.Polygon object which
   * has to be rotated, scaled, and translated before being converted
   * to the int arrays needed for java.awt.Polygon.
   */
  private void makeVertices()
  {
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    
    _x = new double[ _nSides ];
    _y = new double[ _nSides ];
    double alpha = ( _rotation / 180.0 ) * Math.PI;
    
    double   dAlpha = 2 * Math.PI / _nSides;
    for ( int i = 0; i < _nSides; i++ )
    {
      _x[ i ] = Math.cos( alpha ) * _radius;
      _y[ i ] = Math.sin( alpha ) * _radius;
      if ( _x[ i ] < minX )
        minX = _x[ i ];
      if ( _y[ i ] < minY )
        minY = _y[ i ];
      
      alpha += dAlpha;
    }
    _ulX = (int)Math.round( minX );
    _ulY = (int)Math.round( minY );
  }
  //-------------------- makePolygon( int, int ) --------------------------
  /**
   * Create a java.awt.Polygon object for this RegularPolygon specification;
   *   so that the upper left corner of its bounding box is at x,y
   * 
   * The double coordinates in _x[] and _y[] need to be translated 
   * so that the upper left corner of the bounding box of the vertices
   * is at the location specified by the parameters.
   * 
   * The upper left corner of the bounding box of the vertices is
   * stored in the instance variables _ulX and _ulY.
   * 
   * Each vertex (_x[i], _y[i] ) must be translated by 
   *          (locX - _ulX, locY - _ulY )
   * then converted to int and the result assigned to the 
   * positions in the int arrays x[i], y[i].
   * 
   * Since you've just made a new java.awt.Polygon, you also
   * need to get its bounds and save that in the _bnds instance variable.
   */
  private void makePolygon( int locX, int locY )
  {
    _intx = new int[_x.length];
    _inty = new int[_x.length];
    for(int i = 0; i < _x.length; i++)
    { 
      _intx[i] = (int)(_x[i] + locX - _ulX);
      _inty[i] = (int)(_y[i] + locY - _ulY);
    }
    _polygon = new Polygon( _intx , _inty , _intx.length );
    _bnds = _polygon.getBounds();
    ////////////////////////////////////////////////////////////////
    // Need to make the java.awt.Polygon object that represents
    //   this RegularPolygon. See the comments above for help
    ////////////////////////////////////////////////////////////////
    
    
  }
  //----------------------- setRotation( int ) --------------------------
  /**
   * setRotation -- a wheels method
   */
  public void setRotation( int aRotation ) 
  {
    ///////////////////////////////////////////////////////////////
    // This requires regenerating the base vertices and then modifying
    // the vertices of the java.awt.Polygon or creating a new one
    // 
    // Be careful to "remember" the current location of the Polygon
    // so you can know where to move the new specification of the
    // Polygon.
    ////////////////////////////////////////////////////////////////
     int x = getXLocation();
    int y = getYLocation();
    
    _rotation = aRotation;
    makeVertices();
    makePolygon(x, y);
    
    
  }
  //---------------------- setRadius( int ) -----------------------------
  /**
   * Set the radius of the regular polygon to r
   */
  public void setRadius( int r )
  {
    ///////////////////////////////////////////////////////////////
    // This requires regenerating the base vertices and then modifying
    // the vertices of the java.awt.Polygon or creating a new one
    // 
    // Be careful to "remember" the current location of the Polygon
    // so you can know where to move the new specification of the
    // Polygon.
    ////////////////////////////////////////////////////////////////
    int x = getXLocation();
    int y = getYLocation();
    
    _radius = r;
    makeVertices();
    makePolygon(x, y);
    
    
  }
  
  //++++++++++++++++++ wheels-like convenience methods +++++++++++++++++++
  //--------These are all completed ---------------------------------------
  //----------------------- setFrameColor( Color ) --------------------
  /**
   * setFrameColor -- a wheels method
   */
  public void setFrameColor( Color aColor ) 
  {
    _borderColor = aColor;
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
   * setLineWidth -- same as setThickness, but I like the name better
   */
  public void setLineWidth( int w ) 
  {
    setThickness( w );
  }
  
  //----------------------- getXLocation() -------------------------------
  /**
   * getXLocation() - a wheels method
   *                  return int value of x location
   */
  public int getXLocation()
  {
    return (int) _bnds.getX();
  }
  //----------------------- getYLocation() -------------------------------
  /**
   * getYLocation() - a wheels method
   *                  return int value of y location
   */
  public int getYLocation()
  {
    return (int) _bnds.getY();
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
    moveBy( (int)( x - _bnds.getX() ), (int)( y - _bnds.getY() ));
    
  }
  //----------------------- move( int, int ) -------------------------------
  /**
   * moveBy( dx, dy ) -- move the location by delta
   */
  public void moveBy( int aChangeInX, int aChangeInY ) 
  {
    _polygon.translate( aChangeInX, aChangeInY );
    _bnds = _polygon.getBounds();
  }
  //----------------------- display( Graphics2D ) -------------------------
  /**
   * display - calls draw and fill awt methods (this is an rdb method).
   */
  public void display( java.awt.Graphics2D aBetterBrush )
  {
    fill( aBetterBrush );
    draw( aBetterBrush );
  }
  //-------------------------- fill( Graphics2D ) ------------------------
  /**
   * fill - overrides parent method
   */
  public void fill( java.awt.Graphics2D aBetterBrush )
  {
    ////////////////////////////////////////////////////////////
    // 1. Look at the implementaton of fill for AEllipse
    // 2. Look at the java api documentation for Graphics2D
    ////////////////////////////////////////////////////////////
      Color savedColor = aBetterBrush.getColor(); 
      aBetterBrush.setColor( _fillColor );
      aBetterBrush.fillPolygon( _intx , _inty , _intx.length );     
      aBetterBrush.setColor( savedColor );
    
  }
  //--------------------------- draw( Graphics2D ) ------------------------
  /**
   * draw - overrides parent method
   */
  public void draw( java.awt.Graphics2D brush2D ) 
  {
    ////////////////////////////////////////////////////////////
    // 1. Look at the implementaton of draw for AEllipse
    // 2. Look at the java api documentation for Graphics2D
    ////////////////////////////////////////////////////////////
    
      Color savedColor = brush2D.getColor();
      brush2D.setColor( _borderColor );
      java.awt.Stroke savedStroke = brush2D.getStroke();
      
    
      brush2D.setStroke( new java.awt.BasicStroke( _lineWidth ));
      
      brush2D.drawPolygon( _intx , _inty , _intx.length );    
      
      brush2D.setStroke( savedStroke );
      brush2D.setColor( savedColor );
    
  }
}
