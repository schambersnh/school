import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;


/** 
 *  PumpingCircle.java:
 * Creates a Circle that shrinks until the diameter is 50, then grows until the 
 * diameter is back to 100
 * @Stephen Chambers
 * 10/23/10
 */
public class PumpingCircle extends Ellipse
{ 
  //----------------- instance variables --------------------------------------
  
  private int mySize = 100; 
  private boolean growing = false;
  private int inc = 10, count = 5;
  
  //------------------ constructor ---------------------------------------------
  public PumpingCircle(  int x, int y ) 
  {      
    super( x, y );   
    setSize( mySize, mySize );  
  }
  
  
  
  //------------------------------------------------------------------------------
  //
  public void mouseClicked( MouseEvent me )
  {
    ////////////////////////////////////////////////////////////////////////
    // circle should grow by 10 pixels if it is in the growing  state
    // if not in the growing state then it should shrink by 10 pixels
    
    // initially it is in the shrinking state
    // then, if the size is 50 or less it should change to the growing state
    // then, if the size is 100 or greater it should change to the shrinking state
    ///////////////////////////////////////////////////////////////////////
    if((getWidth() == 100) && (getHeight() == 100))
    {
      growing = false;
    }
    else if ((getWidth() == 50) && (getHeight() == 50 ))
    {
      growing = true;
    }
    if (growing)
    {
      grow();
    } 
    else
    {
      shrink();
    } 
  }
  
  //Shrinks Rectangle
  public void shrink()
  {
    setSize(getWidth() - inc, getHeight() - inc);
  }
  //Enlarges Rectangle
  public void grow()
  {
    setSize(getWidth() + inc, getHeight() +inc);
  }
  
  
  
  
  //---------------------------- main -----------------------------------------
  public static void main( String[] args )      
  {
    new Frame();
    PumpingCircle p = new PumpingCircle( 325, 225 );
    p.setSize( 100 , 100 );
  }
} //End of Class