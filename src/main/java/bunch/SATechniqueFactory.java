/****
 *
 *	$Log: SATechniqueFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:55  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/08/13 18:40:07  bsmitc
 *	Added support for SA framework
 *	
 *	Revision 3.1  2000/08/12 22:58:26  bsmitc
 *	Adding Simulated Annealing Support To Project
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

public class SATechniqueFactory
extends GenericFactory{

  String  defaultFactoryItem = "Simple Algorithm";

  public SATechniqueFactory() {
      super();
      setFactoryType("SATechnique");
      addItem("Simple Algorithm", "bunch.SASimpleTechnique");
  }

  public String getDefaultTechnique()
  {
    return defaultFactoryItem;
  }

public
SATechnique
getMethod(String name)
{
  return (SATechnique)getItemInstance(name);
}
}