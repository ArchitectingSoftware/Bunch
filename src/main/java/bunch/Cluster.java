/****
 *
 *	$Log: Cluster.java,v $
 *	Revision 3.0  2002/02/03 18:41:45  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *
 *	Revision 3.2  2000/10/22 15:48:48  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.1  2000/08/16 00:12:45  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.Hashtable;
import java.util.ArrayList;
import bunch.stats.*;

/**
 * This class manages clusters, or partitioned instances of MDG graphs.
 *
 * @author Brian Mitchell
 */
 public class Cluster {

 /**
  * Constants
  */
  public static final double CLUSTER_OBJ_FN_VAL_NOT_DEFINED = -999.0;

  /**
   * Member variables
   */
  int [] clusterVector = null;
  int [] epsilonEdges = null;
  int [] muEdges = null;
  int [] lastMv = new int[3];
  double objFnValue=CLUSTER_OBJ_FN_VAL_NOT_DEFINED;
  Graph graph = null;
  boolean converged = false;
  boolean validMove = false;

  int [] clusterNames = null;
  int [] clustersUsed = null;
  boolean clusterNamesDirty = true;
  int numClustNames = -1;
  boolean clusterNamesChanged = false;
  double baseObjFnValue = CLUSTER_OBJ_FN_VAL_NOT_DEFINED;
  long   numMQEvaluations = 0;
  int    baseNumClusters = 0;
  Cluster baseCluster = null;

  //---------------------------------------------------
  //the following properties
  //track the last "move" so that it can be rolled back
  //without a lot of calculation overhead
  int lastMoveNode = -1;
  int lastMoveOrigCluster = -1;
  int lastMoveNewCluster = -1;
  double lastMoveObjectiveFnValue = 0.0;

  int pushNode = -1;
  int pushCluster = -1;
  double pushObjectiveFnValue = 0.0;

  boolean isDirty = true;
  long     depth = 0;
  ArrayList cDetails = null;
  //-----------------------------------------------------

  ObjectiveFunctionCalculator calculator = null;
  StatsManager stats = StatsManager.getInstance();

  /**
   * This method is the constructor and it initializes the move stack.
   */
  public Cluster() {
    lastMv[0]=lastMv[1]=lastMv[2] = -1;
    depth = 0;
    if(stats.getCollectClusteringDetails())
      cDetails = new ArrayList();
  }

  /**
   * This alternative constructor contains a graph and a cluster vector object
   * on its interface to initialize this instance of the Cluster object.
   */
  public Cluster(Graph g, int []cv)
  {
      lastMv[0]=lastMv[1]=lastMv[2] = -1;
      graph = g;
      setClusterVector(cv);
      initCalculator();
      if(stats.getCollectClusteringDetails())
        cDetails = new ArrayList();

      baseObjFnValue = getObjFnValue();
      baseNumClusters = getNumClusters();
      baseCluster = cloneCluster();
  }

  public Cluster getBaseCluster()
  {
    return baseCluster;
  }

  public int getBaseNumClusters()
  { return baseNumClusters; }

  public double getBaseObjFnValue()
  { return baseObjFnValue;  }

  public long getNumMQEvaluations()
  { return numMQEvaluations; }

  public ArrayList getClusteringDetails()
  { return cDetails; }
  /**
   * Returns the current depth of the cluster.  The depth is the number of times
   * that the cluster has been updated.
   */
  public long getDepth()
  {   return depth; }

  /**
   * This method is used to increment the depth of the cluster.
   */
  public void incrDepth()
  {
    depth++;

    if((cDetails != null) && (stats.getCollectClusteringDetails()))
      cDetails.add(new Double(this.objFnValue));
  }

  /**
   * This method is used to return the number of elements in the MDG.
   */
  public int size()
  {   return clusterVector.length;  }

  /**
   * This method returns the cluster membership for a given node.
   *
   * @param node The index of the node of interest.
   */
  public int getCluster(int node)
  {   return clusterVector[node]; }


  /**
   * This method invalidates the last move.  Thus the history is forgotton
   * causing the MQ of the entire cluster to be recalculated.
   */
  private void invalidateLastMove()
  {
    lastMoveNode = -1;
    lastMoveOrigCluster = -1;
    lastMoveNewCluster = -1;
    epsilonEdges = null;
    muEdges = null;
  }

  /**
   * This method allocates vectors to keep track of the inter- and intra-edges
   * with respect to each cluster.
   */
  public void allocEdgeCounters()
  {
      if (clusterVector == null)
        return;

      epsilonEdges = new int[clusterVector.length];
      muEdges = new int[clusterVector.length];

      for(int i = 0; i < clusterVector.length; i++)
      { epsilonEdges[i] = muEdges[i] = 0; }
  }

  /**
   * This method returns the array of epsilon (inter) edges for the current
   * partition of the MDG.
   */
  public int[] getEpsilonEdgeVector()
  {
    return epsilonEdges;
  }

  /**
   * This method sets the array of epsilon (inter) edges for the current
   * partition of the MDG.
   */
  public void setEpsillonEdgeVector(int [] ev)
  {
    epsilonEdges = ev;
  }

  /**
   * This method gets the mu (intra) edges for the current partition of the
   * MDG.
   */
  public int[] getMuEdgeVector()
  {
    return muEdges;
  }

  /**
   * This method sets the mu (intra) edges for the current partition of the
   * MDG.
   */
  public void setMuEdgeVector(int [] ev)
  {
    muEdges = ev;
  }

  /**
   * This method performs initialization on the Objective Function calculator.
   */
  private boolean initCalculator()
  {
    if (graph == null)
      return false;

    /**
     * Get the calculator from the factory.
     */
    calculator = Graph.objectiveFunctionCalculatorFactory_sd.getSelectedCalculator();
    if (calculator == null)
      return false;

    calculator.init(graph);
    return true;
  }

  /**
   * This method returns the instance of the objective function calculator to
   * the caller.
   *
   * @returns The object instance of the objective function calculator.
   */
  public ObjectiveFunctionCalculator getCalculator()
  {   return calculator;  }


  /**
   * This method indicates if the cluster "is dirty".  If it is the objective
   * function value might be out-of-sync with the cluster vector.
   *
   * @returns True if the cluster is dirty, false otherwise
   */
  public boolean isDirty()
  { return this.isDirty;  }

  /**
   * This method sets the cluster vector for the cluster object.
   *
   * @param cv The cluster vectory array
   */
  public void setClusterVector(int [] cv)
  {
      /**
       * Initialize with the cluster vector, and reset all of the
       * tracking statistics associated with the previous cluster vector
       */
      this.invalidateLastMove();
      isDirty = true;
      clusterVector = new int[cv.length];
      System.arraycopy(cv,0,clusterVector,0,cv.length);

      epsilonEdges = null;
      muEdges = null;
      lastMv = new int[3];
      converged = false;
      validMove = false;

      lastMoveNode = -1;
      lastMoveOrigCluster = -1;
      lastMoveNewCluster = -1;
      lastMoveObjectiveFnValue = 0.0;
      numClustNames = -1;

      pushNode = -1;
      pushCluster = -1;
      pushObjectiveFnValue = 0.0;

      isDirty = true;

      if (graph != null)
         calcObjFn();
  }

  /**
   * This method updates the objective function value.
   */
  public void setObjFnValue(double o)
  {
      objFnValue = o;
  }

  /**
   * This method gets teh objective function value.
   */
  public double getObjFnValue()
  {
      return objFnValue;
  }

  /**
   * This method returns the current cluster vector.
   */
  public final int[] getClusterVector()
  {
      return clusterVector;
  }

  /**
   * This method is used to "force" the entire recalculation of the objective
   * function for the current partition of the MDG.
   */
  public void force()
  {
    isDirty = true;
    validMove = false;
    calcObjFn();
  }

  /**
   * This method calculates the objective function value using the objective
   * function factory.  If the current cluster is not dirty, the previously
   * cached value is returned.  If the cluster is dirty then the MQ
   * function is called.
   */
  public double calcObjFn()
  {
      stats.incrMQCalculations();
      numMQEvaluations++;

       if (graph == null)
         return CLUSTER_OBJ_FN_VAL_NOT_DEFINED;


      if (isDirty == false)
        return objFnValue;

      if (calculator == null)
          initCalculator();

      if (validMove == false)
          invalidateLastMove();

      int [] cltemp = graph.getClusters();

      double objfn = calculator.calculate(this);

      setObjFnValue(objfn);

      isDirty = false;

      return objfn;
  }

  /**
   * This method is used to copy a new cluster vector into the current
   * cluster vector
   *
   * @param cv The cluster vector that will overwrite the previous one.
   */
  private void copy(int [] cv)
  {
      this.setClusterVector(cv);
  }

  /**
   * This method is used to determine if the cluster has converged.
   *
   * @returns True if the cluster has converged, false otherwise.
   */
  public boolean isMaximum()
  {   return converged; }

  /**
   * This method is used to set the state of the cluster instance to indicate
   * that the cluster has converged.
   *
   * @param state True if the cluster has converged, false otherwise.
   */
  public void setConverged(boolean state)
  {   converged = state;   }

  /**
   * This method is used to get an instance of the Graph object that the
   * cluster is "wrapping".  The graph object contains methods to navigate
   * the graph.
   *
   * @returns The instance to the graph object.
   */
  public Graph getGraph()
  {   return graph;  }

  /**
   * This method is used to determine if another cluster is equal to the
   * current cluser.  Equality is defined by this method to agree on 4
   * decimal places.  We need this to ensure that floating point round off
   * errors dont impact the comparisons.
   *
   * @returns True if equal, false if not.
   */
  public boolean isEqualTo(Cluster c)
  {
    double otherMQ = c.getObjFnValue();

    int iMQ = (int)(objFnValue*10000.0);
    int iMQ2 = (int)(otherMQ*10000.0);

    return (iMQ == iMQ2);
  }

  /**
   * This method is used to determine if another cluster is grater then the
   * current cluser.  Greater than, like equals is defined by this method
   * to agree on 4 decimal places.  We need this to ensure that floating
   * point round off errors dont impact the comparisons.
   *
   * @returns True if equal, false if not.
   */
  public boolean isGreaterThan(Cluster c)
  {
    double otherMQ = c.getObjFnValue();

    int iMQ = (int)(objFnValue*10000.0);
    int iMQ2 = (int)(otherMQ*10000.0);

    return (iMQ > iMQ2);
  }

  /**
   * This method allows the state of the current cluster instance to be
   * copied from another cluster instance.
   *
   * @param c The other cluster for which the current clusters state will
   *          be modeled.
   */
  private void setFromCluster(Cluster c)
  {
      if(c.getClusterVector()==null)
        clusterVector = null;
      else
      {
        clusterVector = new int[c.getClusterVector().length];
        System.arraycopy(c.getClusterVector(),0,clusterVector,0,clusterVector.length);
      }

      objFnValue=c.getObjFnValue();
      graph = c.getGraph();
      converged = c.isMaximum();
      calculator = c.getCalculator();
      isDirty = c.isDirty; //c.isDirty();
      depth = c.depth;
      baseObjFnValue = c.baseObjFnValue;
      numMQEvaluations = c.numMQEvaluations;
      baseNumClusters = c.baseNumClusters;
      baseCluster = c.baseCluster;

      if(c.cDetails != null)
        cDetails = (ArrayList)c.cDetails.clone();

      converged = c.converged;
      validMove = c.validMove;

      if((c.epsilonEdges == null)||(c.muEdges==null))
      {
        epsilonEdges = muEdges = null;
      }
      else
      {
        epsilonEdges = new int[c.epsilonEdges.length];
        muEdges = new int[c.muEdges.length];
        System.arraycopy(c.epsilonEdges,0,this.epsilonEdges,0,this.epsilonEdges.length);
        System.arraycopy(c.muEdges,0,this.muEdges,0,this.muEdges.length);
      }

      lastMv = new int[3];
      System.arraycopy(c.lastMv,0,this.lastMv,0,this.lastMv.length);

      lastMoveNode = c.lastMoveNode;
      lastMoveOrigCluster = c.lastMoveOrigCluster;
      lastMoveNewCluster = c.lastMoveNewCluster;
      lastMoveObjectiveFnValue = c.lastMoveObjectiveFnValue;

      pushNode = c.pushNode;
      pushCluster = c.pushCluster;
      pushObjectiveFnValue = c.pushObjectiveFnValue;

      numClustNames = c.numClustNames;
  }

  /**
   * This method is used to clone the current cluster, producing a new
   * cluster which is returned as output from this method.
   *
   * @returns The cloned cluster of the current cluster object.
   */
  public Cluster cloneCluster()
  {
      Cluster c = new Cluster();
      c.setFromCluster(this);
      return c;
  }

  /**
   * This method allows the state of the current cluster instance to be
   * copied from another cluster instance.
   *
   * @param c The other cluster for which the current clusters state will
   *          be modeled.
   */
  public void copyFromCluster(Cluster c)
  {
      setFromCluster(c);
  }

  /**
   * This method returns the number of clusters for the current partition of the
   * MDG
   */
  public int getNumClusters()
  {
    if (numClustNames == -1) return 0;
    else return numClustNames;
  }

  /**
   * This method returns an array containing the list of locked (i.e., can not
   * be changed clusters.  For each element in the returned array, the value
   * will be true if the node is locked, indicating that its cluster membership
   * can not be changed.  If the indicator is false then the cluster can be
   * relocated.
   *
   * @returns An array of nodes indicating what clusters are locked.
   */
  public boolean[] getLocks()
  {
      return graph.getLocks();
  }

  /**
   * This method is used to create a new cluster id.  When a new cluster is
   * created it must be assigned an unique id.
   *
   * @returns A cluser ID not already in use by another cluster.
   */
  public int findNewClusterID()
  {
    int [] clusterNames = getClusterNames();
    int [] tmpVector = new int[clusterVector.length];
    for(int i = 0; i < clusterVector.length; i++)
      tmpVector[i] = i;

    for(int i = 0; i < clusterNames.length; i++)
      tmpVector[clusterNames[i]] = -1;

    int newClusterID = -1;
    for(int i = 0; i < clusterVector.length; i++)
    {
      if(tmpVector[i] != -1) {
        newClusterID = i;
        break;
      }
    }
    return newClusterID;
  }

  /**
   * This method creates a new cluser ID and flages that the cluster names
   * have changed so that appropriate downstream processing can happen.
   *
   * @returns A cluser ID not already in use by another cluster.
   */
  public int allocateNewCluster()
  {
    int newClusterID = findNewClusterID();
    this.clusterNamesChanged = true;
    return newClusterID;
  }

  /**
   * This method moves a node from its current cluster to a new cluster.
   * The node and cluster are represented by thier associated identifier.
   *
   * @param nodeID        The ID of the node to be moved
   * @param newClusterID  The ID of the cluster to which the node is moved
   */
  public boolean moveNodeToNewCluster(int nodeID, int newClusterID)
  {
    this.clusterNamesChanged = true;
    return this.relocate(nodeID,newClusterID);
  }

  /**
   * This method deletes a cluster from the current partition.
   *
   * @param newClusterID  The ID of the cluster to delete
   */
  public boolean removeNewCluster(int newClusterID)
  {
    this.clusterNamesChanged = true;
    return true;
  }

  /**
   * This method tracks if cluster names have changed.  That is new clusters
   * were added or deleted from the partition of the MDG.
   *
   * @returns True if the cluster names have changed, false if not.
   */
  public boolean hasClusterNamesChanged()
  { return clusterNamesChanged; }

  /**
   * This method returns an integer array containing the identifies for all
   * of the valid clusters.
   *
   * @returns An array containing the names (actuall ID's) of the clusters in
   *          the partition of the MDG.
   */
  public int[] getClusterNames()
  {
     Hashtable usedClusts = new Hashtable();

     int[] clusts = new int[clusterVector.length];
     int name;
     int numClusts = 0;
     boolean hasDoubleLocks = graph.hasDoubleLocks();
     boolean [] locks = graph.getLocks();

     /**
      * Dont count the locked clusters (the ones with special modules)
      */
     for (int i=0; i<clusterVector.length; ++i) {
       if (hasDoubleLocks)
         if(locks[i]) continue;

       name = clusterVector[i];
       Integer iNm = new Integer(name);

       if(usedClusts.containsKey(iNm)==false)
       {
           clusts[numClusts] = name;
           numClusts++;
           usedClusts.put(iNm,iNm);
       }
     }
     int[] tmp = new int[numClusts];
     System.arraycopy(clusts, 0, tmp, 0, numClusts);

     numClustNames = numClusts;
     clusterNames = tmp;

     this.clusterNamesChanged = false;
     return tmp;
  }

  /**
   * As nodes are relocated into different clusters, the MQ value changes.  This
   * method returns the objective function value that was the MQ value of the
   * partition prior to the update of the clusters.
   *
   * @returns An the objective function value of the partition prior to the last
   *          move operation.
   */
  public double getLastMvObjFn()
  { return lastMoveObjectiveFnValue;  }

  /**
   * This method returns an array encoding that represents the last move
   * taken by the current partition of the MDG.  The index for the array are:
   *
   *    0.  The node that was moved
   *    1.  The origional cluster
   *    2.  The new cluster
   *
   * This information enables the last move to be "undone" if necessary.
   *
   * @returns A 3 member array with the information necessary to "rollback" the
   *          last move.
   */
  public int[] getLmEncoding()
  {
    lastMv[0]=lastMoveNode;
    lastMv[1]=lastMoveOrigCluster;
    lastMv[2]=lastMoveNewCluster;
    return lastMv;
  }

  /**
   * This method relocates a node from one cluster to a new cluster.  It is
   * basically a move operation.
   *
   * @param node      The ID of the node to be moved
   * @param cluster   The ID of the cluster for the moved node
   *
   * @returns True if the relocation was OK, false if not.
   */
  public boolean relocate(int node, int cluster)
  {
    int currentCluster = clusterVector[node];
    if(currentCluster != cluster)
      return move(node,currentCluster,cluster);

    return true;
  }

  /**
   * This method move's a node from its current cluster to a new cluster.
   *
   * @param node          The ID of the node to be moved
   * @param origCluster   The ID of the cluster for the node prior to the move
   * @param newCluster    The ID of the cluster for the node after the move
   *
   * @returns True if the relocation was OK, false if not.
   */
  public boolean move(int node, int origCluster, int newCluster)
  {
      /**
       * Panic check.  The node is not in its expected cluster.
       */
      if(clusterVector[node] != origCluster)
      {
        System.out.println("This is bad");
        return false;
      }

      //save the last move information for quick rollback
      lastMoveNode = node;
      lastMoveOrigCluster = origCluster;
      lastMoveNewCluster = newCluster;
      lastMoveObjectiveFnValue = getObjFnValue();

      //now make the move
      clusterVector[node] = newCluster;

      isDirty = true;
      validMove = true;
        calcObjFn();
      validMove = false;
      isDirty = false;

      return true;
  }

  /**
   * This method dertermines if the specified last move was valid.
   */
  public boolean isMoveValid()
  { return validMove; }

}