/****
 *
 *	$Log: BunchPreferencesDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:44  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
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

import java.awt.*;
import javax.swing.*;

/**
 * Dialog that can be used in a future version to configure the preferences
 * and then store them.
 *
 * @author Brian Mitchell
 */
public
class BunchPreferencesDialog
  extends JDialog
{
JPanel panel1 = new JPanel();

public
BunchPreferencesDialog(Frame frame, String title, boolean modal)
{
  super(frame, title, modal);
  try {
    jbInit();
    pack();
  }
  catch (Exception ex) {
    ex.printStackTrace();
  }
}

public
BunchPreferencesDialog()
{
  this(null, "", false);
}

private
void
jbInit() throws Exception
{
  getContentPane().add(panel1);
}
}

