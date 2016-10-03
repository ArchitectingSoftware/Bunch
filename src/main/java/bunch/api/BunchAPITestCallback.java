
/**
 * Title:        Bunch Project<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Brian Mitchell<p>
 * Company:      Drexel University - SERG<p>
 * @author Brian Mitchell
 * @version 1.0
 */

 /****
 *
 *	$Log: BunchAPITestCallback.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/10/22 16:14:01  bsmitc
 *	Changed version number to 3.0 to sync with rest of project
 *	
 *	Revision 1.1.1.1  2000/10/22 16:05:57  bsmitc
 *	Initial Version
 *	
 *
 */
package bunch.api;

import java.util.*;

public class BunchAPITestCallback implements ProgressCallbackInterface {

  public BunchAPITestCallback() {
  }

  public void stats(Hashtable h)
  {
    System.out.println("Callback executed");
    System.err.flush();
  }
}