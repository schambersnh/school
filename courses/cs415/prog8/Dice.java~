/**
 * Dice.java
 * Creates a Dice with a Random Value 
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;
import java.util.Random;

public class Dice 
{
  //---------------------Instance Variables-----------------------------------
  private Rectangle outerDie;
  private Dots[] diceDot;
  private int numDie;

  //-------------------------Constructor----------------------------------
  
  public Dice(int x, int y)
  {
    //Random Generator with seed being Time Clock
    Random rand = new Random();
    
    //Creates Outer Frame of Die
    outerDie = new Rectangle(x, y);
    outerDie.setFrameColor(Color.BLACK);
    outerDie.setFillColor(Color.WHITE);
    
    //Array Initialization
    diceDot = new Dots[7];
    
    //Integer value for chosing a Random Die
    int diceNum = 1 + rand.nextInt(7-1);
    numDie = diceNum;
    
    
    //Switch for determining a Random Die
    switch(diceNum)
    {
      case 1: //Die with a Value of 1
      {
        diceDot[1] = new Dots(x+ 17, y+ 17);
        break;
      }
      case 2: //Die with a Value of 2
      {
        diceDot[1] = new Dots(x+5, y+5);
        diceDot[2] = new Dots(x+30, y+30);
        break;
      }
      case 3: //Die with a Value of 3
      {
        diceDot[1] = new Dots(x+ 17, y+ 17);
        diceDot[2] = new Dots(x+5, y+5);
        diceDot[3] = new Dots(x+30, y+30);
        break;
      }
      case 4: //Die with a Value of 4
      {
        diceDot[1] = new Dots(x+5, y+5);
        diceDot[2] = new Dots(x+30, y+30);
        diceDot[3] = new Dots(x+ 30, y + 5);
        diceDot[4] = new Dots(x + 5, y + 30);
        break;
      }
      case 5: //Die with a Value of 5
      {
        diceDot[1] = new Dots(x+ 17, y+ 17);
        diceDot[2] = new Dots(x+5, y+5);
        diceDot[3] = new Dots(x+30, y+30);
        diceDot[4] = new Dots(x+ 30, y + 5);
        diceDot[5] = new Dots(x + 5, y + 30);
        break;
      }
      case 6: //Die with a Value of 6
      {
        diceDot[1] = new Dots(x+5, y+5);
        diceDot[2] = new Dots(x+30, y+30);
        diceDot[3] = new Dots(x+ 30, y + 5);
        diceDot[4] = new Dots(x + 5, y + 30); 
        diceDot[5] = new Dots(x + 5, y + 16);
        diceDot[6] = new Dots(x + 30, y + 16);
      }
    }  
    
  }  
  public int getNumDie()
  {
    return numDie;
  }
  public void setColor(Color myColor)
  {
    outerDie.setFillColor(myColor);
  }
 
 
} //End of Class