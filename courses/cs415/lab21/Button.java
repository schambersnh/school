/**
 * Button.java 
 * 
 * @author mlb
 * rdb: added ButtonListener interface
 */
import java.awt.Color;
import java.awt.event.*;
import wheelsunh.users.*;

public class Button extends RoundedRectangle
{
    private ButtonListener listener;
    private String         label;
    private int            identifier;
    private TextBox tb;
    private Color bodyColor = new Color( 230, 230, 230 );
    private Color bodyColorActive = new Color( 150, 150, 255 );
    private Color frameColor = new Color( 150, 150, 255 );
    private Color frameColorActive = new Color( 150, 150, 255 );
    private ShapeGroup group;

    //------------------------- Constructor -------------------------------
    public Button( int x, int y, int w, String labl, int id, ButtonListener bl )
    {  
        super(x,y);
        tb = new TextBox( labl );
        tb.setLocation( x, y );
     
        tb.setColor(new Color( 0, 0, 0, 0 ) );
        tb.setLocation( x + 20, y - 2 );
        setSize( 20, 20 );
        setFillColor( bodyColor );
        setFrameColor(  frameColor );
        listener = bl;
        label = labl;
        identifier = id;
        setFrameThickness( 2 );
        group = new ShapeGroup();
        group.add( tb );
        group.add( this );
    }
    
    //----------------------------------------------------------------------
    public void mousePressed( MouseEvent e )
    {
        setFillColor( bodyColorActive );
        if(listener != null)
            listener.buttonPressed( label, identifier );
        else
            System.out.println( "No Listener: mousePressed" );
        
    }    
    //----------------------------------------------------------------------
    public void mouseReleased( MouseEvent e )
    {
        setFillColor( bodyColor );
        if( listener != null )
            listener.buttonReleased( label, identifier );
         else
            System.out.println( "No Listener: mouseReleased" );
    }    
    //----------------------------------------------------------------------
    public void mouseClicked( MouseEvent e )
    {
        if(listener != null)
            listener.buttonClicked( label, identifier );
         else
            System.out.println( "No Listener: mouseClicked" );
    }    
      //----------------------------------------------------------------------
    public void setLocation( int x, int y )
    {
        super.setLocation( x, y );
        if( tb != null )
        {
             tb.setLocation( x + 20, y - 2 );   
        }
    }    

    //---------------------- main ------------------------------------
   public static void main( String[ ] args )
   {     
      new Frame();
      Button bp =  new Button( 10,10,10, "Button", 1,  null );
      bp.setLocation( 200, 100 );
   }   
    
    
    
} 