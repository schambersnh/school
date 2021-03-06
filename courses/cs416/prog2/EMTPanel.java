/** 
 * EMTPanel.java: Swing panel for EMT motion assignment.
 *     This class is responsible for 
 *     1. Creating and storing the hospitals (already finished).
 *     2. Creating and storing the emergency sites; the sites are 
 *        created in the JPanel, in response to a MousePressed event
 *        at the site location.
 *     3. Creating the EMTVehicle that must do the traversal
 *     4. Creating the line object that shows where the vehicle
 *        is going. It might be responsible for updating this line;
 *        or that responsibility could be delegated to another class.
 *      
 * @author rdb
 * 02/02/11 Skeleton class created
 * 
 * Modified by Stephen Chambers for Prog 2 on 2/10/2011
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EMTPanel extends JPanel implements Animated, MouseListener
{ 
   //-------------------- class variables -------------------------
   
   //-------------------- instance variables ----------------------
   private JFrame               _frame;   
   private ArrayList<Hospital>  _hospitals;
   private Dispatcher myDispatcher;
   public static ArrayList<EmergencySite> _esites;
   public JLine                 _line;
   

   
   
   //--------------- constants -------------------------------------
   
   private Color[]    hospColors = { Color.WHITE, Color.RED, Color.PINK };
   private int[]      hospitalX = { 50, 500, 500, 250, 300 };
   private int[]      hospitalY = { 400, 100, 350, 400, 50 };
   private int        hospitalW = 80;
   private int        hospitalH = 60;
   
   
   //------------- Constructor ---------------------------------
   /**
    * Create and manage an EMTVehicle using Swing.
    * Create the hospital objects
    */
   public EMTPanel( JFrame frame ) 
   {
   
      super();
      _frame = frame;
      
      this.setLayout( null );
      this.addMouseListener( this);
      this.setBackground( Color.LIGHT_GRAY );
      
      Color emtColor = new Color( 0, 255, 0, 128 ); // semitransparent green
      
      EMTApp.numHospitals = Math.min( EMTApp.numHospitals, 
                                        hospitalX.length );
      
      
      
      
     
      //////////////////////////////////////////////////////////////
      // Create a line object that will show where the vehicle is going
      //   if it is going to an emergency site or a hospital.
      // Add it to the panel.
      // This object could be created in another class, but it does
      //    need to be added to this panel.
      //////////////////////////////////////////////////////////////
      
       _line = new JLine();
      this.add(_line);
      
      /////////////////////////////////////////////////////////////
      // Create an EMTVehicle and add it to the panel.
      // This also could be created in another class and added to
      //    this panel.
      /////////////////////////////////////////////////////////////
      
      EMTVehicle myVehicle = new EMTVehicle(0, 0, Color.RED);

      /////////////////////////////////////////////////////////////
      // Create a FrameTimer with an initial delay of 2000 millisecs
      //   and a delay of 100 millisecs.
      // Start the timer.
      /////////////////////////////////////////////////////////////
      
      FrameTimer timer = new FrameTimer( 100, this );
      timer.setInitialDelay(2000);
      timer.setDelay(100);
      timer.start();

      //////////////////////////////////////////////////////////////
      // Create a collection object to hold the emergency sites.
      //////////////////////////////////////////////////////////////
      
      _esites = new ArrayList<EmergencySite>();
      
      //----------- make the hospitals 
      makeHospitals();
      
      ///////////////////////////////////////////////////////////////
      // Create a Dispatcher, who is responsible for controlling the
      //    travels of the vehicle. 
      // The Dispatcher has to know about or have access to (at least)
      //    -- the collection of emergency sites, 
      //    -- the hospitals array list
      //    -- the timer
      /////////////////////////////////////////////////////////////// 

      myDispatcher = new Dispatcher(_hospitals,
                                    _esites, 
                                    myVehicle, 
                                    timer,
                                    _line);
      
      add(myVehicle);
      myVehicle.newFrame();
       
   
   } 
   //---------------------- makeHospitals() -------------------------
   /**
    * create the desired number of hospitals
    * This method is complete.
    */
   private void makeHospitals()
   {
      _hospitals = new ArrayList<Hospital>();
      for ( int s = 0; s < EMTApp.numHospitals; s++ )
      {
         int hx = hospitalX[ s ];
         int hy = hospitalY[ s ];
         Color hColor = hospColors[ s % hospColors.length ];
         Hospital h = new Hospital( hColor, hx, hy, hospitalW, hospitalH );
        
         _hospitals.add( h);
      }
   }
   
   //+++++++++++++++++++ Animated methods +++++++++++++++++++++++
   private boolean _animated = true;
   /**
    * return true always. Program makes no sense with animation off
    */
   public boolean isAnimated()
   {
      return _animated; // can't turn off animation
   }
   /**
    * cannot turn off animation, so this method is a no-op.
    */
   public void setAnimated( boolean onOff )
   {
   }
   //---------------------- newFrame ------------------------------
   /**
    * move emt for each new frame; this is called by the FrameTimer 
    * listener; it  gets invoked when the time interval elapses and awt 
    * creates an event.
    */
   public void newFrame()
   {  
     myDispatcher.newFrame();
      
      repaint();
   }

   //++++++++++++++++++ MouseListener methods +++++++++++++++++++++++++++
   // You need to implement mousePressed; the others must be there but
   //   will remain "empty".
   //
   //------------------- mousePressed( MouseEvent ) ----------------------
   /**
    * On mousePressed, replace current site with a site at the mouse position.
    */
   public void mousePressed( MouseEvent me )
   {      
      System.out.println("New site: " + me.getPoint());
      ////////////////////////////////////////////////////////////////
      // Need to add a new Emergency site here
      ////////////////////////////////////////////////////////////////
      EmergencySite esite1 = new EmergencySite(me.getPoint());
      this.add(esite1);
      
     
      _esites.add(esite1);
     
      
      repaint();      
   }
   //------------- unused interface methods -----------------
   public void mouseDragged( MouseEvent me ){ }
   public void mouseClicked( MouseEvent me ) {}
   public void mouseEntered( MouseEvent me ) {}
   public void mouseExited( MouseEvent me ) {}
   public void mouseMoved( MouseEvent me ) {}
   public void mouseReleased( MouseEvent me ){}
   //++++++++++++++++++++++ end Mouse Listener methods ++++++++++++++++++
   
   //----------------- paintComponent( Graphics) -------------------------
   /**
    * Call AWT object display methods
    */
   public void paintComponent( Graphics g )
   {
     
      super.paintComponent( g );
      for ( Hospital h: _hospitals )
         h.display( (Graphics2D ) g );
      
      
   }
   
   
      
   //++++++++++++++++++++++ convenience system test ++++++++++++++++++++++++++
   //------------------------ main -----------------------------------
   public static void main( String[] args )
   {
      EMTApp.main( args );
   }
}
