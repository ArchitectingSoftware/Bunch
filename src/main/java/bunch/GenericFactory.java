/****
 *
 *	$Log: GenericFactory.java,v $
 *	Revision 3.0  2002/02/03 18:41:50  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:04  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/10/22 15:48:48  bsmitc
 *	*** empty log message ***
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

import java.util.*;
import java.beans.Beans;

/**
 * A generic factory class. This class should be used as superclass
 * for specific (i.e., typed) factory classes that have to abstract
 * the creation of types that are generalized through an interface.<P>
 * Because factory classes are useful also as configuration objects
 * GenericFactory implements the serializable interface. Therefore,
 * classes that subclass it must be careful to specify as transient any
 * member that they do not want to be persistent.
 *
 * @author Brian Mitchell
 */
public
class GenericFactory
  implements java.io.Serializable
{
protected Hashtable methodTable_d;
public static final long serialVersionUID = 100L;

/**
 * The Factory Type is the name of the abstract class that will be
 * used by the objects stored in the factory. It is used by the
 * #getItemInstance(String) method to obtain the FQN for
 * the default class for the particular factory in question
 */
protected String factoryType_d;

/**
 * GenericFactory constructor, initializes the internal storage to
 * 10 (given that factories are usually used for a small number of
 * objects).
 */
public
GenericFactory()
{
  methodTable_d = new Hashtable(10);
}

/**
 * Sets the type of factory of this class, normally the name of the class
 * of objects that will be stored in this factory
 *
 * @param the name of the class
 * @see #getFactoryType()
 */
public
void
setFactoryType(String name)
{
  factoryType_d = name;
}

/**
 * Obtains the type of factory of this class, normally the name of the class
 * of objects that will be stored in this factory
 *
 * @return the name of the class
 * @see #setFactoryType(java.lang.String)
 */
public
String
getFactoryType()
{
  return factoryType_d;
}

/**
 * Add a new object to the factory with its corresponding key name.
 *
 * @param name the object's key name
 * @param className the name of the class that will be instanced to answer for
 * this object
 */
public
void
addItem(String name, String className)
{
  methodTable_d.put(name, className);
}

/**
 * Obtains a list of the available objects of this factory.
 *
 * @return an enumeration (@see java.util.Enumeration) with all the available
 * keys
 */
public
Enumeration
getAvailableItems()
{
  return methodTable_d.keys();
}

public
String[]
getItemList()
{
  String[] list = new String[methodTable_d.size()];
  Enumeration e = methodTable_d.keys();
  int i=0;
  while (e.hasMoreElements()) {
    list[i++] = (String)e.nextElement();
  }
  return list;
}

/**
 * Obtains the name of an item in the factory. The name obtained is usually a
 * fully-qualified class name, which can be later used to create an
 * instance of the object
 *
 * @param name the name of the object which FQN is to be retrieved
 * @return the item stored for the key received as parameter
 */
public
String
getItemName(String name)
{
  return (String)methodTable_d.get(name);
}

/**
 * Obtains an instance of an item in the factory.
 *
 * @param name the name of the object to be retrieved
 * @return an instance of the name that corresponds to the key passed
 * as parameter
 */
public
Object
getItemInstance(String name)
{
  String cls = null;
  if (name.toLowerCase().equals("default")) {
    cls = "bunch.Default"+factoryType_d;
  }
  else {
    cls = (String)methodTable_d.get(name);
  }

  Object obj = null;

  try {
    obj = (Beans.instantiate(null, cls));
  }
  catch (Exception e) {
    return getItemInstanceFromClass(name);
    //throw new RuntimeException(e.toString());
  }
  return obj;
}

/**
 * Get object instance using a class name as a key
 */
public
Object
getItemInstanceFromClass(String cls)
{
  Object obj = null;

  try {
    obj = (Beans.instantiate(null, cls));
  }
  catch (Exception e) {
    throw new RuntimeException(e.toString());
  }
  return obj;
}

/**
 * This method returns the default factory entry.  If one is not
 * overriden in a concreate class, then a null string will be
 * returned
 */
public String getDefaultMethod()
{
  return null;
}

}
