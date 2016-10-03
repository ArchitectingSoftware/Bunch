/****
 *
 *	$Log: OptimalClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:54  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/08/11 15:04:28  bsmitc
 *	Added support for producing optimal output on the clustering progress
 *	dialog window
 *	
 *	Revision 3.0  2000/07/26 22:46:10  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.*;
import bunch.stats.StatsManager;

public
class OptimalClusteringMethod
  extends ClusteringMethod2
{
boolean hasMorePartitions_d = false;
int[] tmpClusters_d;
int[] nClusters_d;
int NC=0;

public
OptimalClusteringMethod()
{
}

//TO COMPLETE
public
void
run()
{
  Graph graph = getGraph().cloneGraph();
  StatsManager sm = StatsManager.getInstance();
  //setBestGraph(null);
  //Cluster bestCluster = new Cluster();

  int[] clusters = graph.getClusters();
  int   cSz = clusters.length;

  nClusters_d = new int[clusters.length+1];

  //  System.arraycopy(clusters, 0, nClusters_d, 1, clusters.length);
  tmpClusters_d = new int[clusters.length+1];
//  System.arraycopy(clusters, 0, tmpClusters_d, 1, clusters.length);

  clusters = new int[cSz];
  int[] lastCluster = new int[cSz];

  IterationEvent ev = new IterationEvent(this);
  System.arraycopy(nClusters_d, 1, clusters, 0, clusters.length);

  //Cluster currC = new Cluster(graph,null);

  sm.clearExhaustiveFinished();
  sm.setExhaustiveTotal(getMaxIterations());
  sm.incrExhaustiveFinished();
  boolean morePartitions = findNextPartition();

  System.arraycopy(clusters,0,lastCluster,0,clusters.length);

  //currC.setClusterVector(nClusters_d);
  Cluster currC = new Cluster(graph,clusters);
  //currC.setClusterVector(nClusters_d);
  setBestCluster(currC.cloneCluster());
  Cluster bestCluster = new Cluster();
  bestCluster.copyFromCluster(currC);

  double bestOFValue = bestCluster.calcObjFn();
  int j = 2;

  while (morePartitions) {
  //System.out.println("BestOBJ fn = "+bestCluster.getObjFnValue());
  System.arraycopy(nClusters_d, 1, clusters, 0, clusters.length);

    //System.out.print("[");
    for(int i = 0; i < clusters.length;i++)
      if(clusters[i]!=lastCluster[i])
        currC.relocate(i,clusters[i]);
      //System.out.print(clusters[i]+" ");
    //System.out.println("]");

    //currC.setClusterVector(clusters);

    //System.arraycopy(nClusters_d, 1, clusters, 0, clusters.length);
    //graph.calculateObjectiveFunctionValue();

    double ofValue = currC.calcObjFn(); //.getObjectiveFunctionValue();
    if (bunch.util.BunchUtilities.compareGreater(ofValue,bestOFValue)) {
    //if (ofValue > bestOFValue) {
      currC.incrDepth();
      bestCluster.copyFromCluster(currC);
      //bestCluster.incrDepth();
      bestOFValue = ofValue;
      //setBestGraph(currC.getGraph());
      bestCluster.getClusterNames();
      setBestCluster(bestCluster.cloneCluster());
    }
    ev.setIteration(j++);
    System.arraycopy(clusters,0,lastCluster,0,clusters.length);
    morePartitions = findNextPartition();
    sm.incrExhaustiveFinished();
    //this.fireIterationEvent(ev);
  }
//  setBestGraph(bestCluster.getGraph().clone());
}

static int xx=1;
private
boolean
findNextPartition()
{
  int M, L;
  int N = getGraph().getNumberOfNodes();

  if(hasMorePartitions_d) {
    M = N;
    boolean more = true;
    L = nClusters_d[M];
    while(more) {
      L = nClusters_d[M];
      if (tmpClusters_d[L] != 1) {
        more = false;
      }
      else {
        nClusters_d[M] = 1;
        M = M - 1;
      }
    }
    NC = NC + M - N;
    tmpClusters_d[1] = tmpClusters_d[1] + N - M;
    if (L == NC) {
      NC = NC + 1;
      tmpClusters_d[NC] = 0;
    }

    nClusters_d[M] = L + 1;
    tmpClusters_d[L] = tmpClusters_d[L] - 1;
    tmpClusters_d[L+1] = tmpClusters_d[L+1] + 1;

  }
  else {
    NC = 1;
    for(int i=1; i <= N; i++)
      nClusters_d[i] = 1;
    tmpClusters_d[1] = N;
  }

  hasMorePartitions_d = (NC != N);

  return hasMorePartitions_d;
}

public
int
getMaxIterations()
{
  return (int)(getGraph().getNumberOfPartitions());
}

//public Cluster getBestCluster()
//{
//  Graph bestG = getBestGraph();
//  Cluster c = new Cluster(bestG,bestG.getClusters());
//  c.calcObjFn();
//  return c;
//}
}
