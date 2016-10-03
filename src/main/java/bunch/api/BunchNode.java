 /****
 *
 *	$Log: BunchNode.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.4  2000/11/30 01:49:21  bsmitc
 *	Added support for various tests and statistical gathering
 *
 *	Revision 3.3  2000/11/26 20:39:26  bsmitc
 *	Added support for precision and recall calculations by using the
 *	BunchGraph API suite
 *
 *	Revision 3.2  2000/11/26 15:48:30  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.1  2000/11/26 15:45:35  bsmitc
 *	Initial Version - support for the BunchGraph api interface
 *
 *
 */
/**
 * Title:        Bunch Project<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Brian Mitchell<p>
 * Company:      Drexel University - SERG<p>
 * @author Brian Mitchell
 * @version 1.0
 */
package bunch.api;

import java.util.*;

public class BunchNode {

  static public final int NOT_A_MEMBER_OF_A_CLUSTER = -1;

  String nodeName = "";
  int    nodeIndex = -1;
  int    nodeCluster = -1;
  BunchCluster memberCluster = null;
  boolean isNodeCluster = false;
  ArrayList   deps = null;
  ArrayList   backDeps = null;
  HashMap     clusterMemberships = null;

  //public BunchNode()
  //{
  //  BunchNode("",-1,-1);
  //}

  public BunchNode(String name, int index, int cluster,  boolean isCluster)
  {
    nodeName = name;
    nodeIndex = index;
    nodeCluster = cluster;
    isNodeCluster = isCluster;
    clusterMemberships = new HashMap();
  }

  public void subscribeToCluster(BunchCluster bc)
  {
    if(bc != null)
      clusterMemberships.put(bc.getName(),bc);
  }

  public boolean isAMemberOfCluster(String name)
  { return clusterMemberships.containsKey(name); }

  public boolean isAMemberOfCluster(BunchCluster bc)
  { return isAMemberOfCluster(bc.getName()); }

  public int memberOfHowManyClusters()
  { return clusterMemberships.size(); }
  public void setDeps(ArrayList deps, ArrayList backDeps)
  {
    this.deps = deps;
    this.backDeps = backDeps;
  }

  public String getName()
  { return nodeName;  }

  public int  getCluster()
  { return nodeCluster; }

  public void resetCluster(int newClustNumber)
  { nodeCluster = newClustNumber; }

  public Collection getDeps()
  { return deps;  }

  public Collection getBackDeps()
  { return backDeps;  }

  public boolean isCluster()
  { return isNodeCluster; }

  public BunchCluster getMemberCluster()
  { return memberCluster; }

  public void setMemberCluster(BunchCluster bc)
  {
    memberCluster = bc;
    subscribeToCluster(bc);
  }
}