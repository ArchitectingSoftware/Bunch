
/**
 * Title:        Bunch Project<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Brian Mitchell<p>
 * Company:      Drexel University - SERG<p>
 * @author Brian Mitchell
 * @version 1.0
 */

/****
 *
 *	$Log: BunchAPI.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *
 *	Revision 3.3  2001/03/17 14:55:47  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.2  2000/11/30 01:49:21  bsmitc
 *	Added support for various tests and statistical gathering
 *
 *	Revision 3.1  2000/11/26 15:44:36  bsmitc
 *	Various bug patchs
 *
 *	Revision 3.0  2000/10/22 16:14:00  bsmitc
 *	Changed version number to 3.0 to sync with rest of project
 *
 *	Revision 1.1.1.1  2000/10/22 16:05:57  bsmitc
 *	Initial Version
 *
 *
 */
package bunch.api;

import bunch.engine.*;
import bunch.*;
import java.util.*;
import java.io.*;

public class BunchAPI {

  public static final String  CALLBACK_OBJECT_REF = "CallbackObjectReference";
  public static final String  CALLBACK_OBJECT_FREQ = "CallbackObjectFrequency";

  public static final String  RUNTIME             = "Runtime";
  public static final String  MQEVALUATIONS       = "MQEvaluations";

  public static final String  CLUSTER_LEVEL       = "ClusterLevel";
  public static final String  MQVALUE             = "MQValue";
  public static final String  CLUSTER_DEPTH       = "BestClusterDepth";
  public static final String  NUMBER_CLUSTERS     = "BestPartitionClusters";

  public static final String  TOTAL_CLUSTER_LEVELS= "TotalClusterLevels";
  public static final String  RESULT_CLUSTER_OBJS = "ResultClusterObjects";
  public static final String  SA_NEIGHBORS_TAKEN  = "SANeighborsTaken";
  public static final String  MEDIAN_LEVEL_GRAPH  = "MedianLevelGraph";

  public static final String  PR_PRECISION_VALUE  = "PRPrecisionValue";
  public static final String  PR_RECALL_VALUE     = "PRRecallValue";

  public static final String  MQCALC_RESULT_VALUE = "MQCalcResultValue";

  public static final String  ERROR_HASHTABLE     = "ErrorHashtable";
  public static final String  WARNING_HASHTABLE   = "WarningHashtable";
    public static final String REFLEXIVE_EDGE_COUNT = "ReflexiveEdgeCount";

  public static final String OMNIPRESENT_CLIENT = "OmnipresentClient";
  public static final String OMNIPRESENT_SUPPLIER = "OmnipresentSupplier";
  public static final String OMNIPRESENT_CENTRAL = "OmnipresentCentral";
  public static final String LIBRARY_MODULE = "LibraryModule";



  BunchProperties   bunchProps;
  Hashtable         bunchArgs;
  Hashtable         resultsHashtable=null;
  ProgressCallback  progressCB = null;
  int               progressUpdateFreq=1000;
  BunchEngine       engine;

  public BunchAPI() {
    bunchArgs = null;
    engine = new BunchEngine();
  }

  Hashtable loadHTFromProperties(BunchProperties bp)
  {
    Hashtable h = new Hashtable();
    Enumeration e = bp.propertyNames();
    while(e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      String value = bp.getProperty(key);
      h.put(key,value);
    }

    String HCPct = (String)h.get(BunchProperties.ALG_NAHC_HC_PCT);
    if(HCPct != null)
    {
      Integer pct = new Integer(HCPct);
      h.put(BunchProperties.ALG_NAHC_HC_PCT,pct);
      String rndPct = (String)h.get(BunchProperties.ALG_NAHC_RND_PCT);
      if(rndPct != null)
      {
        Integer iRndPct = new Integer(rndPct);
        h.put(BunchProperties.ALG_NAHC_RND_PCT,iRndPct);
      }
    }

    String TimeoutTime = (String)h.get(BunchProperties.TIMEOUT_TIME);
    if(TimeoutTime != null)
    {
      Integer toTime = new Integer(TimeoutTime);
      h.put(BunchProperties.TIMEOUT_TIME,toTime);
    }

    String NAHCPop = (String)h.get(BunchProperties.ALG_NAHC_POPULATION_SZ);
    if(NAHCPop != null)
    {
      Integer pop = new Integer(NAHCPop);
      h.put(BunchProperties.ALG_NAHC_POPULATION_SZ,pop);
    }

    String SAHCPop = (String)h.get(BunchProperties.ALG_SAHC_POPULATION_SZ);
    if(SAHCPop != null)
    {
      Integer pop = new Integer(SAHCPop);
      h.put(BunchProperties.ALG_SAHC_POPULATION_SZ,pop);
    }


    return h;
  }

  public void reset()
  {
    if(bunchArgs != null)
    {
      bunchArgs.clear();
      bunchArgs = null;
    }
  }

  public void setProperties(BunchProperties bp)
  {
      bunchProps = bp;
      Hashtable    htArgs = loadHTFromProperties(bp);
      if(bunchArgs == null)
        bunchArgs = htArgs;
      else
        bunchArgs.putAll(htArgs);
  }

  public void setAPIProperty(Object key, Object value)
  {
    if(bunchArgs == null) bunchArgs = new Hashtable();
    bunchArgs.put(key,value);
  }

  public Object removeAPIProperty(Object key)
  {
    if(bunchArgs != null)
      return bunchArgs.remove(key);
    return null;
  }

  public void setProperties(String fileName) throws FileNotFoundException, IOException
  {
    bunchProps = new BunchProperties();
    bunchProps.load(new FileInputStream(fileName));
    bunchArgs = loadHTFromProperties(bunchProps);
  }

  public void setProperties(InputStream in) throws IOException
  {
    bunchProps = new BunchProperties();
    bunchProps.load(in);
    bunchArgs = loadHTFromProperties(bunchProps);
  }

  public boolean validate()
  {
    boolean rc = true;

    if(bunchProps.getProperty(BunchProperties.MDG_INPUT_FILE_NAME) == null)
      rc = false;

    if(bunchProps.getProperty(BunchProperties.MDG_OUTPUT_FILE_BASE) == null)
    {
      if (bunchProps.getProperty(BunchProperties.OUTPUT_DEVICE).equalsIgnoreCase(BunchProperties.OUTPUT_FILE))
        rc = false;
    }
    return rc;
  }

  public void setProgressCallback(ProgressCallback cb)
  {
    String sFreq = (String)bunchProps.getProperty(bunchProps.PROGRESS_CALLBACK_FREQ);
    Integer i = new Integer(sFreq);
    setProgressCallback(cb,i.intValue());
  }

  public void setProgressCallback(ProgressCallback cb, int freqUpdate)
  {
    progressCB = cb;
    progressUpdateFreq = freqUpdate;
  }

  //public Hashtable getResultsHashtable()
  //{
  //  return resultsHashtable;
  //}

  public Hashtable getResults()
  {
    return engine.getResultsHT();
  }

  public Hashtable getSpecialModules(String mdgFileName)
  {
    return engine.getDefaultSpecialNodes(mdgFileName);
  }

  public Hashtable getSpecialModules(String mdgFileName,double threshold)
  {
    return engine.getDefaultSpecialNodes(mdgFileName,threshold);
  }

  public boolean run()
  {
    boolean rc = true;
    resultsHashtable = new Hashtable();
    if(progressCB != null){
      bunchArgs.put(CALLBACK_OBJECT_REF,progressCB);
      bunchArgs.put(CALLBACK_OBJECT_FREQ,new Integer(progressUpdateFreq));
    }

    engine = new BunchEngine();
    engine.run(bunchArgs);
    return rc;
  }

  public void setDebugStats(boolean b)
  {
    engine.setDebugStats(b);
  }

  public ArrayList getClusters()
  {
    return engine.getClusterList();
  }

  public BunchGraph getPartitionedGraph()
  {
    return getPartitionedGraph(0);
  }

  public ArrayList  getPartitionedBunchGraphs()
  {
    Graph baseGraph = engine.getBestGraph();
    if (baseGraph == null) return null;

    int maxLvl = baseGraph.getGraphLevel();
    if (maxLvl < 0)
      return null;

    BunchGraph []bgA = new BunchGraph[maxLvl+2];

    Graph g = baseGraph;
    while(g.getGraphLevel()>0)
    {
      BunchGraph bg = new BunchGraph();
      boolean rc = bg.construct(g);
      if (rc == false) return null;
      int lvl = g.getGraphLevel();
      bg.setGraphLevel(lvl+1);
      bgA[lvl+1] = bg;
      g = g.getPreviousLevelGraph();
    }

    //panic:  This is not good
    if(g.getGraphLevel() != 0) return null;
    BunchGraph bg = new BunchGraph();
    boolean rc = bg.construct(g);
    if (rc == false) return null;
    bgA[1] = bg;

    int medLevel = Math.max((maxLvl/2),0);
    medLevel++;
    bgA[0] = bgA[medLevel];

    ArrayList al = new ArrayList(bgA.length);
    for(int i = 0; i < bgA.length; i++)
      al.add(i,bgA[i]);

    return al;
  }

  public BunchGraph getPartitionedGraph(int Level)
  {
    Graph baseGraph = engine.getBestGraph();
    if (baseGraph == null) return null;

    int lvl = baseGraph.getGraphLevel();
    if ((Level < 0) || (Level > lvl))
      return null;

    Graph g = baseGraph;
    while(g.getGraphLevel()>Level)
      g = g.getPreviousLevelGraph();

    BunchGraph bg = new BunchGraph();
    boolean rc = bg.construct(g);
    if (rc == false) return null;

    return bg;
  }
}