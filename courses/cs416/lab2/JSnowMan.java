/** 
 * JSnowMan.java:
 * 
 * Displays a simple snow man using the A-wrappers for awt implementation.
 * 
 * @author rdb
 * Created 9/11/07; derived from cs415 demo program, Start.java 
 * Last modified: 01/13/10
 */
import java.awt.*;
import javax.swing.*;


public class JSnowMan extends JComponent
{
   //---------------- instance variables ------------------------------
   // Components need to be accessed when displaying
   //
   private JEllipse    head;
   private JEllipse    body;
   private JEllipse    leftEye;
   private JEllipse    rightEye;
   private JRectangle  hatBody;
   private JRectangle  hatBrim;
   private JRectangle  mouth;
   private JLine       leftArm;
   private JLine       rightArm;
   
   private JLine[]      smile;
    
   // -----------------------------------------------------------------
   /** 
    * Constructor for the JSnowMan class. Arguments are the position.
    */
   public JSnowMan( int x, int y )
   {
      // local variables define location/sizes of components
      int    hatX   = 38,           hatY   = 0;
      int    hatW   = 25,   hatH   = 13;
      
      int    headX     = 25,  headY     = hatY + hatH;
      int    headSize  = 50;
      int    bodyX     = 10,  bodyY     = headY + headSize - 10;
      int    bodySize  = 80; 
      
      int    mouthX    = 40,  mouthY    = headY + 30;
      
      int[]  smileX     = { 0, 5, 15, 20 };
      int[]  smileY     = { 0, 2, 2, 0 };
      
      int    leftEyeX  = 40,  leftEyeY  = headY + 15;
      int    rightEyeX = 55,  rightEyeY = leftEyeY;
      int    eyeSize   = 4;
      int    brimX  = headX + 5,  brimY  = headY;
      int    brimW = headSize - 10, brimH = 4;
      
      int    lArmX1 =  0, lArmY1 = leftEyeY;
      int    lArmX2 = bodyX + 5, lArmY2 = bodyY + 20;
      int    rArmX1 = bodyX + bodySize - 5,  rArmY1 = lArmY2;
      int    rArmX2 = bodyX + bodySize + 40, rArmY2 = mouthY + 14;

      setSize( rArmX2, bodyY + bodySize );
      setLocation( x , y );

  
      // create the two eyes
      leftEye = new JEllipse(  leftEyeX,  leftEyeY );
      leftEye.setColor( Color.BLACK );
      leftEye.setSize( eyeSize, eyeSize );
      this.add( leftEye );
      
      rightEye = new JEllipse(  rightEyeX,  rightEyeY );
      rightEye.setColor( Color.BLACK );
      rightEye.setSize( eyeSize, eyeSize );
      this.add( rightEye );
       
      // create a smile
      smile = new JLine[ smileX.length - 1 ];
      for ( int i = 0; i < smileX.length - 1; i++ )
      {
        smile[ i ] = new JLine( Color.BLACK );
        smile[ i ].setPoints(   mouthX + smileX[ i ], 
                                mouthY + smileY[ i ], 
                                mouthX + smileX[ i + 1 ], 
                                mouthY + smileY[ i + 1 ] );
        smile[ i ].setLineWidth( 2 );
        this.add( smile[i] );
      }
      
      // create the hat and its brim
      hatBody = new JRectangle(  hatX,  hatY );
      hatBody.setSize( hatW, hatH );
      hatBody.setColor( Color.BLACK );
      this.add ( hatBody ) ;
      
      hatBrim = new JRectangle(  brimX,  brimY );
      hatBrim.setSize( brimW, brimH );
      hatBrim.setColor( Color.BLACK );
      this.add( hatBrim );

      // create the body
      body = new JEllipse(  bodyX,  bodyY );
      body.setSize( bodySize, bodySize );
      body.setFillColor( Color.WHITE );
      this.add( body );
      
      // create some arms
      leftArm = new JLine( Color.BLACK );
      leftArm.setPoints(  lArmX1,  lArmY1, 
                          lArmX2,  lArmY2 );
      leftArm.setLineWidth( 3 );
      this.add( leftArm );

      rightArm = new JLine( Color.BLACK  );
      rightArm.setPoints(  rArmX1,  rArmY1, 
                          rArmX2,  rArmY2 );
      rightArm.setLineWidth( 3 );
      this.add( rightArm );
      
      // create the head
      head = new JEllipse(  headX,  headY );
      head.setSize( headSize, headSize );
      head.setFillColor( new Color(255, 255, 255, 128 ) );
      this.add(head);
   }
   // ------------------------ display --------------------------------
   /** 
    * Invoke fill and draw methods of all the components
    */
   public void display( Graphics2D brush )
   {

   }   
   /**/
}//End of Class JSnowMan
