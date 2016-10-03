package bunch.api;

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author
 * @version 1.0
 */

public class BunchAsyncNotifyTest extends BunchAsyncNotify {

  Object monitor;

  public BunchAsyncNotifyTest() {
    monitor = new Object();
  }

  public void notifyDone() {

    System.out.println("We are done");
    synchronized(monitor)
    { monitor.notifyAll();  }
  }

  public void waitUntilDone()
  {
    System.out.println("Getting Ready To Wait");
    try
    {
      synchronized(monitor)
      {  monitor.wait();  }
    }catch(Exception e1)
    {e1.printStackTrace();}
  }
}