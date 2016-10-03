/****
 *
 *	$Log: BunchFrame_AboutBox.java,v $
 *	Revision 3.0  2002/02/03 18:41:44  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *
 *	Revision 3.3  2001/04/03 23:32:12  bsmitc
 *	Added NAHC (really HC) support for Distributed Bunch, updated release
 *	version number to 3.2
 *
 *	Revision 3.2  2001/04/02 19:23:39  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.1  2000/07/26 23:27:55  bsmitc
 *	Changed about box to include updated copywrite date and CVS release tag
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The class that displays the "About" dialog
 *
 * @author Brian Mitchell
 */
public
class BunchFrame_AboutBox
  extends Dialog
  implements ActionListener{
JPanel panel1 = new JPanel();
JPanel panel2 = new JPanel();
JPanel insetsPanel1 = new JPanel();
JPanel insetsPanel2 = new JPanel();
JPanel insetsPanel3 = new JPanel();
JButton button1 = new JButton();
JLabel imageControl1 = new JLabel();
ImageIcon imageIcon;
JLabel label1 = new JLabel();
JLabel label2 = new JLabel();
JLabel label3 = new JLabel();
JLabel label4 = new JLabel();
BorderLayout borderLayout1 = new BorderLayout();
BorderLayout borderLayout2 = new BorderLayout();
FlowLayout flowLayout1 = new FlowLayout();
FlowLayout flowLayout2 = new FlowLayout();
GridLayout gridLayout1 = new GridLayout();
String product = "Bunch 2.0 - JDK 1.2 Edition";
String version = "";
String copyright = "Copyright (c) 1999";
String comments = "Bunch Version 2.0";
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel4 = new JLabel();

public
BunchFrame_AboutBox(Frame parent)
{
  super(parent);
  enableEvents(AWTEvent.WINDOW_EVENT_MASK);
  try {
    jbInit();
  }
  catch (Exception e) {
    e.printStackTrace();
  }

  //imageControl1.setIcon(imageIcon);
  pack();
}

private
void
jbInit()
  throws Exception
{
  //imageIcon = new ImageIcon(getClass().getResource("your image name goes here"));
  this.setTitle("About");
  setResizable(false);
  panel1.setLayout(borderLayout1);
  panel2.setLayout(borderLayout2);
  insetsPanel1.setLayout(flowLayout1);
  insetsPanel2.setLayout(flowLayout1);
  insetsPanel2.setBorder(new EmptyBorder(10, 10, 10, 10));
  gridLayout1.setRows(8);
  gridLayout1.setColumns(1);
  label1.setText("Bunch 4.0.1 - April 2016");
  label2.setText("Drexel University Software Engineering Group (SERG)");
  label3.setText("Copyright (c) 1997-2016");
  label4.setText("Bunch Edition 3D - JDK 1.8");
  insetsPanel3.setLayout(gridLayout1);
  insetsPanel3.setBorder(new EmptyBorder(10, 60, 10, 10));
  button1.setText("OK");
  button1.addActionListener(this);
    jLabel3.setText("For Help and Documentation Please Visit:");
    jLabel1.setText("CVS Release Tag:  REL3-3");
    jLabel4.setForeground(Color.blue);
    jLabel4.setText("http://serg.mcs.drexel.edu/bunch");
    insetsPanel2.add(imageControl1, null);
  panel2.add(insetsPanel2, BorderLayout.WEST);
  this.add(panel1, null);
  insetsPanel3.add(label1, null);
    insetsPanel3.add(label2, null);
    insetsPanel3.add(label3, null);
    insetsPanel3.add(label4, null);
    insetsPanel3.add(jLabel1, null);
    insetsPanel3.add(jLabel2, null);
    insetsPanel3.add(jLabel3, null);
    insetsPanel3.add(jLabel4, null);
  panel2.add(insetsPanel3, BorderLayout.CENTER);
  insetsPanel1.add(button1, null);
  panel1.add(insetsPanel1, BorderLayout.SOUTH);
  panel1.add(panel2, BorderLayout.NORTH);
}

protected
void
processWindowEvent(WindowEvent e)
{
  if (e.getID() == WindowEvent.WINDOW_CLOSING) {
    cancel();
  }
  super.processWindowEvent(e);
}

void
cancel()
{
  dispose();
}

public
void
actionPerformed(ActionEvent e)
{
  if (e.getSource() == button1) {
    cancel();
  }
}
}

