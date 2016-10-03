/****
 *
 *	$Log: Callback.java,v $
 *	Revision 3.0  2002/02/03 18:41:45  bsmitc
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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used to wrapper the various types of callbacks from the
 * client or server that must be handled.  At the minimum each method in the
 * implementation has at lease a string input indicating the message type.
 *
 * @author Brian Mitchell
 * @see    CallbackImpl
 */
public interface Callback extends Remote
{
   byte[] callFromServer(String input) throws RemoteException;
   byte[] callFromServerWithObj(String input, byte[]so) throws RemoteException;
   boolean bCallFromServerWithObj(String input, byte[]so) throws RemoteException;
   boolean bCallFromServer(String input) throws RemoteException;
}
