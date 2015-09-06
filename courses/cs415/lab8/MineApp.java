/** 
 * MineApp.java:
 *   Displays a 2D array of Tiles
 *  most are SafeTiles, a few are MinedTiles
 */
import wheelsunh.users.Frame;
import java.awt.Color;
import java.util.*;

public class MineApp extends Frame 
{ 
    private Tile[][] board;  //  2D array of tiles for the board
        
    //------------------ constructors ---------------------------------------------
    public MineApp() 
    {     
       int offset;       // distance from tile edge to next tile edge
       int boardSize;    // for now, we'll always have square boards
       Random generator; // generates random numbers for tile placement
       boardSize = 8;
       offset    = 25;
       board = new Tile[ boardSize ][ boardSize ];
       generator = new Random(  19580427 );
       
       for ( int i = 0 ; i< boardSize ; i++)
       {
          for ( int j = 0; j < boardSize ; j++)    
          {
             if ( generator.nextInt( boardSize ) == 0 )
                board[ i ][ j ] = new MinedTile( offset * i, offset * j ) ;   
             else
                board[ i ][ j ] = new SafeTile( offset * i, offset * j ) ; 
          }
       }
    }
    
    //---------------------------- main -----------------------------------------
    public static void main( String[] args ) 
    {
        MineApp app = new MineApp();
    }
}