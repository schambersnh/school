/*
 * BadTableException.java
 * Exception for a invalid FrequencyTable format
 * 
 * Stephen Chambers
 * 11/16/10
 * */
public class BadTableException extends Exception
{
  /*
   * Constructor for BadTableException
   * */
  public BadTableException( String message )
  {
    super( message );
  }
} //End of Class