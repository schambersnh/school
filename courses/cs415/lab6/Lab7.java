import wheelsunh.users.*;
import java.awt.Color;

public class Lab7
{

public static void main(String [] args)
{
   Frame f = new Frame();
  //Create 4 Eyeballs
  Eyeball eyeball = new Eyeball();
  Eyeball eyeball2 = new Eyeball();
  Eyeball eyeball3 = new Eyeball();
  Eyeball eyeball4 = new Eyeball();
  
  //Set Location and Color of the Eyeball
  eyeball2.setLocation(200,200);
  eyeball2.setColor(Color.YELLOW);
  
  //Set Location and Color of the Eyeball, then make it mad
  eyeball3.setLocation(300,300);
  eyeball3.setColor(Color.BLUE);
  eyeball3.lookMad();
  
  //Set Location and Color of the Eyeball, then make it mad, then back to normal
  eyeball4.setLocation(400,400);
  eyeball4.setColor(Color.YELLOW);
  eyeball4.lookMad();
  eyeball4.lookNormal();
  
}
}