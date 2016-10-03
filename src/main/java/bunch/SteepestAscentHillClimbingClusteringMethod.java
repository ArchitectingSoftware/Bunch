/****
 *
 *	$Log: SteepestAscentHillClimbingClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:56  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 3.0  2000/07/26 22:46:11  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */

package bunch;

import java.util.*;
import javax.swing.*;

public
class SteepestAscentHillClimbingClusteringMethod
  extends GenericHillClimbingClusteringMethod
{

public
SteepestAscentHillClimbingClusteringMethod()
{
}

protected
Cluster
getLocalMaxGraph(Cluster c)
{
    if (c == null) return null;

    Cluster maxC = c.cloneCluster();
    //Cluster intermC = c.cloneCluster();
    //totalWork = 0;

    double maxOF = maxC.getObjFnValue();
    double originalMax = maxOF;

    int[] clustNames = c.getClusterNames();
    int[] clusters = c.getClusterVector();
    int[] maxClust = maxC.getClusterVector();
    boolean[] locks = c.getLocks();

    //System.out.println("Cluster names before = " + clustNames.length);

    //Cluster maxC = c.cloneCluster();
    //Cluster intermC = c.cloneCluster();

    //double originalMax = maxC.getObjFnValue();
    //double maxOF = originalMax;

    //double maxOF = g.getObjectiveFunctionValue();
    //double originalMax = maxOF;

    //clustNames = c.getClusterNames();

    //int[] clusters = g.getClusters();
    //int[] maxClust = new int[clusters.length];
    //boolean[] locks = g.getLocks();

    //System.arraycopy(clusters, 0, maxClust, 0, clusters.length);

    for (int i=0; i<clusters.length; ++i) {
        int currClust = clusters[i];
        //c.pushNode(i);
        int j=0;
        for (; j<clustNames.length; ++j) {
            if ((!locks[i]) && (clustNames[j] != currClust)) {
                double t = c.getObjFnValue();
                c.relocate(i,clustNames[j]);
                //System.out.println("("+i+","+clustNames[j]+") before="+t+" after="+c.getObjFnValue()+" maxof="+maxOF);
                //**c.move(i,clusters[i],clustNames[j]); //clusters[i] = clustNames[j];
                //c.calcObjFn(); //.calculateObjectiveFunctionValue();

                if (bunch.util.BunchUtilities.compareGreater(c.getObjFnValue(),maxOF)) {
                //if (c.getObjFnValue() > maxOF) {
                    maxC.copyFromCluster(c);
                    //System.arraycopy(clusters, 0, maxClust, 0, clusters.length);
                    maxOF = c.getObjFnValue(); //.getObjectiveFunctionValue();
                }
                //else
                //{
                //    c.move(i,clustNames[j],clusters[i]); //rollbackLastMove();
                //}
            }
        }
        c.relocate(i,currClust);
        //c.popNode();
        //**c.move(i,clusters[i],currClust); //rollbackLastMove();   //clusters[i] = currClust;
    }

//******************** THIS IS NEW EXPIREMENTAL CODE
    if (!bunch.util.BunchUtilities.compareGreater(maxOF,originalMax)) {
      Node [] nodes = c.getGraph().getNodes();
      int newClusterID = c.allocateNewCluster();

        for (int i=0; i<clusters.length; ++i) {
          int currClust = clusters[i];
          c.relocate(i,newClusterID);
          int []edges = nodes[i].getDependencies();

          int j=0;
          for (; j<edges.length; ++j) {
            int otherNode = edges[j];
            if ((!locks[i]) && (!locks[otherNode])) {
                int otherNodeCluster = clusters[otherNode];
                c.relocate(otherNode,newClusterID);

                if (bunch.util.BunchUtilities.compareGreater(c.getObjFnValue(),maxOF)) {
                    maxC.copyFromCluster(c);
                    maxOF = c.getObjFnValue();
                }

                c.relocate(otherNode,otherNodeCluster);
            }
          }
          c.relocate(i,currClust);
        }
      c.removeNewCluster(newClusterID);
    }
//*********************** END OF EXPIREMENTAL CODE

    if (bunch.util.BunchUtilities.compareGreater(maxOF,originalMax)) {
    //if (maxOF > originalMax) {
        c.copyFromCluster(maxC);
        c.incrDepth();
        //System.out.println("Did not converge - depth = "+c.getDepth());
        //c.setClusterVector(maxClust);
        //System.arraycopy(maxClust, 0, clusters, 0, clusters.length);
    }
    else {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true); //.setMaximum(true);
      //System.out.println("Converged");
    }
    //c.calcObjFn(); //.calculateObjectiveFunctionValue();
    //System.out.println("MQ = "+c.getObjFnValue());
    //System.out.println("Cluster names after = " + c.getClusterNames().length);

    return c;
}

public
Configuration
getConfiguration()
{
  boolean reconf=false;
  if (configuration_d == null) {
    reconf = true;
  }

  HillClimbingConfiguration hc = (HillClimbingConfiguration)super.getConfiguration();

  if (reconf) {
    hc.setThreshold(1.0);
    hc.setNumOfIterations(1);
    hc.setPopulationSize(1);
  }
  return hc;
}

public void
setDefaultConfiguration()
{
  HillClimbingConfiguration hc = (HillClimbingConfiguration)super.getConfiguration();

  hc.setThreshold(1.0);
  hc.setNumOfIterations(1);
  hc.setPopulationSize(1);

  setConfiguration(hc);
}
}
