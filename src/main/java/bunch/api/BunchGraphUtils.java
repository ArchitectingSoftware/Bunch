 /****
 *
 *	$Log: BunchGraphUtils.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *
 *	Revision 3.3  2001/03/17 14:55:47  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.2  2000/11/30 01:49:21  bsmitc
 *	Added support for various tests and statistical gathering
 *
 *	Revision 3.1  2000/11/26 20:39:26  bsmitc
 *	Added support for precision and recall calculations by using the
 *	BunchGraph API suite
 *
 */
/**
 * Title:        Bunch Project<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Brian Mitchell<p>
 * Company:      Drexel University - SERG<p>
 * @author Brian Mitchell
 * @version 1.0
 */
package bunch.api;

import java.util.*;
import bunch.*;

public class BunchGraphUtils {

  public static final String MECL_VALUE = "MeclValue";
  public static final String MECL_QUALITY_METRIC = "MeclQualityMetric";

  public BunchGraphUtils() {
  }

  public static Collection getModuleNames(String mdgFileName)
  {
      ArrayList al = new ArrayList();

      Parser p = new DependencyFileParser();
      p.setInput(mdgFileName);
      p.setDelims(" ,\t");

      Graph g = (Graph)p.parse();

      Node[] na = g.getNodes();
      for(int i = 0; i < na.length; i++)
      {
        Node n = na[i];
        al.add(n.getName());
      }

      return al;
  }

  public static BunchGraph constructFromSil(String mdgFileName, String sFileName)
  {
      return constructFromSil(mdgFileName, sFileName,null);
  }

  public static BunchGraph constructFromMdg(String mdgFileName)
  {
      BunchGraph bg = new BunchGraph();

      Parser p = new DependencyFileParser();
      p.setInput(mdgFileName);
      p.setDelims(" ,\t");

      Graph g = (Graph)p.parse();
      Graph g1 = g.cloneAllNodesCluster();

      ObjectiveFunctionCalculatorFactory ocf = new ObjectiveFunctionCalculatorFactory();
      g1.setObjectiveFunctionCalculatorFactory(ocf);

      g1.calculateObjectiveFunctionValue();
      bg.construct(g);
      return bg;
  }

  public static boolean isSilFileOK(String mdgFileName, String sFileName)
  {
      BunchGraph bg = new BunchGraph();

      Parser p = new DependencyFileParser();
      p.setInput(mdgFileName);
      p.setDelims(" ,\t");

      Graph g = (Graph)p.parse();

      ClusterFileParser cfp = new ClusterFileParser();
      cfp.setInput(sFileName);
      cfp.setObject(g);
      cfp.parse();
      
      return cfp.areAllNodesInCluster();
  }
  
  public static ArrayList getMissingSilNodes(String mdgFileName, String sFileName)
  {
      BunchGraph bg = new BunchGraph();

      Parser p = new DependencyFileParser();
      p.setInput(mdgFileName);
      p.setDelims(" ,\t");

      Graph g = (Graph)p.parse();

      ClusterFileParser cfp = new ClusterFileParser();
      cfp.setInput(sFileName);
      cfp.setObject(g);
      cfp.parse();
      
      return cfp.getNodesNotAssignedToClusters();
  }
  
  public static BunchGraph constructFromSil(String mdgFileName, String sFileName,
              String mqCalcClass)
  {
      BunchGraph bg = new BunchGraph();

      Parser p = new DependencyFileParser();
      p.setInput(mdgFileName);
      p.setDelims(" ,\t");

      Graph g = (Graph)p.parse();

      ClusterFileParser cfp = new ClusterFileParser();
      cfp.setInput(sFileName);
      cfp.setObject(g);
      cfp.parse();

      ObjectiveFunctionCalculatorFactory ocf = new ObjectiveFunctionCalculatorFactory();
      g.setObjectiveFunctionCalculatorFactory(ocf);

      if(mqCalcClass == null)
      {
        g.setObjectiveFunctionCalculator(ocf.getDefaultMethod());
      }
      else
      {
        ocf.setCurrentCalculator(mqCalcClass);
        g.setObjectiveFunctionCalculator(mqCalcClass);
      }
      g.calculateObjectiveFunctionValue();
      bg.construct(g);
      return bg;
  }


  public static Hashtable calcPR(BunchGraph expert, BunchGraph cluster)
  {
    Hashtable results = new Hashtable();
    results.clear();
    BunchGraphPR prUtil = new BunchGraphPR(expert,cluster);
    boolean rc = prUtil.run();
    if(rc == false)
      return null;

    results.put("PRECISION", new Double(prUtil.getPrecision()));
    results.put("RECALL", new Double(prUtil.getRecall()));

    double avgPR = (prUtil.getPrecision() + prUtil.getRecall())/(2.0);

    results.put("AVERAGE", new Double(avgPR));
    return results;
  }

  public static long   getMeClDistance(BunchGraph g1, BunchGraph g2)
  {
    MeCl dist = new MeCl(g1,g2);
    long ret = dist.run();
    return ret;
  }

  public static Hashtable   getMeClMeasurement(BunchGraph g1, BunchGraph g2)
  {
    Hashtable h = new Hashtable();
    MeCl dist = new MeCl(g1,g2);
    long ret = dist.run();
    h.put(MECL_VALUE, new Long(ret));

    double quality = dist.getQualityMetric();
    h.put(MECL_QUALITY_METRIC,new Double(quality));
    return h;
  }

  public static long calcSimEdges(BunchGraph g1, BunchGraph g2)
  {
    long matches = 0;
    long nomatch = 0;
    long total = 0;
    HashMap g1Lookup = new HashMap();
    HashMap g2Lookup = new HashMap();
    g1Lookup.clear();
    g2Lookup.clear();

    Iterator load = g1.getEdges().iterator();
    while(load.hasNext())
    {
      BunchEdge be = (BunchEdge)load.next();
      String    key = (be.getSrcNode().getName() + be.getDestNode().getName());
      g1Lookup.put(key,be);
    }

    load = g2.getEdges().iterator();
    while(load.hasNext())
    {
      BunchEdge be = (BunchEdge)load.next();
      String    key = (be.getSrcNode().getName() + be.getDestNode().getName());
      g2Lookup.put(key,be);
    }

    Iterator iG1 = g1.getEdges().iterator();
    while(iG1.hasNext())
    {
      total++;
      BunchEdge be1 = (BunchEdge)iG1.next();
      String    key = (be1.getSrcNode().getName() + be1.getDestNode().getName());
      BunchEdge be2 = (BunchEdge)g2Lookup.get(key);

      boolean be1InSame;
      boolean be2InSame;
      //Investigate be1 to see if in same cluster
      be1InSame = (be1.getSrcNode().getCluster() == be1.getDestNode().getCluster());
//System.out.print("In Same:  " + be1InSame+"  ");
      if(be1InSame == true)
      {
        BunchNode n1 = be2.getSrcNode();
        BunchNode n2 = be2.getDestNode();
        if((n2.isAMemberOfCluster(n1.getMemberCluster())) ||
           (n1.isAMemberOfCluster(n2.getMemberCluster())))
          matches++;
      }
      else
      {
        BunchNode n1 = be2.getSrcNode();
        BunchNode n2 = be2.getDestNode();
        if((n2.isAMemberOfCluster(n1.getMemberCluster())) ||
           (n1.isAMemberOfCluster(n2.getMemberCluster())))
        {
          if((n2.memberOfHowManyClusters() > 1) ||
             (n1.memberOfHowManyClusters() > 1))
            matches++;
        }
        else
          matches++;
      }

      be2InSame = (be2.getSrcNode().getCluster() == be2.getDestNode().getCluster());


      //true if they are in the same or different clusters in both BunchEdges
      //System.out.println(be1InSame+", "+be2InSame + "--- key --->"+key);

      //if(be1InSame == be2InSame)
      //  matches++;
      //else
      //  nomatch++;
    }

    //if((matches+nomatch)!=total) System.out.println("postcondition failed");
    //System.out.println("Total = " + total + "  Matches = "+matches+"  Pct: "+(double)(((double)matches)/((double)total)));
    if(total == 0) return 0;
    return (long)matches;
  }

  public static double calcEdgeSim(BunchGraph g1, BunchGraph g2)
  {
    return calcEdgeSimiliarities(g1,g2);
  }

  public static double calcEdgeSimiliarities(BunchGraph g1, BunchGraph g2)
  {
    long matches = 0;
    long nomatch = 0;
    long total = 0;
    HashMap g1Lookup = new HashMap();
    HashMap g2Lookup = new HashMap();
    g1Lookup.clear();
    g2Lookup.clear();

    Iterator load = g1.getEdges().iterator();
    while(load.hasNext())
    {
      BunchEdge be = (BunchEdge)load.next();
      String    key = (be.getSrcNode().getName() + be.getDestNode().getName());
      g1Lookup.put(key,be);
    }

    load = g2.getEdges().iterator();
    while(load.hasNext())
    {
      BunchEdge be = (BunchEdge)load.next();
      String    key = (be.getSrcNode().getName() + be.getDestNode().getName());
      g2Lookup.put(key,be);
    }

    Iterator iG1 = g1.getEdges().iterator();
    while(iG1.hasNext())
    {
      total++;
      BunchEdge be1 = (BunchEdge)iG1.next();
      String    key = (be1.getSrcNode().getName() + be1.getDestNode().getName());
      BunchEdge be2 = (BunchEdge)g2Lookup.get(key);

//System.out.println("be1 "+be1.getSrcNode().getName()+"->"+be1.getDestNode().getName());
//System.out.println("be2 "+be2.getSrcNode().getName()+"->"+be2.getDestNode().getName());

      boolean be1InSame;
      boolean be2InSame;
      //Investigate be1 to see if in same cluster
      be1InSame = (be1.getSrcNode().getCluster() == be1.getDestNode().getCluster());
//System.out.print("In Same:  " + be1InSame+"  ");
      if(be1InSame == true)
      {
        BunchNode n1 = be2.getSrcNode();
        BunchNode n2 = be2.getDestNode();
        if((n2.isAMemberOfCluster(n1.getMemberCluster())) ||
           (n1.isAMemberOfCluster(n2.getMemberCluster())))
          matches++;
      }
      else
      {
        BunchNode n1 = be2.getSrcNode();
        BunchNode n2 = be2.getDestNode();
        if((n2.isAMemberOfCluster(n1.getMemberCluster())) ||
           (n1.isAMemberOfCluster(n2.getMemberCluster())))
        {
          if((n2.memberOfHowManyClusters() > 1) ||
             (n1.memberOfHowManyClusters() > 1))
            matches++;
        }
        else
          matches++;
      }

      be2InSame = (be2.getSrcNode().getCluster() == be2.getDestNode().getCluster());


      //true if they are in the same or different clusters in both BunchEdges
      //System.out.println(be1InSame+", "+be2InSame + "--- key --->"+key);

      //if(be1InSame == be2InSame)
      //  matches++;
      //else
      //  nomatch++;
    }

    //if((matches+nomatch)!=total) System.out.println("postcondition failed");
    //System.out.println("Total = " + total + "  Matches = "+matches+"  Pct: "+(double)(((double)matches)/((double)total)));
    if(total == 0) return 0.0;
    return (double)(((double)matches)/((double)total));
  }
}

class BunchGraphPR
{
  BunchGraph expertG;
  BunchGraph clusterG;
  double precision = 0.0;
  double recall = 0.0;
  long  combinationsConsidered = 0;
  long  matchingCombinations = 0;

  public BunchGraphPR(BunchGraph expert, BunchGraph cluster)
  {
    expertG = expert;
    clusterG = cluster;
  }

  public boolean run()
  {
    precision = runPR(clusterG,expertG);
    recall = runPR(expertG,clusterG);
    return true;
  }

  private double runPR(BunchGraph g1, BunchGraph g2)
  {
    double result = 0.0;
    combinationsConsidered = 0;
    matchingCombinations = 0;

    Iterator clusterList = g1.getClusters().iterator();
    while(clusterList.hasNext())
    {
      BunchCluster bc = (BunchCluster)clusterList.next();
      processCluster(bc,g2);
    }

    result = (double)matchingCombinations/(double)combinationsConsidered;

    return result;
  }

  private boolean processCluster(BunchCluster bc, BunchGraph bg)
  {
    Object[] nodeO = bc.getClusterNodes().toArray();
    for(int i = 0; i < nodeO.length; i++)
    {
      BunchNode srcNode = (BunchNode)nodeO[i];
      BunchCluster srcClusterInGraph = bg.findNode(srcNode.getName()).getMemberCluster();
      for(int j = i+1; j < nodeO.length; j++)
      {
        combinationsConsidered++;
        BunchNode tgtNode = (BunchNode)nodeO[j];
        String    tgtNodeName = tgtNode.getName();
        if(srcClusterInGraph.containsNode(tgtNodeName))
          matchingCombinations++;
      }
    }
    return true;
  }

  public double getPrecision()
  { return precision; }

  public double getRecall()
  { return recall;  }
}

class MeCl
{
  BunchGraph A;
  BunchGraph B;
  HashMap edgeA;
  long  meclValue;

  public MeCl(BunchGraph g1, BunchGraph g2)
  {
    A = g1;
    B = g2;
    edgeA = new HashMap();
    edgeA.clear();
    meclValue = 0;
  }

  public long run()
  {
    edgeA.clear();
    HashMap Ca = determineSubClusters();

    //dumpSubClusters(Ca);

    constructEdgeSet();

    meclValue = collectSubClusters(Ca);

    return meclValue;
  }

  public long getMeClValue()
  { return meclValue; }

  public double getQualityMetric()
  {
    int edgeCount = A.getEdges().size();
    double pct = ((double)meclValue / (double)edgeCount);
    return (1.0 - pct);
  }

  private void dumpSubClusters(HashMap h)
  {
    Iterator i = h.keySet().iterator();
    while (i.hasNext())
    {
      String clusterName = (String)i.next();
      HashMap Cc = (HashMap)h.get(clusterName);
      Iterator j = Cc.keySet().iterator();
      while(j.hasNext())
      {
        String subCluster = (String)j.next();
        ArrayList al = (ArrayList)Cc.get(subCluster);
        System.out.print(clusterName+"->"+subCluster+": ");
        for(int k = 0; k < al.size(); k++)
        {
          BunchNode bn = (BunchNode)al.get(k);
          System.out.print(bn.getName()+"  ");
        }
        System.out.println();
      }
    }
    System.out.println("\n\n");
  }

  private long collectSubClusters(HashMap Ca)
  {
    long tally=0;
    HashMap Ccollected = new HashMap();
    Ccollected.clear();

    Iterator i = Ca.values().iterator();
    while(i.hasNext())
    {
      HashMap Ci = (HashMap)i.next();
      Iterator j = Ci.keySet().iterator();
      while(j.hasNext())
      {
        String key = (String)j.next();
        ArrayList value = (ArrayList)Ci.get(key);
        tally+=mergeSubCluster(Ccollected,key,value);
      }
    }
    return tally;
  }

  private long mergeSubCluster(HashMap Ccollected, String key, ArrayList value)
  {
    long tally = 0;

    ArrayList currentSubCluster = (ArrayList)Ccollected.get(key);
    if(currentSubCluster == null)
    {
      Ccollected.put(key,value);
      return 0;
    }

    for(int i = 0; i < currentSubCluster.size(); i++)
    {
      BunchNode bn1 = (BunchNode)currentSubCluster.get(i);
      for(int j = 0; j < value.size(); j++)
      {
        BunchNode bn2 = (BunchNode)value.get(j);
        if (!bn2.isAMemberOfCluster(bn1.getMemberCluster().getName()))
        {
          //System.out.println("Looking for edge: " + bn1.getName()+" --> "+bn2.getName());
          tally += this.getConnectedWeight(bn1.getName(),bn2.getName());
        }
      }
    }
    currentSubCluster.addAll(value);
    return tally;
  }

  private void constructEdgeSet()
  {
    Iterator i = A.getEdges().iterator();

    while(i.hasNext())
    {
      BunchEdge be = (BunchEdge)i.next();
      String key = be.getSrcNode().getName() + be.getDestNode().getName();
      Integer weight = new Integer(be.getWeight());
      //System.out.println("Putting edge:  " + key+ "  weight:"+weight);
      edgeA.put(key,weight);
    }
  }

  public int getConnectedWeight(String n1, String n2)
  {
    String key1 = n1+n2;
    String key2 = n2+n1;
    int    total = 0;

    Integer forward = (Integer)edgeA.get(key1);
    if(forward != null){
      //System.out.println("Here forward");
      total += forward.intValue();
    }

    Integer reverse = (Integer)edgeA.get(key2);
    if(reverse != null) {
      //System.out.println("Here reverse");
      total += reverse.intValue();
    }

    //if(total>0)
    //  System.out.println("total = "+total);
    return total;
  }

  public HashMap determineSubClusters()
  {
    HashMap Ca = new HashMap();

    Iterator i = A.getClusters().iterator();
    while(i.hasNext())
    {
      HashMap subClustersA = new HashMap();
      BunchCluster Ai = (BunchCluster)i.next();
      Iterator j = Ai.getClusterNodes().iterator();
      while(j.hasNext())
      {
        BunchNode bnInA = (BunchNode)j.next();
        String nodeName = bnInA.getName();

        //Now find this node in the B graph
        BunchNode bnInB = B.findNode(nodeName);
        String    bnInBClusterName = bnInB.getMemberCluster().getName();
        //---------------------------

        //Now add the current node to the sub cluster
        //hash map for the current cluster in a
        ArrayList members = (ArrayList)subClustersA.get(bnInBClusterName);
        if(members == null)
        {
          members = new ArrayList();
          subClustersA.put(bnInBClusterName,members);
        }
        members.add(bnInA);
        //Now find the appropriate
      }

      Ca.put(Ai.getName(),subClustersA);
    }
    return Ca;
  }
}