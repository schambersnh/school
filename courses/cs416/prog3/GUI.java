/**
  GUI.java
 *
 * Modified from GUI.java in Lab5 by Stephen Chambers for prog2 on 2/17/11
 * 
 */
import java.awt.*;
import java.util.*;
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
  private Point _gunEndPoint;
  private double _angle, dx, dy;
  private Pellet _pellet;
  private Vector<Pellet> myPellets = new Vector<Pellet>();
  private int _y = 30;
  private Vector<JRectangle> myTargets = new Vector<JRectangle>();
  private boolean isDown = false;
  private JPanel _panel;
  private int _pelletCount = 20;
  private Color[] myColors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW,
    Color.ORANGE, Color.MAGENTA, Color.PINK, 
    new Color(10, 235, 116), Color.RED, new Color(157, 94, 203)};
  
  
  
  //------------------------- constructor ----------------------------
  public GUI( int w, int h ) 
  {
    super();
    setLayout( new BorderLayout() );    // make sure no layout manager tries to 
    //   layout the objects added to this JPanel.
    JPanel panel = new JPanel();
    _panel = panel;
    
    _panel.setSize( w, h );
    _panel.setLayout( null );
    add( _panel );
    
    _gun = new JLine(6, 250, 26, 250);
    System.out.println("X: " + _gun.getXLocation() + "Y: " + _gun.getYLocation());
    _gun.setColor(Color.BLACK);
    _gunEndPoint = new Point();
    _gunEndPoint.x = 26;
    _gunEndPoint.y = 250;
    _gun.setThickness(15);
    
    //Create Targets
    makeTargets();
    
    //NOTE: Could not get the label to work, BUT the pellet count is outputed 
    //to the screen to show the grader that it decrements correctly.
    
    String pelletString = String.valueOf(_pelletCount);
    JLabel pelletsLeft = new JLabel("Pellets remaining: " + _pelletCount, JLabel.HORIZONTAL);
    _panel.add(pelletsLeft);

    
    
    _panel.add(_gun);
    this.add(_panel, BorderLayout.CENTER);
    
    buildGUI();    // add the GUI components for this app
    buildDisplay( _panel );        // build the initial display for the app
    
    this.setBackground( Color.white );
    
    
    _timer = new FrameTimer( INTERVAL, this );
    _timer.start();
  }
  //-----------------------  buildGUI() --------------------------
  /**
   * encapsulate code that builds GUI components for the program.
   */
  private void buildGUI()
  {
    // Put two buttons in the North
    String labels[] = { "Fire", "Restart" };
    JPanel northPanel = new JPanel();
    for ( int i = 0; i < labels.length; i++ )
    {
      JButton button = new JButton( labels[ i ] );
      button. addActionListener( new MyButtonListener( i, _panel ));
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
  private void buildSliders( Container parent, JLine gun )
  {
    
    LabeledSlider myAngle = new LabeledSlider("Angle", -45, 45, 0, 1);
    
    
    SliderListener aListen = new SliderListener(myAngle.getJSlider(), "angle"); 
    myAngle.addChangeListener(aListen);
    myAngle.setBorder( new LineBorder( Color.BLACK, 2 ));
    parent.add(myAngle, BorderLayout.WEST);
    
    LabeledSlider mySpeed = new LabeledSlider("Speed", 3, 10, 3, 1);
    SliderListener sListen = new SliderListener(mySpeed.getJSlider(), "speed"); 
    mySpeed.addChangeListener(sListen);
    mySpeed.setBorder( new LineBorder( Color.BLACK, 2 ));
    parent.add(mySpeed, BorderLayout.EAST);
    
    
  }
  private void makeTargets()
  {
    for(int i = 0; i < myTargets.size(); i++)
    {
      myTargets.remove(i);
    }
    for(int i = 0; i < Targets.numTargets; i++)
    {
      JRectangle temp = new JRectangle(335, _y);
      temp.setSize(20, Targets.targetHeight);
      temp.setColor(myColors[i]);
      myTargets.add(temp);
      _panel.add(temp);
      _y += Targets.targetHeight + 10;
    }
  }
  
//++++++++++++++++++++++ MyButtonListener ++++++++++++++++++++++++++
// public inner class for button event handler:
  public class MyButtonListener implements ActionListener
  {
    int _btnId;  // which button is associated with this ActionListener
    JPanel _panel;
    public MyButtonListener( int btnId, JPanel panel )
    {
      _btnId = btnId;   // save the id for the actionPerfomed invocation
      _panel = panel;
    }
    public void actionPerformed( ActionEvent ev )
    {
      switch (_btnId)
      {
        case 0:
          System.out.println( "Fire" );
          
          dx = _speed * Math.cos(_angle);
          dy = _speed * Math.sin(_angle);
          dy = -dy;
          
          _pellet = new Pellet(_gun.getXLocation(), _gun.getYLocation() - 3, 
                               dx, dy);
          _panel.add(_pellet);
          
         
          _pelletCount--;
          myPellets.add(_pellet);
          
          
          break;
        case 1: 
          System.out.println( "Restart" );
          makeTargets();
          _pelletCount = 20;
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
    private String      _id;
    
    public SliderListener( JSlider slider, String id )
    {
      _slider = slider;
      _id     = id;
      if(_id.equals("speed"))
      { 
        _speed = _slider.getValue();
      }
      
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
        int radius  = 20;
        double endY = radius * Math.sin(_angle);
        
        //rotate gun around endpoint
        _gun.setPoints(_gun.getXLocation(), _gun.getYLocation(), _gunEndPoint.x,
                       _gunEndPoint.y - (int)endY );
        _gun.getParent().repaint();
        
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
    System.out.println(_pelletCount);
    //When the last pellet leaves screen, print out game over and stop the timer
    if(_pelletCount == 0 && myPellets.size() == 0)
    {
      System.out.println("Game Over");
      _timer.stop();
    }
    _panel.repaint();
    
    for(int i = 0; i < myTargets.size(); i++)
    {
      if(_pellet!= null)
      {
        
      }
      if(isDown) //if its as low as it can go
      {
         //go up
        myTargets.get(i).setLocation(335, myTargets.get(i).getYLocation()-1);
      }
      if(!isDown) // if its as high as it can go
      {
        //go down
        myTargets.get(i).setLocation(335, myTargets.get(i).getYLocation()+1); 
      }
       //as low as it can go
      if(myTargets.get(myTargets.size() - 1).getYLocation() == 500)
      {
        isDown = true;
      }
      else if(myTargets.get(0).getYLocation() == 0) //as high as it can go
      {
        isDown = false;
      }
      
      if(_pellet != null)
      {
        for(int j = 0; j < myPellets.size(); j++)
        {
          //move all pellets in the array
        myPellets.get(j).move(myPellets.get(j).getdX(), myPellets.get(j).getdY());
        
        //if the pellet intersects with the target
        if(myPellets.get(j).getBounds().intersects(myTargets.get(i).getBounds()))
        {
          myTargets.get(i).setVisible(false); //remove target
          myPellets.get(j).setVisible(false); //remove pellet
          myPellets.remove(myPellets.get(j)); //remove pellet from vector
        }
        //if the pellet is out of the window
        else if(!(_panel.contains(myPellets.get(j).getLocation())))
        {
          myPellets.remove(myPellets.get(j)); //remove pellet from vector
        }
        }
        repaint();
      }
      
    }
  }   
//+++++++++++++++++++ application starter ++++++++++++++++++++
//-------------------------- main ----------------------------
  public static void main( String[] args )
  {
    
  }  
}
