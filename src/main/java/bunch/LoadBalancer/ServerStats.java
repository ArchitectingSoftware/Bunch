/****
 *
 *	$Log: ServerStats.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */

/**
 * Title:        Bunch Version 1.2 Base<p>
 * Description:  Your description<p>
 * Copyright:    Copyright (c) 1999<p>
 * Company:      <p>
 * @author Brian Mitchell
 * @version
 */
package bunch.LoadBalancer;

public class ServerStats {

  public int svrID = -1;
  public int totalWork = 0;
  public int currUOWSz = -1;
  public int workSinceLastAdjustment = 0;

  public ServerStats() {
  }
}