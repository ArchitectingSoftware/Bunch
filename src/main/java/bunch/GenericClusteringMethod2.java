/****
 *
 *	$Log: GenericClusteringMethod2.java,v $
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

/**
 * This class is a refactoring in progress.  It supports the same base interface
 * as ClusteringMethod2, which means that it implments support for using
 * the Cluster objects to manage MDG partitions.
 *
 * @author  Brian Mitchell
 */
public abstract class GenericClusteringMethod2
  extends ClusteringMethod2
{

public static int DEFAULT_NUM_EXPERIMENTS = 200;
public static int DEFAULT_POP_SIZE = 25;
public static double DEFAULT_THRESHOLD = 0.1;

protected Population currentPopulation_d;
protected int popSize_d=DEFAULT_POP_SIZE;

protected int numExperiments_d=DEFAULT_NUM_EXPERIMENTS;
protected double threshold_d=DEFAULT_THRESHOLD;
protected double bestOFValue_d=0.0;

/**
 * Class constructor.
 */
public
GenericClusteringMethod2()
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
   currentPopulation_d = new Population(getGraph());
   currentPopulation_d.genPopulation(getPopSize());

   if (getBestCluster() == null) {
      setBestCluster(currentPopulation_d.getCluster(0).cloneCluster());
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

/**
 * Used to reinitialize the clustering method.  May be overriden in the
 * subclasses
 */
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
  Cluster c2 = currentPopulation_d.getCluster(0);

  if (c2.getObjFnValue() > getBestCluster().getObjFnValue()) {
    setBestCluster(c2);
  }

  long t = System.currentTimeMillis();
  IterationEvent ev = new IterationEvent(this);
  bestOFValue_d = getBestCluster().getObjFnValue();


  for (int x=0; x<numExperiments_d; x++)
  {
    //maximize the current population and check for new maximum
    boolean end = nextGeneration();

    if (bestOFValue_d != getBestCluster().getObjFnValue())
    {
      setBestObjectiveFunctionValue(getBestCluster().getObjFnValue());
      generationsSinceLastChange = x;
    }

    if (end)
    {
      if ((x-generationsSinceLastChange) > (numExperiments_d*getThreshold()))
      {
        break;
      }
      else
      {
        ev.setIteration(x-generationsSinceLastChange);
        ev.setOverallIteration(x);
        fireIterationEvent(ev);
        reInit();
      }
    }
    else
    {
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

