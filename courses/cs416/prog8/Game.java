/**
 * Game.java
 * 
 * Michael DuBreuil
 * Summer 2010
 * 
 * Modified by rdb Spring 2011
 * 
 * Edited by JB
 * Summer 2011
 */
import java.io.FileNotFoundException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel
{
   //----------------- class variables ------------------------------------
   //---- package variables defining metadata information about files
   //     and initial parameter settings; set by FlowQuad
   static int     width = 512;     // size of vector field in x direction
   static int     height = 512;    // size of vector field in y direction
   static int     frameDelay = 60;   // millisecs between frames
   static int     bulletFrameFire = 10; //the modulus for the the 
                                        //frame number for the bullet to fire
   static float bdx = 1; //bullet speed in the x direction
   static float bdy = 1; //for the y direction
   static float bulletAngle = 0;
   static int maxBullets = 50;
   static int numBullets = 0;
   
   public static float bulletSpeed = 10;

   
   //----------------- instance variables ---------------------------------
   
   private int  maxDepth = 4;
   private int  minSize  = 4;   // don't subdivide anything smaller than 4x4
   private int  minTargets = 5;
   
   public boolean showTree = true; // show the quadtree nodes
   
   public  QNode _root;                // root node of the quad tree
   public  LinkedList<Bullet> deadBullets;
   public  ArrayList<Bullet> interNodeBullets;
   
   private ArrayList<Target> targets;
   private Timer frameTimer;
   private int curFrame = 0;
   private boolean rebuildTree;

   //--------------------------- constructor --------------------------->
    public Game( String targetsFile, int maxHits, int maxBullets ) 
   {
      super();
      this.setSize( new Dimension( width , height ));
      this.setBackground(Color.black);
      
      this.maxBullets = maxBullets;
      Target.maxHits = maxHits;
      setAngle( 0 );
      
      createTargets(targetsFile);

      buildTree();
      createFrameTimer();
   }   

   private void createTargets(String targetsFile) {
      Scanner file = null;
      try {
         file = new Scanner( new File (targetsFile));
      } catch (FileNotFoundException ex) {
         System.err.println( "Targets File not found: " + targetsFile );
      }
      
      targets = new ArrayList<Target>();
      
      while ( file.hasNextLine() ){
         Scanner line = new Scanner( file.nextLine() );
         //read in the data for creating the targets, assume valid file
         int x = line.nextInt();
         int y = line.nextInt();
         int w = line.nextInt();
         int h = line.nextInt();
         int hits = line.nextInt();
                  
         Target rect = new Target( x, y, w, h, hits );
         targets.add( rect );        
      }
   }
   
   //---------------------- buildTree() ----------------------------------
   /**
    * build the quad tree
    */
   public void buildTree()
   {
      QNode.resetColors();

      QNode.leafCount = 0;
      for ( int i = 0; i < QNode.leafLevelDistrib.length; i++ )
         QNode.leafLevelDistrib[ i ] = 0;
      
      _root = new QNode( new Rectangle( 0, 0, width - 1, height -1 ), this);
      interNodeBullets = new ArrayList<Bullet>();
      deadBullets = new LinkedList<Bullet>();
      makeChildren( _root, maxDepth );
      rebuildTree = false;
   }
   //---------------------- restart() ----------------------------------
   /**
    * restart bullet movement; need to remove all previous bullets from
    * the panel.
    */
   public void restart()
   {    
      System.out.println( "restart" );
      emptyNode( _root );
      interNodeBullets.clear();
   }
   //----------------- emptyNode( QNode ) ------------------------------------
   /**
    * empty the node and all its children of bullets, move to dead bullets
    */
   private void emptyNode( QNode node )
   {
      if ( node == null )
         return;
      if ( node.kids != null ) // its an internal node!
         for ( int k = 0; k < node.kids.length; k++ )
            emptyNode( node.kids[ k ] );
      else // this is a leaf 
      {
  for ( Bullet b: node.bullets ){
      deadBullets.add( b );
  }
         node.bullets.clear();
      }
   }

   
   //----------------------- checkCounts( String ) ---------------------------
   /**
    * check that counts always add up to total
    */
   static int lastCount = 0; 
   static int callCount = 0;
   public int checkCounts( String label )
   {
      callCount++;
      int between = interNodeBullets.size();
      int in  = _root.treeBulletCount( 0 );
      int sum = between + in;
      if ( lastCount == 0 )
         lastCount = sum;
      else if ( sum != lastCount  || callCount % 10000 == 0 ) 
      {
         System.out.println( label + " b: " + between + " " 
                          + " i: " + in + " == " + sum  + "  " + callCount );
         lastCount = sum;
      }
      return sum;
   }
   
   //------------------------------ newFrame() -----------------------------
   /*
    * newFrame() moves all the bullets forward in time based on vector
    */
   public void newFrame() 
   {
      moveBullets( _root ); // move bullets
      // re-add all the bullets between nodes
      for ( Bullet bullet : interNodeBullets )
         addBullet( bullet );
      interNodeBullets.clear(); // drop the empty list ( removeAll works,
                                  // clear is faster )
      if ( QNode.edgeCount > 0 )
         System.out.println( "Edge count: " + QNode.edgeCount );
      QNode.edgeCount = 0;
      
      calcCollisions( _root );
      
      if ( curFrame % bulletFrameFire == 0 ){
         getCreateBullet();
      }
      
      curFrame++;
      
      if ( rebuildTree ){
         rebuildTree();
      }
      
      repaint();
   }
   
   public void rebuildTreeWhenDone(){
      rebuildTree = true;
   }
   
   private void getCreateBullet(){
       if ( numBullets < maxBullets ) {
    addBullet( new Bullet(this, 
     new Point2D.Float( 0, height/2), bdx, bdy ));
    numBullets++;
       }
       else if ( !deadBullets.isEmpty() ) {
    Bullet b = deadBullets.get( 0 );
    deadBullets.remove( 0 );
    b.restart();
    b.setPosition( new Point2D.Float( 0, height/2) );
    b.setDeltas( bdx, bdy );
    addBullet( b );
       }
       else{
    //System.out.println("Reached max bullets.");
       }
   }
   //----------------------- paintComponent( Graphics ) ---------------------------
   /**
    * paint the field, the bullets and the tree 
    */
   protected void paintComponent( Graphics g ) 
   {
      super.paintComponent( g );
      //System.out.println( "PaintComponent" );
   }
   //----------------------- paint( Graphics ) ---------------------------
   /**
    * paint the field, the bullets and the tree 
    */
   public void paint( Graphics g ) 
   {
      super.paint( g );
      if ( showTree )
         drawNode( g, _root );
      
      for ( Target t : targets ){
         t.draw( g );
      }
      drawBullets( g, _root );
   }

   
   //-------------------- drawBullets( Graphics, QNode ) -------------------
   public void drawBullets( Graphics g, QNode node ) 
   {
      if ( node == null )
         return;
      if ( node.kids == null ) 
      {
         Color col = Color.GREEN;
         if ( showTree )
            col = node.color;
         for ( Bullet bullet : node.bullets ) 
            bullet.draw( g, col );
         return;
      }
      for ( int i = 0; i < node.kids.length; i++ ) 
         drawBullets( g, node.kids[ i ] );
   }
   
   //----------------------- drawNode( Graphics, QNode ) ---------------------
   public void drawNode( Graphics g, QNode node ) 
   {
      if ( node == null )
         return;
      if ( node.kids == null  )
      {
         g.setColor( node.color );
         Rectangle rect = node.getRectangle();
         g.drawRect( rect.x , 
                    rect.y, 
                    rect.width-2, 
                    rect.height-2 );
      }
      if ( node.kids != null )
      {
         for ( QNode kid : node.kids ) 
            drawNode( g, kid );
      }
   }
   
   //----------------- makeChildren( QNode, int, double ) -----------------
   
   public void makeChildren( QNode node, int depth ) 
   {
      if ( node == null )
         return;
      if ( node.getWidth() / 2 < minSize 
             || node.getHeight() / 2 < minSize || depth <= 0 
             || getTargetsNodeContains( node ) < minTargets){
         ArrayList<Target> ts = getTargets(node);
         node.addTargets(ts);
         return;
      }
      
      if ( node.kids != null )
      {
         System.out.println( "Building tree, child link not null!!!" );
         return;
      }
      
      
      //Rectangle rect = node.rect;
      int x = (int)node.getX();
      int y = (int)node.getY();
      int w = (int)node.getWidth();
      int h = (int)node.getHeight();
      int wL = w / 2; // width of left 2 nodes
      int wR = w - wL; // width of right 2 nodes
      int hT = h / 2; // height of top 2 nodes
      int hB = h - hT; // height of bottom 2 nodes
     
      QNode.incrementColor();
      node.kids = new QNode[ 4 ];
      node.kids[ 0 ] = new QNode( new Rectangle( x,      y,      wL, hT ), this);
      node.kids[ 1 ] = new QNode( new Rectangle( x + wL, y,      wR, hT ), this);
      node.kids[ 2 ] = new QNode( new Rectangle( x,      y + hT, wL, hB ), this);
      node.kids[ 3 ] = new QNode( new Rectangle( x + wL, y + hT, wR, hB ), this);
      for ( int i = 0; i < node.kids.length; i++ ) 
         makeChildren( node.kids[ i ], depth - 1 );

      QNode.decrementColor();
   }
   
   private ArrayList<Target> getTargets( QNode node ){
      ArrayList<Target> result = new ArrayList<Target>();
      Rectangle rect = node.getRectangle();
      for ( Target t : targets ){
         if ( rect.intersects( t.x, t.y, t.width, t.height ) ){
            result.add(t);
         }
      }
      return result;
   }

   public int getMinTargets( ){
 return minTargets;
   }

   public void setMinTargets( int m ){
 minTargets = m;
   }
   
   public int getTargetsNodeContains( QNode node ){
      int result = 0;
      Rectangle rect = node.getRectangle();
      for ( Target t : targets ){
         if ( rect.intersects( t.x, t.y, t.width, t.height ) ){
            result += 1;
         }
      }
      
      return result;
   }
   
   //------------------- addBullet( Bullet ) --------------------
   /**
    * Adds a bullet to the field. Adds it to the root
    *       
    */
   public void addBullet( Bullet bullet ) 
   {
      if ( _root.contains( bullet ) )
      {
         addBullet( _root, bullet );
      }         
   }
   //------------------- addBullet( QNode, Bullet ) --------------------
   /**
    * Adds a bullet to a node, but only actually adds it to a leaf of the tree
    * Note: this code assumes the bullet is contained in the node, so it
    *       only has to determine which of 4 children it is in.
    *       It doesn't actually work to use contains because of inconsistencies
    *       in interpretation of the Rectangle contains method (which is
    *       essentially a pixel center based test) and the notion of a 
    *       continuous space which we want to use.
    *       
    */
   public void addBullet( QNode node, Bullet bullet ) 
   {
      if ( node == null ) // this should never happen
      {
         System.err.println( "ERROR: Game.addBullet got null node." );
      }
      if ( node.kids == null )
      {
        node.bullets.add( bullet );
      }
      else
      {
         boolean added = false;
         int len = node.kids.length;
         for ( int i = 0; i < len && !added; i++ ){
            QNode qn = node.kids[i];
            if ( qn.contains( bullet ) ){
               addBullet(qn, bullet);
               added = true;
            }
         }
      } 
   }

   //-------------------- moveBullets( QNode ) ------------------------
   public void moveBullets( QNode node ) 
   {
      if ( node == null )
         return;
      if ( node.kids == null ) 
      {
         Iterator<Bullet> iter = node.bullets.iterator();
         Bullet bullet;
         while ( iter.hasNext()) 
         {
            bullet = iter.next();
            bullet.move();
     
            if ( !node.contains( bullet ) ) 
            {
               iter.remove();
        if ( !_root.contains( bullet ) ){
     deadBullets.add( bullet );
     bullet.expired = true;
        }
        else{
     interNodeBullets.add( bullet );
        }
            }
         }
         return;
      }
      for ( int i = 0; i < node.kids.length; i++ ) 
      {
         moveBullets( node.kids[ i ] );
      }
   }
   
   private void calcCollisions( QNode node ){
      if ( node == null )
         return;
      if ( node.kids == null ) 
      {
         node.calcCollisions();
         return;
      }
      for ( int i = 0; i < node.kids.length; i++ ) 
      {
        calcCollisions( node.kids[ i ] );
         
      }
   }
   
   public void removeTarget( Target t ){
      targets.remove(t);
      removeTargetWork( _root, t );
   }
   
   private void removeTargetWork( QNode node, Target t ){
      if ( node == null )
         return;
      if ( node.kids == null ) 
      {
         node.removeTarget( t );
         return;
      }
      for ( int i = 0; i < node.kids.length; i++ ) 
      {
        removeTargetWork( node.kids[ i ], t );
      }
   }
   
   //----------------------- dumpBullets( QNode ) -------------------------
   /**
    * This method adds all the bullets that are contained in QNode node into
    * an array list and returns it (dumps the bullets into an array list)
    */
   public ArrayList<Bullet> dumpBullets( QNode node ) 
   {
      if ( node == null )
         return null;
      if ( node.kids != null ) {
         for ( QNode kid : node.kids ) {
            node.bullets.addAll( dumpBullets( kid ));
         }
      }
      return node.bullets;
   }
   
   //----------------------- rebuildTree( ) -------------------------
   public void rebuildTree( ) 
   {
      QNode.resetColors();

      ArrayList<Bullet> bullets = new ArrayList<Bullet>();
      if ( _root != null ) 
      {
         bullets.addAll( dumpBullets( _root ) );
         _root = new QNode( _root.getRectangle(), this );
      }

      makeChildren( _root, maxDepth );
      for ( Bullet bullet : bullets )
         addBullet( _root, bullet );
      checkTree();
      rebuildTree = false;
   }
   //-------------------------- checkTree() ----------------------------
   private void checkTree()
   {
      int pCount = _root.treeBulletCount( 0 );
      /***************
      System.out.println( "Bullets in tree: " + pCount + 
                         "   Leaf count: " + QNode.leafCount );
      System.out.print( "LeafLevelDistribution: " );
      /**/
      for ( int i = 0; i < QNode.leafLevelDistrib.length; i++ )
      {
         //System.out.print( QNode.leafLevelDistrib[ i ] + " " );
         QNode.leafLevelDistrib[ i ] = 0;
      }
      //System.out.println( "\n-------------------------" );
      QNode.leafCount = 0;      
   }
   //---------------------- setMaxDepth( int ) -------------------
   /**
    * Set the maximum depth allowed for expanding the quad tree
    *  and rebuild the tree.
    */
   public void setMaxDepth( int max )
   {
      maxDepth =  max;
      rebuildTree();
   }

   public void setDY( float a ){
      bdy = a;
   }
   
   public void setDX( float s ){
      bdx = s;
   }
   
   public void setAngle( float theta ){
      float radian = theta / 180.0f * (float) Math.PI;
      setDX( (float) (  bulletSpeed * Math.cos( radian )));
      setDY( (float) ( -bulletSpeed * Math.sin( radian )));
      //System.out.println( "Angle, dx, dy: " + theta + " " + bdx + " " + bdy );
   }
   
   public void setBFF( int b ){
      bulletFrameFire = b;
   }
   
   private void createFrameTimer(){
      
      frameTimer = new Timer( frameDelay, new ActionListener() 
          {
             public void actionPerformed( ActionEvent e ) 
             {
                newFrame();
             }
          } );
      frameTimer.start();
   }


   //--------------------- main --------------------------------------------
   /**
    * Convenience main to invoke app
    */
   public static void main( String[] args )
   {
      QuadShoot.main( args );
   }
}