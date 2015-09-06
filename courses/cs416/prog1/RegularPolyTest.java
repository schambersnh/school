/**
 * RegularPolyTest.java -- this is a skeleton for a program to 
 *             thoroughly test the ARegularPoly class.
 * 
 *             It needs to be significantly expanded. At the very 
 *             least, every public method of ARegularPoly needs to be
 *             invoked at least once!
 * 
 * @author rdb
 * 01/24/11
 * Modified by Stephen Chambers on 2/2/2011
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RegularPolyTest extends JPanel
{
   //-------------- instance variables ------------------------------
   ArrayList<ARegularPoly> myPolygons;
   //------------------ constructor ---------------------------------
   public RegularPolyTest( )
   {
      ////////////////////////////////////////////////////////
      // Create enough ARegularPoly objects to show that you 
      //   have thoroughly tested this code. At the very least,
      //   your tests should cause the execution of every public method
      //   in the class (not just the ones you had to write).
      ////////////////////////////////////////////////////////
     myPolygons = new ArrayList<ARegularPoly>();
     
     //note: makeVertices() and makePolygon(int, int) are already 
     //tested because they are within theARegularPoly constructor
     
     
      //Testing ARegularPoly(int, int, int, int), setLocation(int, int),
      //getXLocation(), getYLocation(), setColor(Color)
      
      ARegularPoly myPoly = new ARegularPoly(30 , 30 , 6 , 30 );
      
      myPoly.setLocation(30, 30 );
      System.out.println("X coordinate of leftmost polygon: " + 
                         myPoly.getXLocation());
      System.out.println("Y coordinate of leftmost polygon: " + 
                         myPoly.getYLocation());
      myPoly.setColor(Color.CYAN);
      myPolygons.add(myPoly);
      
      //Testing setFrameColor(Color), setFillColor(Color), moveBy(int, int)
      ARegularPoly myPoly2 = new ARegularPoly(100 , 50 , 8 , 50);
      myPoly2.setFrameColor(Color.GREEN);
      myPoly2.setFillColor(Color.YELLOW);
      myPoly2.moveBy(40, 40);
      myPolygons.add(myPoly2); 
      
      //Testing , setLocation(Point), setSize(int, int), setThickness(int), 
      //setLineWidth(int) setRotation(int), setRadius(int)
      ARegularPoly myPoly3 = new ARegularPoly(200, 200, 6, 100);
      Point p = new Point(300, 300);
      myPoly3.setLocation( p );
      myPoly3.setThickness(9);
      myPoly3.setLineWidth(20);
      //radius changed from 100 to 50
      myPoly3.setRadius(50);
      myPoly3.setRotation(15);
      myPolygons.add(myPoly3);
     
      
   }
   //------------- paintComponent( Graphics ) ----------------------
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );
      Graphics2D brush2D = (Graphics2D) g;
      
       for(int i = 0; i < myPolygons.size(); i++)
      {
        myPolygons.get( i ).display( brush2D );
      }
   }
   
   //------------------------ main -----------------------------------
   public static void  main( String[] args )
   {
      JFrame f = new JFrame( "ARegularPoly test" );
      f.setSize( 700, 600 );
      f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      JPanel panel = new RegularPolyTest();
      f.add( panel );
      f.setVisible( true );  // Initially, JFrame is not visible, make it so.
   }            
}