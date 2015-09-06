/**
 * ShapeFunctions.java
 * 
 * This class includes a number of utility functions that can be 
 * applied to wheels.RectangularShape objects.
 * 
 * The current version should support 
 *       double area( Rectangle );
 *       double circumference( Rectangle );
 *       double diagonal(Rectangle);
 * All these functions are declared "static", which means they
 * are invoked by using the class name, not an instance.
 * 
 * @author Stephen Chambers
 * 10/12/10
 * 
 *
 */
import wheelsunh.users.*;

public class ShapeFunctions
{  
  //------------------------------Instance Variables---------------------------

   //--------------------------  area( Rectangle ) -----------------------
   /**
    * Compute the area of a wheels Rectangle. 
    * The area of a rectangle is the product of the length of its 2 sides.
  
    */ 
   public static double area( Rectangle r )
   {
      double area = r.getWidth() * r.getHeight();
      return area;
   }

  

   //-----------------------  circumference( Rectangle ) -----------------------
   /**
    * Rectangle circumference
    * The circumference of a rectangle is just twice the sum of its
    * sides.
    */
   public static double circumference( Rectangle r )
   {
      double circ = (2.0*r.getWidth()) + (2.0*r.getHeight());
      return circ;
   }

  //--------------------------  diagonal( Rectangle ) -----------------------
   /**
    * Rectangle diagonal length
    * The diagonal of a rectangle is the square root of the sum of the 
    * squares of the height and width   use Math.sqrt( double ) to get the
    * square root
    */
   public static double diagonal( Rectangle r )
   {
     double diag = Math.sqrt(Math.pow(r.getHeight(), 2) + 
                             Math.pow(r.getWidth(), 2));
      return diag;
   }
   

   //------------------------------ main -------------------------------------
   // unit test code
   //
   public static void main( String args[] )
   {
      Frame f = new Frame();
      
      Rectangle r = new Rectangle( 100, 100 );
      r.setSize( 20, 30 );
      
      double rArea = ShapeFunctions.area( r );
      System.out.println( "Rectangle area: " + rArea );
      
      double rCirc = ShapeFunctions.circumference( r );
      System.out.println( "Rectangle circumference: " + rCirc );
      
      double rDiag = ShapeFunctions.diagonal( r );
      System.out.println( "Rectangle diagonal: " + rDiag  );
      
    }
}