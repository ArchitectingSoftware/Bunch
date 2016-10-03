/****
 *
 *	$Log: ServerSteepestAscentClusteringMethod.java,v $
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
import  bunch.*;

public class ServerSteepestAscentClusteringMethod
  extends GenericDistribHillClimbingClusteringMethod
{

int[] currWork = null;
int pos=-1, maxPos=-1;

FindNeighbor nServer = new FindNeighbor();

  public ServerSteepestAscentClusteringMethod() {
  }

protected
Cluster
getLocalMaxGraph(Cluster c)
{
System.out.print("IN:  " + c.getObjFnValue() + "  ");

    Cluster maxC = c.cloneCluster();
    Cluster intermC = c.cloneCluster();

    double maxOF = maxC.getObjFnValue();
    double originalMax = maxOF;

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

    while((workVector = getMoreWork()) != null)
    {
      intermC = nServer.clusterWorklist(c,intermC,clustNames,locks,workVector);
      if (bunch.util.BunchUtilities.compareGreater(intermC.getObjFnValue(),maxOF)) {
      //if (intermC.getObjFnValue() > maxOF) {
         maxC.copyFromCluster(intermC);//System.arraycopy(clusters, 0, intermClust, 0, clusters.length);
         maxOF = maxC.getObjFnValue();
      }
    }

    if (bunch.util.BunchUtilities.compareGreater(maxOF, originalMax)) {
    //if (maxOF > originalMax) {
        c.copyFromCluster(maxC);//.arraycopy(maxClust, 0, clusters, 0, clusters.length);
    }
    else {
      //we didn't find a better max partition then it's a maximum
      c.setConverged(true);
    }
    //c.calcObjFn();

System.out.println("OUT:  " + c.getObjFnValue() + "  ");

    return c;
}

int[] getMoreWork()
{
   int start = pos;
   int end   = Math.min(pos+5,maxPos);

   int delta = end-start;

   if(delta==0)
      return null;

   int [] workArea = new int[delta];
   for(int i = 0; i < delta; i++)
      workArea[i]=pos++;

   return workArea;
}

public
Configuration
getConfiguration()
{
  boolean reconf=false;
  if (configuration_d == null) {
    reconf = true;
  }

  HillClimbingConfiguration hc = (HillClimbingConfiguration)super.getConfiguration();

  if (reconf) {
    hc.setThreshold(0.1);
    hc.setNumOfIterations(100);
    hc.setPopulationSize(5);
  }
  return hc;
}

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