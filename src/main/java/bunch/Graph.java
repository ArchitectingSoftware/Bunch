/****
 *
 *	$Log: Graph.java,v $
 *	Revision 3.0  2002/02/03 18:41:51  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *
 *	Revision 3.5  2000/11/26 15:48:13  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.4  2000/10/22 17:30:06  bsmitc
 *	Fixed bug with user-directed clustering. Also, added support to clear
 *	the user-directed clustering option once it is selected
 *
 *	Revision 3.3  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.2  2000/08/11 13:19:10  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.1  2000/08/02 21:40:53  bsmitc
 *	Added support for calculator feature
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

import java.util.Random;

/**
 * A class that contains a source code graph and all the data relevant to
 * Bunch to perform a clustering operation. This data includes nodes (with all
 * the relevant information) and an array that defines to which cluster
 * each Node belongs.<P>
 * NOTE: to create new copies of a graph only to change the cluster information,
 * use the #cloneGraph method provided, which does not copy the node information
 * but only creates a pointer to it.<P>
 *
 * @author Brian Mitchell
 * @version 1.0
 * @see bunch.Node
 */
public
class Graph
    implements java.io.Serializable
{

private Node[] nodes_d;
private Node[] originalNodes_d;
private int[] clusters_d;
private boolean[] locked_d;
private boolean hasLocks_d = false;
private boolean isMaximum_d=false;
private int graphLevel_d = 0;
private boolean isClusterTree_d = false;

private Graph previousLevelGraph_d;

private transient double intradependenciesValue_d;
private transient double interdependenciesValue_d;
private transient double objectiveFunctionValue_d;

private transient Random random_d;

transient ObjectiveFunctionCalculator calculator_d=null;

public static ObjectiveFunctionCalculatorFactory objectiveFunctionCalculatorFactory_sd;

/**
 * Creates an empty Graph. Must call initGraph() later to be able to use this\
 * Graph instance.
 *
 * @see #initGraph(int)
 */
public
Graph()
{
  random_d = new Random(System.currentTimeMillis());
}

private void checkRandomOK()
{
   if(random_d == null)
      random_d = new Random(System.currentTimeMillis());
}

/**
 * Creates a graph with the specified number of nodes by calling initGraph()
 *
 * @param nodes the number of nodes this graph will contain
 * @see #initGraph(int)
 */
public
Graph(int nodes)
{
  this();
  initGraph(nodes);
}

/**
 * This method sets the objective function calculator factory, that will
 * be used later to create new calculators based on a given name. This
 * method must be called before any instance of the Graph class is created.
 *
 * @param of the function calculator factory to set
 * @see #setObjectiveFunctionCalculator(java.lang.String)
 */
public static
void
setObjectiveFunctionCalculatorFactory(ObjectiveFunctionCalculatorFactory of)
{
  objectiveFunctionCalculatorFactory_sd = of;
}

/**
 * Initialized this  graph with the specified number of nodes
 * (calls #clear() to ensure
 * that the Graph is correctly initialized.)
 *
 * @param nodes the number of nodes of the graph
 */
public
void
initGraph(int nodes)
{
  nodes_d = new Node[nodes];
  clusters_d = new int[nodes];
  locked_d = new boolean[nodes];
}

/**
 * Sets the objective function calculator for this graph using the name passed
 * as parameter. The calculator will be obtained from the calculator factory
 * that should have been set before using any Graph class
 *
 * @param name the name of the calculator to set for this graph
 * @see #setObjectiveFunctionCalculatorFactory(ObjectiveFunctionCalculatorFactory)
 */
public
void
setObjectiveFunctionCalculator(String name)
{
  calculator_d = objectiveFunctionCalculatorFactory_sd.getCalculator(name);
  calculator_d.init(this);
}

public ObjectiveFunctionCalculator
getObjectiveFunctionCalculator()
{
  return calculator_d;
}

/**
 * clears a Graph by resetting all the cluster identifiers to -1 and
 * allocating new Node objects for each position in the node array.
 */
public
void
clear()
{
  for (int i=0; i<nodes_d.length; ++i) {
    nodes_d[i] = new Node();
    clusters_d[i] = -1;
    locked_d[i] = false;
    setDoubleLocks(false);
  }
}

/**
 * Clear the nodes that are locked
 */
public void resetNodeLocks()
{
  for (int i=0; i<nodes_d.length; ++i) {
    nodes_d[i].resetNode();
  }
  setDoubleLocks(false);

  boolean locks[] = this.getLocks();

  for(int i = 0; i < locks.length; i++)
    locks[i] = false;

  setLocks(locks);
}

/**
 * Obtain the number of nodes in this graph
 *
 * @return the number of nodes of the graph
 */
public
int
getNumberOfNodes()
{
  return nodes_d.length;
}

/**
 * Obtain the original nodes for this graph in array form.
 *
 * @return the array of nodes.
 * @see #setOriginalNodes(Node[])
 */
public
Node[]
getOriginalNodes()
{
  return originalNodes_d;
}

/**
 * Set the original nodes for this graph in array form. This array will contain
 * the original graph, i.e., prior to pre-processing tasks such as omnipresent
 * module detection
 *
 * @param nodes the array of nodes
 * @see #getOriginalNodes()
 */
public
void
setOriginalNodes(Node[] nodes)
{
  originalNodes_d = nodes;
}

/**
 * Obtain the nodes for this graph in array form.
 * @return the array of nodes.
 *
 * @see #setNodes(Node[])
 */
public
Node[]
getNodes()
{
  return nodes_d;
}

/**
 * Set the nodes for this graph in array form.
 * @param nodes the array of nodes
 *
 * @see #getNodes()
 */
public
void
setNodes(Node[] nodes)
{
  nodes_d = nodes;
}

/**
 * Obtain the clusters for this graph in array form.
 *
 * @return the array of cluster identifiers.
 * @see #setClusters(int[])
 */
public
int[]
getClusters()
{
  return clusters_d;
}

/**
 * Set the clusters for this graph in array form.
 *
 * @param clusters the array of clusters.
 * @see #getClusters()
 */
public
void
setClusters(int[] clusters)
{
  clusters_d = clusters;
}

/**
 * Set the locked clusters for this graph as an array
 * of boolean values. For each position, if the value is true
 * the cluster at that position in the cluster array is considered
 * locked, or unlocked if the value is false
 *
 * @param locked the array of locks
 * @see #getLocks()
 */
public
void
setLocks(boolean[] locked)
{
  locked_d = locked;
}

/**
 * Obtain the cluster locks for this graph in array form.
 *
 * @return the array of cluster locks
 * @see #setLocks(boolean[])
 */
public
boolean[]
getLocks()
{
  return locked_d;
}

/**
 * Creates a clone of the current graph by allocating a new array for the
 * clusters, initializing it (every value to -1) and then referring the
 * node array to the node array in the originating instance. This method
 * is useful to save both space and execution time when creating different
 * partitions of the same graph.
 *
 * @return a new Graph object
 */
public
Graph
cloneGraph()
{
  Graph g = new Graph();
  g.nodes_d = this.nodes_d;
  g.clusters_d = new int[nodes_d.length];
  g.originalNodes_d = this.originalNodes_d;
  g.locked_d = new boolean[nodes_d.length];
  g.intradependenciesValue_d = this.intradependenciesValue_d;
  g.interdependenciesValue_d = this.interdependenciesValue_d;
  g.objectiveFunctionValue_d = this.objectiveFunctionValue_d;
  System.arraycopy(clusters_d, 0, g.clusters_d, 0, clusters_d.length);
  System.arraycopy(locked_d, 0, g.locked_d, 0, locked_d.length);
  g.previousLevelGraph_d = this.previousLevelGraph_d;
  g.graphLevel_d = this.graphLevel_d;
  g.setDoubleLocks(hasDoubleLocks());
  g.random_d = this.random_d;
  g.isClusterTree_d = this.isClusterTree_d;
  g.checkRandomOK();
  return g;
}

/**
 * Obtains the objective function value for this Graph, which must have been
 * previously calculated by calling calculateObjectiveFunctionValue().
 *
 * @return the objective function value
 * @see #calculateObjectiveFunctionValue()
 */
public
double
getObjectiveFunctionValue()
{
  return objectiveFunctionValue_d;
}

/**
 * Sets the objective function value for this Graph. Mainly intended for
 * "internal" use by the class and the OF Calculator Object used by the
 * class.
 *
 * @param the new objective function value for this graph
 * @see #getObjectiveFunctionValue()
 */
public
void
setObjectiveFunctionValue(double objVal)
{
  objectiveFunctionValue_d = objVal;
}


/**
 * Obtains the interdependencies value for this Graph, which must have been
 * previously calculated by calling calculateObjectiveFunctionValue().
 *
 * @return the interdependencies value
 * @see #getObjectiveFunctionValue()
 */
public
double
getInterdependenciesValue()
{
  return interdependenciesValue_d;
}

/**
 * Sets the interdependencies value for this Graph. Mainly intended for
 * "internal" use by the class and the OF Calculator Object used by the
 * class.
 *
 * @return the interdependencies value
 * @see #getInterdependenciesValue()
 */
public
void
setInterdependenciesValue(double inter)
{
  interdependenciesValue_d = inter;
}

/**
 * Obtains the intradependencies value for this Graph, which must have been
 * previously calculated by calling calculateObjectiveFunctionValue().
 *
 * @return the intradependencies value
 * @see #calculateObjectiveFunctionValue()
 * @see #getObjectiveFunctionValue()
 */
public
double
getIntradependenciesValue()
{
  return intradependenciesValue_d;
}

/**
 * Sets the intradependencies value for this Graph. Mainly intended for
 * "internal" use by the class and the OF Calculator Object used by the
 * class.
 *
 * @return the intradependencies value
 * @see #getIntradependenciesValue()
 */
public
void
setIntradependenciesValue(double intra)
{
  intradependenciesValue_d = intra;
}

/**
 * Calculates the Objective Function value for this partitioned graph
 * by calling its ObjectiveFunctionCalculator.
 */
public
void
calculateObjectiveFunctionValue()
{
    if (calculator_d == null) {
      setObjectiveFunctionCalculator(objectiveFunctionCalculatorFactory_sd.getCurrentCalculator());
    }
    //calculator_d.calculate();
    Cluster c = new Cluster(this,this.getClusters());
    c.calcObjFn();
    this.setObjectiveFunctionValue(c.getObjFnValue());
}

/**
 * Obtains the total number of partitions (1..N-partitions) of a set
 * of N elements for the current graph
 *
 * @return the number of partitions
 */
public
long
getNumberOfPartitions()
{
  long p = 0;

  for (int i=1; i <= nodes_d.length; i++) {
    long ip = calcStirling(nodes_d.length,i);
    p += ip;
  }

  return p;
}

/**
 * Computes the number of K-partitions of a set of N elements using a
 * recurrence relation for computing Stirling numbers
 */
private
long
calcStirling(int n, int k)
{
  if (k == 1)
    return 1;
  else if (n == k)
    return 1;
  else
    return calcStirling(n-1, k-1) + k * calcStirling(n-1, k);
}

/**
 * Creates a clone of the current graph that includes all nodes in
 * one cluster.
 *
 * @return the new graph
 */
public
Graph
cloneAllNodesCluster()
{
    Graph g = cloneGraph();
    if (g.hasDoubleLocks()) {
      int num = g.findFreeCluster(g.getClusterNames());
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          g.clusters_d[i] = num;
        }
      }
    }
    else {
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          g.clusters_d[i] = 0;
        }
      }
    }
    return g;
}

/**
 * Finds a cluster number that has not yet been used. The "used" clusters
 * list is passed as parameter. This method will not return an appropriate
 * value if all the clusters have already been used. Therefore, calls
 * to this method must be preceded by checking if the array of "used"
 * clusters equals in length to the number of nodes, in which case the
 * method must not be called.
 *
 * @param c the array of "used" clusters
 * @return a cluster number that is not yet in use
 */
public
int
findFreeCluster(int[] c)
{
  int n=0;
  boolean change=true;
  while (change) {
    change = false;
    for (int i=0; i<c.length; ++i) {
      if (c[i] == n) {
        n++;
        change = true;
        break;
      }
    }
  }
  return n;
}

/**
 * Finds a random cluster number that has not yet been used. The "used" clusters
 * list is passed as parameter. This method will not return an appropriate
 * value if all the clusters have already been used. Therefore, calls
 * to this method must be preceded by checking if the array of "used"
 * clusters equals in length to the number of nodes, in which case the
 * method must not be called. This method is NOT guaranteed to return
 * a free cluster in all cases, since it relies on random number
 * generation, and should be used carefully.
 *
 * @param c the array of "used" clusters
 * @return a cluster random number that is not yet in use
 */
public
int
findFreeRandomCluster(int[] c)
{
  checkRandomOK();
  int n=(int)(random_d.nextFloat()*(clusters_d.length-1));
  int loops = 0;
  boolean change=true;
  while (change && (loops++ < (clusters_d.length*2))) {
    change = false;
    for (int i=0; i<c.length; ++i) {
      if (c[i] == n) {
        n=(int)(random_d.nextFloat()*(clusters_d.length-1));
        change = true;
        break;
      }
    }
  }
  return n;
}

/**
 * Creates a clone of the current graph that includes each node in
 * a cluster by itself.
 *
 * @return the new graph
 */
public
Graph
cloneSingleNodeClusters()
{
    Graph g = cloneGraph();

    if (g.hasDoubleLocks()) {
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          int num = g.findFreeCluster(g.getClusterNames());
          g.clusters_d[i] = num;
        }
      }
    }
    else {
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          g.clusters_d[i] = i;
        }
      }
    }
    return g;
}

/**
 * Defines the current graph clusters as "double locked" i.e., nodes cannot
 * leave OR enter the clusters already defined when the parameter is true.
 *
 * @param a boolean defining is the graph is "double locked" or not
 * @see #hasDoubleLocks()
 */
public
void
setDoubleLocks(boolean v)
{
  hasLocks_d = v;
}

/**
 * Used to know if the current graph has "double locked" clusters, i.e., where nodes cannot
 * leave OR enter the clusters already defined.
 *
 * @return a boolean defining is the graph is "double locked" or not
 * @see #setDoubleLocks(boolean)
 */
public
boolean
hasDoubleLocks()
{
  return hasLocks_d;
}

/**
 * "Shuffles" the clusters of this graph instance, by randomly rearranging the
 * already existent clusters (i.e., this would not be useful when all the nodes
 * are in a single cluster). Used to generate random partitions that are
 * better-performing that purely-random ones.
 */
public
void
shuffleClusters()
{
    int[] clustNames = null;
    if (hasDoubleLocks()) {
      clustNames = this.getUnlockedClusterNames();
    }
    else {
      clustNames = this.getClusterNames();
    }
    if (clustNames == null || clustNames.length == 0) {
      return;
    }
    for (int i=0; i<clusters_d.length; ++i) {
        if ((Math.random() > 0.6) && (!locked_d[i]))
            clusters_d[i] = clustNames[(int)(Math.random()*(clustNames.length-1))];
    }
}

/**
 * Creates a clone of the current graph by including each node in
 * a random cluster.
 *
 * @return the new graph
 */
public
Graph
cloneWithRandomClusters()
{
   checkRandomOK();
    Graph g = cloneGraph();
    if (g.hasDoubleLocks()) {
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          g.clusters_d[i] = g.findFreeRandomCluster(g.getClusterNames());
        }
      }
    }
    else {
      for (int i=0; i<g.clusters_d.length; ++i) {
        if (!g.locked_d[i]) {
          g.clusters_d[i] = (int)(random_d.nextFloat()*(g.clusters_d.length-1));
        }
      }
    }
    return g;
}

/**
 * This method builds and returns a random cluster.  The cluster is encoded into
 * an integer array, where each index indicates the node.  Thus n[0] would have
 * the value of the cluster for node zero.
 */
public int[] getRandomCluster()
{
   checkRandomOK();
   int [] c = new int[nodes_d.length];

    if (hasDoubleLocks()) {
      for (int i=0; i<clusters_d.length; ++i) {
        if (!locked_d[i]) {
          c[i] = findFreeRandomCluster(getClusterNames());
        }
      }
    }
    else {
      for (int i=0; i<clusters_d.length; ++i) {
        if (!locked_d[i]) {
          c[i] = (int)(random_d.nextFloat()*(clusters_d.length-1));
        }
      }
    }
    return c;
}


/**
 * Generate a random cluster, taking into account special or ''locked'' clusters
 */
public int[] genRandomClusterSize()
{
   checkRandomOK();
   int [] c = new int[nodes_d.length];
   int [] existingClusters = getClusters();
   int numNodes = clusters_d.length;

    int nodeCount = 0;
    for (int i=0; i<clusters_d.length; ++i) {
      if (!locked_d[i]) {
        c[i] = nodeCount;
        nodeCount++;
      }
      else
        c[i] = existingClusters[i];
    }
    numNodes = nodeCount;

    int numClusters = ((int)(random_d.nextFloat()*(clusters_d.length-1)))+1;
    int clustSize = numNodes/numClusters;
    int remainder = numNodes%numClusters;

    int currC = 0;
    int currNode = 0;
    while(currC < numClusters)
    {
      int currCSize = 0;
      while(currCSize < clustSize)
      {
        if(!locked_d[currNode])
        {
          currCSize++;
          c[currNode]=currC;
        }
        currNode++;
      }
      currC++;
    }

    //now handle the remainders
    if(currNode < clusters_d.length)
    {
      //init
      int []clustStack = new int[numClusters];

      for(int i = 0; i < clustStack.length;i++)
        clustStack[i] = i;

      //now randomize
      for(int i = 0; i < clustStack.length;i++)
      {
        int pos1 = (int)(random_d.nextFloat()*(clustStack.length-1));
        int pos2 = (int)(random_d.nextFloat()*(clustStack.length-1));
        int tmp = clustStack[pos1];
        clustStack[pos1] = clustStack[pos2];
        clustStack[pos2] = tmp;
      }

      //now assign the cluster numbers
      int stackIdx = 0;
      for(int i = currNode; i < clusters_d.length;i++)
      {
        if(!locked_d[i])
          c[i] = clustStack[stackIdx++];
      }
    }

    //now randomize
    for (int i=0; i<clusters_d.length; ++i) {
      int pos1 = (int)(random_d.nextFloat()*(clusters_d.length-1));
      int pos2 = (int)(random_d.nextFloat()*(clusters_d.length-1));

      if ((!locked_d[pos1])&&(!locked_d[pos2])) {
        int tmp = c[pos1];
        c[pos1] = c[pos2];
        c[pos2] = tmp;
      }
    }
    return c;
}


/**
 * Generate a random cluster, taking into account special or ''locked'' clusters
 */
public int[] genRandomClusterSizeWithLimits(int min, int max)
{
   int range = max-min;

   if (range < 0) return null;

   checkRandomOK();
   int [] c = new int[nodes_d.length];
   int [] existingClusters = getClusters();
   int numNodes = clusters_d.length;

    int nodeCount = 0;
    for (int i=0; i<clusters_d.length; ++i) {
      if (!locked_d[i]) {
        c[i] = nodeCount;
        nodeCount++;
      }
      else
        c[i] = existingClusters[i];
    }
    numNodes = nodeCount;

    int numClusters = ((int)((random_d.nextFloat()*(range-1)))+1+min);
    int clustSize = numNodes/numClusters;
    int remainder = numNodes%numClusters;

    int currC = 0;
    int currNode = 0;
    while(currC < numClusters)
    {
      int currCSize = 0;
      while(currCSize < clustSize)
      {
        if(!locked_d[currNode])
        {
          currCSize++;
          c[currNode]=currC;
        }
        currNode++;
      }
      currC++;
    }

    //now handle the remainders
    if(currNode < clusters_d.length)
    {
      //init
      int []clustStack = new int[numClusters];

      for(int i = 0; i < clustStack.length;i++)
        clustStack[i] = i;

      //now randomize
      for(int i = 0; i < clustStack.length;i++)
      {
        int pos1 = (int)(random_d.nextFloat()*(clustStack.length-1));
        int pos2 = (int)(random_d.nextFloat()*(clustStack.length-1));
        int tmp = clustStack[pos1];
        clustStack[pos1] = clustStack[pos2];
        clustStack[pos2] = tmp;
      }

      //now assign the cluster numbers
      int stackIdx = 0;
      for(int i = currNode; i < clusters_d.length;i++)
      {
        if(!locked_d[i])
          c[i] = clustStack[stackIdx++];
      }
    }

    //now randomize
    for (int i=0; i<clusters_d.length; ++i) {
      int pos1 = (int)(random_d.nextFloat()*(clusters_d.length-1));
      int pos2 = (int)(random_d.nextFloat()*(clusters_d.length-1));

      if ((!locked_d[pos1])&&(!locked_d[pos2])) {
        int tmp = c[pos1];
        c[pos1] = c[pos2];
        c[pos2] = tmp;
      }
    }
    return c;
}

/**
 * Sets the random number generator to be used by this graph instance.
 *
 * @param r the random number generator
 * @see #getRandom()
 */
public
void
setRandom(Random r)
{
    random_d = r;
}

/**
 * Obtains the random number generator to be used by this graph instance.
 *
 * @return the random number generator
 * @see #setRandom(java.util.Random)
 */
public
Random
getRandom()
{
   checkRandomOK();
    return random_d;
}

/**
 * Obtains the cluster names for this partitioned graph in an array
 *
 * @return the array of cluster "names" (numeric ids, actually)
 */
public
int[]
getClusterNames()
{
    int[] clusts = new int[nodes_d.length];
    int name;
    int numClusts = 0;
    for (int i=0; i<clusters_d.length; ++i) {
        name = clusters_d[i];
        int j=0;
        for (j=0; j<numClusts; ++j) {
            if (clusts[j] == name)
                break;
        }
        if (j == numClusts) {
            clusts[j] = name;
            numClusts++;
        }
    }
    int[] tmp = new int[numClusts];
    System.arraycopy(clusts, 0, tmp, 0, numClusts);

    return tmp;
}

/**
 * Obtains the unlocked cluster names for this partitioned graph in an array.
 * Unlocked clusters are those that have been generated *after* the graph was
 * defined as "double locked" in the space that was available
 *
 * @return the array of cluster "names" (numeric ids, actually)
 * @see #setDoubleLocks(boolean)
 */
public
int[]
getUnlockedClusterNames()
{
    int[] clusts = new int[nodes_d.length];
    int name;
    int numClusts = 0;
    for (int i=0; i<clusters_d.length; ++i) {
      if (locked_d[i]) {
        continue;
      }
      name = clusters_d[i];
      int j=0;
      for (j=0; j<numClusts; ++j) {
        if (clusts[j] == name)
          break;
      }
      if (j == numClusts) {
        clusts[j] = name;
        numClusts++;
      }
    }
    int[] tmp = new int[numClusts];
    System.arraycopy(clusts, 0, tmp, 0, numClusts);

    return tmp;
}

/**
 * Returns whether this graph is at a "Local Maximum" or not.
 * true if the current partition does not have a neighbor with
 * a better objective function value.
 *
 * @return true if the graph is a local maximum, false otherwise
 */
public
boolean
isMaximum()
{
  return isMaximum_d;
}

/**
 * Sets whether this graph is at a "Maximum" or not.
 *
 * @param b, which should true if the graph is a local maximum, false otherwise
 */
public
void
setMaximum(boolean b)
{
  isMaximum_d=b;
}

/**
 * Sets the previous level for this graph. Each previous level contains
 * the nodes used for the current graph as clusters. When there is no
 * previous level (level 0) the nodes are the original nodes and the
 * clusters are the basic clusters found. Graphs with level above one
 * can be printed by the GraphOutput classes as graphs were the lower-level
 * clusters appear nested inside them
 *
 * @return the previous level graph
 * @see #getPreviousLevelGraph()
 * @see #setGraphLevel(int)
 */
public
void
setPreviousLevelGraph(Graph g)
{
  previousLevelGraph_d = g;
}

/**
 * Obtains the previous graph (level) for this graph instance.
 *
 * @return the previous level graph
 * @see #setPreviousLevelGraph(bunch.Graph)
 * @see #setGraphLevel(int)
 */
public
Graph
getPreviousLevelGraph()
{
  return previousLevelGraph_d;
}

/**
 * Defines the level of this graph (as a numeric value).
 *
 * @param gl the level as numeric value (i.e., level 0 is the initial partitioned graph)
 * @see #getGraphLevel()
 */
public
void
setGraphLevel(int gl)
{
  graphLevel_d = gl;
}

/**
 * Obtains the level of this graph (as a numeric value).
 *
 * @return the level as numeric value (i.e., level 0 is the initial partitioned graph)
 * @see #setGraphLevel(int)
 */
public
int
getGraphLevel()
{
  return graphLevel_d;
}

/**
 * This method returns if the current graph contains the actual nodes, or if it
 * contains clusters, which is a level up on the tree.  If it is a tree, then
 * the nodes have children and so on.
 */
public boolean isClusterTree()
{ return this.isClusterTree_d; }

public void setIsClusterTree(boolean b)
{ isClusterTree_d = b;  }

/**
 * If the cluster is a tree, find the median level of the tree and return
 * the graph object to the caller.
 */
public Graph getMedianTree()
{
  if (isClusterTree() == false)
    return this;

  int lvl = this.getGraphLevel();
  int medLevel = Math.max((lvl/2),0);
  Graph tmpGraph = this;
  while(tmpGraph.getGraphLevel()>medLevel)
    tmpGraph = tmpGraph.getPreviousLevelGraph();

  return tmpGraph;
}

/**
 * This method always returns the level-0 graph.  This may be this instance
 * itself, or one of its children graphs.
 */
public Graph getDetailedGraph()
{
  int lvl = this.getGraphLevel();

  Graph tmpGraph = this;
  while(tmpGraph.getGraphLevel()>0)
    tmpGraph = tmpGraph.getPreviousLevelGraph();

  return tmpGraph;
}

}

