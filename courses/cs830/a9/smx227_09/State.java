import java.util.ArrayList;


public class State {
	private int num;
	private double reward;
	private double utility = 0.0;
	private double expectedUtility = 0.0;
	private boolean isTerminal;
	private ArrayList<Action> actions;
	private Action setAction;
	
	public State(int num, double reward, int isTerminal ){
		this.num = num;
		this.reward = reward;
		this.isTerminal = isTerminal > 0 ? true: false;
		actions = new ArrayList<Action>();
	}
	
	public int getNum() {
		return num;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public boolean isTerminal() {
		return isTerminal;
	}
	
	public ArrayList<Action> getActions(){
		return actions;
	}
	
	public void addAction(Action newAction){
		actions.add(newAction);
	}
	
	/**
	 * @return the utility
	 */
	public double getUtility() {
		return utility;
	}

	/**
	 * @param utility the utility to set
	 */
	public void setUtility(double utility) {
		this.utility = utility;
	}
	

	/**
	 * @param expectedUtility the expectedUtility to set
	 */
	public void setExpectedUtility(double expectedUtility) {
		this.expectedUtility = expectedUtility;
	}
	
	public double getExpectedUtility(){
		return expectedUtility;
	}

	public double calcMaxUtility() {
		if(isTerminal()){
			return 0;
		}
		else{
			double max = -Double.MAX_VALUE;
			for(Action action: actions){
				double maxUtility = action.getUtility();
				if(maxUtility > max){
					max = maxUtility;
					setAction = action; 
				}
			}
			return max;
		}
	}
	
	public Action getGreedyAction() {
		Action retval = null;
		double max = -Double.MAX_VALUE;
		for(Action action: actions){
			double maxUtility = action.getUtility();
			if(maxUtility > max){
				max = maxUtility;
				retval = action; 
			}
		}
		return retval;
	}
	
	public State applyAction(Action action){
		ProblemInfo info = ProblemInfo.getInstance();
		double prob = info.rng().nextDouble();
		for(Successor s: action.getSuccessors()){
			if(prob <= s.getProbability()){
				return info.getStateList().get(s.getState());
			}
			prob -= s.getProbability();
		}
		return null;
	}

	
	public String toString(){
		return num + ": " + utility;
	}

	public Action getSetAction(){
		return setAction;
	}

}
