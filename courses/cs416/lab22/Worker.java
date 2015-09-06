/**
 * Worker - This class implements the MultiTaskThread class from the 
 *          course notes. It is constructed with a TaskQueue and its
 *          "run" methods continues to remove a task from the queue, 
 *          execute that tasks "run" method and gets another task from
 *          queue -- until the queue is empty.
 * 
 *          It gathers very basic statistics about its processing and reports
 *          them with its toString() method along with a list of the taskIds
 *          processed.
 * 
 * @author rdb
 * 11/20/10
 */

public class Worker extends Thread
{
   //-------------------- instance variables ---------------------
   private TaskQueue    taskQ;
   private Task         task;
   private String       id;
   private int          taskCount = 0;
   private float        totalTime = 0;
   private StringBuffer taskIds;     // ids of tasks completed by this worker
   
   //-------------------- constructor ----------------------------
   public Worker( String workerName, TaskQueue tasks )
   {
      super( workerName );
      id       = workerName;
      taskQ    = tasks;
      taskIds  = new StringBuffer();
   }
   //-------------------- run() -----------------------------------
   /**
    * loop getting tasks from TaskQueue until there are no more
    */
   public void run()
   {
      Task task = taskQ.remove();
      while ( task != null )
      {
         taskCount++;
         long startTime = System.currentTimeMillis();  
         
         task.run();
         
         totalTime += ( System.currentTimeMillis() - startTime ) / 1000.0f;
         taskIds.append( task.taskName + " " );
         
         task = taskQ.remove();
      }
   }
   //-------------------- toString() ---------------------------
   /**
    * reports name of worker, total # tasks completed and average completion
    * time.
    */
   public String toString()
   {
      float avg = 0;
      if ( taskCount > 0 )
         avg = totalTime / taskCount;
      return id + ": Did " + taskCount + " tasks. Ave time: " + avg
                 + " : " + taskIds;
   }
}