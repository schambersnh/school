/** 
 * Program5.java
 *Tests the ATV Class and its SubClasses
 *
 * Stephen Chambers
 * 10/05/10
 */
import wheelsunh.users.*;
import java.awt.Color;

public class Program5 extends wheelsunh.users.Frame
{
  public static void main(String[] args)
    {
      Frame f = new Frame();
      ATV atv1 = new ATV(100, 200);
      HalloweenATV hATV = new HalloweenATV(250,200);
      ValentinesDayATV vATV = new ValentinesDayATV(400,200);
      Button button1 = new Button(160,300, atv1);
      Button button2 = new Button(310,300, hATV);
      Button button3 = new Button(460,300, vATV);
      
      button1.setText("Main ATV");
      button2.setText("Halloween ATV");
      button3.setText("Valentines Day ATV");
      
      button1.setSize(75, 60);
      button2.setSize(75, 60);
      button3.setSize(75, 60);
}
}//End of Class
