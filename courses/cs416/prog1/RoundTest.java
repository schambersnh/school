/**
 * RoundTest.java -- a skeleton for a comprehensive test of
 *    ARoundRectangle. This should be expanded sufficiently that
 *    it is clear from looking at the output that you have tested
 *    the ARoundRectangle thoroughly and understand its parameters.
 * Modified by Stephen Chambers for Program 1 2/2/2011
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RoundTest extends JPanel
{
   //-------------- instance variables ------------------------------
   ArrayList<AShape> myList;
   //------------------ constructor ---------------------------------
   public RoundTest( )
   {
     //Testing ARoundRectangle(), setLocation(int, int), getXLocation(), 
     //getYLocation(), setColor(Color)
      myList = new ArrayList<AShape>();
      ARoundRectangle myRoundRect1 = new ARoundRectangle();
      myRoundRect1.setLocation(30, 30 );
      System.out.println("X coordinate of leftmost RoundRectangle: " + 
                         myRoundRect1.getXLocation());
      System.out.println("Y coordinate of leftmost RoundRectangle: " + 
                         myRoundRect1.getYLocation());
      myRoundRect1.setColor(Color.CYAN);
      myList.add(myRoundRect1);
      
      //Testing ARoundRectangle(int, int, int, int, int, int), 
      //setFrameColor(Color), setFillColor(Color), moveBy(int, int)
      ARoundRectangle myRoundRect2 = new ARoundRectangle(100, 100, 60, 60, 50, 50);
      myRoundRect2.setFrameColor(Color.GREEN);
      myRoundRect2.setFillColor(Color.YELLOW);
      myRoundRect2.moveBy(10, 10);
      myList.add(myRoundRect2); 
      
      //Testing ARoundRectangle(Color), setLocation(Point), setArcSize, 
      //setSize(int, int), setThickness(int), setLineWidth(int)
      ARoundRectangle myRoundRect3 = new ARoundRectangle(Color.MAGENTA);
      Point p = new Point(300, 300);
      myRoundRect3.setArcSize(10, 10);
      myRoundRect3.setLocation( p );
      myRoundRect3.setThickness(9);
      myRoundRect3.setLineWidth(20);
      myRoundRect3.setSize( 60 , 60 );
      myList.add(myRoundRect3);
            
   }
   //------------- paintComponent( Graphics ) ----------------------
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );
      Graphics2D brush2D = (Graphics2D) g;
      for(int i = 0; i < myList.size(); i++)
      {
         myList.get(i).display(brush2D);
      }
   }
   
   //------------------------ main -----------------------------------
   public static void  main( String[] args )
   {
      JFrame f = new JFrame( "ARoundRectangle test" );
      f.setSize( 500, 400 );
      f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      JPanel panel = new RoundTest();
      f.add( panel );
      f.setVisible( true );  // Initially, JFrame is not visible, make it so.
   }            
}