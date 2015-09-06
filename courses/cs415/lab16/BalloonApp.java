/** 
 * BalloonApp.java:
 *
 * mlb 10/2008
 * modified by rdb 10/25/09
 * 
 *  @Stephen Chambers
 * 10/26/10
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.util.Random;


public class BalloonApp extends Frame
{  
   //------------------- instance variables -------------------
   private Random  rng;
   private Color myColor;
   
   //-------------------- constructor ----------------------
   public BalloonApp( )
   {   
      int y = 450;
      
      rng = new Random();
      int count;
      String prompt =  "How many balloons do you want?";
 
      count = BalloonUtilities.readInt( prompt );
   
      while ( count != 0 )
      {        
         System.out.println( "Balloon count: " + count );
         floatBalloons( count, y );
         count = BalloonUtilities.readInt( prompt );
      }     
   }
   
   //------------ floatBalloons( int, int y ) -----------------------------
   /**
    * create an array of n balloons at random x locations and a fixed y
    */
   private void floatBalloons( int num, int y )
   { 

  
      if (num > 1000)
     {
       Balloon.size = 1000;
     }
     
     else
     {
       Balloon.size = 30;
     }   
      
      Balloon[] balloons = new Balloon[ num ]; // create array with n elements
                                           
      for ( int i = 0; i < num; i++ )
      {
        int randX = rng.nextInt(500);
        int randSpeed = 5 + rng.nextInt(14-5);
        Balloon myBalloon = new Balloon(randX, y, randSpeed);
        balloons[i] = myBalloon;
        myColor = randomColor();
        balloons[i].setColor(myColor);    
      }
      BalloonUtilities.sleep( 1000 ); // pause to see what we start with
      
      boolean stillFloaters = true;
      while ( stillFloaters )  // keep looping if at least 1 balloon is rising
      {
         
         for(int i = 0; i < num; i++)
         {
           if(balloons[i] != null)
           {
             if(balloons[i].rise())
             {
               stillFloaters = true;
             }
             else
             {
               balloons[i] = null;
             }
           }
         }

         
         BalloonUtilities.sleep( 100 ); // sleep for just a bit
      }      
   }
   //------------------- randomColor() ------------------------
   /**
    * return a random color from an array of predefined choices
    */
   private Color randomColor()
   {
      Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.CYAN,
                         Color.MAGENTA, Color.BLACK, Color.YELLOW, Color.GRAY };
      return colors[ rng.nextInt( colors.length ) ];
   }
   
   //---------------------------------------------------------
   public static void main( String[ ] args )
   {
      new BalloonApp( ); 
   }  
}//End of Class BalloonApp