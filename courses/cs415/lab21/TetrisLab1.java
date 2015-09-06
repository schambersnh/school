/**
 * This class tries out the JTetromino object. (880 lines total)
 */
import wheelsunh.users.*;
import java.awt.event.*;
import java.awt.Color;
import java.util.*;

public class TetrisLab1 implements ButtonListener
{ 
   
   /******************************************************************/
   private JTetromino one;
   private ButtonPanel bp;
   private Rectangle[][] board;
   
   
   /******************************************************************/
   /**
    * constructor
    */  
   public  TetrisLab1( )
   {
      board = new Rectangle[ Specs.BOARD_ROWS ][ Specs.BOARD_COLS ];
      
      for( int r = 0; r < Specs.BOARD_ROWS; r++ )
         for( int c = 0; c < Specs.BOARD_COLS; c++ )
            makeBoardSquare( r, c );  
      
      one = new JTetromino( 0, 0 );
      one.setRC( 0, 4 );
      
      String labels[ ] = { "Fall" , "Rotate" }; 
      bp = new ButtonPanel( 450, 360, labels, this );
      one.draw( );    // and draw
   }
   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   private void makeBoardSquare( int r, int c )
   {
      int x = Specs.BOARD_X + c * Specs.TILE_SIZE;
      int y = Specs.BOARD_Y + r * Specs.TILE_SIZE;
      
      board[r][c] = new Rectangle( );
      board[r][c].setFillColor( Color.white );
      board[r][c].setFrameColor( Color.black ); 
      board[r][c].setLocation( x, y ); 
      board[r][c].setSize( Specs.TILE_SIZE, Specs.TILE_SIZE ); 
   }
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public void fall( )
   {
      int max = one.getMaxRow();
      max += one.getRow();
      
      if( max < Specs.BOARD_ROWS -1  ) // still room to fall
         one.fall();  // fall 
      else //part of shape hit bottom
      { 
         one.setRC( 0, 4 );   // move back to row 0 col 4
         one.draw( );    // and draw
      } 
      
   }
   
   
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public void rotate( )
   {
      one.rotate();
   }
   
   
   //---------------------------------------------------------------
   public void buttonPressed( String buttonLabel, int buttonId )
   {
      if( buttonId == 0 )
         fall( );
      if( buttonId == 1 )
         rotate( );
   }
   
   public void buttonReleased( String buttonLabel, int buttonId ){}
   public void buttonClicked( String buttonLabel, int buttonId ){}
   
   
   /******************************************************************/
   /**
    * main
    */   
   public static void main(String[] args) 
   {
      new Frame( );
      new TetrisLab1( );
   }
} // end of class Tetris