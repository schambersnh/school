/**
 * LabMineSweeper - an incomplete implementation of  MineSweeper Game.
 *
 * @author mlb
 */

import java.io.*;
import java.awt.Color;
import java.awt.event.*;
import wheelsunh.users.*;
import java.util.*;

public class LabMineSweeper extends ShapeGroup
{
  private final int   SIZE = 26; // tile size
  
  //-------------------------- instance variable s-----------------------
  private int[][]      theMines;   // 1 is in position of a mine
  private MineTile[][] mineField;  // Tile for each position
  private int[][]      theCounts;  // mine neighbors counts
  private int          rows, cols; // # rows and columns in this game board
  private int          percent;    // approximate % of tiles that re mines
  
  //-------------------------------------------------------------------
  /**
   * Construct the game with specify number rows, cols, a random seed
   *         and a target for the % of tiles that will be mined
   */
  public LabMineSweeper( int nRows, int nCols, int seed, int pct ) 
  {
    rows = nRows;
    cols = nCols;
    percent = pct;
    
    mineField = new MineTile[ nRows ][ nCols ];   
    theMines  = new int[ nRows ][ nCols ]; 
    theCounts = new int[ nRows ][ nCols ];
    
    layMineField( seed , percent );
    determineCounts( );
    
    for( int r = 0; r < nRows; r++)
    {
      for( int c = 0; c < nCols; c++)
      {
        mineField[ r ][ c ] = new MineTile( r, c,  SIZE , this );
        mineField[ r ][ c ].setCount( theCounts[ r ][ c ]);        
      }
    }
    System.out.println( this );
  }
  
  //-------------------------------------------------------------------
  /**
   * returns true if both indexes are valid for this board
   */
  private boolean isValid( int r, int c )
  {
    return r >= 0 && r < rows && c >= 0 && c < cols ;   
  }
  
  //-------------------------------------------------------------------
  /**
   * compute the mined tile neighbor counts for a position
   */
  private void count( int theR, int theC )
  {
    for( int r = -1; r <= 1; r++ ) 
    {
      for( int c = -1; c <= 1; c++ )
      {
        if( isValid( theR + r, theC + c ) )
          theCounts[ theR ][ theC ] += theMines[ theR + r ][ theC + c ];
      }
    }  
  }
  
  //-------------------------------------------------------------------
  /**
   * foreach position, compute the mined tile neighbor counts
   */
  private void determineCounts( )
  {
    for( int r = 0; r < rows; r++ )
    {
      for( int c = 0; c < cols; c++ )
      {
        if( theMines[ r ][ c ] == 1 )
          theCounts[ r ] [ c ] = 9;
        else
          count( r, c );
      }
    }
  }
  
  //-------------------------------------------------------------------
  /**
   * Using a random number generator, place mines in the mine field
   */
  private void layMineField ( int n , int pct)
  { 
    Random generator = new Random( n );
    
    for ( int r = 0 ; r < rows ; r++ )
    {
      for ( int c = 0; c < cols ; c++ )   
      {
        if ( generator.nextInt( 100 ) < pct )
          theMines[ r ][ c ] = 1;               
        else
          theMines[ r ][ c ] = 0;
      }
    }
  }
  
  //-------------------------------------------------------------------
  /**
   * The position r,c has been determined to have no mined neighbors.
   *    Uncover all its neighbors and then do the same for all of them
   *    that have no mined neighbors -- until all contiguous 0 tiles
   *    and their neighbors have been uncovered.
   */
  public void uncoverContiguous( int r , int c )
  { 
    
    Stack<MineTile> stack = new Stack<MineTile>();
    stack.add(mineField[r][c]);
    
    while(!stack.empty())
    {
      MineTile curTile = stack.pop();
      r = curTile.getRow();
      c = curTile.getCol();
      for(int i = r-1; i <= r+1; i++)
      {
        for(int j = c-1; j <= c+1; j++)
        {
          if(isValid(i, j))
          {
            if(mineField[i][j].isCovered() & mineField[i][j].getCount() == 0)
            {
              stack.add(mineField[i][j]);
            }
            mineField[i][j].uncover();
          }
          
        }   
        
      }
      
    }
  }

  
  
  


//-------------------------------------------------------------------
/**
 * convert the minefield to a String representation
 */
public String toString( )
{ 
  String s="\nThe Mines\n ";
  for ( int r = 0; r < rows; r++ )
  {
    for ( int c = 0; c < cols ; c++ )
      s = s + theMines[ r ][ c ] + " ";
    s = s + " \n " ;
  }
  s += "\nMined neighbor counts\n ";
  for ( int r = 0; r < rows; r++ )
  {
    for ( int c = 0; c < cols ; c++ )
      if( theCounts[ r ][ c ] == 9 )
      s = s + "X" + " ";
    else
      s = s + theCounts[ r ][ c ] + " ";
    s = s + " \n " ;
  }    
  return( s );
}


//-------------------------------------------------------------------
public static void main(String arg[])
{
  int seed = 1;
  int percent = 15;
  try
  {
    seed = Integer.parseInt( arg[ 0 ] );  
  }
  catch ( Exception e)
  {
    System.out.printf( "Using default seed and percent: %d %d\n", seed, percent );
  }
  try
  {
    percent = Integer.parseInt( arg[ 1 ] );  
  }
  catch ( Exception e)
  {
    System.out.println( "Using default percent: " + percent );
  }
  
  new Frame( );
  new LabMineSweeper( 16, 20, seed, percent );
}
}
