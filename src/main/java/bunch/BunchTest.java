/****
 *
 *	$Log: BunchTest.java,v $
 *	Revision 3.0  2002/02/03 18:41:45  bsmitc
 *	Retag starting at 3.0
 *	
 *	Revision 1.1.1.1  2002/02/03 18:30:03  bsmitc
 *	CVS Import
 *	
 *	Revision 3.1  2000/10/22 15:48:48  bsmitc
 *	*** empty log message ***
 *
 *	Revision 3.0  2000/07/26 22:46:07  bsmitc
 *	*** empty log message ***
 *
 *	Revision 1.1.1.1  2000/07/26 22:43:33  bsmitc
 *	Imported CVS Sources
 *
 *
 */
package bunch;

/**
 * This class is a small built in self test for the old Bunch API.  By
 * ocassionally running this class we can ensure that other changes to the
 * base code still support the old API.
 *
 * @author Brian Mitchell
 */
public
class BunchTest
{

public
BunchTest()
{
}

public static
void
main(String[] args)
{
   try
   {
      System.out.println("TEST: Clustering bunch (need MDG file named bunch)...");
         BunchAPIOld b = new BunchAPIOld("d:\\bunch\\mdgs\\random37.mdg");
         b.runBatch(100);
      System.out.println("TEST Finished, check bunchTest.dot file for output!");
   }
   catch (Exception e) {
    e.printStackTrace();
  }

}
}



