
/*
 * PlayerMove.java
 * Authors: Stephen Chambers, Jackson Corson
 * CS619 Project 1
 * 
 * This class does the bulk of the work in determining how the player gets to the exit
 */

/*Class is part of jar file, but I've included it here for your convenience when coding. You can delete it after if you wish.
 * 
public class MazeIDs {
public static final byte WAY = 0x0; denotes an empty passageway.
public static final byte WALL = 0x1; denotes a maze wall that has not yet been passed.
public static final byte PLAYER = 0x2; denotes the heroine.
public static final byte KNOWN_WALL = 0x3; denotes a maze wall, that has been passed in the past.
public static final byte KEY = 0x5; denotes a key to open a door.
public static final byte V_DOOR = 0x6; denotes a door in a vertical passageway that can be opened with a key.
public static final byte H_DOOR = 0x7; denotes a door in a horizontal passageway that can be opened with a key.
public static final byte ZOMBIE = 0x8; denotes a zombie in a passageway that can be toasted with a flare.
public static final byte FLARE = 0x9; denotes a flare in a passageway.
public static final byte TAR_PIT = 0x0A; denotes an uncovered tar-pit that can be covered with a ladder.
public static final byte LADDER = 0x0B; denotes a ladder in a passageway.
public static final byte COV_PIT = 0x0C; denotes a tar-pit covered with a ladder, and hence passable.




I think we need to implement the right-hand rule in some way, our player will not move about the maze correctly
}
 */

import java.awt.Point;
import java.util.ArrayList;
public class PlayerMove 
{
 //------------------------Instance Variables----------------------------------------------
 private PlayerInventory _inventory;
 private GameConnection _connection;
 private GameUpdate update;
 private GameMove query;
 private Point prevLoc;
 private final byte PLAYER_BYTE_VALUE = 2;
 private String move;
 
 //------------------------PlayerMove(GameConnection, PlayerInventory)---------------------
 public PlayerMove(GameConnection conn, PlayerInventory inv)
 {
  _connection = conn;
  _inventory = inv;
  query = new GameMove((byte)0, new Point(0,0));
 }
 //-----------------------checkMove()------------------------------------------------------
 public Point checkMove()
 {
  
  GameUpdate u;
  u = _connection.sendMove( query );
  Point loc = u.location();
  String s = "";
  ArrayList<Point> obj = new ArrayList<Point>();
  //Gathers a string containing Byte values of every square in the "chunk" given to the player
  //CAREFUL: The chunk will never be a perfect square
  for( int i = 0; i < 3; i++ )
  {
   for( int j = 0; j < 3; j++ )
   {
    obj.add(new Point( loc.x+j, loc.y+i ));
    s += Byte.toString( u.value( loc.x+j, loc.y+i ) );
   }
  }
  //Case for beginning of the game
  if( prevLoc == null )
  {
   prevLoc = u.location();
  }
  
  //see how many objects there are in the string
  int countObj = 0;
  for( int a = 0; a < s.length(); a++ )
  {
   if( s.charAt(a) == '5' )
   {
    countObj++;
   }
   else if( s.charAt(a) == '9' )
   {
    countObj++;
   }
   else if( s.charAt(a) == 'b' )
   {
    countObj++;
   }
  }
  //if no objects
  //use inventory if you can
  //take the first path you see as long as it's not your previous one
  if( countObj == 0 )
  {
   for( int h = 0; h < s.length(); h++ )
   {
    if( s.charAt(h) == '8' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     if(_inventory.getFlares() > 0)
     {
       _inventory.subFlare();
       move = "USEzombie";
       return obj.get(h);
     }
    }
    else if( s.charAt(h) == 'A' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     if(_inventory.hasLadder())
     {
       _inventory.setLadder();
       move = "USEpit";
       return obj.get(h);
     }
    }
    else if( s.charAt(h) == '6' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     if(_inventory.getKeys() > 0)
     {
       _inventory.subKey();
       move = "USEvdoor";
       return obj.get(h);
     }
    }
    else if( s.charAt(h) == '7' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     if(_inventory.getKeys() > 0)
     {
       _inventory.subKey();
       move = "USEhdoor";
       return obj.get(h);
     }
    }
    else if( s.charAt(h) == '0' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     move = "USEway";
     return obj.get(h);
    }
    //make sure the player goes over a covered pit if no ways or objects are there!
    else if( s.charAt(h) == 'c' && obj.get(h).x != prevLoc.x && obj.get(h).y != prevLoc.y )
    {
     move = "USEcovpit";
     return obj.get(h);
    }
   }
   
   return prevLoc;
  }
  else
  {
   //Pick up the object
   for( int h = 0; h < s.length(); h++ )
   {
    if( s.charAt(h) == '5' )
    {
     _inventory.addKey();
     move = "PICKUPkey"
     return obj.get(h);
    }
    else if( s.charAt(h) == '9' )
    {
     _inventory.addFlare();
     move = "PICKUPflare";
     return obj.get(h);
    }
    else if( s.charAt(h) == 'b' && _inventory.hasLadder() == false )
    {
     _inventory.setLadder();
     move = "PICKUPladder";
     return obj.get(h);
    }
   }
  }
  
  /*Check move Cases (not in order)
   * 1. Move right if there are no objects.
   *   If there are, grab the object regardless of what it is
   * 2. If player has reached the end, return new Point(-1, -1)
   * 3. If player reaches dead end (must develop algorithm for determining whether a dead 
   * end is found, maybe a method?) returns the way it came. Keep track of precious location 
   * in order for the player to not loop
   * 4. If object
   *      Check Flares/Ladder/VKey/HKey
   *       If enough, conquer object
   *       If not, choose different path 
   *
   */
 }
 //------------------------sendMove()--------------------------------------------------------
 public int sendMove()
 {
  Point p;
  Point end = new Point(-1,-1);
  p = checkMove();
  if( p == end )
  {
   return 1;
  }
  if( update != null)
  {
   //doesn't that give the corner of the box, not the player?
   prevLoc = update.location();
  }
  //move switch case?
  update = _connection.sendMove( new GameMove( PLAYER_BYTE_VALUE, p ) );
  return 0;
 }

}