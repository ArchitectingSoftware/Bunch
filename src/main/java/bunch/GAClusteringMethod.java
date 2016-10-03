/****
 *
 *	$Log: GAClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:48  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2001/03/17 14:55:23  bsmitc
 *	Added label to DotGraphOutput, Changed inheritance tree for GA method
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
 * A clustering method based on a genetic algorithm. This implementation relies
 * on the bunch.GAMethod interface for actual GA processing. This is necessary
 * because there can be different GA implementations (for example, with
 * different selection mechanisms such as roulette wheel, or tournament)
 * and the basic algorithm does not change but the specifics do. Those specifics
 * are implemented in each GAMethod subclass.
 *
 * @author Brian Mitchell
 *
 * @see bunch.GenericClusteringMethod
 * @see bunch.GAMethod
 */
public
class GAClusteringMethod
  extends GenericClusteringMethod
{
GAConfiguration config_d;
GAMethod method_d;
Feature[] preFeatures_d;
Feature[] features_d;
Feature[] postFeatures_d;

/**
 * Class constructor.
 */
public
GAClusteringMethod()
{
  setConfigurable(true);
  setThreshold(1.0);
}

/**
 * Get the confiruation parameter telling us how many interations to perform
 * at maximum
 */
public
int
getMaxIterations()
{
  return getConfiguration().getNumOfIterations();
}

/**
 * initializes the clustering method based on the input graph characteristics
 */
public
void
init()
{
  setPopSize(getConfiguration().getPopulationSize());
  setNumOfExperiments(getConfiguration().getNumOfIterations());
  config_d = (GAConfiguration)getConfiguration();
  method_d = config_d.getMethod();

  Graph graph = getGraph().cloneGraph();
  method_d.setRandomNumberGenerator(new Random(System.currentTimeMillis()));
  method_d.setBestGraph(graph.cloneWithRandomClusters());
  method_d.getBestGraph().calculateObjectiveFunctionValue();

  currentPopulation_d = new Graph[getPopSize()];

  for (int i=0; i<getPopSize(); ++i) {
    currentPopulation_d[i] = graph.cloneWithRandomClusters();
    currentPopulation_d[i].shuffleClusters();
    currentPopulation_d[i].calculateObjectiveFunctionValue();

    if (currentPopulation_d[i].getObjectiveFunctionValue()
        > getBestGraph().getObjectiveFunctionValue()) {
      setBestGraph(currentPopulation_d[i].cloneGraph());
    }
  }

  currentPopulation_d[0] = currentPopulation_d[0].cloneAllNodesCluster();
  currentPopulation_d[0].calculateObjectiveFunctionValue();

  if (getPopSize() >= 2)
  {
      currentPopulation_d[1] = currentPopulation_d[0].cloneSingleNodeClusters();
      currentPopulation_d[1].calculateObjectiveFunctionValue();
  }

  method_d.setPopulation(currentPopulation_d);
  method_d.setMutationThreshold(config_d.getMutationThreshold());
  method_d.setCrossoverThreshold(config_d.getCrossoverThreshold());
  method_d.init();

  preFeatures_d = config_d.getPreConditionFeatures();
  features_d = config_d.getFeatures();
  postFeatures_d = config_d.getPostConditionFeatures();
}

/**
 * Redefinition of the setBestGraph method
 */
public
void
setBestGraph(Graph g)
{
  if (method_d != null)
    method_d.setBestGraph(g);
}

/**
 * Return the best cluster from this clustering method.  This method
 * is required to be implemented in order to support the superclass
 */
public Cluster getBestCluster()
{
  Graph bestG = getBestGraph();
  Cluster c = new Cluster(bestG,bestG.getClusters());
  c.calcObjFn();
  return c;
}

/**
 * This returns the best graph
 */
public
Graph
getBestGraph()
{
 return method_d.getBestGraph();
}

/**
 * This is the main code for the GA. newGeneration()
 */
public
boolean
nextGeneration()
{
  method_d.calcStatistics();

  int parent1=0, parent2=0;
  int crossp=0;

  int n = method_d.getInitCounter();
  int incr = method_d.getIncrementCounter();
  int top = method_d.getMaxCounter();

  if (preFeatures_d != null) {
    for (int i=0; i<preFeatures_d.length; ++i) {
      preFeatures_d[i].apply(currentPopulation_d);
    }
  }

  while (n < top) {
    method_d.selectReproduceCrossAndMutate(n);

    if (features_d != null) {
      for (int i=0; i<features_d.length; ++i) {
        features_d[i].apply(method_d);
      }
    }

    n+=incr;
  }

  method_d.shakePopulation();

  method_d.finishGeneration();

  currentPopulation_d = method_d.getCurrentPopulation();

  if (postFeatures_d != null) {
    for (int i=0; i<postFeatures_d.length; ++i) {
      postFeatures_d[i].apply(currentPopulation_d);
    }
  }

  method_d.getRandomNumberGenerator().setSeed(System.currentTimeMillis());

  return false;
}

/**
 * This returns the configuration dialog name
 */
public
String
getConfigurationDialogName()
{
  return "bunch.GAClusteringConfigurationDialog";
}

/**
 * This method returns the configuration object to the caller
 */
public
Configuration
getConfiguration()
{
  if (configuration_d == null) {
    configuration_d = new GAConfiguration();
  }
  return configuration_d;
}
}
