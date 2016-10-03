/****
 *
 *	$Log: IterationListener.java,v $
 *	Revision 3.0  2002/02/03 18:41:52  bsmitc
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

/**
 * This interface is used to constrain event types to broadcast
 * iteration events
 *
 * @author Brian Mitchell
 */
public
interface IterationListener
{
public void newIteration(IterationEvent e);
public void newExperiment(IterationEvent e);
}
