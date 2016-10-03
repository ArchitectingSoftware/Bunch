/****
 *
 *	$Log: ObjectiveFunctionCalculator.java,v $
 *	Revision 3.0  2002/02/03 18:41:53  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
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

public
interface ObjectiveFunctionCalculator extends java.io.Serializable
{
public void calculate();
public void init(Graph g);
public double calculate(Cluster c);
}
