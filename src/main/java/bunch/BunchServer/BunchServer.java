/****
 *
 *	$Log: BunchServer.java,v $
 *	Revision 3.0  2002/02/03 18:42:06  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:18  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */
package bunch.BunchServer;

import javax.swing.UIManager;
import java.awt.*;

public class BunchServer {
  boolean packFrame = false;
  boolean guiMode = true;
  String [] startupArgs = null;

  //Construct the application
  public BunchServer() {
  }

  public void setStartupParms(String [] args, boolean runMode)
  {
    guiMode = runMode;
    startupArgs = args;
  }

  public void start()
  {
      if(guiMode == true)
      BunchServerGui();
    else
      BunchServerText();
  }

  public void BunchServerText()
  {
    if (startupArgs.length != 4)
    {
      System.out.println("Usage:  BunchServer NameSpace ServerName NameServerName NameServerPort");
      System.exit(0);
    }
    else
    {
      try{
        BSTextServer bts = new BSTextServer(startupArgs);
        bts.start();
      }catch(Exception ex)
      {
        System.out.println("Exception in text server: " + ex.toString());
      }
    }
  }

  public void BunchServerGui() {
    BSWindow frame = new BSWindow();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
      frame.pack();
    else
      frame.validate();
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  //Main method
  public static void main(String[] args) {
    boolean startGuiMode = true;

    try  {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
    }


    if(args.length == 0)
      startGuiMode = true;
    else
      startGuiMode = false;

    BunchServer theServer = new BunchServer();
    theServer.setStartupParms(args,startGuiMode);
    theServer.start();
  }
}