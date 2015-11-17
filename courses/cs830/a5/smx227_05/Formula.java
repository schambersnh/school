import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Holds clauses
 * @author Stephen
 *
 */
public class Formula {
	private ArrayList<Clause> clauses;
	private HashSet<Integer> finalVars;
	
	/**
	 * Constructor
	 */
	public Formula(){
		clauses = new ArrayList<Clause>();
		finalVars = new HashSet<Integer>();
	}
	
	/**
	 * Copy constructor
	 * @param f
	 */
	public Formula(Formula f){
		this();
		for(Clause c: f.getClauses()){
			Clause temp = new Clause();
			int varSize = c.getVariables().size();
			for(int i = 0; i < varSize; i++){
				Integer newVar = new Integer(c.getVariables().get(i));
				temp.addVariable(newVar);
			}
			this.clauses.add(temp);
		}
		this.getFinalVars().addAll(f.getFinalVars());
	}
	
	/**
	 * Adds a clause to this formula
	 * @param c the clause to add
	 */
	public void addClause(Clause c){
		clauses.add(c);
	}
	
	/**
	 * Set all clauses to unsatisfied
	 */
	public void clearClauses(){
		for(Clause c: clauses){
			c.setSatisfiedVar(0);
		}
	}
	
	/**
	 * Get this formulas clauses
	 * @return
	 */
	public ArrayList<Clause> getClauses(){
		return clauses;
	}
	
	/**
	 * Return if this formula has no clauses
	 * @return
	 */
	public boolean isEmpty(){
		return clauses.isEmpty();
	}
	
	/**
	 * Return if this formula has an empty clause
	 * @return
	 */
	public boolean hasEmptyClause(){
		for(Clause c: clauses){
			if(c.isEmpty())
				return true;
		}
		return false;
	}
	
	/**
	 * Sets the variable in the formula
	 * @param val the variable to set
	 * @param newValue the value of the variable (1 for true, -1 for false)
	 */
	public void setVariable(int val, int newValue){
		int newVariable = 0;
		int notVariable = 0;
		if(val < 0 && newValue == -1){
			newVariable = val;
		}
		else if(val < 0 && newValue == 1){
			newVariable = val * -1;
		}
		else{
			newVariable = val * newValue;
		}
		notVariable = -newVariable;
		for(Clause c: clauses){
			int varSize = c.getVariables().size();
			for(int i = 0; i < varSize; i++){
				int var = c.getVariables().get(i);
				if(var == newVariable){
					c.setSatisfiedVar(var);
					break;
				}
				else if(c.isSatisfied() && c.getSatisfiedVar() == notVariable){
					c.setSatisfiedVar(0);
				}
			}
		}
	}
	
	/**
	 * Gets the "flip value" of setting this var to newValue.
	 * If setting var to newValue resulted in breaking two expressions
	 * and fixing one, the retval would be -1.
	 * @param val the variable to set
	 * @param newValue the value of the variable (1 for true, -1 for false)
	 */
	public int getFlipValue(int val, int newValue){
		int newVariable = 0;
		int notVariable = 0;
		int flipVal = 0;
		if(val < 0 && newValue == -1){
			newVariable = val;
		}
		else if(val < 0 && newValue == 1){
			newVariable = val * -1;
		}
		else{
			newVariable = val * newValue;
		}
		notVariable = -newVariable;
		for(Clause c: clauses){
			int varSize = c.getVariables().size();
			for(int i = 0; i < varSize; i++){
				int var = c.getVariables().get(i);
				if(var == newVariable){
					flipVal++;
					break;
				}
				else if(c.isSatisfied() && c.getSatisfiedVar() == notVariable){
					flipVal--;
				}
			}
		}
		return flipVal;
	}
	
	/**
	 * Returns true if flipping this variable breaks nothing, false otherwise
	 * @param val
	 * @param newValue
	 * @return
	 */
	public boolean isPositiveFlip(int val, int newValue){
		int newVariable = 0;
		int notVariable = 0;
		if(val < 0 && newValue == -1){
			newVariable = val;
		}
		else if(val < 0 && newValue == 1){
			newVariable = val * -1;
		}
		else{
			newVariable = val * newValue;
		}
		notVariable = -newVariable;
		for(Clause c: clauses){
			int varSize = c.getVariables().size();
			for(int i = 0; i < varSize; i++){
				if(c.isSatisfied() && c.getSatisfiedVar() == notVariable){
					return false;
				}
			}
		}
		return true;
	}

	
	/**
	 * Goes through every variable in c, and if the flip value did not break anything,
	 * add it to retval.
	 * @param val the variable to set
	 * @param newValue the value of the variable (1 for true, -1 for false)
	 */
	public ArrayList<Integer> getPositiveFlips(Clause c){
		ArrayList<Integer> retval = new ArrayList<Integer>();
		int varSize = c.getVariables().size();
		for(int i = 0; i < varSize; i++){
			int var = c.getVariables().get(i);
			int newValue = (var < 0) ? -1: 1;
			if(isPositiveFlip(var, newValue))
				retval.add(var);
		}
		return retval;
	}


	
	/**
	 * Get all unsatisfied clauses
	 * @return
	 */
	public ArrayList<Clause> getUnsatisfied(){
		ArrayList<Clause> retval = new ArrayList<Clause>();
		for(Clause c: clauses){
			if(!c.isSatisfied()){
				retval.add(c);
			}
		}
		return retval;
	}
	
	/**
	 * Remove clauses where variable v has value newValue.
	 * Also remove variables from clauses where it has !newValue.
	 */
	public void reduce(int val, int newValue){
		int newVariable = 0;
		int notVariable = 0;
		if(val < 0 && newValue == -1){
			newVariable = val;
		}
		else if(val < 0 && newValue == 1){
			newVariable = val * -1;
		}
		else{
			newVariable = val * newValue;
		}
		notVariable = -newVariable;
		
		Iterator<Clause> iter = clauses.iterator();
		while (iter.hasNext()) {
		    Clause c = iter.next();
			for(int i = 0; i < c.getVariables().size(); i++){
				int var = c.getVariables().get(i);
				if(var == newVariable){
					/* remove this clause */					
					finalVars.add(newVariable);
					iter.remove();
					break;
				}
				else if(var == notVariable){
					c.getVariables().remove(i);
					i--;
				}
			}
		}
	}
	
	/**
	 * Returns the next variable to check
	 * @return
	 */
	public int getNextVariable(){
		return clauses.get(0).getVariables().get(0);
	}
	
	/**
	 * Returns the next variable to check
	 * @return
	 */
	public HashSet<Integer> getFinalVars(){
		return finalVars;
	}



	
	/**
	 * If the fomula contains a unit clause, return a list of unit clauses
	 * @return
	 */
	public ArrayList<Clause> getUnits(){
		ArrayList<Clause> retval = new ArrayList<Clause>();
		for(Clause c: clauses){
			if(c.isUnit())
				retval.add(c);
		}
		return retval;
	}
}
