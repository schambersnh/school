import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This class represents the current world state through a 2D char array.
 * @author Stephen
 *
 */
public class State {
	private Point2D _curLocation;
	private double _theta;
	private double _speed;
	private Control _control;
	public State parent;
	
	/**
	 * Default state constructor
	 */
	public State() {
		parent = null;
		_curLocation = new Point2D.Double(0,0);
	}

	
	/**
	 * State constructor with theta and speed included
	 * @param curLocation
	 * The current location of the robot
	 * @param theta
	 * The direction of the robot
	 * @param speed
	 * The speed of the robot
	 */
	public State(Point2D curLocation, double theta, double speed){
		parent = null;
		_curLocation = new Point2D.Double(curLocation.getX(), curLocation.getY());
		_theta = theta;
		_speed = speed;
	}
	
	/**
	 * Get the current angle of the robot
	 * @return
	 */
	public double getTheta() {
		return _theta;
	}
	
	/**
	 * Set the current angle of the robot
	 * @param theta
	 * The new angle
	 */
	public void setTheta(double theta) {
		_theta = theta;
	}
	
	/**
	 * Get the current speed of the robot
	 * @return
	 */
	public double getSpeed() {
		return _speed;
	}

	/**
	 * Set the current speed of the robot
	 * @param speed
	 * The new speed
	 */
	public void setSpeed(double speed) {
		_speed = speed;
	}


	/**
	 * Gets the current location of the robot
	 * @return
	 */
	public Point2D getLocation() {
		return _curLocation;
	}
	
	/**
	 * Sets the current location of the robot
	 * @param _curLocation
	 */
	public void setLocation(Point2D location) {
		_curLocation = location;
	}
	
	/**
	 * Set the control of this state (i.e. its new direction and magnitude)
	 * @param theta
	 * The new angle of the robot
	 * @param magnitude
	 * The new acceleration of the robot
	 */
	public void setControl(Control newControl){
		_control = newControl;
	}
	
	/**
	 * Get the control of a state
	 * @return
	 */
	public Control getControl(){
		return _control;
	}
}
