/**
 * WheelsApp.java -- CS416 WheelsApp, Spring 2008
 * 
 * This is a wheels application that has been organized to make it a bit 
 * easier to translate into an AWT/Swing application. The wheels hides the 
 * interface to the AWT JPanel. 
 * 
 * @author rdb
 * January 2008
 */

import wheelsunh.users.*;
import java.awt.Color;

public class WheelsApp extends Frame
{
   //-------------------- instance variables ----------------------
   
   //------------------ constructor ---------------------------------
   /**
    * Create a simple scene
    */
   public WheelsApp( String[] args )
   {   
     makeScene();
   }
   //------------------- makeScene() ----------------------------
   /**
    * Create the wheels objects that make up the scene
    */
   private void makeScene()
   {
      Rectangle rect1 = new Rectangle( Color.BLUE );
      rect1.setLocation( 100, 100 );
      rect1.setSize( 40, 40 );
      
      Rectangle rect2 = new Rectangle( Color.RED );
      rect2.setLocation( 200, 200 );
      rect2.setSize( 20, 60 );
      
      Line line1 = new Line();
      line1.setColor( Color.BLACK );
      line1.setPoints( 120, 120, 210, 230 );
      
      Ellipse ell1 = new Ellipse( Color.CYAN );
      ell1.setLocation( 10, 400  );
      ell1.setSize( 40, 10 );
      
      Ellipse ell2 = new Ellipse( Color.MAGENTA );
      ell2.setLocation( 400, 400 );
      ell2.setSize( 30, 30 );
      
      Line line2 = new Line();
      line2.setColor( Color.BLACK );
      line2.setPoints( 25, 405, 415, 415 );  
      
      WheelsBot robot1 = new WheelsBot( Color.YELLOW );
      robot1.setLocation( 450, 300 );
      
      WheelsBot robot2 = new WheelsBot( Color.CYAN );
      robot2.setLocation( 400, 20 );
   }
   
   //------------------------ main -----------------------------------
   public static void main( String[] args )
   {
      WheelsApp app = new WheelsApp( args );
   }
}