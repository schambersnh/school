import java.util.ArrayList;


/**
 * Clause- holds a list of literals
 * @author Stephen
 *
 */
public class Clause implements Comparable{
	private ArrayList<Predicate> predicates;
	public Pair<Clause,Clause> parent;
	private int num;
	
	/**
	 * Constructor
	 */
	public Clause(){
		predicates = new ArrayList<Predicate>();
	}
	
	/**
	 * Copy Constructor
	 */
	public Clause(Clause c){
		this();
		for(Predicate p: c.getPredicates()){
			Predicate newPred = new Predicate(p);
			predicates.add(newPred);
		}
	}
	
	/**
	 * Adds a predicate
	 * @param newPredicate
	 * The predicate to add
	 */
	public void addPredicate(Predicate newPredicate){
		predicates.add(newPredicate);
	}
	
	/**
	 * Returns this clause's predicates
	 * @return
	 */
	public ArrayList<Predicate> getPredicates(){
		return predicates;
	}
	
	/**
	 * Substitutes every occurance of var in this clause
	 * with c.
	 * @param c
	 * The constant being substituted in
	 * @param var
	 * The variable being replace
	 */
	public void substituteConstant(Constant c, Variable var){
		for(Predicate p: predicates){
			p.substituteConstant(c, var);
		}
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(Predicate p: predicates){
			sb.append(p);
			sb.append(" | ");
		}
		if(sb.length() > 0)
			sb.deleteCharAt(sb.length() - 2);
		else
			sb.append("<empty>");
		return sb.toString();
	}

	public void substituteFunction(Function f, Variable var) {
		for(Predicate p: predicates){
			p.substituteFunction(f, var);
		}		
	}

	public void substituteVariable(Variable var1, Variable var2) {
		for(Predicate p: predicates){
			p.substituteVariable(var1, var2);
		}				
	}
	
	public void setNum(int num){
		this.num = num;
	}
	
	public int getNum(){
		return this.num;
	}
	
	@Override
	public int hashCode(){
		int retval = 0;
		for(Predicate p: predicates){
			retval += p.getName().hashCode();
		}
		return retval;
	}
	
	@Override
	public boolean equals(Object o){
		Clause other = (Clause)o;
		return this.toString().equals(other.toString());
	}
	
	public void removePredicate(int index){
		predicates.remove(index);
	}

	@Override
	public int compareTo(Object o) {
		Clause other = (Clause)o;
		return Integer.compare(this.getPredicates().size(), other.getPredicates().size());
	}
}
