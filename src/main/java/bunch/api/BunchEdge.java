 /****
 *
 *	$Log: BunchEdge.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/11/26 15:45:34  bsmitc
 *	Initial Version - support for the BunchGraph api interface
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
package bunch.api;

public class BunchEdge {

  int weight;
  BunchNode srcNode;
  BunchNode destNode;

  public BunchEdge(int w, BunchNode src, BunchNode dest) {
    weight = w;
    srcNode = src;
    destNode = dest;
  }

  public int getWeight()
  { return weight;  }

  public BunchNode getSrcNode()
  { return srcNode; }

  public BunchNode getDestNode()
  { return destNode;  }
}