package edu.cmu.cs.mvelezce.compress.random.from.idta;

import edu.cmu.cs.mvelezce.compress.random.from.other.BaseRandomFromOtherCompression;

import java.util.List;
import java.util.Set;

public abstract class BaseRandomFromIDTACompression extends BaseRandomFromOtherCompression {

  public BaseRandomFromIDTACompression(
      String programName, List<String> options, Set<Set<String>> idtaConfigs) {
    super(programName, options, idtaConfigs);
  }

  public static String getOutputDir() {
    return BaseRandomFromOtherCompression.getOutputDir() + "/idta";
  }
}
