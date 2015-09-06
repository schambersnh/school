/**
 * CrapsApp.java
 * Displays a game of Craps
 * 
 * @Author Stephen Chambers
 * 10/27/10
*/

import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class CrapsApp extends ShapeGroup
{
  //---------------- instance variables ----------------------------------------
  protected Rectangle rectMoney, betRect;
  protected Dice myDice1, myDice2;
  protected int myCash = 100, bet = 15, myDice1Val, myDice2Val;
  protected Point endPoint;
  protected TextBox betText, outcome;
  protected Color myColor;  
  
  //----------------------------------------------------------------------------
   /*
   * Constructor initializing display
   */
  public CrapsApp()
  {
    rectMoney = new Rectangle( 100, 250 );
    rectMoney.setSize( myCash, 10 );
    rectMoney.setFrameColor( Color.BLACK );
    rectMoney.setFillColor( Color.WHITE );
    
    betText = new TextBox( 100, 300 );
    betText.setText( "Bet " + bet + " / " + myCash  );
    
    outcome = new TextBox( 200, 100 );
    
    betRect = new Rectangle( 100, 250 );
    betRect.setSize( bet, 10 );
    betRect.setFillColor( Color.RED );
    betRect.setFrameColor( Color.BLACK );
    add( betRect );
    add( rectMoney ); 
  }
  //----------------------------------------------------------------------------
  /**
   
   * Rolls Dice~ initializes 2 new Dice with random values, then Checks Results
   */
  public void roll()
  {
    if (bet > 0)
    {
    myDice1 = new Dice ( 50, 100 );
    myDice2 = new Dice ( 110, 100 );
    myDice1Val = myDice1.getNumDie();
    myDice2Val = myDice2.getNumDie();
    check( myDice1Val, myDice2Val, bet );
    }
    else
    {
      //Exit the Program
      outcome.setText("Game Over");
      System.exit(0);
    }
  }
/*
 * Checks what needs to be paid out or taken away
 * 
 * */
  
  public int check( int numDie1, int numDie2, int bet )
  {
    if( numDie1 + numDie2 == 2 )//payoff 3:1
    {
      myCash += bet * 3;      
      setColor( Color.GREEN );
      checkCash();
      outcome.setText( "You Scored 2! Won: " + bet * 3 );
     
    }
    else if( numDie1 + numDie2 == 3 || numDie1 + numDie2 == 4 )//payoff 1:1
    {
      myCash += bet;     
      setColor( Color.GREEN );
      checkCash();
      outcome.setText( "You Scored 3 or 4! Won: " + bet );
      
    }
    else if( numDie1 + numDie2 == 10 || numDie1 + numDie2 == 11 )//payoff 2:1
    {
      myCash += bet * 2;     
      setColor( Color.GREEN );
      checkCash();
      outcome.setText( "You Scored 10 or 11! Won: " + bet * 2 );
      
    }
    else if( numDie1 + numDie2 == 12 )//payoff 5:1
    {
      myCash += bet * 5;      
      setColor( Color.GREEN );
      checkCash();
      outcome.setText( "You Scored 12! Won: " + bet * 5 );
    }
    else//lose 
    {
      myCash -= bet;      
      setColor( Color.RED );
      checkCash();
      outcome.setText( "You lost. Lost: " + bet );
    }
    return myCash;
  } 
  public void mouseReleased( java.awt.event.MouseEvent me )
  {
    
  } 
  //----------------------------------------------------------------------------
  /**
   * Stores current mouse point for slider.
   * 
   */
  public void mousePressed( java.awt.event.MouseEvent me )
  {
    endPoint = me.getPoint();
  }
  //----------------------------------------------------------------------------
  /**

   * Drags Slider
   */
  public void mouseDragged( java.awt.event.MouseEvent me )
  {
    Point point = me.getPoint();
    int diffX = point.x - endPoint.x;
    bet = bet + diffX;
    if( bet <= myCash && bet >= 0 )
    {
      betRect.setSize( bet, 10 );      
      betText.setText( "Bet " + bet + " / " + myCash  );      
    }
    else if( bet < 0 )
    {
      bet = 0;
      betText.setText( "Bet " + bet + " / " + myCash  );      
    }
    else
    {
      bet = myCash;
      betRect.setSize( bet, 10 );
      betText.setText( "Bet " + bet + " / " + myCash );      
    }    
    endPoint = point;
  } 
  //----------------------------------------------------------------------------
  /**
   * Check cash
   */
  public void checkCash()
  {
    if( myCash <= 0 )
    {
      myCash = 0;
      bet = 0;
      betText.setText( "Bet " + bet + " / " + myCash  );
      betRect.setSize( bet, 10 );
      rectMoney.setSize( myCash, 10 );      
    }
    else if( bet > myCash )
    {
      bet = myCash;
      betRect.setSize( bet, 10 );
      betText.setText( "Bet " + bet + " / " + myCash  );
      rectMoney.setSize( myCash, 10 );
    }
    else
    {
      betRect.setSize( bet, 10 );
      betText.setText( "Bet " + bet + " / " + myCash  );
      rectMoney.setSize( myCash, 10 );
    }
    
  }  
  //----------------------------------------------------------------------------
  /**
   * Sets Color of both Die
   * 
   */
  public void setColor( Color myColor )
  {
    myDice1.setColor( myColor );
    myDice2.setColor( myColor );
  } 
  //----------------------------------------------------------------------------
  /**
   * Main Method~ initializes the CrapsApp and Roll Button
   * 
   */
  public static void main( String[ ] args )
  {      
    new Frame(  );
    CrapsApp app = new CrapsApp( ); 
    RollButton myRollButton = new RollButton( 50, 175, app );
    myRollButton.setSize( 50, 25 );    
  }  
  
} //End of Class