/****
 *
 *	$Log: WorkRequestEvent.java,v $
 *	Revision 3.0  2002/02/03 18:41:59  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:12  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *	
 *
 */
package bunch;

public class WorkRequestEvent {

  public int [] workToDo = null;
  public int [] workPerformed = null;
  public int requestWorkSz = 0;
  public int actualWorkSz = 0;
  public int svrID = -1;
  public String svrName = "";
  public WorkRequestEvent() {
  }
}