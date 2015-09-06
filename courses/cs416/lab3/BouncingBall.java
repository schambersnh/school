/** 
 * Chapter 7: BouncingBall.java
 * Extends AEllipse, adding the ability to "bounce."
 * 
 * Last modified
 * 01/16/10 rdb: added comments, changed names
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class BouncingBall extends AEllipse implements Animated 
{
   //----------------- instance variables ---------------------
   private int       _moveX, _moveY;  // move steps for each frame
   private Container _parent; // peer object( and container )
   
   //------------------ constructor ---------------------------
   public BouncingBall( Container aPanel, Color aColor )
   {
      super( aColor );
      setMove( 8, 8 );
      _parent   = aPanel;
   }
   
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
   //---------------- moveBy( int, int ) -----------------------------
   public void moveBy( int dx, int dy )
   {
      setLocation( (int) getX() + dx, (int) getY() + dy );
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
      return(int) _parent.getX();
   }
   //------------- minY() ---------------------
   private int minY() 
   {
      return(int) _parent.getY();
   }
   //------------- maxX() ---------------------
   private int maxX() 
   {
      return(int)( _parent.getX() + _parent.getWidth()
                       - this.getWidth() );
   }
   //-------------- maxY() ----------------------
   private int maxY() 
   {
      return(int)( _parent.getY() + _parent.getHeight()
                       - this.getHeight() );
   }
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   //------------------ main -------------------------------
   /**
    * Convenience main for testing the lab code.
    */
   public static void main( String [ ] args ) 
   {
      SwingEventsApp app = new SwingEventsApp( "SwingEventsApp" );
   }
}
