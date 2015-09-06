/**
 * TrackingTarget.java -- Starter: a draggable target
 * 
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;


public class TrackingTarget extends ShapeGroup
{
   //------------------ class variables ------------------------------
   public static int size = 20;       // overall target size
   private static Color colors[] = {Color.BLUE, Color.GREEN, Color.ORANGE,
                                    Color.PINK, Color.YELLOW, Color.CYAN};
   
   //------------------ instance variables ---------------------------
   int curX, curY;               // current location of target
   Point lastMouse;              // last mouse position
   Vector<Point> points;
   Point lastPoint;
   private int lastIndex = -1;
   private Color myColor;
   private Ellipse inner;


   
   //------------------------------------------------------------------
   /**
    * build a 2 layer target at x,y
    */
   public TrackingTarget ( int x, int y )
   {
      Ellipse outer = new Ellipse( 0, 0 );
      outer.setSize( size, size );
      outer.setColor( Color.RED );
      
      int innerSize = size / 2;
      int offset = ( size - innerSize ) / 2;
      inner = new Ellipse( offset, offset );
      inner.setColor( Color.BLACK );
      inner.setSize( innerSize, innerSize );
      
      
      points = new Vector<Point>();
      curX = x;
      curY = y; 
      add( inner );
      add( outer );
      setLocation( x, y );
   }
   
   //+++++++++++++++++++ Draggable interface methods ++++++++++++++++++
   //------------------ mousePressed -------------------
   /**
    * save the mouse press position
    */
   public void mousePressed( MouseEvent me )
   {
            lastMouse = me.getPoint();
   }
   //------------------ mouseReleased -------------------
   /**
    * print a message with the size of the point Collection gathered
    * by dragging.
    */
   public void mouseReleased( MouseEvent me )
   {
   
      System.out.println("Point Collection Size: " + points.size());
      


   }
   
   //------------------ mouseDragged -------------------
   /**
    * move the target by the same dx,dy as mouse moved.
    */
   public void mouseDragged( MouseEvent me )
   {
      Point curMouse = me.getPoint();
      points.add(curMouse);
      curX = curX + curMouse.x - lastMouse.x;
      curY = curY + curMouse.y - lastMouse.y;
      
      setLocation( curX, curY );
      lastMouse = curMouse; 
      points.add(lastMouse);
      /////////////////////////////////////////////////////
      // add the mouse position to a vector of points for
      //   drawing the path later
      ///////////////////////////////////////////////////
      
   }
   
   //------------------ mouseClicked -------------------
   /**
    * Draw the saved path only if the mouse position is inside the inner ellipse.
    * 
    * Save the last point on the path, empty the path and add the last point
    * back into the path. 
    */
   public void mouseClicked( MouseEvent me )
   {  
     cycleColor();
     drawLine();
     //lastPoint = points.get(points.size());
     //points.clear();
     //points.add(lastPoint);
    
     myColor = colors[lastIndex];
     inner.setColor(myColor);
   }
   private void drawLine()
   {
     
     for(int i = 0; i < points.size() - 1; i++)
     {
       Line myLine = new Line(points.get(i), points.get(i+1));
       myLine.setColor(inner.getColor());
     
     }
   }
   public void cycleColor()
   {
     if (lastIndex < 5)
     {
       lastIndex++;
     }
     else if(lastIndex ==5)
     {
       lastIndex = 0;
     }
     
   }
   //--------------------------------------------------------------------------
   //
   public static void main( String[] s )
   {
      Frame f = new Frame();
      TrackingTarget a = new TrackingTarget( 600, 200 ); 
      
      TrackingTarget.size = 30;
      TrackingTarget b = new TrackingTarget( 10, 10 );    
   }
}
