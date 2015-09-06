/**
 * EmergencySite -- represents an emergency site. 
 *         Dragging (mousePressed followed by mouseDragged) repositions it
 *            Not allowed to drag if its the current EmergencySite, easiest
 *            way to do that is make that site not draggable
 *         MouseClicked -- if clicked, this site is no longer an emergency 
 *            site; remove it from the set of sites or somehow make it known
 *            that it is not real emergency site any more.
 * 
 * @author rdb
 * 02/02/11 
 * 
 * Modified by Stephen Chambers for Prog2 on 2/11/2011
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class EmergencySite extends JRectangle
                     implements MouseListener, MouseMotionListener, Draggable
{
   //--------------------- class variables --------------------------------
   public static int    size = 10;
   
   //--------------------- instance variables -----------------------------
   private boolean     _visited = false;;
   
   //--------------------- constants --------------------------------------
   private Color  fillColor = Color.RED;
   private Color  lineColor = Color.BLACK;
   
   //---------------------- constructors ----------------------------------
   /**
    * Generate a site at the specified location.
    */
   public EmergencySite( Point p)
   {
      super( p.x, p.y );
      setFillColor( fillColor );
      setFrameColor( lineColor );
      setSize( size, size );
      
      addMouseListener( this );
      addMouseMotionListener( this );
   }
   //++++++++++++++++++++++ Draggable interface methods +++++++++++++++++++
   private boolean   _draggable = true; // true if this object can be dragged
   public void setDraggable( boolean onOff )
   {
      _draggable = onOff;
   }
   public boolean isDraggable()
   {
      return _draggable;
   }
   public boolean contains( java.awt.geom.Point2D point )
   {
      return getBounds().contains( point );
   }
   
   //++++++++++++++++++++ mouse methods / instance variables ++++++++++++++++
   private Point _saveMouse;   // instance variable for last mouse position
                               //   used for dragging      
   //+++++++++++++++++++++++++ mouseListener methods ++++++++++++++++++++++++
   //-------------- mousePressed -----------------------------------
   public void mousePressed( MouseEvent me )
   {
      ////////////////////////////////////////////////////////////////////
      // Gets the position of the mouse in the container that holds the
      // JComponent: getParent().getMousePosition() and assigns it to the 
      // instance variable, _saveMouse
      ////////////////////////////////////////////////////////////////////
         _saveMouse = getParent().getMousePosition();
   }
   //-------------- mouseClicked -----------------------------------
   public void mouseClicked( MouseEvent me )
   {
     //Set the color to blue and remove it from the sites ArrayList
     setColor(Color.BLUE);
     EMTPanel._esites.remove(this);
   }
 
   //--------------- unimplemented mouse listener methods ---------------------
   public void mouseReleased( MouseEvent me ){}
   public void mouseEntered( MouseEvent me ){}
   public void mouseExited( MouseEvent me ){}
   
   //+++++++++++++++++++ mouseMotionListener methods ++++++++++++++++++++++++
   //---------------- mouseDragged ----------------------------------------
   public void mouseDragged( MouseEvent me )
   {
     /*
      * If draggable, compute the difference between mouse positions and move
      * the EmergencySite by the amount. Then set the instance variable 
      * _saveMouse to this temporary point
      * */
       Point temp = getParent().getMousePosition();
    
    if (temp != null && isDraggable())
    {
      int newX = temp.x;
      int newY = temp.y;
      int diffX = newX - _saveMouse.x;
      int diffY = newY - _saveMouse.y;
      moveBy( diffX, diffY );
      _saveMouse = temp;
    }
    else
    {
      return;
    }
   }

   //----------------- mouseMoved not implemented --------------------------
   public void mouseMoved( MouseEvent me ){}
   //+++++++++++++++++ end MouseMotionListeners ++++++++++++++++++++++++++++
   
   //--------------------- main -----------------------------------
   /**
    * unit test
    */
   public static void main( String[] args )
   {     
      JFrame testFrame = new JFrame();
      testFrame.setSize( 700, 500 );  // define window size
      
      testFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      JPanel testPanel = new JPanel( (LayoutManager) null );
      testFrame.add( testPanel );
      
      EmergencySite s1 = new EmergencySite( new Point( 200, 200 ));
      testPanel.add( s1 );
      
      EmergencySite s2 = new EmergencySite( new Point( 200, 100 ));
      testPanel.add( s2 );
 
      testFrame.setVisible( true );  // Initially, JFrame is not visible, make it so.

   }
}