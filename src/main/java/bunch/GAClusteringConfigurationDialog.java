/****
 *
 *	$Log: GAClusteringConfigurationDialog.java,v $
 *	Revision 3.0  2002/02/03 18:41:48  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.0  2000/07/26 22:46:09  bsmitc
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
import java.awt.event.*;

/**
 * A configuration dialog for the Genetic Algorithm clustering
 * method.
 *
 * @author Brian Mitchell
 *
 * @see bunch.ClusteringConfigurationDialog
 * @see bunch.GAClusteringMethod
 */
public
class GAClusteringConfigurationDialog
  extends ClusteringConfigurationDialog
{

GridBagLayout gridBagLayout2 = new GridBagLayout();
JLabel numGenlabel_d = new JLabel();
JTextField jTextField1 = new JTextField();
JLabel popSizelabel_d = new JLabel();
JTextField popSize_d = new JTextField();
JLabel crossLabel_d = new JLabel();
JTextField jTextField2 = new JTextField();
JLabel mutLabel_d = new JLabel();
JTextField jTextField3 = new JTextField();
JComboBox methodList_d = new JComboBox();

public
GAClusteringConfigurationDialog(Frame frame, String title, boolean modal)
{
  super(frame, title, modal);
}

public
GAClusteringConfigurationDialog()
{
  super();
}

/**
 * Component initialization and layout
 */
public
void
jbInit()
  throws Exception
{
  numGenlabel_d.setText("Number Of Generations:");
  popSizelabel_d.setText("Population Size:");
  crossLabel_d.setText("Crossover Probability:");
  mutLabel_d.setText("Mutation Probability:");
  optionsPanel_d.setLayout(gridBagLayout2);

  jTextField1.setText(Integer.toString(configuration_d.getNumOfIterations()));
  popSize_d.setText(Integer.toString(configuration_d.getPopulationSize()));
  jTextField2.setText(Double.toString(((GAConfiguration)configuration_d).getCrossoverThreshold()));
  jTextField3.setText(Double.toString(((GAConfiguration)configuration_d).getMutationThreshold()));
  java.util.Enumeration e = ((GAConfiguration)configuration_d).getMethodFactory().getAvailableItems();
  while (e.hasMoreElements()) {
    methodList_d.addItem((String)e.nextElement());
  }

  optionsPanel_d.add(new JLabel("GA Selection Method:"), new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(methodList_d, new GridBagConstraints2(1, 0, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(numGenlabel_d, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(jTextField1, new GridBagConstraints2(1, 1, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(popSizelabel_d, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(popSize_d, new GridBagConstraints2(1, 2, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(crossLabel_d, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(jTextField2, new GridBagConstraints2(1, 3, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(mutLabel_d, new GridBagConstraints2(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  optionsPanel_d.add(jTextField3, new GridBagConstraints2(1, 4, 1, 1, 0.4, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

  //do the initialization of the superclass
  super.jbInit();
}

/**
 * Creates a configuration based on the data entered on the dialog and
 * returns it
 *
 * @return the created configuration
 */
protected
Configuration
createConfiguration()
{
  configuration_d.setNumOfIterations(Integer.parseInt(jTextField1.getText()));
  configuration_d.setPopulationSize(Integer.parseInt(popSize_d.getText()));
  ((GAConfiguration)configuration_d).setCrossoverThreshold(Double.valueOf(jTextField2.getText()).doubleValue());
  ((GAConfiguration)configuration_d).setMutationThreshold(Double.valueOf(jTextField3.getText()).doubleValue());
  ((GAConfiguration)configuration_d).setMethod((String)methodList_d.getSelectedItem());
  return configuration_d;
}
}
