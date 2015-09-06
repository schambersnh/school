/**
 * GUIPanel.java -- skeleton class
 * Creates the panel for showing cards
 *
 * 03/18/08: rdb
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;


public class GUIPanel extends JPanel 
{
   //------------------------- instance variables -----------------------
   Game    _game;       // the game object
   
   //------------------------- constructor ----------------------------
   /**
    * GUIPanel constructor creates the game and waits for interaction.
    */
   public GUIPanel() 
   {
      super();                 
      setLayout( new BorderLayout() );

      JPanel gamePanel = new JPanel();
      gamePanel.setLayout( null );
      gamePanel.setBackground( Color.WHITE );

      this.add( gamePanel );
      _game = new Game( gamePanel );

      setSize( 900, 600 );
 
      ///////////////////////////////////////////////////////
      // build the gui
      ///////////////////////////////////////////////////////
      String[] buttonLabels = { "New Game", "Shuffle", "Draw Card",
                                    "Show Deck", "Hide Deck" };
      Component buttonMenu = makeButtonMenu( buttonLabels );
      this.add( buttonMenu, BorderLayout.NORTH );
      
      Component controlMenu = makeControlMenu();
      this.add( controlMenu, BorderLayout.SOUTH );
      
      _game.newGame();
   }
   //------------------- makeControlMenu --------------------------------
   /**
    * create a control menu that includes only a Spinner for the seed
    */
   private Component makeControlMenu( )
   {
      JPanel sMenu = new JPanel(); 
      LabeledSpinner seedSpinner = new LabeledSpinner( "RandomSeed", 0, 30, 0 );
      ///////////////////////////////////////////////////////////////////
      // Code below is an example of an "anonymous" class. We want to extend
      // ChangeListener, but are only going to define 1 method. Rather than
      // creating a whole new class somewhere else in the file, Java lets
      // you define a class without a name at the same time that you
      // create the one instance you will need of that class.
      // Just follow the new statement with { .. }; and include the method
      // (or methods) you want to override.
      ChangeListener  cl = new ChangeListener()
      {  
         public void stateChanged( ChangeEvent ev )
         {  
            JSpinner spinner = (JSpinner) ev.getSource();
            Number value = (Number) spinner.getValue();
            Game.seed = value.intValue();
         }
      };
      seedSpinner.addChangeListener( cl );
      sMenu.add( seedSpinner );
      
      return sMenu;
   }
   //------------------- makeButtonMenu --------------------------------
   /**
    * create the button menu for this application
    */
   private Component makeButtonMenu( String[] labels )
   {
      JPanel bMenu = new JPanel( new GridLayout( 1, 0 )); 
      JButton button;
      for ( int i = 0; i < labels.length; i++ )
      {
         button = new JButton( labels[ i ] );
         bMenu.add( button );
         button.addActionListener( new ButtonListener( i ));
      }
      bMenu.setSize( 40, 400 );
      
      return bMenu;
   }
   //+++++++++++++++++ ButtonListener inner class ++++++++++++++++++++++++
   /**
    * ButtonListener -- distributes button events to appropriate methods
    *                   of the GUIPanel class.
    */
   public class ButtonListener implements ActionListener
   {
      int _buttonId;
      public ButtonListener( int buttonId )
      {
         _buttonId = buttonId;
      }
      public void actionPerformed( ActionEvent ev )
      {
         String msg = null;
         switch ( _buttonId )
         {
            case 0:
               _game.newGame();
               break;
            case 1:
               _game.shuffle();
               break;
            case 2:
               msg = _game.drawCard();
               if ( msg != null )
                  endGame( msg );
               break;
            case 3:
               _game.showDeck();
               break;
            case 4:
               _game.hideDeck();
               break;
          }               
      }
   }
   //------------------- endGame( String ) ----------------------------
   private void endGame( String msg )
   {
      msg = msg + "\nWant a new deck?";
      int choice = JOptionPane.showConfirmDialog( null, msg );
      if ( choice == 0 )
         _game.newGame();
      else if ( choice == 1 )
         System.exit( 0 );
   }
   //------------------ main ------------------------------------------   
   public static void main( String [ ] args ) 
   {
      new CardLab( "Card Lab" );
   }
}   
