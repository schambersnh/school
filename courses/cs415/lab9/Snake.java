/**
 * Snake.java -- F08
 *
 */
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.Color;

public class Snake extends ShapeGroup
{
    //-------------------- instance variables ------------------------------
    private int     direction;
    private Point   last,  next;
    private Ellipse head;
    private Line    body;
    private int     length = 20;
    
    //---------------------- Constructor -------------------------------------
    /**
     * initialize with a cursor location and a location for the widget that
     * shows the current length feedback menus.
     */
    public Snake ( int locX, int locY ) 
    {
        last = new Point( locX, locY );       
        direction = 0;
        body = new Line( locX,locY, locX + length, locY );
        body.setColor( Color.BLACK );
        body.setThickness( 2 );
        head = new Ellipse( locX + length - 3, locY - 3 );
        head.setSize( 6, 6);
        head.setColor( Color.BLACK );
        add( body );
        add( head );
        
    }
    
    //----------------------turn left-----------------------------------
    public void turnLeft() 
    {
        if ( direction == 0 ) //right
        {   
            direction = 3;
            faceUp();
        }
        else if ( direction == 1 ) // down
        {
            direction = 0;
            faceRight();
        }
        else if ( direction == 2 ) //left
        {
            direction = 1;
            faceDown();
        }
        else //up
        {
            direction = 2;
            faceLeft();
        }
    }
    
    //----------------------turn right -----------------------------
    public void turnRight( ) 
    {
         if ( direction == 0 ) //right
        {   
            direction = 1;
            faceDown();
        }
        else if ( direction == 1 ) 
        {
            direction = 2;
            faceLeft();
        }
        else if ( direction == 2 ) 
        {
            direction = 3;
            faceUp();
        }
        else
        {
            direction = 0;
            faceRight();
        }
    }
    
    //---------------- move -----------------------------------------
    public void move( int  inc) 
    {      
        if ( direction == 0 )  //right
        {
            next = new Point( last.x + inc, last.y );
            setLocation( getXLocation() + inc, getYLocation());
        }
        else if ( direction == 1 )  //down
        {
            next = new Point( last.x, last.y + inc );
            setLocation( getXLocation() , getYLocation() + inc );
        }
        else if ( direction == 2 )  //left
        {
            next = new Point( last.x -inc, last.y );
            setLocation( getXLocation() - inc, getYLocation() );
        }
        else //  up
        {
            next = new Point( last.x, last.y - inc );
            setLocation( getXLocation() , getYLocation() -inc );
        }
        
        Line  line = new Line( last, next );    
        line .setColor(Color.green);
        
        last = next;      
    }

      //----------------------face down-----------------------------------
    public void faceDown() 
    {
          body.setPoints( last.x, last.y, last.x, last.y + length );
          head.setLocation( last.x - 3, last.y + length - 3);
    }
    
     //----------------------face up-----------------------------------
    public void faceUp() 
    {
          body.setPoints( last.x, last.y, last.x, last.y - length );
          head.setLocation( last.x - 3, last.y - length - 3 );
    }
     //----------------------face left-----------------------------------
    public void faceLeft() 
    {
          body.setPoints( last.x, last.y, last.x - length, last.y );
          head.setLocation( last.x - 3 - length, last.y - 3 );
    }
     //----------------------face right-----------------------------------
    public void faceRight() 
    {
          body.setPoints( last.x, last.y, last.x + length, last.y );
          head.setLocation( last.x - 3 + length, last.y - 3 );
    }
    
    
    
    
    
    //---------------- Testing Main ---------------------------
    public static void main( String a[] )
    {
        Frame f = new Frame();
        Snake s = new Snake( 100, 100 );
        s.move( 12 );
        s.turnLeft();
        s.move( 12 );
        s.faceDown();
    }    
}