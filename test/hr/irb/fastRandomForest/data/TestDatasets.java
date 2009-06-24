/*
 * TestDatasets.java
 * (C) 2009 the class authors / UCD / ...
 */

/*
 * License comes here.
 */

package hr.irb.fastRandomForest.data;

import weka.core.Instances;

import java.io.InputStream;

/**
 * Some utility methods for loading the testing datasets.
 *
 * @author Santi Villalba
 * @version $Id$
 */
public class TestDatasets{

  private final static String DIABETES = "hr/irb/fastRandomForest/data/diabetes.arff";
  private final static String VOTE = "hr/irb/fastRandomForest/data/vote.arff";

  private static Instances _diabetes;
  private static Instances _vote;

  /** Avoid instantiation */
  private TestDatasets(){}

  public static Instances diabetes() throws Exception{
    if(null == _diabetes)
      _diabetes = loadData(DIABETES);
    return _diabetes;
  }

  public static Instances vote() throws Exception{
    if(null == _vote)
      _vote = loadData(VOTE);
    return _vote;
  }

  private static Instances loadData(String location) throws Exception{

    InputStream is = ClassLoader.getSystemResourceAsStream(location);
    Instances data = new weka.core.converters.ConverterUtils.DataSource(is).getDataSet();

    if(-1 == data.classIndex())
      data.setClassIndex(data.numAttributes() - 1);

    return data;
  }
}