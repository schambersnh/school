/**
 * CrapsApp.java
 * Combines all Classes into a Craps Game
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;

public class CrapsApp
{
  public static void main(String [] args)
  {
    Frame f = new Frame();
    Dice myDice1 = new Dice(100, 100);
    Dice myDice2 = new Dice(150, 100);
    RollButton myRollButton = new RollButton(200, 200, myDice1, myDice2);

    //Slider mySlider = new Slider(300, 300);
  }
  
}
