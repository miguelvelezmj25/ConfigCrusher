package edu.cmu.cs.mvelezce.compress;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;
import edu.cmu.cs.mvelezce.utils.config.Options;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class BaseCompression extends BaseAnalysis<Set<Set<String>>> {

  private final List<String> options;

  public BaseCompression(String programName, List<String> options) {
    super(programName);

    this.options = options;
  }

  @Override
  public void writeToFile(Set<Set<String>> results) throws IOException {
    String outputFile =
        this.outputDir()
            + "/"
            + this.getProgramName()
            + "/"
            + this.getProgramName()
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, results);
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
  }

  protected List<String> getOptions() {
    return options;
  }
}
