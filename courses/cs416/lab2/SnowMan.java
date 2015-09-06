/** 
 * SnowMan.java:
 * 
 * Displays a simple snow man using the A-wrappers for awt implementation.
 * 
 * @author rdb
 * Created 9/11/07; derived from cs415 demo program, Start.java 
 * Last modified: 01/13/10
 */
import java.awt.*;


public class SnowMan
{
   //---------------- instance variables ------------------------------
   // Components need to be accessed when displaying
   //
   private AEllipse    head;
   private AEllipse    body;
   private AEllipse    leftEye;
   private AEllipse    rightEye;
   private ARectangle  hatBody;
   private ARectangle  hatBrim;
   private ARectangle  mouth;
   private ALine       leftArm;
   private ALine       rightArm;
   
   private ALine[]      smile;
    
   // -----------------------------------------------------------------
   /** 
    * Constructor for the SnowMan class. Arguments are the position.
    */
   public SnowMan( int x, int y )
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

      // create the head
      head = new AEllipse( x + headX, y + headY );
      head.setSize( headSize, headSize );
      head.setFillColor( Color.WHITE );
  
      // create the two eyes
      leftEye = new AEllipse( x + leftEyeX, y + leftEyeY );
      leftEye.setColor( Color.BLACK );
      leftEye.setSize( eyeSize, eyeSize );
      
      rightEye = new AEllipse( x + rightEyeX, y + rightEyeY );
      rightEye.setColor( Color.BLACK );
      rightEye.setSize( eyeSize, eyeSize );
       
      // create a smile
      smile = new ALine[ smileX.length - 1 ];
      for ( int i = 0; i < smileX.length - 1; i++ )
      {
        smile[ i ] = new ALine( Color.BLACK );
        smile[ i ].setPoints(  x + mouthX + smileX[ i ], 
                               y + mouthY + smileY[ i ], 
                               x  + mouthX + smileX[ i + 1 ], 
                               y  + mouthY + smileY[ i + 1 ] );
        smile[ i ].setLineWidth( 2 );
      }
      
      // create the hat and its brim
      hatBody = new ARectangle( x + hatX, y + hatY );
      hatBody.setSize( hatW, hatH );
      hatBody.setColor( Color.BLACK );
      
      hatBrim = new ARectangle( x + brimX, y + brimY );
      hatBrim.setSize( brimW, brimH );
      hatBrim.setColor( Color.BLACK );

      // create the body
      body = new AEllipse( x + bodyX, y + bodyY );
      body.setSize( bodySize, bodySize );
      body.setFillColor( Color.WHITE );
      
      // create some arms
      leftArm = new ALine( Color.BLACK );
      leftArm.setPoints( x + lArmX1, y + lArmY1, 
                         x + lArmX2, y + lArmY2 );
      leftArm.setLineWidth( 3 );

      rightArm = new ALine( Color.BLACK  );
      rightArm.setPoints( x + rArmX1, y + rArmY1, 
                         x + rArmX2, y + rArmY2 );
      rightArm.setLineWidth( 3 );
   }
   // ------------------------ display --------------------------------
   /** 
    * Invoke fill and draw methods of all the components
    */
   public void display( Graphics2D brush )
   {
      head.display( brush );
      body.display( brush );
      leftEye.display( brush );
      rightEye.display( brush );
      hatBody.display( brush );
      hatBrim.display( brush );
      leftArm.display( brush );
      rightArm.display( brush );
      for ( int i = 0; i < smile.length; i++ )
         smile[ i ].display( brush );
   }   
   /**/
}//End of Class SnowMan
