/****
 *
 *	$Log: DependencyFileParser.java,v $
 *	Revision 3.0  2002/02/03 18:41:47  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/11/26 15:48:13  bsmitc
 *	Fixed various bugs
 *
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.*;

/**
 * A parser for the dependencies file used as input for bunch. This parser
 * creates a graph from the file with no clusters.
 *
 * @author Brian Mitchell
 *
 * @see bunch.Parser
 * @see bunch.Graph
 */
public
class DependencyFileParser
  extends Parser
{
  private int reflexiveEdges;

/**
 * Inner class used by the parsing process to store the graph
 * information temporarily before converting it into a Graph
 */
class ParserNode
{
public String name;
public Hashtable dependencies;
public Hashtable backEdges;
public Hashtable dWeights;
public Hashtable beWeights;
public int[] arrayDependencies;
public int[] arrayWeights;

/**
 * Data structure to keep track of the node and its dependencies
 */
public
ParserNode(String n)
{
  name = n;
  dependencies = new Hashtable();
  dWeights = new Hashtable();
  backEdges = new Hashtable();
  beWeights = new Hashtable();
}
}

/**
 * Class constructor
 */
public
DependencyFileParser()
{
  reflexiveEdges = 0;
}

/**
 * This method is used to determine if there are reflexive edges in the MDG
 *
 * @return The number of reflexive edges in the input MDG
 */
public int getReflexiveEdges()
{ return reflexiveEdges;  }

/**
 * This method is also used to determine if there are reflexive edges in the MDG
 *
 * @return The number of reflexive edges in the input MDG
 */
public boolean hasReflexiveEdges()
{ return (reflexiveEdges > 0);   }

/**
 * The method of the class where all work is done.
 */
public
Object
parse()
{
  reflexiveEdges = 0;
  Hashtable nodes = new Hashtable();
  Graph retGraph = null;

  try {
    //read all the information from the file
    while (true)
    {
      //Read the next line
      String line = reader_d.readLine();
      if (line == null) {
        break;
      }
      if (line == "") {
        continue;
      }

      //Parse the current line
      StringTokenizer st = new StringTokenizer(line, delims_d);
      if (!st.hasMoreTokens()) {
        continue;
      }

      ParserNode currentNode = null;
      ParserNode targetNode = null;

      //Source Node
      String nod = st.nextToken();

      //New code to check for reflexive edges
      String target = null;
      if (st.hasMoreElements())
        target = st.nextToken();

      if (nod.equals(target))
      {
        reflexiveEdges++;
        continue;
      }

      currentNode = (ParserNode)nodes.get(nod);

      //Node is not known yet, add it to the list
      if (currentNode == null)
      {
        currentNode = new ParserNode(nod);
        nodes.put(nod,currentNode);
      }

      //For now the default weight is 1, it will be overriden if a weight
      //is actually present in the input file...
      Integer w = new Integer(1);

      //Make sure a target node exists
      if (target != null)
      {
        String dep = target;

        //Now if there are more tokens the weight is the next expected
        //token
        if (st.hasMoreElements())
            w = new Integer(st.nextToken());

        //See if the target node is known, if not add it to the list
        targetNode = (ParserNode)nodes.get(dep);
        if (targetNode == null)
        {
          targetNode = new ParserNode(dep);
          nodes.put(dep,targetNode);
        }

        //Add source to target, and target to source if they don't already
        //exist as forward and backward dependencies
        if (!currentNode.dependencies.containsKey(dep))
        {
          currentNode.dependencies.put (dep,dep);
          currentNode.dWeights.put(dep,w);
          //System.out.println("Adding weight " + w);
        }
        else
        {
          Integer wExisting = (Integer)currentNode.dWeights.get(dep);
          Integer wtemp = new Integer(w.intValue() + wExisting.intValue());
          currentNode.dWeights.put(dep,wtemp);
        }

        if (!targetNode.backEdges.containsKey(nod))
        {
          targetNode.backEdges.put(nod,nod);
          targetNode.beWeights.put(nod,w);
        }
        else
        {
          Integer wExisting = (Integer)targetNode.beWeights.get(nod);
          Integer wtemp = new Integer(w.intValue() + wExisting.intValue());
          targetNode.beWeights.put(nod,wtemp);
        }
      }
    }

    //now deal with Bunch Format -- Generate bunch graph object
    int sz = nodes.size();
    Hashtable nameTable = new Hashtable();

    //build temporary name to ID mapping table
    Object [] oa = nodes.keySet().toArray();
    for (int i = 0; i < oa.length; i++)
    {
      String n = (String)oa[i];
      nameTable.put(n,new Integer(i));
    }

    //now build the graph
    retGraph = new Graph(nodes.size());
    retGraph.clear();
    Node[] nodeList = retGraph.getNodes();

    //now setup the datastructure
    Object [] nl = nodes.values().toArray();
    for(int i = 0; i < nl.length; i++)
    {
      Node       n = new Node();
      nodeList[i]  = n;
      ParserNode p = (ParserNode)nl[i];
      n.setName(p.name);
      Integer nid = (Integer)nameTable.get(p.name);
      n.nodeID = nid.intValue();
      n.dependencies = ht2ArrayFromKey(nameTable,p.dependencies);
      n.weights = ht2ArrayValFromKey(nameTable,p.dWeights);
      n.backEdges = ht2ArrayFromKey(nameTable,p.backEdges);
      n.beWeights = ht2ArrayValFromKey(nameTable,p.beWeights);
    }
  }
  catch (java.io.IOException e) {
    e.printStackTrace();
  }

  //dumpGraph(nodes);
  return retGraph;
}

/**
 * Helper routine that given a hashtable of values and a key returns an
 * object array of values
 */
private int[] ht2ArrayFromKey(Hashtable key, Hashtable values)
{
    int [] retArray = new int[values.size()];

    try{
      Object [] oa = values.keySet().toArray();
      for(int i = 0; i < oa.length; i++)
      {
        String s = (String)oa[i];
        Integer val = (Integer)key.get(s);
        retArray[i] = val.intValue();
      }
      return retArray;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
}

/**
 * Since this is a hashtable of hashtables we want to return
 * the contents of the inner hashtable in an integer array format.
 */
private int[] ht2ArrayValFromKey(Hashtable key, Hashtable values)
{
    int [] retArray = new int[values.size()];

    try{
      Object [] oa = values.keySet().toArray();
      for(int i = 0; i < oa.length; i++)
      {
        String s = (String)oa[i];
        Integer value = (Integer)values.get(s);
        retArray[i] = value.intValue();
      }
      return retArray;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return null;
    }
}

/**
 * Debugging routine to dump the graph to standard output
 */
public void dumpGraph(Hashtable h)
{
  Object [] oa = h.keySet().toArray();
  for(int i = 0; i < oa.length; i++)
  {
    ParserNode n = (ParserNode)oa[i];
    System.out.print(n.name+": ");
    Hashtable dep = n.dependencies;
    Object [] oa1 = dep.keySet().toArray();
    for(int j = 0; j < oa1.length; j++)
    {
      ParserNode n1 = (ParserNode)oa1[j];
      System.out.print(n1.name+" ");
    }
    oa1 = n.backEdges.keySet().toArray();
    for(int j = 0; j < oa1.length; j++)
    {
      ParserNode n1 = (ParserNode)oa1[j];
      System.out.print("be("+n1.name+") ");
    }
    System.out.println();
  }
}
}
