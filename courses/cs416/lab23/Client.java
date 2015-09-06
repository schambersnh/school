/**
 * Client - producer part of simple multithreading example in Java 
 *            using BankSim/Teller  model
 *     This "product" is a bank customer; for this simple example
 *     the only information the Client needs to have is the processing
 *     time required for the teller to perform the customer's requests.
 */
public class Client
{
   //------------------ instance variables --------------------------
   public int id;        // just the count when created
   public int service;   // time needed for service
   public int arrival;   // time of arrival
   public int waitTime;  // time client spent waiting in queue
   
   //----------------- constructor -----------------------------------
   public Client( int clientId, int atime, int stime )
   {
      id      = clientId;
      arrival = atime;
      service = stime;
      waitTime = -1;
   }
   //-------------- toString() -------------------------------------
   public String toString()
   {
      String ret = "id: " + id + " arr@" + arrival + "  svc: " + service;
      if ( waitTime >= 0 )
         ret += " wait: " + waitTime;
      return  ret;
   }
}
