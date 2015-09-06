
import java.awt.Point;

/*
 * PlayerMove.java
 * Authors: Stephen Chambers, Jackson Corson
 * CS619 Project 1
 * 
 * This is the main class
 */

public class MyGamePlayer 
{
   //-------------------------------------Instance Variables---------------------------------------------
 private GameConnection connection;
 private PlayerInventory inventory;
 private PlayerMove movement;
 private Point location;
 
 //-------------------------------------MyGamePlayer(string, int, string, string)---------------------
 public MyGamePlayer(String myHost, int myPort, String myGame, String myPass)
 {
	 try
	 {
        connection = new GameConnection(myHost, myPort);
        connection.setUpPlayerClient(myGame, myPass);
	 }
	 catch(Exception e)
	 {
	   
	 }
 }
 //-------------------------------------sendMove()-----------------------------------------------------
 public void sendMove()
 {
	 try
	 {
        movement = new PlayerMove(connection, inventory);
	 }
	 catch(Exception e)
	 {
		 
	 }
  int next = 0;
  while(next==0)
  {
   next = movement.sendMove();
  }
 }
 //-------------------------------------main()---------------------------------------------------------
 public static void main(String[] args)
 {
  MyGamePlayer player = new MyGamePlayer(args[0], Integer.parseInt(args[1]), args[2], args[3]);
 }
 
}
