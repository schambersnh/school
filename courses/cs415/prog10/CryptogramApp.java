/**
 * CryptogramApp.java
 * Displays a cryptogram to be solved by the user
 * 
 * Stephen Chambers
 * 11/9/10
 * 
 * 
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class CryptogramApp
{
  //-----------------Instance Variables --------------------------------------------------------
  
  protected String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ", defaultCipher = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  protected TextBox realQuoteBox, jumbledQuoteBox, defaultCipherBox, myKeyBox, mySolution;
  protected String substitution, myAuthor, realQuote, jumbledQuote;
  protected String userQuote, userAuthor, userKey, myKey;
  protected QuoteGenerator myQuoteGenerator = new QuoteGenerator();
  protected HideShowButton hmySubButton;
  protected int count;
  protected NextButton myNextButton;
  protected SubButton mySubButton;
  
  //----------------------------------------------------------------------------
  /**
   * Constructor
   */
  protected CryptogramApp()
  {
    realQuoteBox = new TextBox( 90, 420 );
    realQuote = myQuoteGenerator.nextQuote();
    realQuote = realQuote.toUpperCase();
    
    myAuthor = realQuote.substring( realQuote.indexOf( "(" ), 
                                 realQuote.indexOf( ")" ) );
    realQuote = realQuote.replace( myAuthor, "" );
    realQuote = realQuote.replace( ")", "" );
   
    myAuthor = StringUtilities.replaceAt( myAuthor, "", 0 );
    realQuoteBox.setText( realQuote + "\n" + myAuthor );
    
    myKey = StringUtilities.scramble( alphabet );    
    myKeyBox = new TextBox( 390, 25 );
    myKeyBox.setSize( 85, 310 );
    myKeyBox.setText( alphabet + "\n\n" + myKey );
    
    
    defaultCipherBox = new TextBox( 280, 25 );
    defaultCipherBox.setSize( 75, 300 );
    defaultCipherBox.setText( alphabet + "\n\n" + defaultCipher );
    
    
    jumbledQuoteBox = new TextBox( 50, 50 );
    jumbledQuoteBox.setText( StringUtilities.translate( realQuote, myKey ) + 
                            "\n" + StringUtilities.translate( myAuthor, myKey ) );
    
   
    userKey = new String( myKey );
    mySolution = new TextBox( 50, 260 );
    userQuote = StringUtilities.translate( realQuote, userKey );
    userAuthor = StringUtilities.translate( myAuthor, userKey );
    mySolution.setText( userQuote + "\n" + userAuthor );
    
    
    realQuoteBox.hide();
    myKeyBox.hide();
    
   
    hmySubButton = new HideShowButton( 400, 400, this );
    myNextButton = new NextButton( 400, 350, this );
    mySubButton = new SubButton( 400, 300, this );
  }
  
  //----------------------------------------------------------------------------
  /**
   * Hides the solution and the real quote.
   */
  protected void hide()
  {
    count++;
    myKeyBox.hide();
    realQuoteBox.hide();
    hmySubButton.count = count;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Show solution
   */
  protected void show()
  {
    count++;
    myKeyBox.show();
    realQuoteBox.show();
    hmySubButton.count = count;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Get the next quote
   */
  protected void next()
  {

    realQuote = myQuoteGenerator.nextQuote();
    realQuote = realQuote.toUpperCase();
    
    myAuthor = realQuote.substring( realQuote.indexOf( "(" ), 
                                 realQuote.indexOf( ")" ) );
    
    realQuote = realQuote.replace( myAuthor, "" );
    realQuote = realQuote.replace( ")", "" );
   
    myAuthor = StringUtilities.replaceAt( myAuthor, "", 0 );
    realQuoteBox.setText( realQuote + "\n" + myAuthor );
    
    myKey = StringUtilities.scramble( alphabet );
    myKeyBox.setText( alphabet + "\n\n" + myKey );
    
    defaultCipher = alphabet;
    defaultCipherBox.setText( alphabet+ "\n\n" + defaultCipher );
    
    jumbledQuoteBox.setText( StringUtilities.translate( realQuote, myKey ) + 
                            "\n" + StringUtilities.translate( myAuthor, myKey ) );
    
    userKey = new String( myKey );    
    userQuote = StringUtilities.translate( realQuote, userKey );
    userAuthor = StringUtilities.translate( myAuthor, userKey );
    mySolution.setText( userQuote + "\n" + userAuthor );
    
    myKeyBox.hide();
    realQuoteBox.hide();
    
  }
  
  //----------------------------------------------------------------------------
  /**
   * Substitute cipher with a letter picked by the user
   */
  protected void substitute()
  {
    substitution = JOptionPane.showInputDialog( null, "Enter Substitution." );   
    
    if( substitution.length() > 0 )
    
      substitution = substitution.trim();
      substitution = substitution.replace( " ", "" );
      substitution = substitution.toUpperCase();
      

      if( alphabet.indexOf( substitution.substring( 0, 1 ) ) != -1 && 
         alphabet.indexOf( substitution.substring( 2 ) ) != -1 )
      {
    
        defaultCipher = StringUtilities.replaceAt( defaultCipher, 
                                                 substitution.substring( 0, 1 ), 
                                                 alphabet.indexOf
                                              ( substitution.substring( 2 ) ) );
        defaultCipherBox.setText( alphabet + "\n\n" + defaultCipher );

        userKey = StringUtilities.replaceAt( myKey, substitution.substring( 0, 1 ), 
                                            alphabet.indexOf
                                              ( substitution.substring( 2 ) ) );
        
        userQuote = StringUtilities.translate( realQuote, userKey );
        userAuthor = StringUtilities.translate( myAuthor, userKey );
        mySolution.setText( userQuote + "\n" 
                                  + userAuthor );
      }
      else
      {
        System.err.println( "Substitution invalid." );
      }
    }
  
  
//----------------------------------------------------------------------------
  /**
   * Main
   */
  protected static void main(String arg[])
  {    
    new Frame( 700, 600 );
    new CryptogramApp();
  }
} //End of Class