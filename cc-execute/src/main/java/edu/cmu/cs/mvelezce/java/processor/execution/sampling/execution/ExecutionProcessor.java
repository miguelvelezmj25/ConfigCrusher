package edu.cmu.cs.mvelezce.java.processor.execution.sampling.execution;

import edu.cmu.cs.mvelezce.analysis.Analysis;

import java.io.File;
import java.io.IOException;

public abstract class ExecutionProcessor implements Analysis<Object> {

  private final String outputDir;

  public ExecutionProcessor(String programName) {
    this.outputDir = this.outputDir() + "/" + programName + "/execution/processed";
  }

  @Override
  public Object analyze() throws IOException, InterruptedException {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public Object analyze(String[] args) throws IOException, InterruptedException {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public void writeToFile(Object results) throws IOException {
    throw new UnsupportedOperationException("implement");
  }

  @Override
  public Object readFromFile(File file) throws IOException {
    throw new UnsupportedOperationException("implement");
  }
}
