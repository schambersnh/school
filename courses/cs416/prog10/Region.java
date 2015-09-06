/**
 * Region - manages a rectangular region in the space 
 *          it knows about its neighbors
 *        it passes objects to neighbors
 */

import java.awt.*;
import java.util.*;

public class Region extends Thread
{
   //--------------------- instance variables -------------------------
   private int row;
   private int col;
   private int xLoc;
   private int yLoc;
   private int width;
   private int height;
   private Rectangle bounds;
   private boolean shuttingDown = false;
   
   private GOset myGOs;
   private GOset northGOs = null;
   private GOset eastGOs = null;
   private GOset southGOs = null;
   private GOset westGOs = null;
   
   private Color myColor = null;
   private int   waitTime = 50;
   private String name;
   
   //--------------------- constructor --------------------------------
   public Region( int r, int c, int x, int y, int w, int h, GOset gos )
   {
      name = "R" + row + "x" + col;
      row   = r;  col    = c;
      xLoc  = x;  yLoc   = y;
      width = w;  height = h; 
      myGOs = gos;
      bounds = new Rectangle( x, y, w, h ); // Rectangle good for testing
                                            // if a Point is inside it
   }
   //------------------ setColor( Color ) ---------------------------
   /**
    * This color is used to color all objects in this region
    */
   public void setColor( Color col )
   {
      myColor = col;
   }
   //--------------------- setNeighbors ----------------------------
   /** 
    * Initialize neighbor refrences; some may be null!
    */
   public void setNeighbors( GOset n, GOset e, GOset s, GOset w )
   {
      northGOs = n;
      eastGOs  = e;
      southGOs = s;
      westGOs  = w;
   }
   //---------------------- setFrameInterval( int ) ---------------------
   /**
    * Frame interval is approximage time in milliseconds for each frame
    * of the animation. We use it as a wait time in the run loop.
    */
   public void setFrameInterval( int millis ) 
   {
      waitTime = millis;
   }
   //-------------------- run() -------------------------------------
   public void run()
   {  
      while ( !shuttingDown )
      {
         //////////////////////////////////////////////////////////////
         // Add code here for checking if objects in this regions
         //    set are still in the region.
         //    if so, color them the regions color.
         //    if not, remove them from this region and pass them on to the
         //       correct neighbor.
         //    border regions won't have 4 neighbors so check for null.
         //       if neighbor is null, don't need to do anything, the
         //       object will soon bounce off the edge of the window
         //       and come back in to the region.
         // GameObjects have a method that returns their center Point.
         //    Rectangle objects have a method that checks if the Rectangle
         //    contains a Point. 
         ///////////////////////////////////////////////////////////////
        for(int i = 0; i < myGOs.size(); i++)
        {
          GameObject curObject = myGOs.get( i );
          if(bounds.contains( curObject.getCenter( ) ))
          {
            curObject.setColor( myColor );
          }
          else
          {
            curObject.setColor( Color.BLACK );
            if(curObject.getCenter( ).y < yLoc && northGOs != null)
            {
              northGOs.add( curObject );
              myGOs.remove( curObject );
              i--; 
            }
            else if(curObject.getCenter( ).y > yLoc + height && southGOs != null)
            {
              southGOs.add( curObject );
              myGOs.remove( curObject );
              i--; 
            }
            else if(curObject.getCenter( ).x < xLoc && westGOs != null)
            {
              westGOs.add( curObject );
              myGOs.remove( curObject );
              i--; 
            }
            else if(curObject.getCenter( ).x > xLoc + width && eastGOs != null)
            {
              eastGOs.add( curObject );
              myGOs.remove( curObject );
              i--; 
            }  
          }
        }
         try 
         {
            Thread.sleep( RegionManager.sleepDelay );
         }
         catch ( InterruptedException ie )
         {
            System.err.printf( "Region: %s interrupted\n", name );
         }           
      }
   }
   //------------------ terminate() ------------------------------------
   /**
    * Signal to shut down in this case we can shut down immediately
    */
   public void terminate()
   {
      shuttingDown = true;
   }
   //////////////////////////////////////////////////////////////////
   // This main is a good test for early debugging; not many regions,
   //    not many initial objects.
   // After awhile either use command line arguments or run from
   //    ThreadsApp
   //////////////////////////////////////////////////////////////////
   //++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++
   public static void main( String[] args )
   {
      String[] defaultArgs = { "2", "2", "10" };
      if ( args.length == 0 )
         ThreadsApp.main( defaultArgs );
      else
         ThreadsApp.main( args );
   }      
}