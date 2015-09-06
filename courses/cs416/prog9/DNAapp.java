/**
 * DNAapp -- App class for dna based applications
 * 
 * @author rdb
 * 03/10/09
 * 
 * 11/14/10 modified for gtf dna assignment
 */
import javax.swing.*;
import java.awt.*;

public class DNAapp extends JFrame
{
   //---------------------- instance variables ----------------------
   private GUI _appPanel;      // the app's JPanel
   
   //--------------------------- constructor -----------------------
   public DNAapp( String title, String[] args )     
   {
      super( title );
 
      this.setBackground( Color.LIGHT_GRAY );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      //Log.addTraceKey( "test" );
      
      // optional command line argument is the sequence data file
      String seqFileName = null;
      if ( args.length > 0 )
         seqFileName = args[ 0 ];
      
      DataManager dm = new DataManager();
      _appPanel = new GUI( dm );
      this.add( _appPanel );            
      
      if ( seqFileName != null )
         dm.readSeqFile( seqFileName );
      
      this.setSize( 1000, 600 );
      this.setVisible( true );
   }
  //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      new DNAapp( "DNA ORF application", args );
   }
}
