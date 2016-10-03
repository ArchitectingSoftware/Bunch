/****
 *
 *	$Log: SASimpleTechnique.java,v $
 *	Revision 3.0  2002/02/03 18:41:55  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.4  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.3  2000/08/14 18:33:25  bsmitc
 *	Fixed bug where the SA configuration information was not being saved
 *	bewteen runs of Bunch
 *
 *	Revision 3.2  2000/08/13 18:40:06  bsmitc
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

public class SASimpleTechnique
extends SATechnique{

  //SA Function = deltaMQ / T
  //T(k+1) = T(k)*alpha;

  public static final String SET_INITIAL_TEMP_KEY = "InitialTemp";
  public static final String SET_ALPHA_KEY = "Alpha";
  public static final String SET_DELTA_MQ  = "DeltaMQ";

  double  configuredTemp = 1.0;
  double  configuredAlpha = 0.85;

  double  initTemp = 1.0;
  double  alpha    = 0.85;
  double  currTemp = initTemp;
  boolean initialized = true;

  public SASimpleTechnique() {
  }

  public Hashtable getConfig()
  {
    Double Alpha = new Double(alpha);
    Double Temp  = new Double(initTemp);
    Hashtable h = new Hashtable();
    h.clear();
    h.put(SET_INITIAL_TEMP_KEY,Temp);
    h.put(SET_ALPHA_KEY,Alpha);
    return h;
  }

  public boolean setConfig(Hashtable h)
  {
    Double Alpha = (Double)h.get(SET_ALPHA_KEY);
    Double Temp = (Double)h.get(SET_INITIAL_TEMP_KEY);

    if((Alpha == null) || (Temp == null))
    {
      initialized = false;
      System.out.println("setConfig() - Setting initialized to false");
      return false;
    }

    alpha = Alpha.doubleValue();
    initTemp = Temp.doubleValue();
    currTemp = initTemp;

    configuredTemp = initTemp;
    configuredAlpha = alpha;

    initialized=true;

    return true;
  }

  public String getConfigDialogName()
  { return "bunch.SASimpleTechniqueDialog"; }

  public boolean configureUsingDialog(java.awt.Frame parent)
  {
    boolean rc = false;

    SASimpleTechniqueDialog dlg = null;

    try {
      dlg = (SASimpleTechniqueDialog)java.beans.Beans.instantiate(null, getConfigDialogName());
      dlg.setModal(true);
      dlg.setTitle("Simulated Annealing Simple Technique Conifugration");
      dlg.setConfiguration(this.getConfig());
      //dlg.jbInit();
      //dlg.pack();
      //dlg.setSize(450, 350);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }


    java.awt.Dimension dlgSize = dlg.getPreferredSize();
    java.awt.Dimension frmSize = parent.getSize();
    java.awt.Point loc = parent.getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);

    dlg.setSATechnique(this);
    dlg.setVisible(true);

    return true;
  }

  public boolean configure()
  {
    return true;
  }

  public boolean init(Hashtable h)
  {
    Double dTemp = (Double)h.get(SET_INITIAL_TEMP_KEY);
    Double dAlpha = (Double)h.get(SET_ALPHA_KEY);

    if((dTemp == null) || (dAlpha == null))
    {
      initialized = false;
      System.out.println("init() - Setting initialized to false");
      return false;
    }

    initTemp = dTemp.doubleValue();
    alpha = dAlpha.doubleValue();
    currTemp = initTemp;

    configuredTemp = initTemp;
    configuredAlpha = alpha;

    initialized = true;
    return true;
  }

  public void reset()
  {
    stats = bunch.stats.StatsManager.getInstance();
    initTemp = configuredTemp;
    alpha = configuredAlpha;
    currTemp = initTemp;
  }

  public boolean changeTemp(Hashtable args)
  {
    if(initialized == false) return false;

    //System.out.println("Changing Temp");
    //T(k+1) = T(K) * alpha
    currTemp *= alpha;
    return true;
  }

  public boolean  accept(Hashtable args)
  {
    if (initialized == false) return false;

    Double deltaMQ = (Double)args.get(SET_DELTA_MQ);
    if(deltaMQ == null)
      return false;

    double  dMQ = deltaMQ.doubleValue();
    return accept(dMQ);
  }

  public boolean  accept(double dMQ)
  {
    if (initialized == false) return false;



    //if(dMQ > 0) return false;

    if (bunch.util.BunchUtilities.compareGreaterEqual(dMQ,0.0))
      return false;

    double  exponent = dMQ/currTemp;

    double prob = Math.exp(exponent);
    double rndProb = this.getNextRndNumber();

    boolean acceptMove = (rndProb < prob);

    //System.out.println("T="+currTemp+"  dMQ="+dMQ+"  prob="+prob+"  rndProp="+rndProb+" result="+(rndProb<prob));

    if(acceptMove)
      stats.incrSAOverrides();

    return acceptMove;
  }

  public static String getDescription()
  {
    return "P(accept) = exp(deltaMQ/T);  T(k+1)=alpha*T(k)";
  }

  public String getObjectDescription()
  {
    return this.getDescription();
  }

  public String getWellKnownName()
  { return "Simple Algorithm";  }

}