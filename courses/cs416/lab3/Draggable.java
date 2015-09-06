/**
 * Draggable
 *       Dragging interface that can be enabled/disabled
 *       This interface is intended to be used for objects that are not
 *          descendents of JComponent (or, really its ancestor, Container).
 *       In particular this works for A-classes and composite objects that
 *          contain A-class objects.
 */

import java.awt.event.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public interface Draggable 
{ 
   public void    setDraggable( boolean onOff );
   public boolean isDraggable();
   public boolean contains( Point2D p );
   public void    moveBy( int x, int y );
}
