/****
 *
 *	$Log: BunchSvrMsgImpl.java,v $
 *	Revision 3.0  2002/02/03 18:42:06  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/08/07 21:49:12  bsmitc
 *	*** empty log message ***
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

import bunch.Callback;
import bunch.Cluster;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import java.util.Properties;
import javax.naming.*;
import java.rmi.RMISecurityManager;

import java.awt.*;
import javax.swing.*;

public class BunchSvrMsgImpl extends PortableRemoteObject implements BunchSvrMsg{

  Callback clientCallback = null;
  private  ServerProperties sp = new ServerProperties();
  BSWindow parent = null;
  ServerClusteringProgress progressWindow = null;
  String jndiName = "";
  boolean textMode = false;
  //ServerClusteringEngine sce = null;

  public BunchSvrMsgImpl() throws RemoteException {
  }

  public void setGUIMode()
  { textMode = false;  }

  public void setTextMode()
  { textMode = true;  }

  public void setJndiName(String s)
  {
    jndiName = s;
  }

  public void setParent(BSWindow win)
  {   parent = win;  }

  public boolean invokeMessage(String name, byte[] serializedClass)
  {
      if(name.equals("Init"))
      {
         DistribInit di = (DistribInit)bunch.util.BunchUtilities.fromByteArray(serializedClass);
         System.out.println("Got the init message");
         sp.svrName=di.svrName;
         sp.svrID=di.svrID;
         sp.theGraph=di.theGraph;
         sp.objFn=di.objFunction;
         sp.clusteringMethod=di.clusteringTechnique;
         sp.bp = di.bp;
         sp.adaptiveEnabled = di.adaptiveEnabled;
          sp.jndiName = jndiName;
          sp.theGraph.setObjectiveFunctionCalculatorFactory(sp.bp.getObjectiveFunctionCalculatorFactory());

         System.out.println("The number of nodes in the graph is:  " + sp.theGraph.getNumberOfNodes());
      }
      else if(name.equals("Run"))
      {
         String msg = "Just got the run message";

         if(parent != null)
            parent.appendLogMsg(msg);
         else
            System.out.println(msg);

         ServerClusteringEngine sce = new ServerClusteringEngine(sp);
         boolean rc = sce.run();

         msg = "Just finished running the server";

         if(parent != null)
            parent.appendLogMsg(msg);
         else
            System.out.println(msg);

         double objFnValue = sp.theGraph.getObjectiveFunctionValue();
         //parent.appendLogMsg("The objective function is = " + objFnValue);
         return rc;
      }
      else if(name.equals("StartIteration"))
      {
      /********
         IterationManager starti = (IterationManager)bunch.util.BunchUtilities.fromByteArray(serializedClass);
         Cluster workCluster = new Cluster(sp.theGraph,starti.clusterVector);
         ClusterUsingVectorSAHC cThread = new ClusterUsingVectorSAHC(parent,sp,workCluster,clientCallback);
         cThread.setProgressWindow(progressWindow);


         Thread t = new Thread(cThread);
         t.start();
      ******/

        final byte []sc = serializedClass;
        bunch.SwingWorker worker_d = new bunch.SwingWorker(/*clusteringMethod_x*/) {
          public Object construct() {
            IterationManager starti = (IterationManager)bunch.util.BunchUtilities.fromByteArray(sc);
            Cluster workCluster = new Cluster(sp.theGraph,starti.clusterVector);
            ClusterUsingVectorSAHC cThread = new ClusterUsingVectorSAHC(parent,sp,workCluster,clientCallback);
            cThread.setProgressWindow(progressWindow);
            cThread.run();

            return "Done";
          }
          public void finished() {
          }
        };
        worker_d.setPriority(Thread.MIN_PRIORITY);
        worker_d.start();




         //parent.appendLogMsg("Just started iteration message...");

         return true;
      }
      else if(name.equals("Done"))
      {
         String msg = "Best Objective Function Value Found = " + sp.bestObjFnValue;
         if (progressWindow != null)
         {
            progressWindow.dispose();
            progressWindow=null;
         }

         if(parent != null)
          parent.appendLogMsg(msg);
         else
          System.out.println(msg);
      }
      else if(name.equals("Start"))
      {
         if ((progressWindow == null)&&(textMode == false))
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
         else
         {
            System.out.println("Clustering in progress...");
            String msg = "Adaptive Algorithm:  ";
            if(sp.adaptiveEnabled = true)
              msg+= "Enabled";
            else
              msg+= "Disabled";
            System.out.println(msg);
         }
      }


      return true;
  }

  public boolean registerCallback(Callback c)
  {
      clientCallback = c;
      sp.clientCB=c;
      System.out.println("Got client callback object");
      try{
      c.callFromServer("Hello from server");
      }catch(Exception ex)
      {}
      return true;
  }

  public boolean doAction(String command)
  {
    return true;
  }

  public Callback getClientCallback()
  {   return clientCallback;  }
}