/****
 *
 *	$Log: BSWindow.java,v $
 *	Revision 3.0  2002/02/03 18:42:05  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2001/04/03 21:39:40  bsmitc
 *	Readded and fixed support for distributed clustering
 *	
 *	Revision 3.0  2000/07/26 22:46:18  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch.BunchServer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import java.util.Properties;
import javax.naming.*;
import java.rmi.RMISecurityManager;

public class BSWindow extends JFrame {
  JMenuBar menuBar1 = new JMenuBar();
  JMenu menuFile = new JMenu();
  JMenuItem menuFileExit = new JMenuItem();
  JMenu menuHelp = new JMenu();
  JMenuItem menuHelpAbout = new JMenuItem();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField nsName = new JTextField();
  JLabel jLabel2 = new JLabel();
  JTextField svrName = new JTextField();
  JButton startPB = new JButton();
  JButton stopPB = new JButton();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JTextField nameServerEF = new JTextField();
  JTextField portEF = new JTextField();
  JLabel jLabel5 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea logText = new JTextArea();
  JButton clearLogPB = new JButton();
  JLabel jLabel6 = new JLabel();
  JLabel msgTxt = new JLabel();

  //-------------
  //REMOTE OBJECT
  //-------------
  BunchSvrMsgImpl bunchMsg;
  InitialContext   corbaContext=null;

  //Construct the frame
  public BSWindow() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {
    this.getContentPane().setLayout(gridBagLayout1);
    this.setSize(new Dimension(428, 385));
    this.setTitle("Bunch Server - v1.0B");
    menuFile.setText("File");
    menuFileExit.setText("Exit");
    menuFileExit.addActionListener(new ActionListener()  {

      public void actionPerformed(ActionEvent e) {
        fileExit_actionPerformed(e);
      }
    });
    menuHelp.setText("Help");
    menuHelpAbout.setText("About");
    menuHelpAbout.addActionListener(new ActionListener()  {

      public void actionPerformed(ActionEvent e) {
        helpAbout_actionPerformed(e);
      }
    });
    jLabel1.setText("Namespace:");
    nsName.setMaximumSize(new Dimension(80, 21));
    nsName.setMinimumSize(new Dimension(80, 21));
    nsName.setPreferredSize(new Dimension(80, 21));
    nsName.setText("BunchServer");
    jLabel2.setText("Server Name:");
    svrName.setMaximumSize(new Dimension(80, 21));
    svrName.setMinimumSize(new Dimension(80, 21));
    svrName.setPreferredSize(new Dimension(80, 21));
    svrName.setText("bServer1");
    startPB.setText("Start");
    startPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        startPB_actionPerformed(e);
      }
    });
    stopPB.setEnabled(false);
    stopPB.setText("Stop");
    stopPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        stopPB_actionPerformed(e);
      }
    });
    jLabel3.setText("Name Server:");
    jLabel4.setText("Port");
    portEF.setMaximumSize(new Dimension(80, 21));
    portEF.setMinimumSize(new Dimension(80, 21));
    portEF.setPreferredSize(new Dimension(80, 21));
    portEF.setText("900");
    portEF.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        portEF_actionPerformed(e);
      }
    });
    nameServerEF.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        nameServerEF_actionPerformed(e);
      }
    });
    nameServerEF.setMaximumSize(new Dimension(80, 21));
    nameServerEF.setMinimumSize(new Dimension(80, 21));
    nameServerEF.setPreferredSize(new Dimension(80, 21));
    nameServerEF.setText("localhost");
    jLabel5.setText("Log Messages:");
    clearLogPB.setText("Clear Log");
    clearLogPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        clearLogPB_actionPerformed(e);
      }
    });
    jLabel6.setText("Message:");
    msgTxt.setFont(new java.awt.Font("Dialog", 1, 12));
    msgTxt.setForeground(Color.red);
    msgTxt.setText("Server not running...");
    menuFile.add(menuFileExit);
    menuHelp.add(menuHelpAbout);
    menuBar1.add(menuFile);
    menuBar1.add(menuHelp);
    this.setJMenuBar(menuBar1);

  //---------------------

        svrName.setText(bunch.util.BunchUtilities.getLocalHostName());

  //-----------------
    this.getContentPane().add(nsName, new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 238, 0));
    this.getContentPane().add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 10, 0));
    this.getContentPane().add(svrName, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 238, 0));
    this.getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(stopPB, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(9, -20, 0, 0), 0, -5));
    this.getContentPane().add(startPB, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, -6));
    this.getContentPane().add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(nameServerEF, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 238, 0));
    this.getContentPane().add(portEF, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 238, 0));
    this.getContentPane().add(jLabel5, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(4, 0, 0, 0), 297, 90));
    this.getContentPane().add(clearLogPB, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 0, 0, 0), 0, -4));
    this.getContentPane().add(jLabel6, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    this.getContentPane().add(msgTxt, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    jScrollPane1.getViewport().add(logText, null);
  }

  //File | Exit action performed
  public void fileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  //Help | About action performed
  public void helpAbout_actionPerformed(ActionEvent e) {
    BSWindow_AboutBox dlg = new BSWindow_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }

  //Overridden so we can exit on System Close
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if(e.getID() == WindowEvent.WINDOW_CLOSING) {
      fileExit_actionPerformed(null);
    }
  }

  void nameServerEF_actionPerformed(ActionEvent e) {

  }

  void portEF_actionPerformed(ActionEvent e) {

  }

  public String getJndiName()
  {
    String jndiName = "/" + nsName.getText() + "/" + svrName.getText();
    return jndiName;
  }

  void startPB_actionPerformed(ActionEvent e) {
    String nameSpace = nsName.getText();
    String server = svrName.getText();
    String nameSvr = nameServerEF.getText();
    String port = portEF.getText();

    try
    {
    	Properties env = new Properties ();

		env.put("java.naming.factory.initial","com.sun.jndi.cosnaming.CNCtxFactory");

      String nsURL = "iiop://"+nameSvr+":"+port;
      newLogMsg("Name Server URL: "+nsURL);
      String cnStr = "/"+nameSpace+"/"+server;
      appendLogMsg("Object Registration Name: " + cnStr);

		env.put("java.naming.provider.url",nsURL);

		InitialContext context = new InitialContext (env);

      //-----------------------------------------------------
      //See if this is the first time binding to a namespace
      //-----------------------------------------------------
      try{
         context.createSubcontext(nameSpace);
      }catch(Exception e1)
      {}

      CompositeName cn = new CompositeName(cnStr);

      bunchMsg = new BunchSvrMsgImpl();
      bunchMsg.setParent(this);
      bunchMsg.setJndiName(this.getJndiName());
      bunchMsg.setGUIMode();

   	context.rebind (cn, bunchMsg);

      corbaContext = context;

      msgOK("SERVER Started OK!");

      startPB.setEnabled(false);
      stopPB.setEnabled(true);

      nsName.setEnabled(false);
      svrName.setEnabled(false);
      nameServerEF.setEnabled(false);
      portEF.setEnabled(false);

    }
    catch (Exception ex)
    {
      msgError("EXCEPTION:  Please see log message area!");
      String excp = ex.toString();
      appendLogMsg(excp);
      ex.printStackTrace();//throw(ex);
    }
  }

  void newLogMsg(String smsg)
  {
      logText.setText("");
      logText.setText(smsg);
  }

  void appendLogMsg(String smsg)
  {
      String s = logText.getText() + "\n" + smsg;
      logText.setText(s);
  }

  void msgOK(String msg)
  {
      msgTxt.setForeground(Color.green);
      msgTxt.setText(msg);
  }

  void msgError(String msg)
  {
      msgTxt.setForeground(Color.red);
      msgTxt.setText(msg);
  }

  void stopPB_actionPerformed(ActionEvent e) {
    try
    {
      String nameSpace = nsName.getText();

      try{
         corbaContext.destroySubcontext(nameSpace);
      }catch(Exception e1)
      {}

   	corbaContext.close();
      msgOK("SERVER Stopped OK!");
      newLogMsg("Server stopped...");
      startPB.setEnabled(true);
      stopPB.setEnabled(false);
      nsName.setEnabled(true);
      svrName.setEnabled(true);
      nameServerEF.setEnabled(true);
      portEF.setEnabled(true);
    }
    catch (Exception ex)
    {
      msgError("EXCEPTION:  Please see log message area!");
      String excp = ex.toString();
      newLogMsg(excp);
    }
  }

  void clearLogPB_actionPerformed(ActionEvent e) {
    logText.setText("");
  }
}
