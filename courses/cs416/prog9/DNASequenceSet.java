/**  UPDATE comments from DNASequenceSet
  * 
 * DNASequenceSet -- This class implements an interface to a collection
 *       of DNA sequences. It can create a sequence collection from a file 
 *       containing DNA sequences in FASTA format or by having sequences added
 *       one at a time from some other source. 
 * 
 *       For the file input, this implementation reads the entire
 *       file at once and stores the sequences into a HashMap of DNASequence
 *       objects with the lookup key being the sequence id -- therefore the
 *       sequence id must be unique. 
 * 
 *       This is a viable implementation only for relative small 
 *       data sets -- an application designed to use real genome data
 *       would not be able to fit all the data in memory at one time.
 * 
 *       Key public methods:
 *           DNASequenceSet( String filename ) -- filename must be a 
 *                valid readable Fasta file or else a FileNotFoundException 
 *                is thrown.
 *           DNASequenceSet() -- to be used for ad hoc sequence creation
 *           void add( String header, String dna ) -- add sequence to collection  
 *           iterator<DNASequence>() -- returns an iterator over the sequences
 *                    in the file.
 *           DNASequence get( int i ) -- returns the i-th sequence in the file
 *                     if it exists; otherwise it returns null
 *           DNASequence get( String seqId ) -- return sequence with specified
 *                     seqId or null if it doesn't exist
 *           int size() -- returns the number of sequences in the set
 * 
 * @author rdb
 * April 25, 2009 as FastaFile.java
 * 
 * Revised 11/13/10 rdb
 *      Renamed to DNASequenceSet; made more general purpose
 * 
 */
import java.awt.Point;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class DNASequenceSet implements Iterable
{
   //------------------------- class variables ---------------------------------
   
   //------------------------- instance variables ------------------------------
   private Scanner scanner = null;
   private String  nextHeader   = null;
   private int     targetength = 0;
   private HashMap<String, DNASequence> sequences;  // efficient random access
   private ArrayList<String>  seqIds;  // allows for iterator in order of add

   //---------------------- constructor ----------------------------------------
   /**
    * Constructor -- for building the set dynamically
    */
   public DNASequenceSet()
   {
      sequences = new HashMap<String,DNASequence>();
      seqIds    = new ArrayList<String>();
   }
   /**
    * Constructor -- for reading from a fasta file
    */
   public DNASequenceSet( String fileName )
   {
      this();               // invoke no-arg constructor
      readSequences( fileName );
   }
   //---------------------- add( String, String ) ----------------------------
   /**
    * Create a sequence from the header and dna and add it to the set
    */
   public void add( String hdr, String dna )
   {
      DNASequence seq = new DNASequence( hdr, dna );
      add( seq );
   }
   //---------------------- add( DNASequence ) ----------------------------
   /**
    * Add a DNASequence object to the set
    */
   public void add( DNASequence seq )
   {
      String id = seq.getId();
      DNASequence known = sequences.get( id );
      if ( known != null )
         Log.error( "DNASequenceSet.add: seq id already defined: " + id );
      else
      {
         seqIds.add( id );
         sequences.put( id, seq );
      }
   }
   //---------------------- size() ----------------------------------------
   /**
    * number of sequences in the file
    */
   public int size()
   {
      if ( sequences == null )
         return 0;
      else
         return sequences.size();
   }
   //---------------------- compositeLength() ----------------------------------------
   /**
    * the total length of all sequences in the file
    */
   public int compositeLength()
   {
      int len = 0;
      if ( sequences != null )
      {
         Iterator<DNASequence> iter = this.iterator();
         while ( iter.hasNext() )
            len += iter.next().length();
      }
      return len;
   }
   
   //---------------------- iterator() ----------------------------------------
   /**
    * return an iterator over the DNASequences
    */
   public Iterator<DNASequence> iterator()
   {
      return new SequenceIterator();
   }
   //---------------------- get( int ) ----------------------------------------
   /**
    * return the i-th sequence or null
    */
   public DNASequence get( int i )
   {
      if ( i >= 0 && i < seqIds.size() )
         return sequences.get( seqIds.get( i ));
      else
         return null;
   }
   //--------------------------- get( String ) ------------------------
   /**
    * Search in the sequences collection for a sequence with the requested id.
    * If found, return it, else return null.
    */
   public DNASequence get( String id )
   {
      DNASequence found = sequences.get( id );;
      return found;
   }
   //--------------------------- clear() ------------------------------
   /**
    * empty the set of all sequences
    */
   public void clear()
   {
      sequences.clear();
      seqIds.clear();
   }
   //---------------------- getComposite() ------------------
   /**
    * Return a single string that is the concatenation of all sequences
    * in the fasta file, separated by the default separator.
    */
   public DNASequence getComposite()
   {
      return getComposite( "**********" );
   }
   
   //---------------------- getComposite( String ) ------------------
   /**
    * Return a single string that is the concatenation of all sequences
    * in the fasta file, separated by the parameter separator.
    */
   public DNASequence getComposite( String separator )
   {
      StringBuffer composite = new StringBuffer();
      Iterator<DNASequence> iter = this.iterator();
      while ( iter.hasNext() )
      {
         composite.append( iter.next().getDNA() );
         composite.append( separator );
      }
      composite.delete( composite.length() - separator.length(), 
                       composite.length() );
      String header = ">compositeSeq of " + sequences.size() + " sequences" ;
      return new DNASequence( header, composite.toString() );
   }
   //++++++++++++++++++++++ public internal class ++++++++++++++++++++++++++++
   public class SequenceIterator implements Iterator<DNASequence>
   {
      //------------------ instance variables ------------------------------
      private Iterator<String> iter = null;
      private String           lastReturned = null;
      //-----------------  constructor ------------------------------------
      public SequenceIterator()
      {
         iter = seqIds.iterator();
      }
      //------------------- hasNext() -----------------------------------
      public boolean hasNext()
      {
         return iter.hasNext();
      }
      //------------------- next() ---------------------------------------
      public DNASequence next()
      {
         lastReturned = iter.next();
         return sequences.get( lastReturned );
      }
      //------------------- remove() ---------------------------------------
      public  void remove()
      {
         if ( lastReturned != null )
         {
            iter.remove();
            sequences.remove( lastReturned );
         }
         lastReturned = null;
      }
   }
      
   //++++++++++++++++++++++ private utility methods ++++++++++++++++++++++++++++
   //------------------------ readSequences( String  ) --------------------------
   private void readSequences( String fileName )
   {
      // open a scanner for the fasta file -- and read the first header
      long start = System.currentTimeMillis();
      openScanner( fileName );      
      while ( nextHeader != null )
      {
         try
         {
            String header = nextHeader;
            String dna = readSequence();
            add( header, dna ); 
         }
         catch ( DNASequence.DNASequenceException dnaEx )
         {
            Log.error( dnaEx.getMessage() + "\nSequence input ignored." );
         }
      }
   }
   
   //------------------------ openScanner() -------------------------------
   private void openScanner( String fileName )
   {
      try
      {
         File inFile = new File( fileName );
         scanner = new Scanner( inFile );
         nextHeader = scanner.nextLine();
         if ( nextHeader.charAt( 0 ) != '>' )
         {
            Log.error( "***Error: " + fileName + " does not start with '>' " );
            scanner = null;
            nextHeader = null;
         }            
      }
      catch ( IOException ioe )
      {
         Log.error( "Open failed: " + fileName + "\n"+ ioe.getMessage() );
         scanner = null;
         nextHeader = null;
      }
      catch ( NoSuchElementException nse )
      {
         Log.error( "***Error: " + fileName + " is empty. " );
         scanner = null;
         nextHeader = null;
      }
   }
   //------------------------ readSequence() -------------------------------
   /**
    * Read an entire sequence from the file; sequence ends when get eof or
    * when get the next header (a line that starts with '>'. If a header 
    * is read, it is stored in the instance variable, "nextHeader".
    * 
    * This method shows a great example of the use of StringBuffer (or 
    * StringBuilder) vs. String. 
    * 
    * To concatenate 18,818 60-char lines into a single String or StringBuffer 
    * or StringBuilder on a 2GB 2GHz MacBookPro, the times were:
    *      StringBuilder/StringBuffer: 0.24-0.31 sec
    *      String:                     172       sec
    */
   private String readSequence()
   {
      String line;        // one line of input
      StringBuffer dna = new StringBuffer();
      //StringBuilder dna = new StringBuilder();
      //String dna = "";
      boolean moreToRead = scanner.hasNextLine();
      nextHeader = null;
      int count = 1;
      while ( moreToRead )
      {
         line = scanner.nextLine();
         if ( line.startsWith( ">" ))
         {
            nextHeader = line;
            moreToRead = false;
         }
         else
         {
            dna.append( line );
            //dna += line;
            moreToRead = scanner.hasNextLine();
         }
      }
      //return dna;  // String impl
      return dna.toString();
   }
   //------------------------ printSequences() -------------------------------
   private void printSequences( PrintStream out )
   {
      out.println( "+++++++++++++++++++++++++++++++++++++++++++++++" );
      Iterator<DNASequence> iter = iterator();
      while ( iter.hasNext() )
      {
         out.print( iter.next( ));
      }
   }
   
   //---------------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      String filename = null;
      DNASequenceSet test;
      
      if ( args.length > 0 )
         filename = args[ 0 ];
      else
         filename = Utilities.getFileName( "DNASequenceSet unit test" );
      
      if ( filename != null )
      {
         test = new DNASequenceSet( filename );
         /****/
         System.out.println( "-------------- iterator test -----------------" );
         test.printSequences( System.out );
         /****
         System.out.println( "-------------- get( i ) test -----------------" );
         for ( int i = 0; i < test.size(); i++ )
            System.out.print( test.get( i ));            
         /******/
      }
   }
}