/****
 *
 *	$Log: ClusteringMethod2.java,v $
 *	Revision 3.0  2002/02/03 18:41:46  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

/**
 * The superclass for all updated clustering methods. A clustering method is (usually) an
 * optimization algorithm that takes a graph as input and produces a partitioned
 * graph as output.
 *
 * NOTE:  This abstract class extends the clusering method class and adds support for
 * working with objects of type cluster as well as type graph.  When all clustering
 * methods can use the newer type Cluster, this class can replace the base Clustering
 * Method class.
 *
 * @author Brian Mitchell
 *
 * @see bunch.NextAscentHillClimbingClusteringMethod
 * @see bunch.SteepestAscentHillClimbingClusteringMethod
 * @see bunch.GAClusteringMethod
 */

public abstract class ClusteringMethod2 extends ClusteringMethod
{
private IterationListener listener_d;
private Graph graph_d;
private Cluster bestCluster_d;
private boolean isConfigurable_d=false;
protected Configuration configuration_d;
protected double elapsedTime_d=0.0;

/**
 * the class constructor
 */
public
ClusteringMethod2()
{
}

/**
 * Initializes the clustering method.
 */
public
void
initialize()
{
  setBestCluster(null);
}

/**
 * Sets the graph to be partitioned by this clustering method
 *
 * @param g the graph to partition
 * @see #getGraph()
 */
public
void
setGraph(Graph g)
{
  graph_d = g;
}

/**
 * Obtains the graph to be partitioned by this clustering method
 *
 * @return the graph
 * @see setGraph(bunch.Graph)
 */
public
Graph
getGraph()
{
  return graph_d;
}

/**
 * Sets the resultant of this clustering method (the best partitioned graph
 * found)
 *
 * @param g the (best) result partitioned cluster
 * @see #getBestGraph()
 */
public
void
setBestCluster(Cluster c)
{
  bestCluster_d = c;
}

/**
 * Obtains the resultant of this clustering method (the best partitioned graph
 * found).  The returned object is a cluster
 *
 * @return the (best) result graph
 * @see setResultGraph(bunch.Graph)
 */
public
Cluster
getBestCluster()
{
  return bestCluster_d;
}

/**
 * Obtains the resultant of this clustering method (the best partitioned graph
 * found).  The returned object is a Graph.
 *
 * @return the (best) result graph
 * @see setResultGraph(bunch.Graph)
 */
public
Graph
getBestGraph()
{
   Cluster best = getBestCluster();
   graph_d.setClusters(best.getClusterVector());
   graph_d.setObjectiveFunctionValue(best.getObjFnValue());
   return graph_d;
}

/**
 * Utility method that returns the objective function value of the best graph
 *
 * @return the objective function value of the best partitioned graph found
 */
public
double
getBestObjectiveFunctionValue()
{
  return bestCluster_d.getObjFnValue();
}

/**
 * Obtains the elapsed time so far for the current clustering process
 *
 * @return the elapsed time
 * @see #setElapsedTime(double)
 */
public
double
getElapsedTime()
{
  return elapsedTime_d;
}

/**
 * Sets the elapsed time so far for the current clustering process (in general,
 * used internally by the clustering algorithm)
 *
 * @param l the elapsed time
 * @see #getElapsedTime()
 */
public
void
setElapsedTime(double l)
{
  elapsedTime_d = l;
}

/**
 * Sets an iteration listener for this clustering method. The listener's
 * "newIteration" method  will be called at every iteration of the
 * partitioning process. This can be used to update a progress bar, for example.
 *
 * @param il the IterationListener for this class
 * @see bunch.IterationListener
 * @see #getIterationListener()
 */
public
void
setIterationListener(IterationListener il)
{
  listener_d = il;
}

/**
 * Obtains the iteration listener set for this clustering method object
 *
 * @return the IterationListener for this object
 * @see bunch.IterationListener
 * @see #setIterationListener(bunch.IterationListener)
 */
public
IterationListener
getIterationListener()
{
  return listener_d;
}

/**
 * Fires an Iteration event to this clustering method's iteration listener
 */
public
void
fireIterationEvent(IterationEvent e)
{
   if (listener_d != null)
   {
      listener_d.newIteration(e);
   }
}

/**
 * Fires an Iteration event to this clustering method's iteration listener
 */
public
void
fireExpermentEvent(IterationEvent e)
{
   if (listener_d != null)
   {
      listener_d.newExperiment(e);
   }
}

/**
 * Obtains the maximum number of iterations this algorithm will perform. Useful
 * to set the parameters for a progress bar, for example
 */
public abstract
int
getMaxIterations();

/**
 * Returns whether or not this clustering method is configurable by an
 * "Options" dialog. In case the method is configurable, it must also
 * specify a class name for the configuration dialog using
 * #getConfigurationDialogName().
 *
 * @return if the method can be configured of not
 * @see #setConfigurable(boolean)
 * @see #getConfigurationDialogName()
 */
public
boolean
isConfigurable()
{
  return isConfigurable_d;
}

/**
 * Defines if this clustering method is configurable by an "options" dialog.
 *
 * @param isC true if the method is configurable, false otherwise
 * @see #getConfigurable()
 * @see #getConfigurationDialogName()
 */
public
void
setConfigurable(boolean isC)
{
  isConfigurable_d = isC;
}

/**
 * Sets the Configuration object for this clustering method. A configuration
 * object contains the information obtained from the configuration dialog.
 * Therefore, this is only used when a method is configurable.
 *
 * @param c the configuration
 * @see #getConfiguration()
 */
public
void
setConfiguration(Configuration c)
{
  configuration_d = c;
}

/**
 * Obtains the configuration object for this clustering method.
 *
 * @return the configuration
 * @see #setConfiguration(bunch.Configuration)
 */
public
Configuration
getConfiguration()
{
  return configuration_d;
}

public
void
setDefaultConfiguration()
{
}

/**
 * Obtains the fully qualified class name (ie. including package) for the
 * dialog class that can configure this clustering method object. This method
 * should be redefined to return the appropriate parameter by subclasses that
 * can be configured.
 *
 * @return a string with the class name for the dialog
 */
public
String
getConfigurationDialogName()
{
  return null;
}
}
