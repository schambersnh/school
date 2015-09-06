/**
 * OTetromino.java
 * Creates a O Tetromino used in the game of Tetris
 * 
 * Stephen Chambers
 * 11/23/10
 */ 

import wheelsunh.users.*;
import java.awt.Color;
import java.util.*;
import java.awt.Point;

public class OTetromino implements Tetromino
{
  //--------------------------instance variables--------------------------------
  protected Vector< Rectangle > tiles; 
  protected int row, col;
  protected Vector< Configuration > config;
  protected int currentConfigIndex;
  protected Configuration currentConfig ;  
  
  //----------------------------------------------------------------------------
  /**
   * Constructor for the STeromino class. 
   */
  public OTetromino( int r,  int c )
  {    
    //parameters are stored as instance variables
    row = r;
    col = c;
    
    tiles = new Vector< Rectangle >();
    
    //adds four rectangles to the Vector tiles
    for( int i = 0; i < 4; i++ )
    {
      Rectangle re = new Rectangle( );
      re.setSize( Specs.TILE_SIZE, Specs.TILE_SIZE );
      re.setFillColor( Specs.O_COLOR );
      re.setFrameColor( Color.GRAY );
      tiles.add( re );
    }
    
    config = new Vector < Configuration >();
    
    //creates configurations
    Configuration oConfigUp = new Configuration( 0, 0, 0, 1, 1, 0, 1, 1 );
    Configuration oConfigLeft = new Configuration( 0, 0, 0, 1, 1, 0, 1, 1 );
    Configuration oConfigRight = new Configuration( 0, 0, 0, 1, 1, 0, 1, 1 );
    Configuration oConfigDown = new Configuration( 0, 0, 0, 1, 1, 0, 1, 1 );
    
    //adds configurations
    config.add( oConfigUp );
    config.add( oConfigLeft );
    config.add( oConfigRight );
    config.add( oConfigDown );
    
    
    currentConfigIndex = 0;
    
    currentConfig = config.get( currentConfigIndex );    
  }
  
  //----------------------------------------------------------------------------
  /**
   * Draws the tile
   */
  public void draw(  )
  {
    for( int i = 0; i < 4; i++ )
    {
      drawTile( i );
    }    
  }
  
  //----------------------------------------------------------------------------
  /**
   * Draws the tile based off of the current configuration.
   */
  public void drawTile( int i )
  {
    //gets the Point
    Point p = new Point( config.get( currentConfigIndex ).get( i ) );
    
    //creates two new local Point variables 
    Point playingBoardPoint = new Point();
    Point graphicPoint = new Point();
    
    //initializes playingBoardPoint to the i'th configuration's x and y plus the
    //number of rows and columns.
    playingBoardPoint.x = p.x + row;
    playingBoardPoint.y = p.y + col;
    
    //initializes a graphic point based off of the playingBoardPoint and the values
    //stored in the Specs.java class
    graphicPoint.x = Specs.BOARD_X + playingBoardPoint.y * Specs.TILE_SIZE;
    graphicPoint.y = Specs.BOARD_Y + playingBoardPoint.x * Specs.TILE_SIZE;
    
    //draws the tile based off of the graphicPoint
    tiles.get( i ).setLocation( graphicPoint );  
  }  
  
  //----------------------------------------------------------------------------
  /**
   * Sets the rows and columns to the given integer values.
   */ 
  public void setRC(int r, int c )
  {
    row = r;
    col = c;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Draw tile one row down
   */ 
  public void fall()
  {
    row++;
    draw();    
  }
  
  //----------------------------------------------------------------------------
  /**
   * Draw tile one column left
   */
  public void moveLeft()
  {
    col--;
    draw();
  }
  
  //----------------------------------------------------------------------------
  /**
   * Draw column one row right
   */
  public void moveRight()
  {
    col++;
    draw();
  }
  
  //----------------------------------------------------------------------------
  /**
   * Returns the current configuration
   */
  public Configuration getConfiguration( )
  {
    return currentConfig;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Returns the current row.
   */ 
  public int getRow( )
  {
    return row;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Returns the current column.
   */
  public int getCol( )
  {
    return col;
  }
  
  
  //----------------------------------------------------------------------------
  /**
   * Calculates the maximum row 
   */
  public int getMaxRow( )
  {
    int max = -1;
    
    
    for( int i = 0; i < 4; i++)
    {
      Point p = config.get( currentConfigIndex ).get( i );
      if( max < p.x )
        max = p.x;
    } 
    return max;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Cauculates the minimum column 
   */ 
  public int getMinCol( )
  {
    int min = Specs.BOARD_COLS;
    
    
    for( int i = 0; i < 4; i++)
    {
      Point p = config.get( currentConfigIndex ).get( i );
      if( p.y < min )
        min = p.y;
    }    
    return min;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Calculates the maximum column 
   */
  public int getMaxCol( )
  {
    int max = -1;
    
    
    for( int i = 0; i < 4; i++)
    {
      Point p = config.get( currentConfigIndex ).get( i );
      if( max < p.y )
        max = p.y;
    }    
    return max;
  }
  
  
  //----------------------------------------------------------------------------
  /**
   * Rotates the shape 
   */
  public void rotate()
  {
    
    if( currentConfigIndex < 3 ) //if less than the Vector's size
      //increase the configurationIndex.
      currentConfigIndex++;
    else
      //else reset to first configuration.
      currentConfigIndex = 0;
    //gets the new configuration
    currentConfig = config.get( currentConfigIndex );
    //redraws the shape with the new configuration.
    draw();
  }
  
  //----------------------------------------------------------------------------
  /**
   * Returns the vector of tiles.
   */
  public Vector<Rectangle> getTiles()
  {
    return tiles;
  }
    //---------------------------------------------------------------------------
  /**
   * Gets Color of Tetromino
   */  
  public Color getColor() 
  {
    return Specs.O_COLOR;
  }
    //---------------------------------------------------------------------------
  /**
   * Hides tetromino
   */  
  public void hide() 
  {
    for(int i = 0; i < tiles.size(); i++)
    {
      tiles.get(i).hide();
    }
  }
   //---------------------------------------------------------------------------
  /*
   * Get the next Configuration
   * */
  public Configuration getNextConfiguration()
  {
    Configuration next = config.get(0);
    if(currentConfigIndex == 0)
    {
      next = config.get(1);
    }
    else if (currentConfigIndex == 1)
    {
      next = config.get(2);
    }
    else if(currentConfigIndex == 2)
    {
      next = config.get(3);
    }
    else if(currentConfigIndex == 3)
    {
      next = config.get(0);
    }
    return next;
  }
}// end of Class
