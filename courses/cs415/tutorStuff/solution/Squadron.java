/**
 * Squadron.java
 * 
 * @author mlb
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Squadron extends ShapeGroup 
{
   //-------------------------- class variables ------------------------
   private static int width = 11;
   private static int depthA = 2 , depthB = 2, depthC = 1;
   private static int depth = depthA + depthB + depthC;
   private Random gen;
   //-------------------------- instance variables ---------------------
   private Point  _loc;
   private int count;
   
   int [] g = {4,4,4,4,4,4,4,4,4,4,4};
   
   private Invader [][] squadron;
   
   private      int w = Invader.shipW;
   private     int h = Invader.shipH;
   private  int space = 6;
   
   //------------------------ constructor ------------------------------
   public Squadron( int x, int y)
   {
      gen = new Random( );
      _loc = new Point( x, y );
      squadron = new Invader[depth][width];
      
      for(int r = 0; r<depth; r++)
         for(int c = 0; c<width;c++)
      {
         squadron[r][c] = new Invader( c* (w + space), r* h*2);
         if( r >= depthC +depthB )
            squadron[r][c].setType(1);
         else if( r >= depthC )
            squadron[r][c].setType(2);
         else
            squadron[r][c].setType(3);
         
         add(squadron[r][c]);
      }
      
      setLocation( x, y );
   }
   
   
   //-------------------------------------------------------------------------------
   /**
    *
    * 
    */
   public boolean contains( Missile t)
   {
      for(int r = 0; r<depth; r++)
         for(int c = 0; c<width;c++)
      {
         if(   squadron[r][c].contains( t )  )
         {
            System.out.println( r + ":" + c);
            return true;
         }
      }
      
      return false;
   }
   
   //-------------------------------------------------------------------------------
   /**
    *
    * 
    */
   public boolean contains( Defender d )
   {
      for(int r = 0; r<depth; r++)
         for(int c = 0; c<width;c++)
      {
         if(   squadron[r][c].contains( d )  )
         {
            return true;
         }
      }
      return false;
   }
   
   
   
   
   //-------------------------------------------------------------------------------
   /**
    * Randomly return a missile
    * 
    */
   public Missile getMissile(  )
   {
      Missile m = null;
      
      int n = gen.nextInt( 1000 );
      
      if( n > 10 ) 
         return null;
      
      // about every 100 calls
      int c = gen.nextInt( 11 );
      int r = 0;
      
      // find lowest
      while( r < squadron.length && squadron[ r ][ c ].isActive() )
      {
         r++;
      }
      r--;
      
      if( r >= 0)
         return new Missile( squadron[r][c].getXLocation()+18 ,squadron[r][c].getYLocation()+24, +5 );
      
      return null;
   }
   
   
   //-------------------------------------------------------------------------------
   /**
    *  
    * 
    */
   public int bottom(  )
   {
      int y = getYLocation();
      
      for(int c = 0; c< width; c++)
      {
         if(g[c] >= 0)
            for(int r = g[c]; r>= 0;r--)
         {
            if( g[c] >=0 && !squadron[r][c].isActive() )
               g[c]--;
         }
      }
      
      int bot = -1;
      
      for(int c = 0; c< width; c++)
      {
         if(g[c]>bot)
            bot = g[c];
      }
      if(bot == -1)
         return -1;
      
      return y + bot* h*2;   
   }
   
   
   
   //-------------------------------------------------------------------------------
   /**
    * One animation step
    * 
    */
   public  int step()
   {
      int dx=0,dy=0;
      if( count < 8 )
         dx = (w+space)/2;
      else if (count ==8)
         dy = 2*h;
      else if (count <17)
         dx = -(w+space)/2;
      else // count = 17
      {
         dy = h;
         count = -1;
      }
      setLocation(getXLocation() + dx , getYLocation() + dy );
      count ++;
      
      return bottom();
   }
   
   
   
   //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      Frame f = new Frame();
      Squadron i = new Squadron( 20, 30);
   }
   
}