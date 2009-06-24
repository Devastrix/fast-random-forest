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
 * Some tests.
 *
 * @author Santi Villalba
 * @version $Id$
 */
public class Test{

  private static void checkMostImportantIsMostImportant(double[] importances, int mostImportante, Instances data){
    double mostImportantImportance = importances[mostImportante];
    for(int i = 0; i < importances.length; i++)
      if(mostImportantImportance < importances[i])
        System.err.println("This is wrong, " +data.attribute(i).name() + ", " + importances[i]);
  }

  private static double[] importances(FastRandomForest frf, Instances data) throws Exception{
    frf.buildClassifier(data);
    return frf.getFeatureImportances();
  }

  public static void main(String... args) throws Exception{

    //Our forest
    FastRandomForest rf = new FastRandomForest();
    rf.setNumTrees(100);

    //Breiman's paper, vote
    Instances data = TestDatasets.vote();
    checkMostImportantIsMostImportant(importances(rf, data), 4, data);

    //Breiman's paper, diabetes
    data = TestDatasets.diabetes();
    checkMostImportantIsMostImportant(importances(rf, data), 1, data);
  }
}