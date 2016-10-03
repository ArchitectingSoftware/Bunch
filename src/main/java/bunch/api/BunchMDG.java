package bunch.api;

import java.util.*;

/****
 *
 *	$Log: BunchMDG.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *
 */

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author
 * @version 1.0
 */

public class BunchMDG {
ArrayList mdgEdges = null;

  public BunchMDG() {
    mdgEdges = new ArrayList();
  }

  public boolean addMDGEdges(Collection c)
  {
    return mdgEdges.addAll(c);
  }

  public boolean setMDGEdges(Collection c)
  {
    mdgEdges.clear();
    return addMDGEdges(c);
  }

  public boolean addMDGEdge(BunchMDGDependency d)
  {
    return mdgEdges.add(d);
  }

  public boolean addMDGEdge(String s, String d, int w, String r)
  {
    BunchMDGDependency bmd = new BunchMDGDependency(s,d,w,r);
    return addMDGEdge(bmd);
  }

  public boolean addMDGEdge(String s, String d, int w)
  {
    BunchMDGDependency bmd = new BunchMDGDependency(s,d,w);
    return addMDGEdge(bmd);
  }

  public boolean addMDGEdge(String s, String d)
  {
    BunchMDGDependency bmd = new BunchMDGDependency(s,d);
    return addMDGEdge(bmd);
  }

  public void clearMDG()
  {
    mdgEdges.clear();
  }

  public Collection getMDGEdges()
  {
    return mdgEdges;
  }
}