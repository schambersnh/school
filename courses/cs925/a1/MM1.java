import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

/*
 * This class represents an M/M/1 queuing simulator. It will need to be provided with a array of lambdas (arrival rates).
 * Once provided, the class will simulate packets arriving an being dispatched by a server through exponential
 * distribution. It will record the average waiting time for each graph.
 */
public class MM1 {
	private double[] _rates;
	private double _currentTime;
	private PriorityQueue<Event> _eventQueue;
	private LinkedBlockingQueue<Event> _waitQueue;
	public MM1(double rates[])
	{
		_rates = new double[rates.length];
		for(int i = 0; i < rates.length; i++)
			_rates[i] = rates[i];
		_eventQueue = new PriorityQueue<Event>();
		_waitQueue = new LinkedBlockingQueue<Event>();
	}
	/**
	 * This function iterates through the given rates in the constructor, calculating
	 * the average waiting time for each rate. The average waiting time is stored in a double
	 * to be returned.
	 * @return
	 */
	public List<Double> simulate(double simTime)
	{
		List<Double> results = new ArrayList<Double>();
		for(int i = 0; i < _rates.length; i++)
			results.add(calculateWaitingTime(_rates[i], simTime));
		System.out.println(results);
		return results;
	}
	/**
	 * Initialize event queue by adding a packet arriving at 0.0 and an end event.
	 */
	private void initEventQueue(double simTime)
	{
		_eventQueue.clear();
		Event initialArrival = new Event(0, 0.0);
		Event end = new Event(2, simTime);
		_eventQueue.add(initialArrival);
		_eventQueue.add(end);
	}
	/**
	 * This is where the crux of the discrete simulation algorithm is done. 
	 * @param rate
	 */
	private double calculateWaitingTime(double rate, double simTime) {
		initEventQueue(simTime);
		_currentTime = 0.0;
		double waitingTime = 0.0;
		double totalPackets = 0.0;
		double serverDoneTime = 0.0;
		
		//The following code was modified from blog.uncommons.org
		Random rng = new MersenneTwisterRNG();
		ExponentialGenerator gen = new ExponentialGenerator(rate, rng);
		
		/* Simulation algorithm */
		while(true)
		{
			/* pop an event off the queue and check type */
			Event cur = _eventQueue.poll();
			int type = cur.getType();
			if(type == 0) /* packet arrival */
			{
				/* a packet arrived */
				totalPackets+= 1.0;
				
				/* schedule the next packet arrival event */
				double newArr = gen.nextValue() + _currentTime;
				//System.out.println(newArr - _currentTime);
				_currentTime = newArr;
				Event packetArr = new Event(0, newArr);
				//System.err.println("Packet arrived at time " + newArr);
				_eventQueue.add(packetArr);
				
				/* create packet departure event */
				double newDepart = gen.nextValue() + _currentTime;
				Event packetDepart = new Event(1);
				packetDepart.setArrivalTime(newArr);
				packetDepart.setDepartureTime(newDepart);

				/* check if the server is busy */
				if(_currentTime > serverDoneTime)
				{
					/* server is not busy, add to event queue and serve */
					_eventQueue.add(packetDepart);
					serverDoneTime = newDepart;
				}
				else
				{
					/* server is busy, add event to wait queue */
					_waitQueue.add(packetDepart);
				}
			}
			else if(type == 1) /* packet departure */
			{
				waitingTime += cur.getDepartureTime() - cur.getArrivalTime();
				//System.err.println(waitingTime + " " + totalPackets);
				if(_waitQueue.peek() != null)
				{
					Event newPacketDepart = _waitQueue.poll();
					_eventQueue.add(newPacketDepart);
					serverDoneTime = newPacketDepart.getDepartureTime();
				}
			}
			else if(type == 2) /* end event */
			{
				break;
			}
			else
			{
				System.err.println("Uknown type");
				System.exit(1);
			}
		}
		return waitingTime/totalPackets;
	}
	public static void main(String[] args)
	{
		double rates[] = {10};
		MM1 simulator = new MM1(rates);
		simulator.simulate(1000000.0);
	}
}
