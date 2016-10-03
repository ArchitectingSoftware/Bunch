/****
 *
 *	$Log: ClusterFileParser.java,v $
 *	Revision 3.0  2002/02/03 18:41:45  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/08/09 14:17:47  bsmitc
 *	Changes made to support agglomerative clustering feature.
 *
 *	Revision 3.0  2000/07/26 22:46:08  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.util.*;

/**
 * This class parses a file in SIL format and updates the provided graph
 * object.  This object is often created from the parser factory.
 *
 * @see bunch.Parser
 *
 * @see http://serg.mcs.drexel.edu
 *
 * @author Brian Mitchell
 */
public
class ClusterFileParser
  extends Parser
{
Graph graph_d;

public
ClusterFileParser()
{
}

/**
 * This method sets the graph that will be updated according to the
 * specified cluster layout in the input SIL file.
 *
 * @param obj An instance of a Graph object.
 */
public
void
setObject(Object obj)
{
  graph_d = (Graph)obj;
}

/**
 * This method returns the graph that is updated with clusters.
 *
 * @returns The updated graph after processing the SIL file.
 */
public
Object
getObject()
{
  return graph_d;
}

/**
 * This is the parse method that reads the input file, sets up the clusters
 * and updates the graph accordingly.
 */
public
Object
parse()
{
  int linecount = 0;
  Node[] nodes = graph_d.getNodes();
  int[] clusters = graph_d.getClusters();
  Vector clusterNames = new Vector();

  try {
    /**
     * For each line...
     */
    while (true) {
      String line = reader_d.readLine();
      if (line == null) {
        break;
      }
      if (line.equals("")) {
        continue;
      }

      /**
       * parse the selected line.  See if the first token is a comment
       */
      StringTokenizer tok = new StringTokenizer(line, ", =");
      String first = tok.nextToken();
      if (first.charAt(0) == '/' && first.charAt(1) == '/') { //then its a comment, ignore
        continue;
      }

      /**
       * The next token is the cluster name
       */
      StringTokenizer tok2 = new StringTokenizer(first, "()");
      tok2.nextToken();
      String cname = tok2.nextToken();
      clusterNames.addElement(cname);

      /**
       * The remaining tokens hold the nodes in the clusters.
       */
      while (tok.hasMoreTokens()) {
        String next = tok.nextToken();
        if (next.charAt(0) == '/' && next.charAt(1) == '/') { //then its a comment, ignore
          --linecount; //to make sure the numbers are correct;
          break;
        }
        else
        for (int i=0; i<nodes.length; ++i) {
          if (nodes[i].getName().equals(next)) {
            clusters[i] = linecount;
          }
        }
      }
      ++linecount;
    }
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    return graph_d;
}

public boolean areAllNodesInCluster()
{
    int[] clusters = graph_d.getClusters();
    
    //the cluster vector is initialized to -1 for all nodes
    //if a clusterid is -1 then something is wrong, as all nodes should be
    //specified in a cluster
    for (int i=0; i<clusters.length; ++i) {
        if (clusters[i] == -1) 
            return false;
    }
    return true;
}

public ArrayList getNodesNotAssignedToClusters()
{
    int[] clusters = graph_d.getClusters();
    ArrayList<String> nodeList = new ArrayList();
    Node[] nodes = graph_d.getNodes();
    
    //the cluster vector is initialized to -1 for all nodes
    //if a clusterid is -1 then something is wrong, as all nodes should be
    //specified in a cluster
    for (int i=0; i<clusters.length; ++i) {
        if (clusters[i] == -1) 
            nodeList.add(nodes[i].getName());
    }
    
    if(nodeList.isEmpty())
        return null;
    else
        return nodeList; 
}

}
