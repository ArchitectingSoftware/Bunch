/****
 *
 *	$Log: NAHCConfiguration.java,v $
 *	Revision 3.1  2002/02/03 18:56:15  bsmitc
 *	Updated documentation
 *	
 *	Revision 3.0  2002/02/03 18:41:53  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *
 *	Revision 3.3  2000/08/19 00:44:39  bsmitc
 *	Added support for configuring the amount of randomization performed when
 *	the user adjusts the "slider" feature of NAHC.
 *
 *	Revision 3.2  2000/08/15 02:52:18  bsmitc
 *	Implemented adjustable NAHC feature.  This feature allows the user to set
 *	a minimum search threshold so that NAHC will not just take the first thing
 *	that it finds.
 *
 *	Revision 3.1  2000/08/13 18:40:06  bsmitc
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

/**
 * A basic class to hold all of the configuration information for the NAHC
 * clustering algorithm.  This class inherits all configuration information
 * from the basic hill climbing class.
 *
 * @author Brian Mitchell
 *
 */
public class NAHCConfiguration
extends HillClimbingConfiguration{

  SATechnique saTechnique = null;
  int         minPctToConsider = 0;
  int         randomizePct = 0;

  public NAHCConfiguration() {
  }

  public int getRandomizePct()
  { return randomizePct;  }

  public void setRandomizePct(int pct)
  { randomizePct = pct; }

  public void setSATechnique(SATechnique t)
  { saTechnique = t; }

  public SATechnique getSATechnique()
  { return saTechnique; }

  public int getMinPctToConsider()
  { return minPctToConsider;  }

  public void setMinPctToConsider(int pct)
  { minPctToConsider = pct; }
}