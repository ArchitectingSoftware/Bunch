/****
 *
 *	$Log: Drifters.java,v $
 *	Revision 3.0  2002/02/03 18:41:48  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:09  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.io.*;
import java.util.Vector;

/**
 * This class helps to eliminate drifters, which are modules that appear
 * in clusters for which they have no connection to any other modules in
 * the cluster.  Drifters should be rare, if even happen at all.
 *
 * @see bunch.GraphOutput
 *
 * @author Brian Mitchell
 */
public
class Drifters
{
public final static int NODE_NOT_CONNECTED=-1;

protected Graph graph_d;
  Vector edges = new Vector();
  int clusters[] = null;
  Node[] nodeList = null;
  int nodes = -1;
  int[][] clustersMatrix = null;
  int[] partners = null;

/**
 * The constructor caches the graph object
 */
public
Drifters(Graph g)
{
   graph_d = g;
}

/**
 * This method intitializes a matrix to track which modules are connected
 */
private
void
initStructures()
{
   clusters = graph_d.getClusters();
   nodeList = graph_d.getNodes();
   nodes = nodeList.length;
   clustersMatrix = new int[nodes][nodes+1];
   partners = new int[nodes];

   for (int i=0; i<nodes; ++i) {
       clustersMatrix[i][0] = 0;
       nodeList[i].cluster = -1;
       partners[i] = 0;
   }
   for (int i=0; i<nodes; ++i) {
       int numCluster = clusters[i];
       clustersMatrix[numCluster][(++clustersMatrix[numCluster][0])] = i;
       nodeList[i].cluster = numCluster;
   }
}

/**
 * This method fixes the drifters.
 *
 * @return True if a drifter is found, false if no drifters are found
 */
public boolean consolidate()
{
   boolean rc;
   boolean foundDrifter = false;
   initStructures();

   for (int i=0; i<nodes; ++i) {
      rc = fixDrifters();
      if(rc == true) foundDrifter = true;
      if (rc == false)
         return foundDrifter;
   }
   return foundDrifter;
}

/**
 * This method is used to fix the actual drifters
 */
public
boolean
fixDrifters()
{
  boolean drifterFound = false;
  int pos=0;

  int id = 0;
  int clusterIndex = 1;

  for (int i=0; i<nodes; ++i) {
  //do for each cluster
    if (clustersMatrix[i][0]>0) {
      //do for each node within each cluster
      for (int j=1; j<=clustersMatrix[i][0]; ++j) {
        int potDrifter = clustersMatrix[i][j];
        int connections = 0;

        for (int k=1; k<=clustersMatrix[i][0]; ++k) {
          int otherNode = clustersMatrix[i][k];

          //check for drifter
          if (potDrifter != otherNode){
            boolean forw = hasEdgeBetween(potDrifter,otherNode);
            boolean back = hasEdgeBetween(otherNode,potDrifter);
            if ((forw==true) || (back==true))
              connections++;
          }
        }

        //if theres is no edge
        if (connections == 0) {
          System.err.println("Drifter: " + nodeList[potDrifter].getName()+ " is a drifter");
          drifterFound = true;
          findDrifterHomes(potDrifter);
          initStructures();
        }
      }
    }
  }
  return drifterFound;
}

/**
 * This method determines if two nodes have a common edge
 */
private
boolean
hasEdgeBetween(int i, int j)
{
   if (nodeList[i].dependencies == null)
      return false;

   int [] e = nodeList[i].dependencies;

   /**
    * Use the matrix to determine
    */
   for (int z = 0; z < e.length; z++)
      if (e[z]==j)
      {
         return true;
      }

   return false;
}

/**
 * This method finds the best home for a given drifter
 *
 * @param The index in the array of the drifter
 */
private
void
findDrifterHomes(int d)
{
  int [] density = new int[nodes];
  Node n = nodeList[d];
  int homeCluster = n.cluster;
  for (int i=0; i<nodes; ++i)
    density[i] = 0;

  for (int i=0; i<nodes; ++i) {
  //do for each cluster
    if (clustersMatrix[i][0]>0) {
    //do for each node within each cluster
      for (int j=1; j<=clustersMatrix[i][0]; ++j) {
        int nid = clustersMatrix[i][j];
        if (nid == d)
        {
          int edges[] = nodeList[d].dependencies;
          if(edges != null){
            for (int ecnt=0;ecnt<edges.length;ecnt++)
              density[nodeList[edges[ecnt]].cluster]++;
          }
        }
        else
        {
          int edges[] = nodeList[nid].dependencies;
          if(edges != null){
            for (int ecnt=0;ecnt<edges.length;ecnt++)
              if(edges[ecnt]==d)
                density[nodeList[nid].cluster]++;
          }
        }
      }
    }
  }
  //dumpFreqArray(d,density);
  int newHome = getMaxConnectedCluster(d,density);
  if (newHome != NODE_NOT_CONNECTED)
    moveNode(d,nodeList[d].cluster,newHome);
}

/**
 * This method moves a node between a source and desitnation cluster
 */
private
void
moveNode(int node, int srcCluster, int destCluster)
{
   clusters[node] = destCluster;
   nodeList[node].cluster = destCluster;
   graph_d.setNodes(nodeList);
   graph_d.setClusters(clusters);
   initStructures();
}

/**
 * This method determines the maximum connected cluster for a given node
 */
private
int
getMaxConnectedCluster(int d, int []density)
{
   int maxConn = NODE_NOT_CONNECTED;
   int maxConnCluster = NODE_NOT_CONNECTED;
   for (int i=0; i < density.length; i++)
   {
      if (density[i] > 0)
      {
         if (density[i] > maxConn) {
            maxConn = density[i];
            maxConnCluster = i;
         }
      }
   }
   String src = nodeList[d].getName();
   return maxConnCluster;
}

/**
 * This method is a debugging routine
 */
private
void
dumpFreqArray(int d, int []density)
{
  for (int i=0; i < density.length; i++)
  {
    if (density[i] > 0)
    {
      String src = nodeList[d].getName();
      System.err.println(src + " has " + density[i] + " connection(s) to cluster "+i);
    }
  }
}
};


