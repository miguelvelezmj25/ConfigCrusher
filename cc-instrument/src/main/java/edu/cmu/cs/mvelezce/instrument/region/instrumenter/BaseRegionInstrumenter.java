package edu.cmu.cs.mvelezce.instrument.region.instrumenter;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrumenter.instrument.BaseInstrumenter;

import java.util.Map;
import java.util.Set;

public abstract class BaseRegionInstrumenter<T> extends BaseInstrumenter {

  private final Set<String> options;
  private final String mainClass;
  private final Map<JavaRegion, T> regionsToData;

  public BaseRegionInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Set<String> options,
      Map<JavaRegion, T> regionsToData) {
    super(programName, srcDir, classDir);

    System.err.println(
        "Why is that there was a method transformer to explicitly add a variable before a return?");

    this.options = options;
    this.mainClass = mainClass;
    this.regionsToData = regionsToData;
  }

  protected Set<String> getOptions() {
    return options;
  }

  protected String getMainClass() {
    return mainClass;
  }

  protected Map<JavaRegion, T> getRegionsToData() {
    return regionsToData;
  }
}
