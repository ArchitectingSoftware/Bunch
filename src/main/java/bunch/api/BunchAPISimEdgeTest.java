/****
 *
 *	$Log: BunchAPISimEdgeTest.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.3  2001/03/17 14:55:47  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.2  2000/11/30 03:08:12  bsmitc
 *	Updated test case
 *
 *	Revision 3.1  2000/11/30 01:49:21  bsmitc
 *	Added support for various tests and statistical gathering
 *
 *
 */

 package bunch.api;

 import java.util.*;
 import java.io.*;

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author Brian Mitchell
 * @version 1.0
 */

public class BunchAPISimEdgeTest {

long totalNodes;
long totalAdjustments;
ArrayList bunchGraphs = null;

int [] esfreq = new int[11];
int [] esIfreq = new int [11];
int [] prfreq = new int[11];
int [] prIfreq = new int [11];
int [] meclFreq = new int [11];
int [] meclIFreq = new int[11];

String mode = "NAHC";

  public BunchAPISimEdgeTest()
  {
    String graphName = "d:\\linux\\linux"; //"e:\\expir\\grappa"; //"e:\\linux\\linux"; //"e:\\expir\\compiler";
    mode = "NAHC";

    System.out.println("***** G R A P H   N A M E :   "+graphName+"\n");
    writeHeader();
    runTest(graphName, false);
    runTest(graphName, true);
  }

  public void runTest(String graphName, boolean removeSpecial)
  {
    totalNodes = totalAdjustments = 0;
    bunchGraphs = new ArrayList();
    boolean removeSpecialModules = removeSpecial;

    for(int i = 0; i < 10; i++)
    {
      this.runClustering(graphName, removeSpecialModules);
      //this.runClustering("e:\\linux\\linux");
    }
    double avgValue = expirPR(prfreq);
    double avgMeclValue = expirMecl(meclFreq);
    double avgESValue = expirES(esfreq);
    double avgIsomorphicValue = expirIsomorphicPR();
    double avgMeclIValue = expirMecl(meclIFreq);
    double avgESIValue = expirES(esIfreq);
    BunchGraph bg = (BunchGraph)bunchGraphs.get(0);
    double avgIsomorphicCount = expirIsomorphicCount();

    //writeHeader();
    if(removeSpecial == false)
    {
      dumpFreqArray("PR (BASELINE)  ", prfreq,avgValue,avgIsomorphicCount);
      dumpFreqArray("MECL(BASELINE) ", meclFreq,avgMeclValue,avgIsomorphicCount);
      dumpFreqArray("ES(BASELINE)   ", esfreq,avgESValue,avgIsomorphicCount);
      dumpFreqArray("PR (NO ISO)    ", prIfreq,avgIsomorphicValue,avgIsomorphicCount);
      dumpFreqArray("MECL(NO ISO)   ", meclIFreq,avgMeclIValue,avgIsomorphicCount);
      dumpFreqArray("ES(NO ISO)     ", esIfreq,avgESIValue,avgIsomorphicCount);
    }
    else
    {
      dumpFreqArray("PR (NO SPEC)   ", prfreq,avgValue, avgIsomorphicCount);
      dumpFreqArray("MECL(NO SPEC)  ", meclFreq,avgMeclValue,avgIsomorphicCount);
      dumpFreqArray("ES(NO SPEC)    ", esfreq,avgESValue,avgIsomorphicCount);
      dumpFreqArray("PR (NO S&I)    ",prIfreq,avgIsomorphicValue,avgIsomorphicCount);
      dumpFreqArray("MECL(NO S&I)   ", meclIFreq,avgMeclIValue,avgIsomorphicCount);
      dumpFreqArray("ES(NO S&I)     ", esIfreq,avgESIValue,avgIsomorphicCount);
    }
    //System.out.println("***** Graph Size: "+ bg.getNodes().size());
    //System.out.println("***** Special Modules Removed:   " + removeSpecialModules);
    //System.out.println("***** AVERAGE ISOMORPHIC COUNT:  " + avgIsomorphicCount);
    //System.out.println("***** AVERAGE PR FOR ALL RUNS:   " + avgValue);
    //System.out.println("***** AVERAGE ISOMORPHIC PR FOR ALL RUNS:  " + avgIsomorphicValue);
    //double pct = (double)totalAdjustments / (double)totalNodes;
    //System.out.println("***** ("+pct+") Total Nodes: "+totalNodes+"  Total Adjustments: "+totalAdjustments);
  }

  private void writeHeader()
  {
    System.out.println("                 |-------------------------------- F R E Q U E N C Y --------------------------------|");
    System.out.println("                   0-9   10-19   20-29   30-39   40-49   50-59   60-69   70-79   80-89   90-99     100     AVG  AVG-ISO");
    System.out.println("                 =====   =====   =====   =====   =====   =====   =====   =====   =====   =====   =====    ====  =======");
  }

  private void dumpFreqArray(String lbl, int []a, double avgValue, double avgIso)
  {
    StringBuffer sb = new StringBuffer("      ");
    System.out.print(lbl+" [");
    for(int i = 0; i < a.length; i++)
    {
      Integer count = new Integer(a[i]);
      String scnt = count.toString();
      StringBuffer sbItem = new StringBuffer(sb.toString());
      sbItem.replace((sbItem.length()-scnt.length()-1),sbItem.length()-1,scnt);
      System.out.print(sbItem);
      if(i < (a.length-1))
        System.out.print("  ");
    }
    System.out.print("] ");

    int avg = (int)(avgValue*100.0);
    if(avg < 100)
      avg++;
    Integer avgI = new Integer(avg);
    String scnt = avgI.toString();
    StringBuffer sbItem = new StringBuffer(sb.toString());
    sbItem.replace((sbItem.length()-scnt.length()-1),sbItem.length()-1,scnt);
    System.out.print(sbItem);

    int avgIsoI = (int)(avgIso);
    avgI = new Integer(avgIsoI);
    scnt = avgI.toString();
    sbItem = new StringBuffer(sb.toString());
    sbItem.replace((sbItem.length()-scnt.length()-1),sbItem.length()-1,scnt);
    System.out.println("   "+sbItem);
  }


  private double expirIsomorphicPR()
  {
    for(int i = 0; i < bunchGraphs.size(); i++)
    {
      BunchGraph g = (BunchGraph)bunchGraphs.get(i);
      g.determineIsomorphic();
    }
    return expirPR(prIfreq);
  }

  private double expirIsomorphicCount()
  {
    int accum = 0;
    for(int i = 0; i < bunchGraphs.size(); i++)
    {
      BunchGraph g = (BunchGraph)bunchGraphs.get(i);
      accum+=g.getTotalOverlapNodes();
    }
    return ((double)accum/(double)bunchGraphs.size());
  }

  private void clearDistArray(int []distArray)
  {
    for(int i = 0; i < distArray.length; i++)
      distArray[i] = 0;
  }

  private int findIndex(double value)
  {
    if((value < 0)||(value > 1.0))
      return 0;

    double tmp = value * 100.0;
    int    iTmp = (int)tmp;
    iTmp /= 10;
    return iTmp;
  }

  private double expirES(int []distArray)
  {
    long trials = 0;
    double accum = 0.0;

    clearDistArray(distArray);
    for(int i = 0; i < bunchGraphs.size(); i++)
    {
      BunchGraph g1 = (BunchGraph)bunchGraphs.get(i);
      for(int j = i; j < bunchGraphs.size(); j++)
      {
        BunchGraph g2 = (BunchGraph)bunchGraphs.get(j);

        Double prValue = new Double(BunchGraphUtils.calcEdgeSimiliarities(g1,g2));

        /***************
         * This block of code is for Precision/Recall Analysis

        Hashtable results = BunchGraphUtils.calcPR(g1,g2);
        Double prValue = (Double)results.get("AVERAGE");
        String prsValue = "null";
        if(prsValue != null)
          prsValue = prValue.toString();
        else
          prValue = new Double(0.0);
        */

        //System.out.println("AVG_PR(graph "+i+", graph"+j+") = "+prsValue);
        if (i != j)
        {
          trials++;
          int idx = this.findIndex(prValue.doubleValue());
          distArray[idx]++;
          accum+=prValue.doubleValue();
        }
      }
    }
    return ((double)accum/(double)trials);
  }

  private double expirPR(int []distArray)
  {
    long trials = 0;
    double accum = 0.0;

    clearDistArray(distArray);
    for(int i = 0; i < bunchGraphs.size(); i++)
    {
      BunchGraph g1 = (BunchGraph)bunchGraphs.get(i);
      for(int j = i; j < bunchGraphs.size(); j++)
      {
        BunchGraph g2 = (BunchGraph)bunchGraphs.get(j);

        //Double prValue = new Double(BunchGraphUtils.calcEdgeSimiliarities(g1,g2));

        Hashtable results = BunchGraphUtils.calcPR(g1,g2);
        Double prValue = (Double)results.get("AVERAGE");
        String prsValue = "null";
        if(prsValue != null)
          prsValue = prValue.toString();
        else
          prValue = new Double(0.0);


        //System.out.println("AVG_PR(graph "+i+", graph"+j+") = "+prsValue);
        if (i != j)
        {
          trials++;
          int idx = this.findIndex(prValue.doubleValue());
          distArray[idx]++;
          accum+=prValue.doubleValue();
        }
      }
    }
    return ((double)accum/(double)trials);
  }

  private double expirMecl(int []distArray)
  {
    long trials = 0;
    double accum = 0.0;

    clearDistArray(distArray);
    for(int i = 0; i < bunchGraphs.size(); i++)
    {
      BunchGraph g1 = (BunchGraph)bunchGraphs.get(i);
      for(int j = i; j < bunchGraphs.size(); j++)
      {
        BunchGraph g2 = (BunchGraph)bunchGraphs.get(j);
        Hashtable meClValue1 = BunchGraphUtils.getMeClMeasurement(g1,g2);
        Hashtable meClValue2 = BunchGraphUtils.getMeClMeasurement(g2,g1);

        //System.out.println("The distance is:  " + meClValue.get(BunchGraphUtils.MECL_VALUE) +
        //            "   quality = "+meClValue.get(BunchGraphUtils.MECL_QUALITY_METRIC));

        Double meclValue1 = (Double)meClValue1.get(BunchGraphUtils.MECL_QUALITY_METRIC);
        Double meclValue2 = (Double)meClValue2.get(BunchGraphUtils.MECL_QUALITY_METRIC);
        double d1 = meclValue1.doubleValue();
        double d2 = meclValue2.doubleValue();

        double d = Math.max(d1,d2);

        Double  meclValue = new Double(d);

        if (i != j)
        {
          trials++;
          int idx = this.findIndex(meclValue.doubleValue());
          distArray[idx]++;
          accum+=meclValue.doubleValue();
        }
      }
    }
    return ((double)accum/(double)trials);
  }

  public void runClustering(String mdgFileName, boolean removeSpecialNodes)
  {
      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();
      bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdgFileName);

      Hashtable htSpecial = api.getSpecialModules(mdgFileName);

      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.TEXT_OUTPUT_FORMAT);

      if(mode.equals("SAHC"))
      {
        bp.setProperty(BunchProperties.ALG_HC_HC_PCT,"100");
        bp.setProperty(BunchProperties.ALG_HC_RND_PCT,"0");
      }

      if(removeSpecialNodes)
        api.setAPIProperty(BunchProperties.SPECIAL_MODULE_HASHTABLE,htSpecial);

      api.setProperties(bp);
      api.run();
      Hashtable results = api.getResults();
      String sMedLvl = (String)results.get(BunchAPI.MEDIAN_LEVEL_GRAPH);
      Integer iMedLvl = new Integer(sMedLvl);

      //===============================================================
      //We could have used any level we want to here.  The median level
      //is often interesting however the parameter can be in the range
      //of 0 < level < BunchAPI.TOTAL_CLUSTER_LEVELS
      //===============================================================
      BunchGraph bg = api.getPartitionedGraph(iMedLvl.intValue());
      //printBunchGraph(bg);
      //findIsomorphic(bg);

      bunchGraphs.add(bg);
      /*
      try
      {  bg.writeSILFile("e:\\linux.sil",true); }
      catch(Exception e)
      {  e.printStackTrace(); }
      */
  }

  public void findIsomorphic(BunchGraph bg)
  {
    Iterator nodeI = bg.getNodes().iterator();
    ArrayList theClusters = new ArrayList(bg.getClusters());
    int adjustCount = 0;
    int nodeAdjustCount = 0;
    int totalCount = bg.getNodes().size();
    boolean nodeIsomorphic = false;
    while(nodeI.hasNext())
    {
      BunchNode bn = (BunchNode)nodeI.next();
      nodeIsomorphic = false;
      int[] cv = howConnected(bg,bn);
      printConnectVector(bn,cv);

      int currClust = bn.getCluster();
      int currStrength = cv[currClust];
      BunchCluster homeCluster = (BunchCluster)theClusters.get(currClust);
      for(int i = 0; i < cv.length; i++)
      {
        if(i == currClust) continue;
        int connectStrength = cv[i];
        if(connectStrength == currStrength)
        {
          BunchCluster bc = (BunchCluster)theClusters.get(i);
          bc.addOverlapNode(bn);
          adjustCount++;
          nodeIsomorphic = true;
          //System.out.println("Node "+bn.getName()+" in cluster "+
          //    homeCluster.getName() +" is isomorphic to cluster "+ bc.getName());
        }
      }
      if(nodeIsomorphic == true) nodeAdjustCount++;
    }
    System.out.println("Adjustments = Nodes: "+nodeAdjustCount+" --> "+adjustCount+"/"+totalCount);
    totalNodes+=totalCount;
    totalAdjustments+=nodeAdjustCount; //adjustCount;
  }

  public void printConnectVector(BunchNode bn, int[] cv)
  {
    String status = "OK:";
    String nodeName = bn.getName();
    int    nodeCluster = bn.getCluster();
    int    homeStrength = cv[nodeCluster];
    String cvStr = "";
    for(int i = 0; i < cv.length; i++)
    {
      String modifier = "";
      int cstr = cv[i];
      if(i == nodeCluster) modifier = "*";
      if(i != nodeCluster)
      {
        if(cstr > homeStrength)
        {
          modifier = ">";
          status = "BAD:";
        }
        if(cstr < homeStrength) modifier = "<";
        if(cstr == homeStrength)
        {
          if(!status.equals("BAD:"))
            status = "ISOMORPHIC:";
          modifier = "=";
        }
      }
      Integer idx = new Integer(i);
      Integer clustStrength = new Integer(cv[i]);
      cvStr += "("+modifier+clustStrength.toString()+")";
    }
    //System.out.println(status+" "+nodeName+" Cluster: "+nodeCluster+":  "+cvStr);
  }

  public int[] howConnected(BunchGraph bg, BunchNode bn)
  {
    int howManyClusters = bg.getClusters().size();
    int [] connectVector = new int[howManyClusters];
    Iterator fdeps = null;
    Iterator bdeps = null;

    for(int i=0; i<connectVector.length;i++)
      connectVector[i] = 0;

    if (bn.getDeps() != null)
    {
      fdeps = bn.getDeps().iterator();
      while(fdeps.hasNext())
      {
        BunchEdge be = (BunchEdge)fdeps.next();
        BunchNode target = be.getDestNode();
        int targetCluster = target.getCluster();
        connectVector[targetCluster]++;
      }
    }


    if (bn.getBackDeps() != null)
    {
      bdeps = bn.getBackDeps().iterator();
      while(bdeps.hasNext())
      {
        BunchEdge be = (BunchEdge)bdeps.next();
        BunchNode target = be.getSrcNode();
        int targetCluster = target.getCluster();
        connectVector[targetCluster]++;
      }
    }

    return connectVector;
  }

  public void printBunchGraph(BunchGraph bg)
  {
    Collection nodeList = bg.getNodes();
    Collection edgeList = bg.getEdges();
    Collection clusterList = bg.getClusters();

    //======================================
    //PRINT THE GRAPH LEVEL INFORMATION
    //======================================
    System.out.println("PRINTING BUNCH GRAPH\n");
    System.out.println("Node Count:         " + nodeList.size());
    System.out.println("Edge Count:         " + edgeList.size());
    System.out.println("MQ Value:           " + bg.getMQValue());
    System.out.println("Number of Clusters: " + bg.getNumClusters());
    System.out.println();

    //======================================
    //PRINT THE NODES AND THIER ASSOCIATED
    //EDGES
    //======================================
    Iterator nodeI = nodeList.iterator();

    while(nodeI.hasNext())
    {
      BunchNode bn = (BunchNode)nodeI.next();
      Iterator fdeps = null;
      Iterator bdeps = null;

      System.out.println("NODE:         " + bn.getName());
      System.out.println("Cluster ID:   " + bn.getCluster());

      //PRINT THE CONNECTIONS TO OTHER NODES
      if (bn.getDeps() != null)
      {
        fdeps = bn.getDeps().iterator();
        while(fdeps.hasNext())
        {
          BunchEdge be = (BunchEdge)fdeps.next();
          String depName = be.getDestNode().getName();
          int weight = be.getWeight();
          System.out.println("   ===> " + depName+" ("+weight+")");
        }
      }

      //PRINT THE CONNECTIONS FROM OTHER NODES
      if (bn.getBackDeps() != null)
      {
        bdeps = bn.getBackDeps().iterator();
        while(bdeps.hasNext())
        {
          BunchEdge be = (BunchEdge)bdeps.next();
          String depName = be.getSrcNode().getName();
          int weight = be.getWeight();
          System.out.println("   <=== " + depName+" ("+weight+")");
        }
      }
      System.out.println();
    }

    //======================================
    //NOW PRINT THE INFORMATION ABOUT THE
    //CLUSTERS
    //======================================
    System.out.println("Cluster Breakdown\n");
    Iterator clusts = bg.getClusters().iterator();
    while(clusts.hasNext())
    {
      BunchCluster bc = (BunchCluster)clusts.next();
      System.out.println("Cluster id:   " + bc.getID());
      System.out.println("Custer name:  " + bc.getName());
      System.out.println("Cluster size: " +bc.getSize());

      Iterator members = bc.getClusterNodes().iterator();
      while(members.hasNext())
      {
        BunchNode bn = (BunchNode)members.next();
        System.out.println("   --> " + bn.getName() + "   ("+bn.getCluster()+")");
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    BunchAPISimEdgeTest bunchAPISimEdgeTest1 = new BunchAPISimEdgeTest();
  }
}