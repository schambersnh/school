/** 
 * Cockroach.java
 * Creates a Cockroach
 * 
 * @Stephen Chambers
 * 10/7/10
 * 
 **/

import java.util.Random;
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class Cockroach extends Ellipse
{
  //--------------------------Instance Variables---------------------------
  private int height, xloc, yloc, rot, x, y;
  Random randX = new Random();
  Random randY = new Random();
  Random randH = new Random();
  Random randR = new Random();
  
  public Cockroach( )
  {
    xloc =randX.nextInt(700);
    yloc = randY.nextInt(500);
    height = randH.nextInt(20-10) + 10;
    rot = randR.nextInt(360);
    
    setLocation(xloc, yloc);
    setSize(3*height, height);
    setRotation(rot);
    setColor(Color.BLACK);
  }
  
  public void mousePressed(MouseEvent e)
  {
    xloc =randX.nextInt(700);
    yloc = randY.nextInt(500);
    setLocation(xloc, yloc);
    Cockroach randCock = new Cockroach();
    x = e.getPoint().x;
    y = e.getPoint().y;
    
    System.out.println("Mouse Event Cooardinates: " + x + ", " + y);
    System.out.println("New Cockroach Location: " + randCock.getXLocation() +
                       ", " + randCock.getYLocation());
    System.out.println("Cockroach Size: " + randCock.getSize());
    System.out.println("Former Cockroach's new location: " + getXLocation() +
                       ", " + getYLocation());
    
  }
  
  public static void main(String [] args)
  {
    Frame f = new Frame();
    Cockroach cock1 = new Cockroach();
  }
  
}
