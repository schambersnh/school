/** 
 * AShapesPanel.java: Swing panel for logging lab
 * 
 * This class reads an input file that defines a simple display consisting
 * of rectangles, ellipses and lines; it creates ARectangle, AEllipse, and
 * ALine objects based on the input specification. References to all the
 * objects are saved in an ArrayList; the paintComponent method then re-draws
 * all these objects whenever it is called -- by calling their display method.
 * 
 * This class follows a standard framework for the main display window 
 * for an  awt application. It is responsible for knowing what objects
 * are being displayed and re-drawing them whenver its paintComponent
 * method is called.
 *      
 * @author Stephen Chambers
 * 1/25/2011
 */

import java.util.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class AShapesPanel extends JPanel
{ 
   //-------------------- instance variables ----------------------
   // Instance variables to reference all the objects created in the scene.
   //
   private ArrayList<AShape> shapes;
   private Color             lastColor = Color.RED;  // last color specified
                                                     // used if no color given
   
   //------------- Constructor ---------------------------------
   /**
    * Create awt objects to be displayed.
    */
   public AShapesPanel( String [ ] args ) 
   {
      String inFileName = "shapes.txt";
      if ( args.length > 0 )
         inFileName = args[ 0 ];
         
      shapes = new ArrayList<AShape>();
      readScene( inFileName );
   }  
   //------------------- readScene() ----------------------------
   /**
    * Create the wheels objects that make up the scene from the input file
    * Each line in the file must be one of
    *    a blank line
    *    a comment line that starts with '#',
    *    a command line that defines an A-object to be created
    * Command lines are of the form
    *    r x y w h [color]
    *    e x y w h [color]
    *    l x1 y1 x2 y2 [color]
    *   where the color field if optional; if it is not present
    *       the last color specified is used, where RED is initial default
    */
   private void readScene( String inFileName )
   {
     Log.traceOff( "--->:<---" );
     Log.trace( "--->", "Entering Method readScene...." );
      Scanner in;
      try 
      {
         in = new Scanner( new File( inFileName ));
         
      }
      catch ( FileNotFoundException fnf )
      {
         Log.error( "AShapesPanel.readScene: FilenotFoundException" );
         return;
      }      
         
      while ( in.hasNextLine() )
      {
          
         String line = in.nextLine().trim();
         Log.trace( "input" , line );
                  if ( line.length() > 0 )
         {
            if ( line.charAt( 0 ) != '#' )  // if it's not a comment, parse it
               parseLine( line );
            else   // check for a special comment that specifies trace commands
            {
               if ( line.startsWith( "#trace" ))
               {
                  String[] tokens = line.split( " " );
                  if ( tokens.length > 2 )
                  {
                     if ( tokens[ 1 ].equals( "on" ))
                        Log.traceOn( tokens[ 2 ] );
                     else if ( tokens[ 1 ].equals( "off" ))
                        Log.traceOff( tokens[ 2 ] );
                  }
               }
            }
         }

      }  
       Log.trace( "<---", "Exiting Method readScene...." );
   }
   //---------------------- parseLine( String ) ----------------------------
   /**
    * Valid commands have a single character token as as first
    * argument, followed by 4 integers, followed by string for the color
    */
   private void parseLine( String line )
   {
      Log.trace( "--->", "Entering Method parseLine...." );
      Scanner cScan = new Scanner( line );
      String cmd = null;
      Integer[] intArgs = new Integer[ 4 ];
      String color = null;
      try 
      {
         cmd = cScan.next();
         for ( int i = 0; i < intArgs.length; i++ )
            intArgs[ i ] = cScan.nextInt();
         color = cScan.next();
      }
      catch ( NoSuchElementException nsee )
      {
         Log.error("AShapesPanel.parseLine:NoSuchElementException");
      }
      String errMsg = checkArgCount( cmd, intArgs );

      if ( errMsg != null )
         Log.error("AshapesPanel.parseLine: Null reference");
      else
      {
         if ( cScan.hasNext() )
         {
            String rest = cScan.nextLine();
            Log.warning( "Warning: Extra tokens on input line ignored: " 
                                  + rest );
         }
         //Log.traceOn("--->:<---");
         errMsg = execute( cmd, intArgs, color );
        // Log.traceOff("--->:<---");
      }  
       Log.trace( "<---", "Exiting Method parseLine...." );
   }
   //-------------------------- checkArgCount -----------------------------
   /**
    * Check to see how many arguments correctly parsed
    * Return null if everything ok; else returns error message
    */
   private String checkArgCount( String cmd, Integer intArgs[] )
   {
      Log.trace( "--->", "Entering Method checkArgCounnt...." );
      char c = cmd.toLowerCase().charAt( 0 );
      String errMsg = null;
      if ( "rel".indexOf( c ) < 0 )
      {
         errMsg = "Invalid shape specification in command: " + cmd;
       
      }
      
      else if ( intArgs[ 0 ] == null )
      {
      errMsg = "Argument 1 is not an int.";
   
      }
      else if ( intArgs[ 1 ] == null )
      {
         errMsg = "Argument 2 is not an int.";
  
      }
      else if ( intArgs[ 2 ] == null )
      {
         errMsg = "Argument 3 is not an int.";
     
      }
      else if ( intArgs[ 3 ] == null )
      {
      errMsg = "Argument 4 is not an int.";
      }
     
      Log.trace("<---", "Exiting Method checkArgCounnt....");
      return errMsg;
       
   }
   //-------------------------- execute ---------------------------------
   /**
    * Check command and arguments and execute if valid
    * Return null if everything ok; else returns error message
    */
   private String execute( String cmd, Integer intArgs[], String color )
   {
      Log.trace( "--->", "Entering Method execute...." );
      char c = cmd.toLowerCase().charAt( 0 );
      String errMsg = null;
      if ( "rel".indexOf( c ) < 0 )
         errMsg = "Invalid shape specification in command: " + cmd;
      else
      {
         Color jColor = lastColor;
         if ( color != null )
         {
            jColor = colorFromString( color );
            if ( jColor == null )
            {
               errMsg = "Invalid color specification: " + color
                        + ".  Using last valid color.";
               jColor = lastColor;
            }
         }
         switch ( c )
         {
            case 'r':  // rectangle command
               ARectangle rect = new ARectangle( intArgs[ 0 ], intArgs[ 1 ] );
               rect.setSize( intArgs[ 2 ], intArgs[ 3 ] );
               rect.setColor( jColor );
               shapes.add( rect );
               break;
            case 'e':  // ellipse command
               AEllipse ell = new AEllipse( intArgs[ 0 ], intArgs[ 1 ] );
               ell.setSize( intArgs[ 2 ], intArgs[ 3 ] );
               ell.setColor( jColor );
               shapes.add( ell );
               break;
            case 'l':  // line command
               ALine line = new ALine( intArgs[ 0 ], intArgs[ 1 ],
                                      intArgs[ 2 ], intArgs[ 3 ] );
               line.setColor( jColor );
               shapes.add( line );
               break;
         }
         lastColor = jColor;
      }         
      Log.trace( "<---", "Exiting Method execute...." );
      return errMsg; 
       
   }
   //-------------------- colorFromString( String ) --------------------------
   /**
    * Given a string representation for color, return the appropriate
    *   Color object. Only works for (some of) the pre-defined Colors.
    */
   private Color colorFromString( String color )
   {
     Log.trace( "--->", "Entering Method colorFromString...." );
      String[] sColors = { "red", "green", "blue", 
                           "cyan", "magenta", "yellow",
                           "black", "white", "orange", "pink",
                           "gray", "darkgray", "lightgray",
                         };
      Color[] jColors = { Color.red, Color.green, Color.blue, 
                          Color.cyan, Color.magenta, Color.yellow,
                          Color.black, Color.white, Color.orange, Color.pink,
                          Color.gray, Color.darkGray, Color.lightGray 
                        };
      if ( color == null )
         return null;
      color = color.toLowerCase();
      Color found = null;
      for ( int c = 0; c < sColors.length && found == null; c++ )
      {   
         if ( color.equalsIgnoreCase( sColors[ c ] ))
            found = jColors[ c ];
      }
       Log.trace( "<---", "Exiting Method colorFromString...." );
      return found;
     
   }
   //------------- paintComponent ---------------------------------------
   /**
    * This method is called from the Java environment when it determines
    * that the JPanel display should be updated; you need to 
    * make appropriate calls here to re-draw the graphical images on
    * the display. Each object created in the constructor above should 
    * have its "fill" and/or "draw" methods invoked with a Graphics2D 
    * object. The Graphics object passed to paintComponent will be a 
    * a Graphics2D object, so it can be coerced to that type and
    * passed along to the "display" method of the objects you created.
    * 
    * Note that the "display" method is not an awt method, but a convenience
    * method defined by the "wrapper" classes. The display method usually
    * passes the graphical objects to both the Graphics2D.fill and
    * Graphics2D.draw methods, except in the case of ALine graphical objects 
    * which cannot be "filled".
    */
   public void paintComponent( Graphics aBrush )
   {
      super.paintComponent( aBrush );
      Graphics2D brush2D = (Graphics2D) aBrush;  // coerce arg to Graphics2D
      

      for ( AShape s: shapes )
      {
         s.display( brush2D );
      }
   }
   //--------------------- main ------------------------------------------
   public static void main( String[] args )
   {
      Log.openLogFile("AShapes.log");
      
      AShapesApp app = new AShapesApp( "Drawing in AWT/Swing", args );
      Log.closeLogFile();
   }
}