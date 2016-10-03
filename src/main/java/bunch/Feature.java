/****
 *
 *	$Log: Feature.java,v $
 *	Revision 3.0  2002/02/03 18:41:48  bsmitc
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
 * An interface used to create procedures that can extend other processes
 * generically. The objects that implement this interface will be serializable.
 *
 * @author Brian Mitchell
 *
 * @see bunch.Configuration
 */
public
interface Feature
  extends java.io.Serializable
{
/**
 * The method to be implemented by classes that use this interface.
 */
public
void
apply(Object o);
}
