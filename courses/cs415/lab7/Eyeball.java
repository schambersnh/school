/* 
 * Create an Eyeball Class with the main testing method being located in lab7.java
 * @author Stephen Chambers
 * 9/21/10
 * */

import wheelsunh.users.*;
import java.awt.Color;

public class Eyeball
{
  ////////////////////Instance Variables//////////////////////////
  Ellipse iris;
  Ellipse ball;
    
 
  Color ballColor = Color.BLUE;
  Color irisColor = Color.WHITE;
  Color frameColor = Color.BLACK;
  
  int ballSizeX = 20;
  int ballSizeY = 20;
  int irisSizeX = 50;
  int irisSizeY = 10;
  int xLocation = 0;
  int yLocation = 0;
  
  public Eyeball()
  {
    // create default eyeball

    iris = new Ellipse(xLocation, yLocation);
    ball = new Ellipse(xLocation+ 15, yLocation + 3);
    iris.setColor(irisColor);
    iris.setFrameColor(frameColor);
    ball.setColor(ballColor);
    iris.setSize(irisSizeX, irisSizeY+ 15);
    ball.setSize(ballSizeX, ballSizeY);
    
    
    
  }
  //Sets Location of the Eyeball
  public void setLocation(int x, int y)
  {
   iris.setLocation(x, y);
   ball.setLocation(x + 15, y+3);
  }
  //Sets Color of the Iris
  public void setColor(Color c)
  {
    iris.setColor(c);
  }
  //Set Color of the pupil to Red
  public void lookMad()
  {
    ball.setColor(Color.RED);
  }
  //Change Eyeball back to normal
  public void lookNormal()
  {
    iris.setColor(irisColor);
    iris.setFrameColor(frameColor);
    ball.setColor(ballColor);
  }
  
 }