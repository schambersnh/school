/** 
 * Program6.java: Puts some Seasonal objects 
 * through a yearly cycle
 * Fall 2010
 * 
 *  @author mlb for cs415 
 */

import wheelsunh.users.*;
import java.awt.Color;

public class Program6 extends Frame 
{  
   //---------------- instance variables -------------------------------------
   private Rectangle _background;   // the sky
   private Color _backgroundColor;
   
   // Three Seasonal objects 
   private Seasonal _seasonal1;  
   private Seasonal _seasonal2;
   private Seasonal _seasonal3;
   private TextBox _text;
   
   //------------------- Program6( )------------------------------------------
   /**
    *  Create a background and three draggable Seasonals and
    *  run them through a daily cycle
    */
   public Program6(  )
   {
      
      // create the background
      _backgroundColor = new Color( 200, 200, 255 );
      _background = new Rectangle(0,0);
      _background.setSize(700,500);
      _background.setColor(_backgroundColor);
      
      _text = new TextBox(300, 10);
      _text.setWidth (100);
      
      
      
      
      ////////////////////////////////////////////
      // Change the code below:
      // replace the nulls with your objects:
      // Construct your three Seasonal objects
      // and assign them to these instance variables
      ////////////////////////////////////////////
      
      _seasonal1 = new ATV(100, 100);  
      _seasonal2 = new Earth(300, 100);
      _seasonal3 = new House(200, 200);
      
      ////////////////////////////////////////////
      // Do not change any other code in 
      // Program6
      ////////////////////////////////////////////

      yearlyCycle();
   }
   
   //--------------------------------------------------------------------
   /**
    *   change season
    * 
    */ 
   public void season( int seasonId ){
      switch ( seasonId )
      {
         case 0: //winter
            _text.setText("Winter");
            if(_seasonal1 != null)
               _seasonal1.winter( );  
            if(_seasonal2 != null)
               _seasonal2.winter( ); 
            if(_seasonal3 != null)
               _seasonal3.winter( );
            break;
            
         case 1://spring
            _text.setText("Spring");
            if(_seasonal1 != null)
               _seasonal1.spring( );   
            if(_seasonal2 != null)
               _seasonal2.spring( );
            if(_seasonal3 != null)
               _seasonal3.spring( );  
            break;
            
         case 2://summer
            _text.setText("Summer");
            if(_seasonal1 != null)  
               _seasonal1.summer( );
            if(_seasonal2 != null)
               _seasonal2.summer( );   
            if(_seasonal3 != null)
               _seasonal3.summer( );
            break;
            
         case 3://fall
            _text.setText("Fall");
            if(_seasonal1 != null)
               _seasonal1.fall( );
            if(_seasonal2 != null)
               _seasonal2.fall( ); 
            if(_seasonal3 != null)
               _seasonal3.fall( ); 
            break; 
      }               
   }
   
   
   
   //------------------------- yearlyCycle(  ) --------------------------------
   /**
    *   go through the changes of season
    */
   public void yearlyCycle (   )
   { 
          season( 3 ) ; //fall

       sleep( 2000 );
       season( 0 ); //winter

       sleep( 2000 );
       season( 1 ); //spring

       sleep( 2000 );
       season( 2 ); //summer

       sleep( 2000 );

       season( 3 ) ; //fall

       sleep( 2000 ); 
       season( 0 ); //winter

       sleep( 2000 );
       season( 1 ); //spring

       sleep( 2000 );
       season( 2 ); //summer

       sleep( 2000 );

       season( 3 ) ; //fall

       sleep( 2000 ); 
       season( 0 ); //winter

       sleep( 2000 );
       season( 1 ); //spring

       sleep( 2000 );
       season( 2 ); //summer
   }
   
   
   //--------------------------- sleep( int ) --------------------------------
   /** 
    * sleep for the given number of milliseonconds
    */
   public static void sleep( int milliseconds ) 
   {
      try
      {
         Thread.sleep( milliseconds );
      }
      catch ( java.lang.InterruptedException e ) 
      {  }
   }
   
   
   
   //-------------------------- main ( String [ ] ) --------------------------
   /** 
    * Start the program
    */
   public static void main( String[ ] args )
   {      
      new Program6(  ); 
   }  
}//End of Class Program6



