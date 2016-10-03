/****
 *
 *	$Log: NAHCClusteringConfigurationDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:52  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.6  2000/10/22 17:47:03  bsmitc
 *	Collapsed NAHC and SAHC into a generic hill climbing method
 *
 *	Revision 3.5  2000/08/19 00:44:39  bsmitc
 *	Added support for configuring the amount of randomization performed when
 *	the user adjusts the "slider" feature of NAHC.
 *
 *	Revision 3.4  2000/08/15 02:52:18  bsmitc
 *	Implemented adjustable NAHC feature.  This feature allows the user to set
 *	a minimum search threshold so that NAHC will not just take the first thing
 *	that it finds.
 *
 *	Revision 3.3  2000/08/14 18:33:25  bsmitc
 *	Fixed bug where the SA configuration information was not being saved
 *	bewteen runs of Bunch
 *
 *	Revision 3.2  2000/08/13 18:40:06  bsmitc
 *	Added support for SA framework
 *
 *	Revision 3.1  2000/08/12 22:16:10  bsmitc
 *	Added support for Simulated Annealing configuration for NAHC technique
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
package bunch;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;
import javax.swing.event.*;

public class NAHCClusteringConfigurationDialog
extends ClusteringConfigurationDialog
//extends JDialog
{
  JPanel panel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  Border border1;
  TitledBorder titledBorder1;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField populationSzEF = new JTextField();
  JPanel jPanel2 = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  Border border2;
  TitledBorder titledBorder2;
  JCheckBox SAEnable = new JCheckBox();
  JLabel jLabel2 = new JLabel();
  JComboBox SATechniqueCB = new JComboBox();
  JButton ConfigurePB = new JButton();
  SATechniqueFactory safactory = new SATechniqueFactory();
  SATechnique saMethod = null;
  JLabel descriptionST = new JLabel();
  Frame parentFrame = null;
  NAHCConfiguration nahcConfig = null;
  JLabel jLabel3 = new JLabel();
  JLabel pctToConsiderST = new JLabel();
  JSlider sliderAdjust = new JSlider();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel fillerLBL = new JLabel();
  JLabel jLabel6 = new JLabel();
  JTextField randomizePctEF = new JTextField();

  public NAHCClusteringConfigurationDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    parentFrame = frame;

    //try {
    //  jbInit();
    //  pack();
    //}
    //catch(Exception ex) {
    //  ex.printStackTrace();
    //}
  }

  public NAHCClusteringConfigurationDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    nahcConfig = (NAHCConfiguration)configuration_d;

    border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(142, 142, 142),new Color(99, 99, 99));
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Clustering Options");
    border2 = BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.white,Color.white,new Color(142, 142, 142),new Color(99, 99, 99));
    titledBorder2 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(142, 142, 142)),"Simulated Annealing");
    panel1.setLayout(gridBagLayout1);
    jPanel1.setBorder(titledBorder1);
    jPanel1.setLayout(gridBagLayout2);
    jLabel1.setText("Population Size:");
    populationSzEF.setText("5");
    populationSzEF.setText(Integer.toString(configuration_d.getPopulationSize()));
    jPanel2.setLayout(gridBagLayout3);
    jPanel2.setBorder(titledBorder2);
    SAEnable.setText("Enable Simulated Annealing");
    SAEnable.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        SAEnable_actionPerformed(e);
      }
    });
    jLabel2.setText("Technique:");
    ConfigurePB.setEnabled(false);
    ConfigurePB.setText("Configure...");
    ConfigurePB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ConfigurePB_actionPerformed(e);
      }
    });

    SATechniqueCB.setEnabled(false);
    SATechniqueCB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        SATechniqueCB_actionPerformed(e);
      }
    });
    descriptionST.setForeground(Color.red);
    descriptionST.setText("Description:  ");
    jLabel3.setText("Minimum % of Search Space To Consider:");
    pctToConsiderST.setForeground(Color.blue);
    pctToConsiderST.setText("  0%");
    sliderAdjust.setValue(0);
    sliderAdjust.addChangeListener(new javax.swing.event.ChangeListener() {

      public void stateChanged(ChangeEvent e) {
        sliderAdjust_stateChanged(e);
      }
    });
    sliderAdjust.addInputMethodListener(new java.awt.event.InputMethodListener() {

      public void inputMethodTextChanged(InputMethodEvent e) {
      }

      public void caretPositionChanged(InputMethodEvent e) {
        sliderAdjust_caretPositionChanged(e);
      }
    });
    jLabel4.setText("NAHC");
    jLabel5.setText("SAHC");
    fillerLBL.setText("               ");
    jLabel6.setText("Randomize %:");
    randomizePctEF.setText("100");
    getContentPane().add(panel1);
    //optionsPanel_d.
    panel1.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 191, 0));
    jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    jPanel1.add(populationSzEF, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 40, 0));
    jPanel1.add(jLabel3, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
    jPanel1.add(sliderAdjust, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
    jPanel1.add(pctToConsiderST, new GridBagConstraints(3, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    jPanel1.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel5, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(fillerLBL, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jLabel6, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
    jPanel1.add(randomizePctEF, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 11, 0));
    //optionsPanel_d.
    panel1.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 0, 0), 0, 0));
    jPanel2.add(SAEnable, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
    jPanel2.add(SATechniqueCB, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 70, 0));
    jPanel2.add(ConfigurePB, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, -4));
    jPanel2.add(descriptionST, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));

    optionsPanel_d.add(panel1);

    Enumeration e = safactory.getAvailableItems();
    while(e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      SATechniqueCB.addItem(name);
    }

    String initTechnique = safactory.getDefaultTechnique();
    SATechniqueCB.setSelectedItem(initTechnique);
    saMethod = (SATechnique)safactory.getItemInstance(initTechnique);
    descriptionST.setText(saMethod.getObjectDescription());

    if (nahcConfig.getSATechnique()==null)
      SATechniqueCB.setEnabled(false);
    else
    {
      SAEnable.setSelected(true);
      SATechniqueCB.setEnabled(true);
      ConfigurePB.setEnabled(true);
      saMethod = nahcConfig.getSATechnique();
      descriptionST.setText(saMethod.getObjectDescription());
      SATechniqueCB.setSelectedItem(saMethod.getWellKnownName());
    }

    sliderAdjust.setValue(nahcConfig.getMinPctToConsider());
    int pct = sliderAdjust.getValue();
    Integer val = new Integer(nahcConfig.getRandomizePct());
    randomizePctEF.setText(val.toString());

    super.jbInit();
  }

/**
 * Create the configuration object for the NAHC hill climbing method.
 */
protected
Configuration
createConfiguration()
{
  configuration_d.setNumOfIterations(1);//(Integer.parseInt(jTextField1.getText()));
  configuration_d.setPopulationSize(Integer.parseInt(populationSzEF.getText()));
  ((HillClimbingConfiguration)configuration_d).setThreshold(1.0);//Double.valueOf(jTextField2.getText()).doubleValue());

  if(SAEnable.isSelected())
  {
    ((NAHCConfiguration)configuration_d).setSATechnique(saMethod);
    Hashtable h = saMethod.getConfig();
    saMethod.setConfig(h);
  }
  else
    ((NAHCConfiguration)configuration_d).setSATechnique(null);

  ((NAHCConfiguration)configuration_d).setMinPctToConsider(sliderAdjust.getValue());

  int pctVal = Integer.parseInt(randomizePctEF.getText());
  ((NAHCConfiguration)configuration_d).setRandomizePct(pctVal);
  return configuration_d;
}

  void SAEnable_actionPerformed(ActionEvent e) {
      boolean state = SAEnable.isSelected();
      if(state == true)
      {
        ConfigurePB.setEnabled(true);
        SATechniqueCB.setEnabled(true);
      }
      else
      {
        ConfigurePB.setEnabled(false);
        SATechniqueCB.setEnabled(false);
      }
  }

  void ConfigurePB_actionPerformed(ActionEvent e) {
    String technique = (String)SATechniqueCB.getSelectedItem();

    if((saMethod == null) || (!saMethod.getWellKnownName().equals(technique)))
    {
      saMethod = (SATechnique)safactory.getItemInstance(technique);
      descriptionST.setText(saMethod.getObjectDescription());
    }
    if(getParenetFrame() == null)
      return;
    saMethod.configureUsingDialog(this.getParenetFrame());
  }

  void SATechniqueCB_actionPerformed(ActionEvent e) {
    //option changed code

    //String technique = (String)SATechniqueCB.getSelectedItem();
    //saMethod = (SATechnique)safactory.getItemInstance(technique);
    //descriptionST.setText(saMethod.getObjectDescription());
  }

  void sliderAdjust_caretPositionChanged(InputMethodEvent e) {
      int pct = sliderAdjust.getValue();
      pctToConsiderST.setText(pct+"%");

  }

  void sliderAdjust_stateChanged(ChangeEvent e) {
      int pct = sliderAdjust.getValue();
      //String val = Integer.toString(pct);
      //switch(val.length())
      //{
      //  case 1: val = "  "+val;
      //          break;
      //  case 2: val = " "+val;
      //          break;
      //}

      pctToConsiderST.setText(pct+"%");
      Integer val = new Integer(100-pct);
      randomizePctEF.setText(val.toString());
  }
}