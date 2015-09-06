/**
 * ArrayUtilities: Some useful static methods for dealing with Arrays.
 */


public class ArrayUtilities
{
   //------------------- rowMax --------------------------------------------
   /**
    * Given a 2D array, return a 1D array whose entries are the maximum values
    * of the corresponding rows in the 2D array. 
    * 
    * If the input array is empty return an array of length 0.
    */
   public static float[] rowMax( float[][] data )
   {
    
      float temp[] = new float[data.length];
      float max;
      for(int i = 0; i < data.length; i++)
      {
        max = data[i][0];
        for(int j = 0; j < data[i].length; j++)
        {
          if (data[i][j] > max)
          {
            max = data[i][j];
          }
        }
        temp[i] = max;
      }
      
      return temp;
   }
   //------------------- colAverage --------------------------------------------
   /**
    * Given a 2D array, return a 1D array whose entries are the average values
    * of the corresponding rows in the 2D array. 
    * 
    * If the input array is empty return an array of length 0.
    */
   public static float[] colAverage( float[][] data )
   {

      float temp[] = new float[data.length];
      float sum = 0.0f, average = 0.0f;
      for(int i = 0; i < data.length; i++)
      {
        for(int j = 0; j < data[0].length; j++)
        {
          sum += data[i][j];
        }
        average = sum / data[0].length;
        temp[i] = average;
        sum = 0;
      }
      
      return temp;
   }
   //++++++++++++++++++++ DO NOT CHANGE ANY CODE BELOW HERE +++++++++++++++++
   //--------------------- toString( float[] ) ------------------------------
   /**
    * generate a string (with no line feeds) from an array floats.
    */
   public static String toString( float[] values )
   {
      if ( values == null )
         return "";
      
      String str = new String( "[" );
      String prefix = " ";  // first value isn't preceded by ,
      for ( int i = 0; i < values.length; i++ )
      {
         str += prefix + values[ i ];
         prefix = ", ";     // all subsequent values need , before them
      }
      str += " ]";
      return str;
   }
}