/**
 * Jumper - 
 * 
 * @author Stephen Chambers
 * 10/19/10
 * derived from the Cockroach class by mlb
 * 
 * History 
 * 10/19/09 rdb Jumper - also generates random color
 *       
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.util.*;

public class Jumper extends Ellipse
{   
  //-------------- instance variables -----------------
  private Random  rng        = null;
  public static int count;
  
  //------------------- Constructor -------------------
  public Jumper()
  {       
    rng = new Random();
    int height = rng.nextInt( 15 ) + 10;
    int width  = height * 3;
    
    if (height % 3 == 0)
    {
      setColor(Color.BLACK);
    }
    else if (height % 3 == 1)
    {
      setColor(Color.BLUE);
    }
    else if (height % 3 == 2)
    {
      setColor(Color.GREEN);
    }
    
    setSize( width, height );
    setRotation( rng.nextInt( 360 ));    
    jump();     
  }
  
  //----------------------- jump() ---------------------------
  public void jump()
  {
    int x = rng.nextInt( 680 ); 
    int y = rng.nextInt( 480 )  ; 
    setLocation( x,  y );
  }
  
  //-------------------mousePressed( MouseEvent  --------------
  public void mousePressed( java.awt.event.MouseEvent e )
  {
    Jumper child = null;
    
    if(getColor() == Color.BLACK)
    {
      setColor(Color.BLUE);
      jump();
      child = new Jumper();
    }
    
    else if(getColor() == Color.BLUE)
    {
      setColor(Color.GREEN);
      jump();
    }
    
    else if(getColor() == Color.GREEN && count < 10)
    {
      setColor(Color.BLACK);
      jump();
    }
    
    else
    {
      setColor(Color.BLACK);
    }
    
    jump();  
    count();
    System.out.println( "------------  roaches! ----- " );
  } 
  
  public void count()
  {
    count++;
    System.out.println("Count: " + count);  
  } 
  //----------------------- main ---------------------------
  public static void main ( String[] argv )     
  {
    Frame m = new Frame();
    Jumper cartoon = new Jumper();
  }   
} //End of Class
