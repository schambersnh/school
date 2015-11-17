/**
 * Variable- Holds a name
 * @author Stephen
 *
 */
public class Variable extends Term {

	/**
	 * Constructor
	 * @param name
	 * The name of this variable
	 */
	public Variable(String name) {
		super(name);
	}
	
	/**
	 * Copy constructor
	 * @param other
	 */
	public Variable(Variable other) {
		super(other.getName());
	}

}
