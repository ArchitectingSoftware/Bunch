/****
 *
 *	$Log: NextAscentHillClimbingClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:53  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.11  2001/04/03 23:32:12  bsmitc
 *	Added NAHC (really HC) support for Distributed Bunch, updated release
 *	version number to 3.2
 *
 *	Revision 3.10  2000/11/26 15:48:13  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.9  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.8  2000/08/19 00:44:39  bsmitc
 *	Added support for configuring the amount of randomization performed when
 *	the user adjusts the "slider" feature of NAHC.
 *
 *	Revision 3.7  2000/08/16 00:12:46  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.6  2000/08/15 02:52:18  bsmitc
 *	Implemented adjustable NAHC feature.  This feature allows the user to set
 *	a minimum search threshold so that NAHC will not just take the first thing
 *	that it finds.
 *
 *	Revision 3.5  2000/08/14 18:29:52  bsmitc
 *	Fixed bug where the code was not exiting after the first (better) partition
 *	of the cluster was found.
 *
 *	Revision 3.4  2000/08/13 18:40:06  bsmitc
 *	Added support for SA framework
 *
 *	Revision 3.3  2000/08/12 22:16:10  bsmitc
 *	Added support for Simulated Annealing configuration for NAHC technique
 *
 *	Revision 3.2  2000/08/11 22:34:20  bsmitc
 *	Set default population back to 10.  With very small populations NAHC tends
 *	to produce one large cluster
 *
 *	Revision 3.1  2000/08/07 21:48:59  bsmitc
 *	*** empty log message ***
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
import javax.swing.*;

public
class NextAscentHillClimbingClusteringMethod
  extends GenericHillClimbingClusteringMethod
{
private Random random_d;

public
NextAscentHillClimbingClusteringMethod()
{
  random_d = new Random(System.currentTimeMillis());
}

protected
Cluster
getLocalMaxGraph(Cluster c)
{
    if (c == null) return null;

    boolean foundbetter;

    SATechnique saAlg = ((NAHCConfiguration)configuration_d).getSATechnique();
    int         iPct  = ((NAHCConfiguration)configuration_d).getRandomizePct();

    //System.out.println("Debug min to consider = "+ ((NAHCConfiguration)configuration_d).getMinPctToConsider());
    //System.out.println("Rnd Pct = "+ ((NAHCConfiguration)configuration_d).getRandomizePct());

    double      dPct  = (double)iPct/100.0;

    boolean acceptSAMove = false;

    Cluster maxC = c.cloneCluster();
    Cluster intermC = c.cloneCluster();
    //totalWork = 0;

    double maxOF = maxC.getObjFnValue();
    double originalMax = maxOF;

    int[] clustNames = c.getClusterNames();
    int[] clusters = c.getClusterVector();
    int[] maxClust = maxC.getClusterVector();
    boolean[] locks = c.getLocks();

    long maxPartitionsToExamine = (clusters.length /*c.size()*/) *(clustNames.length);
    int currClustersExamined = 0;
    double evalPct = (double)(((NAHCConfiguration)configuration_d).getMinPctToConsider())/100.0;
    long partitionsToExamine = (long)(((double)maxPartitionsToExamine)*evalPct);

//System.out.println("partitions to examine = " + partitionsToExamine + "  Min Pct = " + ((NAHCConfiguration)configuration_d).getMinPctToConsider()  );
//System.out.println("Cluster names length = " + clustNames.length + "  "+ c.getClusterNames().length);

    int [] rndClustNameOrdering = new int[clustNames.length];
    int [] rndClustOrdering = new int[clusters.length];

    for(int i = 0; i < rndClustNameOrdering.length;i++)
      rndClustNameOrdering[i] = i;

    for(int i = 0; i < rndClustOrdering.length;i++)
      rndClustOrdering[i] = i;

    //System.arraycopy(clustNames,0,rndClustNameOrdering,0,clustNames.length);
    //System.arraycopy(clusters,0,rndClustOrdering,0,clusters.length);
    int rndFreq = (int)(dPct * (double)((double)rndClustOrdering.length/2.0));

    //for (int i=0; i<(rndClustOrdering.length/2); ++i) {
    for (int i=0; i<rndFreq; ++i) {
      int pos1 = (int)(random_d.nextFloat() * (rndClustOrdering.length-1));
      int pos2 = (int)(random_d.nextFloat() * (rndClustOrdering.length-1));
      int tmp = rndClustOrdering[pos1];
      rndClustOrdering[pos1] = rndClustOrdering[pos2];
      rndClustOrdering[pos2] = tmp;
    }

    rndFreq = (int)(dPct * (double)((double)rndClustNameOrdering.length/2.0));
    //for (int i=0; i<(rndClustNameOrdering.length/2); ++i) {
    for (int i=0; i<rndFreq; ++i) {
      int pos1 = (int)(random_d.nextFloat() * (rndClustNameOrdering.length-1));
      int pos2 = (int)(random_d.nextFloat() * (rndClustNameOrdering.length-1));
      int tmp = rndClustNameOrdering[pos1];
      rndClustNameOrdering[pos1] = rndClustNameOrdering[pos2];
      rndClustNameOrdering[pos2] = tmp;
    }
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

    foundbetter = false;

//    boolean firstMove = true;

    try{
    //**** for (int i=0; i<clusters.length; ++i) {



    for (int i=0; i<clusters.length; ++i) {

        int currNode  = rndClustOrdering[i];
        int currClust = clusters[currNode];//c.getCluster(currNode);
        int tmpClust  = currClust;
//System.out.println();
//System.out.println("Current node = " + currNode + " current Cluster = " + currClust);
        int j=0;
        for (; j<clustNames.length; ++j) {
            if ((!locks[currNode]) && (clustNames[rndClustNameOrdering[j]] != currClust)) {

                currClustersExamined++;
                if((foundbetter)&&(currClustersExamined>partitionsToExamine))
                {
                    if(saAlg != null)
                      saAlg.changeTemp(null);

                  c.copyFromCluster(maxC);
                  c.incrDepth();
                  c.setConverged(false);

//System.out.println("EARLY1: "+(double)currClustersExamined/(double)maxPartitionsToExamine+"%");
                  return c;
                }

//System.out.println("Moving node : " + currNode+" to cluster " + rndClustNameOrdering[j]);
                c.relocate(currNode,clustNames[rndClustNameOrdering[j]]);

                if(saAlg != null)
                {
                  double dMQ = maxOF - c.getObjFnValue();

                  if(dMQ < 0)
                    acceptSAMove = saAlg.accept(dMQ);
                }

                if ((bunch.util.BunchUtilities.compareGreater(c.getObjFnValue(),maxOF))||(acceptSAMove))
                {
                    maxC.copyFromCluster(c);
//System.out.println("c = " + c.getClusterNames().length+"  maxOF = "+maxC.getClusterNames().length);
                    maxOF = c.getObjFnValue(); //.getObjectiveFunctionValue();

//if(firstMove == true)
//  System.out.println("taking first move");
//else
//  System.out.println("not taking first move");

                    foundbetter = true;

                    //if(saAlg != null)
                    //  saAlg.changeTemp(null);

                    if((currClustersExamined>partitionsToExamine)||(acceptSAMove))
                    {

                        if(saAlg != null)
                          saAlg.changeTemp(null);

                      c.copyFromCluster(maxC);
                      c.incrDepth();
                      c.setConverged(false);
//System.out.println("EARLY2: "+(double)currClustersExamined/(double)maxPartitionsToExamine+"%");
//System.out.println("EARLY2: "+(currClustersExamined/maxPartitionsToExamine)+"%");
                      return c;
                    }
                    //else
                    //  c.relocate(currNode,currClust);
                    //break;
                }
                //else
                //  c.relocate(currNode,currClust);
            }
            //if(foundbetter)
            //  break;
            //else
            //{
            //c.relocate(currNode,currClust);
            //}
       }
       c.relocate(currNode,currClust);
//firstMove = false;
//System.out.println("Restoring node " + currNode + " to cluster " + currClust);
    }
    }catch(Exception ex)
    {System.out.println(ex.toString()); }

//******************** THIS IS NEW EXPIREMENTAL CODE
    if (!bunch.util.BunchUtilities.compareGreater(maxOF,originalMax)) {
      Node [] nodes = c.getGraph().getNodes();
      int newClusterID = c.allocateNewCluster();

        for (int i=0; i<clusters.length; ++i) {
          int currNode  = rndClustOrdering[i];
          int currClust = clusters[currNode];

          c.relocate(currNode,newClusterID);
          int []edges = nodes[currNode].getDependencies();

          int j=0;
          for (; j<edges.length; ++j) {
            int otherNode = edges[j];
            if ((!locks[currNode]) && (!locks[otherNode])) {
                int otherNodeCluster = clusters[otherNode];
                c.relocate(otherNode,newClusterID);

                if (bunch.util.BunchUtilities.compareGreater(c.getObjFnValue(),maxOF)) {
                    maxC.copyFromCluster(c);
                    maxOF = c.getObjFnValue();
                    c.copyFromCluster(maxC);
                    c.incrDepth();
                    c.setConverged(false);
//System.out.println("EARLY3");
                    return c;
                }
                c.relocate(otherNode,otherNodeCluster);
            }
          }
          c.relocate(currNode,currClust);
        }
      c.removeNewCluster(newClusterID);
    }
//*********************** END OF EXPIREMENTAL CODE



    if(saAlg != null)
      saAlg.changeTemp(null);

    if (bunch.util.BunchUtilities.compareGreater(maxOF,originalMax))  {
        c.copyFromCluster(maxC);
        c.incrDepth();
    }
    else {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true); //.setMaximum(true);
    }

//System.out.println("LATE");


    return c;
}

protected
Graph
getLocalMaxGraph(Graph g)
{
    double maxOF = g.getObjectiveFunctionValue();

    int[] clustNames = null;
    if (g.hasDoubleLocks()) {
      clustNames = g.getUnlockedClusterNames();
    }
    else {
      clustNames = g.getClusterNames();
    }
    int[] clusters = g.getClusters();
    int[] ranClust = new int[clusters.length];
    boolean[] locks = g.getLocks();

    for (int i=0; i<ranClust.length; ++i) {
      ranClust[i] = i;
    }

    //create a random list of the nodes
    for (int i=0; i<(ranClust.length/2); ++i) {
      int pos1 = (int)(random_d.nextFloat() * (ranClust.length-1));
      int pos2 = (int)(random_d.nextFloat() * (ranClust.length-1));
      int tmp = ranClust[pos1];
      ranClust[pos1] = ranClust[pos2];
      ranClust[pos2] = tmp;
    }

    int freepos=0, freeval=0;
    boolean foundbetter = false;
    int num=0;

    while (!foundbetter && num<ranClust.length) {

      //create a random list of the clusters where the selected node might fit
      for (int i=0; i<(clustNames.length/2); ++i) {
        int pos1 = (int)(random_d.nextFloat() * (clustNames.length-1));
        int pos2 = (int)(random_d.nextFloat() * (clustNames.length-1));
        int tmp = clustNames[pos1];
        clustNames[pos1] = clustNames[pos2];
        clustNames[pos2] = tmp;
      }

//      System.err.println("\nclust=");
//      for (int i=0; i<clustNames.length; ++i) {
//        System.err.print(clustNames[i]+" ");
//      }

      int j = 0;
      int i = ranClust[num++];
      int currClust = clusters[i];

      for (; j<clustNames.length; ++j) {
        if (clustNames[j] == currClust) {
          freepos = j;
          freeval = clustNames[j];
          if (clustNames.length < clusters.length) {
            clustNames[j] = g.findFreeCluster(clustNames);
          }
          break;
        }
      }

      j=0;
      for (; j<clustNames.length; ++j) {
        if (!locks[i]) {
          clusters[i] = clustNames[j];
          g.calculateObjectiveFunctionValue();
          if (g.getObjectiveFunctionValue() > maxOF) {
            foundbetter = true;
            break;
          }
        }
      }
      if (foundbetter) {
        break;
      }
      clusters[i] = currClust;
      clustNames[freepos] = freeval;
    }

    //we didn't find a better max partition
    if (!foundbetter)
        g.setMaximum(true);

    g.calculateObjectiveFunctionValue();
    return g;
}

public
Configuration
getConfiguration()
{
  boolean reconf=false;
  if (configuration_d == null) {
    configuration_d = new NAHCConfiguration();
    reconf = true;
  }

  NAHCConfiguration hc = (NAHCConfiguration)configuration_d;

  if (reconf) {
    hc.setThreshold(1);
    hc.setNumOfIterations(1);
    hc.setPopulationSize(1);
    hc.setMinPctToConsider(0);
    hc.setRandomizePct(100);
    hc.setSATechnique(null);
  }

  return hc;
}

public
String
getConfigurationDialogName()
{
  return "bunch.NAHCClusteringConfigurationDialog";
}

public void
setDefaultConfiguration()
{
  NAHCConfiguration hc = (NAHCConfiguration)getConfiguration();

  hc.setThreshold(1.0/*0.1*/);
  hc.setNumOfIterations(1/*200*/);
  hc.setPopulationSize(1/*10*/);
  hc.setSATechnique(null);
  hc.setMinPctToConsider(0);
  hc.setRandomizePct(100);
  setConfiguration(hc);
}
}
