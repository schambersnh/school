
/**
 * RadioSliderGUI -- Demo program and lab using JSlider and JRadioButton
 * 
 * 02/04/09 rdb
 * Modified by Stephen Chambers on 2/10/2011
 */
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class RadioSliderGUI extends JPanel
{
   //---------------- class variables ------------------------------  
   //--------------- instance variables ----------------------------   
   //------------------- constructor -------------------------------
   /**
    * Constructor gets passed the containing JFrame
    */
   public RadioSliderGUI()     
   {
      this.setLayout( new BorderLayout() );
      JPanel drawPanel = new JPanel();
      drawPanel.setLayout( null );
      this.add( drawPanel );
            
      // create a single rectangle in the middle of the window.
      //   the position, size and color will be controlled by the GUI
      JShape target = new JRectangle( Color.BLUE );
      target.setSize( 40, 40 );
      target.setLocation( 250, 250 );
      drawPanel.add( target );
      
      // build the sliders to control position and size
      buildSliders( this, target );
      
      // build the radio button panel to change the color
      buildRadio( this, target );
   }
  
   //------------------- buildSliders ---------------------------------
   /**
    * Create 3 sliders and add using border layout:
    *   First argument is the containing JFrame into which the sliders
    *     will be placed
    *   2nd argument is a reference to the JShape that is being built.
    * 
    *   South region will have a horizontal slider controlling the x position
    *   West region will have a vertical slider controlling the y position 
    *   East region will have a slider controlling the size
    */
   private void buildSliders( Container parent, JShape target )
   {
      //------------- X Slider  ------------------------------------
      // a horizontal slider in the south panel
      //
      JSlider xSlider = new JSlider( JSlider.HORIZONTAL, 0, 500, 250 );
      addLabels( xSlider, 100 );
      SliderListener sListen = new SliderListener( xSlider, target, "x" );
      xSlider.addChangeListener( sListen );
      xSlider.setBorder( new LineBorder( Color.BLACK, 2 ));
      parent.add( xSlider, BorderLayout.SOUTH );
      
      //------------- Y Slider  ------------------------------------
      //////////////////////////////////////////////////////////////////
      // 1. Copy and edit the above X slider code to make a Y slider
      //    in the EAST border region.
      //    Add code to the SliderListener code to process the X-slider events
      // 2. Copy and edit again to create the Size slider for controlling 
      //    size of the target JShape.
      //    Add code to SliderListener to process S-slider events.
      //////////////////////////////////////////////////////////////////
      JSlider ySlider = new JSlider( JSlider.VERTICAL, 0, 500, 250 );
      addLabels( ySlider, 100 );
      SliderListener sListen2 = new SliderListener( ySlider, target, "y" );
      ySlider.addChangeListener( sListen2 );
      ySlider.setBorder( new LineBorder( Color.BLACK, 2 ));
      ySlider.setInverted(true);
      parent.add( ySlider, BorderLayout.WEST );
      
      //------------- S (size) Slider  ------------------------------------
      // 2. Copy and edit again to create the Size slider for controlling 
      //    size of the target JShape.
      //    Add code to SliderListener to process S-slider events.
      //////////////////////////////////////////////////////////////////
      JSlider sSlider = new JSlider( JSlider.VERTICAL, 4, 100, 50 );
      addLabels( sSlider, 24 );
      SliderListener sListen3 = new SliderListener( sSlider, target, "s" );
      sSlider.addChangeListener( sListen3 );
      sSlider.setBorder( new LineBorder( Color.BLACK, 2 ));
      parent.add( sSlider, BorderLayout.EAST );
   }  
   //---------------- addLabels( JSlider, int ) -----------------------
   /**
    * a utility method to add tick marks.
    * First argument is the slider, the second represents the
    *   major tick mark interval
    *   minor tick mark interval will be 1/10 of that.
    */
   private void addLabels( JSlider slider, int majorTicks )
   {
      slider.setPaintTicks( true );
      slider.setPaintLabels( true );
      slider.setMajorTickSpacing( majorTicks );
      slider.setMinorTickSpacing( majorTicks / 10 );
   }
   //++++++++++++++++++++++++ SliderListener inner class ++++++++++++++++++++++
   /**
    * The SliderListener needs access to 
    *   -- the slider it is associated with (to get that slider's value)
    *   -- the JShape that is being controlled.
    *   -- a string that serves as an identifier for the slider
    * These are passed to the constructor.
    */
   public class SliderListener implements ChangeListener
   {
      private JSlider     _slider;
      private JComponent  _target;
      private String      _id;
      
      public SliderListener( JSlider slider, JComponent target, String id )
      {
         _target = target;
         _slider = slider;
         _id     = id;
      }
      //------------------- stateChanged -----------------------------
      /**
       * Invoked whenever user changes the state of a slider
       */
      public void stateChanged( ChangeEvent ev )
      {
        if(_id.equals("x"))
        { 
        _target.setLocation(_slider.getValue(), _target.getY());
        _target.getParent().repaint();
        }
        else if(_id.equals("y"))
        {
        _target.setLocation(_target.getX(), _slider.getValue());
        _target.getParent().repaint();
        }
        else if(_id.equals("s"))
        {
          _target.setSize(_slider.getValue(), _slider.getValue());
          _target.getParent().repaint();
        }

        //////////////////////////////////////////////////////////////
         // 1. add code to respond to the x-slider. it needs to
         //   change the x-position of the target rectangle
         // 2. After adding the y-slider, need to test here which slider
         //    generated the event; can do that by testing the
         //    _id field that was set in the constructor. 
         //    Compare it (using the String equals method) to the 
         //    String used to create this instance of the SliderListener.
         // 3. After adding the size slider, need to augment this code
         //    to identify and handle events from that slider.
         /////////////////////////////////////////////////////////////
         
         
      }
   }

   //--------------------- buildRadio ------------------------------------
   /**
    * build a radio button panel with exclusive behavior (1 button pressed
    *   at a time.
    * The first argument is the containing JFrame for the button panel
    * The 2nd argument references the rectangle whose color is changed
    *    by button presses.
    */
   private void buildRadio( Container parent, JShape target )
   {  
      // The ButtonGroup defines a set of RadioButtons that must be "exclusive"
      //    -- only 1 can be "active" at a time.
      
      ButtonGroup bGroup = new ButtonGroup();
      JPanel      bPanel = new JPanel(); // defaults to FlowLayout
      bPanel.setBorder( new LineBorder( Color.BLACK, 2 ));
      
      String[]  labels = { "Red", "Blue", "Green", "Cyan" };
      Color[] myColors = { Color.RED, Color.BLUE, Color.GREEN, Color.CYAN };
      // for each entry in the labels array, create a JRadioButton
      for ( int i = 0; i < labels.length; i++ )
      {
         JRadioButton button = new JRadioButton( labels[ i ] );
         ButtonListener bListen =  new ButtonListener( target, myColors[i] );
         button.addActionListener( bListen );
         //bGroup.add( button );
         bPanel.add( button );
      }     
      
      parent.add( bPanel, BorderLayout.NORTH );
   
   }
   //++++++++++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++
   /**
    * This version of the innter ButtonListener class constructor has 2 args:
    *   the JShape being colored 
    *   the color to be assigned
    */
   public class ButtonListener implements ActionListener
   {
      //------ instance variables ---------------
      JShape _target;
      Color _Color;
      
      public ButtonListener( JShape target, Color c )
      {
         // save the parameters as instance variables of the inner class
         _target = target;
         _Color = c;
      }
      public void actionPerformed( ActionEvent ev )
      { 
         // get a reference to the radio button that just got pressed.
         JRadioButton button = (JRadioButton) ev.getSource();
         
         String buttonLabel = button.getText(); // get its text field.
         System.out.println( buttonLabel  + ": Action event.  " );
         
         // update color and repaint target
         _target.setColor(_Color);
         _target.repaint();
      }
   }
   //+++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++++
   /**
    * A convenience main to start main application;RadioSliderLab.main
    */
   public static void main( String[] args )
   {
      RadioSliderLab.main( args );
   }
}
