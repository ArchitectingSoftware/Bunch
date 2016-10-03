/****
 *
 *	$Log: ObjectiveFunctionCalculatorFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:54  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/08/11 22:04:23  bsmitc
 *	Commented out unwanted objective function calculators from the factory
 *
 *	Revision 3.0  2000/07/26 22:46:10  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

/**
 * A factory for different kinds of objective function calculator objects
 *
 * @author Diego Doval
 * @version 1.0
 * @see bunch.ObjectiveFunctionCalculator
 * @see bunch.GenericFactory
 */
public
class ObjectiveFunctionCalculatorFactory
  extends GenericFactory
{
String currObjFnMethod = "Incremental MQ Weighted";
String defaultMethod = "Incremental MQ Weighted";

/**
 * Class constructor, defines the objects that the factory will be able
 * to create
 */
public
ObjectiveFunctionCalculatorFactory()
{
  super();
  setFactoryType("ObjectiveFunctionCalculator");
  addItem("Basic MQ Function", "bunch.BasicMQ");
  addItem("Turbo MQ Function", "bunch.TurboMQ");
  //addItem("Incremental MQ", "bunch.TurboMQIncr");
  addItem("Incremental MQ Weighted", "bunch.TurboMQIncrW");

  addItem("bunch.BasicMQ", "bunch.BasicMQ");
  addItem("bunch.TurboMQ", "bunch.TurboMQ");
  addItem("bunch.ITurboMQ", "bunch.TurboMQIncrW");
  addItem("bunch.TurboMQIncrW", "bunch.TurboMQIncrW");

  //addItem("Turbo MQ Squared", "bunch.TurboMQ2");
  //addItem("Experimental Weighted 2", "bunch.WeightedObjectiveFunctionCalculator2");
  //addItem("Experimental Weighted 3", "bunch.WeightedObjectiveFunctionCalculator3");
  //addItem("Spiros MQ", "bunch.SpirosMQ");
}

/**
 * Obtains the OF Calculator corresponding to name passed as parameter.
 * Utility method that uses the #getItemInstance(java.lang.String) method
 * from GenericFactory and casts the object to a ObjectiveFunctionCalculator object.
 *
 * @param the name for the desired method
 * @return the OF Calculator corresponding to the name
 */
public
ObjectiveFunctionCalculator
getCalculator(String name)
{
  return (ObjectiveFunctionCalculator)getItemInstance(name);
}

public
ObjectiveFunctionCalculator
getSelectedCalculator()
{
  return (ObjectiveFunctionCalculator)getItemInstance(currObjFnMethod);
}

/**
 * This method returns the default clustering method.  It is used in the GUI and
 * API when the clustering algorithm is not explicitly specified.
 */
public String getDefaultMethod()
{
  return defaultMethod;
}

public
String
getCurrentCalculator()
{
   return currObjFnMethod;
}

public
void
setCurrentCalculator(String sCalc)
{
   currObjFnMethod = sCalc;
}

}