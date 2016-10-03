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

public class BunchFileUtil extends JDialog {

  public static String convClassName = "bunch.gxl.converter.MDGtoGXL";
  public static String convClassJarName = "BunchGXL.jar";

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton DonePB = new JButton();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JPanel MDGtoGXL = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField mdgFileNameEF = new JTextField();
  JButton mdgSelectPB = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextField gxlFileNameEF = new JTextField();
  JButton gxlFileSelectPB = new JButton();

  FileDialog fd;
  JFileChooser fileChooser;
  JPanel jPanel2 = new JPanel();
  JButton ConvertPB = new JButton();
  JLabel gxlDTDPathLB = new JLabel();
  JTextField gxlDTDPathEF = new JTextField();
  JButton gxlDTDSelectPB = new JButton();
  JLabel messageST = new JLabel();
  ObjectiveFunctionCalculatorFactory of;
  JCheckBox embedDTDCB = new JCheckBox();

  String convJarName = this.convClassJarName;
  JCheckBox LoadFromClassPathCB = new JCheckBox();
  JLabel JarFilePathST = new JLabel();
  JTextField JarFilePathEF = new JTextField();
  JButton BunchGXLJarPB = new JButton();

  public BunchFileUtil(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public BunchFileUtil() {
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
    MDGtoGXL.setLayout(gridBagLayout1);
    jLabel1.setText("MDG File Name:");
    mdgSelectPB.setText("Select...");
    mdgSelectPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mdgSelectPB_actionPerformed(e);
      }
    });
    jLabel2.setText("GXL File Name:");
    gxlFileSelectPB.setToolTipText("");
    gxlFileSelectPB.setText("Select...");
    gxlFileSelectPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gxlFileSelectPB_actionPerformed(e);
      }
    });
    ConvertPB.setText("Convert...");
    ConvertPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ConvertPB_actionPerformed(e);
      }
    });
    gxlDTDPathLB.setEnabled(false);
    gxlDTDPathLB.setText("GXL DTD Path:");
    gxlDTDSelectPB.setEnabled(false);
    gxlDTDSelectPB.setText("Select...");
    gxlDTDSelectPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gxlDTDSelectPB_actionPerformed(e);
      }
    });
    messageST.setFont(new java.awt.Font("Dialog", 1, 12));
    messageST.setForeground(Color.red);
    messageST.setText("Provide the required ifnromation and press Convert...");
    embedDTDCB.setSelected(true);
    embedDTDCB.setText("Embed the DTD file in generated GXL file");
    embedDTDCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        embedDTDCB_actionPerformed(e);
      }
    });
    gxlDTDPathEF.setEnabled(false);
    gxlDTDPathEF.setText("GXL.dtd");
    mdgFileNameEF.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        mdgFileNameEF_focusLost(e);
      }
    });
    mdgFileNameEF.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        mdgFileNameEF_actionPerformed(e);
      }
    });
    LoadFromClassPathCB.setSelected(true);
    LoadFromClassPathCB.setText("Load Converter Class From CLASSPATH");
    LoadFromClassPathCB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        LoadFromClassPathCB_actionPerformed(e);
      }
    });
    JarFilePathST.setEnabled(false);
    JarFilePathST.setText("BunchGXL Jar File:");
    JarFilePathEF.setEnabled(false);
    JarFilePathEF.setText("BunchGXL.jar");
    BunchGXLJarPB.setEnabled(false);
    BunchGXLJarPB.setText("Select...");
    BunchGXLJarPB.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        BunchGXLJarPB_actionPerformed(e);
      }
    });
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(DonePB, null);
    panel1.add(jTabbedPane1, BorderLayout.CENTER);
    jTabbedPane1.add(MDGtoGXL,  "MDG To GXL Converter");
    MDGtoGXL.add(jLabel1,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 10, 0, 5), 0, 0));
    MDGtoGXL.add(mdgFileNameEF,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 0, 0, 0), 161, 0));
    MDGtoGXL.add(mdgSelectPB,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 0, 10), 0, 0));
    MDGtoGXL.add(jLabel2,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    MDGtoGXL.add(gxlFileNameEF,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 44, 0));
    MDGtoGXL.add(gxlFileSelectPB,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    MDGtoGXL.add(jPanel2,     new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(ConvertPB, null);
    MDGtoGXL.add(gxlDTDPathLB,       new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 20, 0, 0), 0, 0));
    MDGtoGXL.add(gxlDTDPathEF,     new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    MDGtoGXL.add(gxlDTDSelectPB,       new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 10), 0, 0));
    MDGtoGXL.add(messageST,    new GridBagConstraints(0, 8, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 10, 0), 0, 0));
    MDGtoGXL.add(embedDTDCB,     new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    MDGtoGXL.add(LoadFromClassPathCB,        new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    MDGtoGXL.add(JarFilePathST,   new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 5), 0, 0));
    MDGtoGXL.add(JarFilePathEF,  new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    MDGtoGXL.add(BunchGXLJarPB, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    fileChooser = new JFileChooser();

  }

  void DonePB_actionPerformed(ActionEvent e) {
    this.dispose();
  }

  void mdgSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          mdgFileNameEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void gxlFileSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showSaveDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          gxlFileNameEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void ConvertPB_actionPerformed(ActionEvent e) {
      String mdg = this.mdgFileNameEF.getText();
      String gxl = this.gxlFileNameEF.getText();
      boolean embedDTD = this.embedDTDCB.isSelected();
      String gxlPath = this.gxlDTDPathEF.getText();
      String jarPath = JarFilePathEF.getText();

      messageST.setText("Provide the required ifnromation and press Convert...");

      if(this.verifyFileName(mdg) == false)
      {
        this.messageST.setText("MDG File Name/Location is INVALID!");
        return;
      }

      if(this.verifyFilePath(gxl) == false)
      {
        this.messageST.setText("GXL Output File Name/Location is INVALID!");
        return;
      }

      if(LoadFromClassPathCB.isSelected() == false)
      {
        if(this.verifyFileName(jarPath) == false)
        {
          this.messageST.setText("BunchGXL Jar File Name/Location is INVALID!");
          return;
        }
      }

      Object oConv = getGXLHelperClass();
      if(oConv == null)
      {
          this.messageST.setText("BunchGXL Converter Class COULD NOT be loaded!");
          return;
      }

      try
      {
        bunch.gxl.proxy.IMDGtoGXL converter =
          (bunch.gxl.proxy.IMDGtoGXL)oConv;
        if(embedDTD == true)
          converter.setOptions(mdg,gxl,true);
        else
          converter.setOptions(mdg,gxl,gxlPath,false);

        converter.convert();
        this.messageST.setText("Converstion finished successfully!");
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
  }

  void gxlDTDSelectPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          gxlDTDPathEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void embedDTDCB_actionPerformed(ActionEvent e) {
      boolean state = embedDTDCB.isSelected();
      if(state == true)
      {
        gxlDTDPathLB.setEnabled(false);
        gxlDTDPathEF.setEnabled(false);
        gxlDTDSelectPB.setEnabled(false);
      }
      else
      {
        gxlDTDPathLB.setEnabled(true);
        gxlDTDPathEF.setEnabled(true);
        gxlDTDSelectPB.setEnabled(true);
      }
  }

  void mdgFileNameEF_focusLost(FocusEvent e) {
      String val = mdgFileNameEF.getText();
      gxlFileNameEF.setText(val+".gxl");
  }

  private boolean verifyFilePath(String fn)
  {
    try{
      java.io.File f = new java.io.File(fn);
      boolean state = f.createNewFile();

      //If we made it this far the FILE is OK
      if(state == true) f.delete();
      return true;
    }
    catch(Exception e){
      return false;
    }
  }
  private boolean verifyFileName(String fn)
  {
    try{
      java.io.File f = new java.io.File(fn);
      return f.exists();
    }
    catch(Exception e){
      return false;
    }
  }

  private Object getGXLHelperClass()
  {
    try
    {
      Class c = null;

      if(LoadFromClassPathCB.isSelected())
      {
        ClassLoader loader = this.getClass().getClassLoader();
        c = loader.loadClass (this.convClassName);
      }
      else
      {
        String jarName = JarFilePathEF.getText();
        java.net.URL urlList [] = {(new File(jarName)).toURL()};
        ClassLoader loader = new java.net.URLClassLoader(urlList);
        c = loader.loadClass (this.convClassName);
      }
      Object handlerObj = c.newInstance();
      return handlerObj;
    }catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
  void mdgFileNameEF_actionPerformed(ActionEvent e) {

  }

  void BunchGXLJarPB_actionPerformed(ActionEvent e) {
    int returnVal = fileChooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION)
          JarFilePathEF.setText(fileChooser.getSelectedFile().getAbsolutePath());
  }

  void LoadFromClassPathCB_actionPerformed(ActionEvent e) {
      if(LoadFromClassPathCB.isSelected())
      {
        JarFilePathST.setEnabled(false);
        JarFilePathEF.setEnabled(false);
        BunchGXLJarPB.setEnabled(false);
      }
      else
      {
        JarFilePathST.setEnabled(true);
        JarFilePathEF.setEnabled(true);
        BunchGXLJarPB.setEnabled(true);
      }
  }
}