/****
 *
 *	$Log: SASimpleTechniqueDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:55  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/08/13 18:40:07  bsmitc
 *	Added support for SA framework
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
import java.awt.event.*;
import java.util.*;

public class SASimpleTechniqueDialog extends JDialog {
  JPanel panel1 = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel jLabel1 = new JLabel();
  JTextField initialTempEF = new JTextField();
  JLabel jLabel2 = new JLabel();
  JTextField alphaEF = new JTextField();
  JLabel DescriptionST = new JLabel();
  JPanel jPanel1 = new JPanel();
  JButton okPB = new JButton();
  JButton cancelPB = new JButton();

  SASimpleTechnique saTechnique = null;

  public SASimpleTechniqueDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setSATechnique(SASimpleTechnique s)
  { saTechnique = s;  }

  public SASimpleTechniqueDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    panel1.setLayout(gridBagLayout1);
    jLabel1.setText("Initial Temp. T(0):");
    jLabel2.setText("Alpha:");
    DescriptionST.setForeground(Color.red);
    DescriptionST.setText("Description:");
    okPB.setText("OK");
    okPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        okPB_actionPerformed(e);
      }
    });
    cancelPB.setText("Cancel");
    cancelPB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(ActionEvent e) {
        cancelPB_actionPerformed(e);
      }
    });
    getContentPane().add(panel1, BorderLayout.CENTER);
    panel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
    panel1.add(initialTempEF, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 155, 0));
    panel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 10, 0, 10), 0, 0));
    panel1.add(alphaEF, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
    panel1.add(DescriptionST, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(okPB, null);
    jPanel1.add(cancelPB, null);

    DescriptionST.setText("Description Goes Here");
  }

  public void setConfiguration(Hashtable h)
  {
    Double alpha = (Double)h.get(SASimpleTechnique.SET_ALPHA_KEY);
    Double temp =  (Double)h.get(SASimpleTechnique.SET_INITIAL_TEMP_KEY);
    DescriptionST.setText(SASimpleTechnique.getDescription());

    if((alpha==null)||(temp==null))
      return;

    alphaEF.setText(alpha.toString());
    initialTempEF.setText(temp.toString());
  }

  public Hashtable getConfiguration()
  {
    Hashtable h = new Hashtable();
    h.clear();
    h.put(SASimpleTechnique.SET_ALPHA_KEY,new Double(alphaEF.getText()));
    h.put(SASimpleTechnique.SET_INITIAL_TEMP_KEY, new Double(initialTempEF.getText()));
    return h;
  }

  void okPB_actionPerformed(ActionEvent e) {
    setVisible(false);
    if(saTechnique != null)
    {
      Hashtable h = getConfiguration();
      saTechnique.setConfig(h);
    }
    this.dispose();
  }

  void cancelPB_actionPerformed(ActionEvent e) {
    this.dispose();
  }
}