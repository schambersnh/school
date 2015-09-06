/**
 * RollButton.java
 * Creates a Roll Button 
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;

public class RollButton extends TextBox
{
  public static int numDie1, numDie2;
  protected CrapsApp app;
  //------------------------Constructor-------------------------------------
  public RollButton(int x, int y, CrapsApp app)
  {
    setLocation(x, y);
    setText("Roll");
    setFillColor(Color.RED);
    setSize(40, 25);
    this.app = app;
      
  }
  


  //'Rolls' the Dice when the button is Clicked
  public void mousePressed(java.awt.event.MouseEvent e)
  {
    app.roll();
  }
} //End of Class