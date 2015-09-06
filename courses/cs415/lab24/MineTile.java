/**
 * MineTile - represents a single tile in the MineSweeper game
 *
 * @author mlb
 */

import java.awt.Color;
import java.awt.event.*;
import wheelsunh.users.*;

public class MineTile extends Rectangle
{
   //------------------------ instance variables --------------------------
   private TextBox t;             // Show mined count
   private int count;             // count of mined neighbors of this tile
   private int row, col;          // row, col position of this tile
   private int size;              // size of tile
   private LabMineSweeper mines;  // game class
   private boolean uncovered;     // true if tile revealed
   
//------------------------- Constructors -------------------------------
   public MineTile( int r, int c, int size , LabMineSweeper m)
   {
      super(  );
      setLocation( c * size, r * size );
      setFillColor( Color.BLACK);
      setFrameColor( Color.GRAY );
      setSize( size, size );
      this.row = r;
      this.col = c;
      this.size = size;
      this.mines = m;
      this.count = 0;    
   }
   
   //------------------------------------------------------------------- 
   /** 
    * mutator to set the count of mined neighbors
    */
   public void setCount( int c )
   {
      count = c;
   }
   
   //-------------------------------------------------------------------
   /** 
    * mouse pressed triggers the uncovering process
    */
   public void mousePressed( MouseEvent e )
   {   
      uncover();
      if( count == 0 )
         mines.uncoverContiguous( row, col );    
   }
   
   //-------------------------------------------------------------------
   /**
    * uncover this tile to reveal the count
    */
   public void uncover()
   {
      hide(); 
      t = new TextBox( );
      t.setLocation( col * size, row * size );
      t.setFillColor( Color.WHITE);
      t.setFrameColor( Color.GRAY );
      t .setSize( size, size );
      t.setText("" + count ); 
      uncovered = true;     
   }
   
   //----------------------- accessor method -----------------------------
   public int getRow()
   {
      return row;  
   }
   
   //-------------------------------------------------------------------
   public int getCol()
   {
      return col;  
   }
     
   //-------------------------------------------------------------------
   public int getCount()
   {
      return count;  
   }
   
   //-------------------------------------------------------------------
   /**
    * returns true if tile is still covered
    */
   public boolean isCovered()
   {
      return !uncovered;  
   }   
} 
