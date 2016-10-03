/****
 *
 *	$Log: GraphOutput.java,v $
 *	Revision 3.0  2002/02/03 18:41:51  bsmitc
 *	Retag starting at 3.0
 *
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *
 *	Revision 3.4  2000/08/18 21:08:00  bsmitc
 *	Added feature to support tree output for dotty and text
 *
 *	Revision 3.3  2000/08/16 00:12:46  bsmitc
 *	Extended UI to support various views and output options
 *
 *	Revision 3.2  2000/08/11 22:01:55  bsmitc
 *	Updated dot output driver to support new output options - all, top, median
 *
 *	Revision 3.1  2000/08/11 13:19:10  bsmitc
 *	Added support for generating various output levels - all, median, one
 *
 *	Revision 3.0  2000/07/26 22:46:09  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:34  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

import java.io.*;

/**
 * A generic class to output a partitioned graph. Subclasses must define
 * the #write() method to implement different output formats.
 *
 * @see bunch.DotGraphOutput
 * @see bunch.TSGraphOutput
 * @see bunch.TXTGraphOutput
 *
 * @author Brian Mitchell
 */
public abstract
class GraphOutput
{
public final static int OUTPUT_TOP_ONLY = 1;
public final static int OUTPUT_MEDIAN_ONLY = 2;
public final static int OUTPUT_ALL_LEVELS = 3;
public final static int OUTPUT_DETAILED_LEVEL_ONLY = 4;

protected Graph graph_d;
protected BufferedWriter writer_d;
protected String fileName_d, currentName_d, basicName_d;
protected boolean writeNestedLevels = false;
protected boolean agglomWriteAllLevels = false;
protected int outputTechnique = OUTPUT_MEDIAN_ONLY;


protected int baseID = 0;

/**
 * Class constructor
 */
public
GraphOutput()
{
}

/**
 * Determines the output technique
 */
public void setOutputTechnique(int t)
{ outputTechnique = t;  }

/**
 * This method is meant to be subclassed to determine the type of output driver
 */
public int getOutputTechnique()
{ return outputTechnique; }

/**
 * Set the flag for outputing nested levels
 */
public void setNestedLevels(boolean b)
{ writeNestedLevels = b;  }

/**
 * Setup if we want to write all nested levels in agglomerative mode
 */
public void setAgglomWriteAllLevels(boolean b)
{ agglomWriteAllLevels = b;  }


/**
 * This method determines if the output driver should write nested levels
 */
public boolean getWriteNestedLevels()
{ return writeNestedLevels; }

public boolean getAgglomWriteAllLevels()
{ return agglomWriteAllLevels; }
/**
 * Sets the partitioned graph to be printed to the stream
 *
 * @param g the graph to print
 */
public
void
setGraph(Graph g)
{
  graph_d = g;
}

/**
 * Obtains the partitioned graph to be printed to the stream
 *
 * @return the graph to print
 */
public
Graph
getGraph()
{
  return graph_d;
}

/**
 * Sets the "basename" for the graph's output file. The base name is
 * the filename without extension or other special additions such as
 * a number to identify the generation number where this graph was
 * created. The basename is used to construct the current name for
 * the graph output instance, which is the actual name that should be used
 * by the #write() method when it is called.
 * <P>This name does not include extension but it includes the full path
 * to the file.
 *
 * @param name the output file's base name
 * @see #setBaseName(java.lang.String)
 * @see #setCurrentName(java.lang.String)
 */
public
void
setBaseName(String name)
{
  fileName_d = name;
}

/**
 * Obtains the "basename" for the graph's output file.
 *
 * @return the output file's base name
 * @see #setBaseName(java.lang.String)
 */
public
String
getBaseName()
{
  return fileName_d;
}

/**
 * Obtains the "currentname" for the graph's output file. The current name
 * is the one that should actually be used by the write method, since it is
 * constructed using the base name by the calling object.
 * <P>This name does not include extension but it includes the full path
 * to the file.
 *
 * @return the output file's current name
 * @see #setCurrentName(java.lang.String)
 * @see #setBaseName(java.lang.String)
 */
public
void
setCurrentName(String n)
{
  currentName_d = n;
}

/**
 * Obtains the "currentname" for the graph's output file.
 *
 * @return the output file's current name
 * @see #setCurrentName(java.lang.String)
 */
public
String
getCurrentName()
{
  return currentName_d;
}

/**
 * Obtains the "basic name" for the graph's output file. The basic name
 * is similar to the base name, except that the path where the file
 * will be stored is not included. It can be used when an output method
 * requires the file name without the path to insert it into one of
 * its output files, for example.
 *
 * @return the output file's basic name
 * @see #getBasicName()
 */
public
void
setBasicName(String bn)
{
  basicName_d = bn;
}

/**
 * Obtains the "basic name" for the graph's output file.
 *
 * @return the output file's basic name
 * @see #setBasicName(java.lang.String)
 */
public
String
getBasicName()
{
  return basicName_d;
}

/**
 * This is the main method that must be defined by GraphOutput subclasses.
 * This method's implementations should output the graph into a file
 * or files in the format specific to that subclass.
 */
public abstract
void
write();
}
