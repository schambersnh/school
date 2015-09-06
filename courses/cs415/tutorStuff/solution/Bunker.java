                                       /**
 * Bunker.java
 * 
 */
     

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Bunker extends ShapeGroup
{
   //-------------------------- class variables ------------------------
   public static int rows = 10;
   public static int cols = 20;
   
   private Rectangle bunker [][];
   private Random gen;
   private int hits = 0;
      
   //------------------------ constructor ------------------------------
   public Bunker( int x, int y)
   {
       bunker = new Rectangle [rows][cols];
         for(int r = 0; r<rows; r++)
         for(int c = 0; c<cols;c++)
      {
           bunker[r][c] = new Rectangle( c* 4, r* 4) ;
           bunker[r][c].setSize(4,4);
           bunker[r][c].setColor(Color.black);
           add(bunker[r][c]);
      }
       gen = new Random();
       setLocation(x,y);
   }

   
    public boolean contains( Missile m)
   {
      Point t = m.getTop();
      Point b = m.getBottom();
      if(hits >= 10)
         setLocation(-500,-500);
      
      
      if(contains(t) )
      {
          int x = m.getXLocation();
          int mx = getXLocation();
          int col = (x - mx)/2;
          for(int i = 0; i<20;i++)
          {
             int r = gen.nextInt(rows);
             int c = gen.nextInt(cols);
             
             bunker[r][c].setColor(Color.white);
          }
          hits ++; 
          return true;
         
      }
      else if(contains(b) )
      {
       
          int x = m.getXLocation();
          int mx = getXLocation();
          int col = (x - mx)/2;
          for(int i = 0; i<20;i++)
          {
             int r = gen.nextInt(rows);
             int c = gen.nextInt(cols);
             bunker[r][c].setColor(Color.white);
             
          }
         hits ++;
           return true;
      }
      
      return false;
   }
   
      //------------------------------- main -------------------------------
   public static void main( String [] args ) 
   {
      Frame f = new Frame();
      Bunker i = new Bunker( 20, 30);
   }
   
}