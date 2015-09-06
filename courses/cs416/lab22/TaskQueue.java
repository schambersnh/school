/**
 * TaskQueue -- the set of Tasks to be executed; this is just a wrapper
 *              around an ArrayList object, but it implements queue 
 *              behavior -- not very efficiently, but good enough for this lab!
 *              
 * @author rdb
 * 11/18/10
 */
import java.util.*;

public class TaskQueue
{
   //------------------ instance variables ----------------------
   private ArrayList<Task> taskQ;
   int size = 0;
   
   //------------------- constructor ----------------------------
   public TaskQueue()
   {
      taskQ = new ArrayList<Task>();
   }
   //------------------------- add( Task ) -------------------------
   /**
    * add a task to the end of the queue
    */
   public void add( Task task )
   {
      taskQ.add( task );
      size++;
   }
   //------------------------- remove( ) -------------------------
   /**
    * remove and return the task at the head of the queue;
    * if no taskQ left, return null
    */
   //////////////////////////////////////////////////////////
   // 4. add "synchronized" to the beginning of the next line
   //////////////////////////////////////////////////////////
   synchronized public Task remove()
   {
      if ( size == 0 )
         return null;
      else
      {
         size--;
         return taskQ.remove( 0 );
      }
   }
   //------------------------- get( int  ) -------------------------
   /**
    * get a reference to the i-th entry in the task
    */
   public Task get( int i )
   {
      if ( i < 0 || i >= size()  )
         return null;
      else
         return taskQ.get( i );
   }
   /**
    * return the size of the queue
    */
   public int size()
   {
      return size;
   }
}
