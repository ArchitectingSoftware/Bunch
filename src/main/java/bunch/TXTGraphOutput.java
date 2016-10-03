/****
 *
 *	$Log: TXTGraphOutput.java,v $
 *	Revision 3.0  2002/02/03 18:41:57  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.7  2000/10/22 15:48:49  bsmitc
 *	*** empty log message ***
 *	
 *	Revision 3.6  2000/08/18 21:08:00  bsmitc
 *	Added feature to support tree output for dotty and text
 *
 *	Revision 3.5  2000/08/17 00:26:05  bsmitc
 *	Fixed omnipresent and library support for nodes in the MDG not connected to
 *	anything but the omnipresent nodes and libraries.
 *
 *	Revision 3.4  2000/08/16 00:33:15  bsmitc
 *	Changed output subsystem name to be consistent with the DOT subsystem names
 *
 *	Revision 3.3  2000/08/16 00:12:46  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.2  2000/08/11 13:19:11  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.1  2000/07/26 23:42:15  bsmitc
 *	Updated findStrongestNode() method to check for null arrays.  Null arrays
 *	may have been causing a null pointer exception in rare cases.
 *
 *	Revision 3.0  2000/07/26 22:46:11  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.*;

import java.io.*;
import java.util.Vector;

public
class TXTGraphOutput
  extends GraphOutput
{

boolean hasSuppliers = false;
boolean hasClients = false;
boolean hasCentrals = false;
boolean hasLibraries = false;

public
TXTGraphOutput()
{
}

public void writeHeader(Graph gBase) throws IOException
{
    writer_d.write("// ------------------------------------------------------------ \n");
    writer_d.write("// created with bunch v2 \n");
    writer_d.write("// Objective Function value = "+gBase.getObjectiveFunctionValue()+"\n");
    writer_d.write("// ------------------------------------------------------------ \n\n");
}

public void checkForSpecialModules(Node[] originalNodes)
{
    if (originalNodes != null) {
      hasSuppliers = false;
      hasClients = false;
      hasCentrals = false;
      hasLibraries = false;

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
    }
}

public void writeSpecialModules(Node[] originalNodes) throws IOException
{
    ArrayList deadList = new ArrayList();
    deadList.clear();

    if (originalNodes != null) {
      hasSuppliers = false;
      hasClients = false;
      hasCentrals = false;
      hasLibraries = false;

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
            if(count > 1) writer_d.write(", ");
            writer_d.write(originalNodes[i].getName());
            count++;
          }
          if(originalNodes[i].getType() >= Node.DEAD)
            if((originalNodes[i].getType()-Node.DEAD)==Node.LIBRARY) {
              if(count > 1) writer_d.write(", ");
              writer_d.write(originalNodes[i].getName());
              deadList.add(originalNodes[i]);
              count++;
            }
        }
        writer_d.write("\n");
      }

      count = 1;
      if (hasSuppliers) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_suppliers) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.SUPPLIER) {
            if(count > 1) writer_d.write(", ");
            writer_d.write(originalNodes[i].getName());
            count++;
          }
          if(originalNodes[i].getType() >= Node.DEAD)
            if((originalNodes[i].getType()-Node.DEAD)==Node.SUPPLIER) {
              if(count > 1) writer_d.write(", ");
              writer_d.write(originalNodes[i].getName());
              deadList.add(originalNodes[i]);
              count++;
            }
        }
        writer_d.write("\n");
      }

      count = 1;
      if (hasClients) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_clients) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CLIENT) {
            if(count > 1) writer_d.write(", ");
            writer_d.write(originalNodes[i].getName());
            count++;
          }
          if(originalNodes[i].getType() >= Node.DEAD)
            if((originalNodes[i].getType()-Node.DEAD)==Node.CLIENT) {
              if(count > 1) writer_d.write(", ");
              writer_d.write(originalNodes[i].getName());
              deadList.add(originalNodes[i]);
              count++;
            }
        }
        writer_d.write("\n");
      }

      count = 1;
      if (hasCentrals) {
        //create suppliers cluster
        writer_d.write("SS(omnipresent_centrals) = ");
        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CENTRAL) {
            if(count > 1) writer_d.write(", ");
            writer_d.write(originalNodes[i].getName());
            count++;
          }
          if(originalNodes[i].getType() >= Node.DEAD)
            if((originalNodes[i].getType()-Node.DEAD)==Node.CENTRAL) {
              if(count > 1) writer_d.write(", ");
              writer_d.write(originalNodes[i].getName());
              deadList.add(originalNodes[i]);
              count++;
            }
        }
        writer_d.write("\n");
      }
    }
}


public void writeClosing() throws IOException
{
    //writer_d.close();
}

public void genClusterOLD(Node n, long baseID) throws IOException
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

public void generateClustersOLD(Node []na) throws IOException
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
            String cName = findStrongestNode(tmp);
            //cName = "(SS-L"+tmp.nodeLevel+"):"+cName;
            cName = cName+".ssL"+tmp.nodeLevel;
            long clustID = tmp.nodeID+(baseID++);
            writer_d.write("subgraph cluster"+clustID+" {\n");
            writer_d.write("label = \""+cName+"\";\n");
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

public void generateClusters(Graph cLvlG) throws IOException
{
  Graph nextLvlG = null;

  if((cLvlG.getClusterNames().length <= 1)&&(cLvlG.getPreviousLevelGraph()!=null))
    cLvlG = cLvlG.getPreviousLevelGraph();


  NextLevelGraph nextLvl = new NextLevelGraph();
  nextLvlG = nextLvl.genNextLevelGraph(cLvlG);


  Node[]         nodeList = nextLvlG.getNodes();
  int            Lvl      = cLvlG.getGraphLevel();
  Vector         cVect = new Vector();
  cVect.clear();

  long base=1000;
  writer_d.write("SS(ROOT) = ");
  int count = 1;
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


  for(int i = 0; i < cVect.size(); i++)
  {
    if(count > 1) writer_d.write(", ");
    count++;
    Node n = (Node)cVect.elementAt(i);
    //String ssName = "(SS-L"+n.nodeLevel+"):"+n.getName();
    String ssName = n.getName()+".ssL"+n.nodeLevel;
    writer_d.write(ssName);
  }

  if(hasSuppliers)
  {
    if(count > 1) writer_d.write(", ");
    count++;
    writer_d.write("omnipresent_suppliers");
  }
  if(hasClients)
  {
    if(count > 1) writer_d.write(", ");
    count++;
    writer_d.write("omnipresent_clients");
  }
  if(hasCentrals)
  {
    if(count > 1) writer_d.write(", ");
    count++;
    writer_d.write("omnipresent_centrals");
  }
  if(hasLibraries)
  {
    if(count > 1) writer_d.write(", ");
    count++;
    writer_d.write("libraries");
  }
  writer_d.write("\n");

  echoNestedTree(cVect);
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
      findStrongestNode(n);
      //writer_d.write("SS("+ssName+") = ");
      //String ssName = "(SS-L"+n.nodeLevel+"):"+n.getName();
      String ssName = n.getName()+".ssL"+n.nodeLevel;
      writer_d.write("SS("+ssName+") = ");
      for(int i = 0; i < n.children.length; i++)
      {
        Node c = n.children[i];
        if(c.isCluster())
        {
          l.addLast(c);
          findStrongestNode(c);
          //ssName = "(SS-L"+c.nodeLevel+"):"+c.getName();
          ssName = c.getName()+".ssL"+c.nodeLevel;
          writer_d.write(ssName);
        }
        else
          writer_d.write(c.getName());
        if(i < (n.children.length-1))
          writer_d.write(", ");
        else
          writer_d.write("\n");
      }
    }
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
//System.out.println("Cluster count = " + cLvlG.getClusterNames().length);

  if((cLvlG.getClusterNames().length <= 1)&&(cLvlG.getPreviousLevelGraph()!=null))
  {
    cLvlG = cLvlG.getPreviousLevelGraph();
    fixupNodeList(cLvlG);
//System.out.println("Cluster count 2= " + cLvlG.getClusterNames().length);
  }

  //if(cLvlG.getClusterNames().length>1)
  //{
    NextLevelGraph nextLvl = new NextLevelGraph();
    nextLvlG = nextLvl.genNextLevelGraph(cLvlG);
  //}
  //else
  //  nextLvlG = cLvlG;

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

    Vector newCluster = new Vector();
    newCluster.removeAllElements();
    cVect.addElement(newCluster);
    echoNestedChildren(tmp,newCluster);
    //Write the Header
    //writer_d.write("SS("+tmp.getName()+") = ");

    //for(int j = 0; j < tmp.children.length;j++)
    //    echoNestedChildren(tmp);

    //Close cluster
    //writer_d.write("\n");
  }
  WriteOutputClusters(cVect,Lvl);
}

public String findStrongestNode(Node n)
{
  if (n.isCluster() == false)
    return "";
//System.out.println("Children count = " + n.children.length);
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
//System.out.println("node "+curr.getName()+" " + children.length+" nodes");
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
  String ssName = findStrongestNode(nodeV);
  n.setName(ssName);
  //ssName = "(SS-L"+n.nodeLevel+"):"+ssName;
  ssName = ssName+".ssL"+n.nodeLevel;
  return ssName;
}

public String findStrongestNode(Vector v)
{
  int maxEdgeWeight = 0;
  int maxEdgeCount = 0;
  Node domEdgeNode = null;
  Node domWeightNode = null;

  if (v == null) return "EmptyCluster";
  //if(v.size()==0)
  //  System.out.println("size is 0");
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

    if(edgeCount >= maxEdgeCount)
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

    if(edgeWeights >= maxEdgeWeight)
    {
      maxEdgeWeight = edgeWeights;
      domWeightNode = n;
    }
  }
  //Using edge counts now, but can switch to weights later
//if(domEdgeNode == null)
//  System.out.println("node is null");
//if(domEdgeNode.getName()==null)
//  System.out.println("name is null");
  return domEdgeNode.getName();
}

public void WriteOutputClusters(Vector cVect, int lvl) throws IOException
{
  if(cVect==null) return;

  for(int i = 0; i < cVect.size(); i++)
  {
    Vector cluster = (Vector)cVect.elementAt(i);
    String cName = findStrongestNode(cluster);
    //if(lvl>0)
    //  cName += "L"+lvl;

    //cName+=".ss";

    writer_d.write("SS("+cName+".ss) = ");
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

public void write()
{
  Graph gWriteGraph = graph_d;
//System.out.println("Write Called");
  int technique = this.getOutputTechnique();
  String fileName = this.getCurrentName();

  switch(technique)
  {
    case GraphOutput.OUTPUT_ALL_LEVELS:
    {
      Graph gLvl = graph_d;

      while(gLvl.getGraphLevel() > 0)
      {
        fixupNodeList(gLvl);
        if(gLvl.getClusterNames().length <= 1)
        {
          gLvl = gLvl.getPreviousLevelGraph();
          continue;
        }
        String fName = fileName+"L"+gLvl.getGraphLevel()+".bunch";
        writeGraph(fName,gLvl);
        gLvl = gLvl.getPreviousLevelGraph();
      }

      fileName += ".bunch";
      writeGraph(fileName,graph_d.getMedianTree());

      break;
    }
    case GraphOutput.OUTPUT_MEDIAN_ONLY:
    {
      fileName += ".bunch";

      Graph g = graph_d;
      if (graph_d.isClusterTree())
        g = graph_d.getMedianTree();
      writeGraph(fileName,g);
      break;
    }
    case GraphOutput.OUTPUT_TOP_ONLY:
    {
      fileName += ".bunch";
      writeGraph(fileName,graph_d);
      break;
    }
    case GraphOutput.OUTPUT_DETAILED_LEVEL_ONLY:
    {
      fileName += ".bunch";
      Graph tmpG = graph_d;
      while(tmpG.getGraphLevel() > 0)
        tmpG=tmpG.getPreviousLevelGraph();

      writeGraph(fileName,tmpG);
      break;
    }
  }
}
public void writeGraph(String fileName, Graph g)
{
  try
  {
    writer_d = new BufferedWriter(new FileWriter(fileName));
    generateOutput(g);
    writer_d.close();
  }
  catch (IOException e) {
    e.printStackTrace();
  }
}

public void fixupNodeList(Graph g)
{
  //this is basically a hack, and an update to the Graph class should
  //be made to ensure that this happens there
  Node [] nodeList = g.getNodes();
  int  [] clusters = g.getClusters();
  for(int i = 0; i < nodeList.length; i++)
    nodeList[i].cluster = clusters[i];
}


public
void
generateOutput(Graph g) throws IOException
{

  Graph gBase = g;    //graph_d;
  Graph gWriteGraph = g;  //graph_d;

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


  //try {
  // 	writer_d = new BufferedWriter(new FileWriter(getCurrentName()+".bunch"));
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

   //Node [] on = gBase.getOriginalNodes();
   //if((on != null) &&(on.length != nodeList.length))
   //   writeSpecialModules(gBase.getOriginalNodes());

   //if (graph_d.isClusterTree())
   // gWriteGraph = graph_d.getMedianTree();

   Node [] on = gBase.getOriginalNodes();
   if((on != null) &&(on.length != nodeList.length))
      checkForSpecialModules(gBase.getOriginalNodes());

   if(getWriteNestedLevels() == false)
      genChildrenFromOneLevel(gWriteGraph);
   else
     generateClusters(gWriteGraph);

   on = gBase.getOriginalNodes();
   if((on != null) &&(on.length != nodeList.length))
      writeSpecialModules(gBase.getOriginalNodes());

   //genChildrenFromOneLevel(gWriteGraph);

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
  //} catch (IOException e) {
  //  e.printStackTrace();
  //}
}
};


