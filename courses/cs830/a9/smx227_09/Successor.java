
public class Successor {
	private int state;
	private double probability;
	
	public Successor(int state, double prob){
		this.state = state;
		this.probability = prob;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
}
