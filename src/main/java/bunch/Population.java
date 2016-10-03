/****
 *
 *	$Log: Population.java,v $
 *	Revision 3.0  2002/02/03 18:41:54  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/11/26 15:48:14  bsmitc
 *	Fixed various bugs
 *	
 *	Revision 3.1  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.0  2000/07/26 22:46:11  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.Vector;
import java.util.Enumeration;

public class Population {

  Vector   pop = new Vector();
  static   Graph g = null;
  Cluster  bestCluster = null;

  public Population(Graph graph) {
      g = graph.cloneGraph();
  }

  public void shuffle()
  {
      for(int i = 0; i < pop.size(); i++)
      {
         Cluster c = (Cluster)pop.elementAt(i);
         g.setClusters(c.getClusterVector());
         g.shuffleClusters();
         c.setClusterVector(g.getClusters());
         c.setConverged(false);
      }

  }

  public void genPopulation(int howMany)
  {
      pop.removeAllElements();
      for(int i = 0; i < howMany; i++)
      {
         //UNCOMMENT THE BELOW LINE FOR ORIGIONAL FUNCTION
         //int [] clusterV = g.getRandomCluster();

         //COMMENT THE BELOW LINE TO REMOVE THE EXPIREMENTAL FUNCTION
         int [] clusterV = g.genRandomClusterSize();
         Cluster c = new Cluster(g,clusterV);
         pop.addElement(c);
      }
  }

  public int size()
  {
      return pop.size();
  }

  public Cluster getCluster(int whichOne)
  {
      if ((whichOne >= 0) && (whichOne < size()))
         return (Cluster)pop.elementAt(whichOne);
      else
         return null;
  }

  public Enumeration elements()
  {
      return pop.elements();
  }
}