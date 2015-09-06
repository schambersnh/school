/**
 * Stephen Chambers
 * 11/16/10
 * This class models a Tetromino for tetris. 
 */ 

import wheelsunh.users.*;
import java.awt.Color;
import java.util.*;
import java.awt.Point;

public  class JTetromino 
{
  
  /******************************************************************/
  //  instance variables
  
  // the four tiles for this shape
  protected Vector< Rectangle > tiles; 
  
  // the current board row and column for this shape
  protected int row, col;
  
  // Four configurations for the tile rotations
  protected Vector< Configuration > config;
  
  // the index of the current configuration
  protected int currentConfigIndex;
  
  // the current configuration
  protected Configuration currentConfig ;
  
  
  /******************************************************************/
  /**
   * Constructor: 
   */ 
  public JTetromino( int r,  int c )
  {     
    row = r; 
    col = c;
     tiles = new Vector<Rectangle>();
    for(int i = 0; i < 4; i++)
    {
      Rectangle temp = new Rectangle();
      temp.setSize(Specs.TILE_SIZE, Specs.TILE_SIZE);
      temp.setColor(Specs.J_COLOR);
      temp.setFrameColor(Color.GRAY);
      tiles.add(temp);
    }
      
      config = new Vector<Configuration>();
      Configuration myConfig1 = new Configuration(0, 1, 1, 1, 2, 1, 2, 0);
      Configuration myConfig2 = new Configuration(0, 0, 0, 1, 0, 2, 1, 2);
      Configuration myConfig3 = new Configuration(0, 0, 1, 0, 1, 1, 1, 2);
      Configuration myConfig4 = new Configuration(0, 0, 1, 0, 2, 0, 0, 1);
      
      config.add(myConfig1);
      config.add(myConfig2);
      config.add(myConfig3);
      config.add(myConfig4);
      
      currentConfigIndex = 0;
      currentConfig = config.get(currentConfigIndex);
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  //  call drawTile for each tile
  public void draw(  )
  {
    for(int i = 0; i < 4; i++)
    {
      drawTile(i);
    }
    
  }
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  // Draw the i'th tile
  public void drawTile( int i )
  {
    Point temp = new Point();
    temp = config.get(currentConfigIndex).get(i);
    
    Point playingBoardPoint = new Point(temp.x + row, temp.y + col);
   
    
    Point graphicsPoint = new Point();
    graphicsPoint.x = Specs.BOARD_X + playingBoardPoint.y * Specs.TILE_SIZE;
    graphicsPoint.y = Specs.BOARD_X + playingBoardPoint.x * Specs.TILE_SIZE;
    tiles.get(i).setLocation(graphicsPoint.x, graphicsPoint.y);
    
    
  }
  
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /**
   *  set the board row and column
   */ 
  public void setRC(int r, int c )
  {
    row = r;
    col = c;
  }
  
  /******************************************************************/
  /**
   * Fall: make  the shape fall one row
   */ 
  public void fall()
  {
    row +=1;
    draw();
  }
  
  /******************************************************************/
  /**
   *  get the current configuration
   */ 
  public Configuration getConfiguration( )
  {
    return currentConfig;
  }
  
  //******************************************************************/
  /**
   *  get the current board row
   */ 
  public int getRow( )
  {
    return row;
  }
  
  // ******************************************************************/
  /**
   *   get the current board column 
   */ 
  public int getCol( )
  {
    return col;
  }
  
  
  /******************************************************************/
  /**
   *   get the maximum row index for this shape
   */ 
  public int getMaxRow( )
  {
    int max = -1;
    
    Point temp = new Point();
    temp = config.get(currentConfigIndex).get(currentConfigIndex);
    max = temp.y;
    
    
    return max;
  }
  
  
  /******************************************************************/
  /**
   *  Rotate the shape
   */ 
  public void rotate()
  {
    if(currentConfigIndex < 3)
    {
      currentConfigIndex++;
    }
    else
    {
      currentConfigIndex = 0;
    }
    
    currentConfig = config.get(currentConfigIndex);
    draw();
    
    
    
  }
  
  
  /******************************************************************/
  /**
   *   Get the tiles from this shape
   */ 
  public Vector<Rectangle> getTiles()
  {
    return tiles;
  }
  
  
  
  
  //---------------------- testing  main ------------------------------------
  public static void main( String[] args )
  {     
    new Frame();
    new TetrisLab1( );
    
  }
}// end of JTetromino
