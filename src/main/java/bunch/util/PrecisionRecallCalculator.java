/****
 *
 *	$Log: PrecisionRecallCalculator.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:06  bsmitc
 *	CVS Import
 *	
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
package bunch.util;

import java.util.*;
import java.io.*;
import java.lang.Math.*;
import java.text.*;

public class PrecisionRecallCalculator {

  String m_S_filename1;
  String m_S_filename2;

  String S_precision, S_recall;
  Vector m_v_expert_modules_names;
  Vector m_v_expert_modules_content;
  Vector m_v_tested_modules_names;
  Vector m_v_tested_modules_content;

  public PrecisionRecallCalculator(String expertFileName, String testFileName) {
    m_S_filename1= new String(expertFileName);
    m_S_filename2= new String(testFileName);
    m_v_expert_modules_names = new Vector();
    m_v_expert_modules_content = new Vector();
    m_v_tested_modules_names = new Vector();
    m_v_tested_modules_content = new Vector();
    ReadBunch();
    compare();
  }

  /********
  public static void main(String args[])
  {
    System.out.println("Start...");
    if (args.length<2)
      System.exit(1);

    PaRCalc_main pr = new PaRCalc_main(args[0], args[1]);

    pr.ReadBunch();
    pr.compare();

  }

  ***********/

  public String get_precision()
  {
    return S_precision;
  }

  public String get_recall()
  {
    return S_recall;
  }

  public void compare()
  {
    Compare cp = new Compare(m_v_expert_modules_content, m_v_tested_modules_content, m_v_tested_modules_names);
    cp.do_compare();
    S_precision = cp.get_precision()+"%";
    S_recall = cp.get_recall()+"%";

    //System.out.println("P: "+S_precision+"\nR: "+S_recall);
  }

  public void ReadBunch()
  {
    Hashtable ht1 = new Hashtable();
    GBunchRW bunch1= new GBunchRW(m_S_filename1);

    ht1 = bunch1.read();

    m_v_expert_modules_names = bunch1.getModuleNames();
    m_v_expert_modules_content = bunch1.getModulesContent();

    //remove all the tree information
    boolean found = false;
    for (int i =0; i<m_v_expert_modules_names.size();i++)
    {
      String S_module_name = new String(m_v_expert_modules_names.get(i).toString());
      Vector v_module_content = new Vector((Vector)m_v_expert_modules_content.get(i));
      found = false;
      for (int j=0;j<v_module_content.size() && !found;j++)
      {
        if (m_v_expert_modules_names.contains(v_module_content.get(j)))
        {
          m_v_expert_modules_names.remove(i);
          m_v_expert_modules_content.remove(i);
          i--;
          found = true;
        }
      }
    }

    //System.out.println(m_v_expert_modules_names);
    //System.out.println(m_v_expert_modules_content);

    //read the tested clusters
    Hashtable ht2 = new Hashtable();
    GBunchRW bunch2= new GBunchRW(m_S_filename2);

    ht2 = bunch2.read();

    m_v_tested_modules_names = bunch2.getModuleNames();
    m_v_tested_modules_content = bunch2.getModulesContent();

    //remove all the tree information
    for (int i =0; i<m_v_tested_modules_names.size();i++)
    {
      String S_module_name = new String(m_v_tested_modules_names.get(i).toString());
      Vector v_module_content = new Vector((Vector)m_v_tested_modules_content.get(i));
      found = false;
      for (int j=0;j<v_module_content.size() && !found;j++)
      {
        if (m_v_tested_modules_names.contains(v_module_content.get(j)))
        {
          m_v_tested_modules_names.remove(i);
          m_v_tested_modules_content.remove(i);
          i--;
          found = true;
        }
      }
    }

    //System.out.println(m_v_tested_modules_names);
    //System.out.println(m_v_tested_modules_content);

  }

  /*********
  public PaRCalc_main(String filename1, String filename2)
  {
    m_S_filename1= new String(filename1);
    m_S_filename2= new String(filename2);
    m_v_expert_modules_names = new Vector();
    m_v_expert_modules_content = new Vector();
    m_v_tested_modules_names = new Vector();
    m_v_tested_modules_content = new Vector();

  }
  **********/
}





class Compare
{
  private Hashtable m_ht_vars_orig; // used to get the index of the original vars
  private Hashtable m_ht_vars_new; // used to get the index of the new vars
  private Vector m_v_original_distance, m_v_new_distance_name, m_v_new_distance_number;
  private double m_d_recall, m_d_precision;

  public String get_precision()
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    String fx = nf.format(m_d_precision);
    return fx;
  }

  public String get_recall()
  {
    NumberFormat nf = NumberFormat.getNumberInstance();
    String fx = nf.format(m_d_recall);
    return fx;
  }

  public Compare(Vector orig, Vector newname, Vector newnumber)
  {
    m_ht_vars_orig = new Hashtable();
    m_ht_vars_new = new Hashtable();
    m_v_original_distance = new Vector(orig);
    m_v_new_distance_name = new Vector(newname);
    m_v_new_distance_number = new Vector(newnumber);
    m_d_recall =0.0;
    m_d_precision=0.0;
  }

  public void do_compare()
  {

    //System.out.println("Orig: \n"+m_v_original_distance);
    //System.out.println("new names: \n"+m_v_new_distance_name);
    //System.out.println("new numbers: \n"+m_v_new_distance_number);

// this section will try to calculate the Recall % : it will compare the
// % of intra pairs in the expert partition that were found by the algorithem

    boolean found1 = false;
    boolean found2 = false;
    int pairs_found = 0 ;
    int pairs_total = 0;
    int not_found = 0;

    Vector v_temp = new Vector();
    Vector v_new = new Vector();
    for (int i=0; i<m_v_original_distance.size(); i++)
    {
      v_temp = (Vector)m_v_original_distance.get(i);
      //System.out.println(v_temp.size());
      pairs_total += v_temp.size() * (v_temp.size()-1) /2;
      //System.out.println("Total number of pairs: "+pairs_total);

      for (int j=0; j<v_temp.size()-1; j++)
      {
        found1=false;
        String s_var1 = new String(v_temp.get(j).toString());
        for (int l=0; l<m_v_new_distance_name.size() && !found1;l++) //this will find the first variable in the new clusters
        {
          v_new = (Vector)m_v_new_distance_name.get(l);
          if (v_new.indexOf(s_var1)>=0)
          {
            //System.out.println("______________________________FOUND: "+s_var1+" __________________\n"+v_new);
            found1 = true;
          }
        }
        //System.out.println("Total: "+pairs_total);
        if (!found1)
        {
          not_found++;
          pairs_total-=(v_temp.size()-1-j);
          //System.out.println("Total after not found: "+pairs_total+" removed: "+(v_temp.size()-1-j));
        }
        if (found1)
        {
          //System.out.println("Latest New: "+v_new);
          //System.out.println("v1: "+s_var1);
          for(int k=j+1; k<v_temp.size();k++)
          {
            String s_var2 = new String(v_temp.get(k).toString());
            //System.out.println("v1: "+s_var1+" - v2: "+s_var2);
            if(v_new.indexOf(s_var2)>=0)
            {
              pairs_found++;
              //System.out.println("Found a pair: "+s_var1+" "+s_var2);
            }
          }
          //System.out.println("Found: "+pairs_found+" pairs");
        }
      }
    }
    if (pairs_total!=0)
      m_d_recall = (double)pairs_found*100/pairs_total;
    //System.out.println("Recall= "+m_d_recall+"% not found: "+not_found);

// this part will calculate the precision

    pairs_found = 0 ;
    pairs_total = 0;
    not_found = 0;

    for (int i=0; i<m_v_new_distance_name.size(); i++)
    {
      v_temp = (Vector)m_v_new_distance_name.get(i);
      //System.out.println(v_temp.size());
      pairs_total += v_temp.size() * (v_temp.size()-1) /2;
      //System.out.println("Total number of pairs: "+pairs_total);

      for (int j=0; j<v_temp.size()-1; j++)
      {
        found1=false;
        String s_var1 = new String(v_temp.get(j).toString());
        for (int l=0; l<m_v_original_distance.size() && !found1;l++) //this will find the first variable in the new clusters
        {
          v_new = (Vector)m_v_original_distance.get(l);
          if (v_new.indexOf(s_var1)>=0)
          {
            //System.out.println("______________________________FOUND: "+s_var1+" __________________\n"+v_new);
            found1 = true;
          }
        }
        //System.out.println("Total: "+pairs_total);
        if (!found1)
        {
          not_found++;
          pairs_total-=(v_temp.size()-1-j);
          //System.out.println("Total after not found: "+pairs_total+" removed: "+(v_temp.size()-1-j));
        }
        if (found1)
        {
          //System.out.println("Latest New: "+v_new);
          //System.out.println("v1: "+s_var1);
          for(int k=j+1; k<v_temp.size();k++)
          {
            String s_var2 = new String(v_temp.get(k).toString());
            //System.out.println("v1: "+s_var1+" - v2: "+s_var2);
            if(v_new.indexOf(s_var2)>=0)
            {
              pairs_found++;
              //System.out.println("Found a pair: "+s_var1+" "+s_var2);
            }
          }
          //System.out.println("Found: "+pairs_found+" pairs");
        }
      }
    }
    if (pairs_total!=0)
      m_d_precision=(double)pairs_found*100/pairs_total;
    //System.out.println("Precision= "+m_d_precision+"% not found: "+not_found);
  }

}

class GBunchRW {
  private Hashtable m_ht_bunchread;
  private String m_S_filename;

  public GBunchRW(String filename) {
    m_ht_bunchread = new Hashtable(); //the main hashtable that will be returned
    m_S_filename = new String(filename);
  }

  public Hashtable read()
  {
    int i_start_location_of_SS =0;
    int i_end_location_of_SS =0;
    String S_module_name = new String();
    Vector v_module_value = new Vector();

    try {
      BufferedReader br = new BufferedReader(new FileReader(m_S_filename));
      while (true) {
        v_module_value = new Vector();
        S_module_name = new String();

        String line = br.readLine();

        if (line == null)
      	  break;
        line = line.trim();

        if (line.length() == 0)
          continue;

        if (line.length() > 1 && line.charAt(0) == '/' && line.trim().charAt(1) == '/')
          continue;

        i_start_location_of_SS = line.indexOf("SS(")+3;
        i_end_location_of_SS = line.indexOf(")");
        S_module_name = line.substring(i_start_location_of_SS, i_end_location_of_SS);
        line = line.substring(line.indexOf("=")+1);

        //System.out.println(line+" :"+S_module_name+".");

        StringTokenizer st = new StringTokenizer(line, ",");
        while (st.hasMoreTokens())
          v_module_value.add(st.nextToken().trim());

        m_ht_bunchread.put(S_module_name,v_module_value);
      }

    } catch (java.io.IOException e) {
      System.out.println("Opps: "+e);
    }

    return (Hashtable)m_ht_bunchread.clone();
  }

  public void write(Hashtable ht)
  {
    try {
      FileWriter fos = new FileWriter(m_S_filename);

      fos.write("//Created automatically using GBunchRW...\n");
    Enumeration keys = ht.keys();
    while (keys.hasMoreElements())
    {
      String S_temp = new String(keys.nextElement().toString());
      Vector v_temp = new Vector((Vector)ht.get(S_temp));

      fos.write("SS("+S_temp+")= ");  //write the name of every module
      for (int i=0;i<v_temp.size()-1;i++)
      {
        fos.write(v_temp.get(i)+", ");
      }
      fos.write(v_temp.get(v_temp.size()-1).toString()+"\n"); //write the last var
    }

      fos.close();
    } catch (java.io.IOException e) {
      System.out.println("Opps: "+e);
    }

  }//end of method

  public Vector getModuleNames()
  {
    Vector v_temp = new Vector();
    Enumeration keys = m_ht_bunchread.keys();
    while (keys.hasMoreElements())
    {
      String S_temp = new String(keys.nextElement().toString());
      v_temp.add(S_temp);
    }

    return (Vector)v_temp.clone();
  }

  public Vector getModulesContent()
  {
    Vector v_modules = new Vector();
    //Vector v_temp = new Vector();

    Enumeration keys = m_ht_bunchread.keys();
    while (keys.hasMoreElements())
    {
      String S_temp = new String(keys.nextElement().toString());
      Vector v_temp = new Vector((Vector)m_ht_bunchread.get(S_temp));
      v_modules.add(v_temp);
    }
    return (Vector)v_modules.clone();
  }
} //end of class
