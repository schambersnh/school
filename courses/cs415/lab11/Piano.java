/*
 * Piano.java
 * Creates a Piano that can be dragged among other features
 * @Stephen Chambers
 * 10/5/10
 * */

import java.awt.Color;
import wheelsunh.users.*;
import java.awt.event.MouseEvent;
import java.awt.Point;

public class Piano extends ShapeGroup implements Draggable
{
  //---------------------------------Instance Variables------------------------
  private Rectangle body, leg1, leg2;
  private Rectangle keys;
  private Color originalColor;
  private Point lastMousePosition;
  private int x = 100;
  private int y = 100;
  private int origx, origy;
  //---------------------------------------------------------------------------
  
   //--------------------------No parameter Constructor-------------------------
   public Piano()
   {
     body = new Rectangle(x, y);
     body.setColor(Color.BLACK);
     body.setSize(x, y-50);
     add(body);
     
     leg1 = new Rectangle(x, y+50);
     leg1.setColor(Color.BLACK);
     leg1.setSize(25, 30);
     add(leg1);
     
     leg2 = new Rectangle(x+75, y+50);
     leg2.setColor(Color.BLACK);
     leg2.setSize(25, 30);
     add(leg2);
     
     keys = new Rectangle(x+5, y+35);
     keys.setColor(Color.WHITE);
     keys.setSize(85, 10);
     add(keys);
   }
   //------Two Parameter Constructor used for Setting Piano's Location----------
      public Piano(int x, int y)
     {
       body = new Rectangle(x, y);
       body.setColor(Color.BLACK);
       body.setSize(100, 50);
       add(body);
     
       leg1 = new Rectangle(x, y+50);
       leg1.setColor(Color.BLACK);
       leg1.setSize(25, 30);
       add(leg1);
     
       leg2 = new Rectangle(x+75, y+50);
       leg2.setColor(Color.BLACK);
       leg2.setSize(25, 30);
       add(leg2);
     
       keys = new Rectangle(x+5, y+35);
       keys.setColor(Color.WHITE);
       keys.setSize(85, 10);
       add(keys);
     }
   //--------------------Sets Color of the Piano--------------------------------
      public void setColor(Color myColor)
      {
        body.setColor(myColor);
      }
  //---------------------Gets Color of the Piano--------------------------------    
      public Color getColor()
      {
        return body.getColor();
      }
  //Saves Location of the mouse position, make sure the original Color is kept,
      //then sets Color to Gray and saves the original location in two IV's
      public void mousePressed(MouseEvent me)
      {
        lastMousePosition = me.getPoint();
        originalColor = getColor();
        setColor(Color.GRAY);
        origx = getXLocation();
        origy = getYLocation();
      }
   //Sets Color to the Color of the Piano before mousePressed(), then creates a
      //new Piano at the original location
      public void mouseReleased(MouseEvent me)
      {
        setColor(originalColor);
        Piano piano4 = new Piano(origx, origy);
        piano4.setColor(originalColor.darker());
      }
   //Drags the Piano Object based on the difference between the current mouse 
        //position and the starting mouse position
      public void mouseDragged(MouseEvent me)
      {
          Point currentPoint = me.getPoint(); 
          int diffX = currentPoint.x - lastMousePosition.x; 
          int diffY = currentPoint.y - lastMousePosition.y; 
          setLocation( getLocation().x + diffX, getLocation().y + diffY ); 
          lastMousePosition = currentPoint;
      }
     
   //---------------------------Testing Main------------------------------------
   public static void main(String [] args)
   {
     Frame f = new Frame();
     Piano piano1 = new Piano();
     Piano piano2 = new Piano(200, 200); 
     Piano piano3 = new Piano(300, 300);
     piano3.setColor(Color.RED);
   }
   
   
   
}//End of Class