import java.util.ArrayList;


public class Planner {
	private ArrayList<State> stateList;
	private ProblemInfo info;
	private int numBackups; 
	
	public Planner(ArrayList<State> stateList){
		this.stateList = stateList;
		info = ProblemInfo.getInstance();
	}
	
	public ArrayList<Action> getPolicy(){
		if(info.getAlgorithm().equals("vi")){
			return getPolicyVI();
		}
		else{
			return getPolicyRTDP();
		}
	}
	
	public ArrayList<Action> getPolicyVI(){
		ArrayList<Action> retval = new ArrayList<Action>();
		/** 
		 * Repeat until happy
		 *   for each state s
		 *     U'(s) = R(s) + discount * max(T(s,a,s')*U(s'))
		 *   U = U'
		 */
		setTerminalStates();
		double maxDiff = -Double.MAX_VALUE;
		do {
			maxDiff = -Double.MAX_VALUE;
			for(State s: stateList){
				if(!s.isTerminal()){
					double newMax = s.calcMaxUtility();
					double newUtility = s.getReward() + info.getDiscount() * newMax;
					double diff = Math.abs(newUtility - s.getUtility());
					if(diff > maxDiff){
						maxDiff = diff;
					}
					s.setExpectedUtility(newUtility);
					numBackups++;
				}
			}
			updateUtilities();
		} while(maxDiff > info.getTermCrit());
		/* get the action that each state was set to */
		for(State s: stateList){
			retval.add(s.getSetAction());
		}
		return retval;
	}
	
	/**
	 * Update all utilities with their "expected utility". This buffer is
	 * necessary to separate the iterations (iteration n does not want to 
	 * use iteration n+1's value).
	 * @param sList
	 */
	private void updateUtilities(){
		/* update the states with their new utilities */
		for(State s: stateList){
			s.setUtility(s.getExpectedUtility());
		}
	}
	
	/**
	 * Set all terminal states to their rewards
	 * @param sList
	 */
	private void setTerminalStates(){
		for(State s: stateList){
			if(s.isTerminal()){
				s.setUtility(s.getReward());
				s.setExpectedUtility(s.getReward());
			}
		}
	}
	
	public int getNumBackups(){
		return numBackups;
	}
	
	public ArrayList<Action> getPolicyRTDP(){
		ArrayList<Action> retval = new ArrayList<Action>();
		
		initStates();
		for(int i = 0; i < (int)info.getTermCrit(); i++){
			State cur = info.getStateList().get(info.getStartState());
			while(!cur.isTerminal()){
				/* pick the greedy action */
				Action greedyAction = cur.getGreedyAction();
				
				/* simulate to next state */
				State next = cur.applyAction(greedyAction);
				cur = next;
				
				/* update the states utility */
				double newMax = cur.calcMaxUtility();
				//System.out.println(newMax);
				double newUtility = cur.getReward() + info.getDiscount() * newMax;
				cur.setUtility(newUtility);
				numBackups++;
			}
		}
		for(State s: stateList){
			//System.out.println(s);
			retval.add(s.getGreedyAction());
		}
		return retval;
	}
	
	/**
	 *  initialize all utilities to an "upper bound"
	 */
	private void initStates(){
		/* find max reward */
		double max = -Double.MAX_VALUE;
		for(State s: stateList){
			if(s.getReward() > max){
				max = s.getReward();
			}
		}
		
		/* set each states utility to maxReward * # states 
		 * If terminal state, initialize to its reward */
		for(State s: stateList){
			if(s.isTerminal()){
				s.setUtility(s.getReward());
			}
			else{
				s.setUtility(max);
				//System.out.println(s);
			}
		}
	}
	

}
