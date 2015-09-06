/**
 * JPlayer.java-- a composite J object that is comprised of parts that
 *      are J-objects. Note that this implementation of a composite
 *      graphics object is different from what you probably did for
 *      the previous lab -- this one extends JComponent which changes how 
 *      several things need to be done. In particular, this object must
 *      compute its size from its components and invoke setSize
 *      explicitly. 
 * 
 * @author rdb
 * Last edited: January 2010
 * 
 * modified by Stephen Chambers for Lab3 on 2/3/2011
 */

import java.awt.geom.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class JPlayer extends JComponent implements Animated, MouseListener,
  MouseMotionListener
{
  //---------------- instance variables ------------------------
  private Rectangle _bounds;   // composite bounds for all components
  private int _moveX;
  private int _moveY;
  
  //--------------------  constructors ---------------------------
  /**
   * Constructor for JPlayer
   */
  public JPlayer( Color aColor, int x, int y )
  {       
    int    headX = 5,  headY = 0,  headW = 20, headH = 20;
    int    bodyX = 0,  bodyY = 20, bodyW = 30, bodyH = 50;
    
    // The parts are defined relative to 0,0
    JEllipse head = new JEllipse( headX, headY );
    head.setSize( headW, headH );
    head.setColor( aColor );
    this.add( head );
    
    JRectangle body = new JRectangle( bodyX, bodyY );
    body.setSize( bodyW, bodyH );
    body.setColor( aColor.darker().darker() );
    this.add( body );
    
    setLocation( x, y );
    
    addMouseListener( this );
    addMouseMotionListener( this );
  }
  //---------------- add( JComponent ) ---------------------------
  /**
   * override add method to compute and set bounds information as 
   * components are added to the object.
   */
  public void add( JComponent comp )
  {
    super.add( comp );
    if ( _bounds == null )
      _bounds = new Rectangle( comp.getBounds() );
    else
      _bounds = _bounds.union( comp.getBounds() );
    super.setBounds( _bounds ); // update location/size     
  }
  //---------------- moveBy( int, int ) -----------------------------
  public void moveBy( int dx, int dy )
  {
    setLocation( (int)getX() + dx, (int)getY() + dy );
  }
  //+++++++++++++
  //+++++++++++++++ methods used for animation +++++++++++++++++++++
  //---------------- setMove( int, int ) ---------------------------
  /**
   * set the incremental steps taken by each move.
   */
  public void setMove( int dx, int dy )
  {
    _moveX = dx;
    _moveY = dy;
  }
  
  //++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++
  private boolean _animated = true; // instance variable used in interface
  //---------------------- newFrame() ----------------------------------
  public boolean isAnimated()
  {
    return _animated;
  }
  //---------------------- setAnimated( boolean ) --------------------
  public void setAnimated( boolean onOff )
  {
    _animated = onOff;
  }
  //---------------------- newFrame() -------------------------------
  /**
   * invoked for each frame of animation; 
   * update the position of the ball, then check whether it has hit
   *   any of the 4 boundaries of the display panel; if it does,
   *   bounce it off the boundary, by revising the move parameters,
   *   but also force the ball to be entirely on screen; this last
   *   bit is necessary primarily when a ball is started far enough
   *   off screen that the next frame increment isn't enough to
   *   get its position on screen, so code will reverse step again,
   *   putting it further off, etc. 
   */
  public void newFrame() 
  {
    int nextX = (int)getX() + _moveX;
    int nextY = (int)getY() + _moveY;
    if ( nextX <= minX() ) 
    {
      _moveX = - _moveX;
      nextX = minX();   // force entire object on screen 
    }
    else if ( nextX >= maxX() ) 
    {
      _moveX = - _moveX;
      nextX = maxX();
    }
    
    if ( nextY <= minY() ) 
    {
      _moveY = - _moveY;
      nextY = minY();
    }
    else if ( nextY >= maxY() ) 
    {
      _moveY = - _moveY;
      nextY =  maxY();
    }
    setLocation( nextX, nextY );
  }
  //+++++++++++++++ end Animated interface +++++++++++++++++++++++++++++++++++++
  //+++++++++++++++ methods to get boundaries of parent container ++++++++++++++
  //------------- minX() ---------------------
  private int minX() 
  {
    return(int) getParent().getX();
  }
  //------------- minY() ---------------------
  private int minY() 
  {
    return(int) getParent().getY();
  }
  //------------- maxX() ---------------------
  private int maxX() 
  {
    return(int)( getParent().getX() + getParent().getWidth()
                  - this.getWidth() );
  }
  //-------------- maxY() ----------------------
  private int maxY() 
  {
    return(int)( getParent().getY() + getParent().getHeight()
                  - this.getHeight() );
  }
  //++++++++++++++++++++ mouse methods / instance variables ++++++++++++++++
  private Point _saveMouse;   // instance variable for last mouse position
  //   used for dragging
  
  //+++++++++++++++++++++++++ mouseListener methods ++++++++++++++++++++++++
  public void mousePressed( MouseEvent me )
  {
    ////////////////////////////////////////////////////////////////////
    // me.getPoint(), which we've used before is the location of the 
    // mouse "inside" the JComponent; this won't work.
    //
    // We need the position of the mouse in the container that holds the
    // JComponent: getParent().getMousePosition()
    //
    // Assign it to the instance variable, _saveMouse
    ////////////////////////////////////////////////////////////////////
    _saveMouse = getParent().getMousePosition();
    
  }
  public void mouseClicked( MouseEvent me ){}
  public void mouseReleased( MouseEvent me ){}
  public void mouseEntered( MouseEvent me ){}
  public void mouseExited( MouseEvent me ){}
  
  //+++++++++++++++++++ mouseMotionListener methods ++++++++++++++++++++++++
  public void mouseDragged( MouseEvent me )
  {
    //////////////////////////////////////////////////////////////////////
    //  Get new position of mouse:
    //         getParent().getMousePosition()
    //  For each of x and y coordinates, compute
    //     dX = newX - oldX (stored in _saveMouse.x)
    //     dY = newY - oldY (stored in _saveMouse.y)
    //  invoke moveBy( dX, dY ) 
    //  Save new position in _saveMouse
    //
    // WARNING: If the mouse is moved out of the frame, the position is
    //          returned as a null reference. You need to check that
    //          before using wither the saved position of the new position.
    //          If either is off screen, don't do anything!!!
    //////////////////////////////////////////////////////////////////////
    Point temp = getParent().getMousePosition();
    
    if (temp != null)
    {
      int newX = temp.x;
      int newY = temp.y;
      int diffX = newX - _saveMouse.x;
      int diffY = newY - _saveMouse.y;
      moveBy( diffX, diffY );
      _saveMouse = temp;
    }
    else
    {
      return;
    }
    
    
    
  }
  public void mouseMoved( MouseEvent me ){}
  //+++++++++++++++++ end MouseMotionListeners ++++++++++++++++++++++++++++
  
  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  //------------------ main -------------------------------
  /**
   * Convenience main for testing the lab code.
   */
  public static void main( String [ ] args ) 
  {
    SwingEventsApp app = new SwingEventsApp( "SwingEventsApp" );
  }
}
