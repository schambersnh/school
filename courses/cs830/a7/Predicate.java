import java.util.ArrayList;
import java.util.Iterator;


/**
 * Predicate- Holds a name and a term list
 * @author Stephen
 *
 */
public class Predicate {
	private String name;
	private boolean sign;
	private ArrayList<Term> termList;
	
	/**
	 * Constructor
	 * @param name
	 * The name of this predicate
	 */
	public Predicate(String newName){
		name = newName;
		if(name.charAt(0) == '-'){
			this.sign = false;
			name = name.substring(1,name.indexOf('('));
		}
		else{
			this.sign = true;
			name = name.substring(0,name.indexOf('('));
		}
		termList = new ArrayList<Term>();
	}
	
	/**
	 * Copy Constructor
	 * @param p
	 */
	public Predicate(Predicate p) {
		termList = new ArrayList<Term>();
		setName(p.getName());
		setSign(p.getSign());
		for(Term t: p.getTermList()){
			Term newTerm = Term.create(t);
			termList.add(newTerm);
		}
	}
	
	/**
	 * Substitutes every occurance of var in this predicate
	 * with c.
	 * @param c
	 * The constant being substituted in
	 * @param var
	 * The variable being replace
	 */
	public void substituteConstant(Constant c, Variable var){
		int index = termList.indexOf(var);
		while(index != -1){
			termList.set(index, c);
			index = termList.indexOf(var);
		}
		for(Term t: termList){
			if(t instanceof Function){
				((Function)t).substituteConstant(c, var);
			}
		}
	}
	public void substituteFunction(Function f, Variable var) {
		int index = termList.indexOf(var);
		while(index != -1){
			termList.set(index, f);
			index = termList.indexOf(var);
		}
		for(Term t: termList){
			if(t instanceof Function){
				((Function)t).substituteFunction(f, var);
			}
		}
	}
	
	public void substituteVariable(Variable var1, Variable var2) {
		if(var1.equals(var2))
			return;
		int index = termList.indexOf(var1);
		while(index != -1){
			termList.set(index, var2);
			index = termList.indexOf(var1);
		}
		for(Term t: termList){
			if(t instanceof Function){
				((Function)t).substituteVariable(var1, var2);
			}
		}		
	}



	/**
	 * Adds a term to this predicate's term list
	 * @param newTerm
	 */
	public void addTerm(Term newTerm){
		termList.add(newTerm);
	}
	
	/**
	 * Returns the term list within this predicate
	 * @return
	 */
	public ArrayList<Term> getTermList(){
		return termList;
	}
	
	/**
	 * Returns true if the predicate is positive, false otherwise
	 * @return
	 */
	public boolean isPositive(){
		return sign;
	}
	
	/**
	 * Get this predicate's name
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set this predicate's name
	 * @return
	 */
	public void setName(String newName){
		name = newName;
	}
	
	/**
	 * Set this predicate's sign
	 * @return
	 */
	public void setSign(boolean newSign){
		sign = newSign;
	}

	/**
	 * Get this predicate's sign
	 * @return
	 */
	public boolean getSign(){
		return sign;
	}

	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		if(!isPositive())
			sb.append("-");
		sb.append(name);
		sb.append("(");
		
		for(Term t: getTermList()){
			sb.append(t);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
}
