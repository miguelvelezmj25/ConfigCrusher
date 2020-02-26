package edu.cmu.cs.mvelezce.e2e.execute.executor.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.java.results.processed.PerfExecution;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public abstract class BaseE2EExecutionParser extends BaseRawExecutionParser<PerfExecution> {

  public BaseE2EExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  public void logExecution(Set<String> configuration, int iter) throws IOException {
    File dirFile = new File(".");
    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"}, false);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
    }

    PerfExecution perfExecution =
        this.getPerfExecution(configuration, serializedFiles.iterator().next());
    this.logExecution(iter, perfExecution);
  }

  protected abstract PerfExecution getPerfExecution(Set<String> configuration, File file)
      throws IOException;

  private void logExecution(int iter, PerfExecution perfExecution) throws IOException {
    String outputFile = this.getRawOutputDir(iter) + "/" + UUID.randomUUID() + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, perfExecution);
  }

  @Override
  protected PerfExecution readFromFile(File file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(file, new TypeReference<PerfExecution>() {});
  }
}
