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
   private TreeApp    _app;
   
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * and a reference to the application class.
    */
   public ControlPanel( Component parent, TreeApp app ) 
   {
      super( new GridLayout(0, 1) );
      _parent = parent;
      _app    = app;
            
      //create sliders
      add( new DataSizeSlider() ); 
      add( new NodeWidthSlider() );

      //create misc check boxes and quit button
      JPanel buttonRow = new JPanel();
      
      buttonRow.add( new SeedSpinner() );
       
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
         super( "Random seed", 
               -1, _app.maxSeed, _app.rngSeed );
      }
      //------------------------- valueChanged( int ) -----------------------
      public void valueChanged( int newValue ) 
      {
         _app.rngSeed = newValue;
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
         super( "Data size", _app.minDataSize, _app.maxDataSize, 
                             _app.defaultDataSize );
      }
      //------------------- valueChanged( int ) -----------------------
      public void valueChanged( int newValue ) 
      {
         _app.dataSize = newValue;
      }
   }
   //+++++++++++++++++++++++ NodeWidthSlider class ++++++++++++++++++++
   /**
    * private inner class to update the child size ratio with a slider
    */
   private class NodeWidthSlider extends ParameterSlider 
   {
      //---------------- constructor --------------------------------------
      public NodeWidthSlider () 
      {
         super( "Node width", _app.minNodeWidth, _app.maxNodeWidth, 
                             _app.defaultNodeWidth );
      }
      //------------------- valueChanged( int ) -----------------------
      public void valueChanged( int newValue ) 
      {
         _app.setNodeWidth( newValue );
      }
   }
}
