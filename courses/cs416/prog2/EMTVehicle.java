/* 
 * EMTVehicle.java:
 *   
 * A very simple representation of an emt vehicle going to emergency
 * sites and hospitals.
 * 
 * This class is responsible for:
 *   1. remembering its "home" position; the one specified at creation time.
 *   2. having a public interface method that allows the dispatcher
 *      to tell it where to go next.
 *   3. being able to move itself by steps to that "goal" position
 *      at some specified speed. 
 *   4. given a goal position and a speed,   
 *       -computing the number of frames it will take to get there
 *       -computing the floating point incremental x and y
 *        changes needed for all but the last step
 *       -maintaining a floating point representation for the
 *        "precisely correct" position for each step.
 *       -converting the "correct" position to the closest
 *        pixel (int) location at each step; this means using
 *        (int)Math.round( floatX or floatY ), not just casting
 *        the float values to int.
 *   5. Stopping when it gets to the goal position and having some
 *      way that the dispatcher can find out that it got to its
 *      goal location.
 *   6. Having a public interface method that allows the dispatcher
 *      to get its home position or to tell it to go to its home
 *      position.
 * 
 * 
 * @author rdb
 * 02/02/11  Skeleton class created
 * 
 * Modified by Stephen Chambers on 2/10/2011
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class EMTVehicle extends JComponent implements Animated
{ 
   //---------------------- instance variables ------------------------------
   private int     _goalX;     // if moving, this is target location
   private int     _goalY;
   private int     _speed;     // current speed
   private double  _locX;
   private double  _curY;
   private double  _curX;
   private double  _locY;
   private double _distance;
   private double  _stepX;
   private double  _stepY;
   private int     _nSteps; 
   private int     _homeX;
   private int     _homeY;
   private boolean _active = true;
   // EMTVehicle display variables
   private JRectangle body;
   private int bodyW = 40, bodyH = 20;
   private int bodyX = 0;
   private int bodyY = 0;
 
   private int wheelY = 15;
   private int wheelDiam = 10;
   private JEllipse wheel1;
   private int wheel1X = 5;

   private JEllipse wheel2;
   private int wheel2X = 25;
   private int wheel2Y = 20;
  
   //------------------------- constructor -----------------------------------
   public EMTVehicle ( int x, int y, Color bodyColor ) 
   {
      /////////////////////////////////////////////////////////
      // Save the location as "home"
      // Create 2 or more J-objects to represent the vehicle
      // Add the J-objects using the add method
      // set the location
      ///////////////////////////////////////////////////////////
     _homeX = x;
     _homeY = y;
     
      body = new JRectangle(bodyX, bodyY);
      body.setSize( bodyW, bodyH );
      this.add(body);
      
      wheel1 = new JEllipse(wheel1X, wheel2Y);
      wheel1.setSize(wheelDiam, wheelDiam);
      wheel1.setColor(Color.BLACK);
      this.add(wheel1);
      
      wheel2 = new JEllipse(wheel2X, wheel2Y);
      wheel2.setSize(wheelDiam, wheelDiam);
      wheel2.setColor(Color.BLACK);
      this.add(wheel2);
      
      

      setLocation( x, y );
   }
   //------------------------- constructor -----------------------------------
   public EMTVehicle ( Point loc, Color col ) 
   {  
      this( loc.x, loc.y, col );
   }
   //---------------- add( JComponent ) ---------------------------
   /**
    * override add method to compute and set bounds information as 
    * components are added to the object.
    */
   private Rectangle  _bounds = null;    // instance variable
   
   public void add( JComponent comp )
   {
      super.add( comp );
      if ( _bounds == null )
         _bounds = new Rectangle( comp.getBounds() );
      else
         _bounds = _bounds.union( comp.getBounds() );
      super.setBounds( _bounds ); // update location/size     
   }
   //------------------ travelTo( int, int, int ) --------------------------
   /**
    * travel to the specified location at the specified speed
    */
   public void travelTo( int x, int y, int speed )
   {
     //turn Animation "on"
     setAnimated(true);
     
     //Get the current x and y coordinates and typecast them to type double
     _curX = (double)getX();
     _curY = (double)getY();
     
     //save the x and y coordinates passed to method as the "goal coordinate"
     //instance variables
     _goalX = x;
     _goalY = y;
     
     //Typecast the location passed to method into type double
     _locX = (double)x;
     _locY = (double)y;
     
     //calculate change in x and y and square them
     double deltax = Math.pow((_locX - _curX), 2);
     double deltay = Math.pow((_locY - _curY), 2);
     
     //Determine distance using distance formula
     _distance = Math.sqrt(deltax + deltay);
     
     //determine number of steps user must take
     _nSteps = (int)_distance / speed;
     
     //determine the XStep and the YStep 
     _stepX = (x - _curX) / _nSteps;
     _stepY = (y - _curY) / _nSteps;
   }
   //------------------------getHome()----------------------------------
   /*This method simply returns a Point containing the two instance variables
    * representing the "home" of the vehicle"
    * */
   public Point getHome()
   {
     Point temp = new Point();
     temp.x = _homeX;
     temp.y = _homeY;
     return temp;
   }
  
   //++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++
   private boolean _animated = false;
   //---------------------- isAnimated() ----------------------------------
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
    * update the position of the vehicle; check if it has reached the
    * goal position -- if it has, turn off animation.
    */
   public void newFrame() 
   {
     int curX = getX();
     int curY = getY();
      if ( curX == _goalX && curY == _goalY ) //At the Goal
      {
        setAnimated(false);
         return;
      }
      //Stil have steps to take
      else if ((_goalX - _stepX > _stepX) && (_goalY - _stepY > _stepY))
      {
        double x = curX + _stepX;
        double y = curY + _stepY;
        
        setLocation((int)x, (int)y);
      }
      else //One more step
      {
        setLocation(_goalX, _goalY);
      }
   }
   //+++++++++++++++ end Animated interface +++++++++++++++++++++++++++++

   //--------------------- main -----------------------------------
   /**
    * unit test
    */
   public static void main( String[] args )
   {     
      JFrame testFrame = new JFrame();
      testFrame.setSize( 700, 500 );  // define window size
      
      testFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      JPanel testPanel = new JPanel( (LayoutManager) null );
      testFrame.add( testPanel );
      
      EMTVehicle d1 = new EMTVehicle( 200, 200, Color.GREEN );
      testPanel.add( d1 );
      System.out.println( d1.getLocation() );
      
      EMTVehicle d2 = new EMTVehicle( new Point( 100, 200 ), Color.BLUE );
      testPanel.add( d2 );
      
      testFrame.setVisible( true ); 
   }
}
