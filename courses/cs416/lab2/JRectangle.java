/**
 * JRectangle.java-- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class. 
 *    The class extends JComponent, but the interface is patterned
 *    after that of wheels. 
 * 
 * @author rdb
 * January 2008 (Gwheels)
 *
 * 01/24/09 rdb: Renamed Gwheels from previous semesters as JWheels
 * 01/25/09 rdb: Figured out correct way to set clipping regions with 
 *               arbitrary line thickness
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class JRectangle extends JAreaShape 
{
   //---------------- instance variables ------------------------
   //--------------------  constructors ---------------------------
   /**
    * Constructor from JRectangle
    */
   public JRectangle()
   { 
      this( Color.RED );
   }
   /**
    * Constructor from JRectangle
    */
   public JRectangle( Color aColor )
   { 
      this( 0, 0 );
      setColor( aColor );
   }
   /**
    * Another wheels-like constructor
    */
   public JRectangle( int x, int y )
   {
      super( x, y );
   }

   //----------------------- paintComponent( Graphics ) ------------------
   /**
    * paintComponent - calls draw and fill awt methods (this is an rdb method).
    */
   public void paintComponent( java.awt.Graphics brush )
   {
      super.paintComponent( brush );
      
      Graphics2D brush2 = (Graphics2D) brush;
      int w = getWidth();
      int h = getHeight();
      int lw = getLineWidth();
      
      // The Graphics2D object has a "clipping" window that will ignore any
      // attempt to draw outside of it. The default window is the size of the
      // component. Unfortunately, the border of a Rectangle is 1 pixel longer
      // and 1 pixel wider than the interior AND the size of the JComponent is 
      // determined by the interior. So, we want to create a clipping window 
      // that is  big enough to hold the boundary and the interior. Since
      // the boundary can have an arbitrary linewidth, need to have a clip
      // region that is "linewidth" bigger on ALL sides. Hence, it starts
      // at -linewidth, -linewidth, and its width and height are increased
      // by 2 * linewidth.
      //   
      brush2.setClip( -lw, -lw, w + 2 * lw, h + 2 * lw );
      
      // note that the location of the Rectangle is the location of the
      // JComponent. All drawing is relative to this location. Hence, we
      // draw all ellipses at ( 0, 0 ) -- their location in their JComponent.

      brush2.setColor( getFillColor() );
      brush2.fillRect( 0, 0, w, h );
      brush2.setStroke( new BasicStroke( lw ));
      brush2.setColor( getFrameColor() );
      brush2.drawRect( 0, 0, w, h );
   }   
}
