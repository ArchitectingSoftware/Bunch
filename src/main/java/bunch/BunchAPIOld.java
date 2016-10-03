/****
 *
 *	$Log: BunchAPIOld.java,v $
 *	Revision 3.0  2002/02/03 18:41:43  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/10/22 15:48:47  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.1  2000/08/19 22:07:37  bsmitc
 *	Renamed BunchAPI to BunchAPIOld
 *
 *	Revision 3.0  2000/07/26 22:46:07  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import javax.swing.*;
import java.util.Vector;

import javax.swing.event.*;

/**
 * This is the old bunch api class.  It was renamed to BunchAPIOld after the
 * updated API was created and put in its own package.  This class is depricated
 * and will be removed in a later version of Bunch
 *
 * @author Brian Mitchell
 * @see class bunch.api.BunchAPI
 */
public
class BunchAPIOld
{

/**
 * These are the static parameters used to drive the clustering options
 */
public static final int OUTPUT_DOTTY = 1;
public static final int OUTPUT_TOMSAWYER = 2;
public static final int OUTPUT_TEXT = 3;

public static final int ALG_NAHC = 1;
public static final int ALG_SAHC = 2;
public static final int ALG_GA = 3;
public static final int ALG_OPTIMAL = 4;

public static final int MQ_TURBO = 1;
public static final int MQ_ORIG = 2;
public static final int MQ_WEIGHTED = 3;

public static final int GAMETHOD_RW = 1;
public static final int GAMETHOD_TOURNAMENT = 2;

public static final float DEFAULT_OPM_THRESHOLD = (float)3.0;

FileDialog fileSelector_d;
ClusteringMethod clusteringMethod_d;
GraphOutput graphOutput_d;
Graph initialGraph_d;
Graph lastResultGraph_d;
BunchPreferences preferences_d;
Configuration configuration_d;
DefaultListModel standardNodeListModel_d = new DefaultListModel();
DefaultListModel suppliersListModel_d = new DefaultListModel();
DefaultListModel clientsListModel_d = new DefaultListModel();
DefaultListModel centralListModel_d = new DefaultListModel();
DefaultListModel librariesListModel_d = new DefaultListModel();
String mqCalc_d = "default";
String clusterName_d = "";
String outputMethod = "Dotty";
String inputFileName = "";
String outputFileName = "";
boolean doDrifters = true;
HillClimbingConfiguration hcc = null;
GAConfiguration gac = null;
String mdgFileName = "";
String delims = "";

int   alg = ALG_NAHC;
int   mqFn = MQ_TURBO;

/**
 * This is the constructor for the BunchAPIOld class.
 *
 * @param sMDGFile  The name of the mdg file to be used for the clustering
 *                  process
 */
public
BunchAPIOld(String sMDGFile) throws Exception
{
	mdgFileName = sMDGFile;
	delims = " ";
  	init(sMDGFile," ");
}



/**
 * This is the constructor for the BunchAPIOld class.
 *
 * @param sMDGFile  The name of the mdg file to be used for the clustering
 *                  process
 *
 * @param  delims   The list of delimters to use in the parsing process
 */
public
BunchAPIOld(String sMDGFile, String delims) throws Exception
{
    mdgFileName = sMDGFile;
    delims = delims;
    init(sMDGFile,delims);
}


/**
 * The init method initializes the clustering engine with default parameters
 * similar to those found on the BunchAPI front end.
 *
 * @param sMDGFile  The name of the mdg file to be used for the clustering
 *                  process
 *
 * @param  delims   The list of delimters to use in the parsing process
 */
public void init(String sMDG, String delims) throws Exception
{
   preferences_d = (BunchPreferences)(Beans.instantiate(null, "bunch.BunchPreferences"));
   Graph.setObjectiveFunctionCalculatorFactory(preferences_d.getObjectiveFunctionCalculatorFactory());
   inputFileName = sMDG;
   outputFileName = sMDG;
   parseGraph(sMDG,delims);
   setOutputFile(sMDG);
   setClusteringMethod(ALG_SAHC);
   setMQCalc(MQ_TURBO);
   setOutputMethod(OUTPUT_DOTTY);
   setDriftersOptimization(true);
   setDefaultPreferences();
}

/**
 * Loads the ClusteringMethod class that corresponds to the name passed as parameter
 * The class is loaded by asking the ClusteringMethodFactory for it.
 *
 * @param method the name of the ClusteringMethod to load
 * @see bunch.ClusteringMethodFactory.getMethod(ClusteringMethod)
 */
public
void
setClusteringMethod(int iMethod)
{
  String method;

   alg = iMethod;

   /**
    * Set the clustering method based on the method id.  The mehod is a string
    * used to select the name from the factory.  This is not a good coding
    * approach and has been improved in the non-depricated version of the BunchAPI
    */
   switch(iMethod)
   {
      case ALG_NAHC:
         method = "NAHC:    nodes in [50,100)";
         setHillClimbingConfiguration(1,10,0.1); //NAHC
         configuration_d = hcc;
         break;
      case ALG_SAHC:
         method = "SAHC:    nodes in [10,50)";
         setHillClimbingConfiguration(1,5,0.1); //SAHC
         configuration_d = hcc;
         break;
      case ALG_GA:
         method = "GA:          nodes in [100,...)";
         setGAConfiguration(6900,230,0.6,0.025,GAMETHOD_RW); //GA
         configuration_d = gac;
         break;
      case ALG_OPTIMAL:
         method = "Optimal: nodes in [1, 10)";
         configuration_d = null;
         break;
      default:
         method = "SAHC:    nodes in [10,50)";
         break;
   }

  /**
   * Set the preferences object using the default preferences of the
   * clustering method.  Also set the clustering method factory with the
   * appropriate clustering algorithm
   */
  if (!method.getClass().getName().equals(method)) {
    clusteringMethod_d = preferences_d.getClusteringMethodFactory().getMethod(method);
    if (configuration_d == null)
      configuration_d = clusteringMethod_d.getConfiguration();

    clusteringMethod_d.setIterationListener(null);

    if (initialGraph_d!=null&&configuration_d!=null) {
      configuration_d.init(initialGraph_d);

    clusteringMethod_d.setConfiguration(configuration_d);

    }
  }
}

/**
 * Parse the mdg graph with the specified delimeters and create the graph
 * object for the api class.  This graph object will be used by the other
 * methods in this class.
 *
 * @param sMDGFile  The name of the mdg file to be used for the clustering
 *                  process
 *
 * @param  delims   The list of delimters to use in the parsing process
 */
void
parseGraph(String filename, String delims)
{
    Parser p = preferences_d.getParserFactory().getParser("dependency");
    p.setInput(filename);
    p.setDelims(delims);
    initialGraph_d = (Graph)p.parse();
    if (configuration_d != null) {
     configuration_d.init(initialGraph_d);
    }
}

/**
 * This method is used to select the output file name
 *
 * @param filename Sets the name of the output file.  The output file type
 *                 is set in the output driver
 */
public
void
setOutputFile(String filename)
{
    outputFileName = filename;
}

/**
 * This method is used to parse the MDG graph.  The result, if successful is
 * to properly initialize the graph object (initialGraph_d)
 *
 * @param filename the name of the MDG file
 */
void
parseClusterFile(String filename)
{
    Parser p = preferences_d.getParserFactory().getParser("cluster");
    p.setInput(filename);
    p.setObject(initialGraph_d);
    p.parse();
}

/**
 * Called to run the clustering algorithm.  This method is called after all
 * of the parameters are set.
 *
 */
public
void
run()
{
  //configures the bunch environment based on the options selected
  configureOptions();

    /**
     * Setup the locked nodes, or the nodes that do not participate in the
     * clustering process.  These are libraries or omnipresent modules
     */
    int[] clust = initialGraph_d.getClusters();
    boolean[] locks = initialGraph_d.getLocks();
    for (int i=0; i<clust.length; ++i) {
      if (clust[i] != -1) {
        locks[i] = true;
      }
    }

    /**
     * Iniitalize the clustering method, set the output file and perform the
     * clustering.
     */
    clusteringMethod_d.initialize();
    clusteringMethod_d.setGraph(initialGraph_d.cloneGraph());
    graphOutput_d.setBasicName(outputFileName);
    clusteringMethod_d.run();
    outputGraph();
}

/**
 * This method is used to run a clustering algorithm multiple times.  It is
 * useful for performing expirements
 *
 * @param howMany   Sets the number of times to run the clustering algorithm.
 *
 * @exception Exception Cascades downstream exceptions upwards.
 */
public
void
runBatch(int howMany) throws Exception
{
	int expNum = 0;

        /**
         * Create a log file to capture the clustering results
         */
	configuration_d.createLogFile();

        /**
         * Perform the clustering run the specified number of times
         */
	for (int z = 0; z < howMany; z++)
	{
          expNum++;
          configureOptions();

    	  int[] clust = initialGraph_d.getClusters();
    	  boolean[] locks = initialGraph_d.getLocks();
    	  for (int i=0; i<clust.length; ++i)
          {
            if (clust[i] != -1) {
              locks[i] = true;
            }
    	  }

    	  clusteringMethod_d.initialize();
    	  clusteringMethod_d.setGraph(initialGraph_d.cloneGraph());
    	  graphOutput_d.setBasicName(outputFileName);
		configuration_d.expNumber_d	= expNum;
		configuration_d.runBatch_d = true;
    	  clusteringMethod_d.run();
    	  outputGraph();
	}

        /**
         * Were done, close the log file
         */
	configuration_d.closeLogFile();
}


/**
 * stores the graph that resulted from the previous level of hierarchial clustering.
 * This can be used later to generate another level of the graph and then optimize it
 *
 * @param g the graph to set as result graph
 * @see #getLastResultGraph()
 */
public
void
setLastResultGraph(Graph g)
{
  lastResultGraph_d = g;
}

/**
 * Obtain the last graph in the clustering hierarchy.
 *
 * @see #setLastResultGraph(bunch.Graph)
 */
public
Graph
getLastResultGraph()
{
  return lastResultGraph_d;
}

/**
 * Obtain the current GraphOutput instance in use
 *
 * @return the current instance of a subclass of the GraphOutput class
 */
public
GraphOutput
getGraphOutput()
{
  return graphOutput_d;
}

/**
 * Obtain the current ClusteringMethod instance in use
 *
 * @return the current instance of a subclass of the ClusteringMethod class
 */
public
ClusteringMethod
getClusteringMethod()
{
  return clusteringMethod_d;
}


/**
 * Set the MQ Calculator function by its ordinal number that were defined
 * as public static final int constants.
 *
 * @param iCalc The ordinal value for the MQ Calculator
 */
public void setMQCalc(int iCalc)
{
   mqFn = iCalc;

   /**
    * As of this writing the MQ calculator must be specified by name, this
    * design has been improved and is incorporated into the new BunchAPI class.
    */
   switch(iCalc)
   {
      case MQ_TURBO:
         mqCalc_d = "Turbo MQ Function";
         break;
      case MQ_ORIG:
         mqCalc_d = "MQ Function";
         break;
      case MQ_WEIGHTED:
         mqCalc_d = "Eperimental Weighted 2";
         break;
      default:
         mqCalc_d = "Turbo MQ Function";
         break;
   }

   /**
    * Get the objective function calculator from the factory
    */
   String objFnCalc = mqCalc_d;
   (preferences_d.getObjectiveFunctionCalculatorFactory()).setCurrentCalculator(objFnCalc);
}

/**
 * Configures the bunch environment based on the options selected.  For now
 * this includes setting the objective function calculator.
 */
public
void
configureOptions()
{
   String objFnCalc = mqCalc_d;
   (preferences_d.getObjectiveFunctionCalculatorFactory()).setCurrentCalculator(objFnCalc);
}

/**
 * Sets the hill climbing configuration paramers.
 *
 * @param iIterations The number of iterations to perform
 * @param iPopSize    The population size
 * @param dThreshold  The threshold to determine convergance
 */
public
void
setHillClimbingConfiguration(int iInterations, int iPopSz, double dThreshold )
{
   hcc = new HillClimbingConfiguration();
   hcc.init(initialGraph_d);
   hcc.setNumOfIterations(iInterations);
   hcc.setPopulationSize(iPopSz);
   hcc.setThreshold(dThreshold);
}

/**
 * Sets the genetic algorithm configuration paramers.
 *
 * @param iIterations     The number of iterations to perform
 * @param iPopSize        The population size
 * @param crossThreshold  The crossover probability 0 <= x <= 1
 * @param mutationThreshod The mutation probability 0 <= x <= 1
 * @param iMethod         The selection method
 */
public
void
setGAConfiguration(int iInterations, int iPopSz, double crossThreshold,
         double mutationThreshold, int iMethod )
{
   gac = new GAConfiguration();
   gac.init(initialGraph_d);
   gac.setNumOfIterations(iInterations);
   gac.setPopulationSize(iPopSz);
   gac.setCrossoverThreshold(crossThreshold);
   gac.setMutationThreshold(mutationThreshold);

   if (iMethod == GAMETHOD_RW)
      gac.setMethod("roulette wheel");
   else if (iMethod == GAMETHOD_TOURNAMENT)
      gac.setMethod("tournament");
   else
      gac.setMethod("roulette wheel");
}

/**
 * Select the output method, which is used to setup the output file format
 *
 * @param iMethod The output method based on the defined constnts.
 */
public
void
setOutputMethod(int iMethod)
{
   String sMethod;

   /**
    * As of this writing the mehod must be specified by string. This has
    * been improved in the latest version of the BunchAPI.
    */
   switch(iMethod)
   {
      case OUTPUT_DOTTY:
         sMethod = "Dotty";
         break;
      case OUTPUT_TOMSAWYER:
         sMethod = "Tom Sawyer";
         break;
      case OUTPUT_TEXT:
         sMethod = "Text";
         break;
      default:
         sMethod = "Dotty";
         break;
   }

   outputMethod = sMethod;
   graphOutput_d = preferences_d.getGraphOutputFactory().getOutput(outputMethod);
   graphOutput_d.setBaseName(outputFileName);
   graphOutput_d.setBasicName(outputFileName);
}

/**
 * Used to indicate if "drifter" modules should be orphan adopted
 *
 * @param doIT If true, consolidate the drifters, if false dont consolidate
 *             the drifters.
 */
public
void
setDriftersOptimization(boolean doIt)
{
   doDrifters = doIt;
}

/**
 * Outputs the partitioned graph based on the previously selected output factory.
 */
void
outputGraph()
{
    graphOutput_d = preferences_d.getGraphOutputFactory().getOutput(outputMethod);
    graphOutput_d.setBaseName(clusterName_d);
    graphOutput_d.setBasicName(outputFileName);
    graphOutput_d.setCurrentName(outputFileName);
    graphOutput_d.setGraph(clusteringMethod_d.getBestGraph());
    graphOutput_d.write();
}

/**
 * Sets the default parametes for the GA, SAHC and NAHC algorithms.  The newer
 * BunchAPI class supports the generic HillClimbing feature.
 */
private void setDefaultPreferences()
{
  /**
   * Set the defaults based on the defaults in the Bunch GUI.
   */
   setGAConfiguration(6900,230,0.6,0.025,GAMETHOD_RW); //GA
   setHillClimbingConfiguration(100,5,0.1); //SAHC

   if (mqFn == MQ_TURBO)
   {
      if (alg == ALG_NAHC)
         setHillClimbingConfiguration(1,10,1.0); //NAHC
      else if (alg == ALG_NAHC)
         setHillClimbingConfiguration(1,5,1.0); //SAHC
   }
   else
   {
      if (alg == ALG_NAHC)
         setHillClimbingConfiguration(200,10,0.1); //NAHC
      else if (alg == ALG_NAHC)
         setHillClimbingConfiguration(100,5,0.1); //SAHC
   }
}

/**
 * Excludes omnipresent modules based on the default threshold
 *
 */
public
void
excludeOmnipresentModules()
{
   excludeOmnipresentModules(DEFAULT_OPM_THRESHOLD);
}

/**
 * Excludes omnipresent modules based on the specified threshold.
 *
 * @param threshod  The multiple of average edge weight used to determine if a
 *                  module is omnipresent
 */
public
void
excludeOmnipresentModules(float threshold)
{
  Node[] nodeList = initialGraph_d.getNodes();

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
        && !usesModule(librariesListModel_d, nodeList[i].getName())) {
      standardNodeListModel_d.removeElement(nodeList[i].getName());
      clientsListModel_d.addElement(nodeList[i].getName());
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
        && !usesModule(librariesListModel_d, nodeList[i].getName())) {
      standardNodeListModel_d.removeElement(nodeList[i].getName());
      suppliersListModel_d.addElement(nodeList[i].getName());
    }
  }

  //looking for central nodes (nodes that are clients and suppliers
  for (int i=0; i<clientsListModel_d.getSize(); ++i) {
    String client = (String) clientsListModel_d.getElementAt(i);
    for (int j=0; j<suppliersListModel_d.getSize(); j++) {
      String supp = (String) suppliersListModel_d.getElementAt(j);
      if (client.equals(supp)){
        centralListModel_d.addElement(client);
        break;
      }
    }
  }

  for (int i=0; i<centralListModel_d.getSize(); ++i) {
    String name = (String)centralListModel_d.elementAt(i);
    clientsListModel_d.removeElement(name);
    suppliersListModel_d.removeElement(name);
  }

  /**
   * Now that the omnipresent modules have been marked, update the graph
   * class to respect these settings
   */
  arrangeLibrariesClientsAndSuppliers();
}

/**
 * This method sets the libraries, clients and suppliers defined in their
 * respective panes to the graph, just previous to processing.
 */
private
void
arrangeLibrariesClientsAndSuppliers()
{
  Node[] nodeList = initialGraph_d.getNodes();
  Node[] originalList = nodeList;

  //tag the nodes with their type (matching them by name from the lists)
  for (int j=0; j<originalList.length; ++j) {
    for (int i=0; i<suppliersListModel_d.size(); ++i) {
      String name = originalList[j].getName();
      if (name.equals((String)suppliersListModel_d.elementAt(i))) {
        originalList[j].setType(Node.SUPPLIER);
        break;
      }
    }
    for (int i=0; i<clientsListModel_d.size(); ++i) {
      String name = originalList[j].getName();
      if (name.equals((String)clientsListModel_d.elementAt(i))) {
        originalList[j].setType(Node.CLIENT);
        break;
      }
    }
    for (int i=0; i<centralListModel_d.size(); ++i) {
      String name = originalList[j].getName();
      if (name.equals((String)centralListModel_d.elementAt(i))) {
        originalList[j].setType(Node.CENTRAL);
        break;
      }
    }

    for (int i=0; i<librariesListModel_d.size(); ++i) {
      String name = originalList[j].getName();
      if (name.equals((String)librariesListModel_d.elementAt(i))) {
        originalList[j].setType(Node.LIBRARY);
        break;
      }
    }
  }

  /**
   * Calculate the new node list size
   */
  nodeList = new Node[originalList.length -
                (clientsListModel_d.size()+suppliersListModel_d.size()+
                +centralListModel_d.size()+librariesListModel_d.size())];
  int j=0;

  //build new node list without omnipresent modules
  for (int i=0; i<originalList.length; ++i) {
    if (originalList[i].getType() == Node.NORMAL) {
      nodeList[j++] = originalList[i].cloneNode();
    }
  }

  //rebuild dependencies for the new list
  for (int i=0; i<nodeList.length; ++i) {
      int[] deps = nodeList[i].getDependencies();
      if (deps != null) {
        int[] weights = nodeList[i].getWeights();
        int count=0;
        for (int n=0; n<deps.length; ++n) {
          if (originalList[deps[n]].getType() != Node.NORMAL) {
            count++;
            deps[n] = -1;
          }
          else {
            String name = originalList[deps[n]].getName();
            for (int x=0; x<nodeList.length; ++x) {
              if (name.equals(nodeList[x].getName())) {
                deps[n] = x;
                break;
              }
            }
          }
        }

        if (deps.length == count) {
          nodeList[i].setDependencies(null);
          nodeList[i].setWeights(null);
        }
        else {
          int[] ndeps = new int[deps.length-count];
          int[] ws = null;
          if (weights != null) {
            ws = new int[deps.length-count];
          }
          int m=0;
          for (int x=0; x<deps.length; ++x) {
            if (deps[x] != -1) {
              ndeps[m++] = deps[x];
              if (weights != null) {
                ws[m-1] = weights[x];
              }
            }
          }
          nodeList[i].setDependencies(ndeps);
          nodeList[i].setWeights(ws);
        }
      }
  }

  //reinitialize the graph with the new nodes
  initialGraph_d.initGraph(nodeList.length);
  initialGraph_d.clear();
  initialGraph_d.setNodes(nodeList);
  initialGraph_d.setOriginalNodes(originalList);
}

/**
 * Determines if a modules is used.
 *
 * @param list The list of modules
 * @param element   The element to check against the list
 *
 * @returns True if the element is used, false if not
 */
private
boolean
usesModule(DefaultListModel list, String element)
{
  for (int i=0; i<list.size(); ++i) {
    if (element.equals((String)list.elementAt(i))) {
      return true;
    }
  }
  return false;
}

/**
 * Returns the best graph from the clustering method object.
 */
public
Graph
getBestGraph()
{
  if (clusteringMethod_d != null)
    return clusteringMethod_d.getBestGraph();
  else
     return null;
}

}

