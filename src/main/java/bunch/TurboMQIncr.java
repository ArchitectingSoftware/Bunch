/****
 *
 *	$Log: TurboMQIncr.java,v $
 *	Revision 3.0  2002/02/03 18:41:58  bsmitc
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

/**
 * The basic objective function calculator. This calculation method does not
 * take into account the weights of the edges between nodes in the graph.
 * The method uses the graph obtained in the #init(bunch.Graph) method
 * and then makes the calculations, setting the appropriate values in the
 * graph when finished.
 *
 * @see bunch.ObjectiveFunctionCalculator
 * @see bunch.ObjectiveFunctionCalculatorFactory
 */
public
class TurboMQIncr
  implements ObjectiveFunctionCalculator
{
private Graph graph_d;
private static int[][] clusterMatrix_d = null;
private Node[] nodes_x;
private int[] clusters_x = null;
private int numberOfNodes_d;

private int[] muE = null;
private int[] epE = null;

/**
 * Class constructor
 */
public
TurboMQIncr()
{
}

/**
 * Initialization for the OF Calculator using the data of the Graph passed
 * as parameter.
 *
 * @param g the graph which OF will be calculated
 */
public
void
init(Graph g)
{
  graph_d = g;
  numberOfNodes_d = g.getNumberOfNodes();
  nodes_x = g.getNodes();
  //clusters_x = g.getClusters();
  //clusterMatrix_d = new int[numberOfNodes_d][numberOfNodes_d+1];

  //if (clusterMatrix_d == null)
  //  clusterMatrix_d = new int[numberOfNodes_d][numberOfNodes_d+1];

  //for (int i=0; i<clusterMatrix_d.length; ++i) {
  //  clusterMatrix_d[i][0] = 0;
  //}
}

public double calculate(Cluster c)
{
  //if(clusters_x == null)
  //  clusters_x = c.getClusterNames();

  //remove this
  //c.allocEdgeCounters();
  //-----------------------------

  //return calcAll(c);

  if(c.isMoveValid() == false)
  {
    if(clusters_x == null)
      clusters_x = c.getClusterNames();
    return calcAll(c);
  }
  else
    return calcIncr(c,c.getLmEncoding());

/*******
  muE = c.getMuEdgeVector();
  epE = c.getEpsilonEdgeVector();
  int []lastMv = c.getLmEncoding();

  if((lastMv[0] == -1)||(lastMv[1] == -1) || (lastMv[2] == -1))
    return calcAll(c);

  if ((muE == null) || (epE == null))
      return calcAll(c);

  return calcIncr(c,lastMv);
****/

  //calculate();
  //return graph_d.getObjectiveFunctionValue();
}

private double calcAll(Cluster c)
{
//System.out.println("Doing the full calc");
  c.allocEdgeCounters();
  muE = c.getMuEdgeVector();
  epE = c.getEpsilonEdgeVector();

  for(int i = 0; i < nodes_x.length; i++)
  {
    Node n = nodes_x[i];

    int [] fe = n.dependencies;
    int [] feW = n.weights;
    int [] be = n.backEdges;
    int [] beW = n.beWeights;
    int [] cv = c.getClusterVector();
    int currentNode = n.nodeID;
    //String nodeName = n.name_d;
    int currentNodeCluster = cv[currentNode];
    n.cluster = cv[n.nodeID];

    for(int j = 0; j < fe.length; j++)
    {
      int target = fe[j];
      int targetCluster = cv[target];

      if (targetCluster == currentNodeCluster)
        muE[currentNodeCluster]++;
      else
        epE[currentNodeCluster]++;
    }
  }
  double MQ = 0.0;
  for(int k = 0; k < clusters_x.length; k++)
  {
    int currentCluster = clusters_x[k];
    double dMuE = (double)muE[currentCluster];
    double dEpE = (double)epE[currentCluster];

    if ((dMuE+dEpE)>0)
    {
      MQ += (dMuE / (dMuE + dEpE));
      //MQ += CFk;
    }
  }
//System.out.println("Returning MQ = " + MQ);
  return MQ;
}

private double calcIncr(Cluster c, int[]lastMv)
{
  //int []lastMv = c.getLmEncoding();

  muE = c.getMuEdgeVector();
  epE = c.getEpsilonEdgeVector();
  double lastObjFn = c.getLastMvObjFn();

  int currentNode = lastMv[0];
  int lmOrigC     = lastMv[1];
  int lmNewC      = lastMv[2];

  double CFiOrig  = calcCFi(lmOrigC);
  double CFiNew   = calcCFi(lmNewC);

  Node n = nodes_x[currentNode];

  int [] fe = n.dependencies;
  int [] feW = n.weights;
  int [] be = n.backEdges;
  int [] beW = n.beWeights;
  int [] cv = c.getClusterVector();
  //int currentNode = n.nodeID;
    //String nodeName = n.name_d;
  int currentNodeCluster = cv[currentNode];
  n.cluster = cv[n.nodeID];

  for(int j = 0; j < fe.length; j++)
  {
    int target = fe[j];
    int targetCluster = cv[target];  //cluster of edge

    Node x = nodes_x[target];
    String nname = x.getName();
    int nid = n.nodeID;

    if(targetCluster == lmNewC)
    {
        muE[lmNewC]++;
        epE[lmOrigC]--;
    }else if(targetCluster == lmOrigC)
    {
        muE[lmOrigC]--;
        epE[lmNewC]++;
    }
    else
    {
        epE[lmOrigC]--;
        epE[lmNewC]++;
    }
  }
  //---- now the back edges

  for(int j = 0; j < be.length; j++)
  {
    int target = be[j];
    int targetCluster = cv[target];  //cluster of edge

    if(targetCluster == lmNewC)
    {
        muE[lmNewC]++;
        epE[lmNewC]--;
    }else if(targetCluster == lmOrigC)
    {
        muE[lmOrigC]--;
        epE[lmOrigC]++;
    }else
    {
        //epE[lmOrigC]--;
        //epE[lmNewC]++;
    }
  }

 //now the updated MQ
  double newCFiOrig  = calcCFi(lmOrigC);
  double newCFiNew   = calcCFi(lmNewC);

  double MQ = c.getLastMvObjFn();

  MQ = MQ - CFiOrig - CFiNew + newCFiOrig + newCFiNew;

  //System.out.println("MQ is: " + MQ);
  return MQ;
}

private double calcCFi(int c)
{
  double dMuE = (double)muE[c];
  double dEpE = (double)epE[c];

  if ((dMuE+dEpE)>0)
      return (dMuE / (dMuE + dEpE));
  else
      return 0;
}

/**
 * Calculate the objective function value for the graph passed in the
 * #init(bunch.Graph) method.
 */
public
void
calculate()
{
  int k=0;
  double intra=0.0;
  double inter=0.0;
  double objTalley = 0.0;

  clusters_x = graph_d.getClusters();

  if (clusterMatrix_d.length != numberOfNodes_d)
    clusterMatrix_d = null;
  if (clusterMatrix_d == null)
    clusterMatrix_d = new int[numberOfNodes_d][numberOfNodes_d+1];

  for (int i=0; i<numberOfNodes_d; ++i) {
    clusterMatrix_d[i][0] = 0;
    nodes_x[i].cluster = -1;
  }

  int pos=0;
  for (int i=0; i<numberOfNodes_d; ++i) {
    int numCluster = clusters_x[i];
    clusterMatrix_d[numCluster][(++clusterMatrix_d[numCluster][0])] = i;
    nodes_x[i].cluster = numCluster;
  }
  double dDebug = graph_d.getObjectiveFunctionValue();

  for (int i=0; i<clusterMatrix_d.length; ++i) {
    if (clusterMatrix_d[i][0] > 0) {
      int[] clust = clusterMatrix_d[i];
      objTalley += calculateIntradependenciesValue(clust, i);
      k++;
    }
  }

  graph_d.setIntradependenciesValue(0);
  graph_d.setInterdependenciesValue(0);
  graph_d.setObjectiveFunctionValue(objTalley);
}

/**
 * Calculates the intradependencies (intraconnectivity) value for the given cluster
 * of the graph.
 */
public
double
calculateIntradependenciesValue(int[] c, int numCluster)
{
  double intradep=0.0;
  double intraEdges=0.0;
  double interEdges=0.0;
  double exitEdges=0.0;
  int k=0;
  for (int i=1; i<=c[0]; ++i) {
    Node node = nodes_x[c[i]];
    k++;
    int[] c2 = node.dependencies;
    int[] w = node.weights;

    if (c2 != null) {
      for (int j=0; j<c2.length; ++j) {
        if (nodes_x[c2[j]].cluster == numCluster) {
//if (w != null)
//System.out.println("(" + node.getName() + "," + nodes_x[c2[j]].getName() + ") = " + w[j]);
//System.out.println("Edge weight = " + w[j]);
          intradep += w[j];
          intraEdges++;
        }
        else
        {
          exitEdges += w[j];
          interEdges++;
        }
      }
    }
  }

  if ((k==0) || (k == 1))
    k=1;
  else
    k = k * (k-1);

//  System.out.println("Cluster = " + numCluster);
//  System.out.println("Num in Cluster = " + k);
//  System.out.println("IntraEdge Weight = " + intradep);
//  System.out.println("InterEdge Weight = " + exitEdges);

  double objValue = 0;

//  if (exitEdges == 0)
//   objValue = (intraEdges / k);
//  else
//   objValue = (intraEdges / k) * (1 / exitEdges);

   //---------------------------------------------
   //GOOD
   //---------------------------------------------
   if ((exitEdges+intradep) == 0)
      objValue = 0;
   else
      objValue = (intradep / (intradep+exitEdges));
//      objValue = (intraEdges/(intraEdges+interEdges)) * (intradep / (intradep+exitEdges));

//  System.out.println("Obj Cluster Val = " + objValue);
//  System.out.println("***********************************");

  return objValue;
}


/**
 * Calculates the interdependencies (interconnectivity) between to given clusters
 */
public
double
calculateInterdependenciesValue(int[] c1, int[] c2, int nc1, int nc2)
{
  double interdep=0.0;
  for (int i=1; i<=c1[0]; ++i) {
    int[] ca = nodes_x[c1[i]].dependencies;
    int[] w = nodes_x[c1[i]].weights;

    if (ca != null) {
      for (int j=0; j<ca.length; ++j) {
//if (w != null)
//System.out.println("(" + nodes_x[c1[i]].getName() + "," + nodes_x[ca[j]].getName() + ") = " + w[j]);
        if (nodes_x[ca[j]].cluster == nc2) {
          interdep += w[j];
        }
      }
    }
  }

  for (int i=1; i<=c2[0]; ++i) {
    int[] ca = nodes_x[c2[i]].dependencies;
    int[] w = nodes_x[c2[i]].weights;

    if (ca != null) {
      for (int j=0; j<ca.length; ++j) {
//if (w != null)
//System.out.println("(" + nodes_x[c1[i]].getName() + "," + nodes_x[ca[j]].getName() + ") = " + w[j]);
        if (nodes_x[ca[j]].cluster == nc1) {
          interdep += w[j];
        }
      }
    }
  }
  interdep = ((interdep)/(2.0 * ((double)(c1[0])) * ((double)(c2[0]))));
  return interdep;
}
}
