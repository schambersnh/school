import java.util.ArrayList;
import java.util.HashSet;

/**
 * The action class holds a list of preconditions and add delete effects
 * @author Stephen
 *
 */
public class Action {
	private Predicate action;
	private ArrayList<Predicate> preconditions;
	private ArrayList<Predicate> addList;
	private ArrayList<Predicate> deleteList;
	
	public Action(Predicate action){
		this.action = action;
		preconditions = new ArrayList<Predicate>();
		addList = new ArrayList<Predicate>();
		deleteList = new ArrayList<Predicate>();
	}
	
	public Action(Action copy){
		preconditions = new ArrayList<Predicate>();
		addList = new ArrayList<Predicate>();
		deleteList = new ArrayList<Predicate>();
		action = new Predicate(copy.getAction());
		for(Predicate p: copy.getPreconditions()){
			Predicate temp = new Predicate(p);
			preconditions.add(temp);
		}
		for(Predicate del: copy.getDeleteList()){
			Predicate temp = new Predicate(del);
			deleteList.add(temp);
		}
		for(Predicate add: copy.getAddList()){
			Predicate temp = new Predicate(add);
			addList.add(temp);
		}
	}
	
	/**
	 * 
	 * @return the action clause
	 */
	public Predicate getAction(){
		return action;
	}

	/**
	 * @return the preconditions
	 */
	public ArrayList<Predicate> getPreconditions() {
		return preconditions;
	}
	
	public ArrayList<Predicate> getTruePreconditions(){
		ArrayList<Predicate> truePreds = new ArrayList<Predicate>();
		for(Predicate p: preconditions){
			if(p.getSign())
				truePreds.add(p);
		}
		return truePreds;
	}

	/**
	 * @return the addList
	 */
	public ArrayList<Predicate> getAddList() {
		return addList;
	}

	/**
	 * @return the deleteList
	 */
	public ArrayList<Predicate> getDeleteList() {
		return deleteList;
	}
	
	/**
	 * Add a precondition
	 * @param pre
	 */
	public void addPrecondition(Predicate pre){
		pre.setSign(true);
		preconditions.add(pre);
	}
	
	/**
	 * Add a negative precondition
	 * @param pre
	 */
	public void addNegPrecondition(Predicate pre){
		pre.setSign(false);
		preconditions.add(pre);
	}
	
	/**
	 * Add to the add list
	 * @param pre
	 */
	public void addToAddList(Predicate pre){
		addList.add(pre);
	}
	
	/**
	 * Add to the delete list
	 * @param pre
	 */
	public void addToDeleteList(Predicate pre){
		deleteList.add(pre);
	}

	public void substituteConstant(Constant c, Variable var) {
		//System.out.println("subsituting " + c + " for " + var);
		action.substituteConstant(c, var);
		//System.out.println(action);
		for(Predicate preCond: preconditions){
			preCond.substituteConstant(c, var);
		}
		for(Predicate addP: addList){
			addP.substituteConstant(c, var);
		}
		for(Predicate delP: deleteList){
			delP.substituteConstant(c, var);
		}
		/*System.out.println("pres: " + preconditions);
		System.out.println("add: " + addList);
		System.out.println("del: " + deleteList);*/
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.action.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		Action other = (Action)obj;
		return this.action.equals(other.getAction());
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(action);
//		sb.append("pre: ");
//		sb.append(preconditions);
//		sb.append("add: ");
//		sb.append(addList);
//		sb.append("del: ");
//		sb.append(deleteList);
		return sb.toString();
	}
}
