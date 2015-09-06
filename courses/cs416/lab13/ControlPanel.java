/** 
 * ControlPanel.java:
 * A panel of sliders and other controls for the application
 *
 * @author Matthew Plumlee
 * modified by rdb 
 * 
 * CS416 Spring 2008
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel 
{
   //---------------- instance variables ---------------------------
   private Component  _parent;
   
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * and a reference to the application class.
    */
   public ControlPanel( Component parent ) 
   {
      super( new GridLayout( 0, 1) );
      _parent = parent;
            
      //create sliders
      add( new DataSizeSlider() );         

      //create misc check boxes and quit button
      JPanel buttonRow = new JPanel();
      
      buttonRow.add( new SeedSpinner() );
      buttonRow.add( new QuitButton(), BorderLayout.EAST );
      
      add( buttonRow );
   }
   //+++++++++++++++++++++++ SeedSpinner class ++++++++++++++++++++
   /**
    * private inner class to update the spinner for recursive depth parameter.
    */
   private class SeedSpinner extends ParameterSpinner 
   {
      //---------------- constructor --------------------------------------
      public SeedSpinner () 
      {
         super( "Random seed", 1, TreeTasks.maxSeed, TreeTasks.rngSeed );
      }
      //------------------------- valueChanged( int ) -----------------------
      public void valueChanged( int newValue ) 
      {
         TreeTasks.rngSeed = newValue;
      }
   }
   
   //+++++++++++++++++++++++ DataSizeSlider class ++++++++++++++++++++
   /**
    * private inner class to update the child size ratio with a slider
    */
   private class DataSizeSlider extends ParameterSlider 
   {
      //---------------- constructor --------------------------------------
      public DataSizeSlider () 
      {
         super( "Data size", TreeTasks.minDataSize, TreeTasks.maxDataSize, 
                             TreeTasks.defaultDataSize );
      }
      //------------------- valueChanged( int ) -----------------------
      public void valueChanged( int newValue ) 
      {
         TreeTasks.dataSize = newValue;
      }
   }
}
