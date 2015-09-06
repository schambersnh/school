/**
 * TetrisStart.java
 * Creates a class modeling a game of Tetris. Pieces can be moved with the 
 * arrow keys and a new one is created when a piece hits the bottom
 * 
 * Stephen Chambers
 * 11/23/10
 *
 * 
 *
 * 

 */
import wheelsunh.users.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.*;

public class TetrisStart implements Animator, KeyListener
{ 
  
  //--------------------------Instance Variables--------------------------------
  private Tetromino currentTetromino;
  private int currentTetrominoSetter = 0;
  private Rectangle[][] myBoard;
  private AnimationTimer myTimer;
  
  
  //---------------------------------------------------------------------------
  /**
   * Constructor creates a two dimensional array of rectangles as a board
   * and initializes a timer
   */  
  public TetrisStart( )
  {
    myBoard = new Rectangle[ Specs.BOARD_ROWS ][ Specs.BOARD_COLS ];
    myTimer = new AnimationTimer( 1000, this );
    myTimer.start(); //start timer
    
    //creates the board of rectangles
    for( int r = 0; r < Specs.BOARD_ROWS; r++ )
      for( int c = 0; c < Specs.BOARD_COLS; c++ )
      makeBoardSquare( r, c ); 
    
    //assigns currentTetromino to a new JTetromino
    currentTetromino = new JTetromino( 0, 0 );
    
    //sets its location and draws it
    currentTetromino.setRC( 0, 3 );
    currentTetromino.draw();
  }  
  
  //---------------------------------------------------------------------------
  /**
   * Method that draws board, used in the constructor's nested for loop
   */  
  private void makeBoardSquare( int r, int c )
  {
    int x = Specs.BOARD_X + c * Specs.TILE_SIZE;
    int y = Specs.BOARD_Y + r * Specs.TILE_SIZE;
    
    //sets the index given by the parameters as a new Rectangle
    myBoard[r][c] = new Rectangle( );
    myBoard[r][c].setFillColor( Color.BLUE );
    myBoard[r][c].setFrameColor( Color.BLUE ); 
    myBoard[r][c].setLocation( x, y ); 
    myBoard[r][c].setSize( Specs.TILE_SIZE, Specs.TILE_SIZE ); 
  }
  
  //---------------------------------------------------------------------------
  /**
   * Drops a tetromino every second
   */  
  public void animate( )
  {
    int max = currentTetromino.getMaxRow();    
    max += currentTetromino.getRow();
    
    
    if( max < Specs.BOARD_ROWS - 1 ) //if shape can fall
      currentTetromino.fall();
    
    //gets the next Tetromino
    else
    { 
      setCurrentTet();
    } 
    
  }
  
  //---------------------------------------------------------------------------
  /**
   * Sets the current Tetromino
   */  
  public void setCurrentTet()
  {
    //increments index
    currentTetrominoSetter++;
    
    
    if( currentTetrominoSetter > 3 ) //out of Tetrominos!
      currentTetrominoSetter = 0; //reset index
    
    /*
     * Else if block creates a new Tetromino if the currentTetrominoSetter is
     * 0, 1, or 2.
     * */
    if( currentTetrominoSetter == 0 )
    {
      currentTetromino = new JTetromino( 0, 0 );
      

      currentTetromino.setRC( 0, 3 );
      currentTetromino.draw();
    }

    else if( currentTetrominoSetter == 1 )
    {
      currentTetromino = new ITetromino( 0, 0 );
      

      currentTetromino.setRC( 0, 3 );
      currentTetromino.draw();
    }
    

    else if( currentTetrominoSetter == 2 )
    {
      currentTetromino = new STetromino( 0, 0 );
      
     
      currentTetromino.setRC( 0, 3 );
      currentTetromino.draw();
    }
    

    else if( currentTetrominoSetter == 3 )
    {
      currentTetromino = new TTetromino( 0, 0 );
      

      currentTetromino.setRC( 0, 3 );
      currentTetromino.draw();
    }
  }
  
  //---------------------------------------------------------------------------
  /**
   * Drops the Tetromino 
   */  
  public void fall()
  {
    int max = currentTetromino.getMaxRow();
    max += currentTetromino.getRow();
    
    
    if( max < Specs.BOARD_ROWS - 1 ) //if shape can fall
      currentTetromino.fall();
    
    //else get the new Tetromino
    else
    { 
      setCurrentTet();
    } 
  }
  
  //---------------------------------------------------------------------------
  /**
   *Moves the currentTetromino left 
   */  
  public void moveLeft()
  {
    int min = currentTetromino.getMinCol();
    min += currentTetromino.getCol();
    
   
    if( min > 0 ) //if shape can move left
      currentTetromino.moveLeft();    
  }
  
  //---------------------------------------------------------------------------
  /**
   * Moves the currentTetromino right
   */  
  public void moveRight()
  {
    int max = currentTetromino.getMaxCol();
    max += currentTetromino.getCol();
    
  
    if( max < Specs.BOARD_COLS - 1  ) //if shape can move right
      currentTetromino.moveRight();
  }
  
  //---------------------------------------------------------------------------
  /**
   * Rotates the currentTetromino 
   */  
  public void rotate( )
  {
    currentTetromino.rotate();
  }
  
  //---------------------------------------------------------------------------
  /**
   * Handles the key pressed events.
   */  
  public void keyPressed(KeyEvent e) 
  { 
    if( e.getKeyCode() == 37 )//left
    {
      moveLeft();
    }
    else if ( e.getKeyCode() == 38 )//up
    {
      rotate();      
    }
    else if ( e.getKeyCode() == 39 )//right
    {
      moveRight();
    }
    else if ( e.getKeyCode() == 40 )//down
    {
      fall();
    }
    else
    {
      System.out.println( "Other." );
    }
  }
  
  //---------------------------------------------------------------------------
  /**
   * Implemented from KeyListener
   */  
  public void keyReleased(KeyEvent e) 
  {
    
  }
  
  //---------------------------------------------------------------------------
  /**
   * Implemented from KeyListener
   */  
  public void keyTyped( KeyEvent e ) 
  {
    
  }
  
  //------------------------main method-----------------------------------------
  public static void main(String[] args) 
  {
    Frame f = new Frame( );
    TetrisStart tetris = new TetrisStart( );
    f.addKeyListener( tetris );
  }
  
} //End Of Class