import java.util.ArrayList;

/**
 * Function- holds a name and a term list
 * @author Stephen
 *
 */
public class Function extends Term{
	private ArrayList<Term> termList;
	
	/**
	 * Constructor
	 * @param newName
	 * The name of this function
	 */
	public Function(String newName) {
		super(newName);
		termList = new ArrayList<Term>();
		if(name.indexOf('(') != -1){
			name = name.substring(0, name.indexOf('('));	
		}
	}
	
	/**
	 * Copy Constructor
	 * @param newName
	 * The name of this function
	 */
	public static Function copy(Function other) {
		Function newFunc = new Function(other.getName());
		for(Term t: other.getTermList()){
			Term t2 = Term.create(t);
			newFunc.addTerm(t2);
		}
		return newFunc;
	}
	
	/**
	 * Adds a term to this function
	 * @param newTerm
	 */
	public void addTerm(Term newTerm){
		termList.add(newTerm);
	}
	
	/**
	 * Returns the term list of this function
	 * @return
	 */
	public ArrayList<Term> getTermList(){
		return termList;
	}

	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
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

	public void substituteConstant(Constant c, Variable var) {
		int index = termList.indexOf(var);
		while(index != -1){
			termList.set(index, c);
			index = termList.indexOf(var);
		}
		for(Term t: termList){
			if(t instanceof Function){
				((Function) t).substituteConstant(c, var);
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
				((Function) t).substituteFunction(f, var);
			}
		}		
	}
	
	public boolean containsVar(Variable var){
		int index = termList.indexOf(var);
		if(index != -1){
			return true;
		}
		for(Term t: termList){
			if(t instanceof Function){
				return ((Function) t).containsVar(var);
			}
		}	
		return false;
	}

	public void substituteVariable(Variable var1, Variable var2) {
		int index = termList.indexOf(var1);
		while(index != -1){
			termList.set(index, var2);
			index = termList.indexOf(var1);
		}
		for(Term t: termList){
			if(t instanceof Function){
				((Function) t).substituteVariable(var1, var2);
			}
		}				
	}	
	
}
