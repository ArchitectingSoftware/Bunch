package bunch.util;

/**
 * Title:        Bunch Clustering Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Drexel University
 * @author Brian Mitchell
 * @version 1.0
 */

import java.io.*;
import bunch.*;

public class MQCalculator {

  public MQCalculator() {
  }

  public static double CalcMQ(String mdgFileName, String silFileName, String calculatorName)
  {
    try
    {
      String mdg = mdgFileName;
      String sil = silFileName;

      Parser p = new DependencyFileParser();
      p.setInput(mdg);
      p.setDelims(" \t");

      Graph g = (Graph)p.parse();
      ObjectiveFunctionCalculatorFactory ofc = new ObjectiveFunctionCalculatorFactory();
      ofc.setCurrentCalculator((String)calculatorName);
      g.setObjectiveFunctionCalculatorFactory(ofc);

      g.setObjectiveFunctionCalculator((String)calculatorName);

      ClusterFileParser cfp = new ClusterFileParser();
      cfp.setInput(sil);
      cfp.setObject(g);
      cfp.parse();
      g.calculateObjectiveFunctionValue();

      //figure out the total number of edges
      long edgeCnt = 0;
      Node[] n = g.getNodes();
      for(int i = 0; i < n.length; i++)
      {
        if (n[i].dependencies != null)
          edgeCnt += n[i].dependencies.length;
      }

      //set output values
      return g.getObjectiveFunctionValue();
      //System.out.println("Objective function value = " + g.getObjectiveFunctionValue());

    }
    catch(Exception calcExcept)
    {
      calcExcept.printStackTrace();
      return -1.0;
    }
  }
}