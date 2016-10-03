/****
 *
 *	$Log: BunchEngine.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *
 *	Revision 3.4  2001/03/17 14:55:59  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.3  2000/11/30 03:08:31  bsmitc
 *	Updated statstics collection capabilities
 *
 *	Revision 3.2  2000/11/26 22:22:27  bsmitc
 *	Added support to build the structures necessary to support the BunchGraph
 *	API suite and classes
 *
 *	Revision 3.1  2000/11/26 15:47:44  bsmitc
 *	Updated engine to support errors and warnings, also included support for
 *	using the precision and recall calculator.
 *
 *	Revision 3.0  2000/10/22 16:17:25  bsmitc
 *	Changed initial version to 3.0
 *
 *	Revision 1.1.1.1  2000/10/22 16:16:14  bsmitc
 *	Initial Version
 *
 *
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
package bunch.engine;

import bunch.*;
import bunch.api.*;
import bunch.stats.*;

import java.util.*;
import java.beans.*;

public class BunchEngine {


  Hashtable bunchArgs = null;
  Hashtable results = null;
  ClusteringMethod clusteringMethod_d;
  GraphOutput graphOutput_d;
  Graph initialGraph_d;
  BunchPreferences preferences_d;
  StatsManager stats = bunch.stats.StatsManager.getInstance();
  Configuration configuration_d;
  ProgressCallbackInterface cbInterfaceObj;
  int callbackFrequency;
  long startTime;
  long endTime;
  long totalTime=0;
  Cluster baseCluster = null;
  ArrayList clusterList = null;
  javax.swing.Timer timeoutTimer = null;
  Thread clusteringProcessThread = null;
  int reflexiveEdgeCount = 0;

  String precision;
  String recall;
  String MQCalcMdgFileName;
  String MQCalcSilFileName;
  String MQCalcValue;

  public BunchEngine() {
  }

  String getFileDelims()
  {
    String delims = "";
    String def_delims = (String)bunchArgs.get(BunchProperties.MDG_PARSER_DELIMS);
    if(def_delims != null)
      delims += def_delims;

    if (((String)bunchArgs.get(BunchProperties.MDG_PARSER_USE_SPACES)).equalsIgnoreCase("TRUE"))
      delims = " " + delims;  //includes the space character
    if (((String)bunchArgs.get(BunchProperties.MDG_PARSER_USE_TABS)).equalsIgnoreCase("TRUE"))
      delims = "\t" + delims; //includes the tab character

    return delims;
  }

  Hashtable getSAConfigHTFromString(String saKey)
  {
    Hashtable h = new Hashtable();

    StringTokenizer st = new StringTokenizer(saKey,",");
    while(st.hasMoreElements())
    {
      String key = st.nextToken();
      StringTokenizer keyTokenizer = new StringTokenizer(key,"=");
      if(keyTokenizer.countTokens() != 2)
        continue;
      String keyValue = keyTokenizer.nextToken();
      Double value = new Double(keyTokenizer.nextToken());
      h.put(keyValue,value);
    }

    return h;
  }

  Collection parseStringToCollection(String saKey)
  {
    ArrayList al = new ArrayList();

    StringTokenizer st = new StringTokenizer(saKey,",");
    while(st.hasMoreElements())
    {
      al.add(st.nextToken());
    }
    return al;
  }

private String[] stringArrayFromString(String in)
{
  if(in == null) return null;

  StringTokenizer st = new StringTokenizer(in," ,\n\r");
  int howMany = st.countTokens();
  String []retArray = new String[howMany];
  int idx = 0;
  while(st.hasMoreElements())
    retArray[idx++] = st.nextToken();

  if(idx == 0) return null;
  return retArray;
}

/**
 * This method sets the libraries, clients and suppliers defined in their
 * respective panes to the graph, just previous to processing.
 */
public
void
arrangeLibrariesClientsAndSuppliers(Graph g,
                                    Hashtable special)

{
  Object []suppliers = null; //new Object[0]; //null;
  Object []clients = null; //new Object[0]; //null;
  Object []centrals = null; //new Object[0]; //null;
  Object []libraries = null; //new Object[0]; //null;

  if(special.get(BunchAPI.OMNIPRESENT_CENTRAL)!= null)
    centrals = ((Collection)special.get(BunchAPI.OMNIPRESENT_CENTRAL)).toArray();
  if(special.get(BunchAPI.OMNIPRESENT_CLIENT)!=null)
    clients = ((Collection)special.get(BunchAPI.OMNIPRESENT_CLIENT)).toArray();
  if(special.get(BunchAPI.OMNIPRESENT_SUPPLIER)!=null)
    suppliers = ((Collection)special.get(BunchAPI.OMNIPRESENT_SUPPLIER)).toArray();
  if(special.get(BunchAPI.LIBRARY_MODULE)!=null)
    libraries = ((Collection)special.get(BunchAPI.LIBRARY_MODULE)).toArray();

  Node[] nodeList = g.getNodes();
  Node[] originalList = nodeList;

  //tag the nodes with their type (matching them by name from the lists)
  for (int j=0; j<originalList.length; ++j) {
    if(suppliers != null) {
      for (int i=0; i<suppliers.length; ++i) {
        String name = originalList[j].getName();
        if (name.equals((String)suppliers[i])) {
          originalList[j].setType(Node.SUPPLIER);
          break;
        }
      }
    }
    if(clients != null) {
      for (int i=0; i<clients.length; ++i) {
        String name = originalList[j].getName();
        if (name.equals((String)clients[i])) {
          originalList[j].setType(Node.CLIENT);
          break;
        }
      }
    }
    if(centrals != null) {
      for (int i=0; i<centrals.length; ++i) {
        String name = originalList[j].getName();
        if (name.equals((String)centrals[i])) {
          originalList[j].setType(Node.CENTRAL);
          break;
        }
      }
    }

    if(libraries != null) {
      for (int i=0; i<libraries.length; ++i) {
        String name = originalList[j].getName();
        if (name.equals((String)libraries[i])) {
          originalList[j].setType(Node.LIBRARY);
          break;
        }
      }
    }
  }

  int deadNodes = 0;
  //now consolidate nodes that only point to omnipresent, libs, and suppliers
  for (int i=0; i<originalList.length; ++i) {
    if (originalList[i].getType() == Node.NORMAL) {
      boolean noNormalDeps = true;
      int []tmpDeps = originalList[i].getDependencies();
      int []tmpBeDeps = originalList[i].getBackEdges();
      int client = 0;
      int supplier = 0;
      int central = 0;
      int library = 0;
      for(int j = 0; j < tmpDeps.length; j++)
      {
        if ((originalList[tmpDeps[j]].getType() == Node.NORMAL) ||
            (originalList[tmpDeps[j]].getType() >= Node.DEAD))
        {
          noNormalDeps = false;
          break;
        }
        else
        {
          switch(originalList[tmpDeps[j]].getType())
          {
            case Node.CLIENT:
              client++; break;
            case Node.SUPPLIER:
              supplier++; break;
            case Node.CENTRAL:
              central++; break;
            case Node.LIBRARY:
              library++;  break;
          }
        }
      }
      for(int j = 0; j < tmpBeDeps.length; j++)
      {
        if ((originalList[tmpBeDeps[j]].getType() == Node.NORMAL) ||
            (originalList[tmpBeDeps[j]].getType() >= Node.DEAD))
        {
          noNormalDeps = false;
          break;
        }
        else
        {
          switch(originalList[tmpBeDeps[j]].getType())
          {
            case Node.CLIENT:
              client++; break;
            case Node.SUPPLIER:
              supplier++; break;
            case Node.CENTRAL:
              central++; break;
            case Node.LIBRARY:
              library++;  break;
          }
        }
      }
      if (noNormalDeps == true)
      {
        deadNodes++;
        int n1 = Math.max(client,supplier);
        int n2 = Math.max(central,library);
        int max = Math.max(n1,n2);
        int type = Node.CLIENT;

        if(max == client)   type = Node.CLIENT;
        if(max == supplier) type = Node.SUPPLIER;
        if(max == central)  type = Node.CENTRAL;
        if(max == library)  type = Node.LIBRARY;
        originalList[i].setType(Node.DEAD+max);
      }
    }
  }

  //now we have all the special modules tagged
  nodeList = new Node[originalList.length -
                (clients.length+suppliers.length+ deadNodes+
                +centrals.length+libraries.length)];
  int j=0;

  Hashtable normal = new Hashtable();
  //build new node list without omnipresent modules
  for (int i=0; i<originalList.length; ++i) {
    if (originalList[i].getType() == Node.NORMAL) {
      normal.put(new Integer(originalList[i].getId()),new Integer(j));
      nodeList[j++] = originalList[i].cloneNode();
    }
  }



  for (int i = 0; i < nodeList.length; ++i)
  {
    nodeList[i].nodeID = i;
    int[] deps = nodeList[i].getDependencies();
    int[] beDeps = nodeList[i].getBackEdges();
    int[] weight = nodeList[i].getWeights();
    int[] beWeight = nodeList[i].getBeWeights();
    int depsRemoveCount = 0;
    int beDeptsRemoveCount = 0;

    Integer tmpAssoc;
    for(int z = 0; z < deps.length; z++)
    {
      tmpAssoc = (Integer)normal.get(new Integer(deps[z]));
      if (tmpAssoc == null) {
        deps[z] = -1;
        depsRemoveCount++;
      } else {
        deps[z] = tmpAssoc.intValue();
      }
    }

    for(int z = 0; z < beDeps.length; z++) {
      tmpAssoc = (Integer)normal.get(new Integer(beDeps[z]));
      if (tmpAssoc == null) {
        beDeps[z] = -1;
        beDeptsRemoveCount++;
      } else {
        beDeps[z] = tmpAssoc.intValue();
      }
    }

    if(depsRemoveCount  > 0)
    {
      int []newDeps = new int[deps.length-depsRemoveCount];
      int []newWeight = new int[deps.length-depsRemoveCount];

      int pos = 0;
      for (int z = 0; z < deps.length; z++)
        if(deps[z] != -1) {
          newDeps[pos] = deps[z];
          newWeight[pos] = weight[z];
          pos++;
        }
        deps = newDeps;
        weight = newWeight;
    }

    if(beDeptsRemoveCount  > 0)
    {
      int []newBeDeps = new int[beDeps.length-beDeptsRemoveCount];
      int []newBeWeight = new int[beDeps.length-beDeptsRemoveCount];

      int pos = 0;
      for (int z = 0; z < beDeps.length; z++)
        if(beDeps[z] != -1) {
          newBeDeps[pos] = beDeps[z];
          newBeWeight[pos] = beWeight[z];
          pos++;
        }
        beDeps = newBeDeps;
        beWeight = newBeWeight;
    }

    nodeList[i].setDependencies(deps);
    nodeList[i].setWeights(weight);
    nodeList[i].setBackEdges(beDeps);
    nodeList[i].setBeWeights(beWeight);
  }


  //reinitialize the graph with the new nodes
  g.initGraph(nodeList.length);
  g.clear();
  g.setNodes(nodeList);
  g.setOriginalNodes(originalList);
}

  public Hashtable getDefaultSpecialNodes(String graphName)
  { return getDefaultSpecialNodes(graphName, 3.0);  }

  public Hashtable getDefaultSpecialNodes(String graphName, double threshold)
  {
    try
    {
      Hashtable h = new Hashtable();
      Hashtable centrals = new Hashtable();
      Hashtable clients = new Hashtable();
      Hashtable suppliers = new Hashtable();
      Hashtable libraries = new Hashtable();

      //=====================================================
      //Construct the graph
      //=====================================================
      BunchPreferences prefs =
        (BunchPreferences)(Beans.instantiate(null, "bunch.BunchPreferences"));

      //Parser p = prefs.getParserFactory().getParser("dependency");
      //p.setInput(graphName);
      //Graph g = (Graph)p.parse();

      String parserClass = "dependency";
      if(graphName.endsWith(".gxl") || graphName.endsWith(".GXL"))
        parserClass = "gxl";

      Parser p = preferences_d.getParserFactory().getParser(parserClass);
      p.setInput(graphName);
      Graph g = (Graph)p.parse();

      Node[] nodeList = g.getNodes();

      //find libraries
      for (int i=0; i<nodeList.length; ++i) {
        String nname = nodeList[i].getName();
        if ((nodeList[i].getDependencies() == null|| nodeList[i].getDependencies().length==0)
              && !clients.containsKey(nodeList[i].getName())
              && !suppliers.containsKey(nodeList[i].getName())
              && !centrals.containsKey(nodeList[i].getName())) {
          libraries.put(nodeList[i].getName(), nodeList[i].getName());
        }
      }


      //Code at around line 2020 in bunchframe.java

      //find clients
      double avg = 0.0, sum = 0.0;
      for (int i=0; i<nodeList.length; ++i) {
        if (nodeList[i].getDependencies() != null) {
          sum += nodeList[i].getDependencies().length;
        }
      }
      avg = sum/nodeList.length;
      avg = avg * threshold;
      for (int i=0; i<nodeList.length; ++i) {
        if (nodeList[i].getDependencies() != null
            && nodeList[i].getDependencies().length > avg
            && !libraries.containsKey(nodeList[i].getName())) {
          clients.put(nodeList[i].getName(),nodeList[i].getName());
        }
      }

      //find suppliers
      avg = 0.0; sum = 0.0;
      int[] inNum = new int[nodeList.length];

      for (int j=0; j<nodeList.length; ++j) {
        int currval = 0;
        for (int i=0; i<nodeList.length; ++i) {
          int[] deps = nodeList[i].getDependencies();
          if (deps != null) {
            for (int n=0; n<deps.length; ++n) {
              if (deps[n] == j) {
                currval++;
              }
            }
          }
        }
        inNum[j] = currval;
      }
      for (int i=0; i<inNum.length; ++i) {
        sum += inNum[i];
      }
      avg = sum/nodeList.length;
      avg = avg * threshold;
      for (int i=0; i<nodeList.length; ++i) {
        if (inNum[i] > avg
            && !libraries.containsKey(nodeList[i].getName())) {
          suppliers.put(nodeList[i].getName(),nodeList[i].getName());
        }
      }

      //looking for central nodes (nodes that are clients and suppliers
      ArrayList clientsAL = new ArrayList(clients.values());

      for (int i=0; i<clientsAL.size(); ++i) {
        String client = (String) clientsAL.get(i);
        if(suppliers.containsKey(client))
          centrals.put(client,client);
      }

      Enumeration e = centrals.elements();
      while(e.hasMoreElements())
      {
        String elem = (String)e.nextElement();
        clients.remove(elem);
        suppliers.remove(elem);
      }

      //=====================================================
      //return the hashtable
      //=====================================================
      h.put(BunchAPI.OMNIPRESENT_CENTRAL,centrals.values());
      h.put(BunchAPI.OMNIPRESENT_CLIENT,clients.values());
      h.put(BunchAPI.OMNIPRESENT_SUPPLIER,suppliers.values());
      h.put(BunchAPI.LIBRARY_MODULE,libraries.values());
      return h;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  Hashtable getSpecialModulesFromProperties()
  {
    Hashtable h = new Hashtable();
    ArrayList emptyList = new ArrayList();
    boolean   containsSpecial = false;

    emptyList.clear();

    if(bunchArgs.get(BunchProperties.OMNIPRESENT_BOTH) != null) {
      containsSpecial = true;
      String mods = (String)bunchArgs.get(BunchProperties.OMNIPRESENT_BOTH);
      Collection c = parseStringToCollection(mods);
      h.put(BunchAPI.OMNIPRESENT_CENTRAL,c);
    }
    else
      h.put(BunchAPI.OMNIPRESENT_CENTRAL,(Collection)emptyList);

    if(bunchArgs.get(BunchProperties.OMNIPRESENT_CLIENTS) != null) {
      containsSpecial = true;
      String mods = (String)bunchArgs.get(BunchProperties.OMNIPRESENT_CLIENTS);
      Collection c = parseStringToCollection(mods);
      h.put(BunchAPI.OMNIPRESENT_CLIENT,c);
    }
    else
      h.put(BunchAPI.OMNIPRESENT_CLIENT,(Collection)emptyList);

    if(bunchArgs.get(BunchProperties.OMNIPRESENT_SUPPLIERS) != null) {
      containsSpecial = true;
      String mods = (String)bunchArgs.get(BunchProperties.OMNIPRESENT_SUPPLIERS);
      Collection c = parseStringToCollection(mods);
      h.put(BunchAPI.OMNIPRESENT_SUPPLIER,c);
    }
    else
      h.put(BunchAPI.OMNIPRESENT_SUPPLIER,(Collection)emptyList);

    if(bunchArgs.get(BunchProperties.LIBRARY_LIST) != null) {
      containsSpecial = true;
      String mods = (String)bunchArgs.get(BunchProperties.LIBRARY_LIST);
      Collection c = parseStringToCollection(mods);
      h.put(BunchAPI.LIBRARY_MODULE,c);
    }
    else
      h.put(BunchAPI.LIBRARY_MODULE,(Collection)emptyList);

    if(containsSpecial == true)
      return h;
    else
      return null;
  }

  boolean initClustering()
  {
    try{
      //
      clusterList = new ArrayList();

      //Load Preferences
      preferences_d = (BunchPreferences)(Beans.instantiate(null, "bunch.BunchPreferences"));


      //Construct Graph
      if(bunchArgs.get(BunchProperties.MDG_INPUT_FILE_NAME) != null)
      {
        Parser p = preferences_d.getParserFactory().getParser("dependency");
        p.setInput((String)bunchArgs.get(BunchProperties.MDG_INPUT_FILE_NAME));
        p.setDelims(getFileDelims());
        initialGraph_d = (Graph)p.parse();
        reflexiveEdgeCount = ((DependencyFileParser)p).getReflexiveEdges();
      }

      if(bunchArgs.get(BunchProperties.MDG_GRAPH_OBJECT) != null)
      {
        bunch.api.BunchMDG mdgObj = (bunch.api.BunchMDG)
            bunchArgs.get(BunchProperties.MDG_GRAPH_OBJECT);

        initialGraph_d = bunch.util.BunchUtilities.toInternalGraph(mdgObj);
        reflexiveEdgeCount = 0;
      }


        //NOW HANDLE USER DIRECTED CLUSTERING, IF SET AND THE LOCKS
        String userSILFile = (String)bunchArgs.get(BunchProperties.USER_DIRECTED_CLUSTER_SIL);
        if(userSILFile != null)
        {
          boolean lock = true;
          String lockStr = (String)bunchArgs.get(BunchProperties.LOCK_USER_SET_CLUSTERS);
          if(lockStr != null)
            if(lockStr.equalsIgnoreCase("false")) lock = false;

          Parser cp = preferences_d.getParserFactory().getParser("cluster");
          cp.setInput(userSILFile);
          cp.setObject(initialGraph_d);
          cp.parse();
          if(lock==true)
            initialGraph_d.setDoubleLocks(true);

          //=================================
          //Now lock the clusters
          //=================================
          int[] clust = initialGraph_d.getClusters();
          boolean[] locks = initialGraph_d.getLocks();
          for (int i=0; i<clust.length; ++i) {
            if (clust[i] != -1) {
              locks[i] = true;
            }
          }
        }

      //See if there are special modules
      if(bunchArgs.get(BunchProperties.SPECIAL_MODULE_HASHTABLE) != null)
      {
        Hashtable special = (Hashtable)bunchArgs.get(BunchProperties.SPECIAL_MODULE_HASHTABLE);
        arrangeLibrariesClientsAndSuppliers(initialGraph_d,special);
      }

      Hashtable specialFromInput = getSpecialModulesFromProperties();
      if (specialFromInput != null)
        arrangeLibrariesClientsAndSuppliers(initialGraph_d,specialFromInput);

      //Load Clusteirng Method Handler
      String clustAlg = (String)bunchArgs.get(BunchProperties.CLUSTERING_ALG);
      if(clustAlg==null) return false;
      clusteringMethod_d = preferences_d.getClusteringMethodFactory().getMethod(clustAlg);
      if(clusteringMethod_d == null) return false;

      configuration_d = clusteringMethod_d.getConfiguration();
      if (initialGraph_d!=null&&configuration_d!=null)
        configuration_d.init(initialGraph_d);

      if(clustAlg.equals(BunchProperties.ALG_GA))
      {
        GAConfiguration gaConfig = (GAConfiguration)configuration_d;

        String method = (String)bunchArgs.get(BunchProperties.ALG_GA_SELECTION_METHOD);
        String cProb = (String)bunchArgs.get(BunchProperties.ALG_GA_CROSSOVER_PROB);
        String mProb = (String)bunchArgs.get(BunchProperties.ALG_GA_MUTATION_PROB);
        String popSz = (String)bunchArgs.get(BunchProperties.ALG_GA_POPULATION_SZ);
        String numGens = (String)bunchArgs.get(BunchProperties.ALG_GA_NUM_GENERATIONS);

        if(method != null)
        {
          String tournMethod = "tournament";
          String roulMethod = "roulette wheel";
          if(method.equals(BunchProperties.ALG_GA_SELECTION_ROULETTE))
            gaConfig.setMethod(roulMethod);
          if(method.equals(BunchProperties.ALG_GA_SELECTION_TOURNAMENT))
            gaConfig.setMethod(tournMethod);
        }

        if(numGens != null)
        {
          int nGens = Integer.parseInt(numGens);
          gaConfig.setNumOfIterations(nGens);
        }

        if(cProb != null)
        {
          double crossProb = Double.parseDouble(cProb);
          gaConfig.setCrossoverThreshold(crossProb);
        }

        if(mProb != null)
        {
          double mutationProb = Double.parseDouble(mProb);
          gaConfig.setMutationThreshold(mutationProb);
        }

        if(popSz != null)
        {
          int pSize = Integer.parseInt(popSz);
          gaConfig.setPopulationSize(pSize);
        }
      }

      if(clustAlg.equals(BunchProperties.ALG_SAHC))
      {
        Integer popSz = (Integer)bunchArgs.get(BunchProperties.ALG_SAHC_POPULATION_SZ);

        if(popSz != null)
          configuration_d.setPopulationSize(popSz.intValue());
      }

      if(clustAlg.equals(BunchProperties.ALG_HILL_CLIMBING))
      {
        NAHCConfiguration c = (NAHCConfiguration)configuration_d;
        if(bunchArgs.get(BunchProperties.ALG_HC_RND_PCT) != null)
        {
          Integer randomize = (Integer)bunchArgs.get(BunchProperties.ALG_HC_RND_PCT);
          c.setRandomizePct(randomize.intValue());
        }

        if(bunchArgs.get(BunchProperties.ALG_HC_HC_PCT) != null)
        {

          Integer hcThreshold = (Integer)bunchArgs.get(BunchProperties.ALG_HC_HC_PCT);
          //System.out.println("Setting minumum to consider= "+hcThreshold);
          c.setMinPctToConsider(hcThreshold.intValue());

        }
      }

      if(clustAlg.equals(BunchProperties.ALG_NAHC))
      {
        Integer HCPct = (Integer)bunchArgs.get(BunchProperties.ALG_NAHC_HC_PCT);
        Integer rndPct = (Integer)bunchArgs.get(BunchProperties.ALG_NAHC_RND_PCT);
        Integer popSz = (Integer)bunchArgs.get(BunchProperties.ALG_NAHC_POPULATION_SZ);

        NAHCConfiguration c = (NAHCConfiguration)configuration_d;

        if(popSz != null)
          c.setPopulationSize(popSz.intValue());

        if(HCPct != null)
        {
          c.setMinPctToConsider(HCPct.intValue());

          if(rndPct != null)
            c.setRandomizePct(rndPct.intValue());
          else
          {
            int pctTmp = 100-HCPct.intValue();
            c.setRandomizePct(pctTmp);
          }
        }

        String SAClass = (String)bunchArgs.get(BunchProperties.ALG_NAHC_SA_CLASS);
        if(SAClass != null)
        {
          SATechnique saHandler = (SATechnique)Beans.instantiate(null,SAClass);
          if(saHandler != null)
          {
            String saHandlerArgs = (String)bunchArgs.get(BunchProperties.ALG_NAHC_SA_CONFIG);
            if(saHandlerArgs != null)
            {
              Hashtable saConfig = getSAConfigHTFromString(saHandlerArgs);
              saHandler.setConfig(saConfig);
            }
            c.setSATechnique(saHandler);
          }
        }
      }

      //now set if we are clustering trees or one level
      if (((String)bunchArgs.get(BunchProperties.CLUSTERING_APPROACH)).equalsIgnoreCase(BunchProperties.AGGLOMERATIVE))
        initialGraph_d.setIsClusterTree(true);
      else
        initialGraph_d.setIsClusterTree(false);

      //now setup the calculator
      String objFnCalc = (String)bunchArgs.get(BunchProperties.MQ_CALCULATOR_CLASS);
      (preferences_d.getObjectiveFunctionCalculatorFactory()).setCurrentCalculator(objFnCalc);
      Graph.setObjectiveFunctionCalculatorFactory(preferences_d.getObjectiveFunctionCalculatorFactory());
      initialGraph_d.setObjectiveFunctionCalculator(objFnCalc);

      //now setup the clustering method object
      clusteringMethod_d.initialize();
      clusteringMethod_d.setGraph(initialGraph_d.cloneGraph());

      //now init the stats manager
      stats.getInstance();

      //see if a callback class is setup, if so save a reference to the class
      cbInterfaceObj = (ProgressCallbackInterface)bunchArgs.get(bunch.api.BunchAPI.CALLBACK_OBJECT_REF);
      Integer iTmp = (Integer)bunchArgs.get(bunch.api.BunchAPI.CALLBACK_OBJECT_FREQ);
      if(iTmp != null)
        callbackFrequency = iTmp.intValue();

      //see if there is a timout requested
      Integer toTime = (Integer)bunchArgs.get(BunchProperties.TIMEOUT_TIME);
      if(toTime != null)
        timeoutTimer = new javax.swing.Timer(toTime.intValue(),new TimeoutTimer());

      //now set the graph output driver
      graphOutput_d = null;
      String outputMode = (String)bunchArgs.get(BunchProperties.OUTPUT_FORMAT);
      if((outputMode != null)||(!outputMode.equalsIgnoreCase(BunchProperties.NULL_OUTPUT_FORMAT)))
      {
        String driver = null;
        if(outputMode.equalsIgnoreCase(BunchProperties.DOT_OUTPUT_FORMAT))
          driver = "Dotty";
        else if(outputMode.equalsIgnoreCase(BunchProperties.TEXT_OUTPUT_FORMAT))
          driver = "Text";
        else if(outputMode.equalsIgnoreCase(BunchProperties.GXL_OUTPUT_FORMAT))
          driver = "GXL";

        if(driver != null)
        {
          String outFileName = (String)bunchArgs.get(BunchProperties.OUTPUT_FILE);
          if (outFileName == null)
            outFileName = (String)bunchArgs.get(BunchProperties.MDG_INPUT_FILE_NAME);

          graphOutput_d = preferences_d.getGraphOutputFactory().getOutput(driver);

          String outTree = (String)bunchArgs.get(BunchProperties.OUTPUT_TREE);
          if(outTree != null)
          {
            if(outTree.equalsIgnoreCase("true"))
            {
              graphOutput_d.setNestedLevels(true);
            }
          }

          graphOutput_d.setBaseName(outFileName); //(String)bunchArgs.get(BunchProperties.MDG_INPUT_FILE_NAME));
          graphOutput_d.setBasicName(outFileName); //(String)bunchArgs.get(BunchProperties.MDG_INPUT_FILE_NAME));
          String outputFileName = graphOutput_d.getBaseName();
          String outputPath = (String)bunchArgs.get(BunchProperties.OUTPUT_DIRECTORY);
          if(outputPath != null)
          {
            java.io.File f = new java.io.File(graphOutput_d.getBaseName());
            String filename = f.getName();
            outputFileName = outputPath+filename;
          }
          graphOutput_d.setCurrentName(outputFileName);
          //System.out.println("Current name is " + outputFileName);
        }
      }
    }catch(Exception e1)
    {
      e1.printStackTrace();
      return false;
    }
    return true;
  }

  public Graph getBestGraph()
  {
    if (clusteringMethod_d == null)
      return null;

    return clusteringMethod_d.getBestGraph().cloneGraph();
  }

  boolean runClusteringAsync(final BunchAsyncNotify nObject)
  {

    nObject.setStatus(bunch.api.BunchAsyncNotify.STATUS_RUNNING);
    bunch.SwingWorker worker_d = new bunch.SwingWorker()
      //Runnable runThread = new Runnable()
    {
      public Object construct()
      {
        try{
          runClustering();
        }
        catch(Exception threadEx){ threadEx.printStackTrace(); }
        return "Done";
      }
      public void interrupt()
      {
        this.suspend();
        super.interrupt();
      }
      public void finished()
      {
        nObject.setStatus(bunch.api.BunchAsyncNotify.STATUS_DONE);
        nObject.notifyDone();
      }
    };

    worker_d.setPriority(Thread.MIN_PRIORITY);
    worker_d.start();
    nObject.setThread(worker_d.getThread());
    return true;
  }

  boolean runClustering()
  {
    if(initClustering() == false)
      return false;

    BunchAsyncNotify notifyClass = null;

    //clusteringMethod_d.run();
    //if (bunchArgs.get(BunchProperties.RUN_ASYNC_NOTIFY_CLASS) != null)
    //    notifyClass = (BunchAsyncNotify)bunchArgs.get(BunchProperties.RUN_ASYNC_NOTIFY_CLASS);
    //if(notifyClass == null)
    //{
      ExecuteClusteringEngine ce = new ExecuteClusteringEngine();//clusteringMethod_d,bunchArgs);
    //}
    //else
    //{
    //  ExecuteClusteringEngineAsync ce = new ExecuteClusteringEngineAsync(notifyClass);
    //}

    Cluster bestC = clusteringMethod_d.getBestCluster();
    baseCluster = clusteringMethod_d.getBestCluster().cloneCluster();
    clusterList.add(clusteringMethod_d.getBestCluster().cloneCluster());

    //System.out.println("MQ-Lvl"+ bestC.getGraph().getGraphLevel()+" is: " + bestC.getObjFnValue());
    String cApproach = (String)bunchArgs.get(BunchProperties.CLUSTERING_APPROACH);
    if(cApproach.equalsIgnoreCase(BunchProperties.AGGLOMERATIVE))
    {
      Graph g = clusteringMethod_d.getBestGraph().cloneGraph();

      int []cNames = g.getClusterNames();  //c.getClusterNames();
      while(cNames.length>1)
      {
        //level++;
        NextLevelGraph nextL = new NextLevelGraph();
        Graph newG=nextL.genNextLevelGraph(g);

        newG.setPreviousLevelGraph(g);
        newG.setGraphLevel(g.getGraphLevel()+1);

        clusteringMethod_d.setGraph(newG);
        clusteringMethod_d.initialize();

        //if(notifyClass == null)
        //{
        //  ExecuteClusteringEngine ce = new ExecuteClusteringEngine();//clusteringMethod_d,bunchArgs);
        //}
        //else
        //{
        //  ExecuteClusteringEngineAsync ce = new ExecuteClusteringEngineAsync(notifyClass);
        //}

        ce = new ExecuteClusteringEngine();//clusteringMethod_d,bunchArgs);


        //clusteringMethod_d.run();
        bestC = clusteringMethod_d.getBestCluster();
        clusterList.add(clusteringMethod_d.getBestCluster().cloneCluster());
        //System.out.println("MQ-Lvl"+ bestC.getGraph().getGraphLevel()+" is: " + bestC.getObjFnValue());
        //currentViewC = new Cluster(clusteringMethod_x.getBestGraph().cloneGraph(),
        //                clusteringMethod_x.getBestGraph().getClusters());
        //          currentViewC.force();
        //          bestCLL.addLast(currentViewC);
                  //bestCLL.addLast(clusteringMethod_x.getBestCluster());

        g = clusteringMethod_d.getBestGraph().cloneGraph();
                  //c = clusteringMethod_x.getBestCluster();
        cNames = g.getClusterNames();  //c.getClusterNames();
      }
    }
    if(graphOutput_d != null)
    {
      graphOutput_d.setGraph(clusteringMethod_d.getBestGraph());
      graphOutput_d.write();
    }

    //if(notifyClass != null)
    //  notifyClass.notifyDone();

    return true;
  }

  boolean runMQCalc()
  {
    MQCalcMdgFileName = (String)bunchArgs.get(BunchProperties.MQCALC_MDG_FILE);
    MQCalcSilFileName = (String)bunchArgs.get(BunchProperties.MQCALC_SIL_FILE);
    String MQCalcClass = (String)bunchArgs.get(BunchProperties.MQ_CALCULATOR_CLASS);

    double mqResult = bunch.util.MQCalculator.CalcMQ(MQCalcMdgFileName,MQCalcSilFileName,MQCalcClass);
    Double Dmq = new Double(mqResult);
    MQCalcValue =  Dmq.toString();
    return true;
  }

  boolean runPRCalc()
  {
    String clusterF = (String)bunchArgs.get(BunchProperties.PR_CLUSTER_FILE);
    String expertF = (String)bunchArgs.get(BunchProperties.PR_EXPERT_FILE);

    bunch.util.PrecisionRecallCalculator calc =
      new bunch.util.PrecisionRecallCalculator(expertF,clusterF);

    precision = calc.get_precision();
    recall = calc.get_recall();

    return true;
  }

  private int getMedianLevelNumber()
  {
      if (clusteringMethod_d == null)
        return -1;

      Graph g = clusteringMethod_d.getBestGraph();
      Graph medianG = g.getMedianTree();
      return medianG.getGraphLevel();
  }

  public Hashtable getResultsHT()
  {
    String runMode = (String)bunchArgs.get(BunchProperties.RUN_MODE);
    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_CLUSTER))
    {
      return getClusteringResultsHT();
    }

    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_PR_CALC))
    {
      return getPRResultsHT();
    }

    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_MQ_CALC))
    {
      return getMQCalcResultsHT();
    }

    return null;
  }

  public Hashtable getMQCalcResultsHT()
  {
    results = new Hashtable();
    if (MQCalcValue == null)
      return null;

    results.put(BunchAPI.MQCALC_RESULT_VALUE,MQCalcValue);
    return results;
  }

  public Hashtable getPRResultsHT()
  {
    results = new Hashtable();
    if ((precision == null) || (recall == null))
      return null;

    results.put(BunchAPI.PR_PRECISION_VALUE,precision);
    results.put(BunchAPI.PR_RECALL_VALUE,recall);
    return results;
  }

  public void setDebugStats(boolean b)
  { stats.setCollectClusteringDetails(b);}

  public ArrayList getClusterList()
  {
    return this.clusterList;
  }

  public Hashtable getClusteringResultsHT()
  {
      if(clusteringMethod_d == null) return null;
      if(baseCluster == null) return null;

      results = new Hashtable();

      Long rt = new Long(totalTime);
      Long mqEvals = new Long(stats.getMQCalculations());
      Integer totalClusterLevels = new Integer(clusterList.size());
      Long saMovesTaken = new Long(stats.getSAOverrides());
      Integer medianLvl = new Integer(getMedianLevelNumber());

      results.put(BunchAPI.RUNTIME,rt.toString());
      results.put(BunchAPI.MQEVALUATIONS,mqEvals.toString());
      results.put(BunchAPI.TOTAL_CLUSTER_LEVELS,totalClusterLevels.toString());
      results.put(BunchAPI.SA_NEIGHBORS_TAKEN,saMovesTaken.toString());
      results.put(BunchAPI.MEDIAN_LEVEL_GRAPH,medianLvl.toString());

      //now handle errors & warnings
      Hashtable errorHT = new Hashtable();
      results.put(BunchAPI.ERROR_HASHTABLE,errorHT);

      Hashtable warningHT = new Hashtable();
      if (reflexiveEdgeCount > 0)
      {
        Integer re = new Integer(reflexiveEdgeCount);
        warningHT.put(BunchAPI.REFLEXIVE_EDGE_COUNT,re.toString());
      }
      results.put(BunchAPI.WARNING_HASHTABLE,warningHT);

      Hashtable []resultClusters = new Hashtable[clusterList.size()];

      for(int i = 0; i < clusterList.size(); i++)
      {
        Integer level = new Integer(i);
        Hashtable lvlHT = new Hashtable();
        lvlHT.clear();

        Cluster c = (Cluster)clusterList.get(i);
        Double bestMQ = new Double(c.getObjFnValue());
        Long clusterDepth = new Long(c.getDepth());
        Integer numClusters = new Integer(c.getClusterNames().length);

        lvlHT.put(BunchAPI.CLUSTER_LEVEL,level.toString());
        lvlHT.put(BunchAPI.MQVALUE,bestMQ.toString());
        lvlHT.put(BunchAPI.CLUSTER_DEPTH,clusterDepth.toString());
        lvlHT.put(BunchAPI.NUMBER_CLUSTERS,numClusters.toString());

        resultClusters[i] = lvlHT;
      }

      results.put(BunchAPI.RESULT_CLUSTER_OBJS,resultClusters);
      stats.cleanup();

      Configuration cTmp = clusteringMethod_d.getConfiguration();
      if(cTmp instanceof bunch.NAHCConfiguration)
      {
        bunch.NAHCConfiguration nahcConf = (bunch.NAHCConfiguration)cTmp;
        if (nahcConf.getSATechnique() != null)
          nahcConf.getSATechnique().reset();
      }

      return results;
  }

  public boolean run(Hashtable args)
  {
    bunchArgs = args;

    String runMode = (String)bunchArgs.get(BunchProperties.RUN_MODE);
    if(runMode == null) return false;

    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_CLUSTER))
    {
      boolean rc;
      BunchAsyncNotify notifyClass = null;
      if (bunchArgs.get(BunchProperties.RUN_ASYNC_NOTIFY_CLASS) != null)
        notifyClass = (BunchAsyncNotify)bunchArgs.get(BunchProperties.RUN_ASYNC_NOTIFY_CLASS);

      if(notifyClass == null)
        rc = runClustering();
      else
        rc = runClusteringAsync(notifyClass);

      //prepareResultsHT();
      return rc;
    }
    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_PR_CALC))
    {
      return runPRCalc();
    }

    if(runMode.equalsIgnoreCase(BunchProperties.RUN_MODE_MQ_CALC))
    {
      return runMQCalc();
    }
    return false;
  }

class ExecuteClusteringEngine
{
    //ClusteringMethod clusteringMethod_d;
    //Hashtable bunchArgs;
    Object    monitor;

    ExecuteClusteringEngine()
    {
      //clusteringMethod_d = cm;
      //bunchArgs = ba;
      monitor = new Object();
      run();
    }

    public void run()
    {
      Runnable runThread = new Runnable()
      {
        public void run()
        {
        try{
          clusteringProcessThread = Thread.currentThread();

          startTime = System.currentTimeMillis();

          if(timeoutTimer != null)
            timeoutTimer.start();

              clusteringMethod_d.run();
          endTime = System.currentTimeMillis();
          totalTime += (endTime-startTime);

          if(timeoutTimer != null)
            timeoutTimer.stop();

          synchronized(monitor)
          { monitor.notifyAll();  }

          if(clusteringProcessThread != null)
            synchronized(clusteringProcessThread)
            { clusteringProcessThread = null;}
        }
        //catch(InterruptedException iExcpt) {System.out.println("Thrad interrupted");}
        catch(Exception threadEx){ threadEx.printStackTrace(); }
        }
      };

      Thread t = new Thread(runThread);
      //clusteringProcessThread = t;
      t.start();
      //t.interrupt();

      try
      {
        synchronized(monitor)
        {  monitor.wait();  }
      }catch(Exception e1)
      {e1.printStackTrace();}
    }
}

class ExecuteClusteringEngineAsync
{
    //ClusteringMethod clusteringMethod_d;
    //Hashtable bunchArgs;
    BunchAsyncNotify    notifyObject = null;
    bunch.SwingWorker worker_d = null;

    ExecuteClusteringEngineAsync(BunchAsyncNotify nObject)
    {
      notifyObject = nObject;
      //clusteringMethod_d = cm;
      //bunchArgs = ba;
      //monitor = new Object();
      run();
    }

    public void run()
    {
      notifyObject.setStatus(bunch.api.BunchAsyncNotify.STATUS_RUNNING);
      worker_d = new bunch.SwingWorker()
      //Runnable runThread = new Runnable()
      {
        public Object construct()
        {
          try{

            clusteringProcessThread = Thread.currentThread();

            startTime = System.currentTimeMillis();

            if(timeoutTimer != null)
              timeoutTimer.start();

            clusteringMethod_d.run();
            endTime = System.currentTimeMillis();
            totalTime += (endTime-startTime);

            if(timeoutTimer != null)
              timeoutTimer.stop();

            //synchronized(monitor)
            //{ monitor.notifyAll();  }

            //if(clusteringProcessThread != null)
            //  synchronized(clusteringProcessThread)
            //  { clusteringProcessThread = null;}
          }
          //catch(InterruptedException iExcpt) {System.out.println("Thrad interrupted");}
          catch(Exception threadEx){ threadEx.printStackTrace(); }
          return "Done";
        }
        public void interrupt()
        {
            this.suspend();
            super.interrupt();
        }
        public void finished()
        {
          notifyObject.setStatus(bunch.api.BunchAsyncNotify.STATUS_DONE);
          //notifyObject.notifyDone();
        }
      };

      worker_d.setPriority(Thread.MIN_PRIORITY);
      worker_d.start();
    }
}

//********************
// For handling timeouts
//
class TimeoutTimer implements java.awt.event.ActionListener
  {
    public void actionPerformed(java.awt.event.ActionEvent e)
    {
      try
      {
        synchronized(clusteringProcessThread)
        {
          if(clusteringProcessThread == null)
            return;
          //System.out.println("Interrupting thread");
          clusteringProcessThread.interrupt();
        }
      }catch(Exception timerEx)
      { timerEx.printStackTrace(); }
    }
  }

}
