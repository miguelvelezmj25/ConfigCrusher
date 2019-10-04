package edu.cmu.cs.mvelezce.instrument.region;

import edu.cmu.cs.mvelezce.analysis.region.java.JavaRegion;
import edu.cmu.cs.mvelezce.instrumenter.instrument.BaseInstrumenter;

import java.util.Map;

public abstract class BaseRegionInstrumenter<T> extends BaseInstrumenter {

  private final String mainClass;
  private final Map<JavaRegion, T> regionsToData;

  public BaseRegionInstrumenter(
      String programName,
      String mainClass,
      String srcDir,
      String classDir,
      Map<JavaRegion, T> regionsToData) {
    super(programName, srcDir, classDir);

    System.err.println(
        "Why is that there was a method transformer to explicitly add a variable before a return?");

    this.mainClass = mainClass;
    this.regionsToData = regionsToData;
  }

  protected String getMainClass() {
    return mainClass;
  }

  protected Map<JavaRegion, T> getRegionsToData() {
    return regionsToData;
  }
}
