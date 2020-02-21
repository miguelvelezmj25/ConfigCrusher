package edu.cmu.cs.mvelezce.e2e.execute.instrument.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.ProcessedPerfExecution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class E2EInstrumentExecutionParser extends BaseE2EExecutionParser {

  public E2EInstrumentExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  protected long getExecutionTime(File file) throws IOException {
    String time = "";

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      time = line;
    }

    reader.close();

    return Long.parseLong(time);
  }

  @Override
  protected ProcessedPerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<ProcessedPerfExecution>() {});
  }
}
