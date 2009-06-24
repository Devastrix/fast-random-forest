/*
 * Test.java
 * (C) 2009 the class authors / UCD / ...
 */

/*
 * License comes here.
 */

package hr.irb.fastRandomForest;

import hr.irb.fastRandomForest.data.TestDatasets;
import org.junit.Assert;
import org.junit.Test;
import weka.core.Instances;

/**
 * Some tests.
 *
 * @author Santi Villalba
 * @version $Id$
 */
public class TestFRF{

  private static double[] importances(FastRandomForest frf, Instances data) throws Exception{
    frf.buildClassifier(data);
    return frf.getFeatureImportances();
  }

  private static FastRandomForest frf(){
    FastRandomForest rf = new FastRandomForest();
    rf.setNumTrees(100);
    return rf;
  }

  private static void checkMostImportantIsMostImportant(double[] importances, int mostImportant){
    double mostImportantImportance = importances[mostImportant];
    for(double importance : importances)
      Assert.assertTrue(mostImportantImportance >= importance);
  }


  @Test
  public void testVariableImportancesSameAsBreiman() throws Exception{

    //Breiman's paper, vote
    checkMostImportantIsMostImportant(importances(frf(), TestDatasets.vote()), 3);

    //Breiman's paper, diabetes
    checkMostImportantIsMostImportant(importances(frf(), TestDatasets.diabetes()), 1);
  }
}