/**
 * Hospital.java - represent a hospital using A-classes
 * 
 *        This class is completed.
 * 
 *        Note: the location returned by getLocation is the front door,
 *              not the upper left corner of the bounding box.
 * @author rdb
 * 02/02/2011
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Hospital
{
   //---------------- instance variables ------------------------
   // er door location -- returned by getLocation
   private Point             _erDoor;
   private ArrayList<AShape> _shapes;
   
   //--------- magic constants
   private int winH   = 10;
   private int winW   = 6;
   private int winGap = 3;
   private int floorH = 20;
   
   //--------------------  constructors ---------------------------
   /**
    * Constructor for Hospital: specify color, location, and size
    */
   public Hospital( Color aColor, int x, int y, int w, int h )
   {     
      _shapes = new ArrayList<AShape>();
      
      ARectangle building = new ARectangle( x, y );
      building.setFillColor( aColor );
      building.setFrameColor( Color.BLACK );
      building.setSize( w, h );
      _shapes.add( building );
      
      int floors = h / floorH;
      int dx     = winW + winGap;
      int nWins  = w / dx;       // # windows per floor
      int xOffset = ( w % dx + winGap ) / 2;
      int yOffset = 5;
      
      int yp = y + yOffset;
      for ( int f = 0; f < floors; f++ )
      {
         int xp = x + xOffset;       
         for ( w = 0; w < nWins; w++ )
         {
            ARectangle window = new ARectangle( xp, yp );
            window.setFillColor( Color.WHITE );
            window.setFrameColor( Color.BLACK );
            window.setSize( winW, winH );
            _shapes.add( window );
            xp += dx;
         }
         yp += floorH;
      }
      _erDoor = new Point( x - 10, y + h );
   }
   //--------------------- getLocation() --------------------------------
   /**
    * return location a bit to the left of the lower right corner
    */
   public Point getLocation()
   {
      return _erDoor;
   }
   //----------------- display( Graphics2D ) ----------------------------
   /**
    * delegate calls to the AWT objects
    */
   public void display( Graphics2D g2 )
   {
      for ( AShape shape: _shapes )
         shape.display( g2 );
   }
}    