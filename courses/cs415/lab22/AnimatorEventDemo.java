import wheelsunh.users.*;
import java.awt.event.*;

public class AnimatorEventDemo implements KeyListener, Animator
{  
  private AnimationTimer timer;
  private Rectangle spinner;
  private static final int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40;
  
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /**
   *   Create a Timer and start it
   * 
   */
  public AnimatorEventDemo( ) 
  {  
    
    spinner = new Rectangle(200,200);
    timer = new AnimationTimer(10, this);
    timer.start();
    
    
  }
  
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /**
   *  One animation step
   * 
   */
  public void animate()
  {
    spinner.setRotation(spinner.getRotation() + 3);
  }
  
  
  
  
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /** Handle the key pressed event. 
    *  Use this for action key input such as arrows --- use
    *  37  left
    *  38   up
    *  39   right
    *  40   down
    */
  public void keyPressed(KeyEvent e) 
  {
    
    
    if(e.getKeyCode() == 37)
    {
      System.out.println("LEFT");
      spinner.setLocation(spinner.getXLocation() - 10, spinner.getYLocation());
    }
    else if(e.getKeyCode() == 38)
    {
      System.out.println("UP");
      spinner.setLocation(spinner.getXLocation(), spinner.getYLocation() - 10);
      timer.stop();
    }
    else if(e.getKeyCode() == 39)
    {
      System.out.println("RIGHT");
      spinner.setLocation(spinner.getXLocation() + 10, spinner.getYLocation());
    }
    else if(e.getKeyCode() == 40)
    {
      System.out.println("DOWN");
      spinner.setLocation(spinner.getXLocation(), spinner.getYLocation() + 10);
      timer.start();
    }
    
  }
  
  
  
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /** Handle the key released event */
  public void keyReleased(KeyEvent e) {}
  
  //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  /** Handle the key typed event. */
  public void keyTyped( KeyEvent e ) {}
  
  
  //--------------------------- main -----------------------------------
  /*
   * 
   */
  public static void main( String args[ ] )
  {
    Frame frame = new Frame( );
    AnimatorEventDemo  demo = new AnimatorEventDemo(  ); 
    frame.addKeyListener(demo);
    
  }
}
