package edu.cmu.cs.mvelezce.tool.instrumentation.java.region;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.region.JavaRegion;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.taint.InfluencingTaints;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class DynamicBaseRegionInstrumenter extends
    BaseRegionInstrumenter<InfluencingTaints> {

  public static final String DIRECTORY =
      Options.DIRECTORY + "/instrumentation/dynamic/java/programs";

  public DynamicBaseRegionInstrumenter(String programName, String classDir,
      Map<JavaRegion, InfluencingTaints> regionsToInfluencingTaints) {
    super(programName, classDir, regionsToInfluencingTaints);
  }

//  public DynamicBaseRegionInstrumenter(String programName) {
//    this(programName, null, new HashMap<>());
//  }

  public void writeToFile(Map<JavaRegion, InfluencingTaints> relevantRegionsToOptions)
      throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

  public Map<JavaRegion, InfluencingTaints> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("Implement");
  }

}
