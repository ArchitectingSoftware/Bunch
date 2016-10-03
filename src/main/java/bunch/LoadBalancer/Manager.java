/****
 *
 *	$Log: Manager.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
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
package bunch.LoadBalancer;

import java.util.*;

public class Manager {

  public final static int    RE_EVAL_FREQ = 50;
  public final static double ADJUSTMENT_THRESHOLD = 0.15;
  public final static int    MAX_WORK_MULTIPLIER = 10;
  public final static int    STABILITY_THRESHOLD = 5;

  public int baseUOWSz = 5;
  public int minUOWSz = baseUOWSz;
  public int maxUOWSz = baseUOWSz * 10;
  public boolean useAdaptiveAlg = true;

  int stabilityCounter = 0;


  Hashtable svrList = new Hashtable();

  ServerStats [] ssArray = null;
  int         svrCount = 0;
  int         reEvalTracker = 0;

  public Manager() {
  }

  public int createNewServer()
  {
    ServerStats ss = new ServerStats();
    ss.svrID = svrCount;
    ss.currUOWSz = baseUOWSz;

//System.out.println("Server base unit of work size is:  "+ss.currUOWSz);

    svrCount++;

    //This might seem inefficient to reconstruct the array everytime, but
    //array lookups will be faster than object lookups at runtime (I hope)

    ServerStats [] tmpSSArray = new ServerStats[svrCount];
    if (ssArray != null)
      System.arraycopy(ssArray,0,tmpSSArray,0,ssArray.length);

    tmpSSArray[ss.svrID] = ss;

    ssArray = tmpSSArray;

    return ss.svrID;
  }

  public boolean incrementWork(int sid)
  {
    ServerStats ss = ssArray[sid];
    ss.totalWork++;
    ss.workSinceLastAdjustment++;
    reEvalTracker++;

    if(reEvalTracker >=  RE_EVAL_FREQ)
    {
        reEvalTracker = 0;
        return adaptiveUpdate();
    }
    return true;
  }

  private boolean adaptiveUpdate()
  {
    if(ssArray == null)
      return false;

    int workCounter = 0;
    int totalServers = ssArray.length;

    if(totalServers == 0)
      return false;

    if(totalServers == 1)
    {
      ssArray[0].workSinceLastAdjustment = 0;
      return true;
    }

    boolean madeAdjustments = false;

    double avgExpectedWork = (double)RE_EVAL_FREQ / (double)totalServers;

    for(int i = 0; i < totalServers; i++)
    {
      double pctAdjustment = (double)(ssArray[i].workSinceLastAdjustment)/avgExpectedWork;
      int uowSz         = ssArray[i].currUOWSz;

      //now govern if speedup is necessary
      if (pctAdjustment >= (1.0 + ADJUSTMENT_THRESHOLD))
        pctAdjustment = 1.0 + ADJUSTMENT_THRESHOLD;

      //now govern if slowdown is necessary
      if (pctAdjustment <= (1.0 - ADJUSTMENT_THRESHOLD))
        pctAdjustment = 1.0 - ADJUSTMENT_THRESHOLD;

      int newUOWSz = (int)((double)(uowSz)*pctAdjustment);

      if (newUOWSz == uowSz)
        if(pctAdjustment > 1.0)
          newUOWSz++;
        else if(pctAdjustment < 1.0)
          newUOWSz--;

      //now check for bounds
      if(newUOWSz < baseUOWSz)
        newUOWSz = baseUOWSz;

      if(newUOWSz > (baseUOWSz * this.MAX_WORK_MULTIPLIER))
        newUOWSz = (baseUOWSz * this.MAX_WORK_MULTIPLIER);

      //debug message
      if(uowSz != newUOWSz)
      {
        madeAdjustments = true;

//        System.out.println("Adjusting work for server id: " +
//            ssArray[i].svrID+" from " + uowSz + " to " + newUOWSz);
      }

      ssArray[i].workSinceLastAdjustment = 0;
      ssArray[i].currUOWSz = newUOWSz;
    }

    if(madeAdjustments == false)
    {
      stabilityCounter++;
      if(stabilityCounter >= this.STABILITY_THRESHOLD)
      {
        stabilityCounter = 0;
        for(int i = 0; i < totalServers; i++)
        {
            ssArray[i].currUOWSz++;
//            System.out.println("Just adjusted all servers up 1 to break stalemate");
        }
      }
    }
    else
    {
      stabilityCounter = 0;
    }
    return true;
  }

  public int getCurrentUOWSz(int sid)
  {
    ServerStats ss = ssArray[sid];
    return ss.currUOWSz;
  }




}