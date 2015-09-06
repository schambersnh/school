/**
 * HotBox -- a mouse sensitive Rectangle
 *        mouseClicked events should alternate the color of the rectangle
 *              between red and its original color
 *        mousePressed and mouseDragged should change the size of the 
 *              rectangle; width should be changed by the x-difference
 *              and height by the y-difference. Do not allow either width
 *              or height to be set to a value less than 4.
 */
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.event.*;
import java.awt.Color;

public class HotBox extends Rectangle
{
   //----------------- instance variables ---------------------------
  Point lastMousePosition;

   //------------------- constructor --------------------------------
   /**
    * set position and color
    */
   public HotBox( int x, int y,  Color col )
   {
      setLocation(x, y);
      setColor(col);
   }

   //-------------------- mouseClicked -------------------------------
   /**
    * mouseClicked events should toggle the rectangle's color between
    * red and its original color.
    */
   public void mouseClicked( MouseEvent me )
   {
     
      if(this.getColor() == Color.RED)
      {
        setColor(Color.BLACK);
      }
      else
      {
        setColor(Color.RED);
      }
   }
   
   //-------------------- mousePressed -------------------------------
   /**
    * mousePressed and mouseDragged events should change size of rectangle
    */
   public void mousePressed( MouseEvent me )
   {
        lastMousePosition = me.getPoint();
   }
   
   //-------------------- mouseDragged -------------------------------
   /**
    * mousePressed and mouseDragged events should change size of rectangle
    * difference in x should change width
    * difference in y should change height
    */
   public void mouseDragged( MouseEvent me )
   {
          Point currentPoint = me.getPoint(); 
          int diffX = currentPoint.x - lastMousePosition.x; 
          int diffY = currentPoint.y - lastMousePosition.y;
          
          if(getWidth() >=4 && getHeight() >= 4)
          {
          setSize(getWidth() +diffX, getHeight() +diffY);
          }
          lastMousePosition = currentPoint;
   }

   //-------------------------- main - unit test code -----------------------
   public static void main( String[] args )
   {
      Frame f = new Frame();
      HotBox h1 = new HotBox( 100, 100, Color.GREEN );
      
      HotBox h2 = new HotBox( 200, 200, Color.BLUE );
      
      HotBox h3 = new HotBox( 400, 50, Color.BLACK );
   }
}