import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the current world state through a 2D char array.
 * @author Stephen
 *
 */
public class State {
	private String _actionList;
	private ArrayList<Point> _blockedCells;
	private ArrayList<Point> _dirtyCells;
	private Point _curLocation;
	private Point _bounds;
	private boolean _useBattery;
	private ArrayList<Point> _chargingStations;
	private int _batteryPower;
	private int _numRecharges;

	/**
	 * Main State constructor
	 * @param actionList
	 * The actions it took to get to this state
	 * @param blockedCells
	 * A list of the locations of the blocked cells 
	 * @param dirtyCells
	 * A list of the locations of the dirty cells
	 * @param curLocation
	 * The current location of the robot
	 * @param bounds
	 * A point containing the x boundary and the y boundary
	 * @param chargingStations 
	 * @param battery
	 * Whether or not the "battery" flag is on, requiring the robot to keep track
	 * of power.
	 */
	public State(String actionList, ArrayList<Point> blockedCells, ArrayList<Point> dirtyCells, 
				Point curLocation, Point bounds, boolean useBattery, ArrayList<Point> chargingStations)
	{
		_actionList = actionList;
		_blockedCells = blockedCells;
		_dirtyCells = (ArrayList<Point>) dirtyCells.clone();
		_curLocation = (Point) curLocation.clone();
		_bounds = (Point) bounds.clone();
		_useBattery = useBattery;
		if(_useBattery)
			_chargingStations = chargingStations;
	}
		
	/**
	 * Get the current action list of this state
	 */
	public String getActionList(){
		return _actionList;
	}
	
	/**
	 * Get the blocked cells for this state
	 */
	public ArrayList<Point> getBlockedCells(){
		return _blockedCells;
	}

	
	/**
	 * Get the dirt remaining for this state
	 */
	public ArrayList<Point> getDirtyCells(){
		return _dirtyCells;
	}
		
	/**
	 * Get the current location of this state
	 */
	public Point getLocation(){
		return _curLocation;
	}
	
	/**
	 * Get the current bounds of this state
	 */
	public Point getBounds(){
		return _bounds;
	}

	
	/**
	 * Determine if this state is a goal state
	 */
	public boolean isGoalState(){
		return _dirtyCells.size() == 0;
	}
	
	public ArrayList<State> getNeighbors(){
		ArrayList<State> neighbors = new ArrayList<State>();
		
		//Immediately return if vaccuming is a possibility
		State vaccum = this.vaccum();
		if(vaccum != null){
			//System.out.println("Can Vacuum");
			neighbors.add(vaccum);
			return neighbors;
		}

		State west = this.moveWest();
		if(west != null){
			//System.out.println("Can Move West");
			neighbors.add(west);
		}
		
		
		if(_useBattery){
			State charged = this.recharge();
			if(charged != null){
				//System.out.println("Can recharge");
				neighbors.add(charged);
			}
		}
		
		State north = this.moveNorth();		
		if(north != null){
			//System.out.println("Can Move North");
			neighbors.add(north);
		}

		State south = this.moveSouth();
		if(south != null){
			//System.out.println("Can Move South");
			neighbors.add(south);
		}
		
		State east = this.moveEast();		
		if(east != null){
			//System.out.println("Can Move East");
			neighbors.add(east);
		}								
		
		return neighbors;
	}
	
	/**
	 * Move north: create a world if this state moved north.
	 * Return the new state if succeeded, null otherwise
	 */
	public State moveNorth(){
		Point north = new Point(_curLocation.x, _curLocation.y-1);
		if(canMove(north)){
			String newActionList = _actionList + "N";
			State northState = new State(newActionList, _blockedCells, _dirtyCells, 
						north, _bounds, _useBattery, _chargingStations);
			northState.setBatteryPower(_batteryPower - 1);
			northState.setNumCharges(_numRecharges);
			return northState;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Move South: create a world if this state moved south
	 * Return the new state if succeeded, null otherwise
	 */
	public State moveSouth(){
		Point south = new Point(_curLocation.x, _curLocation.y+1);
		if(canMove(south)){
			String newActionList = _actionList + "S";
			State southState = new State(newActionList, _blockedCells, _dirtyCells, 
						south, _bounds, _useBattery, _chargingStations);
			southState.setBatteryPower(_batteryPower - 1);
			southState.setNumCharges(_numRecharges);
			return southState;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Move west: create a world if this state moved west
	 * Return the new state if succeeded, null otherwise
	 */
	public State moveWest(){
		Point west = new Point(_curLocation.x - 1, _curLocation.y);
		if(canMove(west)){
			String newActionList = _actionList + "W";
			State westState = new State(newActionList, _blockedCells, _dirtyCells, 
						west, _bounds, _useBattery, _chargingStations);
			westState.setBatteryPower(_batteryPower - 1);
			westState.setNumCharges(_numRecharges);
			return westState;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Move north: create a world if this state moved east
	 * Return the new state if succeeded, null otherwise
	 */
	public State moveEast(){
		Point east = new Point(_curLocation.x + 1, _curLocation.y);
		if(canMove(east)){
			String newActionList = _actionList + "E";
			State eastState = new State(newActionList, _blockedCells, _dirtyCells, 
						east, _bounds, _useBattery, _chargingStations);
			eastState.setBatteryPower(_batteryPower - 1);
			eastState.setNumCharges(_numRecharges);
			return eastState;
		}
		else{
			return null;
		}
	}
	
	/**
	 * Vaccum, a world if this state vaccumed
	 */
	public State vaccum(){
		if(_dirtyCells.contains(_curLocation)){
			//If only one charge remains, cannot vaccum.
			if(_useBattery & (_batteryPower - 1) == 0){
				return null;
			}
			String newActionList = _actionList + "V";
			State vaccumedState = new State(newActionList, _blockedCells, _dirtyCells, 
						_curLocation, _bounds, _useBattery, _chargingStations);
			vaccumedState.setBatteryPower(_batteryPower -1);
			vaccumedState.setNumCharges(_numRecharges);
			vaccumedState.getDirtyCells().remove(_curLocation);
			return vaccumedState;
		}
		else{
			return null;
		}
	}
	/**
	 * Recharge, a world if this state recharged
	 */
	public State recharge(){
		if(_chargingStations.contains(_curLocation)){
			String newActionList = _actionList + "R";
			State chargedState = new State(newActionList, _blockedCells, _dirtyCells, 
						_curLocation, _bounds, _useBattery, _chargingStations);
			int newBatteryPower = ((_bounds.x + _bounds.y) * 2) + 1;
			chargedState.setBatteryPower(newBatteryPower);
			chargedState.setNumCharges(_numRecharges + 1);
			return chargedState;
		}
		else{
			return null;
		}
	}

	
	/**
	 * Sets the battery power of this state
	 * @param powerVal
	 * An integer representing how many more moves this state can make
	 */
	public void setBatteryPower(int powerVal){
		if(_useBattery)
			_batteryPower = powerVal;
	}
	
   /**
    * Get the current battery power of the robot
    * @return
    */
   public int getBatteryPower(){
	   return _batteryPower;
   }
   
	/**
	 * Sets the number of times this state has recharged
	 * @param num
	 * An integer representing how many times this state has charged
	 */
	public void setNumCharges(int num){
		if(_useBattery)
			_numRecharges = num;
	}
	
  /**
   * Get the current number of times this state has recharged
   * @return
   */
  public int getNumCharges(){
	   return _numRecharges;
  }


	
	/**
	 * Does bounds checking on a move to make sure it is valid
	 */
	private boolean canMove(Point dest){
		if(dest.x > _bounds.x || dest.x < 0 || dest.y > _bounds.y || dest.y < 0){
			return false;
		}
		else if(_blockedCells.contains(dest)){
			return false;
		}
		else if(_useBattery && (_batteryPower - 1) == 0){
				return false;
		}
		else{
			return true;
		}
	}		
}
