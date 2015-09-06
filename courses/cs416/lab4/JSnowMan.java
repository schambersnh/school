/** 
 * JSnowMan.java:
 * 
 * Displays a simple snow man using multiple Wheels Shapes.
 * The entire snowman is built in the constructor.
 * 
 * @author rdb
 * Created 9/11/07; derived from cs415 demo program, Start.java 
 * 01/28/10 rdb: revised to put JComponents into JSnowMan.
 * 01/31/10 rdb: revised to use Animated interface, rather than Movable
 * 
 * modified by Stephen Chambers for lab 5 on 2/8/2011
 */
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class JSnowMan extends JComponent 
                     implements Animated, MouseListener, MouseMotionListener
{
   //---------------- instance variables ------------------------------
   private float   _dX;           // steps sizes for each move
   private float   _dY;
   private Point _lastMouse = null;
             
   private Vector<JShape>  _components; // components of this composite
     
   // -----------------------------------------------------------------
   /** 
    * Constructor for the JSnowMan class. 
    *    Container argument is needed by this Composite class: it defines
    *        the Container in which the snow man is allowed to move
    *    x,y are the position of the JSnowMan.
    */
   public JSnowMan( int x, int y )
   {
      _components = new Vector<JShape>();
      
      buildHat( );   // create the hat and its brim  
      buildBody( );   // create the body
      buildArms( );   // create some arms
      buildFace( );   // create the face      
      buildHead( );   // create the head
     
      //super.setSize( _xMax, _yMax );
      setLocation( x, y );
                 
      this.addMouseListener( this );
      this.addMouseMotionListener( this );   
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
   //++++++++++++ mouseListener/mouseMotionListeer methods +++++++++++++++++++
   public void mousePressed( MouseEvent me )
   { 
      _lastMouse = getParent().getMousePosition();
      //System.out.println( "JSnowMan pressed at " + _lastMouse ); 
   }   
   public void mouseDragged( MouseEvent me )
   { 
      Point curMouse;
      curMouse = getParent().getMousePosition();
      
      if ( _lastMouse != null && curMouse != null
             && !curMouse.equals( _lastMouse )) 
      {
         //System.out.println( "JSnowMan dragged to " + curMouse ); 
         setLocation( this.getX() + curMouse.x - _lastMouse.x, 
                     this.getY() + curMouse.y - _lastMouse.y );
         _lastMouse = curMouse;
         getParent().repaint();
      }
   }   
   public void mouseClicked( MouseEvent me ) 
   { 
      //System.out.println( "JSnowMan mouse clicked" ); 
   }   
   public void mouseEntered( MouseEvent me ) 
   { 
      //System.out.println( "JSnowMan mouse entered" ); 
   }   
   public void mouseExited( MouseEvent me ) 
   { 
      //System.out.println( "JSnowMan mouse exited" ); 
   }   
   public void mouseMoved( MouseEvent me ) 
   { 
      //System.out.println( "JSnowMan mouse moved" ); 
   }   
   public void mouseReleased( MouseEvent me )
   { 
      //System.out.println( "JSnowMan mouse released" ); 
   }   
   
   //++++++++++++++++++ component building methods +++++++++++++++++++
   //----------------------- buildFace ------------------------------
   private void buildFace()
   {
      int[]  smileX     = { 40, 45, 55, 60 };
      int[]  smileY     = { 43, 45, 45, 43 };
      
      int    leftEyeX  = 40,  leftEyeY  = 30;
      int    rightEyeX = 55,  rightEyeY = 30;
      int    eyeSize   = 4;
      
       // create the two eyes
      JEllipse leftEye = new JEllipse( leftEyeX, leftEyeY );
      leftEye.setColor( Color.BLACK );
      leftEye.setSize( eyeSize, eyeSize );
      _components.add( leftEye );
      this.add( leftEye );
      
      JEllipse rightEye = new JEllipse( rightEyeX, rightEyeY );
      rightEye.setColor( Color.BLACK );
      rightEye.setSize( eyeSize, eyeSize );
      _components.add( rightEye );
      this.add( rightEye );
      
      // create a smile
      JLine[] smile = new JLine[ smileX.length - 1 ];
      for ( int i = 0; i < smileX.length - 1; i++ )
      {
         smile[ i ] = new JLine(Color.BLACK );
         smile[ i ].setPoints(  smileX[ i ], smileY[ i ], 
                              smileX[ i + 1 ], smileY[ i + 1 ] );
         smile[ i ].setLineWidth( 2 );
         _components.add( smile[ i ] );
         this.add( smile[ i ] );
      }
   }
   //----------------------- buildHat ------------------------------
   private void buildHat()
   {
      int    brimX  = 30,  brimY  = 15;
      int    brimW  = 40,  brimH = 4;
      int    hatX   = 38,  hatY   = 0;
      int    hatW   = 25,  hatH   = 13;
      
      JRectangle hatBody = new JRectangle( hatX, hatY );
      hatBody.setSize( hatW, hatH );
      hatBody.setColor( Color.BLACK );
      _components.add( hatBody );
      this.add( hatBody );
      
      JRectangle hatBrim = new JRectangle( brimX, brimY );
      hatBrim.setSize( brimW, brimH );
      hatBrim.setColor( Color.BLACK );
      _components.add( hatBrim );
      this.add( hatBrim );
   }
   //----------------------- buildBody ------------------------------
   private void buildBody()
   {
      int    bodyX     = 10,  bodyY     = 55;
      int    bodySize  = 80; 
      
      JEllipse body = new JEllipse( bodyX, bodyY );
      body.setSize( bodySize, bodySize );
      body.setFillColor( Color.WHITE );
      body.setFrameColor( Color.RED );
      body.setLineWidth( 2 );
      _components.add( body );
      this.add( body );
   }
   //----------------------- buildArms ------------------------------
   private void buildArms()
   {
      int    lArmX1 = 0, lArmY1 = 35, lArmX2 = 15, lArmY2 = 75;
      int    rArmX1 = 85, rArmY1 = 75, rArmX2 = 120, rArmY2 = 55;
      
      JLine leftArm = new JLine( Color.BLACK );
      leftArm.setPoints( lArmX1, lArmY1, 
                        lArmX2, lArmY2 );
      leftArm.setLineWidth( 3 );
      _components.add( leftArm );
      this.add( leftArm );
      
      JLine rightArm = new JLine( Color.BLACK  );
      rightArm.setPoints( rArmX1, rArmY1, 
                         rArmX2, rArmY2 );
      rightArm.setLineWidth( 3 );
      _components.add( rightArm );
      this.add( rightArm );
   }
   //----------------------- buildHead ------------------------------
   private void buildHead()
   {
      int    headX     = 25,  headY     = 15;
      int    headSize  = 50;
      
      JEllipse head = new JEllipse( headX, headY );
      head.setSize( headSize, headSize );
      head.setFillColor( java.awt.Color.WHITE );
      _components.add( head );
      this.add( head );
   } 
   
   //------------------------ setLocation( Point ) ----------------------
   /**
    * display the snowman at the specified Point
    */
   public void setLocation(Point p) 
   {
      setLocation( p.x, p.y );
   }
   
   //+++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++++++++
   //++++++++++++++++++++++++ Animated interface methods +++++++++++++++++++++
   private boolean _animated = true;  // instance variable
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
   //---------------------- newFrame() -----------------------------------------
   /**
    * move the JSnowMan by the specified incremental steps.
    */
   public void newFrame() 
   {
      // get the expected next location
      float nextX = this.getX() + _dX;
      float nextY = this.getY() + _dY;
      float maxX  = nextX + this.getWidth();
      float maxY  = nextY + this.getHeight();
      
      // but check if we have hit a boundary of the panel we're in
      // first check x bounds: left and right
      // Since the JSnowMan is quite large, this is the "safer" version, that 
      //    prevents any part of the JSnowMan from going outside the panel.
      if ( nextX < 0 )                      // outside on the left?
      {
         _dX = - _dX;                       // if so, reverse x increment
         nextX = 0; 
      }
      else if ( maxX > getParent().getWidth()  ) // outside on the right?
      {                                     
         _dX = - _dX;                       // if so, reverse x increment
         nextX = getParent().getWidth() - this.getWidth();
      }
      if ( nextY < 0 )                      // outside on the top?
      {
         _dY = - _dY;                       // if so, reverse x increment
         nextY = 0; 
      }
      else if ( maxY > getParent().getHeight()  ) // outside on the bottom?
      {                                     
         _dY = - _dY;                       // if so, reverse x increment
         nextY = getParent().getHeight() - this.getHeight();
      }
      this.setLocation( (int)nextX, (int)nextY );       // update location
      this.repaint();
   }
   //----------------------- setMove( int, int ) ------------------------
   /**
    * set the move increments
    */
   public void setMove( float dx, float dy )
   {
      _dX = dx;
      _dY = dy;
   }
   //---------------------- int getMoveX() --------------------------
   /**
    * return the x move increment
    */
   public float getMoveX()
   {
      return _dX;
   }
   //---------------------- int getMoveY() --------------------------
   /**
    * return the y move increment
    */
   public float getMoveY()
   {
      return _dY;
   }
 }
