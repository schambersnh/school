import java.util.*;
import java.io.*;

public class QuoteGenerator 
{
   private Scanner in;
   private String quote;
   private Random rng;
   private ArrayList<String>  quotes;
   private int idx;
   
   // true for grading  false normal mode
   private boolean gradeMode = false;
   private int n = 0;
   
   
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public QuoteGenerator(  )
   {
      this( "quotes.txt" );
   }
   
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public QuoteGenerator(String file)
   {  
      if( gradeMode)
         idx = 0;
      else
      {
         try
         {
            BufferedReader padBR= new BufferedReader( new FileReader( file )   );
            
            System.out.println( "Quote Generator Started with File: " +
                               file + "   ");
            rng = new Random();
            in = new Scanner( padBR );
            quotes = new ArrayList<String>();
            
            while(in.hasNextLine() )
               quotes.add(in.nextLine());
         }
         catch( Exception e )
         {
            System.out.println( "Could not open File " + file );  
            System.exit(0);
         }   
      }
      
      Collections.shuffle(quotes);
   }
   
   
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public String nextQuote( )
   {
      n = ( n + 1 ) % quotes.size();
      if( n >= quotes.size() )
      {
         return null;  
      }
      else
         return quotes.get(n);
   }
   
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public ArrayList<String> getQuotes( )
   {
         return quotes;
   }
   
   
   
   //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   public static void main(String arg[])
   {
      QuoteGenerator wg;
      
      if( arg.length < 1)
         wg = new QuoteGenerator(   );
      else
         wg = new QuoteGenerator( arg[0] );
      
      String s = wg.nextQuote();
      for(int i = 0; i < 20; i++ )
      {
         System.out.println(s);
         s = wg.nextQuote();
      }
   }   
}