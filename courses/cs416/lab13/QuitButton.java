/** 
 * QuitButton.java:
 * A simple button that quits the application when pressed.
 * Based on Sanders and van Dam's QuitButton.java, p. 287.
 *
 * Matthew Plumlee
 * CS416 Spring 2008
 */

import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

public class QuitButton extends JButton 
{
   //-------------- constructor -------------------------------
   public QuitButton() 
   {
      super( "Quit" );
      addActionListener( new QuitListener() );
   }
   //+++++++++++++++ QuitListener private class +++++++++++++++++++++
   private class QuitListener implements ActionListener 
   {
      public void actionPerformed( ActionEvent e ) 
      {
         System.exit( 0 );
      }
   }
}