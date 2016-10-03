/****
 *
 *	$Log: DistribInit.java,v $
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

import bunch.*;

public class DistribInit implements java.io.Serializable{

  public String svrName;
  public int    svrID;
  public Graph theGraph;
  public String clusteringTechnique;
  public String objFunction;
  public Configuration config;
  public BunchPreferences bp;
  public boolean adaptiveEnabled;

  public DistribInit() {
  }
}