/****
 *
 *	$Log: StatsManager.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *
 *	Revision 1.3  2000/08/13 18:40:33  bsmitc
 *	Added support for SA framework and dumping the output to a log file
 *
 *	Revision 1.2  2000/08/11 15:04:36  bsmitc
 *	Added support for producing optimal output on the clustering progress
 *	dialog window
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
package bunch.stats;

import java.io.*;

public class StatsManager {

    public static String logFileNm = "BunchStats.log";
    long mqCalculations = 0;
    long calcAllCalcs=0;
    long calcIncrCalcs=0;
    long exhaustiveTotal = -1;
    long exhaustiveFinished = 0;
    long simulatedAnnealingOverrides = 0;

    boolean collectClusteringDetails = false;

    //make this a singleton
    private StatsManager() {
    }

    static private StatsManager singletonObj;

    public static StatsManager getInstance() {
        if (singletonObj == null) {
            synchronized(StatsManager.class) {
                if (singletonObj == null) {
                    singletonObj = new StatsManager();
                }
            }
        }
        return singletonObj;
    }

    static public void cleanup()
    { singletonObj = null;  }

    public void setCollectClusteringDetails(boolean b)
    { collectClusteringDetails = b;}

    public boolean getCollectClusteringDetails()
    { return collectClusteringDetails;  }

    public long getMQCalculations()
    { return mqCalculations;  }

    public long incrMQCalculations()
    { return ++mqCalculations;  }

    public long incrCalcAllCalcs()
    { return ++calcAllCalcs; }

    public long getCalcAllCalcs()
    { return calcAllCalcs; }

    public long incrCalcIncrCalcs()
    { return ++calcIncrCalcs; }

    public long getCalcIncrCalcs()
    { return calcIncrCalcs; }

    public void setExhaustiveTotal(int t)
    { exhaustiveTotal = t; }

    public long getExhaustiveTotal()
    { return exhaustiveTotal; }

    public long getExhaustiveFinished()
    { return exhaustiveFinished;  }

    public void incrExhaustiveFinished()
    { exhaustiveFinished++; }

    public void clearExhaustiveFinished()
    { exhaustiveFinished = 0; }

    public int getExhaustivePct()
    {
      if(exhaustiveTotal <= 0) return 0;

      double pct = (double)exhaustiveFinished/(double)exhaustiveTotal;
      pct *= 100.0;
      int iPct = (int)pct;
      return iPct;
    }

    public long getSAOverrides()
    { return simulatedAnnealingOverrides;  }

    public void incrSAOverrides()
    { simulatedAnnealingOverrides++; }

    public boolean dumpStatsLog()
    {
      try
      {
          BufferedWriter writer = new BufferedWriter(new FileWriter(logFileNm));
            writer.write("Total MQ Calculations:  " + mqCalculations + "\n");
            writer.write("Simulated Annealing Overrides: " + simulatedAnnealingOverrides + "\n");
          writer.close();
      }
      catch(Exception e)
      {
        System.out.println("Error creating the logfile at location: " + logFileNm);
        return false;
      }
      return true;
    }
}