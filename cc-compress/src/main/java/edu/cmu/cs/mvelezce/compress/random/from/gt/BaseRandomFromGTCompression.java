package edu.cmu.cs.mvelezce.compress.random.from.gt;

import edu.cmu.cs.mvelezce.compress.random.from.other.BaseRandomFromOtherCompression;

import java.util.List;
import java.util.Set;

public abstract class BaseRandomFromGTCompression extends BaseRandomFromOtherCompression {

  public BaseRandomFromGTCompression(
      String programName, List<String> options, Set<Set<String>> gtConfigs) {
    super(programName, options, gtConfigs);
  }

  public static String getOutputDir() {
    return BaseRandomFromOtherCompression.getOutputDir() + "/gt";
  }
}
