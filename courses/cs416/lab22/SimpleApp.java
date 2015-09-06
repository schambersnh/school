/**
 * SimpleApp.java - This is a simple non-threaded application that simulates
 *                  the execution of a program that is composed of a set of
 *                  predefined tasks that must be completed in any order.
 */
import java.util.*;

public class SimpleApp
{
   //--------------------- class variables -------------------------
   // min and max service times for tasks   
   public static int minService = 500;
   public static int maxService = 1000;

   //--------------------- instance variables -------------------------
   //---------------------- constructor --------------------------------
   /**
    * build a TaskQueue, then loop through the queue, removing tasks from
    * the queue and executing them.
    */
   public SimpleApp( int nTasks )
   {
      TaskQueue  taskQ = generateTasks( nTasks );
      
      long startTime = System.currentTimeMillis();
         
      System.out.print( "Tasks done: " );
      // Loop through the tasks and perform them
      while ( taskQ.size() > 0 )
      {
         Task task = taskQ.remove();
         task.run();
         System.out.print( task + ", " ); // finished, print id
      } 
      System.out.println();
            
      float time = ( System.currentTimeMillis() - startTime ) / 1000.0f;
      
      System.out.println( " Elapsed time: " + time );
   }
   //----------------- TaskQueue generateTasks( int ) ----------------------
   /**
    * generate the specified number of tasks with random service times
    */
   private TaskQueue generateTasks( int nTasks )
   {
      TaskQueue  taskQ    = new TaskQueue();
      int svcRange   = maxService - minService;
      int totalSvc   = 0; 
      
      // create the tasks
      Random rng = new Random( 1 );
      for ( int t = 0; t < nTasks; t++ )
      {
         int serviceTime = minService + rng.nextInt( svcRange );
         totalSvc += serviceTime;
         taskQ.add( new Task( "T" + t, serviceTime ));
      }
      
      float taskTime = totalSvc / 1000.0f;
      System.out.println( "#Tasks: " + nTasks + " Total time: " + taskTime );
      return taskQ;  
   }
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public static void main( String[] args )
   {
      SimpleApp m = new SimpleApp( 10 );
   }
}