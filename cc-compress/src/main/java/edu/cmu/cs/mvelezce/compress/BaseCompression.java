package edu.cmu.cs.mvelezce.compress;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;

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
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
  }

  public List<String> getOptions() {
    return options;
  }
}
