/** 
 *   MissionApp.java:
 *   Sends ATV on a mission
 *   mlb 10/09
 * Modified (twp 6/10) for the ATV application.
 */

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;

public class MissionApp 
{  
   private Deployable atv;            // the deployable ATV
   
   // Mission details
   private Vector< Point > mission;   // the mission iteneary
   private int delay = 30;            // mission delay
   private int power = 100;           // initial power amount
   private int fps= 20;               // frames per second
   private int step = 15;             // the distance between waypoints
   
   // Name of background file
   private  String background = "mountain.jpg";
   

   //---------------------------------------------------------
   public MissionApp(  )
   {
      // Draw the background image
      Image im = new Image( background );      
      im.setLocation( 0, 0 );
      //im.setSize( 700, 500 );
      
      // Draw the ATV
      atv = new MissionATV( -500, -500 );  
      atv.setColor( Color.PINK );
      
      // Get the mission orders
      mission = new Vector< Point >( );
      getMissionOrders( );
      
      
      // Check for errors in the mission details
      if( fps == 0 || power == 0 || mission.size() == 0 )
      {
         System.out.println( "Mission file format error! cannot run mission" );
         System.out.println( "f: " + power + " fps: " + fps + " size: " +
                                                              mission.size() );
      }
      else
      {
         atv.setPower( power );                        
         atv.setStartPoint( mission.get( 0 ) );
         atv.setStepSize( 3 );
         
         // Finally, lets run the Mission!!
         for ( int i = 1; i < mission.size(  ) ; i++ ) 
         {
            atv.setNextPoint( mission.get ( i ) );
            
            while ( !atv.move() )
               sleep( 1000 / fps );
         }   
      }
   }
   
   //----------------------------getMissionOrders( ) --------------------------
   /** 
    *  get mission choice from user with JOptionPane
    */
   private void getMissionOrders( )
   {
      int m = 0; 
      try{
         String s  = JOptionPane.showInputDialog( null, 
                                       "Choose a mission, enter: 1 , 2 or 3 " );
         m = Integer.parseInt( s );
         
         if( m < 0 || m > 3 )
         {
            m = 1;
            System.out.println( "Option invalid choice, using mission 1" );              
         }
      }
      catch( Exception e )
      {
         m = 1;
         System.out.println( "Option invalid input, using  mission 1" );   
      }

      if( m==1 )
         readMission( "mission1.txt" );
      else if( m == 2 )
         readMission( "mission2.txt" );    
      else {
         readMission( "mission3.txt" );
      }
   }
   
   
   //---------------------readMission( String ) --------------------
   /**
    *  Opens the mission file "orders" and reads in the mission details
    *  a mission file contains a delay and a power amount followd by a list of
    *  x y pairs.
    */
   private void readMission( String orders ) 
   {
      String fileName = orders.trim( );
      File inputFile = new File( fileName );
      int x = 0, y = 0;
      int lastX = -100, lastY = -100;
      Scanner scanFile;
      try
      {
         scanFile = new Scanner( inputFile );
      }
      catch( IOException e)
      {
         System.err.println( "Mission file: \"" + inputFile +"\" could not be found!" +
                            "\nMission can not be run!" );
         return;
      }
      
      if( scanFile.hasNextInt( ) )
      {
         fps = scanFile.nextInt( );
      }
      
      if( scanFile.hasNextInt( ) )
      {
         power = scanFile.nextInt( );
      }
      
      while ( scanFile.hasNextInt( ) )
      {
         x = scanFile.nextInt( );
         if( scanFile.hasNextInt () )
         {
            y = scanFile.nextInt( );
            if ( Math.sqrt( Math.pow( x - lastX, 2 ) +
                           ( Math.pow( y - lastY, 2))) > step )
            {
               mission.add( new Point( x, y ) );        
               lastX = x;
               lastY = y;
            }
         }             
      }
      
      // ensure the last point is added
      if ( lastY != y )
         mission.add( new Point( x, y ) );  
   }
   
   // ----------------------------------------------------------------
   /** Pauses the thread for the specified number of milliseconds.
     * 
     */
   public static void sleep( int time )
   {
      try
      {
         Thread.sleep( time );
      }
      catch ( java.lang.InterruptedException e ) 
      {  }
   }
   
   //---------------------------------------------------------
   public static void main( String[ ] args )
   {      
      new Frame(  );
      new MissionApp( ); 
   }  
}//End of Class MissionApp
