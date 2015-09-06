/**
 * JEllipse.java-- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class. 
 *    The class extends JComponent, but the interface is patterned
 *    after that of wheels. 
 * 
 * @author (of the modifications) rdb
 * January 2008
 *
 * 01/24/09 rdb: Renamed Gwheels from previous semester as JWheels.
 * 01/25/09 rdb: Figured out correct way to set clipping regions with 
 *               arbitrary line thickness.
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class JEllipse extends JAreaShape 
{
   //---------------- instance variables ------------------------
   //--------------------  constructors ---------------------------
   /**
    * Constructor from JEllipse
    */
   public JEllipse()
   { 
      this( Color.RED );
   }
   /**
    * Constructor from JEllipse
    */
   public JEllipse( Color aColor )
   { 
      super( aColor );
   }
   /**
    * Another wheels-like constructor
    */
   public JEllipse( int x, int y )
   {
      super( x, y );
   }

   //----------------------- paintComponent( Graphics ) ---------------------
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
      // component. Unfortunately, the border of a Ellipse is 1 pixel longer
      // and 1 pixel wider than the interior AND the size of the JComponent is 
      // determined by the interior. So, we want to create a clipping window 
      // that is  big enough to hold the boundary and the interior. Since
      // the boundary can have an arbitrary linewidth, need to have a clip
      // region that is "linewidth" bigger on ALL sides. Hence, it starts
      // at -linewidth, -linewidth, and its width and height are increased
      // by 2 * linewidth.
      //   
      brush2.setClip( -lw, -lw, w + 2 * lw, h + 2 * lw );
      
      // note that the location of the Ellipse is the location of the
      // JComponent. All drawing is relative to this location. Hence, we
      // draw all ellipses at ( 0, 0 ) -- their location in their JComponent.
      brush2.setColor( getFillColor() );      
      brush2.fillOval( 0, 0, w, h );
      brush2.setStroke( new BasicStroke( lw ));
      brush2.setColor( getFrameColor() );
      brush2.drawOval( 0, 0, w, h );
   }   
}
