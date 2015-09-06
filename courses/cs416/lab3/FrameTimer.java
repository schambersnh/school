/**
 * Chapter 7: FrameTimer.java (originally named, MoveTimer)
 * A subclass of Timer that can be used for animation.
 * It also serves as an example of the code for an "event source" object.
 * Version 2 of 2
 */
import javax.swing.*;
import java.awt.event.*;

public class FrameTimer extends javax.swing.Timer 
{
   //---------------- instance variables ---------------------------
   private Animated _mover; // peer object
   
   //------------------- constructor -------------------------------
   /**
    * Takes a time interval (in milliseconds) and an Animated object
    */
   public FrameTimer( int anInterval, Animated aAnimated ) 
   {
      super( anInterval, null );
      _mover = aAnimated;          // save reference to the object
      
      // create an instance of the inner class, MoveListener
      MoveListener myListener = new MoveListener();
      
      // tell the javax.swing.Timer object to tell myListener
      //   object whenever  the timer interval elapses.
      this.addActionListener( myListener );
   }
   
   //+++++++++++++++ MoveListener "inner" class +++++++++++++++++++++
   private class MoveListener implements ActionListener 
   {
      //---------------- actionPerformed( ActionEvent ) -------------
      /**
       * This method is called when the time interval elapses.
       */
      public void actionPerformed( ActionEvent e )
      {
         // invoke the "newFrame" method of the Animated object
         _mover.newFrame();
      }
   }
}

