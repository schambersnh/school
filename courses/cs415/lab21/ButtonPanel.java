/**
 * ButtonPanel 
 * 
 * @author mlb
 * 3/10
 */
import wheelsunh.users.*;
import java.util.*;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.io.File;

public class ButtonPanel implements ButtonListener
{
   //------------------- class variables -------------------------------
   private static int    frameWidth  = 700;
   private static int    frameHeight = 500;
   private  ButtonListener client;
   private String[] labels;
   private int numberOfButtons;
   private int longestLabel = -1;
   private Rectangle panel;
   private int x, y, w, h;
   private ShapeGroup group;
   
   //------------------instance variables ------------------------------
 

   
   //------------------- constructor -----------------------------------
   public ButtonPanel(int x, int y,  String[] args, ButtonListener bl )
   {
       
      group = new ShapeGroup(); 
      client = bl;
      labels = args;
      numberOfButtons = labels.length;
      this.x = x;
      this.y = y;
      
      for(int i = 0; i < numberOfButtons; i++)
      {
        if( labels[i].length() > longestLabel)
            longestLabel = labels[i].length();
      }
      
      w = longestLabel*7 + 48;
      h = numberOfButtons * 25 + 15; 
      panel = new Rectangle(x,y);
      panel.setFillColor(Color.white);
       panel.setFrameColor(Color.black);
       panel.setSize(w,h );
       group.add(panel);
      makeButtons();
   }
      
   //-------------------------- makeButtons() -----------------------------------
   /**
    * make the buttons for interaction
    */
   private void makeButtons()
   {
            // build the desired buttons
      int bh = 20;  // button box height
      int bw = 20;  // button box width

      Button[] buttons = new Button[ labels.length ];
      int bx = x + 8;  // x coord of all buttons
      int by = y + 8;  
      
      for ( int b = 0; b < buttons.length; b++, by += (bh+7) )
      {
         buttons[ b ] = new Button( bx, by, bw, labels[ b ], b, this ); 
         group.add( buttons[ b ]);
      }
   }
   //++++++++++++++++++++++++++ ButtonListener methods ++++++++++++++++++++++++++
   public void buttonPressed( String buttonLabel, int buttonId )
   {
       if(client != null)
           client.buttonPressed( buttonLabel, buttonId );
       else
           System.out.println("No Client: mousePressed");
   }
   public void buttonReleased( String buttonLabel, int buttonId )
   {
      if(client != null)
           client.buttonReleased( buttonLabel, buttonId );
       else
           System.out.println("No Client: mouseReleased");
   }
   public void buttonClicked( String buttonLabel, int buttonId )
   {
      if(client != null)
           client.buttonClicked( buttonLabel, buttonId );
       else
           System.out.println("No Client: mouseClicked");
   }
   //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


   
  //-------------------------- -----------------------------------
   /**
    * 
    */
   private void setLocation( int x, int y )
   {
       if( group != null )
        group.setLocation( x, y );
   }

 
//---------------------- main ------------------------------------
   public static void main( String[] args )
   {     
      new Frame();
      String test [] = { "One", "two", "Three", "veryverylong" };
      ButtonPanel bp =  new ButtonPanel( 10, 10, test,  null );
      bp.setLocation( 200, 100 );
      
       String test2 [] = { "One" };
      ButtonPanel bp2 =  new ButtonPanel( 10,10, test2,  null );
      bp2.setLocation( 200, 300 );
   }   
}