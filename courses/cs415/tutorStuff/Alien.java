import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Alien extends ShapeGroup 
{
  //class variables
  public static int width = 36;
  public static int height = 24;
  
  public static int loadMissles = 20;
  
  //instance
  private Point location;
  private Point end;
  private double dx = 5;
  private double dy = 0;
  private boolean isActive = true;
  private Rectangle body;
  
  private int missleStartX, missleStartY; // start location of missle
  
  private int misslesLeft;
  private int gunWidth = 10, gunHeight = 20;
  
  //constructor
  public Alien(int x, int y)
  {
    //instantiate instance variables
    location = new Point(x, y);
    body = new Rectangle();
    body.setColor(Color.ORANGE);
    body.setSize(width, height);
    add(body);
    setLocation(x, y);
  }
  //ADD CONTAINS METHOD FOR LASER CANNON FOR PART 2
  
  //the following method checks if a missle intersected with the alien
  /*public boolean contains()
  {
     
  }*/
  
}