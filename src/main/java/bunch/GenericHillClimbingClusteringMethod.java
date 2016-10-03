/****
 *
 *	$Log: GenericHillClimbingClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:50  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
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
import java.io.*;

/**
 * A generic hill climbing clustering method class, intended to provide common services
 * to both hill-climbing algorithms (next ascent and steepest ascent).
 * The class basically takes charge of executing each generation, leaving to the
 * subclasses the task of performing the actual improvement by defining
 * #getLocalMaxGraph(bunch.Graph) method.
 *
 * @author Brian Mitchell
 *
 * @see bunch.NextAscentHillClimbingClusteringMethod
 * @see bunch.SteepestAscentHillClimbingClusteringMethod
 */
public abstract
class GenericHillClimbingClusteringMethod
  extends GenericClusteringMethod2
{
HillClimbingConfiguration config_d;

/**
 * This method indicates that the default behavior of a generic hill-climbing
 * clustering algorithm is configurable.  This is used to indicate if there
 * is a UI available
 */
public
GenericHillClimbingClusteringMethod()
{
  setConfigurable(true);
}

/**
 * Run the init() method to initialize the hill-climbing algorithm.  Notice
 * that the parent in the hierarchy is also called.  Subclasses are expected
 * to implement thier own init() if necessary, but call thier parent.
 */
public
void
init()
{
  config_d = (HillClimbingConfiguration)getConfiguration();
  this.setNumOfExperiments(config_d.getNumOfIterations());
  this.setThreshold(config_d.getThreshold());
  this.setPopSize(config_d.getPopulationSize());

  super.init();
}

/**
 * Implementation of the nextGeneration method common to both
 * hill climbing algorithms (next ascent and steepest ascent).
 */
public
boolean
nextGeneration()
{
  long [] sequence = new long[currentPopulation_d.size()];
  BufferedWriter writer_d;

  /**
   * Batch mode configuration dump to stdout
   */
  if (configuration_d.runBatch_d == true)
  {
    System.out.println("Run Batch = " + configuration_d.runBatch_d);
    System.out.println("Exp Number = " + configuration_d.expNumber_d);
  }

  try
  {
    String outLine ="";
    String sCluster = "";
    String sAligned = "";

    for (int i = 0; i < currentPopulation_d.size(); i++)
      sequence[i] = 0;

    if (false)
      for (int i=0; i<currentPopulation_d.size(); ++i)
	if (configuration_d.runBatch_d == true)
	{
          int exp = configuration_d.expNumber_d;
          sCluster = "";
          sAligned = "";
          int []n = currentPopulation_d.getCluster(i).getClusterVector();

          int[] c = new int[n.length];

          for (int z = 0; z < n.length; z++)
            c[z] = n[z];

          realignClusters(c);

          for (int zz = 0; zz < n.length; zz++)
          {
            sCluster += n[zz] + "|";
            sAligned += c[zz] + "|";
          }
          sequence[i]++;
          outLine = exp+","+i + "," + sequence[i] + "," + currentPopulation_d.getCluster(i).getObjFnValue()+","+ sCluster+","+sAligned;
          configuration_d.writer_d.write(outLine + "\r\n");
        }

    boolean end=false;
    while (!end)
    {
      end = true;
      for (int i=0; i<currentPopulation_d.size(); ++i)
      {
        if (!currentPopulation_d.getCluster(i).isMaximum())
        {
          if (configuration_d.runBatch_d == true)
          {
            int exp = configuration_d.expNumber_d;
	    sCluster = "";
            sAligned = "";
	    int []n = currentPopulation_d.getCluster(i).getClusterVector();

            int[] c = new int[n.length];

            for (int z = 0; z < n.length; z++)
              c[z] = n[z];

            realignClusters(c);

	    for (int zz = 0; zz < n.length; zz++)
            {
	      sAligned += c[zz] + "|";
              sCluster += n[zz] + "|";
            }
	    sequence[i]++;
	    outLine = exp+","+i + "," + sequence[i] + "," + currentPopulation_d.getCluster(i).getObjFnValue()+","+ sCluster+","+sAligned;
	    configuration_d.writer_d.write(outLine + "\r\n");
          }

          //end of intrumentation code
          getLocalMaxGraph(currentPopulation_d.getCluster(i));
        }

        if (!currentPopulation_d.getCluster(i).isMaximum())
        {
          end = false;
        }
        if (currentPopulation_d.getCluster(i).getObjFnValue()
                > getBestCluster().getObjFnValue())
        {
          setBestCluster(currentPopulation_d.getCluster(i).cloneCluster());
        }
      }
    }
    return end;
  }
  catch(Exception e)
  {return false;}
}

/**
 * Used for debugging, giving consecutive numbers for numbering
 * clusters
 */
private void realignClusters(int[] c)
{
  int[] map = new int[c.length];
  int index = 0;

  for (int i = 0; i < c.length; i++)
    map[i] = -1;

  for (int j = 0; j < c.length; j++)
  {
    int clus = c[j];

    if (map[clus] == -1)
    {
      index++;
      map[clus] = index;
    }
  }

  for (int k=0; k< c.length;k++)
  {
    c[k]=map[c[k]];
  }
}


/**
 * This is method that is redefined by the subclasses for each specific
 * hill-climbing algorithm, i.e., where the hill-climbing is actually performed
 */
protected abstract
Cluster
getLocalMaxGraph(Cluster c);

/**
 * This method is used to "shake" or reinitialize clusters
 */
public
void
reInit()
{
    currentPopulation_d.shuffle();
}

/**
 * This method is redefined at this point because both hill-climbing method
 * variants share the same configuration parameters (with different
 * default values, though, which is why they redefine the #getConfiguration()
 * method)
 *
 * @return the fully qualified class name for the hill-climbing configuration dialog
 * @see #getConfiguration()
 */
public
String
getConfigurationDialogName()
{
  return "bunch.HillClimbingClusteringConfigurationDialog";
}

/**
 * Creates and returns a configuration for hill-climbing algorithms.
 * Subclasses for this generic algorithm class redefine this method
 * to set the appropriate default values for each of them to the
 * configuration returned by this method and then return it as
 * expected.
 *
 * @return a HillClimbing configuration object
 */
public
Configuration
getConfiguration()
{
  if (configuration_d == null) {
    configuration_d = new HillClimbingConfiguration();
  }
  return configuration_d;
}

public
void
setConfiguration(HillClimbingConfiguration c)
{
   configuration_d = c;
}
}
