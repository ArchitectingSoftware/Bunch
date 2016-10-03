/****
 *
 *	$Log: GenericClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:50  bsmitc
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

import java.util.*;
import javax.swing.*;

/**
 * A generic clustering method class, intended to provide common services
 * to both hill-climbing and genetic algorithm optimization methods.
 * The class basically takes charge of running each generation, calling the
 * listeners when necessary, etc, requiring that the subclasses define
 * the #nextGeneration() method.
 *
 * @author Brian Mitchell
 *
 * @see bunch.GAClusteringMethod
 * @see bunch.GenericHillClimbingClusteringMethod
 */
public abstract
class GenericClusteringMethod
  extends ClusteringMethod
{

public static int DEFAULT_NUM_EXPERIMENTS = 200;
public static int DEFAULT_POP_SIZE = 25;
public static double DEFAULT_THRESHOLD = 0.1;

protected Graph[] currentPopulation_d;
protected int popSize_d=DEFAULT_POP_SIZE;

protected int numExperiments_d=DEFAULT_NUM_EXPERIMENTS;
protected double threshold_d=DEFAULT_THRESHOLD;
protected double bestOFValue_d=0.0;

/**
 * Class constructor.
 */
public
GenericClusteringMethod()
{
    setPopSize(DEFAULT_POP_SIZE);
    setThreshold(DEFAULT_THRESHOLD);
    setNumOfExperiments(DEFAULT_NUM_EXPERIMENTS);
}

/**
 * Generic initialization
 */
public
void
init()
{
  Graph graph = getGraph().cloneGraph();
  graph.getRandom().setSeed(System.currentTimeMillis());
  if (getBestGraph() == null) {
    setBestGraph(graph.cloneWithRandomClusters());
  }

  currentPopulation_d = new Graph[getPopSize()];

  for (int i=0; i<getPopSize(); ++i)
  {
    currentPopulation_d[i] = graph.cloneWithRandomClusters();
    currentPopulation_d[i].calculateObjectiveFunctionValue();
    currentPopulation_d[i].setMaximum(false);
  }

  /*
   * UNCOMMENT THE FOLLOWING BLOCK OF CODE IF YOU WANT TO PRELOAD ALL INITIAL
   * POPULATIONS WITH TWO POPULATION MEMBERS.  THE FIRST MEMBER CONTAINING
   * A SINGLE CLUSTER WITH ALL NODES.  THE SECOND MEMBER CONTAINING N CLUSTERS
   * EACH CONTAINING A SINGLE NODE.
   *
   *
  currentPopulation_d[0] = currentPopulation_d[0].cloneAllNodesCluster();
  currentPopulation_d[0].calculateObjectiveFunctionValue();

  if (getPopSize() >= 2)
  {
      currentPopulation_d[1] = currentPopulation_d[0].cloneSingleNodeClusters();
      currentPopulation_d[1].calculateObjectiveFunctionValue();
  }
  */
}

public
void
reInit()
{
}


/**
 * Redefinition of the main method for a clustering method.
 */
public
void
run()
{
  init();

  int generationsSinceLastChange = 0;

  //try the "all nodes in one cluster" partition
  Graph g2 = currentPopulation_d[0].cloneAllNodesCluster();
  g2.calculateObjectiveFunctionValue();

  if (g2.getObjectiveFunctionValue() > getBestGraph().getObjectiveFunctionValue()) {
    setBestGraph(g2);
  }

  //try the "each node in one cluster" partition
  Graph g3 = currentPopulation_d[0].cloneSingleNodeClusters();
  g3.calculateObjectiveFunctionValue();

  if (g3.getObjectiveFunctionValue() > getBestGraph().getObjectiveFunctionValue()) {
    setBestGraph(g3);
  }

  long t = System.currentTimeMillis();
  IterationEvent ev = new IterationEvent(this);
  bestOFValue_d = getBestGraph().getObjectiveFunctionValue();


  for (int x=0; x<numExperiments_d; x++)
  {
    //maximize the current population and check for new maximum
    boolean end = nextGeneration();

    if (bestOFValue_d != getBestGraph().getObjectiveFunctionValue()) {
        setBestObjectiveFunctionValue(getBestGraph().getObjectiveFunctionValue());
        generationsSinceLastChange = x;
    }

    if (end) {
      if ((x-generationsSinceLastChange) > (numExperiments_d*getThreshold())) {
        break;
      }
      else {
        ev.setIteration(x-generationsSinceLastChange);
        ev.setOverallIteration(x);
        fireIterationEvent(ev);
        reInit();
      }
    }
    else {
      ev.setIteration(x);
      ev.setOverallIteration(x);
      fireIterationEvent(ev);
    }

    setElapsedTime((((double)(System.currentTimeMillis()-t))/1000.0));
  }

  ev.setIteration(getMaxIterations());
  ev.setOverallIteration(getMaxIterations());
  this.fireIterationEvent(ev);
	setElapsedTime((((double)(System.currentTimeMillis()-t))/1000.0));
}

/**
 * Method that must be defined by subclasses. This method is called once
 * per each iteration of the main "for" loop in the #run() method.
 */
public abstract
boolean
nextGeneration();

/**
 * Define the threshold that determines when no further improvement can be
 * expected. This threshold is a percentage of the total number of
 * generations the algorithm will run. If that percentage of generations
 * has elapsed without change in the best graph found, the algorithm is
 * considered finished.
 *
 * @param t the threshold percentage expressed as a real value
 * @see #getThreshold()
 */
public
void
setThreshold(double t)
{
    threshold_d = t;
}

/**
 * Obtain the threshold that determines when no further improvement can be
 * expected.
 *
 * @return the threshold percentage expressed as a real (double) value
 * @see #setThreshold(double)
 */
public
double
getThreshold()
{
    return threshold_d;
}

/**
 * Returns the maximum number of iterations to run until the threshold
 * is crossed.
 *
 * @return the threshold as the number of iterations
 * @see #getNumOfExperiments()
 * @see #getThreshold()
 */
public
int
getMaxIterations()
{
  return (int)(getNumOfExperiments()*getThreshold());
}

/**
 * Sets the overall maximum number of experiments to perform before
 * stopping.
 *
 * @param max the maximum number of experiments to run
 * @see #getNumOfExperiments()
 */
public
void
setNumOfExperiments(int max)
{
  numExperiments_d = max;
}

/**
 * Obtains the overall maximum number of experiments to perform before
 * stopping.
 *
 * @return the maximum number of experiments to run set for this clustering method
 * @see #setNumOfExperiments(int)
 */
public
int
getNumOfExperiments()
{
  return numExperiments_d;
}

/**
 * Sets the population size used for this clustering method.
 *
 * @param psz the population size set for this clustering method
 * @see #getPopSize()
 */
public
void
setPopSize(int psz)
{
  popSize_d = psz;
}

/**
 * Obtains the population size used for this clustering method.
 *
 * @return the population size set for this clustering method
 * @see #setPopSize(int)
 */
public
int
getPopSize()
{
  return popSize_d;
}

/**
 * Sets the objective function value of the best graph found so far
 *
 * @param v the best OF value
 * @see #getBestObjectiveFunctionValue()
 */
public
void
setBestObjectiveFunctionValue(double v)
{
  bestOFValue_d = v;
}

/**
 * Obtains the objective function value of the best graph found so far
 *
 * @return the best OF value
 * @see #setBestObjectiveFunctionValue(double)
 */
public
double
getBestObjectiveFunctionValue()
{
  return bestOFValue_d;
}
}
