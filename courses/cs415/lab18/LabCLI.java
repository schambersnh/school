/**
 * LabCLI.java 
 * 
 * @author rdb
 * 11/02/09
 * 11/04/07
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class LabCLI extends Frame
{
  //------------------- class variables ------------------------- 
  
  //------------------ instance variables -----------------------
  private Random     rng;         // random number generator for  positions.
  
  private int minSize = 10;    // minimum size for rectangles and ellipses
  private int maxSize = 70;    // maximum size for rectangles and ellipses
  private int maxX    = 450;   // max X position
  private int maxY    = 400;   // max Y position
  private Color curColor;
  
  //------------------ Constructors       -----------------------
  public LabCLI( String[] args )
  {
    Scanner reader;         // We'll do input with a Scanner
    rng = new Random( 1 );  // set up random number generator
    
    String fileName = null;
    curColor = Color.BLUE;
    
    if ( args.length > 0 )
      fileName = args[ 0 ];
    reader = makeScanner( fileName );
    
    while ( reader.hasNextLine() )
    {
      String line = reader.nextLine();
      line = line.trim();
      System.out.println( "Input:" + line );
      if (line.length() > 0)
      {
        parse( line );
      }
      
    }      
  }
  //------------------ parse ----------------------------
  /**
   * parse the input line
   */
  private void parse( String line )
  {
    if (!line.startsWith("#"))
    {
      if (line.startsWith("b") || line.startsWith("B"))
      {
        doB(line);
      }
      else if (line.startsWith("c") || line.startsWith("C"))
      {
        doC(line);
      }
      else if (line.startsWith("d") || line.startsWith("D"))
      {
        doD(line);
      }
      else if (line.startsWith("e") || line.startsWith("E"))
      {
        doE(line);
      }
      else if (line.startsWith("r") || line.startsWith("R"))
      {
        doR(line);
      }
      
      else
      {
        System.out.println("Command not recognized");
      }
    }
  }
  //----------------- doB ------------------------
  /**
   *
   */
  private void doB( String line )
  {
    System.out.println("B: " + line);
  }
  
  //----------------- doC ------------------------
  /**
   * This will become a color definition command
   */
  private void doC( String line )
  {
    // The following declaration defines an array of 4 Strings and initializes 
    // the elements of the array to the specified values.
    String color = line.substring(line.indexOf(" "), line.length()).trim();
    String colors[] = { "red", "blue", "black", "green" };
    
    
    // We can use this initialization syntax for any array. The next declaration
    // defines an array of awt Color constants that corresponds to the strings
    // in the "colors" array above
    Color  awtColors[] = { Color.RED, Color.BLUE, Color.BLACK, Color.GREEN };
    for(int i = 0; i < 4; i++)
    {
      if (color.equalsIgnoreCase(colors[i]))
      {
        curColor = awtColors[i];
      }
    }
    System.out.println("C: " + line);
  }
  
  //----------------- doD ------------------------
  /**
   * 
   * 
   */
  private void doD( String line )
  {
    line = JOptionPane.showInputDialog(null, "prompt");
    System.out.println("D: " + line);
  }
  
  //----------------- doE ------------------------
  /**
   * draw a wheels ellipse at a random position and size
   */
  private void doE( String line )
  {
    int xLoc = rng.nextInt( maxX );
    int yLoc = rng.nextInt( maxY );
    int w    = minSize + rng.nextInt( maxSize ); 
    int h    = minSize + rng.nextInt( maxSize ); 
    
    Ellipse myEllipse = new Ellipse(xLoc, yLoc);
    myEllipse.setSize(w, h);
    myEllipse.setColor(curColor);
  }
  
  //----------------- doR ------------------------
  /**
   * draw a wheels rectangle at a random position and size
   */
  private void doR( String line )
  {
    int xLoc = rng.nextInt( maxX );
    int yLoc = rng.nextInt( maxY );
    int w    = minSize + rng.nextInt( maxSize ); 
    int h    = minSize + rng.nextInt( maxSize ); 
    
    Rectangle myRect = new Rectangle(xLoc, yLoc);
    myRect.setSize(w, h);
    myRect.setColor(curColor);
  }
  
  //---------------------- makeScanner ----------------------
  /**
   * create and return a Scanner object. 
   *    If the parameter references a valid file, create a Scanner from it.
   *    else create a Scanner from standard input
   */
  public Scanner makeScanner( String filename )
  {
    if ( filename == null )
      return new Scanner( System.in );
    else
    {
      try
      {
        return new Scanner( new FileReader( filename ));
      }
      catch ( IOException ioex )
      {
        System.err.println( "***Error -- file does not exist: " + filename );
        System.err.println( "        opening System.in." );
        return new Scanner( System.in );
      }
    }
  }
  
  //------------------------ main --------------------------
  public static void main( String[] args )
  {
    LabCLI lab = new LabCLI( args );
  }
}
