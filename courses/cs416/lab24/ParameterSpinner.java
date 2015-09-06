/** 
 * ParameterSpinner.java:
 * An abstract class that combines a spinner and a label and makes 
 * it easier to modify a value.
 * 
 * Child classes must define the valueChanged( int ) method.
 *
 * Matthew Plumlee
 * CS416
 * Section 1
 */

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

abstract public class ParameterSpinner extends JPanel {
   //---------------- instance variables -------------------------

   //------------------- constructor ------------------------------
   /**
    * Constructor specifies min, max  and initial values.
    */
    public ParameterSpinner ( String name, int min, int max, int val ) {
      super();
      init( name, min, max, val);
   }
   //------------------------ init( String, int, int, int ) ------------------
   /**
    * create the label and spinner and set the parameters.
    */
   private void init ( String name, int min, int max, int val ) {
      JLabel label = new JLabel( name );
      label.setSize( new Dimension( 40, 30 ));
      add( label, BorderLayout.WEST );
      
      javax.swing.SpinnerModel model = new javax.swing.SpinnerNumberModel( val, min, max, 1 );
      JSpinner spinner = new JSpinner( model );
      spinner.addChangeListener( new ParameterListener());
 
      spinner.setPreferredSize( new Dimension( 60, 40 ));
      add(spinner);
   }

   //------------------------- valueChanged( int ) ---------------------------
   /**
    * called when spinner value is changed; must be defined by children classes.
    */
   abstract protected void valueChanged( int newValue );
   
   //++++++++++++++++++++++++ ParameterListener inner class +++++++++++++++++
   /**
    * Spinner listener
    */
   private class ParameterListener implements ChangeListener {
      // no constructor needed -- parent works fine
      //------------------------ stateChanged( ChangeEvent )------------------
      public void stateChanged( ChangeEvent e ) {
         Number newValue = (Number)((JSpinner)e.getSource()).getValue();
         valueChanged( newValue.intValue() );
      }
   }   
}