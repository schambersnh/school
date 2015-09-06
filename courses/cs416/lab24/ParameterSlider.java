/** 
 * ParameterSlider.java:
 * 
 * An abstract class that combines a label and slider and simplifies
 * the coded needed to modify a value. 
 * 
 * The class supports either direct values as int or % values as double. 
 * Children classes must override
 *        valueChanged( int )    --  if they are using direct values
 * or     valueChanged( double ) -- if they are using %
 *
 * @author Matthew Plumlee
 * CS416 Spring 2008
 */

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

abstract public class ParameterSlider extends JPanel {
   //-------------------- instance variables ----------------------------
   private boolean asPercent;    // true if should interpret values as %
   
   //------------------------ constructors ------------------------------
   /**
    * int argument constructor defines is a "value" slider
    *       (as opposed to a % slider).
    */
   public ParameterSlider ( String name, int min, int max, int val ) {
      super();
      init( name, min, max, val);
      asPercent = false;
   }
   
   /**
    * double argument constructor defines a % slider
    */
   public ParameterSlider ( String name, double min, double max, double val ) {
      super();
      init( name + " (%)", (int)( min * 100 ), 
                           (int)( max * 100 ), 
                           (int)( val * 100 ));
      asPercent = true;
   }
   //------------------ init( String, int, int, int ) ------------------------
   /**
    * Create the label and slider and initialize their parameters.
    */
   private void init ( String name, int min, int max, int val ) {
      JLabel label = new JLabel( name );
      label.setPreferredSize( new Dimension( 130, 40) );
      add( label, BorderLayout.WEST );
      
      JSlider slider = new JSlider( min, max, val );
      slider.addChangeListener( new ParameterListener());
      slider.setPaintTicks( true );
      slider.setPaintLabels( true );
      slider.setMajorTickSpacing( 10 );
      slider.setMinorTickSpacing( 1 );
      slider.setPreferredSize( new Dimension( 600, 40 ));
      add( slider );
   }

   //--- One of the following methods must be overridden by children ---------
   protected void valueChanged( int newValue ) {}      // override for value
   protected void valueChanged( double newPerCent ) {} // override for %
   
   //+++++++++++++++++++++++++++ ParameterListener class +++++++++++++++++++++
   /**
    * Inner class to serve as slider listener.
    */
   private class ParameterListener implements ChangeListener {
      // no constructor needed -- parent works fine
      //-------------- stateChanged( ChangeEvent ) -----------------
      /**
       * called when state of underlying slider changes
       */
      public void stateChanged( ChangeEvent e ) {
         if ( asPercent ) {
            valueChanged( ((JSlider)e.getSource()).getValue() / 100.0 );
         }
         else {
            valueChanged( ((JSlider)e.getSource()).getValue() );
         }
      }
   }
}