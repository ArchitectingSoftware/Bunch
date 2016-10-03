/****
 *
 *	$Log: FindNeighbor.java,v $
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

public class FindNeighbor {

  public FindNeighbor() {
  }

public
Cluster
clusterWorklist(Cluster c, Cluster maxC, int[] clustNames, boolean[] locks, int[] workList)
{
    //c.force();                                //set the cluster vector dirty
    double maxOF = c.getObjFnValue();
    double originalMax = maxOF;

    //int[] clustNames = c.getClusterNames();

    int[] clusters = c.getClusterVector();

    int[] maxClust = maxC.getClusterVector();
    //boolean[] locks = c.getLocks();

                   //SRC          DEST
    //System.arraycopy(clusters, 0, maxClust, 0, clusters.length);

    for (int i=0; i<workList.length; ++i) {
        int currNode = workList[i];
        int currClust = clusters[currNode];
        int j=0;
        for (; j<clustNames.length; ++j) {
            if ((!locks[i]) && (clustNames[j] != currClust)) {
                c.relocate(currNode, clustNames[j]); //
                //clusters[currNode] = clustNames[j];
                //c.calcObjFn();
                if (bunch.util.BunchUtilities.compareGreater(c.getObjFnValue(),maxOF)) {
                //if (c.getObjFnValue() > maxOF) {
                    maxC.copyFromCluster(c);
                    //System.arraycopy(clusters, 0, maxClust, 0, clusters.length);
                    maxOF = c.getObjFnValue();
                }
            }
        }
        c.relocate(currNode,currClust);
        //clusters[currNode] = currClust;
    }

    //if (maxOF > originalMax) {
    //    System.arraycopy(maxClust, 0, clusters, 0, clusters.length);
    //}
    //else {
    //  //we didn't find a better max partition then it's a maximum
    //  c.setConverged(true);
    //}
    //maxC.calcObjFn();
    return maxC;
}

}