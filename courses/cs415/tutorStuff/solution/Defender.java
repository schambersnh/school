/**
 * Defender.java
 * 
 * @author rdb
 * 10/24/09
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Defender extends ShapeGroup
{
   //-------------------------- class variables ------------------------
   public static int shipW = 50;
   public static int shipH = 30;
   private Missile missile;
   public static int loadMissiles   = 20;
   
   //-------------------------- instance variables ---------------------

   private Rectangle body, gun;
   private Color[] colors = {Color.green, Color.orange, Color.red};
   private int cCount = 0;
   
   private int                px, py;      // starting location of the missile
   
   private int       missilesLeft;
   private int     gunW = 10, gunH = 20;
      
   //------------------------ constructor ------------------------------
   public Defender( int x, int y)
   {
    //  _loc = new Point( x, y );
      
    //  missile = new Torpedo( 20, -10, -5);
     // add(missile);
      
      
      body = new Rectangle(0,0);
     
      body.setColor( colors[cCount++] );
      body.setSize(shipW, shipH);
      add(body);
      
      gun = new Rectangle(20, -10);
      gun.setColor( new Color( 50, 50, 50, 200 ) );
      gun.setSize(10, 10);
      add(gun);
      
 
      setLocation( x, y );
   }

   public void setType(int t)
   {
       if(t == 1)
          body.setColor(Color.blue);
       else if(t == 2)
          body.setColor(Color.green);
        else if(t == 3)
          body.setColor(Color.cyan);
       
          
   }
   
      public Point getTop()
   {
      return gun.getLocation();  
   }
   
   
  public void die()
   {
     if( cCount < 3 )
        body.setColor( colors[cCount++] );
     else
        setLocation(-100,-100);
   }
   
   
   //------------------ shoot() ------------------------
   public void shoot(Missile missile)
   {
     
     missile = new Missile( getXLocation() + 20, getYLocation() -30, -5) ;

     
     
   }
   
      public boolean contains( Missile t )
   {

      if(  contains( t.getBottom() )  )
      {
            return true;
      }
      
      return false;
      
   }
   
   
   
   
      //------------------ east() ------------------------
   public void east()
   {
      
     setLocation(getXLocation() + 10, getYLocation() );
     if(  getXLocation()+ getWidth() > 652)
        setLocation(652 - getWidth(), getYLocation() );
   }
   
      //------------------ west() ------------------------
   public void west()
   {
     setLocation(getXLocation() - 10, getYLocation() );
     if( getXLocation() < 0   )
        setLocation( 0 , getYLocation() );
     
   }
   
   
      //------------------ down() ------------------------
   public void down()
   {
      System.out.println("Down");
   }
   
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
//  getX() + shipW < Defenders.panelWidth){
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
      Defender i = new Defender( 250, 450);
   }
   
}