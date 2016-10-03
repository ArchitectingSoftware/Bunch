/****
 *
 *	$Log: BunchFrame.java,v $
 *	Revision 3.0  2002/02/03 18:41:44  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *
 *	Revision 3.18  2001/04/03 23:32:11  bsmitc
 *	Added NAHC (really HC) support for Distributed Bunch, updated release
 *	version number to 3.2
 *
 *	Revision 3.17  2001/04/03 21:39:28  bsmitc
 *	Readded and fixed support for distributed clustering
 *
 *	Revision 3.16  2001/04/02 19:23:39  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.15  2000/11/26 15:48:12  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.14  2000/10/22 17:47:02  bsmitc
 *	Collapsed NAHC and SAHC into a generic hill climbing method
 *
 *	Revision 3.13  2000/10/22 17:30:06  bsmitc
 *	Fixed bug with user-directed clustering. Also, added support to clear
 *	the user-directed clustering option once it is selected
 *
 *	Revision 3.12  2000/10/22 15:48:48  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.11  2000/08/18 21:07:59  bsmitc
 *	Added feature to support tree output for dotty and text
 *
 *	Revision 3.10  2000/08/17 00:26:04  bsmitc
 *	Fixed omnipresent and library support for nodes in the MDG not connected to
 *	anything but the omnipresent nodes and libraries.
 *
 *	Revision 3.9  2000/08/16 00:12:45  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.8  2000/08/13 18:40:05  bsmitc
 *	Added support for SA framework
 *
 *	Revision 3.7  2000/08/11 13:19:10  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.6  2000/08/09 14:17:47  bsmitc
 *	Changes made to support agglomerative clustering feature.
 *
 *	Revision 3.5  2000/08/07 21:48:59  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.4  2000/08/02 21:40:53  bsmitc
 *	Added support for calculator feature
 *
 *	Revision 3.3  2000/08/01 19:04:37  bsmitc
 *	Updated menu item to have Bunch Utilities and calculators
 *
 *	Revision 3.2  2000/07/28 14:26:19  bsmitc
 *	Added support for the TXTTree Graph output option
 *
 *	Revision 3.1  2000/07/27 10:54:15  bsmitc
 *	Name of clustered input file is no longer removed after clustering.  Tab is now
 *	included as a default delimeter in the input processing of MDG files.
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

import bunch.BunchServer.BunchSvrMsg;
import bunch.stats.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import javax.swing.*;
import java.io.*;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.RMISecurityManager;
import java.util.*;

import javax.swing.event.*;

/**
 * Main GUI class for Bunch. This class is launched from the main() method in
 * Bunch.java.
 *
 * @author Brian Mitchell
 * @see bunch.Bunch.main(String[])
 */
public
class BunchFrame
  extends JFrame
{
/**
 * Constants for BunchFrame
 */
public static final int DEFAULT_UNIT_OF_WORK_SZ = 5;


/**
 * Objects for all of the controls and menus on the GUI
 */
JMenuBar bunchMenubar_d = new JMenuBar();
JMenu fileMenu_d = new JMenu();
JMenuItem menuFileExit = new JMenuItem();
JMenu helpMenu_d = new JMenu();
JMenuItem menuHelpAbout = new JMenuItem();
GridBagLayout gridBagLayout1 = new GridBagLayout();
JPanel bunchSettingsPanel_d = new JPanel();
GridBagLayout gridBagLayout2 = new GridBagLayout();
JButton selectOutputFileButton_d = new JButton();
JTextField outputClusterFilename_d = new JTextField();
JButton selectGraphFileButton_d = new JButton();
JTextField inputGraphFilename_d = new JTextField();
JLabel outputLabel_d = new JLabel();
JLabel outputFileLabel_d = new JLabel();
JLabel inputGraphLabel_d = new JLabel();
JLabel clusteringLabel_ = new JLabel();
JComboBox clusteringMethodList_d = new JComboBox();
JComboBox outputFileFormatList_d = new JComboBox();
JButton clusteringOptionsButton_d = new JButton();
JButton runActionButton_d = new JButton();
FileDialog fileSelector_d;
ClusteringMethod clusteringMethod_d;
GraphOutput graphOutput_d;
Graph initialGraph_d;
BunchPreferences preferences_d;
JMenu configureMenu_d = new JMenu();
JMenuItem configureOptionsMenuItem_d = new JMenuItem();
JTabbedPane mainTabbedPane_d = new JTabbedPane();
JLabel commandLabel_d = new JLabel();
JComboBox actionList_d = new JComboBox();
JLabel optionsLabel_d = new JLabel();
Configuration configuration_d;
String fileBasicName_d=null;
JButton outputLastButton_d = new JButton();
JButton nextLevelGraphButton_d = new JButton();
Graph lastResultGraph_d=null;
JPanel omnipresentPane_d = new JPanel();

GridBagLayout gridBagLayout3 = new GridBagLayout();
JLabel nodesLabel_d = new JLabel();
JLabel suppliersLabel_d = new JLabel();
JList standardNodeList_d = new JList();
DefaultListModel standardNodeListModel_d = new DefaultListModel();
JList suppliersList_d = new JList();
DefaultListModel suppliersListModel_d = new DefaultListModel();
JList clientsList_d = new JList();
DefaultListModel clientsListModel_d = new DefaultListModel();
JList centralList_d = new JList();
DefaultListModel centralListModel_d = new DefaultListModel();
JScrollPane standardNodeListPane_d;
JScrollPane clientsListPane_d;
JScrollPane suppliersListPane_d;
JLabel clientsLabels_d = new JLabel();
JButton sendToSuppliersButton_d = new JButton();
JButton receiveFromClientsButton_d = new JButton();
JButton receiveFromSuppliersButton_d = new JButton();
JButton sendToClientsButton_d = new JButton();
JPanel supplierButtonPanel_d = new JPanel();
GridBagLayout gridBagLayout4 = new GridBagLayout();
JPanel clientButtonPanel_d = new JPanel();
GridBagLayout gridBagLayout5 = new GridBagLayout();
JPanel omniInternalPane_d = new JPanel();
JButton findOmnipresentNodesButton_d = new JButton();
JTextField findOmnipresentThreshold_d = new JTextField();
JLabel findOmniLabel1_d = new JLabel();
JLabel findOmnilabel2_d = new JLabel();

GridBagLayout gridBagLayout6 = new GridBagLayout();
GridBagLayout gridBagLayout8 = new GridBagLayout();
JLabel suppliersLabel2_d = new JLabel();
JButton findLibraryNodesButton_d = new JButton();
JList standardNodeListLib_d = new JList();
JList librariesList_d = new JList();
JLabel nodesLabel2_d = new JLabel();
JButton sendLibToClientsButton_d = new JButton();
JScrollPane librariesListPane_d;
JScrollPane standardNodeListPaneLib_d;
JScrollPane centralListPane_d = new JScrollPane();
JLabel findOmniLabel1_d1 = new JLabel();
JPanel clientButtonPanel_d1 = new JPanel();
JPanel omniInternalPane_d1 = new JPanel();
JPanel librariesPane_d = new JPanel();
DefaultListModel librariesListModel_d = new DefaultListModel();
JButton receiveLibFromClientsButton_d = new JButton();
JPanel userDirectedClusteringPane_d = new JPanel();
GridBagLayout gridBagLayout7 = new GridBagLayout();
JTextField inputClusterFile_d = new JTextField();
JButton inputClusterFileSelectButton_d = new JButton();
JLabel inputClusterLabel_d = new JLabel();
JCheckBox lockClustersCheckbox_d = new JCheckBox();
JPanel centralButtonPanel_d = new JPanel();
GridBagLayout gridBagLayout9 = new GridBagLayout();
JButton receiveFromCentralButton_d = new JButton();
JButton sendToCentralButton_d = new JButton();
JLabel centralLabel_d = new JLabel();
  JPanel clusteringOptions = new JPanel();
  GridBagLayout gridBagLayout10 = new GridBagLayout();
  JCheckBox consolidateDriftersCB = new JCheckBox();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JComboBox ClusteringAlgEF = new JComboBox();
  JTextField delimEF = new JTextField();
  JLabel jLabel3 = new JLabel();
  JCheckBox spaceDelimCB = new JCheckBox();
  JCheckBox tabDelimCB = new JCheckBox();
  JButton visualizeButton_d = new JButton();
  JPanel distPane = new JPanel();
  GridBagLayout gridBagLayout11 = new GridBagLayout();
  JCheckBox distClustEnableCB = new JCheckBox();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JTextField nameServerEF = new JTextField();
  JLabel jLabel6 = new JLabel();
  JTextField portEF = new JTextField();
  JButton queryNS = new JButton();
  JLabel jLabel7 = new JLabel();
  JButton includeDistSvrsPB = new JButton();
  JTextField nameSpaceEF = new JTextField();
  JScrollPane jScrollPane1 = new JScrollPane();
  JList serverList = new JList();

  Vector serverVector;
  Vector activeServerVector;
  CallbackImpl svrCallback;
  BunchEvent bevent = null;
  JLabel jLabel8 = new JLabel();
  JTextField UOWSzEF = new JTextField();
  JButton deactivatePB = new JButton();
  JCheckBox adaptiveEnableCB = new JCheckBox();

  StatsManager stats = StatsManager.getInstance();
  JCheckBox timeoutEnable = new JCheckBox();
  JTextField maxRuntimeEF = new JTextField();
  JLabel jLabel9 = new JLabel();
  JMenu utilityMenu_d = new JMenu();
  JMenuItem utilityMeasurementCalc = new JMenuItem();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JComboBox agglomOutputCB = new JComboBox();
  JCheckBox outputTreeCB = new JCheckBox();
  JButton ClearClusterFile = new JButton();
  JMenuItem menuShowDistributedTab = new JMenuItem();
  JMenuItem clusteringUtilsMenu = new JMenuItem();
  JMenuItem fileUtilsMenu = new JMenuItem();

/**
 * The BunchFrame class constructor. Basically calls the jbInit() method.
 *
 * @see #jbInit()
 */
public
BunchFrame()
{
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
  try {
      jbInit();
  }
  catch (Exception e) {
    e.printStackTrace();
  }
}

/**
 * Component initialization.  Draws the controls on the window and initializes
 * The appropriate bunch objects.
 */
private
void
jbInit()
  throws Exception
{
  //load preferences, either from the class or from a serialized file (bunch.BunchPreferences.ser)
  //this will allow to store the configuration in a future version
  preferences_d = (BunchPreferences)(Beans.instantiate(null, "bunch.BunchPreferences"));

//obtain the available clustering methods and add them to the method list
  String[] methodList = preferences_d.getClusteringMethodFactory().getItemList();
  for (int i=0; i<methodList.length; ++i) {
    clusteringMethodList_d.addItem(methodList[i]);
  }

  //Setup the default clustering method
  String defaultCM = preferences_d.getClusteringMethodFactory().getDefaultMethod();
  setClusteringMethod(defaultCM);
  clusteringMethodList_d.setSelectedItem(defaultCM);

  //setup the objective function calculator
  String[] mqFnList = preferences_d.getObjectiveFunctionCalculatorFactory().getItemList();
  for (int i=0; i<mqFnList.length; ++i) {
    ClusteringAlgEF.addItem(mqFnList[i]);
  }

  String defaultMqFn = preferences_d.getObjectiveFunctionCalculatorFactory().getDefaultMethod();
  ClusteringAlgEF.setSelectedItem(defaultMqFn);




  String defOutputType = preferences_d.getGraphOutputFactory().defaultOption;

  outputFileFormatList_d.setSelectedItem(defOutputType);
    outputFileFormatList_d.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        outputFileFormatList_d_actionPerformed(e);
      }
    });

  standardNodeListPane_d = new JScrollPane(standardNodeList_d);
  clientsListPane_d = new JScrollPane(clientsList_d);
  suppliersListPane_d = new JScrollPane(suppliersList_d);
  centralListPane_d = new JScrollPane(centralList_d);
  librariesListPane_d = new JScrollPane(librariesList_d);
  standardNodeListPaneLib_d = new JScrollPane(standardNodeListLib_d);

  //sets the generic OFCalculatorFactory that will be used by Graph classes instances
  //
  Graph.setObjectiveFunctionCalculatorFactory(preferences_d.getObjectiveFunctionCalculatorFactory());


  //do layout of the components
  this.getContentPane().setLayout(gridBagLayout1);
  this.setSize(new Dimension(622, 476));
  this.setTitle("Bunch v4.0.1 - April 2016");
  selectOutputFileButton_d.setText("Select...");
  selectOutputFileButton_d.setActionCommand("Select Output File");
  selectOutputFileButton_d.addActionListener(new BunchFrame_selectOutputFileButton_d_actionAdapter(this));
  selectGraphFileButton_d.setText("Select...");

  selectGraphFileButton_d.setActionCommand("Select Input Graph File");
  selectGraphFileButton_d.addActionListener(new BunchFrame_selectGraphFileButton_d_actionAdapter(this));
  outputLabel_d.setText("Output Cluster File:");
  outputFileLabel_d.setText("Output File Format:");
  inputGraphLabel_d.setText("Input Graph File:");
  clusteringLabel_.setText("Clustering Method:");
  clusteringOptionsButton_d.setText("Options");
  clusteringOptionsButton_d.setActionCommand("Select Clustering Options");
  runActionButton_d.setText("Run");
  fileSelector_d = new FileDialog(this);
    clusteringMethodList_d.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clusteringMethodList_d_actionPerformed(e);
      }
    });
  clusteringMethodList_d.addItemListener(new BunchFrame_clusteringMethodList_d_itemAdapter(this));
  clusteringOptionsButton_d.addActionListener(new BunchFrame_clusteringOptionsButton_d_actionAdapter(this));
  runActionButton_d.setActionCommand("Run Action");
  runActionButton_d.addActionListener(new BunchFrame_runActionButton_d_actionAdapter(this));
  configureMenu_d.setText("Configure");
  configureOptionsMenuItem_d.setText("Options");
  commandLabel_d.setText("Action:");
  optionsLabel_d.setText("Options:");
  outputLastButton_d.setText("Save...");
  nextLevelGraphButton_d.setText("Generate Next Level");
  nodesLabel_d.setText("Nodes:");
  sendToSuppliersButton_d.setText("->");
  sendToSuppliersButton_d.setFont(new Font("Monospaced", 0, 11));
  sendToSuppliersButton_d.addActionListener(new BunchFrame_sendToSuppliersButton_d_actionAdapter(this));
  receiveFromClientsButton_d.setText("<-");
  receiveFromClientsButton_d.setFont(new Font("Monospaced", 0, 11));
  receiveFromClientsButton_d.addActionListener(new BunchFrame_receiveFromClientsButton_d_actionAdapter(this));
  findOmnipresentNodesButton_d.setText("jButton1");
  findOmnipresentNodesButton_d.setText("Find");
  findOmnipresentNodesButton_d.addActionListener(new BunchFrame_findOmnipresentNodesButton_d_actionAdapter(this));
  findOmniLabel1_d.setText("omnipresent modules with");
  findOmnipresentThreshold_d.setText("3.0");
  findOmnilabel2_d.setText("times the average connections");
  suppliersLabel2_d.setText("Libraries:");
  findLibraryNodesButton_d.setText("jButton1");
  findLibraryNodesButton_d.setText("Find");
  findLibraryNodesButton_d.addActionListener(new BunchFrame_findLibraryNodesButton_d_actionAdapter(this));
  nodesLabel2_d.setText("Nodes:                      ");
  sendLibToClientsButton_d.setText("->");
  sendLibToClientsButton_d.setFont(new Font("Monospaced", 0, 11));
  sendLibToClientsButton_d.addActionListener(new BunchFrame_sendLibToClientsButton_d_actionAdapter(this));
  findOmniLabel1_d1.setText("library modules automatically");
  clientButtonPanel_d1.setLayout(gridBagLayout8);
  librariesPane_d.setLayout(gridBagLayout6);
  receiveLibFromClientsButton_d.setText("<-");
  receiveLibFromClientsButton_d.setFont(new Font("Monospaced", 0, 11));
  inputClusterFile_d.setText("                                                         ");
  inputClusterFileSelectButton_d.setText("jButton1");
  inputClusterFileSelectButton_d.setActionCommand("Select Input Cluster File");
  inputClusterFileSelectButton_d.setText("Select...");
  inputClusterLabel_d.setText("Input Cluster File:");
  lockClustersCheckbox_d.setEnabled(false);
  lockClustersCheckbox_d.setText("Lock Clusters");
  receiveFromCentralButton_d.setText("<-");
  receiveFromCentralButton_d.setFont(new Font("Monospaced", 0, 11));
  sendToCentralButton_d.setText("->");
  sendToCentralButton_d.setFont(new Font("Monospaced", 0, 11));
  centralLabel_d.setText("Clients & Suppliers");
  centralLabel_d.setToolTipText("");
  consolidateDriftersCB.setText("jCheckBox1");
  consolidateDriftersCB.setActionCommand("Consolidate Drifters");
  consolidateDriftersCB.setText("Consolidate Drifters");
  consolidateDriftersCB.setFont(new Font("Dialog", 0, 12));
  consolidateDriftersCB.setSelected(true);
  consolidateDriftersCB.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      consolidateDriftersCB_actionPerformed(e);
    }
  });
  jLabel1.setText("Use the following options to control Bunch\'s clustering engine:");
  jLabel2.setText("Clustering Algorithm:");
  delimEF.setFont(new Font("Dialog", 0, 12));
  delimEF.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      delimEF_actionPerformed(e);
    }
  });
  jLabel3.setText("Graph File Delimiters:");
  spaceDelimCB.setText("Include Space");
  spaceDelimCB.setActionCommand("spaceDelim");
  spaceDelimCB.setFont(new Font("Dialog", 0, 12));
  spaceDelimCB.setSelected(true);
  spaceDelimCB.setToolTipText("Include spaces");
  tabDelimCB.setText("Tab");
  tabDelimCB.setToolTipText("Include Tabs");
  tabDelimCB.setSelected(true);
  spaceDelimCB.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      spaceDelimCB_actionPerformed(e);
    }
  });
  ClusteringAlgEF.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(ActionEvent e) {
      ClusteringAlgEF_actionPerformed(e);
    }
  });
  clusteringOptions.setLayout(gridBagLayout10);
  sendToCentralButton_d.addActionListener(new BunchFrame_sendToCentralButton_d_actionAdapter(this));
  receiveFromCentralButton_d.addActionListener(new BunchFrame_receiveFromCentralButton_d_actionAdapter(this));
  centralButtonPanel_d.setLayout(gridBagLayout9);
  inputClusterFileSelectButton_d.addActionListener(new BunchFrame_inputClusterFileSelectButton_d_actionAdapter(this));
  userDirectedClusteringPane_d.setLayout(gridBagLayout7);
  receiveLibFromClientsButton_d.addActionListener(new BunchFrame_receiveLibFromClientsButton_d_actionAdapter(this));
  clientButtonPanel_d.setLayout(gridBagLayout5);
  supplierButtonPanel_d.setLayout(gridBagLayout4);
  receiveFromSuppliersButton_d.setText("<-");
  receiveFromSuppliersButton_d.setFont(new Font("Monospaced", 0, 11));
  receiveFromSuppliersButton_d.addActionListener(new BunchFrame_receiveFromSuppliersButton_d_actionAdapter(this));
  sendToClientsButton_d.setText("->");
  sendToClientsButton_d.setFont(new Font("Monospaced", 0, 11));
  sendToClientsButton_d.addActionListener(new BunchFrame_sendToClientsButton_d_actionAdapter(this));
  suppliersLabel_d.setText("Suppliers:");
  clientsLabels_d.setText("Clients:");
  omnipresentPane_d.setLayout(gridBagLayout3);
  outputLastButton_d.addActionListener(new BunchFrame_outputLastButton_d_actionAdapter(this));
  nextLevelGraphButton_d.addActionListener(new BunchFrame_nextLevelGraphButton_d_actionAdapter(this));
  configureOptionsMenuItem_d.addActionListener(new BunchFrame_configureOptionsMenuItem_d_actionAdapter(this));
  bunchSettingsPanel_d.setLayout(gridBagLayout2);
  fileMenu_d.setText("File");
  menuFileExit.setText("Exit");
  menuFileExit.addActionListener(new BunchFrame_menuFileExit_ActionAdapter(this));
  helpMenu_d.setText("Help");
  menuHelpAbout.setText("About");
  menuHelpAbout.addActionListener(new BunchFrame_menuHelpAbout_ActionAdapter(this));
  visualizeButton_d.setEnabled(false);
  visualizeButton_d.setText("Visualize...");

  distPane.setLayout(gridBagLayout11);
  distClustEnableCB.setText("jCheckBox1");
  distClustEnableCB.setText("Enable Distributed Clustering");
  distClustEnableCB.addChangeListener(new javax.swing.event.ChangeListener() {

    public void stateChanged(ChangeEvent e) {
      distClustEnableCB_stateChanged(e);
    }
  });
  jLabel4.setText("Namespace:");
  jLabel5.setText("Name Server:");
  jLabel6.setText("Port:");
  queryNS.setText("Query Name Server");
  queryNS.addActionListener(new java.awt.event.ActionListener() {

    public void actionPerformed(ActionEvent e) {
      queryNS_actionPerformed(e);
    }
  });
  queryNS.addActionListener(new java.awt.event.ActionListener() {

    public void actionPerformed(ActionEvent e) {
      queryNS_actionPerformed(e);
    }
  });
  queryNS.addActionListener(new BunchFrame_queryNS_actionAdapter(this));
  jLabel7.setFont(new java.awt.Font("Dialog", 1, 12));
  jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
  jLabel7.setText("Results (Select to Activate - use CTRL key for multiple selections)");
  includeDistSvrsPB.setText("Include Selected Servers");
  includeDistSvrsPB.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      includeDistSvrsPB_actionPerformed(e);
    }
  });

  nameSpaceEF.setText("BunchServer");
  portEF.setText("900");
  jLabel8.setText("Base UOW Size:");
  UOWSzEF.setText("5");
  deactivatePB.setEnabled(false);
  deactivatePB.setSelected(true);
  deactivatePB.setText("Deactivate All Servers");
  deactivatePB.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      deactivatePB_actionPerformed(e);
    }
  });
  serverList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

  public void valueChanged(ListSelectionEvent e) {
      serverList_valueChanged(e);
    }
  });
  adaptiveEnableCB.setSelected(true);
  adaptiveEnableCB.setEnabled(false);
  adaptiveEnableCB.setText("Use Adaptive Load Balancing");
  timeoutEnable.setText("Limit Runtime To");
  timeoutEnable.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      timeoutEnable_actionPerformed(e);
    }
  });
  jLabel9.setText("(ms)");
  maxRuntimeEF.setEnabled(false);
  maxRuntimeEF.setText("1000");
  utilityMenu_d.setText("Utility");
  utilityMeasurementCalc.setText("Measurement Calculators...");
  utilityMeasurementCalc.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      utilityMeasurementCalc_actionPerformed(e);
    }
  });
  actionList_d.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      actionList_d_actionPerformed(e);
   }
  });
  jLabel10.setText("Agglomerative");
  jLabel11.setText("Output Options:");
  agglomOutputCB.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      agglomOutputCB_actionPerformed(e);
    }
  });
  outputTreeCB.setText("Generate Tree Fomat");
  ClearClusterFile.setText("Clear");
  ClearClusterFile.addActionListener(new java.awt.event.ActionListener() {

  public void actionPerformed(ActionEvent e) {
      ClearClusterFile_actionPerformed(e);
    }
  });
  menuShowDistributedTab.setText("Show Distributed Tab");
  menuShowDistributedTab.addActionListener(new java.awt.event.ActionListener() {
  public void actionPerformed(ActionEvent e) {
      menuShowDistributedTab_actionPerformed(e);
    }
  });
  clusteringUtilsMenu.setText("Clustering Utilities...");
  clusteringUtilsMenu.addActionListener(new BunchFrame_clusteringUtilsMenu_actionAdapter(this));
  fileUtilsMenu.setText("File Utilities...");
    fileUtilsMenu.addActionListener(new BunchFrame_fileUtilsMenu_actionAdapter(this));
    fileMenu_d.add(menuFileExit);
  helpMenu_d.add(menuHelpAbout);
  bunchMenubar_d.add(fileMenu_d);
  bunchMenubar_d.add(utilityMenu_d);
  bunchMenubar_d.add(helpMenu_d);
  this.setJMenuBar(bunchMenubar_d);
  this.setResizable(false);

  standardNodeList_d.setModel(standardNodeListModel_d);
  clientsList_d.setModel(clientsListModel_d);
  suppliersList_d.setModel(suppliersListModel_d);
  centralList_d.setModel(centralListModel_d);
  librariesList_d.setModel(librariesListModel_d);
  standardNodeListLib_d.setModel(standardNodeListModel_d);

  actionList_d.addItem("Agglomerative Clustering");
  actionList_d.addItem("User-Driven Clustering");

  this.getContentPane().add(runActionButton_d, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  this.getContentPane().add(mainTabbedPane_d, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 4, 6), 53, 50));
  mainTabbedPane_d.addTab("Basic", bunchSettingsPanel_d);
  bunchSettingsPanel_d.add(selectOutputFileButton_d, new GridBagConstraints2(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(outputClusterFilename_d, new GridBagConstraints2(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(selectGraphFileButton_d, new GridBagConstraints2(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(inputGraphFilename_d, new GridBagConstraints2(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(outputLabel_d, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 0, 0));
  bunchSettingsPanel_d.add(outputFileLabel_d, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 10, 2, 2), 0, 0));
  bunchSettingsPanel_d.add(inputGraphLabel_d, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 2, 2), 0, 0));
  bunchSettingsPanel_d.add(clusteringLabel_, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 2, 2), 0, 0));
  bunchSettingsPanel_d.add(clusteringMethodList_d, new GridBagConstraints2(1, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(outputFileFormatList_d, new GridBagConstraints2(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(clusteringOptionsButton_d, new GridBagConstraints2(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(outputLastButton_d, new GridBagConstraints2(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  bunchSettingsPanel_d.add(nextLevelGraphButton_d, new GridBagConstraints2(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

  mainTabbedPane_d.addTab("Clustering Options", clusteringOptions);
  clusteringOptions.add(consolidateDriftersCB, new GridBagConstraints2(0, 1, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(jLabel1, new GridBagConstraints2(0, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 14));
  clusteringOptions.add(jLabel2, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 4), 0, 0));
  clusteringOptions.add(ClusteringAlgEF, new GridBagConstraints2(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 4, 0), 67, 0));
  clusteringOptions.add(delimEF, new GridBagConstraints2(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 7, 5), 73, 6));
  clusteringOptions.add(jLabel3, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.VERTICAL, new Insets(4, 0, 10, 0), 4, 0));
  clusteringOptions.add(spaceDelimCB, new GridBagConstraints2(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(tabDelimCB, new GridBagConstraints2(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(timeoutEnable, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(maxRuntimeEF, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(jLabel9, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 0, 0), 0, 0));
  clusteringOptions.add(jLabel10, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 0, -2, 0), 0, 0));
  clusteringOptions.add(jLabel11, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(agglomOutputCB, new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
  clusteringOptions.add(outputTreeCB, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
           ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
  this.getContentPane().add(commandLabel_d, new GridBagConstraints2(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
  this.getContentPane().add(actionList_d, new GridBagConstraints2(0, 3, 1/*2*/, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 5, 10), 72, 0));
  this.getContentPane().add(optionsLabel_d, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 10, 5, 5), 0, 0));

  mainTabbedPane_d.addTab("Libraries", librariesPane_d);
  librariesPane_d.add(suppliersLabel2_d,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 175, 0));
  librariesPane_d.add(librariesListPane_d,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 3, 0, 7), 0, 0));
  librariesPane_d.add(clientButtonPanel_d1,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 125));
  clientButtonPanel_d1.add(sendLibToClientsButton_d, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
  clientButtonPanel_d1.add(receiveLibFromClientsButton_d, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
           ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
  librariesPane_d.add(omniInternalPane_d1, new GridBagConstraints2(0, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  omniInternalPane_d1.add(findLibraryNodesButton_d, null);
  omniInternalPane_d1.add(findOmniLabel1_d1, null);
  librariesPane_d.add(nodesLabel2_d,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 125, 0));
  librariesPane_d.add(standardNodeListPaneLib_d,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 4, 0, 6), 0, 0));
  mainTabbedPane_d.addTab("Omnipresent", omnipresentPane_d);
  omnipresentPane_d.add(standardNodeListPane_d, new GridBagConstraints2(0, 1, 1, 5, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
  omnipresentPane_d.add(nodesLabel_d, new GridBagConstraints2(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 10, 4, 0), 0, 0));
  omnipresentPane_d.add(suppliersLabel_d, new GridBagConstraints2(2, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  omnipresentPane_d.add(suppliersListPane_d, new GridBagConstraints2(2, 1, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 10));
  omnipresentPane_d.add(clientsLabels_d, new GridBagConstraints2(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
  omnipresentPane_d.add(clientsListPane_d, new GridBagConstraints2(2, 3, 2, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 13));
  omnipresentPane_d.add(supplierButtonPanel_d, new GridBagConstraints2(1, 1, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 4, 10, 6), 0, 0));
  supplierButtonPanel_d.add(receiveFromSuppliersButton_d, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
  supplierButtonPanel_d.add(sendToSuppliersButton_d, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
  omnipresentPane_d.add(clientButtonPanel_d, new GridBagConstraints2(1, 3, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(1, 5, 9, 5), 0, 0));
  clientButtonPanel_d.add(sendToClientsButton_d, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
  clientButtonPanel_d.add(receiveFromClientsButton_d, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
  omnipresentPane_d.add(omniInternalPane_d, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(20, 5, 5, 5), 0, 0));
  omniInternalPane_d.add(findOmnipresentNodesButton_d, null);
  omniInternalPane_d.add(findOmniLabel1_d, null);
  omniInternalPane_d.add(findOmnipresentThreshold_d, null);
  omniInternalPane_d.add(findOmnilabel2_d, null);
  omnipresentPane_d.add(centralButtonPanel_d, new GridBagConstraints(1, 5, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(1, 5, 9, 5), 0, 0));
  centralButtonPanel_d.add(receiveFromCentralButton_d, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
  centralButtonPanel_d.add(sendToCentralButton_d, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));
  omnipresentPane_d.add(centralListPane_d, new GridBagConstraints2(2, 5, 1, 1, 1.0, 1.0
            ,GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 12));
  omnipresentPane_d.add(centralLabel_d, new GridBagConstraints2(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));

  mainTabbedPane_d.addTab("User Directed Clustering", userDirectedClusteringPane_d);
  userDirectedClusteringPane_d.add(inputClusterFile_d, new GridBagConstraints2(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  userDirectedClusteringPane_d.add(inputClusterFileSelectButton_d, new GridBagConstraints2(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
  userDirectedClusteringPane_d.add(inputClusterLabel_d, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  userDirectedClusteringPane_d.add(lockClustersCheckbox_d, new GridBagConstraints2(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  userDirectedClusteringPane_d.add(ClearClusterFile, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

  distPane.add(distClustEnableCB, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(jLabel4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 16), 0, 0));
  distPane.add(jLabel5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(nameServerEF, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(jLabel6, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(portEF, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(queryNS, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, -7));
  distPane.add(jLabel7, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(8, 0, 1, 0), 27, 0));
  distPane.add(includeDistSvrsPB, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(9, 0, 0, 0), 0, -5));
  distPane.add(nameSpaceEF, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(jScrollPane1, new GridBagConstraints(0, 7, 3, 2, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 0), 6, 58));
  distPane.add(jLabel8, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
  distPane.add(UOWSzEF, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
  distPane.add(deactivatePB, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(2, 7, 0, 0), 0, -5));
  distPane.add(adaptiveEnableCB, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 1), -6, 0));
  jScrollPane1.getViewport().add(serverList, null);
  configureMenu_d.add(configureOptionsMenuItem_d);
  utilityMenu_d.add(utilityMeasurementCalc);
  utilityMenu_d.add(clusteringUtilsMenu);
    utilityMenu_d.add(fileUtilsMenu);
  utilityMenu_d.add(menuShowDistributedTab);

  //disable the tabs that should be disabled until a graph is loaded
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(omnipresentPane_d), false);
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(librariesPane_d), false);
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(userDirectedClusteringPane_d), false);
  runActionButton_d.setEnabled(false);
  nextLevelGraphButton_d.setEnabled(false);
  outputLastButton_d.setEnabled(false);
  inputClusterFile_d.setEditable(false);
  inputGraphFilename_d.setEditable(false);
  visualizeButton_d.setEnabled(false);

  consolidateDriftersCB.setVisible(false);

  agglomOutputCB.addItem("Output Median Level");
  agglomOutputCB.addItem("Output Detailed Level");
  agglomOutputCB.addItem("Output Top Level");
  agglomOutputCB.addItem("Output All Levels");





  setLastResultGraph(null);

  //obtain the available output formats and add them to the list
  methodList = preferences_d.getGraphOutputFactory().getItemList();
  for (int i=0; i<methodList.length; ++i) {
    outputFileFormatList_d.addItem(methodList[i]);
  }
  String defaultOutput = preferences_d.getGraphOutputFactory().defaultOption;
  outputFileFormatList_d.setSelectedItem(defaultOutput);


}

/**
 * Check if the clustering technique is agglomerative.
 *
 * @returns True if agglomerative, false if user-directed
 */
public boolean isAgglomerativeTechnique()
{
  String action = (String)actionList_d.getSelectedItem();
  if(action.equals("Agglomerative Clustering"))
    return true;

  return false;
}

/**
 * Check if the clustering technique is user-directed.
 *
 * @returns True if user-directed, false if agglomerative
 */
public boolean isUserDrivenTechnique()
{
  String action = (String)actionList_d.getSelectedItem();
  if(action.equals("User-Driven Clustering"))
    return true;

  return false;
}

/**
 * Loads the ClusteringMethod class that corresponds to the name passed as parameter
 * The class is loaded by asking the ClusteringMethodFactory for it.
 *
 * @param method the name of the ClusteringMethod to load
 * @see bunch.ClusteringMethodFactory.getMethod(ClusteringMethod)
 */
private
void
setClusteringMethod(String method)
{
  if (!method.getClass().getName().equals(method)) {
    clusteringMethod_d = preferences_d.getClusteringMethodFactory().getMethod(method);
    clusteringOptionsButton_d.setEnabled(clusteringMethod_d.isConfigurable());
    configuration_d = clusteringMethod_d.getConfiguration();
    if (initialGraph_d!=null&&configuration_d!=null) {
      configuration_d.init(initialGraph_d);
    }
  }
}

/**
 * Method that is executed when the Exit option in the File menu is called.
 */
public
void
fileExit_actionPerformed(ActionEvent e)
{
  System.exit(0);
}

/**
 * Method that is executed when the About option in the Help menu is called.
 */
public
void
helpAbout_actionPerformed(ActionEvent e)
{
  BunchFrame_AboutBox dlg = new BunchFrame_AboutBox(this);
  Dimension dlgSize = dlg.getPreferredSize();
  Dimension frmSize = getSize();
  Point loc = getLocation();
  dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  dlg.setModal(true);
  dlg.show();
}

/**
 * Overriden so we can exit on System Close without implementing WindowListener
 */
protected
void
processWindowEvent(WindowEvent e)
{
  super.processWindowEvent(e);
  if (e.getID() == WindowEvent.WINDOW_CLOSING) {
    fileExit_actionPerformed(null);
  }
}

/**
 * Called to get the list of delimeters that is used to parse the input
 * MDG file.
 *
 * @returns A string with a list of delimeters
 */
public String getDelims()
{
  String delims = delimEF.getText();
  boolean state = spaceDelimCB.isSelected();
  if (state == true)
      delims = " " + delims;  //includes the space character
  state = tabDelimCB.isSelected();
  if (state == true)
      delims = "\t" + delims; //includes the tab character

  return delims;
}

boolean checkFile(String fileName)
{
  File f = new File(fileName);

  return f.isFile();
}
/**
 * Used to parse the input MDG graph.  This activity happens after the
 * input MDG file graph is selected
 */
void
selectGraphFileButton_d_actionPerformed(ActionEvent e)
{
  /**
   * Get the delimeters
   */
  String delims = delimEF.getText();
  boolean state = spaceDelimCB.isSelected();
  if (state == true)
      delims = " " + delims;  //includes the space character
  state = tabDelimCB.isSelected();
  if (state == true)
      delims = "\t" + delims; //includes the tab character

  /**
   * Parse the MDG file with the appropriate input parser
   */
  fileSelector_d.setVisible(true);
  if (fileSelector_d.getFile() != null) {
    String filename = fileSelector_d.getDirectory()+fileSelector_d.getFile();
    if(checkFile(filename) == false)
    {
      JOptionPane.showMessageDialog(this,
              "There is a problem with the selected MDG file - "+filename,
              "Error: Bad File Name",
              JOptionPane.ERROR_MESSAGE);
      return;
    }

    inputGraphFilename_d.setText(filename);
    String parserClass = "dependency";
    if(filename.endsWith(".gxl") || filename.endsWith(".GXL"))
      parserClass = "gxl";

    Parser p = preferences_d.getParserFactory().getParser(parserClass);
    p.setInput(filename);
    p.setDelims(delims);

    initialGraph_d = (Graph)p.parse();

    /**
     * Check if there are reflexive edges in the MDG file.  If so, warn the
     * user that they were removed.
     */
    //if (((DependencyFileParser)p).hasReflexiveEdges())
    if(p.hasReflexiveEdges())
    {
      //int count = ((DependencyFileParser)p).getReflexiveEdges();
      int count = p.getReflexiveEdges();
      Integer reflexiveEdgeCnt = new Integer(count);

      String msg =  "Bunch has determined that your input\n";
             msg += "MDG contains " + reflexiveEdgeCnt.toString() + " reflexive edges.\n";
             msg += "Bunch assumes cohesiveness in modules/classes, thus these\n";
             msg += "edges will be removed from the custering process.";

      JOptionPane.showMessageDialog(this,
              msg, "Warning: Found Reflexive Edges",
              JOptionPane.WARNING_MESSAGE);
    }

    if (configuration_d != null) {
      configuration_d.init(initialGraph_d);
    }

    /**
     * If agglomerative clustering, setup the tree of graphs to be used to
     * manage the hierarchy of partitioned graphs.
     */
    String cmd = (String)actionList_d.getSelectedItem();
    if(cmd.equals("Agglomerative Clustering"))
    {
       if(initialGraph_d != null)
          initialGraph_d.setIsClusterTree(true);
        nextLevelGraphButton_d.setEnabled(false);
    }
    else
    {
      if(initialGraph_d != null)
        initialGraph_d.setIsClusterTree(false);
      nextLevelGraphButton_d.setEnabled(true);
    }
    outputClusterFilename_d.setText(filename);
    clearGUIElements(false);
  }
}

/**
 * Resets some GUI elements to their default values (in some cases disables
 * them) when a new graph file has been loaded
 */
public
void
clearGUIElements(boolean nextLevel)
{
  inputClusterFile_d.setText("");
  fileBasicName_d = fileSelector_d.getFile();
  standardNodeListModel_d.removeAllElements();
  clientsListModel_d.removeAllElements();
  suppliersListModel_d.removeAllElements();
  centralListModel_d.removeAllElements();
  librariesListModel_d.removeAllElements();
  Node[] nl = initialGraph_d.getNodes();
  for (int i=0; i<nl.length; ++i) {
   standardNodeListModel_d.addElement(nl[i].getName());
  }
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(omnipresentPane_d), true);
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(librariesPane_d), true);
  mainTabbedPane_d.setEnabledAt(mainTabbedPane_d.indexOfComponent(userDirectedClusteringPane_d), true);

  runActionButton_d.setEnabled(true);
  if (!nextLevel) {
    nextLevelGraphButton_d.setEnabled(false);
    outputLastButton_d.setEnabled(false);
    visualizeButton_d.setEnabled(false);
  }
}

/**
 * Called when the "Select" Button for the Output File field has been pressed
 * loads a file dialog box, obtains the name and sets it
 *
 * @param e the ActionEvent that triggered the method call
 */
void
selectOutputFileButton_d_actionPerformed(ActionEvent e)
{
  fileSelector_d.setVisible(true);
  if (fileSelector_d.getFile() != null) {
    String filename = fileSelector_d.getDirectory()+fileSelector_d.getFile();
    outputClusterFilename_d.setText(filename);
    fileBasicName_d = fileSelector_d.getFile();
  }
}

/**
 * This method is called when the "Configure" option in the Options menu is called
 * To be used in the future to support storable configurations for bunch.
 */
void
configureOptionsMenuItem_d_actionPerformed(ActionEvent e)
{
}

/**
 * Called when the "Options" Button for the Clustering Method list has been pressed
 * loads a file dialog box, obtains the name and sets it
 *
 * @param e the ActionEvent that triggered the method call
 */
void
clusteringOptionsButton_d_actionPerformed(ActionEvent e)
{
  ClusteringConfigurationDialog dlg = null;

  try {
    dlg = (ClusteringConfigurationDialog)Beans.instantiate(null, clusteringMethod_d.getConfigurationDialogName());
    dlg.setModal(true);
    dlg.setParentFrame(this);
    dlg.setTitle("Clustering Algorithm Configuration");
    dlg.setGraph(initialGraph_d);
    dlg.setConfiguration(configuration_d);
    dlg.jbInit();
    dlg.pack();
  }
  catch (Exception ex) {
    ex.printStackTrace();
  }
  if (inputGraphFilename_d.getText() == null
            || inputGraphFilename_d.getText().equals("")
            || initialGraph_d == null) {

    JOptionPane.showMessageDialog(this,
              "Error: missing input graph.", "MQ Calculation: Missing Parameter",
              JOptionPane.ERROR_MESSAGE);
    return;
  }
  Dimension dlgSize = dlg.getPreferredSize();
  Dimension frmSize = getSize();
  Point loc = getLocation();
  dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  dlg.setVisible(true);

  clusteringMethod_d.setConfiguration(dlg.getConfiguration());
}

/**
 * Called when a new item is selected on the list of Clustering Methods
 */
void
clusteringMethodList_d_itemStateChanged(ItemEvent e)
{
  setClusteringMethod((String)clusteringMethodList_d.getSelectedItem());
  setupClusteringOptions();
}

/**
 * Called when the "Select" Button for the Input Cluster File field has been pressed
 * loads a file dialog box, obtains the name and loads the file. This feature is
 * used to manage user-directed clustering.
 *
 * @param e the ActionEvent that triggered the method call
 */
void
inputClusterFileSelectButton_d_actionPerformed(ActionEvent e)
{
  if (inputGraphFilename_d.getText() == null
            || inputGraphFilename_d.getText().equals("")) {
      JOptionPane.showMessageDialog(this,
              "Error: missing input graph. \n Select an input graph file first.", "Error: Missing Parameter",
              JOptionPane.ERROR_MESSAGE);
      return;
  }
  fileSelector_d.setVisible(true);
  if (fileSelector_d.getFile() != null) {
    String filename = fileSelector_d.getDirectory()+fileSelector_d.getFile();
    inputClusterFile_d.setText(filename);

    Parser p = preferences_d.getParserFactory().getParser("cluster");
    p.setInput(filename);
    p.setObject(initialGraph_d);
    p.parse();
    lockClustersCheckbox_d.setEnabled(true);
  }
}

/**
 * Get the unit of work used for distributed clustering.  If one is not
 * specified by the user, use the default which is a constant to this class.
 *
 * @returns The selected unit of work size
 */
public int getUOWSz()
{
  try{
    Integer i = new Integer(UOWSzEF.getText());
    return i.intValue();
  }catch(Exception e)
  {  return DEFAULT_UNIT_OF_WORK_SZ;  }
}

/**
 * Specifies if the distributed clustering adaptive feature is enabled.  If so
 * the unit of work will be dynamically updated, both up and down, during the
 * distributed clustering process.
 *
 * @returns True if the flag is set, false if not
 */
public boolean getAdaptiveEnableFlag()
{
  return  adaptiveEnableCB.isSelected();
}

/**
 * Returns the callback proxy object for the Bunch Server.  This object can
 * be used to "callback" the bunch server.  It is used in respose to distributed
 * events.
 *
 * @returns The object instance of the distributed server callback
 */
public CallbackImpl getSvrCallback()
{
   return svrCallback;
}

/**
 * Called when the "Run" Button has been pressed. This will execute the
 * action selected in the list of actions that appears to the left of
 * the button
 *
 * @param e the ActionEvent that triggered the method call
 */
void
runActionButton_d_actionPerformed(ActionEvent e)
{

  /**
   * First ensure that an input graph is specified
   */
  if (outputClusterFilename_d.getText() == null
          || outputClusterFilename_d.getText().equals(""))
  {
    JOptionPane.showMessageDialog(this,
            "Error: missing input graph file\nor output graph filename.", "MQ Calculation: Missing Parameter",
            JOptionPane.ERROR_MESSAGE);
    return;
  }

  /**
   * Determine the appropriate options based on the GUI parameters
   */
  String method = (String)clusteringMethodList_d.getSelectedItem();
  String outputMethod = (String)outputFileFormatList_d.getSelectedItem();

  mainTabbedPane_d.setSelectedComponent(bunchSettingsPanel_d);

  /**
   * Now setup the special clusters
   */
  if (lockClustersCheckbox_d.isSelected()) {
    initialGraph_d.setDoubleLocks(true);
  }

  int[] clust = initialGraph_d.getClusters();
  boolean[] locks = initialGraph_d.getLocks();
  for (int i=0; i<clust.length; ++i) {
    if (clust[i] != -1) {
      locks[i] = true;
    }
  }

  if (librariesListModel_d.size() > 0 || suppliersListModel_d.size() > 0
      || clientsListModel_d.size() > 0 || centralListModel_d.size() > 0) {
      arrangeLibrariesClientsAndSuppliers();
  }

  clusteringMethod_d.initialize();
  clusteringMethod_d.setGraph(initialGraph_d.cloneGraph());
  graphOutput_d = preferences_d.getGraphOutputFactory().getOutput(outputMethod);
  graphOutput_d.setBaseName(outputClusterFilename_d.getText());
  graphOutput_d.setBasicName(fileBasicName_d);
  configureOptions();

  //==================
  //DISTRIB CLUSTERING ENTRY POINT
  //=================
  /**
   * This path of code is used for the distributed clustering options
   */
  boolean doDistrib = distClustEnableCB.isSelected();
  if(doDistrib == true)
  {
    /**
     * Get the distributed options
     */
    bunch.LoadBalancer.Manager lbManager = new bunch.LoadBalancer.Manager();
    bunch.BunchServer.DistribInit diMsg = new bunch.BunchServer.DistribInit();
    diMsg.theGraph = initialGraph_d;
    diMsg.clusteringTechnique = method;
    diMsg.objFunction = (String)ClusteringAlgEF.getSelectedItem();
    diMsg.config = configuration_d;
    diMsg.bp = preferences_d;

    lbManager.baseUOWSz = this.getUOWSz();
    lbManager.useAdaptiveAlg = this.getAdaptiveEnableFlag();

    /**
     * Process the server vector for each server.  Initialize each server
     */
    if(activeServerVector!= null)
       for (int i = 0; i < activeServerVector.size(); i++)
       {
          Binding b = (Binding)activeServerVector.elementAt(i);
          diMsg.svrID = lbManager.createNewServer();
          diMsg.svrName = b.getName();
          diMsg.adaptiveEnabled = getAdaptiveEnableFlag();
          byte[] so = bunch.util.BunchUtilities.toByteArray(diMsg);
          if (so != null)
          {
             BunchSvrMsg bsm = (BunchSvrMsg)b.getObject();
             try{
               boolean rc = bsm.invokeMessage("Init",so);
             }catch(Exception ex)
             {
                JOptionPane.showMessageDialog(this,
                   ex.toString(), "Error Initializing Server: " + b.getName(),
                   JOptionPane.ERROR_MESSAGE);
             }
          }
       }

       /**
        * Get ready to do the distributed clustering
        */
       try
       {
          bevent = new BunchEvent();

          DistributedHCClusteringMethod dcm =
             new DistributedHCClusteringMethod();

          dcm.setEventObject(bevent);
          dcm.setActiveServerVector(activeServerVector);

          lbManager.baseUOWSz = this.getUOWSz();
          lbManager.useAdaptiveAlg = this.getAdaptiveEnableFlag();
          svrCallback.bevent = bevent;
          svrCallback.lbManager = lbManager;

          /**
           * For now, only NAHC is supported in the distributed version
           * of Bunch.  Set its configuration parameters to the default
           * values. These can be overriden by the user on the GUI.
           */
          NAHCConfiguration hcc = (NAHCConfiguration)dcm.getConfiguration();

          hcc.setNumOfIterations(1);
          hcc.setThreshold(1.0);
          hcc.setRandomizePct(100);
          hcc.setMinPctToConsider(0);

          ((GenericDistribHillClimbingClusteringMethod)dcm).setConfiguration(configuration_d);

          /**
           * Initialize the distributed clustering engine
           */
          dcm.initialize();
          dcm.setGraph(initialGraph_d.cloneGraph());

          /**
           * Display the distributed clustering dialog box which will be used
           * to manage the distributed clustering process
           */
          DistribClusteringProgressDlg dlg = null;
          dlg = new DistribClusteringProgressDlg(this, "Distributed Clustering " + initialGraph_d.getNumberOfNodes() + " nodes...", true,dcm);

          Dimension dlgSize = dlg.getPreferredSize();
          Dimension frmSize = getSize();
          Point loc = getLocation();
          dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
          dlg.setVisible(true);
       }

       /**
        * Catch and notify the user of a distributed clustering error.
        */
       catch(Exception ex)
       {
          JOptionPane.showMessageDialog(this,
            ex.toString(), "Error Doing Distributed Clustering: " + ex.toString(),
            JOptionPane.ERROR_MESSAGE);
       }
    }
    /**
     * We are using the standard, non distributed clustering engine
     */
    else
    {
      /**
       * Get ready to display the clustering progress dialog box which will
       * manage the clustering process.
       */
      ClusteringProgressDialog dlg = null;
      dlg = new ClusteringProgressDialog(this, "Clustering " + initialGraph_d.getNumberOfNodes() + " nodes...", true);

      Dimension dlgSize = dlg.getPreferredSize();
      Dimension frmSize = getSize();
      Point loc = getLocation();
      dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);

      dlg.setModal(false);
      dlg.setVisible(true);
      dlg.startClustering();
      dlg.setModal(true);


      /**
       * If clustering is user-managed setup the generate next level feature by
       * enabling the button
       */
      if(!actionList_d.getSelectedItem().equals("Agglomerative Clustering"))
        nextLevelGraphButton_d.setEnabled(true);
      outputLastButton_d.setEnabled(true);

      /**
       * If the output format is dotty, enable the visualize button.
       */
      if (outputFileFormatList_d.getSelectedItem().equals("Dotty"))
         visualizeButton_d.setEnabled(true);
    }
}

/**
 * This method sets the libraries, clients and suppliers defined in their
 * respective panes to the graph, just previous to processing.
 */
public
void
arrangeLibrariesClientsAndSuppliers()
{
  //Node[] nodeList = initialGraph_d.getNodes();
  Node[] nodeList = null;

  if(initialGraph_d.getOriginalNodes() != null)
    nodeList = initialGraph_d.getOriginalNodes();
  else
    nodeList = initialGraph_d.getNodes();

  Node[] originalList = (Node [])nodeList.clone();
  Node[] origListCopy = (Node [])nodeList.clone();

  for (int j=0; j<originalList.length; ++j)
    originalList[j].setType(Node.NORMAL);


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
        if ((originalList[tmpDeps[j]].getType() == Node.NORMAL) || // )  // ||
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
        if ((originalList[tmpBeDeps[j]].getType() == Node.NORMAL) || //&& (noNormalDeps==true))//||
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
                (clientsListModel_d.size()+suppliersListModel_d.size()+ deadNodes+
                +centralListModel_d.size()+librariesListModel_d.size())];
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
  initialGraph_d.initGraph(nodeList.length);
  initialGraph_d.clear();
  initialGraph_d.setNodes(nodeList);
  //initialGraph_d.setOriginalNodes(originalList);
  initialGraph_d.setOriginalNodes(origListCopy);
}

/**
 * This feature is used for debugging.  It shows the state of the modules
 * both before and after removing special modules
 */
public void debugDump(int[] b, int[]a)
{
  System.out.print("Before: ");
  for(int i = 0; i< b.length; i++)
    System.out.print(b[i]+" ");
  System.out.println();
  System.out.print("After: ");
  for(int i = 0; i< a.length; i++)
    System.out.print(a[i]+" ");
  System.out.println();
  System.out.println();
}

/**
 * stores the graph that resulted from the last Action. This can be used
 * later to generate another level of the graph and then optimize it
 *
 * @param g the graph to set as result graph
 * @see #getLastResultGraph()
 */
public
void
setLastResultGraph(Graph g)
{
  if (g == null) {
    outputLastButton_d.setEnabled(false);
    nextLevelGraphButton_d.setEnabled(false);
  }
  else {
    outputLastButton_d.setEnabled(true);
    if(!actionList_d.getSelectedItem().equals("Agglomerative Clustering"))
        nextLevelGraphButton_d.setEnabled(true);
  }
  lastResultGraph_d = g;
}

/**
 * Obtain the last graph resultant of an Action.
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
 * Configures the bunch environment based on the options selected
 * To be used in a future version.
 */
public
void
configureOptions()
{
  String s = (String)agglomOutputCB.getSelectedItem();

  /**
   * Handle the output option specified by the user
   */
  if (s.equals("Output All Levels"))
    graphOutput_d.setOutputTechnique(GraphOutput.OUTPUT_ALL_LEVELS);
  else if (s.equals("Output Median Level"))
    graphOutput_d.setOutputTechnique(GraphOutput.OUTPUT_MEDIAN_ONLY);
  else if (s.equals("Output Top Level"))
    graphOutput_d.setOutputTechnique(GraphOutput.OUTPUT_TOP_ONLY);
  else if (s.equals("Output Detailed Level"))
    graphOutput_d.setOutputTechnique(GraphOutput.OUTPUT_DETAILED_LEVEL_ONLY);

  graphOutput_d.setNestedLevels(outputTreeCB.isSelected());
}

/**
 * Gets the output method based on the selected output file format drop down
 * list box.
 */
public String getOutputMethod()
{
  return (String)outputFileFormatList_d.getSelectedItem();
}

/**
 * Obtains the initial, pre clustered graph.
 *
 * @returns The initial graph
 */
public Graph getInitalGraph()
{
  return initialGraph_d;
}
/**
 * Creates the "next level" graph for the current result graph. This lets
 * the user partition the last result graph using the resultant clusters
 * as nodes, therefore obtaining higher level partitions. The output will then
 * be in the form of a graph with nested clusters. When the graph has only one
 * cluster the method will display an error dialog, since no further
 * partitioning can be performed.
 *
 * @param e the ActionEvent that triggered the method call
 * @see #getLastResultGraph()
 */
void
nextLevelGraphButton_d_actionPerformed(ActionEvent e)
{
  if (getLastResultGraph() == null) {
    throw new RuntimeException("Error:\n Result graph was null but output button \nwas enabled!");
  }
  Graph g = getLastResultGraph();
  int[] cNames = g.getClusterNames();
  if (cNames.length == 1) { //there is only one cluster, we're finished
      JOptionPane.showMessageDialog(this,
              "Already only one cluster present.\nCan't create another level.", "Next Level Cluster Generation",
              JOptionPane.ERROR_MESSAGE);
      return;
  }

  NextLevelGraph nextL = new NextLevelGraph();
  Graph newG=nextL.genNextLevelGraph(g);
  initialGraph_d  = newG;
  initialGraph_d.setPreviousLevelGraph(g);
  initialGraph_d.setGraphLevel(g.getGraphLevel()+1);

  outputClusterFilename_d.setText(outputClusterFilename_d.getText()+"L"+initialGraph_d.getGraphLevel());
  clearGUIElements(true);
}

/**
 * Outputs the last graph created by an Action in the format selected on the
 * output format list.
 *
 * @param e the ActionEvent that triggered the method call
 * @see #getLastResultGraph()
 */
void
outputLastButton_d_actionPerformed(ActionEvent e)
{
    if (getLastResultGraph() == null) {
      throw new RuntimeException("Error:\n Result graph was null but output button \nwas enabled!");
    }
    if (outputClusterFilename_d.getText() == null
            || outputClusterFilename_d.getText().equals("")) {

      JOptionPane.showMessageDialog(this,
              "Error: Missing output graph filename.", "Output Graph: Missing Parameter",
              JOptionPane.ERROR_MESSAGE);
      return;
    }

    String outputMethod = (String)outputFileFormatList_d.getSelectedItem();
    graphOutput_d = preferences_d.getGraphOutputFactory().getOutput(outputMethod);
    graphOutput_d.setBaseName(outputClusterFilename_d.getText());
    graphOutput_d.setBasicName(fileBasicName_d);
    graphOutput_d.setCurrentName(outputClusterFilename_d.getText());
    graphOutput_d.setGraph(getLastResultGraph());
    graphOutput_d.write();
}

/**
 * Moves an item from the supplier nodes list to the normal nodes list in the
 * Omnipresent modules pane
 */
void
receiveFromSuppliersButton_d_actionPerformed(ActionEvent e)
{
  if (suppliersList_d.getSelectedIndex() != -1) {
    String element = (String)suppliersListModel_d.elementAt(suppliersList_d.getSelectedIndex());
    suppliersList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    standardNodeListModel_d.addElement(element);
    suppliersListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    suppliersList_d.revalidate();
    suppliersListPane_d.revalidate();
  }
}

/**
 * Moves an item from the normal nodes list to the supplier nodes list in the
 * Omnipresent modules pane
 */
void
sendToSuppliersButton_d_actionPerformed(ActionEvent e)
{
  if (standardNodeList_d.getSelectedIndex() != -1) {
    String element = (String)standardNodeListModel_d.elementAt(standardNodeList_d.getSelectedIndex());
    suppliersList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    suppliersListModel_d.addElement(element);
    standardNodeListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    suppliersList_d.revalidate();
    suppliersListPane_d.revalidate();
  }
}

/**
 * Moves an item from the normal nodes list to the client nodes list in the
 * Omnipresent modules pane
 */
void
sendToClientsButton_d_actionPerformed(ActionEvent e)
{
  if (standardNodeList_d.getSelectedIndex() != -1) {
    String element = (String)standardNodeListModel_d.elementAt(standardNodeList_d.getSelectedIndex());
    clientsList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    clientsListModel_d.addElement(element);
    standardNodeListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    clientsList_d.revalidate();
    clientsListPane_d.revalidate();
  }
}

/**
 * Moves an item from the client nodes list to the normal nodes list in the
 * Omnipresent modules pane
 */
void
receiveFromClientsButton_d_actionPerformed(ActionEvent e)
{
  if (clientsList_d.getSelectedIndex() != -1) {
    String element = (String)clientsListModel_d.elementAt(clientsList_d.getSelectedIndex());
    clientsList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    standardNodeListModel_d.addElement(element);
    clientsListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    clientsList_d.revalidate();
    clientsListPane_d.revalidate();
  }
}

/**
 * Moves an item from the normal nodes list to the client nodes list in the
 * Omnipresent modules pane
 */
void
sendToCentralButton_d_actionPerformed(ActionEvent e)
{
  if (standardNodeList_d.getSelectedIndex() != -1) {
    String element = (String)standardNodeListModel_d.elementAt(standardNodeList_d.getSelectedIndex());
    centralList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    centralListModel_d.addElement(element);
    standardNodeListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    centralList_d.revalidate();
    centralListPane_d.revalidate();
  }
}

/**
 * Moves an item from the client nodes list to the normal nodes list in the
 * Omnipresent modules pane
 */
void
receiveFromCentralButton_d_actionPerformed(ActionEvent e)
{
  if (centralList_d.getSelectedIndex() != -1) {
    String element = (String)centralListModel_d.elementAt(centralList_d.getSelectedIndex());
    centralList_d.setSelectedIndex(-1);
    standardNodeList_d.setSelectedIndex(-1);
    standardNodeListModel_d.addElement(element);
    centralListModel_d.removeElement(element);
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    centralList_d.revalidate();
    centralListPane_d.revalidate();
  }
}

/**
 * Automatically finds the omnipresent (client/supplier) modules in the
 * current loaded graph based on a multiple (user-defined) of the average
 * number of outgoing/incoming edges to the node
 */
void
findOmnipresentNodesButton_d_actionPerformed(ActionEvent e)
{
  //check for input graph
  if (inputGraphFilename_d.getText() == null
            || inputGraphFilename_d.getText().equals("")
            || initialGraph_d == null) {

      JOptionPane.showMessageDialog(this,
              "Error: Missing input graph.", "Omnipresent Calculator: Missing Parameter",
              JOptionPane.ERROR_MESSAGE);
      return;
  }

  //check for selected omnipresent
  if (clientsListModel_d.size() > 0 || suppliersListModel_d.size() > 0 || centralListModel_d.size() > 0) {
    int result = JOptionPane.showConfirmDialog(this,
                "This will clear the clients and suppliers\n you have already selected\n and start again.\n Are you sure?", "Cancel Automatic Calculation?",
                JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.NO_OPTION) {
      return;
    }

    //clear clients and suppliers lists
    for (int i=0; i<clientsListModel_d.size(); ++i) {
      standardNodeListModel_d.addElement(clientsListModel_d.elementAt(i));
    }
    for (int i=0; i<suppliersListModel_d.size(); ++i) {
      standardNodeListModel_d.addElement(suppliersListModel_d.elementAt(i));
    }
    for (int i=0; i<centralListModel_d.size(); ++i) {
      standardNodeListModel_d.addElement(centralListModel_d.elementAt(i));
    }
    clientsListModel_d.removeAllElements();
    suppliersListModel_d.removeAllElements();
    centralListModel_d.removeAllElements();
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    clientsList_d.revalidate();
    clientsListPane_d.revalidate();
    suppliersList_d.revalidate();
    suppliersListPane_d.revalidate();
    centralList_d.revalidate();
    centralListPane_d.revalidate();
  }

  clientsList_d.setSelectedIndex(-1);
  suppliersList_d.setSelectedIndex(-1);
  centralList_d.setSelectedIndex(-1);
  standardNodeList_d.setSelectedIndex(-1);

  double threshold = Double.valueOf(findOmnipresentThreshold_d.getText()).doubleValue();
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

  //revalidate components
  standardNodeList_d.revalidate();
  standardNodeListPane_d.revalidate();
  clientsList_d.revalidate();
  clientsListPane_d.revalidate();
  suppliersList_d.revalidate();
  suppliersListPane_d.revalidate();
  centralList_d.revalidate();
  centralListPane_d.revalidate();
}

/**
 * Moves an item from the library nodes list to the normal nodes list in the
 * Library modules pane
 */
void
receiveLibFromClientsButton_d_actionPerformed(ActionEvent e)
{
  if (librariesList_d.getSelectedIndex() != -1) {
    String element = (String)librariesListModel_d.elementAt(librariesList_d.getSelectedIndex());
    librariesList_d.setSelectedIndex(-1);
    standardNodeListLib_d.setSelectedIndex(-1);
    standardNodeListModel_d.addElement(element);
    librariesListModel_d.removeElement(element);
    standardNodeListLib_d.revalidate();
    standardNodeListPaneLib_d.revalidate();
    librariesList_d.revalidate();
    librariesListPane_d.revalidate();
  }
}

/**
 * Moves an item from the normal nodes list to the library nodes list in the
 * Library modules pane
 */
void
sendLibToClientsButton_d_actionPerformed(ActionEvent e)
{
  if (standardNodeListLib_d.getSelectedIndex() != -1) {
    String element = (String)standardNodeListModel_d.elementAt(standardNodeListLib_d.getSelectedIndex());
    librariesList_d.setSelectedIndex(-1);
    standardNodeListLib_d.setSelectedIndex(-1);
    librariesListModel_d.addElement(element);
    standardNodeListModel_d.removeElement(element);
    standardNodeListLib_d.revalidate();
    standardNodeListPaneLib_d.revalidate();
    librariesList_d.revalidate();
    librariesListPane_d.revalidate();
  }
}

/**
 * Automatically finds the library modules in the
 * current loaded graph based on what nodes have only incoming connections
 */
void
findLibraryNodesButton_d_actionPerformed(ActionEvent e)
{
  //check for input graph
  if (inputGraphFilename_d.getText() == null
            || inputGraphFilename_d.getText().equals("")
            || initialGraph_d == null) {

      JOptionPane.showMessageDialog(this,
              "Error: Missing input graph.", "Library Finder: Missing Parameter",
              JOptionPane.ERROR_MESSAGE);
      return;
  }

  //check for existent libraries in the libraries list
  if (librariesListModel_d.size() > 0) {
    int result = JOptionPane.showConfirmDialog(this,
                "This will clear the libraries\n you have already selected\n and start again.\n Are you sure?", "Cancel Automatic Calculation?",
                JOptionPane.YES_NO_OPTION);
    if (result == JOptionPane.NO_OPTION) {
      return;
    }

    //clear libraries list
    for (int i=0; i<librariesListModel_d.size(); ++i) {
      standardNodeListModel_d.addElement(librariesListModel_d.elementAt(i));
    }
    librariesListModel_d.removeAllElements();
    standardNodeList_d.revalidate();
    standardNodeListPane_d.revalidate();
    librariesList_d.revalidate();
    librariesListPane_d.revalidate();
  }

  librariesList_d.setSelectedIndex(-1);
  standardNodeList_d.setSelectedIndex(-1);

  Vector libraries = new Vector();
  Vector normal = new Vector();
  Node[] nodeList = initialGraph_d.getNodes();

  //find libraries
  for (int i=0; i<nodeList.length; ++i) {
    String nname = nodeList[i].getName();
    if ((nodeList[i].getDependencies() == null|| nodeList[i].getDependencies().length==0)
          && !usesModule(clientsListModel_d, nodeList[i].getName())
          && !usesModule(suppliersListModel_d, nodeList[i].getName())
          && !usesModule(centralListModel_d, nodeList[i].getName())) {
      standardNodeListModel_d.removeElement(nodeList[i].getName());
      librariesListModel_d.addElement(nodeList[i].getName());
    }
  }

  //revalidate components
  standardNodeList_d.revalidate();
  standardNodeListPane_d.revalidate();
  librariesList_d.revalidate();
  librariesListPane_d.revalidate();
}

/**
 * method used to check when a string object appears in a list
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
 * Acttion listner class for when the clustering algorithm is changed by the user
 */
void ClusteringAlgEF_actionPerformed(ActionEvent e) {
    String objFnCalc = (String)ClusteringAlgEF.getSelectedItem();
    (preferences_d.getObjectiveFunctionCalculatorFactory()).setCurrentCalculator(objFnCalc);

    setupClusteringOptions();
}

/**
 * Action listner when the clustering method list is altered.
 */
void clusteringMethodList_d_actionPerformed(ActionEvent e) {
}

/**
 * Used to initialize the clustering options
 */
void setupClusteringOptions()
{
  String objFnCalc = (String)ClusteringAlgEF.getSelectedItem();

  if (clusteringMethod_d != null)
  {
    if (clusteringMethod_d instanceof GenericHillClimbingClusteringMethod)
       if (objFnCalc.equals("Turbo MQ Function"))
       {
          HillClimbingConfiguration hcc = (HillClimbingConfiguration)clusteringMethod_d.getConfiguration();
          hcc.setNumOfIterations(1);
          hcc.setThreshold(1.0);
          ((GenericHillClimbingClusteringMethod)clusteringMethod_d).setConfiguration(hcc);
       }
       else
       {
          clusteringMethod_d.setDefaultConfiguration();
       }

    configuration_d = clusteringMethod_d.getConfiguration();
  }
}

/**
 * Action listner for when the consolidate drifters feature is selected
 */
void consolidateDriftersCB_actionPerformed(ActionEvent e) {
}

/**
 * Action listener for when the contents of the delimiters entry field is
 * altered
 */
void delimEF_actionPerformed(ActionEvent e) {
}

/**
 * Action listner to process the "use space" delimiter checkbox
 */
void spaceDelimCB_actionPerformed(ActionEvent e) {
}

/**
 * This method is used to qurey the name server for the active bunch servers.  It
 * will be used to populate a listbox of available bunch servers
 */
void queryNS_actionPerformed(ActionEvent e) {
  try
  {
    DefaultListModel svrLM = new DefaultListModel();
    serverList.setModel(svrLM);

    Properties env = new Properties ();

    env.put("java.naming.factory.initial","com.sun.jndi.cosnaming.CNCtxFactory");

    String namingURL = "iiop://"+nameServerEF.getText()+":"+portEF.getText();
    env.put("java.naming.provider.url",namingURL);

    /**
     * Get the initial context based on the provided parametes.  This activity
     * calls the name server
     */
    InitialContext context = new InitialContext (env);

    /**
     * Obtain the list of server bindings
     */
    NamingEnumeration ne = context.listBindings(nameSpaceEF.getText());

    serverVector = new Vector();
    serverVector.removeAllElements();

    /**
     * Populate the server listbox
     */
    while(ne.hasMoreElements())
    {
      Binding b = (Binding)ne.next();
      serverVector.addElement(b);
      svrLM.addElement(b.getName());
    }

    /**
     * Preselect all of the servers.  The user can manually unselect them.
     */
    int sz = svrLM.size();
    if (sz > 0)
    {
      int [] selectAll = new int[sz];
      for(int z = 0; z < sz; z++)
        selectAll[z] = z;

      serverList.setSelectedIndices(selectAll);
    }
  }
  /**
   * Handle a name server exception.
   */
  catch(Exception excpt)
  {
    String msg =  bunch.util.BunchUtilities.DelimitString(excpt.toString(),25);
    JOptionPane.showMessageDialog(this,
        msg, "Naming Server Exception",
        JOptionPane.ERROR_MESSAGE);
  }
}

/**
 * Create the callback object for the distributed client.
 */
private void CreateCallbackObj()
{
  try
  {
    svrCallback = new CallbackImpl();
  }
  catch(Exception excpt)
  {
    String msg = excpt.toString()+"\n\n\n\n";

    JOptionPane.showMessageDialog(this,
        msg, "Error Creating Callback Object",
        JOptionPane.ERROR_MESSAGE);
  }
}

/**
 * This method is a utility to report an exception to the user
 */
public void ReportException(String title, Exception excpt)
{
         String msg = excpt.toString()+"\n\n\n\n";

         JOptionPane.showMessageDialog(this,
              msg, title,
              JOptionPane.ERROR_MESSAGE);
}

/**
 * This method registers the distributed serves with the Bunch client.
 */
void includeDistSvrsPB_actionPerformed(ActionEvent e) {
  CreateCallbackObj();
  int [] idx = serverList.getSelectedIndices();

  DefaultListModel svrLM = (DefaultListModel)serverList.getModel();

  /**
   * Reset the active server vector
   */
  if(activeServerVector != null)
    activeServerVector.removeAllElements();
  else
    activeServerVector = new Vector();

  /**
   * Deactivate all servers, this is useful if there are already servers
   * activated from previous distributed clustering runs
   */
  deactivateAllServers();

  /**
   * Activate the servers specified by the user.
   */
  for(int i = 0; i < idx.length; i++)
  {
    String  lbMsg = (String)svrLM.elementAt(idx[i]);
    lbMsg = "SELECTED--> " + lbMsg;
    svrLM.setElementAt(lbMsg,idx[i]);
    Binding b = (Binding)serverVector.elementAt(idx[i]);
    activeServerVector.addElement(b);
    BunchSvrMsg bsm = (BunchSvrMsg)b.getObject();

    /**
     * Try to activate the selected server, handle the exception if the
     * activation attempt fails.
     */
    try{
      bsm.registerCallback((Callback)svrCallback);
    }catch (Exception excpt)
    { ReportException("Error Registering Callback",excpt); }
  }

  /**
   * Now that ther servers are activated, enable the deactivate button.
   */
  if(activeServerVector.size()>0)
    deactivatePB.setEnabled(true);
  else
    deactivatePB.setEnabled(false);

  serverList.clearSelection();

  /**
   * Setup the initial unit of work for the servers.
   */
  Integer uowSz = new Integer(UOWSzEF.getText());
  svrCallback.baseUOWSz = uowSz.intValue();
}

/**
 * This method deactivates all of the active servers
 */
private void deactivateAllServers()
{
  /**
   * Get the list of active servers
   */
  DefaultListModel svrLM = (DefaultListModel)serverList.getModel();

  /**
   * Remove all servers from the active server list.
   */
  if(activeServerVector != null)
    activeServerVector.removeAllElements();

  svrLM.clear();

  /**
   * Add the previously active servers to the list of servers that can
   * be activiated.
   */
  for(int i = 0; i < serverVector.size(); i++)
  {
    Binding b = (Binding)serverVector.elementAt(i);
    svrLM.addElement(b.getName());
  }

  /**
   * Setup the UI to allow for servers to be reactivated.
   */
  deactivatePB.setEnabled(false);
  serverList.clearSelection();
}

/**
 * This action listner class is invoked when the deactivate server
 * pushbutton is pressed
 */
void deactivatePB_actionPerformed(ActionEvent e) {
  deactivateAllServers();
}

/**
 * This method is an internal debugging routine that is called when the
 * server list changes
 */
void serverList_valueChanged(ListSelectionEvent e) {
    //System.out.println("Server List Value changed");
}

/**
 * This method processes a mouse click event on the distributed server list
 */
void serverList_mouseClicked(MouseEvent e)
{
  if(serverList.isSelectionEmpty())
  {
    includeDistSvrsPB.setEnabled(false);
    deactivatePB.setEnabled(false);
  }
  else
    includeDistSvrsPB.setEnabled(true);
}

/**
 * This method is invoked when the distributed clustering feature is enabled.
 * This method then enables the adaptive unit of work feature of distributed
 * Bunch.
 */
void distClustEnableCB_stateChanged(ChangeEvent e) {
  if(distClustEnableCB.isSelected()==true)
    adaptiveEnableCB.setEnabled(true);
  else
    adaptiveEnableCB.setEnabled(false);
}

/**
 * This method is invoked when the timeout enable checkbox is pressed.  The action
 * associated with this event is to enable the entry field that collects the maximum
 * number of miliseconds to execute before terminating the clustering process.
 */
void timeoutEnable_actionPerformed(ActionEvent e) {
  boolean state = timeoutEnable.isSelected();
  if(state == true)
  {
    maxRuntimeEF.setEnabled(true);
    getTimoutTime();
  }
  else
    maxRuntimeEF.setEnabled(false);
}

/**
 * This method is used to obtain a boolean flag indicating if the runtime is
 * constrained.
 *
 * @returns True if the timeout enable checkbox is selected, false otherwise
 */
public boolean limitRuntime()
{
  return timeoutEnable.isSelected();
}

/**
 * This method returns the amount of time to run prior to terminating the
 * clustering process.  This value is only useful if the limitRuntime() method
 * returns true.
 *
 * @returns The maximum about of runtime allowed to the clustering process.
 */
public long getTimoutTime()
{
  try
  {
    Long to = new Long(maxRuntimeEF.getText());
    return to.longValue();
  }
  catch(Exception ex)
  {
    ReportException("Error Getting TimeoutValue",ex);
  }
  return 0;
}

/**
 * This method is called in response to the usser selecting the MQ calucalator
 * utility.  It displays the MQ Calculator dialog box.
 */
void menuMQCalc_actionPerformed(ActionEvent e)
{
    MQCalculatorUtil mqCalcUtil = new
        MQCalculatorUtil(this,"MQ Calculator Utilility",true);

    Dimension dlgSize = mqCalcUtil.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    mqCalcUtil.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    mqCalcUtil.setVisible(true);
}

/**
 * This method is called when the user selects the measurment caluclator
 * menu option to display a dialog box of available calculators.
 */
void utilityMeasurementCalc_actionPerformed(ActionEvent e)
{
  bunch.util.MeasurementUtil CalcUtil = new
  bunch.util.MeasurementUtil(this,"Calculator Utilility",true);

  Dimension dlgSize = CalcUtil.getPreferredSize();
  Dimension frmSize = getSize();
  Point loc = getLocation();
  CalcUtil.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  CalcUtil.setVisible(true);
}

/**
 * This method is called when the user changes the clustering option type
 * from user-directed to agglomerative.
 */
void actionList_d_actionPerformed(ActionEvent e)
{
  String cmd = (String)actionList_d.getSelectedItem();

  /**
   * Basically we are controlling if the graph is a tree of graphs (agglomerative),
   * or a single graph (user-directed)
   */
  if(cmd.equals("Agglomerative Clustering"))
  {
    if(initialGraph_d != null)
      initialGraph_d.setIsClusterTree(true);
    nextLevelGraphButton_d.setEnabled(false);
    agglomOutputCB.setSelectedItem("Output Median Level");
  }
  else
  {
    if(initialGraph_d != null)
      initialGraph_d.setIsClusterTree(false);
    nextLevelGraphButton_d.setEnabled(true);
    agglomOutputCB.setSelectedItem("Output Top Level");
  }
}

/**
 * This method is called when the output checkbox for agglomerative
 * clustering is selected.  Currently, this function has been
 * depricated.
 */
void agglomOutputCB_actionPerformed(ActionEvent e) {
}

/**
 * This method controls the type of output to be generated after the clustering
 * process executes.  It controls the level and if a "tree" format (i.e., the
 * nested clusters) will be generated in the output format.
 */
void outputFileFormatList_d_actionPerformed(ActionEvent e)
{
  String choice = (String)outputFileFormatList_d.getSelectedItem();
  if(choice.equals("Text Tree"))
    agglomOutputCB.setSelectedItem("Output Top Level");
  else
  {
    String cmd = (String)actionList_d.getSelectedItem();

    if(cmd.equals("Agglomerative Clustering"))
      agglomOutputCB.setSelectedItem("Output Median Level");
    else
      agglomOutputCB.setSelectedItem("Output Top Level");
  }
}

/**
 * This method is executed when the user indicates that they want to clear the
 * specified cluster file.  This is a feature of user-directed clustering.
 */
void ClearClusterFile_actionPerformed(ActionEvent e)
{
  inputClusterFile_d.setText("");
  initialGraph_d.resetNodeLocks();
  lockClustersCheckbox_d.setEnabled(false);
}

/**
 * By default the distributed tab is hidden.  If the user uses the
 * show distributed tab menu item then the tab is displayed, and the menu
 * item changes to hide the distributed tab.
 */
void menuShowDistributedTab_actionPerformed(ActionEvent e)
{
  int idx = mainTabbedPane_d.indexOfComponent(distPane);
  if(idx == -1)
  {
    mainTabbedPane_d.add(distPane, "Distributed Clustering",2);
    menuShowDistributedTab.setText("Hide Distributed Tab");
  }
  else
  {
    mainTabbedPane_d.remove(distPane);
    menuShowDistributedTab.setText("Show Distributed Tab");
  }
}

/**
 * This method is invoked when the user hits the clustering utils menu item.
 * The action is to display a dialog box that contains various bunch utilities.
 */
void clusteringUtilsMenu_actionPerformed(ActionEvent e)
{
  bunch.util.BunchClusteringUtil BunchUtil = new
  bunch.util.BunchClusteringUtil(this,"Bunch Utilility",true);

  Dimension dlgSize = BunchUtil.getPreferredSize();
  Dimension frmSize = getSize();
  Point loc = getLocation();
  BunchUtil.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  BunchUtil.setVisible(true);
}

  void fileUtilsMenu_actionPerformed(ActionEvent e) {

  bunch.util.BunchFileUtil BunchUtil = new
  bunch.util.BunchFileUtil(this,"Bunch File Utilities",true);

  Dimension dlgSize = BunchUtil.getPreferredSize();
  Dimension frmSize = getSize();
  Point loc = getLocation();
  BunchUtil.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
  BunchUtil.setVisible(true);

  }

}
//*******************************************
//END OF THE BUNCHFRAME CLASS
//*******************************************


/**
 * An inner class for handling the Exit menu option
 */
class BunchFrame_menuFileExit_ActionAdapter implements ActionListener{
  BunchFrame adaptee;

  BunchFrame_menuFileExit_ActionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.fileExit_actionPerformed(e);
  }
}

/**
 * An inner class for handling the About menu option
 */
class BunchFrame_menuHelpAbout_ActionAdapter implements ActionListener{
  BunchFrame adaptee;

  BunchFrame_menuHelpAbout_ActionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.helpAbout_actionPerformed(e);
  }
}


/**
 * An inner class for handling the MDG selection file option.
 */
class BunchFrame_selectGraphFileButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_selectGraphFileButton_d_actionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.selectGraphFileButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the select output file button
 */
class BunchFrame_selectOutputFileButton_d_actionAdapter
  implements java.awt.event.ActionListener{
  BunchFrame adaptee;

  BunchFrame_selectOutputFileButton_d_actionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.selectOutputFileButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the configure options button.  A dialog will be
 * displayed that is custom to the clustering algorithm. This inner class simply
 * dispatches the request to the approperiate handler.
 */
class BunchFrame_configureOptionsMenuItem_d_actionAdapter
  implements java.awt.event.ActionListener{
  BunchFrame adaptee;

  BunchFrame_configureOptionsMenuItem_d_actionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.configureOptionsMenuItem_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the run button.
 */
class BunchFrame_runActionButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_runActionButton_d_actionAdapter(BunchFrame adaptee)
  {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e)
  {
    adaptee.runActionButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the slecection of the input cluster file.  This
 * is used for user directed clustering.
 */
class BunchFrame_inputClusterFileSelectButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_inputClusterFileSelectButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.inputClusterFileSelectButton_d_actionPerformed(e);
  }
}


/**
 * An inner class for handling the slecection of the clustering options
 */
class BunchFrame_clusteringOptionsButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_clusteringOptionsButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.clusteringOptionsButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the slecection of the clustering method.
 */
class BunchFrame_clusteringMethodList_d_itemAdapter
  implements java.awt.event.ItemListener{

  BunchFrame adaptee;

  BunchFrame_clusteringMethodList_d_itemAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.clusteringMethodList_d_itemStateChanged(e);
  }
}

/**
 * An inner class for handling the processing of the output last level
 * button.
 */
class BunchFrame_outputLastButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_outputLastButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.outputLastButton_d_actionPerformed(e);
  }
}


/**
 * An inner class for handling the generation of the next level graph.
 */
class BunchFrame_nextLevelGraphButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_nextLevelGraphButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.nextLevelGraphButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the automatic determination of the omnipresent
 * client and suppliers.
 */
class BunchFrame_receiveFromSuppliersButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_receiveFromSuppliersButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.receiveFromSuppliersButton_d_actionPerformed(e);
  }
}


/**
 * An inner class for handling the manual selection of omnipresent suppliers.
 */
class BunchFrame_sendToSuppliersButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_sendToSuppliersButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.sendToSuppliersButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual selection of omnipresent clients.
 */
class BunchFrame_sendToClientsButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_sendToClientsButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.sendToClientsButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual deselection of omnipresent clients.
 */
class BunchFrame_receiveFromClientsButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_receiveFromClientsButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.receiveFromClientsButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the determination of omnipresent nodes in the
 * MDG.
 */
class BunchFrame_findOmnipresentNodesButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_findOmnipresentNodesButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.findOmnipresentNodesButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual selection of library modules.
 */
class BunchFrame_receiveLibFromClientsButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_receiveLibFromClientsButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.receiveLibFromClientsButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual selection of omnipresent clients
 * and suppliers.
 */
class BunchFrame_sendToCentralButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_sendToCentralButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.sendToCentralButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual deselection of omnipresent clients
 * and suppliers.
 */
class BunchFrame_receiveFromCentralButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_receiveFromCentralButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.receiveFromCentralButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the manual selection of librarys.
 */
class BunchFrame_sendLibToClientsButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_sendLibToClientsButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.sendLibToClientsButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the automatic detection of library modules.
 */
class BunchFrame_findLibraryNodesButton_d_actionAdapter
  implements java.awt.event.ActionListener{

  BunchFrame adaptee;

  BunchFrame_findLibraryNodesButton_d_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.findLibraryNodesButton_d_actionPerformed(e);
  }
}

/**
 * An inner class for handling the event to query the name server.
 */
class BunchFrame_queryNS_actionAdapter
  implements java.awt.event.ActionListener {

  BunchFrame adaptee;

  BunchFrame_queryNS_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.queryNS_actionPerformed(e);
  }
}

/**
 * An inner class for handling the user selection of the clustering utilities
 * menu.
 */
class BunchFrame_clusteringUtilsMenu_actionAdapter
  implements java.awt.event.ActionListener {

  BunchFrame adaptee;

  BunchFrame_clusteringUtilsMenu_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.clusteringUtilsMenu_actionPerformed(e);
  }
}

class BunchFrame_fileUtilsMenu_actionAdapter implements java.awt.event.ActionListener {
  BunchFrame adaptee;

  BunchFrame_fileUtilsMenu_actionAdapter(BunchFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.fileUtilsMenu_actionPerformed(e);
  }
}




