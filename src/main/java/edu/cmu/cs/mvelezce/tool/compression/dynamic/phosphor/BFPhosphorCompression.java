package edu.cmu.cs.mvelezce.tool.compression.dynamic.phosphor;

import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.SinkData;
import java.util.Collection;
import java.util.Set;

public class BFPhosphorCompression extends PhosphorCompression {

  public BFPhosphorCompression(String programName) {
    super(programName);
  }

  public BFPhosphorCompression(String programName, Set<String> options,
      Collection<SinkData> constraints) {
    super(programName, options, constraints);
  }

  @Override
  public String getOutputDir() {
    return Options.DIRECTORY + "/compression/phosphor/bf/java/programs/" + this.getProgramName();
  }
}
