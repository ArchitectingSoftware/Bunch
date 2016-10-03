package bunch.api;

import java.util.*;
import java.io.*;
import java.sql.*;

/****
 *
 *	$Log: BunchAPISimAnalysis.java,v $
 *	Revision 1.1.1.1  2002/02/03 18:30:05  bsmitc
 *	CVS Import
 *	
 */

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author Brian Mitchell
 * @version 1.0
 */

public class BunchAPISimAnalysis {

  String testID = "CIAT1";
  Connection con = null;
  PreparedStatement writeRecord = null;

  double pr = 0;
  double pr1 = 0;
  double es = 0;
  double es1 = 0;
  double mc = 0;
  double mc1 = 0;
  int total = 0;

  public BunchAPISimAnalysis() {
    testID = "CIAT1";

    //doTest("d:\\linux\\linux","d:\\linux\\linux",10);
    doTest("e:\\hack\\hack","e:\\hack\\hack",25);
    //this.genHackMDG("e:\\hack\\hack",1000);
    //System.out.println("HACK");
    //randomHack("e:\\hack\\hack",25,1000);
  }


  public void randomHack(String baseFName, int count, int mcount)
  {
    Random r = new Random(System.currentTimeMillis());

    for(int i = 0; i < count; i++)
    {
      int base = 30+ r.nextInt(10);
      Hashtable h = new Hashtable();
      h.clear();

      for(int j = 0; j < base; j++)
      {
        String id = "SS_"+j;
        h.put(id,new Vector());
      }

      for(int j = 0; j < mcount; j++)
      {
        int ssID = r.nextInt(base);
        String mName = "M"+j;
        String ssStrID = "SS_" + ssID;
        Vector v = (Vector)h.get(ssStrID);
        if(v == null) System.out.println("A BUG...");
        v.add(mName);
      }

      dumpRandomOutput(baseFName,i,h);
    }
  }

  public void dumpRandomOutput(String baseFName, int id, Hashtable h)
  {
    String outFileName = baseFName+id+".bunch";
    try
    {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));
      Enumeration e = h.keys();
      while (e.hasMoreElements())
      {
        String ssKey = (String)e.nextElement();
        Vector v = (Vector)h.get(ssKey);
        if(v.size() == 0)
          continue;

        writer.write("SS("+ssKey+")=");
        for(int j = 0; j < v.size(); j++)
        {
          String mname = (String)v.elementAt(j);
          writer.write(mname);
          if(j < (v.size()-1))
            writer.write(",");
          else
            writer.write("\n");
        }
      }

      writer.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }


  public void genHackMDG(String baseFName, int howMany)
  {
    String outFileName = baseFName;
    Random r = new Random(System.currentTimeMillis());
    try
    {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));

      for(long i = 0; i < (10*howMany); i++)
      {
        int rNum = r.nextInt((howMany*howMany));
        int m1 = rNum / howMany;
        int m2 = rNum % howMany;
        String M1 = "M"+m1;
        String M2 = "M"+m2;
        writer.write(M1+" "+M2+"\n");
      }

      //for(int i = 0; i < howMany-1; i++)
      //{
      //  String M1 = "M"+i;
      //  String M2 = "M"+(i+1);
      //  writer.write(M1+" "+M2+"\n");
      //}

      writer.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  public void doTest(String mdgFileName, String baseFileName, int howMany)
  {
    BunchGraph  bgList[] = new BunchGraph[howMany];

    //setDB();

    for(int i = 0; i < howMany; i++)
    {
      Integer idx = new Integer(i);
      String fn = baseFileName + idx.toString() + ".bunch";
      bgList[i] = BunchGraphUtils.constructFromSil(mdgFileName,fn);
    }

    doPrecisionRecall("PR",bgList);
    pr = pr1;
    doEdgeSim("ES",bgList);
    es = es1;
    doMecl("MECL",bgList);
    mc = mc1;

    pr1 = es1 = mc1 = 0;
    //now setup the isomorphic
    for(int i = 0; i < howMany; i++)
      bgList[i].determineIsomorphic();

    doPrecisionRecall("PRNOI",bgList);
    doEdgeSim("ESNOI",bgList);
    doMecl("MECLNOI",bgList);

    total = 0;
    for(int i = 0; i < howMany; i++)
      for(int j = i+1; j < howMany; j++)
        total++;


    System.out.println();
    int numNodes = bgList[0].getNodes().size();

    System.out.println("Node Count = " + numNodes);
    System.out.println("AVG(PR) = "+(pr / (double)total ));
    System.out.println("AVG(ES) = "+(es / (double)total ));
    System.out.println("AVG(MC) = "+(mc / (double)total ));
    System.out.println("AVG(PR_NOS) = "+(pr1 / (double)total ));
    System.out.println("AVG(ES_NOS) = "+(es1 / (double)total ));
    System.out.println("AVG(MC_NOS) = "+(mc1 / (double)total ));
  }

  public void doPrecisionRecall(String fn, BunchGraph [] bgList)
  {
    int sz = bgList.length;
    for(int i = 0; i < sz; i++)
      for(int j = i+1; j < sz; j++)
      {
        BunchGraph bg1 = bgList[i];
        BunchGraph bg2 = bgList[j];
        Hashtable ht1 = BunchGraphUtils.calcPR(bg1,bg2);
        Double prValue = (Double)ht1.get("AVERAGE");
        System.out.print(fn+"("+i+","+j+") = ");
        if(prValue != null)
        {
          double dTmp = prValue.doubleValue() * 100.0;
          pr1 += dTmp;
          System.out.println((int)dTmp);
        }
        else
          System.out.println("0");
      }
  }

  public void doEdgeSim(String fn, BunchGraph [] bgList)
  {
    int sz = bgList.length;
    for(int i = 0; i < sz; i++)
      for(int j = i+1; j < sz; j++)
      {
        BunchGraph bg1 = bgList[i];
        BunchGraph bg2 = bgList[j];

        Double esValue = new Double(BunchGraphUtils.calcEdgeSimiliarities(bg1,bg2));

        System.out.print(fn+"("+i+","+j+") = ");
        if(esValue != null)
        {
          double dTmp = esValue.doubleValue() * 100.0;
          es1 += dTmp;
          System.out.println((int)dTmp);
        }
        else
          System.out.println("0");
      }
  }

  public void doMecl(String fn, BunchGraph [] bgList)
  {
    int sz = bgList.length;
    for(int i = 0; i < sz; i++)
      for(int j = i+1; j < sz; j++)
      {
        BunchGraph bg1 = bgList[i];
        BunchGraph bg2 = bgList[j];

        Hashtable meClValue1 = BunchGraphUtils.getMeClMeasurement(bg1,bg2);
        Hashtable meClValue2 = BunchGraphUtils.getMeClMeasurement(bg2,bg1);

        //System.out.println("The distance is:  " + meClValue.get(BunchGraphUtils.MECL_VALUE) +
        //            "   quality = "+meClValue.get(BunchGraphUtils.MECL_QUALITY_METRIC));

        Double meclValue1 = (Double)meClValue1.get(BunchGraphUtils.MECL_QUALITY_METRIC);
        Double meclValue2 = (Double)meClValue2.get(BunchGraphUtils.MECL_QUALITY_METRIC);
        double d1 = meclValue1.doubleValue();
        double d2 = meclValue2.doubleValue();


        long simEdges = BunchGraphUtils.calcSimEdges(bg1,bg2);
        long totalEdges = bg1.getEdges().size();
        long diffEdges = totalEdges - simEdges;

        long mc1 = BunchGraphUtils.getMeClDistance(bg1,bg2); //(long)((1.0-d1)*(double)totalEdges);
        long mc2 = BunchGraphUtils.getMeClDistance(bg2,bg1);//(long)((1.0-d2)*(double)totalEdges);
        //long totalmc = (long)(d1+d2);

        if(diffEdges != (mc1+mc2))
          System.out.println("EDGESIM = "+diffEdges+"   MC="+(mc1+mc2));
        //double es = esValue.doubleValue();
        //if(es != ((d1+d2)/2.0))
        //  System.out.println("Values are not equal: " + es +" != "+((d1+d2)/2.0));
        //else
        //  System.out.println("Values are equal");

        double d = Math.max(d1,d2);

        Double  meclValue = new Double(d);

        System.out.print(fn+"("+i+","+j+") = ");
        if(meclValue != null)
        {
          double dTmp = meclValue.doubleValue() * 100.0;
          mc1 += dTmp;
          System.out.println((int)dTmp);
        }
        else
          System.out.println("0");
      }
  }

  public void setDB()
  {
      try
    {
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

      con = DriverManager.getConnection("jdbc:odbc:MyInventory");

      String stmt = "insert into ClustResults values ( ? , ? , ? , ?)";
      writeRecord = con.prepareStatement(stmt);

      String delStmt = "delete from ClustResults where TESTID = \'" + testID + "\'";
      Statement s = con.createStatement();
      s.executeUpdate(delStmt);

      //Connection con = DriverManager.getConnection("jdbc:odbc:MyInventory");
      //Statement s = con.createStatement();

      //ResultSet rs = s.executeQuery("select * from items");
      //while(rs.next())
      //  System.out.println("Title = " + rs.getString("Title")+","+rs.getString("Type")+","+rs.getString("DateObtained"));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void buildCmpDB(BunchGraph bg1, BunchGraph bg2)
  {
  }

  public void echoCmpGraph(int idx, BunchGraph bg)
  {
    ArrayList bnList = new ArrayList(bg.getNodes());
    int maxSize = bnList.size();

    for(int i = 0; i < maxSize; i++)
    {
      BunchNode n1 = (BunchNode)bnList.get(i);
      for(int j = i; j < maxSize; j++)
      {
        int result;

        BunchNode n2 = (BunchNode)bnList.get(j);
        if(n1.getCluster() == n2.getCluster())
          result = 1;
        else
          result = 0;

        try
        {
          writeRecord.setString(1,testID);
          writeRecord.setString(2,n1.getName());
          writeRecord.setString(3,n2.getName());
          writeRecord.setInt(4,result);

          writeRecord.executeUpdate();
        }catch(Exception e)
        { e.printStackTrace();  }

      }
    }
  }

  public static void main(String[] args) {
  System.out.println("HACK1");
    BunchAPISimAnalysis bunchAPISimAnalysis1 = new BunchAPISimAnalysis();
  }
}