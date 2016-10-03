/****
 *
 *	$Log: ClusteringProgressDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:46  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.10  2000/11/26 15:48:13  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.9  2000/10/22 15:48:48  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.8  2000/08/19 00:44:39  bsmitc
 *	Added support for configuring the amount of randomization performed when
 *	the user adjusts the "slider" feature of NAHC.
 *
 *	Revision 3.7  2000/08/18 21:08:00  bsmitc
 *	Added feature to support tree output for dotty and text
 *
 *	Revision 3.6  2000/08/17 00:26:04  bsmitc
 *	Fixed omnipresent and library support for nodes in the MDG not connected to
 *	anything but the omnipresent nodes and libraries.
 *
 *	Revision 3.5  2000/08/16 00:12:45  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.4  2000/08/13 18:40:06  bsmitc
 *	Added support for SA framework
 *
 *	Revision 3.3  2000/08/11 15:04:28  bsmitc
 *	Added support for producing optimal output on the clustering progress
 *	dialog window
 *
 *	Revision 3.2  2000/08/11 13:19:10  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.1  2000/08/09 14:17:48  bsmitc
 *	Changes made to support agglomerative clustering feature.
 *
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
//import java.util.*;

import bunch.stats.*;

/**
 * Class that shows the current progress of a running ClusteringMethod.
 * The class launches the method and then updates the progress bars
 * accordingly, waiting for user input when finished. The dialog also
 * lets the user pause the optimization and output it, then resuming the
 * optimization process. Instances of this class are managed and created
 * from the bunch.BunchFrame class. Also, this class implements the
 * bunch.IterationListener interface to connect with the clustering method
 * and know its status.
 *
 * @author Brian Mitchell
 * @see bunch.ClusteringMethod
 * @see bunch.BunchFrame.runActionButton_d_actionPerformed(java.awt.event.ActionEvent)
 * @see bunch.BunchFrame
 */
public
class ClusteringProgressDialog
  extends JDialog
  implements IterationListener{

public static final int MODE_END      = 1;
public static final int MODE_LEVEL    = 2;
public static final int MODE_TEMP     = 3;

long updateCounter = 0;

JPanel panel1 = new JPanel();
GridBagLayout gridBagLayout1 = new GridBagLayout();
JLabel percentLabel_d = new JLabel();
JLabel overallPercentLabel_d = new JLabel();
JProgressBar overallProgressBar_d = new JProgressBar();
JLabel timeTitleLabel_d = new JLabel();
JLabel currentTimeLabel_d = new JLabel();
BunchFrame frame_d = null;
GraphOutput graphOutput_x = null;
ClusteringMethod clusteringMethod_x = null;
bunch.SwingWorker worker_d;
int iteration_d=0, overallIteration_d;
boolean finished_d=false;
boolean showOverallProgressBar_d= true;
JLabel jLabel1 = new JLabel();
JLabel IterationsProcessed_st = new JLabel();
JLabel jLabel2 = new JLabel();
long startTime;
Timer eventTimer;
Timer toTimer;
JPanel BestClustPanel = new JPanel();
GridBagLayout gridBagLayout2 = new GridBagLayout();
JLabel jLabel4 = new JLabel();
JLabel jLabel5 = new JLabel();
JLabel DepthCount = new JLabel();
JLabel bestMQValueFound_d = new JLabel();
JLabel MQEvalCount = new JLabel();
JPanel jPanel1 = new JPanel();
JButton outputButton_d = new JButton();
JButton viewPB = new JButton();
JButton pauseButton_d = new JButton();
JButton cancelButton_d = new JButton();
JLabel CurrentActivity = new JLabel();
JLabel jLabel3 = new JLabel();

StatsManager stats = StatsManager.getInstance();
boolean cancelPending = false;
boolean isPaused = false;
JLabel jLabel6 = new JLabel();
JLabel numClusters = new JLabel();
Graph origGraph = null;
RunMonitor monitor = new RunMonitor();
String basicTitle = "";
JLabel progressLbl = new JLabel();
JLabel progressMsg = new JLabel();
boolean isExhaustive = false;
JLabel gotoLBL = new JLabel();
JComboBox lvlViewerCB = new JComboBox();
java.util.LinkedList bestCLL = new java.util.LinkedList();
Cluster currentViewC = null;

/**
 * Dialog constructor
 */
public
ClusteringProgressDialog(Frame frame, String title, boolean modal)
{
    super(frame, title, modal);
    frame_d = (BunchFrame)frame;

    /**
     * Get required initialization objects from the parent frame
     */
    startTime = System.currentTimeMillis();
    graphOutput_x = frame_d.getGraphOutput();
    clusteringMethod_x = frame_d.getClusteringMethod();
    clusteringMethod_x.setIterationListener(this);

    String methodName = clusteringMethod_x.getClass().getName();
    if (methodName.equals("bunch.GAClusteringMethod"))
      showOverallProgressBar_d = false;

    eventTimer = new Timer(2000,new updateTimer());
    toTimer = new Timer((int)frame_d.getTimoutTime(),new timeoutTimer());


    try  {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
}


/**
 * This method updates the title of the window.  The title is updated as the
 * clusering activity continues.
 */
public void updateTitle(int level)
{
  setTitle(basicTitle + " (Level "+level+")");
}

/**
 * This method is used to start the clustering process.  Notice that the clustering
 * process uses the swing worker utility to run the clustering process in another
 * thread.  This approach leaves the UI responsive during the clustering activity.
 */
public void startClustering()
{
  /*
   * Constructing the SwingWorker() causes a new Thread
   * to be created that will call construct(), and then
   * finished().  Note that finished() is called even if
   * the worker is interrupted because we catch the
   * InterruptedException in doWork().
   * (Note: a SwingWorker object is necessary because Swing (a.k.a JFC)
   * components are not thread-aware.
   */

  /**
   * If the user wants to limit the runtime start the timer - toTimer.
   */
  if(frame_d.limitRuntime())
  {
    toTimer.start();
  }

  String basicTitle = this.getTitle();
  bestCLL.clear();

  /**
   * Create the worker thread
   */
  worker_d = new bunch.SwingWorker()
  {
    public Object construct()
    {
      /**
       * START CLUSTERING
       */
      try
      {
        int level=0;
        CurrentActivity.setText("Clustering...");
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        eventTimer.start();

        if(frame_d.isAgglomerativeTechnique())
          updateTitle(level);

        /**
         * RUN the clustering method
         */
        clusteringMethod_x.run();

        currentViewC = new Cluster(clusteringMethod_x.getBestGraph().cloneGraph(),
              clusteringMethod_x.getBestGraph().getClusters());

        /**
         * Save the best cluster object from the clustering run
         */
        currentViewC.force();
        currentViewC.copyFromCluster(clusteringMethod_x.getBestCluster());

        bestCLL.addLast(currentViewC);

        /**
         * If agglomerative mode setup for the next level(s)
         */
        if(frame_d.isAgglomerativeTechnique())
        {
          /**
           * Get the best graph
           */
          Graph g = clusteringMethod_x.getBestGraph().cloneGraph();

          int []cNames = g.getClusterNames();

          /**
           * While there is more then 1 cluster, there are still more levels
           * to create.
           */
          while(cNames.length>1)
          {
            level++;
            updateTitle(level);

            /**
             * Create the next level graph from the current graph
             */
            NextLevelGraph nextL = new NextLevelGraph();
            Graph newG=nextL.genNextLevelGraph(g);

            newG.setPreviousLevelGraph(g);
            newG.setGraphLevel(g.getGraphLevel()+1);

            clusteringMethod_x.setGraph(newG);
            clusteringMethod_x.initialize();

            /**
             * Now cluster the next level graph
             */
            clusteringMethod_x.run();

            currentViewC = new Cluster(clusteringMethod_x.getBestGraph().cloneGraph(),
                                      clusteringMethod_x.getBestGraph().getClusters());

            /**
             * Get the results and add them to the tree of results
             */
            currentViewC.force();
            currentViewC.copyFromCluster(clusteringMethod_x.getBestCluster());
            bestCLL.addLast(currentViewC);

            g = clusteringMethod_x.getBestGraph().cloneGraph();

            cNames = g.getClusterNames();
          }
        }
      }catch(Exception ex)
      {ex.printStackTrace();}

      /**
       * Finished clustering
       */
      return "Done";
    }

    /**
     * Used to interrupt the current swing worker thread.
     */
    public void interrupt()
    {
            this.suspend();
            super.interrupt();
    }

    /**
     * Used as a callback to notifiy that the clustering activity has finished.
     */
    public void finished()
    {
      /**
       * Stop the timer
       */
      eventTimer.stop();
      toTimer.stop();

      CurrentActivity.setText("Post Processing...");

      if (showOverallProgressBar_d)
        overallProgressBar_d.setValue(overallProgressBar_d.getMaximum());

      updateStats();

      /**
       * Update the UI by enabling/disabling certain buttons
       */
      outputButton_d.setEnabled(false);
      pauseButton_d.setEnabled(false);
      cancelButton_d.setText("Close");

      /**
       * If dotty is the output format, enable the view button
       */
      if(frame_d.getOutputMethod().equals("Dotty"))
        viewPB.setEnabled(true);

      /**
       * Dump the runtime stats
       */
      bunch.stats.StatsManager.cleanup();

      Configuration cTmp = clusteringMethod_x.getConfiguration();
      if(cTmp instanceof bunch.NAHCConfiguration)
      {
        bunch.NAHCConfiguration nahcConf = (bunch.NAHCConfiguration)cTmp;
        if (nahcConf.getSATechnique() != null)
          nahcConf.getSATechnique().reset();
      }

      /**
       * Dump the output
       */
      outputGraph(MODE_END);

      /**
       * Update the UI
       */
      CurrentActivity.setForeground(Color.red.darker());
      CurrentActivity.setText("Finished Clustering!");

      /**
       * If the mode is agglomerative then update the drop down list box
       * to enable the user to traverse the different levels.
       */
      if(frame_d.isAgglomerativeTechnique())
      {
        Graph tmpG = clusteringMethod_x.getBestCluster().getGraph();
        int gLvl = tmpG.getGraphLevel();
        int medianLevel = tmpG.getMedianTree().getGraphLevel();

        for (int i = 0; i <= gLvl; i++)
          if (i == 0)
            lvlViewerCB.addItem("Level " + i+ " <-- Detail Level");
          else if (i == medianLevel)
            lvlViewerCB.addItem("Level " + i+ " <-- Median Level");
          else
            lvlViewerCB.addItem("Level " + i);

        lvlViewerCB.setEnabled(true);

        int outTechnique = graphOutput_x.getOutputTechnique();

        int median = clusteringMethod_x.getBestCluster().getGraph().getMedianTree().getGraphLevel();

        /**
         * Handle the specified output as specified by the user
         */
        switch(outTechnique)
        {
          case GraphOutput.OUTPUT_ALL_LEVELS:
          case GraphOutput.OUTPUT_MEDIAN_ONLY:
          { lvlViewerCB.setSelectedIndex(median); break;  }

          case GraphOutput.OUTPUT_DETAILED_LEVEL_ONLY:
          { lvlViewerCB.setSelectedIndex(0);  break;  }

          case GraphOutput.OUTPUT_TOP_ONLY:
          {
            lvlViewerCB.setSelectedIndex(clusteringMethod_x.getBestCluster().getGraph().getGraphLevel());
            break;
          }
        }
      }

      /**
       * Were finished with the post processing, dump the logss and set the finished
       * flag to true
       */
      setFinished(true);
      stats.dumpStatsLog();
    }
  };

  /**
   * Setting the worker thread to minimum priority will maximize the responsiveness
   * of the UI
   */
  worker_d.setPriority(Thread.MIN_PRIORITY);
  worker_d.start();
}

/**
 * Parameter-less constructor
 */
public
ClusteringProgressDialog()
{
  this(null, "", false);
}

/**
 * Outputs the current result graph created by the running clustering method
 * If the parameter is false it means that the clustering has been paused
 * to create an output graph and them presumably resume it. This means
 * that the output graph will have (appended to its name) the number of the
 * iteration the clustering method was in when it was paused.
 *
 * @param end a boolean value indicating if the clustering is finished or not
 */
public
void
outputGraph(int mode)
{
  //consolidate the drifters
  boolean state = frame_d.consolidateDriftersCB.isSelected();
  boolean driftersFound=false;

  if (state == true)
    //driftersFound = consolidateDrifters();
    if(driftersFound) System.out.println("Drifters were found!!!!");

  /**
   * Output the level specified by the user when the clustering process finishes
   * (MODE_END), or output the specified level graph (MODE_LEVEL)
   */
  if (mode == MODE_END) {
    graphOutput_x.setCurrentName(graphOutput_x.getBaseName());
  }
  else if (mode == MODE_LEVEL) {
    graphOutput_x.setCurrentName(graphOutput_x.getBaseName() + "-" + overallIteration_d);
  } else
    graphOutput_x.setCurrentName(graphOutput_x.getBaseName() + "-" + "TMP");

  /**
   * Get the graph output factory and write the output file
   */
  graphOutput_x.setGraph(clusteringMethod_x.getBestGraph());
  frame_d.setLastResultGraph(clusteringMethod_x.getBestGraph().cloneGraph());
  graphOutput_x.write();
}

/**
 * This method is used to consolidate the drifters in the provided graph.
 *
 * @return If there are drifters present, return TRUE, otherwise return FALSE.
 */
public
boolean
consolidateDrifters()
{
  Drifters d = new Drifters(clusteringMethod_x.getBestGraph());
  return d.consolidate();
}


/**
 * Component initialization
 */
void
jbInit()
  throws Exception
{
  panel1.setLayout(gridBagLayout1);
  timeTitleLabel_d.setText("Elapsed Time:");
  currentTimeLabel_d.setText("0.0  seconds                      ");
  jLabel2.setText("Total MQ Evaluations:");
  BestClustPanel.setBorder(BorderFactory.createEtchedBorder());

  BestClustPanel.setLayout(gridBagLayout2);
  jLabel4.setText("Depth (h):");
  jLabel5.setText("MQ Value:");
  DepthCount.setText("0");
  bestMQValueFound_d.setText("0.0");
  MQEvalCount.setText("0");
  Border b = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
        "Best Cluster Statistics");
  BestClustPanel.setBorder(b);

  if (showOverallProgressBar_d){
    //noop
  }
  else
    panel1.add(overallPercentLabel_d, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

  outputButton_d.setEnabled(false);
  outputButton_d.setText("Output");
  outputButton_d.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      outputButton_d_actionPerformed(e);
    }
  });

  viewPB.setEnabled(false);
  viewPB.setText("View Graph");
  viewPB.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      viewPB_actionPerformed(e);
    }
  });

  pauseButton_d.setText("Pause");
  pauseButton_d.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      pauseButton_d_actionPerformed(e);
    }
  });

  cancelButton_d.setText("Cancel");
  cancelButton_d.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      cancelButton_d_actionPerformed(e);
    }
  });

  CurrentActivity.setForeground(Color.blue);
  CurrentActivity.setText("Initializing...");
  jLabel3.setText("Activity:");
  jLabel6.setText("Number of Clusters:");
  numClusters.setText("0");
  progressLbl.setText("Progress:");
  progressMsg.setText("0/0 - 0% Finished");
  gotoLBL.setText("Go To Level:");
  lvlViewerCB.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      lvlViewerCB_actionPerformed(e);
    }
  });

  this.getContentPane().add(panel1, BorderLayout.CENTER);
  panel1.add(timeTitleLabel_d, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(3, 5, 0, 0), 10, 0));
  panel1.add(currentTimeLabel_d, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  panel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  panel1.add(IterationsProcessed_st, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 50, 0));
  panel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(8, 5, 0, 0), 10, 0));
  panel1.add(MQEvalCount, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(8, 0, 0, 0), 0, 0));
  panel1.add(BestClustPanel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  BestClustPanel.add(jLabel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 69, 0));
  BestClustPanel.add(jLabel5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 5, 0));
  BestClustPanel.add(DepthCount, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 203, 0));
  BestClustPanel.add(bestMQValueFound_d, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  BestClustPanel.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 0, 0));
  BestClustPanel.add(numClusters, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  BestClustPanel.add(gotoLBL, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
  BestClustPanel.add(lvlViewerCB, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, -3));
  panel1.add(jPanel1, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  jPanel1.add(outputButton_d, null);
  jPanel1.add(viewPB, null);
  jPanel1.add(pauseButton_d, null);
  jPanel1.add(cancelButton_d, null);
  panel1.add(CurrentActivity, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(3, 0, 0, 0), 0, 0));
  panel1.add(jLabel3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(3, 5, 0, 0), 10, 0));
  panel1.add(progressLbl, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 5, 0, 0), 0, 0));
  panel1.add(progressMsg, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

  basicTitle = this.getTitle();

  if(!(clusteringMethod_x instanceof OptimalClusteringMethod))
  {
    progressLbl.setVisible(false);
    progressMsg.setVisible(false);
    isExhaustive = false;
  }
  else
    isExhaustive = true;

  if(!frame_d.isAgglomerativeTechnique())
  {
    lvlViewerCB.setVisible(false);
    gotoLBL.setVisible(false);
  }
  else
    lvlViewerCB.setEnabled(false);
}

/**
 * Called when the "Output" button is pressed
 *
 * @param e the action event that generated this method call
 */
void
outputButton_d_actionPerformed(ActionEvent e)
{
  outputGraph(MODE_LEVEL);
}

/**
 * Called when the "Pause" button is pressed. This button changes its
 * label each time it is pressed since the function needed of it
 * changes
 *
 * @param e the action event that generated this method call
 */
void
pauseButton_d_actionPerformed(ActionEvent e)
{
  if(isPaused == false)
  {
    /**
     * THE DESIRED MODE IS PAUSE
     */
    isPaused = true;

    /**
     * Tell the worker to pause
     */
    if(worker_d != null)
      worker_d.suspend();
    eventTimer.stop();
    updateStats();
    CurrentActivity.setText("Paused");
    pauseButton_d.setText("Resume");
    outputButton_d.setEnabled(true);

    /**
     * Take a snapshot of the best cluster seen so far.
     */
    currentViewC = new Cluster(clusteringMethod_x.getBestGraph().cloneGraph(),
                               clusteringMethod_x.getBestGraph().getClusters());
    currentViewC.force();
    graphOutput_x.setGraph(clusteringMethod_x.getBestGraph().cloneGraph());

    /**
     * If the format is dotty, enable the view button.
     */
    if(frame_d.getOutputMethod().equals("Dotty"))
      viewPB.setEnabled(true);
  }
  else
  {
    /**
     * THE DESIRED MODE IS RESUME
     */
    outputButton_d.setEnabled(false);
    viewPB.setEnabled(false);
    pauseButton_d.setText("  Pause  ");
    eventTimer.start();
    CurrentActivity.setText("Clustering...");

    /**
     * Restart the worker thread
     */
    if(worker_d != null)
      worker_d.resume();
    isPaused = false;
  }
}

/**
 * Used to define when the clustering method has finished (for internal
 * purposes)
 *
 * @param v if the clustering is finished or not
 * @see #isFinished()
 */
public
void
setFinished(boolean v)
{
  finished_d = v;
  if(v == true)
    eventTimer.stop();
  else
    eventTimer.start();
}

/**
 * Used to know when the clustering method has finished (for internal
 * purposes)
 *
 * @return if the clustering is finished or not
 * @see #setFinished(boolean)
 */
public
boolean
isFinished()
{
  return finished_d;
}

/**
 * Called when the "Cancel" button is pressed
 *
 * @param e the action event that generated this method call
 */
void
cancelButton_d_actionPerformed(ActionEvent e)
{
  if(cancelPending == true)
    return;
  cancelPending = true;

  stats.cleanup();

      Configuration cTmp = clusteringMethod_x.getConfiguration();
      if(cTmp instanceof bunch.NAHCConfiguration)
      {
        bunch.NAHCConfiguration nahcConf = (bunch.NAHCConfiguration)cTmp;
        if (nahcConf.getSATechnique() != null)
          nahcConf.getSATechnique().reset();
      }

  if (isFinished()) { //in this case the cancel button will say "Close"
    setVisible(false);
    dispose();
    return;
  }

  /**
   * Pause the worker thread prior to finishing.  Give the user one last
   * chance to change their mind
   */
  if(worker_d != null)
    worker_d.suspend();

  int result = JOptionPane.showConfirmDialog(frame_d,
              "This will cancel the clustering process.\n Are you sure?", "Cancel Clustering?",
              JOptionPane.YES_NO_OPTION);

  if (result == JOptionPane.NO_OPTION)
  {
    /**
     * The user wants to continue
     */
    if(worker_d != null)
      worker_d.resume();
    cancelPending = false;
    return;
  }
  else
  {
    /**
     * The user really wants to cancel
     */
    if(worker_d != null)
      worker_d.interrupt();
    setVisible(false);
    this.setFinished(true);
    dispose();
  }
}

/**
 * Called by the clustering method when a new iteration is started. Used to update
 * the status bar and other visual elements.
 *
 * NOTE:  The UI update technique has changed, but we leave this method in here
 * so that additional callback processing can be handled if necessary in the future
 *
 * @param e the iteration event that launched this method call
 */
public
void
newIteration(IterationEvent e)
{
}


/**
 * Called by the clustering method when a new expiriment is started. Used to update
 * the status bar and other visual elements.
 *
 * @param e the iteration event that launched this method call
 */
public void
newExperiment(IterationEvent e)
{
  Integer i = new Integer(e.getExpNum());
  IterationsProcessed_st.setText(i.toString());
}

/**
 * Used when the view button is pressed to display the output.  This button is
 * currently only enabled when the output type is dotty.
 */
void viewPB_actionPerformed(ActionEvent e)
{
  try
  {
    /**
     * Find the desired level graph, based on the users selection
     */
    int desiredLvl = currentViewC.getGraph().getGraphLevel();
    Graph tmpG = clusteringMethod_x.getBestGraph().cloneGraph();
    while ((tmpG != null) && (tmpG.getGraphLevel() > desiredLvl))
      tmpG = tmpG.getPreviousLevelGraph();

    /**
     * Write a temporary output file for visualization
     */
    ((DotGraphOutput)graphOutput_x).writeGraph("bunchtmp.dot",tmpG);

    /**
     * Execute dotty using the runtime support in the JVM
     */
    Runtime r = Runtime.getRuntime();
    r.exec("dotty bunchtmp.dot");
  }catch(Exception ex)
  {
    JOptionPane.showMessageDialog(this,
        "Error (check if dotty is in your path): " + ex.toString(),
        "Error Execing Graph Viewer",
        JOptionPane.ERROR_MESSAGE);
    ex.printStackTrace();
  }
}

/**
 * This method is called when it is time to update the stats on the
 * clustering progress window.
 */
public void updateStats()
{
  if(clusteringMethod_x.getBestCluster()==null) return;

  double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000;
  double mq = clusteringMethod_x.getBestCluster().getObjFnValue();
  long depth = clusteringMethod_x.getBestCluster().getDepth();
  long totalMQCalcs = stats.getMQCalculations();

  if ((++updateCounter % 10) == 0)
    clusteringMethod_x.getBestCluster().getClusterNames();

  int nc = clusteringMethod_x.getBestCluster().getNumClusters();

  currentTimeLabel_d.setText(Double.toString(elapsedTime) + " seconds");
  bestMQValueFound_d.setText(Double.toString(mq));
  DepthCount.setText(Long.toString(depth));
  numClusters.setText(Integer.toString(nc));
  MQEvalCount.setText(Long.toString(totalMQCalcs));

  if(isExhaustive == true)
  {
    long done = stats.getExhaustiveFinished();
    long total = stats.getExhaustiveTotal();
    int  pct = stats.getExhaustivePct();
    String msg = done + "/"+total+" - "+pct+"% Finished";
    progressMsg.setText(msg);
  }
}

/**
 * This class invokes the actionPerformed method when the timer pops.  The
 * result is the stats on the window will be updated
 */
class updateTimer implements ActionListener
{
  public void actionPerformed(ActionEvent e)
  {
    updateStats();
  }
}

/**
 * This class is used to indicate that a timeout event has happened.  When
 * encountered it interrupts and stops the clustering process.  It also tells
 * the clustering thread that it is finished so that it can dump its output.
 */
class timeoutTimer implements ActionListener
{
  public void actionPerformed(ActionEvent e)
  {
    if(worker_d != null)
    {
      worker_d.interrupt();
      worker_d.finished();
      CurrentActivity.setText("Finished due to Timeout!");
    }
  }
}


/**
 * This action listner handles the event when the drop down list for the
 * desired clustering level is changed.  It updates everything to properly
 * display the stats for the specified level
 */
void lvlViewerCB_actionPerformed(ActionEvent e)
{
  int lvl = lvlViewerCB.getSelectedIndex();

  Object [] clustO = bestCLL.toArray();
  Cluster lvlC = null;

  for(int i = 0; i < clustO.length; i++)
  {
    Cluster tmpC = (Cluster)clustO[i];
    if (tmpC.getGraph().getGraphLevel() == lvl)
    {
      lvlC = tmpC;
      break;
    }
  }

  if (lvlC == null) return;

  currentViewC = lvlC;
  bestMQValueFound_d.setText(Double.toString(lvlC.getObjFnValue()));
  DepthCount.setText(Long.toString(lvlC.getDepth()));
  numClusters.setText(Integer.toString(lvlC.getClusterNames().length));
  this.setTitle(basicTitle + " (Level "+lvl+")");
}
}

/**
 * This class is used to indicate if the clustering process is done.  It is
 * used as a signal to the other classes.
 */
class RunMonitor
{
  public boolean isDone = false;
}