/****
 *
 *	$Log: ServerProperties.java,v $
 *	Revision 3.0  2002/02/03 18:42:07  bsmitc
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

import  bunch.*;

public class ServerProperties {

  public String svrName = null;
  public int    svrID = -1;
  public Graph theGraph = null;
  public String clusteringMethod = null;
  public String objFn = null;
  public Configuration cfg = null;
  public BunchPreferences bp = null;
  public Callback clientCB = null;
  public boolean adaptiveEnabled = true;
  public String jndiName = "";
  public double bestObjFnValue = -1.0;

  public ServerProperties() {
  }
}