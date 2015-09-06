/*
 * Bullet.java
 * 
 * Michael du Breuil
 * Bullets do not set their own color, it's managed by the brush that gets
 * passed to the paint method, allowing the field and quadtree to manage the
 * color. It also saves on calling the set color method every time, and makes
 * it easier to diagnose if the quadtree is working correctly
 * 
 * Summer 2010
 * 
 * Modified by rdb
 * April 2011
 * 
 * Edited by JB
 * Summer 2011
 */

import java.awt.*;
import java.awt.geom.*;

public class Bullet extends Ellipse2D.Float
{
   //------------------ class variables  ------------------------------
   private static int     size = 3;
   
   //------------------ instance variables  ------------------------------
   private float dx, dy;
   private Game game;
   private double vectorScale;
   private int displayScale;
   private Point2D.Float position;
   public boolean expired = false;
   
   
   //------------------------- constructor -------------------------------
   public Bullet( Game parent, Point2D.Float pos, float dx, float dy ) 
   {
      super( pos.x, pos.y, size, size );
      position = pos;
      this.dx = dx;
      this.dy = dy;
   }
   
   public void move() 
   {
      if ( !expired ) 
      {
  setPosition( this.x + dx, this.y + dy );
      }
   }
   
   public void restart() 
   {
      expired = false;
   }
   
   public Point2D.Float getPosition() 
   {
      return position;
   }

   public void setDeltas( float dx, float dy ){
       this.dx = dx;
       this.dy = dy;
   }
   
   public void setPosition( Point2D.Float pos ) 
   {
      this.setPosition( pos.x, pos.y );
   }
   
   public void setPosition( float x, float y ) 
   {
      this.position.x = this.x = x;
      this.position.y = this.y = y;
      setFrame( this.x, this.y, size, size );
   }
   
   public void draw( Graphics g, Color color ) 
   {
      Color bak = g.getColor();
      Graphics2D g2 = (Graphics2D) g;
      g.setColor(color);
      int ix = (int)x;
      int iy = (int)y;
      g2.draw( this );
      g.setColor(bak);
   }
}
