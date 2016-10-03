/****
 *
 *	$Log: BunchEvent.java,v $
 *	Revision 3.0  2002/02/03 18:41:44  bsmitc
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


/**
 * This class encapsulates BunchEvents which are used for asynchronous
 * interaction between the BunchComponents.  Each Event has a unique event
 * id
 *
 * @author Brian Mitchell
 */
public class BunchEvent {

  public BunchEvent() {
  }
  public static final int EVENT_GOTWORK     = 1;
  public static final int EVENT_SERVERDONE = 2;

  public static final int EVENT_STATE_NOT_INIT  = -1;
  public static final int EVENT_STATE_SUBMITTED = 1;
  public static final int EVENT_STATE_PENDING   = 2;
  public static final int EVENT_STATE_PROCESSED = 3;

  private int theEvent;
  private Object eventObj = null;
  private Object notifyObj = null;
  private Thread submitterThread = null;
  private int eventState = EVENT_STATE_NOT_INIT;

  /**
   * Set the event state
   *
   * @param The event state based on one of the constants
   */
  public void setEventState(int state)
  {   eventState = state;   }

  /**
   * Get the event state
   */
  public int getEventState()
  {   return eventState; }

  /**
   * Set the event object.  This object implements the functionality of the
   * actual bunch event
   */
  public void setEventObj(Object eo)
  {   eventObj = eo; }

  /**
   * Set the object that is to be notified when an event arrives
   */
  public void setNotifyObj(Object no)
  {   notifyObj = no;   }

  /**
   * Get the notifiy object instance
   */
  public Object getNotifyObj()
  {   return notifyObj; }

  /**
   * Get the thread of the submitter.  This way we can suspend it to
   * simulate synchronous operations.
   */
  public Thread getSubmitterThread()
  {   return submitterThread; }

  /**
   * Set the submitter thread. This way we can suspend it to simulate
   * synchronous operations.
   */
  public void setSubmitterThread(Thread st)
  {   submitterThread = st;   }

  /**
   * Get the event object instance
   */
  public Object getEventObj()
  {   return eventObj; }

  /**
   * Set the ID of the event.  The event ID must be unique.
   */
  public void setEventID(int e)
  {   theEvent = e; }

  /**
   * Get the ID of the event
   */
  public int getEventID()
  {   return theEvent;  }

  /**
   * Set the event.
   *
   * @param e   The Event ID
   * @param eo The Event Object
   */
  public void setEvent(int e, Object eo)
  {
      theEvent = e;
      eventObj = eo;
  }
}
