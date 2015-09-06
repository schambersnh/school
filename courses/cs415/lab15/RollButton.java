/**
 * RollButton.java
 * Creates a Roll Button that Generates Two Dice
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;

public class RollButton extends TextBox
{
  public static int numDie1, numDie2;
  //------------------------Constructor-------------------------------------
  public RollButton(int x, int y, Dice dice1, Dice dice2)
  {
    setLocation(x, y);
    setText("Roll");
    setFillColor(Color.RED);
    setSize(40, 25);
    roll(dice1, dice2);
  }
  
  //Generates Two Random Dice
  public void roll(Dice dice1, Dice dice2)
  {
    dice1 = new Dice(100, 100);
    dice2 = new Dice(100, 150);

  }
  //'Rolls' the Dice when the button is Clicked
  public void mousePressed(java.awt.event.MouseEvent e)
  {
    roll();
  }
} //End of Class