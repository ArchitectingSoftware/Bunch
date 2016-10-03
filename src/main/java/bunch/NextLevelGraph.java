/****
 *
 *	$Log: NextLevelGraph.java,v $
 *	Revision 3.0  2002/02/03 18:41:53  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.2  2000/08/11 13:19:11  bsmitc
 *	Added support for generating various output levels - all, median, one
 *	
 *	Revision 3.1  2000/08/09 14:17:48  bsmitc
 *	Changes made to support agglomerative clustering feature.
 *
 *	Revision 3.0  2000/07/26 22:46:10  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */

/**
 * Title:        Bunch Version 1.2 Base<p>
 * Description:  Your description<p>
 * Copyright:    Copyright (c) 1999<p>
 * Company:      <p>
 * @author Brian Mitchell
 * @version
 */
package bunch;

import java.util.*;

public class NextLevelGraph {

class NodeInfo
{
  public String name;
  public int    id;
  public Hashtable dependencies;
  public Hashtable backEdges;
  public Hashtable dWeights;
  public Hashtable beWeights;
  public Hashtable childNodes;
  public int[] arrayDependencies;
  public int[] arrayWeights;

  public
  NodeInfo(String n)
  {
    name = n;
    id = -1;
    dependencies = new Hashtable();
    dWeights = new Hashtable();
    backEdges = new Hashtable();
    beWeights = new Hashtable();
    childNodes = new Hashtable();
  }
}

  public NextLevelGraph() {
  }

  public Graph genNextLevelGraph(Graph g)
  {
    int    lvl          = g.getGraphLevel()+1;
    int[]  cnames       = g.getClusterNames();
    Node[] nodeList     = g.getNodes();
    Node[] newNodeList  = new Node[cnames.length];
    Hashtable cnameht   = new Hashtable();
    Hashtable clusterMap = new Hashtable();
    int       nodeLevel = g.getGraphLevel();

    int [] clusters = g.getClusters();
    for(int i = 0; i < nodeList.length; i++)
    {
      nodeList[i].cluster = clusters[i];
    }
    cnames = g.getClusterNames();

    for(int i = 0; i < cnames.length; i++)
    {
      String name = "clusterLvl"+lvl+"Num"+i;
      NodeInfo ni = new NodeInfo(name);
      ni.id = i;
      cnameht.put(new Integer(cnames[i]),ni);
    }


    for(int i = 0; i < nodeList.length; i++)
    {
        Node srcNode = nodeList[i];
//System.out.println("Node Name="+srcNode.getName());

        int[] edges = srcNode.getDependencies();
        int[] weights = srcNode.getWeights();

        clusterMap.put(new Integer(srcNode.nodeID),new Integer(srcNode.cluster));

        NodeInfo niTmp =  (NodeInfo)cnameht.get(new Integer(srcNode.cluster));
        niTmp.childNodes.put(new Integer(srcNode.nodeID),srcNode);

        for(int j = 0; j < edges.length; j++)
        {
          Node destNode = nodeList[edges[j]];
          int srcCluster = srcNode.cluster;
          int destCluster = destNode.cluster;

          if(srcCluster == destCluster) continue;


          int weight = weights[j];

          NodeInfo niSrc  = (NodeInfo)cnameht.get(new Integer(srcCluster));
          NodeInfo niDest = (NodeInfo)cnameht.get(new Integer(destCluster));

          Integer src = new Integer(niSrc.id);
          Integer dest= new Integer(niDest.id);
          Integer w   = new Integer(weight);

          if(niSrc.dependencies.get(dest) == null)
          {
            niSrc.dependencies.put(dest, dest);
            niSrc.dWeights.put(dest,w);
          }
          else
          {
            Integer edgeW = (Integer)niSrc.dWeights.get(dest);
            w = new Integer(w.intValue()+edgeW.intValue());
            niSrc.dWeights.put(dest,w);
          }

          if(niDest.backEdges.get(src) == null)
          {
            niDest.backEdges.put(src,src);
            niDest.beWeights.put(src,w);
          }
          else
          {
            Integer edgeW = (Integer)niDest.beWeights.get(src);
            w = new Integer(w.intValue()+edgeW.intValue());
            niDest.beWeights.put(src,w);
          }
       }
    }

    //Now build the new Bunch Structure
    Graph retGraph = new Graph(cnameht.size());
    retGraph.clear();
    Node[] newNL = retGraph.getNodes();

    Object [] nl = cnameht.values().toArray();
    for(int i = 0; i < nl.length; i++)
    {
      Node        n = new Node();
      NodeInfo    ni = (NodeInfo)nl[i];
      newNL[ni.id]=n;
      n.setName(ni.name);
      n.nodeID = ni.id;
      n.setIsCluster(true);
      n.nodeLevel = nodeLevel;
      n.children = new Node[ni.childNodes.size()];
      int numForwardDep = ni.dependencies.size();
      int numBackDep = ni.backEdges.size();
      n.dependencies = new int[numForwardDep];
      n.weights = new int[numForwardDep];
      n.backEdges = new int[numBackDep];
      n.beWeights = new int[numBackDep];

      int j = 0;
      for(Enumeration e = ni.childNodes.elements(); e.hasMoreElements();)
        n.children[j++]=(Node)e.nextElement();

      //Object [] oa = ni.childNodes.entrySet().toArray();
      //for(int j = 0; j < oa.length; j++)
      //  n.children[j] = (Node)oa[j];

      updateEdgeArrays(ni.dependencies,n.dependencies,ni.dWeights,n.weights);
      updateEdgeArrays(ni.backEdges,n.backEdges,ni.beWeights,n.beWeights);

      //Uncomment the following line for debug
      //dumpNode(n);
    }

    retGraph.setPreviousLevelGraph(g);
    retGraph.setGraphLevel(g.getGraphLevel()+1);
    retGraph.setIsClusterTree(g.isClusterTree());

    return retGraph;
  }

  private void dumpNode(Node n)
  {
    System.out.println("Node: " + n.name_d);
    System.out.println("Node ID:  " + n.nodeID);
    System.out.print("EDGES(Weight): ");
    for(int i = 0; i < n.dependencies.length; i++)
      System.out.print(n.dependencies[i]+"("+n.weights[i]+")");
    System.out.println();
    System.out.print("BACK EDGES(Weight): ");
    for(int i = 0; i < n.backEdges.length; i++)
      System.out.print(n.backEdges[i]+"("+n.beWeights[i]+")");
    System.out.println();
    System.out.print("Children: ");
    for(int i = 0; i < n.children.length; i++)
      System.out.print("["+n.children[i].name_d+"] ");
    System.out.println();
    System.out.println("======================================");
  }

  private void updateEdgeArrays(Hashtable edgeH, int[]edge, Hashtable weightH, int[]weight)
  {
    int [] tmpEdge = edge; //new int[edgeH.size()];
    int [] tmpWeight = weight; //new int[edgeH.size()];

    Object [] eo = edgeH.keySet().toArray();
    for(int i = 0; i < eo.length; i++)
    {
      Integer Ikey = (Integer)eo[i];
      Integer Iweight = (Integer)weightH.get(Ikey);
      int edgeTo = Ikey.intValue();
      int edgeWeight = Iweight.intValue();
      tmpEdge[i]=edgeTo;
      tmpWeight[i]=edgeWeight;
    }
    edge = tmpEdge;
    weight = tmpWeight;
  }
}

