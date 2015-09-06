/**
 * ButtonListener interface - A ButtonListener object creates a 
 *     Button object and passes itself to the Button as a "listener".
 *     When a button event occurs, the Button invokes the 
 *     corresponding method of the ButtonListener, so it 
 *     knows what has happened.
 * 
 * @author rdb
 * 11/15/09
 */
public interface ButtonListener
{
   public void buttonPressed( String buttonLabel, int buttonId );
   public void buttonReleased( String buttonLabel, int buttonId );
   public void buttonClicked( String buttonLabel, int buttonId );
}