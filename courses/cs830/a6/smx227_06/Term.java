import java.util.ArrayList;

/**
 * Term- Can either be a variable, constant, or function
 * @author Stephen
 *
 */
public class Term {
	protected String name;

	
	/**
	 * Constructor
	 * @param name
	 */
	public Term(String name){
		this.name = name;
	}
	
	
	/**
	 * Create a term
	 * @param other
	 * @return
	 */
	public static Term create(Term other){
		if(other instanceof Variable){
			return new Variable(other.getName());
		}
		else if(other instanceof Constant){
			return new Constant(other.getName());
		}
		else{
			return Function.copy((Function)other);
		}
	}
		
	/**
	 * Returns the name of this term
	 */
	public String getName(){
		return name;
	}
		
	/**
	 * Returns the name of this term
	 */
	public void setName(String newName){
		name = newName;
	}
	

	@Override
	public boolean equals(Object obj) {
		Term t = (Term)obj;
		return name.equals(t.getName());
	}

	@Override
	public String toString(){
		return name;
	}

}
