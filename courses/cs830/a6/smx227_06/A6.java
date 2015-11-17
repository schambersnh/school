import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class A6 {
	private static boolean local = false;
	
	
	public static void main(String[] args){		
		//String filename = "p1.cnf";
		//String filename = "p2.cnf";
		//String filename = "p3.cnf";
		//String filename = "e1.cnf";
		//String filename = "e2.cnf";
		//String filename = "e3.cnf";
		//String filename = "e4.cnf";
		//String filename = "e5.cnf";
		//String filename = "e6.cnf";
		//String filename = "e7.cnf";
		//String filename = "test.cnf";
		//String filename = "s1.cnf";
		String filename = "s2.cnf";
		//String filename = "s3.cnf";

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

		/* Read in clauses */
		Scanner stdin = new Scanner(System.in);
		Clause c1 = GrammarListener.parseSentence("c1", stdin.nextLine());
		Clause c2 = GrammarListener.parseSentence("c2", stdin.nextLine());
		//System.out.println(c1);
		//System.out.println(c2);
		
		Unifier unifier = new Unifier();
		ArrayList<Pair<Clause,Clause>> result = unifier.unifyClauses(c1, c2);
		if(result.isEmpty()){
			System.out.println("Could not unify clauses");
		}
		for(Pair<Clause,Clause> clausePair: result){
			System.out.println(clausePair.getLeft());
			System.out.println(clausePair.getRight());
			System.out.println();
		}
		stdin.close();

	}
}
