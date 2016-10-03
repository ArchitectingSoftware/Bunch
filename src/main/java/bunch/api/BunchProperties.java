
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
 *	$Log: BunchProperties.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *
 *	Revision 3.3  2001/03/17 14:55:47  bsmitc
 *	Added additional features to the API
 *
 *	Revision 3.2  2000/11/30 01:49:22  bsmitc
 *	Added support for various tests and statistical gathering
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

/**
 * This class defines the static properties used by the Bunch API to establish
 * the default behavior.
 *
 * @author Brian Mitchell
 */
public class BunchProperties extends Properties{

  //HERE ARE THE STRING KEYS
  public final static String MDG_INPUT_FILE_NAME      = "MDGInputFile";
      public final static String MDG_PARSER_DELIMS      = "MDGParserDelimeters";
      public final static String MDG_PARSER_USE_SPACES  = "MDGParserUseSpaces";
      public final static String MDG_PARSER_USE_TABS    = "MDGParserUseTabs";
  public final static String OUTPUT_DEVICE            = "OutputDevice";
      public final static String OUTPUT_FILE            = "OutputFile";
      public final static String OUTPUT_HASHTABLE       = "OutputHashtable";
  public final static String MDG_OUTPUT_FILE_BASE     = "MDGOutputFile";
  public final static String  MDG_OUTPUT_MODE         = "MDGOutputMode";
      public final static String OUTPUT_NONE              = "NoOutputFile";
      public final static String OUTPUT_DETAILED          = "MDGOutputDetailed";
      public final static String OUTPUT_MEDIAN            = "MDGOutputMedian";
      public final static String OUTPUT_TOP               = "MDGOutputTop";
  public final static String OUTPUT_TREE                  = "OutputTree";
  public final static String OUTPUT_FORMAT                = "OutputFormat";
      public final static String  DOT_OUTPUT_FORMAT       = "DotOutputFormat";
      public final static String  TEXT_OUTPUT_FORMAT      = "TextOutputFormat";
      public final static String  GXL_OUTPUT_FORMAT       = "GXLOutputFormat";
      public final static String  NULL_OUTPUT_FORMAT      = "NullOutputFormat";
  public final static String OUTPUT_DIRECTORY             = "OutputDirectory";
  public final static String CLUSTERING_APPROACH      = "ClusteringApproach";
      public final static String ONE_LEVEL                = "ClustApproachOneLevel";
      public final static String AGGLOMERATIVE            = "ClustApproachAgglomerative";
  public final static String MQ_CALCULATOR_CLASS          = "MQCalculatorClass";
      public final static String MQ_CALC_DEFAULT_CLASS      = "bunch.TurboMQIncrW";
  public final static String ECHO_RESULTS_TO_CONSOLE  = "EchoResultsToConsole";
  public final static String CLUSTERING_ALG           = "ClusteringAlgorithm";
      public final static String ALG_EXHAUSTIVE         = "Exhaustive";
      public final static String ALG_GA                 = "GA";
        public final static String ALG_GA_SELECTION_METHOD = "GASelectionMethod";
          public final static String ALG_GA_SELECTION_TOURNAMENT = "GASelectionMethodTournament";
          public final static String ALG_GA_SELECTION_ROULETTE = "GASelectionMethodRoulette";
        public final static String ALG_GA_POPULATION_SZ    = "GAPopulationSize";
        public final static String ALG_GA_CROSSOVER_PROB   = "GACrossoverProbability";
        public final static String ALG_GA_MUTATION_PROB    = "GAMutationProb";
        public final static String ALG_GA_NUM_GENERATIONS  = "GANumGenerations";
      public final static String ALG_NAHC               = "NAHC";
        public final static String ALG_NAHC_POPULATION_SZ = "NAHCPopulationSize";
        public final static String ALG_NAHC_HC_PCT        = "NAHCHillClimbPct";
        public final static String ALG_NAHC_RND_PCT       = "NAHCRandomizePct";
        public final static String ALG_NAHC_SA_CLASS     = "NAHCSimulatedAnnealingClass";
        public final static String ALG_NAHC_SA_CONFIG     = "NAHCSimulatedAnnealingConfig";
      //
      //The hill climbing technique is an alias for NAHC
      //
      public final static String ALG_HILL_CLIMBING      = "NAHC";
        public final static String ALG_HC_POPULATION_SZ = "NAHCPopulationSize";
        public final static String ALG_HC_HC_PCT        = "NAHCHillClimbPct";
        public final static String ALG_HC_RND_PCT       = "NAHCRandomizePct";
        public final static String ALG_HC_SA_CLASS     = "NAHCSimulatedAnnealingClass";
        public final static String ALG_HC_SA_CONFIG     = "NAHCSimulatedAnnealingConfig";
      public final static String ALG_SAHC               = "SAHC";
        public final static String ALG_SAHC_POPULATION_SZ = "SAHCPopulationSize";
  public final static String RUN_MODE                 = "RunMode";
      public final static String RUN_MODE_CLUSTER       = "Cluster";
      public final static String RUN_MODE_MQ_CALC       = "MQCalculator";
        public final static String MQCALC_MDG_FILE        = "MQCalcMDGFile";
        public final static String MQCALC_SIL_FILE        = "MQCalcSILFile";
      public final static String RUN_MODE_MOJO_CALC     = "MOJOCalculator";
        public final static String MOJO_FILE_1            = "MOJOFile1";
        public final static String MOJO_FILE_2            = "MOJOFile2";
      public final static String RUN_MODE_PR_CALC       = "PRCalculator";
          public final static String  PR_EXPERT_FILE      = "PRExpertDecomposition";
          public final static String  PR_CLUSTER_FILE     = "PRClusterDecomposition";
  public final static String  LIBRARY_LIST            = "LibraryList";
  public final static String  OMNIPRESENT_CLIENTS     = "OmnipresentClients";
  public final static String  OMNIPRESENT_SUPPLIERS   = "OmnipresentSuppliers";
  public final static String  OMNIPRESENT_BOTH        = "OmnipresentBoth";
  public final static String  PROGRESS_CALLBACK_CLASS = "ProgressCallbackClass";
  public final static String  PROGRESS_CALLBACK_FREQ  = "ProgressCallbackFreq";
  public final static String  TIMEOUT_TIME            = "TimoutTime";
  public final static String  SPECIAL_MODULE_HASHTABLE= "SpecialModuleHashTable";
  public static final String  MDG_GRAPH_OBJECT         = "MDGGraphObject";
  public static final String  CLUSTERING_THREAD_PRIORITY = "ClusteringThreadPriority";
  public static final String  RUN_ASYNC_NOTIFY_CLASS     = "RunAsyncNotifyClass";
  public static final String  USER_DIRECTED_CLUSTER_SIL  = "UserDirectedClusterSIL";
  public static final String  LOCK_USER_SET_CLUSTERS     = "LockUserSetClusters";

  public BunchProperties() {
      super();
      defaults = new Properties();
      defaults.setProperty(MDG_PARSER_USE_SPACES,"True");
      defaults.setProperty(MDG_PARSER_USE_TABS, "True");
      defaults.setProperty(MDG_OUTPUT_MODE,OUTPUT_MEDIAN);
      defaults.setProperty(ECHO_RESULTS_TO_CONSOLE,"False");
      defaults.setProperty(CLUSTERING_ALG,ALG_NAHC);
      defaults.setProperty(RUN_MODE,RUN_MODE_CLUSTER);
      defaults.setProperty(MDG_OUTPUT_MODE,OUTPUT_MEDIAN);
      defaults.setProperty(OUTPUT_TREE,"False");
      defaults.setProperty(OUTPUT_DEVICE,OUTPUT_FILE);
      defaults.setProperty(CLUSTERING_APPROACH,AGGLOMERATIVE);
      defaults.setProperty(MQ_CALCULATOR_CLASS,MQ_CALC_DEFAULT_CLASS);
      defaults.setProperty(PROGRESS_CALLBACK_FREQ,"1000");
      defaults.setProperty(OUTPUT_FORMAT,NULL_OUTPUT_FORMAT);
      defaults.setProperty(MQ_CALCULATOR_CLASS,"Incremental MQ Weighted");
      defaults.setProperty(MQ_CALC_DEFAULT_CLASS,"Incremental MQ Weighted");
  }
}