//---------------------- NormalDistribution class --------------------------
/**
 * Based on Random.nextGaussian that returns a Normal (Gaussian) distribuiton
 * based on a mean of 0 and standard deviation of 1.
 * 
 * Given a non-zero mean, we define a standard deviation of mean/4 which
 * seems fine for our purposes.
 */
import java.util.*;
public class NormalDistribution
{
   //------------- instance variables ------------
   private Random _rng;
   private double _mean;
   private double _stdDev;
   //------------------ constructor ---------------
   public NormalDistribution( double mean )
   {
      _mean = mean;
      _stdDev = mean / 4;
      _rng  = new Random( 0 );
   }
   //------------------- next() ---------------------
   public double next()
   {
      double x = _rng.nextGaussian() * _stdDev + _mean; 
      return  Math.max( 0, x );  
   }
   //------------------------ main --------------------------------
   public static void main( String[] args )
   {
      int numNum = 100000;
      int mean = 256;
      int nBins = mean / 4;
      NormalDistribution nd = new NormalDistribution( 256 );
      int[] bins = new int[ nBins ];
      for ( int i = 0; i < bins.length; i++ )
         bins[ i ] = 0;
      double maxV = -2000000.0;
      double minV = 2000000.0;
      for ( int j = 0; j < numNum; j++ )
      {
         double v = nd.next();
         if ( v < minV ) minV = v;
         else if ( v > maxV ) maxV = v;
         int bin = (int) ( v * ( nBins - 1 ) /( 2.0f * mean ));
         if ( bin < 0 ) 
            bin = 0;
         else if ( bin >= bins.length )
            bin = bins.length - 1;
         //System.out.println( v + " " + bin );
         bins[ bin ]++;
      }
      System.out.println( "min,max: " + minV + " " + maxV );
      int max = 0;
      for ( int i = 0; i < bins.length; i++ )
         if ( bins[ i ] > max )
            max = bins[ i ];
      float scale = 40.0f / max;
      for ( int i = 0; i < bins.length; i++ )
      {
         System.out.println( bins[ i ] );
         /**/
         int numStar = (int)( bins[i] * scale );
         for ( int d = 0; d < numStar; d++ )
             System.out.print( "*" );
         System.out.println();
         /**/
      }
      
   }
}
