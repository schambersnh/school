/**
 * Missile.java
 * 
 * 
 * 
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Missile extends ShapeGroup
{
   //-------------------------- class variables ------------------------

   //-------------------------- instance variables ---------------------
   private RoundedRectangle body;
   private Line l1,l2,l3,ll1,ll2,ll3;
   private int dy;
   private int z = 0;
 
   //------------------------ constructor ------------------------------
   public Missile( int x, int y, int diffy)
   {

     l1 = new Line( 0, 0, 4, 9 );
     l1.setColor(Color.red);
     l1.setThickness(2);
     add(l1);
     
       l2 = new Line( 4, 9, 0, 18 );
     l2.setColor(Color.red);
     l2.setThickness(2);
     add(l2);
     
     
     l3 = new Line( 0,18, 4, 27 );
     l3.setColor(Color.red);
     l3.setThickness(2);
     add(l3);
 
          ll1 = new Line( 4, 0, 0, 9 );
     ll1.setColor(Color.red);
     ll1.setThickness(2);
     add(ll1);
     
       ll2 = new Line( 0, 9, 4, 18 );
     ll2.setColor(Color.red);
     ll2.setThickness(2);
     add(ll2);
     
     
     ll3 = new Line( 4,18, 0, 27 );
     ll3.setColor(Color.red);
     ll3.setThickness(2);
     add(ll3);
     
     setLocation(x,y);
     dy = diffy;
     
     zag();
     
   }
   
   public Point getTop()
   {
      return getLocation();  
   }
   
      public Point getBottom()
   {
         Point p = getLocation();
      return new Point( p.x,p.y+ 27); 
      
   }
   

      
      
   public boolean isActive( )
   {
       return getYLocation() > -90;
   }
   
   
      
      
      
   private void zig()
   {
     l1.hide();
     l2.hide();
     l3.hide();
     ll1.show();
     ll2.show();
     ll3.show();
   }
   
      
   private void zag()
   {
     ll1.hide();
     ll2.hide();
     ll3.hide();
     l1.show();
     l2.show();
     l3.show();
   }
   
   public int step()
   {
      setLocation( getXLocation(), getYLocation() + dy );
      
    if( z++ %3 == 0)
       zag();
    else
       zig();

    
    
      return 0;
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
//  getX() + shipW < Missiles.panelWidth){
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
      Missile i = new Missile( 20, 30, 5);
   }
   
}