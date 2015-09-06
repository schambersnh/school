/**
 * DataManager -- manage the reading and construction of the various
 *                data objects needed in this application
 *
 * @author rdb
 * Modified by Stephen Chambers for Program 9
 * 04/18/11
 */
import java.util.*;
import java.util.regex.*;
import java.io.*;

public class DataManager
{
  //-------------------------- class variables --------------------------
  //-------------------------- instance variables -----------------------
  DNASequenceSet _sequenceSet;
  DNASequence _curSequence;
  Pattern _pattern;
  Matcher _matcher;
  DNASequenceSet _seq = new DNASequenceSet();
  DNASequenceSet _reverseSeq = new DNASequenceSet();
  DNASequence _reverse;
  PrintWriter out;
  //-------------------------- constructor ------------------------------
  /**
   * Constructor gets an optional file name for the sequence file to
   * be opened.
   */
  public DataManager()
  {
    //log.openFile( "ORFS.txt" );
    try
    {
    FileWriter outFile = new FileWriter( "ORFS.txt" );
    out = new PrintWriter(outFile);
    }
    catch(Exception e)
    {
      System.err.println( "Error opening File" + e.getMessage( ) );
    }
  }
  //+++++++++++++++++++++++++ public methods ++++++++++++++++++++++++++++
  //---------------------- readSeqFile( String ) -----------------------
  /**
   * 1. read the Seq file, by invoking the "fileName" constructor of 
   *    DNASequenceSet.
   * 2. Initialize your code and the the rest of the system to so the 
   *    first contig in the set to be the current contig. 
   *     a. tell the gui how many contigs are in the set and what 
   *        the current value of the contig spinner appropriately should 
   *        be. Use
   *             GUI,setContigSpinner
   *     b. do what needs to be done to tell the DNADisplay object
   *        what the "reference" sequence for display is.
   */
  public void readSeqFile( String seqFileName )
  {
    _sequenceSet = new DNASequenceSet(seqFileName);
    GUI.setContigSpinner( 0 , _sequenceSet.size() - 1 );
    
    DNADisplay.setReference( _sequenceSet.get( 0 ) );
    _curSequence = _sequenceSet.get( 0 );
    
    
    // Log.error( "Cannot open file: " + seqFileName );
  }
  //----------------- findORFs() ------------------------------------
  /**
   * Find the longest orfs in all 6 reading frames, print information about
   * them, create DNASequence objects to represent them, set their
   * referencePosition appropriately, and invoke DNADisplay methods to
   * get them displayed
   */
  public void findORFs()
  {
    out.println("----------Longest ORFs for " + _curSequence.getId() + 
                " + strand-----------------");
    String frame1 = _curSequence.getDNA();
    helperFindORFs( frame1 , 0 );
    String frame2 = frame1.substring( 1, frame1.length() );
    helperFindORFs(frame2 , 1);
    String frame3 = frame2.substring( 1, frame2.length() );
    helperFindORFs( frame3 , 2) ;
    
    out.println("----------Longest ORFs for " + _curSequence.getId() +
                " - strand-----------------");
    String reverseFrame1 = helperReverseComplement();
    _reverse = new DNASequence( "-rc" + _curSequence.getId() , reverseFrame1 );
    reverseHelperFindORFs( reverseFrame1 , 0 );
    String reverseFrame2 = reverseFrame1.substring(1 , reverseFrame1.length( ) );
    reverseHelperFindORFs( reverseFrame2 , 1 );
    String reverseFrame3 = reverseFrame2.substring(1 , reverseFrame2.length( ) );
    reverseHelperFindORFs( reverseFrame3 , 2 );
    
    drawORFs();
  }
  public void helperFindORFs( String curSequence , int i )
  {
    int curOff = i;
    int maxOff = i;
    
    String myPattern = "^((...)*?)(GTA|TAG|$)";
    
    _pattern = Pattern.compile( myPattern, 
                               Pattern.COMMENTS | Pattern.MULTILINE );
    
    
    String max = "";
    _matcher = _pattern.matcher( curSequence );

      while ( _matcher.find( ) )
      {
      
        if(_matcher.group( 1 ).length() > max.length( ))
        {
          max = _matcher.group( 1 );
          maxOff = curOff;
        }
        //"Brute force location change of string
        curSequence = curSequence.substring(
        _matcher.group( 0 ).length( ), curSequence.length( ));
        
        curOff += _matcher.group( 0 ).length( );
        
        _matcher = _pattern.matcher( curSequence );
      }
      if( max.equals( "" ) )
      {
       max = curSequence;
      }
 
      DNASequence mySeq = new DNASequence( _curSequence.getId( ) + i , max );
      mySeq.setReferencePosition( maxOff );
      _seq.add( mySeq );
      out.println( _curSequence.getId() + ": frame: " + i + " @ " + maxOff + 
                  " -> " + (max.length() + maxOff - 1 ) );
      
  }
  public void reverseHelperFindORFs( String curSequence , int i )
  {
    int curOff = i;
    int maxOff = i;
    String myPattern = "^((...)*?)(GTA|TAG|$)";
    _pattern = Pattern.compile( myPattern, 
                               Pattern.COMMENTS | Pattern.MULTILINE );
    
    String max = "";
    _matcher = _pattern.matcher( curSequence );

      while ( _matcher.find( ) )
      {
       
        if( _matcher.group( 1 ).length( ) >= max.length( ) )
        {
          max = _matcher.group( 1 );
          curOff += _matcher.group( 0 ).length( );
          maxOff = curOff;

        }
  
        //"Brute force location change of string
        curSequence = curSequence.substring(
        _matcher.group( 0 ).length( ), curSequence.length( ) );
        
        _matcher = _pattern.matcher( curSequence );
      }
      
      if( max.equals( "" ) )
      {
       max = curSequence;
      }
      else
      {
          maxOff = i;
      }
      DNASequence mySeq = new DNASequence( _curSequence.getId( ) + "0" + 
                                          max.length( ) , max );
      mySeq.setReferencePosition( maxOff );

      
      _reverseSeq.add( mySeq );
      out.println( _curSequence.getId( ) + "- rc" + ": frame: " + i + 
                  " @ " + maxOff + " -> " + ( max.length() + maxOff - 1 ) );
      //out.close();
  }
  

  
  //--------------- setReference( int ) --------------------------------
  /**
   * set the reference display to be the DNASequence; this method is
   * called from the GUI (and you might use it yourself)
   */
  public void setReference( int refIndex )
  {
    DNADisplay.setReference(_sequenceSet.get( refIndex ));
    _curSequence = _sequenceSet.get( refIndex );
  }
  //---------------------- reverseComplement(  ) ----------------
  /**
   * Sets the current sequence to be the reverse complement of the current
   * sequence. This method is called from the GUI.
   */
  public void reverseComplement()
  {
    String curSequence = helperReverseComplement( );
    _curSequence.setDNA( curSequence );
    DNADisplay.setReference( _curSequence );
  }
    public String helperReverseComplement( )
  {
    String curSequence = _curSequence.getDNA( );
    
    StringBuilder reverseSequence =  new StringBuilder( curSequence );
    
    for(int i = reverseSequence.length() - 1; i >= 0; i--)
    {
      if( reverseSequence.charAt( i ) == 'C' )
      {
        reverseSequence.setCharAt( i , 'G' );
      }
      else if( reverseSequence.charAt( i ) == 'G' )
      {
        reverseSequence.setCharAt( i , 'C' );
      }
      else if( reverseSequence.charAt( i ) == 'A' )
      {
        reverseSequence.setCharAt( i , 'T' );
      }
      else if( reverseSequence.charAt( i ) == 'T' )
      {
        reverseSequence.setCharAt( i , 'A' );
      }
    }
    
    curSequence = reverseSequence.reverse( ).toString( );
    
    
   return curSequence;
    
  }
  
  public void drawORFs( )
  {
    DNADisplay.setReference( _curSequence) ;
    DNADisplay.addSequence( _seq );
    _seq.clear( );
    DNADisplay.useRegion( "reverse" );
     DNADisplay.setReference( "reverse" , _reverse );
    DNADisplay.addSequence( "reverse" , _reverseSeq );
    _reverseSeq.clear( );
    out.close( );
    
  }
  //++++++++++++++++++++++++ main +++++++++++++++++++++++++++++++++++++++++
  /**
   * Invoke DNAapp with the simple test file
   */
  public static void main( String[] args )
  {
    String[] myArgs = { "simpleDNA.txt" };
    if ( args.length == 0 )
      DNAapp.main( myArgs );
    else
      DNAapp.main( args );        
  }
}
