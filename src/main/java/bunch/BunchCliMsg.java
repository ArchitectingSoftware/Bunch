/****
 *
 *	$Log: BunchCliMsg.java,v $
 *	Revision 3.0  2002/02/03 18:41:43  bsmitc
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

/**
 * This interface is used by the Bunch client in distributed node as the
 * callback entry point.  All messages received have a name to identify thier
 * type, and a serialized class to contain the resultant class.
 *
 * @author Brian Mitchell
 */
package bunch;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BunchCliMsg extends Remote{

  boolean recvMessage(String name, byte[] serializedClass) throws RemoteException;
}
