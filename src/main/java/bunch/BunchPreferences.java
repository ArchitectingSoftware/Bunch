/****
 *
 *	$Log: BunchPreferences.java,v $
 *	Revision 3.0  2002/02/03 18:41:44  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:07  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

/**
 * This class contains the settings used by Bunchv2. (Currently most of
 * the options are wired at compile time. However, the structure exists to
 * make this configuration loadable simply by providing a dialog box
 * that lets the user configure the options and then store them in a
 * java Serialized object)
 *
 * @author Brian Mitchell
 */
public
class BunchPreferences
  implements java.io.Serializable
{

/**
 * The main entities for managing the clustering process are the clustering
 * method, objective function, parser and output factories.  This class keeps
 * track as a global singleton of the factory instances.
 */
ClusteringMethodFactory methodFactory_d;
ObjectiveFunctionCalculatorFactory calculatorFactory_d;
ParserFactory parserFactory_d;
GraphOutputFactory outputFactory_d;

public static final long serialVersionUID = 100L;

/**
 * class constructor.  Create the factory objects.
 */
public
BunchPreferences()
{
  methodFactory_d =  new ClusteringMethodFactory();
  calculatorFactory_d = new ObjectiveFunctionCalculatorFactory();
  parserFactory_d = new ParserFactory();
  outputFactory_d = new GraphOutputFactory();
}

/**
 * Sets the factory of clustering methods for this preferences object
 *
 * @param fac the new clustering method factory
 * @see #getClusteringMethodFactory()
 * @see bunch.ClusteringMethodFactory
 */
public
void
setClusteringMethodFactory(ClusteringMethodFactory fac)
{
  methodFactory_d = fac;
}

/**
 * Obtains the factory of Clustering Methods set to this preferences object
 *
 * @return the clustering method factory
 * @see #setClusteringMethodFactory(bunch.ClusteringMethodFactory)
 * @see bunch.ClusteringMethodFactory
 */
public
ClusteringMethodFactory
getClusteringMethodFactory()
{
  return methodFactory_d;
}

/**
 * Sets the factory of objective function calculator objects for this preferences object
 *
 * @param fac the new OF Calculator method factory
 * @see #getObjectiveFunctionCalculatorFactory()
 * @see bunch.ObjectiveFunctionCalculatorFactory
 */
public
void
setObjectiveFunctionCalculatorFactory(ObjectiveFunctionCalculatorFactory fac)
{
  calculatorFactory_d = fac;
}

/**
 * Obtains the factory of objective function calculator
 * methods set to this preferences object
 *
 * @return the OF Calculator method factory
 * @see #setObjectiveFunctionCalculatorFactory(bunch.ObjectiveFunctionCalculatorFactory)
 * @see bunch.ObjectiveFunctionCalculatorFactory
 */
public
ObjectiveFunctionCalculatorFactory
getObjectiveFunctionCalculatorFactory()
{
  return calculatorFactory_d;
}

/**
 * Sets the factory of parsers for this preferences object
 *
 * @param fac the new parser factory
 * @see #getParserFactory()
 * @see bunch.ParserFactory
 */
public
void
setParserFactory(ParserFactory fac)
{
  parserFactory_d = fac;
}

/**
 * Obtains the factory of parsers set to this preferences object
 *
 * @return the parser factory
 * @see #setParserFactory(bunch.ParserFactory)
 * @see bunch.ParserFactory
 */
public
ParserFactory
getParserFactory()
{
  return parserFactory_d;
}

/**
 * Obtains the factory of output methods set to this preferences object
 *
 * @return the graph output method factory
 * @see #setGraphOutputFactory(bunch.GraphOutputFactory)
 * @see bunch.GraphOutputFactory
 */
public
GraphOutputFactory
getGraphOutputFactory()
{
  return outputFactory_d;
}

/**
 * Sets the factory of graph output objects for this preferences object
 *
 * @param fac the new graph output object factory
 * @see #getGraphOutputFactory()
 * @see bunch.GraphOutputFactory
 */
public
void
setGraphOutputFactory(GraphOutputFactory og)
{
  outputFactory_d = og;
}
}
