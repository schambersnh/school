/** 
 * Deployable.java:
 * Fall 2009
 * A Deployable object can be sent on a mission:
 * it implements the following methods:
 * 
 *  @author mlb for cs415 8/09
 * Modified (twh 6/10) to remove moveTo, setTimeInterval, and setFuel;
 * and add setNextPoint, move, and setPower.
 */

public interface Deployable
{
    // set the starting point for a new mission
    public void setStartPoint( java.awt.Point p );
    
    // set the initial amount of energy for a deployable object
    public void setPower( double n );
    
    // set the color
    public void setColor( java.awt.Color c ); 
    
    // set the next waypoint for the mission
    public void setNextPoint( java.awt.Point p );
    
    // set the step size for the ATV
    public void setStepSize( double size );
    
    // step toward the next waypoint and return whether the Deployable
    // has reached it
    public boolean move();
}