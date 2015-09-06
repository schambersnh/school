/**
 * ScannerUtilities: Some useful static methods associated with Scanners.
 */

import java.util.*;

public class ScannerUtilities
{
  /**
   * tokenize: Read all tokens in the scanner, return an ArrayList<Object>
   *                with the tokens in order. 
   *      You should recognize the following token types:
   *            byte
   *            short
   *            int
   *            float
   *            boolean
   *      and create objects of type:
   *            Byte
   *            Short
   *            Integer
   *            Float
   *            Boolean
   *      All other tokens are returned as String.
   */
  public static ArrayList<Object> tokenize( Scanner scan )
  {
    ArrayList<Object> tokens = new ArrayList<Object>();
    while(scan.hasNext())
    {
      if(scan.hasNextByte())
      {
        tokens.add(new Byte(scan.nextByte()));
      }
      else if(scan.hasNextShort())
      {
        tokens.add(new Short(scan.nextShort()));
      }
      else if(scan.hasNextInt())
      {
        tokens.add(new Integer(scan.nextInt()));
      }
      else if(scan.hasNextFloat())
      {
        tokens.add(new Float(scan.nextFloat()));
      }
      else if(scan.hasNextBoolean())
      {
        tokens.add(new Boolean(scan.nextBoolean()));
      }
      else
      {
        tokens.add(new String(scan.next()));
      }
    } 
    return tokens;
  }      
}