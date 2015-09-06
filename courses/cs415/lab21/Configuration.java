/**
 *  Represents the four row-column 2D array coordinates for the tiles 
 *  of a tetris shape.
 *  
 * mlb fall 2010
 */ 
import wheelsunh.users.*;
import java.util.*;
import java.awt.*;

public class Configuration 
{
    /******************************************************************/
    // a vector of rour row-column coordinates
   // coordinates are stored as points
    private Vector<Point> vector;
   
    
    /******************************************************************/
    /**
     * Constructor, 8 integers representing the four row-column 
     * coordinates for the four tiles of a tetris shape
     */ 
    public Configuration( int r1, int c1, 
                          int r2, int c2,
                          int r3, int c3, 
                          int r4, int c4 )
    {
        super();
        vector = new Vector< Point >();
        vector.add( new Point( r1, c1 ) );
        vector.add( new Point( r2, c2 ) );
        vector.add( new Point( r3, c3 ) );
        vector.add( new Point( r4, c4 ) );
    }
    

   /******************************************************************/
    /**
     *  Get the i'th row-column coordinate in the configuration
     */ 
    public Point get( int i )
    {
       return vector.get( i );
    }
    
     /******************************************************************/
    /**
     *   Convert to a String
     */ 
    public String  toString()
    { 
        String s = "{ (" +  get(0).x + ", " +  get(0).y + "), "+
            "(" +  get(1).x + ", " +  get(1).y + "), "+ 
            "(" +  get(2).x + ", " +  get(2).y + "), "+ 
            "(" +  get(3).x + ", " +  get(3).y + ") } ";
        
        return s; 
    } 
    
}//END 
