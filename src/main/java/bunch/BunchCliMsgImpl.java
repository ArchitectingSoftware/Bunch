/****
 *
 *	$Log: BunchCliMsgImpl.java,v $
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
package bunch;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import java.util.Properties;
import javax.naming.*;
import java.rmi.RMISecurityManager;

/**
 * This class implements the BunchCliMsg interface.  For the current distribtued
 * serivces of Bunch, this clss is not used.  Instead of direct reverse calls the
 * Callback class is used.
 *
 * All distributed operations are initiated by the Bunch Client and all responses
 * are by asynchronous callback.
 *
 * @author Brian Mitchell
 */
public class BunchCliMsgImpl extends PortableRemoteObject implements BunchCliMsg{

  public BunchCliMsgImpl() throws RemoteException {
  }

  public boolean recvMessage(String name, byte[] serializedClass)
  {
      return true;
  }
}