/**
 * Invader.java
 * 
 * @author rdb
 * 10/24/09
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Invader extends ShapeGroup
{
   //-------------------------- class variables ------------------------
   public static int shipW = 36;
   public static int shipH = 24;
   
   public static int loadMissiles   = 20;
   
   //-------------------------- instance variables ---------------------
   private Point  _loc;
   private Point  _end;
   private double _dx = 5;
   private double _dy = 0;
   private boolean active = true;
   private Rectangle body;

   
   private int                px, py;      // starting location of the missile
   
   private int       missilesLeft;
   private int     gunW = 10, gunH = 20;
      
   //------------------------ constructor ------------------------------
   public Invader( int x, int y)
   {
      _loc = new Point( x, y );
      body = new Rectangle();
     // body.setLocation(0, gunH);
      body.setColor( new Color( 50, 50, 50, 200 ) );
      body.setSize(shipW, shipH);
      add(body);
     // gun = new JRectangle();
     // gun.setLocation((shipW/2) - (gunW/2), 0);
     // gun.setColor( new Color( 50, 50, 50, 200 ) );
     // gun.setSize(gunW, gunH);
    //  add(gun);
     // _end = new Point(x + (shipW/2) - (gunW/2), y);
      setLocation( x, y );
   }
   
   
   public boolean contains( Defender t )
   {

      if( active &&  body.contains( t.getTop() )  )
      {
            return true;
      }
      
      return false;
      
   }
   
   
   
   public boolean contains( Missile t )
   {

      if( active &&  body.contains( t.getTop() )  )
      {
            body.hide();
            active = false;
            return true;
      }
      
      return false;
      
   }
   
    public boolean isActive( )
   {
   return active;
   }
   
   
   
//   //------------------- getEnd() ---------------------------------------
//   /**
//    * return the Point that is the end of the gun
//    */
//   public Point getEnd()
//   {
//      return _end;
//   }
//   //-------------------- setLocation( int, int ) -------------------------
//   /**
//    * override JLine method; since JLine setLocation( Point ) calls this, we
//    * don't need to override that one.
//    */
//   public void setLocation( int x, int y )
//   {
//      _loc.x = x;
//      _loc.y = y;
//      updatePoints();
//      super.setLocation(x, y);
//   }
   public void setType(int t)
   {
       if(t == 1)
          body.setColor(Color.blue);
       else if(t == 2)
          body.setColor(Color.green);
        else if(t == 3)
          body.setColor(Color.cyan);
       
          
   }
//   //------------------ getMissilesLeft() ------------------------
//   public int getMissilesLeft()
//   {
//      return missilesLeft;
//   }
//   //---------------- add( JComponent ) ---------------------------
//   /**
//    * override add method to compute and set bounds information as 
//    * components are added to the object.
//    */
//   private java.awt.Rectangle  _bounds = null;    // instance variable
//   
//   public void add( JComponent comp )
//   {
//      super.add( comp );
//      if ( _bounds == null )
//         _bounds = new java.awt.Rectangle( comp.getBounds() );
//      else
//         _bounds = _bounds.union( comp.getBounds() );
//      super.setBounds( _bounds ); // update location/size     
//   }
//
//   public void keyPressed(KeyEvent e){
//       if (e.getKeyCode() == KeyEvent.VK_LEFT && getX() > 0){
//    _loc.x -= _dx;
//    updatePoints();
//       }
//       else if (e.getKeyCode() == KeyEvent.VK_RIGHT && 
//  getX() + shipW < Invaders.panelWidth){
//    _loc.x += _dx;
//    updatePoints();
//       }
//   }
//   public void keyReleased(KeyEvent e){
//   }
//   public void keyTyped(KeyEvent e){
//   }
   
      //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      Frame f = new Frame();
      Invader i = new Invader( 20, 30);
   }
   
}