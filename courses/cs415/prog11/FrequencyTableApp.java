/*
 * FrequencyTableApp.java
 * File access system for a frequency of words
 * 
 * Stephen Chambers
 * 11/16/10
 * */

//imports
import java.util.Scanner;
import java.io.*;
import java.util.Vector;

public class FrequencyTableApp
{
  //--------------------------Instance Variables-------------------------------
  Scanner keyBoard, myScan, myFileScan;
  String command = "", argument, input;
  Vector<FrequencyLine> table;
  File inFile;
  BufferedWriter p;
  /*
   * Constructor for the Frequency Table, gets a command from the user and an 
   * argument for the method
   * */
  public FrequencyTableApp()
  {
    //initialize FrequencyLine vector
    table = new Vector<FrequencyLine>();
    while(!command.equals("q"))
    {
      //takes input from the user
      keyBoard = new Scanner(System.in);
      input = keyBoard.nextLine();
      
      //makes a scanner from the user input
      myScan = new Scanner(input);
      //string that determines which method to call
      command = myScan.next();
      //h and q do not have additional arguments, if statement is necessary 
      if(!command.equals("h") && !command.equals("q"))
      {
        //saves argument to be passed to methods
        argument = myScan.next();
      }
      /*
       * Matches up user commands with their respective methods
       * */
      if(command.equals("r"))
      {
        doR(argument);
      }
      else if(command.equals("p"))
      {
        doP(argument);
      }
      else if(command.equals("f"))
      {
        doF(argument);
      }
      
      else if(command.equals("s"))
      {
        doS(argument);
      }
      else if(command.equals("l")) /*must catch BadTableException if format is 
                                     incorrect*/
      {
        try
        {
        doL(argument);
        }
        catch(BadTableException e)
        {
          System.out.println("Invalid Format");
        }
      }
      else if(command.equals("q"))
      {
        System.out.println("File access complete.");
      }
      else if(command.equals("h"))
      {
 System.out.println("r: Read a new text file with the given name.\n" + 
 "p: Print a section of the frequency table.\n" + 
 "s: Save the frequency table to a file with the given name\n" +
 "l: Load a saved correctly formatted frequency table from a file\n" +
 "f: Find a specific word in the current list and print the number of times" + 
 "it occurs\n" +
 "q: quit");                                           
      }  
    }
  }
  
  
   /*
   * doR reads a file and inserts words and their count alphabetically into a 
   * vector
   * */
  public void doR(String filename)
  {
    //Clear table
    table.clear();
    String next;
    boolean duplicate = false;
    //File object of the filename given by the user
    inFile = new File(filename);
    Scanner myFileScan;
    
    try
    {
      //Scanner of the file given by the user
      myFileScan = new Scanner(inFile);
      myFileScan.useDelimiter("[\\s\\Q,!()?;/\".\\E]");
     
      while(myFileScan.hasNext()) //still something in the file
      {
        //assigns next token in file to string
        next = myFileScan.next();
        
        
        if(next.length() >= 4) //only counts words greater than 4
        {
          for(int i = 0; i < table.size(); i++)
          {
            if(table.get( i ).getWord().equals( next ) ) /*if word in table
                                                     is equal to word in file*/
            {
              table.get( i ).incCount(); //increment count
              duplicate = true; //word has a duplicate
            }   
          }
          
          if( !duplicate) //if word has not shown up in table
          {
            int i = 0;
            //insert alphabetically
            while( i < table.size() 
                    && next.compareTo( table.get( i ).getWord()) >= 0) 
            i++;
            table.add (i, new FrequencyLine( next ) ); 
            
          }
          duplicate = false; //assume the word is not a duplicate
        }
        
      }
    }
    catch(IOException e) //Must catch IOException if a file is being accessed
    {
      System.err.println( e.getMessage() );
      return;
    }
    System.out.println("File read successfully.");
    
    
    
  }
   /*
   * doP prints out all words and their respective frequency countsthat begin
   * with letter passed by the user
   * */
  public void doP(String letter)
  {
    try
    {
      for(int i = 0; i < table.size(); i++)
      {
        if(table.get(i).getWord().startsWith(letter)) /*if current word starts
                                                    with letter passed by user*/
        {
          System.out.print(table.get( i )); //print the current FrequencyLine
        }
      }
    }
    catch(NullPointerException e) //if file is not read first
    {
      System.out.println("Read a file first!");
    }
  }
   /*
   * doF reads a word from the user and checks if that word is in the text file.
   * If so, it prints the word and its respective frequency.
   * */
  public void doF(String word)
  {
    try
    {
      for(int i = 0; i < table.size(); i++)
      {
        if(table.get(i).getWord().equals(word))/*if word in table equals word in
                                                 file*/
        {
          System.out.print(table.get( i ));
        }
      }
    }
    catch(NullPointerException e) //if file is not read first
    {
      System.out.println("Read a file first!");
    }
  }
   /*
   * doS saves the Frequency Table by writing it into a file. A buffered writer 
   * is used and must be flushed after it's written
   * */
  public void doS(String filename)
  {
    try
    {
      
      p = new BufferedWriter( new FileWriter( filename ) ); 
      for(int i = 0; i < table.size(); i++)
      {
       
        p.write(table.get(i).toString()); //write to file
        p.flush(); //flush buffer
      }
      
    }
    
    catch(IOException e) //must catch IOException if file access is being used
    {
      e.getMessage();
    }
    catch(NullPointerException e) //if file is not read first
    {
      System.out.println("Read a file first!");
    }
    
    System.out.println("File Written Successfully.");
  }
   /*
   * doL loads a Frequency Table if and only if the format is correct
   * */
  public void doL( String filename ) throws BadTableException
  {
    
    table.clear(); //clear table
    String next;
    
    inFile = new File( filename );
    Scanner myFileScan;
    
    try
    {
      
      myFileScan = new Scanner( inFile );
      while(myFileScan.hasNextLine()) //while there are still lines in the file
      {
        next = myFileScan.nextLine();
        
        /*split creates an array of Strings. The
         *elements are the strings seperated by the argument. If the length is 
         * two, the file has both a string and an integer, and only that. If the 
         * length of the array is not 2, the format is incorrect and an 
         * exception must be thrown.*/
        
        if( next.split( " " ).length == 2 ) 
        {
          table.add (new FrequencyLine( next ) ); 
        }
        else
        {
          throw new BadTableException( "Invalid Format" );/*throws exception to 
                                                           constructor*/
        }
        
       
        
      }
      
    }
    catch ( IOException e ) //must catch IOException for file access
    {
      e.getMessage();
    }
    catch( NullPointerException e )
    {
      System.out.println( "Read the file first!" );
    }
      System.out.println("File Loaded."); 
    
  }
  //---------------------------------Main--------------------------------------
    public static void main( String [] args )
    {
      //invokes FrequencyTableApp constructor
      FrequencyTableApp myApp = new FrequencyTableApp( );
    }
  } //End of Class