/**
 * Dispatcher.java -- Controls the activities of the EMTVehicle
 *        Resonsibilities:
 *            1. Know the current state of the vehicle: is it at home,
 *               heading to an emergency, heading for hospital, heading home.
 *            2. Tell the vehicle what to do next. Normally, this will be
 *               after it gets to its next destination. However, if the
 *               vehicle is heading home and an emergency site gets added,
 *               it should get re-directed to the site.
 *            3. It must know about or be able to get information from
 *               a. the emergency sites collection
 *               b. the hospital collection
 *               c. the timer (because it needs to know when that 
 *                  vehicle needs to wait for 2 seconds. The easiest
 *                  way to do this is to simply "restart()" the timer.
 *                  This works because there is nothing else moving in
 *                  this application except one vehicle. If that weren't
 *                  the case, the timer would have to keep running and
 *                  the code would have to count events in order to figure
 *                  out when 2 seconds had elapsed.)
 * 
 *        Framework:
 *            This class should implement the Animated interface, and get
 *            called (by EMTPanel) on every frame event. For example, 
 *            if the vehicle is home or going home and there is an entry 
 *            in the sites array, the dispatcher needs to tell the vehicle 
 *            to go to the next site in the array. There are of course 
 *            several other cases.
 * 
 * @author rdb
 * 02/02/11 -- Skeleton for dispatcher 
 * 
 * 
 */
import java.util.*;
import java.awt.*;

public class Dispatcher implements Animated
{
   //-------------------- instance variables ---------------------
   private ArrayList<Hospital> _hospitals;
   
   //------------------ constructor -----------------------------
   public Dispatcher( 
                     ArrayList<Hospital> hospitals
                     // what else do you need or want?
                    )
   {
      _hospitals = hospitals;
      // most/all of what you get, will be saved in instance variables
   }
   //---------------------- setNextSite() -------------------------
   /**
    * identify the next emergency site, if there is one
    */
   private EmergencySite getNextSite()
   {
      EmergencySite next = null;
      ///////////////////////////////////////////////////////////////
      // get next site from list of sites or from someone who has the
      //    list of sites.
      ///////////////////////////////////////////////////////////////

      
      
      return next;
   }
   //--------------- getClosestHospital( Point ) -------------------------------
   /**
    * find the closest hospital site to the parameter
    *    This method is complete.
    * Note that we don't bother to compute the correct distance,
    *    we make the decision based on the square of the distance
    *    which saves the computation of the square root, a reasonably
    *    costly operation that serves no purpose for this test.
    */
   private Hospital getClosestHospital( Point loc )
   {
      double distanceSq = Float.MAX_VALUE;
      Hospital   closest    = null;
      for ( Hospital h: _hospitals )
      {
         double d2 = loc.distanceSq( h.getLocation() );
         if ( d2 < distanceSq )
         {
            distanceSq = d2;
            closest = h;
         }
      }
      return closest;
   }
      
   //++++++++++++++++++++++ Animated interface +++++++++++++++++++++++++
   private boolean _animated = true;
   //---------------------- isAnimated() ----------------------------------
   public boolean isAnimated()
   {
      return _animated;
   }
   //---------------------- setAnimated( boolean ) --------------------
   public void setAnimated( boolean onOff )
   {
      _animated = onOff;
   }
   //---------------------- newFrame ------------------------------
   /**
    * invoked for each frame of animation; figure out what to do with
    *    the vehicle at this frame.
    */
   public void newFrame()
   {
      //////////////////////////////////////////////////////////////////
      // If the vehicle is moving, update its position (by calling its 
      //    newFrame method. 
      // Somehow, need to know if it has reached its goal position. 
      //    If so, figure out what the next goal should be.
      // 
      //    If previous goal was an emergency site, new goal is hospital,
      //    If previous goal was a hospital (or if it was at home,
      //       or if it was going home), new goal is the next
      //       emergency site, if there is one, or home if no more 
      //       emergencies.
      /////////////////////////////////////////////////////////////////
      
      
   }
      //+++++++++++++++ end Animated interface ++++++++++++++++++++++++++++++++++
}