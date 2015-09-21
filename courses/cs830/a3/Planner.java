import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Double;

/**
 * This class performs the RRT search algorithm
 * @author Stephen
 *
 */
public class Planner {
	private State _root;
	private Point2D _bounds;
	private Point2D _goal;
	private ArrayList<Point2D.Double> _blockedCells;
	
	/**
	 * Planner Constructor
	 * @param curLocation
	 * The current location of the robot
	 * @param goal
	 * The goal location
	 * @param bounds
	 * The bounds of the world
	 * @param blockedCells
	 * A list of the blocked cells in the world
	 */
	public Planner(Point2D.Double curLocation, Point2D.Double goal, 
			Point2D.Double bounds, ArrayList<Point2D.Double> blockedCells) {
		_root = new State(curLocation, 0, 0);
		_bounds = bounds;
		_goal = goal;
		_blockedCells = blockedCells;
	}
	
	/**
	 * Finds a valid, collision free path to the destination
	 * @return The goal state
	 */
	public State findPath(){
		State retval = null;
		ArrayList<State> rrt = new ArrayList<State>();
		rrt.add(_root);
		

		while(retval == null){
			/* Sample a random State */
			Random randGen = new Random();
			State sample = new State();
			
			/*There is a 5% chance that the goal state will be generated */
			int goalBias = 5;
			if(randGen.nextInt(100) < goalBias){
				sample.setLocation(_goal);
				sample.setTheta(0);
				sample.setSpeed(0);
			}
			else{
				double randX = _bounds.getX() * randGen.nextDouble();
				double randY = _bounds.getY() * randGen.nextDouble();
				double randTheta = -Math.PI + (Math.PI - -Math.PI) * randGen.nextDouble();
				double randSpeed = 2.0 * randGen.nextDouble();
				sample.setLocation(new Point2D.Double(randX, randY));
				sample.setTheta(randTheta);
				sample.setSpeed(randSpeed);
			}
			
			/* Find the nearest state in the tree */
			State closest = findNearestState(sample, rrt);
						
			/* Generate 10 random controls */
			ArrayList<Control> controls = new ArrayList<Control>();
			for(int i = 0; i < 10; i++){
				double randDirection = -Math.PI + (Math.PI - -Math.PI) * randGen.nextDouble();
				double randMagnitude = 0.5 * randGen.nextDouble();
				controls.add(new Control(randDirection, randMagnitude));
			}
			
			/* Get a list of potential states based on the ten random controls */
			ArrayList<State> potentialStates = getPotentialStates(closest, controls);
			
			/* Find the closest potential state to the target and add to tree */
			State closestPotentialState = findNearestState(sample, potentialStates);
			
			/* no controls were valid, continue through loop */
			if(closestPotentialState == null){
				continue;
			}
			
			/* save reference to the state that got us here and add to tree */
			closestPotentialState.parent = closest;
			rrt.add(closestPotentialState);
			
			/* Check for goal state */
			double deltaX = _goal.getX() - closestPotentialState.getLocation().getX();
			double deltaY = _goal.getY() - closestPotentialState.getLocation().getY();
			double dist = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			
			/* Found a goal! */
			if(dist < 1.0){
					retval = closestPotentialState;
			}
		}
		return retval;
	}

	/**
	 * Generates a list of potential states based on an initial state and a
	 * list of controls. Will ONLY return states that do not collide or 
	 * go off the map.
	 * @param sample 
	 * The initial state that the controls will be applied to
	 * @param controls
	 * A list of controls that will be applied to the sample state
	 * @return
	 */
	private ArrayList<State> getPotentialStates(State sample,
			ArrayList<Control> controls) {
		boolean invalid = false;
		ArrayList<State> retval = new ArrayList<State>();
		for(Control c: controls){
			State s = new State(sample.getLocation(), sample.getTheta(), sample.getSpeed());
	        
			/* Integrate the new state based on the control */
			double dt = 0.1;
			for(int i = 0; i < 10; i++){
		        //Rotate
		        double theta = s.getTheta();
		        theta += c.getDirection() * dt;
		        theta %= 2 * Math.PI;
		        if(theta < 0){
		        	theta += 2 * Math.PI;
		        }
		        s.setTheta(theta);
		 
		        //Increase speed
		        double speed = s.getSpeed();
		      	speed += c.getMagnitude() * dt;
		      	s.setSpeed(speed);
		 
		        //Modify position based on the speed
		      	double x = s.getLocation().getX();
		      	double y = s.getLocation().getY();
		        x += Math.cos(s.getTheta()) * s.getSpeed() * dt;
		        y += Math.sin(s.getTheta()) * s.getSpeed() * dt;
		        s.getLocation().setLocation(x, y);
		        if(!canMove(s)){
		        	invalid = true;
		        	break;
		        }
			}
			/* a ministep failed, try the next one */
			if(invalid){
				invalid = false;
				break;
			}
			else{
				s.setControl(c);
				retval.add(s);
			}
		}
		return retval;
	}

	private boolean canMove(State source) {
		Point2D sourceLoc = source.getLocation();
		
		/* check if still within the map */
		if(sourceLoc.getX() > _bounds.getX() || sourceLoc.getY() > _bounds.getY() ||
				sourceLoc.getX() < 0 || sourceLoc.getY() < 0){
				return false;
		}
		
		/* check to see if we are within a blocked cell */
		for(Point2D blocked: _blockedCells){
			if(sourceLoc.getX() > blocked.getX() && sourceLoc.getX() < (blocked.getX() + 1) &&
					sourceLoc.getY() > blocked.getY() && sourceLoc.getY() < (blocked.getY() + 1)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the state in "stateList" that is closest to the state "sample".
	 * Distance is calculated using euclidean distance, and additionally
	 * adding the difference between the two angles and speeds as well. 
	 * The distance is normalized
	 * @param sample
	 * The reference state
	 * @param stateList
	 * The list that we must choose a state from
	 * @return
	 */
	private State findNearestState(State sample, ArrayList<State> stateList) {
		State retval = null;
		double smallest = Double.MAX_VALUE;
		for(State s: stateList){
			double dx = sample.getLocation().getX() - s.getLocation().getX();
			double dy = sample.getLocation().getY() - s.getLocation().getY();
			double dtheta = s.getTheta() - sample.getTheta();
			//The following line correctly "wraps around" the circle to find the distance
			dtheta = Math.atan2(Math.sin(dtheta), Math.cos(dtheta));
			double dspeed = sample.getSpeed() - s.getSpeed();
			double dist = Math.sqrt(Math.pow(dx/_bounds.getX(), 2) + Math.pow(dx/_bounds.getY(), 2) + 
					Math.pow(dtheta/Math.PI, 2) + Math.pow(dspeed/2.0, 2));
			if(dist < smallest){
				smallest = dist;
				retval = s;
			}
		}
		return retval;
	}
}
