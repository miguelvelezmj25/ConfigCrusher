package edu.cmu.cs.mvelezce.instrument.region.utils.propagation.intra;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionAnalyzer.BlockRegionAnalyzer;
import edu.cmu.cs.mvelezce.instrument.region.utils.blockRegionMatcher.BlockRegionMatcher;

import java.util.Map;
import java.util.Set;

public abstract class BaseExpander<T> extends BlockRegionAnalyzer<T> {

  public BaseExpander(
      String programName,
      String debugDir,
      Set<String> options,
      BlockRegionMatcher blockRegionMatcher,
      Map<JavaRegion, T> regionsToData) {
    super(programName, debugDir, options, blockRegionMatcher, regionsToData);
  }

  @Override
  protected String debugFileName(String methodName) {
    return "expandData/" + methodName;
  }
}
