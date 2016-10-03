/****
 *
 *	$Log: BunchSvrMsg.java,v $
 *	Revision 3.0  2002/02/03 18:42:06  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:18  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */
package bunch.BunchServer;

import bunch.Callback;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BunchSvrMsg extends Remote{

  boolean invokeMessage(String name, byte[] serializedClass) throws RemoteException;
  boolean registerCallback(Callback c) throws RemoteException;
  boolean doAction(String command) throws RemoteException;
}
