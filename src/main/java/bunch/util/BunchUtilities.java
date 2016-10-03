/****
 *
 *	$Log: BunchUtilities.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *	
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch.util;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.*;
import bunch.api.*;
import bunch.*;

public class BunchUtilities {





public final static double defaultPrecision = 0.0001;

/**
 *		Deserialize an object from a byte array
 */
public static
Object
fromByteArray(byte[] byteArray)
{
   if (byteArray == null || byteArray.length == 0) {
  	   return null;
   }

	try {
			  ByteArrayInputStream bai = new ByteArrayInputStream(byteArray);
			  ObjectInputStream oi = new ObjectInputStream(bai);

			  return oi.readObject();
	}
	catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

/**
 *    Serialize an object into a byte array
 */
public static
byte[]
toByteArray(Serializable obj)
{
	if (obj == null) {
  	return null;
  }

	try {
			  ByteArrayOutputStream bao = new ByteArrayOutputStream();
			  ObjectOutputStream oo = new ObjectOutputStream(bao);

			  oo.writeObject(obj);
			  oo.flush();

			  return bao.toByteArray();
	}
	catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}

public static String DelimitString(String input, int rowWidth)
{
   System.out.println(input);
   StringBuffer sb = new StringBuffer(input);
   int totalLen = input.length();
   String out = "";
   int pos = 0;

   while(pos < totalLen)
   {
      int getSize = Math.min(rowWidth,(totalLen-pos));
      int newOffset = pos+getSize;
      while(newOffset < totalLen)
      {
         char c = sb.charAt(newOffset);
         if((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'))
            break;
         else
            newOffset++;
      }

      out += sb.substring(pos,newOffset) + "\n";
      pos+=newOffset+1;
   }

   return out;
}


public static boolean compareEqual(double a, double b)
{
  int ia = (int)(a/defaultPrecision);
  int ib = (int)(b/defaultPrecision);

  return (ia == ib);
}

public static boolean compareGreater(double a, double b)
{
  int ia = (int)(a/defaultPrecision);
  int ib = (int)(b/defaultPrecision);

  return (ia > ib);
}

public static boolean compareGreaterEqual(double a, double b)
{
  int ia = (int)(a/defaultPrecision);
  int ib = (int)(b/defaultPrecision);

  return (ia >= ib);
}

public static String getLocalHostName()
{
  try
  {
    String sname = java.net.InetAddress.getLocalHost().getHostName();

    //may be a short name or a fully qualified name...only want the host
    //name part

    java.util.StringTokenizer st = new java.util.StringTokenizer(sname,".");

    //only interested in the first token as the host name


    String hname = st.nextToken();

    return hname;

  }
  catch(Exception ex)
  {
    return "bSvrLocal";
  }
}

public static bunch.Graph toInternalGraph(bunch.api.BunchMDG bunchMDG)
{
  ArrayList al = new ArrayList(bunchMDG.getMDGEdges());
  Hashtable nodes = new Hashtable();

  for(int i = 0; i < al.size(); i++)
  {
    bunch.api.BunchMDGDependency bmd = (bunch.api.BunchMDGDependency)al.get(i);

    ParserNode currentNode = null;
    ParserNode targetNode = null;

    /**
     * Ignore reflexive edges
     */
    if (bmd.getSrcNode().equals(bmd.getDestNode()))
      continue;

    currentNode = (ParserNode)nodes.get(bmd.getSrcNode());
    //Node is not known yet, add it to the list
    if (currentNode == null)
    {
        currentNode = new ParserNode(bmd.getSrcNode());
        nodes.put(bmd.getSrcNode(),currentNode);
    }

    targetNode = (ParserNode)nodes.get(bmd.getDestNode());
    //Node is not known yet, add it to the list
    if (targetNode == null)
    {
        targetNode = new ParserNode(bmd.getDestNode());
        nodes.put(bmd.getDestNode(),targetNode);
    }

    String src = bmd.getSrcNode();
    String dep = bmd.getDestNode();
    Integer w = new Integer(bmd.getEdgeW());  //The edge weight

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

    if (!targetNode.backEdges.containsKey(src))
    {
      targetNode.backEdges.put(src,src);
      targetNode.beWeights.put(src,w);
    }
    else
    {
      Integer wExisting = (Integer)targetNode.beWeights.get(src);
      Integer wtemp = new Integer(w.intValue() + wExisting.intValue());
      targetNode.beWeights.put(src,wtemp);
    }

    //----------------
    //DataStructure updated for edge now
    //----------------
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
  bunch.Graph retGraph = new bunch.Graph(nodes.size());
  retGraph.clear();
  bunch.Node[] nodeList = retGraph.getNodes();

  //now setup the datastructure
  Object [] nl = nodes.values().toArray();
  for(int i = 0; i < nl.length; i++)
  {
    bunch.Node       n = new bunch.Node();
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

  return retGraph;
}

/**
 * Helper routine that given a hashtable of values and a key returns an
 * object array of values
 */
private static int[] ht2ArrayFromKey(Hashtable key, Hashtable values)
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
private static int[] ht2ArrayValFromKey(Hashtable key, Hashtable values)
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

}

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