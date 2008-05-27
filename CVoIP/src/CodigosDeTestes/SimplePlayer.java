/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CodigosDeTestes;

/**
 *
 * @author Eriko Verissimo
 */
// Fig. 22.1: SimplePlayer.java
// Opens and plays a media file from
// local computer, public URL, or an RTP session

// Java core packages
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

// Java extension packages
import javax.swing.*;
import javax.media.*;

public class SimplePlayer extends JFrame {

   // Java media player
   private Player player;

   // visual content component
   private Component visualMedia;

   // controls component for media
   private Component mediaControl;

   // main container
   private Container container;

   // media file and media locations
   private File mediaFile;
   private URI fileURL;

   // constructor for SimplePlayer
   public SimplePlayer()
   {
      super( "Simple Java Media Player" );

      container = getContentPane();

      // panel containing buttons
      JPanel buttonPanel = new JPanel();
      container.add( buttonPanel, BorderLayout.NORTH );

      // opening file from directory button
      JButton openFile = new JButton( "Open File" );
      buttonPanel.add( openFile );

      // register an ActionListener for openFile events
      openFile.addActionListener(

         // anonymous inner class to handle openFile events
         new ActionListener() {

            // open and create player for file
            public void actionPerformed( ActionEvent event )
            {
               mediaFile = getFile();

               if ( mediaFile != null ) {

                  // obtain URL from file
                  try {
                     fileURL = mediaFile.toURI();
                  }

                  // file path unresolvable
                  catch ( Exception badURL ) {
                     badURL.printStackTrace();
                     showErrorMessage( "Problema de URL" );
                  }

                  makePlayer( fileURL.getPath() );

               }

            }  // end actionPerformed

         }  // end ActionListener

      ); // end call to method addActionListener

      // URL opening button
      JButton openURL = new JButton( "Open Locator" );
      buttonPanel.add( openURL );

      // register an ActionListener for openURL events
      openURL.addActionListener(

         // anonymous inner class to handle openURL events
         new ActionListener() {

            // open and create player for media locator
            public void actionPerformed( ActionEvent event )
            {
               String addressName = getMediaLocation();

               if ( addressName != null )
                  makePlayer( addressName );
            }

         }  // end ActionListener

      ); // end call to method addActionListener

      // turn on lightweight rendering on players to enable
      // better compatibility with lightweight GUI components
      Manager.setHint( Manager.LIGHTWEIGHT_RENDERER,
         Boolean.TRUE );

   }  // end SimplePlayer constructor

   // utility method for pop-up error messages
   public void showErrorMessage( String error )
   {
      JOptionPane.showMessageDialog( this, error, "Error",
         JOptionPane.ERROR_MESSAGE );
   }

   // get file from computer
   public File getFile()
   {
      JFileChooser fileChooser = new JFileChooser();

      fileChooser.setFileSelectionMode(
         JFileChooser.FILES_ONLY );

      int result = fileChooser.showOpenDialog( this );

      if ( result == JFileChooser.CANCEL_OPTION )
         return null;

      else
         return fileChooser.getSelectedFile();
   }

   // get media location from user input
   public String getMediaLocation()
   {
      String input = "rtp://127.0.0.1:8080/video";//JOptionPane.showInputDialog(this, "Enter URL" );

      // if user presses OK with no input
      if ( input != null && input.length() == 0 )
         return null;

      return input;
   }

   // create player using media's location
   public void makePlayer( String mediaLocation )
   {
      // reset player and window if previous player exists
      if ( player != null )
         removePlayerComponents();

      // location of media source
      MediaLocator mediaLocator =
         new MediaLocator( mediaLocation );

      if ( mediaLocator == null ) {
         showErrorMessage( "Error opening file" );
         return;
      }

      // create a player from MediaLocator
      try {
         player = Manager.createPlayer( mediaLocator );

         // register ControllerListener to handle Player events
         player.addControllerListener(
            new PlayerEventHandler() );

         // call realize to enable rendering of player's media
         player.start();
      }

      // no player exists or format is unsupported
      catch ( NoPlayerException noPlayerException ) {
         noPlayerException.printStackTrace();
      }

      // file input error
      catch ( IOException ioException ) {
         ioException.printStackTrace();
      }

   }  // end makePlayer method

   // return player to system resources and
   // reset media and controls
   public void removePlayerComponents()
   {
     // remove previous video component if there is one
     if ( visualMedia != null )
        container.remove( visualMedia );

     // remove previous media control if there is one
     if ( mediaControl != null )
        container.remove( mediaControl );

     // stop player and return allocated resources
     player.close();
   }

   // obtain visual media and player controls
   public void getMediaComponents()
 /**/{
      // get visual component from player
      visualMedia = player.getVisualComponent();

      // add visual component if present
      if ( visualMedia != null )
        container.add( visualMedia, BorderLayout.CENTER );

      // get player control GUI
      mediaControl = player.getControlPanelComponent();

      // add controls component if present
      if ( mediaControl != null )
         container.add( mediaControl, BorderLayout.SOUTH );

   }  // end method getMediaComponents

   // handler for player's ControllerEvents
   private class PlayerEventHandler extends ControllerAdapter {

      // prefetch media feed once player is realized
        @Override
      public void realizeComplete(
         RealizeCompleteEvent realizeDoneEvent )
      {
         player.prefetch();
      }

      // player can start showing media after prefetching
      @Override
      public void prefetchComplete(
         PrefetchCompleteEvent prefetchDoneEvent )
      {
         getMediaComponents();

         // ensure valid layout of frame
         validate();

         // start playing media
         player.realize();

      }  // end prefetchComplete method

      // if end of media, reset to beginning, stop play
      @Override
      public void endOfMedia( EndOfMediaEvent mediaEndEvent )
      {
         player.setMediaTime( new Time( 0 ) );
         player.stop();
      }

   }  // end PlayerEventHandler inner class

   // execute application
   public static void main( String args[] )
   {
      SimplePlayer testPlayer = new SimplePlayer();

      testPlayer.setSize( 300, 300 );
      testPlayer.setLocation( 300, 300 );
      testPlayer.setDefaultCloseOperation( EXIT_ON_CLOSE );
      testPlayer.setVisible( true );
   }

}  // end class SimplePlayer
