/*
 * Particle.java
 * 
 * Particles should be created at start time (or whenever the number of
 *   particles changes) and reused throughout the animation. Creating new
 *   copies every time one dies, is lots of overhead.
 * 
 * Particles do not set their own color, it's managed by the brush that gets
 * passed to the paint method, allowing the field and quadtree to manage the
 * color. It also saves on calling the set color method every time, and makes
 * it easier to diagnose if the quadtree is working correctly
 * 
 * Michael du Breuil
 * Summer 2010
 * 
 * Modified by rdb
 * April 2011
 */

import java.awt.*;
import java.awt.geom.*;

public class Particle extends Point2D.Float
{
   //------------------ class variables  ------------------------------
   public int age;
   public boolean isAlive;
   //------------------ instance variables  ------------------------------
   public Vector2D velocity;
   public Point2D.Float position;
   public int lifetime;
   public VectorField field;
   
   //------------------------- constructor -------------------------------
   public Particle( VectorField parent, Point2D.Float pos, int life ) 
   {
      super( pos.x, pos.y );
      field = parent;
      position = pos;
      lifetime = life;
   }
   //--------------------------- restart ---------------------------------
   /**
    * Tell the particle it has been restarted, its age becomes 0 and it 
    * is alive again.
    */
   public void restart() 
   {
     this.age = 0;
     this.isAlive = true;
   }
   public void display()
   {

   }
   
   //+++++++++++++++++++++ accessors/mutators +++++++++++++++++++++++++++++++
   // these don't need separate method comments
   //
   //----------------- setVelocity( Vector 2D ) -----------------------------
   public void setVelocity( Vector2D newVelocity ) 
   {
     this.velocity = newVelocity;
   }
         
   //----------------- setPosition( Point2D.fLoat ) -----------------------
   public void setPosition( Point2D.Float pos ) 
   {
     this.position = pos;
   }
   
   //----------------- setLifetime( int ) -----------------------------
   public void setLifetime( int life ) 
   {
     this.lifetime = life;
   }
   //------------------getVelocity( )---------------------------------------
     public Vector2D getVelocity( ) 
   {
     return this.velocity;
   }
         
   //----------------- getPosition( ) -----------------------
   public Point2D.Float getPosition(  ) 
   {
     return this.position;
   }
   
   //----------------- getLifetime( ) -----------------------------
   public int getLifetime( ) 
   {
     return this.lifetime;
   }
}
