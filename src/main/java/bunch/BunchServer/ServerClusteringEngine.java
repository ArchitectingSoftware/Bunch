/****
 *
 *	$Log: ServerClusteringEngine.java,v $
 *	Revision 3.0  2002/02/03 18:42:07  bsmitc
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

public class ServerClusteringEngine {

  ServerProperties sProps;
  
  public ServerClusteringEngine(ServerProperties sp) {
      sProps = sp;
  }

  public boolean run()
  {
      sProps.theGraph.setRandom(new java.util.Random(10));
      //ClusteringMethod2 cm = (ClusteringMethod2)new SteepestAscentHillClimbingClusteringMethod2();
      ClusteringMethod2 cm = (ClusteringMethod2)new ServerSteepestAscentClusteringMethod();

      HillClimbingConfiguration hcc = (HillClimbingConfiguration)cm.getConfiguration();
      hcc.setNumOfIterations(1);
      hcc.setThreshold(1.0);
      ((GenericDistribHillClimbingClusteringMethod)cm).setConfiguration(hcc);
      
      cm.initialize();
      cm.setGraph(sProps.theGraph.cloneGraph());
      cm.run();
      Cluster c = cm.getBestCluster();
      Graph g = cm.getBestGraph();

      sProps.theGraph = g.cloneGraph();

      return true;
  }

  
} 