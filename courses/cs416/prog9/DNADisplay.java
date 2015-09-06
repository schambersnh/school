/** 
 * DNADisplay.java: This class supports the display of DNA alignment data.
 *        The top line of the display shows a "reference" DNA string defined by
 *        a single instance of DNASequence, typically quite long.
 * 
 *        Below the reference (on 1 or more lines) are multiple DNASequence
 *        objects whose horizontal positions place them at appropriate locations
 *        compared to the reference. These could be sequence reads that "align"
 *        at that position, or introns that are located at the start position
 *        in the chromosome, but are spliced out when dna is converted to
 *        rna in the process of being translated to proteins.
 * 
 *        There are scrollbars in horizontal and vertical directions.
 * 
 * rdb
 * CS416 Spring 2009
 * 04/18/11 rdb: Major revision. Define explicit Region objects; each Region
 *               has its own (optional) reference.
 * 
 * Notes: fix mouse position code
 */

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;

public class DNADisplay extends JPanel 
{  
   //------------------- class variables --------------------------
   static private  DNADisplay   me;    // helps interface public static methods 
                                       //   with private instance methods
   static private  boolean      reUseTracks = false;  // true if multiple seq
                                                      // allowed in same track
                                                  // only happen if no overlap
   
   //------------------- instance variables ------------------------
   private Region         _r0;       //  initial region is handled separately
   
   private ArrayList<Region>      _regions;
   private HashMap<String,Region> _rNames; // Map region name to Region 
   
   private GUI       _gui;    // GUI reference to handle mouse pos mapping

   
   
   //------------- magic constants
   private int       seqX       = 10; // left of all sequence display
   private int       seqY       = 20; // top of all sequence display
   private int       nucHeight  = 20;
   private int       ySpace     = 4; // extra y space before/after track block
      //     
   private int       nucW      = 10;  // space for the nucleotide
   private int       fontSize      = 16;  // font depends on nucW
   private int[]     fontSizeArray = { 8, 10, 12, 14, 15, 16, 18 };
          // By experimentation:
          //    (11,18) (10,16) (9,15) (8,14) (7,12) (6,10) (5,8)
   private int       maxWidth = 11;
   private int       minTextWidth = 5;
   private int       minTextHeight = 10;
   private int       minBorderWidth = 3;
   private int       trackHeight = 20;
   private int       trackVOffset = 6;
   private int       trackHSpace = 10;  // # nucleotide space between seq in
                                          // same track
   private int       separatorH = 3;
   private int       separatorW = 20000;

   private int       defaultW   = 20000;
   private int       defaultH   = 1000; 

   //--------------------- constructor ----------------------------
   public DNADisplay ( GUI gui ) 
   {
      super();
      if ( GUI.batch )
         return;
      
      me = this;
      _gui = gui;
      setLayout( null );
      setPreferredSize( new Dimension( defaultW, defaultH ));
      setupMouseListeners();
      init();

      update();
   }
   //----------------------- init ----------------------------------------
   private void init()
   {    
      _r0 = new Region(); 
      _r0.name = "--";
      _regions = new ArrayList<Region>();
      _regions.add( _r0 );
      _rNames = new HashMap<String,Region>();
      _rNames.put( "--", _r0 );      
   }
   //----------------------- setupMouseListeners() -----------------------
   private void setupMouseListeners()
   {
      this.addMouseListener(
      new MouseListener() { 
         public void mousePressed( MouseEvent me ) {};
         public void mouseReleased( MouseEvent me ) {};
         public void mouseEntered( MouseEvent me ) {};
         public void mouseExited( MouseEvent me ) {};
         public void mouseClicked( MouseEvent me )
         {
            Point mouseAt = me.getPoint();
            int   pickY = me.getY();
            String msg = null;
            
            for ( int r = 0; r < _regions.size() && msg == null; r++ )
            {
               Region region = _regions.get( r );
               if ( pickY >= region.y && pickY <= region.y + region.height )
                  msg = pickRegion( region, mouseAt );
             }
             if ( msg == null )
                msg = "Seq not found at " + mouseAt;
             _gui.setSequencePicked( msg );
         }
      } );
      this.addMouseMotionListener(
      new MouseMotionListener() { 
         public void mouseDragged( MouseEvent me ) {};
         public void mouseMoved( MouseEvent me )
         {
            Point mouseAt = me.getPoint();
            int nucPosition = ( me.getX() - seqX ) / nucW;
            _gui.setReferencePosition( nucPosition );
         }
      } );
   }
   //------------------ pickRegion( Region, Point ) -----------------------
   private String pickRegion( Region region, Point p )
   {
      String msg = null;
      if ( pickReference( region, p ) ) // reference pick?
         msg = region.reference.toString();
      else // check the tracks
      {
         int nucPos = ( p.x - seqX ) / nucW;
         int track = getTrackFromY( region, p.y );
         if ( track >= 0 )
         {
            String header = findReadPicked( region, track, nucPos );
            if ( header != null )
               msg = header;
         }   
      }
      return msg;
   }
   //------------------ pickReference( Region, Point ) --------------------
   private boolean pickReference( Region r, Point p ) 
   {
      return  r.reference != null 
           && p.y >= r.y && p.y <= r.y + nucHeight 
           && p.x >= r.x && p.x <= r.x + r.width;
   }
   //------------------ getTrackFromY( Region, int y ) --------------------
   /**
    * Given a y value, find the track in the region that encloses the y
    */
   private int getTrackFromY( Region r, int y )
   {
      int dy = y - r.y;
      if ( r.reference != null )
         dy -= nucHeight + ySpace;
      int theTrack = -1; 
      for ( int t = 0; t < r.trackY.size() && theTrack == -1 ; t++ ) 
      {
         Integer yTrack = r.trackY.get( t );
         int h = r.trackH.get( t );
         if ( h > 0 ) // height > 0 is a track for a dna sequence
         {
            int yt = yTrack.intValue();
            int ymax = yt + h;
            if ( dy >= yt  && dy < yt + h ) 
               theTrack = t;
         }
      }
      return theTrack;
   }
   //----------------------- setReference( DNASequence ) ---------------------
   /**
    * define a new list to display
    */
   public static void setReference( DNASequence dna )
   {
      me.init();
      me.setReferenceP( me._r0, dna );
      me._gui.setReference( dna );
   }
   //------------------ setReference( String, DNASequence ) --------------
   /**
    * define a new reference for 
    */
   public static void setReference( String regionId, DNASequence dna )
   {
      Region r = me._rNames.get( regionId );
      if ( r == null )
         Log.error( "DNADisplay.setReference: invalid region id: " + regionId );
      else
         me.setReferenceP( r, dna );
   }
   //-------------- setReferenceP( Region, DNASequence ) -----------------
   /**
    * set the reference sequence for a region
    */
   private void setReferenceP( Region r, DNASequence seq )
   {
      r.reference = seq;
      update();     
   }
   //----------------------- getNucHeight() ---------------------
   /**
    * get the nucleotide display height for read sequences
    */
   public int getNucHeight()
   {
      return trackHeight;
   }
   //----------------------- setNucHeight( int ) ---------------------
   /**
    * set the nucleotide display height for read sequences
    */
   public void setNucHeight( int h )
   {
      trackHeight = h;
   }
   //----------------------- eraseSecondary( Region ) ----------------------
   /**
    * clear all secondary display components.
    */
   private void eraseSecondary( Region r )
   {
      if ( r == null ) 
         return;
      if ( r.tracks != null )
      {
         r.tracks.clear();
         r.trackH.clear();
         r.trackY.clear();
      }
      r.minTrack = 0;
      r.hiTrack = 0;
      r.hiTrackY = 30;
      update();
   }
   //------------------- addSequence( DNASequenceSet ) --------------
   /**
    * add a set of reads to the list to be displayed
    */
   public static void addSequence( DNASequenceSet seqSet )
   {
      me.addSeqP( me._r0, seqSet );
   }
   //------------------- addSequence( String, DNASequenceSet ) --------------
   /**
    * add a set of reads to the list to be displayed
    */
   public static void addSequence( String regionId, DNASequenceSet seqSet )
   { 
      me.addSeqP( regionId, seqSet );  // need to convert string to region
   }
   //------------------- addSeqP( String, DNASequenceSet ) --------------
   /**
    * add a set of reads to the list to be displayed
    */
   private void addSeqP( String regionId, DNASequenceSet seqSet )
   {
      Region r = _rNames.get( regionId );
      if ( r == null )
         Log.error( "DNADisplay.addSequence Undefined region: " + regionId );
      else
         addSeqP( r, seqSet );
   }
   //------------------- addSeqP( Region, DNASequenceSet ) --------------
   /**
    * add a set of reads to the list to be displayed
    */
   private void addSeqP( Region region, DNASequenceSet seqSet )
   {
      if ( seqSet == null )
      {
         Log.error( "DNADisplay.addSequence got null parameter " );
         return;
      }
      Iterator<DNASequence> iter = seqSet.iterator();
      while ( iter.hasNext() )
      {
         DNASequence seq = iter.next();
         Log.trace( "test", "Adding: " + seq.getId());
         addSeqP( region, seq ); // invoke private version of read
      }
      
      //update();
   }
   //--------------- addSequence( String, DNASequence ) --------------------
   /**
    * add a read to the specified region
    */
   public static void addSequence( String region, DNASequence seq )
   {
      me.addSeqP( region, seq ); // invoke private version of read
   }
   //----------------------- addSeqP( String, DNASequence ) ----------------
   /**
    * add a read to a region
    */
   private void addSeqP( String regionId, DNASequence seq )
   {
      Region r = _rNames.get( regionId );
      if ( r == null )
         Log.error( "DNADisplay.addSequence Undefined region: " + regionId );
      else
         addSeqP( r, seq );
   }
   //----------------------- addSeqP( Region, DNASequence ) ----------------
   /**
    * add a read to a region
    */
   private void addSeqP( Region r, DNASequence seq )
   {
      if ( seq == null )
      {
         Log.error( "DNADisplay.addSequence got null seq parameter " );
         return;
      }
      assignReadToTrack( r, seq );
      update();
   }
   
   //----------------------  setNucleotideWidth( int ) -------------------
   public void setNucleotideWidth( int newW )
   {
      if ( newW > 0 && newW < maxWidth )
      {
         nucW = newW;
         if ( nucW >= minTextWidth )
            fontSize = fontSizeArray[ nucW - minTextWidth ];
         minTextHeight = nucW * 2;
      }   
      update();
   }
   //------------------------ updateSize( Dimension ) -----------------------------
   /**
    * update the size of the display panel
    */
   public void updateSize( Dimension dim )
   {
      this.setPreferredSize( dim );
      this.setSize( dim );
      Dimension mDim = new Dimension( dim.width, dim.height - seqY );
      update();
   }
   //------------------------ clear() -----------------------------
   /**
    * Clear all information about this display
    */
   public static void clear()
   {
      me.init();
   }
   //------------------------ update() -----------------------------
   /**
    * update the graphical representation of the list being shown
    */
   public static void update()
   {
      if ( GUI.batch )
         return;
      me.revalidate();
      me.repaint();
   }
   //------------------------- useRegion( String ) ---------------------
   /**
    * Start a new vertical region, param is id for later referencing, 
    */
   public static void useRegion( String id )
   {
      me.useRegionP( id, 0 );
   }
   //------------------------- useRegion( String, int ) ---------------------
   /**
    * Start a new vertical region, 1st param is id for later referencing, 
    * 2nd parameter is number of tracks of space between regions.
    */
   public static void useRegion( String id, int space )
   {
      me.useRegionP( id, space );
   }
   //------------------------- useRegionP( String, int ) -------------------
   /**
    * Private non-static method to start a new vertical region, 
    * 1st parameter is id for later reference
    * 2nd parameter is number of tracks of space between regions.
    */
   private void useRegionP( String id, int space )
   {
      Region r = _rNames.get( id );
      if ( r != null )
         clearRegionP( id );
      else
      {
         //Log.info( "Creating region: " + id );
         r = new Region();
         r.name = id;
         r.space = space;
         _rNames.put( id, r );
         _regions.add( r );
         
         // fill in phony tracks
         for ( int i = 0; i < space; i++ )
         {
            r.tracks.add( null );
            r.trackH.add( new Integer( -1 ));
            r.trackY.add( new Integer( -1 ));
         }
         r.hiTrack += space;
         r.minTrack = r.hiTrack + 1;
         r.hiTrackY += space * separatorH;
      }
   }
   //------------------------- clearRegion() ---------------------
   /**
    * Clear all components of the primary region
    */
   public static void clearRegion()
   {
      me.clearRegionP( me._r0 );
   }
   //------------------------- clearRegion( String ) ---------------------
   /**
    * Clear all components of a region
    */
   public static void clearRegion( String id )
   {
      me.clearRegionP( id );
   }
   //------------------------- clearRegionP( String ) ---------------------
   /**
    * Clear all components of a region
    */
   private void clearRegionP( String id )
   {
      Region r = _rNames.get( id );
      if ( r == null )
         Log.error( "DNADisplay.clearRegion: undefined region: " + id );
      else
         clearRegionP( r ); 
   }
   //------------------------- clearRegionP( Region ) ---------------------
   /**
    * Clear all components of a region
    */
   private void clearRegionP( Region r )
   {
      if ( r != null )
      {
         eraseSecondary( r );
         r.reference = null;
      }
   }
   //--------------- assignReadToTrack( Region,  DNASequence ) ------------
   /**
    * Check each of the tracks from top to bottom 
    *    if read fits in track without overlap with existing reads
    *       add it to the track
    *    if track is empty
    *       create new list for track and add this read to it.
    *       track lists are ordered by starting position   
    */
   private void assignReadToTrack( Region r, DNASequence seq )
   {
      if ( GUI.batch || seq == null ) 
         return;

      int t = r.minTrack;   // start at minTrack
      boolean assigned = false;
      
      if ( reUseTracks )  // if can re-use tracks, try to
      {
         while ( t < r.tracks.size() && !assigned )
            assigned = fitsInTrack( r, t++, seq );
      }
      if ( !assigned )
      {
         DNASequenceSet track = new DNASequenceSet();
         track.add( seq );
         r.tracks.add( track );
         r.trackH.add( new Integer( trackHeight ) );
         r.trackY.add( new Integer( r.hiTrackY ));
         r.hiTrackY += trackHeight + trackVOffset;
      }
      if ( t > r.hiTrack )
         r.hiTrack = t;
   }
   //-------------------- fitsInTrack( List, DNASequence ) -------------------
   /**
    * check if the sequence can be displayed in this track, if so, do it
    * return true if it fit, false otherwise
    */
   private boolean fitsInTrack( Region r, int t, DNASequence seq )
   {
      if ( r.trackH.get( t ) == -1 )
         return false;
      DNASequenceSet track = r.tracks.get( t );
      
      Iterator<DNASequence> iter = track.iterator();
      boolean fits = true;
      DNASequence s = null;
      while ( iter.hasNext() && fits )
      {
         s = iter.next();
         fits = !overlap( s, seq );
      }
      
      if ( fits )
         track.add( seq );
      return fits;
   }
   //------------------- overlap( DNASequence, DNASequence )--------------
   /**
    * returns true if the two sequences overlap, false otherwise
    */
   private boolean overlap( DNASequence s1, DNASequence s2 )
   {
      int start1 = s1.getReferencePosition() - trackHSpace * nucW;
      int end1 = start1 + s1.length() - 1 + 2 * trackHSpace * nucW;
      int start2 = s2.getReferencePosition();
      int end2 = start2 + s2.length() - 1;
      
      return !( start2 > end1 || start1 > end2 );
   }
   //------------------- overlap( DNASequence, int )--------------
   /**
    * returns true if the sequence includes the position
    */
   private boolean overlap( DNASequence s, int pos )
   {
      int start = s.getReferencePosition();
      int end = start + s.length() - 1;
     
      return start <= pos && pos <= end;
   }
   //------------------ findReadPicked( Region, int, int ) ---------------
   private String findReadPicked( Region r, int t, int pos )
   {
      if ( t < 0 || t >= r.tracks.size() )
         return null;
      DNASequenceSet track = r.tracks.get( t );
      
      Iterator<DNASequence> iter = track.iterator();
      DNASequence s = null;
      boolean     found = false;
      while ( iter.hasNext() && !found )
      {
         s = iter.next();
         found = overlap( s, pos );
      }
        
      if ( !found )
         return null;
      else 
         return s.toString();
   }
   //----------------------- layoutDisplay(  ) ---------------------
   /**
    * layout - positions the references and tracks
    */
   public void layoutDisplay( Region r, int x, int y )
   {
         int refW = 0;
         r.x = x;
         r.y = y;
         if ( r.reference != null )
         {
            y += nucHeight;
            refW = r.reference.getDNA().length() * nucW;
         }
         r.width = refW; 
         y += ySpace;
         y += placeReads( r, y );
         if ( r.drawSeparator )
            y += separatorH;
         r.height = y - seqY;   // update region height
         y += ySpace;
   }
   //------------ placeReads( Region, int  ) ---------------------
   /**
    * Determine placements of tracks in region, returns y space used
    */
   private int placeReads( Region r, int yStart )
   {
      int trackY = yStart;
      int lastH  = 0;
      for ( int t = 0; t < r.tracks.size(); t++ )
      {
         DNASequenceSet track = r.tracks.get( t );
         trackY = r.trackY.get( t ) + yStart;
         //r.trackY.set( t, trackY );
         int h = r.trackH.get( t );
         if ( h == -1 )   // this is a separator track, draw a line
            lastH = separatorH;
         else
         {
            Iterator<DNASequence> iter = track.iterator();
            while ( iter.hasNext() )
            {
               DNASequence seq = iter.next();
               int p = seq.getReferencePosition();
               if ( p >= 0 )
               {
                  int len = seq.getDNA().length();
                  int leftX = seqX + p * nucW;
                  int rightX = leftX + len * nucW;
                  if ( rightX > r.width )
                     r.width = rightX;
                  lastH = h;
               }
            }
         }
      }
      return trackY + lastH - yStart + 10;
   }
   //----------------------- paintComponent( Graphics ) ---------------------
   /**
    * paintComponent - calls draw and fill awt methods
    */
   public void paintComponent( java.awt.Graphics brush )
   {
      super.paintComponent( brush );
      Graphics2D brush2 = (Graphics2D) brush;
      
      Color saveColor = brush2.getColor();
      Stroke saveStroke = brush2.getStroke();
      int x = seqX;
      int y = seqY;
      int w = 0;    // width of region
     
      for ( Region r: _regions )
      { 
         int refW = 0;
         r.x = x;
         r.y = y;
         if ( r.reference != null )
         {
            y += drawDNA( brush2, r.reference.getDNA(), r.x, r.y, nucHeight );
            refW = r.reference.getDNA().length() * nucW;
         }
         r.width = refW; 
         y += ySpace;
         y += drawReads( r, y, brush2 );
         y += ySpace;
         if ( r.drawSeparator )
            drawSeparator( brush2, y );
         y += ySpace;
         r.height = y - seqY;   // update region height
         y += ySpace;
      }
      brush2.setColor( saveColor );
      brush2.setStroke( saveStroke );
   }
   //------------ drawReads( Region, Graphics2D  ) ---------------------
   /**
    * Generate display for the reads, returns y space used
    */
   private int drawReads( Region r, int yStart, Graphics2D brush )
   {
      int trackY = yStart;
      int lastH  = 0;
      for ( int t = 0; t < r.tracks.size(); t++ )
      {
         DNASequenceSet track = r.tracks.get( t );
         trackY = r.trackY.get( t ) + yStart;
         //r.trackY.set( t, trackY );
         int h = r.trackH.get( t );
         if ( h == -1 )   // this is a separator track, draw a line
         {
            lastH = drawSeparator( brush, trackY );
            if ( track != null && track.size() != 0 )
               Log.warning( "DNADisplay: separator track not empty" );
         }
         else
         {
            Iterator<DNASequence> iter = track.iterator();
            while ( iter.hasNext() )
            {
               DNASequence seq = iter.next();
               int p = seq.getReferencePosition();
               if ( p >= 0 )
               {
                  int len = seq.getDNA().length();
                  int leftX = seqX + p * nucW;
                  drawDNA( brush, seq.getDNA(), leftX, trackY, h );
                  int rightX = leftX + len * nucW;
                  if ( rightX > r.width )
                     r.width = rightX;
                  lastH = h;
               }
            }
         }
      }
      return trackY + lastH - yStart + 10;
   }
   //------------ drawSeparator( Graphics2D, int ) ---------------------------
   /**
    * draw a separator line, returns y space used
    */
   private int drawSeparator( Graphics2D brush, int y )
   {
      brush.setColor( Color.BLACK );
      Stroke saveStroke = brush.getStroke();
      brush.setStroke( new BasicStroke( separatorH ));
      brush.drawLine( 0, y, separatorW, y );
      brush.setStroke( saveStroke );
      return separatorH;
   }
   //------------ drawDNA( Graphics2D, String, int, int, int  ) --------------
   /**
    * Generate display for a dna sequence, returns y space used
    */
   private int drawDNA( Graphics2D brush, String dna, 
                         int xStart, int yStart, int height )
   {
      if ( dna == null )
         return 0;
      int dx = nucW;
      int dy = height;
      int x = xStart;
      int y = yStart;
      
      for ( int n = 0; n < dna.length(); n++ )
      {
         switch ( dna.charAt( n ) )
         {
            case 'A': case 'a': brush.setColor( Color.GREEN ); break;
            case 'T': case 't': brush.setColor( Color.RED.brighter() ); break;
            case 'G': case 'g': brush.setColor( Color.YELLOW ); break;
            case 'C': case 'c': brush.setColor( Color.MAGENTA); break;
            case 'N': case 'n': brush.setColor( Color.CYAN ); break;
            case 'X': case 'x': brush.setColor( Color.GRAY ); break;
            case '-':           brush.setColor( Color.BLACK ); break;
            case '*':           brush.setColor( Color.WHITE ); break;
            default:            brush.setColor( Color.GRAY ); break;
         }
         brush.fillRect( x, y, dx, dy );
         if ( nucW >= minBorderWidth )
         {
            brush.setColor( Color.BLACK );
            brush.drawRect( x, y, dx, dy );
         }
         x += dx;
      } 
      if ( nucW >= minTextWidth && height >= minTextHeight )
      {
         brush.setFont( new Font( "Monospaced", Font.PLAIN, fontSize ));
         brush.setColor( Color.BLACK );
         int textBaseline = (int) ( dy * 0.75 );
         brush.drawString( dna, xStart, yStart + textBaseline );
      }
      return dy;
   }   
   //+++++++++++++++++++++++ Region inner class +++++++++++++++++++++++++++
   private class Region
   {
      String      name;                  // region name
      int         space;                 // space to skip at top
      DNASequence reference = null;      // optional reference
      boolean     drawSeparator = true;  // draw separator line at top of track
      int         x = -1;                // if >= 0 left side of region
      int         y = -1;                // if >= 0 screen position of top
      int         width  = -1;           // if > 0 width of region 
      int         height = -1;           // if > 0 height of region
      
      // variables for organizing reads
      int       minTrack = 0;   // min track allowed to get reads
      int       hiTrack = 0;    // highest # track with reads in it
      int       hiTrackY = 30; // highest Y position of tracks so far
      
      // tracks represent a horizontal strip of display area at a fixed y
      //         with a fixed height
      Vector<DNASequenceSet> tracks = new Vector<DNASequenceSet>(); 
      Vector<Integer>        trackH = new Vector<Integer>(); // height of track 
      Vector<Integer>        trackY = new Vector<Integer>(); // y in region
   }
   
      
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      //String[] myArgs = { "data/ref.txt", "data/reads.txt" };
      String[] myArgs = { "8PrinceContigs.txt" };
      DNAapp.main( myArgs );
   }
}
