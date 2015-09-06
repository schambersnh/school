/**
 *  Chapter 4: Draggable.java
 *  An interface for objects that can be dragged with the mouse. 
 * 
 *  From: Sanders and van Dam, Object-Oriented Programming in Java
 *        Addison-Wesley
 *  We have added getColor
 */
import java.awt.Color;
import java.awt.event.MouseEvent;

public interface Draggable
{
    public void  setColor( java.awt.Color aColor );
    public Color getColor();
    public void  setLocation( int x, int y );
    public void  mousePressed( MouseEvent me );
    public void  mouseDragged( MouseEvent me );
    public void  mouseReleased( MouseEvent me );
}
 