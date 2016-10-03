 /****
 *
 *	$Log: Bunch.java,v $
 *	Revision 3.0  2002/02/03 18:41:43  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/08/18 21:07:59  bsmitc
 *	Added feature to support tree output for dotty and text
 *
 *	Revision 3.1  2000/07/26 23:27:55  bsmitc
 *	Changed about box to include updated copywrite date and CVS release tag
 *
 *	Revision 3.0  2000/07/26 22:46:07  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import javax.swing.UIManager;
import java.awt.*;

/**
 * The main aplication launcher class. This class basically sets the
 * general parameters (such as GUI) and then creates a BunchFrame and
 * displays it.
 *
 * @see bunch.BunchFrame
 */
public
class Bunch
{
boolean packFrame = false;

public
Bunch()
{
  BunchFrame frame = new BunchFrame();

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

public static
void
main(String[] args)
{
  try {
    if (args.length > 0) {
      System.setErr(new java.io.PrintStream(new java.io.FileOutputStream(args[0])));
    }

    //--------------------------------------------------------------------------------
    //Below is generated, but uncomment the desired layout manager
    //--------------------------------------------------------------------------------
    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//"javax.swing.plaf.windows.WindowsLookAndFeel");
    //UIManager.setLookAndFeel(new javax.swing.plaf.motif.MotifLookAndFeel());
    UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
  }
  catch (Exception e) {
    try{
       UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }catch (Exception e2) {
       e2.printStackTrace();
    }
  }

  if (args.length == 1)
  {
    String a = args[0];
    //--------------------------------------------------------------------------------
    //Bunch can be started in server mode by placing a -s or -server at the end of
    //the command line
    //--------------------------------------------------------------------------------
    if ((a.equalsIgnoreCase("-s")) || (a.equalsIgnoreCase("-server")))
    {
      bunch.BunchServer.BunchServer theServer = new bunch.BunchServer.BunchServer();
      theServer.setStartupParms(args,true);
      theServer.start();
    }
    else
      System.out.println("Bad arguement, for BunchServer use -s or -server");
  }
  else
    new Bunch();
}
}



