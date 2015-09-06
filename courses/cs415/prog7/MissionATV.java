import java.awt.Point;
import java.awt.Color;
import wheelsunh.users.*;

public class MissionATV extends ATV implements Deployable
{
  //----------------------------Instance Variables----------------------------
  private double power= 100.0, dist;
  private double x, y, currentX, currentY;
  private double step;
  private Point startPoint, endPoint, tempPoint;
  private double deltaX, deltaY;
  private Line line1;
  
  
  public MissionATV(int x, int y)
  {
    super(x, y);
  }
  // set the starting point for a new mission
  public void setStartPoint( java.awt.Point p )
  {
    startPoint = p;
    setLocation((int)startPoint.x, (int)startPoint.y);
  }
  
  // set the initial amount of energy for a deployable object
  public void setPower( double n )
  {
    power = n;
  }
  
  // set the next waypoint for the mission
  public void setNextPoint( java.awt.Point p )
  {
    endPoint = p;
    deltaX = endPoint.x - startPoint.x;
    deltaY = endPoint.y - startPoint.y;
    currentX = getXLocation();
    currentY = getYLocation();
    Display disp = new Display(10, 50, power);
     //Calculate the Distance to the Next Waypoint
    dist = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaY, 2.0));
    System.out.println(dist);
  }
  
  // set the step size for the ATV
  public void setStepSize( double size )
  {
    step = size;
  }
   public void setStepSize( int size )
  {
   
  }
  
  // step toward the next waypoint and return whether the Deployable
  // has reached it
  public boolean move()
  {
    
    //If out of power, change the ATV to white and end the mission
    if ( power <= 0 )
    {  
      setColor(Color.WHITE);
      return false;
    }
    else
    {
      //move one step
      
      if ( dist >= step ) // still a long way to go
      {
        
        
        
        x = currentX + (step/dist) * (deltaX);
        y = currentY + (step/dist) *  (deltaY);
        setLocation((int)x, (int)y);
        
        currentX = getXLocation();
        currentY = getYLocation();
        
        
        
        if ( deltaY <= 0 ) //uphill
        {
          //Draw a Line from the Start Point to the End Point
          line1 = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
          line1.setColor(Color.RED);
          System.out.println("Subtratcting Power");
          power += deltaY;
        }
        else //downhill
        {
          //Draw a Line from the Start Point to the End Point
          line1 = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
          line1.setColor(Color.GREEN);
          System.out.println("Adding power");
          power -= (1.0/4.0)* (deltaY);
        }
        
        dist -= step;
        System.out.println("New Distance: " + dist);
        
        //disp.setValue(power);
        System.out.println("X: " + x + "\nY: " + y + "\nPower: " + power);
        
        return false;
        
      }
      else
      {
        setLocation(endPoint);
        setPower(100.0);
        return true; 
      }
      
    } 
    
  }
  
}
