/****
 *
 *	$Log: GAMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:49  bsmitc
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

import java.util.Random;

/**
 * A superclass for all specific Genetic Algorithm implementations. A GAMethod
 * subclass will basically define the #selectReproduceCrossAndMutate(int) method
 * of this class for the specific mechanisms required by that method.
 * Also, each GAMethod subclass can redefine the counter minimum, maximum and
 * increment values so the iterations of the GA are performed according to the
 * method's needs.
 *
 * @author  Brian Mitchell
 */
public abstract
class GAMethod
{
protected int initCounter_d=0, maxCounter_d=0, increment_d=1;
protected Random randomGenerator_d;
protected Graph[] currentPopulation_d, newPopulation_d;
protected Graph bestGraph_d;
protected double averageOFValue_d = 0.0, sumOFValue_d=0.0;
protected double crossoverThreshold_d;
protected float mutationThreshold_d, mutCounter_d=0;
protected double[] fitnessArray_d;
protected double maxOFValue_d=0.0, minOFValue_d=1.0;

public
GAMethod()
{
}

/**
 * Sets the random number generator for this method. This is important since
 * GA's rely heavily in random numbers for different stages of processing
 * (e.g., crossover, mutation). Random number generators usually will be resetted
 * every few iterations to ensure more "randomness".
 *
 * @param rgen the new random number generator
 * @see #getRandomNumberGenerator()
 */
public
void
setRandomNumberGenerator(Random rgen)
{
  randomGenerator_d = rgen;
}

/**
 * Obtain the random number generator for this GA method.
 *
 * @return the random number generator used by this method
 * @see #setRandomNumberGenerator(java.util.Random)
 */
public
Random
getRandomNumberGenerator()
{
  return randomGenerator_d;
}

/**
 * Sets the population to be used by this method. The GAClusteringMethod
 * is only aware of one population used by the method.
 */
public
void
setPopulation(Graph[] currPop)
{
  currentPopulation_d = currPop;
  newPopulation_d = new Graph[currentPopulation_d.length];
  fitnessArray_d = new double[currentPopulation_d.length];
  for (int i=0; i<newPopulation_d.length; ++i) {
    newPopulation_d[i] = currentPopulation_d[i].cloneGraph();
  }
}

/**
 * Returns the current population of the GAMethod.
 *
 * @return an array with the population
 * @see #setPopulation(bunch.Graph[])
 */
public
Graph[]
getCurrentPopulation()
{
  return currentPopulation_d;
}

/**
 * Abstract method intended to be used by the GAMethod subclasses
 * to initialize the method prior to processing.
 */
public abstract
void
init();


/**
 * This is the core of the method. Subclasses will define this method
 * implementing the actual processing mechanism for the specific method.
 * The integer received as parameter specifies the iteration number to
 * be processed. Therefore, each GA Generation will make "n" calls to
 * this method to fill the next population.
 *
 * @param pos the current iteration number
 */
public abstract
void
selectReproduceCrossAndMutate(int pos);

public
double
getScaledFitnessSimple(double fit)
{
  return (fit +1.0)/2.0;
}

/**
 * Calculates the statistics for the population (used by GA Optimization Methods)
 * including all fitness values, maximum, minimum and average fitness values for
 * the population.
 */
public
void
calcStatistics()
{
  maxOFValue_d = 0.0;
  minOFValue_d = 1.0;
  sumOFValue_d=0.0;

  for (int i=0; i<currentPopulation_d.length; ++i) {
    currentPopulation_d[i].calculateObjectiveFunctionValue();
    fitnessArray_d[i] = (currentPopulation_d[i].getObjectiveFunctionValue()+1.0)/2.0;
    if (fitnessArray_d[i] > maxOFValue_d) {
      maxOFValue_d = fitnessArray_d[i];
      if (currentPopulation_d[i].getObjectiveFunctionValue()
              > getBestGraph().getObjectiveFunctionValue()) {
        setBestGraph(currentPopulation_d[i].cloneGraph());
      }
    }
    if (minOFValue_d > fitnessArray_d[i]) {
      minOFValue_d = fitnessArray_d[i];
    }
  }

  processFitnessValues();
}

/**
 * Process the calculated fitness values as specifically required by each
 * GA Method. Intended to be redefined by GAMethod subclasses.
 * <P>(Note: not defined as an abstract method to allow subclasses to simply
 * ignore the method).
 */
protected
void
processFitnessValues()
{
}

/**
 * Used to rearrange the population when it has stabilized in a single value
 * Intended to be redefined by GAMethod subclasses.
 * <P>(Note: not defined as an abstract method to allow subclasses to simply
 * ignore the method).
 */
public
void
shakePopulation()
{
}

/**
 * Sets the initial value of the counter for this method. This value will
 * be used by the bunch.GAClusteringMethod.run() method to initialize the
 * internal generation counter for the GA.
 *
 * @param i the initial counter value
 * @see #getInitCounter()
 */
public
void
setInitCounter(int i)
{
  initCounter_d = i;
}

/**
 * Obtains the initial value of the counter for this method.
 *
 * @return the initial counter value
 * @see @setInitCounter(int)
 */
public
int
getInitCounter()
{
  return initCounter_d;
}

/**
 * Sets the maximum value of the counter for this method. This value will
 * be used by the bunch.GAClusteringMethod.run() method as upper bound
 * for the GA's internal generation counter.
 *
 * @param i the maximum counter value
 * @see #getMaxCounter()
 */
public
void
setMaxCounter(int m)
{
  maxCounter_d = m;
}

/**
 * Obtains the maximum value of the counter for this method.
 *
 * @return the maximum counter value
 * @see #setMaxCounter(int)
 */
public
int
getMaxCounter()
{
  return maxCounter_d;
}

/**
 * Obtains the counter increment for this method.
 *
 * @return the counter increment value
 * @see #setIncrementCounter(int)
 */
public
int
getIncrementCounter()
{
  return increment_d;
}

/**
 * Sets the increment value of the counter for this method. This value will
 * be used by the bunch.GAClusteringMethod.run() method to increment the
 * for the GA's internal generation counter each iteration.
 *
 * @param i the counter increment value
 * @see #getIncrementCounter()
 */
public
void
setIncrementCounter(int i)
{
  increment_d = i;
}

/**
 * Obtains the best graph found by the GAMethod so far.
 *
 * @return the best result graph
 * @see #setBestGraph(bunch.Graph)
 */
public
Graph
getBestGraph()
{
  return bestGraph_d;
}

/**
 * Sets the best graph found by the GAMethod so far, storing a copy
 * of the Graph to avoid problems if the best graph found is later
 * modified by the algorithm.
 *
 * @param g the best result graph
 * @see #getBestGraph()
 */
public
void
setBestGraph(Graph g)
{
  bestGraph_d = g != null ? g.cloneGraph() : null;
}

/**
 * Obtain the new population generated by the GA
 *
 * @return an array of graphs
 */
public
Graph[]
getNewPopulation()
{
  return newPopulation_d;
}

/**
 * Sets the value of the summation of all the objective function values for this
 * GAMethod
 *
 * @param val the value of the sum
 * @see #getSumObjectiveFunctionValue()
 */
public
void
setSumObjectiveFunctionValue(double val)
{
  sumOFValue_d = val;
}

/**
 * Obtains the value of the summation of all the objective function values for this
 * GAMethod
 *
 * @return the value of the sum
 * @see #setSumObjectiveFunctionValue(double)
 */
public
double
getSumObjectiveFunctionValue()
{
  return sumOFValue_d;
}

/**
 * Sets the average value of all the objective function values for this
 * GAMethod
 *
 * @param val the average OF value for this method's population
 * @see #getAverageObjectiveFunctionValue()
 */
public
void
setAverageObjectiveFunctionValue(double val)
{
  averageOFValue_d = val;
}

/**
 * Obtains the average value of all the objective function values for this
 * GAMethod
 *
 * @return the average OF value for this method's population
 * @see #setAverageObjectiveFunctionValue(double)
 */
public
double
getAverageObjectiveFunctionValue()
{
  return averageOFValue_d;
}

/**
 * Sets the mutation threshold for this method, expressed in chance of
 * a mutation ocurring. (e.g., 0.004 is a chance of 4 in a thousand)
 *
 * @param t the mutation threshold
 * @see #getMutationThreshold()
 */
public
void
setMutationThreshold(double t)
{
  mutationThreshold_d = (float)t;
}

/**
 * Obtains the mutation threshold for this method
 *
 * @return the mutation threshold
 * @see #setMutationThreshold(double)
 */
public
double
getMutationThreshold()
{
  return (double)mutationThreshold_d;
}

/**
 * Sets the crossover threshold for this method, expressed in chance of
 * a crossover ocurring. (e.g., 0.6 is a 60% chance of a crossover happening)
 *
 * @param t the crossover threshold
 * @see #getCrossoverThreshold()
 */
public
void
setCrossoverThreshold(double t)
{
  crossoverThreshold_d = t;
}

/**
 * Obtains the crossover threshold for this method
 *
 * @return the crossover threshold
 * @see #setCrossoverThreshold(double)
 */
public
double
getCrossoverThreshold()
{
  return crossoverThreshold_d;
}

/**
 * This method is called by the GAClusteringMethod when a generation is finished.
 * by default, it assigns the new population as the current one.
 */
public
void
finishGeneration()
{
  Graph[] tmpPop = currentPopulation_d;
  currentPopulation_d = newPopulation_d;
  newPopulation_d = tmpPop;
}
}
