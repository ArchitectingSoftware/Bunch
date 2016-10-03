
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
 *	$Log: BunchAPITest.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *
 *	Revision 3.5  2001/03/17 14:55:47  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.4  2000/11/30 01:49:21  bsmitc
 *	Added support for various tests and statistical gathering
 *
 *	Revision 3.3  2000/11/26 22:22:52  bsmitc
 *	Added new test for new precision and recall measurements
 *
 *	Revision 3.2  2000/11/26 20:39:26  bsmitc
 *	Added support for precision and recall calculations by using the
 *	BunchGraph API suite
 *
 *	Revision 3.1  2000/11/26 15:44:36  bsmitc
 *	Various bug patchs
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

public class BunchAPITest {

long totalNodes;
long totalAdjustments;
ArrayList bunchGraphs = null;

int [] prfreq = new int[11];
int [] prIfreq = new int [11];

 public void BunchAPITest4() {

      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();

      BunchMDG bmdg = new BunchMDG();


      //bmdg.addMDGEdge("m1","m2");
      //bmdg.addMDGEdge("m2","m1");
      //bmdg.addMDGEdge("m1","m3");
      //bmdg.addMDGEdge("m4","m5");
      //bmdg.addMDGEdge("m5","m4");
      //bmdg.addMDGEdge("m4","m3");

      boolean doWithFile = false;

      if(doWithFile == false)
      {
        bmdg.addMDGEdge("50",  "105", 1);
        bmdg.addMDGEdge("170", "56",  7);
        bmdg.addMDGEdge("29",  "144", 4);
        bmdg.addMDGEdge("150", "211", 10);
        bmdg.addMDGEdge("211", "328", 1);
        bmdg.addMDGEdge("29", "105", 1);
        bmdg.addMDGEdge("211", "14",  34);
        bmdg.addMDGEdge("21", "16",  1);
        bmdg.addMDGEdge("21", "144", 6);
        bmdg.addMDGEdge("17", "16",  2);
        bmdg.addMDGEdge("17", "144", 1);
        bmdg.addMDGEdge("17", "105", 11);
        bmdg.addMDGEdge("14", "16",  6);
        bmdg.addMDGEdge("14", "144", 7);
        bmdg.addMDGEdge("14", "105", 9);
        bmdg.addMDGEdge("170", "6",   3);
        bmdg.addMDGEdge("308", "50",  4);
        bmdg.addMDGEdge("6", "105", 2);
        bmdg.addMDGEdge("211", "150", 12);
        bmdg.addMDGEdge("21", "82",  2);
        bmdg.addMDGEdge("125", "56",  4);
        bmdg.addMDGEdge("14", "82",  4);
        bmdg.addMDGEdge("56", "125", 8);
        bmdg.addMDGEdge("170", "14",  25);
        bmdg.addMDGEdge("144", "6",   8);
        bmdg.addMDGEdge("79", "17",  2);
        bmdg.addMDGEdge("467", "79",  1);
        bmdg.addMDGEdge("82", "21",  20);
        bmdg.addMDGEdge("150", "328", 5);
        bmdg.addMDGEdge("79", "21",  1);
        bmdg.addMDGEdge("150", "14",  4);
        bmdg.addMDGEdge("29", "125", 11);
        bmdg.addMDGEdge("144", "14",  6);
        bmdg.addMDGEdge("79", "211", 67);
        bmdg.addMDGEdge("79", "56",  7);
        bmdg.addMDGEdge("56", "79",  6);
        bmdg.addMDGEdge("14", "125", 8);
        bmdg.addMDGEdge("53", "79",  30);
        bmdg.addMDGEdge("11", "125", 8);
        bmdg.addMDGEdge("50", "79",  12);
        bmdg.addMDGEdge("119", "328", 5);
        bmdg.addMDGEdge("144", "150", 10);

        api.setAPIProperty(BunchProperties.MDG_GRAPH_OBJECT,bmdg);
      }
      else
      {
        bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,
          "e:\\SampleMDGs\\paul.mdg");
      }
      //ArrayList edges = new ArrayList();
      //BunchMDGDependency be1 = new BunchMDGDependency("m1","m2");
      //BunchMDGDependency be2 = new BunchMDGDependency("m2","m1");
      //BunchMDGDependency be3 = new BunchMDGDependency("m1","m3");
      //BunchMDGDependency be4 = new BunchMDGDependency("m4","m5");
      //BunchMDGDependency be5 = new BunchMDGDependency("m5","m4");
      //BunchMDGDependency be6 = new BunchMDGDependency("m4","m3");

      //edges.add(be1);
      //edges.add(be2);
      //edges.add(be3);
      //edges.add(be4);
      //edges.add(be5);
      //edges.add(be6);

      //bmdg.addMDGEdges(edges);
      ////api.setAPIProperty(BunchProperties.MDG_GRAPH_OBJECT,bmdg);
     // bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,"e:\\expir\\rcs");
      ////bp.setProperty(BunchProperties.OUTPUT_FILE,"e:\\samplemdgs\\rcsBrian2");
      //bp.setProperty(BunchProperties.OMNIPRESENT_SUPPLIERS, "m4,m5");

      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.NULL_OUTPUT_FORMAT);
      ////bp.setProperty(BunchProperties.MDG_OUTPUT_MODE, BunchProperties.OUTPUT_DETAILED);


      bp.setProperty(BunchProperties.CLUSTERING_APPROACH,BunchProperties.AGGLOMERATIVE);

      //bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.NULL_OUTPUT_FORMAT);
      bp.setProperty(BunchProperties.PROGRESS_CALLBACK_CLASS,"bunch.api.BunchAPITestCallback");
      bp.setProperty(BunchProperties.PROGRESS_CALLBACK_FREQ,"5");
      api.setProperties(bp);
      System.out.println("Running...");
        api.run();
      Hashtable results = api.getResults();
      System.out.println("Results:");

      String rt = (String)results.get(BunchAPI.RUNTIME);
      String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
      String levels = (String)results.get(BunchAPI.TOTAL_CLUSTER_LEVELS);
      String saMovesTaken = (String)results.get(BunchAPI.SA_NEIGHBORS_TAKEN);

      System.out.println("Runtime = " + rt + " ms.");
      System.out.println("Total MQ Evaluations = " + evals);
      System.out.println("Total Levels = " + levels);
      System.out.println("Simulated Annealing Moves Taken = " + saMovesTaken);
      System.out.println();

      //Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

      //BunchGraph bg = api.getPartitionedGraph();
      //if (bg != null)
      //  bg.printGraph();

      //Integer iLvls = new Integer(levels);
      //for(int i = 0; i < iLvls.intValue(); i++)
      //{
      //  System.out.println(" ************* LEVEL "+i+" ******************");
      //  BunchGraph bgLvl = api.getPartitionedGraph(i);
      //  bgLvl.printGraph();
      //  System.out.println("\n\n");
      //}
  }

  public static Hashtable collectFinalGraphs(String mdgFileName, String baseFileDirectory, int howMany)
  {
    BunchGraph  bgList[] = new BunchGraph[howMany];
    String baseOutputFileName = mdgFileName;

    if((baseFileDirectory != null) && (!baseFileDirectory.equals("")))
    {
      File f = null;
      String baseFileName = "";
      try{
        f = new File(mdgFileName);
        baseFileName = f.getName();
        //System.out.println(baseFileName);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }

      String pathSep = File.separator;
      if(!baseFileDirectory.endsWith(pathSep))
        baseFileDirectory += pathSep;

      baseOutputFileName = baseFileDirectory + baseFileName;
    }

    //Now process the data
    for(int i = 0; i < howMany; i++)
    {
      Integer idx = new Integer(i);
      String fn = baseOutputFileName + idx.toString() + ".bunch";
      bgList[i] = BunchGraphUtils.constructFromSil(mdgFileName,fn);
    }

    String referenceFile = baseFileDirectory + "temp.bunch";
    BunchGraph bgRef = BunchGraphUtils.constructFromSil(mdgFileName,referenceFile);

    Hashtable h = new Hashtable();
    h.put("reference",bgRef);
    h.put("results",bgList);

    return h;
  }

  public static Hashtable processFinalResults(Hashtable in)
  {
    BunchGraph [] bgList = (BunchGraph [])in.get("results");
    BunchGraph bgRef = (BunchGraph)in.get("reference");

    double meclAccum = 0.0, meclMin = 100.0, meclMax = 0.0;
    double prAccum = 0.0, prMin = 100.0, prMax = 0.0;
    double esAccum = 0.0, esMin = 100.0, esMax = 0.0;

    if((bgList == null)||(bgRef == null)) return null;

    for(int i = 0; i < bgList.length; i++)
    {
      BunchGraph bg = bgList[i];

      double esValue = BunchGraphUtils.calcEdgeSimiliarities(bg,bgRef);
System.out.println("ES:"+esValue);
      esAccum +=esValue;
      if(esValue < esMin) esMin = esValue;
      if(esValue > esMax) esMax = esValue;

      Hashtable h1 = BunchGraphUtils.calcPR(bg,bgRef);
      Double prValue = (Double)h1.get("AVERAGE");
      prAccum += prValue.doubleValue();
System.out.println("PR:"+prValue.doubleValue());
      if(prValue.doubleValue() < prMin) prMin = prValue.doubleValue();
      if(prValue.doubleValue() > prMax) prMax = prValue.doubleValue();

      Hashtable meClValue1 = BunchGraphUtils.getMeClMeasurement(bg,bgRef);
      Hashtable meClValue2 = BunchGraphUtils.getMeClMeasurement(bgRef,bg);
      Double meclValue1 = (Double)meClValue1.get(BunchGraphUtils.MECL_QUALITY_METRIC);
      Double meclValue2 = (Double)meClValue2.get(BunchGraphUtils.MECL_QUALITY_METRIC);
      double d1 = meclValue1.doubleValue();
      double d2 = meclValue2.doubleValue();
      double meclValue = Math.max(d1,d2);
      meclAccum += meclValue;
System.out.println("ML:"+meclValue);
      if(meclValue < meclMin) meclMin = meclValue;
      if(meclValue > meclMax) meclMax = meclValue;
    }

    double denom = (double)bgList.length;
    double mecl = meclAccum / denom;
    double pr = prAccum / denom;
    double es = esAccum / denom;

    Hashtable h = new Hashtable();

    h.put("mecl",new Double(mecl));
    h.put("pr",new Double(pr));
    h.put("es",new Double(es));

    h.put("meclMin",new Double(meclMin));
    h.put("prMin",new Double(prMin));
    h.put("esMin",new Double(esMin));

    h.put("meclMax",new Double(meclMax));
    h.put("prMax",new Double(prMax));
    h.put("esMax",new Double(esMax));

    System.out.println("==============STATS RESULTS=================");
    System.out.println("Mecl = " + meclMin +", "+mecl+", "+meclMax);
    System.out.println("PR   = " + prMin+", "+pr+", "+prMax);
    System.out.println("ES   = " + esMin+", "+es+", "+esMax);

    return h;
  }

  public void BunchAPITestxxx()
  {
    String baseDir = "e:\\SampleMDGs\\";
    String mdgFileName = "compiler";
    String pathMDG = baseDir+mdgFileName;
    int    howMany = 50;

        Hashtable res = collectFinalGraphs(pathMDG,baseDir,howMany);
        Hashtable mes = processFinalResults(res);
  }


  private double calcSlope(ArrayList inputX, ArrayList inputY)
  {
    double n = (double)inputX.size();
    double SSxx = 0.0;
    double SSxy = 0.0;

    double Sxi2 = 0.0;
    double Sxi = 0.0;
    double Syi = 0.0;
    double Sxy = 0.0;

    if(inputX.size() != inputY.size()) return -1.0;

    for(int i = 0; i < inputX.size(); i++)
    {
      Double dxi = (Double)inputX.get(i);
      double xi = dxi.doubleValue();
      double xi2 = xi * xi;
      Double Dyi = (Double)inputY.get(i);
      double yi = Dyi.doubleValue();
      double xy = xi*yi;

      Sxi2 += xi2;
      Sxi  += xi;
      Syi  += yi;
      Sxy  += xy;
    }

    SSxx = Sxi2 - (Sxi/n);
    SSxy = Sxy - ((Sxi * Syi)/n);

    double slope = SSxy/SSxx;

    return slope;
  }
  private Hashtable calcVelocityAccel(ArrayList input)
  {
    Hashtable h = new Hashtable();
    ArrayList ax = new ArrayList();

    //Need at least 3 values to do this
    if(input.size() < 3) return null;

    //Build x values, simple index for now
    for(int i = 0; i < input.size(); i++)
      ax.add(new Double((double)i));

    //get the regression for the velocity
    double v = calcSlope(ax,input);

    //now setup for the acceleration, determine the average velocity intervals
    ArrayList axv = new ArrayList();
    ArrayList ayv = new ArrayList();
    for(int i = 1; i < input.size(); i++)
    {
      double deltaX;
      double deltaY;
      Double y1 = (Double)input.get(i-1);
      Double y2 = (Double)input.get(i);
      Double x1 = (Double)ax.get(i-1);
      Double x2 = (Double)ax.get(i);
      deltaX = x2.doubleValue()-x1.doubleValue();
      deltaY = y2.doubleValue()-y1.doubleValue();
      //for x measure slope;
      double slope = deltaY / deltaX;

      //for y measure use the midpoint
      double xmid = (x2.doubleValue()-x1.doubleValue()) / 2.0;
      axv.add(new Double(xmid));
      ayv.add(new Double(slope));
    }

    //now do acceleration
    double accel = calcSlope(axv,ayv);

    h.clear();
    h.put("V",new Double(v));
    h.put("A",new Double(accel));
    return h;
  }

public void BunchAPITest1x()
{
    
    String mdgFile = "c:\\research\\mdgs\\pgsql";
    String cluFile = "c:\\research\\mdgs\\pgsql.clu";

    System.out.println("Starting...");
    BunchProperties bp = new BunchProperties();
    bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdgFile);
    bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
    bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.TEXT_OUTPUT_FORMAT);
    bp.setProperty(BunchProperties.OUTPUT_TREE,"true");
    bp.setProperty(BunchProperties.OUTPUT_FILE,cluFile);

    BunchAPI api = new BunchAPI();
    api.setProperties(bp);
    api.run();
    System.out.println("Done");
}


  public void BunchAPITestOld99()
  {

  String mdg     = "e:\\samplemdgs\\bison";
  int    numRuns = 1;
  boolean useSA  = false;

  long min = 99999; //something large here;
  long max = 0;
  long accum = 0;



  for(int i = 0; i < numRuns; i++)
  {
    BunchAPI api = new BunchAPI();
    BunchProperties bp = new BunchProperties();

    bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdg);

    bp.setProperty(BunchProperties.CLUSTERING_ALG, BunchProperties.ALG_HILL_CLIMBING);

    if(useSA)
    {
      bp.setProperty(BunchProperties.ALG_HC_HC_PCT, "30");
      bp.setProperty(BunchProperties.ALG_HC_RND_PCT, "20");
      bp.setProperty(BunchProperties.ALG_HC_SA_CLASS, "bunch.SASimpleTechnique");
      bp.setProperty(BunchProperties.ALG_HC_SA_CONFIG, "InitialTemp=10.0,Alpha=0.85");
      bp.setProperty(BunchProperties.OUTPUT_FORMAT, BunchProperties.NULL_OUTPUT_FORMAT);
    }

    bp.setProperty(BunchProperties.ALG_HC_HC_PCT,"100");


    bp.setProperty(BunchProperties.OUTPUT_FORMAT, BunchProperties.GXL_OUTPUT_FORMAT);

    api.setProperties(bp);


    //api.setDebugStats(true);
    long startTime = System.currentTimeMillis();
        api.run();
    long runTime = System.currentTimeMillis()-startTime;
    ArrayList cList = api.getClusters();
    for(int zz = 0; zz< cList.size(); zz++)
    {
      System.out.println("LEVEL = "+zz);
      bunch.Cluster c = (bunch.Cluster)cList.get(zz);
      ArrayList alc = c.getClusteringDetails();

      long depth = c.getDepth();
      double baseMQ = c.getBaseObjFnValue();
      double finalMQ = c.getObjFnValue();
      int numClusters = c.getNumClusters();
      long numMQEvaluations = c.getNumMQEvaluations();


      System.out.println("Depth: "+depth+"  BaseMQ: "+baseMQ+"  FinalMQ: "+finalMQ+
              "  NumClusters: "+numClusters+"  MQEvals: "+numMQEvaluations);

      if(alc != null){

        //for(int zzz = 0; zzz < alc.size(); zzz++)
        //  System.out.print("["+alc.get(zzz)+"] " );
        //System.out.println();
        if(alc.size()>2)
        {
          double start = Double.parseDouble(alc.get(0).toString());
          double end   = Double.parseDouble(alc.get(alc.size()-1).toString());
          double mqInterval = end-start;
          double improvement = 0.0;
          double steps = 0.0;
          for(int zzz = 1; zzz < alc.size()-1; zzz++)
          {
            double ds = Double.parseDouble(alc.get(zzz).toString());
            double dsLast = Double.parseDouble(alc.get(zzz-1).toString());
            improvement += (ds - dsLast);
            double pct = (ds -start) / mqInterval;
            System.out.println("   i["+zzz+"]="+pct);
            steps++;
          }
          System.out.println("Steps = "+(int)steps+"  Avg. Step Size = "+(improvement/steps));
          System.out.println();
        }

        Hashtable h = this.calcVelocityAccel(alc);
        if(h != null)
        {
          System.out.println("***** V = "+h.get("V"));
          System.out.println("***** A = "+h.get("A"));
        }
      }
      else{
        System.out.println("List of details is null");
      }
    }



    System.out.println("Run "+i+":  Finished in "+runTime+" ms.");

    if(runTime > max) max = runTime;
    if(runTime < min) min = runTime;
    accum += runTime;
  }

  System.out.println();
  System.out.println("MIN Runtime = "+min+" ms.");
  System.out.println("MAX Runtime = "+max+" ms.");
  System.out.println("AVG Runtime = "+((double)accum/(double)numRuns)+" ms.");
  System.out.println("USE SA = "+useSA);
  }

  public void BunchAPITestBigBad() {

      String mdg="e:\\samplemdgs\\compiler";
      String sil="e:\\samplemdgs\\compiler.bunch";
/*
      BunchGraph g = BunchGraphUtils.constructFromSil(mdg,sil);
      double v = g.getMQValue();
      System.out.println("Default Mq value= "+v);

      g = BunchGraphUtils.constructFromSil(mdg,sil,"bunch.BasicMQ");
      v = g.getMQValue();
      System.out.println("Basic Mq value= "+v);

      g = BunchGraphUtils.constructFromSil(mdg,sil,"bunch.TurboMQ");
      v = g.getMQValue();
      System.out.println("Turbo Mq value= "+v);

      g = BunchGraphUtils.constructFromSil(mdg,sil,"bunch.ITurboMQ");
      v = g.getMQValue();
      System.out.println("ITurbo Mq value= "+v);

      if(true) System.exit(0);
 */
      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();
      //BunchAsyncNotifyTest nt = new BunchAsyncNotifyTest();

      bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdg);
      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);

      //bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_GA);
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.TEXT_OUTPUT_FORMAT);
      bp.setProperty(BunchProperties.OUTPUT_TREE,"true");
      bp.setProperty(BunchProperties.OUTPUT_FILE,"e:\\samplemdgs\\compiler.clu");

      //bp.setProperty(BunchProperties.USER_DIRECTED_CLUSTER_SIL,"e:\\samplemdgs\\compiler.locks");
      //bp.setProperty(BunchProperties.LIBRARY_LIST,"declarations");
      //bp.setProperty(BunchProperties.MQ_CALCULATOR_CLASS,"bunch.TurboMQIncrW");

      //bp.setProperty(BunchProperties.ALG_GA_POPULATION_SZ,"100");
      //bp.setProperty(BunchProperties.ALG_GA_NUM_GENERATIONS,"100");

      //gerations = 100, population = 100

      api.setProperties(bp);

      //api.setAPIProperty(BunchProperties.RUN_ASYNC_NOTIFY_CLASS,nt);

      System.out.println("Running...");
        api.run();

      java.io.File f1 = new java.io.File("e:\\samplemdgs\\compiler.clu.bunch");
      java.io.File fnew = new java.io.File("e:\\samplemdgs\\compiler.clu");
      fnew.delete();
      f1.renameTo(fnew);

      //Thread t = nt.getThread();
      //System.out.println("Thread ID is: " + nt.getThread());
      //nt.waitUntilDone();


      Hashtable results = api.getResults();
      System.out.println("Results:");

      String rt = (String)results.get(BunchAPI.RUNTIME);
      String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
      String levels = (String)results.get(BunchAPI.TOTAL_CLUSTER_LEVELS);
      String saMovesTaken = (String)results.get(BunchAPI.SA_NEIGHBORS_TAKEN);
      String medLvl = (String)results.get(BunchAPI.MEDIAN_LEVEL_GRAPH);

      System.out.println("Runtime = " + rt + " ms.");
      System.out.println("Total MQ Evaluations = " + evals);
      System.out.println("Simulated Annealing Moves Taken = " + saMovesTaken);
      System.out.println("Median Level: "+medLvl);
      System.out.println();

      BunchGraph gg = api.getPartitionedGraph(Integer.parseInt("0"/*medLvl*/));
      System.out.println("MQ Value = "+gg.getMQValue());

      if(true)System.exit(0);

      Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

      BunchGraph bg = api.getPartitionedGraph();
      if (bg != null)
        bg.printGraph();

      Integer iLvls = new Integer(levels);
      for(int i = 0; i < iLvls.intValue(); i++)
      {
        System.out.println(" ************* LEVEL "+i+" ******************");
        BunchGraph bgLvl = api.getPartitionedGraph(i);
        bgLvl.printGraph();
        System.out.println("\n\n");
      }
  }

  private void dump(String s, Collection c)
  {
    System.out.println("Special Modules: "+s);
    if(c == null)
      System.out.println("====>null");
    else {
      Iterator i = c.iterator();
      while(i.hasNext())
      {
        System.out.println("====>"+i.next());
      }
    }

    System.out.println();
  }

  public void BunchAPITestOld()
  {
      BunchAPI api = new BunchAPI();
      Hashtable htSpecial = api.getSpecialModules("e:\\linux\\linux");

      Collection suppliers = (Collection)htSpecial.get(BunchAPI.OMNIPRESENT_SUPPLIER);
      Collection clients  = (Collection)htSpecial.get(BunchAPI.OMNIPRESENT_CLIENT);
      Collection centrals = (Collection)htSpecial.get(BunchAPI.OMNIPRESENT_CENTRAL);
      Collection libraries = (Collection)htSpecial.get(BunchAPI.LIBRARY_MODULE);
      dump("clients",clients);
      dump("suppliers",suppliers);
      dump("centrals",centrals);
      dump("libraries",libraries);
  }

  public void BunchAPITest5()
  {
    BunchProperties bp = new BunchProperties();

    bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,"e:\\expir\\small");
    bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_GA);

    bp.setProperty(BunchProperties.ALG_GA_POPULATION_SZ,"50");

    //bp.setProperty(BunchProperties.RUN_MODE,BunchProperties.RUN_MODE_MQ_CALC);
    //bp.setProperty(BunchProperties.MQCALC_MDG_FILE,"e:\\expir\\compiler");
    //bp.setProperty(BunchProperties.MQCALC_SIL_FILE,"e:\\expir\\compilerSIL.bunch");

    BunchAPI api = new BunchAPI();
    api.setProperties(bp);
    api.run();
    Hashtable results = api.getResults();
    printResutls(results);

    //String MQValue = (String)results.get(BunchAPI.MQCALC_RESULT_VALUE);
    //System.out.println("MQ Value is: " + MQValue);
  }

  public void printResutls(Hashtable results)
  {
        String rt = (String)results.get(BunchAPI.RUNTIME);
      String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
      String levels = (String)results.get(BunchAPI.TOTAL_CLUSTER_LEVELS);
      String saMovesTaken = (String)results.get(BunchAPI.SA_NEIGHBORS_TAKEN);

      System.out.println("Runtime = " + rt + " ms.");
      System.out.println("Total MQ Evaluations = " + evals);
      System.out.println("Simulated Annealing Moves Taken = " + saMovesTaken);
      System.out.println();
      Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

      for(int i = 0; i < resultLevels.length; i++)
      {
        Hashtable lvlResults = resultLevels[i];
        System.out.println("***** LEVEL "+i+"*****");
        String mq = (String)lvlResults.get(BunchAPI.MQVALUE);
        String depth = (String)lvlResults.get(BunchAPI.CLUSTER_DEPTH);
        String numC = (String)lvlResults.get(BunchAPI.NUMBER_CLUSTERS);

        System.out.println("  MQ Value = " + mq);
        System.out.println("  Best Cluster Depth = " + depth);
        System.out.println("  Number of Clusters in Best Partition = " + numC);
        System.out.println();
      }
  }

  public void BunchAPITest8()
  {
    String graphName = "e:\\expir\\rcs";

    System.out.println("***** G R A P H   N A M E :   "+graphName+"\n");
    writeHeader();
    runTest(graphName, false);
    runTest(graphName, true);
  }
  public void runTest(String graphName, boolean removeSpecial)
  {
    totalNodes = totalAdjustments = 0;
    bunchGraphs = new ArrayList();
    //String graphName = "e:\\linux\\linux";
    //String graphName = "e:\\expir\\compiler";
    boolean removeSpecialModules = removeSpecial;

    for(int i = 0; i < 2; i++)
    {
      this.runClustering(graphName, removeSpecialModules);
      //this.runClustering("e:\\linux\\linux");
    }
    double avgValue = expirPR(prfreq);
    double avgIsomorphicValue = expirIsomorphicPR();
    BunchGraph bg = (BunchGraph)bunchGraphs.get(0);
    double avgIsomorphicCount = expirIsomorphicCount();

    //writeHeader();
    if(removeSpecial == false)
    {
      dumpFreqArray("BASELINE       ", prfreq,avgValue,avgIsomorphicCount);
      dumpFreqArray("NO ISOMORPHIC  ",prIfreq,avgIsomorphicValue,avgIsomorphicCount);
    }
    else
    {
      dumpFreqArray("NO SPECIAL     ", prfreq,avgValue, avgIsomorphicCount);
      dumpFreqArray("NO SPEC & ISO  ",prIfreq,avgIsomorphicValue,avgIsomorphicCount);
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

        Double prValue = new Double(BunchGraphUtils.calcEdgeSimiliarities(g1,g2));

        Hashtable meClValue = BunchGraphUtils.getMeClMeasurement(g1,g2);


        System.out.println("The distance is:  " + meClValue.get(BunchGraphUtils.MECL_VALUE) +
                    "   quality = "+meClValue.get(BunchGraphUtils.MECL_QUALITY_METRIC));
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

  public void runClustering(String mdgFileName, boolean removeSpecialNodes)
  {
      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();
      bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdgFileName);

      Hashtable htSpecial = api.getSpecialModules(mdgFileName);

      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.TEXT_OUTPUT_FORMAT);

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

  public void BunchAPITest3()
  {
    try{
      String mdgFile = "e:\\expir\\cia";
      int runCount = 50;

      FileWriter outF = new FileWriter(mdgFile+".txt");
      java.io.BufferedWriter out = new BufferedWriter(outF);

      for(int i = 0; i < runCount; i++)
      {
        BunchAPI api = new BunchAPI();
        BunchProperties bp = new BunchProperties();

        bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,mdgFile);
        bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.TEXT_OUTPUT_FORMAT);

        bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
        bp.setProperty(BunchProperties.ALG_HC_HC_PCT,"100");
        bp.setProperty(BunchProperties.ALG_HC_RND_PCT,"0");

        Integer cnt = new Integer(i);
        String outFileName = mdgFile + cnt.toString();
        bp.setProperty(BunchProperties.OUTPUT_FILE,outFileName);
        bp.setProperty(BunchProperties.ECHO_RESULTS_TO_CONSOLE,"True");
        api.setProperties(bp);

        //System.out.println("Running...");
          api.run();
        //System.out.println("Done!");

        Hashtable results = api.getResults();
        //System.out.println("Results:");

        String rt = (String)results.get(BunchAPI.RUNTIME);
        String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
        String medLvl = (String)results.get(BunchAPI.MEDIAN_LEVEL_GRAPH);
        Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

        Hashtable medLvlResults = resultLevels[Integer.parseInt(medLvl)];

        String numClusters = (String)medLvlResults.get(BunchAPI.NUMBER_CLUSTERS);
        String mqValue = (String)medLvlResults.get(BunchAPI.MQVALUE);

        String outLine = outFileName + "\t" + numClusters.toString() + "\t" + mqValue.toString() + "\r\n";
        out.write(outLine);
        if ((i % 10) == 0)
          System.out.println("Pct = " + (double)i / (double)runCount);
        //System.out.println("Runtime = " + rt + " ms.");
        //System.out.println("Total MQ Evaluations = " + evals);
      }

      out.close();
      outF.close();

      outF = new FileWriter(mdgFile+"_pr.txt");
      out = new BufferedWriter(outF);
      long total = (runCount * (runCount-1))/2;
      long performed = 0;

      for (int i = 0; i < runCount; i++)
      {
          BunchAPI api = new BunchAPI();
          BunchProperties bp = new BunchProperties();
          for (int j = i+1; j < runCount; j++)
          {
            if (i == j) continue;
            performed++;

            Integer iI = new Integer(i);
            Integer iJ = new Integer(j);
            String file1 = mdgFile + iI.toString() + ".bunch";
            String file2 = mdgFile + iJ.toString() + ".bunch";
            bp.setProperty(BunchProperties.RUN_MODE, BunchProperties.RUN_MODE_PR_CALC);
            bp.setProperty(BunchProperties.PR_CLUSTER_FILE,file1);
            bp.setProperty(BunchProperties.PR_EXPERT_FILE,file2);
            api.setProperties(bp);
            api.run();
            Hashtable results = api.getResults();
            String precision = (String)results.get(BunchAPI.PR_PRECISION_VALUE);
            String recall = (String)results.get(BunchAPI.PR_RECALL_VALUE);
            String outLine = "PR("+file1+", "+file2+")\t" + precision + "\t" + recall+"\r\n";

            out.write(outLine);
            if ((performed % 100) == 0)
              System.out.println("Pct PR: " + (double)performed/(double)total);
          }
      }
    }catch(Exception e)
    { e.printStackTrace(); }
  }

  BunchAPITest() {
      BunchAPI api = new BunchAPI();
      BunchProperties bp = new BunchProperties();
      bp.setProperty(BunchProperties.MDG_INPUT_FILE_NAME,"/Users/brianmitchell/dev/mdgs/incl");

      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_HILL_CLIMBING);
     //bp.setProperty(BunchProperties.ALG_HC_POPULATION_SZ,"12");
      //bp.setProperty(BunchProperties.ALG_HC_POPULATION_SIZE,"12");
      bp.setProperty(BunchProperties.ALG_HC_HC_PCT,"55");
      bp.setProperty(BunchProperties.ALG_HC_RND_PCT,"20");
      bp.setProperty(BunchProperties.ALG_HC_SA_CLASS,"bunch.SASimpleTechnique");
      bp.setProperty(BunchProperties.ALG_HC_SA_CONFIG,"InitialTemp=100.0,Alpha=0.95");
      //bp.setProperty(BunchProperties.TIMEOUT_TIME,"500");

/*
      bp.setProperty(BunchProperties.CLUSTERING_ALG,BunchProperties.ALG_SAHC);
      bp.setProperty(BunchProperties.ALG_SAHC_POPULATION_SZ,"10");
*/
      bp.setProperty(BunchProperties.OUTPUT_FORMAT,BunchProperties.DOT_OUTPUT_FORMAT);
      bp.setProperty(BunchProperties.OUTPUT_DIRECTORY,"/Users/brianmitchell/dev/mdgs");

      //bp.setProperty(BunchProperties.PROGRESS_CALLBACK_CLASS,"bunch.api.BunchAPITestCallback");
      //bp.setProperty(BunchProperties.PROGRESS_CALLBACK_FREQ,"0");
      api.setProperties(bp);
      System.out.println("Running...");


      api.run();
      Hashtable results = api.getResults();
      System.out.println("Results:");

      String rt = (String)results.get(BunchAPI.RUNTIME);
      String evals = (String)results.get(BunchAPI.MQEVALUATIONS);
      String levels = (String)results.get(BunchAPI.TOTAL_CLUSTER_LEVELS);
      String saMovesTaken = (String)results.get(BunchAPI.SA_NEIGHBORS_TAKEN);

      System.out.println("Runtime = " + rt + " ms.");
      System.out.println("Total MQ Evaluations = " + evals);
      System.out.println("Simulated Annealing Moves Taken = " + saMovesTaken);
      System.out.println();
      Hashtable [] resultLevels = (Hashtable[])results.get(BunchAPI.RESULT_CLUSTER_OBJS);

      for(int i = 0; i < resultLevels.length; i++)
      {
        Hashtable lvlResults = resultLevels[i];
        System.out.println("***** LEVEL "+i+"*****");
        String mq = (String)lvlResults.get(BunchAPI.MQVALUE);
        String depth = (String)lvlResults.get(BunchAPI.CLUSTER_DEPTH);
        String numC = (String)lvlResults.get(BunchAPI.NUMBER_CLUSTERS);

        System.out.println("  MQ Value = " + mq);
        System.out.println("  Best Cluster Depth = " + depth);
        System.out.println("  Number of Clusters in Best Partition = " + numC);
        System.out.println();
      }

      try
      {

        Runtime r = Runtime.getRuntime();
        r.exec("dot -Tps e:\\pstopcl\\incl.dot > e:\\pstopcl\\in\\incl.ps");
      }catch(Exception ex)
      { ex.printStackTrace(); }
  }



  public static void main(String[] args) {
    BunchAPITest bunchAPITest1 = new BunchAPITest();
  }
}