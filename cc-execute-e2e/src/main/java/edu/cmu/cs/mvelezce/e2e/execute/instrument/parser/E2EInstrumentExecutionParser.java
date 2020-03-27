package edu.cmu.cs.mvelezce.e2e.execute.instrument.parser;

import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.region.manager.RegionsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class E2EInstrumentExecutionParser extends BaseE2EExecutionParser {

  public E2EInstrumentExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  protected PerfExecution getPerfExecution(Set<String> configuration, File file)
      throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String timeAsString = "";
    String line;

    while ((line = reader.readLine()) != null) {
      timeAsString = line;
    }

    reader.close();

    long time = Long.parseLong(timeAsString);
    Map<String, Long> regionsToTimes = new HashMap<>();
    regionsToTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), time);

    return new PerfExecution(configuration, regionsToTimes);
  }
}
