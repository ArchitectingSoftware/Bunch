/****
 *
 *	$Log: ClusterUsingVectorSAHC.java,v $
 *	Revision 3.0  2002/02/03 18:42:06  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:18  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */
package bunch.BunchServer;

import bunch.*;
import javax.swing.*;
import java.awt.*;

public class ClusterUsingVectorSAHC implements Runnable
{

int[] currWork = null;
int pos=-1, maxPos=-1;
Cluster  currCluster = null;
Callback cliCallback = null;
Graph    graph = null;
ServerProperties sProps = null;
IterationManager iMgr = null;
BSWindow parent = null;
ServerClusteringProgress progressWindow = null;
int   totalWork = 0;
boolean adaptiveEnabled = true;
int currUOWSz = -1;

FindNeighbor nServer = new FindNeighbor();

  public ClusterUsingVectorSAHC(BSWindow p, ServerProperties sp, Cluster c, Callback cb) {
      parent = p;
      sProps = sp;
      graph = sp.theGraph;
      currCluster = c;
      cliCallback = cb;
      progressWindow = null;
      iMgr = new IterationManager();
      totalWork = 0;
      adaptiveEnabled = sp.adaptiveEnabled;
  }

public void setProgressWindow(ServerClusteringProgress pw)
{
   progressWindow = pw;
}

public void disposeProgressWindow()
{
   if (progressWindow != null)
      progressWindow.dispose();
}

public void run()
{
/*************
   if (progressWindow == null)
   {
      progressWindow = new ServerClusteringProgress(parent,"Clustering In Progress...",
         false);
      Dimension dlgSize = progressWindow.getPreferredSize();
      Dimension frmSize = parent.getSize();
      Point loc = parent.getLocation();
      progressWindow.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
         (frmSize.height - dlgSize.height) / 2 + loc.y);
      progressWindow.setVisible(true);
      progressWindow.repaint();
   }
**********/
//System.out.println("STARTING---> MQ = " + currCluster.getObjFnValue());
   Cluster bestCluster = maximizeCluster(currCluster);
   ReportToClientBestCluster(bestCluster);
//System.out.println("ENDING-----> MQ = " + bestCluster.getObjFnValue());

   //disposeProgressWindow();

   //progressWindow.dispose();

}

public Cluster maximizeCluster(Cluster c)
{
    if (c == null) return null;

    Cluster maxC = c.cloneCluster();
    Cluster intermC = c.cloneCluster();
    totalWork = 0;

    double maxOF = maxC.getObjFnValue();
    double originalMax = maxOF;

    if(sProps.bestObjFnValue < maxOF)
      sProps.bestObjFnValue = maxOF;

    int[] clustNames = c.getClusterNames();

    int[] clusters = c.getClusterVector();

    int[] maxClust = maxC.getClusterVector();

    boolean[] locks = c.getLocks();

    //***********
    //take this out later
    //***********
    //if(pos == -1)
    //{
      pos = 0;
      maxPos = clusters.length;
    //}

    int[] workVector = null;

    while((workVector = getMoreWork(workVector)) != null)
    {
      totalWork += workVector.length;
      intermC = nServer.clusterWorklist(c,intermC,clustNames,locks,workVector);
      if (progressWindow != null)
        progressWindow.updateWorkProcessed(workVector.length,intermC.getObjFnValue(),currUOWSz,adaptiveEnabled);
      if (intermC.getObjFnValue() > maxOF) {
         maxC.copyFromCluster(intermC);//System.arraycopy(clusters, 0, intermClust, 0, clusters.length);
         maxOF = maxC.getObjFnValue();
      }
    }

    if (maxOF > originalMax) {
        c.copyFromCluster(maxC);//.arraycopy(maxClust, 0, clusters, 0, clusters.length);
        if(sProps.bestObjFnValue < maxOF)
            sProps.bestObjFnValue = maxOF;
    }
    else {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true);
    }
    //c.calcObjFn();

    return c;
}

int[] getMoreWork(int [] lastWorkVector)
{
   if (iMgr == null) return null;

   iMgr.direction  = IterationManager.DIR_TO_CLIENT;
   iMgr.msgType    = IterationManager.MSG_GET_CLUSTER_VECTOR;
   iMgr.jndiServerName = sProps.jndiName;  // parent.getJndiName();
   iMgr.svrID = sProps.svrID;
   iMgr.workVector = lastWorkVector;
   //iMgr.workVector = null;

   try
   {
      byte [] so = bunch.util.BunchUtilities.toByteArray(iMgr);
      byte [] rtdObj = cliCallback.callFromServerWithObj("GET_NEXT_VECTOR",so);
      iMgr = (IterationManager)bunch.util.BunchUtilities.fromByteArray(rtdObj);
      currUOWSz = iMgr.uowSz;
      return iMgr.clusterVector;
   }
   catch(Exception ex)
   {
      System.out.println("EXCEPTION - getMoreWork(): " + ex.toString());
      return null;
   }
}

void DumpWorkVector(int []wv)
{
   System.out.println("DEBUG:  DUMPING WORK VECTOR");
   for(int i = 0; i < wv.length; i++)
      System.out.println("   "+i+" = " + wv[i]);
}

void ReportToClientBestCluster(Cluster best)
{
   if (iMgr == null) return;

   iMgr.direction  = IterationManager.DIR_TO_CLIENT;
   iMgr.msgType    = IterationManager.MSG_SEND_CLUSTER_VECTOR;
   iMgr.workVector = best.getClusterVector();
   iMgr.jndiServerName = sProps.jndiName;  //parent.getJndiName();

   try
   {
      byte [] so = bunch.util.BunchUtilities.toByteArray(iMgr);
      boolean rc = cliCallback.bCallFromServerWithObj("BEST_ITERATION_VECTOR",so);
      //parent.appendLogMsg("Sending back MQ = " + best.getObjFnValue());
   }
   catch(Exception ex)
   {
      System.out.println("EXCEPTION - ReportBestCluster(): " + ex.toString());
   }
}

}