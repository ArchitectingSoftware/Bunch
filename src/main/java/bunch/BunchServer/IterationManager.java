/****
 *
 *	$Log: IterationManager.java,v $
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

import bunch.*;

public class IterationManager implements java.io.Serializable{
  public static final int DIR_TO_CLIENT = 1;
  public static final int DIR_TO_SERVER = 2;

  public static final String MSG_GET_CLUSTER_VECTOR = "GET_CLUSTER_VECTOR";
  public static final String MSG_SEND_CLUSTER_VECTOR = "SEND_CLUSTER_VECTOR";

  public String  msgType = null;
  public int     msgID = -1;
  public String  jndiServerName = null;
  public int     svrID = -1;
  public int[]   clusterVector = null;
  public int[]   workVector = null;
  public int     direction = -1;
  public int     uowSz = -1;

  public IterationManager() {
  }
}
