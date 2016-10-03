/****
 *
 *	$Log: ClusteringMethodFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:46  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/11/26 15:48:12  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.1  2000/10/22 17:47:02  bsmitc
 *	Collapsed NAHC and SAHC into a generic hill climbing method
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

import java.util.*;

/**
 * A factory for different kinds of objects that calculate the
 * optimum clustering for a graph
 *
 * @author Brian Mitchell
 *
 * @see bunch.ClusteringMethod
 * @see bunch.GenericFactory
 */
public
class ClusteringMethodFactory
  extends GenericFactory
{

String defaultMethod = "Hill Climbing";

/**
 * Class constructor, defines the objects that the factory will be able
 * to create
 */
public
ClusteringMethodFactory()
{
  super();
  setFactoryType("ClusteringMethod");
  addItem("Hill Climbing", "bunch.GeneralHillClimbingClusteringMethod");
  addItem("NAHC", "bunch.NextAscentHillClimbingClusteringMethod");
  addItem("SAHC", "bunch.SteepestAscentHillClimbingClusteringMethod");
  addItem("GA", "bunch.GAClusteringMethod");
  addItem("Exhaustive", "bunch.OptimalClusteringMethod");
}

/**
 * This method returns the default clustering method.  It is used in the GUI and
 * API when the clustering algorithm is not explicitly specified.
 */
public String getDefaultMethod()
{
  return defaultMethod;
}

/**
 * This method returns a list of items in the factory.
 *
 * @return A string array containing the keys in the factory.
 */
public String[] getItemList()
{
  String[] masterList = super.getItemList();
  String[] resList    = new String[masterList.length-2];

  int resPos = 0;
  for(int i = 0; i < masterList.length; i++)
  {
    String item = masterList[i];
    if ((item.equals("SAHC")) || (item.equals("NAHC")))
      continue;
    else
      resList[resPos++] = item;
  }

  return resList;
}

/**
 * Obtains the clustering method corresponding to name passed as parameter.
 * Utility method that uses the #getItemInstance(java.lang.String) method
 * from GenericFactory and casts the object to a ClusteringMethod object.
 *
 * @param the name for the desired method
 * @return the clustering method corresponding to the name
 */
public
ClusteringMethod
getMethod(String name)
{
  return (ClusteringMethod)getItemInstance(name);
}
}
