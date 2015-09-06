import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;


/** 
 *  MorphingShape.java:
 * Alternately shows and hides shapes to give the appearence that the shape is 
 * changing on a mouseclick
 * @Stephen Chambers
 * 10/23/10
 *  
 */
public class MorphingShape extends ShapeGroup
{ 
   //----------------- instance variables --------------------------------------

   private Rectangle r;
   private RoundedRectangle rr;
   private Ellipse e;
   private boolean shownrect = true, shownrr, shownell;
   
   //------------------ constructors ---------------------------------------------
   public MorphingShape(int x, int y ) 
   {        
        r = new Rectangle( x, y );
        add( r );
        r.setColor( Color.GREEN );
        
        rr = new RoundedRectangle( x, y );
        add( rr );
        rr.setColor( Color.RED );
        
        e = new Ellipse( x, y );
        add( e );
        e.setColor( Color.BLUE );
        //////////////////////////////////////////
        //   hide all but one shape and remember
        //   which one is showing
        ///////////////////////////////////////////
        
          r.show();
          rr.hide();
          e.hide();
          shownrect = true;   
   }
    

   //-----------------------------------------------------------------------------
   public void mouseClicked( MouseEvent me )
   {
         ///////////////////////////////////////////
         // change rectangle  to rounded rectangle  
         // or rounded rectangle  to ellipse 
         // or ellipse to rectangle 
         ///////////////////////////////////////////
        
            
        if (shownrect)
        {
          
          rr.hide();
          r.show();
          e.hide();
          shownrr = true;
          shownrect = false;
           System.out.println("Rect: " + shownrect + "\nRR: " + shownrr +
                              "\nELL: " + shownell + "\n");
        }
       
        
         else if (shownrr)
        {
         
          rr.show();
          r.hide();
          e.hide();
          shownell = true;
          shownrr = false;
           System.out.println("Rect: " + shownrect + "\nRR: " + shownrr + 
                              "\nELL: " + shownell + "\n");
        }

         
         else if(shownell)
        {
          
          rr.hide();
          r.hide();
          e.show();
          shownrect = true;
          shownell = false;
           System.out.println("Rect: " + shownrect + "\nRR: " + shownrr + 
                              "\nELL: " + shownell + "\n");
        }
 
     
   }
     
   
   //---------------------------- main -----------------------------------------
   public static void main( String[ ] args )      
   {
      new Frame( );

      new MorphingShape( 325, 225 );
   }
} //End of Class