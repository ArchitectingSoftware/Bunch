/****
 *
 *	$Log: BSTextServer.java,v $
 *	Revision 3.0  2002/02/03 18:42:05  bsmitc
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

/**
 * Title:        Bunch Version 1.2 Base<p>
 * Description:  Your description<p>
 * Copyright:    Copyright (c) 1999<p>
 * Company:      <p>
 * @author Brian Mitchell
 * @version
 */
package bunch.BunchServer;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import java.util.Properties;
import javax.naming.*;
import java.rmi.RMISecurityManager;

public class BSTextServer {

  String nameSpace = "";
  String server = "";
  String nameSvr = "";
  String port = "";
  BunchSvrMsgImpl bunchMsg = null;
  InitialContext corbaContext = null;
  String jndiName = "";


  public BSTextServer(String [] args) throws Exception {
    if (args.length != 4)
      throw(new Exception("Invalid Parameter(s), can not start text server!"));

    nameSpace = args[0];
    server = args[1];
    nameSvr = args[2];
    port = args[3];
  }

  public String getJndiName()
  {
    return jndiName;
  }

  public boolean start()
  {

    try
    {
    	Properties env = new Properties ();

	env.put("java.naming.factory.initial","com.sun.jndi.cosnaming.CNCtxFactory");

        String nsURL = "iiop://"+nameSvr+":"+port;
        System.out.println("Name Server URL: "+nsURL);

        String cnStr = "/"+nameSpace+"/"+server;
        jndiName = cnStr;
        System.out.println("Object Registration Name: " + cnStr);

	env.put("java.naming.provider.url",nsURL);

	InitialContext context = new InitialContext (env);

        //-----------------------------------------------------
        //See if this is the first time binding to a namespace
        //-----------------------------------------------------
        try{
         context.createSubcontext(nameSpace);
        }catch(Exception e1)
        {}

        CompositeName cn = new CompositeName(cnStr);

        bunchMsg = new BunchSvrMsgImpl();
        bunchMsg.setParent(null);
        bunchMsg.setJndiName(jndiName);
        bunchMsg.setTextMode();

   	context.rebind (cn, bunchMsg);

        corbaContext = context;

        System.out.println("SERVER Started OK!");

        return true;
      }
      catch (Exception ex)
      {
            String excp = ex.toString();
            System.out.println("Server exception: "+excp);
            return false;
      }
  }
}