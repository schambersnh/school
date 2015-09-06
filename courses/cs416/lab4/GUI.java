/**
 * Chapter 7: GUI.java
 * Creates the panel to be placed inside the SwingApp window.
 *
 * 1/30/08: rdb
 *    Renamed (old name was BallApp)
 * 01/28/09: rdb
 *    Revised to use null Layout
 * 
 * modifed by Stephen Chambers for Lab 5 on 2/8/2011
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.LineBorder;

public class GUI extends JPanel implements Animated 
{
  //------------------------- instance variables -----------------------
  
  
  private FrameTimer _timer;
  private final int INTERVAL = 100;
  private JLine _gun;
  private int _speed;
  private double _angle;
  
  //------------------------- constructor ----------------------------
  public GUI( int w, int h ) 
  {
    super();
    setLayout( new BorderLayout() );    // make sure no layout manager tries to 
    //   layout the objects added to this JPanel.
    JPanel panel = new JPanel();
    
    panel.setSize( w, h );
    panel.setLayout( null );
    add( panel );
    
    buildGUI();    // add the GUI components for this app
    buildDisplay( panel );        // build the initial display for the app
    
    this.setBackground( Color.white );
    
    _gun = new JLine(6, 250, 26, 250);
    _gun.setThickness(15);
    panel.add(_gun);
    this.add(panel, BorderLayout.CENTER);
    
    _timer = new FrameTimer( INTERVAL, this );
    _timer.start();
  }
  //-----------------------  buildGUI() --------------------------
  /**
   * encapsulate code that builds GUI components for the program.
   */
  private void buildGUI( )
  {
    // Put two buttons in the North
    String labels[] = { "Fire", "Restart" };
    JPanel northPanel = new JPanel();
    for ( int i = 0; i < labels.length; i++ )
    {
      JButton button = new JButton( labels[ i ] );
      button. addActionListener( new MyButtonListener( i ));
      northPanel.add( button );
    }
    add( northPanel, BorderLayout.NORTH );
    
    
  } 
  //-----------------------  buildDisplay() --------------------------
  /**
   * encapsulate code that builds display components for startup.
   */
  private void buildDisplay( JPanel panel )
  {
    // ADD DISPLAY HERE
    buildSliders(this, _gun);
  }
  private void buildSliders( Container parent, JShape gun )
  {
    
    LabeledSlider myAngle = new LabeledSlider("Angle", -45, 45, 0, 1);
    
    
    SliderListener aListen = new SliderListener(myAngle.getJSlider(), gun, "angle"); 
    myAngle.addChangeListener(aListen);
    myAngle.setBorder( new LineBorder( Color.BLACK, 2 ));
    parent.add(myAngle, BorderLayout.WEST);
    
    LabeledSlider mySpeed = new LabeledSlider("Speed", 0, 30, 15, 1);
    SliderListener sListen = new SliderListener(mySpeed.getJSlider(), gun, "speed"); 
    mySpeed.addChangeListener(sListen);
    mySpeed.setBorder( new LineBorder( Color.BLACK, 2 ));
    parent.add(mySpeed, BorderLayout.EAST);
   
    
  }
  
//++++++++++++++++++++++ MyButtonListener ++++++++++++++++++++++++++
// public inner class for button event handler:
  public class MyButtonListener implements ActionListener
  {
    int _btnId;  // which button is associated with this ActionListener
    public MyButtonListener( int btnId )
    {
      _btnId = btnId;   // save the id for the actionPerfomed invocation
    }
    public void actionPerformed( ActionEvent ev )
    {
      switch (_btnId)
      {
        case 0:
          System.out.println( "Fire" );
         /*
         * Calculate dx dy
         * dx = speed * cos()
         * dy = speed * sin()
         * Put pellets in a vector
         * */
          break;
        case 1: 
          System.out.println( "Restart" );
          break;   
      }
      //////////////////////////////////////////////////////////////
      // add code here to test _btnId and do what needs to be
      //   done for each button. 
      // As an inner class, code here has access to all instance
      //   variables and methods of the outer class.
      //////////////////////////////////////////////////////////////
    }
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
      if(_id.equals("speed"))
      { 
        _speed = _slider.getValue();
      }
      else if(_id.equals("angle"))
      {
        _angle = Math.toRadians(_slider.getValue());
        //compute Angle here
        
        
        
        
        
        System.out.println("Angle: " + _angle + "\nXLine: " + _gun.getX() + "\nYLine: " + _gun.getY());
     
        
   
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
  
  
//++++++++++++++++++++++++ Animated interface methods +++++++++++++++++++++
  private boolean _animated = true;  // instance variable
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
//-------------------------- newFrame() --------------------------------
  /**
   * implements Animated interface. On each time event, 
   * move each component
   */
  public void newFrame() 
  {
    
  }   
//+++++++++++++++++++ application starter ++++++++++++++++++++
//-------------------------- main ----------------------------
  public static void main( String[] args )
  {
    
  }  
}
