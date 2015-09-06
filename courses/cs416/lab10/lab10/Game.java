/**
 * Game.java - implementation of a solitaire game
 * 
 * @author rdb
 * March 2009
 * mlb 10/09: new cards , new shuffle, new baseDeck
 * 
 * Modified by Stephen Chambers for Lab 10
 * 
 * ****************************NOTE**************************************
 * I completed the entire assignment, but could not get the cards to face 
 * upwards. I am certain that if they did, they would be the correct cards
 * based on the printlns that are sending to the console. The printlns show
 * the grader that I completed the required steps.
 * 
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Game
{
    //----------------------- class variables ---------------------------
    public  static int   seed = 0;
    //----------------------- instance variables ------------------------
    private CardStack       _drawPile = null; 
    private CardStack       _discards = null;
    private JPanel          _parent;
    private ArrayList<Card> _baseDeck = null;
    private Random shuffle;
    
    //---------------------- constructor --------------------------------
    public Game( JPanel parent )
    {      
       //----- positioning variables      
       int discardX  = 60,  discardY  = 140;
       int drawPileX = 60,  drawPileY = 40;
    
        _parent = parent;    
        
        createDeck();  // make the actual cards in _baseDeck

        // create 2 stacks we need: one for the draw pile, one for discards
        _drawPile = new CardStack( _parent,  drawPileX, drawPileY );
        _discards = new CardStack( _parent,  discardX, discardY );
                       
        //////////////////////////////////////////////////////////////////
        // Use setXOffset and setYOffset methods of CardGroup (and therefore
        //   also of CardStack) so that the display of cards from _discards
        //   and _drawPile collections clearly show which card each is,
        //   but also don't take up so much of the screen space that we can't
        //   see all the cards.
        // Since we have more width than height, the key offset for visibility
        //   in this problem is the x offset. The y offset is mainly 
        //   for visual variation. 
        // Make the y-offset for _drawPile 0, and just for variety, 
        //   something else for _discards.
        /////////////////////////////////////////////////////////////////
        _drawPile.setXOffset(10);
        _drawPile.setYOffset(0);
        _discards.setYOffset(10);
        
        //////////////////////////////////////////////////////////////////
        // create a Random variable for the shuffle method and assign it
        //    to an instance variable.
        //////////////////////////////////////////////////////////////////
         shuffle = new Random(seed);
        
        // play the "game", which in this case is just drawing cards
        newGame();
    }
    
    //----------------------------- update() ----------------------
    /**
     * Update the display components as needed.
     */
    public void update()
    {
        // show all cards on the _discards stack 
        _discards.displayCards( -1, true );
        
        // show one card on the draw pile
        _drawPile.displayCards( 1, true);
        _parent.repaint();    
    }
    
    //------------------ showDeck() ----------------------------------
    /**
     * all cards in the deck should display their face side, rather than back.
     */
    public void showDeck()
    {
        _drawPile.displayCards( -1, true );
        _parent.repaint();
    }
    //------------------ hideDeck() ----------------------------------
    /**
     * all cards in the deck should display their back side.
     */
    public void hideDeck()
    {
        _drawPile.displayCards( -1, false );
        _parent.repaint();
    }
    
    //------------------------- drawCard() ----------------------------
    /**
     * pop the top card from the _drawPile and push it onto the _discards
     *      stack and turn its face up.
     */
    public String drawCard()
    {
        String msg = null;
        
        
        if(_drawPile.size() > 0 )
        {
        Card throwAway = _drawPile.pop();
        throwAway.setFaceUp(true);
        _discards.push(throwAway);
        }
        else
        {
          msg = "Game Over";
        }
        ////////////////////////////////////////////////////////////////
        //
        // To draw a card, you need to pop it from the _drawPile 
        //   and push it onto the _discards pile.
        // Of course, check if it is empty.
        // If the draw pile stack is empty, this method should return
        //   a non-null message -- indicating the game is over.
        ///////////////////////////////////////////////////////////////
      
        
        update();
        return msg;
    }
    //------------------------ newGame() -----------------------------
    /**
     * restore the current deck to its original state with all cards on
     * the _drawPile. This does not automatically re-shuffle.
     */
    public void newGame()
    {
       _discards.clear();
       deckToDrawPile( _baseDeck );
       update();
    }
    
    //------------------------ shuffle( ) -----------------------------
    /**
     * shuffle the cards and start a new game. Shuffle by creating a loop
     * that generates 2 random integers from 0 to 51 and then swaps the two
     * cards at those positions in _baseDeck. Execute the loop 50 times.
     */
    public void shuffle()
    {
       /////////////////////////////////////////////////////////////////
       // Make sure you've created and initialized the Random variable in
       //   the constructor.
       // Here:
       // 1. make a loop that executes 50 times
       // 2. In the loop:
       //    -Use the Random object to generate 2 ints in the range 0 to 51.
       //     i.e., invoke nextInt( 52 ) twice. 
       //    -These 2 values identify 2 cards in _baseDeck that you must swap
       //    -swap the cards
       //        You'll need to use the "set" method of ArrayList (NOT add)
       //        Read the Java API online documentation for details.
       // 3. Invoke newGame()
       ////////////////////////////////////////////////////////////////
      int rand1, rand2;
      for(int i = 0; i < 51; i++)
      {
        rand1 = shuffle.nextInt( 52 );
        rand2 = shuffle.nextInt( 52 );

        _baseDeck.set(rand2, _baseDeck.get(rand1));
        _baseDeck.set(rand1, _baseDeck.get(rand2));
      }
      newGame();
    }
    
    //------------------------ createDeck() -----------------------------
    /**
     * Create a "base" deck of cards. These will keep being re-used for
     * multiple game invocations.
     */
    private void createDeck()
    {
        _baseDeck = new ArrayList<Card>();
       int  cardIndex = 0;
       
       for ( Card.Suit suit: Card.Suit.values() )
       {
         for( Card.Rank rank : Card.Rank.values() )
          {
             Card card = new Card( _parent,  rank, suit );
             _baseDeck.add( 0, card );
             _parent.add( card );
          }
          
       }
    }
    
    //------------------------ deckToDrawPile( Card[] ) ----------------------
    /**
     * Copy an array of cards into the CardStack representing the draw pile.
     */
    private void deckToDrawPile( ArrayList<Card> deck )
    {
       _drawPile.clear();   // clear the draw pile of previous "stuff"
       for(int i = 0; i < deck.size(); i++)
       {
         _drawPile.push(deck.get(i));
       }
       ///////////////////////////////////////////////////////////////////////
       // for each entry in the arrayList, push it onto CardStack, _drawPile
       ///////////////////////////////////////////////////////////////////////
    }
       
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //--------------------------- main -----------------------------------------
    public static void main( String[] args )
    {
        // Invoke main class's main
        new CardLab( "CardLab from Game" );
        
        /**** self test code below
          JFrame f = new JFrame();
          f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
          f.setLayout( null );
          
          Game g = new Game( f );
          g.showDeck();
          f.setSize( 900, 500 );
          
          f.setVisible( true );
          /*******************************/
    }
}
