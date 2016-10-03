/****
 *
 *	$Log: Configuration.java,v $
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

import java.io.*;

/**
 * A generic configuration class for clustering methods. This basic class
 * includes parameters for a population size, the number of iterations for
 * the algorithm and three sets of "Features" for pre/post and processing
 * during the algorithm. Features are intended to extend an algorithm in
 * generic form. This class is also Serializable so it can easily be saved
 * or transmitted over the network if necessary.
 *
 * @author Brian Mitchell
 *
 * @see bunch.Feature
 * @see bunch.GAConfiguration
 * @see bunch.HillClimbingConfiguration
 */
public
class Configuration
  implements java.io.Serializable
{
Feature[] preFeatures_d;
Feature[] features_d;
Feature[] postFeatures_d;
int numIterations_d;
int popSize_d;
public int expNumber_d = 0;
public boolean runBatch_d = false;
BufferedWriter writer_d = null;

/**
 * Class constructor.
 */
public
Configuration()
{
}

/**
 * Initializes this configuration object based on the characteristics of
 * the graph passed as parameter. Intended for redefinition by subclasses.
 *
 * @param g the graph used to generate a default configuration
 */
public
void
init(Graph g)
{
}

/**
 * This method is used to create a log file.  The name is hard coded for now
 * as this feature is mainly for debugging.
 */
public void createLogFile() throws Exception
{
	writer_d = new BufferedWriter(new FileWriter("c:\\bunch.log"));
}

/**
 * This method closes the log file.
 */
public void closeLogFile() throws Exception
{
	writer_d.close();
}

/**
 * Sets the maximum number of iterations to be performed by the clustering method
 *
 * @param n the number of iterations
 * @see #getNumOfIterations()
 */
public
void
setNumOfIterations(int n)
{
  numIterations_d = n;
}

/**
 * Obtains the maximum number of iterations to be performed by the clustering method
 *
 * @return the number of iterations
 * @see #setNumOfIterations(int)
 */
public
int
getNumOfIterations()
{
  return numIterations_d;
}


/**
 * Sets the population size to be used by the clustering method
 *
 * @param p the population's size
 * @see #getPopulationSize()
 */
public
void
setPopulationSize(int p)
{
  popSize_d = p;
}

/**
 * Obtains the population size to be used by the clustering method
 *
 * @return population size
 * @see #setPopulationSize(int)
 */
public
int
getPopulationSize()
{
  return popSize_d;
}

/**
 * Obtains the array of pre-condition features for this configuration.
 *
 * @return the array of precondition features
 * @see #setPreConditionFeatures(bunch.Feature[])
 */
public
Feature[]
getPreConditionFeatures()
{
  return preFeatures_d;
}

/**
 * Sets the array of pre-condition features for this configuration.
 *
 * @param p the array of precondition features
 * @see #getPreConditionFeatures()
 */
public
void
setPreConditionFeatures(Feature[] p)
{
  preFeatures_d=p;
}

/**
 * Obtains the array of features that are executed along with the
 * clustering algorithm for this configuration.
 *
 * @return the array of features
 * @see #setFeatures(bunch.Feature[])
 */
public
Feature[]
getFeatures()
{
  return features_d;
}

/**
 * Sets the array of features that are executed along with the
 * clustering algorithm for this configuration.
 *
 * @param f the array of features
 * @see #getFeatures()
 */
public
void
setFeatures(Feature[] f)
{
  features_d=f;
}

/**
 * Obtains the array of post-condition features for this configuration.
 *
 * @return the array of postcondition features
 * @see #setPostConditionFeatures(bunch.Feature[])
 */
public
Feature[]
getPostConditionFeatures()
{
  return postFeatures_d;
}

/**
 * Sets the array of post-condition features for this configuration.
 *
 * @param p the array of postcondition features
 * @see #getPostConditionFeatures()
 */
public
void
setPostConditionFeatures(Feature[] p)
{
  postFeatures_d=p;
}
}
