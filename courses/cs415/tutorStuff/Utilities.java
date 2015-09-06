/** 
 * Utilities.java:
 *  
 *mlb 4/2012
 */

import java.util.*;
import  sun.audio.*;    
import  java.io.*;

public class Utilities
{  

  //---------------------------------------------------------
  //  Depricated Sun audio
  // may generate warnings
   public static void play (String file)
    {
       AudioStream as;
        try
        {
            AudioPlayer.player.start( new AudioStream( new FileInputStream( file ) ) );   
        }
        catch(Exception e)
        {
            System.out.println("Can not open audio file");          
        }
    }


}//End of Class 