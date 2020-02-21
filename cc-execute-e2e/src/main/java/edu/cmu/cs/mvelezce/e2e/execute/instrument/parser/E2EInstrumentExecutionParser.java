package edu.cmu.cs.mvelezce.e2e.execute.instrument.parser;

import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;

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
}
