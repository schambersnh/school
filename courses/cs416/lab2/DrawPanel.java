/**
 * DrawPanel -- template for JPanel class for a Swing based application.
 */

import java.awt.*;
import javax.swing.*;

public class DrawPanel extends JPanel
{
   //------ instance variables for contents of panel -------
   // declare instance variables for graphical objects
   private JSnowMan man1;
   private JSnowMan man2;
   private JSnowMan man3;
   private ARectangle myRect;
   
   //------ Constructor ------------
   public DrawPanel()
   {
      super();
      setLayout( null );   // need this in order to put stuff in the panel.
      this.setBackground( Color.GRAY );
      
      // add creation of graphical objects, such as an ellipse
      man1 = new JSnowMan( 50, 50 );
      this.add(man1);
      man2 = new JSnowMan( 250, 50 );
      this.add(man2);
      man3 = new JSnowMan( 50, 300 );
      this.add(man3);
      
      // create two polygons; nothing will appear until
      //    JPolygon implementation is completed
      int[] houseX = { 0, 0, 15, 15, 25, 25, 40, 40, 20 };
      int[] houseY = { 20, 60, 60, 30, 30, 60, 60, 20, 0 };
      
      JPolygon house = new JPolygon( houseX, houseY );
      house.setFrameColor( Color.BLACK );
      house.setFillColor( Color.GREEN );
      this.add( house );
      house.setLocation( 400, 100 );
      
      int[] hexagonX = { 0, 0, 20, 40, 40, 20  };
      int[] hexagonY = { 20, 40, 60, 40, 20, 0 };
      JPolygon hex = new JPolygon( hexagonX, hexagonY );
      hex.setLocation( 500, 100 );
      this.add( hex );
      
      myRect = new ARectangle();
      myRect.setLocation(300, 300);
      
   }
   //--------- paintComponent( Graphics ) ---------------
   public void paintComponent( Graphics aBrush )
   {
      super.paintComponent( aBrush );
      
      // add code here to draw each object on the panel
      Graphics2D brush2D = (Graphics2D) aBrush;
 
      man1.display( brush2D );
      man2.display( brush2D );
      man3.display( brush2D );
      myRect.display( brush2D );
   }
}