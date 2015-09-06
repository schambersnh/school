/**
 * QNode - a node in a quadtree
 * 
 * Edited by JB
 * Summer 2011
 */
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

public class QNode 
{
   // --------------- instance variables --------------------------
   private static final Color[] colors = { Color.WHITE, Color.GREEN,
      Color.BLUE, Color.LIGHT_GRAY, Color.CYAN, Color.MAGENTA,
      Color.ORANGE, Color.PINK, Color.YELLOW};
   
   private     Rectangle      bounds;
   private     Rectangle      container; 
   public      QNode[]        kids;
   public      ArrayList<Bullet> bullets;
   public      Color           color;
   static private final int    usedColors = colors.length;
   static private       int    nextColor = 0;
 
   public static int leafCount = 0;
   public static int[] leafLevelDistrib = new int[ 20 ];
   private ArrayList<Target> targets;
   private Game game;
   // ---------------- constructors --------------------------------
   private static int counter = 0;
   public QNode( Rectangle rect, Game g ) 
   {
      bullets = new ArrayList<Bullet>();
      targets = new ArrayList<Target>();
      this.bounds = rect;
      container = new Rectangle( bounds.x, bounds.y, 
                                   bounds.width + 1, bounds.height + 1 );
      kids = null;
      color = colors[ nextColor % usedColors ];
      game = g;
   }
   //---------------- accessors -----------------------------------
   public double getX() 
   {
      return bounds.getX();
   }
   public double getY() 
   {
      return bounds.getY();
   }
   public double getWidth() 
   {
      return bounds.getWidth();
   }
   public double getHeight() 
   {
      return bounds.getHeight();
   }
   // -------------- treeBulletCount() -----------------------------------
   public int treeBulletCount( int depth ) 
   {
      int size = bullets.size();
      if ( kids != null )
      {
         depth++;
         for ( int c = 0; c < kids.length; c++ )
            size += kids[ c ].treeBulletCount( depth );
      }
      else
      {
         leafCount++;
         if ( depth < leafLevelDistrib.length )
            leafLevelDistrib[ depth ]++;
      }
      return size;
   }
   
   // -------------- toString() -----------------------------------
   @Override
   public String toString() 
   {
      return bullets.toString();
   }
   //----------------- contains( Bullet ) -----------------------
   public static int edgeCount = 0;
   public boolean contains( Bullet bullet ) 
   {
      if ( container.contains( bullet.getPosition().x,
                           bullet.getPosition().y ) )
         return true;
      //++++ Let's check the Rectangle2D contains
      Rectangle2D r = new Rectangle2D.Float( bounds.x, bounds.y, 
                        bounds.width + 1.000001f, bounds.height + 1.000001f );
      if ( r.contains( bullet.getPosition() ))
      {
         //System.out.println( "Contains edge: " + bullet );
         edgeCount++;
         return true;
      }
      return false;
      
   }
   //------------------- getRectangle() -----------------------------------
   public Rectangle getRectangle()
   {
      return bounds;
   }

   public void addTargets( ArrayList<Target> ts ){
      targets.addAll( ts );
   }
   
   public void removeTarget( Target t ){
      targets.remove(t);
   }
   
    public void calcCollisions(){
      Iterator<Bullet> iter = bullets.iterator();
      
      while ( iter.hasNext() ){
         Bullet p = iter.next();
         Iterator<Target> iterT = targets.iterator();
         while ( iterT.hasNext() ){
            Target t = iterT.next();
            if ( t.contains( p.x, p.y ) ){
               boolean result = t.hit();
        game.deadBullets.add( p );
               iter.remove();
               if ( !result ){
                  iterT.remove();
                  game.removeTarget(t);
                  game.rebuildTreeWhenDone();
               }
            }
         }
      }
   }
   
   // -------------- resetColors() -----------------------------------
   public static void resetColors(){
       nextColor = 0;
   }
   
   // -------------- incrementColor() -----------------------------------
   public static void incrementColor(){
       nextColor++;
   }
   
   // -------------- decrementColor() -----------------------------------
   public static void decrementColor(){
       nextColor--;
   }
}
