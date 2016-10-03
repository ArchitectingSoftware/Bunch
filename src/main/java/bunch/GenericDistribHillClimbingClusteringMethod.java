/****
 *
 *	$Log: GenericDistribHillClimbingClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:50  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2001/04/03 23:32:12  bsmitc
 *	Added NAHC (really HC) support for Distributed Bunch, updated release
 *	version number to 3.2
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

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.RMISecurityManager;
import java.util.*;

import bunch.BunchServer.BunchSvrMsg;
import bunch.BunchServer.IterationManager;

/**
 * A generic hill climbing clustering method class, intended to provide common services
 * to both hill-climbing algorithms (next ascent and steepest ascent).
 * The class basically takes charge of executing each generation, leaving to the
 * subclasses the task of performing the actual improvement by defining
 * #getLocalMaxGraph(bunch.Graph) method.  This class also implments necessary
 * behavior to support distributed clustering algorithms.
 *
 * @author Brian Mitchell
 *
 * @see bunch.NextAscentHillClimbingClusteringMethod
 * @see bunch.SteepestAscentHillClimbingClusteringMethod
 */
public abstract
class GenericDistribHillClimbingClusteringMethod
  extends GenericClusteringMethod2
{
//HillClimbingConfiguration config_d;
NAHCConfiguration config_d;
protected BunchEvent                event = null;
protected Vector                    activeServerVector = null;
protected CallbackImpl              svrCallback = null;
protected SynchronizedEventQueue    eventQ = null;
protected int                       BaseUOWSize = 5;
protected boolean                   useAdaptiveAlg = true;

public
GenericDistribHillClimbingClusteringMethod()
{
  setConfigurable(true);
}

/**
 * Used to set a bunch event object, which will be used to communicate
 * between the coordinator and the remote bunch servers.
 */
public void setEventObject(BunchEvent be)
{
   event = be;
}

/**
 * Set to indicate that the adapative loadbalancing mager will be used.
 */
public void setAdaptiveEnable(boolean b)
{ useAdaptiveAlg = b; }

/**
 * Used to set the default unit of work size.
 */
public void setUOWSize(int i)
{ BaseUOWSize = i;  }

/**
 * Set the number of active servers that are ready to assist with clustering
 */
public void setActiveServerVector(Vector v)
{
   activeServerVector = v;
}

/**
 * Setup the server callback object.  The server will call this object when
 * it wants to initiate communication to the client
 */
public void setSvrCallback(CallbackImpl cb)
{
   svrCallback = cb;
}

/**
 * Event queue management:  get, set and make.  These methods controll the
 * asynchronous capabilities of distributed bunch.
 */
public void setEventQueue(SynchronizedEventQueue eq)
{  eventQ = eq;   }

/**
 * Get instance of cached event queue
 */
public SynchronizedEventQueue getEventQueue()
{  return eventQ;  }

/**
 * Create a new event queue
 */
public void makeEventQueue()
{  eventQ = new SynchronizedEventQueue(Thread.currentThread()); }

/**
 * Create a new event
 */
public void initBunchEvent()
{
   event = new BunchEvent();
}


/**
 * Initialize the generic distributed hill climbing clustering method
 */
public
void
init()
{
  config_d = (NAHCConfiguration)getConfiguration();

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

    manageDistribWorkIteration("Start");


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
        Cluster dbgC = currentPopulation_d.getCluster(i);

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

    manageDistribWorkIteration("Done");
    return end;
  }
  catch(Exception e)
  {
    manageDistribWorkIteration("Done");
    return false;
  }
}

/**
 * This is the manager/coordinator for the distribtued work.
 */
private void manageDistribWorkIteration(String activity)
{
  Vector svrV = this.activeServerVector;

  if (svrV == null)
    return;

  IterationManager im = new IterationManager();

  byte[] so = bunch.util.BunchUtilities.toByteArray(im);

  for(int i = 0; i < svrV.size(); i++)
  {
    Binding b = (Binding)activeServerVector.elementAt(i);
    if (so != null)
    {
      BunchSvrMsg bsm = (BunchSvrMsg)b.getObject();
      try{
        boolean rc = bsm.invokeMessage(activity,so);
      }catch(Exception ex) {
        System.out.println("EXCEPTION - manageDistribWorkIteration("+activity+"):  " + ex.toString());
        return;
      }
    }
  }
  return;
}

/**
 * This method renumbers the clusters so that they have sequential IDs
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
getLocalMaxGraph(Cluster g);

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
