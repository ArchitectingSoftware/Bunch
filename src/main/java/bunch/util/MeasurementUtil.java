/****
 *
 *	$Log: MeasurementUtil.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *	
 *	Revision 1.3  2000/08/07 21:49:31  bsmitc
 *	Added support for calculator function
 *
 *	Revision 1.1  2000/08/01 19:03:48  bsmitc
 *	Initial Version Added to Repository
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

public class MeasurementUtil extends JDialog {
  JPanel panel1 = new JPanel();
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  FileDialog fd;
  JFileChooser fileChooser;
  BunchFrame bunchFrame;
  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel MQCalcPanel = new JPanel();
  JPanel PRCalcTab = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  Border border3;
  TitledBorder titledBorder3;
  JPanel jPanel2 = new JPanel();
  Border border4;
  TitledBorder titledBorder4;
  JPanel jPanel3 = new JPanel();
  JButton MQcalculatePB = new JButton();
  JButton MQCancelPB = new JButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField mdgEF = new JTextField();
  JButton mdgSelectPB = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextField silEF = new JTextField();
  JButton silSelectPB = new JButton();
  JLabel jLabel3 = new JLabel();
  JComboBox calculatorCB = new JComboBox();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jLabel4 = new JLabel();
  JLabel nodeST = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel edgesST = new JLabel();
  JLabel jLabel8 = new JLabel();
  JLabel clusterST = new JLabel();
  ObjectiveFunctionCalculatorFactory of;
  JLabel mqLabel = new JLabel();
  JLabel mqST = new JLabel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel jPanel4 = new JPanel();
  Border border5;
  TitledBorder titledBorder5;
  JPanel PRTab = new JPanel();
  Border border6;
  TitledBorder titledBorder6;
  JPanel jPanel6 = new JPanel();
  JButton prCalcPB = new JButton();
  JButton prCancel = new JButton();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  JLabel jLabel5 = new JLabel();
  JTextField expertFileEF = new JTextField();
  JButton expertSelectPB = new JButton();
  JLabel jLabel7 = new JLabel();
  JTextField sampleFileEF = new JTextField();
  JButton samplePB = new JButton();
  GridLayout gridLayout2 = new GridLayout();
  JLabel jLabel9 = new JLabel();
  JLabel jLabel10 = new JLabel();
  JLabel precisionST = new JLabel();
  JLabel recallST = new JLabel();
  JPanel jPanel7 = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  JPanel ESMeclTab = new JPanel();
  Border border7;
  TitledBorder titledBorder7;
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  JLabel jLabel11 = new JLabel();
  JTextField GraphAEF = new JTextField();
  JButton GraphASPB = new JButton();
  JLabel jLabel12 = new JLabel();
  JTextField GraphBEF = new JTextField();
  JButton GraphBSelPB = new JButton();
  JLabel jLabel13 = new JLabel();
  JTextField MDGEF = new JTextField();
  JButton MDGSelPB = new JButton();
  JLabel jLabel14 = new JLabel();
  JComboBox MeasurementDD = new JComboBox();
  JPanel jPanel5 = new JPanel();
  Border border8;
  TitledBorder titledBorder8;
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel8 = new JPanel();
  JButton CalculatePB = new JButton();
  JButton EdgeSimCancelPB = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea resultsTA = new JTextArea();

  public MeasurementUtil(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      bunchFrame = (BunchFrame)frame;
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public MeasurementUtil() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    border1 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
    titledBorder1 = new TitledBorder(border1,"Input Parameters");
    border2 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
    titledBorder2 = new TitledBorder(border2,"Results");
    border3 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(142, 142, 142),new Color(99, 99, 99));
    titledBorder3 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Inputs");
    border4 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
    titledBorder4 = new TitledBorder(border4,"Outputs");
    border5 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142));
    titledBorder5 = new TitledBorder(border5,"Inputs");
    border6 = BorderFactory.createEmptyBorder();
    titledBorder6 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Outputs");
    border7 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    titledBorder7 = new TitledBorder(border7,"Inputs");
    border8 = BorderFactory.createEtchedBorder(Color.white,new Color(134, 134, 134));
    titledBorder8 = new TitledBorder(border8,"Outputs");
    panel1.setLayout(borderLayout1);
    MQCalcPanel.setLayout(gridBagLayout1);
    jPanel1.setBorder(titledBorder3);
    jPanel1.setLayout(gridBagLayout2);
    jPanel2.setBorder(titledBorder4);
    jPanel2.setLayout(gridLayout1);
    MQcalculatePB.setText("Calculate");
    MQcalculatePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        MQcalculatePB_actionPerformed(e);
      }
    });
    MQCancelPB.setText("Cancel");
    MQCancelPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        MQCancelPB_actionPerformed(e);
      }
    });
    jLabel1.setText("MDG File:");
    mdgSelectPB.setText("Select...");
    mdgSelectPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        mdgSelectPB_actionPerformed(e);
      }
    });
    jLabel2.setText("SIL File:");
    silSelectPB.setText("Select...");
    silSelectPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        silSelectPB_actionPerformed(e);
      }
    });
    jLabel3.setText("Calculator:");
    gridLayout1.setRows(4);
    gridLayout1.setColumns(2);
    gridLayout1.setHgap(1);
    jLabel4.setText("Graph Size (Nodes):");
    nodeST.setToolTipText("");
    nodeST.setText("0");
    jLabel6.setText("Graph Size (Edges):");
    edgesST.setText("0");
    jLabel8.setText("Number of Clusters:");
    clusterST.setText("0");
    mqLabel.setText("Objective Function Value(MQ):");
    mqST.setText("0.0");
    PRCalcTab.setLayout(gridBagLayout3);
    jPanel4.setBorder(titledBorder5);
    jPanel4.setLayout(gridBagLayout4);
    PRTab.setBorder(titledBorder6);
    PRTab.setLayout(gridLayout2);
    prCalcPB.setText("Calculate");
    prCalcPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prCalcPB_actionPerformed(e);
      }
    });
    prCancel.setText("Cancel");
    prCancel.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        prCancel_actionPerformed(e);
      }
    });
    jLabel5.setText("Expert Decomposition:");
    expertSelectPB.setText("Select...");
    expertSelectPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        expertSelectPB_actionPerformed(e);
      }
    });
    jLabel7.setText("Sample Decomposition:");
    samplePB.setText("Select...");
    samplePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        samplePB_actionPerformed(e);
      }
    });
    gridLayout2.setRows(2);
    gridLayout2.setColumns(2);
    jLabel9.setText("  Precision:");
    jLabel10.setText("  Recall:");
    precisionST.setText("0 %");
    recallST.setText("0 %");
    jPanel7.setLayout(gridBagLayout5);
    ESMeclTab.setBorder(titledBorder7);
    ESMeclTab.setLayout(gridBagLayout6);
    jLabel11.setText("Graph A (SIL FIle):");
    GraphASPB.setText("Select...");
    GraphASPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GraphASPB_actionPerformed(e);
      }
    });
    jLabel12.setText("Graph B (SIL File):");
    GraphBSelPB.setText("Select...");
    GraphBSelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GraphBSelPB_actionPerformed(e);
      }
    });
    jLabel13.setText("MDG File:");
    MDGSelPB.setText("Select...");
    MDGSelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MDGSelPB_actionPerformed(e);
      }
    });
    jLabel14.setText("Measurement:");
    jPanel5.setBorder(titledBorder8);
    jPanel5.setLayout(borderLayout2);
    CalculatePB.setText("Calculate...");
    CalculatePB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        CalculatePB_actionPerformed(e);
      }
    });
    EdgeSimCancelPB.setText("Cancel");
    EdgeSimCancelPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        EdgeSimCancelPB_actionPerformed(e);
      }
    });
    calculatorCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        calculatorCB_actionPerformed(e);
      }
    });
    getContentPane().add(panel1);
    panel1.add(jTabbedPane1, BorderLayout.CENTER);
    jTabbedPane1.add(MQCalcPanel, "MQ Calculator");
    jTabbedPane1.add(PRCalcTab, "Precision/Recall Calculator");
    PRCalcTab.add(jPanel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 10, 10));
    jPanel4.add(jLabel5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(expertFileEF, new GridBagConstraints(1, 0, 4, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 5, 0, 5), 194, 0));
    jPanel4.add(expertSelectPB, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -6));
    jPanel4.add(jLabel7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(sampleFileEF, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    jPanel4.add(samplePB, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -6));
    PRCalcTab.add(PRTab, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    PRTab.add(jLabel9, null);
    PRTab.add(precisionST, null);
    PRTab.add(jLabel10, null);
    PRTab.add(recallST, null);
    PRCalcTab.add(jPanel6, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel6.add(prCalcPB, null);
    jPanel6.add(prCancel, null);
    jTabbedPane1.add(jPanel7, "EdgeSim/MeCl");
    jPanel7.add(ESMeclTab, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 28, 0));
    ESMeclTab.add(jLabel11, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    ESMeclTab.add(GraphAEF, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 171, 0));
    ESMeclTab.add(GraphASPB, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -4));
    ESMeclTab.add(jLabel12, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    ESMeclTab.add(GraphBEF, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    ESMeclTab.add(GraphBSelPB, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -4));
    ESMeclTab.add(jLabel13, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    ESMeclTab.add(MDGEF, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    ESMeclTab.add(MDGSelPB, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -4));
    ESMeclTab.add(jLabel14, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    ESMeclTab.add(MeasurementDD, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    jPanel7.add(jPanel5, new GridBagConstraints(3, 1, 1, 5, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 52));
    jPanel5.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(resultsTA, null);
    jPanel7.add(jPanel8, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel8.add(CalculatePB, null);
    jPanel8.add(EdgeSimCancelPB, null);

    MQCalcPanel.add(jPanel1, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 10, 10));
    jPanel1.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 25, 0));
    jPanel1.add(mdgEF, new GridBagConstraints(1, 1, 5, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 220, 0));
    jPanel1.add(mdgSelectPB, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -6));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(silEF, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    jPanel1.add(silSelectPB, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -6));
    jPanel1.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(calculatorCB, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    MQCalcPanel.add(jPanel2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 0));
    jPanel2.add(jLabel4, null);
    jPanel2.add(nodeST, null);
    jPanel2.add(jLabel6, null);
    jPanel2.add(edgesST, null);
    jPanel2.add(jLabel8, null);
    jPanel2.add(clusterST, null);
    jPanel2.add(mqLabel, null);
    jPanel2.add(mqST, null);
    MQCalcPanel.add(jPanel3, new GridBagConstraints(3, 3, 1, 3, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 34));
    jPanel3.add(MQcalculatePB, null);
    jPanel3.add(MQCancelPB, null);

    MeasurementDD.addItem("EdgeSim");
    MeasurementDD.addItem("MeCl");

    mqLabel.setForeground(Color.red.darker());
    mqST.setForeground(Color.red.darker());
    //fd = new FileDialog(this.getRootPane().getContentPane().);
    fileChooser = new JFileChooser();

    of = new ObjectiveFunctionCalculatorFactory();
    Enumeration e = of.getAvailableItems();
    while(e.hasMoreElements())
      calculatorCB.addItem((String)e.nextElement());

    calculatorCB.setSelectedItem(of.getCurrentCalculator());
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void mdgSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          mdgEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void silSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          silEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void MQCancelPB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void MQcalculatePB_actionPerformed(ActionEvent e) {
      
      
    try
    {
      String mdg = mdgEF.getText();
      String sil = silEF.getText();
      
      //ensure the SIL file covers the MDG graph
      if(!BunchGraphUtils.isSilFileOK(mdg, sil))
      {
          String out = "The SIL File is Missing Nodes from MDG";
          out += "\r\nThe following modules need to be in the SIL File:\r\n";
          
          ArrayList mlist = BunchGraphUtils.getMissingSilNodes(mdg, sil);
          for(int i = 0; i < mlist.size(); i++)
              out += "\r\n"+(i+1)+". "+mlist.get(i);
          
          JOptionPane.showMessageDialog(null,
                out,
                "SIL File Error",
                JOptionPane.ERROR_MESSAGE);  
          return;
      }
      

      Parser p = new DependencyFileParser();
      p.setInput(mdg);
      p.setDelims(bunchFrame.getDelims());

      Graph g = (Graph)p.parse();
      ObjectiveFunctionCalculatorFactory ofc = new ObjectiveFunctionCalculatorFactory();
      ofc.setCurrentCalculator((String)calculatorCB.getSelectedItem());
      g.setObjectiveFunctionCalculatorFactory(ofc);

      g.setObjectiveFunctionCalculator((String)calculatorCB.getSelectedItem());

      ClusterFileParser cfp = new ClusterFileParser();
      cfp.setInput(sil);
      cfp.setObject(g);
      cfp.parse();
      g.calculateObjectiveFunctionValue();

      //figure out the total number of edges
      long edgeCnt = 0;
      Node[] n = g.getNodes();
      for(int i = 0; i < n.length; i++)
      {
        if (n[i].dependencies != null)
          edgeCnt += n[i].dependencies.length;
      }

      //set output values
      nodeST.setText(Integer.toString(g.getNodes().length));
      clusterST.setText(Integer.toString(g.getClusterNames().length));
      edgesST.setText(Long.toString(edgeCnt));
      mqST.setText(Double.toString(g.getObjectiveFunctionValue()));
      //System.out.println("Objective function value = " + g.getObjectiveFunctionValue());

    }
    catch(Exception calcExcept)
    {
      calcExcept.printStackTrace();
    }
  }

  void expertSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          expertFileEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void samplePB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          sampleFileEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void prCancel_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void prCalcPB_actionPerformed(ActionEvent e) {
      String expertFileName = expertFileEF.getText();
      String sampleFileName = sampleFileEF.getText();

      PrecisionRecallCalculator prcalc = new PrecisionRecallCalculator(expertFileName,sampleFileName);
      precisionST.setText(prcalc.get_precision());
      recallST.setText(prcalc.get_recall());
  }

  void GraphASPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          GraphAEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void GraphBSelPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          GraphBEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void MDGSelPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          MDGEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void CalculatePB_actionPerformed(ActionEvent e) {
      String ga = GraphAEF.getText();
      String gb = GraphBEF.getText();
      String mdg = MDGEF.getText();
      String tt = (String)MeasurementDD.getSelectedItem();
      
      //Ensure that SIL Files OK
      if(!BunchGraphUtils.isSilFileOK(mdg, ga))
      {
          String out = "Graph A SIL File Missing Nodes in MDG";
          out += "\r\nThe following modules need to be in the SIL File:\r\n";
          
          ArrayList mlist = BunchGraphUtils.getMissingSilNodes(mdg, ga);
          for(int i = 0; i < mlist.size(); i++)
              out += "\r\n"+(i+1)+". "+mlist.get(i);
          
          resultsTA.setText(out);
          return;
      }
      if(!BunchGraphUtils.isSilFileOK(mdg, gb))
      {
          String out = "Graph B SIL File Missing Nodes in MDG";
          out += "\r\nThe following modules need to be in the SIL File:\r\n";
          
          ArrayList  mlist = BunchGraphUtils.getMissingSilNodes(mdg, gb);
          for(int i = 0; i < mlist.size(); i++)
              out += "\r\n"+(i+1)+". "+mlist.get(i);
          
          resultsTA.setText(out);
          return;
      }
      //ALL IS GOOD, do the CALC

      BunchGraph bgA = BunchGraphUtils.constructFromSil(mdg,ga);
      BunchGraph bgB = BunchGraphUtils.constructFromSil(mdg,gb);

      String out = "";

      if(tt.equalsIgnoreCase("EdgeSim"))
      {
        double es = BunchGraphUtils.calcEdgeSim(bgA,bgB);
        es *= 10000;
        int ies = (int)es;
        double es2 = ies / 100.0;

        out += "EdgeSim(A,B) = " + es2 + "\r\n";
      }
      else
      {
        double m1 = BunchGraphUtils.getMeClDistance(bgA,bgB);
        double m2 = BunchGraphUtils.getMeClDistance(bgB,bgA);

        m1 = 100.0- m1;
        m2 = 100.0- m2;

        double mmin = Math.min(m1,m2);

        out += "MeCl(A,B) = " + m1 + "%\r\n";
        out += "MeCl(B,A) = " + m2 + "%\r\n";

        if (m1 <= m2)
          out += "MeCl = " + m1 + "% because MeCl(A,B) <= Mecl(B,A)\r\n";
        else
          out += "MeCl = " + m2 + "% because MeCl(B,A) < Mecl(A,B)\r\n";
      }

      //out += "******************************\r\n\r\n";

      resultsTA.setText(out);
  }

  void EdgeSimCancelPB_actionPerformed(ActionEvent e) {
      this.dispose();
  }

  void calculatorCB_actionPerformed(ActionEvent e) {

  }

/*****
  void utilityTypeCB_actionPerformed(ActionEvent e) {
      String val = (String)utilityTypeCB.getSelectedItem();
      if(val.equals("MQ Calculator"))
      {
        f1Label.setText("     MDG File:");
        f2Label.setText("     SIL File:");
      }else if (val.equals("Precision/Recall Evaluator"))
      {
        f1Label.setText("Cluster File:");
        f2Label.setText("Expert File:");
      }
  }

  void f1PB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          f1Name.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void f2PB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
      f2Name.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void f1Name_actionPerformed(ActionEvent e) {
      System.out.println("DEBUG1");
      if((f1Name.getText().length() > 0) & (f2Name.getText().length() > 0))
        MQcalculatePB.setEnabled(true);
      else
        MQcalculatePB.setEnabled(false);
  }

  void f2Name_actionPerformed(ActionEvent e) {
      System.out.println("DEBUG2");
      if((f1Name.getText().length() > 0) & (f2Name.getText().length() > 0))
        MQcalculatePB.setEnabled(true);
      else
        MQcalculatePB.setEnabled(false);
  }

  void f1Name_propertyChange(PropertyChangeEvent e) {
      System.out.println("f1 property changed");
  }

  void f1Name_inputMethodTextChanged(InputMethodEvent e) {
    System.out.println("DEBUG3");
  }

  void MQcalculatePB_actionPerformed(ActionEvent e) {
      String val = (String)utilityTypeCB.getSelectedItem();
      if(val.equals("MQ Calculator"))
      {
        handleMQCalc();
      }else if (val.equals("Precision/Recall Evaluator"))
      {
        handlePRCalc();
      }
  }

  void handleMQCalc()
  {
    String MDGFile = f1Name.getText();
    String SILFile = f2Name.getText();

    Parser p = new DependencyFileParser();
    p.setInput(MDGFile);

    Graph g = (Graph)p.parse();
    ObjectiveFunctionCalculatorFactory ofc = new ObjectiveFunctionCalculatorFactory();
    ofc.setCurrentCalculator(parentFrame.getC);
    //g.setObjectiveFunctionCalculator(parentFrame.

  }

  void handlePRCalc()
  {
    String ClusteredFile  = f1Name.getText();
    String ExpertFile     = f2Name.getText();
  }
**********/
}