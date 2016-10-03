/****
 *
 *	$Log: DistribClusteringProgressDlg.java,v $
 *	Revision 3.0  2002/02/03 18:41:47  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2001/04/03 21:39:29  bsmitc
 *	Readded and fixed support for distributed clustering
 *
 *	Revision 3.0  2000/07/26 22:46:09  bsmitc
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
import java.awt.event.*;

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
 *
 * @see bunch.ClusteringMethod
 * @see bunch.BunchFrame.runActionButton_d_actionPerformed(java.awt.event.ActionEvent)
 * @see bunch.BunchFrame
 */
public
class DistribClusteringProgressDlg
  extends JDialog
  implements IterationListener{

JPanel panel1 = new JPanel();
GridBagLayout gridBagLayout1 = new GridBagLayout();
JLabel percentLabel_d = new JLabel();
JLabel overallPercentLabel_d = new JLabel();
JProgressBar progressBar_d = new JProgressBar();
JProgressBar overallProgressBar_d = new JProgressBar();
JLabel bestMQLabel_d = new JLabel();
JLabel bestMQValueFound_d = new JLabel();
JLabel timeTitleLabel_d = new JLabel();
JLabel currentTimeLabel_d = new JLabel();
JButton pauseButton_d = new JButton();
JButton outputButton_d = new JButton();
JButton cancelButton_d = new JButton();
BunchFrame frame_d = null;
GraphOutput graphOutput_x = null;
ClusteringMethod2 clusteringMethod_x = null;
bunch.SwingWorker worker_d;
int iteration_d=0, overallIteration_d;
boolean finished_d=false;
boolean showOverallProgressBar_d= true;
JLabel jLabel1 = new JLabel();
JLabel IterationsProcessed_st = new JLabel();

/**
 * Dialog constructor
 */
public
DistribClusteringProgressDlg(Frame frame, String title, boolean modal, ClusteringMethod2 cm2)
{
  super(frame, title, modal);
  frame_d = (BunchFrame)frame;

  /**
   * Get required initialization objects from the parent frame
   */
  graphOutput_x = frame_d.getGraphOutput();
  clusteringMethod_x = cm2;
  clusteringMethod_x.setIterationListener(this);

  String methodName = clusteringMethod_x.getClass().getName();

  if (methodName.equals("bunch.GAClusteringMethod"))
    showOverallProgressBar_d = false;

  /**
   * Setup the UI
   */
  try  {
    jbInit();
    pack();
  }
  catch (Exception ex) {
    ex.printStackTrace();
  }

  progressBar_d.setMaximum(clusteringMethod_x.getMaxIterations());
  progressBar_d.setMinimum(0);

  /**
   * Make and get the inbound and outbound queues for dealing with the server
   */
  ((GenericDistribHillClimbingClusteringMethod)clusteringMethod_x).makeEventQueue();
  SynchronizedEventQueue eventQ =
    ((GenericDistribHillClimbingClusteringMethod)clusteringMethod_x).getEventQueue();

  CallbackImpl svrCB = frame_d.getSvrCallback();
  svrCB.eventQ = eventQ;

  BunchEvent   be    = new BunchEvent();
  svrCB.bevent = be;
  ((GenericDistribHillClimbingClusteringMethod)clusteringMethod_x).setEventObject(be);

  /**
   * Get adaptive work status
   */
  int uowSz = frame_d.getUOWSz();
  boolean useAdaptive = frame_d.getAdaptiveEnableFlag();

  ((GenericDistribHillClimbingClusteringMethod)clusteringMethod_x).setUOWSize(uowSz);
  ((GenericDistribHillClimbingClusteringMethod)clusteringMethod_x).setAdaptiveEnable(useAdaptive);

  /*
   * Constructing the SwingWorker() causes a new Thread
   * to be created that will call construct(), and then
   * finished().  Note that finished() is called even if
   * the worker is interrupted because we catch the
   * InterruptedException in doWork().
   * (Note: a SwingWorker object is necessary because Swing (a.k.a JFC)
   * components are not thread-aware.
   */
  worker_d = new bunch.SwingWorker()
  {
    public Object construct()
    {
      /**
       * Run the distributed clustering method
       */
      clusteringMethod_x.run();
      return "Done";
    }

    /**
     * Callback when clustering activity is finished.
     */
    public void finished()
    {
      Runnable doSetProgressBarValue = new Runnable()
      {
        public void run()
        {
          progressBar_d.setValue(progressBar_d.getMaximum());
          if (showOverallProgressBar_d)
            overallProgressBar_d.setValue(overallProgressBar_d.getMaximum());
          bestMQLabel_d.setForeground(Color.red.darker());
          bestMQValueFound_d.setForeground(Color.red.darker());
          outputButton_d.setEnabled(false);
          pauseButton_d.setEnabled(false);
          cancelButton_d.setText("Close");
          bestMQLabel_d.setText(bestMQLabel_d.getText());
          bestMQValueFound_d.setText(Double.toString(clusteringMethod_x.getBestObjectiveFunctionValue()));
          currentTimeLabel_d.setText(Double.toString(clusteringMethod_x.getElapsedTime()) + "  seconds");
        }
      };
      SwingUtilities.invokeLater(doSetProgressBarValue);
      outputGraph(true);
      setFinished(true);
    }
  };

  /**
   * Set the worker thread to low priority so that the UI remains responsive
   */
  worker_d.setPriority(Thread.MIN_PRIORITY);
  worker_d.start();
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
outputGraph(boolean end)
{
  //consolidate the drifters
  boolean state = frame_d.consolidateDriftersCB.isSelected();
  if (state == true)
      consolidateDrifters();

  if (end) {
    graphOutput_x.setCurrentName(graphOutput_x.getBaseName());
  }
  else {
    graphOutput_x.setCurrentName(graphOutput_x.getBaseName() + "-" + overallIteration_d);
  }

  Cluster c = clusteringMethod_x.getBestCluster();
  Graph g = c.getGraph();
  graphOutput_x.setGraph(g);
  frame_d.setLastResultGraph(g.cloneGraph());
  graphOutput_x.write();
}

public
void
consolidateDrifters()
{
  Drifters d = new Drifters(clusteringMethod_x.getBestGraph());
  d.consolidate();
}


/**
 * Component initialization
 */
void
jbInit()
  throws Exception
{
  panel1.setLayout(gridBagLayout1);

  bestMQLabel_d.setText("Best MQ value found:");
  timeTitleLabel_d.setText("Elapsed Time:");
  bestMQValueFound_d.setText("                                             ");
  currentTimeLabel_d.setText("0.0  seconds                      ");
  pauseButton_d.setText("  Pause  ");

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

  outputButton_d.setText("Output");
  outputButton_d.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      outputButton_d_actionPerformed(e);
    }
  });

  getContentPane().add(panel1);
  outputButton_d.setEnabled(false);
  if (showOverallProgressBar_d)
  {
    //NOP
  }
  else
    panel1.add(overallPercentLabel_d, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0,
      GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

  panel1.add(progressBar_d, new GridBagConstraints2(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 165, 0));

  panel1.add(timeTitleLabel_d, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));
  panel1.add(currentTimeLabel_d, new GridBagConstraints2(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 2, 5), 0, 0));

  panel1.add(bestMQLabel_d, new GridBagConstraints2(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 5, 5), 0, 0));
  panel1.add(bestMQValueFound_d, new GridBagConstraints2(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 5, 5, 5), 0, 0));

  panel1.add(outputButton_d, new GridBagConstraints2(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  panel1.add(pauseButton_d, new GridBagConstraints2(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  panel1.add(cancelButton_d, new GridBagConstraints2(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  panel1.add(jLabel1, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  panel1.add(IterationsProcessed_st, new GridBagConstraints2(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 50, 0));
}

/**
 * Called when the "Output" button is pressed
 *
 * @param e the action event that generated this method call
 */
void
outputButton_d_actionPerformed(ActionEvent e)
{
  outputGraph(false);
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
  if (pauseButton_d.getText().equals("  Pause  ")) {
    worker_d.suspend();
    pauseButton_d.setText("Resume");
    outputButton_d.setEnabled(true);
  }
  else {
    outputButton_d.setEnabled(false);
    pauseButton_d.setText("  Pause  ");
    worker_d.resume();
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
  if (isFinished()) { //in this case the cancel button will say "Close"
    setVisible(false);
    dispose();
    return;
  }

  worker_d.suspend();
  int result = JOptionPane.showConfirmDialog(frame_d,
              "This will cancel the clustering process.\n Are you sure?", "Cancel Clustering?",
              JOptionPane.YES_NO_OPTION);
  if (result == JOptionPane.NO_OPTION) {
    worker_d.resume();
    return;
  }
  else {
    worker_d.finished();
  }
}

/**
 * Called by the clustering method when a new iteration is started. Used to update
 * the status bar and other visual elements.
 *
 * @param e the iteration event that launched this method call
 */
public
void
newIteration(IterationEvent e)
{
  iteration_d = e.getIteration();

  overallIteration_d = e.getOverallIteration();
  Runnable doSetProgressBarValue = new Runnable()
  {
    public void run()
    {
      progressBar_d.setValue(iteration_d);
      if (showOverallProgressBar_d)
        overallProgressBar_d.setValue(overallIteration_d);

      bestMQValueFound_d.setText(Double.toString(clusteringMethod_x.getBestObjectiveFunctionValue()));
      currentTimeLabel_d.setText(Double.toString(clusteringMethod_x.getElapsedTime()) + "  seconds");
    }
  };
  SwingUtilities.invokeLater(doSetProgressBarValue);

  if (iteration_d >= clusteringMethod_x.getMaxIterations()) {
    worker_d.finished();
  }
}

public void
newExperiment(IterationEvent e)
{
  Integer i = new Integer(e.getExpNum());
  IterationsProcessed_st.setText(i.toString());
}
}

