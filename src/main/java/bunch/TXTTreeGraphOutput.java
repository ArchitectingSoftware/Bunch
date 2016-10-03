/****
 *
 *	$Log: TXTTreeGraphOutput.java,v $
 *	Revision 3.0  2002/02/03 18:41:57  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.5  2000/08/18 21:08:01  bsmitc
 *	Added feature to support tree output for dotty and text
 *	
 *	Revision 3.4  2000/08/16 00:33:15  bsmitc
 *	Changed output subsystem name to be consistent with the DOT subsystem names
 *
 *	Revision 3.3  2000/08/11 13:19:11  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.2  2000/07/28 14:26:19  bsmitc
 *	Added support for the TXTTree Graph output option
 *
 *	Revision 3.1  2000/07/28 12:51:28  bsmitc
 *	Initial Version Added To Project
 *
 *	Revision 1.1.2.1  2000/07/28 12:30:46  bsmitc
 *	Added Class to Project
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

import java.io.*;
import java.util.Vector;

public class TXTTreeGraphOutput   extends GraphOutput {

  public TXTTreeGraphOutput() {
  }

  public void writeHeader(Graph gBase) throws IOException
{
    writer_d.write("// ------------------------------------------------------------ \n");
    writer_d.write("// created with bunch v2 \n");
    writer_d.write("// Objective Function value = "+gBase.getObjectiveFunctionValue()+"\n");
    writer_d.write("// ------------------------------------------------------------ \n\n");
}

public void writeSpecialModules(Node[] originalNodes) throws IOException
{
    if (originalNodes != null) {
      boolean hasSuppliers = false;
      boolean hasClients = false;
      boolean hasCentrals = false;
      boolean hasLibraries = false;

      for (int j=0; j<originalNodes.length; ++j) {
        if (!hasSuppliers && originalNodes[j].getType() == Node.SUPPLIER) {
          hasSuppliers = true;
        }
        if (!hasClients && originalNodes[j].getType() == Node.CLIENT) {
          hasClients = true;
        }
        if (!hasCentrals && originalNodes[j].getType() == Node.CENTRAL) {
          hasCentrals = true;
        }
        if (!hasLibraries && originalNodes[j].getType() == Node.LIBRARY) {
          hasLibraries = true;
        }
      }

      int count = 1;
      if (hasLibraries) {
        //create libraries cluster
        writer_d.write("SS(libraries) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.LIBRARY) {
            writer_d.write(originalNodes[i].getName());
            count++;
            if(count == originalNodes.length)
              writer_d.write("\n");
            else
              writer_d.write(", ");
          }
        }
      }


      count = 1;
      if (hasSuppliers) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_suppliers) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.SUPPLIER) {
            writer_d.write(originalNodes[i].getName());
            count++;
            if(count == originalNodes.length)
              writer_d.write("\n");
            else
              writer_d.write(", ");
          }
        }
      }

      count = 1;
      if (hasClients) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_clients) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CLIENT) {
            writer_d.write(originalNodes[i].getName());
            count++;
            if(count == originalNodes.length)
              writer_d.write("\n");
            else
              writer_d.write(", ");
          }
        }
      }


      count = 1;
      if (hasCentrals) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_centrals) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CENTRAL) {
            count++;
            if(count == originalNodes.length)
              writer_d.write("\n");
            else
              writer_d.write(", ");
          }
        }
      }
    }
}


public void writeClosing() throws IOException
{
    writer_d.close();
}

public void genCluster(Node n, long baseID) throws IOException
{
  Stack st = new Stack();
  Hashtable ht = new Hashtable();

  st.push(n);
  while(!st.empty())
  {
      Node tmp = (Node)st.peek();
      if(tmp.isCluster())
      {
        if(ht.containsKey(tmp.name_d))
        {
          writer_d.write("}\n\n");
          ht.remove(tmp.name_d);
          st.pop();
        }
        else
        {
            long clustID = tmp.nodeID+(baseID++);
            writer_d.write("subgraph cluster"+clustID+" {\n");
            writer_d.write("label = \""+tmp.name_d+"\";\n");
            writer_d.write("color = black;\n");
            writer_d.write("style = bold;\n\n");
            for(int j = 0; j < tmp.children.length;j++)
              st.push(tmp.children[j]);
            ht.put(tmp.name_d,tmp.name_d);
        }
      }
      else
      {
        writer_d.write("\""+tmp.getName()+"\"[shape=ellipse,color=lightblue,fontcolor=black,style=filled];\n");
        st.pop();
      }
  }
}

public void generateClusters(Node []na) throws IOException
{
  long base=1000;
  for(int i = 0; i<na.length;i++)
  {
    genCluster(na[i], base);
    base+=1000;
  }
}

public void echoNestedChildrenOLD(Node n) throws IOException
{
  Stack s = new Stack();
  boolean firstNode = true;
  s.push(n);
  while(!s.isEmpty())
  {
    Node tmpNode = (Node)s.pop();
    if(tmpNode.children==null)
      continue;
    for(int i = 0; i < tmpNode.children.length; i++)
    {
      Node childNode = tmpNode.children[i];
      if(childNode.isCluster())
        s.push(childNode);
      else
      {
        if(firstNode == false)
          writer_d.write(", ");
        else
          firstNode = false;    //dont write the comma on the first node
        writer_d.write(childNode.getName());
      }
    }
  }
}

public void genChildrenFromOneLevelOLD(Graph cLvlG) throws IOException
{
  NextLevelGraph nextLvl = new NextLevelGraph();
  Graph          nextLvlG = nextLvl.genNextLevelGraph(cLvlG);
  Node[]         nodeList = nextLvlG.getNodes();

  for(int i = 0; i<nodeList.length;i++)
  {
    Node tmp = nodeList[i];
    if(tmp.children == null)
      continue;

    if(tmp.children.length==0)
      continue;

    //Write the Header
    writer_d.write("SS("+tmp.getName()+") = ");

    //for(int j = 0; j < tmp.children.length;j++)
        echoNestedChildrenOLD(tmp);

    //Close cluster
    writer_d.write("\n");
  }
}

public void echoNestedChildren(Node n, Vector v) throws IOException
{
  Stack s = new Stack();
  boolean firstNode = true;
  s.push(n);
  while(!s.isEmpty())
  {
    Node tmpNode = (Node)s.pop();
    if(tmpNode.children==null)
      continue;
    for(int i = 0; i < tmpNode.children.length; i++)
    {
      Node childNode = tmpNode.children[i];
      if(childNode.isCluster())
        s.push(childNode);
      else
      {
        //if(firstNode == false)
        //  writer_d.write(", ");
        //else
        //  firstNode = false;    //dont write the comma on the first node
        v.addElement(childNode); //writer_d.write(childNode.getName());
      }
    }
  }
}

public void genChildrenFromOneLevel(Graph cLvlG) throws IOException
{
  Graph nextLvlG = null;
  if(cLvlG.getClusterNames().length > 1)
  {
    NextLevelGraph nextLvl = new NextLevelGraph();
    nextLvlG = nextLvl.genNextLevelGraph(cLvlG);
  }
  else
    nextLvlG = cLvlG;

  Node[]         nodeList = nextLvlG.getNodes();
  Vector         cVect    = new Vector();
  int            Lvl      = cLvlG.getGraphLevel();

  cVect.removeAllElements();
  for(int i = 0; i<nodeList.length;i++)
  {
    Node tmp = nodeList[i];
    if(tmp.children == null)
      continue;

    if(tmp.children.length==0)
      continue;

    findStrongestNode(tmp);
    cVect.addElement(tmp);
  }

  if(cVect.size()>0)
    writer_d.write("SS(ROOT) = ");

  for (int i = 0; i < cVect.size(); i++)
  {
    Node n = (Node)cVect.elementAt(i);
    writer_d.write(n.getName());
    if(i < (cVect.size()-1))
      writer_d.write(", ");
    else
      writer_d.write("\n");
  }

  echoNestedTree(cVect);
    //Write the Header
    //writer_d.write("SS("+tmp.getName()+") = ");

    //for(int j = 0; j < tmp.children.length;j++)
    //    echoNestedChildren(tmp);

    //Close cluster
    //writer_d.write("\n");
  //}
  //WriteOutputClusters(cVect,Lvl);
}

public void echoNestedTree(Vector v) throws IOException
{
  LinkedList l = new LinkedList();

  for(int i = 0; i < v.size(); i++)
    l.addLast(v.elementAt(i));

  while(l.size() > 0)
  {
    Node n = (Node)l.removeFirst();
    if((n.children != null)&(n.children.length>0))
    {
      String ssName = findStrongestNode(n);
      //writer_d.write("SS("+ssName+") = ");
      writer_d.write("SS("+n.getName()+") = ");
      for(int i = 0; i < n.children.length; i++)
      {
        Node c = n.children[i];
        if(c.isCluster())
        {
          l.addLast(c);
          findStrongestNode(c);
        }
        writer_d.write(c.getName());
        if(i < (n.children.length-1))
          writer_d.write(", ");
        else
          writer_d.write("\n");
      }
    }
  }
}

public String findStrongestNode(Node n)
{
  if (n.isCluster() == false)
    return "";

  int lvl = n.nodeLevel;
  boolean lvlIncr = false;

  Vector nodeV = new Vector();

  LinkedList l = new LinkedList();
  l.clear();
  nodeV.clear();

  //Seed the linked list

  l.addLast(n);
  while (!l.isEmpty())
  {
    Node curr = (Node)l.removeFirst();
    if (curr.isCluster())
    {
      Node[] children = curr.children;
      if((children != null) && (children.length>0))
        for(int j = 0; j < children.length; j++)
          l.addLast(children[j]);
    }
    else
    {
      nodeV.add(curr);
    }
  }

  //String ssName = "ss"+(findStrongestNode(nodeV))+"L"+n.nodeLevel;
  String ssName = "(SS-L"+n.nodeLevel+"):"+findStrongestNode(nodeV);
  n.setName(ssName);
  return ssName;
}


public String findStrongestNode(Vector v)
{
  int maxEdgeWeight = 0;
  int maxEdgeCount = 0;
  Node domEdgeNode = null;
  Node domWeightNode = null;

  if (v == null) return "EmptyCluster";

  for(int i = 0; i < v.size();i++)
  {
    Node n = (Node)v.elementAt(i);
    String name = n.getName();
    int  edgeWeights=0;
    int depCount = 0;
    int beCount = 0;

    if(n.dependencies!=null)
      depCount = n.dependencies.length;

    if(n.backEdges != null)
      beCount = n.backEdges.length;

    int edgeCount = depCount + beCount;

    if(edgeCount > maxEdgeCount)
    {
      maxEdgeCount = edgeCount;
      domEdgeNode = n;
    }

    if(n.weights!=null)
      for(int j = 0; j < n.weights.length;j++)
        edgeWeights+=n.weights[j];

    if(n.beWeights!=null)
      for(int j = 0; j < n.beWeights.length;j++)
        edgeWeights+=n.beWeights[j];

    if(edgeWeights > maxEdgeWeight)
    {
      maxEdgeWeight = edgeWeights;
      domWeightNode = n;
    }
  }
  //Using edge counts now, but can switch to weights later
  return domEdgeNode.getName();
}

public void WriteOutputClusters(Vector cVect, int lvl) throws IOException
{
  if(cVect==null) return;

  for(int i = 0; i < cVect.size(); i++)
  {
    Vector cluster = (Vector)cVect.elementAt(i);
    String cName = findStrongestNode(cluster);
    if(lvl>0)
      cName += "L"+lvl;

    cName+=".ss";

    writer_d.write("SS("+cName+") = ");
    for(int j = 0; j < cluster.size(); j++)
    {
      Node n = (Node)cluster.elementAt(j);
      writer_d.write(n.getName());
      if(j<(cluster.size()-1))
        writer_d.write(", ");
      else
        writer_d.write("\n");
    }
  }
}

public
void
write()
{

  Graph gBase = graph_d;
  while (gBase.getGraphLevel() != 0)
    gBase = gBase.getPreviousLevelGraph();

  int clusters[] = gBase.getClusters();
  Node[] nodeList = gBase.getNodes();
  int nodes = nodeList.length;
  int[][] clustMatrix = new int[nodes][nodes+1];

  Vector edges = new Vector();

  //int clusters[] = graph_d.getClusters();
  //Node[] nodeList = graph_d.getNodes();
  //int nodes = nodeList.length;
  //int[][] clustersMatrix = new int[nodes][nodes+1];


  try {
	 	writer_d = new BufferedWriter(new FileWriter(getCurrentName()+".bunch"));

   for (int i=0; i<nodes; ++i) {
        clustMatrix[i][0] = 0;
        nodeList[i].cluster = -1;
    }

    int pos=0;
    for (int i=0; i<nodes; ++i) {
        int numCluster = clusters[i];
        clustMatrix[numCluster][(++clustMatrix[numCluster][0])] = i;
        nodeList[i].cluster = numCluster;
    }

   //writeHeader(gBase);

   Node [] on = gBase.getOriginalNodes();
   if((on != null) &&(on.length != nodeList.length))
      writeSpecialModules(gBase.getOriginalNodes());
   genChildrenFromOneLevel(graph_d);
   writeClosing();

   /***********
    for (int i=0; i<nodes; ++i) {
        clustersMatrix[i][0] = 0;
        nodeList[i].cluster = -1;
    }

    int pos=0;
    for (int i=0; i<nodes; ++i) {
        int numCluster = clusters[i];
        clustersMatrix[numCluster][(++clustersMatrix[numCluster][0])] = i;
        nodeList[i].cluster = numCluster;
    }


    writeHeader();
    Node [] on = gBase.getOriginalNodes();

    if((on != null) &&(on.length != nodeList.length))
      writeSpecialModules(gBase.getOriginalNodes());

    NextLevelGraph nlg = new NextLevelGraph();
    Graph upOne = nlg.genNextLevelGraph(graph_d);

    //Writing navigation file: header
    //writer_d.write("// ------------------------------------------------------------ \n");
    //writer_d.write("// created with bunch v2 \n");
    //writer_d.write("// Objective Function value = "+graph_d.getObjectiveFunctionValue()+"\n");
    //writer_d.write("// ------------------------------------------------------------ \n\n");

		int id = 0;
    int clusterIndex = 1;
		for (int i=0; i<nodes; ++i) {
			if (clustersMatrix[i][0]>0) {
				writer_d.write("SS("+clusterIndex+") = ");
				for (int j=1; j<=clustersMatrix[i][0]; ++j) {
					String name = nodeList[clustersMatrix[i][j]].getName();
					nodeList[clustersMatrix[i][j]].cluster = clusterIndex;
          if (j == (clustersMatrix[i][0]))
						writer_d.write(name+"\n");
					else
						writer_d.write(name+", ");
				}
	      clusterIndex++;
    	}
    }

		writer_d.close();
************/
  } catch (IOException e) {
    e.printStackTrace();
  }
}
}