/**
 * Tile for BoggleBoard -- skeleton
 * 
 * Knows its letter and its location on the board, but you
 *    may want to revise the the "letter" handling to 
 *    account for the fact that "q" is really "qu".
 * 
 * Needs to know about its visited status
 * and who its neighboring tiles are.
 * 
 * @author jb
 * Summer 2010
 * 
 * 02/19/11 rdb: Formatting and style edits
 */

import java.util.*;

public class Tile
{
   //---------------------- instance variables -------------------------
   private String          letter;
   private int             row, col;
   private boolean         _visited = false;
   
   //------------------ constructor --------------------------------------
   public Tile( int r, int c, String l )
   {
      row = r;
      col = c;
      letter = l;
   }
   //------------------ getCol() --------------------------------------
   /**
    * returns the col location
    */
   public int getCol()
   {
      return col;
   }
   //-------------------getVistied()-----------------------------------
   public boolean getVisited()
   {
     return _visited;
   }
     //-------------------setVistied()-----------------------------------
   public void setVisited(boolean b)
   {
     _visited = b;
   }
   //------------------ getRow() --------------------------------------
   /**
    * returns the row location
    */ 
   public int getRow()
   {
      return row;
   }
   //------------------ getLetter( ) ----------------------------
   /**
    * Returns the letter this tile contains
    */
   public String getLetter()
   {
      return letter;
   }
   //--------------------------- toString() -------------------------
   public String toString()
   {
      return letter;
   }
}