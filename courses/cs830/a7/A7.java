import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class A7 {
	private static boolean local = false;
	
	
	public static void main(String[] args){		
		//String filename = "unification-1.cnf";
		//String filename = "unification-2.cnf";
		//String filename = "unification-2b.cnf";
		//String filename = "unification-3.cnf";
		//String filename = "unification-4.cnf";
		//String filename = "unification-5.cnf";
		//String filename = "test.cnf";
		//String filename = "ans-lit-1.cnf";
		String filename = "ans-lit-2.cnf";

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

		/* Read in the KB */
		ArrayList<Clause> kb = new ArrayList<Clause>();
		ArrayList<Clause> resultList = new ArrayList<Clause>();
		
		Scanner stdin = new Scanner(System.in);
		int count = 1;
		Clause query = null;
		while(stdin.hasNextLine()){
			String line = stdin.nextLine();
			//System.out.println(line);
			if(line.equals("--- negated query ---")){
				query = GrammarListener.parseSentence("c" + count, stdin.nextLine());
				query.setNum(count);
			}
			else{
				Clause newClause = GrammarListener.parseSentence("c" + count, line);
				newClause.setNum(count);
				kb.add(newClause);
			}
			
			count++;
		}
		for(Clause c: kb){
			System.out.println(c.getNum() + ": " + c);
			resultList.add(c);
		}
		System.out.println(query.getNum() + ": " + query);
		resultList.add(query);
		
		boolean wantsAnswer = query.toString().indexOf("Ans") != -1;
		Resolver resolver = new Resolver(kb, query, count);
		Clause result = null;
		while(result == null){
			result = resolver.resolve(wantsAnswer);
			if(resolver.isNoSolution()){
				break;
			}
		}
		if(result == null){
			System.out.println("No proof exists");
		}
		else{
			Stack<Clause> resultStack = new Stack<Clause>();
			resolver.stackify(result, resultStack);
			while(!resultStack.isEmpty()){
				Clause temp = resultStack.pop();
				int indexLeft = resultList.indexOf(temp.parent.getLeft());
				int indexRight = resultList.indexOf(temp.parent.getRight());
				if(indexLeft != -1 && indexRight != -1){
					System.out.println((indexLeft+1) + " and " + 
							(indexRight+1) + " give " + count + ": " + temp);
				} else if(indexLeft != -1){
					System.out.println((indexLeft+1) + " and " + 
							temp.parent.getRight().getNum() + " give " + count + ": " + temp);
				} else if(indexRight != -1){
					System.out.println(temp.parent.getLeft().getNum() + " and " + 
							(indexRight+1) + " give " + count + ": " + temp);
				}
				else{
					System.out.println(temp.parent.getLeft().getNum() + " and " + 
							temp.parent.getRight().getNum() + " give " + count + ": " + temp);
				}
				resultList.add(temp);
				count++;
			}
		}
		System.out.println(resolver.getNumResolutions() + " total resolutions");
		stdin.close();
	}
}
