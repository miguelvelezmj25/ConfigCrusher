package edu.cmu.cs.mvelezce.e2e.execute.time.parser;

import edu.cmu.cs.mvelezce.e2e.execute.executor.parser.BaseE2EExecutionParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class E2ETimeExecutionParser extends BaseE2EExecutionParser {

  public static final String USER = "user";
  public static final String REAL = "real";

  private final String inputToParse;

  public E2ETimeExecutionParser(String programName, String outputDir) {
    this(programName, outputDir, USER);
  }

  public E2ETimeExecutionParser(String programName, String outputDir, String inputToParse) {
    super(programName, outputDir);

    this.inputToParse = inputToParse;
  }

  @Override
  protected long getExecutionTime(File file) throws IOException {
    List<String> results = this.deserialize(file);
    List<String> entries = this.getEntries(results);

    long userTime = -1;

    for (int i = 0; i < entries.size(); i++) {
      if (!entries.get(i).equals(this.inputToParse)) {
        continue;
      }

      userTime = (long) (Double.parseDouble(entries.get(i + 1)) * 1E9);
      break;
    }

    if (userTime < 0) {
      throw new RuntimeException("Did not find user time");
    }

    return userTime;
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
