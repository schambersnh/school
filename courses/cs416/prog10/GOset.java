/**
 * GOset -- a set GameObjects to be executed; this is just a wrapper
 *              around an ArrayList object, but it implements queue 
 *              behavior -- not very efficiently, but good enough for this task!
 *  All the methods come in pairs:
 *     - private version that does the work
 *     - public version that just calls the private version
 * 
 * You need to change the public versions to test the flag:
 *    RegionManager.synchronize 
 * If it is false, just call the private method.
 * If it is true, you need to call the private method inside a
 *    synchronized block using this.
 * 
 * @author rdb
 * 04/29/11 Two  more changes to increase failure rate when un-synchronized
 *          - Added a yield() call in remove: this was the winner!
 *          - Added new entries at the start of the array list; more time means
 *            more chance of being interrupted 
 */
import java.util.*;

public class GOset
{
   //------------------ instance variables ----------------------
   private ArrayList<GameObject> goSet;
   
   private Random rng;
   private int    size = 0;
   
   //------------------- constructor ----------------------------
   public GOset()
   {
      rng = new Random();
      goSet = new ArrayList<GameObject>();
      size = 0;
   }
   //------------------------- add( GameObject ) -------------------------
   /**
    * add a game object to the end of the set
    */
   public void add( GameObject go )
   {
     if(RegionManager.synchronize == false)
     {
      addP( go );
     }
     else
     {
       synchronized(this)
       {
       addP( go );
       }
     }
   }
   private void addP( GameObject go )
   {
      ///////////////////////////////////////////////////////////////////
      // This is an artificial complication (for this game) aimed at making a
      // non-synchronized execution more likely to fail. We force
      // a little more work by putting the object at location 0.
      // Since this requires more work in ArrayList, it increases the chance
      // of being interrupted before finishing
      //
      if ( size > 0 )
         goSet.add( rng.nextInt( size + 1 ), go );
      else
         goSet.add( go );
      Thread.currentThread().yield();  // force task switch
      size++;
   }
   //------------------------- remove( int ) -------------------------
   /**
    * remove and return the object at the head of the queue;
    * if no goSet left, return null
    */
   public GameObject remove( int i )
   {
      if(RegionManager.synchronize == false)
     {
      return removeP( i );
     }
     else
     {
       synchronized(this)
       {
       return removeP( i );
       }
     }
      
   }
   private GameObject removeP( int i )
   {
      if ( size() == 0 )
         return null;
      else
      {
         GameObject r = goSet.remove( i );
         Thread.currentThread().yield();   // force thread switch
         size--;
         return r;
      }
   }
   //------------------------- remove( GameObject ) -------------------------
   /**
    * remove and return the object at the head of the queue;
    * if no goSet left, return null
    */
   public boolean remove( GameObject go )
   {
     if(RegionManager.synchronize == false)
     {
      return removeP( go );
     }
     else
     {
       synchronized(this)
       {
       return removeP( go );
       }
     }

   }
   private boolean removeP( GameObject go )
   {
      if ( size() == 0 )
         return false;
      else
      {
         boolean flag = goSet.remove( go );
         Thread.currentThread().yield();   // force thread switch
         size--;
         return flag;
      }
   }
   //------------------------- contains( GameObject ) -------------------------
   /**
    * return true if the object is in this GOset
    */
   public boolean contains( GameObject go )
   {
     if(RegionManager.synchronize == false)
     {
      return containsP( go );
     }
     else
     {
       synchronized(this)
       {
       return containsP( go );
       }
     }
      
   }
   private boolean containsP( GameObject go )
   {
     if(RegionManager.synchronize == false)
     {
      return goSet.contains( go );
     }
     else
     {
       synchronized(this)
       {
       return goSet.contains( go );
       }
     }
      
   }
   //----------------------- clear() -------------------------
   /**
    * clear all entries from goSet
    */
   public void clear()
   {
     if(RegionManager.synchronize == false)
     {
      clearP();
     }
     else
     {
       synchronized(this)
       {
       clearP();
       }
     }
      
   }
   private void clearP()
   {
      goSet.clear();
      size = 0;
   }
   //------------------------- get( int  ) -------------------------
   /**
    * get a reference to the i-th entry in the task
    */
   public GameObject get( int i )
   {
     if(RegionManager.synchronize == false)
     {
      return getP( i );
     }
     else
     {
       synchronized(this)
       {
       return getP( i );
       }
     }
      
   }
   private GameObject getP( int i )
   {
      if ( i < 0 || i >= size()  )
         return null;
      else
         return goSet.get( i );
   }
   
   //-------------------------- size() --------------------------------
   /**
    * return the size of the queue
    */
   public int size()
   {
      if(RegionManager.synchronize == false)
     {
      return sizeP();
     }
     else
     {
       synchronized(this)
       {
       return sizeP();
       }
     }
      
   }
   private int sizeP()
   { 
      return size;   
   }
}
