import java.util.ArrayList;
import java.util.Random;

/**
 * This class performs the DLL and Walk-SAT algorithms
 * @author Stephen
 *
 */
public class Planner {	
	private int nodesExplored;
	private int maxNumTries;
	private int maxNumFlips;
	private String searchAlg;
	private int numVariables;
	public static boolean DEBUG = false;
	private ArrayList<Integer> finalVars;

	/**
	 * Constructor
	 */
	public Planner(String searchAlg, int numVariables) {
		this.searchAlg = searchAlg;
		this.numVariables = numVariables;
		finalVars = new ArrayList<Integer>();
	}
	
	public boolean isFormulaSatisfiable(Formula f){
		if(searchAlg.equals("dll")){
			return isSatisfiable(f);
		}
		else{
			return walkSat(f);
		}
	}
	
	/**
	 * This function performs the DLL algorithm
	 * @param f
	 * @return
	 */
	public boolean isSatisfiable(Formula f){
		Formula formulaCopy = new Formula(f);
		
		/* unit propagation */
		ArrayList<Clause> unitClauses = f.getUnits();
		while(!unitClauses.isEmpty()){
			for(Clause c: unitClauses){
				if(c.isEmpty()){
					break;
				}
				if(c.getVariables().get(0) > 0){
					setVariable(f, c.getVariables().get(0), 1);
				}
				else{
					setVariable(f, c.getVariables().get(0), -1);
				}
			}
			unitClauses = f.getUnits();
		}

		if(f.isEmpty()){
			finalVars.addAll(f.getFinalVars());
			return true;
		}
		if(f.hasEmptyClause())
			return false;
		int v = f.getNextVariable();
		if(isSatisfiable(setVariable(f, v, 1)))
			return true;
		else{
			return isSatisfiable(setVariable(formulaCopy, v, -1));
		}
	}
	
	/**
	 * Set the value to 'true' or 'false' by multiplying it by newValue.
	 * Remove clauses where v has value.
	 * Remove v from clauses where it has -value.
	 * @param newValue
	 * @return
	 */
	private Formula setVariable(Formula formula, int val, int newValue){
		nodesExplored++;
		formula.reduce(val, newValue);
		return formula;
	}
	
	/**
	 * Return the final formulas set variables
	 * @return
	 */
	public ArrayList<Integer> getFinalVars(){
		return finalVars;
	}
	
	/**
	 * Performs the walksat algorithm to determine if the formula is satisfiable
	 * @param f
	 * @return
	 */
	public boolean walkSat(Formula f){
		Random random = new Random();
		int rand = 0;
		int maxFlips = 10000;
		int maxTries = Integer.MAX_VALUE;
		int i = 0;

		/* Get all possible variables */
		ArrayList<Integer> vars = new ArrayList<Integer>();
		for(i = 0; i < numVariables; i++){
			vars.add(i+1);
			finalVars.add(i+1);
		}
		
		long startTime = System.nanoTime();
		for(int k = 0; k < maxTries; k++){			
			/* assign all variables randomly */
			for(i = 0; i < numVariables; i++){
				rand = (random.nextInt(2) == 0) ? -1 : 1;
				vars.set(i, vars.get(i) * rand);
				finalVars.set(i,  finalVars.get(i) * rand);
				f.clearClauses();
				f.setVariable(vars.get(i), rand);
			}
			
			for(i = 0; i < maxFlips; i++){
				int valToFlip = 0;
				/* get a random unsatisfied clause */
				ArrayList<Clause> unsats = f.getUnsatisfied();
				if(unsats.isEmpty()){
					return true;
				}
				Clause randClause = unsats.get(random.nextInt(unsats.size()));
				
				/* Get all positive flips */
				ArrayList<Integer> positiveFlips = f.getPositiveFlips(randClause);
				if(!positiveFlips.isEmpty()){
					int randIndex = random.nextInt(positiveFlips.size());
					valToFlip = positiveFlips.get(randIndex);
				}
				else{
					int coin = random.nextInt(2);
					if(coin == 1){
						int randIndex = random.nextInt(randClause.getVariables().size());
						valToFlip = randClause.getVariables().get(randIndex);
					}
					else{
						int varSize = randClause.getVariables().size();
						int min = Integer.MAX_VALUE;
						for(int j = 0; j < varSize; j++){
							int var = randClause.getVariables().get(j);
							int newValue = (var < 0) ? -1: 1;
							int flipVal = f.getFlipValue(var, newValue);
							if(flipVal < min){
								min = flipVal;
								valToFlip = var;
							}
						}
					}
				}
				/* flip the value */
				int newSign = (valToFlip < 0) ? -1: 1;
				finalVars.set(Math.abs(valToFlip) - 1, valToFlip);
				f.setVariable(valToFlip, newSign);
				if(f.getUnsatisfied().isEmpty()){
					return true;
				}
				maxNumFlips++;
			}
			maxNumTries++;
			long newTime = System.nanoTime();
			long diffInSeconds = Math.abs((startTime - newTime)) / 1000000000;
			if(diffInSeconds >= 30){
				return false;
			}
		}
		return false;
	}
		
			
	/**
	 * Get the nodes explored for the last algorithm run
	 * @return the nodes explored
	 */
	public int getNodesExplored(){
		return nodesExplored;
	}	
	
	/**
	 * Get the max tries for the last algorithm run
	 * @return the nodes explored
	 */
	public int getMaxTries(){
		return maxNumTries;
	}	

	
	/**
	 * Get the max flips for the last algorithm run
	 * @return the nodes explored
	 */
	public int getMaxFlips(){
		return maxNumFlips;
	}	

}
