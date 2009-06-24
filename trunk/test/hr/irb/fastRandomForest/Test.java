/*
 * Test.java
 * (C) 2009 the class authors / UCD / ...
 */

/*
 * License comes here.
 */

package hr.irb.fastRandomForest;

import hr.irb.fastRandomForest.data.TestDatasets;
import weka.core.Instances;

/**
 * @author Santi Villalba
 * @version $Id$
 */
public class Test{

  public static void main(String ... args) throws Exception{

    Instances data = TestDatasets.vote();
    FastRandomForest rf = new FastRandomForest();
    rf.setNumTrees(100);
    double tic = System.nanoTime();
    rf.buildClassifier(data);
    System.err.println("Nanos: " +(System.nanoTime() - tic));
    double[] featureImportances = rf.getFeatureImportances();
    for(int i = 0; i < featureImportances.length; i++)
      System.err.println(data.attribute(i).name() +", " +featureImportances[i]);
  }
}