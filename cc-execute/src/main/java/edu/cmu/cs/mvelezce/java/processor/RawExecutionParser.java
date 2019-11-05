package edu.cmu.cs.mvelezce.java.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.java.results.raw.RawPerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public final class RawExecutionParser {

  private final String programName;
  private final String outputDir;

  public RawExecutionParser(String programName, String outputDir) {
    this.programName = programName;
    this.outputDir = outputDir;
  }

  public void logExecution(Set<String> configuration, int iter) throws IOException {
    List<String> trace = this.parseTrace();
    RawPerfExecution rawPerfExecution = new RawPerfExecution(configuration, trace);

    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, rawPerfExecution);
  }

  public String getRawOutputDir(int iter) {
    return this.outputDir + "/" + this.programName + "/execution/raw/" + iter;
  }

  private List<String> parseTrace() throws IOException {
    File dirFile = new File(".");
    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"}, false);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
    }

    return this.deserialize(serializedFiles.iterator().next());
  }

  private List<String> deserialize(File file) throws IOException {
    List<String> trace = new ArrayList<>();

    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line;

    while ((line = reader.readLine()) != null) {
      trace.add(line);
    }

    reader.close();

    return trace;
  }
}
