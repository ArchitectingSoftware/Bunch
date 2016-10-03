/****
 *
 *	$Log: ParserFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:54  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
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

/**
 * A factory for parsers of different kinds
 *
 * @author Diego Doval
 * @version 1.0
 * @see bunch.Parser
 * @see bunch.GenericFactory
 */
public
class ParserFactory
  extends GenericFactory
{

public
ParserFactory()
{
  super();
  setFactoryType("Parser");
  addItem("dependency", "bunch.DependencyFileParser");
  addItem("gxl", "bunch.gxl.parser.GXLGraphParser");
  addItem("cluster", "bunch.ClusterFileParser");
}

public
Parser
getParser(String name)
{
  return (Parser)getItemInstance(name);
}
}
