package edu.cmu.cs.mvelezce.e2e.execute.time.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.results.E2ETimePerfExecution;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;

import java.io.File;
import java.io.IOException;

public class E2EUserTimeExecutionParser extends E2ETimeExecutionParser {

  public E2EUserTimeExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  protected PerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    E2ETimePerfExecution e2eTimePerfExecution =
        mapper.readValue(file, new TypeReference<E2ETimePerfExecution>() {});

    return new PerfExecution(
        e2eTimePerfExecution.getConfiguration(), e2eTimePerfExecution.getRegionsToUserPerf());
  }
}
