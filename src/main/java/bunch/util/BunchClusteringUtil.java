package bunch.util;

import bunch.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.io.*;
import java.beans.*;

import bunch.api.*;

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author Brian Mitchell
 * @version 1.0
 */

public class BunchClusteringUtil extends JDialog {
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton DonePB = new JButton();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel OrphanAdoption = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField mdgFileNameEF = new JTextField();
  JButton mdgSelectPB = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextField silFileNameEF = new JTextField();
  JButton silFileSelectPB = new JButton();

  FileDialog fd;
  JFileChooser fileChooser;
  JLabel jLabel3 = new JLabel();
  JTextField orphanEF = new JTextField();
  JPanel jPanel2 = new JPanel();
  JButton RunPB = new JButton();
  JButton DeterminePB = new JButton();
  JLabel jLabel4 = new JLabel();
  JTextField outputSILEF = new JTextField();
  JButton outputSILPB = new JButton();
  JLabel messageST = new JLabel();
  JLabel jLabel6 = new JLabel();
  JComboBox calculatorCB = new JComboBox();
  ObjectiveFunctionCalculatorFactory of;

  public BunchClusteringUtil(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public BunchClusteringUtil() {
    this(null, "", false);
  }
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    DonePB.setText("Close");
    DonePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DonePB_actionPerformed(e);
      }
    });
    OrphanAdoption.setLayout(gridBagLayout1);
    jLabel1.setText("MDG File Name:");
    mdgSelectPB.setText("Select...");
    mdgSelectPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mdgSelectPB_actionPerformed(e);
      }
    });
    jLabel2.setText("SIL File Name:");
    silFileSelectPB.setToolTipText("");
    silFileSelectPB.setText("Select...");
    silFileSelectPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        silFileSelectPB_actionPerformed(e);
      }
    });
    jLabel3.setText("Orphan Module:");
    RunPB.setText("Run...");
    RunPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        RunPB_actionPerformed(e);
      }
    });
    DeterminePB.setText("Detect...");
    DeterminePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        DeterminePB_actionPerformed(e);
      }
    });
    jLabel4.setText("Output SIL File:");
    outputSILPB.setText("Select...");
    outputSILPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        outputSILPB_actionPerformed(e);
      }
    });
    messageST.setFont(new java.awt.Font("Dialog", 1, 12));
    messageST.setForeground(Color.red);
    messageST.setText("Provide the required ifnromation and press Run...");
    jLabel6.setText("MQ Calculator:");
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(DonePB, null);
    panel1.add(jTabbedPane1, BorderLayout.CENTER);
    jTabbedPane1.add(OrphanAdoption, "Orphan Adoption");
    OrphanAdoption.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 5), 0, 0));
    OrphanAdoption.add(mdgFileNameEF, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 161, 0));
    OrphanAdoption.add(mdgSelectPB, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    OrphanAdoption.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    OrphanAdoption.add(silFileNameEF, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 44, 0));
    OrphanAdoption.add(silFileSelectPB, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    OrphanAdoption.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    OrphanAdoption.add(orphanEF, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    OrphanAdoption.add(jPanel2, new GridBagConstraints(0, 5, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(RunPB, null);
    OrphanAdoption.add(DeterminePB, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 10), 0, 0));
    OrphanAdoption.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
    OrphanAdoption.add(outputSILEF, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    OrphanAdoption.add(outputSILPB, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    OrphanAdoption.add(messageST, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    OrphanAdoption.add(jLabel6, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
    OrphanAdoption.add(calculatorCB, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));

    fileChooser = new JFileChooser();

    of = new ObjectiveFunctionCalculatorFactory();
    Enumeration e = of.getAvailableItems();
    while(e.hasMoreElements())
      calculatorCB.addItem((String)e.nextElement());

    calculatorCB.setSelectedItem(of.getCurrentCalculator());
  }

  void DonePB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void mdgSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          mdgFileNameEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void silFileSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          silFileNameEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void RunPB_actionPerformed(ActionEvent e) {

    String outputSil = this.outputSILEF.getText();
    String orphan = this.orphanEF.getText();
    String mdg = mdgFileNameEF.getText();

    int bestCID = -1;
    double bestMQ = 0;
    BunchCluster bestCluster = null;

    BunchGraph g = bunch.api.BunchGraphUtils.constructFromSil(mdgFileNameEF.getText(),
              silFileNameEF.getText());

    int numClusters = g.getClusters().size();
    ArrayList cl = new ArrayList(g.getClusters());
    BunchNode bn = g.findNode(orphan);

    for(int i = 0; i < numClusters; i++)
    {
      BunchCluster bc = (BunchCluster)cl.get(i);
      int cID = bc.getID();
      bn.resetCluster(cID);
      bc.addNode(bn);

      try{
      g.writeSILFile(outputSil);
      }catch (Exception ex)
      {
        messageST.setText("Exception while writing the output SIL file!");
      }

      double mqResult = bunch.util.MQCalculator.CalcMQ(mdg,outputSil,
                    (String)calculatorCB.getSelectedItem());

      if (mqResult > bestMQ)
      {
        bestMQ = mqResult;
        bestCID = bc.getID();
        bestCluster = bc;
      }

      bc.removeNode(bn);
      bn.resetCluster(BunchNode.NOT_A_MEMBER_OF_A_CLUSTER);
    }

    if(bestCluster != null)
    {
      int id = bestCluster.getID();
      bn.resetCluster(id);
      bestCluster.addNode(bn);

      try{
      g.writeSILFile(outputSil);
      }catch (Exception ex)
      {
        messageST.setText("Exception while writing the final output SIL file!");
      }

      double mqResult = bunch.util.MQCalculator.CalcMQ(mdg,outputSil,
                    (String)calculatorCB.getSelectedItem());
    }
  }

  void DeterminePB_actionPerformed(ActionEvent e) {
    BunchGraph g = bunch.api.BunchGraphUtils.constructFromSil(mdgFileNameEF.getText(),
              silFileNameEF.getText());

    ArrayList al = new ArrayList(g.getNodes());
    for(int i = 0; i < al.size(); i++)
    {
      BunchNode n = (BunchNode)al.get(i);
      if (n.getCluster() == BunchNode.NOT_A_MEMBER_OF_A_CLUSTER)
        this.orphanEF.setText(n.getName());
    }
  }

  void outputSILPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showSaveDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          outputSILEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }
}