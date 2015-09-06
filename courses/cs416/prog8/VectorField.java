/**
 * VectorField.java
 * 
 * Michael DuBreuil
 * Summer 2010
 * 
 * Modified by rdb Spring 2011
 */
import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JPanel;

public class VectorField extends JPanel
  implements ComponentListener
{
  //----------------- class variables ------------------------------------
  //---- package variables defining metadata information about files
  //     and initial parameter settings; set by FlowQuad
  static File    xData;     // file of x vector components
  static File    yData;     // file of y vector components
  static int     width;     // size of vector field in x direction
  static int     height;    // size of vector field in y direction
  static int     displayScale; // scale factor for drawing
  static double  vectorScale;  // scale factor for vector coords (pre-display)
  static int     numParticles; // # particles to use
  static int     timerDelay;   // millisecs between frames
    
 
  
  public static int    stepFactor = 3;// resolution reduction factor for field
  
  private static Vector2D[][] vectors;
  
  //----------------- instance variables ---------------------------------
  // scalars that control the amount the graphical size of the field is
  // scaled, and the vectors, this allows vectors to be drawn larger or
  // smaller independently of field size
  
  private int  maxDepth = 4;
  private int  minSize  = 4;   // don't subdivide anything smaller than 4x4
  
  private int displayWidth;  // panel width
  private int displayHeight; // panel height
  
  // booleans to control how the vectors are displayed
  // end fixture supersedes dual tone, if both false draws simple lines
  public boolean dualTone = false; // uses two colors to draw the vectors
  public boolean endFixture = true; // draw small circle at end of vector

  public  boolean showTree = true; // show the quadtree nodes
  public  boolean showField = true; // show the vector feild
  public  boolean showParticles = true; // show the particles
  
  private QuadTreeRake quadTreeRake; // particle emitter
  public  QNode _root;                // root node of the quad tree
  public  ArrayList<Particle> interNodeParticles;
  
  private double minStdDev = 0;
  private double minPerCentSD = 0.75;
  
  
  //--------------------------- constructor --------------------------->
  public VectorField() 
  {
    super();
    this.setSize( 
                 new Dimension( width * displayScale, height * displayScale ));
    if ( xData.length() != yData.length() ) 
    {
      System.err.println( "File size mismatch: x, y file lengths: " 
                           + xData.length() + " x "  + yData.length());
      System.exit(  1 );
    }
    vectors = new Vector2D[ width ][ height ];
    readData( xData, yData );
    buildTree();
    quadTreeRake = new QuadTreeRake( this, timerDelay, numParticles );      
    addComponentListener( this );  // ComponentListener gets window resize
  }
  //---------------------- buildTree() ----------------------------------
  /**
   * build the quad tree
   */
  public void buildTree()
  {
    if(showTree)
    {
    _root = new QNode(new Rectangle(width, height));
    _root.divide(maxDepth, 1, minStdDev);
    }
  }
  //-----------------------drawTree()------------------------------------
  public void drawTree(Graphics g)
  {
    _root.draw(g);
  }
  //---------------------- restart() ----------------------------------
  /**
   * restart particle flow; need to remove all previous particles from
   * the panel.
   */
  public void restart()
  {     
  }
  //------------------------- getVector( Point2D.Float ) ----------------
  /*
   * getVector( Point2D.float ) returns the velocity from the vector field all
   * positions are turned into positive values
   */
  public static Vector2D getVector( Point2D.Float position ) 
  {
    return getVector( (int)Math.abs( position.y ), 
                     (int)Math.abs( position.x ) );
  }   
  
  //------------------------- getVector( int, int ) ----------------
  /*
   * getVector( int, int ) returns the velocity from the vector field all
   * positions are turned into positive values
   */
  public static Vector2D getVector( int y, int x ) 
  {
    return vectors[y][x];
    
  }   
  
  //------------------------------ newFrame() -----------------------------
  /*
   * newFrame() moves all the particles forward in time based on vector
   */
  public void newFrame() 
  {
   repaint(); 
  }
  //----------------------- paint( Graphics ) ---------------------------
  /**
   * paint the field, the particles and the tree 
   */
  public void paint( Graphics g ) 
  {
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    if(showField)
    {
      for(int i = 0; i < vectors[0].length; i += stepFactor)
      {
        for(int j = 0; j < vectors.length; j += stepFactor)
        {
          if(Vector2D.showEnd || Vector2D.dualTone)
          {
            vectors[i][j].draw(g, i, j, Color.RED, vectorScale*stepFactor);
          }
          else
          {
            vectors[i][j].drawLine(g, i, j, Color.RED, vectorScale*stepFactor);
          }
        }
      }
    }
      if(showTree)
      drawTree(g);
  }
  
  //------------------- addParticle( Particle ) --------------------
  /**
   * Adds a particle to a node, but only actually adds it to a leaf of the tree
   */
  public void addParticle( Particle particle ) 
  {
    //_root.add(_root, particle);
  }
  
  //+++++++++++++++++++++ accessor/mutator methods ++++++++++++++++++++++
  /**
   * set the vectorScale value
   */
  public void setVectorScale( double vScale ) 
  {
    vectorScale = vScale;
  }
  //---------------------- setMaxDepth( int ) -------------------
  /**
   * Set the maximum depth allowed for expanding the quad tree
   *  and rebuild the tree.
   */
  public void setMaxDepth( int max )
  {
    maxDepth = max;
    buildTree();
  }
  //---------------------- setPerCentMinStdDev( double ) -------------------
  /**
   * std deviation defines criteria for whether a quadtree node has chidren.
   * We base the std deviation test as a percentage of the s.d. for the 
   * whole tree:
   *            minStdDev = minPerCentStdDev * rootStdDev
   *            if stdDev in the node < minStdDev, don't subdivide
   */
  public void setPerCentMinStdDev( double min )
  {
    double rootStdDev = _root.calculateStdDeviation();
    minStdDev = min * rootStdDev;
    buildTree();
  }
  
  
  ///////////////////////////////////////////////////////////////////////////
  //
  // You probably don't need to change anything below here
  //
  ///////////////////////////////////////////////////////////////////////////
  
  //---------------------- getRake() ---------------------------------------
  /**
   * return the particle generator
   */
  public QuadTreeRake getRake() 
  {
    return quadTreeRake;
  }
  //------------------------- randomPoint( ) ----------------
  /*
   * returns a random integer point in the vector field as a Point2D.Float
   */
  public static Point2D.Float randomPoint() 
  {
    return new Point2D.Float( (int)( Math.random() * width ), 
                             (int)( Math.random() * height ));
  }
  //---------------------- readData( File, File ) -------------------
  /**
   * Read and combine 2 input files, one with x coord of flow, other sith
   * y coord of flow.
   * 
   * First line has nRows nCols
   */
  private void readData( File xInput, File yInput )
  {
    /*
     * assuming the files are the size reported to us, else crash out
     * if there's more in the file after specified amount it is ignored
     */
    try {
      DataInputStream dataX = 
        new DataInputStream( new FileInputStream( xInput ));
      DataInputStream dataY = 
        new DataInputStream( new FileInputStream( yInput ));
      
      for (  int y = 0; y < height; y++ ) 
      {
        for (  int x = 0; x < width; x++ ) 
        {
          vectors[ y ][ x ] = new Vector2D( 
                                           dataX.readFloat(), dataY.readFloat());
        }
      }
      dataX.close();
      dataY.close();
    } catch ( IOException e ) {
      System.err.println( e.toString());
      System.exit( 1 );
    }
  }
  //+++++++++++++++++++++ ComponentListener methods +++++++++++++++++++++++
  //----------------- unimplemented -------------------------------------
  public void componentHidden( ComponentEvent e ){}
  public void componentShown( ComponentEvent e ){}
  public void componentMoved( ComponentEvent e ){}
  //------------------ componentResized( ... ) --------------------------
  public void componentResized( ComponentEvent e )
  {
    displayWidth = getWidth();
    displayHeight = getHeight();
    int xScale = Math.round( (float) displayWidth / width );
    int yScale = Math.round( (float) displayHeight / height );
    this.displayScale = Math.max( xScale, yScale );
    
    // heuristic to guess at a reasonable stepFactor
    if ( displayScale >= 12 )
      stepFactor = 1;
    else if ( displayScale >= 9 )
      stepFactor = 2;
    else if ( displayScale >= 6 )
      stepFactor = 3;
    else if ( displayScale >= 3 )
      stepFactor = 4;
    else 
      stepFactor = 5;
    System.out.println( "Resizing: " + getSize() + " : displayScale = " 
                         + displayScale + " fieldSkip = " + stepFactor );
  }
  
  //--------------------- main --------------------------------------------
  /**
   * Convenience main to invoke app
   */
  public static void main( String[] args )
  {
    FlowQuad.main( args );
  }
}