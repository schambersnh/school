import java.awt.Color;
import wheelsunh.users.*;
public class CheckCode
{
//-----------------------Instance Variables-------------------------------------
  protected Rectangle outerCheck;
  protected Peg myCheckPegs[];
  //---------------------------------------------------------------------------
  /*
   * Constructor for the CheckCode class, creates an outer Rectangle holding the
   * four inner pegs that will change their color based on the users guess.
   * */
  //---------------------------------------------------------------------------
  public CheckCode(int x, int y)
  {
    outerCheck = new Rectangle(x, y);
    outerCheck.setSize(25, 25);
    outerCheck.setFillColor(Color.WHITE);
    outerCheck.setFrameColor(Color.BLACK);
    
    myCheckPegs = new Peg[4];
    for(int i = 0; i < 4; i++)
    {
      myCheckPegs[i] = new Peg(0, 0, Color.WHITE, false, null);
      myCheckPegs[i].setSize(8, 8);
      myCheckPegs[i].setFrameColor(Color.BLACK);
    }
    myCheckPegs[0].setLocation(outerCheck.getXLocation() + 3, 
                               outerCheck.getYLocation() + 3);
    
    myCheckPegs[1].setLocation(outerCheck.getXLocation() + 15, 
                               outerCheck.getYLocation() + 3);
    
    myCheckPegs[2].setLocation(outerCheck.getXLocation() + 3, 
                               outerCheck.getYLocation() + 15);
    
    myCheckPegs[3].setLocation(outerCheck.getXLocation() + 15, 
                               outerCheck.getYLocation() + 15);
  }
  //---------------------------------------------------------------------------
  /*
   * Get the array of pegs inside of the CheckCode rectangle
   * */
  //---------------------------------------------------------------------------
  public Peg[] getCheckCode()
  {
    return myCheckPegs;
  }
  
}