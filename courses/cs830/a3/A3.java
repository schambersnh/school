import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * A3.java
 * Does simple event parsing 
 */

/**
 * @author Stephen
 *
 */
public class A3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/* Populate initial world state */
		Scanner stdin = new Scanner(System.in);
		String line;
		
		int cols = stdin.nextInt();
		int rows = stdin.nextInt();
		
		Point2D.Double bounds = new Point2D.Double(cols - 1, rows -1);
		Point2D.Double curLocation = new Point2D.Double();
		Point2D.Double goal = new Point2D.Double();
		ArrayList<Point2D.Double> blockedCells = new ArrayList<Point2D.Double>();
		for(int i = rows-1; i >= 0; i--){
			line = stdin.next();
			for(int j = 0; j < cols; j++){
				char cur = line.charAt(j);
				if(cur == '#'){
					blockedCells.add(new Point2D.Double(j, i));
				}
			}
		}
		curLocation.setLocation(stdin.nextDouble(), stdin.nextDouble());
		goal.setLocation(stdin.nextDouble(), stdin.nextDouble());
		stdin.close();
		
		/* Get goal from planner */
		Planner planner = new Planner(curLocation, goal, bounds, blockedCells);
		State goalState = planner.findPath();

		/* print results */
		Stack<State> stack = new Stack<State>();
		State s = goalState;
		while(s != null){
			stack.push(s);
			s = s.parent;	
		}
		while(!stack.isEmpty()){
			State temp = stack.pop();
			if(stack.isEmpty()){
				System.out.println(temp.getLocation().getX() + " " + temp.getLocation().getY() + 
						" " + temp.getTheta() + " " + temp.getSpeed());
			}
			else{
				System.out.println(temp.getLocation().getX() + " " + temp.getLocation().getY() + 
						" " + temp.getTheta() + " " + temp.getSpeed() + " " + 
						stack.peek().getControl().getDirection() + " " + stack.peek().getControl().getMagnitude());
			}
		}
	}
}