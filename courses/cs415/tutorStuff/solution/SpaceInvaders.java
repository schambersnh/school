import wheelsunh.users.*;
import java.awt.event.*;
import java.util.*;
import java.awt.Point;
import java.awt.Color;

public class SpaceInvaders  implements Animator,  KeyListener
{  
   private AnimationTimer timer;
   private TextBox box;
   private int count = 10;
   private Squadron squadron;
   private Vector<Missile> missiles;
   private Missile t;
   private int lifeCount = 3, score = 0;;
   private Defender defender;
   private Bunker bunkers[];
   private int tCount;
   private static final int LEFT = 37, UP = 38, RIGHT = 39, DOWN = 40;
   private int mult = 100;
   private TextBox tb;
   private boolean sound = false;
   public static int fh = 650;
   //---------------------------
   
   //-------------------------------------------------------------- 
   public SpaceInvaders( ) 
   {  
      // animate will be called every 1000 milliseconds
      int interval = 1000;      
      // create the times to call animate on this animator
      timer = new AnimationTimer(10, this);
      
      t = new Missile( -100,-100, -5) ;
      
      tb = new TextBox(250, 0);;
      tb.setColor(new Color(0,0,0,0));
      refresh();
      squadron = new Squadron( 8,20 );
      //t = new Missile( 40,30, 3);
      defender = new Defender( 250,  fh - 50 );
      
      
      bunkers = new Bunker[4];
      for (int i = 0; i<4;i++)
         bunkers[i] = new Bunker( 50 + i *150, fh -100);
      
      missiles = new Vector<Missile>();
      // start the animation
      
      if(sound)
      {
      Utilities.play( "wavs/explosion.wav");
      Utilities.play( "wavs/fastInvader4.wav");
      Utilities.play( "wavs/shoot.wav");
      }
      
      
      timer.start();
   }
   
   
   //----------------------------------------------------------------------------------------- 
   public void animate()
   {
      // step squadron every mult ticks
      if( tCount > mult)
      {
        int result = squadron.step(); 
         
        if(result <0 )
        {
             timer.stop(); 
               //new Image( "win.jpg");    
               return;
           
           
        }
        
         if(result >=700 || squadron.contains( defender ))
      {
            timer.stop(); 
               //new Image( "go.jpg");    
               return;
      }
         
         if( sound)
             Utilities.play( "wavs/fastInvader4.wav");
         tCount = 0;
         if(mult >20)
            mult-=1;
      }
      tCount++;
      
      // move all alien missiles
      moveAlienMissiles( );
      
      // move defender missile      
      moveDefenderMissile(  ); 
      
      
   }
   
   private void refresh()
   {
      tb.setText("Score: "+ score +"  Lives: " + lifeCount);
   }
   
   
   //-----------------------------------------------------------------------------------------  
   private void moveAlienMissiles( )
   {
      
      Missile am = squadron.getMissile(); 
      if( am != null)
         missiles.add(am);
      
      // for every missile
      for( int i = 0; i < missiles.size(); i++)
      {
         Missile t = missiles.get(i);
         t.step();
         
         if(t.getTop().y > 800)
         {
            missiles.remove(t); 
            i--;
            continue;
         }
         
         for( int idx = 0; idx< bunkers.length; idx++)
         {
            if( bunkers[idx].contains( t ) )
            {
                if( sound)
                     Utilities.play( "wavs/explosion.wav");
               missiles.remove(t); 
               t.setLocation(-500,-500);
               i--;
               break;
            }
         }
         
         if( defender.contains( t ) )
         {
            lifeCount --;
            defender.die();
            if(lifeCount == 0 )
            {
               timer.stop(); 
               new Image( "go.jpg");
            } 
              if( sound)
                 Utilities.play( "wavs/explosion.wav");
            missiles.remove(t); 
            t.setLocation(-500,-500);
            i--;
            refresh();
         }
      }
   }
   
   //----------------------------------------------------------------------------------------- 
   private void moveDefenderMissile( )
   {
      if( ! t.isActive() )
         return;
      
      t.step();   
      if( squadron.contains( t )  )
      {
         t.setLocation(-100,-100);
          if( sound)
             Utilities.play( "wavs/invaderkilled.wav");
         score+=10;
         refresh();
      }
      else if(  t.getBottom().y < 0 )
      {
         t.setLocation(-100,-100);
      }
      
      for(int idx = 0;idx< bunkers.length;idx++)
      {
         if( bunkers[idx].contains( t ))
         {
            t.setLocation(-100,-100);
             if( sound)
            Utilities.play( "wavs/explosion.wav");
            break;
         }
      }
   }
   
   
   
   
   
   
   
//----------------------------------------------------------------------------------------- 
   /** Handle the key pressed event from the text field. */
   public void keyPressed(KeyEvent e) {
      //System.out.println("XXKEY PRESSED: " + e.getKeyCode( ) );
      
      int code = e.getKeyCode( ); 
      //  System.out.println("KEY PRESSED: " + e.getKeyCode( ) );
      switch( code)
      {
         case UP:
            if(! t.isActive() )
         {
            t.setLocation( defender.getXLocation() + 20, defender.getYLocation() -30 ) ;
             if( sound)
            Utilities.play( "wavs/shoot.wav");
         }
            break;
         case DOWN:
            defender.down();
            break;
         case LEFT:
            defender.west();
            break;
         case RIGHT:
            defender.east();
      }
      
   }
   
   
   /** Handle the key released event from the text field. */
   public void keyReleased(KeyEvent e) {
      // System.out.println("XXKEY RELEASED: " + e.getKeyCode( ) );
   }
   
   
   /** Handle the key typed event from the text field. */
   public void keyTyped(KeyEvent e) {
      // System.out.println( "XXKEY TYPED: " + e.getKeyCode( ) );
      
   }
   
   //--------------------------- main -----------------------------------
   /*
    * 
    */
   public static void main( String args[ ] )
   {
      Frame f = new Frame( 652, fh );
      SpaceInvaders app = new SpaceInvaders(  ); 
      
      f.addKeyListener( app );  
   }
}