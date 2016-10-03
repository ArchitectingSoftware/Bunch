/****
 *
 *	$Log: TSGraphOutput.java,v $
 *	Revision 3.0  2002/02/03 18:41:56  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
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

import java.io.*;
import java.util.Vector;

public
class TSGraphOutput
  extends GraphOutput
{
public
TSGraphOutput()
{
}

public
void
write()
{
  Vector edges = new Vector();
  int clusters[] = graph_d.getClusters();
  Node[] nodeList = graph_d.getNodes();
  int nodes = nodeList.length;

  try {
	 	writer_d = new BufferedWriter(new FileWriter(getCurrentName()+".nav"));


    int pos=0;
    int minClusterNum = Integer.MAX_VALUE;
    int maxClusterNum = 0;

    for (int i=0; i<nodes; ++i) {
        int numCluster = clusters[i];
        if (numCluster < minClusterNum)
        	minClusterNum = numCluster;
        else if (numCluster > maxClusterNum)
        	maxClusterNum = numCluster;
        nodeList[i].cluster = -1;
		}

		int clusterNum = maxClusterNum - minClusterNum;
	  int[][] clustersMatrix = new int[clusterNum+1][nodes+1];
    for (int i=0; i<=clusterNum; ++i) {
        clustersMatrix[i][0] = 0;
    }

    for (int i=0; i<nodes; ++i) {
        int numCluster = clusters[i];
        numCluster -= minClusterNum;

        clustersMatrix[numCluster][(++clustersMatrix[numCluster][0])] = i;
        nodeList[i].cluster = numCluster;
    }

    Node[] originalNodes = graph_d.getOriginalNodes();
    boolean hasSuppliers = false;
    boolean hasClients = false;
    boolean hasLibraries = false;
    boolean hasCentrals = false;

    if (originalNodes != null && originalNodes.length != nodeList.length) {
      for (int i=0; i<originalNodes.length; ++i) {
        if (!hasSuppliers && originalNodes[i].getType() == Node.SUPPLIER) {
          hasSuppliers = true;
        }
        if (!hasClients && originalNodes[i].getType() == Node.CLIENT) {
          hasClients = true;
        }
        if (!hasCentrals && originalNodes[i].getType() == Node.CENTRAL) {
          hasCentrals = true;
        }
        if (!hasLibraries && originalNodes[i].getType() == Node.LIBRARY) {
          hasLibraries = true;
        }
      }
		}

    //Writing navigation file: header
    writer_d.write("// ------------------------------------------------------------ \n");
    writer_d.write("// created with bunch v2 */\n");
    writer_d.write("// Objective Function value = "+graph_d.getObjectiveFunctionValue()+"\n");
    writer_d.write("// ------------------------------------------------------------ \n\n");
		writer_d.write("// Graph Layout Toolkit\n");
		writer_d.write("Navigation file\n");
		writer_d.write("// topDown\n");
		writer_d.write("FALSE\n");
		writer_d.write("// needsRecalculation\n");
		writer_d.write("TRUE\n");
		writer_d.write("// needsRelabeling\n");
		writer_d.write("FALSE\n\n");

    //Writing navigation file: including current cluster file
		writer_d.write("// Graphs\n");

 		writer_d.write(getBasicName()+"C.tss\n");
	  FileWriter clustersFile = new FileWriter(getCurrentName()+"C.tss");
		clustersFile.write("// Circular Layout\n");
		clustersFile.write("// undirected\n");
		clustersFile.write("FALSE\n");
		clustersFile.write("// Symmetric Layout\n");
		clustersFile.write("// undirected\n");
		clustersFile.write("FALSE\n");
		clustersFile.write("// Hierarchical Layout\n");
		clustersFile.write("// undirected\n");
		clustersFile.write("FALSE\n");
		clustersFile.write("// Orthogonal Layout\n");
		clustersFile.write("// undirected\n");
		clustersFile.write("FALSE\n");
		clustersFile.write("// Digraph\n");
		clustersFile.write("// lastLayoutStyle\n");
		clustersFile.write("ORTHOGONAL\n");
		clustersFile.write("// layoutStyle\n");
		clustersFile.write("ORTHOGONAL\n");
		clustersFile.write("\n");
		clustersFile.write("\n");
		clustersFile.write("// Editor Digraph\n");
		clustersFile.write("Version 3.0\n");
		clustersFile.write(getBasicName()+"\n");
		clustersFile.write("\n");
		clustersFile.write("// Nodes\n");


    if (originalNodes != null && originalNodes.length != nodeList.length) {
			//write first libraries, suppliers and clients clusters first
      if (hasLibraries) {
        //create libraries cluster
		    //Writing navigation file: including current cluster file
				writer_d.write(getBasicName()+"_lib.tss\n");
				clustersFile.write("// Node\n");
				clustersFile.write("// name\n");
				clustersFile.write("Libraries\n");
				clustersFile.write("// Editor Node\n");
				clustersFile.write("Libraries\n");
				clustersFile.write("TSEShapeNodeView 2\n");
				clustersFile.write("0x0\n");
				clustersFile.write("0x7480F8\n");
				clustersFile.write("2\n");
				clustersFile.write("// width\n");
				clustersFile.write("140 \n");
				clustersFile.write("// height\n");
				clustersFile.write("40\n");

			  FileWriter libsFile = new FileWriter(getCurrentName()+"_lib.tss");
				libsFile.write("// Hierarchical Layout\n");
				libsFile.write("// magnifiedSpacing\n");
				libsFile.write("FALSE\n");
				libsFile.write("// undirected\n");
				libsFile.write("TRUE\n");
				libsFile.write("// Digraph\n");
				libsFile.write("// lastLayoutStyle\n");
				libsFile.write("HIERARCHICAL\n");
				libsFile.write("// layoutStyle\n");
				libsFile.write("HIERARCHICAL\n");
				libsFile.write("\n");
				libsFile.write("// Editor Digraph\n");
				libsFile.write("Version 3.0\n");
				libsFile.write(" \n\n");
				libsFile.write("// Nodes\n");

        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.LIBRARY) {
		 				libsFile.write("// Node\n");
						libsFile.write("// name\n");
						libsFile.write(originalNodes[i].getName()+"\n");
						libsFile.write("// Editor Node\n");
						libsFile.write(originalNodes[i].getName()+"\n");
						libsFile.write("TSEShapeNodeView 4\n");
						libsFile.write("0x0\n");
						libsFile.write("0x4958E9\n");
						libsFile.write("2\n");
						libsFile.write("// width\n");
						libsFile.write(originalNodes[i].getName().length()*10+"\n");
						libsFile.write("// height\n");
						libsFile.write("25\n");
          }
        }
				libsFile.close();
      }


      if (hasSuppliers) {
        //create suppliers cluster
		    //Writing navigation file: including current cluster file
				writer_d.write(getBasicName()+"_sup.tss\n");
				clustersFile.write("// Node\n");
				clustersFile.write("// name\n");
				clustersFile.write("Suppliers\n");
				clustersFile.write("// Editor Node\n");
				clustersFile.write("Suppliers\n");
				clustersFile.write("TSEShapeNodeView 2\n");
				clustersFile.write("0x0\n");
				clustersFile.write("0x7480F8\n");
				clustersFile.write("2\n");
				clustersFile.write("// width\n");
				clustersFile.write("130 \n");
				clustersFile.write("// height\n");
				clustersFile.write("40\n");

			  FileWriter supsFile = new FileWriter(getCurrentName()+"_sup.tss");
				supsFile.write("// Hierarchical Layout\n");
				supsFile.write("// magnifiedSpacing\n");
				supsFile.write("FALSE\n");
				supsFile.write("// undirected\n");
				supsFile.write("TRUE\n");
				supsFile.write("// Digraph\n");
				supsFile.write("// lastLayoutStyle\n");
				supsFile.write("HIERARCHICAL\n");
				supsFile.write("// layoutStyle\n");
				supsFile.write("HIERARCHICAL\n");
				supsFile.write("\n");
				supsFile.write("// Editor Digraph\n");
				supsFile.write("Version 3.0\n");
				supsFile.write(" \n\n");
				supsFile.write("// Nodes\n");

        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.SUPPLIER) {
		 				supsFile.write("// Node\n");
						supsFile.write("// name\n");
						supsFile.write(originalNodes[i].getName()+"\n");
						supsFile.write("// Editor Node\n");
						supsFile.write(originalNodes[i].getName()+"\n");
						supsFile.write("TSEShapeNodeView 4\n");
						supsFile.write("0x0\n");
						supsFile.write("0x4958E9\n");
						supsFile.write("2\n");
						supsFile.write("// width\n");
						supsFile.write(originalNodes[i].getName().length()*10+"\n");
						supsFile.write("// height\n");
						supsFile.write("30\n");
          }
        }
				supsFile.close();
      }

      if (hasClients) {
        //create clients cluster
		    //Writing navigation file: including current cluster file
				writer_d.write(getBasicName()+"_cli.tss\n");
				clustersFile.write("// Node\n");
				clustersFile.write("// name\n");
				clustersFile.write("Clients\n");
				clustersFile.write("// Editor Node\n");
				clustersFile.write("Clients\n");
				clustersFile.write("TSEShapeNodeView 2\n");
				clustersFile.write("0x0\n");
				clustersFile.write("0x7480F8\n");
				clustersFile.write("2\n");
				clustersFile.write("// width\n");
				clustersFile.write("130 \n");
				clustersFile.write("// height\n");
				clustersFile.write("40\n");

			  FileWriter cliFile = new FileWriter(getCurrentName()+"_cli.tss");
				cliFile.write("// Hierarchical Layout\n");
				cliFile.write("// magnifiedSpacing\n");
				cliFile.write("FALSE\n");
				cliFile.write("// undirected\n");
				cliFile.write("TRUE\n");
				cliFile.write("// Digraph\n");
				cliFile.write("// lastLayoutStyle\n");
				cliFile.write("HIERARCHICAL\n");
				cliFile.write("// layoutStyle\n");
				cliFile.write("HIERARCHICAL\n");
				cliFile.write("\n");
				cliFile.write("// Editor Digraph\n");
				cliFile.write("Version 3.0\n");
				cliFile.write(" \n\n");
				cliFile.write("// Nodes\n");

        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CLIENT) {
		 				cliFile.write("// Node\n");
						cliFile.write("// name\n");
						cliFile.write(originalNodes[i].getName()+"\n");
						cliFile.write("// Editor Node\n");
						cliFile.write(originalNodes[i].getName()+"\n");
						cliFile.write("TSEShapeNodeView 4\n");
						cliFile.write("0x0\n");
						cliFile.write("0x4958E9\n");
						cliFile.write("2\n");
						cliFile.write("// width\n");
						cliFile.write(originalNodes[i].getName().length()*10+"\n");
						cliFile.write("// height\n");
						cliFile.write("30\n");
          }
        }
				cliFile.close();
      }

///////////

      if (hasCentrals) {
        //create clients cluster
		    //Writing navigation file: including current cluster file
				writer_d.write(getBasicName()+"_cen.tss\n");
				clustersFile.write("// Node\n");
				clustersFile.write("// name\n");
				clustersFile.write("Clients/Suppliers\n");
				clustersFile.write("// Editor Node\n");
				clustersFile.write("Clients/Suppliers\n");
				clustersFile.write("TSEShapeNodeView 2\n");
				clustersFile.write("0x0\n");
				clustersFile.write("0x7480F8\n");
				clustersFile.write("2\n");
				clustersFile.write("// width\n");
				clustersFile.write("240 \n");
				clustersFile.write("// height\n");
				clustersFile.write("40\n");

			  FileWriter cenFile = new FileWriter(getCurrentName()+"_cen.tss");
				cenFile.write("// Hierarchical Layout\n");
				cenFile.write("// magnifiedSpacing\n");
				cenFile.write("FALSE\n");
				cenFile.write("// undirected\n");
				cenFile.write("TRUE\n");
				cenFile.write("// Digraph\n");
				cenFile.write("// lastLayoutStyle\n");
				cenFile.write("HIERARCHICAL\n");
				cenFile.write("// layoutStyle\n");
				cenFile.write("HIERARCHICAL\n");
				cenFile.write("\n");
				cenFile.write("// Editor Digraph\n");
				cenFile.write("Version 3.0\n");
				cenFile.write(" \n\n");
				cenFile.write("// Nodes\n");

        for (int i=0; i<originalNodes.length; ++i) {
          if (originalNodes[i].getType() == Node.CENTRAL) {
		 				cenFile.write("// Node\n");
						cenFile.write("// name\n");
						cenFile.write(originalNodes[i].getName()+"\n");
						cenFile.write("// Editor Node\n");
						cenFile.write(originalNodes[i].getName()+"\n");
						cenFile.write("TSEShapeNodeView 4\n");
						cenFile.write("0x0\n");
						cenFile.write("0x4958E9\n");
						cenFile.write("2\n");
						cenFile.write("// width\n");
						cenFile.write(originalNodes[i].getName().length()*10+"\n");
						cenFile.write("// height\n");
						cenFile.write("30\n");
          }
        }
				cenFile.close();
      }
  	}

///////////


		int id = 0;
    int clusterIndex = 0;
		for (int i=0; i<=clusterNum; ++i) {
			if (clustersMatrix[i][0]>0) {
		    //Writing navigation file: including current cluster file
				writer_d.write(getBasicName()+clusterIndex+".tss\n");
		    //Writing clusters file: new cluster node for current cluster
				clustersFile.write("// Node\n");
				clustersFile.write("// name\n");
				clustersFile.write("Cluster "+clusterIndex+"\n");
				clustersFile.write("// Editor Node\n");
				clustersFile.write("Cluster "+clusterIndex+"\n");
				clustersFile.write("TSEShapeNodeView 4\n");
				clustersFile.write("0x0\n");
				clustersFile.write("0x9AC884\n");
				clustersFile.write("2\n");
				clustersFile.write("// width\n");
				clustersFile.write("120 \n");
				clustersFile.write("// height\n");
				clustersFile.write("40\n");

			  FileWriter currentFile = new FileWriter(getCurrentName()+(clusterIndex)+".tss");
				currentFile.write("// Hierarchical Layout\n");
				currentFile.write("// magnifiedSpacing\n");
				currentFile.write("TRUE\n");
				currentFile.write("// undirected\n");
				currentFile.write("FALSE\n");
				currentFile.write("// Digraph\n");
				currentFile.write("// lastLayoutStyle\n");
				currentFile.write("HIERARCHICAL\n");
				currentFile.write("// layoutStyle\n");
				currentFile.write("HIERARCHICAL\n");
				currentFile.write("\n");
				currentFile.write("// Editor Digraph\n");
				currentFile.write("Version 3.0\n");
				currentFile.write(" \n\n");
				currentFile.write("// Nodes\n");

		    //Writing current cluster file: nodes
				for (int j=1; j<=clustersMatrix[i][0]; ++j) {
					String name = nodeList[clustersMatrix[i][j]].getName();
					nodeList[clustersMatrix[i][j]].cluster = clusterIndex;
					currentFile.write("// Node\n");
					currentFile.write("// name\n");
					currentFile.write(name+"\n");
					currentFile.write("// Editor Node\n");
					currentFile.write(name+"\n");
					currentFile.write("TSEShapeNodeView 4\n");
					currentFile.write("0x0\n");
					currentFile.write("0xfbb3b3\n");
					currentFile.write("2\n");
					currentFile.write("// width\n");
					currentFile.write(name.length()*9+"\n");
					currentFile.write("// height\n");
					currentFile.write("20\n");

				}

		    //Writing current cluster file: edges between cluster's nodes
				currentFile.write("\n\n");
				currentFile.write("// Edges\n");
        int edgeCounter = 0;
        for (int k=0; k<nodes; ++k) {
          int[] l = nodeList[k].dependencies;
          if (l != null) {
             for (int j=0; j<l.length; ++j) {
              if ((nodeList[k].cluster == clusterIndex) &&
                  (clusterIndex == nodeList[l[j]].cluster)) {
        				currentFile.write("// Edge\n");
  			        currentFile.write("// name\n");
	        			currentFile.write("Edge "+(edgeCounter++)+"\n");
        				currentFile.write("// fromNodeName\n");
        				currentFile.write(nodeList[k].getName()+"\n");
        				currentFile.write("// toNodeName\n");
				        currentFile.write(nodeList[l[j]].getName()+"\n");
              }
             }
          }
        }

        clusterIndex++;
        currentFile.close();
			}
		}


    //Writing clusters nodes file: edges between different clusters
    int edgeCounter = 0;
  	clustersFile.write("\n\n");
		clustersFile.write("// Edges\n");
    boolean equalEdge = false;
    for (int k=0; k<nodes; ++k) {
      int[] l = nodeList[k].dependencies;
      if (l != null) {
        for (int j=0; j<l.length; ++j) {
          if (nodeList[k].cluster != nodeList[l[j]].cluster) {
            Edge newEdge = new Edge(nodeList[k], nodeList[l[j]]);
            for (int h=0; h<edges.size(); h++) {
              equalEdge = ((Edge) edges.elementAt(h)).equalByCluster(newEdge);
              if (equalEdge)
                break;
            }
            if (!equalEdge) {
    				  clustersFile.write("// Edge\n");
              clustersFile.write("// name\n");
	         	  clustersFile.write("Cluster Edge "+(edgeCounter++)+"\n");
        	 	  clustersFile.write("// fromNodeName\n");
        	 	  clustersFile.write("Cluster "+nodeList[k].cluster+"\n");
        	 	  clustersFile.write("// toNodeName\n");
				      clustersFile.write("Cluster "+nodeList[l[j]].cluster+"\n");
              edges.addElement(newEdge);
            }
          }
        }
      }
    }
		clustersFile.close();

    //Writing navigation file: relations between clusters nodes and clusters files
		writer_d.write("// Navigation relationships\n");
    if (hasLibraries) {
			writer_d.write("Libraries\n");
			writer_d.write(getBasicName()+"_lib.tss\n");
    }
    if (hasSuppliers) {
			writer_d.write("Suppliers\n");
			writer_d.write(getBasicName()+"_sup.tss\n");
    }
    if (hasClients) {
			writer_d.write("Clients\n");
			writer_d.write(getBasicName()+"_cli.tss\n");
    }
    if (hasCentrals) {
			writer_d.write("Clients/Suppliers\n");
			writer_d.write(getBasicName()+"_cen.tss\n");
    }
		for (int i=0; i<clusterIndex; i++) {
			writer_d.write("Cluster "+i+"\n");
			writer_d.write(getBasicName()+i+".tss\n");
  	}
		writer_d.close();
  }
  catch (IOException e) {
    e.printStackTrace();
  }
}
};

class Edge {

public Node from_d, to_d;

public Edge(Node from, Node to) {
  from_d = from;
  to_d = to;
}

public
Node
getFrom()
{
  return from_d;
}

public
Node
getTo()
{
  return to_d;
}

public
boolean
equalByCluster(Edge e)
{
  if (from_d.cluster == e.getFrom().cluster &&
      to_d.cluster == e.getTo().cluster)
      return true;
  else
      return false;
}
};
