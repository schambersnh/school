
/*
 * PlayerMove.java
 * Authors: Stephen Chambers, Jackson Corson
 * CS619 Project 1
 * 
 * This class keeps track of the objects that the player has
 */

//Note: Doors are specified as vertical or horizontal but every key works on either type of door.
public class PlayerInventory 
{ 
    //-----------------------------------Instance Variables------------------------------------------------
 private int numKeys;
 private int numFlares;
 private boolean hasLadder;
 private int curFlares = 2;
 
 //-----------------------------------addKey()---------------------------------------------------------
 public void addKey()
 {
   numKeys++;   
 }
 
 //-----------------------------------addFlare()---------------------------------------------------------
 public void addFlare()
 {
   numFlares++;   
 }
 
 //-----------------------------------setLadder()---------------------------------------------------------
 public void setLadder()
 {
   if(hasLadder)
   {
    hasLadder = false;
   }
   else
   {
    hasLadder = true;
   }
 }
 
 //-----------------------------------subKey()---------------------------------------------------------
 public void subKey()
 {
   numKeys--;   
 }
 
 //-----------------------------------subFlare()---------------------------------------------------------
 public void subFlare()
 {
   //Jackson: Not tested. Seemed feasible to me.
   if(curFlares == 2)
   {
  curFlares--;  
   }
   else
   {
    numFlares--;
    curFlares = 2;
   }
 }
 //-----------------------------------getKeys()---------------------------------------------------------
 public int getKeys()
 {
   return numKeys;   
 }
 //-----------------------------------getFlares()---------------------------------------------------------
 public int getFlares()
 {
   /*Jackson: Too bad we can't use a flare struct here. If you think of a way to encapsulate the current uses
   of flares left, feel free to change it*/
   return numFlares;   
 }
 //-----------------------------------hasLadder()---------------------------------------------------------
 public boolean hasLadder()
 {
   return hasLadder;
 }
}
