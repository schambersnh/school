/**
 * Game.java - implementation of a card game
 * 
 * @author rdb
 * March 2009
 * mlb 10/09: new cards , new shuffle, new baseDeck
 */

import javax.swing.*;
import java.util.*;

public class Game
{
  //----------------------- class variables ---------------------------
  public  static int       seed = 0;
  
  //----------------------- instance variables ------------------------
  
  private JPanel          _parent;
  private ArrayList<Card> _baseDeck = null;
  private CardStack       _deck = null;
  private Random          _rng = null;     // DO NOT USE THIS VARIABLE
  
  private Hand[]          _hands;
  private int            _curHand;
  private int            _leadingHand = 0;
  private CardStack[]     _tricks;
  private Card[] _cardsPlayed = new Card[4];
  private Card.Suit       _suitPlayed;
  private boolean         _hasSuit;
  private int             _turn;
  
  
  
  //------------------  constants --------------------------------
  //----- positioning variables   
  private int xOffset  = 15;        // "standard" card offset
  private int handDY   = 100;       // offset of hands rows
  private int handX    = 150;       // x-loc for all hands
  private int topHandY = 200;       // top hands row
  private int tricksX  = 40;        // x-loc for all tricks stacks
  private int deckX    = 40;
  private int deckY    = 20;
  private int cardPlayedY = 480;
  
  private String label[] = { "North", "East", "South", "West" };
  
  //---------------------- constructor --------------------------------
  public Game( JPanel parent )
  {         
    _parent = parent; 
    
    createBaseDeck();   // build the base deck
    _deck = new CardStack( _parent, deckX, deckY );
    _deck.setXOffset( xOffset );
    
    _hands  = new Hand[ 4 ];
    _tricks = new CardStack[ 4 ];
    
    for ( int i = 0, y = topHandY; i < _hands.length; i++, y += handDY )
    {
      _hands[ i ]  = new Hand( _parent, handX, y );
      _hands[ i ].setXOffset( xOffset );
      
      _tricks[ i ] = new CardStack( _parent, tricksX, y );
      _tricks[ i ].makeBaseIcon( label[ i ] );
    }
    
    _rng = new Random( seed );
    
    makeDeck(); // make deck from which to deal 
    
  }
  
  //------------------------- play() ----------------------------
  /**
   * One player must choose a card to lead, others must follow
   * suit if possible. Highest card of the suit led wins and
   * starts the next play. Player 0 is first player.
   * 
   * The String msg returned to GUIPanel is a mechanism for telling
   * the GUIPanel when the game is over (or if a serious error 
   * occurred). This method should normall return null (not a zero-length
   * string). Any non-null return value is interpreted as the end of 
   * the game.
   */
  public String play()
  {
    /*
     * Null pointers before whoWon() is called
     * Adding tricks to hand
     * */
    
    String msg = "Game Over";
    ///////////////////////////////////////////////////////////
    // Implement the method. Be sure to use lots of methods!!!
    //    You might think about whether any classes might help
    ///////////////////////////////////////////////////////////
    
     if(_turn == 4)
    { 
      //testMethods(_hands[3].getCards(), _suitPlayed );
      JOptionPane.showMessageDialog(null, "Hand " + 
                                    whoWon(_cardsPlayed, _suitPlayed) + " won.");
      hideHands();
      if(_hands[_curHand].size() == 0)
      {
        showTricks();
        gameOver();
         return msg;
      }
      _turn = 0;
      return null;
    }
    else
    {
      _turn++;
    }
    Card temp = playCard(_hands[_curHand].getCards(), _suitPlayed);
    //System.err.println(_curHand);
    //Card temp = _hands[_curHand].get(0);
    System.out.println(temp);
    if(_curHand == _leadingHand)
    {
      _suitPlayed = temp.getSuit();
    }
    _hands[_curHand].remove(temp);

    _cardsPlayed[_curHand] = temp;
      
    temp.setFaceUp(true);
    temp.setLocation(cardPlayedY, temp.getY());
    _parent.add(temp);
    
      if(_curHand == 3)
    {
      _curHand = 0;
    }
    else
    {
      _curHand++;
    } 
     // In order to prevent infinite loop when
    // "Play to end" is clicked, the starter
    // code returns a non-null message.
    // as soon as you start working on play(),
    // you need to return null.
    return null;  // return null if game is NOT over
  }
  public Card playCard(CardList cards, Card.Suit mySuit)
  {
    
    if(mySuit == null || _curHand == _leadingHand)
    {
      
      Card _firstCard = getLowestCard(cards);
      _suitPlayed = _firstCard.getSuit();
      
      return _firstCard;
    }
    else if(isLastPlayer() && hasWinningSuit(cards, mySuit) && hasPointCards(_cardsPlayed))
    {
      
      
      return getHighestCard(cards, mySuit);
    }
    else if(hasPointCards(_cardsPlayed) || !isLastPlayer())
    {
      
      
      return getHigh(cards, mySuit);
    }
    
    if(!hasSuit(cards, mySuit))
    {
      Card queen = _hands[_curHand].find(Card.Suit.HEARTS, Card.Rank.QUEEN);
      if(queen == null)
      {
        return getHighestCard(cards, Card.Suit.HEARTS);
      }
      else
      {
        return queen;
      }
    }
    else
    {
      ;
      
      return getHighestCard(cards, mySuit);
    }
    
  }
  public void testMethods(CardList cards, Card.Suit mySuit)
  {
//    System.out.println("getHigh: " + getHigh(cards, mySuit));
//    System.out.println("hasWinningSuit: " + hasWinningSuit(cards, mySuit));
//    System.out.println("isLastPlayer: " + isLastPlayer());
//    System.out.println("getLowestCard(CardList, suit): " + getLowestCard(cards, mySuit));
//    System.out.println("getLowestCard(CardList): " + getLowestCard(cards));                  
//    System.out.println("getHighestCard(CardList, suit): " + getHighestCard(cards, mySuit));
//    System.out.println("getHighestCard(ArrayList<Card>, suit): " + getHighestCard(_cardsPlayed, mySuit));
//    System.out.println("hasPointCards: " + hasPointCards(_cardsPlayed));
  }
  //---------------------------getHigh(CardList, Card.Suit)--------------------
  /*
   * Gets the highest card that will NOT win the trick
   * */
  public Card getHigh(CardList cards, Card.Suit mySuit)
  {
   
    CardList temp = _hands[_curHand].getSuitList(mySuit);
   
    if(temp.size() == 0)
    {
      //return getHighestCard(cards, mySuit);
      System.err.println("cards.get(0) " + cards.get(0));
      System.err.println("cardsSize: " + cards.size());
      return cards.get(0);
    }
    int highestCard = getHighestCard(_cardsPlayed, mySuit).getRank().ordinal();
    
    int max = 0;
    int index = 0;
    for(int i = 0; i < temp.size(); i++)
    {
      if(temp.get(i).getRank().ordinal() > max)
      {
        if(max < highestCard)
        {
          max = temp.get(i).getRank().ordinal();
          index = i;
        }
      }
    }
    //System.err.println("GetHighTempReturn: " + temp.get(index));
    return temp.get(index);
  }
  //-------------------hasSuit(CardList, Card.Suit )-------------
  public boolean hasWinningSuit(CardList cards, Card.Suit mySuit)
  {
    for(int i = 0; i < cards.size(); i++)
    {
      if(cards.get(i).getSuit().ordinal() == mySuit.ordinal() && 
         cards.get(i).getRank().ordinal() > 
         getHighestCard(_cardsPlayed, mySuit).getRank().ordinal() )
      {
        _hasSuit = true;
      }
    }
    return _hasSuit;
  }
  //-----------------hasSuit()---------------------------------
  public boolean hasSuit(CardList cards, Card.Suit mySuit)
  {
    for(int i = 0; i < cards.size(); i++)
    {
      if(cards.get(i).getSuit().ordinal() == mySuit.ordinal())
      {
        _hasSuit = true;
      }
    }
    return _hasSuit;
  }
  //-------------------------isLastPlayer()-----------------------------------
  public boolean isLastPlayer()
  {
    
    boolean lastPlayer = false;
    if(_curHand == _leadingHand - 1 || (_leadingHand == 0 && _curHand == 3))
    {
      lastPlayer = true;
    }
    return lastPlayer;
  }
  //---------------------getLowestCard(CardList, Card.Suit)------------------
  public Card getLowestCard(CardList cards, Card.Suit mySuit)
  {
    int min = cards.get(0).getRank().ordinal();
    int index = 0;
    for(int i = 0; i < cards.size(); i++)
    {
      if(cards.get(i).getRank().ordinal() < min && cards.get(i).getSuit() == mySuit)
      {
        min = cards.get(i).getRank().ordinal();
        index = i;
      }
    }
    return cards.get(index);
  }
  //---------------------getLowestCard(CardList)------------------
  public Card getLowestCard(CardList cards)
  {
    int min = cards.get(0).getRank().ordinal();
    Card.Suit minSuit = cards.get(0).getSuit();
    

    int index = 0;
    for(int i = 0; i < cards.size(); i++)
    {
      if(cards.get(i).getRank().ordinal() < min && cards.get(i).getSuit() == minSuit)
      {
        min = cards.get(i).getRank().ordinal();
        index = i;
      }
    }
    return cards.get(index);
  }
  //---------------------getHighestCard(CardList, Card.Suit)------------------
  public Card getHighestCard(CardList cards, Card.Suit mySuit)
  {
    int max = 0;
    int index = 0;
    for(int i = 0; i < cards.size(); i++)
    {
      if(cards.get(i).getRank().ordinal() > max && cards.get(i).getSuit() == mySuit)
      {
        max = cards.get(i).getRank().ordinal();
        index = i;
      }
    }
    System.err.println("Last Card: " + cards.get(index));
    return cards.get(index);
  }
  //---------------------getHighestCard(ArrayList, Card.Suit)------------------
  public Card getHighestCard(Card[] cards, Card.Suit mySuit)
  {
    int max = -1;
    int index = 0;
    for(int i = 0; i < cards.length; i++)
    {
      if (cards[i]!= null)
      {
      if(cards[i].getRank().ordinal() > max && cards[i].getSuit() == mySuit)
      {
        max = cards[i].getRank().ordinal();
        index = i;
      }
      }
    }
    return cards[index];
  }
  //-----------------------hasPointCards(ArrayList)----------------------------
  public boolean hasPointCards(Card[] cards)
  {
    boolean hasPointCards = false;
    for(int i = 0; i < cards.length; i++)
    {
      if(cards[i]!= null)
      {
      if(cards[i].getSuit().ordinal() == 2 || 
         (cards[i].getRank().ordinal() == 12 && cards[i].getSuit().ordinal() == 3))
      {
        hasPointCards = true;
      }
      }
    }
    return hasPointCards;
  }
//----------------------------getResults()-----------------------------------
  public int[] getResults()
  {
    int heartAppearances = 0;
    int queenOfSpades = 0;
    int[] results = new int[4];
    for(int i = 0; i < 4; i++)
    {
      for(int j = 0; j < _tricks[i].size(); j++)
      {
        Card temp = _tricks[i].pop();
        if(temp.getRank().ordinal() == 12 && temp.getSuit().ordinal() == 3)
        {
          results[i] += 13;
          queenOfSpades++;
        }
        else if(temp.getSuit().ordinal() == 2)
        {
          results[i]++;
          heartAppearances++;
        }
        if(heartAppearances == 12 && queenOfSpades == 1)
        {
          results[i] = 0;
          shotTheMoon(i, results);
        }
        heartAppearances = 0;
      }
    }
    return results;
  }
  public void shotTheMoon(int i, int[] results)
  {
    if(i == 0)
    {
      results[1] = 26;
      results[2] = 26;
      results[3] = 26;
    }
    else if(i == 1)
    {
      results[0] = 26;
      results[2] = 26;
      results[3] = 26;  
    }
    else if(i == 2)
    {
      results[0] = 26;
      results[1] = 26;
      results[3] = 26;  
    }
    else if(i == 3)
    {
      results[0] = 26;
      results[2] = 26;
      results[1] = 26;  
    }
    
  }
  public int whoWon(Card[] cards, Card.Suit mySuit)
  {
    int max = 0;
    int index = 0;
    for(int i = 0; i < cards.length; i++)
    {
      if(cards[i].getRank().ordinal() > max && cards[i].getSuit() == mySuit)
      {
        max = cards[i].getRank().ordinal();
        index = i;
      }
    }
    
    int yloc = cards[index].getY();
    for(int i = 0; i < cards.length; i++)
    {
      _tricks[index].push(cards[i]);
      cards[i].setLocation(tricksX, yloc);
      cards[i] = null;
    }
    _leadingHand = index;
    _curHand = index;
    System.err.println(  "curHand: " + _curHand + "\nleadingHand: " + _leadingHand);
    return index;
  }
  
  //------------------------- gameOver ---------------------------
  /**
   * The game is over, compute score and build a message
   */
  private String gameOver()
  {
//    System.out.println("Tricks:");
//    for(int i = 0; i < 4; i++)
//    {
//      for(int j = 0; j < _tricks[i].size(); j++)
//      {
//        System.out.println(_tricks[i].pop());
//      }
//    }
    for(int i = 0; i < 4; i++)
    {
      System.out.println(_tricks[i]);
    }
    String s = "Game results: \n";
    int[] results = getResults();
    JOptionPane.showMessageDialog(null, "Hand 1: " + results[0] +
                                  "\nHand 2: " + results[1] +
                                  "\nHand 3: " + results[2] +
                                  "\nHand 4: " + results[3]);
    ///////////////////////////////////////////////////////////////
    // Expand this code
    ///////////////////////////////////////////////////////////////
    return s;
  }
  //------------------------ newGame() -----------------------------
  /**
   * Shuffle the cards and deal them to the hands
   */
  public void newGame()
  {
    ////////////////////////////////////////////////////////////
    // Implement this method
    //
    // The existing methods makeDeck, createBaseDeck, and shuffle below
    //    will be useful.
    /////////////////////////////////////////////////////////////
    
    shuffle( );
    makeDeck( );
    
    _parent.repaint( );
    
    //Clear whatever cards are in the hands
    for(int i = 0; i < 4; i++)
    {
      _hands[ i ].clear( );
    }
    for(int i = 0; i < 4; i++) //number of players is always 4
    {
      for(int j = 0; j < 13; j++) //number of cards is always 13
      {
        _hands[ i ].add( _deck.pop( ) ); //for each hand, add 13 cards
      }
    }
    _curHand = 0;
    _leadingHand = 0;
    _turn = 0;
    hideHands( ); //makes it so cards facedown are shown on first press of 
    //newGame
  }
  
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  ////////////////////////////////////////////////////////////////////
  //
  // You probably don't need to change any code below here.
  //
  ///////////////////////////////////////////////////////////////////
  //------------------ showHands() ----------------------------------
  /**
   * all cards in all hands should display their face side, rather than back.
   * also show the deck -- in case it's not empty
   */
  public void showHands()
  {
    for ( int i = 0; i < _hands.length; i++ )
      _hands[ i ].displayCards( -1, true );
    _deck.displayCards( -1, true );
    _parent.repaint();
  }
  
  //------------------ hideHands() ----------------------------------
  /**
   * all cards in the deck should display their back side.
   */
  public void hideHands()
  {
    for ( int i = 0; i < _hands.length; i++ )
      _hands[ i ].displayCards( -1, false );
    _deck.displayCards( 0, false );
    _parent.repaint();
  }
  //------------------ showTricks() ----------------------------------
  /**
   * all cards in the deck should display their face side, rather than back.
   */
  public void showTricks()
  {
    for ( int i = 0; i < _hands.length; i++ )
    {
      _tricks[ i ].setXOffset( xOffset );
      _tricks[ i ].displayCards( -1, true );
    }
    _parent.repaint();
  }
  //------------------ hideTricks() ----------------------------------
  /**
   * all cards in the deck should display their back side.
   */
  public void hideTricks()
  {
    for ( int i = 0; i < _hands.length; i++ )
    {
      _tricks[ i ].setXOffset( 0 );
      _tricks[ i ].displayCards( -1, false );
    }
    _parent.repaint();
  }
  //------------------------ shuffle( ) -----------------------------
  /**
   * shuffle the cards in _baseDeck. Shuffle by creating a loop
   * that generates 2 random integers from 0 to 51 and then swaps the two
   * cards at those positions in _baseDeck. Execute the loop 50 times.
   */
  public void shuffle()
  {
    //////////////////////////////////////////////////////////////
    // DO NOT MODIFY THIS CODE
    //
    // DO NOT USE THE _rng VARIABLE FOR ANYTHING ELSE
    //////////////////////////////////////////////////////////////
    for ( int i = 0; i < 50; i++ )
    {
      int p1 = _rng.nextInt( 52 );
      int p2 = _rng.nextInt( 52 );
      Card temp = _baseDeck.get( p1 );
      _baseDeck.set( p1, _baseDeck.get( p2 ) );
      _baseDeck.set( p2, temp );
    }
  }
  //------------------------ makeDeck() -------------------------
  /**
   * Make the deck of cards from _baseDeck
   */
  private void makeDeck()
  {
    if ( _deck != null )
      _deck.clear();
    _deck.displayCards( -1, false );
    
    for ( int c = 0; c < _baseDeck.size(); c++ )
      _deck.push( _baseDeck.get( c ));
    _deck.displayCards( 0, false );
  }
  //------------------------ createBaseDeck() -----------------------------
  /**
   * Create a "base" deck of cards. These will keep being re-used for
   * multiple game invocations.
   */
  private void createBaseDeck()
  {
    _baseDeck = new ArrayList<Card>();
    
    for ( Card.Suit suit: Card.Suit.values() )
    {
      for ( Card.Rank rank: Card.Rank.values() )
      {
        Card card = new Card( _parent,  rank, suit );
        _baseDeck.add( card );
        _parent.add( card );
      }
    }
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  //--------------------------- main -----------------------------------------
  public static void main( String[] args )
  {
    // Invoke main class's main
    new Hearts( "Hearts from Game" );
    
    /**** self test code below
      JFrame f = new JFrame();
      f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      f.setLayout( null );
      
      Game g = new Game( f );
      g.showHands();
      f.setSize( 900, 500 );
      
      f.setVisible( true );
      /*******************************/
  }
}
