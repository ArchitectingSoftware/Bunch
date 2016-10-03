/****
 *
 *	$Log: SteepestAscentHillClimbingClusteringMethod2.java,v $
 *	Revision 3.0  2002/02/03 18:41:56  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
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
class SteepestAscentHillClimbingClusteringMethod2
  extends GenericDistribHillClimbingClusteringMethod
{

public
SteepestAscentHillClimbingClusteringMethod2()
{
}

protected
Cluster
getLocalMaxGraph(Cluster c)
{
System.out.print("IN:  " + c.getObjFnValue() + "  ");
    double maxOF = c.getObjFnValue();
    double originalMax = maxOF;

    int[] clustNames = c.getClusterNames();

    int[] clusters = c.getClusterVector();
    
    int[] maxClust = new int[clusters.length];
    boolean[] locks = c.getLocks();
    
                   //SRC          DEST
    System.arraycopy(clusters, 0, maxClust, 0, clusters.length);

    for (int i=0; i<clusters.length; ++i) {
        int currClust = clusters[i];
        int j=0;
        for (; j<clustNames.length; ++j) {
            if ((!locks[i]) && (clustNames[j] != currClust)) {
                clusters[i] = clustNames[j];
                c.calcObjFn();
                if (c.getObjFnValue() > maxOF) {
                    System.arraycopy(clusters, 0, maxClust, 0, clusters.length);
                    maxOF = c.getObjFnValue();
                }
            }
        }
        clusters[i] = currClust;
    }

    if (maxOF > originalMax) {
        System.arraycopy(maxClust, 0, clusters, 0, clusters.length);
    }
    else {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true);
    }
    c.calcObjFn();

System.out.println("OUT:  " + c.getObjFnValue() + "  ");
    
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
    hc.setThreshold(0.1);
    hc.setNumOfIterations(100);
    hc.setPopulationSize(5);
  }
  return hc;
}

public void
setDefaultConfiguration()
{
  HillClimbingConfiguration hc = (HillClimbingConfiguration)super.getConfiguration();

  hc.setThreshold(0.1);
  hc.setNumOfIterations(100);
  hc.setPopulationSize(5);

  setConfiguration(hc);
}
}
