package edu.cmu.cs.mvelezce.e2e.execute.time.parser;

import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;
import edu.cmu.cs.mvelezce.e2e.execute.time.parser.results.E2ETimePerfExecution;
import edu.cmu.cs.mvelezce.java.execute.BaseExecutor;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.region.manager.RegionsManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class E2ETimeExecutionParser extends BaseE2EExecutionParser {

  public E2ETimeExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  protected PerfExecution getPerfExecution(Set<String> configuration, File file)
      throws IOException {
    List<String> results = this.deserialize(file);
    List<String> entries = this.getEntries(results);

    long realTime = this.getTime(entries, BaseExecutor.REAL);
    Map<String, Long> regionsToTimes = new HashMap<>();
    regionsToTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), realTime);

    long userTime = this.getTime(entries, BaseExecutor.USER);
    Map<String, Long> regionsToUserTimes = new HashMap<>();
    regionsToUserTimes.put(RegionsManager.PROGRAM_REGION_ID.toString(), userTime);

    return new E2ETimePerfExecution(configuration, regionsToTimes, regionsToUserTimes);
  }

  private long getTime(List<String> entries, String entryToFind) {
    long time = -1;

    for (int i = 0; i < entries.size(); i++) {
      if (!entries.get(i).equals(entryToFind)) {
        continue;
      }

      time = (long) (Double.parseDouble(entries.get(i + 1)) * 1E9);
      break;
    }

    if (time < 0) {
      throw new RuntimeException("Did not find " + entryToFind + " time");
    }

    return time;
  }

  private List<String> deserialize(File file) throws IOException {
    List<String> results = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      results.add(line.trim().toLowerCase());
    }

    reader.close();

    if (results.size() != 3) {
      throw new RuntimeException("The resulting files does not have 3 lines");
    }

    return results;
  }

  private List<String> getEntries(List<String> lines) {
    List<String> entries = new ArrayList<>();

    for (String line : lines) {
      line = line.trim();

      for (String entry : line.split(" ")) {
        if (entry.isEmpty()) {
          continue;
        }

        entries.add(entry);
      }
    }

    if (entries.size() != 6) {
      throw new RuntimeException("There are not 6 entries in the resulting file");
    }

    return entries;
  }
}
