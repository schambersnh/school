/** 
 * ATV.java
 * Builds an ATV extending ShapeGroup
 *
 * Stephen Chambers
 * 10/05/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class ATV extends ShapeGroup


{
    
    //---------------- instance variables ------------------------------
    

    protected Rectangle body;
    protected Ellipse wheel, wheel2;
    protected Line line1;
    protected Rectangle flag;
    protected Ellipse inFlag;
    protected int x1, y1, x2, y2;
    protected int padX = 100, padY = 80;
    Color originalBodyColor, originalWheelColor, originalWheel2Color;
    Color originalLineColor;
   
      
    

    
    
    // -----------------------------------------------------------------
    /** Constructor accepting no parameters
      */
    public ATV()
    {
   
        // Creates body of the ATV1
        body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
        body.setFillColor(Color.RED);
        body.setSize(padX, padY-30);
        add(body);
        
        //Creates the two wheels of the ATV1
        wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
        wheel.setColor(Color.GREEN);
        wheel.setFillColor(java.awt.Color.GREEN);
        wheel.setSize(padX - 70, padY- 50);
        add(wheel);
        
        wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
        wheel2.setColor(Color.GREEN);
        wheel2.setFillColor(java.awt.Color.GREEN);
        wheel2.setSize(padX - 70, padY - 50);
        add(wheel2);
        
        //Creates the Flagpole of ATV1
        line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
        line1.setThickness(5);
        line1.setColor(java.awt.Color.BLACK);
        add(line1);
        
        //Creates the Outisde of the flag of ATV1
        flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
        flag.setFillColor(java.awt.Color.WHITE);
        flag.setFrameColor(java.awt.Color.BLACK);
        flag.setSize(padX - 75, padY - 65);
        add(flag);
        
        //Creates the circle inside of the flag of ATV1
        inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
        inFlag.setSize(padX - 90, padY - 70);
        add(inFlag);
        
        setLocation(0,0);
     
    }
    //--------------------------------------------------------------------
    //Constructor accepting only a color
    public ATV(Color myColor)
    {
     
        // Creates body of the ATV1
        body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
        body.setFillColor(myColor);
        body.setSize(padX, padY-30);
        add(body);
        
        //Creates the two wheels of the ATV1
        wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
        wheel.setColor(Color.GREEN);
        wheel.setFillColor(java.awt.Color.GREEN);
        wheel.setSize(padX - 70, padY- 50);
        add(wheel);
        
        wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
        wheel2.setColor(Color.GREEN);
        wheel2.setFillColor(java.awt.Color.GREEN);
        wheel2.setSize(padX - 70, padY - 50);
        add(wheel2);
        
        //Creates the Flagpole of ATV1
        line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
        line1.setThickness(5);
        line1.setColor(java.awt.Color.BLACK);
        add(line1);
        
        //Creates the Outisde of the flag of ATV1
        flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
        flag.setFillColor(java.awt.Color.WHITE);
        flag.setFrameColor(java.awt.Color.BLACK);
        flag.setSize(padX - 75, padY - 65);
        add(flag);
        
        //Creates the circle inside of the flag of ATV1
        inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
        inFlag.setSize(padX - 90, padY - 70);
        add(inFlag);
        
        setLocation(0,0);
     
    }
    //-----------------------------------------------------------------
    //Constructor accepting the location and color of the ATV
    public ATV(int x1, int y1 )
    {
       // Creates body of the ATV1
        body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
        body.setFillColor(Color.RED);
        body.setSize(padX, padY-30);
        add(body);
        
        //Creates the two wheels of the ATV1
        wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
        wheel.setColor(Color.GREEN);
        wheel.setFillColor(java.awt.Color.GREEN);
        wheel.setSize(padX - 70, padY- 50);
        add(wheel);
        
        wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
        wheel2.setColor(Color.GREEN);
        wheel2.setFillColor(java.awt.Color.GREEN);
        wheel2.setSize(padX - 70, padY - 50);
        add(wheel2);
        
        //Creates the Flagpole of ATV1
        line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
        line1.setThickness(5);
        line1.setColor(java.awt.Color.BLACK);
        add(line1);
        
        //Creates the Outisde of the flag of ATV1
        flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
        flag.setFillColor(java.awt.Color.WHITE);
        flag.setFrameColor(java.awt.Color.BLACK);
        flag.setSize(padX - 75, padY - 65);
        add(flag);
        
        //Creates the circle inside of the flag of ATV1
        inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
        inFlag.setSize(padX - 90, padY - 70);
        add(inFlag);
   } 
    //Set the Color of the ATV
    public void setColor(Color myColor)
    {
      body.setColor(myColor);
    }
    //------------------------------------------------------------------
    //Get the Color of the ATV body
    public Color getColor()
    {
      return body.getColor();
    }
    public void moveForward(int move)
    {
      setLocation(getXLocation(), getYLocation() - move);
    }
    //Change Colors of the ATV
    public void mousePressed(MouseEvent e)
    {
       originalBodyColor = body.getColor();
       originalWheelColor = wheel.getColor();
       originalWheel2Color = wheel2.getColor();
       originalLineColor = line1.getColor();
       
       body.setColor(Color.GREEN);
       wheel.setColor(Color.RED);
       wheel2.setColor(Color.RED);
       line1.setColor(Color.YELLOW);
    }
    
    public void mouseReleased(MouseEvent e)
    {
     body.setColor(originalBodyColor);
     wheel.setColor(originalWheelColor);
     wheel2.setColor(originalWheel2Color);
     line1.setColor(originalLineColor);
    }
    
    public void activate()
    {
       originalBodyColor = body.getColor();
       originalWheelColor = wheel.getColor();
       originalWheel2Color = wheel2.getColor();
       originalLineColor = line1.getColor();
       
       body.setColor(Color.GREEN);
       wheel.setColor(Color.RED);
       wheel2.setColor(Color.RED);
       line1.setColor(Color.YELLOW);
    }
   
    
    public void deactivate()
    {
     body.setColor(originalBodyColor);
     wheel.setColor(originalWheelColor);
     wheel2.setColor(originalWheel2Color);
     line1.setColor(originalLineColor);
    }
    
    
    
    
}//End of Class 