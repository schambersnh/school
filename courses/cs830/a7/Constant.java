/**
 * Constant- holds a constant
 * @author Stephen
 *
 */
public class Constant extends Term{

	/**
	 * Constructor
	 * @param name
	 * The name of this constant
	 */
	public Constant(String name) {
		super(name);
	}
	
	/**
	 * Copy constructor
	 * @param other
	 */
	public Constant(Constant other){
		super(other.getName());
	}
}
