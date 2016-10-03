package bunch.api;

/****
 *
 *	$Log: BunchMDGDependency.java,v $
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

public class BunchMDGDependency {

  String srcNode;
  String destNode;
  int    edgeW;
  String relType;

  public BunchMDGDependency() {
    srcNode = null;
    destNode = null;
    edgeW = 0;
    relType = null;
  }

  public BunchMDGDependency(String s, String d, int w, String r)
  {
    srcNode = s;
    destNode = d;
    edgeW = w;
    relType = r;
  }

  public BunchMDGDependency(String s, String d, int w)
  {
    srcNode = s;
    destNode = d;
    edgeW = w;
  }

  public BunchMDGDependency(String s, String d)
  {
    srcNode = s;
    destNode = d;
    edgeW = 1;
  }

  public void setSrcNode(String n)
  { srcNode = n;  }

  public String getSrcNode()
  { return srcNode; }

  public void setDestNode(String n)
  { destNode = n;  }

  public String getDestNode()
  { return destNode; }

  public void setRelType(String t)
  { relType = t;  }

  public String getRelType()
  { return relType; }

  public void setEdgeW(int w)
  { edgeW = w;  }

  public int getEdgeW()
  { return edgeW; }
}