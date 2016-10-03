
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
 *	$Log: BunchStatsTest.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/10/22 16:14:01  bsmitc
 *	Changed version number to 3.0 to sync with rest of project
 *	
 *	Revision 1.1.1.1  2000/10/22 16:05:57  bsmitc
 *	Initial Version
 *	
 *
 */
package bunch.api;

import java.util.*;
import java.io.*;

public class BunchStatsTest {

  public BunchStatsTest()
  {
    //runStatsTest();
    checkGraphTest();
  }

  public void checkGraphTest()
  {
    try
    {
      String filename = "d:\\proj\\bunch\\examples\\bison"; //"e:\\incl";
      bunch.BunchPreferences pref = (bunch.BunchPreferences)(java.beans.Beans.instantiate(null, "bunch.BunchPreferences"));

      bunch.Parser p = pref.getParserFactory().getParser("dependency");
      p.setInput(filename);
      p.setDelims(" \t");
      bunch.Graph g = (bunch.Graph)p.parse();

      String objFnCalc =  "bunch.TurboMQIncrW";
      (pref.getObjectiveFunctionCalculatorFactory()).setCurrentCalculator(objFnCalc);
      bunch.Graph.setObjectiveFunctionCalculatorFactory(pref.getObjectiveFunctionCalculatorFactory());
      g.setObjectiveFunctionCalculator(objFnCalc);

      if(g == null)
      {
        System.out.println("The graph is null");
        return;
      }

      for(int i = 0; i < 100; i++)
      {
        int [] clusterV = g.genRandomClusterSize(); //.getRandomCluster();
        bunch.Cluster c = new bunch.Cluster(g,clusterV);
        System.out.println("NumClusters = "+c.getClusterNames().length+" MQ Value = "+c.getObjFnValue());
      }

    }catch(Exception ex)
    { ex.printStackTrace(); }
  }



  public void runStatsTest() {

    String fileName = "e:\\bunchstats.txt";

  try{
    BufferedWriter writer_d = new BufferedWriter(new FileWriter(fileName));
    double mqAccum = 0.0;
    int    testRuns = 0;
    String header = "Run Number, Runtime(ms), Best MQ, Depth, Number of Clusters, MQ Evaluations, SA Neighbors Taken";
    System.out.println(header);
    writer_d.write(header+"\r\n");

    for(int i = 0; i < 100; i++)
    {
      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();
      bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,"e:\\incl");
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.NULL_OUTPUT_FORMAT);

      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_NAHC);
      bp.setProperty(BunchProperties.ALG_NAHC_HC_PCT,"1");
      //bp.setProperty(BunchProperties.ALG_NAHC_RND_PCT,"20");
      //bp.setProperty(BunchProperties.ALG_NAHC_SA_CLASS,"bunch.SASimpleTechnique");
      //bp.setProperty(BunchProperties.ALG_NAHC_SA_CONFIG,"InitialTemp=179.0,Alpha=0.995");
      //bp.setProperty(BunchProperties.ALG_NAHC_SA_CONFIG,"InitialTemp=1.0,Alpha=0.85");

      api.setProperties(bp);

      api.run();

      Hashtable results = api.getResults();

      String rt = (String)results.get(BunchAPI.RUNTIME);
      String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
      String levels = (String)results.get(BunchAPI.TOTAL_CLUSTER_LEVELS);
      String saMovesTaken = (String)results.get(BunchAPI.SA_NEIGHBORS_TAKEN);

      Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

      String mq = "null";
      String depth="null";
      String numC="null";
      if(resultLevels.length>=1)
      {
        Hashtable lvlResults = resultLevels[0];
        mq = (String)lvlResults.get(BunchAPI.MQVALUE);
        depth = (String)lvlResults.get(BunchAPI.CLUSTER_DEPTH);
        numC = (String)lvlResults.get(BunchAPI.NUMBER_CLUSTERS);
      }

      String outString = i+","+rt+","+mq+","+depth+","+numC+","+evals+","+saMovesTaken;
      System.out.println(outString);
      writer_d.write(outString+"\r\n");
      testRuns++;
      mqAccum+= Double.parseDouble(mq);
    }
    writer_d.close();
    System.out.println();
    System.out.println("***** Average MQ = " + (mqAccum/((double)testRuns)));

  }
  catch(Exception e) { e.printStackTrace(); }
   }

  public static void main(String[] args) {
    BunchStatsTest bunchStatsTest1 = new BunchStatsTest();
  }
}