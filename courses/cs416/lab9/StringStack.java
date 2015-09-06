/**
 * A  StringSack implementation using Vector 
 *   This implementation associates the "top" of the stack with
 *   the beginning of the Vector (position 0)
 * 
 * @author rdb
 * September 2010
 * ---------------------------------------------------------------
 * Performance results:
 * ---------------------
 * Original implementation (add to position 0)
 *           Push Time   Pop Time
 * 100000:  
 * 
 * Revised implementation (add to end)
 *           Push Time   Pop Time
 *  100000:
 * 1000000:
 * ---------------------------------------------------------------
 * "Essay" answers:
 * ----------------
 * 6. Why do you think the first implementation is so much worse than
 *    the second?
 * 
 * 
 * 
 * 
 */

import java.util.Vector;

public class StringStack
{
   //----------------- instance variables ----------------------------
   private Vector<String> _stack;
  
   //------------------ constructor ---------------------------------
   public StringStack()
   {
      _stack = new Vector<String>();
   }
   
   //------------------ push ---------------------------------
   public void push( String item )
   {
      _stack.add( 0, item );  // add to start of Vector
   }
   
   //------------------ pop ---------------------------------
   public String pop()
   {
      String retVal = null;
      if ( _stack.size() > 0 )
      {
         retVal = _stack.get( 0 );
         _stack.remove( 0 );
      }
      return retVal;
   }
   //------------------ isEmpt() ---------------------------------   
   public boolean isEmpty()
   {
      return _stack.size() == 0;
   }    
   //------------------ size() ---------------------------------
   public int size()
   {
      return _stack.size();
   }   
   //----------------- toString() -------------------------------
   public String toString()
   {
      return _stack.toString();
   }
   
   //=======================================================================
   //---------------------------- main -----------------------------
   /**
    * main does some very basic tests
    */
   public static void main( String[] args )
   {
      boolean timing = true;      
      if ( args.length > 0 )
         timing = false;

      StringStack test = new StringStack();
      
      test.push( "A" );
      test.push( "B" );
      test.push( "C" );
      System.out.println( "Stack should have C,B,A: " + test );
      String c = test.pop();
      System.out.println( "Should have popped C: " + c );
      test.pop();
      test.push( "D" );
      System.out.println( "Stack should have D,A: " + test );
      
      //////////////////////////////////////////////////////////
      // Timing tests
      ////////////////////////////////////////////////////////// 
      if ( timing )
      {
         pushPopTimer();
         pushPop2Timer();
      }
   }
   //--------------- pushPopTimer() --------------------------------
   /**
    * Timing tests for the push/pop implementation
    */
   private static void pushPopTimer()
   {
      String plusBar = " +++++++++++++++++++++++++++++ ";
      System.out.println( plusBar + "push/pop tests" + plusBar );

      ////////////////////////////////////////////////////////////////
      //
      // Execute time tests for push/pop
      //
      ////////////////////////////////////////////////////////////////
   }
   //--------------- pushPop2Timer() --------------------------------
   /**
    * Timing tests for the push/pop implementation
    */
   private static void pushPop2Timer()
   {
      String plusBar = " +++++++++++++++++++++++++++++ ";
      System.out.println( plusBar + "push2/pop2 tests" + plusBar );

      ////////////////////////////////////////////////////////////////
      //
      // Execute time tests for push2/pop2
      //
      ////////////////////////////////////////////////////////////////
   }
   
   //------------------------- timeTest -----------------------------
   /**
    * This method does some very basic performance tests on the implementation.
    *
    * The code pushes a very large number of objects onto the stack and
    * then pops them all off.
    * 
    * Actually it pushes the SAME object onto the stack a large number
    * of times -- this removes the overhead of creating a large number
    * of objects which might muddy the evaluation.
    * 
    * It's not a completely realistic use of a stack, but it is good 
    * enough to do some simple implementation comparisons
    */
   private static void timeTest( int count )
   {
      System.out.println( "-------------- " + count + " items --------------" );
      StringStack stack = new StringStack();
      String       sample = new String( "copy" );
      
      long        startTime = System.currentTimeMillis();
      
      for ( int i = 0; i < count; i++ )
         stack.push( sample );
      
      long   pushTime = System.currentTimeMillis() - startTime;
      System.out.println( "Push time = " + ( pushTime / 1000.0f ) + " seconds" );

      startTime = System.currentTimeMillis();
      
      while ( ! stack.isEmpty() )
         stack.pop();
      
      long    popTime = System.currentTimeMillis() - startTime;
      System.out.println( "Pop time  = " + ( popTime / 1000.0f ) + " seconds" ); 
   }
   //------------------------- timeTest2 -----------------------------
   /**
    * This method is a copy of timeTest, except that it uses calls to
    * methods: push2 and pop2
    * in order to test alternative implementation strategies.
    * 
    * Because push2 and pop2 are not implemented initially, the
    * comment start after the method definition must be deleted so that
    * the code is "uncommented".
    */   
   private static void timeTest2( int count )
   {
      /************** Close this comment (or delete the line) ***************
        
      System.out.println( "-------------- " + count + " items --------------" );
      StringSack stack = new StringSack();
      String       sample = new String( "copy" );
      
      long        startTime = System.currentTimeMillis();
      
      for ( int i = 0; i < count; i++ )
         stack.push2( sample );
      
      long   pushTime = System.currentTimeMillis() - startTime;
      System.out.println( "Push time = " + ( pushTime / 1000.0f ) + " seconds" );

      startTime = System.currentTimeMillis();
      
      while ( ! stack.isEmpty() )
         stack.pop2();
      
      long    popTime = System.currentTimeMillis() - startTime;
      System.out.println( "Pop time  = " + ( popTime / 1000.0f ) + " seconds" ); 
      /*********************************************************/
   }
}