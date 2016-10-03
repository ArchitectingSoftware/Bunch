/****
 *
 *	$Log: ServerClusteringProgress.java,v $
 *	Revision 3.0  2002/02/03 18:42:07  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:18  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */

//Title:        Bunch Version 1.2 Base
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Brian Mitchell
//Company:
//Description:  Your description

package bunch.BunchServer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ServerClusteringProgress extends JDialog {
  JPanel panel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JLabel NeighborsProcessedST = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel BestMQST = new JLabel();
  JButton toggleServerPB = new JButton();
  int     workProcessed = 0;
  double  bestMQ=0.0;
  Thread  workerThread = null;
  JLabel jLabel3 = new JLabel();
  JLabel uowSz = new JLabel();
  JLabel adaptiveEnableLabel = new JLabel();
  JLabel adaptiveEnableMsg = new JLabel();
  JLabel jLabel4 = new JLabel();
  int     currentRefresh = 1;
  long    totalUpdates = 0;
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JSlider UpdateSpeed = new JSlider();
  JLabel jLabel7 = new JLabel();
  JLabel jLabel8 = new JLabel();

  public ServerClusteringProgress(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try  {
      workProcessed = 0;
      bestMQ = 0.0;
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setWorkerThread(Thread t)
  { workerThread = t; }

  public ServerClusteringProgress() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    panel1.setLayout(gridBagLayout1);
    jLabel1.setText("Neighbors Processed");
    NeighborsProcessedST.setText("0");
    jLabel2.setText("Best MQ So Far:");
    BestMQST.setText("0.0");
    toggleServerPB.setText("Pause Server");
    toggleServerPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        toggleServerPB_actionPerformed(e);
      }
    });
    jLabel3.setText("Current UOW Size:");
    uowSz.setText("0");
    adaptiveEnableLabel.setText("Adaptive Algorithm:");
    adaptiveEnableMsg.setText("Enabled");
    jLabel4.setText("Update Frequency:");
    jLabel5.setText("     ");
    jLabel6.setText("      ");
    UpdateSpeed.setPaintLabels(true);
    UpdateSpeed.setPaintTicks(true);
    UpdateSpeed.setMaximum(10);
    UpdateSpeed.setValueIsAdjusting(true);
    UpdateSpeed.setOpaque(false);
    UpdateSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        UpdateSpeed_stateChanged(e);
      }
    });
    jLabel7.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel7.setForeground(Color.blue);
    jLabel7.setText("Fast");
    jLabel8.setFont(new java.awt.Font("Dialog", 1, 12));
    jLabel8.setForeground(Color.blue);
    jLabel8.setText("Slow");
    getContentPane().add(panel1);
    panel1.add(jLabel1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.VERTICAL, new Insets(0, 10, 0, 21), 0, 0));
    panel1.add(NeighborsProcessedST, new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 142, 0));
    panel1.add(jLabel2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
    panel1.add(BestMQST, new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(toggleServerPB, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 0, -6));
    panel1.add(jLabel3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 9, 0, 0), 0, 0));
    panel1.add(uowSz, new GridBagConstraints(3, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(adaptiveEnableLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 9, 0, 0), 0, 0));
    panel1.add(adaptiveEnableMsg, new GridBagConstraints(3, 3, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(jLabel4, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 9, 0, 0), 0, 0));
    panel1.add(jLabel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(jLabel6, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(UpdateSpeed, new GridBagConstraints(3, 4, 3, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -9));
    panel1.add(jLabel7, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(jLabel8, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    currentRefresh = 10;
  }

  public void updateWorkProcessed(int amt, double mq, int uowSize, boolean adaptiveEnabled)
  {
      workProcessed += amt;
      if (mq > bestMQ)
         bestMQ = mq;

      totalUpdates++;

      if((totalUpdates % (currentRefresh*currentRefresh)) != 0)
      {
        // System.out.println("TotalUpdates == " + totalUpdates);
        return;
      }

      int iMQ = (int)(bestMQ * 10000);

      Integer iwp = new Integer(workProcessed);
      Double  mqDbl  = new Double((double)iMQ/10000);
      Integer iUOW = new Integer(uowSize);

      String  adaptEnab = "Enabled";
      if(adaptiveEnabled == false)
        adaptEnab = "Disabled";

      NeighborsProcessedST.setText(iwp.toString());
      BestMQST.setText(mqDbl.toString());
      uowSz.setText(iUOW.toString());
      adaptiveEnableMsg.setText(adaptEnab);
  }

  void toggleServerPB_actionPerformed(ActionEvent e) {
    if (toggleServerPB.getText().equals("Pause Server"))
    {
      toggleServerPB.setText("Resume Server");
      synchronized(workerThread){
        try{
          workerThread.wait();
        }catch(Exception ex) {}
      }
    }
    else
    {
      toggleServerPB.setText("Pause Server");
      synchronized(workerThread){
        try{
          workerThread.notify();
        }catch(Exception ex) {};
      }
    }
  }

  void UpdateSpeed_stateChanged(ChangeEvent e) {
      int val = UpdateSpeed.getValue();
      currentRefresh = (val+1);
  }
}
