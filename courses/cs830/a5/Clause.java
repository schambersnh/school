import java.util.ArrayList;

/**
 * Holds variables
 * @author Stephen
 *
 */
public class Clause {
    private ArrayList<Integer> variables;
    private int satisfiedVariable = 0;
    
    /**
     * Constructor
     */
    public Clause(){
    	variables = new ArrayList<Integer>();
    }
    
    /**
     * Adds a variable to this clause
     */
    public void addVariable(int var){
    	variables.add(var);
    }
    
	/**
	 * Get this clauses variables
	 * @return
	 */
	public ArrayList<Integer> getVariables(){
		return variables;
	}
	
	/**
	 * Return if this clause has no variables
	 * @return
	 */
	public boolean isEmpty(){
		return variables.isEmpty();
	}
	
	@Override
	public String toString(){
		return variables.toString();
	}

    
    /**
	 * @return the satisfied
	 */
	public boolean isSatisfied() {
		return satisfiedVariable != 0;
	}

	/**
	 * @param satisfied the satisfied to set
	 */
	public void setSatisfiedVar(int satisfied) {
		this.satisfiedVariable = satisfied;
	}
	
	/**
	 * Get satisfied var
	 */
	public int getSatisfiedVar() {
		return satisfiedVariable;
	}


	/**
     * Check if this clause is a unit clause
     * @return
     */
    public boolean isUnit(){
    	return variables.size() == 1;
    }
}
