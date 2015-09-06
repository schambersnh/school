/**
 * JPolygon.java-- a convenience class for simplifying access to awt graphics
 *    objects supported by the Graphics AWT class. 
 *    The class extends JComponent, but the interface is patterned
 *    after that of wheels. 
 * 
 * @author rdb
 *    derived from JRectangle
 *    contains a java.awt.Polygon instance
 */

import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

public class JPolygon extends JAreaShape 
{
   //---------------- instance variables ------------------------
   Polygon poly;
   int[] saveX;
   int[] saveY;
   
   //--------------------  constructors ---------------------------
   /**
    * Constructor from JPolygon
    */
   public JPolygon( int[] x, int[] y )
   { 
      super( );
      saveX = new int[x.length];
      saveY = new int[y.length];
      for(int i = 0; i < saveX.length; i++)
      {
        saveX[ i ] = x[ i ];
      }
      for(int i = 0; i < saveY.length; i++)
      {
        saveY[ i ] = y[ i ];
      }
    
      Polygon poly = new Polygon( x,  y,  x.length);
      this.setBounds(poly.getBounds());
      ///////////////////////////////////////////////////////////////
      // 1. Create an instance of java.awt.Polygon
      //        look it up on the web.
      //    Use the constructor that takes x and y coordinate arrays and
      //       a length. The length should be the minimum of the lengths
      //       of the x and y arrays passed to this constructor
      //    Assign the "new" Polygon to the instance variable "poly"
      //       that has already been declared. Don't 
      //
      // 2. Once you've created the polygon, you need to assign its
      //       bounds to the bounds of the JPolygon (which is a JComponent).
      //    JComponent has a setBounds method that takes a java.awt.Rectangle
      //       as its only argument.
      //    java.awt.Polygon has a "getBounds" method that has no parameters
      //       and returns a java.awt.Rectangle.
      ///////////////////////////////////////////////////////////////
      
      
   }

   //----------------------- paintComponent( Graphics2D ) ------------------
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
      brush2.fillPolygon( saveX, saveY, saveX.length );
      brush2.setStroke( new BasicStroke( lw ));
      brush2.setColor( getFrameColor() );
      brush2.drawPolygon(saveX, saveY, saveX.length );
      // All you need to change are the calls to fillRect and drawRect
      // Replace them with calls to fillPolygon and drawPolygon
      //     Look these methods up under the java.awt.Graphics2D
      //     class documentation on the web.
      /////////////////////////////////////////////////////////////////
   }   
}
