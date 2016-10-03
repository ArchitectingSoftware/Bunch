/****
 *
 *	$Log: CallbackImpl.java,v $
 *	Revision 3.0  2002/02/03 18:41:45  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import bunch.BunchServer.IterationManager;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;

/**
 * This class implements the callback functionality that is used by the
 * distributed version of Bunch.
 *
 * @author Brian Mitchell
 */
public class CallbackImpl extends PortableRemoteObject implements Callback{

  /**
   * Constants for message types
   */
  public static final String GET_NEXT_VECTOR = "GET_NEXT_VECTOR";
  public static final String BEST_ITERATION_VECTOR = "BEST_ITERATION_VECTOR";

  int    MsgIdCtr = 0;
  public int baseUOWSz = 5;
  public BunchEvent bevent = null;
  public SynchronizedEventQueue  eventQ = null;
  public bunch.LoadBalancer.Manager lbManager = null;

  public CallbackImpl() throws RemoteException{
  }

  /**
   * This method implments a simple call from a server with a simple operation.  No
   * encoded object is passed on the interface.  Currently no messages are implemented
   * using this method.
   */
  public byte[]  callFromServer(String data)
  {
      System.out.println("Thread: " + Thread.currentThread().getName()+"  CALLBACK RECEIVED FROM SERVER:  " + data);
      return null;
  }

  /**
   * This method accepts a call from the server with an event, executes the event
   * and sends a serialized object back to the caller.
   *
   * @param input The command name
   * @param so    The input serialized object containing the command
   *
   * @returns     A serialized object containing the response object
   */
  public byte[] callFromServerWithObj(String input, byte[]so)
  {
    /**
     * Handle the get next vector command which involves sending more work
     * to a requesting server
     */
    if(input.equals(GET_NEXT_VECTOR))
    {
      try
      {
        BunchEvent bevt = new BunchEvent();

        /**
         * The serialized event is an InterationManager instance for this type
         * of message
         */
        IterationManager im = (IterationManager)bunch.util.BunchUtilities.fromByteArray(so);

        /**
         * Build a work request event
         */
        WorkRequestEvent wre = new WorkRequestEvent();
        lbManager.incrementWork(im.svrID);
        wre.requestWorkSz=lbManager.getCurrentUOWSz(im.svrID);       //baseUOWSz;
        wre.workPerformed = im.workVector;
        wre.svrID = im.svrID;
        wre.svrName = im.jndiServerName;
        bevt.setEventObj(wre);
        bevt.setSubmitterThread(Thread.currentThread());

        /**
         * Submit the event and wait for a response.
         */
        bevt = eventQ.putAndWait(bevt);

        /**
         * Return the work vial the clusterVector member of the iteration manager
         * object instance.  Get this value from the work request event instance.
         */
        im.clusterVector = wre.workToDo;
        im.uowSz = lbManager.getCurrentUOWSz(im.svrID);
        //DEBUGDump(im);

        /**
         * Create the return serialized object and return it to the caller.
         */
        byte [] soRet = bunch.util.BunchUtilities.toByteArray(im);
        return soRet;
      }
      catch(Exception ex)
      {
        System.out.println("EXCEPTION - callFromServerWithObj():  " + ex.toString());
      }
    }
    return null;
  }


  /**
   * This method accepts a call from the server with an event, executes the event
   * and sends a boolean response
   *
   * @param input The command name
   * @param so    The input serialized object containing the command
   *
   * @returns     A boolean status code
   */
  public boolean bCallFromServerWithObj(String input, byte[]so)
  {
    /**
     * For this message the best iteration vector is being provided by
     * a server.
     */
    if(input.equals(BEST_ITERATION_VECTOR))
    {
      try
      {
        BunchEvent bevt = new BunchEvent();

        IterationManager im = (IterationManager)bunch.util.BunchUtilities.fromByteArray(so);

        //DEBUGBiDump(im);

        /**
         * Create a work finished event to collect the parameters from the server.
         */
        WorkFinishedEvent wfe = new WorkFinishedEvent();
        wfe.clusterVector = im.workVector;
        bevt.setEventObj(wfe);
        bevt.setSubmitterThread(Thread.currentThread());

        /**
         * Execute the event and wait for a response.
         */
        bevt = eventQ.putAndWait(bevt);
        return true;
      }
      catch(Exception ex)
      {
        System.out.println("EXCEPTION - bCallFromServerWithObj():  " + ex.toString());
      }
    }
    return false;
  }

  /**
   * This method implments a simple call from a server with a simple operation.  No
   * encoded object is passed on the interface.  Currently no messages are implemented
   * using this method.
   */
  public boolean bCallFromServer(String input)
  {
    return true;
  }

  /**
   * This debugging routine is dumps the contents of the Iteration Manager object to
   * standard output.
   *
   * @param im  The InterationManager instance.
   */
  public synchronized void DEBUGDump(IterationManager im)
  {
      System.out.print(im.jndiServerName+": [ ");
      if(im.clusterVector == null)
         System.out.print("no work");
      else
         for(int i = 0; i < im.clusterVector.length; i++)
            System.out.print(im.clusterVector[i] + " ");
      System.out.println("]");
  }

  /**
   * This debugging routine is dumps the contents of the Iteration Manager object to
   * standard output for the BestInteration message type.
   *
   * @param im  The InterationManager instance.
   */
  public synchronized void DEBUGBiDump(IterationManager im)
  {
      System.out.println("***********************************************");
      System.out.println("Server:  " + im.jndiServerName + " just reported a work finished event");
      System.out.println("***********************************************");
  }
}