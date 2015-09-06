/**
 * GraphLab
 * Reads in a graph file specified as the only argument
 * Creates a graph from the file and displays it
 * 
 * @author rdb
 * derived from code written by Jonathan Brown
 * Created 11/1/10
 */

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.util.*;
import java.io.File;

public class GraphLab 
{
   //----------------------- class variables ---------------------------------
   public static boolean interactive = true;

   //----------------------- instance variables -------------------------------
   private Digraph       graph;
   
   //++++++++++++++++++++++++++++ Constructors ++++++++++++++++++++++++++++++++
   /**
    * Reads in the dungeon file and creates the display
    */
   public GraphLab( String title, String filename )     
   {
      readGraphFile( filename );
      System.out.println( "------ Graph: -------------" );
      System.out.println( graph.longString() );                        
      if ( interactive )
         query();
   }
   
   //++++++++++++++++++++++++++ Private Methods +++++++++++++++++++++++++++++++
   //------------------------ parseLine -------------------------------------
   /**
    * parse a line of input from the graph file. Lines are of the form:
    *    edge node1 node2
    * 
    * where node1 and node2 are arbitrary Strings (with no spaces) that 
    *       represent a node in the graph. If either node id has not yet been
    *       encountered create a Node object for it and add it to the Digraph
    *       object.
    * Create an Edge from node1 to node2 and add it to the list of edges
    *    in the node1 object.
    * Print an error message for lines that don't start with "edge" or that
    *    don't have 2 node fields; you can ignore extra characters after the
    *    first 3 tokens on the line.
    */
   private void parseLine( String line )
   {
      if ( line == null || line.trim().length() == 0 )
         return;
      String[] tokens = line.split( " +" );
     
      /////////////////////////////////////////////////////////////////
      // Add code here to check for "edge" and "node" commands and
      //    to add the edges and nodes to the Digraph that is referenced
      //    by the instance variable "graph".
      /////////////////////////////////////////////////////////////////
      
      String cmd = tokens[ 0 ];
      if(cmd.equals("node"))
      {
      
        for(int i = 1; i < tokens.length; i++)
        {
          graph.addNode(tokens[i]);
        }
      }
      
      else if (cmd.equals("edge")  && tokens.length >= 3)
      {
        graph.addEdge(tokens[1], tokens[2]);
      }
      else
      {
        System.err.println("Cmd error");
      }
  
   }

   //-------------------------- query() --------------------------------------
   /**
    * prompt user for queries to the graph
    */
   private void query()
   {
      String prompt = "Enter query: \n"
                   +  "             g       graph.longString)\n"
                   +  "             s       graph.toString\n"
                   +  "             n id    node.longString";
      String msg = "";
      
      String cmd = JOptionPane.showInputDialog( null, prompt );
      while ( cmd != null && cmd.trim().length() > 0 )
      {
         cmd = cmd.trim();
         switch ( cmd.charAt( 0 ) )
         {
            case 'g': msg = graph.longString();
                      break;
            case 's': msg = graph.toString();
                      break;
            case 'n': 
                      String id = cmd.substring( 1 ).trim();
                      if ( id.length() > 0 )
                      {
                         Node n = graph.getNode( id );
                         if ( n == null )
                            msg = "Unknown id " + id;
                         else
                            msg = graph.getNode( id ).longString();
                      }
                      else 
                         msg = "No id specified";
                      break;
            default:
                      msg = "Invalid query: " + cmd;
         }
         cmd = JOptionPane.showInputDialog( null, msg + "\n\n" + prompt );
      }
   }   

   //------------------ readGraphFile( String filename ) --------------------
   /**
    * opens a new dungeon file and resets all the objects
    * The command processor actually parses the file see that for
    * more details
    */
   private void readGraphFile( String filename )
   {
      Scanner reader;
      graph = new Digraph();
      
      try
      {
         reader = new Scanner( new File( filename ) );
         while ( reader.hasNextLine() )
            parseLine( reader.nextLine() );
      }
      catch ( java.io.FileNotFoundException e )
      {
         System.err.println( e );
         System.exit( 1 );
      }
   }
      
   //+++++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++++++++
   public static void main( String [ ] args ) 
   {
      int fileIndex = 0;
      if ( args.length > 0 && args[ 0 ].equals( "-" ))
      {
         fileIndex = 1;
          GraphLab.interactive = false;  
      }
      else 
          GraphLab.interactive = true;  
      if ( args.length > fileIndex )
         new GraphLab( "GraphLab", args[ fileIndex ] );
      else
         new GraphLab( "GraphLab", "graph1.txt" );
   }
}
