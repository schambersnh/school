/*
 * FlowQuad.java
 * Michael du Breuil
 * 
 * Summer 2010
 * 
 * Edited by rdb, March 2011
 */

//package FlowQuad;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class FlowQuad extends JFrame 
{
   public FlowQuad( String title ) 
   {
      super( title );
      JPanel contentPanel = new JPanel( new BorderLayout());
     
      VectorField field = new VectorField();
      
      // The interaction control panel is a separate window
      contentPanel.add( field, BorderLayout.CENTER );
      GUIPanel control = new GUIPanel( field );
      control.setLocation( 700, 0 );
      control.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      this.add( contentPanel );
     
      // The initial window size is good for a square vector field with sides
      // that are a power of 2, plus some extra in x and y (8 and 30) that 
      // appear to be about right for window pieces outside the draw area.
      this.setSize( new Dimension( 768  + 8, 768 + 30 ));
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      //  make both windows visible
      this.setVisible( true );
      control.setVisible( true );
   }
   
   //-------------------------------- main -------------------------------
   public static void main( String[] args ) 
   {
      String fileName;
      if ( args.length > 0 )
         fileName = args[ 0 ];
      else 
         fileName = Utils.getFileName( "Choose a flow field metadata file" );
      Properties metaData = new Properties();
      try {
         metaData.load( new FileInputStream( fileName ));
      } 
      catch ( IOException fnf ) {
         System.err.println( "Can't find file: " + fileName );
         System.exit( -1 );
      }
      // ----- get all the properties from the prpoerty file and
      //  
      VectorField.xData = new File( metaData.getProperty( "xfile" ));
      VectorField.yData = new File( metaData.getProperty( "yfile" ));
      VectorField.width = Utils.toInt( metaData.getProperty( "xsize" ), 128 );
      VectorField.height = Utils.toInt( metaData.getProperty( "ysize" ), 128 );
      VectorField.displayScale 
         = Utils.toInt( metaData.getProperty( "displayScale" ), 6 );
      VectorField.vectorScale 
         = Utils.toDouble( metaData.getProperty( "vectorScale" ), 10.75 );
      
      VectorField.numParticles 
         = Utils.toInt( metaData.getProperty( "numParticles" ), 200 );
      VectorField.timerDelay = Utils.toInt( metaData.getProperty( "delay" ), 30 );

      FlowQuad vectorField = new FlowQuad( "Static Vector Field" );
   }
}
