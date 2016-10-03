/****
 *
 *	$Log: GraphOutputFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:51  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *
 *	Revision 3.2  2000/08/18 21:09:59  bsmitc
 *	Removed text tree and tom sawyer from the output factory
 *
 *	Revision 3.1  2000/07/28 14:26:19  bsmitc
 *	Added support for the TXTTree Graph output option
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
 * A factory for graph output generators
 *
 * @author Brian Mitchell
 * @version 1.0
 * @see bunch.GraphOutput
 * @see bunch.GenericFactory
 */
public
class GraphOutputFactory
  extends GenericFactory
{

public String defaultOption = "Dotty";
/**
 * Class constructor, defines the objects that the factory will be able
 * to create
 */
public
GraphOutputFactory()
{
  super();
  setFactoryType("GraphOutput");
  addItem("Dotty", "bunch.DotGraphOutput");
  addItem("Text", "bunch.TXTGraphOutput");
  addItem("GXL","bunch.gxl.io.GXLGraphOutput");
  //addItem("Tom Sawyer", "bunch.TSGraphOutput");
  //addItem("Text Tree","bunch.TXTTreeGraphOutput");
}

/**
 * Obtains the graph output generator corresponding to name passed as parameter.
 * Utility method that uses the #getItemInstance(java.lang.String) method
 * from GenericFactory and casts the object to a GraphOutput object.
 *
 * @param the name for the desired output generator
 * @return the graph output generator corresponding to the name
 */
public
GraphOutput
getOutput(String name)
{
  return (GraphOutput)getItemInstance(name);
}

public String getDefaultMethod()
{
  return this.defaultOption;
}
}
