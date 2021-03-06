/**
 * MultiTaskPerThreadApp.java - The main app that uses more tasks than threads
 */
import java.util.*;

public class MultiTaskPerThreadApp
{
  //--------------------- class variables -------------------------
  // min and max service times for taskQ   
  public static int minService = 20;
  public static int maxService = 200;
  
  //--------------------- instance variables -------------------------
  //---------------------- constructor --------------------------------
  public MultiTaskPerThreadApp( int nTasks, int nWorkers )
  {
    System.out.printf( "-------------- %d tasks   %d workers ------------\n",
                      nTasks, nWorkers );
    TaskQueue  taskQ     = generateTasks( nTasks );
    long       startTime = System.currentTimeMillis();
    Worker[] myWorkers = new Worker[nWorkers];
    
    for(int i = 0; i < myWorkers.length; i++)
    {
      Worker temp = new Worker("W" + i, taskQ);
      myWorkers[i] = temp;
      myWorkers[i].start();
    }
    for ( int t = 0; t < myWorkers.length; t++ )
    {
      try
      {
        myWorkers[t].join();
        System.out.println(myWorkers[t].toString());
      }
      catch(InterruptedException e)
      {
        
      }
      
      
      
    }
    /////////////////////////////////////////////////////////////
    // 2a. Define and initialized an array of nWorkers Worker objects. 
    //  b. For each entry in the array, create a Worker object
    //       - arg 1 is a name field; make it "W" + the array index.
    //       - arg 2 is a TaskQueue; pass in "taskQ"
    //     "start" the Worker object; Worker extends Thread, so these 
    //     are also Thread objects.  
    //
    //  c. Make a 2nd for loop over the array of Workers
    //     - copy the try-catch block from ThreadsPerTaskApp.java
    //     - modify it to "join" on the t entry of the array of Workers
    //       and print the toString() value from the t-th Worker; this
    //       method returns a summary of the tasks completed by this
    //       Worker thread.
    //
    //       Use a println here!
    ///////////////////////////////////////////////////////////////   
    
    
    
    
    float time = ( System.currentTimeMillis() - startTime ) / 1000.0f;     
    System.out.println( " Elapsed time: " + time );
  }
  //----------------- TaskQueue generateTasks( int ) ----------------------
  /**
   * generate the specified number of taskQ with random service times
   */
  private TaskQueue generateTasks( int nTasks )
  {
    TaskQueue taskQ = new TaskQueue();
    int       svcRange   = maxService - minService;
    int       totalSvc   = 0; 
    
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
    // 3. Try a variety of task and thread counts. For each attempt, COPY
    //    the lines of existing code. change the parameters, compile and 
    //    execute. Change println to report if failed and how.
    //
    //              LEAVE all previous tests in the code!!!!!
    //   
    //    For example try:
    //            20 10
    //           100 20
    //           200 20
    //           400 20
    //           400 30
    //           400 40
    //           500 50
    // Depending on your machine, these may not be the right ones to
    //   find a race condition, you may need fewer tasks and more threads?
    ///////////////////////////////////////////////////////////////////
    MultiTaskPerThreadApp m;
    try
    {
    m = new MultiTaskPerThreadApp( 500, 50 );
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
    }
    
     try
    {
    m = new MultiTaskPerThreadApp( 200, 500 );
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
    }
    
     try
    {
    m = new MultiTaskPerThreadApp( 1050, 30 );
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
    }
    
         try
    {
    m = new MultiTaskPerThreadApp( 500, 5 );
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
    }
    
         try
    {
    m = new MultiTaskPerThreadApp( 400, 30 );
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
    }

  }
}