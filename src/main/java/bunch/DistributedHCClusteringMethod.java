/****
 *
 *	$Log: DistributedHCClusteringMethod.java,v $
 *	Revision 3.0  2002/02/03 18:41:47  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2001/04/03 23:32:12  bsmitc
 *	Added NAHC (really HC) support for Distributed Bunch, updated release
 *	version number to 3.2
 *
 *	Revision 3.1  2001/04/03 21:42:35  bsmitc
 *	Baseline version of basic HC in a distributed mode
 *
 *
 */
package bunch;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.RMISecurityManager;
import java.util.*;

import bunch.BunchServer.IterationManager;
import bunch.BunchServer.BunchSvrMsg;

/**
 * This class implements the distributed hill climbing clustering algorithm.
 *
 * @author Brian Mitchell
 *
 */
public class DistributedHCClusteringMethod extends GenericDistribHillClimbingClusteringMethod {

/**
 * Constants related to work status
 */
public static final int STAT_PENDING = 0;
public static final int STAT_CHECKED_OUT = 1;
public static final int STAT_FINISHED = 2;
public static final int NO_SERVER_WORKING = -1;

  public DistributedHCClusteringMethod() {
  }

  int [] workQueue;
  int [] serverWorkingElement;
  int [] status;
  int pendingCount;
  int checkedOutCount;
  int finishedCount;
  int currWorkVectorIdx = 0;
  int totalCount = 0;
  int baseUnitSize = 5;

  Stack workQ = new Stack();
  Hashtable pendingQ = new Hashtable();

  int msgIDCtr = 0;
  transient int runningServers = 0;

/**
 * This method initializes the work vectors used to manage the work
 * of the clustering servers.  The vectors are built from a node in
 * a cluster.
 *
 * @param c  The cluster that will be used to build the work vector
 *
 * @return Returns true if the work vectors were correclty built, false if
 *         not.
 */
private boolean initWorkVectors(Cluster c)
{
   if (c == null) return false;

   int [] cv = c.getClusterVector();
   int vSize = cv.length;
   totalCount = vSize;

   //------------
   //Now the workQ stuff
   //------------
   workQ.clear();
   pendingQ.clear();


   /**
    * If the work queue is null, then allocate it
    */
   if(workQueue == null)
   {
      workQueue = new int[vSize];
      serverWorkingElement = new int[vSize];
      status = new int[vSize];
   }

   /**
    * If the work queue is not of the proper size, reallocate it
    */
   if(workQueue.length != vSize)
   {
      workQueue = new int[vSize];
      serverWorkingElement = new int[vSize];
      status = new int[vSize];
   }


   runningServers = 0;

   currWorkVectorIdx = 0;

   /**
    * Initialze the work vectors to indicate that no server is working
    * and the status is pending.
    */
   for(int i = 0; i < vSize; i++)
   {
      workQueue[i] = i;
      serverWorkingElement[i] = NO_SERVER_WORKING;
      status[i] = STAT_PENDING;
      workQ.add(new Integer(i));
   }

   /**
    * Initialize the statistics and coutners.
    */
   pendingCount = 0;
   checkedOutCount = 0;
   finishedCount = 0;

   return true;
}

/**
 * This method starts the current iteration.  It notifies the servers that we
 * are about to start the current interation.  This method builds a
 * BunchSvrMsg object containing all necessary initizization data for each of
 * the servers (e.g., the MDG, the current partition, etc).
 *
 * This method then sends the initialization message to each of the servers.
 *
 * @param c The starting cluster
 */
private boolean startIteration(Cluster c)
{
   Vector svrV = this.activeServerVector;
   IterationManager im = new IterationManager();
   initWorkVectors(c);

   //im.direction = IterationManger.DIR_TO_SERVER;
   im.workVector= c.getClusterVector();
   im.clusterVector=c.getClusterVector();
   im.msgID = ++msgIDCtr;

   /**
    * Serialize the iteration manager object
    */
   byte[] so = bunch.util.BunchUtilities.toByteArray(im);

   /**
    * Traverse the active servers, sending each one the start iteration
    * message
    */
   for(int i = 0; i < svrV.size(); i++)
   {
      Binding b = (Binding)activeServerVector.elementAt(i);
      if (so != null)
      {
         BunchSvrMsg bsm = (BunchSvrMsg)b.getObject();
         try{
            boolean rc = bsm.invokeMessage("StartIteration",so);
            synchronized(this)
            {
               if (rc == true)
                  runningServers++;
            }
         }catch(Exception ex)
         {
            System.out.println("EXCEPTION - startIteration():  " + ex.toString());
            return false;
         }
      }
   }
   return true;
}

/**
 * Given a starting cluster, this method produces the "improved" cluster
 * by using our hill climbing algorithm.
 */
protected
Cluster
getLocalMaxGraph(Cluster c)
{
  eventQ.setManagerThread(Thread.currentThread());
  initWorkVectors(c);

  Cluster maxC = c.cloneCluster();
  Cluster intermC = c.cloneCluster();

  double originalMax = maxC.getObjFnValue();
  double maxOF = originalMax;

  int[] clustNames = c.getClusterNames();
  int[] clusters = c.getClusterVector();
  long maxPartitionsToExamine = clustNames.length;
  int currClustersExamined = 0;
  double evalPct = (double)(((NAHCConfiguration)configuration_d).getMinPctToConsider())/100.0;
  long partitionsToExamine = (long)(((double)maxPartitionsToExamine)*evalPct);

  boolean exceededMaxExamination = false;

//System.out.println("partitions to examine = " + partitionsToExamine + "  Min Pct = " + ((NAHCConfiguration)configuration_d).getMinPctToConsider()  );

  try
  {
    /**
     * Start the current iteration
     */
    boolean rc = startIteration(c);
    if(rc == false) return c;

    /**
     * Keep going while there is more work to process
     */
    while(isMoreWork())
    {
      /**
       * Get the current event (i.e., Work).  It is either going to be
       * a work request event so send work to the requester, or a work
       * finsihed event, so process the results
       */
       BunchEvent be = eventQ.getEvent();

       //Its a request for work...
       if(be.getEventObj() instanceof WorkRequestEvent)
       {
          WorkRequestEvent wre = (WorkRequestEvent)be.getEventObj();

          //How much work was requested...
          wre.workToDo = getMoreWork(wre.requestWorkSz);
          if(wre.workToDo== null)
            wre.actualWorkSz=0;
          else
            wre.actualWorkSz = wre.workToDo.length;

          //We may be out of work, so let the server know...
          if (exceededMaxExamination == true)
          {
            wre.workToDo = null;
            wre.actualWorkSz = 0;
          }
       }
       //Its a notification that work has finished...
       else if(be.getEventObj() instanceof WorkFinishedEvent)
       {
          WorkFinishedEvent wfe = (WorkFinishedEvent)be.getEventObj();
          intermC.setClusterVector(wfe.clusterVector);
          finishedCount += wfe.clusterVector.length;

          //The server is done, so reduce the number of servers counter
          synchronized(this)
          {
               runningServers--;
          }

          //Is the work returned from the server better then anything else
          //seen so far, if so save it...
          if (intermC.getObjFnValue() > maxOF) {
             maxC.copyFromCluster(intermC);
             maxOF = maxC.getObjFnValue();

             if(finishedCount >= partitionsToExamine)
                exceededMaxExamination = true;
          }
       }
       /**
        * Release the event, were done...
        */
       eventQ.releaseEvent(be);
    }
  }
  catch(Exception e)
  {
    System.out.println("EXCEPTION - getLocalMaxGraph():  " + e.toString());
    return c;
  }

  if (maxOF > originalMax)
  {
    //we found a better max partition, save it into c
    c.copyFromCluster(maxC);
  }
  else
  {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true);
  }

  /**
   * What we return will be the same cluster if we did not find something better,
   * but we will set the setConverged flag to true.  If we found something better,
   * however, setConverged will be false, and we will return the improved partition.
   */
  return c;
}

/**
 * This method determines if there is more work to perform for the current
 * iteration
 *
 * @return true if more work exists, false if not
 */
boolean isMoreWork() throws Exception
{
   if (currWorkVectorIdx == totalCount)
   {
      synchronized(this)
      {
         if(runningServers == 0)
            return false;
         else
            return true;
      }
   }
   else
      return true;
}

/**
 * This method returns more work based on the work request size.  If the
 * requested amount of work does not exist, it will return less.  If there
 * is no more work the vector returned will be null.
 *
 * @return An array of work to be processed, null if no more work remains.
 */
int [] getMoreWork(int requestSz)
{
  /**
   * Persist indexes to the remaining work
   */
  int start = currWorkVectorIdx;
  int pos = start;
  int maxPos = totalCount;
  int end   = Math.min(pos+requestSz,maxPos);

  int delta = end-start;

  currWorkVectorIdx += delta;
  checkedOutCount   += delta;

  //Handle the condition if no more work remains.
  if(delta==0)
    return null;

  int [] workArea = new int[delta];

  /**
   * Build the work vector
   */
  for(int i = 0; i < delta; i++)
  {
    workArea[i]=workQueue[pos++];
    status[i]=STAT_PENDING;
  }

  return workArea;
}

/**
 * This method is required by the base class to handle requests for
 * configuraiton information.
 */
public
Configuration
getConfiguration()
{
  boolean reconf=false;

  /**
   * If the configuration object is null, allocate it
   */
  if (configuration_d == null) {
    configuration_d = new NAHCConfiguration();
    reconf = true;
  }

  NAHCConfiguration hc = (NAHCConfiguration)configuration_d;

  /**
   * If the first time through, set the defaults.
   */
  if (reconf) {
    hc.setThreshold(1);
    hc.setNumOfIterations(1);
    hc.setPopulationSize(1);
    hc.setMinPctToConsider(0);
    hc.setRandomizePct(100);
    hc.setSATechnique(null);
  }

  return hc;
}

/**
 * This is a request to setup the configuration.  The base configuration is
 * obtainined by the parent.
 */
public void
setDefaultConfiguration()
{
  HillClimbingConfiguration hc = (HillClimbingConfiguration)super.getConfiguration();

  hc.setThreshold(0.1);
  hc.setNumOfIterations(100);
  hc.setPopulationSize(5);

  setConfiguration(hc);
}

}