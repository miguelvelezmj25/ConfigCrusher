package edu.cmu.cs.mvelezce.eval.java.blackbox.execute.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cmu.cs.mvelezce.eval.java.blackbox.results.BlackBoxResult;
import edu.cmu.cs.mvelezce.java.execute.parser.BaseRawExecutionParser;
import edu.cmu.cs.mvelezce.utils.config.Options;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class BlackBoxExecutionParser extends BaseRawExecutionParser<BlackBoxResult> {

  public BlackBoxExecutionParser(String programName, String outputDir) {
    super(programName, outputDir);
  }

  @Override
  public void logExecution(Set<String> configuration, int iter) throws IOException {
    long time = this.getExecutionTime();
    BlackBoxResult blackBoxResult = new BlackBoxResult(configuration, time);

    String outputFile =
        this.getOutputDir()
            + "/"
            + this.getProgramName()
            + "/execution/"
            + iter
            + "/"
            + UUID.randomUUID()
            + Options.DOT_JSON;
    File file = new File(outputFile);
    file.getParentFile().mkdirs();

    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(file, blackBoxResult);
  }

  private long getExecutionTime() throws IOException {
    File dirFile = new File(".");
    Collection<File> serializedFiles = FileUtils.listFiles(dirFile, new String[] {"ser"}, false);

    if (serializedFiles.size() != 1) {
      throw new RuntimeException("The directory " + dirFile + " must have 1 file.");
    }

    return this.deserialize(serializedFiles.iterator().next());
  }

  private long deserialize(File file) throws IOException {
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
  protected BlackBoxResult readFromFile(File perfFile) throws IOException {
    throw new UnsupportedOperationException("implement");
  }
}
