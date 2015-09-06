/** 
 * GUI.java:
 * A JPanel to control the application's user interface and its 
 * connection to the main application code.
 * 
 * 
 * This GUI supports the DNAapp program
 *
 * @author rdb
 * Spring 2009
 * Spring 2010 - Modified to be used for contig displays with introns
 * Fall   2010 - Modified to use for gtf access
 * 
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class GUI extends JPanel 
{
   //------------------ class variables ---------------------------
   public static boolean        batch = false;
   public static LabeledSpinner contigSpin;
   public static GUI            me;
   
   //---------------- instance variables ---------------------------
   private DNADisplay        _display;
   private JLabel         _referencePosition; // loc of mouse in reference
   private int            _refPos;
   private JLabel         _seqPicked; // loc of mouse in reference
   private String         _seqPickedId = "";
   private JLabel         _seqInfo;
      
   private DataManager    _dataMgr;
   //------------------- constructor -------------------------------
   /**
    * Container parent of the control panel is passed as an argument
    * along with the application parameters.
    */
   public GUI( DataManager dm ) 
   {
      super ( new BorderLayout() );
      me = this;
      _dataMgr = dm;
      _display = new DNADisplay( this );
      
      int displayWidth = _display.getPreferredSize().width; 
      int displayHeight = _display.getPreferredSize().height;
      
      final JScrollPane sPane = new JScrollPane( _display,
               ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
               ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
      );     
      this.add( sPane, BorderLayout.CENTER );
      
      JPanel northPanel = makeNorth();
      this.add( northPanel, BorderLayout.NORTH );
      
      JPanel southPanel = makeSouth();
      this.add( southPanel, BorderLayout.SOUTH );
      
      sPane.setPreferredSize( new Dimension( displayWidth, displayHeight ));
   }
   //------------------- makeNorth() -----------------------------------
   /**
    * Create buttons and orthlog spinner in North
    */
   private JPanel makeNorth()
   {
      JPanel northPanel = new JPanel( new FlowLayout() );
      
      //create Buttons in the North
      Component buttonMenu = makeButtonMenu();
      northPanel.add( buttonMenu );
      
      int max = 20;
      contigSpin = new LabeledSpinner( "Contig: ", 0, max, 0 );
      contigSpin.addChangeListener(
         new ChangeListener()
         {
            public void stateChanged( ChangeEvent ev )
            {
               JSpinner spinner = (JSpinner) ev.getSource();
               int spinValue = ((Number) spinner.getValue()).intValue();
               _dataMgr.setReference( spinValue );
            }
          }
         );
      northPanel.add( contigSpin);
                   
      return northPanel;
   }
   //------------------- makeSouth() -----------------------------------
   /**
    * Create two sliders and 3 JLabels in the south region
    */
   private JPanel makeSouth()
   {
      //create display panel size scrollbar in the south
      JPanel southPanel = new JPanel( new GridLayout( 0, 1 ));
      JPanel sliderPanel = new JPanel( new GridLayout( 1, 2 ));
      LabeledSlider nucWidth = 
      new LabeledSlider( "NucWidth", 1, 11, 10 )
         { 
            public void stateChanged( ChangeEvent ev )
            {
               int newWidth = ((JSlider)ev.getSource()).getValue() ;
               _display.setNucleotideWidth( newWidth );
            }
         };
      nucWidth.getJSlider().setMajorTickSpacing( 1 );
      sliderPanel.add( nucWidth );         
      
      int displayWidth = _display.getPreferredSize().width;
      LabeledSlider paneWidth = 
         new LabeledSlider( "PaneWidth", 0, 100000, displayWidth )
         { 
            public void stateChanged( ChangeEvent ev )
            {
               int h = _display.getPreferredSize().height;
               int newWidth = ((JSlider)ev.getSource()).getValue() ;
               _display.updateSize( new Dimension( newWidth, h ));
               this.revalidate();
            }
         };
      paneWidth.getJSlider().setMajorTickSpacing( 50000 );
      sliderPanel.add( paneWidth ); 
      
      JPanel labelPanel = new JPanel( new GridLayout( 1, 3 ));
      createLabels( labelPanel );
      southPanel.add( sliderPanel );
      southPanel.add( labelPanel );
      return southPanel;
   }
   //--------------------- createLabels( Panel ) -----------------------
   /**
    * create information labels and add to specified panel
    */
   private void createLabels( JPanel panel )
   {
      // create a label for reference  position of mouse
      _referencePosition = new JLabel( " Reference position: " + _refPos ); 
      _referencePosition.setBorder( new LineBorder( Color.BLACK ) );
      panel.add( _referencePosition );
      
      // create a label for last picked sequence
      _seqPicked = new JLabel( " Seq: " + _seqPickedId );
      _seqPicked.setBorder( new LineBorder( Color.BLACK ) );
      panel.add( _seqPicked );

      // create a label for contig information 
      _seqInfo = new JLabel( " DNASequence: "  );
      _seqInfo.setBorder( new LineBorder( Color.BLACK ) );
      panel.add( _seqInfo );
   }      

   //----------------- updateCurGene( String ) ------------------------
   // The contig has changed, show that in the label and the Spinner
   //
   public void updateCurGene( int index, String label )
   {
      if ( _seqInfo != null )
          _seqInfo.setText( label );
      if ( contigSpin != null )
         contigSpin.getJSpinner().setValue( index );
   }
   //----------------- setReferencePosition --------------------------
   public void setReferencePosition( int pos )
   {
      _refPos = pos;
      _referencePosition.setText( " Reference position: " + _refPos + "  " );
   }
   //----------------- setSequencePicked( String ) --------------------------
   public void setSequencePicked( String id )
   {
      _seqPickedId = id;
      _seqPicked.setText( " Seq: " + id );
   }
   //----------------------- makeButtonMenu --------------------------------
   private Component makeButtonMenu()
   {
      // JPanel defaults to FlowLayout
      String[] labels = { "Open File", "Find ORFs", "RevComp" };
      
      JPanel bMenu = new JPanel( new GridLayout( 1, 0 )); 
      JButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         button = new JButton( labels[ i ] );
         //button.setFont( getFont().deriveFont( 11.0f ));
         bMenu.add( button );
         button.addActionListener( new ButtonListener( i ));
      }      
      return bMenu;
   }
   //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener handles all button events and passes them along
    * to methods of the ListPanel class.
    */
   private class ButtonListener implements ActionListener
   {
      int _buttonId;
      public ButtonListener( int buttonId )
      {
         _buttonId = buttonId;
      }
      public void actionPerformed( ActionEvent ev )
      {
          switch ( _buttonId )
          {
             case 0:   // Open new seq file
                readFile();
                break;
             case 1:   // show orfs
                _dataMgr.findORFs();
                break;
             case 2:   // Reverse complement
                _dataMgr.reverseComplement();
                break;
          }
      }
   } 
   //---------------------- readFile() -------------------------------
   /**
    * read the sequence file
    */
   private void readFile( )
   {
      String fileName = Utilities.getFileName( "Choose Sequence file" );
      
      if ( fileName == null || fileName.length() == 0 )
         return;
      _dataMgr.readSeqFile( fileName );
   }
   //-------------------setContigSpinner( int, int ) ------------------
   /**
    * Set the current and max values of the contigSpinner
    */
   public static void setContigSpinner( int cur, int max )
   {               
         SpinnerModel model = new SpinnerNumberModel( 0, cur, max, 1 );
         contigSpin.getJSpinner().setModel( model );
   }
   //--------------- setReference( DNASequence ) -------------------------
   /**
    * set the reference display to be the DNASequence
    */
   public static void setReference( DNASequence ref )
   {
      if ( ref != null )
         me._seqInfo.setText( ref.toString() );
      else
         me._seqInfo.setText( "---------" );
   }
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      String[] defaultArgs = { "simpleDNA.txt" };
      new DNAapp( "DNAapp", defaultArgs );
   }
}
