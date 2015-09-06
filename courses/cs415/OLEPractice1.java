/**
 * OLEPractice1 -- OLE practice
   //********************** DO NOT CHANGE CODE IN THIS CLASS ******************
   // This class provides simple tests of the tasks for the lab.
 */

import java.util.*;
import wheelsunh.users.*;


public class OLEPractice1
{
   public static void main( String[] args )
   {
      // --------------------------- test rowMax ------------------------
      float dataFixed[][] = { { 0, 2, 4, 3, 5, 1 },
                         { 10, 12, 14, 13, 15, 11 },
                         { -20, -22, -26, -23, -25, -21 },
                         { 30, -32, 34, -33, 35, -31 }
                       };
      float dataVar[][] = { { 0, 2, 4, 3, 5, 1 },
                         { 10, 12, 14, 13, 15 },
                         { -20, -22, -26, -23 },
                         { 30, -32, 34 }
                       };

      System.out.println( "------------------ rowMax test ---------------" );
      System.out.println( "----------- Normal test: " );
      float[] rMax = ArrayUtilities.rowMax( dataVar );
    
      System.out.println( ArrayUtilities.toString( rMax ));      
      
      System.out.println( "----------- Empty array test: " );
      rMax = ArrayUtilities.rowMax( new float[0][0] );
      System.out.println( ArrayUtilities.toString( rMax ));      
      System.out.println( "-------------------------------------------------" );  
      
      //---------------------- test colAverage ------------------------------
      System.out.println( "------------------ colAverage test ---------------" );
      System.out.println( "----------- Normal test: " );
      float[] cAvg = ArrayUtilities.colAverage( dataFixed );
    
      System.out.println( ArrayUtilities.toString( cAvg ));      
      
      System.out.println( "----------- Empty array test: " );
      cAvg = ArrayUtilities.colAverage( new float[1][0] );
      System.out.println( ArrayUtilities.toString( cAvg ));      
      System.out.println( "-------------------------------------------------" );  
      
      // ----------------------------- test tokenize ---------------------------
      String scannerInput = 
         "Try some numbers: 23 456.67 -4 126888 5.6E32 and "
         + "true FALSE FaLsE tRUE bigger: 1.234567899E38";
      
      System.out.println( "------------------ tokenize test ---------------" );
      Scanner sstr = new Scanner( scannerInput );
      ArrayList<Object> tokens = ScannerUtilities.tokenize( sstr );
      
      for ( int i = 0; i < tokens.size(); i++ )
      {
         Object t = tokens.get( i );
         System.out.println( "Token : " + i + ": " 
                               + t.getClass().getName() + "  =  " + t );
      }
      System.out.println( "-------------------------------------------------" );  

      // ----------------------------- test indexAll ---------------------------
      
      System.out.println( "----------------  indexAll tests ----------------" );  
      System.out.println( "--------- AAAAA ::: AA ---" );
      Vector<Integer> locs = StringUtilities.indexAll( "AAAAA", "AA" );      
      System.out.println( locs );

      System.out.println( "--------- AAAAA ::: bb ---" );
      locs = StringUtilities.indexAll( "AAAAA", "bb" );
      System.out.println( locs );

      System.out.println( "--------- the last of the mohicans: "
                                      + " the hit of the 40's ::: the" );
      locs = StringUtilities.indexAll( "the last of the mohicans: "
                                      + " the hit of the 40's", "the" );
      System.out.println( locs );
      System.out.println( "-------------------------------------------------" );     
   }   
}