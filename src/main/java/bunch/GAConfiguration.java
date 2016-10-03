/****
 *
 *	$Log: GAConfiguration.java,v $
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

/**
 * A subclass of Configuration with specific parameters used by the Genetic
 * Algorithm clustering method.
 *
 * @author Brian Mitchell
 *
 * @see bunch.GAClusteringConfigurationDialog
 * @see bunch.GAClusteringMethod
 */
public
class GAConfiguration
  extends Configuration
{
GAMethod method_d;
double mutationThreshold_d;
double crossoverThreshold_d;
GAMethodFactory methodFactory_d;

/**
 * Parameterless class constructor.
 */
public
GAConfiguration()
{
  methodFactory_d = new GAMethodFactory();
}

/**
 * Utility class constructor that receives a graph, and calls #init(bunch.Graph).
 *
 * @param g the graph used to set the default values
 * @see #init(bunch.Graph)
 */
public
GAConfiguration(Graph g)
{
  init(g);
}

/**
 * Initializes this GAConfiguration object with values appropriate
 * to the characteristics of the graph passed as parameter.
 *
 * @param g the graph that will be used to create the default values for the
 * configuration object
 */
public
void
init(Graph g)
{
  int nodes = g.getNumberOfNodes();
  setNumOfIterations(nodes * 100);
  setPopulationSize(nodes * 10);
  setCrossoverThreshold(0.6+0.2 * (getPopulationSize()/1000));
  int bitsize=0;
  for (bitsize=0; bitsize<nodes; ++bitsize) {
    double d = Math.pow(2, bitsize);
    if (d > nodes) {
      break;
    }
  }
  setMutationThreshold(0.005 * bitsize);
  setMethod(methodFactory_d.getMethod((String)(methodFactory_d.getAvailableItems().nextElement())));
}

/**
 * Obtains the GAMethod factory being used by this GAConfiguration object.
 * The factory is used to know what type of GAMethods are available and to return
 * an instance of one of them when necessary.
 *
 * @return the ga method factory
 */
public
GAMethodFactory
getMethodFactory()
{
  return methodFactory_d;
}

/**
 * Obtains the GAMethod currently selected for this GAConfiguration
 *
 * @return the ga method selected
 * @see #setMethod(bunch.GAMethod)
 */
public
GAMethod
getMethod()
{
  return method_d;
}

/**
 * Sets the GAMethod currently selected for this GAConfiguration
 *
 * @param m the ga method to set to this configuration instance
 * @see #getMethod()
 */
public
void
setMethod(GAMethod m)
{
  method_d = m;
}

/**
 * Utility method to set the GAMethod currently selected for this GAConfiguration passing
 * the name of the class. The name of the method will be used to obtain
 * a corresponding GAMethod instance by calling GAMethodFactory.
 *
 * @param m the ga method to set to this configuration instance
 * @see #getMethod()
 * @see #setMethod(bunch.GAMethod)
 */
public
void
setMethod(String m)
{
  setMethod(methodFactory_d.getMethod(m));
}

/**
 * Sets the mutation threshold for this configuration object, expressed in chance of
 * a mutation ocurring. (e.g., 0.004 is a chance of 4 in a thousand)
 *
 * @param t the mutation threshold
 * @see #getMutationThreshold()
 */
public
void
setMutationThreshold(double m)
{
  mutationThreshold_d = m;
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
  return mutationThreshold_d;
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
setCrossoverThreshold(double c)
{
  crossoverThreshold_d = c;
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
}

