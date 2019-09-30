package edu.cmu.cs.mvelezce.tool.compression;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.tool.Options;
import edu.cmu.cs.mvelezce.tool.analysis.taint.java.dynamic.phosphor.ConfigConstraint;
import java.io.File;
import java.io.IOException;
import java.util.Set;

// TODO make a base class for both static and dynamic results
public abstract class BaseDynamicCompression<T> extends AbstractCompression<T> {

  private final Set<String> options;
  private final Set<ConfigConstraint> constraints;

  protected BaseDynamicCompression(String programName, Set<String> options,
      Set<ConfigConstraint> constraints) {
    super(programName);
    this.options = options;
    this.constraints = constraints;
  }

  @Override
  public void writeToFile(T compressedConfigurations) throws IOException {
    String outputFile = this.getOutputDir() + "/" + this.getProgramName() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, compressedConfigurations);
  }

  @Override
  public String getOutputDir() {
    return Options.DIRECTORY + "/compression/dynamic/java/programs/" + this.getProgramName();
  }

  public Set<ConfigConstraint> getConstraints() {
    return constraints;
  }

  public Set<String> getOptions() {
    return options;
  }
}
