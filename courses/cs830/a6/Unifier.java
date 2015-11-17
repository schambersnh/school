import java.util.ArrayList;


public class Unifier {
	
	/**
	 * Constructor
	 */
	public Unifier(){
		
	}
	
	/**
	 * This is a little non-intuitive. This functions grabs pairs of 
	 * integers from the "getAllPairs(Clause,Clause) function.
	 * These integers represent the index into the clause
	 * where that predicate is located. So the left of the pair
	 * is the index of that predicate in c1, and the
	 * right of the pair is the index of that predicate in c2.
	 * @param c1
	 * The first clause to unify
	 * @param c2
	 * The second clause to unify
	 * @return
	 * A list of all unified clauses
	 */
	public ArrayList<Pair<Clause,Clause>> unifyClauses(Clause c1, Clause c2){
		ArrayList<Pair<Clause,Clause>> retval = new ArrayList<Pair<Clause,Clause>>();
		
		/* get a list of predicate pairs to unify */
		ArrayList<Pair<Integer,Integer>> pairList = getAllPairs(c1, c2);
		
		for(Pair<Integer,Integer> pair: pairList){
			Clause copy1 = new Clause(c1);
			Clause copy2 = new Clause(c2);
			
			int numTerms = copy1.getPredicates().get(pair.getLeft()).getTermList().size();
			boolean unifiable = true;
			for(int i = 0; i < numTerms; i++){
				/* Grab a term pair */
				Term t1 = copy1.getPredicates().get(pair.getLeft()).getTermList().get(i); 
				Term t2 = copy2.getPredicates().get(pair.getRight()).getTermList().get(i); 
				if(!unifyTerm(copy1, copy2, t1, t2)){
					unifiable = false;
					break;
				}
			}
			if(unifiable)
				retval.add(new Pair<Clause,Clause>(copy1, copy2));
		}
		return retval;
	}
	
	public boolean unifyTerm(Clause c1, Clause c2, Term t1, Term t2){
		/*if t1 and t2 are different constants, it is not unifiable
		 * If they are the same, then continue to the next pair */
		if(t1 instanceof Constant && t2 instanceof Constant){
			return t1.equals(t2);
		}
		/* if one is a function and the other is a constant, it is not unifiable */
		if(t1 instanceof Constant && t2 instanceof Function ||
			t2 instanceof Constant && t1 instanceof Function){
			return false;
		}
		/* if t1 is a constant and t2 is a variable, subsitute t1 for t2. */
		if(t1 instanceof Constant && t2 instanceof Variable){
			c2.substituteConstant((Constant)t1, (Variable)t2);
			c1.substituteConstant((Constant)t1, (Variable)t2);
		}
		/* if t2 is a constant and t1 is a variable, subsitute t2 for t1. */
		if(t2 instanceof Constant && t1 instanceof Variable){
			c1.substituteConstant((Constant)t2, (Variable)t1);
			c2.substituteConstant((Constant)t2, (Variable)t1);
		}
		/* if t1 are different functions, it is not unifiable
		 * If they are the same, unify their argument lists */
		if(t1 instanceof Function && t2 instanceof Function){
			if(!t1.equals(t2)){
				return false;
			}
			else{
				int funcTermSize = ((Function) t1).getTermList().size();
				for(int i = 0; i < funcTermSize; i++){
					Term tFunc1 = ((Function) t1).getTermList().get(i);
					Term tFunc2 = ((Function) t2).getTermList().get(i);
					if(!unifyTerm(c1, c2, tFunc1, tFunc2)){
						return false;
					}
				}
			}
		}
		/* if var occurs in function, it is not unifiable */
		if(t1 instanceof Function && t2 instanceof Variable){
			/*System.out.println("t1: " + t1.getName());
			System.out.println("t2: " + t2.getName());*/
			if(((Function) t1).containsVar((Variable)t2)){
				return false;
			}
			else{
				/**Substitute function(t1) for var(t2)*/
				c2.substituteFunction((Function)t1, (Variable)t2);
			}
		}
		/* if var occurs in function, it is not unifiable */
		if(t2 instanceof Function && t1 instanceof Variable){
			if(((Function) t2).containsVar((Variable)t1)){
				return false;
			}
			else{
				/**Substitute function(t2) for var(t1)*/
				c1.substituteFunction((Function)t2, (Variable)t1);
			}
		}
		/* if both are variables, substitute one variable for the other */
		if(t1 instanceof Variable && t2 instanceof Variable){
			c1.substituteVariable((Variable)t1, (Variable)t2);
			c2.substituteVariable((Variable)t1, (Variable)t2);
		}
		return true;
	}
	/**
	 * Return a list of predicate pair indexes in c1 and c2
	 * @param c1
	 * The first clause
	 * @param c2
	 * The second clause
	 * @return
	 * A list of valid predicate pairs that could possibly be unified
	 */
	public ArrayList<Pair<Integer,Integer>> getAllPairs(Clause c1, Clause c2){
		ArrayList<Pair<Integer,Integer>> pairList = new ArrayList<Pair<Integer,Integer>>();
		for(int i = 0; i < c1.getPredicates().size(); i++){
			for(int j = 0; j < c2.getPredicates().size(); j++){
				Predicate p1 = c1.getPredicates().get(i);
				Predicate p2 = c2.getPredicates().get(j);
				if(isPair(p1,p2)){
					pairList.add(new Pair<Integer,Integer>(i,j));
				}
			}
		}
		return pairList;
	}
	
	/**
	 * Returns if p1 and p2 are pairs. 
	 * A pair is defined as the same predicate name appearing
	 * in both clauses with opposite signs.
	 * NOTE: if two predicates have term lists of differing size,
	 * they can never be unified. Therefore, they are not included
	 * in the return value.
	 * @param p1
	 * @param p2
	 * @return
	 */
	private boolean isPair(Predicate p1, Predicate p2){
		return p1.getName().equals(p2.getName()) && p1.getSign() == !p2.getSign()
				&& p1.getTermList().size() == p2.getTermList().size();
	}

}
