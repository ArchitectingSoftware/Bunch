/****
 *
 *	$Log: SATechnique.java,v $
 *	Revision 3.0  2002/02/03 18:41:55  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.3  2000/08/14 18:33:26  bsmitc
 *	Fixed bug where the SA configuration information was not being saved
 *	bewteen runs of Bunch
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

import java.util.*;

public abstract class SATechnique {

  protected Hashtable SAargs = new Hashtable();
  protected Random    rndNum = new Random();
  protected bunch.stats.StatsManager stats = bunch.stats.StatsManager.getInstance();

  public SATechnique() {
    rndNum.setSeed(System.currentTimeMillis());
  }

  public abstract boolean init(Hashtable h);

  public abstract String  getConfigDialogName();

  public abstract boolean configure();

  public abstract boolean changeTemp(Hashtable h);

  public boolean configureUsingDialog(java.awt.Frame parent)
  { return false; }

  public boolean  accept()
  { return false; }

  public boolean  accept(Hashtable args)
  { return false; }

  public boolean accept(double dMQ)
  { return false; }

  public Hashtable getConfig()
  { return null;  }

  public boolean setConfig(Hashtable h)
  { return false; }

  public double   getNextRndNumber()
  {
    return rndNum.nextDouble();
  }

  public void reset()
  {}

  public static String getDescription()
  { return "";  }

  public String getObjectDescription()
  { return this.getDescription(); }

  public abstract String getWellKnownName();
}