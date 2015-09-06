import java.util.Comparator;

/**
 * This class defines an event. In our case, this 'event' is either a packet arrival, packet departure, or
 * the end of the simulation.The packet arrival and packet departure events have an associated packet with it.
 * @author Stephen
 *
 */
public class Event implements Comparable {
	/*
	 * 0: Packet Arrival
	 * 1: Packet Departure
	 * 2: End
	 */
	private int _type;
	private double _arrivalTime;
	private double _departureTime;
	public Event(int eventType, double arrivalTime)
	{
		_type = eventType;
		_arrivalTime = arrivalTime;
	}
	public Event(int eventType)
	{
		_type = eventType;
	}
	
	public int getType() {
		return _type;
	}
	public double getArrivalTime() {
		return _arrivalTime;
	}
	public void setArrivalTime(double _arrivalTime) {
		this._arrivalTime = _arrivalTime;
	}
	public double getDepartureTime() {
		return _departureTime;
	}
	public void setDepartureTime(double _departureTime) {
		this._departureTime = _departureTime;
	}
	@Override
	public int compareTo(Object o) {
		Event newEvent = (Event)o;
		
		if(_arrivalTime < newEvent.getArrivalTime())
			return -1;
		else if(_arrivalTime == newEvent.getArrivalTime())
			return 0;
		else
			return 1;
	}
	
}
