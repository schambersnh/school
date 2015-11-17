import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Resolver- This class takes in a KB and a negated query and resolves
 * @author Stephen
 *
 */

public class Resolver {
	private PriorityQueue<Clause> kb;
	private ArrayList<Clause> sos;
	private HashSet<Clause> sosHash;
	private Unifier unifier;
	private int count;
	private boolean noSolution = false;
	private int numResolutions;
	
	/**
	 * Constructor
	 * @param kb
	 * The knowledge base to perform resolution on
	 * NOTE: Holds set of support as well
	 * @param query
	 * The query we are trying to solve
	 */
	public Resolver(ArrayList<Clause> kb, Clause query, int count){
		this.kb = new PriorityQueue<Clause>();
		this.sos = new ArrayList<Clause>();
		this.count = count;
		this.unifier = new Unifier();
		this.sosHash = new HashSet<Clause>();
		for(Clause c: kb){
			this.kb.add(c);
		}
		this.kb.add(query);
		this.sosHash.add(query);
		this.sos.add(query);
	}
	
	public Clause resolve(boolean wantsAnswer){
		boolean addedClause = false;
		for(Clause sosClause: sos){
			for(Clause kbClause: kb){
				ArrayList<Pair<Integer,Integer>> pairList = unifier.getAllPairs(sosClause, kbClause);
				if(!pairList.isEmpty()){
					ArrayList<Clause> newClauses = unifier.unifyClauses(sosClause, kbClause);
					numResolutions++;
					Pair<Clause,Clause> parent = new Pair<Clause,Clause>(sosClause, kbClause);
					//System.out.println("unifying " + sosClause + " and " + kbClause);
					if(newClauses.isEmpty()){
						/* These two clauses could not be unified */
						continue;
					}
					for(Clause c: newClauses){
						//System.out.println("got a new clause: " + c);
						if(wantsAnswer){
							if(c.getPredicates().size() == 1 && 
									c.getPredicates().get(0).getName().equals("Ans")){
								/* all thats left is the answer literal, return! */
								c.parent = parent;
								return c;
							}
						}
						else{
							if(c.getPredicates().isEmpty()){
								/* Got the empty clause! */
								c.parent = parent;
								return c;
							}
						}
						if(!sosHash.contains(c)){
							c.setNum(count);
							/*System.out.println(sosClause.getNum() + " and " + 
									kbClause.getNum() + " give " + count + ":" + c);*/
							//System.out.println("parent: " + parent.getLeft().getNum() + " and " + parent.getRight().getNum());
							c.parent = parent;
							kb.add(c);
							sos.add(c);
							sosHash.add(c);
							count++;
							addedClause = true;
						}
					}
					if(addedClause){
						return null;
					}
				}
			}
		}
		noSolution = true;
		return null;
	}
	
	public boolean isNoSolution(){
		return noSolution;
	}
	
	public int getNumResolutions(){
		return numResolutions;
	}

	public Stack<Clause> stackify(Clause result, Stack<Clause> retval) {
		if(result.parent == null){
			return retval;
		}
		retval.push(result);
		//System.out.println("left: " + result.parent.getLeft().getNum() + "  " + result.parent.getLeft());
		//System.out.println("right: " + result.parent.getRight().getNum() + "  " + result.parent.getRight());
		stackify(result.parent.getLeft(), retval);
		stackify(result.parent.getRight(), retval);
		return retval;
	}
}
