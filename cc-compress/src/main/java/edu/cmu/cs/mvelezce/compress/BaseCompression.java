package edu.cmu.cs.mvelezce.compress;

import edu.cmu.cs.mvelezce.analysis.BaseAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public abstract class BaseCompression extends BaseAnalysis<Set<Set<String>>> {

  public BaseCompression(String programName) {
    super(programName);
  }

  @Override
  public void writeToFile(Set<Set<String>> results) throws IOException {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public Set<Set<String>> readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
  }
}
