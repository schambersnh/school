/** 
 * ATVDriver.java:
 *     Puts  ATV's through their paces 
 * mlb f10
 */

import wheelsunh.users.*;
import java.awt.Color;

public class ATVDriver extends Frame
{ 
    //---------------- instance variables ------------------------------
    private ATV     myATV;           // the myATV being tested
    private TextBox label,         // Shows the test being performed
                    colorLabel,    // Shows results of color test
                    locStartLabel, // Show start location of moveForward test
                    locEndLabel;   // Show end location of moveForward test
    
    //--------------- constructor --------------------------------------
    /**
     * All the work is done in the constructor:
     *    Construct a ATV and then test it.
     *    TextBox objects are displayed to label each test and to show results
     */
    public ATVDriver()
    {
        label = new TextBox( 270, 40 );
        label.setSize( 290, 100 );
        label.setText( "No parameter Constructor" );
        
        myATV = new ATV();
        
        sleep( 2000 );

        label.setText( "Two parameter Constructor\n ( 20,200 )" );
        sleep( 1000 );
        myATV = new ATV( 20, 200 );
        
        sleep( 2000 );
       
        label.setText( "One parameter Constructor\n (ORANGE)" );
        sleep( 1000 );
        myATV = new ATV( Color.ORANGE );
        
        sleep( 2000 );
        
        label.setText( "SetLocation ( 40, 350 ) " );
        sleep( 1000 );
        myATV.setLocation( 40, 350 );
        
        sleep( 2000 );
        label.setText( "SetColor (PINK) " );
        sleep( 1000 );
        myATV.setColor( Color.pink );
        
        
        sleep( 2000 );
        colorLabel = new TextBox( 270, 140 );
        colorLabel.setSize( 290,100 );
        
        colorLabel.setText( "getColor =  " + myATV.getColor() +
                           "\n ( 225, 175, 175 )" );
        
        sleep( 2000 );
        
        locStartLabel = new TextBox( 270, 240 );
        locStartLabel.setSize( 290,100 );      
        locStartLabel.setText( "Start Location" +
                               "\ngetXlocation =  " + myATV.getXLocation() +
                               "\ngetYLocation = " + myATV.getYLocation() +
                               "\n (40, 350)\n" );     
        sleep( 1000 );
           
        label.setText( "moveForward " );
        sleep( 1000 );

        for( int i = 0; i < 60; i++ )
        {
            myATV.moveForward( 5 );
            sleep( 20 );
        }
        
        locEndLabel = new TextBox( 270, 410 );
        locEndLabel.setSize( 290,100 ); 
        locEndLabel.setText( "End Location" +
                                "\ngetXlocation = " + myATV.getXLocation() +
                                "\ngetYLocation = " + myATV.getYLocation() +
                                "\n (40, 50)\n" );
    }
    
    //--------------------- sleep( int ) -----------------------------------
    /**
     * Wait specified number of milliseconds before continuing execution.
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
    
    //-------------------- main -------------------------------------------
    /**
     * Just create a ATVDriver object
     */
    public static void main( String[] args )
    {
        new ATVDriver();
    }
}
