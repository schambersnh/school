/**
 * A control class simply keeps track of a direction and a magnitude
 * @author Stephen
 *
 */
public class Control {
	private double _direction;
	private double _magnitude;
	
	public Control(double direction, double magnitude){
		_direction = direction;
		_magnitude = magnitude;
	}

	/**
	 * @return the direction
	 */
	public double getDirection() {
		return _direction;
	}

	/**
	 * @return the magnitude
	 */
	public double getMagnitude() {
		return _magnitude;
	}

	/**
	 * @param direction the direction to set
	 */
	public void set_direction(double direction) {
		_direction = direction;
	}

	/**
	 * @param magnitude the magnitude to set
	 */
	public void set_magnitude(double magnitude) {
		_magnitude = magnitude;
	}
	
}
