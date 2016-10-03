/****
 *
 *	$Log: GAMethodFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:49  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
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

/**
 * A factory for different kinds of methods for the GA
 *
 * @author Diego Doval / Brian Mitchell
 * @version 1.0
 * @see bunch.GAMethod
 * @see bunch.GenericFactory
 */
public
class GAMethodFactory
  extends GenericFactory
{

/**
 * Class constructor, defines the objects that the factory will be able
 * to create
 */
public
GAMethodFactory()
{
  super();
  setFactoryType("GAMethod");
  addItem("tournament", "bunch.GATournamentMethod");
  addItem("roulette wheel", "bunch.GARouletteWheelMethod");
}

/**
 * Obtains the GA method corresponding to name passed as parameter.
 * Utility method that uses the #getItemInstance(java.lang.String) method
 * from GenericFactory and casts the object to a GAMethod object.
 *
 * @param the name for the desired method
 * @return the GA method corresponding to the name
 */
public
GAMethod
getMethod(String name)
{
  return (GAMethod)getItemInstance(name);
}
}
