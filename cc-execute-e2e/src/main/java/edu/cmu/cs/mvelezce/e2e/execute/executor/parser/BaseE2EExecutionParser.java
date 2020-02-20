package edu.cmu.cs.mvelezce.e2e.execute.executor.parser;

import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

public abstract class BaseE2EExecutionParser
    extends BaseRawExecutionParser<ProcessedPerfExecution> {

  public BaseE2EExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }
}
