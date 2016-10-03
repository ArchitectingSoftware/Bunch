/****
 *
 *	$Log: HillClimbingClusteringConfigurationDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:51  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.3  2000/08/13 18:40:06  bsmitc
 *	Added support for SA framework
 *
 *	Revision 3.2  2000/08/12 22:16:10  bsmitc
 *	Added support for Simulated Annealing configuration for NAHC technique
 *
 *	Revision 3.1  2000/08/11 22:13:04  bsmitc
 *	Removed generation selection feature
 *
 *	Revision 3.0  2000/07/26 22:46:10  bsmitc
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
//import borland.jbcl.layout.GridBagConstraints2;
import java.awt.event.*;
import java.util.*;

/**
 * A configuration dialog for the Hill Climbing clustering
 * methods.
 *
 * @author Brian Mitchell
 *
 * @see bunch.ClusteringConfigurationDialog
 * @see bunch.HillClimbingClusteringMethod
 * @see bunch.NextAscentHillClimbingClusteringMethod
 * @see bunch.SteepestAscentHillClimbingClusteringMethod
 */
public
class HillClimbingClusteringConfigurationDialog
  extends ClusteringConfigurationDialog
{

GridBagLayout gridBagLayout2 = new GridBagLayout();
JLabel numGenlabel_d = new JLabel();
JTextField jTextField1 = new JTextField();
JLabel popSizelabel_d = new JLabel();
JTextField popSize_d = new JTextField();
JLabel thresholdLabel_d = new JLabel();
JTextField jTextField2 = new JTextField();


public
HillClimbingClusteringConfigurationDialog(Frame frame, String title, boolean modal)
{
  super(frame, title, modal);
}

public
HillClimbingClusteringConfigurationDialog()
{
  super();
}

/**
 * Creates the basic configuration window.  The idea is that this class is extended
 * to add additional UI controls for specific hill climbing clustering configruations
 */
public
void
jbInit()
  throws Exception
{
  numGenlabel_d.setText("Number Of Generations:");
  popSizelabel_d.setText("Population Size:");
  thresholdLabel_d.setText("Cutoff Threshold:");
  optionsPanel_d.setLayout(gridBagLayout2);

  jTextField1.setText(Integer.toString(configuration_d.getNumOfIterations()));
  popSize_d.setText(Integer.toString(configuration_d.getPopulationSize()));
  jTextField2.setText(Double.toString(((HillClimbingConfiguration)configuration_d).getThreshold()));

  optionsPanel_d.add(numGenlabel_d, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(jTextField1, new GridBagConstraints2(1, 0, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(popSizelabel_d, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(popSize_d, new GridBagConstraints2(1, 1, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  //optionsPanel_d.add(thresholdLabel_d, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
  //          ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  //optionsPanel_d.add(jTextField2, new GridBagConstraints2(1, 2, 1, 1, 0.4, 0.0
  //          ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

  //HACK fix later
  numGenlabel_d.setVisible(false);
  jTextField1.setVisible(false);

  super.jbInit();
}

/**
 * Creates a configuration based on the data entered on the dialog and
 * returns it.
 *
 * @return the created configuration
 */
protected
Configuration
createConfiguration()
{
  configuration_d.setNumOfIterations(Integer.parseInt(jTextField1.getText()));
  configuration_d.setPopulationSize(Integer.parseInt(popSize_d.getText()));
  ((HillClimbingConfiguration)configuration_d).setThreshold(Double.valueOf(jTextField2.getText()).doubleValue());
  return configuration_d;
}
}
