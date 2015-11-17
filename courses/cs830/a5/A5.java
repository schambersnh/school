import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A3.java
 * Does simple event parsing 
 */

/**
 * @author Stephen
 *
 */
public class A5 {
	private static boolean local = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String searchAlg = "dll";
		if(args.length == 1){
			if(args[0].equals("-w"))
				searchAlg = "walk-sat";
			else{
				System.err.println("Unknown command line argument");
				System.exit(1);;
			}
		}
		/* Populate initial world state */

		//String filename = "spec_input.txt";
		//String filename = "quinn.cnf";
		//String filename = "small.cnf";
		//String filename = "unit.cnf";
		//String filename = "tiny.cnf";
		String filename = "aim50.cnf";
		//String filename = "aim-100.cnf";
		//String filename = "zebra.cnf";
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************
		if(local){
			try {
				System.setIn(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************

		/* Populate initial world state */
		Scanner stdin = new Scanner(System.in);
		String line;
		Formula formula = new Formula();
		int numVariables = 0;
		int numClauses = 0;
		while(stdin.hasNext()){
			line = stdin.nextLine();
			Scanner subscan = new Scanner(line);
			if(line.charAt(0) == 'c'){
				continue;
			}
			else if(line.startsWith("p cnf"))
			{
				subscan.next();
				subscan.next();
				numVariables = subscan.nextInt();
				numClauses = subscan.nextInt();
				
				/* scan in formula */
				while(stdin.hasNext()){
					Clause c = new Clause();
					int cur = stdin.nextInt();
					while(cur != 0){
						c.addVariable(cur);
						cur = stdin.nextInt();
					}
					formula.addClause(c);
				}

			}				
			subscan.close();
		}
		
		Planner planner = new Planner(searchAlg, numVariables);
		boolean result = planner.isFormulaSatisfiable(formula);
		if(result){
			System.out.println("s cnf " + 1 + " " + numVariables + " " + numClauses);
			ArrayList<Integer> resultVars = planner.getFinalVars();
			int size = resultVars.size();
			for(int i = 0; i < size; i++){
				System.out.println("v" + " " + resultVars.get(i));
			}
		}
		else{
			if(searchAlg.equals("walk-sat"))
				System.out.println("s cnf " + -1 + " " + numVariables + " " + numClauses);
			else
				System.out.println("s cnf " + 0 + " " + numVariables + " " + numClauses);
		}
		if(searchAlg.equals("dll")){
			System.out.println(planner.getNodesExplored() + " branching nodes explored.");
		}
		else{
			System.out.println(planner.getMaxTries() + " total tries.");
			System.out.println(planner.getMaxFlips() + " total flips.");
		}
		stdin.close();
	}
}