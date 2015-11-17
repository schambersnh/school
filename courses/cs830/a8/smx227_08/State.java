import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class represents the current world state through a 2D char array.
 * @author Stephen
 *
 */
public class State {
	private HashSet<Predicate> predicates;
	private ProblemInfo info;
	private ArrayList<Action> actionsSoFar;
	
	public State()
	{
		info = ProblemInfo.getInstance();
		predicates = new HashSet<Predicate>();
		actionsSoFar = new ArrayList<Action>();
	}
	
	public State(State copy){
		this();
		for(Predicate stateLiteral: copy.getPredicates()){
			Predicate p = new Predicate(stateLiteral);
			predicates.add(p);
		}
		for(Action a: copy.getActionsSoFar()){
			Action newAction = new Action(a);
			actionsSoFar.add(newAction);
		}
	}
			
	public ArrayList<Action> getActionsSoFar() {
		return actionsSoFar;
	}

	public ArrayList<State> getChildren(){
		ArrayList<State> children = new ArrayList<State>();
		HashSet<Action> applicableActions = getApplicableActions();
		//System.out.println("applicable actions: " + applicableActions);
		for(Action action: applicableActions){
			State s = new State(this);
			for(Predicate del: action.getDeleteList()){
				/* slight hack to remove the true version from
				 * the hash set and add the false one 
				 */
				del.setSign(true);
				s.getPredicates().remove(del);
				del.setSign(false);
				s.getPredicates().add(del);
			}
			for(Predicate add: action.getAddList()){
				add.setSign(false);
				s.getPredicates().remove(add);
				add.setSign(true);
				s.getPredicates().add(add);
			}
			s.takeAction(action);
			children.add(s);
		}
		//System.out.println("children: " + children);
		return children;
	}
	
	private void takeAction(Action action) {
		actionsSoFar.add(action);
		
	}

	public HashSet<Predicate> getPredicates(){
		return predicates;
	}
	
	public HashSet<Predicate> getTruePredicates(){
		HashSet<Predicate> truePreds = new HashSet<Predicate>();
		for(Predicate p: predicates){
			if(p.getSign())
				truePreds.add(p);
		}
		return truePreds;
	}
	
	
	public void addPredicate(Predicate pre){
		predicates.add(pre);
	}
	
	public void addNegPredicate(Predicate pre){
		pre.setSign(false);
		predicates.add(pre);
	}

	
	public HashSet<Action> getApplicableActions(){
//		System.out.println("initial state: " + this);
//		System.out.println("getting applicable actions for this state");
		HashSet<Action> possibleActions = new HashSet<Action>();
		for(Predicate stateLiteral: predicates){
			if(info.getGroundList().get(stateLiteral) != null)
				possibleActions.addAll(info.getGroundList().get(stateLiteral));
		}
		/* add the null list to possible actions */
		possibleActions.addAll(info.getGroundList().get(null));
		Iterator<Action> iter = possibleActions.iterator();
		while(iter.hasNext()){
			Action a = iter.next();
			if(!this.getTruePredicates().containsAll(a.getTruePreconditions())){
				iter.remove();
			}
		}
		return possibleActions;
	}
	
	public void updateInfo(){
		info = ProblemInfo.getInstance();
	}
	
	public String toString(){
		return predicates.toString();
	}
	
	  
  /**
   * 
   */
  public int calcHScore(){
	  if(info.getHeuristic().equals("h0")){
		  return 0;
	  }
	  else if(info.getHeuristic().equals("h-goal-lits")){
		  int val = info.getGoal().getPredicates().size();
		  for(Predicate p : predicates){
			  if(info.getGoal().getPredicates().contains(p)){
				  val--;
			  }
		  }
		  return val;
		  
	  }
	  else if(info.getHeuristic().equals("h1")){
		  /* max */
		  HashSet<Predicate> allLiterals = new HashSet<Predicate>();
		  allLiterals.addAll(predicates);
		  int t = 0;
		  while(!allLiterals.containsAll(info.getGoal().getPredicates())){
			  HashSet<Predicate> curLiterals = new HashSet<Predicate>();
			  for(Predicate p: allLiterals){
				  if(info.getGroundList().get(p) != null){
					  for(Action action: info.getGroundList().get(p)){
						  if(allLiterals.containsAll(action.getPreconditions())){
							  for(Predicate del: action.getDeleteList()){
								  del.setSign(false);
								  if(!allLiterals.contains(del)){
									 del.setSign(true);
									 curLiterals.remove(del);
									 curLiterals.remove(del);
									 del.setSign(false);
									 del.setTimestep(t); 
									 curLiterals.add(del);
								  }
							  }
							  for(Predicate add: action.getAddList()){
								  if(!allLiterals.contains(add)){
									  add.setTimestep(t);
									  curLiterals.add(add);
								  }
							  }
						  }
					  }
				  }
			  }
			  t++;
			  if(curLiterals.isEmpty())
				  break;
			  allLiterals.addAll(curLiterals);
		  }
		  allLiterals.retainAll(info.getGoal().getPredicates());
		  int val = 0;
		  for(Predicate goal: allLiterals){
			  if(goal.getTimestep() > val){
				  val = goal.getTimestep();
			  }
		  }
		  return val;
	  }
	  else if(info.getHeuristic().equals("h1sum")){
		  /* sum */
		  HashSet<Predicate> allLiterals = new HashSet<Predicate>();
		  allLiterals.addAll(predicates);
		  int t = 0;
		  while(!allLiterals.containsAll(info.getGoal().getPredicates())){
			  HashSet<Predicate> curLiterals = new HashSet<Predicate>();
			  for(Predicate p: allLiterals){
				  if(info.getGroundList().get(p) != null){
					  for(Action action: info.getGroundList().get(p)){
						  if(allLiterals.containsAll(action.getPreconditions())){
							  for(Predicate del: action.getDeleteList()){
								  del.setSign(false);
								  if(!allLiterals.contains(del)){
									 del.setSign(true);
									 curLiterals.remove(del);
									 del.setSign(false);
									 del.setTimestep(t); 
									 curLiterals.add(del);
								  }
							  }
							  for(Predicate add: action.getAddList()){
								  if(!allLiterals.contains(add)){
									  add.setTimestep(t);
									  curLiterals.add(add);
								  }
							  }
						  }
					  }
				  }
			  }
			  t++;
			  if(curLiterals.isEmpty())
				  break;
			  allLiterals.addAll(curLiterals);
		  }
		  allLiterals.retainAll(info.getGoal().getPredicates());
		  int val = 0;
		  for(Predicate goal: allLiterals){
			 val += goal.getTimestep();
		  }
		  return val;
	  }
	  else{
		  System.err.println("Warning: unrecognized heuristic");
		  return 0;
	  }
  }
  
  public boolean isGoalState(){
	  //System.out.println(this.getPredicates());
	  HashSet<Predicate> newGoal = new HashSet<Predicate>(info.getGoal().getPredicates());
	  newGoal.retainAll(this.getPredicates());
	  //System.out.println(newGoal);
	  return this.getPredicates().containsAll(info.getGoal().getPredicates());
  }


  @Override
  public int hashCode() {
	  int result = 0;
	  for(Predicate p: predicates){
	   	  result += p.hashCode();
	  }
	  return result;
  }


  @Override
  public boolean equals(Object obj) {
	  State other = (State)obj;
	  if(!this.getPredicates().containsAll(other.getPredicates())){
		  return false;
	  }
	  if(!other.getPredicates().containsAll(this.getPredicates())){
		  return false;
	  }
	  return true;
  }
}
